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

import com.skype.ipc.SidGetResponding;
import com.skype.api.Contact;
import com.skype.api.Contact.GetAvatarResponse;
import com.skype.api.ContactGroup;
import com.skype.skypekitclient.SkypekitClient;
import com.skype.skypekitclient.utils.Ask;

/**
 *
 */
public class CommandContactList extends ClientCommandInterface
{
    // private static final String TAG = "CommandContactList";

    private SkypekitClient skClient;

    protected CommandContactList(SkypekitClient skypekitClient)
    {
        super();
        skClient = skypekitClient;
    }

    @Override
    public String getName()
    {
        return "ContactList";
    }

    public void executeHelp()
    {
        skClient.out("\n[CONTACTLIST] - l\n"
                + "\tlB - list all buddies\n"
                + "\tlg - list contacts in contact groups\n"
                + "\tlr - rename a contact\n"
                + "\tlp - list contact properties\n"
                + "\tlb - block a contact\n"
                + "\tlu - unblock a contact\n"
                + "\tld - delete a	contact from list\n"
                + "\tla - report abuse\n"
                + "\tlv - store contact avatar\n"
                + "\tlR - refresh profile\n");
    }

    public void execute_p() // list contact properties
    {
        String skypename = Ask.ask("get properties from (skypename) : ");
        Contact contact = find_contact(skypename);
        if (contact == null) {
            skClient.error("Unable to get contact");
            return;
        }
        skClient.out("contact oid " + contact.getOid());

        contact.mgetProfile();

        skClient.out("\t" + "type\t\t\t" + contact.getType());
        skClient.out("\t" + "skypename\t\t" + contact.getSkypeName());
        skClient.out("\t" + "pstnnumber\t\t" + contact.getPstnNumber());
        skClient.out("\t" + "fullname\t\t" + contact.getFullName());
        skClient.out("\t" + "birthday\t\t" + contact.getBirthday());
        skClient.out("\t" + "gender\t\t\t" + contact.getGender());
        skClient.out("\t" + "languages\t\t" + contact.getLanguages());
        skClient.out("\t" + "country\t\t\t" + contact.getCountry());
        skClient.out("\t" + "province\t\t" + contact.getProvince());
        skClient.out("\t" + "city\t\t\t" + contact.getCity());
        skClient.out("\t" + "phone_home\t\t" + contact.getPhoneHome());
        skClient.out("\t" + "phone_office\t\t" + contact.getPhoneOffice());
        skClient.out("\t" + "phone_mobile\t\t" + contact.getPhoneMobile());
        skClient.out("\t" + "emails\t\t\t" + contact.getEmails());
        skClient.out("\t" + "homepage\t\t" + contact.getHomepage());
        skClient.out("\t" + "about\t\t\t" + contact.getAbout());
        skClient.out("\t" + "avatar_image\t\t" + contact.getAvatarImage());
        skClient.out("\t" + "mood_text\t\t" + contact.getMoodText());
        skClient.out("\t" + "rich_mood_text\t\t" + contact.getRichMoodText());
        skClient.out("\t" + "timezone\t\t" + contact.getTimezone());
        skClient.out("\t" + "capabilities\t\t" + contact.getCapabilities());
        skClient.out("\t" + "profile_timestamp\t" + contact.getProfileTimestamp());
        skClient.out("\t" + "nrof_authed_buddies\t"
                + contact.getNrofAuthedBuddies());
        skClient.out("\t" + "ipcountry\t\t" + contact.getIpCountry());
        skClient.out("\t" + "avatar_timestamp\t" + contact.getAvatarTimestamp());
        skClient.out("\t" + "mood_timestamp\t\t" + contact.getMoodTimestamp());
        skClient.out("\t" + "received_authrequest\t"
                + contact.getReceivedAuthRequest());
        skClient.out("\t" + "authreq_timestamp\t" + contact.getAuthRequestTimestamp());
        skClient.out("\t" + "lastonline_timestamp\t" + contact.getLastOnlineTimestamp());
        skClient.out("\t" + "availability\t\t" + contact.getAvailability());
        skClient.out("\t" + "displayname\t\t" + contact.getDisplayName());
        skClient.out("\t" + "refreshing\t\t" + contact.getRefreshing());
        skClient.out("\t" + "given_authlevel\t\t" + contact.getGivenAuthLevel());
        skClient.out("\t" + "given_displayname\t" + contact.getGivenDisplayName());
        skClient.out("\t" + "assigned_comment\t" + contact.getAssignedComment());
        skClient.out("\t" + "lastused_timestamp\t" + contact.getLastUsedTimestamp());
        skClient.out("\t" + "authrequest_count\t" + contact.getAuthRequestCount());
        skClient.out("\t" + "assigned_phone1\t\t" + contact.getAssignedPhone1());
        skClient.out("\t" + "assigned_phone1_label\t"
                + contact.getAssignedPhone1Label());
        skClient.out("\t" + "assigned_phone2\t\t" + contact.getAssignedPhone2());
        skClient.out("\t" + "assigned_phone2_label\t"
                + contact.getAssignedPhone2Label());
        skClient.out("\t" + "assigned_phone3\t\t" + contact.getAssignedPhone3());
        skClient.out("\t" + "assigned_phone3_label\t"
                + contact.getAssignedPhone3Label());
    }

