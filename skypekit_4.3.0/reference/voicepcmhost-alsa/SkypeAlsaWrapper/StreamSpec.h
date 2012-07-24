#pragma once

/*!
 * Stream specifications.
 * Describes an audio I/O stream characteristics.
 */
 #include "AlsaFormat.h"
 
class StreamSpec
{
public:
    unsigned      rate;             //!< Sample rate.
    Format        format;           //!< Sample data format.
    unsigned      channels;         //!< Number of channels.
    unsigned      firstChannel;     //!< Channel offset for multichannel configurations.
	unsigned      fragmentFrames;   //!< Fragment is minimum chunk transferred between sound device and baudio. Determines latency.
    unsigned      bufferFrames;     //!< Size of hw buffer in frames.

    enum { STREAM_SPEC_TEXT_MAX = 64 };

public:
    StreamSpec();
    StreamSpec(unsigned rate, Format fmt = FORMAT_SINT16, unsigned ch = 2);

    static inline unsigned FrameBytes(Format fmt, unsigned channels) { return FormatBytes(fmt) * channels; }
    inline unsigned FrameBytes() const { return FrameBytes(format, channels); }

    static unsigned FormatBytes(Format fmt);
    inline unsigned FormatBytes() const { return FormatBytes(format); }

	static double FramesToMs(unsigned frames, unsigned rate);
	inline double FramesToMs(unsigned frames) const { return FramesToMs(frames, rate); }
	inline double FragmentMs()                const { return FramesToMs(fragmentFrames, rate); }
	inline double BufferMs()                  const { return FramesToMs(bufferFrames, rate); }
	
	inline unsigned BytesPerSecond()          const { return rate * FrameBytes(); }

    static const char* FormatName(Format fmt);
    inline const char* FormatName() const { return FormatName(format); }

    /*!
     * Print stream specification into user-provided buffer.
     * Will print at most STREAM_SPEC_TEXT_MAX characters
     * including terminating null.
     */
    void PrintStreamSpec(char *buf, size_t bufsize) const;
};

inline StreamSpec::StreamSpec()
    : rate(48000)
    , format(FORMAT_SINT16)
    , channels(2)
    , firstChannel(0)
    , fragmentFrames(480) // 10ms at 48Khz
    , bufferFrames(4800) // 100ms at 48KHz
{
}

inline StreamSpec::StreamSpec(unsigned rate_, Format format_, unsigned channels_)
    : rate(rate_)
    , format(format_)
    , channels(channels_)
    , firstChannel(0)
    , fragmentFrames(rate_/100)
    , bufferFrames(rate_/10)
{
}
