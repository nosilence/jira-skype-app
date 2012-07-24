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
import com.skype.api.Participant;
import com.skype.api.Skype;
import com.skype.ipc.SidGetResponding;

/** The Conversation class encapsulates all types of communication possible with Skype client. Instant messaging, calls, video calls, file transfers, SMS, screen sharing - all take place within the context of a Conversation. Contacts are represented in Conversation as Participant objects. This also applies to contacts of PSTN type. All events in a conversation are represented as Message objects.   */
public final class Conversation extends SidObject {
	public enum Type implements EnumConverting {
		/** 1:1 conversations, there is a one dialog per identity */
		DIALOG                 (1),
		/** equivalent of a multichat */
		CONFERENCE             (2),
		/** a conference that has been terminated (disbanded chat) */
		TERMINATED_CONFERENCE  (3),
		/** voice-only conference, when host is using a legacy non-conversation client */
		LEGACY_VOICE_CONFERENCE(4),
		/** chat used for legacy shared groups, can be ignored */
		LEGACY_SHAREDGROUP     (5);
		private final int key;
		Type(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return DIALOG; }
		public EnumConverting convert(int from) { return Type.get(from); }
		public EnumConverting[] getArray(final int size) { return new Type[size]; }
		public static Type get(int from) {
			switch (from) {
			case 1: return DIALOG;
			case 2: return CONFERENCE;
			case 3: return TERMINATED_CONFERENCE;
			case 4: return LEGACY_VOICE_CONFERENCE;
			case 5: return LEGACY_SHAREDGROUP;
			}
			return DIALOG;
		}
		public static final int DIALOG_VALUE                  = 1;
		public static final int CONFERENCE_VALUE              = 2;
		public static final int TERMINATED_CONFERENCE_VALUE   = 3;
		public static final int LEGACY_VOICE_CONFERENCE_VALUE = 4;
		public static final int LEGACY_SHAREDGROUP_VALUE      = 5;
	}
	public enum MyStatus implements EnumConverting {
		/** connecting to conference */
		CONNECTING          (1),
		RETRY_CONNECTING    (2),
		/** unused */
		DOWNLOADING_MESSAGES(3),
		/** conference is full for now, being queued */
		QUEUED_TO_ENTER     (4),
		/** I'm applying to join the conference */
		APPLICANT           (5),
		/** My application to join the conference was denied */
		APPLICATION_DENIED  (6),
		/** The password I provided is incorrect */
		INVALID_ACCESS_TOKEN(7),
		/** I'm part of the conference, I can participate */
		CONSUMER            (8),
		/** I was kicked from the conference */
		RETIRED_FORCEFULLY  (9),
		/** I left the conference */
		RETIRED_VOLUNTARILY (10);
		private final int key;
		MyStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return CONNECTING; }
		public EnumConverting convert(int from) { return MyStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new MyStatus[size]; }
		public static MyStatus get(int from) {
			switch (from) {
			case  1: return CONNECTING;
			case  2: return RETRY_CONNECTING;
			case  3: return DOWNLOADING_MESSAGES;
			case  4: return QUEUED_TO_ENTER;
			case  5: return APPLICANT;
			case  6: return APPLICATION_DENIED;
			case  7: return INVALID_ACCESS_TOKEN;
			case  8: return CONSUMER;
			case  9: return RETIRED_FORCEFULLY;
			case 10: return RETIRED_VOLUNTARILY;
			}
			return CONNECTING;
		}
		public static final int CONNECTING_VALUE           =  1;
		public static final int RETRY_CONNECTING_VALUE     =  2;
		public static final int DOWNLOADING_MESSAGES_VALUE =  3;
		public static final int QUEUED_TO_ENTER_VALUE      =  4;
		public static final int APPLICANT_VALUE            =  5;
		public static final int APPLICATION_DENIED_VALUE   =  6;
		public static final int INVALID_ACCESS_TOKEN_VALUE =  7;
		public static final int CONSUMER_VALUE             =  8;
		public static final int RETIRED_FORCEFULLY_VALUE   =  9;
		public static final int RETIRED_VOLUNTARILY_VALUE  = 10;
	}
	public enum LocalLiveStatus implements EnumConverting {
		/** there isn't a live session */
		NONE                   (0),
		/** trying to start or join a live session */
		STARTING               (1),
		/** there is a live session ringing */
		RINGING_FOR_ME         (2),
		/** the conference is live for me */
		IM_LIVE                (3),
		/** I put the live session on hold */
		ON_HOLD_LOCALLY        (5),
		/** the live session was put on hold by someone else */
		ON_HOLD_REMOTELY       (6),
		/** there is a live session on-going, I'm not participating but I could join */
		OTHERS_ARE_LIVE        (7),
		/** there is a live session on-going without me, but I can't join because it's full */
		OTHERS_ARE_LIVE_FULL   (11),
		/** playing a voicemail (dialog only) */
		PLAYING_VOICE_MESSAGE  (8),
		/** recording a voicemail (dialog only) */
		RECORDING_VOICE_MESSAGE(9),
		/** a live session just finished, we stay in this state for RECENTLY_LIVE_TIMEOUT setup key */
		RECENTLY_LIVE          (10),
		/** call is being transferred */
		TRANSFERRING           (12);
		private final int key;
		LocalLiveStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return NONE; }
		public EnumConverting convert(int from) { return LocalLiveStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new LocalLiveStatus[size]; }
		public static LocalLiveStatus get(int from) {
			switch (from) {
			case  0: return NONE;
			case  1: return STARTING;
			case  2: return RINGING_FOR_ME;
			case  3: return IM_LIVE;
			case  5: return ON_HOLD_LOCALLY;
			case  6: return ON_HOLD_REMOTELY;
			case  7: return OTHERS_ARE_LIVE;
			case 11: return OTHERS_ARE_LIVE_FULL;
			case  8: return PLAYING_VOICE_MESSAGE;
			case  9: return RECORDING_VOICE_MESSAGE;
			case 10: return RECENTLY_LIVE;
			case 12: return TRANSFERRING;
			}
			return NONE;
		}
		public static final int NONE_VALUE                    =  0;
		public static final int STARTING_VALUE                =  1;
		public static final int RINGING_FOR_ME_VALUE          =  2;
		public static final int IM_LIVE_VALUE                 =  3;
		public static final int ON_HOLD_LOCALLY_VALUE         =  5;
		public static final int ON_HOLD_REMOTELY_VALUE        =  6;
		public static final int OTHERS_ARE_LIVE_VALUE         =  7;
		public static final int OTHERS_ARE_LIVE_FULL_VALUE    = 11;
		public static final int PLAYING_VOICE_MESSAGE_VALUE   =  8;
		public static final int RECORDING_VOICE_MESSAGE_VALUE =  9;
		public static final int RECENTLY_LIVE_VALUE           = 10;
		public static final int TRANSFERRING_VALUE            = 12;
	}
	/** values for opt_admin_only_activities property */
	public enum AllowedActivity implements EnumConverting {
		/** allowed to set the CONVERSATION_META properties */
		SET_META       (1),
		/** allowed to add participants to the conference */
		ADD_CONSUMERS  (2),
		/** allowed to speak, but not write */
		SPEAK          (4),
		/** allowed to speak and write */
		SPEAK_AND_WRITE(8);
		private final int key;
		AllowedActivity(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return SET_META; }
		public EnumConverting convert(int from) { return AllowedActivity.get(from); }
		public EnumConverting[] getArray(final int size) { return new AllowedActivity[size]; }
		public static AllowedActivity get(int from) {
			switch (from) {
			case 1: return SET_META;
			case 2: return ADD_CONSUMERS;
			case 4: return SPEAK;
			case 8: return SPEAK_AND_WRITE;
			}
			return SET_META;
		}
		public static final int SET_META_VALUE        = 1;
		public static final int ADD_CONSUMERS_VALUE   = 2;
		public static final int SPEAK_VALUE           = 4;
		public static final int SPEAK_AND_WRITE_VALUE = 8;
	}
	public enum ParticipantFilter implements EnumConverting {
		/** All participants (may included some that are RETIRED or OUTLAW, but not all of them) */
		ALL                     (0),
		/** Participants that can receive messages, including myself */
		CONSUMERS               (1),
		/** Only people who are applying to join the conversation */
		APPLICANTS              (2),
		/** Consumers and applicants */
		CONSUMERS_AND_APPLICANTS(3),
		/** Myself */		MYSELF                  (4),
		/** All consumers except myself */
		OTHER_CONSUMERS         (5);
		private final int key;
		ParticipantFilter(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return ALL; }
		public EnumConverting convert(int from) { return ParticipantFilter.get(from); }
		public EnumConverting[] getArray(final int size) { return new ParticipantFilter[size]; }
		public static ParticipantFilter get(int from) {
			switch (from) {
			case 0: return ALL;
			case 1: return CONSUMERS;
			case 2: return APPLICANTS;
			case 3: return CONSUMERS_AND_APPLICANTS;
			case 4: return MYSELF;
			case 5: return OTHER_CONSUMERS;
			}
			return ALL;
		}
		public static final int ALL_VALUE                      = 0;
		public static final int CONSUMERS_VALUE                = 1;
		public static final int APPLICANTS_VALUE               = 2;
		public static final int CONSUMERS_AND_APPLICANTS_VALUE = 3;
		public static final int MYSELF_VALUE                   = 4;
		public static final int OTHER_CONSUMERS_VALUE          = 5;
	}
	public enum ListType implements EnumConverting {
		/** bookmarked or in_inbox or live or with_meta_info or activity in last 30 days */
		ALL_CONVERSATIONS       (0),
		/** only last 6 months conversations are kept there */
		INBOX_CONVERSATIONS     (1),
		/** is_bookmarked is set */
		BOOKMARKED_CONVERSATIONS(2),
		/** local_livestatus is different from NONE */
		LIVE_CONVERSATIONS      (3),
		/** all conversations, without any of the limits of ALL_CONVERSATIONS */
		REALLY_ALL_CONVERSATIONS(5);
		private final int key;
		ListType(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return ALL_CONVERSATIONS; }
		public EnumConverting convert(int from) { return ListType.get(from); }
		public EnumConverting[] getArray(final int size) { return new ListType[size]; }
		public static ListType get(int from) {
			switch (from) {
			case 0: return ALL_CONVERSATIONS;
			case 1: return INBOX_CONVERSATIONS;
			case 2: return BOOKMARKED_CONVERSATIONS;
			case 3: return LIVE_CONVERSATIONS;
			case 5: return REALLY_ALL_CONVERSATIONS;
			}
			return ALL_CONVERSATIONS;
		}
		public static final int ALL_CONVERSATIONS_VALUE        = 0;
		public static final int INBOX_CONVERSATIONS_VALUE      = 1;
		public static final int BOOKMARKED_CONVERSATIONS_VALUE = 2;
		public static final int LIVE_CONVERSATIONS_VALUE       = 3;
		public static final int REALLY_ALL_CONVERSATIONS_VALUE = 5;
	}
	private final static byte[] P_IDENTITY_req = {(byte) 90,(byte) 71,(byte) 204,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_TYPE_req = {(byte) 90,(byte) 71,(byte) 134,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_LIVE_HOST_req = {(byte) 90,(byte) 71,(byte) 150,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_LIVE_START_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 206,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_LIVE_IS_MUTED_req = {(byte) 90,(byte) 71,(byte) 228,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_ALERT_STRING_req = {(byte) 90,(byte) 71,(byte) 152,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_IS_BOOKMARKED_req = {(byte) 90,(byte) 71,(byte) 153,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_GIVEN_DISPLAY_NAME_req = {(byte) 90,(byte) 71,(byte) 157,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_DISPLAY_NAME_req = {(byte) 90,(byte) 71,(byte) 156,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_LOCAL_LIVE_STATUS_req = {(byte) 90,(byte) 71,(byte) 159,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_INBOX_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 160,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_INBOX_MESSAGE_ID_req = {(byte) 90,(byte) 71,(byte) 205,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_UNCONSUMED_SUPPRESSED_MESSAGES_req = {(byte) 90,(byte) 71,(byte) 207,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_UNCONSUMED_NORMAL_MESSAGES_req = {(byte) 90,(byte) 71,(byte) 208,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_UNCONSUMED_ELEVATED_MESSAGES_req = {(byte) 90,(byte) 71,(byte) 209,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_UNCONSUMED_MESSAGES_VOICE_req = {(byte) 90,(byte) 71,(byte) 202,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_ACTIVE_VOICEMAIL_req = {(byte) 90,(byte) 71,(byte) 203,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_CONSUMPTION_HORIZON_req = {(byte) 90,(byte) 71,(byte) 211,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_LAST_ACTIVITY_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 213,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_SPAWNED_FROM_CONVO_ID_req = {(byte) 90,(byte) 71,(byte) 147,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_CREATOR_req = {(byte) 90,(byte) 71,(byte) 135,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_CREATION_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 136,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_MY_STATUS_req = {(byte) 90,(byte) 71,(byte) 151,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_OPT_JOINING_ENABLED_req = {(byte) 90,(byte) 71,(byte) 154,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_OPT_ENTRY_LEVEL_RANK_req = {(byte) 90,(byte) 71,(byte) 138,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_OPT_DISCLOSE_HISTORY_req = {(byte) 90,(byte) 71,(byte) 139,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_OPT_ADMIN_ONLY_ACTIVITIES_req = {(byte) 90,(byte) 71,(byte) 141,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_PASSWORD_HINT_req = {(byte) 90,(byte) 71,(byte) 212,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_META_NAME_req = {(byte) 90,(byte) 71,(byte) 142,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_META_TOPIC_req = {(byte) 90,(byte) 71,(byte) 143,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_META_GUIDELINES_req = {(byte) 90,(byte) 71,(byte) 145,(byte) 7,(byte) 93,(byte) 18};
	private final static byte[] P_META_PICTURE_req = {(byte) 90,(byte) 71,(byte) 146,(byte) 7,(byte) 93,(byte) 18};
	/** Properties of the Conversation class */
	public enum Property implements PropertyEnumConverting {
		P_UNKNOWN                       (0,0,null,0,null),
		P_IDENTITY                      (972, 1, P_IDENTITY_req, 0, null),
		P_TYPE                          (902, 2, P_TYPE_req, 0, Type.get(0)),
		P_LIVE_HOST                     (918, 3, P_LIVE_HOST_req, 0, null),
		P_LIVE_START_TIMESTAMP          (974, 4, P_LIVE_START_TIMESTAMP_req, 0, null),
		P_LIVE_IS_MUTED                 (996, 5, P_LIVE_IS_MUTED_req, 0, null),
		P_ALERT_STRING                  (920, 6, P_ALERT_STRING_req, 0, null),
		P_IS_BOOKMARKED                 (921, 7, P_IS_BOOKMARKED_req, 0, null),
		P_GIVEN_DISPLAY_NAME            (925, 8, P_GIVEN_DISPLAY_NAME_req, 0, null),
		P_DISPLAY_NAME                  (924, 9, P_DISPLAY_NAME_req, 0, null),
		P_LOCAL_LIVE_STATUS             (927, 10, P_LOCAL_LIVE_STATUS_req, 0, LocalLiveStatus.get(0)),
		P_INBOX_TIMESTAMP               (928, 11, P_INBOX_TIMESTAMP_req, 0, null),
		P_INBOX_MESSAGE_ID              (973, 12, P_INBOX_MESSAGE_ID_req, 9, null),
		P_UNCONSUMED_SUPPRESSED_MESSAGES(975, 13, P_UNCONSUMED_SUPPRESSED_MESSAGES_req, 0, null),
		P_UNCONSUMED_NORMAL_MESSAGES    (976, 14, P_UNCONSUMED_NORMAL_MESSAGES_req, 0, null),
		P_UNCONSUMED_ELEVATED_MESSAGES  (977, 15, P_UNCONSUMED_ELEVATED_MESSAGES_req, 0, null),
		P_UNCONSUMED_MESSAGES_VOICE     (970, 16, P_UNCONSUMED_MESSAGES_VOICE_req, 0, null),
		P_ACTIVE_VOICEMAIL              (971, 17, P_ACTIVE_VOICEMAIL_req, 7, null),
		P_CONSUMPTION_HORIZON           (979, 18, P_CONSUMPTION_HORIZON_req, 0, null),
		P_LAST_ACTIVITY_TIMESTAMP       (981, 19, P_LAST_ACTIVITY_TIMESTAMP_req, 0, null),
		P_SPAWNED_FROM_CONVO_ID         (915, 20, P_SPAWNED_FROM_CONVO_ID_req, 18, null),
		P_CREATOR                       (903, 21, P_CREATOR_req, 0, null),
		P_CREATION_TIMESTAMP            (904, 22, P_CREATION_TIMESTAMP_req, 0, null),		P_MY_STATUS                     (919, 23, P_MY_STATUS_req, 0, MyStatus.get(0)),
		P_OPT_JOINING_ENABLED           (922, 24, P_OPT_JOINING_ENABLED_req, 0, null),
		P_OPT_ENTRY_LEVEL_RANK          (906, 25, P_OPT_ENTRY_LEVEL_RANK_req, 0, Participant.Rank.get(0)),
		P_OPT_DISCLOSE_HISTORY          (907, 26, P_OPT_DISCLOSE_HISTORY_req, 0, null),
		P_OPT_ADMIN_ONLY_ACTIVITIES     (909, 27, P_OPT_ADMIN_ONLY_ACTIVITIES_req, 0, null),
		P_PASSWORD_HINT                 (980, 28, P_PASSWORD_HINT_req, 0, null),
		P_META_NAME                     (910, 29, P_META_NAME_req, 0, null),
		P_META_TOPIC                    (911, 30, P_META_TOPIC_req, 0, null),
		P_META_GUIDELINES               (913, 31, P_META_GUIDELINES_req, 0, null),
		P_META_PICTURE                  (914, 32, P_META_PICTURE_req, 0, null);
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
			case 972: return P_IDENTITY;
			case 902: return P_TYPE;
			case 918: return P_LIVE_HOST;
			case 974: return P_LIVE_START_TIMESTAMP;
			case 996: return P_LIVE_IS_MUTED;
			case 920: return P_ALERT_STRING;
			case 921: return P_IS_BOOKMARKED;
			case 925: return P_GIVEN_DISPLAY_NAME;
			case 924: return P_DISPLAY_NAME;
			case 927: return P_LOCAL_LIVE_STATUS;
			case 928: return P_INBOX_TIMESTAMP;
			case 973: return P_INBOX_MESSAGE_ID;
			case 975: return P_UNCONSUMED_SUPPRESSED_MESSAGES;
			case 976: return P_UNCONSUMED_NORMAL_MESSAGES;
			case 977: return P_UNCONSUMED_ELEVATED_MESSAGES;
			case 970: return P_UNCONSUMED_MESSAGES_VOICE;
			case 971: return P_ACTIVE_VOICEMAIL;
			case 979: return P_CONSUMPTION_HORIZON;
			case 981: return P_LAST_ACTIVITY_TIMESTAMP;
			case 915: return P_SPAWNED_FROM_CONVO_ID;
			case 903: return P_CREATOR;
			case 904: return P_CREATION_TIMESTAMP;
			case 919: return P_MY_STATUS;
			case 922: return P_OPT_JOINING_ENABLED;
			case 906: return P_OPT_ENTRY_LEVEL_RANK;
			case 907: return P_OPT_DISCLOSE_HISTORY;
			case 909: return P_OPT_ADMIN_ONLY_ACTIVITIES;
			case 980: return P_PASSWORD_HINT;
			case 910: return P_META_NAME;
			case 911: return P_META_TOPIC;
			case 913: return P_META_GUIDELINES;
			case 914: return P_META_PICTURE;
			}
			return P_UNKNOWN;
		}
		public static final int P_IDENTITY_VALUE                       = 972;
		public static final int P_TYPE_VALUE                           = 902;
		public static final int P_LIVE_HOST_VALUE                      = 918;
		public static final int P_LIVE_START_TIMESTAMP_VALUE           = 974;
		public static final int P_LIVE_IS_MUTED_VALUE                  = 996;
		public static final int P_ALERT_STRING_VALUE                   = 920;
		public static final int P_IS_BOOKMARKED_VALUE                  = 921;
		public static final int P_GIVEN_DISPLAY_NAME_VALUE             = 925;
		public static final int P_DISPLAY_NAME_VALUE                   = 924;
		public static final int P_LOCAL_LIVE_STATUS_VALUE              = 927;
		public static final int P_INBOX_TIMESTAMP_VALUE                = 928;
		public static final int P_INBOX_MESSAGE_ID_VALUE               = 973;
		public static final int P_UNCONSUMED_SUPPRESSED_MESSAGES_VALUE = 975;
		public static final int P_UNCONSUMED_NORMAL_MESSAGES_VALUE     = 976;
		public static final int P_UNCONSUMED_ELEVATED_MESSAGES_VALUE   = 977;
		public static final int P_UNCONSUMED_MESSAGES_VOICE_VALUE      = 970;
		public static final int P_ACTIVE_VOICEMAIL_VALUE               = 971;
		public static final int P_CONSUMPTION_HORIZON_VALUE            = 979;
		public static final int P_LAST_ACTIVITY_TIMESTAMP_VALUE        = 981;
		public static final int P_SPAWNED_FROM_CONVO_ID_VALUE          = 915;
		public static final int P_CREATOR_VALUE                        = 903;
		public static final int P_CREATION_TIMESTAMP_VALUE             = 904;
		public static final int P_MY_STATUS_VALUE                      = 919;
		public static final int P_OPT_JOINING_ENABLED_VALUE            = 922;
		public static final int P_OPT_ENTRY_LEVEL_RANK_VALUE           = 906;
		public static final int P_OPT_DISCLOSE_HISTORY_VALUE           = 907;
		public static final int P_OPT_ADMIN_ONLY_ACTIVITIES_VALUE      = 909;
		public static final int P_PASSWORD_HINT_VALUE                  = 980;
		public static final int P_META_NAME_VALUE                      = 910;
		public static final int P_META_TOPIC_VALUE                     = 911;
		public static final int P_META_GUIDELINES_VALUE                = 913;
		public static final int P_META_PICTURE_VALUE                   = 914;
		public static final Property[] mget_info_mreq = { P_DISPLAY_NAME, P_UNCONSUMED_NORMAL_MESSAGES, P_INBOX_TIMESTAMP };
	}
	/** Setupkey SETUPKEY_ENABLE_BIRTHDAY_NOTIFICATION type:int default value:"1" <br>Enables/disables birthday notification messages. <br> - 0 - disable;  <br> - 1 - enable; <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String ENABLE_BIRTHDAY_NOTIFICATION = "Lib/Conversation/EnableBirthday";
	
	/** Setupkey SETUPKEY_INBOX_UPDATE_TIMEOUT type:int  <br>Timeout in seconds, how old the Conversation.P_INBOX_TIMESTAMP has to be for it to be re-sorted in the inbox. <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String INBOX_UPDATE_TIMEOUT = "Lib/Conversation/InboxUpdateTimeout";
	
	/** Setupkey SETUPKEY_RECENTLY_LIVE_TIMEOUT type:int default value:"20" <br>The number of seconds a formerly live conversation will remain the Conversation.LIVE_CONVERSATIONS filter. Note that while the conversation remains in Conversation.LIVE_CONVERSATIONS filter, Skype.OnConversationListChange events will not fire if there is another call coming up within the same conversation. Seeting this key to 0 will cause conversations to exit the Conversation.LIVE_CONVERSATIONS list immediately, after live state drops. <br><br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String RECENTLY_LIVE_TIMEOUT = "Lib/Conversation/RecentlyLiveTimeout";
	
	/** Setupkey SETUPKEY_DISABLE_CHAT type:int  Disables chat (for voice only clients). <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String DISABLE_CHAT = "Lib/Chat/DisableChat";
	
	/** Setupkey SETUPKEY_DISABLE_CHAT_HISTORY type:int  <br>Disables storage of chat history. <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String DISABLE_CHAT_HISTORY = "Lib/Message/DisableHistory";
	
	/** Setupkey SETUPKEY_CHAT_HISTORY_DAYS type:int  <br>Time limit for keeping local chat message history. <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String CHAT_HISTORY_DAYS = "Lib/Chat/HistoryDays";
	
	/** Setupkey SETUPKEY_CHATDB_LIMIT_KB type:int default value:"0" Use this key to limit the size of the chat db. Value is in KB. By default there is no limit. A minimum of 16 MB is recommended. */
	public static final String CHATDB_LIMIT_KB = "Lib/Chat/ChatDBLimitKb";
	
	/** Setupkey SETUPKEY_DISABLE_CHAT_ACTIVITY_INDICATION type:int  <br>Enables/disables transmitting typing indicator signals to othe participants of conversations. <br> - 0 - disable;  <br> - 1 - enable; <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */	public static final String DISABLE_CHAT_ACTIVITY_INDICATION = "Lib/Chat/DisableActivityIndication";
	
	/** Setupkey SETUPKEY_CALL_NOANSWER_TIMEOUT type:int default value:"15" <br>Timeout in seconds after which the incoming live session will stop ringing (and if possible, proceed to voicemail or call forward). <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String CALL_NOANSWER_TIMEOUT = "Lib/Call/NoAnswerTimeout";
	
	/** Setupkey SETUPKEY_CALL_SEND_TO_VM type:int  <br>Autoforwarding of incoming calls to voicemail. <br> - 0 - off <br> - 1 - on <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String CALL_SEND_TO_VM = "Lib/Call/SendToVM";
	
	/** Setupkey SETUPKEY_CALL_APPLY_CF type:int  <br>Enables/disables call forwarding. <br> - 0 - disable;  <br> - 1 - enable; <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String CALL_APPLY_CF = "Lib/Call/ApplyCF";
	
	/** Setupkey SETUPKEY_CALL_EMERGENCY_COUNTRY type:string  <br>Country code for emergency calls <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String CALL_EMERGENCY_COUNTRY = "Lib/Call/EmergencyCountry";
	
	private final static byte[] setOption_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 1};
	/** Setter method for Conversation option properties. Option properties are all Conversation properties starting with OPT_ prefix. 
	 * @param propKey Conversation property key, for example: Conversation.OPT_JOINING_ENABLED 
	 * @param value New value for the option property. 
	 */
	public void setOption(int propKey, int value) {
		try {
			sidDoRequest(setOption_req)
			.addEnumParm(1, propKey)
			.addUintParm(2, value)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setTopic_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 2};
	/** Setter for Conversation class META_TOPIC. This topic will be set for remote participants as well. 
	 * @param topic New conversation topic. 
	 * @param isXml Notifies remote UIs that the new topic contains xml tags. 
	 */
	public void setTopic(String topic, boolean isXml) {
		try {
			sidDoRequest(setTopic_req)
			.addStringParm(1, topic)
			.addBoolParm(2, isXml)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setGuidelines_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 3};
	/** Setter for Conversation META_GUIDELINES. This property will be visible to remote participants of the conversation. 
	 * @param guidelines New value for the META_GUIDELINES property. 
	 * @param isXml Set true to notify remote UIs that the new guideline contains XML tags. 
	 */
	public void setGuidelines(String guidelines, boolean isXml) {
		try {
			sidDoRequest(setGuidelines_req)
			.addStringParm(1, guidelines)
			.addBoolParm(2, isXml)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setPicture_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 4};
	/** Sets the conversation's avatar to the specified JPEG image, which is propagated to both local and remote participants. Before calling this method, you should use Skype.ValidateAvatar to verify that jpeg references a valid JPEG image. 
	 * @param jpeg Conversation avatar binary. 
	 */
	public void setPicture(byte[] jpeg) {
		try {
			sidDoRequest(setPicture_req)
			.addBinaryParm(1, jpeg)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] spawnConference_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 6};
	/** When called from dialog conversation, this spawns a new conversation, with existing two dialog participants plus new contact identities given in the identitiesToAdd list. You do not need to add existing dialog participants to the string list. In fact, passing only the existing participants in the identities list will cause the method call to fail (return false), the same as if the list was empty. This method will also return false if the original conversation was not a dialog (contained more than two participants). Also note that this method always creates a new Conversation - even if a conversation with exactly the same participant list existed before. 
	 * @param identitiesToAdd String list of additional participant identities. You do not need to add existing two participants from the original dialog to this list. 
	 * @return conference Returns the resulting conversation or 0 if the method call failed. 
	 */
	public Conversation spawnConference(String[] identitiesToAdd) {
		try {
			return (Conversation) sidDoRequest(spawnConference_req)
			.addStringListParm(1, identitiesToAdd)
			.endRequest().getObjectParm(1, 18, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] addConsumers_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 7};
	/** Takes one or more Contact identities and creates corresponding Participant objects within the context of this Conversation, which must be of type CONFERENCE. If you have an existing dialog conversation, use SpawnConference instead. 
	 * @param identities Contact identities to be added to the Conversation. 
	 */
	public void addConsumers(String[] identities) {
		try {
			sidDoRequest(addConsumers_req)
			.addStringListParm(1, identities)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] assimilate_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 9};
	/** Merges two live conversations. For example, if the user already has a live conversation up - let's call it conversation A. Then a new incoming call occurs - another conversation obtains LOCAL_LIVESTATUS == Conversation.RINGING_FOR_ME, let's call it conversation B. The user wishes to pick up the new incoming call and add it to the existing one. For this you can first call B->JoinLiveSession and then merge two calls with A->Assimilate(B, A). The second argument will return the merged conversation. Note that there are actually three conversation objects involved: A (before merge), B and C (after the merge). Normally it would make sense to have the first conversation (A) as the second argument, so that it gets overwritten with the assimilation result. 
	 * @param otherConversation The new conversation to be merged with the one already in live state. 
	 * @return conversation Returns a 3rd live conversation, result of merging two existing ones. 
	 */
	public Conversation assimilate(Conversation otherConversation) {
		try {
			return (Conversation) sidDoRequest(assimilate_req)
			.addObjectParm(1, otherConversation)
			.endRequest().getObjectParm(1, 18, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] joinLiveSession_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 10};
	/** starts, answers or joins a live session (first one to join becomes LIVE_HOST)
	 * @param accessToken if starting a live session, allows to set a custom access token
	 */
	public void joinLiveSession(String accessToken) {
		try {
			sidDoRequest(joinLiveSession_req)
			.addStringParm(1, accessToken)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] ringOthers_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 36};
	/** This is an alternative to calling Ring method for each Participant individually. This also works with dialogs (with identities containing only one item). 
	 * @param identities List of Participants to ring. Leaving the list empty will result in ringing all participants of at least speaker level. 
	 * @param videoCall If true, indicates that we want to do a video call (video still needs to be separately enabled) 
	 * @param origin When call is initiated from web link, this argument must contain the URI that was used 
	 */
	public void ringOthers(String[] identities, boolean videoCall, String origin) {
		try {
			sidDoRequest(ringOthers_req)			.addStringListParm(1, identities)
			.addBoolParm(2, videoCall)
			.addStringParm(3, origin)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] muteMyMicrophone_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 11};
	/** Sets VOICE_STATUS to LISTENING in the Participant instance associated with us, causing any input from our microphone to be ignored. This is a Conversation class method, rather than Participant class, because this only applies to local participant. 
	 */
	public void muteMyMicrophone() {
		try {
			sidDoRequest(muteMyMicrophone_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] unmuteMyMicrophone_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 12};
	/** Sets VOICE_STATUS to SPEAKING in the Participant instance associated with us, causing any input from our microphone to be sent to the call host. This is a Conversation class method, rather than Participant class, because this only applies to local participant. 
	 */
	public void unmuteMyMicrophone() {
		try {
			sidDoRequest(unmuteMyMicrophone_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] holdMyLiveSession_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 13};
	/** Puts the conversation on hold - Conversation LOCAL_LIVESTATUS changes to ON_HOLD_LOCALLY and to ON_HOLD_REMOTELY for remote participants. 
	 */
	public void holdMyLiveSession() {
		try {
			sidDoRequest(holdMyLiveSession_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] resumeMyLiveSession_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 14};
	/** Resumes call from local hold. 
	 */
	public void resumeMyLiveSession() {
		try {
			sidDoRequest(resumeMyLiveSession_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] leaveLiveSession_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 15};
	/** Hang up or refuse to answer an incoming call. Set postVoiceAutoresponse to true to enable a caller to leave a voicemail message. 
	 * @param postVoiceAutoresponse
	 */
	public void leaveLiveSession(boolean postVoiceAutoresponse) {
		try {
			sidDoRequest(leaveLiveSession_req)
			.addBoolParm(1, postVoiceAutoresponse)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] startVoiceMessage_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 45};
	/** Begin recording a voice mail for this conversation's remote participant. Applies to conversations of type DIALOG only. 
	 */
	public void startVoiceMessage() {
		try {
			sidDoRequest(startVoiceMessage_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] transferLiveSession_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 40};
	/**
	 * This method is for doing call transfers. NB! Call transfers only work in one-on-one conversations (dialogs). Before attempting call transfer, you should check availability of transfer recipients with Conversation class CanTransferLiveSession method. If the current conversation has a live session up, that session (call) will be transferred to targets specified in the identities list. Note that identities is a string list - all identities in that list will get incoming calls. The first one of them to pick up the call - wins, and rest of the transfer targets will stop ringing. 
	 * 
	 * Let's take a closer look how this works in practice. We have three call participants involved in the process, and two separate conversations. Let there be three callers: Caller A (call originator), Caller B (transferor) and Caller C (recipient of transfer). 
	 * 
	 *  - Caller A - calls Caller B; Caller B picks up the call - live conversation C1 is now up with A and B in it. 
	 *  - After awhile, Caller B initiates call transfers to Caller C (and optionally to Callers D, E, F.. ). LOCAL_LIVESTATUS of C1 will get set to TRANSFERRING for both A and B. 
	 *  - Caller C picks up the call. Conversation C1 will go off live status. For Caller B, conversation C1 LOCAL_LIVESTATUS will change to RECENTLY_LIVE. Another live conversation - C2 gets spawned, with Caller A and Caller C in it. For caller C, participant object representing caller A will have TRANSFERRED_BY property set to identity of caller A. For Caller B (in now no longer live conversation C1), participant object representing caller A gets its TRANSFERRED_TO property set to identity of caller C. 
	
	 * @param identities String list of transfer target identities. As soon as first one in this list picks up the call, others will stop ringing. 
	 * @param transferTopic Optional conversation topic. This value will get set as META_TOPIC property of the conversation at the transferee end. Note that this is the only case where META_TOPIC field is used in context of dialog conversations. Thus assumption that remote UI will display topic field in case of dialogs may not be 100% correct. 
	 * @param context Leave empty if you don't know what to use it for
	 */
	public void transferLiveSession(String[] identities, String transferTopic, byte[] context) {
		try {
			sidDoRequest(transferLiveSession_req)
			.addStringListParm(1, identities)
			.addStringParm(2, transferTopic)
			.addBinaryParm(3, context)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] canTransferLiveSession_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 46};
	/** Checks if the identity is available for receiving a transferred live session. If you are going to attempt to go for multiple transfer targets, you should use this check for all the target identities. 
	 * @param identity Target identity. 
	 * @return result Returns true if call transfer to given target is possible. 
	 */
	public boolean canTransferLiveSession(String identity) {
		try {
			return sidDoRequest(canTransferLiveSession_req)
			.addStringParm(1, identity)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] sendDtmf_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 16};
	/** Sends DTMF tone to a live conversation. 
	 * @param dtmf Outgoing dtmf tone, possible values come from Participant.DTMF enumerator. 
	 * @param lengthInMs Duration in milliseconds. Defaults to 260 ms. Note that the DTMF tone can be also cancelled with Conversation.StopSendDTMF method. 
	 */
	public void sendDtmf(Participant.Dtmf dtmf, int lengthInMs) {
		try {
			sidDoRequest(sendDtmf_req)
			.addEnumParm(1, dtmf)
			.addUintParm(2, lengthInMs, 260)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] stopSendDtmf_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 48};
	/** Stops the current DTMF tone being played into conversation. For example, use this method to cancel DTMF signals started with Conversation.SendDTMF before the duration given in lengthInMS runs out. 
	 */
	public void stopSendDtmf() {
		try {
			sidDoRequest(stopSendDtmf_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setMyTextStatusTo_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 18};
	/** Sets local user typing indicator in the Conversation. Remote Participants can display these in their UI. 
	 * @param status Typing indicator status value - Participant.TEXT_STATUS 
	 */
	public void setMyTextStatusTo(Participant.TextStatus status) {
		try {
			sidDoRequest(setMyTextStatusTo_req)
			.addEnumParm(1, status)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] postText_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 19};
	/** Posts the specified text the conversation, and populates message with a reference to the corresponding Message object (if no error occurred during execution). The isXML argument can be used if the client UI has already taken care of converting message text to xml (for example, your UI might enable users to use bold tags in text messages.) 	 * @param text Text value of the outgoing message (gets set as BODY_XML property of the Message object). 
	 * @param isXml For cases where the text argument was already encoded as xml message. 
	 * @return message Returns the Message object created as a result of this method (if successful). 
	 */
	public Message postText(String text, boolean isXml) {
		try {
			return (Message) sidDoRequest(postText_req)
			.addStringParm(1, text)
			.addBoolParm(2, isXml)
			.endRequest().getObjectParm(1, 9, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] postContacts_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 20};
	/**
	 * Takes a list of Contacts as an argument and posts the list into the Conversation. The purpose of this feature is to enable sharing contacts between users, without resorting to contact search. Instead, if user A has contacts B and C, he can post contact C into chat with contact B. At this point, Contact B can add contact C to his contact list. From remote side, the posted contacts will appear as messages with type Message.POSTED_CONTACTS appearing in the conversation. The UI should allow adding these contacts from messages with this type into the contact list. 
	 * 
	 * The list of posted contacts can be retrieved with the Message.GetContacts method.  
	 * 
	 * Additionally, the UI then can parse the posted Contact data out of the Message.P_BODY_XML property. The list of contacts is wrapped between <contacts ..> </contacts> tags. Each contact item in the xml has following format: 
	 *  - t - contact type. "s" - skype contact; "p" - phone number; 
	 *  - s - skypename, present only in skypename contacts (t="s") 
	 *  - p - phone number, present only in phone number contacts (t="p") 
	 *  - f - contact's full name, if available 
	 *  - d - contact's display name, if available 
	 * 
	 * Note that only the type (t) field is mandatory. Depending on type, either skypename (s) or phone number (p) fields are always present. Full name and display name fields are optional. 
	 * 
	 * Example BODY_XML with skypname contact:  
	 * @code 
	 * <contacts alt="alt text"><c t="s" s="skypename" f="full name"/></contacts> 
	 * </CODE> 
	 * 
	 * Example BODY_XML with PSTN contact:  
	 * @code 
	 * <contacts alt="alt text"><c t="p" p="+37212345678" d="Some PSTN number"/></contacts> 
	 * </CODE>  
	 * 
	 * Example BODY_XML with multiple contacts:  
	 * @code 
	 * <contacts alt="alt text"><c t="p" p="+37212345678" d="Some PSTN number"/><c t="s" s="someskypename"/></contacts> 
	 * </CODE> 
	
	 * @param contacts List of Contact objects, to be posted in the conversation. 
	 */
	public void postContacts(Contact[] contacts) {
		try {
			sidDoRequest(postContacts_req)
			.addObjectListParm(1, contacts)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] postFiles_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 21};
	public class PostFilesResponse {
		public Skype.TransferSendfileError errorCode;
		public String errorFile;
	};
	
	/** Takes a list of fully-qualified filenames and initiates corresponding file transfers in the conversation. From the remote side, incoming file transfers will appear as a conversation message with type POSTED_FILES. Once such a message is detected, the list of file transfer objects can be retrieved with Message.GetTransfers. At that point, remote participants will need to accept or decline those transfers. 
	 * @param paths list of fully-qualified filenames to be transferred 
	 * @param body Optional BODY_XML property for POSTED_FILES type messages that show up in remote UI. 
	 * @return PostFilesResponse
	 * <br> - errorCode Error code, possible values come from the TRANSFER_SENDFILE_ERROR enumerator. This will be set for the first failed fail. The failed file is identified in the error_file return argument. 
	 * <br> - errorFile Filename of the file that triggered error. 
	 */
	public PostFilesResponse postFiles(String[] paths, String body) {
		try {
			Decoding decoder = sidDoRequest(postFiles_req)
			.addFilenameListParm(1, paths)
			.addStringParm(2, body)
			.endRequest();
			PostFilesResponse result = new PostFilesResponse();
			result.errorCode = (Skype.TransferSendfileError) decoder.getEnumParm(1, Skype.TransferSendfileError.get(0), false);
			result.errorFile = decoder.getFilenameParm(2, true);
			return result;
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] postVoiceMessage_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 22};
	/** Stops the active voicemail recording and sends it (dialog only)
	 * @param voicemail This argument is deprecated as of SDK version 3.2. Instead of manually constructing Voicemail object, you can call Conversation.StartVoiceMessage method to start recording a voicemail in context of a dialog. PostVoiceMessage will stop recording this voicemail and post it in the dialog. If instead of sending Voicemail, the user decides to cancel it, you should use Conversation.LeaveLiveSession method (Voicemail.Cancel is deprecated). 
	 * @param body Optional text message that remote UI can display in conversation, to notify the user of a new voicemail. 
	 */
	public void postVoiceMessage(Voicemail voicemail, String body) {
		try {
			sidDoRequest(postVoiceMessage_req)
			.addObjectParm(1, voicemail)
			.addStringParm(2, body)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] postSms_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 23};
	/** Takes an SMS instance created by Skype.CreateOutgoingSms and posts it in the conversation. Note that you will need to set both Sms body text (Sms.SetBody) and recipient list (Sms.SetTargets) before you can post the object. 
	 * @param sms SMS object. 
	 * @param body This argument is currently ignored. The message text needs to be set with Sms.SetBody method, prior to passing the Sms object to this method 
	 */
	public void postSms(Sms sms, String body) {
		try {
			sidDoRequest(postSms_req)
			.addObjectParm(1, sms)
			.addStringParm(2, body)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] getJoinBlob_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 24};
	/**
	 * Retrieves a binary join blob for joining public conversations, which are always of type CONFERENCE. If called for a dialog, the blob argument will contain the empty string. The best way to create a Public Chat is to first create a fresh conversation with Skype class CreateConference, then minimally apply the public chat options OPT_JOINING_ENABLED and OPT_ENTRY_LEVEL_RANK - options, like this (C++):  
	 * @code 
	 * C->SetOption(Conversation.OPT_JOINING_ENABLED, true); 
	 * </CODE> 
	 * 
	 * When that is done, you can call GetJoinBlob to retrieve the blob string. Use the blob string to generate and post an HTML link whose href attribute looks like this: href="skype:?chat&blob=_BLOB_GOES_HERE" A person running Skype desktop client can click this link to join the conversation and have that conversation opened in his UI. Note that the conversation host (creator) needs to be online for new joiners-via-link to start participating in the Public Chat.
	
	 * @return blob Returns the public conversation join blob. 
	 */
	public String getJoinBlob() {
		try {
			return sidDoRequest(getJoinBlob_req)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] join_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 25};
	/** Tries to join a public conversation (aka public chat). This method is only useful if you have used Skype.GetConversationByBlob method with alsoJoin argument set to false. 
	 */
	public void join() {
		try {
			sidDoRequest(join_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] enterPassword_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 26};
	/** Submits password for joining password-protected conversation. 
	 * @param password Password string. 
	 */
	public void enterPassword(String password) {		try {
			sidDoRequest(enterPassword_req)
			.addStringParm(1, password)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setPassword_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 27};
	/** Sets password protection/new password for the conversation. 
	 * @param password New password. 
	 * @param hint Password hint. 
	 */
	public void setPassword(String password, String hint) {
		try {
			sidDoRequest(setPassword_req)
			.addStringParm(1, password)
			.addStringParm(2, hint)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] retireFrom_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 28};
	/** Leaves the conference. Not applicable to dialogs. 
	 */
	public void retireFrom() {
		try {
			sidDoRequest(retireFrom_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] delete_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 47};
	/** Deletes this conversation, which must be of type CONFERENCE - dialogs between local user and any of his contacts are always persistant. Note that this also removes corresponding Message and Participant objects. 
	 */
	public void delete() {
		try {
			sidDoRequest(delete_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] renameTo_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 29};
	/** Changes the META_NAME property of the conversation. Note that unlike topic and guidelines, this rename is just local - remote participants can have their own private names for conversations. 
	 * @param name New name for the conversation. Passing an empty string in this argument causes the META_NAME to unset. 
	 */
	public void renameTo(String name) {
		try {
			sidDoRequest(renameTo_req)
			.addStringParm(1, name)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setBookmark_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 30};
	/** Setter for Conversation class IS_BOOKMARKED. 
	 * @param bookmark Set true to set the bookmark, false to remove the bookmark. 
	 */
	public void setBookmark(boolean bookmark) {
		try {
			sidDoRequest(setBookmark_req)
			.addBoolParm(1, bookmark)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setAlertString_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 31};
	/** Setter for Conversation class ALERT_STRING property. The main use of this property is checking bodies of incoming messages in the conversation for the alert string and producing notifications in UI for the user, when appropriate. 
	 * @param alertString Substring to check in BODY_XML property of incoming messages. 
	 */
	public void setAlertString(String alertString) {
		try {
			sidDoRequest(setAlertString_req)
			.addStringParm(1, alertString)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] removeFromInbox_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 32};
	/** Removes conversation from Inbox. 
	 */
	public void removeFromInbox() {
		try {
			sidDoRequest(removeFromInbox_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] addToInbox_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 33};
	/** Sets Conversation inbox_timestamp property. If the timestamp argument is left empty or is greater than conversation consumption horizon, then the conversation will be restored to the inbox. 
	 * @param timestamp If left empty or set to 0, the inbox_timestamp property is set to current time. 
	 */
	public void addToInbox(long timestamp) {
		try {
			sidDoRequest(addToInbox_req)
			.addTimestampParm(1, timestamp)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setConsumedHorizon_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 34};
	/** This method can be used to set the consumption (read) status of messages in the conversation. It sets Message.CONSUMPTION_STATUS to Message.CONSUMED for all messages in the conversation, older than the given timestamp. If the second argument is set to true, it also modifies messages more recent than the timestamp, by marking them as unconsumed. 
	 * @param timestamp Consumption cutoff timestamp. Setting this to current time will mark all messages in the conversation as consumed. 
	 * @param alsoUnconsume If set to true, this also marks messages newer than the cutoff timestamp as unconsumed. For example, setting timestamp to 0 and also_unconsumed to true, will unconsume all messages in the conversation. 
	 */
	public void setConsumedHorizon(long timestamp, boolean alsoUnconsume) {
		try {
			sidDoRequest(setConsumedHorizon_req)
			.addTimestampParm(1, timestamp)
			.addBoolParm(2, alsoUnconsume)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] markUnread_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 35};
	/** sets consumption horizon to last inbox message id timestamp
	 */
	public void markUnread() {
		try {
			sidDoRequest(markUnread_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] isMemberOf_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 37};
	/** Checks if the conversation is a member of the given ContactGroup 
	 * @param group ContactGroup 
	 * @return result True if this conversation is a member of the ContactGroup specified by the group argument contains the conversation 
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
	private final static byte[] getParticipants_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 38};
	/** Retrieves the list of this conversation's current participants, which you can optionally request to be filtered. If no Participants pass the filter, an empty list will be returned (the method itself still returns true). 
	 * @param filter Conversation.PARTICIPANTFILTER - defaults to Conversation.ALL 
	 * @return participants List of conversation Participant objects that passed the filter. 
	 */
	public Participant[] getParticipants(ParticipantFilter filter) {
		try {
			return (Participant[]) sidDoRequest(getParticipants_req)
			.addEnumParm(1, filter)
			.endRequest().getObjectListParm(1, 19, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getLastMessages_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 39};
	public class GetLastMessagesResponse {
		public Message[] contextMessages;
		public Message[] unconsumedMessages;
	};
	
	/** Returns recent messages. The messages are returned in two lists - new messages (unconsumed) and recent message history (context messages). The context message list contains messages that are already above the consumption horizon but are fairly recent, making it likely that displaying them in UI would be good default behaviour. 
	 * @param requireTimestamp If set to a non-zero value, includes messages no earlier than this timestamp, if not, includes messages from the last 24 hours only 
	 * @return GetLastMessagesResponse
	 * <br> - contextMessages Already consumed messages, provided for context
	 * <br> - unconsumedMessages Unconsumed messages
	 */
	public GetLastMessagesResponse getLastMessages(long requireTimestamp) {
		try {
			Decoding decoder = sidDoRequest(getLastMessages_req)
			.addTimestampParm(1, requireTimestamp)
			.endRequest();
			GetLastMessagesResponse result = new GetLastMessagesResponse();
			result.contextMessages = (Message[]) decoder.getObjectListParm(1, 9, false);
			result.unconsumedMessages = (Message[]) decoder.getObjectListParm(2, 9, true);
			return result;
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] findMessage_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 41};
	/** Finds the most recent Message object in the conversation that contains the substring specified by the text argument. If no matching messages are found, this method will return false. The search proceeds backwards in time, starting from the timestamp argument. To continue searching, you can start with timestamp=MAX_UINT, retrieve the TIMESTAMP property of the matching message, decrement it by one, and submit it as timestamp for the next FindMessage call. 	 * @param text Substring to search for. 
	 * @param fromTimestampUp
	 * @return message Returns matching message or 0 if there was no match. As the likelihood of this object being invalid is quite high, you should always check for method return value before you start calling methods of this object. 
	 */
	public Message findMessage(String text, long fromTimestampUp) {
		try {
			return (Message) sidDoRequest(findMessage_req)
			.addStringParm(1, text)
			.addTimestampParm(2, fromTimestampUp)
			.endRequest().getObjectParm(1, 9, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] attachVideoToLiveSession_req = {(byte) 90,(byte) 82,(byte) 18,(byte) 147,(byte) 1};
	/** Attaches send video to livesession. There is no detach. If you don't want to use this video in livesession anymore - stop it. Video object will stay attached until the end of livesession
	 * @param sendVideo
	 */
	public void attachVideoToLiveSession(Video sendVideo) {
		try {
			sidDoRequest(attachVideoToLiveSession_req)
			.addObjectParm(1, sendVideo)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	/***
	 * generic multiget of a list of Property
	 * @param requested the list of requested properties of Conversation
	 * @return SidGetResponding
	 */
	public SidGetResponding sidMultiGet(Property[] requested) {
		return super.sidMultiGet(requested);
	}
	/***
	 * generic multiget of list of Property for a list of Conversation
	 * @param requested the list of requested properties
	 * @return SidGetResponding[] can be casted to (Conversation[]) if all properties are cached
	 */
	static public SidGetResponding[] sidMultiGet(Property[] requested, Conversation[] objects) {
		return SidObject.sidMultiGet(requested, objects);
	}
	/*** multiget the following properties
	 * - P_DISPLAY_NAME
	 * - P_UNCONSUMED_NORMAL_MESSAGES
	 * - P_INBOX_TIMESTAMP
	 */
	public Conversation mgetInfo() {
		return (Conversation) super.sidMultiGet(Property.mget_info_mreq, this);
	}
	/*** multiget the following properties for a list of Conversation
	 * - P_DISPLAY_NAME
	 * - P_UNCONSUMED_NORMAL_MESSAGES
	 * - P_INBOX_TIMESTAMP
	 * @param objects targets of the request
	 * @return Conversation[] responses
	 */
	static public Conversation[] mgetInfo(Conversation[] objects) {
		return (Conversation[]) SidObject.sidMultiGet(Property.mget_info_mreq, objects, objects);
	}
	/** contact identity in case of dialogs, chat name in case of conferences */
	public String getIdentity() {
		synchronized(this) {
			if ((mSidCached & 0x1) != 0)
				return mIdentity;
		}
		return sidRequestStringProperty(Property.P_IDENTITY);
	}
	/** type of the conversation */
	public Type getType() {
		synchronized(this) {
			if ((mSidCached & 0x2) != 0)
				return mType;
		}
		return (Type) sidRequestEnumProperty(Property.P_TYPE);
	}
	/** host of current live session. none => no session. myself in case of 1:1 calls */
	public String getLiveHost() {
		synchronized(this) {
			if ((mSidCached & 0x4) != 0)
				return mLiveHost;
		}
		return sidRequestStringProperty(Property.P_LIVE_HOST);
	}
	/** moment when first participant other than host joined the current or last live session */
	public long getLiveStartTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x8) != 0)
				return mLiveStartTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_LIVE_START_TIMESTAMP);
	}
	/** if live session is muted */
	public boolean getLiveIsMuted() {
		synchronized(this) {
			if ((mSidCached & 0x10) != 0)
				return mLiveIsMuted;
		}
		return sidRequestBoolProperty(Property.P_LIVE_IS_MUTED);
	}
	/** '' everything matches, '=' nothing matches, '=string' string matches */
	public String getAlertString() {
		synchronized(this) {
			if ((mSidCached & 0x20) != 0)
				return mAlertString;
		}
		return sidRequestStringProperty(Property.P_ALERT_STRING);
	}
	/** if conversation is bookmarked/flagged */
	public boolean getIsBookmarked() {
		synchronized(this) {
			if ((mSidCached & 0x40) != 0)
				return mIsBookmarked;
		}
		return sidRequestBoolProperty(Property.P_IS_BOOKMARKED);
	}
	/** local name assigned via Rename */
	public String getGivenDisplayName() {
		synchronized(this) {
			if ((mSidCached & 0x80) != 0)
				return mGivenDisplayName;
		}
		return sidRequestStringProperty(Property.P_GIVEN_DISPLAY_NAME);
	}
	/** resulting display name of the conversation (based on given name, topic, participant list, etc) */
	public String getDisplayName() {
		synchronized(this) {
			if ((mSidCached & 0x100) != 0)
				return mDisplayName;
		}
		return sidRequestStringProperty(Property.P_DISPLAY_NAME);
	}
	/** if the conversation is live and in which status it is then */
	public LocalLiveStatus getLocalLiveStatus() {
		synchronized(this) {
			if ((mSidCached & 0x200) != 0)
				return mLocalLiveStatus;
		}
		return (LocalLiveStatus) sidRequestEnumProperty(Property.P_LOCAL_LIVE_STATUS);
	}
	/** timestamp to sort the conversations in inbox by. 0 means not in inbox */
	public long getInboxTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x400) != 0)
				return mInboxTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_INBOX_TIMESTAMP);
	}
	/** ID of the message that caused INBOX_TIMESTAMP to be set */
	public Message getInboxMessageId() {
		synchronized(this) {
			if ((mSidCached & 0x800) != 0)
				return mInboxMessageId;
		}
		return (Message) sidRequestObjectProperty(Property.P_INBOX_MESSAGE_ID);
	}
	/** number of messages in UNCONSUMED_SUPPRESSED consumption status */
	public int getUnconsumedSuppressedMessages() {
		synchronized(this) {
			if ((mSidCached & 0x1000) != 0)
				return mUnconsumedSuppressedMessages;
		}
		return sidRequestUintProperty(Property.P_UNCONSUMED_SUPPRESSED_MESSAGES);
	}
	/** number of messages in UNCONSUMED_NORMAL consumption status */
	public int getUnconsumedNormalMessages() {
		synchronized(this) {
			if ((mSidCached & 0x2000) != 0)
				return mUnconsumedNormalMessages;
		}
		return sidRequestUintProperty(Property.P_UNCONSUMED_NORMAL_MESSAGES);
	}
	/** DEPRECATED, not set anymore */
	public int getUnconsumedElevatedMessages() {
		synchronized(this) {
			if ((mSidCached & 0x4000) != 0)
				return mUnconsumedElevatedMessages;
		}
		return sidRequestUintProperty(Property.P_UNCONSUMED_ELEVATED_MESSAGES);
	}
	/** if there are unconsumed voice or call messages in the conversation */
	public boolean getUnconsumedMessagesVoice() {
		synchronized(this) {
			if ((mSidCached & 0x8000) != 0)
				return mUnconsumedMessagesVoice;
		}
		return sidRequestBoolProperty(Property.P_UNCONSUMED_MESSAGES_VOICE);
	}
	/** ID of voice message that is being played or recorded in this conversation */
	public Voicemail getActiveVoicemail() {
		synchronized(this) {
			if ((mSidCached & 0x10000) != 0)
				return mActiveVoicemail;
		}
		return (Voicemail) sidRequestObjectProperty(Property.P_ACTIVE_VOICEMAIL);
	}
	/** consumption cutoff timestamp: messages after (but not including) this are considered unconsumed */
	public long getConsumptionHorizon() {
		synchronized(this) {
			if ((mSidCached & 0x20000) != 0)
				return mConsumptionHorizon;
		}
		return sidRequestTimestampProperty(Property.P_CONSUMPTION_HORIZON);
	}
	/** timestamp of last activity in conversation */
	public long getLastActivityTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x40000) != 0)
				return mLastActivityTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_LAST_ACTIVITY_TIMESTAMP);
	}
	/** dialog this conference was spawned from */
	public Conversation getSpawnedFromConvoId() {
		synchronized(this) {
			if ((mSidCached & 0x80000) != 0)
				return mSpawnedFromConvoId;
		}
		return (Conversation) sidRequestObjectProperty(Property.P_SPAWNED_FROM_CONVO_ID);
	}
	/** identity of conversation creator (doesn't apply to dialogs) */
	public String getCreator() {
		synchronized(this) {
			if ((mSidCached & 0x100000) != 0)
				return mCreator;
		}
		return sidRequestStringProperty(Property.P_CREATOR);
	}
	/** timestamp of creation, tells you how far you can retrieve messages */
	public long getCreationTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x200000) != 0)				return mCreationTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_CREATION_TIMESTAMP);
	}
	/** my status in this conversation (connecting, participating, retired, etc) (doesn't apply to dialogs) */
	public MyStatus getMyStatus() {
		synchronized(this) {
			if ((mSidCached & 0x400000) != 0)
				return mMyStatus;
		}
		return (MyStatus) sidRequestEnumProperty(Property.P_MY_STATUS);
	}
	/** if it's a public conversation (doesn't apply to dialogs) */
	public boolean getOptJoiningEnabled() {
		synchronized(this) {
			if ((mSidCached & 0x800000) != 0)
				return mOptJoiningEnabled;
		}
		return sidRequestBoolProperty(Property.P_OPT_JOINING_ENABLED);
	}
	/** rank that is auto-assigned at join (doesn't apply to dialogs) */
	public Participant.Rank getOptEntryLevelRank() {
		synchronized(this) {
			if ((mSidCached & 0x1000000) != 0)
				return mOptEntryLevelRank;
		}
		return (Participant.Rank) sidRequestEnumProperty(Property.P_OPT_ENTRY_LEVEL_RANK);
	}
	/** if history visible to new consumers (doesn't apply to dialogs) */
	public boolean getOptDiscloseHistory() {
		synchronized(this) {
			if ((mSidCached & 0x2000000) != 0)
				return mOptDiscloseHistory;
		}
		return sidRequestBoolProperty(Property.P_OPT_DISCLOSE_HISTORY);
	}
	/** activities that only ADMIN can do. Bitmap of ALLOWED_ACTIVITY values (doesn't apply to dialogs) */
	public int getOptAdminOnlyActivities() {
		synchronized(this) {
			if ((mSidCached & 0x4000000) != 0)
				return mOptAdminOnlyActivities;
		}
		return sidRequestIntProperty(Property.P_OPT_ADMIN_ONLY_ACTIVITIES);
	}
	/** public conversation password hint, use SetPassword to set (doesn't apply to dialogs) */
	public String getPasswordHint() {
		synchronized(this) {
			if ((mSidCached & 0x8000000) != 0)
				return mPasswordHint;
		}
		return sidRequestStringProperty(Property.P_PASSWORD_HINT);
	}
	/** deprecated, not used */
	public String getMetaName() {
		synchronized(this) {
			if ((mSidCached & 0x10000000) != 0)
				return mMetaName;
		}
		return sidRequestStringProperty(Property.P_META_NAME);
	}
	/** conversation topic (doesn't apply to dialogs) */
	public String getMetaTopic() {
		synchronized(this) {
			if ((mSidCached & 0x20000000) != 0)
				return mMetaTopic;
		}
		return sidRequestXmlProperty(Property.P_META_TOPIC);
	}
	/** guidelines (doesn't apply to dialogs) */
	public String getMetaGuidelines() {
		synchronized(this) {
			if ((mSidCached & 0x40000000) != 0)
				return mMetaGuidelines;
		}
		return sidRequestXmlProperty(Property.P_META_GUIDELINES);
	}
	/** conversation picture, in jpeg format (doesn't apply to dialogs) */
	public byte[] getMetaPicture() {
		synchronized(this) {
			if ((mSidCached & 0x80000000) != 0)
				return mMetaPicture;
		}
		return sidRequestBinaryProperty(Property.P_META_PICTURE);
	}
	public String sidGetStringProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 972:
			return mIdentity;
		case 918:
			return mLiveHost;
		case 920:
			return mAlertString;
		case 925:
			return mGivenDisplayName;
		case 924:
			return mDisplayName;
		case 903:
			return mCreator;
		case 980:
			return mPasswordHint;
		case 910:
			return mMetaName;
		case 911:
			return mMetaTopic;
		case 913:
			return mMetaGuidelines;
		}
		return "";
	}
	public SidObject sidGetObjectProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 973:
			return mInboxMessageId;
		case 971:
			return mActiveVoicemail;
		case 915:
			return mSpawnedFromConvoId;
		}
		return null;
	}
	public boolean sidGetBoolProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 996:
			return mLiveIsMuted;
		case 921:
			return mIsBookmarked;
		case 970:
			return mUnconsumedMessagesVoice;
		case 922:
			return mOptJoiningEnabled;
		case 907:
			return mOptDiscloseHistory;
		}
		return false;
	}
	public int sidGetIntProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 975:
			return mUnconsumedSuppressedMessages;
		case 976:
			return mUnconsumedNormalMessages;
		case 977:
			return mUnconsumedElevatedMessages;
		case 909:
			return mOptAdminOnlyActivities;
		}
		return 0;
	}
	public EnumConverting sidGetEnumProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 902:
			return mType;
		case 927:
			return mLocalLiveStatus;
		case 919:
			return mMyStatus;
		case 906:
			return mOptEntryLevelRank;
		}
		return null;
	}
	public byte[] sidGetBinaryProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 914);
		return mMetaPicture;
	}
	public String getPropertyAsString(final int prop) {
		switch (prop) {
		case 972: return getIdentity();
		case 902: return getType().toString();
		case 918: return getLiveHost();
		case 996: return Boolean.toString(getLiveIsMuted());
		case 920: return getAlertString();
		case 921: return Boolean.toString(getIsBookmarked());
		case 925: return getGivenDisplayName();
		case 924: return getDisplayName();
		case 927: return getLocalLiveStatus().toString();
		case 973: return getInboxMessageId() != null ? Integer.toString(getInboxMessageId().getOid()) : "(null)";
		case 975: return Integer.toString(getUnconsumedSuppressedMessages());
		case 976: return Integer.toString(getUnconsumedNormalMessages());
		case 977: return Integer.toString(getUnconsumedElevatedMessages());
		case 970: return Boolean.toString(getUnconsumedMessagesVoice());
		case 971: return getActiveVoicemail() != null ? Integer.toString(getActiveVoicemail().getOid()) : "(null)";
		case 915: return getSpawnedFromConvoId() != null ? Integer.toString(getSpawnedFromConvoId().getOid()) : "(null)";
		case 903: return getCreator();
		case 919: return getMyStatus().toString();
		case 922: return Boolean.toString(getOptJoiningEnabled());
		case 906: return getOptEntryLevelRank().toString();
		case 907: return Boolean.toString(getOptDiscloseHistory());
		case 909: return Integer.toString(getOptAdminOnlyActivities());
		case 980: return getPasswordHint();
		case 910: return getMetaName();
		case 914: return "<binary>";
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
			int bit  = 1<<((idx-1)%32);
			synchronized (this) {
				mSidCached|=bit;
				switch (propertyId) {
				case 972:
					if (svalue != null) mIdentity = svalue;
					else mSidCached &=~bit;
					break;
				case 902: mType = Type.get(value); break;
				case 918:
					if (svalue != null) mLiveHost = svalue;
					else mSidCached &=~bit;
					break;
				case 996: mLiveIsMuted = value != 0; break;
				case 920:
					if (svalue != null) mAlertString = svalue;
					else mSidCached &=~bit;
					break;
				case 921: mIsBookmarked = value != 0; break;
				case 925:
					if (svalue != null) mGivenDisplayName = svalue;
					else mSidCached &=~bit;
					break;
				case 924:
					if (svalue != null) mDisplayName = svalue;
					else mSidCached &=~bit;
					break;
				case 927: mLocalLiveStatus = LocalLiveStatus.get(value); break;
				case 973:
					if (value != 0)
						mInboxMessageId = (Message) mSidRoot.sidGetObject(property.getModuleId(), value);
					else {
						mInboxMessageId = null;
						mSidCached &=~bit;
					}
					break;
				case 975: mUnconsumedSuppressedMessages = value; break;
				case 976: mUnconsumedNormalMessages = value; break;
				case 977: mUnconsumedElevatedMessages = value; break;
				case 970: mUnconsumedMessagesVoice = value != 0; break;
				case 971:
					if (value != 0)
						mActiveVoicemail = (Voicemail) mSidRoot.sidGetObject(property.getModuleId(), value);
					else {
						mActiveVoicemail = null;
						mSidCached &=~bit;
					}
					break;
				case 915:
					if (value != 0)
						mSpawnedFromConvoId = (Conversation) mSidRoot.sidGetObject(property.getModuleId(), value);
					else {
						mSpawnedFromConvoId = null;
						mSidCached &=~bit;
					}
					break;
				case 903:
					if (svalue != null) mCreator = svalue;					else mSidCached &=~bit;
					break;
				case 919: mMyStatus = MyStatus.get(value); break;
				case 922: mOptJoiningEnabled = value != 0; break;
				case 906: mOptEntryLevelRank = Participant.Rank.get(value); break;
				case 907: mOptDiscloseHistory = value != 0; break;
				case 909: mOptAdminOnlyActivities = value; break;
				case 980:
					if (svalue != null) mPasswordHint = svalue;
					else mSidCached &=~bit;
					break;
				case 910:
					if (svalue != null) mMetaName = svalue;
					else mSidCached &=~bit;
					break;
				default: mSidCached&=~bit; break;
				}
			}
		}
		ConversationListener listener = ((Skype) mSidRoot).getConversationListener();
		if (listener != null)
			listener.onPropertyChange(this, property, value, svalue);
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final String newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 972:
			mSidCached |= 0x1;
			mIdentity=  newValue;
			break;
		case 918:
			mSidCached |= 0x4;
			mLiveHost=  newValue;
			break;
		case 920:
			mSidCached |= 0x20;
			mAlertString=  newValue;
			break;
		case 925:
			mSidCached |= 0x80;
			mGivenDisplayName=  newValue;
			break;
		case 924:
			mSidCached |= 0x100;
			mDisplayName=  newValue;
			break;
		case 903:
			mSidCached |= 0x100000;
			mCreator=  newValue;
			break;
		case 980:
			mSidCached |= 0x8000000;
			mPasswordHint=  newValue;
			break;
		case 910:
			mSidCached |= 0x10000000;
			mMetaName=  newValue;
			break;
		case 911:
			mSidCached |= 0x20000000;
			mMetaTopic=  newValue;
			break;
		case 913:
			mSidCached |= 0x40000000;
			mMetaGuidelines=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final SidObject newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 973:
			mSidCached |= 0x800;
			mInboxMessageId= (Message) newValue;
			break;
		case 971:
			mSidCached |= 0x10000;
			mActiveVoicemail= (Voicemail) newValue;
			break;
		case 915:
			mSidCached |= 0x80000;
			mSpawnedFromConvoId= (Conversation) newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final int newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 902:
			mSidCached |= 0x2;
			mType= Type.get(newValue);
			break;
		case 996:
			mSidCached |= 0x10;
			mLiveIsMuted= newValue != 0;
			break;
		case 921:
			mSidCached |= 0x40;
			mIsBookmarked= newValue != 0;
			break;
		case 927:
			mSidCached |= 0x200;
			mLocalLiveStatus= LocalLiveStatus.get(newValue);
			break;
		case 975:
			mSidCached |= 0x1000;
			mUnconsumedSuppressedMessages=  newValue;
			break;
		case 976:
			mSidCached |= 0x2000;
			mUnconsumedNormalMessages=  newValue;
			break;
		case 977:
			mSidCached |= 0x4000;
			mUnconsumedElevatedMessages=  newValue;
			break;
		case 970:
			mSidCached |= 0x8000;
			mUnconsumedMessagesVoice= newValue != 0;
			break;
		case 919:
			mSidCached |= 0x400000;
			mMyStatus= MyStatus.get(newValue);
			break;
		case 922:
			mSidCached |= 0x800000;
			mOptJoiningEnabled= newValue != 0;
			break;
		case 906:
			mSidCached |= 0x1000000;
			mOptEntryLevelRank= Participant.Rank.get(newValue);
			break;
		case 907:
			mSidCached |= 0x2000000;
			mOptDiscloseHistory= newValue != 0;
			break;
		case 909:
			mSidCached |= 0x4000000;
			mOptAdminOnlyActivities=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final byte[] newValue) {
		final int propId = prop.getId();
		assert(propId == 914);
		mSidCached |= 0x80000000;
		mMetaPicture=  newValue;
	}
	public String           mIdentity;
	public Type             mType;
	public String           mLiveHost;
	public long             mLiveStartTimestamp;
	public boolean          mLiveIsMuted;
	public String           mAlertString;
	public boolean          mIsBookmarked;
	public String           mGivenDisplayName;
	public String           mDisplayName;
	public LocalLiveStatus  mLocalLiveStatus;
	public long             mInboxTimestamp;
	public Message          mInboxMessageId;
	public int              mUnconsumedSuppressedMessages;
	public int              mUnconsumedNormalMessages;
	public int              mUnconsumedElevatedMessages;
	public boolean          mUnconsumedMessagesVoice;
	public Voicemail        mActiveVoicemail;
	public long             mConsumptionHorizon;
	public long             mLastActivityTimestamp;
	public Conversation     mSpawnedFromConvoId;
	public String           mCreator;
	public long             mCreationTimestamp;
	public MyStatus         mMyStatus;
	public boolean          mOptJoiningEnabled;
	public Participant.Rank mOptEntryLevelRank;
	public boolean          mOptDiscloseHistory;
	public int              mOptAdminOnlyActivities;
	public String           mPasswordHint;
	public String           mMetaName;
	public String           mMetaTopic;
	public String           mMetaGuidelines;
	public byte[]           mMetaPicture;
	public int moduleId() {
		return 18;
	}
	
	public Conversation(final int oid, final SidRoot root) {
		super(oid, root, 32);
	}
}
