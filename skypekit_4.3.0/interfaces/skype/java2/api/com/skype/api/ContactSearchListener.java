package com.skype.api;

import com.skype.api.ContactSearch;


public interface ContactSearchListener {
	/** This event gets called when there are changes to ContactSearch properties defined in ContactSearch.Property */
	public void onPropertyChange(ContactSearch object, ContactSearch.Property p, int value, String svalue);
	/** This callback is fired when a new matching contact has been found during the search.  * @param contact
	 * @param rankValue
	 */
	public void onNewResult(ContactSearch object, Contact contact, int rankValue);
}
