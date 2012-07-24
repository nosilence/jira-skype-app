package com.skype.api;

import com.skype.api.Message;


public interface MessageListener {
	/** This event gets called when there are changes to Message properties defined in Message.Property */
	public void onPropertyChange(Message object, Message.Property p, int value, String svalue);
}
