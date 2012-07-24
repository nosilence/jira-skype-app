package com.skype.api;

import com.skype.api.Contact;


public interface ContactListener {
	/** This event gets called when there are changes to Contact properties defined in Contact.Property */
	public void onPropertyChange(Contact object, Contact.Property p, int value, String svalue);
}
