#pragma once

#include <alsa/asoundlib.h>

/*!
 * Supported sample formats.
 */
enum Format {
    FORMAT_UNKNOWN = 0x00,
    FORMAT_SINT8   = 0x01,
    FORMAT_SINT16  = 0x02,
    FORMAT_SINT24  = 0x04,
    FORMAT_SINT32  = 0x08,
    FORMAT_FLOAT32 = 0x10,
    FORMAT_FLOAT64 = 0x20
};

/*!
 * Type holding a set of format flags.
 */
typedef int Formats; 

snd_pcm_format_t FormatToAlsa(Format format);
