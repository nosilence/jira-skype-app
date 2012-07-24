#pragma once

#include <memory.h>
#include <assert.h>
#include <algorithm>
#include "CompilerBarrier.h"
#include "Log.h"

/*!
 * Phantom ringbuffer provides a buffer access with a special 'shadow' area
 * to facilitate continuous reading/writing on wraparounds.
 */
class PhantomRingbuffer
{
public:
    PhantomRingbuffer(size_t frameBytes, size_t logicalFrames, size_t phantomFrames);
    ~PhantomRingbuffer();

    /*! 
     * Get pointer to read area of at most @param frames frames in size.
     * @param[out] frames maximum size of the readable area.
     * @returns pointer to start of readable area.
     */
    char* ReadBuf(size_t& frames);

    /*! 
     * Tell the buffer we have read that many frames
     */
    void Read(size_t frames);

    /*! 
     * Get pointer to write area of at most @param frames frames in size.
     * @param[out] frames maximum size of the writable area.
     * @returns pointer to start of writable area.
     */
    char* WriteBuf(size_t& frames);

    /*! 
     * Tell the buffer that we have written @param frames frames (will touch shadow area if needed).
     */
    void Written(size_t frames);

    // or

    /*! 
     * Write given number of frames from @param data to buffer.
     */
    size_t Write(const char* data, size_t frames);

    /*!
     * How many frames of the ringbuffer are written/readable.
     * Could be more than you can actually read in one run!
     */
    int Occupied() const;

    /*!
     * For debugging purposes. Check that logical/phantom sizes are consistent and shadow area data is consistent.
     */
    bool FulfillsInvariant() const;

private:
    /*!
     * @internal
     * Update the shadow or beginning area depending on where the write occured.
     */
    void Touch(size_t frames);

    /*!
     * @internal
     * Update the shadow area corresponding to beginning of the buffer.
     */
    void UpdatePhantom(unsigned pos, size_t frames);

    /*!
     * @internal
     * Update the beginning corresponding to shadow area.
     */
    void UpdateBeginning(unsigned pos, size_t frames);

private:
    size_t BufferSize() const {
        return (logicalFrames + phantomFrames) * frameBytes;
    }

    size_t frameBytes;                   //!< frame size in bytes to yield byte offsets
    size_t logicalFrames, phantomFrames; //!< buffer size in logical and phantom frames (total frames equals the sum)
    size_t readPos, writePos;            //!< read and write pos in frames
    char* buffer;
    size_t dstTail;
    size_t srcTail;
};

/* Completely inline implementation follows. */

inline PhantomRingbuffer::PhantomRingbuffer(size_t frameBytes_, size_t logicalFrames_, size_t phantomFrames_)
    : frameBytes(frameBytes_)
    , logicalFrames(logicalFrames_)
    , phantomFrames(phantomFrames_)
    , dstTail(0)
    , srcTail(0)
{
    buffer = new char [BufferSize()];
    if (!buffer)
    {
        frameBytes = logicalFrames = phantomFrames = 0;
    }
    readPos = writePos = 0;
}

inline PhantomRingbuffer::~PhantomRingbuffer()
{
    delete [] buffer;
}

inline int PhantomRingbuffer::Occupied() const
{
    const size_t writePosSnapshot = writePos;
    const size_t readPosSnapshot = readPos;
    COMPILER_BARRIER();
    if (writePosSnapshot >= readPosSnapshot)
        return writePosSnapshot - readPosSnapshot;
    return logicalFrames - readPosSnapshot + writePosSnapshot;
}

inline char* PhantomRingbuffer::ReadBuf(size_t& frames)
{
    const size_t writePosSnapshot = writePos;
    const size_t readPosSnapshot = readPos;
    COMPILER_BARRIER();
    if (readPosSnapshot > writePosSnapshot)
        frames = logicalFrames - readPosSnapshot + std::min(writePosSnapshot, phantomFrames);
    else
        frames = writePosSnapshot - readPosSnapshot;

    const size_t pos = readPosSnapshot * frameBytes;
    assert(pos < BufferSize());
    return &buffer[pos];
}

