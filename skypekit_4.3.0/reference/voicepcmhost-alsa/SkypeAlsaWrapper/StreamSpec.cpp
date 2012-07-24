//======================================================================================================================
// StreamSpec
//======================================================================================================================

#include "StreamSpec.h"

/*! Compute latency for given buffer frames and data rate */
double StreamSpec::FramesToMs(unsigned frames, unsigned rate)
{
    double lat = 0.0;
    if (rate > 0)
        lat = (double)(1000.0 * frames) / (double)rate;
    return lat;
}

unsigned StreamSpec::FormatBytes(Format format)
{
    switch (format)
	{
        case FORMAT_SINT8:
            return 1;
        case FORMAT_SINT16:
            return 2;
        case FORMAT_SINT24:
        case FORMAT_SINT32:
        case FORMAT_FLOAT32:
            return 4;
        case FORMAT_FLOAT64:
            return 8;
        case FORMAT_UNKNOWN:
        default:
            return 0;
    }
}

const char* StreamSpec::FormatName(Format format)
{
    switch (format)
	{
        case FORMAT_SINT8:
            return "sint8";
        case FORMAT_SINT16:
            return "sint16";
        case FORMAT_SINT24:
            return "sint24";
        case FORMAT_SINT32:
            return "sint32";
        case FORMAT_FLOAT32:
            return "float32";
        case FORMAT_FLOAT64:
            return "float64";
        case FORMAT_UNKNOWN:
        default:
            return "unknown";
    }
}

//
// example output: "196000Hz float64 2ch+0 4096/2048"
//                  |           |      |       |
//                  sample rate |      |       |
//                            format   |       |
//                             channels+offset |
//                                  buffer size/fragment size in frames
//
void StreamSpec::PrintStreamSpec(char *buf, size_t bufsize) const
{
    snprintf(buf, bufsize,
    "%uHz %s %dch+%d %d/%d",
    rate, FormatName(), channels, firstChannel, bufferFrames, fragmentFrames);
}

//======================================================================================================================
