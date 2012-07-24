package com.skype.api;

import com.skype.api.Voicemail;


public interface VoicemailListener {
	/** This event gets called when there are changes to Voicemail properties defined in Voicemail.Property */
	public void onPropertyChange(Voicemail object, Voicemail.Property p, int value, String svalue);
}
