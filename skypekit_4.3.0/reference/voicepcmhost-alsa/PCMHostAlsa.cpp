#include <vector>
#include "PCMHostAlsa.hpp"
#include "AudioStream.h"
#include <algorithm>
#include <iostream>
#include "Log.h"

typedef signed short SAMPLE_TYPE;

// Allowed sampleRate range.
#define MIN_FS 8000
#define MAX_FS 48000

int sample_rates[] = {MAX_FS, 44100, 32000, 16000, MIN_FS, 22050}; // in order of preference
#define MAX_FS_TRIALS  (sizeof(sample_rates)/sizeof(sample_rates[0]))

#define boundi(minval, val, maxval) (std::min(maxval, std::max(minval, val)))

static const StreamDirection dirs[NR_OF_SUPPORTED_DEVICES] = { INPUT, OUTPUT, NOTIFICATION };

static const char *devTypeNames[NR_OF_DEVICES] = {"capture", "playback", "notification"};

AlsaPCMInterface::AlsaPCMInterface(SkypePCMCallbackInterface* transport)
    : m_transport(transport)
    , m_input_started(0)
    , m_output_started(0)
    , m_input_muted(0)
    , m_output_muted(0)
    , m_input_volume(100)
    , m_output_volume(100)
    , m_input_rate(MAX_FS)
    , m_output_rate(MAX_FS)
    , m_input_channels(1)
    , m_output_channels(2)
    , audio(0)
{
}

AlsaPCMInterface::~AlsaPCMInterface()
{      
}

int AlsaPCMInterface::Init()
{
    audio = new AlsaBackend();
    if (!audio || !audio->IsGood())
    {
        delete audio;
        audio = NULL;
        printf("Init error\n");
        return PCMIF_ERROR_NOT_INITIALIZED;
    }

    device[INPUT_DEVICE].device = audio->GetDefaultDeviceInfo(INPUT);
    device[OUTPUT_DEVICE].device = audio->GetDefaultDeviceInfo(OUTPUT);
    device[PCM_DEVICE].device = audio->GetDefaultDeviceInfo(OUTPUT);

    return PCMIF_OK;
}

int AlsaPCMInterface::Start(int deviceType)
{
    Log("Start Start, deviceType=%d \n", deviceType);
    if ((deviceType < 0) || (deviceType >= NR_OF_SUPPORTED_DEVICES))
    {
        printf("Error: %s:%s:%d\n", __FILE__, __func__, __LINE__);
        return PCMIF_ERROR_UNKNOWN_DEVICE;
    }
    DeviceSettings& dev = device[deviceType];

    if (!audio || !audio->IsGood())
    {
        printf("Error: %s:%s:%d\n", __FILE__, __func__, __LINE__);
        return PCMIF_ERROR_NOT_INITIALIZED;
    }
    
    if (dev.started)
    {
        printf("Error: %s:%s:%d\n", __FILE__, __func__, __LINE__);
        return PCMIF_ERROR_ALREADY_RUNNING;
    }
    
    DeviceInfo* audioDev;
    unsigned int buffer_frames;
    bool format_acceptable = false;
    bool try_default_device = false;
    unsigned int FS_trials = 0;

    while ((!format_acceptable) && (FS_trials < MAX_FS_TRIALS))
    {
        dev.spec.rate = sample_rates[FS_trials];
        format_acceptable = true;
        FS_trials++;

        // Buffer size adaptive to sampleRate
        // need 10ms buffers ideally, which is:
        buffer_frames = dev.spec.rate / 100;
        buffer_frames = boundi(256, (int)buffer_frames, 4096);

        audioDev = try_default_device ? audio->GetDefaultDeviceInfo(deviceType == INPUT_DEVICE ? INPUT : OUTPUT)
                                      : dev.device;

       Log("AlsaPCMHost Start: Starting %s device '%s', buffer size %u frames\n", devTypeNames[deviceType], audioDev->displayName, buffer_frames);

        dev.spec.channels = deviceType == OUTPUT_DEVICE ? m_output_channels : m_input_channels;
        dev.spec.format = FORMAT_SINT16;
        dev.spec.fragmentFrames = buffer_frames;
        dev.spec.bufferFrames = buffer_frames * 10;
       
        dev.userData = this;
        dev.callback = (deviceType == INPUT_DEVICE) ? input_callback : output_callback;
        dev.stream = audio->OpenStream(dirs[deviceType], audioDev, dev.spec , dev.callback, /*userData:*/&dev);

        if (!dev.stream)
        {
            printf("AlsaPCMHost Start: Cannot start %s device\n", devTypeNames[deviceType]);
            format_acceptable = false;
            if (FS_trials < MAX_FS_TRIALS)
            {
                printf("AlsaPCMHost Start: failed to start %s device with %dHz\n", devTypeNames[deviceType], dev.spec.rate);
            }
            else
            {
                if (!try_default_device)
                {
                    try_default_device = true;
                    FS_trials = 0;
                }
                else
                {
                    printf("AlsaPCMHost Start: Couldn't start %s device. Failing.\n", devTypeNames[deviceType]);
                    return PCMIF_ERROR;
                }
            }
        }
    }

    char sample_spec[StreamSpec::STREAM_SPEC_TEXT_MAX];
    dev.spec.PrintStreamSpec(sample_spec, sizeof(sample_spec));
    Log("AlsaPCMHost Start: Started %s device (%s)", devTypeNames[deviceType], sample_spec);

    dev.stream->Start();
    dev.started = true;
    
    if (deviceType == OUTPUT_DEVICE)
    {
        m_output_started = true;
    }
    else
    {
        m_input_started = true;
    }

    //char stream_spec[AudioStream::STREAM_SPEC_TEXT_MAX];
    //dev.stream->PrintStreamSpec(stream_spec, sizeof(stream_spec));
    //printf("AlsaPCMHost Start: Stream started, buffer size %d/%d frames, latency %.3g msec (%s)\n", dev.spec.bufferFrames, dev.spec.fragmentFrames, dev.spec.FragmentMs(), stream_spec);
    //printf("end Start, deviceType=%d\n", deviceType);
    return PCMIF_OK;   
}

