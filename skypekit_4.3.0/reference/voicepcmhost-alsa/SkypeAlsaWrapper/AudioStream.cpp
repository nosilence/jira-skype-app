//======================================================================================================================
// AudioStream
//======================================================================================================================

#include "AudioStream.h"
#include "MutexLock.h"
#include "Log.h"

AudioStream::AudioStream()
    : apiHandle(0)
    , dir(UNKNOWN)
    , userInterleaved(true)
    , deviceInterleaved(true)
    , userFormat(FORMAT_UNKNOWN)
    , deviceFormat(FORMAT_UNKNOWN)
    , userChannels(0)
    , deviceChannels(0)
    , buffer(0)
    , inCallback(0)
    , streamTime(0.0)
    , latency(0)
    , mixer(0)
    , desiredVolume(0.0)
    , running(0)
{
    pthread_mutex_init(&mutex, NULL);
}

AudioStream::~AudioStream()
{
    Log("~AudioStream()");
    Close();
    pthread_mutex_destroy(&mutex);
}

bool AudioStream::Start()
{
    if (!callback.object)
        return false;
    return callback.object->StartStream(this);
}

void AudioStream::Stop()
{
    if (!callback.object)
        return;
    callback.object->StopStream(this);
}

void AudioStream::Close()
{
    Log("AudioStream::close()\n");

    if (!callback.object)
        return;

    MutexLock lock(mutex);
    Log("AudioStream::close() got mutex");

    if (dir == UNKNOWN) {
        Log("AudioStream.close(): no open stream to close!");
        return;
    }

    if (running)
        Stop();

    // perform backend-specific shutdown
    callback.object->CloseStream(this);

    delete callback.thread;

    dir = UNKNOWN;
    delete buffer; buffer = NULL;
}

double AudioStream::GetVolume()
{
    if (!callback.object)
        return 0.0;
    return callback.object->GetStreamVolume(this);
}

void AudioStream::SetVolume(double volume)
{
    if (!callback.object)
        return;
    callback.object->SetStreamVolume(this, volume);
}

void AudioStream::PrintStreamSpec(char* buf, size_t bufsize)
{
    snprintf(buf, bufsize,
    "AudioStream dump:|"
    "apiHandle:%p|"
    "dir:%s (%d)|"
    "userInterleaved:%d|"
    "deviceInterleaved:%d|"
    "userFormat:%x|"
    "deviceFormat:%x|"
    "nUserChannels:%d|"
    "nDeviceChannels:%d|"
    "running:%d|"
    "streamTime:%.5g|"
    "latency:%ld|"
    "options{.rate:%d|"
    ".format:%x|"
    ".nChannels:%d|"
    ".firstChannel:%d|"
    ".fragment_frames:%d|"
    ".buffer_frames:%d}|"
    "mixer:%p|"
    "callbackInfo{.object:%p|"
    ".stream:%p|"
    ".thread:%p|"
    ".callback:%p|"
    ".userdata:%p|"
    ".api_info:%p|"
    ".is_running:%d}|",
    apiHandle,
    dir == INPUT ? "INPUT" : dir == OUTPUT ? "OUTPUT" : "NOTIFY", dir,
    userInterleaved,
    deviceInterleaved,
    userFormat,
    deviceFormat,
    userChannels,
    deviceChannels,
    running,
    streamTime,
    latency,
    spec.rate, 
    spec.format,
    spec.channels, 
    spec.firstChannel,
    spec.fragmentFrames, 
    spec.bufferFrames,
    mixer,
    callback.object, 
    callback.stream,
    callback.thread, 
    callback.callback,
    callback.userdata, 
    callback.apiInfo,
    callback.isRunning);
}

void AudioStream::Dump()
{
    char buf[STREAM_SPEC_TEXT_MAX];
    memset(buf, 0, STREAM_SPEC_TEXT_MAX);
    PrintStreamSpec(buf, sizeof(buf));
    size_t len = strlen(buf);
    for(size_t i = 0; i < len; ++i)
        if(buf[i]=='|')
            buf[i]='\n';
    Log("%s", buf);
}

//======================================================================================================================
