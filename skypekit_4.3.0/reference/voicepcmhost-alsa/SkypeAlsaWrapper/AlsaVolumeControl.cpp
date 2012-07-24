#include "AlsaWrappers.h"
#include "AlsaVolumeControl.h"
#include "Log.h"
#include <math.h>

#define ERROR_EXIT(func, txt, exitcode) do \
{ \
    int result = func; \
    Log("called "#func" result %s / %d", snd_strerror(result), result); \
    if (result < 0) { \
        Log(txt " failed: %s", snd_strerror(result)); \
        ret = exitcode; \
        Log("GetSetVolume(ctl %s, elem %s, min %d, max %d, step %d, outvol %d, newvol %d)", ctlDevice, elemName, (min ? *min : -1), \
        (max ? *max : -1), (step ? *step : -1), (outVol ? *outVol : -1), (newVol ? *newVol : -1)); \
        return ret; \
    } \
} while(0)

AlsaVolumeControl::~AlsaVolumeControl()
{
    if (ctlDevice)
        free(ctlDevice);
    if (micVolume)
        free(micVolume);
    if (micBoost)
        free(micBoost);
    if (micSwitch)
        free(micSwitch);
}

double AlsaVolumeControl::GetVolume(AudioStream* /*s*/)
{
    int min, max;
    int outVol;

    GetSetVolume(ctlDevice, micVolume, &min, &max, NULL, &outVol, NULL);
    double vol = (outVol - min) / (double)(max - min);

    Log("GetVolume: %d => %f", outVol, vol);
    return vol;
}

void AlsaVolumeControl::SetVolume(AudioStream* /*s*/, double vol)
{
    int min, max;//, step;
    int newVol;

    GetSetVolume(ctlDevice, micVolume, &min, &max, NULL/*&step*/, NULL, NULL);
    newVol = ceil(vol * (max - min) + min);
    Log("SetVolume: %f => %d", vol, newVol);
    GetSetVolume(ctlDevice, micVolume, NULL, NULL, NULL, NULL, &newVol);
}

int AlsaVolumeControl::GetSetVolume(const char* ctlDevice, const char* elemName, int* min, int* max, int* step, int* outVol, int* newVol)
{
    int ret = 0;
    int valueCount = 0;
    SndHctl hctl;
    snd_hctl_elem_t *control = NULL;
    SndCtlElemId elemid;
    SndCtlElemValue elemvalue;
    SndCtlElemInfo eleminfo;

    if(!elemName)
        return -1;

    ERROR_EXIT(hctl.open(ctlDevice), "snd_hctl_open", -1);
    ERROR_EXIT(hctl.load(), "snd_hctl_load", -1);

    elemid.setName(elemName);
    elemid.setInterface(SND_CTL_ELEM_IFACE_MIXER);
    control = snd_hctl_find_elem(hctl, elemid);

    if(!control)
    {
        Log("Could not find mixer control %s", elemName);
        return -1;
    }

    ERROR_EXIT(snd_hctl_elem_info(control, eleminfo), "snd_hctl_elem_info", -1);
    valueCount = eleminfo.count();

    if (valueCount <= 0)
    {
        Log("No adjustable values in control %s", elemName);
        return -1;
    }

    if (min || max || step)
    {
        if (!eleminfo.isReadable())
        {
            Log("snd_ctl_elem_info_is_readable returned false; cannot return info");
            return -1;
        }

        if (min)
            *min = eleminfo.min();

        if (max)
            *max = eleminfo.max();

        if (step)
            *step = eleminfo.step();
    }

    if (outVol)
    {
        ERROR_EXIT(snd_hctl_elem_read(control, elemvalue), "snd_hctl_elem_read", -1);

        int vol = 0;
        for(int i = 0; i < valueCount; i++)
            vol += snd_ctl_elem_value_get_integer(elemvalue, i);
        vol /= valueCount;
        *outVol = vol;
    }

    if (newVol)
    {
        ERROR_EXIT(snd_hctl_elem_read(control, elemvalue), "snd_hctl_elem_read", -1);

        for(int i = 0; i < valueCount; i++)
            snd_ctl_elem_value_set_integer(elemvalue, i, *newVol);

        ERROR_EXIT(snd_hctl_elem_write(control, elemvalue), "snd_hctl_elem_write", -1);
    }

    Log("GetSetVolume(ctl %s, elem %s, min %d, max %d, step %d, outvol %d, newvol %d)", ctlDevice, elemName, (min ? *min : -1), (max ? *max : -1), (step ? *step : -1), (outVol ? *outVol : -1), (newVol ? *newVol : -1));
    return ret;
}

#undef ERROR_EXIT

void AlsaVolumeControl::ProbeMixer(DeviceInfo* device)
{
    SndMixer mixer;

    SndHctl hctl;
    SndHctlElem hctl_elem;
    int element_count;
    const char *qs2;
    int mic_prio=-1;
    int boost_prio=-1;
    int switch_prio=-1;

    const int PRIO_ELEMENT_MIC_CAPTURE = 4;
    const int PRIO_ELEMENT_MIC         = 3;
    const int PRIO_ELEMENT_CAPTURE     = 2;
    const int PRIO_ELEMENT_PCM_CAPTURE = 1;

    if (ctlDevice)
        free(ctlDevice);
    ctlDevice = strdup(device->guid);

    // will fail for hw:0,0 for example (mixer is on hw:0), so remove all after ','
    char *tok = strchr(ctlDevice, ',');
    if (tok && *tok == ',')
        *tok = 0;

    // Open ctl/hctl
    if (!hctl.open(ctlDevice)) {
        Log("Could not open HCTL for device '%s'.", ctlDevice);
        return; // No mixer for this device
    }
    if (!hctl.load()) {
        Log("Could not load parameters into HCTL.");
        return; // No mixer for this device
    }
    element_count = hctl.elementCount();

    // enumerate mixer elements
    hctl_elem.first(hctl);
    for (int l1 = 0; l1 < element_count; l1++, hctl_elem.next())
    {
        int new_prio = -1;
        snd_ctl_elem_type_t elem_type;
        SndCtlElemInfo eleminfo;
        if (snd_hctl_elem_info(hctl_elem, eleminfo) < 0)
        {
            Log("Could not get info on element.");
            continue;
        }
        elem_type = eleminfo.type();
        qs2 = eleminfo.name();
        if((elem_type == SND_CTL_ELEM_TYPE_BOOLEAN)
        || (elem_type == SND_CTL_ELEM_TYPE_INTEGER))
        {
            // Find for Mic control - select the most likely one depending on name
            if (strstr(qs2, "Mic") && strstr(qs2, SND_CTL_NAME_CAPTURE)) {
                Log("Mic Capture element found: %s\n", qs2);
                new_prio=PRIO_ELEMENT_MIC_CAPTURE;
            } else if (strstr(qs2, "Mic") && !strstr(qs2, SND_CTL_NAME_PLAYBACK)) {
                Log("Mic Capture element found: %s\n", qs2);
                new_prio=PRIO_ELEMENT_MIC;
            } else if (strncmp(qs2, SND_CTL_NAME_CAPTURE, strlen(SND_CTL_NAME_CAPTURE))==0) {
                Log("Capture element found: %s\n", qs2);
                new_prio=PRIO_ELEMENT_CAPTURE;
            } else if (strstr(qs2, "PCM") && strstr(qs2, SND_CTL_NAME_CAPTURE)) {
                Log("Mic Capture Volume element found: %s\n", qs2);
                new_prio=PRIO_ELEMENT_PCM_CAPTURE;
            }

            // Check that it is relevant control for us (Volume, boost or Select)
            if ((new_prio>mic_prio) && strstr(qs2, SND_CTL_NAME_IEC958_VOLUME)) {
                mic_prio = new_prio;
                free(micVolume);
                micVolume = strdup(qs2);
            } else if ((new_prio>boost_prio) && strstr(qs2, "Boost")) {
                boost_prio = new_prio;
                free(micBoost);
                micBoost = strdup(qs2);
            } else if ((new_prio>switch_prio) && strstr(qs2, "Switch")) {
                switch_prio = new_prio;
                free(micSwitch);
                micSwitch = strdup(qs2);
            }
        }
    }

    Log("AlsaVolumeControl selected: mic '%s' boost '%s' switch '%s'", micVolume, micBoost, micSwitch);
}

