#include "AlsaDeviceDetection.h"
#include "Log.h"

/* For new detection */
int sc_errcheck(int retval, const char *doingwhat, int cardnr, int devnr)
{
    if (retval<0) {
        if (devnr>= 0) {
            Log("Error %s for card %d, device %d: %s.  Skipping.", doingwhat, cardnr, devnr, snd_strerror(retval));
        } else {
            Log("Error %s for card %d: %s.  Skipping.", doingwhat, cardnr, snd_strerror(retval));
        }
        return 1;
    }
    return 0;
}

int errcheck(int retval, const char *doingwhat)
{
    if (retval<0) {
        Log("Error %s: %s.  Skipping.", doingwhat, snd_strerror(retval));
        return 1;
    }
    return 0;
}

/* Disregard some standard plugins - for classic detection */
int IgnorePlugin(const char* plugin)
{
    static const char *ignored_plugins[] = { "default", "hw", "plughw", "plug", "dmix", "dsnoop", "tee", "front", "rear", "side", "center_lfe", "iec958", "spdif", "modem", "phoneline", "surround40", "surround41", "surround50", "surround51", "surround71", "file", "null", "shm", "cards", "rate_convert", NULL };
    int i = 0;
    while (ignored_plugins[i])
    {
        if (!strcmp(plugin, ignored_plugins[i]))
            return 1;
        ++i;
    }

    return 0;
}

bool PcmPreProbe(DeviceInfo& info, StreamDirection dir)
{
    int                    err;
    unsigned               min, max;
    snd_pcm_stream_t       stream = dir == INPUT ? SND_PCM_STREAM_CAPTURE : SND_PCM_STREAM_PLAYBACK;
    snd_pcm_t*             pcm    = NULL;
    snd_pcm_hw_params_t*   params;
    snd_pcm_format_mask_t* fmask;

    struct pair { snd_pcm_format_t alsa; Format fmt; } formats[] = {
        { SND_PCM_FORMAT_S8, FORMAT_SINT8 },
        { SND_PCM_FORMAT_S16, FORMAT_SINT16 },
        { SND_PCM_FORMAT_S24, FORMAT_SINT24 },
        { SND_PCM_FORMAT_S32, FORMAT_SINT32 },
        { SND_PCM_FORMAT_FLOAT, FORMAT_FLOAT32 },
        { SND_PCM_FORMAT_FLOAT64, FORMAT_FLOAT64 }
    };

    snd_pcm_hw_params_alloca(&params);
    snd_pcm_format_mask_alloca(&fmask);

    Log("   Trying to open pcm '%s'", info.guid);
    err = snd_pcm_open(&pcm, info.guid, stream, SND_PCM_NONBLOCK);

    if (errcheck(err, "opening sound device"))
        return false;

    err = snd_pcm_hw_params_any(pcm, params);

    if (errcheck(err, "obtaining hardware parameters"))
    {
        snd_pcm_close(pcm);
        return false;
    }

    snd_pcm_hw_params_get_channels_min(params, &min);
    snd_pcm_hw_params_get_channels_max(params, &max);

    if (min == max)
    {
        if (min == 1)  { Log("    1 channel"); }
        else           { Log("    %d channels", min); }
    }
    else                 Log("    %u..%u channels", min, max);

    if (dir == INPUT)
        info.inMaxChannels = max;
    else
        info.outMaxChannels = max;

    snd_pcm_hw_params_get_rate_min(params, &min, NULL);
    snd_pcm_hw_params_get_rate_max(params, &max, NULL);
    Log("    Sampling rate %u..%u Hz", min, max);
    snd_pcm_hw_params_get_format_mask(params, fmask);

    info.supportedFormats = 0;

    for (unsigned o = 0; o < sizeof(formats)/sizeof(formats[0]); o++)
        if (snd_pcm_format_mask_test(fmask, formats[o].alsa))
        {
            info.supportedFormats |= formats[o].fmt;
            Log("    Supported format: %s", StreamSpec::FormatName(formats[o].fmt));
        }

    snd_pcm_close(pcm);
    pcm = NULL;

    info.probed = true;
    info.direction |= (int)dir;
    return true;
}

/* Probe hardware device for INPUT or OUTPUT */
bool DevProbe(snd_ctl_t* handle, snd_pcm_info_t* pcminfo, int card, int dev, StreamDirection dir)
{
    Log("dev_probe %s\n", dir == INPUT ? "INPUT" : "OUTPUT");

    int nsubd, err;

    snd_pcm_info_set_device(pcminfo, dev);
    snd_pcm_info_set_subdevice(pcminfo, 0);
    snd_pcm_info_set_stream(pcminfo, dir == INPUT ? SND_PCM_STREAM_CAPTURE : SND_PCM_STREAM_PLAYBACK);

    err = snd_ctl_pcm_info(handle, pcminfo);
    if (err == -ENOENT) return false;

    if (sc_errcheck(err, "obtaining device info", card, dev))
        return false;

    nsubd = snd_pcm_info_get_subdevices_count(pcminfo);

    if (sc_errcheck(nsubd, "obtaining device info", card, dev))
        return false;

    Log("   Device %d, ID '%s', name '%s', %d subdevices (%d available)",
            dev, snd_pcm_info_get_id(pcminfo), snd_pcm_info_get_name(pcminfo),
            nsubd, snd_pcm_info_get_subdevices_avail(pcminfo));

    return true;
}

