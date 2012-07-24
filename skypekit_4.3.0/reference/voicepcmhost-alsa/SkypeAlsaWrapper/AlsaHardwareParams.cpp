#include <alsa/asoundlib.h>
#include <stdint.h>
#include "AlsaParams.h"
#include "Log.h"

#define MSEC_PER_SEC 1000

AlsaHardwareParamsWrapper::AlsaHardwareParamsWrapper()
{
	snd_pcm_hw_params_malloc(&hwParams);//return 0 on success FIXME	
}

AlsaHardwareParamsWrapper::~AlsaHardwareParamsWrapper()
{
	snd_pcm_hw_params_free(hwParams);	
}

AlsaHardwareParams::AlsaHardwareParams(snd_pcm_t* handle)
	: pcmHandle(handle)
{
}

int AlsaHardwareParams::set_params(StreamSpec* ss, bool* use_mmap)
{
	AlsaHardwareParamsWrapper hwParamsCopy;
	StreamSpec lss = *ss;
    snd_pcm_uframes_t _period_size = lss.fragmentFrames;
    snd_pcm_uframes_t _buffer_size = lss.bufferFrames;
	bool lmmap = use_mmap ? *use_mmap : true;
	int ret;

	if ((ret = snd_pcm_hw_params_any(pcmHandle, hwParams)) < 0) {
        Log("snd_pcm_hw_params_any() failed: %s", snd_strerror(ret));
        return ret;
    }

    if ((ret = snd_pcm_hw_params_set_rate_resample(pcmHandle, hwParams, 0)) < 0) {
        Log("snd_pcm_hw_params_set_rate_resample() failed: %s", snd_strerror(ret));
        return ret;
    }

    if (lmmap) {

        if (snd_pcm_hw_params_set_access(pcmHandle, hwParams, SND_PCM_ACCESS_MMAP_INTERLEAVED) < 0) {

            /* mmap() didn't work, fall back to interleaved */

            if ((ret = snd_pcm_hw_params_set_access(pcmHandle, hwParams, SND_PCM_ACCESS_RW_INTERLEAVED)) < 0) {
                Log("snd_pcm_hw_params_set_access() failed: %s", snd_strerror(ret));
                return ret;
            }

            lmmap = false;
        }

    } else if ((ret = snd_pcm_hw_params_set_access(pcmHandle, hwParams, SND_PCM_ACCESS_RW_INTERLEAVED)) < 0) {
        Log("snd_pcm_hw_params_set_access() failed: %s", snd_strerror(ret));
        return ret;
    }

    if ((ret = set_format(&lss.format)) < 0)
        return ret;

    if ((ret = snd_pcm_hw_params_set_rate_near(pcmHandle, hwParams, &lss.rate, NULL)) < 0) {
        Log("snd_pcm_hw_params_set_rate_near() failed: %s", snd_strerror(ret));
        return ret;
    }

    /* We ignore very small sampling rate deviations */
    if (lss.rate >= ss->rate*.95 && lss.rate <= ss->rate*1.05)
        lss.rate = ss->rate;

	{
		unsigned int c = lss.channels;

		if ((ret = snd_pcm_hw_params_set_channels_near(pcmHandle, hwParams, &c)) < 0) {
		    Log("snd_pcm_hw_params_set_channels_near(%u) failed: %s", lss.channels, snd_strerror(ret));
            return ret;
		}

		lss.channels = c;
	}

	_period_size = (snd_pcm_uframes_t) (((uint64_t) _period_size * lss.rate) / ss->rate);
	_buffer_size = (snd_pcm_uframes_t) (((uint64_t) _buffer_size * lss.rate) / ss->rate);

    if (_buffer_size > 0 || _period_size > 0) {
        snd_pcm_uframes_t max_frames = 0;

		if ((ret = snd_pcm_hw_params_get_buffer_size_max(hwParams, &max_frames)) < 0)
        {
		    Log("snd_pcm_hw_params_get_buffer_size_max() failed: %s", snd_strerror(ret));
        }
		else
        {
		    Log("Maximum hw buffer size is %lu ms", (long unsigned) (max_frames * MSEC_PER_SEC / lss.rate));
        }

        /*
         * Some ALSA drivers really don't like if we set the buffer size first and the number of periods second
         * (which would make a lot more sense to me). So, try a few combinations before we give up. 
         */
        if (_buffer_size > 0 && _period_size > 0) {
            snd_pcm_hw_params_copy(hwParamsCopy, hwParams);

            /* First try: set buffer size first, followed by period size */
            if (set_buffer_size(_buffer_size) >= 0 &&
                set_period_size(_period_size) >= 0 &&
                snd_pcm_hw_params(pcmHandle, hwParams) >= 0) {
                Log("Set buffer size first (to %lu samples), period size second (to %lu samples).", (unsigned long) _buffer_size, (unsigned long) _period_size);
                return SetParameterSuccess(ss, lss, use_mmap, lmmap, _period_size, _buffer_size);
            }

            snd_pcm_hw_params_copy(hwParams, hwParamsCopy);

            /* Second try: set period size first, followed by buffer size */
            if (set_period_size(_period_size) >= 0 &&
                set_buffer_size(_buffer_size) >= 0 &&
                snd_pcm_hw_params(pcmHandle, hwParams) >= 0) {
                Log("Set period size first (to %lu samples), buffer size second (to %lu samples).", (unsigned long) _period_size, (unsigned long) _buffer_size);
                return SetParameterSuccess(ss, lss, use_mmap, lmmap, _period_size, _buffer_size);
            }

            snd_pcm_hw_params_copy(hwParams, hwParamsCopy);
        }

        if (_buffer_size > 0) {
            snd_pcm_hw_params_copy(hwParamsCopy, hwParams);

            /* Third try: set only buffer size */
            if (set_buffer_size(_buffer_size) >= 0 &&
                snd_pcm_hw_params(pcmHandle, hwParams) >= 0) {
                Log("Set only buffer size (to %lu samples).", (unsigned long) _buffer_size);
                return SetParameterSuccess(ss, lss, use_mmap, lmmap, _period_size, _buffer_size);
            }

            snd_pcm_hw_params_copy(hwParams, hwParamsCopy);
        }

        if (_period_size > 0) {
            snd_pcm_hw_params_copy(hwParamsCopy, hwParams);

            /* Fourth try: set only period size */
            if (set_period_size(_period_size) >= 0 &&
                snd_pcm_hw_params(pcmHandle, hwParams) >= 0) {
                Log("Set only period size (to %lu samples).", (unsigned long) _period_size);
                return SetParameterSuccess(ss, lss, use_mmap, lmmap, _period_size, _buffer_size);
            }

            snd_pcm_hw_params_copy(hwParams, hwParamsCopy);
        }
    }

    Log("Set neither period nor buffer size.");

    /* Last chance, set nothing */
    if  ((ret = snd_pcm_hw_params(pcmHandle, hwParams)) < 0) {
        Log("snd_pcm_hw_params failed: %s", snd_strerror(ret));
        return ret;
    }

    return SetParameterSuccess(ss, lss, use_mmap, lmmap, _period_size, _buffer_size);
}

