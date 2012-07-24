package com.skype.ipc;

import java.io.IOException;

public interface InputTransporting {
	public void skipBytes(int numBytes) throws IOException; 
	public void readBytes(byte [] dest) throws IOException;
	public void readBytes(byte [] dest, int offset, int numBytes) throws IOException;
	public int  readByte() throws IOException;
	public void close() throws IOException;
}

