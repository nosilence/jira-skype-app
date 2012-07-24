#include "DeviceInfo.h"
#include "AlsaBackend.h"
#include "AlsaVolumeControl.h"

DeviceInfo DeviceInfo::null;

DeviceInfo::DeviceInfo()
    : probed(false)
    , type(TYPE_UNKNOWN)
    , direction(UNKNOWN)
    , inMinChannels(0)
    , inMaxChannels(0)
    , outMinChannels(0)
    , outMaxChannels(0)
    , supportedFormats()
    , supportedRates()
    , anyRate(false)
    , minRate(0)
    , maxRate(~0)
    , mixer(0)
{
    memset(guid, 0, DEVICE_NAME_MAXLEN);
    memset(displayName, 0, DEVICE_NAME_MAXLEN);
    memset(productId, 0, DEVICE_NAME_MAXLEN);
    id = DEVICE_INVALID_ID;
}

DeviceInfo::~DeviceInfo()
{
    delete mixer;
}

void DeviceInfo::SetDevice(unsigned int inputId, const char *inputGuid, const char *inputName, const char *inputHwName)
{
    id = inputId;

    strncpy(guid, inputGuid, sizeof(guid));

    strncpy(displayName, inputName, sizeof(displayName));
    strncat(displayName, " (", sizeof(displayName)-strlen(displayName)-1);
    strncat(displayName, inputHwName, sizeof(displayName)-strlen(displayName)-1);
    strncat(displayName, ")", sizeof(displayName)-strlen(displayName)-1);
}

bool DeviceInfo::IsSampleRateSupported(int rate)
{
    if (anyRate)
        return (minRate <= rate && rate <= maxRate);
    else
        for (size_t i = 0; i < supportedRates.size(); ++i)
            if (supportedRates[i] == rate)
                return true;
    return false;
}

