//
// Example audio backend for ALSA.
//
// Written by Stanislav Karchebny <berkus@skype.net>
// Copyright (c) 2007-2012 Skype Technologies SA.
//

#include <algorithm>
#include <alsa/asoundlib.h>
#include <alsa/control.h>
#include <alsa/version.h>
#include <limits.h>
#include <sys/time.h>
#include <math.h> //fabs()
#include "AlsaDeviceDetection.h"
#include "AlsaVolumeControl.h"
#include "AudioStream.h"
#include "MutexLock.h"
#include "AlsaParams.h"
#include "Log.h"

// TODO: consolidate all these wrappers
#ifdef _DEBUG
#define _TRACE_CALL(func) \
do { \
    int result = func; \
    Log("called "#func" result %s / %d", snd_strerror(result), result); \
} while(0)
#else
#define _TRACE_CALL(func) func
#endif

#define _EE_CALL(func, doClose, phandle, errmsg, ...) \
do { \
    int result = func; \
    Log("called "#func" result %s / %d", snd_strerror(result), result); \
    if (result < 0) { \
        if (doClose) snd_pcm_close(phandle); \
        delete audioStream; \
        Log(errmsg, __VA_ARGS__); \
        Log("%s",snd_strerror(result)); \
        Log("returning 0\n"); \
        return NULL; \
    } \
} while(0)

//======================================================================================================================
// AlsaHandle
//======================================================================================================================

/*!
 * A structure to hold various information related to the ALSA API implementation.
 * @internal
 */
class AlsaHandle
{
public:
    snd_pcm_t *handle;
    bool xrun;
	bool first;
    DeviceInfo::type_e type;

    AlsaHandle()
        : handle(0)
        , xrun(false)
		, first(true)
        , type(DeviceInfo::TYPE_UNKNOWN)
    {}
};

//======================================================================================================================
// AlsaThread (get data for input, output and notifications)
//======================================================================================================================

/*!
 * ALSA thread manages callback calls.
 * Using ALSA-initiated callback calls is less reliable and therefore not used.
 * @internal
 */
class AlsaThread 
{
    CallbackInfo* info;
    const char* thread_name;
    bool stopPending;
    bool started;
public:
    AlsaThread(CallbackInfo* i, const char* name) 
        : info(i)
        , thread_name(name)
    	, stopPending(false)
    	, started(false)
    {
    }
    virtual ~AlsaThread()
    {
        stopPending = true;
        while(started) {
	        pthread_yield();
        }
    }

    inline void stop() { stopPending = true; }

    static void* run(void* arg)
    {
        AlsaThread* pthis = static_cast<AlsaThread*> (arg);
        AlsaBackend* object = static_cast<AlsaBackend*>(pthis->info->object);
        AudioStream* stream = pthis->info->stream;
        AlsaHandle* apiInfo = static_cast<AlsaHandle*>(pthis->info->apiInfo);
        
        pthis->started = true;

        while (!pthis->stopPending)
        {
            if (!stream->running)
            {
                if (stream->callback.isRunning)
                    pthis->sleep(50);

                continue;
            }

            if (stream->dir == INPUT)
                object->CallbackInputEvent(stream);
            else if (stream->dir >= OUTPUT)
                object->CallbackOutputEvent(stream);
            else
                break;

            // Workaround for PCM null plugin hogging 100% CPU.
            if (apiInfo->type == DeviceInfo::TYPE_NULL)
                pthis->sleep(100);
            else
                pthis->sleep(10);
        }
        pthis->started = false;
        Log("AlsaThread exiting");
        return pthis;
    }

    void sleep(long ms) 
    {
        timespec ts;
    	ts.tv_sec =(time_t) (ms / 1000);
    	ts.tv_nsec=(time_t) ((ms % 1000) * 1000 * 1000);
    	nanosleep(&ts,NULL);
    }
};

//======================================================================================================================
// AlsaBackend
//======================================================================================================================

static void alsa_error_handler(const char *file, int line, const char *function, int err, const char *fmt, ...)
{
    Log("AlsaBackend: ALSA error in %s:%d (%s) - %d\n", file, line, function, err);
}

AlsaBackend::AlsaBackend()
{
    Log("%s\n",__PRETTY_FUNCTION__);
    snd_lib_error_set_handler(alsa_error_handler);
}

AlsaBackend::~AlsaBackend()
{
    Log("%s\n",__PRETTY_FUNCTION__);
    snd_lib_error_set_handler(0);
    snd_config_update_free_global();
    std::map<AudioStream*, AlsaThread*>::iterator iter;
    for (iter = p_alsathread.begin(); iter != p_alsathread.end(); iter++)
    {
        delete iter->second;
        delete iter->first;
    }
}

void AlsaBackend::InitDevicesList()
{
    devicesList.clear();
    devicesList.push_back(DeviceInfo::null);
}

bool AlsaBackend::IsGood()
{
    if (devicesList.size() == 0)
        UpdateDevicesList();
    return devicesList.size() > 0;
}

void AlsaBackend::GetDevices(AlsaBackend::DeviceInfoPtrs& devs, StreamDirection dir)
{
    Log("%s",__PRETTY_FUNCTION__);
    UpdateDevicesList();

    devs.clear();
    // i = 1 to skip DeviceInfo::null at the start of devicesList
    for (size_t i = 1; i < devicesList.size(); ++i)
        if ((devicesList[i].direction == 0) || (devicesList[i].direction & (int)dir))
            devs.push_back(&devicesList[i]);
}

bool AlsaBackend::ProbeDevice(DeviceInfo* device, StreamSpec& spec)
{
    char *name;

    if (devicesList.size() <= 1)
        UpdateDevicesList();

    size_t i = 1;
    // find device in device pool by given ID
    for (; i < devicesList.size(); ++i)
    {
        if ((device->guid[0] == 0 && device->id == devicesList.at(i).id) ||
           strncmp(devicesList.at(i).guid, device->guid, DEVICE_NAME_MAXLEN) == 0)
        {
            Log("found device");
            name = device->displayName;
            break;
        }
    }
    if (i >= devicesList.size())
    {
        Log("ProbeDevice failed!");
        return false;
    }

    // If device is BLUETOOTH type, don't waste time and start with 8KHz immediately. A2DP is not supported.
    if (device->type == DeviceInfo::TYPE_BLUETOOTH)
    {
        spec.rate = 8000;
        spec.channels = 1;
    }

    int result;
    char hwctl[8];
    snd_ctl_t *chandle = 0;
    int openMode = SND_PCM_ASYNC;
    snd_pcm_stream_t stream;
    snd_pcm_info_t *pcminfo;
    snd_pcm_info_alloca(&pcminfo);
    snd_pcm_t *phandle = 0;
    snd_pcm_hw_params_t *params;
    snd_pcm_hw_params_alloca(&params);

    // Probe mixer for device
    if (!device->mixer)
        device->mixer = new AlsaVolumeControl;
    static_cast<AlsaVolumeControl*>(device->mixer)->ProbeMixer(device);

    // First try for playback
    stream = SND_PCM_STREAM_PLAYBACK;

    bool skipPlayback = false;

    if (device->type == DeviceInfo::TYPE_HW)
    {
        snprintf(hwctl, 8, "hw:%d", device->id);
        Log("Trying to open ctl %s", hwctl);
        result = snd_ctl_open(&chandle, hwctl, SND_CTL_NONBLOCK);
        //@todo snd_ctl_card_info() call?
        if (result >= 0)
        {
            snd_pcm_info_set_device(pcminfo, device->id);
            snd_pcm_info_set_subdevice(pcminfo, 0);
            snd_pcm_info_set_stream(pcminfo, stream);

            result = snd_ctl_pcm_info(chandle, pcminfo);
            if (result < 0) 
            {
                Log("Device probably doesn't support playback (dev %d): %s", device->id, snd_strerror(result));
                skipPlayback = true;
            }
        }
        else
        {
            Log("Device probably doesn't have ctl interface.");
            chandle = 0;
        }
    }
    if (!skipPlayback)
    {
        result = snd_pcm_open(&phandle, device->guid, stream, openMode | SND_PCM_NONBLOCK);
        if (result >= 0)
        {
            // The device is open ... fill the parameter structure.
            result = snd_pcm_hw_params_any(phandle, params);
            if (result >= 0)
            {
                // Get output channel information.
                unsigned int value;
                result = snd_pcm_hw_params_get_channels_max(params, &value);
                if (result >= 0)
                {
                    device->outMaxChannels = value;
                    snd_pcm_close(phandle);
                }
                else
                {
                    snd_pcm_close(phandle); // @todo unify handle closing calls in case of errors
                    Log("error getting device %s output channels: %s", name, snd_strerror(result));
                }
            }
            else
            {
                snd_pcm_close(phandle); // @todo unify handle closing calls in case of errors (_EE_CALL)
                Log("snd_pcm_hw_params error for device %s: %s", name, snd_strerror(result));
            }
        }
        else
        {
            Log("snd_pcm_open error for playback on device %s: %s", name, snd_strerror(result));
        }
    }
    // Now try for capture
    stream = SND_PCM_STREAM_CAPTURE;
    snd_pcm_info_set_stream(pcminfo, stream);

    if (device->type == DeviceInfo::TYPE_HW && chandle)
    {
        result = snd_ctl_pcm_info(chandle, pcminfo);
        snd_ctl_close(chandle);
        if (result < 0) {
            Log("Device probably doesn't support capture: %s", snd_strerror(result));
            if (device->outMaxChannels == 0)  // cannot playback/capture at all
            {
                Log("ProbeDevice failed!");
                return false;
            }
            return ProbeParameters(device, spec, phandle, stream, pcminfo, name, params);
        }
    }

    result = snd_pcm_open(&phandle, device->guid, stream, openMode | SND_PCM_NONBLOCK);
    if (result < 0) {
        Log("snd_pcm_open error for capture on device %s: %s", name, snd_strerror(result));
        if (device->outMaxChannels == 0) 
        {
            Log("ProbeDevice failed!");
            return false;
        }
        return ProbeParameters(device, spec, phandle, stream, pcminfo, name, params);
    }

    // The device is open ... fill the parameter structure.
    result = snd_pcm_hw_params_any(phandle, params);
    if (result < 0) {
        snd_pcm_close(phandle);
        Log("snd_pcm_hw_params error for device %s: %s", name, snd_strerror(result));
        if (device->outMaxChannels == 0)
        {
            Log("ProbeDevice failed!");
            return false;
        }
        return ProbeParameters(device, spec, phandle, stream, pcminfo, name, params);
    }
    unsigned int value;
    result = snd_pcm_hw_params_get_channels_max(params, &value);
    if (result < 0) {
        snd_pcm_close(phandle);
        Log("error getting device %s input channels: %s", name, snd_strerror(result));
        if (device->outMaxChannels == 0)
        {
            Log("ProbeDevice failed!");
            return false;
        }
        return ProbeParameters(device, spec, phandle, stream, pcminfo, name, params);
    }
    device->inMaxChannels = value;
    snd_pcm_close(phandle);
    return ProbeParameters(device, spec, phandle, stream, pcminfo, name, params);
}

bool AlsaBackend::ProbeParameters(DeviceInfo* device, StreamSpec& spec, snd_pcm_t* phandle, snd_pcm_stream_t stream, snd_pcm_info_t *pcminfo, char *name, snd_pcm_hw_params_t *params)
{
    // At this point, we just need to figure out the supported data
    // formats and sample rates.  We'll proceed by opening the device in
    // the direction with the maximum number of channels, or playback if
    // they are equal.  This might limit our sample rate options, but so
    // be it.

    int result;
    
    // FIXME: specify the direction in options beforehand?
    if (device->outMaxChannels >= device->inMaxChannels)
        stream = SND_PCM_STREAM_PLAYBACK;
    else
        stream = SND_PCM_STREAM_CAPTURE;
    snd_pcm_info_set_stream(pcminfo, stream);

    result = snd_pcm_open(&phandle, device->guid, stream, SND_PCM_ASYNC | SND_PCM_NONBLOCK);
    if (result < 0) {
        Log("snd_pcm_open error for device %s: %s", name, snd_strerror(result));
        Log("ProbeParameters failed!");
        return false;
    }

    // The device is open ... fill the parameter structure.
    result = snd_pcm_hw_params_any(phandle, params);
    if (result < 0) {
        snd_pcm_close(phandle);
        Log("snd_pcm_hw_params error for device %s: %s", name, snd_strerror(result));
        Log("ProbeParameters failed!");
        return false;
    }

    // Test if exact required rate is supported.
    if (snd_pcm_hw_params_test_rate(phandle, params, spec.rate, 0) == 0)
    {
        Log("Matching rate %d found", spec.rate);
    }
    else
    {
        // try to set rate near the requested one
        //.....
        Log("Matching rate NOT found.");
        unsigned int exactRate = spec.rate;
        result = snd_pcm_hw_params_set_rate_near(phandle, params, &exactRate, 0);
        if (result < 0) {
            snd_pcm_close(phandle);
            Log("no supported sample rates found for device %s: %s", name, snd_strerror(result));
            Log("ProbeParameters failed!");
            return false;
        }

        if (spec.rate != exactRate) {
            Log("The rate %d Hz is not supported by your hardware. Using %d Hz instead.", spec.rate, exactRate);
            spec.rate = exactRate;
        }
    }

    // Probe the supported data formats ... we don't care about endian-ness just yet
    // Check the requested format first and be happy if it works.
    snd_pcm_format_t format;
    device->supportedFormats = 0;
    format = FormatToAlsa(spec.format);
    if (snd_pcm_hw_params_test_format(phandle, params, format) == 0)
    {
        device->supportedFormats |= spec.format;
        snd_pcm_close(phandle);
        device->probed = true;
        return true;
    }

    // Check that we have at least one supported format
    if (device->supportedFormats == 0) {
        Log("pcm device %s data format is not supported, cannot open", name);
        Log("ProbeParameters failed!");
        return false;
    }

    // That's all ... close the device and return
    snd_pcm_close(phandle);
    device->probed = true;
    return true;
}

DeviceInfo* AlsaBackend::GetDeviceInfo(size_t idx)
{
    if (devicesList.size() <= 1)
        UpdateDevicesList();

    if (idx >= 0 && idx < devicesList.size())
        return &devicesList[idx];

    return &devicesList[0];
}


DeviceInfo* AlsaBackend::GetDeviceInfo(std::string guid)
{
    UpdateDevicesList();
    int i = 0;
    for (i = 0; i < (int) devicesList.size(); i++)
    { 
         if (guid == devicesList.at(i).guid)
         {
             Log("device with corresponding guid found");
             return &devicesList[i];
         }
    }

    return &devicesList[0];
}

AudioStream* AlsaBackend::OpenStream(StreamDirection dir, DeviceInfo* device, StreamSpec& spec, SOUNDCARD_CALLBACK callback, void* userdata)
{
    if (!device->probed)
        if (!ProbeDevice(device, spec))
            return NULL;

    if (!device->mixer)
    {
        Log("Device was probed but no mixer set, trying to enable now.");
        device->mixer = new AlsaVolumeControl;
        static_cast<AlsaVolumeControl*>(device->mixer)->ProbeMixer(device);
    }

    snd_pcm_stream_t stream;
    if (dir == INPUT)
        stream = SND_PCM_STREAM_CAPTURE;
    else if (dir >= OUTPUT)
        stream = SND_PCM_STREAM_PLAYBACK;
    else
    {
        return NULL;
    }

    AudioStream *audioStream = new AudioStream;
    MutexLock lock(audioStream->mutex);
    Log("AlsaBackend.open_stream_lock");

    //int result;
    snd_pcm_t *phandle = 0;
    int openMode = SND_PCM_ASYNC;
    snd_output_t *out;

    _EE_CALL(snd_pcm_open(&phandle, device->guid, stream, openMode), false, phandle, "pcm device %s won't open for %s", device->displayName, dir==INPUT?"input":"output");

    // Fill the parameter structure.
    AlsaHardwareParams alsa_hw_params(phandle);

#define DUMP_PARAMS(out, title, func)                \
    if (!snd_output_buffer_open(&out))               \
    {                                                \
        char* str;                                   \
        Log(title);                                  \
        func;                                        \
        snd_output_putc(out, '\0');                  \
        if (snd_output_buffer_string(out, &str) > 0) \
            Log("%s",str);                           \
        snd_output_close(out);                       \
    }

    DUMP_PARAMS(out, "AlsaBackend: dump hardware params just after device open:", snd_pcm_hw_params_dump(alsa_hw_params, out));

    // Set up interleaving
    audioStream->userInterleaved = true;
    audioStream->deviceInterleaved = true;

    bool use_mmap = false;
    alsa_hw_params.set_params(&spec, &use_mmap);

    audioStream->deviceFormat = audioStream->userFormat = spec.format;
    audioStream->deviceChannels = audioStream->userChannels = spec.channels;

    DUMP_PARAMS(out, "AlsaBackend: dump hardware params after installation:", snd_pcm_hw_params_dump(alsa_hw_params, out));

    AlsaSoftwareParams sw_params(phandle);
    sw_params.set_params(spec.fragmentFrames, true);

    DUMP_PARAMS(out, "AlsaBackend: dump software params after installation:", snd_pcm_sw_params_dump(sw_params, out));

    // Allocate the ApiHandle if necessary and then save.
    AlsaHandle *apiInfo = 0;
    if (audioStream->apiHandle == 0) {
        apiInfo = new AlsaHandle;
        if (!apiInfo) 
        {
            Log("error allocating AlsaHandle memory");
            delete audioStream;
            return NULL;

        }
        audioStream->apiHandle = (void *) apiInfo;
        apiInfo->handle = 0;
    }
    else {
        apiInfo = (AlsaHandle *) audioStream->apiHandle;
    }
    apiInfo->handle = phandle;
    apiInfo->xrun = false;
    apiInfo->type = device->type;

    audioStream->spec = spec;
    audioStream->running = 0;
    audioStream->dir = dir;
    audioStream->mixer = device->mixer;

    // Setup callback thread.
    audioStream->callback.object = this;
    audioStream->callback.stream = audioStream;
    audioStream->callback.callback = callback;
    audioStream->callback.userdata = userdata;
    audioStream->callback.apiInfo = apiInfo;

    audioStream->callback.isRunning = 1;

    // Allocate necessary internal buffer.
    audioStream->buffer = new PhantomRingbuffer(spec.FrameBytes(), spec.bufferFrames, spec.fragmentFrames);
    if (audioStream->buffer == NULL)
    {
        Log("error allocating ringbuffer memory");
        if (apiInfo) 
        {
            if (apiInfo->handle)
                snd_pcm_close(apiInfo->handle);
            delete apiInfo;
            apiInfo = NULL;
            audioStream->apiHandle = 0;
        }

        delete audioStream;
        return NULL;
    }

    p_alsathread[audioStream] = new AlsaThread(&audioStream->callback, dir == INPUT ? "alsa_capture_thread" : "alsa_playback_thread");
    pthread_create(&thrd,NULL,&(AlsaThread::run),p_alsathread[audioStream]);
    pthread_detach(thrd);
    Log("Using %0.1f fragments of size %lu bytes (%0.2fms), buffer size is %lu bytes (%0.2fms)",
                (double) (spec.bufferFrames * spec.FrameBytes()) / (double) (spec.fragmentFrames * spec.FrameBytes()),
                (long unsigned) spec.fragmentFrames * spec.FrameBytes(),
                spec.FragmentMs(),
                (long unsigned) spec.bufferFrames * spec.FrameBytes(),
                spec.BufferMs());

    return audioStream;
}

int AlsaBackend::GetDefaultDevice(StreamDirection /*dir*/)
{
    return 1; // 0 is the null DeviceInfo
}

bool AlsaBackend::StartStream(AudioStream* stream)
{
    Log("%s",__PRETTY_FUNCTION__);

    if (stream->running) {
        Log("startStream(): the stream is already running!");
        return false;
    }

    AlsaHandle *apiInfo = static_cast<AlsaHandle *>(stream->apiHandle);
    apiInfo->first = true;

    // Atomically kick-off stream callback processing.
    stream->running = 1;
    return true;
}

void AlsaBackend::StopStream(AudioStream* stream)
{
    Log("%s",__PRETTY_FUNCTION__);

    if (!stream->running) {
        Log("StopStream(): the stream is already stopped!");
        return;
    }

    // Atomically mark callback thread as stopped.
    stream->running = 0;
    stream->callback.isRunning = 0;

    int result = 0;
    AlsaHandle* apiInfo = (AlsaHandle *)stream->apiHandle;
    if (apiInfo)
    {
        snd_pcm_t* handle = apiInfo->handle;
        result = snd_pcm_drop(handle);
    }

    if (result < 0) {
        Log("StopStream: error stopping input pcm device, %s", snd_strerror(result));
    }

    if (stream->inCallback)
    {
        int tries = 0;
        while (stream->inCallback && tries++ < 20)
            sleep(10);
    }

    stream->mixer = 0;
    if (p_alsathread.count(stream)) {
        delete p_alsathread[stream];
        p_alsathread.erase(stream);
    }
}

void AlsaBackend::CloseStream(AudioStream* stream)
{
    if (p_alsathread.count(stream)) {
        delete p_alsathread[stream];
        p_alsathread.erase(stream);
    }
    AlsaHandle *apiInfo = static_cast<AlsaHandle*>(stream->apiHandle);
    if (apiInfo) {
        if (apiInfo->handle)
            snd_pcm_close(apiInfo->handle);
        delete apiInfo;
    }
    stream->apiHandle = NULL;
}

double AlsaBackend::GetStreamVolume(AudioStream* stream)
{
    Log("GetStreamVolume: mixer %p", stream->mixer);
    double actualVolume = stream->mixer ? stream->mixer->GetVolume(stream) : stream->desiredVolume;

    if (fabs(actualVolume - stream->desiredVolume) < 0.1)
        return stream->desiredVolume;
    else
        return actualVolume;
}

void AlsaBackend::SetStreamVolume(AudioStream* stream, double volume)
{
    Log("SetStreamVolume: mixer %p", stream->mixer);
    if (stream->mixer)
        stream->mixer->SetVolume(stream, volume);
    stream->desiredVolume = volume;
}

/*!====================================================================================================================
//
// List of possible return values for snd_pcm_{write,read}{i,n} functions:
//
// -EPIPE
// This error means xrun (underrun for playback or overrun for capture). The underrun can happen when an application
// does not feed new samples in time to alsa-lib (due to CPU usage). The overrun can happen when an application does not
// take new captured samples in time from alsa-lib.
//
// -ESTRPIPE
// This error means that system has suspended drivers. The application should wait in loop when snd_pcm_resume() !=
// -EAGAIN and then call snd_pcm_prepare() when snd_pcm_resume() return an error code. If snd_pcm_resume() does not
// fail (a zero value is returned), driver supports resume and the snd_pcm_prepare() call can be ommited.
//
// -EBADFD
// This error means that the device is in a bad state. It means that the handshake between application and alsa-lib is
// corrupted.
//
// -ENOTTY, -ENODEV
// This error can happen when device is physically removed (for example some hotplug devices like USB or PCMCIA,
// CardBus or ExpressCard can be removed on the fly).
*/
static int HandleXrun(int result, int expected, AudioStream* stream, const char* call_name)
{
    AlsaHandle *apiInfo = static_cast<AlsaHandle*>(stream->apiHandle);
    if (!apiInfo) { // This shouldn't happen but it does (due to while loop in callback_output_event).
        Log("AlsaBackend: HandleXrun called when ALSA stream is not live!");
        return 0;
    }
    snd_pcm_t *pcm = static_cast<snd_pcm_t*>(apiInfo->handle);

    if (result < expected)
    {
        // SPAMMING! Use for testing only.
        Log("%s: %d<%d, %s\n", call_name, result, expected, snd_strerror(result));
    }

    // snd_pcm_recover can handle most of the issues here, but it may also return error if it couldn't handle
    // something, so we repeatedly update result with return value of snd_pcm_recover() and fall-through to handle
    // more error returns if possible, but we do not strive to handle them all the way - if result is still negative
    // after processing we will just stop the device and make it return error.

    // Overrun/underrun.
    // Log, set flag, call snd_pcm_recover() or snd_pcm_prepare().
    if (result == -EPIPE)
    {
        Log("%s: Underrun!", call_name);
        apiInfo->xrun = true;
        result = snd_pcm_recover(pcm, result, /*silent:*/1);
        if (result == 0)
            apiInfo->xrun = false;
    }
    // Drivers are in suspend, according to alsa docs need to snd_pcm_resume() and maybe snd_pcm_prepare():
    if (result == -ESTRPIPE)
    {
        Log("%s: System suspended, trying to resume.", call_name);
        result = snd_pcm_recover(pcm, result, /*silent:*/1);
    }

    if (result == -EBADFD)
    {
        /* Bad stuff happened, stop the stream. */
        Log("Bad stuff happened, EBADFD from ALSA. Aborting stream.");
        stream->running = false;
    }

    if (result == -ENOTTY || result == -ENODEV)
    {
        /* Device gone: either switch back to default or stop stream? */
        Log("Running device has been unplugged, stopping stream.");
        stream->running = false;
    }

    // If ALSA wants us to try again, well.. try again.
    if (result == -EAGAIN)
        return result;

    // Extra log line for actually short writes or reads - should be rare and may indicate ALSA driver 
    // problems. So log it.
    if ((expected > 0) && (result > 0) && (result < expected))
    {
        Log("HandleXrun: short %s %d<%d\n", call_name, result, expected);
    }
    
    if (result < 0) {
        Log("HandleXrun %s recovery failed: %d<0, %s", call_name, result, snd_strerror(result));
    }

    return result;
}

/* from PA */
static snd_pcm_sframes_t alsa_safe_avail(snd_pcm_t *pcm, size_t hwbuf_size, const StreamSpec& ss)
{
    snd_pcm_sframes_t n;
    size_t k;

    /* Some ALSA drivers expose weird bugs, let's inform the user about what is going on */

    n = snd_pcm_avail_update(pcm);

    if (n <= 0)
        return n;

    k = (size_t) n * ss.FrameBytes();

    if (k >= hwbuf_size * 5 ||
        k >= ss.BytesPerSecond() * 10)
    {
        // Print this on console to allow users to report their driver.
        printf("snd_pcm_avail_update() returned a value that is exceptionally large: %lu bytes (%lu ms).\n"
               "Most likely this is a bug in the ALSA driver. Please report this issue to the ALSA developers.\n",
               (unsigned long) k,
               (unsigned long) ss.FramesToMs(k / ss.FrameBytes()));

        /* Mhmm, let's try not to fail completely */
        n = (snd_pcm_sframes_t) (hwbuf_size / ss.FrameBytes());
    }

    return n;
}

static int AlsaPrepareStart(snd_pcm_t* handle)
{
    Log("Starting pcm handle.");
    int state = snd_pcm_state(handle);
    if ((state != SND_PCM_STATE_PREPARED) && (state != SND_PCM_STATE_RUNNING))
    {
        Log("Wrong handle state: %d", snd_pcm_state(handle));
        int result = snd_pcm_prepare(handle);
        if (result < 0)
        {
            Log("snd_pcm_prepare failed: %s", snd_strerror(result));
        }
        state = snd_pcm_state(handle);
    }

    if (state == SND_PCM_STATE_PREPARED)
    {
        snd_pcm_start(handle);
    }
    else
    {
        if (state != SND_PCM_STATE_RUNNING)
        {
            Log("Could not start playback! Handle state %d", state);
        }
    }
    return state;
}

/*!
 * Process recording buffer.
 *
 * This handler is called from a callback thread. Can access only some of the stream variables and
 * cannot modify stream internal state (except input buffers).
 */
void AlsaBackend::CallbackInputEvent(AudioStream* inputStream)
{
    MutexLock lock(inputStream->mutex);

    AlsaHandle *apiInfo = (AlsaHandle *) inputStream->apiHandle;
    snd_pcm_t *handle = (snd_pcm_t *)apiInfo->handle;
    snd_pcm_sframes_t avail;
    int result;

    if (apiInfo->first)
    {
        AlsaPrepareStart(handle);
        apiInfo->first = false;
    }

    snd_pcm_sframes_t delay;
    result = snd_pcm_delay(handle, &delay);
    if ((result == 0) && (delay > 0))
    {
        inputStream->latency = delay;
        Log("Update inputStream latency: %d", (int)delay);
    }

    avail = alsa_safe_avail(handle, inputStream->spec.bufferFrames * inputStream->spec.FrameBytes(), inputStream->spec);
    Log("inputStream avail: %d", (int) avail);
    if (avail < 0)
    {
        result = HandleXrun(avail, 0, inputStream, "cie:alsa_safe_avail");
    }

    size_t remaining;
    snd_pcm_sframes_t read;

    char* bufptr = inputStream->buffer->WriteBuf(remaining);
    Log("inputStream buffer available: %d", remaining);

    if ((remaining > 0) && (avail > 0))
    {
        remaining = std::min((size_t)avail, remaining);
        read = snd_pcm_readi(handle, bufptr, remaining);
        remaining = HandleXrun(read, remaining, inputStream, "snd_pcm_readi");
        inputStream->buffer->Written(remaining);
    }

    snd_pcm_sframes_t samplesPer10ms = inputStream->spec.rate / 100;
    size_t samplesInBuffer = inputStream->buffer->Occupied();

    inputStream->buffer->ReadBuf(samplesInBuffer);
    
    DeviceSettings *device = static_cast<DeviceSettings*>(inputStream->callback.userdata);

    // Pick 10ms off of ringbuffer and send to SAL for processing.
    while ((samplesInBuffer >= (size_t)samplesPer10ms) && (inputStream->running))
    {
        if (device->callback && device->started)
        {
            char* bufptr = inputStream->buffer->ReadBuf(samplesInBuffer);
            inputStream->callback.callback((short*)bufptr, samplesPer10ms, device->spec.rate, device->spec.channels, device->userData);
        }

        inputStream->buffer->Read(samplesPer10ms);
        inputStream->buffer->ReadBuf(samplesInBuffer);
    }
}

static int UnixWrite(AlsaHandle *apiInfo, const char* buffer, snd_pcm_sframes_t frames)
{
    return snd_pcm_writei((snd_pcm_t*)apiInfo->handle, buffer, frames);
}

//! Process playback buffer.
void AlsaBackend::CallbackOutputEvent(AudioStream* outputStream)
{
    int result;

    DeviceSettings* device = static_cast<DeviceSettings*>(outputStream->callback.userdata);
    AlsaHandle *apiInfo = (AlsaHandle *)outputStream->apiHandle;
    snd_pcm_t *handle = (snd_pcm_t *)apiInfo->handle;

    apiInfo->xrun = false;

    snd_pcm_sframes_t samplesPer10ms = outputStream->spec.rate / 100;
    snd_pcm_sframes_t avail;

    while (outputStream->running && (apiInfo->type != DeviceInfo::TYPE_NULL))
    {
        avail = alsa_safe_avail(handle, outputStream->spec.bufferFrames * outputStream->spec.FrameBytes(), outputStream->spec);
        if (avail < 0)
        {
            if ((result = HandleXrun(avail, 0, outputStream, "coe:alsa_safe_avail")) == 0)
                continue;

            return;
        }

        if (avail < samplesPer10ms)
           break;

        //printf("Filling up to %ld frames\n", avail);

        snd_pcm_sframes_t occupied = outputStream->buffer->Occupied();

        if (occupied < samplesPer10ms)
        {
            size_t toFill;
            char* bufptr = outputStream->buffer->WriteBuf(toFill);

            if (toFill >= (size_t)samplesPer10ms)
            {
                outputStream->callback.callback((short*)bufptr, samplesPer10ms, device->spec.rate,
                    device->spec.channels, device->userData);
                if (!outputStream->running)
                    break;
                outputStream->buffer->Written(samplesPer10ms);
            }
        }

        size_t toWrite;
        char* bufptr = outputStream->buffer->ReadBuf(toWrite);
        toWrite = HandleXrun(UnixWrite(apiInfo, bufptr, toWrite), toWrite, outputStream, "UnixWrite");

        if ((ssize_t)toWrite > 0)
        {
            outputStream->buffer->Read(toWrite);
        }

        if (apiInfo->first) {
            Log("Starting playback.");
            AlsaPrepareStart(handle);
            apiInfo->first = false;
        }
    }
}

void AlsaBackend::UpdateDevicesList()
{
    Log("AlsaBackend::UpdateDeviceList\n");

    DeviceInfo info;
    void **hints, **n;
    char *name, *descr, *desc;
    unsigned devices = 0;

    InitDevicesList();

    info.SetDevice(devices++, "default", "Default device", "default");
    info.type = DeviceInfo::TYPE_PLUG;
    info.direction = 0;
    PcmPreProbe(info, OUTPUT);
    PcmPreProbe(info, INPUT);
    devicesList.push_back(info);

    // Start with safe alsa detection, list the devices from software config.

    snd_config_update();

    if (snd_device_name_hint(-1, "pcm", &hints) < 0)
        return;

    n = hints;

    while (*n != NULL) {
        name = snd_device_name_get_hint(*n, "NAME");
        descr = snd_device_name_get_hint(*n, "DESC");

        if (!descr)
            desc = (char*)"";
        else
        {
            desc = descr;
            for (int i = strlen(desc); i > 0; i--)
                if (desc[i-1] == '\n')
                    desc[i-1] = ' ';
        }

        if (IgnorePlugin(name))
        {
            Log("Ignoring ALSA device %s", name);
        }
        else
        {
            info.SetDevice(devices++, name, desc, name);
            info.type = DeviceInfo::TYPE_PLUG;
            info.probed = false;
            info.direction = 0;

            PcmPreProbe(info, OUTPUT);
            PcmPreProbe(info, INPUT);

            if (info.direction != 0)
                devicesList.push_back(info);
        }

        if (name != NULL)
            free(name);
        if (descr != NULL)
            free(descr);

        n++;
    }
    snd_device_name_free_hint(hints);

    // Continue with new detection, this is a more thorough test with probing device characteristics

    enum { IDLEN = 12 };
    char hwdev[IDLEN+1];
    int card, err, dev;
    snd_ctl_t*             handle = NULL;
    snd_ctl_card_info_t*   cardinfo;
    snd_pcm_info_t*        pcminfo;

    snd_ctl_card_info_alloca(&cardinfo);
    snd_pcm_info_alloca(&pcminfo);

    card = -1;
    while (snd_card_next(&card) == 0 && card >= 0)
    {
        snprintf(hwdev, IDLEN, "hw:%d", card);
        err = snd_ctl_open(&handle, hwdev, 0);

        if (sc_errcheck(err, "opening control interface", card, -1))
            continue;

        err = snd_ctl_card_info(handle, cardinfo);

        if (sc_errcheck(err, "obtaining card info", card, -1))
        {
            snd_ctl_close(handle);
            continue;
        }

        Log("Card %d, ID '%s', name '%s'", card, snd_ctl_card_info_get_id(cardinfo), snd_ctl_card_info_get_name(cardinfo));

        dev = -1;

        if (snd_ctl_pcm_next_device(handle, &dev) < 0)
        {
            snd_ctl_close(handle);
            continue;
        }

        while (dev >= 0)
        {
            if (!DevProbe(handle, pcminfo, card, dev, OUTPUT) && !DevProbe(handle, pcminfo, card, dev, INPUT))
            {
                if (snd_ctl_pcm_next_device(handle, &dev) < 0)
                    break;
            }

            snprintf(hwdev, IDLEN, "hw:%d,%d", card, dev);
            char strbuf[DEVICE_NAME_MAXLEN];
            snprintf(strbuf, DEVICE_NAME_MAXLEN, "%s, %s", snd_ctl_card_info_get_name(cardinfo), snd_pcm_info_get_name(pcminfo));
            info.SetDevice(devices++, hwdev, strbuf, hwdev);
            info.type = DeviceInfo::TYPE_HW;
            info.probed = false;
            info.direction = 0;

            PcmPreProbe(info, OUTPUT);
            PcmPreProbe(info, INPUT);

            Log("**********\n%s :: %s\n**********\n", info.guid, info.displayName);

            devicesList.push_back(info);

            if (snd_ctl_pcm_next_device(handle, &dev) < 0)
                break;
        }

        snd_ctl_close(handle);
    }

    // And complement with chewing a bit on user-defined entries, too.
    // from PortAudio
    /* Iterate over plugin devices */

    snd_config_t *topNode = NULL;
    assert(snd_config);

    if ((err = snd_config_search(snd_config, "pcm", &topNode)) >= 0)
    {
        snd_config_iterator_t i, next;

        snd_config_for_each(i, next, topNode)
        {
            const char *tpStr = "unknown", *idStr = NULL;
            int err = 0;

            snd_config_t *n = snd_config_iterator_entry(i), *tp = NULL;

            if ((err = snd_config_search(n, "type", &tp)) < 0)
            {
                if (-ENOENT != err)
                {
                    Log("plugin list error: %s", snd_strerror(err));
                }
            }
            else
            {
                snd_config_get_string(tp, &tpStr);
            }
            snd_config_get_id(n, &idStr);
            if (IgnorePlugin(idStr))
            {
                Log("Ignoring ALSA plugin device %s of type %s", idStr, tpStr);
                continue;
            }
            Log("Found plugin %s of type %s", idStr, tpStr);

            info.SetDevice(devices++, idStr, idStr, tpStr);
            info.probed = false;
            info.direction = 0;

            if (strncmp(tpStr, "bluetooth", 9)==0)
                info.type = DeviceInfo::TYPE_BLUETOOTH;
            else if(strncmp(tpStr, "null", 4) == 0)
            {
                info.type = DeviceInfo::TYPE_NULL;
                // Never need to probe the null device.
                info.probed = true;
            }
            else if(strncmp(tpStr, "unknown", 4) == 0)
                info.type = DeviceInfo::TYPE_UNKNOWN;
            else
            {
                info.type = DeviceInfo::TYPE_PLUG;
                // No need to preprobe bluetooth, null and unknown(?) types.
                PcmPreProbe(info, OUTPUT);
                PcmPreProbe(info, INPUT);
            }

            devicesList.push_back(info);
        }
    }
    else
    {
        Log("Iterating over ALSA plugins failed: %s", snd_strerror(err));
    }
}
