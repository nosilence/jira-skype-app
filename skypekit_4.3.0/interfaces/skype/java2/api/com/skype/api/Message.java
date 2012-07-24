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
import com.skype.api.Skype;
import com.skype.ipc.SidGetResponding;

/**
 * Events in a conversation context are expressed as Messages. It is therefore useful to think of Message objects as events, rather than specifically text chat messages. 
 * 
 * Message member functions all return a Boolean indicating the success (true) or failure (false) of processing the request itself (transport, runtime availability, and so forth) - not the success or failure of its associated functionality. For example, Message.Edit returns true if it was able to make a determination, and its result parameter reflects whether this Message can be edited. Similarly, Message.Edit returns false if it was unable to make a determination, and the value of its result parameter is undefined. 
 * 
 * Message member functions that are specific to a Message TYPE return false if this Message is not of that type. For example, Message.GetVoiceMessage will return false if this Message's type is not POSTED_VOICE_MESSAGE. 
 * 
 * The actual meaning of a Message can be determined by its P_TYPE property. The meanings of most other Message properties depend on the value of P_TYPE. For example, let's take P_BODY_XML property. 
 * 
 * Following messages have a text entered by the user as a body. It may contain emoticons, URLs, etc. 
 *  - POSTED_TEXT 
 *  - POSTED_EMOTE 
 *  - SET_METADATA 
 *  - REQUESTED_AUTH 
 * 
 * Following messages have a custom XML format for the body (see the specific section on these message types for details): 
 *  - POSTED_CONTACTS 
 *  - POSTED_VOICE_MESSAGE 
 *  - POSTED_FILES 
 *  - POSTED_SMS 
 *  - STARTED_LIVESESSION and ENDED_LIVESESSION (same format) 
 * 
 * Following messages do not use the body property: 
 *  - SPAWNED_CONFERENCE 
 *  - ADDED_CONSUMERS 
 *  - ADDED_APPLICANTS 
 *  - RETIRED_OTHERS 
 *  - RETIRED 
 *  - SET_RANK 
 *  - HAS_BIRTHDAY 
 *  - GRANTED_AUTH 
 *  - BLOCKED 
 * 
 * Messages such as POSTED_TEXT use a small subset of a HTML-like markup to control the visual representation of the text. This markup is used by POSTED_TEXT and POSTED_EMOTE, but also for the conversation topic (CONVERSATION_META_TOPIC property and the body of the SET_METADATA message) and for authorization requests. 
 * 
 * Having chat messages in XML format means that all formatting is indicated by XML tags. This includes emoticons and URls. The big advantage is that it makes the parsing of the message by the UI much easier. The UI does not need to do emoticons or URL detection, this is already done and it only needs to look for the XML tags. 
 * 
 * For text messages, it is possible for the UI to simply ignore (meaning strip) the XML and the message will be understandable fine, it will only have lost some formatting. 
 * 
 * But it is obviously nicer to display at least the most commonly used tags. 
 * 
 * To strip the XML: 
 *  - if they have the alt="sometext" attribute set, return sometext as the output of that tag and ignore the rest of tag and all nested sub tags 
 *  - if no alt="" attribute set, use tag content as output - <sometag>hereissomething</sometag> is output as hereissomething
 *  - if no alt="" and no tag content, ignore the tag altogether (return nothing) 
 * Skype for Windows supports displaying many XML tags, but only a sub-set is regularly used and should be supported by the UI for a good experience. These are the ones described here. 
 * Animated emoticons 
 * Emoticons are encoded with the "ss" tag. The element content is the plain text representation. It has a "type" attribute indicating the emoticons canonical name. Example:  
 * @code 
 * Hi <ss type="smile">:-)</ss>  
 * </CODE> 
 * 
 * Flag emoticons 
 * Flag emoticons are little flags. They are encoded with the "flag" tag. The element contents is the plain text representation and it has a "country" attribute which is a 2-letter ISO-3166 country code. The user can enter a flag using "(flag:XX)", where XX is a valid ISO country code. Example:  
 * @code 
 * I am in <flag country="cc">CC</flag>  
 * </CODE> 
 * 
 * Links 
 * If the library detects a URL, it will encode it using the html "a" tag, with the "href" attribute indicating the URL. The plain text representation is what the user originally typed. Example:  
 * @code 
 * I am in <a href="http://wwww.skype.com">www.skype.com</a> 
 * </CODE> 
 * 
 * Alert matches 
 * When a conversation is configured to display only alerts if specific words are present in the message (see "/alertson [text to match]" command), if a message matches the alert, it will be marked with the <alertmatch> tag. This allows the UI to highlight the word matching. Example:  
 * @code 
 * Maybe <alertmatch>Vincent</alertmatch> knows the answer  
 * </CODE> 
 * 
 * Bold, italic, etc 
 * Skype for Windows also supports displaying bold and italic text, using the "b" and "i" tags. 
 * 
 * Encoding messages 
 * When sending a chat message via PostText(), there is the possibility to indicate if the library should do the XML encoding, or if the message has already been encoded. Usually, the UI can let library do the encoding. This is the case when the message does not contain any specific formatting. It may contain emoticons or URls, which will be detected by the library encoder and converted into XML tags. 
 * If the message has some more complex encoding, such as a quote or some bold text, it is up to the UI to encode the message. 
 */
