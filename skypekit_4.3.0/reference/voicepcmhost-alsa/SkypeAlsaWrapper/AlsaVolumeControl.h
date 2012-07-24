#pragma once

#include "VolumeControl.h"
#include "DeviceInfo.h"

/*!
 * ALSA volume control interface.
 */
class AlsaVolumeControl : public VolumeControl
{
    char* ctlDevice;
    char* micBoost;
    char* micVolume;
    char *micSwitch;
    // int   micVolumeSet;

    int GetSetVolume(const char* ctlDevice, const char* elem_name, int* min, int* max, int* step, int* out_vol, int* new_vol);

public:
    AlsaVolumeControl()
        : VolumeControl()
        , ctlDevice(0)
        , micBoost(0)
        , micVolume(0)
        , micSwitch(0)
        //, micVolumeSet(-1)
    {}
    virtual ~AlsaVolumeControl();

    virtual double GetVolume(AudioStream* s);
    virtual void SetVolume(AudioStream* s, double vol);

    void ProbeMixer(DeviceInfo* device);
};
