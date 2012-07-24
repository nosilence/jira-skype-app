package com.skype.ipc;

import java.io.IOException;

public final class EventBuffer implements InputTransporting {
	private final int MIN_BUFFER_SIZE = 512;

	public EventBuffer(int listMaxNested, int listMaxElem) {
		mBuffer        = new byte[MIN_BUFFER_SIZE];
		mBegin         = 0;
		mEnd           = 0;
		mSize          = 0;
		mListMaxNested = listMaxNested;
		mListMaxElem   = listMaxElem;
		mListDepth     = 0;
	}

	public synchronized boolean isEmpty() {
//System.out.println("### EventBuffer size = "+mSize);
		return mSize == 0;
	}

	//
	// InputTransporting
	// 

	synchronized public void skipBytes(int numBytes) throws IOException {
		int n        = numBytes;
		int capacity = mBuffer.length;
		assert(n <= mSize);
		if ((mBegin+n) > capacity) {
			int len = capacity - mBegin;
			n      -= len;
			mBegin  = 0;
		}
		mBegin = (mBegin+n)&(capacity-1);
		mSize -= numBytes;
		// shrink?
		if (mSize < MIN_BUFFER_SIZE && capacity > MIN_BUFFER_SIZE)
			resize(MIN_BUFFER_SIZE);
//		return numBytes;
	}

	synchronized public void readBytes(byte [] dest, int offset, int numBytes) throws IOException {
		// one shall not read more than what is available as the payloads are fully buffered
		// before getting accessible 
		int n = numBytes;
		int capacity = mBuffer.length;
		assert(n <= mSize);
		if ((mBegin+n) > capacity) {
			int len = capacity - mBegin;
			System.arraycopy(mBuffer, mBegin, dest, offset, len);
			mSize  -= len;
			n      -= len;
			offset += len;
			mBegin  = 0;
		}
		System.arraycopy(mBuffer, mBegin, dest, offset, n);
		mBegin = (mBegin+n)&(capacity-1);
		mSize -= n;
		// shrink?
		if (mSize < MIN_BUFFER_SIZE && capacity > MIN_BUFFER_SIZE)
			resize(MIN_BUFFER_SIZE);
	}

	synchronized public int readByte() throws IOException {
		assert(mSize > 0);
		int capacity = mBuffer.length;
		int b = mBuffer[mBegin];
		mBegin = (mBegin+1)&(capacity-1);
		mSize--;
		if (mSize < MIN_BUFFER_SIZE && capacity > MIN_BUFFER_SIZE)
			resize(MIN_BUFFER_SIZE);
		return b;
	}

	public void readBytes(byte[] dest) throws IOException {
		readBytes(dest, 0, dest.length);
	}

	private void grow(int minSize) {
		int capacity = mBuffer.length+mBuffer.length;
		while (capacity < minSize) {
			capacity = capacity + capacity;
		}
		resize(capacity);
	}

	private void resize(int capacity) {
//System.out.println("### EventBuffer resize = "+capacity+" size "+mSize);
		byte[] newBuffer = new byte[capacity];
		if (mSize > 0) {
			if (mBegin < mEnd) {
				System.arraycopy(mBuffer, mBegin, newBuffer, 0, mSize);
			} else {
				System.arraycopy(mBuffer, mBegin, newBuffer, 0, mBuffer.length-mBegin);
				System.arraycopy(mBuffer, 0, newBuffer, mBuffer.length-mBegin, mEnd);
			}
		}
		mBegin  = 0;
		mEnd    = mSize;
		mBuffer = newBuffer;
	}

	private int putByte(int b) {
		if (mSize == mBuffer.length)
			grow(mBuffer.length+1);
		mBuffer[mEnd++] = (byte) b;
		mEnd &= mBuffer.length-1;
		mSize++;
		return b;
	}

	private void putBytes(int n, InputTransporting reader) throws IOException {
		if ((mSize+n) > mBuffer.length) 
			grow(mSize+n);
		if ((mEnd+n) > mBuffer.length) {
			int m = mBuffer.length - mEnd;
			reader.readBytes(mBuffer, mEnd, m);
			mEnd   = 0;
			mSize += m;
			n     -= m;
		}
		reader.readBytes(mBuffer, mEnd, n);
		mEnd  += n;
		if (mEnd == mBuffer.length) mEnd = 0;
		mSize += n;
	}

	private int bufferUint(InputTransporting transport) throws IOException {
		int shift = 0;
		int result = 0;
		while (true) {
			int value = putByte(transport.readByte()) & 0xFF;
			result = result | ((value & 0x7f) << shift);
			shift = shift + 7;
			if ((value & 0x80) == 0)
				break;
        	}
	        return result;
	}

	private long bufferUint64(InputTransporting transport) throws IOException {
		int shift = 0;
		long result = 0;
		while (true) {
			int value = putByte(transport.readByte()) & 0xFF;
			result = result | ((value & 0x7f) << shift);
			shift = shift + 7;
			if ((value & 0x80) == 0)
				break;
        	}
	        return result;
	}

	private void bufferValue(int kind, InputTransporting transport) throws IOException {
		switch (kind) {
		case 'i': case 'O': case 'u': case 'e': case 'b':
			bufferUint(transport);
			return;
		case 'T': case 'F': case 'N':
			return;
		case 'U':
			bufferUint64(transport);
			return;
		case 'S': case 'X': case 'f': case 'B':
			putBytes(bufferUint(transport), transport);
			return;
		case '[': {
			if (mListDepth++ > mListMaxNested) throw new ProtocolException("listDepth");
			int elemKind = putByte(transport.readByte());
			int numElem  = 0;
			while (elemKind != ']') {
				bufferValue(elemKind, transport);
				if (numElem++ > mListMaxElem) throw new ProtocolException("list too large");
			}
			mListDepth--;
			return;
		}
		default:
			throw new ProtocolException("unknown kind");
		}
	}

	public synchronized void bufferEvent(InputTransporting transport) throws IOException {
		putByte('Z');
		putByte('E');
		bufferUint(transport); // moduleId
		bufferUint(transport); // eventId
		do {
			int kind = putByte(transport.readByte());
			if (kind == 'z') return; 
			bufferUint(transport);
			bufferValue(kind, transport);
		} while (true);
	}

	public synchronized void bufferChange(InputTransporting transport) throws IOException {
		putByte('Z');
		putByte('C');
		bufferUint(transport); // moduleId
		bufferUint(transport); // oid
		boolean valueExpected = true;
		do {
			int sign = putByte(transport.readByte()); // next property?
			if (sign == ']') { // end of property
				if (valueExpected) {
					 throw new ProtocolException("expecting a value");
				}
				sign = putByte(transport.readByte());
				if (sign == ']') { // end objects
					sign = putByte(transport.readByte());
					if (sign == ']') {
						sign = putByte(transport.readByte());
						if (sign != 'z') throw new ProtocolException("change shall terminate");
						return;
					} else if (sign == ',') { // next module
						bufferUint(transport); // moduleId
						bufferUint(transport); // oid
						valueExpected = true;
					} else {
						throw new ProtocolException("");
					}
				} else if (sign == ',') { // next object
					bufferUint(transport); // oid
					valueExpected = true;
				} else {
					throw new ProtocolException("");
				}
			} else { // next property
				bufferUint(transport);
				bufferValue(sign, transport);
				valueExpected = false;
			}
		} while (true);
	}

	public void close() throws IOException {
		mSize = 0;
	}

	byte[] mBuffer;
	int    mBegin;
	int    mEnd;
	int    mSize;
	int    mListDepth;
	int    mListMaxNested;
	int    mListMaxElem;
}

