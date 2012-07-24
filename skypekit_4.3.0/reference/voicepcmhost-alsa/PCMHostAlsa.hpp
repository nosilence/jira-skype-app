#pragma once

#include <stdio.h>
#include "SidPlatform.hpp"
#include "SidPCMInterface.hpp"
#include "AlsaBackend.h"

/*!
 * This device type is used by the PCMHost interface.
 */
#ifndef INPUT_DEVICE
#define INPUT_DEVICE 0
#endif
#ifndef OUTPUT_DEVICE
#define OUTPUT_DEVICE 1
#endif
#ifndef PCM_DEVICE
#define PCM_DEVICE 2
#endif
#define NR_OF_SUPPORTED_DEVICES (PCM_DEVICE+1)

//======================================================================================================================
// interface
//======================================================================================================================

class AlsaPCMInterface : public SkypePCMInterface//, public Sid::Thread
{
public:
    AlsaPCMInterface(SkypePCMCallbackInterface* transport);
    ~AlsaPCMInterface();

    virtual int Init();
    virtual int Start(int deviceType);
    virtual int Stop(int deviceType);
    virtual int GetDefaultDevice(int deviceType, Sid::String& guid, Sid::String& name, Sid::String& productID);
    virtual int UseDefaultDevice(int deviceType);
    virtual int GetCurrentDevice(int deviceType, Sid::String& guid, Sid::String& name, Sid::String& productID); 
    virtual int UseDevice(int deviceType, const Sid::String& guid);
    virtual int GetDeviceCount(int deviceType, uint& count);
    virtual int GetDevices(int deviceType, Sid::List_String& guid, Sid::List_String& name, Sid::List_String& productID);
    virtual int GetVolumeParameters(int deviceType, uint &range_min, uint &range_max, uint &volume, int &boost);
    virtual int SetVolume(int deviceType, unsigned int volume);
    virtual int SetInputBoost(int boost);
    virtual int GetMute(int deviceType, int &muted);
    virtual int SetMute(int deviceType, int mute);
    virtual int GetSampleRateCount(int deviceType, uint& count);
    virtual int GetSupportedSampleRates(int deviceType, Sid::List_uint& sampleRates);
    virtual int GetCurrentSampleRate(int deviceType, unsigned int &sampleRate);
    virtual int SetSampleRate(int deviceType, unsigned int sampleRate);
    virtual int SetNumberOfChannels(int deviceType, int numberOfChannels);
    virtual int CustomCommand(const Sid::String& /*cmd*/, Sid::String& /*response*/) {return PCMIF_ERROR;}

//    virtual void Run();

private:
    int outputCallback(void* outputBuffer, unsigned int nBufferFrames);
    int inputCallback(void* inputBuffer, unsigned int nBufferFrames);

    static int output_callback(short *buff, short noOfSamples, unsigned int fs, unsigned int nChannels, void *data);
    static int input_callback(short *buff, short noOfSamples, unsigned int fs, unsigned int nChannels, void *data);

private:
    SkypePCMCallbackInterface* m_transport;
    volatile sig_atomic_t m_input_started, m_output_started;
    bool m_input_muted, m_output_muted;
    int m_input_volume, m_output_volume;
    int m_input_rate, m_output_rate;
    int m_input_channels, m_output_channels;

    Sid::Binary m_playback_buffer;
    Sid::Binary m_playback_buffer_transport;
    Sid::Binary m_capture_buffer;

    AlsaBackend*    audio;
    DeviceSettings  device[NR_OF_SUPPORTED_DEVICES];
};