    public void execute_d() // delete a contact from list
    {
        String skypename = Ask.ask("delete contact name: ");
        Contact buddy = find_contact(skypename);
        if (buddy != null) {
            ContactGroup cg = skClient.skype.getHardwiredContactGroup(ContactGroup.Type.ALL_BUDDIES);
            if (cg != null)
                cg.removeContact(buddy);
        }
    }

    public Contact find_contact(String skypename)
    {
        if (skClient.notLoggedIn())
            return null;

        ContactGroup cg = skClient.skype.getHardwiredContactGroup(ContactGroup.Type.ALL_KNOWN_CONTACTS);
        Contact[] contacts;

        if ((cg == null) || (null == (contacts = cg.getContacts()))) {
            skClient.error("Unable to get contact list");
            return null;
        }

        for (Contact contact : contacts) {
            if (skypename.equals(contact.getIdentity())) {
                return contact;
            }
        }

        return null; // No contact with matching skypename
    }

    public void execute_b() // block a contact
    {
        String skypename = Ask.ask("block name: ");
        Contact buddy = find_contact(skypename);
        if (buddy != null) {
            buddy.setBlocked(true, false);
        }
    }

    public void execute_u() // unblock a contact
    {
        String skypename = Ask.ask("unblock name: ");
        Contact buddy = find_contact(skypename);
        if (buddy != null) {
            buddy.setBlocked(false, false);
        }
    }

    public void execute_a() // report abuse
    {
        String skypename = Ask.ask("report abuse from: ");
        Contact buddy = find_contact(skypename);
        if (buddy != null) {
            buddy.setBlocked(true, true);
        }
    }

