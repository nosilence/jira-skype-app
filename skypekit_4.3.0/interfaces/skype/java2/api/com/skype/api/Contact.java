package com.skype.api;

import com.skype.ipc.SidRoot;
import com.skype.ipc.SidObject;
import com.skype.ipc.EnumConverting;
import com.skype.ipc.PropertyEnumConverting;
import com.skype.ipc.Decoding;
import com.skype.ipc.Encoding;
import com.skype.ipc.Encoding;
import java.io.IOException;
import com.skype.ipc.PropertyEnumConverting;
import com.skype.api.ContactGroup;
import com.skype.ipc.SidGetResponding;

/** Address book entry. Encapsulates methods like GetIdentity, GetAvatar, SendAuthRequest, OpenConversation etc. Single contact can have additional phone numbers attached to it (ASSIGNED_PHONE1 .. ASSIGNED_PHONE3). Note that in the context of a conversation, Contacts are represented by Participant objects. Contact member functions all return a Boolean indicating the success (true) or failure (false) of processing the request itself (transport, runtime availability, and so forth)?not the success or failure of its associated functionality. For example, Contact.IsMemberOf returns true if it was able to make a determination, and its result parameter reflects whether this Contact is a member of the target group. Similarly, Contact.IsMemberOf returns false if it was unable to make a determination, and the value of its result parameter is undefined.  */
public final class Contact extends SidObject {
	/** Same as with CAPABILITY, enumerator is used by both Contact and Account objects.  */
	public enum Type implements EnumConverting {
		/** Contact/account has no pre-identified type. This type is reported by default for SkypeKit clients.  */
		UNRECOGNIZED    (0),
		/** Normal Skype contact.  */
		SKYPE           (1),
		/** Normal PSTN contact.  */
		PSTN            (2),
		/** Emergency number (i.e. 911).  */
		EMERGENCY_PSTN  (3),
		FREE_PSTN       (4),
		/** Undisclosed PSTN number.  */
		UNDISCLOSED_PSTN(5),
		/** This type is currently used by Windows desktop clients for contacts imported from Outlook.  */
		EXTERNAL        (6);
		private final int key;
		Type(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return UNRECOGNIZED; }
		public EnumConverting convert(int from) { return Type.get(from); }
		public EnumConverting[] getArray(final int size) { return new Type[size]; }
		public static Type get(int from) {
			switch (from) {
			case 0: return UNRECOGNIZED;
			case 1: return SKYPE;
			case 2: return PSTN;
			case 3: return EMERGENCY_PSTN;
			case 4: return FREE_PSTN;
			case 5: return UNDISCLOSED_PSTN;
			case 6: return EXTERNAL;
			}
			return UNRECOGNIZED;
		}
		public static final int UNRECOGNIZED_VALUE     = 0;
		public static final int SKYPE_VALUE            = 1;
		public static final int PSTN_VALUE             = 2;
		public static final int EMERGENCY_PSTN_VALUE   = 3;
		public static final int FREE_PSTN_VALUE        = 4;
		public static final int UNDISCLOSED_PSTN_VALUE = 5;
		public static final int EXTERNAL_VALUE         = 6;
	}
	/** Describes the recognized relational states between a local account and a remote contact.  */
	public enum AuthLevel implements EnumConverting {
		/** Authorization request is either ignored or pending. In this state several functionalities may be blocked, depending on settings. For example, accounts may only allow seeing online presence to be viewable or only receive calls from authorized contacts.  */
		NONE            (0),
		/** Contact has been authorized by the local account.  */
		AUTHORIZED_BY_ME(1),
		/** Contact has been blocked by the local account. This prevents incoming calls, chat messages, additional authorization requests etc.  */
		BLOCKED_BY_ME   (2);
		private final int key;
		AuthLevel(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return NONE; }
		public EnumConverting convert(int from) { return AuthLevel.get(from); }
		public EnumConverting[] getArray(final int size) { return new AuthLevel[size]; }
		public static AuthLevel get(int from) {
			switch (from) {
			case 0: return NONE;
			case 1: return AUTHORIZED_BY_ME;
			case 2: return BLOCKED_BY_ME;
			}
			return NONE;
		}
		public static final int NONE_VALUE             = 0;
		public static final int AUTHORIZED_BY_ME_VALUE = 1;
		public static final int BLOCKED_BY_ME_VALUE    = 2;
	}
	/** Describes the superset list of possible Account and Contact online statuses. In case of Account they apply to local user, in case of Contact they apply to remote contacts.  */
	public enum Availability implements EnumConverting {
		/** Contact online status cannot be determined. This availability state should not normally reach the SkypeKit UI level.  */
		UNKNOWN                   (0),
		/** Seeing Contact online status is blocked because authorization between contact and local account has not taken place.  */
		PENDINGAUTH               (8),
		/** Remote contact has been blocked by local account. This applies to online accounts.  */
		BLOCKED                   (9),
		/** Remote SkypeOut contact has been blocked by local account.  */
		BLOCKED_SKYPEOUT          (11),
		/** Contact does not have an online status because he is a PSTN contact.  */
		SKYPEOUT                  (10),
		/** Contact appears to be offline.  */
		OFFLINE                   (1),
		/** Contact appears to be offline but has voicemail enabled.  */
		OFFLINE_BUT_VM_ABLE       (12),
		/** Contact appears to be offline but has enabled call forwarding, so calls may actually get through to him.  */
		OFFLINE_BUT_CF_ABLE       (13),
		/** Contact / Account is online  */
		ONLINE                    (2),
		/** Contact / Account is online but away from keyboard. This can be either turned on manually or by automatic timer. In Windows desktop client, the timer can be configured with minute precision.  */
		AWAY                      (3),
		/** This online status is marked as deprecated. If a remote contact indicates its status as NOT_AVAILABLE, the UI should handle this as equivalent of AWAY status.  */
		NOT_AVAILABLE             (4),
		/** Contact / Account is online but does not wish to be disturbed. This status supersedes AWAY status when the account is DO_NOT_DISTURB the AWAY timer should not modify the status.  */
		DO_NOT_DISTURB            (5),
		/** This online status is marked as deprecated. If a remote contact indicates its status as SKYPE_ME, the UI should handle this as equivalent of ONLINE status.  */
		SKYPE_ME                  (7),
		/** Account status is set to INVISIBLE. This status in not applicable to remote Contacts. When the remote contact has set his availability to INVISIBLE, he will appear as OFFLINE to others.  */
		INVISIBLE                 (6),
		/** only possible for local user/account */
		CONNECTING                (14),
		ONLINE_FROM_MOBILE        (15),
		/** *_FROM_MOBILE only possible for remote user */
		AWAY_FROM_MOBILE          (16),
		NOT_AVAILABLE_FROM_MOBILE (17),
		DO_NOT_DISTURB_FROM_MOBILE(18),
		SKYPE_ME_FROM_MOBILE      (20);
		private final int key;
		Availability(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return UNKNOWN; }
		public EnumConverting convert(int from) { return Availability.get(from); }
		public EnumConverting[] getArray(final int size) { return new Availability[size]; }
		public static Availability get(int from) {
			switch (from) {
			case  0: return UNKNOWN;
			case  8: return PENDINGAUTH;
			case  9: return BLOCKED;
			case 11: return BLOCKED_SKYPEOUT;
			case 10: return SKYPEOUT;
			case  1: return OFFLINE;
			case 12: return OFFLINE_BUT_VM_ABLE;
			case 13: return OFFLINE_BUT_CF_ABLE;
			case  2: return ONLINE;
			case  3: return AWAY;
			case  4: return NOT_AVAILABLE;
			case  5: return DO_NOT_DISTURB;
			case  7: return SKYPE_ME;
			case  6: return INVISIBLE;
			case 14: return CONNECTING;
			case 15: return ONLINE_FROM_MOBILE;
			case 16: return AWAY_FROM_MOBILE;
			case 17: return NOT_AVAILABLE_FROM_MOBILE;
			case 18: return DO_NOT_DISTURB_FROM_MOBILE;
			case 20: return SKYPE_ME_FROM_MOBILE;
			}
			return UNKNOWN;
		}
		public static final int UNKNOWN_VALUE                    =  0;
		public static final int PENDINGAUTH_VALUE                =  8;
		public static final int BLOCKED_VALUE                    =  9;
		public static final int BLOCKED_SKYPEOUT_VALUE           = 11;
		public static final int SKYPEOUT_VALUE                   = 10;
		public static final int OFFLINE_VALUE                    =  1;
		public static final int OFFLINE_BUT_VM_ABLE_VALUE        = 12;
		public static final int OFFLINE_BUT_CF_ABLE_VALUE        = 13;		public static final int ONLINE_VALUE                     =  2;
		public static final int AWAY_VALUE                       =  3;
		public static final int NOT_AVAILABLE_VALUE              =  4;
		public static final int DO_NOT_DISTURB_VALUE             =  5;
		public static final int SKYPE_ME_VALUE                   =  7;
		public static final int INVISIBLE_VALUE                  =  6;
		public static final int CONNECTING_VALUE                 = 14;
		public static final int ONLINE_FROM_MOBILE_VALUE         = 15;
		public static final int AWAY_FROM_MOBILE_VALUE           = 16;
		public static final int NOT_AVAILABLE_FROM_MOBILE_VALUE  = 17;
		public static final int DO_NOT_DISTURB_FROM_MOBILE_VALUE = 18;
		public static final int SKYPE_ME_FROM_MOBILE_VALUE       = 20;
	}
	public enum ExtraAuthReqFields implements EnumConverting {
		/** send verified e-mail blob with this auth request */
		SEND_VERIFIED_EMAIL  (1),
		/** send verified company blob with this auth request */
		SEND_VERIFIED_COMPANY(2);
		private final int key;
		ExtraAuthReqFields(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return SEND_VERIFIED_EMAIL; }
		public EnumConverting convert(int from) { return ExtraAuthReqFields.get(from); }
		public EnumConverting[] getArray(final int size) { return new ExtraAuthReqFields[size]; }
		public static ExtraAuthReqFields get(int from) {
			switch (from) {
			case 1: return SEND_VERIFIED_EMAIL;
			case 2: return SEND_VERIFIED_COMPANY;
			}
			return SEND_VERIFIED_EMAIL;
		}
		public static final int SEND_VERIFIED_EMAIL_VALUE   = 1;
		public static final int SEND_VERIFIED_COMPANY_VALUE = 2;
	}
	/**
	 * This enumerator is used by both Contact and Account objects. Thus the items here can have slightly different meaning, depending on which context you will examine their values. In case of Contact, the values apply to a user - across all the instances that user has logged in with Skype. In case of Account, the capability is that of a local, currently logged in instance of Skype client. 
	 * 
	 * The values that CAPABILITY items can have are also dependant on class context. In context of Contact, a capability can be CAPABILITY_MIXED. Which in case of CAPABILITY_VIDEO, for example, would mean that the remote use has logged in with different clients, some of which support video calls and some of which don't. In context of Account - there are no mixed result. Currently logged in Skype instance either supports video or it doesn't. 
	 */
	public enum Capability implements EnumConverting {
		/** For Account object, this is the same as CAPABILITY_CAN_BE_SENT_VM - it indicates that the currently logged in Skype instance supports voicemails. For Contact objects, it means that their remote system supports sending voicemails - there is no technical method for Skype to detect whether they are capable of receiving voicemails, so the assumption is that they can.  */
		CAPABILITY_VOICEMAIL         (0),
		/** Indicates that the contact/account has SkypeOut and is thus capable of making PSTN calls.  */
		CAPABILITY_SKYPEOUT          (1),
		/** Indicates that the contact/account has SkypeIn and is thus capable of answering PSTN calls.  */
		CAPABILITY_SKYPEIN           (2),
		/** For contacts, this is a combination of CAPABILITY_VOICEMAIL for local account (local Skype client supports sending voicemails) and CAPABILITY_VOICEMAIL of the Contact - if the contact supports sending voicemails then hopefully they can also receive them.  */
		CAPABILITY_CAN_BE_SENT_VM    (3),
		/** Indicates that Account/Contact supports call forwarding.  */
		CAPABILITY_CALL_FORWARD      (4),
		/** Indicates that Account/Contact supports call video calls.  */
		CAPABILITY_VIDEO             (5),
		/** In context of Contact, this indicates that the user is noticed running at least one Skype implementation that supports text messaging. Basically, it applies to a user. When applied to Account, the meaning is slightly different. In that case it indicates that currently running Skype implementation supports chat messaging. So, for Account objects, this is a node (rather than user) capability.  */
		CAPABILITY_TEXT              (6),
		/** Indicates that the contact/account is flagged as SkypePrime service provider. This is linked to Account class SERVICE_PROVIDER_INFO property.  */
		CAPABILITY_SERVICE_PROVIDER  (7),
		/** This is a legacy item, from the old times when conference calls with more than 5 people were limited to SkypePro accounts. In other words, this item is no longer relevant and will likely be removed at some point in future.  */
		CAPABILITY_LARGE_CONFERENCE  (8),
		CAPABILITY_COMMERCIAL_CONTACT(9),
		/** Indicates that Account/Contact supports call transfers to PSTN numbers.  */
		CAPABILITY_PSTN_TRANSFER     (10),
		/** Indicates that the user has had his chat capability removed by Skype. Basically, this means that the user is reported as spammer too many times. This applies for both Contact and Account objects - which means your client can check locally, if the currently logged in user has been marked as a spammer.  */
		CAPABILITY_TEXT_EVER         (11),
		/** Indicates that the user (Account or Contact) has had his voice call capability removed by Skype.  */
		CAPABILITY_VOICE_EVER        (12),
		/** Indicates that the instance of Skype client Account/Contact is or in Contact's case has at least occasionally been flagged as a mobile device.  */
		CAPABILITY_MOBILE_DEVICE     (13),
		CAPABILITY_PUBLIC_CONTACT    (14);
		private final int key;
		Capability(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return CAPABILITY_VOICEMAIL; }
		public EnumConverting convert(int from) { return Capability.get(from); }
		public EnumConverting[] getArray(final int size) { return new Capability[size]; }
		public static Capability get(int from) {
			switch (from) {
			case  0: return CAPABILITY_VOICEMAIL;
			case  1: return CAPABILITY_SKYPEOUT;
			case  2: return CAPABILITY_SKYPEIN;
			case  3: return CAPABILITY_CAN_BE_SENT_VM;
			case  4: return CAPABILITY_CALL_FORWARD;
			case  5: return CAPABILITY_VIDEO;
			case  6: return CAPABILITY_TEXT;
			case  7: return CAPABILITY_SERVICE_PROVIDER;
			case  8: return CAPABILITY_LARGE_CONFERENCE;
			case  9: return CAPABILITY_COMMERCIAL_CONTACT;
			case 10: return CAPABILITY_PSTN_TRANSFER;
			case 11: return CAPABILITY_TEXT_EVER;
			case 12: return CAPABILITY_VOICE_EVER;
			case 13: return CAPABILITY_MOBILE_DEVICE;
			case 14: return CAPABILITY_PUBLIC_CONTACT;
			}
			return CAPABILITY_VOICEMAIL;
		}
		public static final int CAPABILITY_VOICEMAIL_VALUE          =  0;
		public static final int CAPABILITY_SKYPEOUT_VALUE           =  1;
		public static final int CAPABILITY_SKYPEIN_VALUE            =  2;
		public static final int CAPABILITY_CAN_BE_SENT_VM_VALUE     =  3;
		public static final int CAPABILITY_CALL_FORWARD_VALUE       =  4;
		public static final int CAPABILITY_VIDEO_VALUE              =  5;
		public static final int CAPABILITY_TEXT_VALUE               =  6;
		public static final int CAPABILITY_SERVICE_PROVIDER_VALUE   =  7;
		public static final int CAPABILITY_LARGE_CONFERENCE_VALUE   =  8;
		public static final int CAPABILITY_COMMERCIAL_CONTACT_VALUE =  9;
		public static final int CAPABILITY_PSTN_TRANSFER_VALUE      = 10;
		public static final int CAPABILITY_TEXT_EVER_VALUE          = 11;
		public static final int CAPABILITY_VOICE_EVER_VALUE         = 12;
		public static final int CAPABILITY_MOBILE_DEVICE_VALUE      = 13;
		public static final int CAPABILITY_PUBLIC_CONTACT_VALUE     = 14;
	}
	/** List of possible states of each of the Contact class CAPABILITY items.  */
	public enum Capabilitystatus implements EnumConverting {
		/** Contact does not have the capability  */
		NO_CAPABILITY    (0),
		/** Contact has occasionally logged in with Skype client that supports the capability. For example, a contact may have Skype client on several machines, only some of which have webcam - in which case CAPABILITY_VIDEO would have its value set as CAPABILITY_MIXED.  */
		CAPABILITY_MIXED (1),
		/** Contact has the capability  */		CAPABILITY_EXISTS(2);
		private final int key;
		Capabilitystatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return NO_CAPABILITY; }
		public EnumConverting convert(int from) { return Capabilitystatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new Capabilitystatus[size]; }
		public static Capabilitystatus get(int from) {
			switch (from) {
			case 0: return NO_CAPABILITY;
			case 1: return CAPABILITY_MIXED;
			case 2: return CAPABILITY_EXISTS;
			}
			return NO_CAPABILITY;
		}
		public static final int NO_CAPABILITY_VALUE     = 0;
		public static final int CAPABILITY_MIXED_VALUE  = 1;
		public static final int CAPABILITY_EXISTS_VALUE = 2;
	}
	private final static byte[] P_TYPE_req = {(byte) 90,(byte) 71,(byte) 202,(byte) 1,(byte) 93,(byte) 2};
	private final static byte[] P_SKYPE_NAME_req = {(byte) 90,(byte) 71,(byte) 4,(byte) 93,(byte) 2};
	private final static byte[] P_PSTN_NUMBER_req = {(byte) 90,(byte) 71,(byte) 6,(byte) 93,(byte) 2};
	private final static byte[] P_FULL_NAME_req = {(byte) 90,(byte) 71,(byte) 5,(byte) 93,(byte) 2};
	private final static byte[] P_BIRTHDAY_req = {(byte) 90,(byte) 71,(byte) 7,(byte) 93,(byte) 2};
	private final static byte[] P_GENDER_req = {(byte) 90,(byte) 71,(byte) 8,(byte) 93,(byte) 2};
	private final static byte[] P_LANGUAGES_req = {(byte) 90,(byte) 71,(byte) 9,(byte) 93,(byte) 2};
	private final static byte[] P_COUNTRY_req = {(byte) 90,(byte) 71,(byte) 10,(byte) 93,(byte) 2};
	private final static byte[] P_PROVINCE_req = {(byte) 90,(byte) 71,(byte) 11,(byte) 93,(byte) 2};
	private final static byte[] P_CITY_req = {(byte) 90,(byte) 71,(byte) 12,(byte) 93,(byte) 2};
	private final static byte[] P_PHONE_HOME_req = {(byte) 90,(byte) 71,(byte) 13,(byte) 93,(byte) 2};
	private final static byte[] P_PHONE_OFFICE_req = {(byte) 90,(byte) 71,(byte) 14,(byte) 93,(byte) 2};
	private final static byte[] P_PHONE_MOBILE_req = {(byte) 90,(byte) 71,(byte) 15,(byte) 93,(byte) 2};
	private final static byte[] P_EMAILS_req = {(byte) 90,(byte) 71,(byte) 16,(byte) 93,(byte) 2};
	private final static byte[] P_HOMEPAGE_req = {(byte) 90,(byte) 71,(byte) 17,(byte) 93,(byte) 2};
	private final static byte[] P_ABOUT_req = {(byte) 90,(byte) 71,(byte) 18,(byte) 93,(byte) 2};
	private final static byte[] P_AVATAR_IMAGE_req = {(byte) 90,(byte) 71,(byte) 37,(byte) 93,(byte) 2};
	private final static byte[] P_MOOD_TEXT_req = {(byte) 90,(byte) 71,(byte) 26,(byte) 93,(byte) 2};
	private final static byte[] P_RICH_MOOD_TEXT_req = {(byte) 90,(byte) 71,(byte) 205,(byte) 1,(byte) 93,(byte) 2};
	private final static byte[] P_TIMEZONE_req = {(byte) 90,(byte) 71,(byte) 27,(byte) 93,(byte) 2};
	private final static byte[] P_CAPABILITIES_req = {(byte) 90,(byte) 71,(byte) 36,(byte) 93,(byte) 2};
	private final static byte[] P_PROFILE_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 19,(byte) 93,(byte) 2};
	private final static byte[] P_NROF_AUTHED_BUDDIES_req = {(byte) 90,(byte) 71,(byte) 28,(byte) 93,(byte) 2};
	private final static byte[] P_IP_COUNTRY_req = {(byte) 90,(byte) 71,(byte) 29,(byte) 93,(byte) 2};
	private final static byte[] P_AVATAR_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 182,(byte) 1,(byte) 93,(byte) 2};
	private final static byte[] P_MOOD_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 183,(byte) 1,(byte) 93,(byte) 2};
	private final static byte[] P_RECEIVED_AUTH_REQUEST_req = {(byte) 90,(byte) 71,(byte) 20,(byte) 93,(byte) 2};
	private final static byte[] P_AUTH_REQUEST_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 25,(byte) 93,(byte) 2};
	private final static byte[] P_LAST_ONLINE_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 35,(byte) 93,(byte) 2};
	private final static byte[] P_AVAILABILITY_req = {(byte) 90,(byte) 71,(byte) 34,(byte) 93,(byte) 2};
	private final static byte[] P_DISPLAY_NAME_req = {(byte) 90,(byte) 71,(byte) 21,(byte) 93,(byte) 2};
	private final static byte[] P_REFRESHING_req = {(byte) 90,(byte) 71,(byte) 22,(byte) 93,(byte) 2};
	private final static byte[] P_GIVEN_AUTH_LEVEL_req = {(byte) 90,(byte) 71,(byte) 23,(byte) 93,(byte) 2};
	private final static byte[] P_GIVEN_DISPLAY_NAME_req = {(byte) 90,(byte) 71,(byte) 33,(byte) 93,(byte) 2};
	private final static byte[] P_ASSIGNED_COMMENT_req = {(byte) 90,(byte) 71,(byte) 180,(byte) 1,(byte) 93,(byte) 2};
	private final static byte[] P_LAST_USED_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 39,(byte) 93,(byte) 2};
	private final static byte[] P_AUTH_REQUEST_COUNT_req = {(byte) 90,(byte) 71,(byte) 41,(byte) 93,(byte) 2};
	private final static byte[] P_ASSIGNED_PHONE1_req = {(byte) 90,(byte) 71,(byte) 184,(byte) 1,(byte) 93,(byte) 2};
	private final static byte[] P_ASSIGNED_PHONE1_LABEL_req = {(byte) 90,(byte) 71,(byte) 185,(byte) 1,(byte) 93,(byte) 2};
	private final static byte[] P_ASSIGNED_PHONE2_req = {(byte) 90,(byte) 71,(byte) 186,(byte) 1,(byte) 93,(byte) 2};
	private final static byte[] P_ASSIGNED_PHONE2_LABEL_req = {(byte) 90,(byte) 71,(byte) 187,(byte) 1,(byte) 93,(byte) 2};
	private final static byte[] P_ASSIGNED_PHONE3_req = {(byte) 90,(byte) 71,(byte) 188,(byte) 1,(byte) 93,(byte) 2};
	private final static byte[] P_ASSIGNED_PHONE3_LABEL_req = {(byte) 90,(byte) 71,(byte) 189,(byte) 1,(byte) 93,(byte) 2};
	private final static byte[] P_POPULARITY_ORD_req = {(byte) 90,(byte) 71,(byte) 42,(byte) 93,(byte) 2};
	/** Properties of the Contact class */
	public enum Property implements PropertyEnumConverting {
		P_UNKNOWN               (0,0,null,0,null),
		P_TYPE                  (202, 1, P_TYPE_req, 0, Type.get(0)),
		P_SKYPE_NAME            (4, 2, P_SKYPE_NAME_req, 0, null),
		P_PSTN_NUMBER           (6, 3, P_PSTN_NUMBER_req, 0, null),
		P_FULL_NAME             (5, 4, P_FULL_NAME_req, 0, null),
		P_BIRTHDAY              (7, 5, P_BIRTHDAY_req, 0, null),
		P_GENDER                (8, 6, P_GENDER_req, 0, null),
		P_LANGUAGES             (9, 7, P_LANGUAGES_req, 0, null),
		P_COUNTRY               (10, 8, P_COUNTRY_req, 0, null),
		P_PROVINCE              (11, 9, P_PROVINCE_req, 0, null),
		P_CITY                  (12, 10, P_CITY_req, 0, null),
		P_PHONE_HOME            (13, 11, P_PHONE_HOME_req, 0, null),
		P_PHONE_OFFICE          (14, 12, P_PHONE_OFFICE_req, 0, null),
		P_PHONE_MOBILE          (15, 13, P_PHONE_MOBILE_req, 0, null),
		P_EMAILS                (16, 14, P_EMAILS_req, 0, null),
		P_HOMEPAGE              (17, 15, P_HOMEPAGE_req, 0, null),
		P_ABOUT                 (18, 16, P_ABOUT_req, 0, null),
		P_AVATAR_IMAGE          (37, 17, P_AVATAR_IMAGE_req, 0, null),
		P_MOOD_TEXT             (26, 18, P_MOOD_TEXT_req, 0, null),
		P_RICH_MOOD_TEXT        (205, 19, P_RICH_MOOD_TEXT_req, 0, null),
		P_TIMEZONE              (27, 20, P_TIMEZONE_req, 0, null),
		P_CAPABILITIES          (36, 21, P_CAPABILITIES_req, 0, null),
		P_PROFILE_TIMESTAMP     (19, 22, P_PROFILE_TIMESTAMP_req, 0, null),
		P_NROF_AUTHED_BUDDIES   (28, 23, P_NROF_AUTHED_BUDDIES_req, 0, null),
		P_IP_COUNTRY            (29, 24, P_IP_COUNTRY_req, 0, null),
		P_AVATAR_TIMESTAMP      (182, 25, P_AVATAR_TIMESTAMP_req, 0, null),
		P_MOOD_TIMESTAMP        (183, 26, P_MOOD_TIMESTAMP_req, 0, null),
		P_RECEIVED_AUTH_REQUEST (20, 27, P_RECEIVED_AUTH_REQUEST_req, 0, null),
		P_AUTH_REQUEST_TIMESTAMP(25, 28, P_AUTH_REQUEST_TIMESTAMP_req, 0, null),
		P_LAST_ONLINE_TIMESTAMP (35, 29, P_LAST_ONLINE_TIMESTAMP_req, 0, null),
		P_AVAILABILITY          (34, 30, P_AVAILABILITY_req, 0, Availability.get(0)),
		P_DISPLAY_NAME          (21, 31, P_DISPLAY_NAME_req, 0, null),
		P_REFRESHING            (22, 32, P_REFRESHING_req, 0, null),
		P_GIVEN_AUTH_LEVEL      (23, 33, P_GIVEN_AUTH_LEVEL_req, 0, AuthLevel.get(0)),
		P_GIVEN_DISPLAY_NAME    (33, 34, P_GIVEN_DISPLAY_NAME_req, 0, null),
		P_ASSIGNED_COMMENT      (180, 35, P_ASSIGNED_COMMENT_req, 0, null),
		P_LAST_USED_TIMESTAMP   (39, 36, P_LAST_USED_TIMESTAMP_req, 0, null),
		P_AUTH_REQUEST_COUNT    (41, 37, P_AUTH_REQUEST_COUNT_req, 0, null),
		P_ASSIGNED_PHONE1       (184, 38, P_ASSIGNED_PHONE1_req, 0, null),
		P_ASSIGNED_PHONE1_LABEL (185, 39, P_ASSIGNED_PHONE1_LABEL_req, 0, null),
		P_ASSIGNED_PHONE2       (186, 40, P_ASSIGNED_PHONE2_req, 0, null),
		P_ASSIGNED_PHONE2_LABEL (187, 41, P_ASSIGNED_PHONE2_LABEL_req, 0, null),		P_ASSIGNED_PHONE3       (188, 42, P_ASSIGNED_PHONE3_req, 0, null),
		P_ASSIGNED_PHONE3_LABEL (189, 43, P_ASSIGNED_PHONE3_LABEL_req, 0, null),
		P_POPULARITY_ORD        (42, 44, P_POPULARITY_ORD_req, 0, null);
		private final int    key;
		private final int    idx;
		private final byte[] req;
		private final int    mod;
		private final EnumConverting enumConverter;
		Property(int key, int idx, byte[] req, int mod, EnumConverting converter) {
			this.key = key;
			this.idx = idx;
			this.req = req;
			this.mod = mod;
			this.enumConverter = converter;
		};
		public boolean  isCached()    { return idx > 0;   }
		public int      getIdx()      { return idx;       }
		public int      getId()       { return key;       }
		public byte[]   getRequest()  { return req;       }
		public EnumConverting getDefault()  { return P_UNKNOWN; }
		public int      getModuleId() { return mod;       }
		public EnumConverting getEnumConverter()    { return enumConverter;   }
		public EnumConverting convert(final int from) { return Property.get(from); }
		public EnumConverting[] getArray(final int size) { return new Property[size]; }
		public static Property get(final int from) {
			switch (from) {
			case 202: return P_TYPE;
			case   4: return P_SKYPE_NAME;
			case   6: return P_PSTN_NUMBER;
			case   5: return P_FULL_NAME;
			case   7: return P_BIRTHDAY;
			case   8: return P_GENDER;
			case   9: return P_LANGUAGES;
			case  10: return P_COUNTRY;
			case  11: return P_PROVINCE;
			case  12: return P_CITY;
			case  13: return P_PHONE_HOME;
			case  14: return P_PHONE_OFFICE;
			case  15: return P_PHONE_MOBILE;
			case  16: return P_EMAILS;
			case  17: return P_HOMEPAGE;
			case  18: return P_ABOUT;
			case  37: return P_AVATAR_IMAGE;
			case  26: return P_MOOD_TEXT;
			case 205: return P_RICH_MOOD_TEXT;
			case  27: return P_TIMEZONE;
			case  36: return P_CAPABILITIES;
			case  19: return P_PROFILE_TIMESTAMP;
			case  28: return P_NROF_AUTHED_BUDDIES;
			case  29: return P_IP_COUNTRY;
			case 182: return P_AVATAR_TIMESTAMP;
			case 183: return P_MOOD_TIMESTAMP;
			case  20: return P_RECEIVED_AUTH_REQUEST;
			case  25: return P_AUTH_REQUEST_TIMESTAMP;
			case  35: return P_LAST_ONLINE_TIMESTAMP;
			case  34: return P_AVAILABILITY;
			case  21: return P_DISPLAY_NAME;
			case  22: return P_REFRESHING;
			case  23: return P_GIVEN_AUTH_LEVEL;
			case  33: return P_GIVEN_DISPLAY_NAME;
			case 180: return P_ASSIGNED_COMMENT;
			case  39: return P_LAST_USED_TIMESTAMP;
			case  41: return P_AUTH_REQUEST_COUNT;
			case 184: return P_ASSIGNED_PHONE1;
			case 185: return P_ASSIGNED_PHONE1_LABEL;
			case 186: return P_ASSIGNED_PHONE2;
			case 187: return P_ASSIGNED_PHONE2_LABEL;
			case 188: return P_ASSIGNED_PHONE3;
			case 189: return P_ASSIGNED_PHONE3_LABEL;
			case  42: return P_POPULARITY_ORD;
			}
			return P_UNKNOWN;
		}
		public static final int P_TYPE_VALUE                   = 202;
		public static final int P_SKYPE_NAME_VALUE             =   4;
		public static final int P_PSTN_NUMBER_VALUE            =   6;
		public static final int P_FULL_NAME_VALUE              =   5;
		public static final int P_BIRTHDAY_VALUE               =   7;
		public static final int P_GENDER_VALUE                 =   8;
		public static final int P_LANGUAGES_VALUE              =   9;
		public static final int P_COUNTRY_VALUE                =  10;
		public static final int P_PROVINCE_VALUE               =  11;
		public static final int P_CITY_VALUE                   =  12;
		public static final int P_PHONE_HOME_VALUE             =  13;
		public static final int P_PHONE_OFFICE_VALUE           =  14;
		public static final int P_PHONE_MOBILE_VALUE           =  15;
		public static final int P_EMAILS_VALUE                 =  16;
		public static final int P_HOMEPAGE_VALUE               =  17;
		public static final int P_ABOUT_VALUE                  =  18;
		public static final int P_AVATAR_IMAGE_VALUE           =  37;
		public static final int P_MOOD_TEXT_VALUE              =  26;
		public static final int P_RICH_MOOD_TEXT_VALUE         = 205;
		public static final int P_TIMEZONE_VALUE               =  27;
		public static final int P_CAPABILITIES_VALUE           =  36;
		public static final int P_PROFILE_TIMESTAMP_VALUE      =  19;
		public static final int P_NROF_AUTHED_BUDDIES_VALUE    =  28;
		public static final int P_IP_COUNTRY_VALUE             =  29;
		public static final int P_AVATAR_TIMESTAMP_VALUE       = 182;
		public static final int P_MOOD_TIMESTAMP_VALUE         = 183;
		public static final int P_RECEIVED_AUTH_REQUEST_VALUE  =  20;
		public static final int P_AUTH_REQUEST_TIMESTAMP_VALUE =  25;
		public static final int P_LAST_ONLINE_TIMESTAMP_VALUE  =  35;
		public static final int P_AVAILABILITY_VALUE           =  34;
		public static final int P_DISPLAY_NAME_VALUE           =  21;
		public static final int P_REFRESHING_VALUE             =  22;
		public static final int P_GIVEN_AUTH_LEVEL_VALUE       =  23;
		public static final int P_GIVEN_DISPLAY_NAME_VALUE     =  33;
		public static final int P_ASSIGNED_COMMENT_VALUE       = 180;
		public static final int P_LAST_USED_TIMESTAMP_VALUE    =  39;
		public static final int P_AUTH_REQUEST_COUNT_VALUE     =  41;
		public static final int P_ASSIGNED_PHONE1_VALUE        = 184;
		public static final int P_ASSIGNED_PHONE1_LABEL_VALUE  = 185;
		public static final int P_ASSIGNED_PHONE2_VALUE        = 186;
		public static final int P_ASSIGNED_PHONE2_LABEL_VALUE  = 187;
		public static final int P_ASSIGNED_PHONE3_VALUE        = 188;
		public static final int P_ASSIGNED_PHONE3_LABEL_VALUE  = 189;
		public static final int P_POPULARITY_ORD_VALUE         =  42;
		public static final Property[] mget_info_mreq = { P_AVAILABILITY, P_DISPLAY_NAME };
		public static final Property[] mget_profile_mreq = { P_SKYPE_NAME, P_PSTN_NUMBER, P_FULL_NAME, P_MOOD_TEXT, P_EMAILS, P_PHONE_HOME, P_PHONE_OFFICE, P_PHONE_MOBILE, P_BIRTHDAY, P_GENDER, P_LANGUAGES, P_COUNTRY, P_PROVINCE, P_CITY, P_HOMEPAGE, P_ABOUT, P_TIMEZONE };
	}
	private final static byte[] getIdentity_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 2};
	/**
	 * getIdentity
	 * @return identity returns CONTACT_SKYPENAME or CONTACT_PSTNNUMBER value
	 */
	public String getIdentity() {
		try {
			return sidDoRequest(getIdentity_req)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] getAvatar_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 4};
	public class GetAvatarResponse {
		public boolean present;
		public byte[] avatar;
	};
	
	/** Returns Conrtact's avatar image (JPG). 
	 * @return GetAvatarResponse
	 * <br> - present 
	 *  - true: the Contact has a custom avatar image 
	 *  - false: the Contact does not have a custom avatar image 
	
	 * <br> - avatar The avatar image data (JPG). If present is false, this will be the Skype-assigned default avatar 
	 */
	public GetAvatarResponse getAvatar() {
		try {
			Decoding decoder = sidDoRequest(getAvatar_req)
			.endRequest();
			GetAvatarResponse result = new GetAvatarResponse();
			result.present = decoder.getBoolParm(1, false);
			result.avatar = decoder.getBinaryParm(2, true);
			return result;
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getVerifiedEmail_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 3};
	/** returns verified-by-Skype e-mail for this contact if exists and verifiable
	 * @return email
	 */
	public String getVerifiedEmail() {
		try {
			return sidDoRequest(getVerifiedEmail_req)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] getVerifiedCompany_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 8};
	/** returns verified-by-Skype company for this contact if exists and verifiable
	 * @return company
	 */
	public String getVerifiedCompany() {
		try {
			return sidDoRequest(getVerifiedCompany_req)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] isMemberOf_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 6};	/** Checks whether the contact is member of a contact group given in group reference argument. 
	 * @param group The target contact group 
	 * @return result 
	 *  - true: the Contact is a member of the target contact group 
	 *  - false: the Contact is not a member of the target contact group 
	
	 */
	public boolean isMemberOf(ContactGroup group) {
		try {
			return sidDoRequest(isMemberOf_req)
			.addObjectParm(1, group)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] isMemberOfHardwiredGroup_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 7};
	/** Checks whether the contact is member of a pre-defined contact group given in the TYPE argument (type for this property comes from the ContactGroup class). 
	 * @param groupType The type designator of the target pre-defined contact group. For example, specify this parameter as ContactGroup.TYPE.RECENTLY_CONTACTED_CONTACTS to determine if you've had a recent conversation with this Contact. 
	 * @return result 
	 *  - true: the Contact is a member of the target contact group 
	 *  - false: the Contact is not a member of the target contact group 
	
	 */
	public boolean isMemberOfHardwiredGroup(ContactGroup.Type groupType) {
		try {
			return sidDoRequest(isMemberOfHardwiredGroup_req)
			.addEnumParm(1, groupType)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] setBlocked_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 22};
	/** Blocks or unblocks any further incoming communication attempts from this contact. 
	 * @param blocked 
	 *  - true: block this contact 
	 *  - false: unblock this contact 
	
	 * @param abuse Optional parameter to report abuse by this Skype user when blocking this Contact. Note that you can specifiy this parameter as true only when blocking a Contact. Defaults to false if omitted. 
	 */
	public void setBlocked(boolean blocked, boolean abuse) {
		try {
			sidDoRequest(setBlocked_req)
			.addBoolParm(1, blocked)
			.addBoolParm(2, abuse)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] ignoreAuthRequest_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 21};
	/** Rejects and removes a pending authorization request from this Contact. 
	 */
	public void ignoreAuthRequest() {
		try {
			sidDoRequest(ignoreAuthRequest_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] giveDisplayName_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 10};
	/** sets CONTACT_GIVEN_DISPLAYNAME. clears if size(name)==0
	 * @param name
	 */
	public void giveDisplayName(String name) {
		try {
			sidDoRequest(giveDisplayName_req)
			.addStringParm(1, name)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setBuddyStatus_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 12};
	/** Adds or removes this Contact from the ALL_BUDDIES hardwired group. 
	 * @param isMyBuddy 
	 *  - true: add this contact to the ALL_BUDDIES group 
	 *  - false: delete contact from the ALL_BUDDIES group 
	
	 * @param syncAuth This argument is deprecated and should not be used. 
	 */
	public void setBuddyStatus(boolean isMyBuddy, boolean syncAuth) {
		try {
			sidDoRequest(setBuddyStatus_req)
			.addBoolParm(1, isMyBuddy)
			.addBoolParm(2, syncAuth, true)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] sendAuthRequest_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 13};
	/** Sends a contact authorization request to this user. 
	 * @param message Text that typically introduces the requesting user and details the reason for the authorization request. This text will be set as Message.P_BODY_XML property of the notification Message the remote user will receive upon getting the authortization request. While this can be the empty string, it cannot be null. 
	 * @param extrasBitmask Indicates additional information to include with this authorization request: 
	 *  - 0 (zero): do not include any additional information 
	 *  - SEND_VERIFIED_EMAIL: include the requestor's verified e-mail address 
	 *  - SEND_VERIFIED_COMPANY: include verified information regarding the requestor's company 
	 *  - SEND_VERIFIED_EMAIL + SEND_VERIFIED_COMPANY: include both e-mail and company information 
	
	 */
	public void sendAuthRequest(String message, int extrasBitmask) {
		try {
			sidDoRequest(sendAuthRequest_req)
			.addStringParm(1, message)
			.addUintParm(2, extrasBitmask)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] hasAuthorizedMe_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 14};
	/** the contact has accepted my auth request
	 * @return result
	 */
	public boolean hasAuthorizedMe() {
		try {
			return sidDoRequest(hasAuthorizedMe_req)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] setPhoneNumber_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 15};
	/**
	 * Sets the three P_ASSIGNED_PHONEx and P_ASSIGNED_PHONEx_LABEL properties, where x reflects the value of num. Basically, this enables you to add up to 3 additional phone numbers to a contact, that can then be used for calling them, should they be offline. 
	 * 
	 * The P_ASSIGNED.. properties are Contact properties, not Account properties. These you can set for other people in local user's contact list. The values for these properties will synchronize over different instances the local user has logged in. This synchronization is not instantaneous. To force synchronization, re-login on both instances seems to work quite nicely. 
	 * 
	 * The Skype Windows desktop client uses the following conventions when interpreting and assigning values to these properties. While your solution can assign arbitrary values to these properties, we strongly recommend using these conventions to ensure interoperability with the Skype Windows desktop client. Keep in mind that the "number" of these property pairs has no relationship to how the Skype Windows client interprets their label property value strings. For example, the Skype Windows client will interpret P_ASSIGNED_PHONE3_LABEL as "Home" if its value is the string "0".  
	 * 
	 * Label strings: 
	 * 
	 * Populate the label properties with string representations of the numbers "0" through "3", rather than descriptive strings like "Home", "Mobile", and so forth. The Skype desktop clients interpret the numeric string values as: 
	 *  - "0" (zero) - "Home" 
	 *  - "1" (one) - "Office" 
	 *  - "2" - "Mobile" 
	 *  - "3" - "Other" 
	 * 
	 * Keep in mind that the "number" of a property pair has no relationship to its label string. For example, the Skype Windows client will interpret P_ASSIGNED_PHONE3_LABEL as "Home" if its value is the string "0". 
	 * 
	 * Phone number strings: 
	 * 
	 * The Skype Windows desktop client has distinct conventions for Skype Contacts and PSTN (SkypeOut) Contacts: any or all of the property pairs can be used for Skype Contacts; P_ASSIGNED_PHONE1 cannot be used for PSTN Contacts and P_ASSIGNED_PHONE1_LABEL has special meaning for PSTN Contacts. 
	 * Specifically, the Skype desktop clients use P_ASSIGNED_PHONE1_LABEL as the label for a PSTN Contact's primary number (regardless of whether it's home, mobile, or office), and use P_PSTNNUMBER to hold the actual number. 
	
	 * @param num The property pair being set, which must be in the range 1..3 Note that there are only three target slots here, for four ("0".."3") label values. 
	 * @param label The label text for the property being set 
	 * @param number The phone number for the property being set 
	 */
	public void setPhoneNumber(int num, String label, String number) {
		try {
			sidDoRequest(setPhoneNumber_req)
			.addUintParm(1, num)
			.addStringParm(2, label)
			.addStringParm(3, number)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] openConversation_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 17};	/** Retrieves a dialog conversation with the Contact. 
	 * @return conversation Retrieved dialog. 
	 */
	public Conversation openConversation() {
		try {
			return (Conversation) sidDoRequest(openConversation_req)
			.endRequest().getObjectParm(1, 18, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] hasCapability_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 18};
	/** Retrieves a Contact.Capability value. Sets P_REFRESHING to true while querying from server. This method is functionally the same as Contact.GetCapabilityStatus except that it returns a bool value rather than Contact.CAPABILITYSTATUS 
	 * @param capability The target capability 
	 * @param queryServer 
	 *  - true: obtains the data from the p2p network/server, and sets P_REFRESHING to true for the duration  
	 *  - false: obtains the data from the local client 
	
	 * @return result 
	 *  - true: the Contact has the target capability through at least one of the Skype clients they have logged into. Corresponds to CAPABILITY_MIXED and CAPABILITY_EXISTS 
	 *  - false: the Contact does not have the target capability. Corresponds to NO_CAPABILITY 
	
	 */
	public boolean hasCapability(Capability capability, boolean queryServer) {
		try {
			return sidDoRequest(hasCapability_req)
			.addEnumParm(1, capability)
			.addBoolParm(2, queryServer)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] getCapabilityStatus_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 19};
	/** Retrieves a Contact.Capability value. Sets P_REFRESHING to true while querying from server. 
	 * @param capability The target capability, see Contact.Capability enumerator. 
	 * @param queryServer 
	 *  - true: obtains the data from the p2p network/server, and sets P_REFRESHING to true for the duration  
	 *  - false: obtains the data from the local client 
	
	 * @return status Status of the target capability. 
	 */
	public Capabilitystatus getCapabilityStatus(Capability capability, boolean queryServer) {
		try {
			return (Capabilitystatus) sidDoRequest(getCapabilityStatus_req)
			.addEnumParm(1, capability)
			.addBoolParm(2, queryServer)
			.endRequest().getEnumParm(1, Capabilitystatus.get(0), true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return Capabilitystatus.get(0);
		}
	}
	private final static byte[] refreshProfile_req = {(byte) 90,(byte) 82,(byte) 2,(byte) 20};
	/** Refreshes all properties, capabilities, and statuses associated with this Contact from p2p/CBL, and sets P_REFRESHING to true for the duration. 
	 */
	public void refreshProfile() {
		try {
			sidDoRequest(refreshProfile_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	/***
	 * generic multiget of a list of Property
	 * @param requested the list of requested properties of Contact
	 * @return SidGetResponding
	 */
	public SidGetResponding sidMultiGet(Property[] requested) {
		return super.sidMultiGet(requested);
	}
	/***
	 * generic multiget of list of Property for a list of Contact
	 * @param requested the list of requested properties
	 * @return SidGetResponding[] can be casted to (Contact[]) if all properties are cached
	 */
	static public SidGetResponding[] sidMultiGet(Property[] requested, Contact[] objects) {
		return SidObject.sidMultiGet(requested, objects);
	}
	/*** multiget the following properties
	 * - P_AVAILABILITY
	 * - P_DISPLAY_NAME
	 */
	public Contact mgetInfo() {
		return (Contact) super.sidMultiGet(Property.mget_info_mreq, this);
	}
	/*** multiget the following properties for a list of Contact
	 * - P_AVAILABILITY
	 * - P_DISPLAY_NAME
	 * @param objects targets of the request
	 * @return Contact[] responses
	 */
	static public Contact[] mgetInfo(Contact[] objects) {
		return (Contact[]) SidObject.sidMultiGet(Property.mget_info_mreq, objects, objects);
	}
	/*** multiget the following properties
	 * - P_SKYPE_NAME
	 * - P_PSTN_NUMBER
	 * - P_FULL_NAME
	 * - P_MOOD_TEXT
	 * - P_EMAILS
	 * - P_PHONE_HOME
	 * - P_PHONE_OFFICE
	 * - P_PHONE_MOBILE
	 * - P_BIRTHDAY
	 * - P_GENDER
	 * - P_LANGUAGES
	 * - P_COUNTRY
	 * - P_PROVINCE
	 * - P_CITY
	 * - P_HOMEPAGE
	 * - P_ABOUT
	 * - P_TIMEZONE
	 */
	public Contact mgetProfile() {
		return (Contact) super.sidMultiGet(Property.mget_profile_mreq, this);
	}
	public Type getType() {
		synchronized(this) {
			if ((mSidCached & 0x1) != 0)
				return mType;
		}
		return (Type) sidRequestEnumProperty(Property.P_TYPE);
	}
	/** defined if it is a SKYPE contact */
	public String getSkypeName() {
		synchronized(this) {
			if ((mSidCached & 0x2) != 0)
				return mSkypeName;
		}
		return sidRequestStringProperty(Property.P_SKYPE_NAME);
	}
	public String getPstnNumber() {
		synchronized(this) {
			if ((mSidCached & 0x4) != 0)
				return mPstnNumber;
		}
		return sidRequestStringProperty(Property.P_PSTN_NUMBER);
	}
	public String getFullName() {
		synchronized(this) {
			if ((mSidCached & 0x8) != 0)
				return mFullName;
		}
		return sidRequestStringProperty(Property.P_FULL_NAME);
	}
	/** integer of YYYYMMDD format */
	public int getBirthday() {
		synchronized(this) {
			if ((mSidCached & 0x10) != 0)
				return mBirthday;
		}
		return sidRequestUintProperty(Property.P_BIRTHDAY);
	}
	/** 1-male, 2-female */
	public int getGender() {
		synchronized(this) {
			if ((mSidCached & 0x20) != 0)
				return mGender;
		}
		return sidRequestUintProperty(Property.P_GENDER);
	}
	/** ISO language code list, space separated */
	public String getLanguages() {
		synchronized(this) {
			if ((mSidCached & 0x40) != 0)
				return mLanguages;
		}
		return sidRequestStringProperty(Property.P_LANGUAGES);
	}
	/** ISO country code */
	public String getCountry() {
		synchronized(this) {
			if ((mSidCached & 0x80) != 0)
				return mCountry;
		}
		return sidRequestStringProperty(Property.P_COUNTRY);
	}
	public String getProvince() {
		synchronized(this) {
			if ((mSidCached & 0x100) != 0)
				return mProvince;
		}
		return sidRequestStringProperty(Property.P_PROVINCE);
	}
	public String getCity() {
		synchronized(this) {
			if ((mSidCached & 0x200) != 0)
				return mCity;
		}
		return sidRequestStringProperty(Property.P_CITY);
	}
	/** This corresponds to the Account.P_PHONE_HOME property. The value is set by the remote user in Account profile. As Contact property, this is read-only.  */
	public String getPhoneHome() {
		synchronized(this) {
			if ((mSidCached & 0x400) != 0)
				return mPhoneHome;
		}
		return sidRequestStringProperty(Property.P_PHONE_HOME);
	}
	/** This corresponds to the Account.P_PHONE_OFFICE property. The value is set by the remote user in Account profile. As Contact property, this is read-only. If the Contact has not populated his Account profile with sufficient phone numbers, the UI should implement locally adding additional phone numbers to Contact P_ASSIGNED_PHONE1 .. P_ASSIGNED_PHONE3 properties (and corresponding labels). See Contact.SetPhoneNumber method for more information.  */
	public String getPhoneOffice() {
		synchronized(this) {
			if ((mSidCached & 0x800) != 0)
				return mPhoneOffice;
		}
		return sidRequestStringProperty(Property.P_PHONE_OFFICE);
	}
	/** This corresponds to the Account.P_PHONE_MOBILE property. The value is set by the remote user in Account profile. As Contact property, this is read-only.  */
	public String getPhoneMobile() {
		synchronized(this) {
			if ((mSidCached & 0x1000) != 0)
				return mPhoneMobile;
		}
		return sidRequestStringProperty(Property.P_PHONE_MOBILE);
	}
	/** will be hashed before advertising/querying, space separated */
	public String getEmails() {
		synchronized(this) {
			if ((mSidCached & 0x2000) != 0)
				return mEmails;
		}
		return sidRequestStringProperty(Property.P_EMAILS);
	}
	public String getHomepage() {
		synchronized(this) {
			if ((mSidCached & 0x4000) != 0)
				return mHomepage;
		}
		return sidRequestStringProperty(Property.P_HOMEPAGE);
	}
	/** arbitrary introductory text */
	public String getAbout() {
		synchronized(this) {
			if ((mSidCached & 0x8000) != 0)
				return mAbout;
		}
		return sidRequestStringProperty(Property.P_ABOUT);	}
	/** Contact avatar pictures are in JPG format. The original size of avatar pictures are in no more than 96 x 96 pixels in size. However, as they can be smaller, scaling the pictures up too much to fit your UI can lead to distorted images.  */
	public byte[] getAvatarImage() {
		synchronized(this) {
			if ((mSidCached & 0x10000) != 0)
				return mAvatarImage;
		}
		return sidRequestBinaryProperty(Property.P_AVATAR_IMAGE);
	}
	/** Personal mood message (visible to authorized users only).   */
	public String getMoodText() {
		synchronized(this) {
			if ((mSidCached & 0x20000) != 0)
				return mMoodText;
		}
		return sidRequestStringProperty(Property.P_MOOD_TEXT);
	}
	/** XML version of personal mood text */
	public String getRichMoodText() {
		synchronized(this) {
			if ((mSidCached & 0x40000) != 0)
				return mRichMoodText;
		}
		return sidRequestXmlProperty(Property.P_RICH_MOOD_TEXT);
	}
	/** 24*3600+diff_to_UTC_in_seconds. NB! changes with DST  */
	public int getTimezone() {
		synchronized(this) {
			if ((mSidCached & 0x80000) != 0)
				return mTimezone;
		}
		return sidRequestUintProperty(Property.P_TIMEZONE);
	}
	/** binary tag that can be queried via Contact.HasCapability() */
	public byte[] getCapabilities() {
		synchronized(this) {
			if ((mSidCached & 0x100000) != 0)
				return mCapabilities;
		}
		return sidRequestBinaryProperty(Property.P_CAPABILITIES);
	}
	/** UNIX timestamp of last profile change */
	public long getProfileTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x200000) != 0)
				return mProfileTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_PROFILE_TIMESTAMP);
	}
	/** count of this user's authorized contacts  */
	public int getNrofAuthedBuddies() {
		synchronized(this) {
			if ((mSidCached & 0x400000) != 0)
				return mNrofAuthedBuddies;
		}
		return sidRequestUintProperty(Property.P_NROF_AUTHED_BUDDIES);
	}
	/** ISO country code assigned by the IP */
	public String getIpCountry() {
		synchronized(this) {
			if ((mSidCached & 0x800000) != 0)
				return mIpCountry;
		}
		return sidRequestStringProperty(Property.P_IP_COUNTRY);
	}
	/** UNIX timestamp of when current avatar was set */
	public long getAvatarTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x1000000) != 0)
				return mAvatarTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_AVATAR_TIMESTAMP);
	}
	/** NOT SET FOR CONTACTS. For Account object, UNIX timestamp of when current mood was set */
	public long getMoodTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x2000000) != 0)
				return mMoodTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_MOOD_TIMESTAMP);
	}
	/** set if the contact is waiting to be authorized. The value contains auth. request text  */
	public String getReceivedAuthRequest() {
		synchronized(this) {
			if ((mSidCached & 0x4000000) != 0)
				return mReceivedAuthRequest;
		}
		return sidRequestStringProperty(Property.P_RECEIVED_AUTH_REQUEST);
	}
	/** timestamp of last received auth-request */
	public long getAuthRequestTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x8000000) != 0)
				return mAuthRequestTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_AUTH_REQUEST_TIMESTAMP);
	}
	/** X timestamp of last successful ping to that user */
	public long getLastOnlineTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x10000000) != 0)
				return mLastOnlineTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_LAST_ONLINE_TIMESTAMP);
	}
	/** Contact.AVAILABILITY */
	public Availability getAvailability() {
		synchronized(this) {
			if ((mSidCached & 0x20000000) != 0)
				return mAvailability;
		}
		return (Availability) sidRequestEnumProperty(Property.P_AVAILABILITY);
	}
	/** always set (assigned by lib by looking at various fields) */
	public String getDisplayName() {
		synchronized(this) {
			if ((mSidCached & 0x40000000) != 0)
				return mDisplayName;
		}
		return sidRequestStringProperty(Property.P_DISPLAY_NAME);
	}
	/** true if querying additional information from p2p or server  */
	public boolean getRefreshing() {
		synchronized(this) {
			if ((mSidCached & 0x80000000) != 0)
				return mRefreshing;
		}
		return sidRequestBoolProperty(Property.P_REFRESHING);
	}
	/** Contact.AUTHLEVEL, change via Contact.GiveAuthlevel() */
	public AuthLevel getGivenAuthLevel() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x1) != 0)
				return mGivenAuthLevel;
		}
		return (AuthLevel) sidRequestEnumProperty(Property.P_GIVEN_AUTH_LEVEL);
	}
	/** change via Contact.GiveDisplayname() */
	public String getGivenDisplayName() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x2) != 0)
				return mGivenDisplayName;
		}
		return sidRequestStringProperty(Property.P_GIVEN_DISPLAY_NAME);
	}
	/** change via Contact.AssignComment() */
	public String getAssignedComment() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x4) != 0)
				return mAssignedComment;
		}
		return sidRequestStringProperty(Property.P_ASSIGNED_COMMENT);
	}
	/** UNIX timestamp of last outbound session (call, chat, FT, etc) */
	public long getLastUsedTimestamp() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x8) != 0)
				return mLastUsedTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_LAST_USED_TIMESTAMP);
	}
	/** for contacts that have CONTACT_RECEIVED_AUTHREQUEST, how many times in a row they have requested it without positive answer */
	public int getAuthRequestCount() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x10) != 0)
				return mAuthRequestCount;
		}
		return sidRequestUintProperty(Property.P_AUTH_REQUEST_COUNT);
	}
	/** See Contact.SetPhoneNumber method for more information.  */
	public String getAssignedPhone1() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x20) != 0)
				return mAssignedPhone1;
		}
		return sidRequestStringProperty(Property.P_ASSIGNED_PHONE1);
	}
	/** See Contact.SetPhoneNumber method for more information.  */
	public String getAssignedPhone1Label() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x40) != 0)
				return mAssignedPhone1Label;
		}
		return sidRequestStringProperty(Property.P_ASSIGNED_PHONE1_LABEL);
	}
	/** See Contact.SetPhoneNumber method for more information.  */
	public String getAssignedPhone2() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x80) != 0)
				return mAssignedPhone2;
		}
		return sidRequestStringProperty(Property.P_ASSIGNED_PHONE2);
	}
	/** See Contact.SetPhoneNumber method for more information.  */
	public String getAssignedPhone2Label() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x100) != 0)
				return mAssignedPhone2Label;
		}
		return sidRequestStringProperty(Property.P_ASSIGNED_PHONE2_LABEL);
	}
	/** See Contact.SetPhoneNumber method for more information.  */
	public String getAssignedPhone3() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x200) != 0)
				return mAssignedPhone3;
		}
		return sidRequestStringProperty(Property.P_ASSIGNED_PHONE3);
	}
	/** See Contact.SetPhoneNumber method for more information.  */
	public String getAssignedPhone3Label() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x400) != 0)
				return mAssignedPhone3Label;
		}
		return sidRequestStringProperty(Property.P_ASSIGNED_PHONE3_LABEL);
	}
	/** Contact's order by presence popularity.  */
	public int getPopularityOrd() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x800) != 0)
				return mPopularityOrd;
		}
		return sidRequestUintProperty(Property.P_POPULARITY_ORD);
	}
	public String sidGetStringProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 4:
			return mSkypeName;
		case 6:
			return mPstnNumber;
		case 5:
			return mFullName;
		case 9:
			return mLanguages;
		case 10:
			return mCountry;
		case 11:
			return mProvince;
		case 12:
			return mCity;
		case 13:
			return mPhoneHome;
		case 14:
			return mPhoneOffice;
		case 15:
			return mPhoneMobile;
		case 16:
			return mEmails;
		case 17:
			return mHomepage;
		case 18:
			return mAbout;
		case 26:
			return mMoodText;
		case 205:
			return mRichMoodText;
		case 29:
			return mIpCountry;
		case 20:
			return mReceivedAuthRequest;
		case 21:
			return mDisplayName;
		case 33:			return mGivenDisplayName;
		case 180:
			return mAssignedComment;
		case 184:
			return mAssignedPhone1;
		case 185:
			return mAssignedPhone1Label;
		case 186:
			return mAssignedPhone2;
		case 187:
			return mAssignedPhone2Label;
		case 188:
			return mAssignedPhone3;
		case 189:
			return mAssignedPhone3Label;
		}
		return "";
	}
	public boolean sidGetBoolProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 22);
		return mRefreshing;
	}
	public int sidGetIntProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 7:
			return mBirthday;
		case 8:
			return mGender;
		case 27:
			return mTimezone;
		case 28:
			return mNrofAuthedBuddies;
		case 41:
			return mAuthRequestCount;
		case 42:
			return mPopularityOrd;
		}
		return 0;
	}
	public EnumConverting sidGetEnumProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 202:
			return mType;
		case 34:
			return mAvailability;
		case 23:
			return mGivenAuthLevel;
		}
		return null;
	}
	public byte[] sidGetBinaryProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 37:
			return mAvatarImage;
		case 36:
			return mCapabilities;
		}
		return null;
	}
	public String getPropertyAsString(final int prop) {
		switch (prop) {
		case 202: return getType().toString();
		case 4: return getSkypeName();
		case 6: return getPstnNumber();
		case 5: return getFullName();
		case 7: return Integer.toString(getBirthday());
		case 8: return Integer.toString(getGender());
		case 9: return getLanguages();
		case 10: return getCountry();
		case 11: return getProvince();
		case 12: return getCity();
		case 13: return getPhoneHome();
		case 14: return getPhoneOffice();
		case 15: return getPhoneMobile();
		case 16: return getEmails();
		case 17: return getHomepage();
		case 18: return getAbout();
		case 37: return "<binary>";
		case 26: return getMoodText();
		case 27: return Integer.toString(getTimezone());
		case 36: return "<binary>";
		case 28: return Integer.toString(getNrofAuthedBuddies());
		case 29: return getIpCountry();
		case 20: return getReceivedAuthRequest();
		case 34: return getAvailability().toString();
		case 21: return getDisplayName();
		case 22: return Boolean.toString(getRefreshing());
		case 23: return getGivenAuthLevel().toString();
		case 33: return getGivenDisplayName();
		case 180: return getAssignedComment();
		case 41: return Integer.toString(getAuthRequestCount());
		case 184: return getAssignedPhone1();
		case 185: return getAssignedPhone1Label();
		case 186: return getAssignedPhone2();
		case 187: return getAssignedPhone2Label();
		case 188: return getAssignedPhone3();
		case 189: return getAssignedPhone3Label();
		case 42: return Integer.toString(getPopularityOrd());
		}
		return "<unkown>";
	}
	public String getPropertyAsString(final Property prop) {
		return getPropertyAsString(prop.getId());
	}
	protected void sidOnChangedProperty(final int propertyId, final int value, final String svalue) {
		final Property property = Property.get(propertyId);
		if (property == Property.P_UNKNOWN) return;
		final int idx = property.getIdx();
		if (idx != 0) {
			int word = (idx-1) / 32;
			int bit  = 1<<((idx-1)%32);
			synchronized (this) {
				if (word == 0) mSidCached |= bit; else mSidExtraCached[word-1] |= bit;
				switch (propertyId) {
				case 202: mType = Type.get(value); break;
				case 4:
					if (svalue != null) mSkypeName = svalue;
					else mSidCached &=~bit;
					break;
				case 6:
					if (svalue != null) mPstnNumber = svalue;
					else mSidCached &=~bit;
					break;
				case 5:
					if (svalue != null) mFullName = svalue;
					else mSidCached &=~bit;
					break;
				case 7: mBirthday = value; break;
				case 8: mGender = value; break;
				case 9:
					if (svalue != null) mLanguages = svalue;
					else mSidCached &=~bit;
					break;
				case 10:
					if (svalue != null) mCountry = svalue;
					else mSidCached &=~bit;
					break;
				case 11:
					if (svalue != null) mProvince = svalue;
					else mSidCached &=~bit;
					break;
				case 12:
					if (svalue != null) mCity = svalue;
					else mSidCached &=~bit;
					break;
				case 13:
					if (svalue != null) mPhoneHome = svalue;
					else mSidCached &=~bit;
					break;
				case 14:
					if (svalue != null) mPhoneOffice = svalue;
					else mSidCached &=~bit;
					break;
				case 15:
					if (svalue != null) mPhoneMobile = svalue;
					else mSidCached &=~bit;
					break;
				case 16:
					if (svalue != null) mEmails = svalue;
					else mSidCached &=~bit;
					break;
				case 17:
					if (svalue != null) mHomepage = svalue;
					else mSidCached &=~bit;
					break;
				case 18:
					if (svalue != null) mAbout = svalue;
					else mSidCached &=~bit;
					break;
				case 26:
					if (svalue != null) mMoodText = svalue;
					else mSidCached &=~bit;
					break;
				case 27: mTimezone = value; break;
				case 28: mNrofAuthedBuddies = value; break;
				case 29:
					if (svalue != null) mIpCountry = svalue;
					else mSidCached &=~bit;
					break;
				case 20:
					if (svalue != null) mReceivedAuthRequest = svalue;
					else mSidCached &=~bit;
					break;
				case 34: mAvailability = Availability.get(value); break;
				case 21:
					if (svalue != null) mDisplayName = svalue;
					else mSidCached &=~bit;
					break;
				case 22: mRefreshing = value != 0; break;
				case 23: mGivenAuthLevel = AuthLevel.get(value); break;
				case 33:
					if (svalue != null) mGivenDisplayName = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 180:
					if (svalue != null) mAssignedComment = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 41: mAuthRequestCount = value; break;
				case 184:
					if (svalue != null) mAssignedPhone1 = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 185:
					if (svalue != null) mAssignedPhone1Label = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 186:
					if (svalue != null) mAssignedPhone2 = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 187:
					if (svalue != null) mAssignedPhone2Label = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 188:
					if (svalue != null) mAssignedPhone3 = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 189:
					if (svalue != null) mAssignedPhone3Label = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 42: mPopularityOrd = value; break;
				default: if (word == 0) mSidCached &= ~bit; else mSidExtraCached[word-1] &= ~bit; break;
				}
			}
		}
		ContactListener listener = ((Skype) mSidRoot).getContactListener();
		if (listener != null)
			listener.onPropertyChange(this, property, value, svalue);
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final String newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 4:
			mSidCached |= 0x2;
			mSkypeName=  newValue;
			break;
		case 6:
			mSidCached |= 0x4;
			mPstnNumber=  newValue;
			break;
		case 5:
			mSidCached |= 0x8;
			mFullName=  newValue;
			break;
		case 9:
			mSidCached |= 0x40;
			mLanguages=  newValue;
			break;
		case 10:
			mSidCached |= 0x80;
			mCountry=  newValue;
			break;
		case 11:
			mSidCached |= 0x100;
			mProvince=  newValue;
			break;
		case 12:
			mSidCached |= 0x200;
			mCity=  newValue;
			break;
		case 13:
			mSidCached |= 0x400;
			mPhoneHome=  newValue;
			break;
		case 14:
			mSidCached |= 0x800;
			mPhoneOffice=  newValue;
			break;
		case 15:
			mSidCached |= 0x1000;
			mPhoneMobile=  newValue;
			break;
		case 16:
			mSidCached |= 0x2000;
			mEmails=  newValue;
			break;
		case 17:
			mSidCached |= 0x4000;
			mHomepage=  newValue;
			break;
		case 18:
			mSidCached |= 0x8000;
			mAbout=  newValue;
			break;
		case 26:
			mSidCached |= 0x20000;
			mMoodText=  newValue;
			break;
		case 205:
			mSidCached |= 0x40000;
			mRichMoodText=  newValue;
			break;
		case 29:
			mSidCached |= 0x800000;
			mIpCountry=  newValue;
			break;
		case 20:
			mSidCached |= 0x4000000;
			mReceivedAuthRequest=  newValue;
			break;
		case 21:
			mSidCached |= 0x40000000;
			mDisplayName=  newValue;
			break;
		case 33:
			mSidExtraCached[0] |= 0x2;
			mGivenDisplayName=  newValue;			break;
		case 180:
			mSidExtraCached[0] |= 0x4;
			mAssignedComment=  newValue;
			break;
		case 184:
			mSidExtraCached[0] |= 0x20;
			mAssignedPhone1=  newValue;
			break;
		case 185:
			mSidExtraCached[0] |= 0x40;
			mAssignedPhone1Label=  newValue;
			break;
		case 186:
			mSidExtraCached[0] |= 0x80;
			mAssignedPhone2=  newValue;
			break;
		case 187:
			mSidExtraCached[0] |= 0x100;
			mAssignedPhone2Label=  newValue;
			break;
		case 188:
			mSidExtraCached[0] |= 0x200;
			mAssignedPhone3=  newValue;
			break;
		case 189:
			mSidExtraCached[0] |= 0x400;
			mAssignedPhone3Label=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final int newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 202:
			mSidCached |= 0x1;
			mType= Type.get(newValue);
			break;
		case 7:
			mSidCached |= 0x10;
			mBirthday=  newValue;
			break;
		case 8:
			mSidCached |= 0x20;
			mGender=  newValue;
			break;
		case 27:
			mSidCached |= 0x80000;
			mTimezone=  newValue;
			break;
		case 28:
			mSidCached |= 0x400000;
			mNrofAuthedBuddies=  newValue;
			break;
		case 34:
			mSidCached |= 0x20000000;
			mAvailability= Availability.get(newValue);
			break;
		case 22:
			mSidCached |= 0x80000000;
			mRefreshing= newValue != 0;
			break;
		case 23:
			mSidExtraCached[0] |= 0x1;
			mGivenAuthLevel= AuthLevel.get(newValue);
			break;
		case 41:
			mSidExtraCached[0] |= 0x10;
			mAuthRequestCount=  newValue;
			break;
		case 42:
			mSidExtraCached[0] |= 0x800;
			mPopularityOrd=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final byte[] newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 37:
			mSidCached |= 0x10000;
			mAvatarImage=  newValue;
			break;
		case 36:
			mSidCached |= 0x100000;
			mCapabilities=  newValue;
			break;
		}
	}
	public Type         mType;
	public String       mSkypeName;
	public String       mPstnNumber;
	public String       mFullName;
	public int          mBirthday;
	public int          mGender;
	public String       mLanguages;
	public String       mCountry;
	public String       mProvince;
	public String       mCity;
	public String       mPhoneHome;
	public String       mPhoneOffice;
	public String       mPhoneMobile;
	public String       mEmails;
	public String       mHomepage;
	public String       mAbout;
	public byte[]       mAvatarImage;
	public String       mMoodText;
	public String       mRichMoodText;
	public int          mTimezone;
	public byte[]       mCapabilities;
	public long         mProfileTimestamp;
	public int          mNrofAuthedBuddies;
	public String       mIpCountry;
	public long         mAvatarTimestamp;
	public long         mMoodTimestamp;
	public String       mReceivedAuthRequest;
	public long         mAuthRequestTimestamp;
	public long         mLastOnlineTimestamp;
	public Availability mAvailability;
	public String       mDisplayName;
	public boolean      mRefreshing;
	public AuthLevel    mGivenAuthLevel;
	public String       mGivenDisplayName;
	public String       mAssignedComment;
	public long         mLastUsedTimestamp;
	public int          mAuthRequestCount;
	public String       mAssignedPhone1;
	public String       mAssignedPhone1Label;
	public String       mAssignedPhone2;
	public String       mAssignedPhone2Label;
	public String       mAssignedPhone3;
	public String       mAssignedPhone3Label;
	public int          mPopularityOrd;
	/***
	 * invalidateCache: the next time the property is get, it will be querried to the runtime, meanwhile it can be discarded.
	 * This allows fine grained cache management. Note that this doesn't delete the property, you still have to set it to null
	 * to get a chance having this behavior. The rationale if that the generated properties being public, you can directly assign it to null
	 * whilst a generated invalidateCache would require switching on the values to do so.
	 * Contact o; o.invalidate(Contact.Property.P_MY_PROP); o.mMyProp = null;
	 * @param property the property to be invalidated
	 */
	public void invalidateCache(final PropertyEnumConverting property) {
		int idx   = property.getIdx();
		if (idx-- > 0) {
			int word = idx / 32;
			if (word-- == 0) mSidCached = ~(1<<(idx%32));
	 else mSidExtraCached[word] &=~(1<<(idx%32));
		}
	}
	
	protected boolean isCached(final PropertyEnumConverting property) {
		int idx = property.getIdx();
		if (idx-- > 0) {
			int word = idx / 32;
			if (word-- == 0) return (mSidCached & (1<<(idx%32))) != 0;
			else return (mSidExtraCached[word] & (1<<(idx%32))) != 0;
		}
		return false;
	}
	
	protected boolean hasCached() {
		if (mSidCached != 0) return true;
		for (int i = 0, e = mSidExtraCached.length; i < e; i++)
			if (mSidExtraCached[i] != 0) return true;
		return false;
	}
	private int[] mSidExtraCached = new int[1];
	public int moduleId() {
		return 2;
	}
	
	public Contact(final int oid, final SidRoot root) {
		super(oid, root, 44);
	}
}
