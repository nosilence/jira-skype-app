package com.skype.api;

import com.skype.api.Transfer;


public interface TransferListener {
	/** This event gets called when there are changes to Transfer properties defined in Transfer.Property */
	public void onPropertyChange(Transfer object, Transfer.Property p, int value, String svalue);
}
