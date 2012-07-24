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
import com.skype.ipc.SidGetResponding;

/**
 * Wrapper class that includes voicemail-specific methods and properties. In the Skype Conversation API, Voicemail is actually something of a misnomer for what would be more accurately called Voice Message. 
 * 
 * The traditional Voicemail use case involves recording a voice message when an incoming call does not get answered in a pre-determined amount of time. In the Skype Conversation API, voicemail does not depend on a call going unanswered - you can post a voice message asynchronously into any dialog conversation at any time. 
 * 
 * In fact, a high-level action flow directing unanswered incoming live sessions to voicemail is not something provided by the Conversation API - implementation of this use case is largely up to your UI. 
 * 
 * The fact that your UI must retrieve incoming Voicemails by monitoring changes to a Conversation instance's Messages illustrates this conceptual difference between traditional voicemail and voice messages. The message type Message.POSTED_VOICE_MESSAGE indicates that a Message instance should be handled as a voice message instead of by displaying its body text in the Conversation UI. Message.GetVoiceMessage enables you to retrieve the associated Voicemail instance; Voicemail.StartPlayback enables you to listen to the message audio. 
 * 
 * To put it another way, the object chain goes like this:  
 * @code 
 * Contact->Conversation->Message->Voicemail  
 * </CODE>  
 * 
 * There are three basic types of Voicemail objects: 
 *  - INCOMING - received voice messages that can be retrieved from Message objects; 
 *  - OUTGOING - outbound voice messages that can be created with Conversation.StartVoiceMessage; 
 *  - GREETING - voice messages that represent auto-answer greetings, either recorded by the user (CUSTOM_GREETING) or included as part of SkypeKit (DEFAULT_GREETING). This is the audio that gets played back to sender of the voice message before he can start recording his voice message. 
 * 
 * Before trying to send out a voicemail, you should ensure that target Contact has the capability to receive them. Use Contact.GetCapabilityStatus to check for Contact.CAPABILITY_CAN_BE_SENT_VM. 
 * 
 * Recording and Sending a Voice Message 
 * 
 * The first step is to obtain a dialog Conversation with the target Contact. In that conversation, you can initiate the outgoing voice message with Conversation.StartVoiceMessage 
 * 
 * Note that this call does not return an actual Voicemail object. To catch when an object gets created, you will need to check Conversation.P_ACTIVE_VM_ID property updates. 
 * 
 * After invoking Conversation.StartVoiceMessage, SkypeKit instantiates a Voicemail instance for the target Contact's greeting (with type CUSTOM_GREETING or DEFAULT_GREETING). At this point, the Conversation.P_ACTIVE_VM_ID property update fires, newVM contains a reference to the greeting, and playback of the greeting for the sender starts automatically.  
 * 
 * Once the greeting playback finishes, SkypeKit instantiates a second Voicemail instance for the outgoing voice message. At this point, the Conversation.P_ACTIVE_VM_ID property update fires again, newVM now contains a reference to the outgoing message, and recording starts automatically. If you want to include notification and/or error handling for whether this message was sent successfully, you should make a copy of newVM now. 
 * 
 * Once the user finishes (or abandons) recording of their message, they want to either send the message or to cancel it. To send the message, use Conversation.PostVoiceMessage; to cancel the message, use Conversation.LeaveLiveSession. 
 * 
 * Both of these actions results in the Conversation.P_ACTIVE_VM_ID property update firing for a third time, setting the value to NULL. However, the Voicemail object will actually continue its existence past this point. Saving a reference to the message's Voicemail object when you start recording it enables you to keep receiving Voicemail property updates. This in turn enables your UI to check whether voice message send succeeded or failed. 
 * 
 * The relevant terminal state Voicemail.P_STATUS property values are: 
 *  - Voicemail.CANCELLED - recording and/or sending of this message was cancelled 
 *  - Voicemail.UPLOADED - message sent 
 *  - Voicemail.FAILED - message could not be sent 
 * 
 * Receiving and Playing Back a Voice Message 
 * 
 * On the remote side, the Voicemail appears as a Message object of type Message.POSTED_VOICE_MESSAGE. The message's author property contains the Skype Name of the Voicemail originator, and its BodyXml property contains the message length and title text in following format: 
 * 
 * @code 
 * <voicemail alt="Sent voicemail to people in this conversation."><message length="5" ></message></voicemail>  
 * </CODE> 
 * 
 * Receiver side UI can then retrieve the Voicemail object from the message with Message.GetVoiceMessage and 
 * start local playback with Message.StartPlayback. 
 */