int AlsaPCMInterface::Stop(int deviceType)
{
    Log("start Stop, deviceType=%d\n", deviceType);
    if ((deviceType < 0) || (deviceType >= NR_OF_SUPPORTED_DEVICES))
    {
        printf("Error: %s:%s:%d\n", __FILE__, __func__, __LINE__);
        return PCMIF_ERROR_UNKNOWN_DEVICE;
    }

    DeviceSettings& dev = device[deviceType];

    if (!audio || !audio->IsGood())
        return PCMIF_ERROR_NOT_INITIALIZED;
    if (!dev.started)
        return PCMIF_OK; // nothing to do

    Log("AlsaPCMInterface Stop: Stopping stream\n");
    dev.stream->Stop();
    Log("AlsaPCMInterface Stop: Stopped stream\n");
  
    delete dev.stream; 
    dev.stream = 0;

    Log("AlsaPCMHost: Stopped %s device\n", devTypeNames[deviceType]);

    dev.started = false;
    if (deviceType == OUTPUT_DEVICE)
    {
        m_output_started = false;
    }
    else
    {
        m_input_started = false;
    }
    Log("end Stop, devicetype=%d\n", deviceType);
    return PCMIF_OK;
}

//=== device control ===================================================================================================

int AlsaPCMInterface::GetDefaultDevice(int /*deviceType*/, Sid::String& /*guid*/, Sid::String& /*name*/, Sid::String& /*productID*/)
{
    //Log("GetDefaultDevice, devicetype=%d\n", deviceType);
    // Not supported, we don't do anything here
    return PCMIF_OK;
}

int AlsaPCMInterface::UseDefaultDevice(int /*deviceType*/)
{
    //Log("UseDefaultDevice %d\n", deviceType);
    // Not supported, we don't do anything here
    return PCMIF_OK;
}

int AlsaPCMInterface::GetCurrentDevice(int deviceType, Sid::String& guid, Sid::String& name, Sid::String& productID)
{
    Log("start GetCurrentDevice %d\n", deviceType);
    DeviceSettings& currentdevice = device[deviceType];
    guid = currentdevice.device->guid;
    name = currentdevice.device->displayName;
    productID = currentdevice.device->productId;
    Log("end GetCurrentDevice %d\n", deviceType);
    return PCMIF_OK;
}

