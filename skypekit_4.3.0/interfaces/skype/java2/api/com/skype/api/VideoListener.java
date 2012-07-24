package com.skype.api;

import com.skype.api.Video;


public interface VideoListener {
	/** This event gets called when there are changes to Video properties defined in Video.Property */
	public void onPropertyChange(Video object, Video.Property p, int value, String svalue);
}
