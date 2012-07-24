//
// Example audio backend for ALSA.
//
// Written by Stanislav Karchebny <berkus@skype.net>
// Copyright (c) 2007-2012 Skype Technologies SA.
//
#pragma once

#include <map>
#include "DeviceInfo.h"

class AlsaThread;

class StreamSpec;

class AudioStream;

typedef int (*SOUNDCARD_CALLBACK)(short *buff, short noOfSamples, unsigned int fs, unsigned int nChannels, void *userData);

/*!
 * Direction in which the stream is playing.
 * Doubles as set of flags in DeviceInfo.
 */
enum StreamDirection {
    UNKNOWN = 0,
    INPUT = 1,
    OUTPUT = 2,
    NOTIFICATION = 4
};

/*!
 * Status of the stream.
 */
enum StreamState {
    STREAM_CLOSED = -2,   //!< Stream is not open.
    STREAM_STOPPED = -1,  //!< Stream is prepared but not playing.
    STREAM_RUNNING = 0    //!< Stream is in progress.
};

/*!
 * Implementation of a specific API.
 */
class AlsaBackend
{
public:
    typedef std::vector<DeviceInfo*> DeviceInfoPtrs;
    typedef std::vector<DeviceInfo>  DeviceInfoList;

    AlsaBackend();
    virtual ~AlsaBackend();

    /*!
     * @return true if backend was initialized successfully and is operational.
     */
    bool IsGood();

    /*!
     * Force update of internal cached device list.
     */
    void UpdateDevicesList();

    /*!
     * List all found devices with capability to work in direction @c dir
     * without probing their exact characteristics. If @c dir is UNKNOWN,
     * return all found devices regardless of their direction.
     *
     * Backend may choose to probe and cache device characteristics provided
     * probing time is guaranteed to be small and will not stall the process.
     * Backend may choose to ignore @c dir and return the same list all the time.
     *
     * @param[out] devs List of pointers to devices cached in the internal device list (may become invalidated).
     */
    void GetDevices(DeviceInfoPtrs& devs, StreamDirection dir);

    /*!
     * Probe device characteristics.
     *
     * Try to select parameters closest to the passed in @c spec, modifying it if
     * exact match is not possible.
     * @return @c true if device was probed successfully. @c spec may contain updated constraints.
     */
    bool ProbeDevice(DeviceInfo* device, StreamSpec& spec);

    /*!
     * Get device info from pool of cached infos based on index.
     * @return device info reference to internal pool entry.
     */
    DeviceInfo* GetDeviceInfo(size_t idx);

    /*!
     * Get device info from pool of cached infos based on GUID.
     * @return device info pointer to internal pool entry.
     */
    DeviceInfo* GetDeviceInfo(std::string guid);

    /*!
     * Try and open the stream for capture or playback using given device.
     *
     * @param options will be modified to reflect actual options used to open the device.
     * @return @c NULL if stream open failed. Otherwise, a stream handle is returned,
     * which can be used to operate the stream.
     */
    AudioStream* OpenStream(StreamDirection dir, DeviceInfo* device, StreamSpec& spec, SOUNDCARD_CALLBACK callback, void* userdata);

    /*!
     * Get index of device to be used as default for capture or playback.
     *
     * @return index of corresponding default device in detected devices list.
     */
    int GetDefaultDevice(StreamDirection dir);
    
    /*!
     * Get DefaultDeviceInfo for the stream direction
     */
    inline DeviceInfo* GetDefaultDeviceInfo(StreamDirection dir) {return GetDeviceInfo(GetDefaultDevice(dir)); };
    
    // Methods below are called by AudioStream

    /*!
     * Kick off the stream running.
     * After this call data is transferred to/from audio device.
     * Note that audio callback function can be called prior to call of this method.
     * @return @c true if stream started successfully.
     */
    bool StartStream(AudioStream* s);

    /*!
     * Stop the stream. No data is transferred after this call. Audio callback function
     * may still be called from the backend to complete pending audio data.
     */
    void StopStream(AudioStream* s);

    /*!
     * Close stream, freeing all used resources.
     * Audio callback will not be called after this method returns.
     */
    void CloseStream(AudioStream* s);

    /*!
     * Get current stream volume.
     * @return fractional volume between 0.0 and 1.0
     */
    double GetStreamVolume(AudioStream* s);

    /*!
     * Set current stream volume.
     * @param volume should be value between 0.0 and 1.0
     */
    void SetStreamVolume(AudioStream* s, double volume);

protected: friend class AlsaThread; // Internal use only.
    void CallbackInputEvent(AudioStream* s);
    void CallbackOutputEvent(AudioStream* s);

protected:
    void InitDevicesList();     //!< @internal method to init devicesList.
    DeviceInfoList devicesList; //!< Cached list of detected devices.

private:
    bool ProbeParameters(DeviceInfo* device, StreamSpec& spec, snd_pcm_t* phandle, snd_pcm_stream_t stream, snd_pcm_info_t *pcminfo, char *name, snd_pcm_hw_params_t *params);
    pthread_t thrd;
    std::map<AudioStream*, AlsaThread*> p_alsathread; // @todo: hmm makes sense to link thread with streams instead then?
};