int AlsaPCMInterface::UseDevice(int deviceType, const Sid::String& guid)
{
    std::string strguid(guid);
    Log("start UseDevice %d guid=%s\n", deviceType, strguid.c_str());
    DeviceSettings& currentdevice = device[deviceType];

    if(currentdevice.started)
    {   
        Log("currentdevice started, end UseDevice %d\n", deviceType);
        return PCMIF_ERROR_ALREADY_RUNNING;
    }
        
    // For PC try to open in stereo for output
    if (deviceType == INPUT_DEVICE) 
    {
        currentdevice.spec.channels = 1;
        m_input_channels = 1;
    } 
    else 
    {
        if (currentdevice.device->outMinChannels >= 2)
        {
           currentdevice.spec.channels = 2;
           m_output_channels = 2;
        }
        else
        {
           currentdevice.spec.channels = 1;
           m_output_channels = 1;
        }
    }
    
    
    currentdevice.device = audio->GetDeviceInfo(strguid); 

    return PCMIF_OK;
}

int AlsaPCMInterface::GetDeviceCount(int deviceType, uint& count)
{
    Log("start GetDeviceCount %d\n", deviceType);
    AlsaBackend::DeviceInfoPtrs devs;
    audio->GetDevices(devs, deviceType == INPUT_DEVICE ? INPUT : OUTPUT);
    count = devs.size();
    Log("end GetDeviceCount %d, count = %d\n", deviceType, count);
    return PCMIF_OK;
}

int AlsaPCMInterface::GetDevices(int deviceType, Sid::List_String& guid, Sid::List_String& name, Sid::List_String& productID)
{
    Log("start GetDevices %d\n", deviceType);

    AlsaBackend::DeviceInfoPtrs devs;
    audio->GetDevices(devs, deviceType == INPUT_DEVICE ? INPUT : OUTPUT);
    
    for (uint idevnum = 0; idevnum < devs.size(); idevnum++)
    {
        guid.append(devs[idevnum]->guid);
        name.append(devs[idevnum]->displayName);
        productID.append(devs[idevnum]->productId);
    } 
    Log("end GetDevices %d\n", deviceType);
    return PCMIF_OK;
}

//=== volume control ===================================================================================================

int AlsaPCMInterface::GetVolumeParameters(int deviceType, unsigned int &range_min, unsigned int &range_max, unsigned int &volume, int &boost)
{
    Log("start GetVolumeParameters, devicetype = %d\n", deviceType);
    if (deviceType != INPUT_DEVICE)
    {        
        return PCMIF_ERROR_PROP_NOT_SUPPORTED;
    }
    DeviceSettings& currentdevice = device[deviceType];    
    if (currentdevice.stream)
    {    
        volume = currentdevice.stream->GetVolume() * 65535;
    }
    else
    {
        volume = 65535;
    }
    range_min = 0;
    range_max = 65535;
                
    // boost not supported
    boost = -1;
    Log("end GetVolumeParameters, devicetype=%d\n", deviceType);
    return PCMIF_OK;
}

int AlsaPCMInterface::SetVolume(int deviceType, unsigned int volume)
{
    Log("start SetVolume %s %d\n", devTypeNames[deviceType], volume);
    // Currently only input devices have volume control
    if (deviceType != INPUT_DEVICE)
    {
        return PCMIF_ERROR_PROP_NOT_SUPPORTED;
    }    
    DeviceSettings& currentdevice = device[deviceType];  
    if(!currentdevice.started)
    {        
        printf("end SetVolume: Error, device not started\n");
        return PCMIF_ERROR_NOT_INITIALIZED;
    }
    if (currentdevice.stream)
    {
        double v = volume / 65535.0;
        currentdevice.stream->SetVolume(v);
    }
    Log("end SetVolume %s %d\n", devTypeNames[deviceType], volume);  
    return PCMIF_OK;
}

int AlsaPCMInterface::SetInputBoost(int /*boost*/)
{
    //Log("SetInputBoost %d\n", boost);
    return PCMIF_ERROR_PROP_NOT_SUPPORTED;
}

int AlsaPCMInterface::GetMute(int /*deviceType*/, int& /*muted*/)
{
    //Log("GetMute %s %d\n", devTypeNames[deviceType], muted);
    return PCMIF_ERROR_PROP_NOT_SUPPORTED;
}

int AlsaPCMInterface::SetMute(int /*deviceType*/, int /*mute*/)
{
    //Log("SetMute %s %d\n", devTypeNames[deviceType], mute);
    return PCMIF_ERROR_PROP_NOT_SUPPORTED;
}

