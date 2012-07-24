package com.skype.ipc;

import java.io.IOException;

public interface OutputTransporting {
	void writeBytes(final byte [] src) throws IOException;
	void writeBytesAndFlush(final byte [] src) throws IOException;
	void writeByte(final int value) throws IOException;
	void writeByteAndFlush(final int value) throws IOException;
	void close() throws IOException;
}

