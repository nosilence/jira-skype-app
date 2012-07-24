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

/** Conversation participant class. Instances of this class represent contacts when in the context of conversations. Amongst others, this class has a Ring method for requesting live status with the target contact. This class also holds typing indicator property and access rights for the contact in context of public conversations.  */
public final class Participant extends SidObject {
	/** Recognized values for the P_RANK property. The P_RANK controls participant's privileges in the conversation. See Participant.CanSetRankTo and Participant.SetRankTo methods.  */
	public enum Rank implements EnumConverting {
		/** Creator of the chat. There can be only one participant with this type per conversation. Other participants cannot be promoted to Creator rank.  */
		CREATOR  (1),
		/** Participant who has administrator privileges  */
		ADMIN    (2),
		/** Participant who can speak and write  */
		SPEAKER  (3),
		/** Participant who can write but not speak  */
		WRITER   (4),
		/** Participant who can read but not write/speak  */
		SPECTATOR(5),
		/** Participant who is applying to join the conversation. Member cannot be demoted to applicants once they have been accepted.   */
		APPLICANT(6),
		/** Participant who has eft or has been kicked from the conversation  */
		RETIRED  (7),
		/** Participant who has been banned from the chat  */
		OUTLAW   (8);
		private final int key;
		Rank(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return CREATOR; }
		public EnumConverting convert(int from) { return Rank.get(from); }
		public EnumConverting[] getArray(final int size) { return new Rank[size]; }
		public static Rank get(int from) {
			switch (from) {
			case 1: return CREATOR;
			case 2: return ADMIN;
			case 3: return SPEAKER;
			case 4: return WRITER;
			case 5: return SPECTATOR;
			case 6: return APPLICANT;
			case 7: return RETIRED;
			case 8: return OUTLAW;
			}
			return CREATOR;
		}
		public static final int CREATOR_VALUE   = 1;
		public static final int ADMIN_VALUE     = 2;
		public static final int SPEAKER_VALUE   = 3;
		public static final int WRITER_VALUE    = 4;
		public static final int SPECTATOR_VALUE = 5;
		public static final int APPLICANT_VALUE = 6;
		public static final int RETIRED_VALUE   = 7;
		public static final int OUTLAW_VALUE    = 8;
	}
	/**
	 * Recognized values for the P_TEXT_STATUS property. The P_TEXT_STATUS property has two uses. Firstly, you can use it to implement typing indicators in your UI, to notify the local user that an incoming chat message from this Participant is imminent.  
	 * 
	 * To set the P_TEXT_STATUS value, so that remote client UIs can display the local user's typing indicator in their UI, use Conversation.SetMyTextStatusTo method. 
	 * 
	 * Transmission of P_TEXT_STATUS updates to remote participants of conversations is controlled via SETUPKEY_DISABLE_CHAT_ACTIVITY_INDICATION setup key. 
	 * 
	 * Secondly, the TEXT_NA value enables you to detect participants who are running clients with no chat capability. 
	 */
	public enum TextStatus implements EnumConverting {
		/** Fallback state in case the text status is not (yet) deternmined.  */
		TEXT_UNKNOWN    (0),
		/** Text status is not applicable as the participant is using a Skype client that does not support chat (for example: voice-only USB phones).  */
		TEXT_NA         (1),
		/** Participant is currently not typing.  */
		READING         (2),
		/** Participant is currently typing.  */
		WRITING         (3),
		/**
		 * This state should be set when following two conditions are true: 
		 *  - interval between keypresses are less than 20 ms; 
		 *  - at least one of the keys adjacent to current key are pressed down. 
		 */
		WRITING_AS_ANGRY(4),
		/** The "Cat on keyboard detection" algorthm in Skype is implemented in the UI level, and as such is not present in the SkypeKit API. Should you wish to implement similar algorthm in your own UI, you can get the basic logic from the PawSense FAQ - http://www.bitboost.com/pawsense/pawsense-faq.html  */
		WRITING_AS_CAT  (5);
		private final int key;
		TextStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return TEXT_UNKNOWN; }
		public EnumConverting convert(int from) { return TextStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new TextStatus[size]; }
		public static TextStatus get(int from) {
			switch (from) {
			case 0: return TEXT_UNKNOWN;
			case 1: return TEXT_NA;
			case 2: return READING;
			case 3: return WRITING;
			case 4: return WRITING_AS_ANGRY;
			case 5: return WRITING_AS_CAT;
			}
			return TEXT_UNKNOWN;
		}
		public static final int TEXT_UNKNOWN_VALUE     = 0;
		public static final int TEXT_NA_VALUE          = 1;
		public static final int READING_VALUE          = 2;
		public static final int WRITING_VALUE          = 3;
		public static final int WRITING_AS_ANGRY_VALUE = 4;
		public static final int WRITING_AS_CAT_VALUE   = 5;
	}
	/** Recognized values for the P_VOICE_STATUS property.   */
	public enum VoiceStatus implements EnumConverting {
		/** Participant voice status is not (yet) determined.  */
		VOICE_UNKNOWN   (0),
		/** Participant is using a Skype client with no audio capability.  */
		VOICE_NA        (1),
		/** Participant is using a Skype client that supports audio.  */
		VOICE_AVAILABLE (2),
		/** Participant is in process of joining current live session. This is a transitional state.  */
		VOICE_CONNECTING(3),
		/** Participant has been invited to join the live session but has not yet accepted.  */
		RINGING         (4),
		/** Participant is in process of joining current live session. This is another transitional state.  */
		EARLY_MEDIA     (5),
		/** Participant has joined the current live session but is currently not transmitting audio.  */
		LISTENING       (6),
		/** Participant has joined the current live session and is transmitting audio. The UI can either use this state to display appropriate "speaking" notification, or alternatively use Participant.P_SOUND_LEVEL if you want your speaking indicator to also reflect audio volume coming from the Participant.  */
		SPEAKING        (7),
		/** Participant has joined the current live session but the audio is currently on hold.  */
		VOICE_ON_HOLD   (8),
		/** Participant will be placed in this state for some seconds after live session has finished. This is another transitional state.  */
		VOICE_STOPPED   (9);
		private final int key;
		VoiceStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return VOICE_UNKNOWN; }
		public EnumConverting convert(int from) { return VoiceStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new VoiceStatus[size]; }
		public static VoiceStatus get(int from) {
			switch (from) {
			case 0: return VOICE_UNKNOWN;
			case 1: return VOICE_NA;
			case 2: return VOICE_AVAILABLE;
			case 3: return VOICE_CONNECTING;
			case 4: return RINGING;
			case 5: return EARLY_MEDIA;
			case 6: return LISTENING;
			case 7: return SPEAKING;
			case 8: return VOICE_ON_HOLD;
			case 9: return VOICE_STOPPED;
			}
			return VOICE_UNKNOWN;
		}
		public static final int VOICE_UNKNOWN_VALUE    = 0;
		public static final int VOICE_NA_VALUE         = 1;
		public static final int VOICE_AVAILABLE_VALUE  = 2;
		public static final int VOICE_CONNECTING_VALUE = 3;
		public static final int RINGING_VALUE          = 4;
		public static final int EARLY_MEDIA_VALUE      = 5;
		public static final int LISTENING_VALUE        = 6;
		public static final int SPEAKING_VALUE         = 7;
		public static final int VOICE_ON_HOLD_VALUE    = 8;
		public static final int VOICE_STOPPED_VALUE    = 9;
	}
	/** Recognized values for the P_VIDEO_STATUS property. This property applies to Participant's video send capability, not capability to receive video.  */
	public enum VideoStatus implements EnumConverting {
		/** Video status is not (yet) determined.  */
		VIDEO_UNKNOWN   (0),
		/** Indicates that this Participant does not have video available..  */
		VIDEO_NA        (1),
		/** Indicates that video is available for this participant. When the Participant.P_VIDEO_STATUS obtains this state, it is possible to retrieve the Video object, using Participant.GetVideo method. Further operations, such as starting or stopping the video send/receive will then go through the Video object.  */		VIDEO_AVAILABLE (2),
		/** Transitional state indicating that the Participant is attempting to initiate video send.  */
		VIDEO_CONNECTING(3),
		/** Indicates that the participant is currently sending video.  */
		STREAMING       (4),
		/** Indicates that the participant video send is currently paused.  */
		VIDEO_ON_HOLD   (5);
		private final int key;
		VideoStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return VIDEO_UNKNOWN; }
		public EnumConverting convert(int from) { return VideoStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new VideoStatus[size]; }
		public static VideoStatus get(int from) {
			switch (from) {
			case 0: return VIDEO_UNKNOWN;
			case 1: return VIDEO_NA;
			case 2: return VIDEO_AVAILABLE;
			case 3: return VIDEO_CONNECTING;
			case 4: return STREAMING;
			case 5: return VIDEO_ON_HOLD;
			}
			return VIDEO_UNKNOWN;
		}
		public static final int VIDEO_UNKNOWN_VALUE    = 0;
		public static final int VIDEO_NA_VALUE         = 1;
		public static final int VIDEO_AVAILABLE_VALUE  = 2;
		public static final int VIDEO_CONNECTING_VALUE = 3;
		public static final int STREAMING_VALUE        = 4;
		public static final int VIDEO_ON_HOLD_VALUE    = 5;
	}
	public enum Dtmf implements EnumConverting {
		DTMF_0    (0),
		DTMF_1    (1),
		DTMF_2    (2),
		DTMF_3    (3),
		DTMF_4    (4),
		DTMF_5    (5),
		DTMF_6    (6),
		DTMF_7    (7),
		DTMF_8    (8),
		DTMF_9    (9),
		DTMF_STAR (10),
		DTMF_POUND(11);
		private final int key;
		Dtmf(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return DTMF_0; }
		public EnumConverting convert(int from) { return Dtmf.get(from); }
		public EnumConverting[] getArray(final int size) { return new Dtmf[size]; }
		public static Dtmf get(int from) {
			switch (from) {
			case  0: return DTMF_0;
			case  1: return DTMF_1;
			case  2: return DTMF_2;
			case  3: return DTMF_3;
			case  4: return DTMF_4;
			case  5: return DTMF_5;
			case  6: return DTMF_6;
			case  7: return DTMF_7;
			case  8: return DTMF_8;
			case  9: return DTMF_9;
			case 10: return DTMF_STAR;
			case 11: return DTMF_POUND;
			}
			return DTMF_0;
		}
		public static final int DTMF_0_VALUE     =  0;
		public static final int DTMF_1_VALUE     =  1;
		public static final int DTMF_2_VALUE     =  2;
		public static final int DTMF_3_VALUE     =  3;
		public static final int DTMF_4_VALUE     =  4;
		public static final int DTMF_5_VALUE     =  5;
		public static final int DTMF_6_VALUE     =  6;
		public static final int DTMF_7_VALUE     =  7;
		public static final int DTMF_8_VALUE     =  8;
		public static final int DTMF_9_VALUE     =  9;
		public static final int DTMF_STAR_VALUE  = 10;
		public static final int DTMF_POUND_VALUE = 11;
	}
	private final static byte[] P_CONVERSATION_req = {(byte) 90,(byte) 71,(byte) 162,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_IDENTITY_req = {(byte) 90,(byte) 71,(byte) 163,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_RANK_req = {(byte) 90,(byte) 71,(byte) 164,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_REQUESTED_RANK_req = {(byte) 90,(byte) 71,(byte) 165,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_TEXT_STATUS_req = {(byte) 90,(byte) 71,(byte) 166,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_VOICE_STATUS_req = {(byte) 90,(byte) 71,(byte) 167,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_VIDEO_STATUS_req = {(byte) 90,(byte) 71,(byte) 168,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_LIVE_IDENTITY_req = {(byte) 90,(byte) 71,(byte) 175,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_LIVE_PRICE_FOR_ME_req = {(byte) 90,(byte) 71,(byte) 170,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_LIVE_FWD_IDENTITIES_req = {(byte) 90,(byte) 71,(byte) 180,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_LIVE_START_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 171,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_SOUND_LEVEL_req = {(byte) 90,(byte) 71,(byte) 173,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_DEBUG_INFO_req = {(byte) 90,(byte) 71,(byte) 174,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_LAST_VOICE_ERROR_req = {(byte) 90,(byte) 71,(byte) 179,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_QUALITY_PROBLEMS_req = {(byte) 90,(byte) 71,(byte) 181,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_LIVE_TYPE_req = {(byte) 90,(byte) 71,(byte) 182,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_LIVE_COUNTRY_req = {(byte) 90,(byte) 71,(byte) 183,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_TRANSFERRED_BY_req = {(byte) 90,(byte) 71,(byte) 184,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_TRANSFERRED_TO_req = {(byte) 90,(byte) 71,(byte) 185,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_ADDER_req = {(byte) 90,(byte) 71,(byte) 186,(byte) 7,(byte) 93,(byte) 19};
	private final static byte[] P_LAST_LEAVEREASON_req = {(byte) 90,(byte) 71,(byte) 187,(byte) 7,(byte) 93,(byte) 19};
	/** Properties of the Participant class */
	public enum Property implements PropertyEnumConverting {
		P_UNKNOWN             (0,0,null,0,null),
		P_CONVERSATION        (930, 1, P_CONVERSATION_req, 18, null),
		P_IDENTITY            (931, 2, P_IDENTITY_req, 0, null),
		P_RANK                (932, 3, P_RANK_req, 0, Rank.get(0)),
		P_REQUESTED_RANK      (933, 4, P_REQUESTED_RANK_req, 0, Rank.get(0)),
		P_TEXT_STATUS         (934, 5, P_TEXT_STATUS_req, 0, TextStatus.get(0)),
		P_VOICE_STATUS        (935, 6, P_VOICE_STATUS_req, 0, VoiceStatus.get(0)),
		P_VIDEO_STATUS        (936, 7, P_VIDEO_STATUS_req, 0, VideoStatus.get(0)),
		P_LIVE_IDENTITY       (943, 8, P_LIVE_IDENTITY_req, 0, null),
		P_LIVE_PRICE_FOR_ME   (938, 9, P_LIVE_PRICE_FOR_ME_req, 0, null),
		P_LIVE_FWD_IDENTITIES (948, 10, P_LIVE_FWD_IDENTITIES_req, 0, null),
		P_LIVE_START_TIMESTAMP(939, 11, P_LIVE_START_TIMESTAMP_req, 0, null),
		P_SOUND_LEVEL         (941, 12, P_SOUND_LEVEL_req, 0, null),
		P_DEBUG_INFO          (942, 13, P_DEBUG_INFO_req, 0, null),
		P_LAST_VOICE_ERROR    (947, 14, P_LAST_VOICE_ERROR_req, 0, null),
		P_QUALITY_PROBLEMS    (949, 15, P_QUALITY_PROBLEMS_req, 0, null),
		P_LIVE_TYPE           (950, 16, P_LIVE_TYPE_req, 0, Skype.IdentityType.get(0)),
		P_LIVE_COUNTRY        (951, 17, P_LIVE_COUNTRY_req, 0, null),
		P_TRANSFERRED_BY      (952, 18, P_TRANSFERRED_BY_req, 0, null),
		P_TRANSFERRED_TO      (953, 19, P_TRANSFERRED_TO_req, 0, null),
		P_ADDER               (954, 20, P_ADDER_req, 0, null),
		P_LAST_LEAVEREASON    (955, 21, P_LAST_LEAVEREASON_req, 0, Skype.LeaveReason.get(0));
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
			case 930: return P_CONVERSATION;
			case 931: return P_IDENTITY;
			case 932: return P_RANK;
			case 933: return P_REQUESTED_RANK;
			case 934: return P_TEXT_STATUS;
			case 935: return P_VOICE_STATUS;
			case 936: return P_VIDEO_STATUS;
			case 943: return P_LIVE_IDENTITY;
			case 938: return P_LIVE_PRICE_FOR_ME;
			case 948: return P_LIVE_FWD_IDENTITIES;
			case 939: return P_LIVE_START_TIMESTAMP;			case 941: return P_SOUND_LEVEL;
			case 942: return P_DEBUG_INFO;
			case 947: return P_LAST_VOICE_ERROR;
			case 949: return P_QUALITY_PROBLEMS;
			case 950: return P_LIVE_TYPE;
			case 951: return P_LIVE_COUNTRY;
			case 952: return P_TRANSFERRED_BY;
			case 953: return P_TRANSFERRED_TO;
			case 954: return P_ADDER;
			case 955: return P_LAST_LEAVEREASON;
			}
			return P_UNKNOWN;
		}
		public static final int P_CONVERSATION_VALUE         = 930;
		public static final int P_IDENTITY_VALUE             = 931;
		public static final int P_RANK_VALUE                 = 932;
		public static final int P_REQUESTED_RANK_VALUE       = 933;
		public static final int P_TEXT_STATUS_VALUE          = 934;
		public static final int P_VOICE_STATUS_VALUE         = 935;
		public static final int P_VIDEO_STATUS_VALUE         = 936;
		public static final int P_LIVE_IDENTITY_VALUE        = 943;
		public static final int P_LIVE_PRICE_FOR_ME_VALUE    = 938;
		public static final int P_LIVE_FWD_IDENTITIES_VALUE  = 948;
		public static final int P_LIVE_START_TIMESTAMP_VALUE = 939;
		public static final int P_SOUND_LEVEL_VALUE          = 941;
		public static final int P_DEBUG_INFO_VALUE           = 942;
		public static final int P_LAST_VOICE_ERROR_VALUE     = 947;
		public static final int P_QUALITY_PROBLEMS_VALUE     = 949;
		public static final int P_LIVE_TYPE_VALUE            = 950;
		public static final int P_LIVE_COUNTRY_VALUE         = 951;
		public static final int P_TRANSFERRED_BY_VALUE       = 952;
		public static final int P_TRANSFERRED_TO_VALUE       = 953;
		public static final int P_ADDER_VALUE                = 954;
		public static final int P_LAST_LEAVEREASON_VALUE     = 955;
		public static final Property[] mget_info_mreq = { P_VOICE_STATUS, P_TEXT_STATUS, P_VIDEO_STATUS, P_RANK, P_LIVE_TYPE, P_LIVE_PRICE_FOR_ME, P_IDENTITY };
	}
	private final static byte[] canSetRankTo_req = {(byte) 90,(byte) 82,(byte) 19,(byte) 1};
	/** Checks whether the current user can set this Participant's conversation privileges to the specified RANK. This enables you to gray out or disable in your UI all the unavailable options for Participant.SetRankTo method. 
	 * @param rank Participant.RANK value to check for. 
	 * @return result Returns true if local user can set participant's rank to the value given in rank argument. 
	 */
	public boolean canSetRankTo(Rank rank) {
		try {
			return sidDoRequest(canSetRankTo_req)
			.addEnumParm(1, rank)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] setRankTo_req = {(byte) 90,(byte) 82,(byte) 19,(byte) 2};
	/** Sets Participant's conversation privileges to the given RANK 
	 * @param rank Target Participant.RANK value. 
	 */
	public void setRankTo(Rank rank) {
		try {
			sidDoRequest(setRankTo_req)
			.addEnumParm(1, rank)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] ring_req = {(byte) 90,(byte) 82,(byte) 19,(byte) 3};
	/** Initiates live conversation attempt with Participant. 
	 * @param identityToUse Ring an alternate identity, such as a PSTN number. 
	 * @param videoCall Enable video. 
	 * @param nrofRedials Unused. 
	 * @param redialPeriod Unused. 
	 * @param autoStartVm Unused. On dialog, if falling on VM, greeting and recording will be automatically started. 
	 * @param origin When call is initiated from web link, must contain the URI that was used
	 */
	public void ring(String identityToUse, boolean videoCall, int nrofRedials, int redialPeriod, boolean autoStartVm, String origin) {
		try {
			sidDoRequest(ring_req)
			.addStringParm(1, identityToUse)
			.addBoolParm(2, videoCall)
			.addUintParm(3, nrofRedials)
			.addUintParm(4, redialPeriod)
			.addBoolParm(5, autoStartVm)
			.addStringParm(6, origin)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] ringIt_req = {(byte) 90,(byte) 82,(byte) 19,(byte) 8};
	/** Rings this participant, using P_LIVE_IDENTITY property if set. 
	 */
	public void ringIt() {
		try {
			sidDoRequest(ringIt_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setLiveIdentityToUse_req = {(byte) 90,(byte) 82,(byte) 19,(byte) 7};
	/** Sets LIVE_IDENTITY property, an alternate identity to use when ringing, such as a PSTN. 
	 * @param identityToUse Empty string will reset it to default, i.e IDENTITY property value 
	 */
	public void setLiveIdentityToUse(String identityToUse) {
		try {
			sidDoRequest(setLiveIdentityToUse_req)
			.addStringParm(1, identityToUse)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] getVideo_req = {(byte) 90,(byte) 82,(byte) 19,(byte) 4};
	/**
	 * Retrieves a reference to the Video object that corresponds to the Participant. It can be either local video - you can check if this participant's name (P_IDENTITY property) matches the name of the currently logged in account (P_SKYPENAME property) or incoming video from a remote participant.  
	 * 
	 * Note that for GetVideo to be successful, the video has to be available for that participant. This can be checked for by examining Participant VIDEO_STATUS property - once it becomes VIDEO_AVAILABLE - you can use GetVideo to obtain the Video object. 
	
	 * @return video Returns reference to a constructed video object. 
	 */
	public Video getVideo() {
		try {
			return (Video) sidDoRequest(getVideo_req)
			.endRequest().getObjectParm(1, 11, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] hangup_req = {(byte) 90,(byte) 82,(byte) 19,(byte) 5};
	/** Removes this participant from the current live session. Note that this does not remove the participant from conversation (for this, use Participant.Retire). It only removes participant from live state. 
	 */
	public void hangup() {
		try {
			sidDoRequest(hangup_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] retire_req = {(byte) 90,(byte) 82,(byte) 19,(byte) 6};
	/** Forcibly removes this participant from conversation. This method is for removing other people from conversations (for example: as administrative punishment for flooding conversation with spam messages). For local user to leave a conversation, use Conversation.RetireFrom instead. 
	 */
	public void retire() {
		try {
			sidDoRequest(retire_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] getLiveSessionVideos_req = {(byte) 90,(byte) 82,(byte) 19,(byte) 145,(byte) 1};
	/** For myself participant returns a list of attached send videos. For non-myself - list of recv videos.
	 * @return videos
	 */
	public Video[] getLiveSessionVideos() {
		try {
			return (Video[]) sidDoRequest(getLiveSessionVideos_req)
			.endRequest().getObjectListParm(1, 11, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	/***
	 * generic multiget of a list of Property
	 * @param requested the list of requested properties of Participant
	 * @return SidGetResponding
	 */
	public SidGetResponding sidMultiGet(Property[] requested) {
		return super.sidMultiGet(requested);
	}
	/***
	 * generic multiget of list of Property for a list of Participant
	 * @param requested the list of requested properties
	 * @return SidGetResponding[] can be casted to (Participant[]) if all properties are cached
	 */
	static public SidGetResponding[] sidMultiGet(Property[] requested, Participant[] objects) {
		return SidObject.sidMultiGet(requested, objects);
	}
	/*** multiget the following properties
	 * - P_VOICE_STATUS
	 * - P_TEXT_STATUS
	 * - P_VIDEO_STATUS
	 * - P_RANK
	 * - P_LIVE_TYPE
	 * - P_LIVE_PRICE_FOR_ME
	 * - P_IDENTITY
	 */
	public Participant mgetInfo() {
		return (Participant) super.sidMultiGet(Property.mget_info_mreq, this);
	}
	/*** multiget the following properties for a list of Participant
	 * - P_VOICE_STATUS
	 * - P_TEXT_STATUS
	 * - P_VIDEO_STATUS
	 * - P_RANK
	 * - P_LIVE_TYPE	 * - P_LIVE_PRICE_FOR_ME
	 * - P_IDENTITY
	 * @param objects targets of the request
	 * @return Participant[] responses
	 */
	static public Participant[] mgetInfo(Participant[] objects) {
		return (Participant[]) SidObject.sidMultiGet(Property.mget_info_mreq, objects, objects);
	}
	/** [ALL] ID of corresponding conversation. Here and below, [ALL] tag indicates that the property has meaning and is set in context of all participants in the conversation. [OTHERS] tag has meaning only for participants who are not the local user.  */
	public Conversation getConversation() {
		synchronized(this) {
			if ((mSidCached & 0x1) != 0)
				return mConversation;
		}
		return (Conversation) sidRequestObjectProperty(Property.P_CONVERSATION);
	}
	/** [ALL] skypename OR pstn_number OR namespace:identity */
	public String getIdentity() {
		synchronized(this) {
			if ((mSidCached & 0x2) != 0)
				return mIdentity;
		}
		return sidRequestStringProperty(Property.P_IDENTITY);
	}
	/** [ALL] Participant.RANK */
	public Rank getRank() {
		synchronized(this) {
			if ((mSidCached & 0x4) != 0)
				return mRank;
		}
		return (Rank) sidRequestEnumProperty(Property.P_RANK);
	}
	/** Not set (should be: requested Participant.RANK, higher than the current one) */
	public Rank getRequestedRank() {
		synchronized(this) {
			if ((mSidCached & 0x8) != 0)
				return mRequestedRank;
		}
		return (Rank) sidRequestEnumProperty(Property.P_REQUESTED_RANK);
	}
	/** [ALL] the typing indicator */
	public TextStatus getTextStatus() {
		synchronized(this) {
			if ((mSidCached & 0x10) != 0)
				return mTextStatus;
		}
		return (TextStatus) sidRequestEnumProperty(Property.P_TEXT_STATUS);
	}
	/** [ALL] voice status */
	public VoiceStatus getVoiceStatus() {
		synchronized(this) {
			if ((mSidCached & 0x20) != 0)
				return mVoiceStatus;
		}
		return (VoiceStatus) sidRequestEnumProperty(Property.P_VOICE_STATUS);
	}
	/** [ALL] video status */
	public VideoStatus getVideoStatus() {
		synchronized(this) {
			if ((mSidCached & 0x40) != 0)
				return mVideoStatus;
		}
		return (VideoStatus) sidRequestEnumProperty(Property.P_VIDEO_STATUS);
	}
	/** [ALL] identity that was used to establish current live session with that participant (can be different from participant identity) */
	public String getLiveIdentity() {
		synchronized(this) {
			if ((mSidCached & 0x80) != 0)
				return mLiveIdentity;
		}
		return sidRequestStringProperty(Property.P_LIVE_IDENTITY);
	}
	/** [OTHERS] 'price_per_minute_float currency' - eg '0.01 EUR'. Note that this property obtains value only after the participant goes into live state. It cannot be used to display call rates before the call starts.  */
	public String getLivePriceForMe() {
		synchronized(this) {
			if ((mSidCached & 0x100) != 0)
				return mLivePriceForMe;
		}
		return sidRequestStringProperty(Property.P_LIVE_PRICE_FOR_ME);
	}
	/** [OTHERS] list of identities where the live session is being forwarded (if they are disclosed), space separated */
	public String getLiveFwdIdentities() {
		synchronized(this) {
			if ((mSidCached & 0x200) != 0)
				return mLiveFwdIdentities;
		}
		return sidRequestStringProperty(Property.P_LIVE_FWD_IDENTITIES);
	}
	/** [ALL] time of joining the live session */
	public long getLiveStartTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x400) != 0)
				return mLiveStartTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_LIVE_START_TIMESTAMP);
	}
	/** [ALL] current 'loudness' level when SPEAKING (0..10) */
	public int getSoundLevel() {
		synchronized(this) {
			if ((mSidCached & 0x800) != 0)
				return mSoundLevel;
		}
		return sidRequestUintProperty(Property.P_SOUND_LEVEL);
	}
	/** [OTHERS] call (audio and video) debug info */
	public String getDebugInfo() {
		synchronized(this) {
			if ((mSidCached & 0x1000) != 0)
				return mDebugInfo;
		}
		return sidRequestStringProperty(Property.P_DEBUG_INFO);
	}
	/** [OTHERS] DEPRECATED, use last_leavereason instead */
	public String getLastVoiceError() {
		synchronized(this) {
			if ((mSidCached & 0x2000) != 0)
				return mLastVoiceError;
		}
		return sidRequestStringProperty(Property.P_LAST_VOICE_ERROR);
	}
	/** [ALL] space separated tokens values: CPU_INUSE CPU_SLOW CPU_HIGH HIGH_ECHO HIGH_NOISE MUTED_INPUT LOW_INPUT MUTED_INPUT_ACTIVITY FW_STRONG FW_BAD NOT_UDP CALL_BW_LOW RECORD_ERROR + values in video debug info */
	public String getQualityProblems() {
		synchronized(this) {
			if ((mSidCached & 0x4000) != 0)
				return mQualityProblems;
		}
		return sidRequestStringProperty(Property.P_QUALITY_PROBLEMS);
	}
	/** [ALL] participant type during livesession as specified in IDENTITYTYPE */
	public Skype.IdentityType getLiveType() {
		synchronized(this) {
			if ((mSidCached & 0x8000) != 0)
				return mLiveType;
		}
		return (Skype.IdentityType) sidRequestEnumProperty(Property.P_LIVE_TYPE);
	}
	/** [OTHERS] participant livesession country code - used for emergency calls only atm */
	public String getLiveCountry() {
		synchronized(this) {
			if ((mSidCached & 0x10000) != 0)
				return mLiveCountry;
		}
		return sidRequestStringProperty(Property.P_LIVE_COUNTRY);
	}
	/** [OTHERS] Transferor identity (transferee side)  */
	public String getTransferredBy() {
		synchronized(this) {
			if ((mSidCached & 0x20000) != 0)
				return mTransferredBy;
		}
		return sidRequestStringProperty(Property.P_TRANSFERRED_BY);
	}
	/** [OTHERS] Identity of recipient of transfer (transferor side, caller side)  */
	public String getTransferredTo() {
		synchronized(this) {
			if ((mSidCached & 0x40000) != 0)
				return mTransferredTo;
		}
		return sidRequestStringProperty(Property.P_TRANSFERRED_TO);
	}
	/** [ALL] Identity of the user who added this participant to the conversation, type: Sid.String   */
	public String getAdder() {
		synchronized(this) {
			if ((mSidCached & 0x80000) != 0)
				return mAdder;
		}
		return sidRequestStringProperty(Property.P_ADDER);
	}
	/** [OTHERS] Last reason for leaving conversation or live session. Provides an enum alternative to last_voice_error as well as the reason this participant RETIRED from the conversation.   */
	public Skype.LeaveReason getLastLeavereason() {
		synchronized(this) {
			if ((mSidCached & 0x100000) != 0)
				return mLastLeavereason;
		}
		return (Skype.LeaveReason) sidRequestEnumProperty(Property.P_LAST_LEAVEREASON);
	}
	public String sidGetStringProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 931:
			return mIdentity;
		case 943:
			return mLiveIdentity;
		case 938:
			return mLivePriceForMe;
		case 948:
			return mLiveFwdIdentities;
		case 942:
			return mDebugInfo;
		case 947:
			return mLastVoiceError;
		case 949:
			return mQualityProblems;
		case 951:
			return mLiveCountry;
		case 952:
			return mTransferredBy;
		case 953:
			return mTransferredTo;
		case 954:
			return mAdder;
		}
		return "";
	}
	public SidObject sidGetObjectProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 930);
		return mConversation;
	}
	public int sidGetIntProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 941);
		return mSoundLevel;
	}
	public EnumConverting sidGetEnumProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 932:
			return mRank;
		case 933:
			return mRequestedRank;
		case 934:
			return mTextStatus;
		case 935:
			return mVoiceStatus;
		case 936:
			return mVideoStatus;
		case 950:
			return mLiveType;
		case 955:
			return mLastLeavereason;
		}
		return null;
	}
	public String getPropertyAsString(final int prop) {
		switch (prop) {
		case 930: return getConversation() != null ? Integer.toString(getConversation().getOid()) : "(null)";
		case 931: return getIdentity();
		case 932: return getRank().toString();
		case 933: return getRequestedRank().toString();
		case 934: return getTextStatus().toString();
		case 935: return getVoiceStatus().toString();
		case 936: return getVideoStatus().toString();
		case 943: return getLiveIdentity();
		case 938: return getLivePriceForMe();
		case 948: return getLiveFwdIdentities();
		case 941: return Integer.toString(getSoundLevel());
		case 942: return getDebugInfo();
		case 947: return getLastVoiceError();
		case 949: return getQualityProblems();		case 950: return getLiveType().toString();
		case 951: return getLiveCountry();
		case 952: return getTransferredBy();
		case 953: return getTransferredTo();
		case 954: return getAdder();
		case 955: return getLastLeavereason().toString();
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
				case 930:
					if (value != 0)
						mConversation = (Conversation) mSidRoot.sidGetObject(property.getModuleId(), value);
					else {
						mConversation = null;
						mSidCached &=~bit;
					}
					break;
				case 931:
					if (svalue != null) mIdentity = svalue;
					else mSidCached &=~bit;
					break;
				case 932: mRank = Rank.get(value); break;
				case 933: mRequestedRank = Rank.get(value); break;
				case 934: mTextStatus = TextStatus.get(value); break;
				case 935: mVoiceStatus = VoiceStatus.get(value); break;
				case 936: mVideoStatus = VideoStatus.get(value); break;
				case 943:
					if (svalue != null) mLiveIdentity = svalue;
					else mSidCached &=~bit;
					break;
				case 938:
					if (svalue != null) mLivePriceForMe = svalue;
					else mSidCached &=~bit;
					break;
				case 948:
					if (svalue != null) mLiveFwdIdentities = svalue;
					else mSidCached &=~bit;
					break;
				case 941: mSoundLevel = value; break;
				case 942:
					if (svalue != null) mDebugInfo = svalue;
					else mSidCached &=~bit;
					break;
				case 947:
					if (svalue != null) mLastVoiceError = svalue;
					else mSidCached &=~bit;
					break;
				case 949:
					if (svalue != null) mQualityProblems = svalue;
					else mSidCached &=~bit;
					break;
				case 950: mLiveType = Skype.IdentityType.get(value); break;
				case 951:
					if (svalue != null) mLiveCountry = svalue;
					else mSidCached &=~bit;
					break;
				case 952:
					if (svalue != null) mTransferredBy = svalue;
					else mSidCached &=~bit;
					break;
				case 953:
					if (svalue != null) mTransferredTo = svalue;
					else mSidCached &=~bit;
					break;
				case 954:
					if (svalue != null) mAdder = svalue;
					else mSidCached &=~bit;
					break;
				case 955: mLastLeavereason = Skype.LeaveReason.get(value); break;
				default: mSidCached&=~bit; break;
				}
			}
		}
		ParticipantListener listener = ((Skype) mSidRoot).getParticipantListener();
		if (listener != null)
			listener.onPropertyChange(this, property, value, svalue);
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final String newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 931:
			mSidCached |= 0x2;
			mIdentity=  newValue;
			break;
		case 943:
			mSidCached |= 0x80;
			mLiveIdentity=  newValue;
			break;
		case 938:
			mSidCached |= 0x100;
			mLivePriceForMe=  newValue;
			break;
		case 948:
			mSidCached |= 0x200;
			mLiveFwdIdentities=  newValue;
			break;
		case 942:
			mSidCached |= 0x1000;
			mDebugInfo=  newValue;
			break;
		case 947:
			mSidCached |= 0x2000;
			mLastVoiceError=  newValue;
			break;
		case 949:
			mSidCached |= 0x4000;
			mQualityProblems=  newValue;
			break;
		case 951:
			mSidCached |= 0x10000;
			mLiveCountry=  newValue;
			break;
		case 952:
			mSidCached |= 0x20000;
			mTransferredBy=  newValue;
			break;
		case 953:
			mSidCached |= 0x40000;
			mTransferredTo=  newValue;
			break;
		case 954:
			mSidCached |= 0x80000;
			mAdder=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final SidObject newValue) {
		final int propId = prop.getId();
		assert(propId == 930);
		mSidCached |= 0x1;
		mConversation= (Conversation) newValue;
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final int newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 932:
			mSidCached |= 0x4;
			mRank= Rank.get(newValue);
			break;
		case 933:
			mSidCached |= 0x8;
			mRequestedRank= Rank.get(newValue);
			break;
		case 934:
			mSidCached |= 0x10;
			mTextStatus= TextStatus.get(newValue);
			break;
		case 935:
			mSidCached |= 0x20;
			mVoiceStatus= VoiceStatus.get(newValue);
			break;
		case 936:
			mSidCached |= 0x40;
			mVideoStatus= VideoStatus.get(newValue);
			break;
		case 941:
			mSidCached |= 0x800;
			mSoundLevel=  newValue;
			break;
		case 950:
			mSidCached |= 0x8000;
			mLiveType= Skype.IdentityType.get(newValue);
			break;
		case 955:
			mSidCached |= 0x100000;
			mLastLeavereason= Skype.LeaveReason.get(newValue);
			break;
		}
	}
	public Conversation       mConversation;
	public String             mIdentity;
	public Rank               mRank;
	public Rank               mRequestedRank;
	public TextStatus         mTextStatus;
	public VoiceStatus        mVoiceStatus;
	public VideoStatus        mVideoStatus;
	public String             mLiveIdentity;
	public String             mLivePriceForMe;
	public String             mLiveFwdIdentities;
	public long               mLiveStartTimestamp;
	public int                mSoundLevel;
	public String             mDebugInfo;
	public String             mLastVoiceError;
	public String             mQualityProblems;
	public Skype.IdentityType mLiveType;
	public String             mLiveCountry;
	public String             mTransferredBy;
	public String             mTransferredTo;
	public String             mAdder;
	public Skype.LeaveReason  mLastLeavereason;
	public int moduleId() {
		return 19;
	}
	
	public Participant(final int oid, final SidRoot root) {
		super(oid, root, 21);
	}
}