public final class Voicemail extends SidObject {
	public enum Type implements EnumConverting {
		INCOMING        (1),
		DEFAULT_GREETING(4),
		CUSTOM_GREETING (2),
		OUTGOING        (3);
		private final int key;
		Type(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return INCOMING; }
		public EnumConverting convert(int from) { return Type.get(from); }
		public EnumConverting[] getArray(final int size) { return new Type[size]; }
		public static Type get(int from) {
			switch (from) {
			case 1: return INCOMING;
			case 4: return DEFAULT_GREETING;
			case 2: return CUSTOM_GREETING;
			case 3: return OUTGOING;
			}
			return INCOMING;
		}
		public static final int INCOMING_VALUE         = 1;
		public static final int DEFAULT_GREETING_VALUE = 4;
		public static final int CUSTOM_GREETING_VALUE  = 2;
		public static final int OUTGOING_VALUE         = 3;
	}
	public enum Status implements EnumConverting {
		NOTDOWNLOADED  (1),
		DOWNLOADING    (2),
		UNPLAYED       (3),
		BUFFERING      (4),
		PLAYING        (5),
		PLAYED         (6),
		BLANK          (7),
		RECORDING      (8),
		RECORDED       (9),
		UPLOADING      (10),
		UPLOADED       (11),
		DELETING       (12),
		FAILED         (13),
		DELETING_FAILED(14),
		CHECKING       (15),
		CANCELLED      (16);
		private final int key;
		Status(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return NOTDOWNLOADED; }
		public EnumConverting convert(int from) { return Status.get(from); }
		public EnumConverting[] getArray(final int size) { return new Status[size]; }
		public static Status get(int from) {
			switch (from) {
			case  1: return NOTDOWNLOADED;
			case  2: return DOWNLOADING;
			case  3: return UNPLAYED;
			case  4: return BUFFERING;
			case  5: return PLAYING;
			case  6: return PLAYED;
			case  7: return BLANK;
			case  8: return RECORDING;
			case  9: return RECORDED;
			case 10: return UPLOADING;
			case 11: return UPLOADED;
			case 12: return DELETING;
			case 13: return FAILED;
			case 14: return DELETING_FAILED;
			case 15: return CHECKING;
			case 16: return CANCELLED;
			}
			return NOTDOWNLOADED;
		}
		public static final int NOTDOWNLOADED_VALUE   =  1;
		public static final int DOWNLOADING_VALUE     =  2;
		public static final int UNPLAYED_VALUE        =  3;
		public static final int BUFFERING_VALUE       =  4;
		public static final int PLAYING_VALUE         =  5;
		public static final int PLAYED_VALUE          =  6;
		public static final int BLANK_VALUE           =  7;
		public static final int RECORDING_VALUE       =  8;
		public static final int RECORDED_VALUE        =  9;
		public static final int UPLOADING_VALUE       = 10;
		public static final int UPLOADED_VALUE        = 11;
		public static final int DELETING_VALUE        = 12;
		public static final int FAILED_VALUE          = 13;
		public static final int DELETING_FAILED_VALUE = 14;
		public static final int CHECKING_VALUE        = 15;
		public static final int CANCELLED_VALUE       = 16;
	}
	public enum FailureReason implements EnumConverting {
		MISC_ERROR                 (1),
		CONNECT_ERROR              (2),
		NO_VOICEMAIL_CAPABILITY    (3),		NO_SUCH_VOICEMAIL          (4),
		FILE_READ_ERROR            (5),
		FILE_WRITE_ERROR           (6),
		RECORDING_ERROR            (7),
		PLAYBACK_ERROR             (8),
		NO_PERMISSION              (9),
		/** receiver turned off voicemail */
		RECEIVER_DISABLED_VOICEMAIL(10),
		/** receiver has not authorized you and privacy is not set to anyone */
		SENDER_NOT_AUTHORIZED      (11),
		/** receiver blocked sender */
		SENDER_BLOCKED             (12);
		private final int key;
		FailureReason(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return MISC_ERROR; }
		public EnumConverting convert(int from) { return FailureReason.get(from); }
		public EnumConverting[] getArray(final int size) { return new FailureReason[size]; }
		public static FailureReason get(int from) {
			switch (from) {
			case  1: return MISC_ERROR;
			case  2: return CONNECT_ERROR;
			case  3: return NO_VOICEMAIL_CAPABILITY;
			case  4: return NO_SUCH_VOICEMAIL;
			case  5: return FILE_READ_ERROR;
			case  6: return FILE_WRITE_ERROR;
			case  7: return RECORDING_ERROR;
			case  8: return PLAYBACK_ERROR;
			case  9: return NO_PERMISSION;
			case 10: return RECEIVER_DISABLED_VOICEMAIL;
			case 11: return SENDER_NOT_AUTHORIZED;
			case 12: return SENDER_BLOCKED;
			}
			return MISC_ERROR;
		}
		public static final int MISC_ERROR_VALUE                  =  1;
		public static final int CONNECT_ERROR_VALUE               =  2;
		public static final int NO_VOICEMAIL_CAPABILITY_VALUE     =  3;
		public static final int NO_SUCH_VOICEMAIL_VALUE           =  4;
		public static final int FILE_READ_ERROR_VALUE             =  5;
		public static final int FILE_WRITE_ERROR_VALUE            =  6;
		public static final int RECORDING_ERROR_VALUE             =  7;
		public static final int PLAYBACK_ERROR_VALUE              =  8;
		public static final int NO_PERMISSION_VALUE               =  9;
		public static final int RECEIVER_DISABLED_VOICEMAIL_VALUE = 10;
		public static final int SENDER_NOT_AUTHORIZED_VALUE       = 11;
		public static final int SENDER_BLOCKED_VALUE              = 12;
	}
	private final static byte[] P_TYPE_req = {(byte) 90,(byte) 71,(byte) 100,(byte) 93,(byte) 7};
	private final static byte[] P_PARTNER_HANDLE_req = {(byte) 90,(byte) 71,(byte) 101,(byte) 93,(byte) 7};
	private final static byte[] P_PARTNER_DISPLAY_NAME_req = {(byte) 90,(byte) 71,(byte) 102,(byte) 93,(byte) 7};
	private final static byte[] P_STATUS_req = {(byte) 90,(byte) 71,(byte) 103,(byte) 93,(byte) 7};
	private final static byte[] P_FAILURE_REASON_req = {(byte) 90,(byte) 71,(byte) 104,(byte) 93,(byte) 7};
	private final static byte[] P_SUBJECT_req = {(byte) 90,(byte) 71,(byte) 105,(byte) 93,(byte) 7};
	private final static byte[] P_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 106,(byte) 93,(byte) 7};
	private final static byte[] P_DURATION_req = {(byte) 90,(byte) 71,(byte) 107,(byte) 93,(byte) 7};
	private final static byte[] P_ALLOWED_DURATION_req = {(byte) 90,(byte) 71,(byte) 108,(byte) 93,(byte) 7};
	private final static byte[] P_PLAYBACK_PROGRESS_req = {(byte) 90,(byte) 71,(byte) 109,(byte) 93,(byte) 7};
	private final static byte[] P_CONVERSATION_req = {(byte) 90,(byte) 71,(byte) 190,(byte) 6,(byte) 93,(byte) 7};
	private final static byte[] P_CHAT_MSG_GUID_req = {(byte) 90,(byte) 71,(byte) 191,(byte) 6,(byte) 93,(byte) 7};
	/** Properties of the Voicemail class */
	public enum Property implements PropertyEnumConverting {
		P_UNKNOWN             (0,0,null,0,null),
		P_TYPE                (100, 1, P_TYPE_req, 0, Type.get(0)),
		P_PARTNER_HANDLE      (101, 2, P_PARTNER_HANDLE_req, 0, null),
		P_PARTNER_DISPLAY_NAME(102, 3, P_PARTNER_DISPLAY_NAME_req, 0, null),
		P_STATUS              (103, 4, P_STATUS_req, 0, Status.get(0)),
		P_FAILURE_REASON      (104, 5, P_FAILURE_REASON_req, 0, FailureReason.get(0)),
		P_SUBJECT             (105, 6, P_SUBJECT_req, 0, null),
		P_TIMESTAMP           (106, 7, P_TIMESTAMP_req, 0, null),
		P_DURATION            (107, 8, P_DURATION_req, 0, null),
		P_ALLOWED_DURATION    (108, 9, P_ALLOWED_DURATION_req, 0, null),
		P_PLAYBACK_PROGRESS   (109, 10, P_PLAYBACK_PROGRESS_req, 0, null),
		P_CONVERSATION        (830, 11, P_CONVERSATION_req, 18, null),
		P_CHAT_MSG_GUID       (831, 12, P_CHAT_MSG_GUID_req, 0, null);
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
			case 100: return P_TYPE;
			case 101: return P_PARTNER_HANDLE;
			case 102: return P_PARTNER_DISPLAY_NAME;
			case 103: return P_STATUS;
			case 104: return P_FAILURE_REASON;
			case 105: return P_SUBJECT;
			case 106: return P_TIMESTAMP;
			case 107: return P_DURATION;
			case 108: return P_ALLOWED_DURATION;
			case 109: return P_PLAYBACK_PROGRESS;
			case 830: return P_CONVERSATION;
			case 831: return P_CHAT_MSG_GUID;
			}
			return P_UNKNOWN;
		}
		public static final int P_TYPE_VALUE                 = 100;
		public static final int P_PARTNER_HANDLE_VALUE       = 101;
		public static final int P_PARTNER_DISPLAY_NAME_VALUE = 102;
		public static final int P_STATUS_VALUE               = 103;
		public static final int P_FAILURE_REASON_VALUE       = 104;
		public static final int P_SUBJECT_VALUE              = 105;
		public static final int P_TIMESTAMP_VALUE            = 106;
		public static final int P_DURATION_VALUE             = 107;
		public static final int P_ALLOWED_DURATION_VALUE     = 108;
		public static final int P_PLAYBACK_PROGRESS_VALUE    = 109;
		public static final int P_CONVERSATION_VALUE         = 830;
		public static final int P_CHAT_MSG_GUID_VALUE        = 831;
	}
	private final static byte[] startRecording_req = {(byte) 90,(byte) 82,(byte) 7,(byte) 3};
	/** Start recording your own auto-answer greeting message (leave message after the beep...) only. Recording of outgoing Voicemail messages start automatically (using Conversation.StartVoiceMessage) after playback of the remote side greeting message has finished. 
	 */
	public void startRecording() {
		try {
			sidDoRequest(startRecording_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] stopRecording_req = {(byte) 90,(byte) 82,(byte) 7,(byte) 4};
	/** Stop recording of your own auto-answer greeting message only. To stop recording of and send an outgoing Voicemail, use Conversation.PostVoiceMessage. 
	 */
	public void stopRecording() {
		try {
			sidDoRequest(stopRecording_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] startPlayback_req = {(byte) 90,(byte) 82,(byte) 7,(byte) 5};
	/** Initiates playback of a voice message 
	 */
	public void startPlayback() {
		try {
			sidDoRequest(startPlayback_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] stopPlayback_req = {(byte) 90,(byte) 82,(byte) 7,(byte) 6};
	/** Terminates playback of a voice message 
	 */
	public void stopPlayback() {
		try {
			sidDoRequest(stopPlayback_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] delete_req = {(byte) 90,(byte) 82,(byte) 7,(byte) 7};
	/** first from server, and then the local copy
	 */
	public void delete() {
		try {
			sidDoRequest(delete_req)			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] cancel_req = {(byte) 90,(byte) 82,(byte) 7,(byte) 8};
	/** Canceling recording of your own auto-answer greeting message. To stop recording of and cancel an outgoing Voicemail, use Conversation.LeaveLiveSession. 
	 */
	public void cancel() {
		try {
			sidDoRequest(cancel_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] checkPermission_req = {(byte) 90,(byte) 82,(byte) 7,(byte) 13};
	/** check if we can send voicemail (unauth,blocked,no priv etc cases). only OUTGOING
	 * @return result
	 */
	public boolean checkPermission() {
		try {
			return sidDoRequest(checkPermission_req)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	/***
	 * generic multiget of a list of Property
	 * @param requested the list of requested properties of Voicemail
	 * @return SidGetResponding
	 */
	public SidGetResponding sidMultiGet(Property[] requested) {
		return super.sidMultiGet(requested);
	}
	/***
	 * generic multiget of list of Property for a list of Voicemail
	 * @param requested the list of requested properties
	 * @return SidGetResponding[] can be casted to (Voicemail[]) if all properties are cached
	 */
	static public SidGetResponding[] sidMultiGet(Property[] requested, Voicemail[] objects) {
		return SidObject.sidMultiGet(requested, objects);
	}
	public Type getType() {
		synchronized(this) {
			if ((mSidCached & 0x1) != 0)
				return mType;
		}
		return (Type) sidRequestEnumProperty(Property.P_TYPE);
	}
	/** registered username of the other party */
	public String getPartnerHandle() {
		synchronized(this) {
			if ((mSidCached & 0x2) != 0)
				return mPartnerHandle;
		}
		return sidRequestStringProperty(Property.P_PARTNER_HANDLE);
	}
	/** user's display name of the other party */
	public String getPartnerDisplayName() {
		synchronized(this) {
			if ((mSidCached & 0x4) != 0)
				return mPartnerDisplayName;
		}
		return sidRequestStringProperty(Property.P_PARTNER_DISPLAY_NAME);
	}
	public Status getStatus() {
		synchronized(this) {
			if ((mSidCached & 0x8) != 0)
				return mStatus;
		}
		return (Status) sidRequestEnumProperty(Property.P_STATUS);
	}
	public FailureReason getFailureReason() {
		synchronized(this) {
			if ((mSidCached & 0x10) != 0)
				return mFailureReason;
		}
		return (FailureReason) sidRequestEnumProperty(Property.P_FAILURE_REASON);
	}
	/** DEPRECATED: subject line */
	public String getSubject() {
		synchronized(this) {
			if ((mSidCached & 0x20) != 0)
				return mSubject;
		}
		return sidRequestStringProperty(Property.P_SUBJECT);
	}
	/** timestamp of creation */
	public long getTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x40) != 0)
				return mTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_TIMESTAMP);
	}
	/** duration in seconds */
	public int getDuration() {
		synchronized(this) {
			if ((mSidCached & 0x80) != 0)
				return mDuration;
		}
		return sidRequestUintProperty(Property.P_DURATION);
	}
	/** max allowed duration in seconds */
	public int getAllowedDuration() {
		synchronized(this) {
			if ((mSidCached & 0x100) != 0)
				return mAllowedDuration;
		}
		return sidRequestUintProperty(Property.P_ALLOWED_DURATION);
	}
	/** VM playback progress in seconds */
	public int getPlaybackProgress() {
		synchronized(this) {
			if ((mSidCached & 0x200) != 0)
				return mPlaybackProgress;
		}
		return sidRequestUintProperty(Property.P_PLAYBACK_PROGRESS);
	}
	/** CONVERSATION_ID of corresponding conversation */
	public Conversation getConversation() {
		synchronized(this) {
			if ((mSidCached & 0x400) != 0)
				return mConversation;
		}
		return (Conversation) sidRequestObjectProperty(Property.P_CONVERSATION);
	}
	/** GUID of the message that the VM is tied to */
	public byte[] getChatMsgGuid() {
		synchronized(this) {
			if ((mSidCached & 0x800) != 0)
				return mChatMsgGuid;
		}
		return sidRequestBinaryProperty(Property.P_CHAT_MSG_GUID);
	}
	public String sidGetStringProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 101:
			return mPartnerHandle;
		case 102:
			return mPartnerDisplayName;
		case 105:
			return mSubject;
		}
		return "";
	}
	public SidObject sidGetObjectProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 830);
		return mConversation;
	}
	public int sidGetIntProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 107:
			return mDuration;
		case 108:
			return mAllowedDuration;
		case 109:
			return mPlaybackProgress;
		}
		return 0;
	}
	public EnumConverting sidGetEnumProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 100:
			return mType;
		case 103:
			return mStatus;
		case 104:
			return mFailureReason;
		}
		return null;
	}
	public byte[] sidGetBinaryProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 831);
		return mChatMsgGuid;
	}
	public String getPropertyAsString(final int prop) {
		switch (prop) {
		case 100: return getType().toString();
		case 101: return getPartnerHandle();
		case 102: return getPartnerDisplayName();
		case 103: return getStatus().toString();
		case 104: return getFailureReason().toString();
		case 105: return getSubject();
		case 107: return Integer.toString(getDuration());
		case 108: return Integer.toString(getAllowedDuration());
		case 109: return Integer.toString(getPlaybackProgress());
		case 830: return getConversation() != null ? Integer.toString(getConversation().getOid()) : "(null)";
		case 831: return "<binary>";
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
				case 100: mType = Type.get(value); break;
				case 101:
					if (svalue != null) mPartnerHandle = svalue;
					else mSidCached &=~bit;
					break;
				case 102:
					if (svalue != null) mPartnerDisplayName = svalue;
					else mSidCached &=~bit;
					break;
				case 103: mStatus = Status.get(value); break;
				case 104: mFailureReason = FailureReason.get(value); break;
				case 105:
					if (svalue != null) mSubject = svalue;
					else mSidCached &=~bit;
					break;
				case 107: mDuration = value; break;
				case 108: mAllowedDuration = value; break;
				case 109: mPlaybackProgress = value; break;
				case 830:
					if (value != 0)
						mConversation = (Conversation) mSidRoot.sidGetObject(property.getModuleId(), value);
					else {
						mConversation = null;
						mSidCached &=~bit;
					}
					break;
				default: mSidCached&=~bit; break;
				}
			}
		}
		VoicemailListener listener = ((Skype) mSidRoot).getVoicemailListener();
		if (listener != null)
			listener.onPropertyChange(this, property, value, svalue);
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final String newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 101:
			mSidCached |= 0x2;
			mPartnerHandle=  newValue;
			break;
		case 102:
			mSidCached |= 0x4;
			mPartnerDisplayName=  newValue;
			break;
		case 105:
			mSidCached |= 0x20;
			mSubject=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final SidObject newValue) {
		final int propId = prop.getId();
		assert(propId == 830);
		mSidCached |= 0x400;
		mConversation= (Conversation) newValue;
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final int newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 100:
			mSidCached |= 0x1;
			mType= Type.get(newValue);
			break;
		case 103:
			mSidCached |= 0x8;
			mStatus= Status.get(newValue);
			break;
		case 104:
			mSidCached |= 0x10;
			mFailureReason= FailureReason.get(newValue);
			break;
		case 107:
			mSidCached |= 0x80;			mDuration=  newValue;
			break;
		case 108:
			mSidCached |= 0x100;
			mAllowedDuration=  newValue;
			break;
		case 109:
			mSidCached |= 0x200;
			mPlaybackProgress=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final byte[] newValue) {
		final int propId = prop.getId();
		assert(propId == 831);
		mSidCached |= 0x800;
		mChatMsgGuid=  newValue;
	}
	public Type          mType;
	public String        mPartnerHandle;
	public String        mPartnerDisplayName;
	public Status        mStatus;
	public FailureReason mFailureReason;
	public String        mSubject;
	public long          mTimestamp;
	public int           mDuration;
	public int           mAllowedDuration;
	public int           mPlaybackProgress;
	public Conversation  mConversation;
	public byte[]        mChatMsgGuid;
	public int moduleId() {
		return 7;
	}
	
	public Voicemail(final int oid, final SidRoot root) {
		super(oid, root, 12);
	}
}
