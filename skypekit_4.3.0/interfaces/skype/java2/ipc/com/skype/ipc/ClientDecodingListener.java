package com.skype.ipc;


public interface ClientDecodingListener {
	void    sidOnMessageDecoded();
	void    sidOnEventBuffered();
	boolean sidWantRead();
}