int AlsaHardwareParams::SetParameterSuccess(StreamSpec* ss, StreamSpec lss, bool* use_mmap, bool lmmap, snd_pcm_uframes_t _period_size, snd_pcm_uframes_t _buffer_size)
{
    int ret = 0;
    
    if (ss->rate != lss.rate)
        Log("Device %s doesn't support %u Hz, changed to %u Hz.", snd_pcm_name(pcmHandle), ss->rate, lss.rate);

    if (ss->channels != lss.channels)
        Log("Device %s doesn't support %u channels, changed to %u.", snd_pcm_name(pcmHandle), ss->channels, lss.channels);

    if (ss->format != lss.format)
        Log("Device %s doesn't support sample format %s, changed to %s.", snd_pcm_name(pcmHandle), ss->FormatName(), ss->FormatName(lss.format));

    if ((ret = snd_pcm_prepare(pcmHandle)) < 0) {
        Log("snd_pcm_prepare() failed: %s\n", snd_strerror(ret));
        return ret;
    }

    if ((ret = snd_pcm_hw_params_current(pcmHandle, hwParams)) < 0) {
        Log("snd_pcm_hw_params_current() failed: %s", snd_strerror(ret));
        return ret;
    }

	int dir;
    if ((ret = snd_pcm_hw_params_get_period_size(hwParams, &_period_size, &dir)) < 0 ||
        (ret = snd_pcm_hw_params_get_buffer_size(hwParams, &_buffer_size)) < 0) {
        Log("snd_pcm_hw_params_get_{period|buffer}_size() failed: %s", snd_strerror(ret));
        return ret;
    }

    ss->rate = lss.rate;
    ss->channels = lss.channels;
    ss->format = lss.format;

	ss->bufferFrames = _buffer_size;
	ss->fragmentFrames = _period_size;

    if (use_mmap)
        *use_mmap = lmmap;

    ret = 0;

    snd_pcm_nonblock(pcmHandle, 1);

    return ret;
}

int AlsaHardwareParams::set_format(Format* f)
{
	int ret;
	
	if ((ret = snd_pcm_hw_params_set_format(pcmHandle, hwParams, FormatToAlsa(*f))) >= 0)
        return ret;

    Log("snd_pcm_hw_params_set_format(%s) failed: %s",
                 snd_pcm_format_description(FormatToAlsa(*f)),
                 snd_strerror(ret));

	static Format try_formats[] = {
		FORMAT_FLOAT32,
		FORMAT_SINT32,
		FORMAT_SINT16,
		FORMAT_SINT24,
		FORMAT_FLOAT64,
		FORMAT_SINT8
	};

    for (unsigned i = 0; i < sizeof(try_formats)/sizeof(try_formats[0]); i++) {
        *f = try_formats[i];

        if ((ret = snd_pcm_hw_params_set_format(pcmHandle, hwParams, FormatToAlsa(*f))) >= 0)
            return ret;

        Log("snd_pcm_hw_params_set_format(%s) failed: %s",
                     snd_pcm_format_description(FormatToAlsa(*f)),
                     snd_strerror(ret));
    }
	
    return -1;
}

int AlsaHardwareParams::set_period_size(snd_pcm_uframes_t size)
{
    snd_pcm_uframes_t s = size;
    int d = 0;
    if (snd_pcm_hw_params_set_period_size_near(pcmHandle, hwParams, &s, &d) < 0) {
        s = size;
        d = -1;
        if (snd_pcm_hw_params_set_period_size_near(pcmHandle, hwParams, &s, &d) < 0) {
            s = size;
            d = 1;
			int ret;
            if ((ret = snd_pcm_hw_params_set_period_size_near(pcmHandle, hwParams, &s, &d)) < 0) {
                Log("snd_pcm_hw_params_set_period_size_near() failed: %s", snd_strerror(ret));
                return ret;
            }
        }
    }

    return 0;
}

int AlsaHardwareParams::set_buffer_size(snd_pcm_uframes_t size)
{
	int ret;

    if ((ret = snd_pcm_hw_params_set_buffer_size_near(pcmHandle, hwParams, &size)) < 0) {
        Log("snd_pcm_hw_params_set_buffer_size_near() failed: %s", snd_strerror(ret));
        return ret;
    }

    return 0;
}

