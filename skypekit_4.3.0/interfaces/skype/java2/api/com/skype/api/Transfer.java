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
 * Transfer in this context refers to transferring (sending/receiving) files among Skype Contacts, not transferring a call to another Skype or PSTN Contact. This class includes file transfer-specific properties and methods, such as FILESIZE, BYTESPERSECOND, Pause and Resume. Recipients of these file transfers must explicitly accept (or decline) any incoming transfer. Transfer instances represent files being sent and received within a Conversation context. Each Transfer instance represents a single file transfer - if a conversation has multiple remote participants, a separate Transfer instance must be instantiated for each remote participant (a Transfer instance is not instantiated for the sender). 
 * 
 * Transfer instances cannot be instantiated directly. Instead, you initiate a file transfer by invoking Conversation.PostFiles. This instantiates a Message instance of type POSTED_FILES, which is added to the Conversation for all the participants (including the sender). The Transfer instance is associated with 
 * this Message instance, and the Message.P_BODY_XML looks like this: 
 * 
 * @code 
 * Some text<files alt=""><file size="2336020" index="0">test.zip</file></files>  
 * </CODE> 
 * 
 * To put it another way, the object chain goes like this: 
 * @code 
 * Conversation->Message->Transfer  
 * </CODE> 
 * 
 * The first part of the message (before the files section) comes from the Conversation.PostFiles body argument. For each file in the message, a file section is provided with three fields: 
 *  - file size in bytes 
 *  - index - more or less arbitrary order in which the transfers should be displayed in the UI 
 *  - file name. 
 * 
 * For practical purposes, the Message.P_BODY_XML property is not all that useful in this context. The Transfer instances, however, contain the state and progress feedback for your UI. You retrieve these Transfer instances using Message.GetTransfers method. Since the sender can post multiple files to 
 * a Conversation using the same Message, Message:GetTransfers retrieves a list of Transfer instances - one per file per recipient. 
 * 
 * You can determine the direction of particular Transfer instance by querying Transfer.P_TYPE (INCOMING/OUTGOING).  
 * 
 * You can implement a progress indicator by monitoring Transfer.P_BYTESTRANSFERRED. Note that when testing this on your local network, you will most likely catch these property change events at the beginning and the end of the transfer only - which does not look like too useful. However, for non-local network transfers where the transfer speeds are in the neighborhood of 200-300 KB per second, you should consider implementing progress feedback as being mandatory and expect to catch multiple property change events for all but the smallest files. 
 * 
 * Another property of interest is Transfer.P_STATUS. The normal transfer status sequence during successful outgoing transfer is this: 
 *  - TRANSFER STATUS -> NEW 
 *  - TRANSFER STATUS -> WAITING_FOR_ACCEPT 
 *  - TRANSFER STATUS -> CONNECTING 
 *  - TRANSFER STATUS -> TRANSFERRING 
 *  - TRANSFER STATUS -> CONNECTING 
 *  - TRANSFER STATUS -> COMPLETED 
 * 
 * The list of all terminal Transfer statuses is: 
 *  - COMPLETED 
 *  - FAILED 
 *  - CANCELLED 
 *  - CANCELLED_BY_REMOTE 
 * 
 * In-progress transfers can be canceled with Transfer.Cancel and paused/resumed with Transfer.Pause and Transfer.Resume. For transfers that complete with a status of FAILED, your UI should provide feedback based on the value of Transfer.P_FAILUREREASON. 
 * 
 * Incoming transfers, once accepted, overwrite existing files with the same name. Before accepting an incoming file transfer, 
 * your UI should prompt the recipient to: 
 *  - accept or decline the file 
 *  - if accepted, specify the directory of here to save the file (with a pre-filled default) 
 *  - if accepted and a file having the same name already exists at the specified destination, your UI should prompt for confirmation to overwrite and provide a way to alter the file name before accepting it 
 * 
 * Similarly, your UI should verify the existence of outgoing files prior to invoking Conversation.PostFiles. 
 * 
 * Note that you should provide both Conversation.PostFiles and Transfer.Accept methods fully-qualified paths. Otherwise, the paths will be assumed to be relative to the path of SkypeKit runtime, since the methods are actually executed in the runtime context. 
 */
