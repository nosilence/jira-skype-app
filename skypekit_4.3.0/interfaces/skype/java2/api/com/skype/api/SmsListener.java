package com.skype.api;

import com.skype.api.Sms;


public interface SmsListener {
	/** This event gets called when there are changes to Sms properties defined in Sms.Property */
	public void onPropertyChange(Sms object, Sms.Property p, int value, String svalue);
}
