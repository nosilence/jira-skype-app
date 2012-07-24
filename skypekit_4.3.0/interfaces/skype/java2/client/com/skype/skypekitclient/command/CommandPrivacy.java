/**
 * Copyright (C) 2010, Skype Limited
 *
 * All intellectual property rights, including but not limited to copyrights,
 * trademarks and patents, as well as know how and trade secrets contained in,
 * relating to, or arising from the internet telephony software of
 * Skype Limited (including its affiliates, "Skype"), including without
 * limitation this source code, Skype API and related material of such
 * software proprietary to Skype and/or its licensors ("IP Rights") are and
 * shall remain the exclusive property of Skype and/or its licensors.
 * The recipient hereby acknowledges and agrees that any unauthorized use of
 * the IP Rights is a violation of intellectual property laws.
 *
 * Skype reserves all rights and may take legal action against infringers of
 * IP Rights.
 *
 * The recipient agrees not to remove, obscure, make illegible or alter any
 * notices or indications of the IP Rights and/or Skype's rights and
 * ownership thereof.
 */

package com.skype.skypekitclient.command;

import com.skype.api.Account;
import com.skype.skypekitclient.SkypekitClient;
import com.skype.skypekitclient.utils.Ask;

/**
 *
 */
public class CommandPrivacy extends ClientCommandInterface {
//    private static final String TAG = "CommandPrivacy";

    private SkypekitClient skClient;

	protected CommandPrivacy(SkypekitClient skypekitClient) {
		super();
		skClient = skypekitClient;
	}
    
	@Override
	public String getName() { return "Privacy"; }

	public void executeHelp()
	{
        skClient.out("\n[Privacy] - p\n" +
                "	pa - change avatar policy\n" +
                "	pb - change contact count policy\n" +
                "	pc - change chat policy\n" +
                "	pi - change skypein policy\n" +
                "	po - change skypeout policy\n" +
                "	pz - change timezone policy\n" +
                "	pv - change voicemail policy\n" +
                "	pw - change web presence policy\n"
        );
	}

	public void execute_a() // change avatar policy
	{
        if (skClient.account != null)
        	skClient.account.setServerSideIntProperty(
        			Account.Property.P_AVATAR_POLICY_VALUE, 
        			Ask.ask_yesno("every one can see") ? Account.AvatarPolicy.EVERYONE_CAN_SEE_VALUE : Account.AvatarPolicy.BUDDIES_OR_AUTHORIZED_CAN_SEE_VALUE);
	}

	public void execute_b() // change contact count policy
	{
        if (skClient.account != null) {
	        String[] possibilities = { "authorized", "noone" };
	        int values[] = { Account.BuddyCountPolicy.DISCLOSE_TO_AUTHORIZED_VALUE, Account.BuddyCountPolicy.DISCLOSE_TO_NOONE_VALUE};
        	int value = Ask.ask_list("show contact count to", possibilities);
        	if (( value > 0) && (value <= values.length))
        		skClient.account.setServerSideIntProperty(Account.Property.P_BUDDY_COUNT_POLICY_VALUE, values[value]);
        }
	}

	public void execute_c() // change chat policy
	{
        if (skClient.account != null) {
        	skClient.account.setServerSideIntProperty(
        			Account.Property.P_CHAT_POLICY_VALUE, 
        			Ask.ask_yesno("every one can add") ? 
        					Account.ChatPolicy.EVERYONE_CAN_ADD_VALUE : 
        					Account.ChatPolicy.BUDDIES_OR_AUTHORIZED_CAN_ADD_VALUE);
        }
	}

	public void execute_i() // change skypein policy
	{
        if (skClient.account != null) {
	        String[] possibilities = { "all", "disclosed", "buddies" };
	        int values[] = { Account.PstnCallPolicy.ALL_NUMBERS_CAN_CALL_VALUE, Account.PstnCallPolicy.DISCLOSED_NUMBERS_CAN_CALL_VALUE, Account.PstnCallPolicy.BUDDY_NUMBERS_CAN_CALL_VALUE};
        	int value = Ask.ask_list("show contact count to", possibilities);
        	if (( value > 0) && (value <= values.length))
        		skClient.account.setServerSideIntProperty(Account.Property.P_PSTN_CALL_POLICY_VALUE, values[value]);
        }
	}

	public void execute_o() // change skypeout policy
	{
        if (skClient.account != null) {
	        String[] possibilities = { "buddies", "everyone" };
	        int values[] = { Account.PhoneNumbersPolicy.PHONENUMBERS_VISIBLE_FOR_BUDDIES_VALUE, Account.PhoneNumbersPolicy.PHONENUMBERS_VISIBLE_FOR_EVERYONE_VALUE};
        	int value = Ask.ask_list("show number to", possibilities);
        	if (( value > 0) && (value <= values.length))
        		skClient.account.setServerSideIntProperty(Account.Property.P_PHONE_NUMBERS_POLICY_VALUE, values[value]);
        }
	}

	public void execute_z() // change timezone policy
	{
        if (skClient.account != null) {
	        String[] possibilities = { "auto", "manual", "undisclosed", "" };
	        int values[] = { Account.TimezonePolicy.TZ_AUTOMATIC_VALUE, Account.TimezonePolicy.TZ_MANUAL_VALUE, Account.TimezonePolicy.TZ_UNDISCLOSED_VALUE};
        	int value = Ask.ask_list("time zone policy", possibilities);
        	if (( value > 0) && (value <= values.length))
        		skClient.account.setServerSideIntProperty(Account.Property.P_TIMEZONE_POLICY_VALUE, values[value]);
        }
	}

	public void execute_v() // change voicemail policy
	{
        if (skClient.account != null) {
            skClient.account.setServerSideIntProperty(Account.Property.P_VOICEMAIL_POLICY_VALUE,
            		Ask.ask_yesno("enable voicemail") ? 
            				Account.VoicemailPolicy.VOICEMAIL_DISABLED_VALUE : 
            				Account.VoicemailPolicy.VOICEMAIL_ENABLED_VALUE);
        }
	}

	public void execute_w() // change web presence policy
	{
        if (skClient.account != null) {
        	skClient.account.setServerSideIntProperty(Account.Property.P_WEB_PRESENCE_POLICY_VALUE,
        			Ask.ask_yesno("enable web presence") ? 
        					Account.WebPresencePolicy.WEBPRESENCE_DISABLED_VALUE : 
        					Account.WebPresencePolicy.WEBPRESENCE_ENABLED_VALUE);
        }
	}

}
