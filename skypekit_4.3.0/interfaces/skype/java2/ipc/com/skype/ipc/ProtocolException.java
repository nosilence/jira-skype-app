package com.skype.ipc;

import java.io.IOException;

public class ProtocolException extends IOException {

	private static final long serialVersionUID = 1L;

	public ProtocolException(final String description) {
		super(description);
	}
}

