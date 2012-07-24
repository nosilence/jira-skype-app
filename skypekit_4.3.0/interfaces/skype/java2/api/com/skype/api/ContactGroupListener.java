package com.skype.api;

import com.skype.api.ContactGroup;


public interface ContactGroupListener {
	/** This event gets called when there are changes to ContactGroup properties defined in ContactGroup.Property */
	public void onPropertyChange(ContactGroup object, ContactGroup.Property p, int value, String svalue);
	/** conversation added or removed from this group * @param conversation
	 */
	public void onChangeConversation(ContactGroup object, Conversation conversation);
	/**
	 * A contact has been added or removed to this ContactGroup. 
	 * NB! On rare occasions, the ContectRef argument to this callback can be NULL. You should always check whether the reference is valid, before accessing methods or properties. 
	 * @param contact
	 */
	public void onChange(ContactGroup object, Contact contact);
}
