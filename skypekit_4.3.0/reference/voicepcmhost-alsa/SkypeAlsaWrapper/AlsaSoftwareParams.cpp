#include <alsa/asoundlib.h>
#include "AlsaParams.h"
#include "Log.h"

AlsaSoftwareParamsWrapper::AlsaSoftwareParamsWrapper()
{
	snd_pcm_sw_params_malloc(&swParams);//return 0 on success FIXME	
}

AlsaSoftwareParamsWrapper::~AlsaSoftwareParamsWrapper()
{
	snd_pcm_sw_params_free(swParams);	
}

AlsaSoftwareParams::AlsaSoftwareParams(snd_pcm_t* handle)
	: pcmHandle(handle)
{
}

int AlsaSoftwareParams::set_params(snd_pcm_uframes_t avail_min, bool period_event)
{
    snd_pcm_uframes_t boundary;
    int err;

    if ((err = snd_pcm_sw_params_current(pcmHandle, swParams) < 0)) {
        Log("Unable to determine current swparams: %s", snd_strerror(err));
        return err;
    }

    if ((err = snd_pcm_sw_params_get_boundary(swParams, &boundary)) < 0) {
        Log("Unable to get boundary: %s", snd_strerror(err));
        return err;
    }

	/* Stop device if more than boundary frames in buffer */
    if ((err = snd_pcm_sw_params_set_stop_threshold(pcmHandle, swParams, boundary)) < 0) {
        Log("Unable to set stop threshold: %s", snd_strerror(err));
        return err;
    }

	/* Disable automatic start */
    if ((err = snd_pcm_sw_params_set_start_threshold(pcmHandle, swParams, (snd_pcm_uframes_t) -1)) < 0) {
        Log("Unable to set start threshold: %s", snd_strerror(err));
        return err;
    }

    if ((err = snd_pcm_sw_params_set_avail_min(pcmHandle, swParams, avail_min)) < 0) {
        Log("snd_pcm_sw_params_set_avail_min() failed: %s", snd_strerror(err));
        return err;
    }

    if ((err = snd_pcm_sw_params(pcmHandle, swParams)) < 0) {
        Log("Unable to set sw params: %s", snd_strerror(err));
        return err;
    }

    return 0;
}