    public void execute_B() // list all buddies
    {
        if (skClient.notLoggedIn())
            return;

        ContactGroup cg = skClient.skype.getHardwiredContactGroup(ContactGroup.Type.ALL_BUDDIES);
        Contact[] buddies;
        if ((cg == null) || (null == (buddies = cg.getContacts()))) {
            skClient.error("Unable to get buddies list");
            return;
        }

        skClient.buddies = buddies;

        switch (Ask.ask_int("use\n0 generic  multiget\n1 generated multiget\n2 single gets: ")) {
        case 0:
            // multiget! but identity is not a property...
            Contact.Property[] properties = { Contact.Property.P_DISPLAY_NAME, Contact.Property.P_AVAILABILITY };
            SidGetResponding[] responses = Contact.sidMultiGet(properties, buddies);
            int r = 0;
            for (SidGetResponding buddy_resp : responses) {
//                 Contact buddy = (Contact) buddy_resp; // all props are cached =>  the response is actually the object... and one could iterate buddies
//                 skClient.out(buddy.getDisplayName() + " (" + buddy.getIdentity() + ") [" + buddy.getAvailability() + "]");
                 Contact buddy = buddies[r++]; // also (Contact)buddy_resp.sidGetObject()
                 skClient.out(buddy_resp.sidGetStringProperty(Contact.Property.P_DISPLAY_NAME) 
                            + " (" + buddy.getIdentity() + ") ["
                            + buddy_resp.sidGetEnumProperty(Contact.Property.P_AVAILABILITY) + "]");
            }
            break;
        case 1:
            Contact[] contacts = Contact.mgetInfo(buddies); // contacs == buddies...
            for (Contact buddy : contacts) {
                skClient.out(buddy.getDisplayName() + " (" + buddy.getIdentity() + ") [" + buddy.getAvailability() + "]");
            }
//            Contact.MgetInfoResponse[] resps = Contact.mgetInfo(buddies);
//            for (Contact.MgetInfoResponse response : resps) {
//                skClient.out(response.getDisplayName() + " (" + response.mSidObject.getIdentity() + ") [" + response.getAvailability() + "]");
//            }
            break;
        case 2:
            for (Contact buddy : buddies) 
                skClient.out(buddy.getDisplayName() + " (" + buddy.getIdentity() + ") [" + buddy.getAvailability() + "]");
            break;
        }
    }

    public void execute_r() // rename a contact
    {
        String skypename = Ask.ask("rename old name: ");
        Contact buddy = find_contact(skypename);
        if (buddy != null) {
            buddy.giveDisplayName(Ask.ask("to newname: "));
        }
    }

    public void execute_g() // list contacts in contact groups
    {
        if (skClient.notLoggedIn())
            return;

        ContactGroup.Type[] cgTypes = ContactGroup.Type.values();
        for (int n = 0; n < cgTypes.length; n++) {
            skClient.out((n + 1) + " - " + cgTypes[n].toString());
        }

        int cnum = Ask.ask_int("select contact group (enter 1-" + cgTypes.length + "): ");

        if ((cnum == 0) || (cnum >= cgTypes.length)) {
            return;
        }

        ContactGroup cg = skClient.skype.getHardwiredContactGroup(cgTypes[cnum - 1]);

        Contact[] contacts;
        if ((cg == null) || (null == (contacts = cg.getContacts()))) {
            skClient.error("Unable to get contact list");
            return;
        }

        skClient.out("contacts = ");
        if (contacts.length == 0) {
            skClient.out("none");
            return;
        }

        String identity;
        int i = 1;
        for (Contact contact : contacts) {
            identity = contact.getIdentity();
            skClient.out((i++) + " - " + identity);
        }
    }

    public void execute_v() // store contact avatar
    {
        String skypename = Ask.ask("skypename: ");
        Contact buddy = find_contact(skypename);
        if (buddy != null) {
            GetAvatarResponse avatar = buddy.getAvatar();
            if ((avatar != null) && avatar.present) {
                byte[] propavatar = buddy.getAvatarImage();
                if (propavatar != null)
                    skClient.out(skypename + " avatar size = " + avatar.avatar.length + " and same as property "
                            + avatar.avatar.equals(propavatar));

            }
            else if (avatar != null) {
                byte[] propavatar = buddy.getAvatarImage();
                if (propavatar != null)
                    skClient.out(skypename + " has no avatar using default of size " + avatar.avatar.length
                            + " prop = " + propavatar.length);
            }
            else {
                skClient.error("Failed to get avatar for " + skypename);
            }
        }
    }

    public void execute_R() // refresh profile
    {
        String skypename = Ask.ask("skypename: ");
        Contact contact = find_contact(skypename);
        if (contact != null) {
            contact.refreshProfile();
        }
    }

}

