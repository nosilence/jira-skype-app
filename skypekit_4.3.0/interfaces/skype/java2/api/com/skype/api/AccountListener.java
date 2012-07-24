package com.skype.api;

import com.skype.api.Account;


public interface AccountListener {
	/** This event gets called when there are changes to Account properties defined in Account.Property */
	public void onPropertyChange(Account object, Account.Property p, int value, String svalue);
}