public final class Message extends SidObject {
	/** The P_TYPE property determines the actual meaning of the Message object. Only Messages of POSTED_TEXT type contain actual text messages. The meaning and content of the rest of the message properties are largely dependant of the value of the Message.P_TYPE.  */
	public enum Type implements EnumConverting {
		/** Conference metadata were changed */
		SET_METADATA        (2),
		/** A conference was spawned from this dialog */
		SPAWNED_CONFERENCE  (4),
		/** Some users were added to the conference */
		ADDED_CONSUMERS     (10),
		/** Some users are applying to be added to the conference */
		ADDED_APPLICANTS    (11),
		/** User was kicked from the conference */
		RETIRED_OTHERS      (12),
		/** User left the conference */
		RETIRED             (13),
		/** Changed the rank of a user in the Conversation (multichat administration)  */
		SET_RANK            (21),
		/** A live session started */
		STARTED_LIVE_SESSION(30),
		/** A live session ended */
		ENDED_LIVE_SESSION  (39),
		/** User requested authorization */
		REQUESTED_AUTH      (50),
		/** User was granted authorization. Notification message that user is now an authorized contact (of the local user).  */
		GRANTED_AUTH        (51),
		/** User was blocked */
		BLOCKED             (53),
		/** A text message */
		POSTED_TEXT         (61),
		/** An emote ('John Doe is laughing', cf /me chat command) */
		POSTED_EMOTE        (60),
		/** The message represents (a set of) contact card(s) posted in the conversation. One message can contain more than one contact cards. The contacts can be retrieved from the message by parsing them out from the P_BODY_XML property. For more information, see Conversation.PostContacts  */
		POSTED_CONTACTS     (63),
		/** The message represents an SMS object that was posted in the Conversation. See Conversation.PostSMS for more details. The Sms object itself can be retrieved from the Message with Message.GetSms The message BODY_XML contains a set of SMS properties, such as status, failurereason, targets, price and timestamp.  */
		POSTED_SMS          (64),
		/** Deprecated, never sent */
		POSTED_ALERT        (65),
		/** A voicemail */
		POSTED_VOICE_MESSAGE(67),
		/** The message represents a (list of) file transfers that were posted in the Conversation with Conversation.PostFiles. Transfer objects can be retrieved from the Message with Message.GetTransfers  */
		POSTED_FILES        (68),
		/** Currently unused.  */
		POSTED_INVOICE      (69),
		/** The message represents a Contact birthday notification.  */
		HAS_BIRTHDAY        (110);
		private final int key;
		Type(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return SET_METADATA; }
		public EnumConverting convert(int from) { return Type.get(from); }
		public EnumConverting[] getArray(final int size) { return new Type[size]; }		public static Type get(int from) {
			switch (from) {
			case   2: return SET_METADATA;
			case   4: return SPAWNED_CONFERENCE;
			case  10: return ADDED_CONSUMERS;
			case  11: return ADDED_APPLICANTS;
			case  12: return RETIRED_OTHERS;
			case  13: return RETIRED;
			case  21: return SET_RANK;
			case  30: return STARTED_LIVE_SESSION;
			case  39: return ENDED_LIVE_SESSION;
			case  50: return REQUESTED_AUTH;
			case  51: return GRANTED_AUTH;
			case  53: return BLOCKED;
			case  61: return POSTED_TEXT;
			case  60: return POSTED_EMOTE;
			case  63: return POSTED_CONTACTS;
			case  64: return POSTED_SMS;
			case  65: return POSTED_ALERT;
			case  67: return POSTED_VOICE_MESSAGE;
			case  68: return POSTED_FILES;
			case  69: return POSTED_INVOICE;
			case 110: return HAS_BIRTHDAY;
			}
			return SET_METADATA;
		}
		public static final int SET_METADATA_VALUE         =   2;
		public static final int SPAWNED_CONFERENCE_VALUE   =   4;
		public static final int ADDED_CONSUMERS_VALUE      =  10;
		public static final int ADDED_APPLICANTS_VALUE     =  11;
		public static final int RETIRED_OTHERS_VALUE       =  12;
		public static final int RETIRED_VALUE              =  13;
		public static final int SET_RANK_VALUE             =  21;
		public static final int STARTED_LIVE_SESSION_VALUE =  30;
		public static final int ENDED_LIVE_SESSION_VALUE   =  39;
		public static final int REQUESTED_AUTH_VALUE       =  50;
		public static final int GRANTED_AUTH_VALUE         =  51;
		public static final int BLOCKED_VALUE              =  53;
		public static final int POSTED_TEXT_VALUE          =  61;
		public static final int POSTED_EMOTE_VALUE         =  60;
		public static final int POSTED_CONTACTS_VALUE      =  63;
		public static final int POSTED_SMS_VALUE           =  64;
		public static final int POSTED_ALERT_VALUE         =  65;
		public static final int POSTED_VOICE_MESSAGE_VALUE =  67;
		public static final int POSTED_FILES_VALUE         =  68;
		public static final int POSTED_INVOICE_VALUE       =  69;
		public static final int HAS_BIRTHDAY_VALUE         = 110;
	}
	public enum SendingStatus implements EnumConverting {
		/** Message has not been delivered to at least one of the participants  */
		SENDING       (1),
		/** Message has been delivered to at least one other participant  */
		SENT          (2),
		/** Message could not be delivered (for SMS this reflects the actual SMS, not the chat message)  */
		FAILED_TO_SEND(3);
		private final int key;
		SendingStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return SENDING; }
		public EnumConverting convert(int from) { return SendingStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new SendingStatus[size]; }
		public static SendingStatus get(int from) {
			switch (from) {
			case 1: return SENDING;
			case 2: return SENT;
			case 3: return FAILED_TO_SEND;
			}
			return SENDING;
		}
		public static final int SENDING_VALUE        = 1;
		public static final int SENT_VALUE           = 2;
		public static final int FAILED_TO_SEND_VALUE = 3;
	}
	/** Indicates if a message has been consumed (meaning read) or not */
	public enum ConsumptionStatus implements EnumConverting {
		/** Message has been read. Note that this is a read-only property. Consumption status of individual messages can not be set selectively. Message consumption status is determined at the conversation level, based conversation consumption horizon and individual message timestamps. Conversation consumption horizon can be updated with Conversation.SetConsumedHorizon method.   */
		CONSUMED             (0),
		/** Do not notify the user that they have this unread message  */
		UNCONSUMED_SUPPRESSED(1),
		/** Notify the user that they have this unread message  */
		UNCONSUMED_NORMAL    (2),
		/** This message consumption state is marked as DEPRECATED  */
		UNCONSUMED_ELEVATED  (3);
		private final int key;
		ConsumptionStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return CONSUMED; }
		public EnumConverting convert(int from) { return ConsumptionStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new ConsumptionStatus[size]; }
		public static ConsumptionStatus get(int from) {
			switch (from) {
			case 0: return CONSUMED;
			case 1: return UNCONSUMED_SUPPRESSED;
			case 2: return UNCONSUMED_NORMAL;
			case 3: return UNCONSUMED_ELEVATED;
			}
			return CONSUMED;
		}
		public static final int CONSUMED_VALUE              = 0;
		public static final int UNCONSUMED_SUPPRESSED_VALUE = 1;
		public static final int UNCONSUMED_NORMAL_VALUE     = 2;
		public static final int UNCONSUMED_ELEVATED_VALUE   = 3;
	}
	/**
	 * For messages of type SET_METADATA that alert participants to changes to the associated Conversation's metadata, indicates which metadata property changed and its P_BODY_XML property contains the changed data. Your UI is expected to detect messages with PARAM_KEY set and to update its visual representation of Conversation accordingly. 
	 * You can use the associated Conversation's properties and methods to obtain the updated metadata rather than parse the message body XML, for example, Conversation.P_META_PICTURE and Conversation.Conversation.GetPropMetaPicture. 
	 */
	public enum SetMetadataKey implements EnumConverting {
		/** Notification message that conversation name has changed.  */
		SET_META_NAME      (3640),
		/** Notification message that conversation topic has changed.  */
		SET_META_TOPIC     (3644),
		/** Notification message that conversation guidelines have changed.  */
		SET_META_GUIDELINES(3652),
		/** Notification message that conversation picture has changed.  */
		SET_META_PICTURE   (3658);
		private final int key;
		SetMetadataKey(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return SET_META_NAME; }
		public EnumConverting convert(int from) { return SetMetadataKey.get(from); }
		public EnumConverting[] getArray(final int size) { return new SetMetadataKey[size]; }
		public static SetMetadataKey get(int from) {
			switch (from) {
			case 3640: return SET_META_NAME;
			case 3644: return SET_META_TOPIC;
			case 3652: return SET_META_GUIDELINES;
			case 3658: return SET_META_PICTURE;
			}
			return SET_META_NAME;
		}
		public static final int SET_META_NAME_VALUE       = 3640;
		public static final int SET_META_TOPIC_VALUE      = 3644;
		public static final int SET_META_GUIDELINES_VALUE = 3652;
		public static final int SET_META_PICTURE_VALUE    = 3658;
	}
	/** Indicates the reason a user could not join or left a Conversation. SkypeKit automatically sets "could not join"-related values. "Left voluntarily"-related values are set as a result of explicit user actions.  */
	public enum Leavereason implements EnumConverting {
		/** User cannot chat (user is currently logged in with a client that has chat disabled - see Contact.CAPABILITY.CAPABILITY_TEXT)  */
		USER_INCAPABLE          (2),
		/** Attempt to add local user to a conversation by an unknown contact  */
		ADDER_MUST_BE_FRIEND    (3),
		/** Attempt to add local user to a conversation by an unauthorized contact  */
		ADDER_MUST_BE_AUTHORIZED(4),
		/** Local user declined an "invitation" to join a chat  */
		DECLINE_ADD             (5),
		/** User decided to end participation in an on-going multi-chat  */
		UNSUBSCRIBE             (6);
		private final int key;
		Leavereason(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return USER_INCAPABLE; }
		public EnumConverting convert(int from) { return Leavereason.get(from); }
		public EnumConverting[] getArray(final int size) { return new Leavereason[size]; }
		public static Leavereason get(int from) {
			switch (from) {
			case 2: return USER_INCAPABLE;
			case 3: return ADDER_MUST_BE_FRIEND;
			case 4: return ADDER_MUST_BE_AUTHORIZED;
			case 5: return DECLINE_ADD;
			case 6: return UNSUBSCRIBE;
			}
			return USER_INCAPABLE;
		}
		public static final int USER_INCAPABLE_VALUE           = 2;
		public static final int ADDER_MUST_BE_FRIEND_VALUE     = 3;		public static final int ADDER_MUST_BE_AUTHORIZED_VALUE = 4;
		public static final int DECLINE_ADD_VALUE              = 5;
		public static final int UNSUBSCRIBE_VALUE              = 6;
	}
	/** bitfield used to express what can be done with a message */
	public enum Permissions implements EnumConverting {
		PERM_NONE     (0),
		/** message can be edited with new content */
		PERM_EDITABLE (1),
		/** message can be deleted whole (edited to blank) */
		PERM_DELETABLE(2);
		private final int key;
		Permissions(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return PERM_NONE; }
		public EnumConverting convert(int from) { return Permissions.get(from); }
		public EnumConverting[] getArray(final int size) { return new Permissions[size]; }
		public static Permissions get(int from) {
			switch (from) {
			case 0: return PERM_NONE;
			case 1: return PERM_EDITABLE;
			case 2: return PERM_DELETABLE;
			}
			return PERM_NONE;
		}
		public static final int PERM_NONE_VALUE      = 0;
		public static final int PERM_EDITABLE_VALUE  = 1;
		public static final int PERM_DELETABLE_VALUE = 2;
	}
	private final static byte[] P_CONVERSATION_req = {(byte) 90,(byte) 71,(byte) 192,(byte) 7,(byte) 93,(byte) 9};
	private final static byte[] P_CONVO_GUID_req = {(byte) 90,(byte) 71,(byte) 120,(byte) 93,(byte) 9};
	private final static byte[] P_AUTHOR_req = {(byte) 90,(byte) 71,(byte) 122,(byte) 93,(byte) 9};
	private final static byte[] P_AUTHOR_DISPLAY_NAME_req = {(byte) 90,(byte) 71,(byte) 123,(byte) 93,(byte) 9};
	private final static byte[] P_GUID_req = {(byte) 90,(byte) 71,(byte) 152,(byte) 6,(byte) 93,(byte) 9};
	private final static byte[] P_ORIGINALLY_MEANT_FOR_req = {(byte) 90,(byte) 71,(byte) 150,(byte) 6,(byte) 93,(byte) 9};
	private final static byte[] P_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 121,(byte) 93,(byte) 9};
	private final static byte[] P_TYPE_req = {(byte) 90,(byte) 71,(byte) 193,(byte) 7,(byte) 93,(byte) 9};
	private final static byte[] P_SENDING_STATUS_req = {(byte) 90,(byte) 71,(byte) 194,(byte) 7,(byte) 93,(byte) 9};
	private final static byte[] P_CONSUMPTION_STATUS_req = {(byte) 90,(byte) 71,(byte) 200,(byte) 7,(byte) 93,(byte) 9};
	private final static byte[] P_EDITED_BY_req = {(byte) 90,(byte) 71,(byte) 222,(byte) 1,(byte) 93,(byte) 9};
	private final static byte[] P_EDIT_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 223,(byte) 1,(byte) 93,(byte) 9};
	private final static byte[] P_PARAM_KEY_req = {(byte) 90,(byte) 71,(byte) 195,(byte) 7,(byte) 93,(byte) 9};
	private final static byte[] P_PARAM_VALUE_req = {(byte) 90,(byte) 71,(byte) 196,(byte) 7,(byte) 93,(byte) 9};
	private final static byte[] P_BODY_XML_req = {(byte) 90,(byte) 71,(byte) 127,(byte) 93,(byte) 9};
	private final static byte[] P_IDENTITIES_req = {(byte) 90,(byte) 71,(byte) 125,(byte) 93,(byte) 9};
	private final static byte[] P_REASON_req = {(byte) 90,(byte) 71,(byte) 198,(byte) 7,(byte) 93,(byte) 9};
	private final static byte[] P_LEAVEREASON_req = {(byte) 90,(byte) 71,(byte) 126,(byte) 93,(byte) 9};
	private final static byte[] P_PARTICIPANT_COUNT_req = {(byte) 90,(byte) 71,(byte) 214,(byte) 7,(byte) 93,(byte) 9};
	/** Properties of the Message class */
	public enum Property implements PropertyEnumConverting {
		P_UNKNOWN             (0,0,null,0,null),
		P_CONVERSATION        (960, 1, P_CONVERSATION_req, 18, null),
		P_CONVO_GUID          (120, 2, P_CONVO_GUID_req, 0, null),
		P_AUTHOR              (122, 3, P_AUTHOR_req, 0, null),
		P_AUTHOR_DISPLAY_NAME (123, 4, P_AUTHOR_DISPLAY_NAME_req, 0, null),
		P_GUID                (792, 5, P_GUID_req, 0, null),
		P_ORIGINALLY_MEANT_FOR(790, 6, P_ORIGINALLY_MEANT_FOR_req, 0, null),
		P_TIMESTAMP           (121, 7, P_TIMESTAMP_req, 0, null),
		P_TYPE                (961, 8, P_TYPE_req, 0, Type.get(0)),
		P_SENDING_STATUS      (962, 9, P_SENDING_STATUS_req, 0, SendingStatus.get(0)),
		P_CONSUMPTION_STATUS  (968, 10, P_CONSUMPTION_STATUS_req, 0, ConsumptionStatus.get(0)),
		P_EDITED_BY           (222, 11, P_EDITED_BY_req, 0, null),
		P_EDIT_TIMESTAMP      (223, 12, P_EDIT_TIMESTAMP_req, 0, null),
		P_PARAM_KEY           (963, 13, P_PARAM_KEY_req, 0, null),
		P_PARAM_VALUE         (964, 14, P_PARAM_VALUE_req, 0, null),
		P_BODY_XML            (127, 15, P_BODY_XML_req, 0, null),
		P_IDENTITIES          (125, 16, P_IDENTITIES_req, 0, null),
		P_REASON              (966, 17, P_REASON_req, 0, null),
		P_LEAVEREASON         (126, 18, P_LEAVEREASON_req, 0, Skype.LeaveReason.get(0)),
		P_PARTICIPANT_COUNT   (982, 19, P_PARTICIPANT_COUNT_req, 0, null);
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
			case 960: return P_CONVERSATION;
			case 120: return P_CONVO_GUID;
			case 122: return P_AUTHOR;
			case 123: return P_AUTHOR_DISPLAY_NAME;
			case 792: return P_GUID;
			case 790: return P_ORIGINALLY_MEANT_FOR;
			case 121: return P_TIMESTAMP;
			case 961: return P_TYPE;
			case 962: return P_SENDING_STATUS;
			case 968: return P_CONSUMPTION_STATUS;
			case 222: return P_EDITED_BY;
			case 223: return P_EDIT_TIMESTAMP;
			case 963: return P_PARAM_KEY;
			case 964: return P_PARAM_VALUE;
			case 127: return P_BODY_XML;
			case 125: return P_IDENTITIES;
			case 966: return P_REASON;
			case 126: return P_LEAVEREASON;
			case 982: return P_PARTICIPANT_COUNT;
			}
			return P_UNKNOWN;
		}
		public static final int P_CONVERSATION_VALUE         = 960;
		public static final int P_CONVO_GUID_VALUE           = 120;
		public static final int P_AUTHOR_VALUE               = 122;
		public static final int P_AUTHOR_DISPLAY_NAME_VALUE  = 123;
		public static final int P_GUID_VALUE                 = 792;
		public static final int P_ORIGINALLY_MEANT_FOR_VALUE = 790;
		public static final int P_TIMESTAMP_VALUE            = 121;
		public static final int P_TYPE_VALUE                 = 961;
		public static final int P_SENDING_STATUS_VALUE       = 962;
		public static final int P_CONSUMPTION_STATUS_VALUE   = 968;
		public static final int P_EDITED_BY_VALUE            = 222;
		public static final int P_EDIT_TIMESTAMP_VALUE       = 223;
		public static final int P_PARAM_KEY_VALUE            = 963;
		public static final int P_PARAM_VALUE_VALUE          = 964;
		public static final int P_BODY_XML_VALUE             = 127;
		public static final int P_IDENTITIES_VALUE           = 125;
		public static final int P_REASON_VALUE               = 966;
		public static final int P_LEAVEREASON_VALUE          = 126;
		public static final int P_PARTICIPANT_COUNT_VALUE    = 982;
		public static final Property[] mget_info_mreq = { P_CONVERSATION, P_AUTHOR_DISPLAY_NAME, P_TYPE, P_BODY_XML, P_TIMESTAMP };
	}
	private final static byte[] canEdit_req = {(byte) 90,(byte) 82,(byte) 9,(byte) 1};
	/** For Message types having a body, determines whether that body is editable by the user.  
	 * @return result
	 */
	public boolean canEdit() {
		try {
			return sidDoRequest(canEdit_req)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] getPermissions_req = {(byte) 90,(byte) 82,(byte) 9,(byte) 58};
	/** get all permissions allowed on this message
	 * @return result
	 */
	public Permissions getPermissions() {
		try {			return (Permissions) sidDoRequest(getPermissions_req)
			.endRequest().getEnumParm(1, Permissions.get(0), true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return Permissions.get(0);
		}
	}
	private final static byte[] edit_req = {(byte) 90,(byte) 82,(byte) 9,(byte) 2};
	/**
	 * For Message types that include a body and are editable:  
	 *  - alters BODY_XML of the message object 
	 *  - sets EDITED_BY and EDIT_TIMESTAMP properties  
	 *  - propagates the changes to remote users. 
	
	 * @param newText New value of the message BODY_XML property. 
	 * @param isXml Specify isXML as true if the message body is formatted as XML; omit it or specify it as false if the message body is plain text. 
	 * @param undo Reverts the message body to the original version. newText argument is ignored when this is set. 
	 * @param legacyPrefix If the edit is received by a client not supporting displaying edits,                       it will be shown as a new message, prefixed by this parameter.                       Default value is a prefix in English.
	 */
	public void edit(String newText, boolean isXml, boolean undo, String legacyPrefix) {
		try {
			sidDoRequest(edit_req)
			.addStringParm(1, newText)
			.addBoolParm(2, isXml)
			.addBoolParm(3, undo)
			.addStringParm(4, legacyPrefix)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] getContacts_req = {(byte) 90,(byte) 82,(byte) 9,(byte) 3};
	/** For messages of type POSTED_CONTACTS, parses the body XML and formats the data as a list of Contact instances. 
	 * @return contacts
	 */
	public Contact[] getContacts() {
		try {
			return (Contact[]) sidDoRequest(getContacts_req)
			.endRequest().getObjectListParm(1, 2, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getTransfers_req = {(byte) 90,(byte) 82,(byte) 9,(byte) 4};
	/** For messages of type POSTED_FILES, parses the body XML and creates a list of Transfer instances. 
	 * @return transfers
	 */
	public Transfer[] getTransfers() {
		try {
			return (Transfer[]) sidDoRequest(getTransfers_req)
			.endRequest().getObjectListParm(1, 6, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getVoiceMessage_req = {(byte) 90,(byte) 82,(byte) 9,(byte) 5};
	/** For messages of type POSTED_VOICE_MESSAGE, parses the body XML and creates a Voicemail instance. 
	 * @return voicemail
	 */
	public Voicemail getVoiceMessage() {
		try {
			return (Voicemail) sidDoRequest(getVoiceMessage_req)
			.endRequest().getObjectParm(1, 7, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getSms_req = {(byte) 90,(byte) 82,(byte) 9,(byte) 6};
	/** For messages of type POSTED_SMS, parses the body XML and creates an SMS instances 
	 * @return sms
	 */
	public Sms getSms() {
		try {
			return (Sms) sidDoRequest(getSms_req)
			.endRequest().getObjectParm(1, 12, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] deleteLocally_req = {(byte) 90,(byte) 82,(byte) 9,(byte) 8};
	/** Deletes this message from the local database. These deletions do not propagate to the other Skype instances that the user may have on other computers. Nor do they affect other participants that have the same message. This method is specifically from removing Message objects from the database - not for removing Messages from conversations. To remove a Message from a conversation, use Message.Edit method to replace the existing body text with an empty string. 
	 */
	public void deleteLocally() {
		try {
			sidDoRequest(deleteLocally_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	/***
	 * generic multiget of a list of Property
	 * @param requested the list of requested properties of Message
	 * @return SidGetResponding
	 */
	public SidGetResponding sidMultiGet(Property[] requested) {
		return super.sidMultiGet(requested);
	}
	/***
	 * generic multiget of list of Property for a list of Message
	 * @param requested the list of requested properties
	 * @return SidGetResponding[] can be casted to (Message[]) if all properties are cached
	 */
	static public SidGetResponding[] sidMultiGet(Property[] requested, Message[] objects) {
		return SidObject.sidMultiGet(requested, objects);
	}
	/*** multiget the following properties
	 * - P_CONVERSATION
	 * - P_AUTHOR_DISPLAY_NAME
	 * - P_TYPE
	 * - P_BODY_XML
	 * - P_TIMESTAMP
	 */
	public Message mgetInfo() {
		return (Message) super.sidMultiGet(Property.mget_info_mreq, this);
	}
	/*** multiget the following properties for a list of Message
	 * - P_CONVERSATION
	 * - P_AUTHOR_DISPLAY_NAME
	 * - P_TYPE
	 * - P_BODY_XML
	 * - P_TIMESTAMP
	 * @param objects targets of the request
	 * @return Message[] responses
	 */
	static public Message[] mgetInfo(Message[] objects) {
		return (Message[]) SidObject.sidMultiGet(Property.mget_info_mreq, objects, objects);
	}
	/** DB ID of corresponding conversation */
	public Conversation getConversation() {
		synchronized(this) {
			if ((mSidCached & 0x1) != 0)
				return mConversation;
		}
		return (Conversation) sidRequestObjectProperty(Property.P_CONVERSATION);
	}
	/** GUID of the Conversation. The GUID is a "global ID" - these values are shared accross Skype client instances and accross all the participants of the conversation.  */
	public String getConvoGuid() {
		synchronized(this) {
			if ((mSidCached & 0x2) != 0)
				return mConvoGuid;
		}
		return sidRequestStringProperty(Property.P_CONVO_GUID);
	}
	/** Identity of the sender. While this is almost always the same as SKYPENAME property of the Contact, in some rare cases it can also be a phone number - for example, incoming voicemail notification Messages (message type = POSTED_VOICE_MESSAGE).  */
	public String getAuthor() {
		synchronized(this) {
			if ((mSidCached & 0x4) != 0)
				return mAuthor;
		}
		return sidRequestStringProperty(Property.P_AUTHOR);
	}
	/** displayname of the sender at the time of posting */
	public String getAuthorDisplayName() {
		synchronized(this) {
			if ((mSidCached & 0x8) != 0)
				return mAuthorDisplayName;
		}
		return sidRequestStringProperty(Property.P_AUTHOR_DISPLAY_NAME);
	}
	/** Unlike the message id, the GUID is the same on all instances and for all participants.  */
	public byte[] getGuid() {
		synchronized(this) {
			if ((mSidCached & 0x10) != 0)
				return mGuid;
		}
		return sidRequestBinaryProperty(Property.P_GUID);
	}
	/** This property gets set when a conference is spawned from dialog Conversation. In that case recent message history from the original dialog is copied to the target conversation. For all the copied messages, the ORIGINALLY_MEANT_FOR property will be set to identity of the remote participant of the original dialog.  */
	public String getOriginallyMeantFor() {
		synchronized(this) {
			if ((mSidCached & 0x20) != 0)
				return mOriginallyMeantFor;
		}
		return sidRequestStringProperty(Property.P_ORIGINALLY_MEANT_FOR);
	}
	/** UNIX timestamp (sent time, adjusted for local clock) */
	public long getTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x40) != 0)
				return mTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_TIMESTAMP);
	}
	public Type getType() {
		synchronized(this) {
			if ((mSidCached & 0x80) != 0)
				return mType;
		}
		return (Type) sidRequestEnumProperty(Property.P_TYPE);
	}
	public SendingStatus getSendingStatus() {
		synchronized(this) {
			if ((mSidCached & 0x100) != 0)
				return mSendingStatus;
		}
		return (SendingStatus) sidRequestEnumProperty(Property.P_SENDING_STATUS);
	}
	public ConsumptionStatus getConsumptionStatus() {
		synchronized(this) {
			if ((mSidCached & 0x200) != 0)
				return mConsumptionStatus;
		}
		return (ConsumptionStatus) sidRequestEnumProperty(Property.P_CONSUMPTION_STATUS);
	}
	/** Identity of the author that last edited this message. NULL if message has not been edited  */
	public String getEditedBy() {
		synchronized(this) {
			if ((mSidCached & 0x400) != 0)
				return mEditedBy;		}
		return sidRequestStringProperty(Property.P_EDITED_BY);
	}
	/** UNIX timestamp of last edit */
	public long getEditTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x800) != 0)
				return mEditTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_EDIT_TIMESTAMP);
	}
	/** Message type-specific parameter. See Message.SET_METADATA_KEY for more information.  */
	public int getParamKey() {
		synchronized(this) {
			if ((mSidCached & 0x1000) != 0)
				return mParamKey;
		}
		return sidRequestUintProperty(Property.P_PARAM_KEY);
	}
	/** Message type-specific parameter  */
	public int getParamValue() {
		synchronized(this) {
			if ((mSidCached & 0x2000) != 0)
				return mParamValue;
		}
		return sidRequestUintProperty(Property.P_PARAM_VALUE);
	}
	/** Message type-specific parameter  */
	public String getBodyXml() {
		synchronized(this) {
			if ((mSidCached & 0x4000) != 0)
				return mBodyXml;
		}
		return sidRequestXmlProperty(Property.P_BODY_XML);
	}
	/**
	 * Message type-specific parameter. Depending of Message type, this property contains: 
	 *  - STARTED_LIVESESSION - list of participants in the cal; 
	 *  - ENDED_LIVESESSION - list of participants in the call; 
	 *  - POSTED_SMS - list of recipients of the message; 
	 *  - SPAWNED_CONFERENCE - the list of identities that were added; 
	 *  - ADDED_CONSUMERS - the list of identities that were added; 
	 *  - RETIRED_OTHERS - the skypename of the participant who was kicked; 
	 *  - SET_RANK - the skypename of the participant whose rank was changed; 
	 *  - REQUESTED_AUTH - Message.P_AUTHOR and Message.P_IDENTITIES are set to the users receiving and requesting the authorization, depending if the message was received or sent; 
	 *  - GRANTED_AUTH - the skypename of the user we granted authorization; 
	 *  - BLOCKED - the skypename of the user who was blocked; 
	 *  - HAS_BIRTHDAY - skypename of current logged in user. 
	 */
	public String getIdentities() {
		synchronized(this) {
			if ((mSidCached & 0x8000) != 0)
				return mIdentities;
		}
		return sidRequestStringProperty(Property.P_IDENTITIES);
	}
	/**
	 * Message type-specific parameter. Possible values for STARTED/ENDED_LIVESESSION (only set for dialogs): 
	 *  - no_answer  
	 *  - manual  
	 *  - busy  
	 *  - connection_dropped 
	 *  - no_skypeout_subscription; 
	 *  - insufficient_funds 
	 *  - internet_connection_lost 
	 *  - skypeout_account_blocked 
	 *  - pstn_could_not_connect_to_skype_proxy 
	 *  - pstn_invalid_number 
	 *  - pstn_number_forbidden 
	 *  - pstn_call_timed_out 
	 *  - pstn_busy 
	 *  - pstn_call_terminated 
	 *  - pstn_network_error 
	 *  - number_unavailable 
	 *  - pstn_call_rejected 
	 *  - pstn_misc_error 
	 *  - internal_error 
	 *  - unable_to_connect 
	 *  - connection_dropped 
	 *  - recording_failed 
	 *  - playback_error 
	 *  - legacy_error 
	 *  - blocked_by_privacy_settings 
	 *  - error 
	 *  - transfer_failed 
	 *  - transfer_insufficient_funds 
	 *  - blocked_by_us 
	 *  - emergency_call_denied 
	 * 
	 * This information is now available as an enum in LEAVEREASON 
	 */
	public String getReason() {
		synchronized(this) {
			if ((mSidCached & 0x10000) != 0)
				return mReason;
		}
		return sidRequestStringProperty(Property.P_REASON);
	}
	/** Leave reason for message of the RETIRED type, and STARTED/ENDED_LIVESESSION.                   Use for STARTED/ENDED_LIVESESSION is to provide simpler, enum based                   handling and deprecates the reason property (only set for dialogs) */
	public Skype.LeaveReason getLeavereason() {
		synchronized(this) {
			if ((mSidCached & 0x20000) != 0)
				return mLeavereason;
		}
		return (Skype.LeaveReason) sidRequestEnumProperty(Property.P_LEAVEREASON);
	}
	/** Number of people who received this message (including local user)  */
	public int getParticipantCount() {
		synchronized(this) {
			if ((mSidCached & 0x40000) != 0)
				return mParticipantCount;
		}
		return sidRequestUintProperty(Property.P_PARTICIPANT_COUNT);
	}
	public String sidGetStringProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 120:
			return mConvoGuid;
		case 122:
			return mAuthor;
		case 123:
			return mAuthorDisplayName;
		case 790:
			return mOriginallyMeantFor;
		case 222:
			return mEditedBy;
		case 127:
			return mBodyXml;
		case 125:
			return mIdentities;
		case 966:
			return mReason;
		}
		return "";
	}
	public SidObject sidGetObjectProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 960);
		return mConversation;
	}
	public int sidGetIntProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 963:
			return mParamKey;
		case 964:
			return mParamValue;
		case 982:
			return mParticipantCount;
		}
		return 0;
	}
	public EnumConverting sidGetEnumProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 961:
			return mType;
		case 962:
			return mSendingStatus;
		case 968:
			return mConsumptionStatus;
		case 126:
			return mLeavereason;
		}
		return null;
	}
	public byte[] sidGetBinaryProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 792);
		return mGuid;
	}
	public String getPropertyAsString(final int prop) {
		switch (prop) {
		case 960: return getConversation() != null ? Integer.toString(getConversation().getOid()) : "(null)";
		case 120: return getConvoGuid();
		case 122: return getAuthor();
		case 123: return getAuthorDisplayName();
		case 792: return "<binary>";
		case 790: return getOriginallyMeantFor();
		case 961: return getType().toString();
		case 962: return getSendingStatus().toString();
		case 968: return getConsumptionStatus().toString();
		case 222: return getEditedBy();
		case 963: return Integer.toString(getParamKey());
		case 964: return Integer.toString(getParamValue());
		case 125: return getIdentities();
		case 966: return getReason();
		case 126: return getLeavereason().toString();
		case 982: return Integer.toString(getParticipantCount());
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
				case 960:
					if (value != 0)
						mConversation = (Conversation) mSidRoot.sidGetObject(property.getModuleId(), value);
					else {
						mConversation = null;
						mSidCached &=~bit;
					}
					break;
				case 120:
					if (svalue != null) mConvoGuid = svalue;
					else mSidCached &=~bit;
					break;
				case 122:
					if (svalue != null) mAuthor = svalue;
					else mSidCached &=~bit;
					break;
				case 123:
					if (svalue != null) mAuthorDisplayName = svalue;
					else mSidCached &=~bit;
					break;
				case 790:
					if (svalue != null) mOriginallyMeantFor = svalue;
					else mSidCached &=~bit;
					break;
				case 961: mType = Type.get(value); break;
				case 962: mSendingStatus = SendingStatus.get(value); break;
				case 968: mConsumptionStatus = ConsumptionStatus.get(value); break;
				case 222:
					if (svalue != null) mEditedBy = svalue;
					else mSidCached &=~bit;
					break;
				case 963: mParamKey = value; break;
				case 964: mParamValue = value; break;
				case 125:
					if (svalue != null) mIdentities = svalue;
					else mSidCached &=~bit;
					break;
				case 966:
					if (svalue != null) mReason = svalue;
					else mSidCached &=~bit;
					break;
				case 126: mLeavereason = Skype.LeaveReason.get(value); break;
				case 982: mParticipantCount = value; break;
				default: mSidCached&=~bit; break;
				}
			}
		}
		MessageListener listener = ((Skype) mSidRoot).getMessageListener();
		if (listener != null)
			listener.onPropertyChange(this, property, value, svalue);
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final String newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 120:
			mSidCached |= 0x2;
			mConvoGuid=  newValue;
			break;
		case 122:
			mSidCached |= 0x4;			mAuthor=  newValue;
			break;
		case 123:
			mSidCached |= 0x8;
			mAuthorDisplayName=  newValue;
			break;
		case 790:
			mSidCached |= 0x20;
			mOriginallyMeantFor=  newValue;
			break;
		case 222:
			mSidCached |= 0x400;
			mEditedBy=  newValue;
			break;
		case 127:
			mSidCached |= 0x4000;
			mBodyXml=  newValue;
			break;
		case 125:
			mSidCached |= 0x8000;
			mIdentities=  newValue;
			break;
		case 966:
			mSidCached |= 0x10000;
			mReason=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final SidObject newValue) {
		final int propId = prop.getId();
		assert(propId == 960);
		mSidCached |= 0x1;
		mConversation= (Conversation) newValue;
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final int newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 961:
			mSidCached |= 0x80;
			mType= Type.get(newValue);
			break;
		case 962:
			mSidCached |= 0x100;
			mSendingStatus= SendingStatus.get(newValue);
			break;
		case 968:
			mSidCached |= 0x200;
			mConsumptionStatus= ConsumptionStatus.get(newValue);
			break;
		case 963:
			mSidCached |= 0x1000;
			mParamKey=  newValue;
			break;
		case 964:
			mSidCached |= 0x2000;
			mParamValue=  newValue;
			break;
		case 126:
			mSidCached |= 0x20000;
			mLeavereason= Skype.LeaveReason.get(newValue);
			break;
		case 982:
			mSidCached |= 0x40000;
			mParticipantCount=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final byte[] newValue) {
		final int propId = prop.getId();
		assert(propId == 792);
		mSidCached |= 0x10;
		mGuid=  newValue;
	}
	public Conversation      mConversation;
	public String            mConvoGuid;
	public String            mAuthor;
	public String            mAuthorDisplayName;
	public byte[]            mGuid;
	public String            mOriginallyMeantFor;
	public long              mTimestamp;
	public Type              mType;
	public SendingStatus     mSendingStatus;
	public ConsumptionStatus mConsumptionStatus;
	public String            mEditedBy;
	public long              mEditTimestamp;
	public int               mParamKey;
	public int               mParamValue;
	public String            mBodyXml;
	public String            mIdentities;
	public String            mReason;
	public Skype.LeaveReason mLeavereason;
	public int               mParticipantCount;
	public int moduleId() {
		return 9;
	}
	
	public Message(final int oid, final SidRoot root) {
		super(oid, root, 19);
	}
}
