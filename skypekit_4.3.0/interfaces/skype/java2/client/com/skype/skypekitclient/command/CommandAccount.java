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
import com.skype.api.Account.GetCapabilityStatusResponse;
import com.skype.api.Contact;
import com.skype.api.Contact.Availability;
import com.skype.api.ContactGroup;
import com.skype.api.Skype;
import com.skype.api.Skype.ValidateProfileStringResponse;
import com.skype.skypekitclient.SkypekitClient;
import com.skype.skypekitclient.utils.Ask;

/**
 *
 */
public class CommandAccount extends ClientCommandInterface
{
    // private static final String TAG = "CommandAccount";

    private SkypekitClient skClient;

    protected CommandAccount(SkypekitClient skypekitClient)
    {
        super();
        skClient = skypekitClient;
    }

    @Override
    public String getName()
    {
        return "Account";
    }

    @Override
    public void executeHelp()
    {
        skClient.out("\n[ACCOUNT] - a\n" +
                "\taL - login (with password)\n" +
                "\taI - login (without password)\n" +
                "\taD - login (with default account)\n" +
                "\taR - register\n" +
                "\taO - logout\n" +
                "\taK - show own capabilities \n" +
                "\tap - change presence \n" +
                "\taP - change password (don't save password)\n" +
                "\tan - change name\n" +
                "\tam - change mood\n" +
                "\tae - change email\n" +
                "\tah - change home phone\n" +
                "\tao - change office phone\n" +
                "\tac - change cell phone\n" +
                "\tab - change birthday\n" +
                "\tag - change gender\n" +
                "\tal - change language\n" +
                "\taC - change country\n" +
                "\tar - change region\n" +
                "\tat - change city\n" +
                "\taw - change homepage\n" +
                "\taa - change about\n" +
                "\taz - change timezone\n" +
                "\taF - set call forwarding\n" +
                "\taA - view authorization requests\n" +
                "\taE - view existing accounts\n" +
                "\taM - view account profile\n" +
                "\taV - validate skypename\n"
                );
    }

    public Availability availabilities[]         = { Availability.INVISIBLE, Availability.DO_NOT_DISTURB,
            Availability.ONLINE, Availability.OFFLINE, Availability.AWAY };
    public String       availabilities_as_text[] = { "INVISIBLE", "DO NOT DISTURB", "ONLINE", "OFFLINE", "AWAY" };

    @Override
    public void execute_p() // change presence
    {
        if (skClient.notLoggedIn())
            return;

        int availability = Ask.ask_list("set presence to", availabilities_as_text);
        skClient.account.setAvailability(availabilities[availability]);

        skClient.out("Changed Availability to " + availabilities_as_text[availability]);
    }

    @Override
    public void execute_L() // Login (with password)
    {
        if (skClient.isLoggedIn()) {
            skClient.error("You have to log out first");
            return;
        }

        skClient.account = skClient.skype.getAccount(Ask.ask("enter user name:"));
        if (skClient.account == null) {
            skClient.error("Unable to get account\n");
            return;
        }

        skClient.account.loginWithPassword(
                Ask.ask("enter password:"),
                Ask.ask_yesno("save password"), true);
    }

    @Override
    public void execute_D() // login with default account
    {
        if (skClient.isLoggedIn()) {
            skClient.error("You have to log out first\n");
            return;
        }

        // Get most recently used account that has pwd saved, empty string if
        // none
        String accname = skClient.skype.getDefaultAccountName();
        if (accname.length() == 0) {
            skClient.error("No default account name with saved password, use aL command to login manually");
        }
        else if (null != (skClient.account = skClient.skype.getAccount(accname))) {
            skClient.error("Unable to get default account, use aL command to login manually");
        }
        else {
            skClient.out("Logging in with default account: " + accname);
            skClient.account.login(Contact.Availability.ONLINE);
        }
    }

    @Override
    public void execute_R() // register new Skype account
    {
        if (skClient.isLoggedIn()) {
            skClient.error("You have to log out first\n");
            return;
        }

        skClient.account = skClient.skype.getAccount(Ask.ask("enter user name:"));

        skClient.account.register(
                Ask.ask("password:"),
                Ask.ask_yesno("save password"),
                true,
                Ask.ask("email:"),
                false);
    }

