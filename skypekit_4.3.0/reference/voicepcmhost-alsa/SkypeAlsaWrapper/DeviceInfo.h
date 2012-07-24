#pragma once

#include <vector>
#include <string>
#include <signal.h> /* For sig_atomic_t */
#include "StreamSpec.h"

#define DEVICE_NAME_MAXLEN			200

#ifndef SOUNDCARD_CALLBACK_DEFINED
#define SOUNDCARD_CALLBACK_DEFINED
typedef int (*SOUNDCARD_CALLBACK)(short *buff, short noOfSamples, unsigned int fs, unsigned int nChannels, void *userData);
#endif

#define DEVICE_INVALID_ID ((unsigned int)-1)

class VolumeControl;

/*!
 * Type holding a set of format flags.
 */
typedef int formats; 


/*!
 * Description of a single device.
 */
class DeviceInfo
{
public:
    enum type_e {
        TYPE_UNKNOWN = -1, //!< Device is not detected yet.
        TYPE_HW,           //!< Hardware device.
        TYPE_PLUG,         //!< ALSA plugin device.
        TYPE_BLUETOOTH,    //!< Bluetooth device, e.g. a headset.
        TYPE_NULL          //!< ALSA null plugin, for special delay handling.
    };

    static DeviceInfo null; //!< Default uninitialized device.
	
	unsigned int            id;			/* Set if identifier is an integer (otherwise DEVICE_INVALID_ID) */
	char                    guid[DEVICE_NAME_MAXLEN];		/* Set if identifier is a string  (otherwise "") */
	char                    displayName[DEVICE_NAME_MAXLEN];		/* Device name (readable) */
	char                    productId[DEVICE_NAME_MAXLEN];
    bool                    probed;    //!< @c true if below device characteristics were probed and are meaningful.
    type_e                  type;      //!< Device type.
    int                     direction; //!< Bitwise flags of stream_direction: indicates if playback and/or capture is supported.
    unsigned                inMinChannels, inMaxChannels;   //!< Supported range of input channels.
    unsigned                outMinChannels, outMaxChannels; //!< Supported range of output channels.
    formats                 supportedFormats; //!< Supported I/O sample formats.
    std::vector<int>        supportedRates;   //!< Supported sample rates.
    bool                    anyRate;    //!< Some devices support all rates from min_rate to max_rate.
    int                     minRate, maxRate; //!< Supported sample rate range (for any_rate devices).
    VolumeControl*          mixer;       //!< Volume control interface.

public:
    DeviceInfo();
    ~DeviceInfo();
    void SetDevice(unsigned int id, const char *guid, const char *name, const char *hwName);

    bool IsSampleRateSupported(int rate);
};


class AudioStream;

struct DeviceSettings
{
    volatile sig_atomic_t    started;   /**< Device I/O is active. */
    SOUNDCARD_CALLBACK callback;  /**< SAL processing callback. */
    void*                    userData;  /**< SAL callback data. */
    DeviceInfo*    device;    /**< Current device ID info. */
    StreamSpec     spec;      /**< Stream sample rate, channels and bitness. */
    AudioStream*   stream;    /**< Stream handle. */

    DeviceSettings() : started(0), callback(0), userData(0), device(0), spec(), stream(0) {}
};