public final class Transfer extends SidObject {
	public enum Type implements EnumConverting {
		INCOMING(1),
		OUTGOING(2);
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
			case 2: return OUTGOING;
			}
			return INCOMING;
		}
		public static final int INCOMING_VALUE = 1;
		public static final int OUTGOING_VALUE = 2;
	}
	/** Recognized values for the P_STATUS property. Reflects current state of this Transfer.  */
	public enum Status implements EnumConverting {
		/** The file has either not been posted (sent) (OUTGOING), or not accepted (received) (INCOMING).  */
		NEW                      (0),
		/** A temporary state that transitions either into TRANSFERRING (relayed or direct) or to FAILED. For unknown reasons, outgoing transfers tend go into this state twice - immediately before the actual data transfer starts and immediately after it ends.  */
		CONNECTING               (1),
		/** The files have been posted but the recipient has not yet accepted (or has declined) the transfer.  */
		WAITING_FOR_ACCEPT       (2),
		/** The transfer has been accepted and file data is being sent/received. Periodic updates of P_BYTESTRANSFERRED property should occur.  */
		TRANSFERRING             (3),
		/** The transfer has been accepted and file data is being sent/received but is going over at least one relay. Since relayed transfers tend to be significantly slower than direct transfers, you might want to differentiate the two in your UI and notify the user that relayed transfer typically take significantly longer to finish.  */
		TRANSFERRING_OVER_RELAY  (4),
		/** The local user (either the sender or a receiver) has paused the transfer.  */
		PAUSED                   (5),
		/** A remote user has paused the transfer. For senders, a receiver has paused the transfer; for receivers, the sender has paused the transfer.  */
		REMOTELY_PAUSED          (6),
		/** Local side (either sender or receiver) has canceled the transfer. This is a final state of the STATE property.  */
		CANCELLED                (7),
		/** File transfer has completed. This is a terminal state.  */
		COMPLETED                (8),
		/** File transfer has failed. This is a terminal state. UI should provide feedback, based on value of P_FAILUREREASON.  */
		FAILED                   (9),
		/** Transfer whose existence has been hinted by corresponding chat message, but which is yet to arrive.  */
		PLACEHOLDER              (10),
		/** Outgoing transfer object from another instance of the same account as current local login, running on another system. Hinted through chat message - only implies an offer was made; not necessarily accepted, failed, or completed.   */
		OFFER_FROM_OTHER_INSTANCE(11),
		/** Remote side (either sender or receiver) has canceled the transfer. This is a final state of the STATE property.  */
		CANCELLED_BY_REMOTE      (12);
		private final int key;
		Status(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return NEW; }
		public EnumConverting convert(int from) { return Status.get(from); }
		public EnumConverting[] getArray(final int size) { return new Status[size]; }
		public static Status get(int from) {
			switch (from) {
			case  0: return NEW;
			case  1: return CONNECTING;
			case  2: return WAITING_FOR_ACCEPT;
			case  3: return TRANSFERRING;
			case  4: return TRANSFERRING_OVER_RELAY;
			case  5: return PAUSED;			case  6: return REMOTELY_PAUSED;
			case  7: return CANCELLED;
			case  8: return COMPLETED;
			case  9: return FAILED;
			case 10: return PLACEHOLDER;
			case 11: return OFFER_FROM_OTHER_INSTANCE;
			case 12: return CANCELLED_BY_REMOTE;
			}
			return NEW;
		}
		public static final int NEW_VALUE                       =  0;
		public static final int CONNECTING_VALUE                =  1;
		public static final int WAITING_FOR_ACCEPT_VALUE        =  2;
		public static final int TRANSFERRING_VALUE              =  3;
		public static final int TRANSFERRING_OVER_RELAY_VALUE   =  4;
		public static final int PAUSED_VALUE                    =  5;
		public static final int REMOTELY_PAUSED_VALUE           =  6;
		public static final int CANCELLED_VALUE                 =  7;
		public static final int COMPLETED_VALUE                 =  8;
		public static final int FAILED_VALUE                    =  9;
		public static final int PLACEHOLDER_VALUE               = 10;
		public static final int OFFER_FROM_OTHER_INSTANCE_VALUE = 11;
		public static final int CANCELLED_BY_REMOTE_VALUE       = 12;
	}
	public enum FailureReason implements EnumConverting {
		SENDER_NOT_AUTHORISED      (1),
		REMOTELY_CANCELLED         (2),
		FAILED_READ                (3),
		FAILED_REMOTE_READ         (4),
		FAILED_WRITE               (5),
		FAILED_REMOTE_WRITE        (6),
		REMOTE_DOES_NOT_SUPPORT_FT (7),
		REMOTE_OFFLINE_FOR_TOO_LONG(8),
		TOO_MANY_PARALLEL          (9),
		PLACEHOLDER_TIMEOUT        (10);
		private final int key;
		FailureReason(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return SENDER_NOT_AUTHORISED; }
		public EnumConverting convert(int from) { return FailureReason.get(from); }
		public EnumConverting[] getArray(final int size) { return new FailureReason[size]; }
		public static FailureReason get(int from) {
			switch (from) {
			case  1: return SENDER_NOT_AUTHORISED;
			case  2: return REMOTELY_CANCELLED;
			case  3: return FAILED_READ;
			case  4: return FAILED_REMOTE_READ;
			case  5: return FAILED_WRITE;
			case  6: return FAILED_REMOTE_WRITE;
			case  7: return REMOTE_DOES_NOT_SUPPORT_FT;
			case  8: return REMOTE_OFFLINE_FOR_TOO_LONG;
			case  9: return TOO_MANY_PARALLEL;
			case 10: return PLACEHOLDER_TIMEOUT;
			}
			return SENDER_NOT_AUTHORISED;
		}
		public static final int SENDER_NOT_AUTHORISED_VALUE       =  1;
		public static final int REMOTELY_CANCELLED_VALUE          =  2;
		public static final int FAILED_READ_VALUE                 =  3;
		public static final int FAILED_REMOTE_READ_VALUE          =  4;
		public static final int FAILED_WRITE_VALUE                =  5;
		public static final int FAILED_REMOTE_WRITE_VALUE         =  6;
		public static final int REMOTE_DOES_NOT_SUPPORT_FT_VALUE  =  7;
		public static final int REMOTE_OFFLINE_FOR_TOO_LONG_VALUE =  8;
		public static final int TOO_MANY_PARALLEL_VALUE           =  9;
		public static final int PLACEHOLDER_TIMEOUT_VALUE         = 10;
	}
	private final static byte[] P_TYPE_req = {(byte) 90,(byte) 71,(byte) 80,(byte) 93,(byte) 6};
	private final static byte[] P_PARTNER_HANDLE_req = {(byte) 90,(byte) 71,(byte) 81,(byte) 93,(byte) 6};
	private final static byte[] P_PARTNER_DISPLAY_NAME_req = {(byte) 90,(byte) 71,(byte) 82,(byte) 93,(byte) 6};
	private final static byte[] P_STATUS_req = {(byte) 90,(byte) 71,(byte) 83,(byte) 93,(byte) 6};
	private final static byte[] P_FAILURE_REASON_req = {(byte) 90,(byte) 71,(byte) 84,(byte) 93,(byte) 6};
	private final static byte[] P_START_TIME_req = {(byte) 90,(byte) 71,(byte) 85,(byte) 93,(byte) 6};
	private final static byte[] P_FINISH_TIME_req = {(byte) 90,(byte) 71,(byte) 86,(byte) 93,(byte) 6};
	private final static byte[] P_FILE_PATH_req = {(byte) 90,(byte) 71,(byte) 87,(byte) 93,(byte) 6};
	private final static byte[] P_FILE_NAME_req = {(byte) 90,(byte) 71,(byte) 88,(byte) 93,(byte) 6};
	private final static byte[] P_FILE_SIZE_req = {(byte) 90,(byte) 71,(byte) 89,(byte) 93,(byte) 6};
	private final static byte[] P_BYTES_TRANSFERRED_req = {(byte) 90,(byte) 71,(byte) 90,(byte) 93,(byte) 6};
	private final static byte[] P_BYTES_PER_SECOND_req = {(byte) 90,(byte) 71,(byte) 91,(byte) 93,(byte) 6};
	private final static byte[] P_CHAT_MSG_GUID_req = {(byte) 90,(byte) 71,(byte) 92,(byte) 93,(byte) 6};
	private final static byte[] P_CHAT_MSG_INDEX_req = {(byte) 90,(byte) 71,(byte) 93,(byte) 93,(byte) 6};
	private final static byte[] P_CONVERSATION_req = {(byte) 90,(byte) 71,(byte) 98,(byte) 93,(byte) 6};
	/** Properties of the Transfer class */
	public enum Property implements PropertyEnumConverting {
		P_UNKNOWN             (0,0,null,0,null),
		P_TYPE                (80, 1, P_TYPE_req, 0, Type.get(0)),
		P_PARTNER_HANDLE      (81, 2, P_PARTNER_HANDLE_req, 0, null),
		P_PARTNER_DISPLAY_NAME(82, 3, P_PARTNER_DISPLAY_NAME_req, 0, null),
		P_STATUS              (83, 4, P_STATUS_req, 0, Status.get(0)),
		P_FAILURE_REASON      (84, 5, P_FAILURE_REASON_req, 0, FailureReason.get(0)),
		P_START_TIME          (85, 6, P_START_TIME_req, 0, null),
		P_FINISH_TIME         (86, 7, P_FINISH_TIME_req, 0, null),
		P_FILE_PATH           (87, 8, P_FILE_PATH_req, 0, null),
		P_FILE_NAME           (88, 9, P_FILE_NAME_req, 0, null),
		P_FILE_SIZE           (89, 10, P_FILE_SIZE_req, 0, null),
		P_BYTES_TRANSFERRED   (90, 11, P_BYTES_TRANSFERRED_req, 0, null),
		P_BYTES_PER_SECOND    (91, 12, P_BYTES_PER_SECOND_req, 0, null),
		P_CHAT_MSG_GUID       (92, 13, P_CHAT_MSG_GUID_req, 0, null),
		P_CHAT_MSG_INDEX      (93, 14, P_CHAT_MSG_INDEX_req, 0, null),
		P_CONVERSATION        (98, 15, P_CONVERSATION_req, 18, null);
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
			case 80: return P_TYPE;
			case 81: return P_PARTNER_HANDLE;
			case 82: return P_PARTNER_DISPLAY_NAME;
			case 83: return P_STATUS;
			case 84: return P_FAILURE_REASON;
			case 85: return P_START_TIME;
			case 86: return P_FINISH_TIME;
			case 87: return P_FILE_PATH;
			case 88: return P_FILE_NAME;
			case 89: return P_FILE_SIZE;
			case 90: return P_BYTES_TRANSFERRED;
			case 91: return P_BYTES_PER_SECOND;
			case 92: return P_CHAT_MSG_GUID;
			case 93: return P_CHAT_MSG_INDEX;
			case 98: return P_CONVERSATION;
			}
			return P_UNKNOWN;
		}
		public static final int P_TYPE_VALUE                 = 80;
		public static final int P_PARTNER_HANDLE_VALUE       = 81;
		public static final int P_PARTNER_DISPLAY_NAME_VALUE = 82;
		public static final int P_STATUS_VALUE               = 83;
		public static final int P_FAILURE_REASON_VALUE       = 84;
		public static final int P_START_TIME_VALUE           = 85;
		public static final int P_FINISH_TIME_VALUE          = 86;
		public static final int P_FILE_PATH_VALUE            = 87;
		public static final int P_FILE_NAME_VALUE            = 88;
		public static final int P_FILE_SIZE_VALUE            = 89;
		public static final int P_BYTES_TRANSFERRED_VALUE    = 90;
		public static final int P_BYTES_PER_SECOND_VALUE     = 91;
		public static final int P_CHAT_MSG_GUID_VALUE        = 92;
		public static final int P_CHAT_MSG_INDEX_VALUE       = 93;
		public static final int P_CONVERSATION_VALUE         = 98;
	}
	private final static byte[] accept_req = {(byte) 90,(byte) 82,(byte) 6,(byte) 3};
	/** Accepts an incoming file transfer and saves it to specified file on the local file system. If the specified file exists, SkypeKit will silently overwrite it. Your UI should prompting the user for confirmation in this case and provide a means for canceling the file transfer or specifying a different target file. 	 * @param filenameWithPath Where on the local file system to save the file being transferred. Note that you should specify the path as being fully-qualified. Otherwise, SkypeKit will be assume it to be relative to the SkypeKit runtime path, since the method is actually executed in the runtime context. 
	 * @return success Set to true if the specified target file was successfully created on the local file system -and- the initial write(s) succeeded. However, the transfer itself can subsequently fail before completion due to its being canceled (either locally or remotely), network failure, local file system space/write issues, and so forth. 
	 */
	public boolean accept(String filenameWithPath) {
		try {
			return sidDoRequest(accept_req)
			.addFilenameParm(1, filenameWithPath)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] pause_req = {(byte) 90,(byte) 82,(byte) 6,(byte) 4};
	/** Temporarily pauses an in-progress incoming or outgoing file transfer. For incoming transfers, only this affects the sender and the invoking recipient only. For outgoing transfers, this affects the sender and all recipients. 
	 */
	public void pause() {
		try {
			sidDoRequest(pause_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] resume_req = {(byte) 90,(byte) 82,(byte) 6,(byte) 5};
	/** Resumes a previously paused file transfer. 
	 */
	public void resume() {
		try {
			sidDoRequest(resume_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] cancel_req = {(byte) 90,(byte) 82,(byte) 6,(byte) 6};
	/** Cancels an in-progress file transfer. Transfer.STATUS will transition to CANCELLED for incoming file transfers and to CANCELLED_BY_REMOTE for outgoing transfers. 
	 */
	public void cancel() {
		try {
			sidDoRequest(cancel_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	/***
	 * generic multiget of a list of Property
	 * @param requested the list of requested properties of Transfer
	 * @return SidGetResponding
	 */
	public SidGetResponding sidMultiGet(Property[] requested) {
		return super.sidMultiGet(requested);
	}
	/***
	 * generic multiget of list of Property for a list of Transfer
	 * @param requested the list of requested properties
	 * @return SidGetResponding[] can be casted to (Transfer[]) if all properties are cached
	 */
	static public SidGetResponding[] sidMultiGet(Property[] requested, Transfer[] objects) {
		return SidObject.sidMultiGet(requested, objects);
	}
	/** INCOMING / OUTGOING  */
	public Type getType() {
		synchronized(this) {
			if ((mSidCached & 0x1) != 0)
				return mType;
		}
		return (Type) sidRequestEnumProperty(Property.P_TYPE);
	}
	/** Skype Name of the remote party of the file transfer. If a file is posted in a conversation with more than one participant, Transfer objects are created for each of them - so a transfer is always to one single remote target.  */
	public String getPartnerHandle() {
		synchronized(this) {
			if ((mSidCached & 0x2) != 0)
				return mPartnerHandle;
		}
		return sidRequestStringProperty(Property.P_PARTNER_HANDLE);
	}
	/** Display name of the remote participant.  */
	public String getPartnerDisplayName() {
		synchronized(this) {
			if ((mSidCached & 0x4) != 0)
				return mPartnerDisplayName;
		}
		return sidRequestStringProperty(Property.P_PARTNER_DISPLAY_NAME);
	}
	/** Current state of the transfer  */
	public Status getStatus() {
		synchronized(this) {
			if ((mSidCached & 0x8) != 0)
				return mStatus;
		}
		return (Status) sidRequestEnumProperty(Property.P_STATUS);
	}
	/** Set whenever P_STATUS transitions to FAILED.  */
	public FailureReason getFailureReason() {
		synchronized(this) {
			if ((mSidCached & 0x10) != 0)
				return mFailureReason;
		}
		return (FailureReason) sidRequestEnumProperty(Property.P_FAILURE_REASON);
	}
	/** UNIX timestamp of when this Transfer instance was instantiated, not when the transfer process actually started (was accepted from receiver side). Do not use this property when calculate the data transfer speed! Instead, monitor changes to P_BYTESPERSECOND.  */
	public long getStartTime() {
		synchronized(this) {
			if ((mSidCached & 0x20) != 0)
				return mStartTime;
		}
		return sidRequestTimestampProperty(Property.P_START_TIME);
	}
	/** UNIX timestamp of when this Transfer COMPLETED or FAILED. This property is never set if the receiving side (local or remote) canceled the transfer.  */
	public long getFinishTime() {
		synchronized(this) {
			if ((mSidCached & 0x40) != 0)
				return mFinishTime;
		}
		return sidRequestTimestampProperty(Property.P_FINISH_TIME);
	}
	/** The path -and- filename of the file being transfered (typically fully qualified). For the receiver, SkypeKit sets this property upon acceptance of the incoming transfer. If not fully qualified, the path is assumed to be relative to the path of the SkypeKit runtime.  */
	public String getFilePath() {
		synchronized(this) {
			if ((mSidCached & 0x80) != 0)
				return mFilePath;
		}
		return sidRequestStringProperty(Property.P_FILE_PATH);
	}
	/** The filename -only- of the file being transfered. The receiver side can use this property to pre-populate relevant UI components while prompting the user to accept the incoming transfer.  */
	public String getFileName() {
		synchronized(this) {
			if ((mSidCached & 0x100) != 0)
				return mFileName;
		}
		return sidRequestStringProperty(Property.P_FILE_NAME);
	}
	/** The size of the file being transferred in bytes. Depending on the magnitude of this value, your UI might want to display the size in terms of kilobytes or even megabytes.  */
	public String getFileSize() {
		synchronized(this) {
			if ((mSidCached & 0x200) != 0)
				return mFileSize;
		}
		return sidRequestStringProperty(Property.P_FILE_SIZE);
	}
	/**
	 * The number of bytes already transferred. Calculate the percentage of the file transferred so far as: 
	 * @code 
	 * P_BYTESTRANSFERRED / (P_FILESIZE / 100);  
	 * </CODE> 
	 * 
	 * Use float variables to avoid problems with files smaller than 100 bytes! 
	 */
	public String getBytesTransferred() {
		synchronized(this) {
			if ((mSidCached & 0x400) != 0)
				return mBytesTransferred;
		}
		return sidRequestStringProperty(Property.P_BYTES_TRANSFERRED);
	}
	/** Current data transfer speed in bytes per second. Typically, your UI will want to display this value as kilobytes per second (KBps).  */
	public int getBytesPerSecond() {
		synchronized(this) {
			if ((mSidCached & 0x800) != 0)
				return mBytesPerSecond;
		}
		return sidRequestUintProperty(Property.P_BYTES_PER_SECOND);
	}
	/** The "global ID" of this Transfer's associated Message instance. GUIDs are shared across Skype client instances and across all users that can see this Message.  */
	public byte[] getChatMsgGuid() {
		synchronized(this) {
			if ((mSidCached & 0x1000) != 0)
				return mChatMsgGuid;
		}
		return sidRequestBinaryProperty(Property.P_CHAT_MSG_GUID);
	}
	/** A more or less arbitrary index for ordering multiple file transfers within the UI.  */
	public int getChatMsgIndex() {
		synchronized(this) {
			if ((mSidCached & 0x2000) != 0)
				return mChatMsgIndex;
		}
		return sidRequestUintProperty(Property.P_CHAT_MSG_INDEX);
	}
	/**
	 * The "global ID" of this Transfer's associated Conversation (as chained through its associated Message). GUIDs are shared across Skype client instances and across all users that can see this Conversation. 
	 * 
	 * Note that currently SkypeKit sets this property for INCOMING file transfers only and returns 0 (zero) for all sending side transfers. This is a known bug. 
	 */
	public Conversation getConversation() {
		synchronized(this) {
			if ((mSidCached & 0x4000) != 0)
				return mConversation;
		}
		return (Conversation) sidRequestObjectProperty(Property.P_CONVERSATION);
	}
	public String sidGetStringProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 81:
			return mPartnerHandle;
		case 82:
			return mPartnerDisplayName;
		case 87:
			return mFilePath;
		case 88:
			return mFileName;		case 89:
			return mFileSize;
		case 90:
			return mBytesTransferred;
		}
		return "";
	}
	public SidObject sidGetObjectProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 98);
		return mConversation;
	}
	public int sidGetIntProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 91:
			return mBytesPerSecond;
		case 93:
			return mChatMsgIndex;
		}
		return 0;
	}
	public EnumConverting sidGetEnumProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 80:
			return mType;
		case 83:
			return mStatus;
		case 84:
			return mFailureReason;
		}
		return null;
	}
	public byte[] sidGetBinaryProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 92);
		return mChatMsgGuid;
	}
	public String getPropertyAsString(final int prop) {
		switch (prop) {
		case 80: return getType().toString();
		case 81: return getPartnerHandle();
		case 82: return getPartnerDisplayName();
		case 83: return getStatus().toString();
		case 84: return getFailureReason().toString();
		case 87: return getFilePath();
		case 88: return getFileName();
		case 89: return getFileSize();
		case 90: return getBytesTransferred();
		case 91: return Integer.toString(getBytesPerSecond());
		case 92: return "<binary>";
		case 93: return Integer.toString(getChatMsgIndex());
		case 98: return getConversation() != null ? Integer.toString(getConversation().getOid()) : "(null)";
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
				case 80: mType = Type.get(value); break;
				case 81:
					if (svalue != null) mPartnerHandle = svalue;
					else mSidCached &=~bit;
					break;
				case 82:
					if (svalue != null) mPartnerDisplayName = svalue;
					else mSidCached &=~bit;
					break;
				case 83: mStatus = Status.get(value); break;
				case 84: mFailureReason = FailureReason.get(value); break;
				case 87:
					if (svalue != null) mFilePath = svalue;
					else mSidCached &=~bit;
					break;
				case 88:
					if (svalue != null) mFileName = svalue;
					else mSidCached &=~bit;
					break;
				case 89:
					if (svalue != null) mFileSize = svalue;
					else mSidCached &=~bit;
					break;
				case 90:
					if (svalue != null) mBytesTransferred = svalue;
					else mSidCached &=~bit;
					break;
				case 91: mBytesPerSecond = value; break;
				case 93: mChatMsgIndex = value; break;
				case 98:
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
		TransferListener listener = ((Skype) mSidRoot).getTransferListener();
		if (listener != null)
			listener.onPropertyChange(this, property, value, svalue);
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final String newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 81:
			mSidCached |= 0x2;
			mPartnerHandle=  newValue;
			break;
		case 82:
			mSidCached |= 0x4;
			mPartnerDisplayName=  newValue;
			break;
		case 87:
			mSidCached |= 0x80;
			mFilePath=  newValue;
			break;
		case 88:
			mSidCached |= 0x100;
			mFileName=  newValue;
			break;
		case 89:
			mSidCached |= 0x200;
			mFileSize=  newValue;
			break;
		case 90:
			mSidCached |= 0x400;
			mBytesTransferred=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final SidObject newValue) {
		final int propId = prop.getId();
		assert(propId == 98);
		mSidCached |= 0x4000;
		mConversation= (Conversation) newValue;
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final int newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 80:
			mSidCached |= 0x1;
			mType= Type.get(newValue);
			break;
		case 83:
			mSidCached |= 0x8;
			mStatus= Status.get(newValue);
			break;
		case 84:
			mSidCached |= 0x10;
			mFailureReason= FailureReason.get(newValue);
			break;
		case 91:
			mSidCached |= 0x800;
			mBytesPerSecond=  newValue;
			break;
		case 93:
			mSidCached |= 0x2000;
			mChatMsgIndex=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final byte[] newValue) {
		final int propId = prop.getId();
		assert(propId == 92);
		mSidCached |= 0x1000;
		mChatMsgGuid=  newValue;
	}
	public Type          mType;
	public String        mPartnerHandle;
	public String        mPartnerDisplayName;
	public Status        mStatus;
	public FailureReason mFailureReason;
	public long          mStartTime;
	public long          mFinishTime;
	public String        mFilePath;
	public String        mFileName;
	public String        mFileSize;
	public String        mBytesTransferred;
	public int           mBytesPerSecond;
	public byte[]        mChatMsgGuid;
	public int           mChatMsgIndex;
	public Conversation  mConversation;
	public int moduleId() {
		return 6;
	}
	
	public Transfer(final int oid, final SidRoot root) {
		super(oid, root, 15);
	}
}