    @Override
    public void execute_I() // login (without password)
    {
        if (skClient.isLoggedIn()) {
            skClient.error("You have to log out first\n");
            return;
        }

        skClient.account = skClient.skype.getAccount(Ask.ask("enter user name:"));

        if (skClient.account.getStatusWithProgress().status != Account.Status.LOGGED_OUT_AND_PWD_SAVED) {
            skClient.error("Password was not saved for given account: unable to login, use aL");
            return;
        }

        skClient.out("Logging in ");
        skClient.account.login(Contact.Availability.ONLINE);
    }

    @Override
    public void execute_O()
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.logout(Ask.ask_yesno("clear password"));
    }

    @Override
    public void execute_P()
    {
        if (skClient.notLoggedIn())
            return;

        String oldp = Ask.ask("enter old password:");
        String newp = Ask.ask("enter new password:");
        String newp2 = Ask.ask("re-enter new password:");
        if (newp.equals(newp2))
            skClient.account.changePassword(oldp, newp, false);
    }

    @Override
    public void execute_K()
    {
        if (skClient.notLoggedIn())
            return;

        for (Contact.Capability c : Contact.Capability.values()) {
            GetCapabilityStatusResponse result = skClient.account.getCapabilityStatus(c);
            if (result.status.equals(Account.CapabilityStatus.CAPABILITY_EXISTS)) // equals or direct ref comp?
                skClient.out("Capability " + c +
                        " status is " + result.status +
                        " (expires on " + result.expiryTimestamp + ")");
            else
                skClient.out("Capability " + c +
                        " status is " + result.status);
        }
    }

    @Override
    public void execute_n() // change name
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_FULL_NAME_VALUE, Ask.ask("full name: "));
    }

    @Override
    public void execute_m() // change mood
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_MOOD_TEXT_VALUE, Ask.ask("mood: "));
    }

    @Override
    public void execute_e() // change email
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_EMAILS_VALUE, Ask.ask("email: "));
    }

    @Override
    public void execute_h() // change home phone
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_PHONE_HOME_VALUE, Ask.ask("phone at home: "));
    }

    @Override
    public void execute_o() // change office phone
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_PHONE_OFFICE_VALUE, Ask.ask("phone at office: "));
    }

    @Override
    public void execute_c() // change cell phone
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_PHONE_MOBILE_VALUE, Ask.ask("cell phone: "));
    }

    @Override
    public void execute_b() // change birthday
    {
        if (skClient.notLoggedIn())
            return;

        String bday = Ask.ask("year (YYYY) : ") + Ask.ask("month (MM): ") + Ask.ask("day (DD): ");
        skClient.account.setIntProperty(Account.Property.P_BIRTHDAY_VALUE, Integer.valueOf(bday));
    }

    @Override
    public void execute_g() // change gender
    {
        if (skClient.notLoggedIn())
            return;

        String possibilities[] = { "male", "female" };
        skClient.account.setIntProperty(Account.Property.P_GENDER_VALUE, Ask.ask_list("gender", possibilities) + 1);
    }

    @Override
    public void execute_l() // change language
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_LANGUAGES_VALUE, Ask.ask("languages: "));
    }

    @Override
    public void execute_C() // change country
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_COUNTRY_VALUE, Ask.ask("country: "));
    }

    @Override
    public void execute_r() // change region
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_PROVINCE_VALUE, Ask.ask("region: "));
    }

    @Override
    public void execute_t() // change city
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_CITY_VALUE, Ask.ask("city: "));
    }

    @Override
    public void execute_w() // change homepage
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_HOMEPAGE_VALUE, Ask.ask("homepage: "));
    }

    @Override
    public void execute_a() // change about
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_ABOUT_VALUE, Ask.ask("about: "));
    }

    @Override
    public void execute_z() // change timezone
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setStrProperty(Account.Property.P_TIMEZONE_VALUE, Ask.ask("timezone: "));
    }

    @Override
    public void execute_F() // set call forwarding
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.setServerSideStrProperty(Account.Property.P_OFFLINE_CALL_FORWARD_VALUE,
                Ask.ask("forward to: (beginSecond, endSecond, identity)"));
    }

    @Override
    public void execute_A() // view auth requests
    {
        ContactGroup cg;
        Contact[] authrequests;

        cg = skClient.skype.getHardwiredContactGroup(ContactGroup.Type.CONTACTS_WAITING_MY_AUTHORIZATION);

        if ((cg == null) || (null == (authrequests = cg.getContacts()))) {
            skClient.error("Unable to get auth requests list\n");
            return;
        }

        if (authrequests.length == 0) {
            skClient.out("Auth requests list is empty\n");
            return;
        }

        for (Contact contact : authrequests) {
            String txt = new String("New authorization request from " + contact.getIdentity() + " authorize?");
            String possibilities[] = { "ignore", "add", "block" };
            switch (Ask.ask_list(txt, possibilities)) {
            case 0:
                contact.ignoreAuthRequest();
                break;
            case 1:
                contact.setBuddyStatus(true, true);
                break;
            case 2:
                contact.setBlocked(true, false);
                break;
            default:
                break;
            }
        }
    }

    @Override
    public void execute_E() // view existing accounts
    {
        String[] names = skClient.skype.getExistingAccounts();

        if (names.length > 0)
            skClient.out("Existing accounts:\n");
        else
            skClient.out("No existing accounts\n");

        for (int i = 0; i < names.length; i++)
            skClient.out((i + 1) + names[i]);
    }

    public void execute_M() // view account profile
    {
        if (skClient.notLoggedIn())
            return;

        skClient.account.mgetProfile();

        skClient.out("\t" + "skypename\t\t" + skClient.account.getSkypeName());
        skClient.out("\t" + "fullname\t\t" + skClient.account.getFullName());
        skClient.out("\t" + "birthday\t\t" + skClient.account.getBirthday());
        skClient.out("\t" + "gender\t\t\t" + skClient.account.getGender());
        skClient.out("\t" + "languages\t\t" + skClient.account.getLanguages());
        skClient.out("\t" + "country\t\t\t" + skClient.account.getCountry());
        skClient.out("\t" + "province\t\t" + skClient.account.getProvince());
        skClient.out("\t" + "city\t\t\t" + skClient.account.getCity());
        skClient.out("\t" + "phone_home\t\t" + skClient.account.getPhoneHome());
        skClient.out("\t" + "phone_office\t\t" + skClient.account.getPhoneOffice());
        skClient.out("\t" + "phone_mobile\t\t" + skClient.account.getPhoneMobile());
        skClient.out("\t" + "emails\t\t\t" + skClient.account.getEmails());
        skClient.out("\t" + "homepage\t\t" + skClient.account.getHomepage());
        skClient.out("\t" + "about\t\t\t" + skClient.account.getAbout());
        skClient.out("\t" + "avatar_image\t\t" + skClient.account.getAvatarImage());
        skClient.out("\t" + "mood_text\t\t" + skClient.account.getMoodText());
        skClient.out("\t" + "rich_mood_text\t\t" + skClient.account.getRichMoodText());
        skClient.out("\t" + "timezone\t\t" + skClient.account.getTimezone());
        skClient.out("\t" + "profile_timestamp\t" + skClient.account.getProfileTimestamp());
        skClient.out("\t" + "nrof_authed_buddies\t" + skClient.account.getNrofAuthedBuddies());
        skClient.out("\t" + "ipcountry\t\t" + skClient.account.getCountry());
        skClient.out("\t" + "avatar_timestamp\t" + skClient.account.getAvatarTimestamp());
        skClient.out("\t" + "mood_timestamp\t\t" + skClient.account.getMoodTimestamp());
        skClient.out("\t" + "availability\t\t" + skClient.account.getAvailability());
    }

    public void execute_V()
    {
        ValidateProfileStringResponse r;

        r = skClient.skype.validateProfileString(Contact.Property.P_SKYPE_NAME_VALUE,
                Ask.ask("Enter skypename to validate:"),
                Ask.ask_yesno("Validate for registration"));

        if (r.result.getId() > Skype.Validateresult.STARTS_WITH_INVALID_CHAR_VALUE)
            skClient.out("result: " + r.result + " free bytes: " + r.freeBytesLeft);
        else
            skClient.out("result: " + r.result + " free bytes: " + r.freeBytesLeft);
    }

}