//=== sample rate control ==============================================================================================
static void EnumerateSampleRates(std::vector<int>& rates)
{
    rates.clear();
    for (unsigned i = 0; i < MAX_FS_TRIALS; i++)
        rates.push_back(sample_rates[i]);
}

int AlsaPCMInterface::GetSampleRateCount(int /*deviceType*/, uint& count)
{
    //Log("start GetSampleRateCount, devicetype=%d\n", deviceType);
    std::vector<int> rates;
    EnumerateSampleRates(rates);
    count = rates.size() * sizeof(unsigned int);
    //Log("end GetSampleRateCount %s %d\n", devTypeNames[deviceType], count);
    return PCMIF_OK;
}

int AlsaPCMInterface::GetSupportedSampleRates(int /*deviceType*/, Sid::List_uint& sampleRates)
{
    //Log("start GetSupportedSampleRates %s\n", devTypeNames[deviceType]);
    std::vector<int> rates;
    EnumerateSampleRates(rates);
    for (unsigned int k = 0; k < rates.size(); k++)
    {
        sampleRates.append(rates[k]);
    }
    //Log("end GetSupportedSampleRates %s\n", devTypeNames[deviceType]);
    return PCMIF_OK;
}

int AlsaPCMInterface::GetCurrentSampleRate(int deviceType, unsigned int &sampleRate)
{
    Log("start GetCurrentSampleRate %s %d\n", devTypeNames[deviceType], sampleRate);
    DeviceSettings& currentdevice = device[deviceType];  
    sampleRate = currentdevice.spec.rate;
    Log("end GetCurrentSampleRate %s %d\n", devTypeNames[deviceType], sampleRate);
    return PCMIF_OK;
}

int AlsaPCMInterface::SetSampleRate(int deviceType, unsigned int sampleRate)
{
    Log("start SetSampleRate %s %d\n", devTypeNames[deviceType], sampleRate);
    DeviceSettings& currentdevice = device[deviceType];  
    currentdevice.spec.rate = boundi(MIN_FS, (int)sampleRate, MAX_FS);
    if (deviceType == INPUT_DEVICE)
    {
        m_input_rate = currentdevice.spec.rate;
    }
    else
    {
        m_output_rate = currentdevice.spec.rate;
    }
    Log("end SetSampleRate %s %d\n", devTypeNames[deviceType], sampleRate);          
    return PCMIF_OK;
}

int AlsaPCMInterface::SetNumberOfChannels(int deviceType, int numberOfChannels)
{
    Log("start SetNumberOfOutputChannels, devicetype=%d, numberofChannels=%d\n", deviceType, numberOfChannels);
    if ((numberOfChannels<1)||(numberOfChannels>2)||
        ((deviceType==INPUT_DEVICE)&&(numberOfChannels==2)))
    {
        printf("end SetNumberofOutputChannels, Error, deviceType=%d, numberofChannels=%d\n", deviceType, numberOfChannels);
        return PCMIF_ERROR;
    }
    DeviceSettings& currentdevice = device[deviceType];
    if (currentdevice.spec.channels < (unsigned int) (numberOfChannels))
    {
        printf("end SetNumberofOutputChannels, Error, deviceType=%d, numberofChannels=%d\n", deviceType, numberOfChannels);
        return PCMIF_ERROR;
    }
    Log("end SetNumberofOutputChannels, deviceType=%d, numberofChannels=%d\n", deviceType, numberOfChannels);
    return PCMIF_OK;
}

//=== audio i/o ========================================================================================================


inline int frame_size(uint frames, uint channels)
{
    return frames * channels * sizeof(SAMPLE_TYPE);
}

int AlsaPCMInterface::input_callback(short* outputBuffer, short nBufferFrames, unsigned, unsigned, void* userdata)
{
    if (!userdata)
        return -1;
    return static_cast<AlsaPCMInterface*>(userdata)->inputCallback(outputBuffer, nBufferFrames);
}

int AlsaPCMInterface::output_callback(short* outputBuffer, short nBufferFrames, unsigned, unsigned, void* userdata)
{
    if (!userdata)
        return -1;
    return static_cast<AlsaPCMInterface*>(userdata)->outputCallback(outputBuffer, nBufferFrames);
}

