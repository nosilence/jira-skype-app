#pragma once

#include <pthread.h>
#include <signal.h>
#include "AlsaFormat.h"
#include "PhantomRingbuffer.h"
#include "StreamSpec.h"
#include "AlsaBackend.h"
/*!
 * This global structure type is used to pass callback information
 * between the private backend stream structure and global callback
 * handling functions.
 */

class AudioStream;

class CallbackInfo
{
public:
    AlsaBackend* object;     //!< Used as a "this" pointer.
    AudioStream* stream;
    pthread_t*   thread;
    SOUNDCARD_CALLBACK   callback;
    void*            userdata;
    void*            apiInfo;   //!< API-specific callback information.
    volatile sig_atomic_t isRunning;

    //! Default constructor.
    CallbackInfo() : object(0), stream(0), thread(0), callback(0), userdata(0), apiInfo(0), isRunning(0) {}
};

/*!
 * PCM data stream.
 */
class AudioStream
{
public:
    void*            apiHandle; //!< Backend-specific.
    StreamDirection  dir;        //!< Either input or output stream.

    //! Format specific information.
    bool   userInterleaved;      //!< User buffer is interleaved.
    bool   deviceInterleaved;    //!< Device buffer is interleaved.
    Format userFormat;
    Format deviceFormat;
    int    userChannels;
    int    deviceChannels;

    //! Sample buffer.
    PhantomRingbuffer*   buffer;
    volatile sig_atomic_t inCallback;

    double          streamTime;        //!< Number of seconds elapsed since the stream started.
    unsigned long   latency;            //!< Calculated stream latency (µs).
    StreamSpec      spec;               //!< Current stream specification.
    VolumeControl* mixer;              //!< Volume control interface, if available.
    double          desiredVolume;     //!< Remembered stream volume for AGC.

    volatile sig_atomic_t   running;
    CallbackInfo callback;
    pthread_mutex_t  mutex;

public:
    AudioStream();
    ~AudioStream();

    void Dump();

    enum { STREAM_SPEC_TEXT_MAX = 1024 };
    void PrintStreamSpec(char* buf, size_t bufsize);

    // Methods below call implementations in the backend.
    bool Start();
    void Stop();
    void Close();
    double GetVolume();
    void SetVolume(double volume);
};
