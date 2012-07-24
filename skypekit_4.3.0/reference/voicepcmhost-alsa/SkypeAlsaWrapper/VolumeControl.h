#pragma once

class AudioStream;

/*!
 * Abstract volume control interface.
 */
class VolumeControl
{
public:
    virtual ~VolumeControl() {}

    virtual double GetVolume(AudioStream* s) = 0;
    virtual void   SetVolume(AudioStream* s, double vol) = 0;
};