int AlsaPCMInterface::outputCallback(void* outputBuffer, unsigned int nBufferFrames)
{
    if(m_output_started)
    {
        Log("%s: %p %d\n", __PRETTY_FUNCTION__, outputBuffer, nBufferFrames);

        // calculate number of bytes needed
        uint requested_bytes = frame_size(nBufferFrames, m_output_channels);

        while(m_playback_buffer.size() < requested_bytes)
        {
            int needed_bytes = requested_bytes - m_playback_buffer.size();
            int bytes_in_playback_buffer = m_playback_buffer.size();

	        // only 10 ms frames are acceptable
            int bytes_to_be_requested = needed_bytes - (needed_bytes % (frame_size(1, m_output_channels) * m_output_rate / 100))
	            + (frame_size(1, m_output_channels) * m_output_rate / 100);

            m_transport->OutputDeviceReady(bytes_to_be_requested/frame_size(1, m_output_channels), m_output_rate, m_output_channels, m_playback_buffer_transport);

            // it is possible that less 10 ms frames were returned than requested, looping in that case
            m_playback_buffer.resize(bytes_in_playback_buffer + m_playback_buffer_transport.size());
            memcpy((char *)m_playback_buffer.data() + bytes_in_playback_buffer, m_playback_buffer_transport.data(), m_playback_buffer_transport.size());


	        // need to prevent endless looping, 0 bytes returned is erroneous situation, play silence
	        if(!m_playback_buffer_transport.size())
	        {
	            memset(outputBuffer, 0, frame_size(nBufferFrames, m_output_channels));
	            return 0;
	        }
        }
        memcpy(outputBuffer, m_playback_buffer.data(), frame_size(nBufferFrames, m_output_channels));
      
        memcpy((char*)m_playback_buffer.data(), m_playback_buffer.data() + frame_size(nBufferFrames, m_output_channels), m_playback_buffer.size() - frame_size(nBufferFrames, m_output_channels));
        m_playback_buffer.resize(m_playback_buffer.size() - frame_size(nBufferFrames, m_output_channels));
    }
    else
    {
        printf("%s: output not started %p %d\n", __PRETTY_FUNCTION__, outputBuffer, nBufferFrames);
    }
    Log("end outputCallback\n");
    return 0;
}

int AlsaPCMInterface::inputCallback(void* inputBuffer, unsigned int nBufferFrames)
{
    Log("start inputCallback\n");
    if(m_input_started)
    {
      Log("%s: %p %d\n", __PRETTY_FUNCTION__, inputBuffer, nBufferFrames);
      int bytes_received = frame_size(nBufferFrames, m_input_channels);
      int bytes_from_last_callback = m_capture_buffer.size();
      int bytes_available = bytes_received + bytes_from_last_callback;
      // only 10 ms frames are processed
      int bytes_to_be_sent = bytes_available - (bytes_available % (frame_size(1, m_input_channels) * m_input_rate / 100));

      m_capture_buffer.resize(bytes_to_be_sent);
      //std::cout<<"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\nAlsaPCMInterface::inputCallback: bytes_from_last_callback="<< bytes_from_last_callback<<", pInputBuffer=" << inputBuffer<<", bytes_to_be_sent=" << bytes_to_be_sent<<std::endl<<"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n"<<std::flush;
      
      memcpy((char*)m_capture_buffer.data() + bytes_from_last_callback, inputBuffer, bytes_to_be_sent - bytes_from_last_callback);

      m_transport->InputDeviceReady(bytes_to_be_sent/frame_size(1, m_input_channels), m_input_rate, m_input_channels, m_capture_buffer);

      m_capture_buffer.resize(bytes_available - bytes_to_be_sent);
      memcpy((char*)m_capture_buffer.data(), (char*)inputBuffer + bytes_to_be_sent - bytes_from_last_callback, bytes_available - bytes_to_be_sent);
    }
    else
    {
      printf("%s: input not started %p %d\n", __PRETTY_FUNCTION__, inputBuffer, nBufferFrames);
    }
    Log("end inputCallback\n");
    return 0;
}

//======================================================================================================================
// instantiation
//======================================================================================================================

SkypePCMInterface* SkypePCMInterfaceGet(SkypePCMCallbackInterface* transport)
{
    Log("SkypePCMInterfaceGet\n");
    return new AlsaPCMInterface(transport);
}

void SkypePCMInterfaceRelease(SkypePCMInterface* pcmif)
{
    Log("SkypePCMInterfaceRelease\n");
    delete pcmif;
}
