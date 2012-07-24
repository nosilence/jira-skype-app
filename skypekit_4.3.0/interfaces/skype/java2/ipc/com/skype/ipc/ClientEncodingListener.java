package com.skype.ipc;

import java.io.IOException;

public interface ClientEncodingListener {
	Decoding sidOnRequestEncoded(int requestId) throws IOException;
	Decoding sidOnOneWayRequestEncoded(int requestId) throws IOException;
	Decoding sidOnGetRequestEncoded() throws IOException;
}