inline void PhantomRingbuffer::Read(size_t frames)
{
    size_t newReadPos = readPos + frames;
    if (newReadPos >= logicalFrames) {
        newReadPos -= logicalFrames;
    }
    COMPILER_BARRIER();
    readPos = newReadPos;
}

inline char* PhantomRingbuffer::WriteBuf(size_t& frames)
{
    const size_t writePosSnapshot = writePos;
    const size_t readPosSnapshot = readPos;
    COMPILER_BARRIER();
    if (writePosSnapshot < readPosSnapshot)
        frames = readPosSnapshot - writePosSnapshot;
    else
        frames = logicalFrames - writePosSnapshot + phantomFrames;

    const size_t pos = writePosSnapshot * frameBytes;
    assert(pos < BufferSize());
    Log("********************************************************************\n");
    Log("PhantomRingBuffer::WriteBuf %d: pBuffer=%p, pos=%d, BufferSize=%d\n", __LINE__, buffer, pos, BufferSize());
    Log("********************************************************************\n");
    return &buffer[pos];
}

inline void PhantomRingbuffer::Written(size_t frames)
{
    Touch(frames);

    size_t newWritePos = writePos + frames;
    if (newWritePos >= logicalFrames) {
        newWritePos -= logicalFrames;
    }
    COMPILER_BARRIER();
    writePos = newWritePos;
}

inline size_t PhantomRingbuffer::Write(const char* data, size_t frames)
{
    size_t availFrames;
    char* buf = WriteBuf(availFrames);
    assert(buf >= buffer);
    assert(buf < &buffer[BufferSize()]);
    
    frames = std::min(frames, availFrames);
    assert(buf + frames * frameBytes <= &buffer[BufferSize()]);
    
    memcpy(buf, data, frames * frameBytes);
    Written(frames);
    return frames;
}

inline void PhantomRingbuffer::Touch(size_t frames)
{
    unsigned touchedEnd = writePos + frames;

    // [............xxxx]xxxxX
    if (touchedEnd > logicalFrames)
    {
        int updateStart;
        // [............Xxxx]xxxxx
        if (writePos < logicalFrames)
            updateStart = 0;
        else
        // [..............]...Xxxxxx
            updateStart = writePos - logicalFrames;

        UpdateBeginning(updateStart, touchedEnd - logicalFrames - updateStart);
    }
    // [Xxxx..|........].....
    else if (writePos < phantomFrames)
    {
        int updateEnd;
        // [xxxxx|xxX......]....
        if (touchedEnd > phantomFrames)
            updateEnd = phantomFrames;
        else
        // [xxxX..|........].....
            updateEnd = touchedEnd;

        UpdatePhantom(writePos, updateEnd - writePos);
    }
}

inline void PhantomRingbuffer::UpdatePhantom(unsigned pos, size_t frames)
{
    if (frames && (pos + frames <= phantomFrames))
    {
        const size_t dst = (logicalFrames + pos) * frameBytes;
        const size_t src = pos * frameBytes;
        const size_t count = frames * frameBytes;

        dstTail = dst + count - 1;
        srcTail = src + count - 1;

        assert(dstTail < BufferSize());
        assert(srcTail < BufferSize());
        
        if (dst > src) 
        {
            assert(dst > srcTail);
        } 
        else 
        {
            assert(dstTail < src);
        }

        memcpy(&buffer[dst], &buffer[src], count);
    }
}

inline void PhantomRingbuffer::UpdateBeginning(unsigned pos, size_t frames)
{
    if (frames && (pos + frames <= phantomFrames))
    {
        const size_t dst = pos * frameBytes;
        const size_t src = (logicalFrames + pos) * frameBytes;
        const size_t count = frames * frameBytes;

        dstTail = dst + count - 1;
        srcTail = src + count - 1;

        assert(dstTail < BufferSize());
        assert(srcTail < BufferSize());

        if (dst > src) 
        {
            assert(dst > srcTail);
        } 
        else 
        {
            assert(dstTail < src);
        }

        memcpy(&buffer[dst], &buffer[src], count);
    }
}

inline bool PhantomRingbuffer::FulfillsInvariant() const
{
    if (phantomFrames > logicalFrames)
        return false;
    return memcmp(buffer, &buffer[logicalFrames * frameBytes], phantomFrames * frameBytes) == 0;
}

