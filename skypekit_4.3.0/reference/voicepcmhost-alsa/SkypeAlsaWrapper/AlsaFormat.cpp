#include "AlsaFormat.h"

snd_pcm_format_t FormatToAlsa(Format format)
{
    switch (format)
    {
        case FORMAT_SINT8:
            return SND_PCM_FORMAT_S8;
        case FORMAT_SINT16:
            return SND_PCM_FORMAT_S16;
        case FORMAT_SINT24:
            return SND_PCM_FORMAT_S24;
        case FORMAT_SINT32:
            return SND_PCM_FORMAT_S32;
        case FORMAT_FLOAT32:
            return SND_PCM_FORMAT_FLOAT;
        case FORMAT_FLOAT64:
            return SND_PCM_FORMAT_FLOAT64;
        default:
            return SND_PCM_FORMAT_UNKNOWN;
    }
}

