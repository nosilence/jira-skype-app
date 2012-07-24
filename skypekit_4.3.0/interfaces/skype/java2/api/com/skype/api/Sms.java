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
 * Wrapper class that includes SMS-specific properties and methods, such as P_BODY and GetTargetPrice. Instantiate SMS instances using Skype.CreateOutgoingSms; post SMS messages to a Conversation using Conversation.PostSMS.  
 * 
 * Each SMS can have multiple targets (normalized phone numbers). Note that in a Conversation context, every SMS instance has a corresponding Message instance. Once you've posted an SMS, you can retrieve its corresponding Message instance using Sms.GetPropChatmsgId. That Message instance's P_BODY_XML property contains the SMS message data, such as price, target phone number(s), failure codes, and so forth, which you can parsed out and display in the UI. To put it another way, the object chain goes like this: 
 * 
 * @code 
 * Conversation->Message->SMS  
 * </CODE> 
 * 
 * Note that SkypeKit SDK supports outgoing SMS messages only. SkypeKit clients, even when logged in with accounts that have SkypeIn numbers, cannot receive SMS messages. 
 */
public final class Sms extends SidObject {
	public enum Type implements EnumConverting {
		/** a normal outgoing SMS message */
		OUTGOING                 (2),
		/** a message requesting a SMS confirmation code be sent to the number provided */
		CONFIRMATION_CODE_REQUEST(3),
		/** a message returning the SMS confirmation code received as a result of a CONFIRMATION_CODE_REQUEST to authorize it */
		CONFIRMATION_CODE_SUBMIT (4);
		private final int key;
		Type(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return OUTGOING; }
		public EnumConverting convert(int from) { return Type.get(from); }
		public EnumConverting[] getArray(final int size) { return new Type[size]; }
		public static Type get(int from) {
			switch (from) {
			case 2: return OUTGOING;
			case 3: return CONFIRMATION_CODE_REQUEST;
			case 4: return CONFIRMATION_CODE_SUBMIT;
			}
			return OUTGOING;
		}
		public static final int OUTGOING_VALUE                  = 2;
		public static final int CONFIRMATION_CODE_REQUEST_VALUE = 3;
		public static final int CONFIRMATION_CODE_SUBMIT_VALUE  = 4;
	}
	public enum OutgoingReplyType implements EnumConverting {
		/** outgoing Sms source ID is set to skypename (where applicable) */
		REPLY_SKYPENAME           (1),
		/** user's verified mobile nr is used as outgoing Sms source ID */
		REPLY_USER_PHONE_NR       (2),
		/** outgoing Sms source ID was allocated from 2-way number pool */
		REPLY_2WAY_AUTOASSIGNED_NR(3),
		/** outgoing Sms targets have different types, use GetTargetReplyType to see the real type */
		REPLY_TYPE_MIXED          (4);
		private final int key;
		OutgoingReplyType(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return REPLY_SKYPENAME; }
		public EnumConverting convert(int from) { return OutgoingReplyType.get(from); }
		public EnumConverting[] getArray(final int size) { return new OutgoingReplyType[size]; }
		public static OutgoingReplyType get(int from) {
			switch (from) {
			case 1: return REPLY_SKYPENAME;
			case 2: return REPLY_USER_PHONE_NR;
			case 3: return REPLY_2WAY_AUTOASSIGNED_NR;
			case 4: return REPLY_TYPE_MIXED;
			}
			return REPLY_SKYPENAME;
		}
		public static final int REPLY_SKYPENAME_VALUE            = 1;
		public static final int REPLY_USER_PHONE_NR_VALUE        = 2;
		public static final int REPLY_2WAY_AUTOASSIGNED_NR_VALUE = 3;
		public static final int REPLY_TYPE_MIXED_VALUE           = 4;
	}
	public enum Status implements EnumConverting {
		/** SMS can be edited and targets changed */
		COMPOSING          (3),
		/** SMS is being sent currently to server */
		SENDING_TO_SERVER  (4),
		/** SMS has been sent to server, pending delivery notification */
		SENT_TO_SERVER     (5),
		/** SMS has been successfully delivered to all recipients */
		DELIVERED          (6),
		/** SMS has been delivered to some recipients, but some failed */
		SOME_TARGETS_FAILED(7),
		/** SMS has not been delivered to any recipient */
		FAILED             (8);
		private final int key;
		Status(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return COMPOSING; }
		public EnumConverting convert(int from) { return Status.get(from); }
		public EnumConverting[] getArray(final int size) { return new Status[size]; }
		public static Status get(int from) {
			switch (from) {
			case 3: return COMPOSING;
			case 4: return SENDING_TO_SERVER;
			case 5: return SENT_TO_SERVER;
			case 6: return DELIVERED;
			case 7: return SOME_TARGETS_FAILED;
			case 8: return FAILED;
			}
			return COMPOSING;
		}
		public static final int COMPOSING_VALUE           = 3;
		public static final int SENDING_TO_SERVER_VALUE   = 4;
		public static final int SENT_TO_SERVER_VALUE      = 5;
		public static final int DELIVERED_VALUE           = 6;
		public static final int SOME_TARGETS_FAILED_VALUE = 7;
		public static final int FAILED_VALUE              = 8;
	}
	public enum FailureReason implements EnumConverting {
		MISC_ERROR               (1),
		SERVER_CONNECT_FAILED    (2),
		NO_SMS_CAPABILITY        (3),
		INSUFFICIENT_FUNDS       (4),
		INVALID_CONFIRMATION_CODE(5),
		USER_BLOCKED             (6),
		IP_BLOCKED               (7),
		NODE_BLOCKED             (8),
		NO_SENDERID_CAPABILITY   (9);
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
			case 1: return MISC_ERROR;
			case 2: return SERVER_CONNECT_FAILED;
			case 3: return NO_SMS_CAPABILITY;
			case 4: return INSUFFICIENT_FUNDS;
			case 5: return INVALID_CONFIRMATION_CODE;
			case 6: return USER_BLOCKED;
			case 7: return IP_BLOCKED;
			case 8: return NODE_BLOCKED;
			case 9: return NO_SENDERID_CAPABILITY;
			}
			return MISC_ERROR;
		}
		public static final int MISC_ERROR_VALUE                = 1;
		public static final int SERVER_CONNECT_FAILED_VALUE     = 2;
		public static final int NO_SMS_CAPABILITY_VALUE         = 3;
		public static final int INSUFFICIENT_FUNDS_VALUE        = 4;
		public static final int INVALID_CONFIRMATION_CODE_VALUE = 5;
		public static final int USER_BLOCKED_VALUE              = 6;
		public static final int IP_BLOCKED_VALUE                = 7;
		public static final int NODE_BLOCKED_VALUE              = 8;
		public static final int NO_SENDERID_CAPABILITY_VALUE    = 9;
	}
	/** this status shows target properties and usability for sending */
	public enum TargetStatus implements EnumConverting {
		TARGET_ANALYZING          (1),
		TARGET_UNDEFINED          (2),
		TARGET_ACCEPTABLE         (3),
		TARGET_NOT_ROUTABLE       (4),
		TARGET_DELIVERY_PENDING   (5),
		TARGET_DELIVERY_SUCCESSFUL(6),
		TARGET_DELIVERY_FAILED    (7);
		private final int key;
		TargetStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return TARGET_ANALYZING; }
		public EnumConverting convert(int from) { return TargetStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new TargetStatus[size]; }
		public static TargetStatus get(int from) {
			switch (from) {
			case 1: return TARGET_ANALYZING;
			case 2: return TARGET_UNDEFINED;
			case 3: return TARGET_ACCEPTABLE;
			case 4: return TARGET_NOT_ROUTABLE;
			case 5: return TARGET_DELIVERY_PENDING;
			case 6: return TARGET_DELIVERY_SUCCESSFUL;
			case 7: return TARGET_DELIVERY_FAILED;
			}
			return TARGET_ANALYZING;
		}
		public static final int TARGET_ANALYZING_VALUE           = 1;
		public static final int TARGET_UNDEFINED_VALUE           = 2;
		public static final int TARGET_ACCEPTABLE_VALUE          = 3;
		public static final int TARGET_NOT_ROUTABLE_VALUE        = 4;
		public static final int TARGET_DELIVERY_PENDING_VALUE    = 5;
		public static final int TARGET_DELIVERY_SUCCESSFUL_VALUE = 6;
		public static final int TARGET_DELIVERY_FAILED_VALUE     = 7;	}
	public enum SetBodyResult implements EnumConverting {
		/** body not set. message status wrong or invalid, or body not valid utf8 string */
		BODY_INVALID         (0),
		/** body too long. set, but truncated. charsUntilNextChunk contains maxChunks value */
		BODY_TRUNCATED       (1),
		/** body was set OK */
		BODY_OK              (2),
		/** last unicode char was ignored, as some of the text would be deleted due to conversion */
		BODY_LASTCHAR_IGNORED(3);
		private final int key;
		SetBodyResult(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return BODY_INVALID; }
		public EnumConverting convert(int from) { return SetBodyResult.get(from); }
		public EnumConverting[] getArray(final int size) { return new SetBodyResult[size]; }
		public static SetBodyResult get(int from) {
			switch (from) {
			case 0: return BODY_INVALID;
			case 1: return BODY_TRUNCATED;
			case 2: return BODY_OK;
			case 3: return BODY_LASTCHAR_IGNORED;
			}
			return BODY_INVALID;
		}
		public static final int BODY_INVALID_VALUE          = 0;
		public static final int BODY_TRUNCATED_VALUE        = 1;
		public static final int BODY_OK_VALUE               = 2;
		public static final int BODY_LASTCHAR_IGNORED_VALUE = 3;
	}
	public enum ConfirmType implements EnumConverting {
		/** Confirm mobile number as SMS sender number */
		ID_SMS    (1),
		/** Confirm mobile number as CLI for SkypeOut calls */
		ID_MOBILE (2),
		/** unused currently */
		ID_SKYPEIN(3);
		private final int key;
		ConfirmType(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return ID_SMS; }
		public EnumConverting convert(int from) { return ConfirmType.get(from); }
		public EnumConverting[] getArray(final int size) { return new ConfirmType[size]; }
		public static ConfirmType get(int from) {
			switch (from) {
			case 1: return ID_SMS;
			case 2: return ID_MOBILE;
			case 3: return ID_SKYPEIN;
			}
			return ID_SMS;
		}
		public static final int ID_SMS_VALUE     = 1;
		public static final int ID_MOBILE_VALUE  = 2;
		public static final int ID_SKYPEIN_VALUE = 3;
	}
	private final static byte[] P_TYPE_req = {(byte) 90,(byte) 71,(byte) 190,(byte) 1,(byte) 93,(byte) 12};
	private final static byte[] P_OUTGOING_REPLY_TYPE_req = {(byte) 90,(byte) 71,(byte) 187,(byte) 9,(byte) 93,(byte) 12};
	private final static byte[] P_STATUS_req = {(byte) 90,(byte) 71,(byte) 191,(byte) 1,(byte) 93,(byte) 12};
	private final static byte[] P_FAILURE_REASON_req = {(byte) 90,(byte) 71,(byte) 192,(byte) 1,(byte) 93,(byte) 12};
	private final static byte[] P_IS_FAILED_UNSEEN_req = {(byte) 90,(byte) 71,(byte) 48,(byte) 93,(byte) 12};
	private final static byte[] P_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 198,(byte) 1,(byte) 93,(byte) 12};
	private final static byte[] P_PRICE_req = {(byte) 90,(byte) 71,(byte) 193,(byte) 1,(byte) 93,(byte) 12};
	private final static byte[] P_PRICE_PRECISION_req = {(byte) 90,(byte) 71,(byte) 49,(byte) 93,(byte) 12};
	private final static byte[] P_PRICE_CURRENCY_req = {(byte) 90,(byte) 71,(byte) 194,(byte) 1,(byte) 93,(byte) 12};
	private final static byte[] P_REPLY_TO_NUMBER_req = {(byte) 90,(byte) 71,(byte) 199,(byte) 1,(byte) 93,(byte) 12};
	private final static byte[] P_TARGET_NUMBERS_req = {(byte) 90,(byte) 71,(byte) 195,(byte) 1,(byte) 93,(byte) 12};
	private final static byte[] P_TARGET_STATUSES_req = {(byte) 90,(byte) 71,(byte) 196,(byte) 1,(byte) 93,(byte) 12};
	private final static byte[] P_BODY_req = {(byte) 90,(byte) 71,(byte) 197,(byte) 1,(byte) 93,(byte) 12};
	private final static byte[] P_CHAT_MSG_req = {(byte) 90,(byte) 71,(byte) 200,(byte) 6,(byte) 93,(byte) 12};
	/** Properties of the Sms class */
	public enum Property implements PropertyEnumConverting {
		P_UNKNOWN            (0,0,null,0,null),
		P_TYPE               (190, 1, P_TYPE_req, 0, Type.get(0)),
		P_OUTGOING_REPLY_TYPE(1211, 2, P_OUTGOING_REPLY_TYPE_req, 0, OutgoingReplyType.get(0)),
		P_STATUS             (191, 3, P_STATUS_req, 0, Status.get(0)),
		P_FAILURE_REASON     (192, 4, P_FAILURE_REASON_req, 0, FailureReason.get(0)),
		P_IS_FAILED_UNSEEN   (48, 5, P_IS_FAILED_UNSEEN_req, 0, null),
		P_TIMESTAMP          (198, 6, P_TIMESTAMP_req, 0, null),
		P_PRICE              (193, 7, P_PRICE_req, 0, null),
		P_PRICE_PRECISION    (49, 8, P_PRICE_PRECISION_req, 0, null),
		P_PRICE_CURRENCY     (194, 9, P_PRICE_CURRENCY_req, 0, null),
		P_REPLY_TO_NUMBER    (199, 10, P_REPLY_TO_NUMBER_req, 0, null),
		P_TARGET_NUMBERS     (195, 11, P_TARGET_NUMBERS_req, 0, null),
		P_TARGET_STATUSES    (196, 12, P_TARGET_STATUSES_req, 0, null),
		P_BODY               (197, 13, P_BODY_req, 0, null),
		P_CHAT_MSG           (840, 14, P_CHAT_MSG_req, 9, null);
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
			case  190: return P_TYPE;
			case 1211: return P_OUTGOING_REPLY_TYPE;
			case  191: return P_STATUS;
			case  192: return P_FAILURE_REASON;
			case   48: return P_IS_FAILED_UNSEEN;
			case  198: return P_TIMESTAMP;
			case  193: return P_PRICE;
			case   49: return P_PRICE_PRECISION;
			case  194: return P_PRICE_CURRENCY;
			case  199: return P_REPLY_TO_NUMBER;
			case  195: return P_TARGET_NUMBERS;
			case  196: return P_TARGET_STATUSES;
			case  197: return P_BODY;
			case  840: return P_CHAT_MSG;
			}
			return P_UNKNOWN;
		}
		public static final int P_TYPE_VALUE                =  190;
		public static final int P_OUTGOING_REPLY_TYPE_VALUE = 1211;
		public static final int P_STATUS_VALUE              =  191;
		public static final int P_FAILURE_REASON_VALUE      =  192;
		public static final int P_IS_FAILED_UNSEEN_VALUE    =   48;
		public static final int P_TIMESTAMP_VALUE           =  198;
		public static final int P_PRICE_VALUE               =  193;
		public static final int P_PRICE_PRECISION_VALUE     =   49;
		public static final int P_PRICE_CURRENCY_VALUE      =  194;
		public static final int P_REPLY_TO_NUMBER_VALUE     =  199;
		public static final int P_TARGET_NUMBERS_VALUE      =  195;
		public static final int P_TARGET_STATUSES_VALUE     =  196;
		public static final int P_BODY_VALUE                =  197;
		public static final int P_CHAT_MSG_VALUE            =  840;
	}
	private final static byte[] getTargetStatus_req = {(byte) 90,(byte) 82,(byte) 12,(byte) 4};
	/** Retrieves the send status of this SMS to a particular recipient (P_TARGET_STATUSES) either prior to or after invoking Conversation.PostSMS. 
	 * @param target The normalized phone number of the target recipient. 
	 * @return status The send status of the target recipient, for example, TARGET_ANALYZING, TARGET_DELIVERY_PENDING, TARGET_DELIVERY_SUCCESSFUL, TARGET_DELIVERY_FAILED, and so forth. TARGET_UNDEFINED implies that the specified target is not a recipient of this SMS. 
	 */
	public TargetStatus getTargetStatus(String target) {
		try {
			return (TargetStatus) sidDoRequest(getTargetStatus_req)
			.addStringParm(1, target)
			.endRequest().getEnumParm(1, TargetStatus.get(0), true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return TargetStatus.get(0);
		}
	}
	private final static byte[] getTargetReplyNumber_req = {(byte) 90,(byte) 82,(byte) 12,(byte) 3};
	/** Call this when target has been delivered, to see what is the assigned number.                      Calling before target has been delivered shows preliminary assigned number, which may not be allocated (ie when delivery fails)	 * @param target
	 * @return number
	 */
	public String getTargetReplyNumber(String target) {
		try {
			return sidDoRequest(getTargetReplyNumber_req)
			.addStringParm(1, target)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] getTargetReplyType_req = {(byte) 90,(byte) 82,(byte) 12,(byte) 10};
	/** Fetch target reply type if toplevel Sms type is REPLY_TYPE_MIXED
	 * @param target
	 * @return type
	 */
	public OutgoingReplyType getTargetReplyType(String target) {
		try {
			return (OutgoingReplyType) sidDoRequest(getTargetReplyType_req)
			.addStringParm(1, target)
			.endRequest().getEnumParm(1, OutgoingReplyType.get(0), true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return OutgoingReplyType.get(0);
		}
	}
	private final static byte[] getTargetPrice_req = {(byte) 90,(byte) 82,(byte) 12,(byte) 13};
	/**
	 * Retrieves the amount of Skype credit necessary to send the SMS to a particular recipient. Defaults to -1 on instantiation and set only when that recipient's status reflects TARGET_ACCEPTABLE. Use Sms.GetPropPrice to retrieve the total cost of this SMS. 
	 * 
	 * Note that the target price is an integer value. Calculate the actual price (in units specified by P_PRICE_CURRENCY) using P_PRICE_PRECISION as: 
	 * @code 
	 * actualTargetPrice = targetPrice / 10^pricePrecision;  
	 * </CODE> 
	
	 * @param target The normalized phone number of the target recipient. 
	 * @return price The price of sending this SMS message to the target recipient. 
	 */
	public int getTargetPrice(String target) {
		try {
			return sidDoRequest(getTargetPrice_req)
			.addStringParm(1, target)
			.endRequest().getUintParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return 0;
		}
	}
	private final static byte[] setTargets_req = {(byte) 90,(byte) 82,(byte) 12,(byte) 6};
	/** Sets the recipient(s) of this SMS. Note that each invocation replaces the target list and re-calculates all prices - they are not additive! 
	 * @param numbers Normalized phone number(s) of the intended recipient(s). 
	 * @return success Set to true if the target list appears to contain valid, normalized telephone numbers. Note that this check is not very reliable. Actual target validity checking occurs asynchronously in the background, and manifests itself as a series of Sms.P_TARGET_STATUSES property change events. 
	 */
	public boolean setTargets(String[] numbers) {
		try {
			return sidDoRequest(setTargets_req)
			.addStringListParm(1, numbers)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] setBody_req = {(byte) 90,(byte) 82,(byte) 12,(byte) 7};
	public class SetBodyResponse {
		public SetBodyResult result;
		public String[] chunks;
		public int charsUntilNextChunk;
	};
	
	/** -The- method for setting the body text of this SMS. While Conversation.PostSMS does have a body argument, that argument is currently unused. 
	 * @param text Message body text. 
	 * @return SetBodyResponse
	 * <br> - result Whether the Message body was successfully set and if not, why not. 
	 * <br> - chunks The Message body as a list of individual chunks. 
	 * <br> - charsUntilNextChunk Number of available characters until creation of the next chunk becomes necessary. 
	 */
	public SetBodyResponse setBody(String text) {
		try {
			Decoding decoder = sidDoRequest(setBody_req)
			.addStringParm(1, text)
			.endRequest();
			SetBodyResponse result = new SetBodyResponse();
			result.result = (SetBodyResult) decoder.getEnumParm(1, SetBodyResult.get(0), false);
			result.chunks = decoder.getStringListParm(2, false);
			result.charsUntilNextChunk = decoder.getUintParm(3, true);
			return result;
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getBodyChunks_req = {(byte) 90,(byte) 82,(byte) 12,(byte) 8};
	public class GetBodyChunksResponse {
		public String[] textChunks;
		public int charsUntilNextChunk;
	};
	
	/** Retrieves string list of SMS text chunks in first argument, while the second argument contains the number of available characters until creation of the next chunk becomes necessary. 
	 * @return GetBodyChunksResponse
	 * <br> - textChunks List of text chunk strings 
	 * <br> - charsUntilNextChunk Number of available characters until creation of the next chunk becomes necessary. 
	 */
	public GetBodyChunksResponse getBodyChunks() {
		try {
			Decoding decoder = sidDoRequest(getBodyChunks_req)
			.endRequest();
			GetBodyChunksResponse result = new GetBodyChunksResponse();
			result.textChunks = decoder.getStringListParm(1, false);
			result.charsUntilNextChunk = decoder.getUintParm(2, true);
			return result;
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	/***
	 * generic multiget of a list of Property
	 * @param requested the list of requested properties of Sms
	 * @return SidGetResponding
	 */
	public SidGetResponding sidMultiGet(Property[] requested) {
		return super.sidMultiGet(requested);
	}
	/***
	 * generic multiget of list of Property for a list of Sms
	 * @param requested the list of requested properties
	 * @return SidGetResponding[] can be casted to (Sms[]) if all properties are cached
	 */
	static public SidGetResponding[] sidMultiGet(Property[] requested, Sms[] objects) {
		return SidObject.sidMultiGet(requested, objects);
	}
	public Type getType() {
		synchronized(this) {
			if ((mSidCached & 0x1) != 0)
				return mType;
		}
		return (Type) sidRequestEnumProperty(Property.P_TYPE);
	}
	public OutgoingReplyType getOutgoingReplyType() {
		synchronized(this) {
			if ((mSidCached & 0x2) != 0)
				return mOutgoingReplyType;
		}
		return (OutgoingReplyType) sidRequestEnumProperty(Property.P_OUTGOING_REPLY_TYPE);
	}
	public Status getStatus() {
		synchronized(this) {
			if ((mSidCached & 0x4) != 0)
				return mStatus;
		}
		return (Status) sidRequestEnumProperty(Property.P_STATUS);
	}
	/** Set asynchronously and meaningful only after invoking Conversation.PostSMS and detecting Sms.STATUS of SOME_TARGETS_FAILED or FAILED.  */
	public FailureReason getFailureReason() {
		synchronized(this) {
			if ((mSidCached & 0x8) != 0)
				return mFailureReason;
		}
		return (FailureReason) sidRequestEnumProperty(Property.P_FAILURE_REASON);
	}
	/** set to 1 when status goes to FAILED. use MarkSeen() to clear */
	public boolean getIsFailedUnseen() {
		synchronized(this) {
			if ((mSidCached & 0x10) != 0)
				return mIsFailedUnseen;
		}
		return sidRequestBoolProperty(Property.P_IS_FAILED_UNSEEN);
	}
	/** unix timestamp of message submission */
	public long getTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x20) != 0)
				return mTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_TIMESTAMP);
	}
	/**
	 * The total price of sending this SMS message (sum of the individual prices to send to each recipient). Defaults to -1 on instantiation and incremented by the price for each recipient once that recipient's status reflects TARGET_ACCEPTABLE. Use Sms.GetTargetPrice to retrieve individual target prices. 
	 * 
	 * A value of MAX_UINT indicates that SkypeKit is actively querying and/or updating the value. Note that P_PRICE is an integer value. Calculate the actual price (in units specified by P_PRICE_CURRENCY) using P_PRICE_PRECISION as: 
	 * 
	 * @code 
	 * actualPrice = price / 10^pricePrecision;  
	 * </CODE> 
	 */
	public int getPrice() {
		synchronized(this) {
			if ((mSidCached & 0x40) != 0)
				return mPrice;
		}
		return sidRequestUintProperty(Property.P_PRICE);
	}
	/** The decimal precision of the SMS price values, both individual and total. For example, a value of 2 indicates that you should divide the price (represented as an integer) by 100 (10^2) to obtain the actual price.  */
	public int getPricePrecision() {
		synchronized(this) {
			if ((mSidCached & 0x80) != 0)
				return mPricePrecision;
		}
		return sidRequestUintProperty(Property.P_PRICE_PRECISION);
	}
	/** should be same as account currency at the time of composing/sending */
	public String getPriceCurrency() {		synchronized(this) {
			if ((mSidCached & 0x100) != 0)
				return mPriceCurrency;
		}
		return sidRequestStringProperty(Property.P_PRICE_CURRENCY);
	}
	/** TYPE is outgoing: number that should receive the replies (DEPRECATED). TYPE is incoming: original source mobile number */
	public String getReplyToNumber() {
		synchronized(this) {
			if ((mSidCached & 0x200) != 0)
				return mReplyToNumber;
		}
		return sidRequestStringProperty(Property.P_REPLY_TO_NUMBER);
	}
	/** space-separated normalised pstn numbers */
	public String getTargetNumbers() {
		synchronized(this) {
			if ((mSidCached & 0x400) != 0)
				return mTargetNumbers;
		}
		return sidRequestStringProperty(Property.P_TARGET_NUMBERS);
	}
	/** binary blob. track with OnPropertyChange(), access with GetTargetStatus(target) */
	public byte[] getTargetStatuses() {
		synchronized(this) {
			if ((mSidCached & 0x800) != 0)
				return mTargetStatuses;
		}
		return sidRequestBinaryProperty(Property.P_TARGET_STATUSES);
	}
	/** actual payload */
	public String getBody() {
		synchronized(this) {
			if ((mSidCached & 0x1000) != 0)
				return mBody;
		}
		return sidRequestStringProperty(Property.P_BODY);
	}
	/** reference to Message */
	public Message getChatMsg() {
		synchronized(this) {
			if ((mSidCached & 0x2000) != 0)
				return mChatMsg;
		}
		return (Message) sidRequestObjectProperty(Property.P_CHAT_MSG);
	}
	public String sidGetStringProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 194:
			return mPriceCurrency;
		case 199:
			return mReplyToNumber;
		case 195:
			return mTargetNumbers;
		case 197:
			return mBody;
		}
		return "";
	}
	public SidObject sidGetObjectProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 840);
		return mChatMsg;
	}
	public boolean sidGetBoolProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 48);
		return mIsFailedUnseen;
	}
	public int sidGetIntProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 193:
			return mPrice;
		case 49:
			return mPricePrecision;
		}
		return 0;
	}
	public EnumConverting sidGetEnumProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 190:
			return mType;
		case 1211:
			return mOutgoingReplyType;
		case 191:
			return mStatus;
		case 192:
			return mFailureReason;
		}
		return null;
	}
	public byte[] sidGetBinaryProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 196);
		return mTargetStatuses;
	}
	public String getPropertyAsString(final int prop) {
		switch (prop) {
		case 190: return getType().toString();
		case 1211: return getOutgoingReplyType().toString();
		case 191: return getStatus().toString();
		case 192: return getFailureReason().toString();
		case 48: return Boolean.toString(getIsFailedUnseen());
		case 193: return Integer.toString(getPrice());
		case 49: return Integer.toString(getPricePrecision());
		case 194: return getPriceCurrency();
		case 199: return getReplyToNumber();
		case 195: return getTargetNumbers();
		case 196: return "<binary>";
		case 197: return getBody();
		case 840: return getChatMsg() != null ? Integer.toString(getChatMsg().getOid()) : "(null)";
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
				case 190: mType = Type.get(value); break;
				case 1211: mOutgoingReplyType = OutgoingReplyType.get(value); break;
				case 191: mStatus = Status.get(value); break;
				case 192: mFailureReason = FailureReason.get(value); break;
				case 48: mIsFailedUnseen = value != 0; break;
				case 193: mPrice = value; break;
				case 49: mPricePrecision = value; break;
				case 194:
					if (svalue != null) mPriceCurrency = svalue;
					else mSidCached &=~bit;
					break;
				case 199:
					if (svalue != null) mReplyToNumber = svalue;
					else mSidCached &=~bit;
					break;
				case 195:
					if (svalue != null) mTargetNumbers = svalue;
					else mSidCached &=~bit;
					break;
				case 197:
					if (svalue != null) mBody = svalue;
					else mSidCached &=~bit;
					break;
				case 840:
					if (value != 0)
						mChatMsg = (Message) mSidRoot.sidGetObject(property.getModuleId(), value);
					else {
						mChatMsg = null;
						mSidCached &=~bit;
					}
					break;
				default: mSidCached&=~bit; break;
				}
			}
		}
		SmsListener listener = ((Skype) mSidRoot).getSmsListener();
		if (listener != null)
			listener.onPropertyChange(this, property, value, svalue);
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final String newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 194:
			mSidCached |= 0x100;
			mPriceCurrency=  newValue;
			break;
		case 199:
			mSidCached |= 0x200;
			mReplyToNumber=  newValue;
			break;
		case 195:
			mSidCached |= 0x400;
			mTargetNumbers=  newValue;
			break;
		case 197:
			mSidCached |= 0x1000;
			mBody=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final SidObject newValue) {
		final int propId = prop.getId();
		assert(propId == 840);
		mSidCached |= 0x2000;
		mChatMsg= (Message) newValue;
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final int newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 190:
			mSidCached |= 0x1;
			mType= Type.get(newValue);
			break;
		case 1211:
			mSidCached |= 0x2;
			mOutgoingReplyType= OutgoingReplyType.get(newValue);
			break;
		case 191:
			mSidCached |= 0x4;
			mStatus= Status.get(newValue);
			break;
		case 192:
			mSidCached |= 0x8;
			mFailureReason= FailureReason.get(newValue);
			break;
		case 48:
			mSidCached |= 0x10;
			mIsFailedUnseen= newValue != 0;
			break;
		case 193:
			mSidCached |= 0x40;
			mPrice=  newValue;
			break;
		case 49:
			mSidCached |= 0x80;
			mPricePrecision=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final byte[] newValue) {
		final int propId = prop.getId();
		assert(propId == 196);
		mSidCached |= 0x800;
		mTargetStatuses=  newValue;
	}
	public Type              mType;
	public OutgoingReplyType mOutgoingReplyType;
	public Status            mStatus;
	public FailureReason     mFailureReason;
	public boolean           mIsFailedUnseen;
	public long              mTimestamp;
	public int               mPrice;
	public int               mPricePrecision;
	public String            mPriceCurrency;
	public String            mReplyToNumber;
	public String            mTargetNumbers;
	public byte[]            mTargetStatuses;
	public String            mBody;
	public Message           mChatMsg;
	public int moduleId() {
		return 12;
	}
	
	public Sms(final int oid, final SidRoot root) {
		super(oid, root, 14);
	}
}
