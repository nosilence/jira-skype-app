#pragma once

#include <alsa/asoundlib.h>
#include "AlsaFormat.h"
#include "StreamSpec.h"
/*! RAII wrapper for ALSA hardware parameters. */
class AlsaHardwareParamsWrapper
{
	snd_pcm_hw_params_t* hwParams;

public:
	AlsaHardwareParamsWrapper();
	~AlsaHardwareParamsWrapper();

	inline operator snd_pcm_hw_params_t*() { return hwParams; }
};

/*! Manipulation class for ALSA hardware parameters. */
class AlsaHardwareParams
{
	snd_pcm_t* pcmHandle;
	AlsaHardwareParamsWrapper hwParams;

public:
	AlsaHardwareParams(snd_pcm_t* handle);

	int set_params(StreamSpec* ss, bool* useMmap);

	int set_format(Format* f);
	int set_period_size(snd_pcm_uframes_t size);
	int set_buffer_size(snd_pcm_uframes_t size);

	inline operator snd_pcm_hw_params_t*() { return hwParams; }
	
private:
    int SetParameterSuccess(StreamSpec* ss, StreamSpec lss, bool* use_mmap, bool lmmap, snd_pcm_uframes_t _period_size, snd_pcm_uframes_t _buffer_size);
};

/*! RAII wrapper for ALSA software parameters. */
class AlsaSoftwareParamsWrapper
{
	snd_pcm_sw_params_t* swParams;

public:
	AlsaSoftwareParamsWrapper();
	~AlsaSoftwareParamsWrapper();

	inline operator snd_pcm_sw_params_t*() { return swParams; }
};

/*! Manipulation class for ALSA software parameters. */
class AlsaSoftwareParams
{
	snd_pcm_t* pcmHandle;
	AlsaSoftwareParamsWrapper swParams;

public:
	AlsaSoftwareParams(snd_pcm_t* handle);
	
	int set_params(snd_pcm_uframes_t avail_min, bool period_event);

	inline operator snd_pcm_sw_params_t*() { return swParams; }
};
