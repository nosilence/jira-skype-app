package com.skype.api;

import com.skype.ipc.SidRoot;
import com.skype.ipc.SidObject;
import com.skype.ipc.EnumConverting;
import com.skype.ipc.PropertyEnumConverting;
import java.io.IOException;
import com.skype.api.ContactGroup;
import com.skype.api.Contact;
import com.skype.api.Conversation;
import com.skype.api.Message;
import com.skype.api.Video;
import com.skype.api.Sms;
import com.skype.ipc.Decoding;
import com.skype.api.Participant;

public class Skype extends SidRoot {
	public enum IdentityType implements EnumConverting {
		UNRECOGNIZED     (0),
		SKYPE            (1),
		SKYPE_MYSELF     (2),
		/** Not used */
		SKYPE_UNDISCLOSED(3),
		PSTN             (4),
		PSTN_EMERGENCY   (5),
		PSTN_FREE        (6),
		PSTN_UNDISCLOSED (7),
		/** Multi-party conversation identity */
		CONFERENCE       (8),
		EXTERNAL         (9);
		private final int key;
		IdentityType(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return UNRECOGNIZED; }
		public EnumConverting convert(int from) { return IdentityType.get(from); }
		public EnumConverting[] getArray(final int size) { return new IdentityType[size]; }
		public static IdentityType get(int from) {
			switch (from) {
			case 0: return UNRECOGNIZED;
			case 1: return SKYPE;
			case 2: return SKYPE_MYSELF;
			case 3: return SKYPE_UNDISCLOSED;
			case 4: return PSTN;
			case 5: return PSTN_EMERGENCY;
			case 6: return PSTN_FREE;
			case 7: return PSTN_UNDISCLOSED;
			case 8: return CONFERENCE;
			case 9: return EXTERNAL;
			}
			return UNRECOGNIZED;
		}
		public static final int UNRECOGNIZED_VALUE      = 0;
		public static final int SKYPE_VALUE             = 1;
		public static final int SKYPE_MYSELF_VALUE      = 2;
		public static final int SKYPE_UNDISCLOSED_VALUE = 3;
		public static final int PSTN_VALUE              = 4;
		public static final int PSTN_EMERGENCY_VALUE    = 5;
		public static final int PSTN_FREE_VALUE         = 6;
		public static final int PSTN_UNDISCLOSED_VALUE  = 7;
		public static final int CONFERENCE_VALUE        = 8;
		public static final int EXTERNAL_VALUE          = 9;
	}
	public enum NormalizeResult implements EnumConverting {
		IDENTITY_OK                   (0),
		IDENTITY_EMPTY                (1),
		IDENTITY_TOO_LONG             (2),
		IDENTITY_CONTAINS_INVALID_CHAR(3),
		PSTN_NUMBER_TOO_SHORT         (4),
		/** identity looks like pstn number but does not start with +/00/011 */
		PSTN_NUMBER_HAS_INVALID_PREFIX(5),
		SKYPENAME_STARTS_WITH_NONALPHA(6),
		/** returned only when isNewSkypeName */
		SKYPENAME_SHORTER_THAN_6_CHARS(7);
		private final int key;
		NormalizeResult(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return IDENTITY_OK; }
		public EnumConverting convert(int from) { return NormalizeResult.get(from); }
		public EnumConverting[] getArray(final int size) { return new NormalizeResult[size]; }
		public static NormalizeResult get(int from) {
			switch (from) {
			case 0: return IDENTITY_OK;
			case 1: return IDENTITY_EMPTY;
			case 2: return IDENTITY_TOO_LONG;
			case 3: return IDENTITY_CONTAINS_INVALID_CHAR;
			case 4: return PSTN_NUMBER_TOO_SHORT;
			case 5: return PSTN_NUMBER_HAS_INVALID_PREFIX;
			case 6: return SKYPENAME_STARTS_WITH_NONALPHA;
			case 7: return SKYPENAME_SHORTER_THAN_6_CHARS;
			}
			return IDENTITY_OK;
		}
		public static final int IDENTITY_OK_VALUE                    = 0;
		public static final int IDENTITY_EMPTY_VALUE                 = 1;
		public static final int IDENTITY_TOO_LONG_VALUE              = 2;
		public static final int IDENTITY_CONTAINS_INVALID_CHAR_VALUE = 3;
		public static final int PSTN_NUMBER_TOO_SHORT_VALUE          = 4;
		public static final int PSTN_NUMBER_HAS_INVALID_PREFIX_VALUE = 5;
		public static final int SKYPENAME_STARTS_WITH_NONALPHA_VALUE = 6;
		public static final int SKYPENAME_SHORTER_THAN_6_CHARS_VALUE = 7;
	}
	/** sync failure reasons when starting a transfer */
	public enum TransferSendfileError implements EnumConverting {
		TRANSFER_OPEN_SUCCESS     (0),
		TRANSFER_BAD_FILENAME     (1),
		TRANSFER_OPEN_FAILED      (2),
		TRANSFER_TOO_MANY_PARALLEL(3);
		private final int key;
		TransferSendfileError(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return TRANSFER_OPEN_SUCCESS; }
		public EnumConverting convert(int from) { return TransferSendfileError.get(from); }
		public EnumConverting[] getArray(final int size) { return new TransferSendfileError[size]; }
		public static TransferSendfileError get(int from) {
			switch (from) {
			case 0: return TRANSFER_OPEN_SUCCESS;
			case 1: return TRANSFER_BAD_FILENAME;
			case 2: return TRANSFER_OPEN_FAILED;
			case 3: return TRANSFER_TOO_MANY_PARALLEL;
			}
			return TRANSFER_OPEN_SUCCESS;
		}
		public static final int TRANSFER_OPEN_SUCCESS_VALUE      = 0;
		public static final int TRANSFER_BAD_FILENAME_VALUE      = 1;
		public static final int TRANSFER_OPEN_FAILED_VALUE       = 2;
		public static final int TRANSFER_TOO_MANY_PARALLEL_VALUE = 3;
	}
	/** Conversation and live state leave reasons for Participants. This type is used for the Participant.P_LAST_LEAVEREASON property. The primary use of this type is to provide detailed cause in case of a call failure.  */
	public enum LeaveReason implements EnumConverting {
		LEAVE_REASON_NONE                         (0),
		/** automatic, user cannot chat (only some older versions might set this) */
		RETIRED_USER_INCAPABLE                    (2),
		/** automatic */
		RETIRED_ADDER_MUST_BE_FRIEND              (3),
		/** automatic */
		RETIRED_ADDER_MUST_BE_AUTHORIZED          (4),
		/** manual reason (afaik no UI uses this) */
		RETIRED_DECLINE_ADD                       (5),
		/** manual reason */
		RETIRED_UNSUBSCRIBE                       (6),
		LIVE_NO_ANSWER                            (100),
		/** live: User hung up */
		LIVE_MANUAL                               (101),
		LIVE_BUSY                                 (102),
		LIVE_CONNECTION_DROPPED                   (103),
		LIVE_NO_SKYPEOUT_SUBSCRIPTION             (104),
		LIVE_INSUFFICIENT_FUNDS                   (105),
		LIVE_INTERNET_CONNECTION_LOST             (106),
		LIVE_SKYPEOUT_ACCOUNT_BLOCKED             (107),
		LIVE_PSTN_COULD_NOT_CONNECT_TO_SKYPE_PROXY(108),
		LIVE_PSTN_INVALID_NUMBER                  (109),
		LIVE_PSTN_NUMBER_FORBIDDEN                (110),
		LIVE_PSTN_CALL_TIMED_OUT                  (111),
		LIVE_PSTN_BUSY                            (112),
		LIVE_PSTN_CALL_TERMINATED                 (113),
		LIVE_PSTN_NETWORK_ERROR                   (114),
		LIVE_NUMBER_UNAVAILABLE                   (115),
		LIVE_PSTN_CALL_REJECTED                   (116),
		LIVE_PSTN_MISC_ERROR                      (117),
		LIVE_INTERNAL_ERROR                       (118),
		LIVE_UNABLE_TO_CONNECT                    (119),
		/** live: Voicemail recording failed */
		LIVE_RECORDING_FAILED                     (120),
		/** live: Voicemail playback failed */
		LIVE_PLAYBACK_ERROR                       (121),
		LIVE_LEGACY_ERROR                         (122),
		LIVE_BLOCKED_BY_PRIVACY_SETTINGS          (123),
		/** live: Fallback error */
		LIVE_ERROR                                (124),
		LIVE_TRANSFER_FAILED                      (125),
		LIVE_TRANSFER_INSUFFICIENT_FUNDS          (126),
		LIVE_BLOCKED_BY_US                        (127),
		LIVE_EMERGENCY_CALL_DENIED                (128);
		private final int key;
		LeaveReason(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return LEAVE_REASON_NONE; }
		public EnumConverting convert(int from) { return LeaveReason.get(from); }
		public EnumConverting[] getArray(final int size) { return new LeaveReason[size]; }
		public static LeaveReason get(int from) {
			switch (from) {
			case   0: return LEAVE_REASON_NONE;
			case   2: return RETIRED_USER_INCAPABLE;
			case   3: return RETIRED_ADDER_MUST_BE_FRIEND;
			case   4: return RETIRED_ADDER_MUST_BE_AUTHORIZED;
			case   5: return RETIRED_DECLINE_ADD;
			case   6: return RETIRED_UNSUBSCRIBE;
			case 100: return LIVE_NO_ANSWER;
			case 101: return LIVE_MANUAL;
			case 102: return LIVE_BUSY;
			case 103: return LIVE_CONNECTION_DROPPED;
			case 104: return LIVE_NO_SKYPEOUT_SUBSCRIPTION;
			case 105: return LIVE_INSUFFICIENT_FUNDS;
			case 106: return LIVE_INTERNET_CONNECTION_LOST;
			case 107: return LIVE_SKYPEOUT_ACCOUNT_BLOCKED;
			case 108: return LIVE_PSTN_COULD_NOT_CONNECT_TO_SKYPE_PROXY;
			case 109: return LIVE_PSTN_INVALID_NUMBER;
			case 110: return LIVE_PSTN_NUMBER_FORBIDDEN;			case 111: return LIVE_PSTN_CALL_TIMED_OUT;
			case 112: return LIVE_PSTN_BUSY;
			case 113: return LIVE_PSTN_CALL_TERMINATED;
			case 114: return LIVE_PSTN_NETWORK_ERROR;
			case 115: return LIVE_NUMBER_UNAVAILABLE;
			case 116: return LIVE_PSTN_CALL_REJECTED;
			case 117: return LIVE_PSTN_MISC_ERROR;
			case 118: return LIVE_INTERNAL_ERROR;
			case 119: return LIVE_UNABLE_TO_CONNECT;
			case 120: return LIVE_RECORDING_FAILED;
			case 121: return LIVE_PLAYBACK_ERROR;
			case 122: return LIVE_LEGACY_ERROR;
			case 123: return LIVE_BLOCKED_BY_PRIVACY_SETTINGS;
			case 124: return LIVE_ERROR;
			case 125: return LIVE_TRANSFER_FAILED;
			case 126: return LIVE_TRANSFER_INSUFFICIENT_FUNDS;
			case 127: return LIVE_BLOCKED_BY_US;
			case 128: return LIVE_EMERGENCY_CALL_DENIED;
			}
			return LEAVE_REASON_NONE;
		}
		public static final int LEAVE_REASON_NONE_VALUE                          =   0;
		public static final int RETIRED_USER_INCAPABLE_VALUE                     =   2;
		public static final int RETIRED_ADDER_MUST_BE_FRIEND_VALUE               =   3;
		public static final int RETIRED_ADDER_MUST_BE_AUTHORIZED_VALUE           =   4;
		public static final int RETIRED_DECLINE_ADD_VALUE                        =   5;
		public static final int RETIRED_UNSUBSCRIBE_VALUE                        =   6;
		public static final int LIVE_NO_ANSWER_VALUE                             = 100;
		public static final int LIVE_MANUAL_VALUE                                = 101;
		public static final int LIVE_BUSY_VALUE                                  = 102;
		public static final int LIVE_CONNECTION_DROPPED_VALUE                    = 103;
		public static final int LIVE_NO_SKYPEOUT_SUBSCRIPTION_VALUE              = 104;
		public static final int LIVE_INSUFFICIENT_FUNDS_VALUE                    = 105;
		public static final int LIVE_INTERNET_CONNECTION_LOST_VALUE              = 106;
		public static final int LIVE_SKYPEOUT_ACCOUNT_BLOCKED_VALUE              = 107;
		public static final int LIVE_PSTN_COULD_NOT_CONNECT_TO_SKYPE_PROXY_VALUE = 108;
		public static final int LIVE_PSTN_INVALID_NUMBER_VALUE                   = 109;
		public static final int LIVE_PSTN_NUMBER_FORBIDDEN_VALUE                 = 110;
		public static final int LIVE_PSTN_CALL_TIMED_OUT_VALUE                   = 111;
		public static final int LIVE_PSTN_BUSY_VALUE                             = 112;
		public static final int LIVE_PSTN_CALL_TERMINATED_VALUE                  = 113;
		public static final int LIVE_PSTN_NETWORK_ERROR_VALUE                    = 114;
		public static final int LIVE_NUMBER_UNAVAILABLE_VALUE                    = 115;
		public static final int LIVE_PSTN_CALL_REJECTED_VALUE                    = 116;
		public static final int LIVE_PSTN_MISC_ERROR_VALUE                       = 117;
		public static final int LIVE_INTERNAL_ERROR_VALUE                        = 118;
		public static final int LIVE_UNABLE_TO_CONNECT_VALUE                     = 119;
		public static final int LIVE_RECORDING_FAILED_VALUE                      = 120;
		public static final int LIVE_PLAYBACK_ERROR_VALUE                        = 121;
		public static final int LIVE_LEGACY_ERROR_VALUE                          = 122;
		public static final int LIVE_BLOCKED_BY_PRIVACY_SETTINGS_VALUE           = 123;
		public static final int LIVE_ERROR_VALUE                                 = 124;
		public static final int LIVE_TRANSFER_FAILED_VALUE                       = 125;
		public static final int LIVE_TRANSFER_INSUFFICIENT_FUNDS_VALUE           = 126;
		public static final int LIVE_BLOCKED_BY_US_VALUE                         = 127;
		public static final int LIVE_EMERGENCY_CALL_DENIED_VALUE                 = 128;
	}
	public enum QualityTestType implements EnumConverting {
		QTT_AUDIO_IN (0),
		QTT_AUDIO_OUT(1),
		QTT_VIDEO_OUT(2),
		QTT_CPU      (3),
		QTT_NETWORK  (4),
		QTT_VIDEO_IN (5);
		private final int key;
		QualityTestType(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return QTT_AUDIO_IN; }
		public EnumConverting convert(int from) { return QualityTestType.get(from); }
		public EnumConverting[] getArray(final int size) { return new QualityTestType[size]; }
		public static QualityTestType get(int from) {
			switch (from) {
			case 0: return QTT_AUDIO_IN;
			case 1: return QTT_AUDIO_OUT;
			case 2: return QTT_VIDEO_OUT;
			case 3: return QTT_CPU;
			case 4: return QTT_NETWORK;
			case 5: return QTT_VIDEO_IN;
			}
			return QTT_AUDIO_IN;
		}
		public static final int QTT_AUDIO_IN_VALUE  = 0;
		public static final int QTT_AUDIO_OUT_VALUE = 1;
		public static final int QTT_VIDEO_OUT_VALUE = 2;
		public static final int QTT_CPU_VALUE       = 3;
		public static final int QTT_NETWORK_VALUE   = 4;
		public static final int QTT_VIDEO_IN_VALUE  = 5;
	}
	public enum QualityTestResult implements EnumConverting {
		QTR_UNDEFINED(0),
		QTR_CRITICAL (1),
		QTR_POOR     (2),
		QTR_AVERAGE  (3),
		QTR_GOOD     (4),
		QTR_EXCELLENT(5);
		private final int key;
		QualityTestResult(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return QTR_UNDEFINED; }
		public EnumConverting convert(int from) { return QualityTestResult.get(from); }
		public EnumConverting[] getArray(final int size) { return new QualityTestResult[size]; }
		public static QualityTestResult get(int from) {
			switch (from) {
			case 0: return QTR_UNDEFINED;
			case 1: return QTR_CRITICAL;
			case 2: return QTR_POOR;
			case 3: return QTR_AVERAGE;
			case 4: return QTR_GOOD;
			case 5: return QTR_EXCELLENT;
			}
			return QTR_UNDEFINED;
		}
		public static final int QTR_UNDEFINED_VALUE = 0;
		public static final int QTR_CRITICAL_VALUE  = 1;
		public static final int QTR_POOR_VALUE      = 2;
		public static final int QTR_AVERAGE_VALUE   = 3;
		public static final int QTR_GOOD_VALUE      = 4;
		public static final int QTR_EXCELLENT_VALUE = 5;
	}
	public enum PrepareSoundResult implements EnumConverting {
		PREPARESOUND_SUCCESS                (0),
		PREPARESOUND_MISC_ERROR             (1),
		PREPARESOUND_FILE_NOT_FOUND         (2),
		PREPARESOUND_FILE_TOO_BIG           (3),
		PREPARESOUND_FILE_READ_ERROR        (4),
		PREPARESOUND_UNSUPPORTED_FILE_FORMAT(5),
		PREPARESOUND_PLAYBACK_NOT_SUPPORTED (6);
		private final int key;
		PrepareSoundResult(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return PREPARESOUND_SUCCESS; }
		public EnumConverting convert(int from) { return PrepareSoundResult.get(from); }
		public EnumConverting[] getArray(final int size) { return new PrepareSoundResult[size]; }
		public static PrepareSoundResult get(int from) {
			switch (from) {
			case 0: return PREPARESOUND_SUCCESS;
			case 1: return PREPARESOUND_MISC_ERROR;
			case 2: return PREPARESOUND_FILE_NOT_FOUND;
			case 3: return PREPARESOUND_FILE_TOO_BIG;
			case 4: return PREPARESOUND_FILE_READ_ERROR;
			case 5: return PREPARESOUND_UNSUPPORTED_FILE_FORMAT;
			case 6: return PREPARESOUND_PLAYBACK_NOT_SUPPORTED;
			}
			return PREPARESOUND_SUCCESS;
		}
		public static final int PREPARESOUND_SUCCESS_VALUE                 = 0;
		public static final int PREPARESOUND_MISC_ERROR_VALUE              = 1;
		public static final int PREPARESOUND_FILE_NOT_FOUND_VALUE          = 2;
		public static final int PREPARESOUND_FILE_TOO_BIG_VALUE            = 3;
		public static final int PREPARESOUND_FILE_READ_ERROR_VALUE         = 4;
		public static final int PREPARESOUND_UNSUPPORTED_FILE_FORMAT_VALUE = 5;
		public static final int PREPARESOUND_PLAYBACK_NOT_SUPPORTED_VALUE  = 6;
	}
	public enum AudioDeviceCapabilities implements EnumConverting {
		HAS_VIDEO_CAPTURE       (1),
		HAS_USB_INTERFACE       (2),
		POSSIBLY_HEADSET        (4),
		HAS_AUDIO_CAPTURE       (8),
		HAS_AUDIO_RENDERING     (16),
		HAS_LOWBANDWIDTH_CAPTURE(32),
		IS_WEBCAM               (64),
		IS_HEADSET              (128),
		POSSIBLY_WEBCAM         (256),
		HAS_VIDEO_RENDERING     (2048),
		HAS_BLUETOOTH_INTERFACE (4096);
		private final int key;
		AudioDeviceCapabilities(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return HAS_VIDEO_CAPTURE; }		public EnumConverting convert(int from) { return AudioDeviceCapabilities.get(from); }
		public EnumConverting[] getArray(final int size) { return new AudioDeviceCapabilities[size]; }
		public static AudioDeviceCapabilities get(int from) {
			switch (from) {
			case    1: return HAS_VIDEO_CAPTURE;
			case    2: return HAS_USB_INTERFACE;
			case    4: return POSSIBLY_HEADSET;
			case    8: return HAS_AUDIO_CAPTURE;
			case   16: return HAS_AUDIO_RENDERING;
			case   32: return HAS_LOWBANDWIDTH_CAPTURE;
			case   64: return IS_WEBCAM;
			case  128: return IS_HEADSET;
			case  256: return POSSIBLY_WEBCAM;
			case 2048: return HAS_VIDEO_RENDERING;
			case 4096: return HAS_BLUETOOTH_INTERFACE;
			}
			return HAS_VIDEO_CAPTURE;
		}
		public static final int HAS_VIDEO_CAPTURE_VALUE        =    1;
		public static final int HAS_USB_INTERFACE_VALUE        =    2;
		public static final int POSSIBLY_HEADSET_VALUE         =    4;
		public static final int HAS_AUDIO_CAPTURE_VALUE        =    8;
		public static final int HAS_AUDIO_RENDERING_VALUE      =   16;
		public static final int HAS_LOWBANDWIDTH_CAPTURE_VALUE =   32;
		public static final int IS_WEBCAM_VALUE                =   64;
		public static final int IS_HEADSET_VALUE               =  128;
		public static final int POSSIBLY_WEBCAM_VALUE          =  256;
		public static final int HAS_VIDEO_RENDERING_VALUE      = 2048;
		public static final int HAS_BLUETOOTH_INTERFACE_VALUE  = 4096;
	}
	public enum OperatingMedia implements EnumConverting {
		OM_UNKNOWN      (0),
		OM_FREE         (1),
		OM_FREE_WIRELESS(2),
		OM_3G           (3),
		OM_4G           (4);
		private final int key;
		OperatingMedia(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return OM_UNKNOWN; }
		public EnumConverting convert(int from) { return OperatingMedia.get(from); }
		public EnumConverting[] getArray(final int size) { return new OperatingMedia[size]; }
		public static OperatingMedia get(int from) {
			switch (from) {
			case 0: return OM_UNKNOWN;
			case 1: return OM_FREE;
			case 2: return OM_FREE_WIRELESS;
			case 3: return OM_3G;
			case 4: return OM_4G;
			}
			return OM_UNKNOWN;
		}
		public static final int OM_UNKNOWN_VALUE       = 0;
		public static final int OM_FREE_VALUE          = 1;
		public static final int OM_FREE_WIRELESS_VALUE = 2;
		public static final int OM_3G_VALUE            = 3;
		public static final int OM_4G_VALUE            = 4;
	}
	/** A value of this type can be returned by one of the following methods (of Skype class): ValidateAvatar, ValidateProfileString, ValidatePassword.   */
	public enum Validateresult implements EnumConverting {
		/** Given property could not be validated. The length of the field was within limits and the value is assumed to be Ok. Your client should treat this value as equivalent to VALIDATED_OK.  */
		NOT_VALIDATED           (0),
		/** Avatar or profile string validation succeeded.  */
		VALIDATED_OK            (1),
		/** Password is too short.  */
		TOO_SHORT               (2),
		/** The value exceeds max size limit for the given property.  */
		TOO_LONG                (3),
		/** Value contains illegal characters.  */
		CONTAINS_INVALID_CHAR   (4),
		/** Value contains whitespace.  */
		CONTAINS_SPACE          (5),
		/** Password cannot be the same as skypename.  */
		SAME_AS_USERNAME        (6),
		/** Value has invalid format.  */
		INVALID_FORMAT          (7),
		/** Value contains invalid word.  */
		CONTAINS_INVALID_WORD   (8),
		/** Password is too simple.  */
		TOO_SIMPLE              (9),
		/** Value starts with an invalid character.  */
		STARTS_WITH_INVALID_CHAR(10);
		private final int key;
		Validateresult(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return NOT_VALIDATED; }
		public EnumConverting convert(int from) { return Validateresult.get(from); }
		public EnumConverting[] getArray(final int size) { return new Validateresult[size]; }
		public static Validateresult get(int from) {
			switch (from) {
			case  0: return NOT_VALIDATED;
			case  1: return VALIDATED_OK;
			case  2: return TOO_SHORT;
			case  3: return TOO_LONG;
			case  4: return CONTAINS_INVALID_CHAR;
			case  5: return CONTAINS_SPACE;
			case  6: return SAME_AS_USERNAME;
			case  7: return INVALID_FORMAT;
			case  8: return CONTAINS_INVALID_WORD;
			case  9: return TOO_SIMPLE;
			case 10: return STARTS_WITH_INVALID_CHAR;
			}
			return NOT_VALIDATED;
		}
		public static final int NOT_VALIDATED_VALUE            =  0;
		public static final int VALIDATED_OK_VALUE             =  1;
		public static final int TOO_SHORT_VALUE                =  2;
		public static final int TOO_LONG_VALUE                 =  3;
		public static final int CONTAINS_INVALID_CHAR_VALUE    =  4;
		public static final int CONTAINS_SPACE_VALUE           =  5;
		public static final int SAME_AS_USERNAME_VALUE         =  6;
		public static final int INVALID_FORMAT_VALUE           =  7;
		public static final int CONTAINS_INVALID_WORD_VALUE    =  8;
		public static final int TOO_SIMPLE_VALUE               =  9;
		public static final int STARTS_WITH_INVALID_CHAR_VALUE = 10;
	}
	public enum ProxyType implements EnumConverting {
		HTTPS_PROXY(0),
		SOCKS_PROXY(1);
		private final int key;
		ProxyType(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return HTTPS_PROXY; }
		public EnumConverting convert(int from) { return ProxyType.get(from); }
		public EnumConverting[] getArray(final int size) { return new ProxyType[size]; }
		public static ProxyType get(int from) {
			switch (from) {
			case 0: return HTTPS_PROXY;
			case 1: return SOCKS_PROXY;
			}
			return HTTPS_PROXY;
		}
		public static final int HTTPS_PROXY_VALUE = 0;
		public static final int SOCKS_PROXY_VALUE = 1;
	}
	public enum App2AppStreams implements EnumConverting {
		ALL_STREAMS     (0),
		SENDING_STREAMS (1),
		RECEIVED_STREAMS(2);
		private final int key;
		App2AppStreams(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return ALL_STREAMS; }
		public EnumConverting convert(int from) { return App2AppStreams.get(from); }
		public EnumConverting[] getArray(final int size) { return new App2AppStreams[size]; }
		public static App2AppStreams get(int from) {
			switch (from) {
			case 0: return ALL_STREAMS;
			case 1: return SENDING_STREAMS;
			case 2: return RECEIVED_STREAMS;
			}
			return ALL_STREAMS;
		}
		public static final int ALL_STREAMS_VALUE      = 0;
		public static final int SENDING_STREAMS_VALUE  = 1;
		public static final int RECEIVED_STREAMS_VALUE = 2;
	}
	/** Setupkey SETUPKEY_DB_STORAGE_QUOTA_KB type:int default value:"0" <br>Use this key to limit the size of the main.db file. Value is in KB. Quota are disabled by default. <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String DB_STORAGE_QUOTA_KB = "*Lib/DbManager/StorageQuotaKb";
	
	/** Setupkey SETUPKEY_DB_PAGE_SIZE type:int default value:"4096" Page size of the databases. Value is in bytes, the default is 4096. */
	public static final String DB_PAGE_SIZE = "*Lib/DbManager/PageSize";
	
	/** Setupkey SETUPKEY_DISABLED_CODECS type:string  <br>Space-separated array of disabled codecs <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String DISABLED_CODECS = "*Lib/Audio/DisableCodecs";
	
	/** Setupkey SETUPKEY_DISABLE_AEC type:boolean  <br>Disables Skype echo canceller <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String DISABLE_AEC = "*Lib/Audio/DisableAEC";
	
	/** Setupkey SETUPKEY_DISABLE_NOISE_SUPPRESSOR type:boolean  <br>Disables Skype noise suppressor <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String DISABLE_NOISE_SUPPRESSOR = "*Lib/Audio/DisableNS";
	
	/** Setupkey SETUPKEY_DISABLE_AGC type:boolean  Disables Skype automatic gain controller <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String DISABLE_AGC = "*Lib/Audio/DisableAGC";	
	/** Setupkey SETUPKEY_DISABLE_DIGITAL_NEAR_END_AGC type:boolean  <br>Disables Skype digital near-end gain controller <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String DISABLE_DIGITAL_NEAR_END_AGC = "*Lib/Audio/DisableDigitalNearEndAGC";
	
	/** Setupkey SETUPKEY_DISABLE_DIGITAL_FAR_END_AGC type:boolean  <br>Disables Skype digital far-end gain controller <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String DISABLE_DIGITAL_FAR_END_AGC = "*Lib/Audio/DisableDigitalFarEndAGC";
	
	/** Setupkey SETUPKEY_BEAMFORMER_MIC_SPACING type:string  <br>Space-separated array of 1 (in case of 2 microphones) or 2 (in case of 4 microphones) integers. SAL beamforming currently only supports 2 and 4-microphone configurations. The values represent the spacing between microphones (in millimeters). <br>In case of 2-microphone setup, Only the first value is used. <br><br>In case of 4-microphone setup, The first value is the distance between inner pair of microphones. The second value is the distance between inner pair of microphones and the outer pair. Like this: <br><br>Let the microphones be on straight line, A B C D. <br>Microphones B and C form the inner pair, while A and D form the outer pair. <br>The first value in the setup string would be distance between B and C. <br>The second value would be distance between A and B (which is the same as distance between C and D). <br><br>With 4-mic setup, you will need to use two channels. The inner pair should go to one channel (left) and the outer pair should go to another (right). <br><br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String BEAMFORMER_MIC_SPACING = "*Lib/Audio/BeamformerMicSpacing";
	
	/** Setupkey SETUPKEY_DISABLE_AUDIO_DEVICE_PROBING type:boolean  <br>Disables audio devices probing <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String DISABLE_AUDIO_DEVICE_PROBING = "*Lib/QualityMonitor/DisableAudioDeviceProbing";
	
	/** Setupkey SETUPKEY_FT_AUTOACCEPT type:int  <br>Controls file transfer auto-accept.  <br> - 0 - off <br> - 1 - on <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String FT_AUTOACCEPT = "Lib/FileTransfer/AutoAccept";
	
	/** Setupkey SETUPKEY_FT_SAVEPATH type:string  <br>Full local path to save incoming file transfers (used for AutoAccept feature) <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String FT_SAVEPATH = "Lib/FileTransfer/SavePath";
	
	/** Setupkey SETUPKEY_FT_INCOMING_LIMIT type:uint  <br>Number of simultaneous incoming file transfers (per user). Value 0 means no limitation.  <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String FT_INCOMING_LIMIT = "Lib/FileTransfer/IncomingLimit";
	
	/** Setupkey SETUPKEY_IDLE_TIME_FOR_AWAY type:int  <br>Number of seconds since the last keyboard or mouse activity, after which the online status of currently logged in account should be set to AWAY. See Account.SetAvailability method for more information. <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String IDLE_TIME_FOR_AWAY = "Lib/Account/IdleTimeForAway";
	
	/** Setupkey SETUPKEY_IDLE_TIME_FOR_NA type:int  <br>The Contact.AVAILABILITY.NOT_AVAILABLE online status has been deprecated. This setup key is no longer in use. <br> */
	public static final String IDLE_TIME_FOR_NA = "Lib/Account/IdleTimeForNA";
	
	/** Setupkey SETUPKEY_PORT type:int  <br>Suggested port number (lib will *try* to use that) <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String PORT = "*Lib/Connection/Port";
	
	/** Setupkey SETUPKEY_HTTPS_PROXY_ENABLE type:int  <br>Set to 0 for automatic proxy detect, 1 to use proxy config below <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String HTTPS_PROXY_ENABLE = "*Lib/Connection/HttpsProxy/Enable";
	
	/** Setupkey SETUPKEY_HTTPS_PROXY_ADDR type:string  <br>name:port of HTTP proxy server <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String HTTPS_PROXY_ADDR = "*Lib/Connection/HttpsProxy/Addr";
	
	/** Setupkey SETUPKEY_HTTPS_PROXY_USER type:string  <br>HTTPS proxy server username <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String HTTPS_PROXY_USER = "*Lib/Connection/HttpsProxy/User";
	
	/** Setupkey SETUPKEY_HTTPS_PROXY_PWD type:string  <br>HTTPS proxy server password (base64 encoded) <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String HTTPS_PROXY_PWD = "*Lib/Connection/HttpsProxy/Pwd";
	
	/** Setupkey SETUPKEY_SOCKS_PROXY_ENABLE type:int  <br>Set to non-zero to enable socks proxy support <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String SOCKS_PROXY_ENABLE = "*Lib/Connection/SocksProxy/Enable";
	
	/** Setupkey SETUPKEY_SOCKS_PROXY_ADDR type:string  <br>name:port of SOCKS proxy server <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String SOCKS_PROXY_ADDR = "*Lib/Connection/SocksProxy/Addr";
	
	/** Setupkey SETUPKEY_SOCKS_PROXY_USER type:string  <br>SOCKS proxy server username <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String SOCKS_PROXY_USER = "*Lib/Connection/SocksProxy/User";
	
	/** Setupkey SETUPKEY_SOCKS_PROXY_PWD type:string  <br>SOCKS proxy server password (base64 encoded) <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String SOCKS_PROXY_PWD = "*Lib/Connection/SocksProxy/Pwd";
	
	/** Setupkey SETUPKEY_LOCALADDRESS type:string  <br>local interface to listen to <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String LOCALADDRESS = "*Lib/Connection/LocalAddress";
	
	/** Setupkey SETUPKEY_DISABLE_PORT80 type:int  <br>1 disables listening of alternative ports (80, 443) <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String DISABLE_PORT80 = "*Lib/Connection/DisablePort80";
	
	/** Setupkey SETUPKEY_DISABLE_UDP type:int  <br>1 disables UDP port binding. should be set before connect <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String DISABLE_UDP = "*Lib/Connection/DisableUDP";
	
	private final static byte[] getVersionString_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 28};
	/** returns the runtime version as a string
	 * @return version
	 */
	public String getVersionString() {
		try {
			return sidDoRequest(getVersionString_req)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] getUnixTimestamp_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 134,(byte) 1};
	/** Returns the time as used in SkypeKit, in the form of a Unix timestamp (number of seconds since 1.1.1970).                   If the local system time is incorrect my more than one year, the time provided                   by the Skype network will be provided, which is correct. Therefore this function                   can be used to adjust the system time if set incorrectly (e.g. if set to 1.1.1970).
	 * @return timestamp
	 */
	public long getUnixTimestamp() {
		try {
			return sidDoRequest(getUnixTimestamp_req)
			.endRequest().getTimestampParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return 0;
		}
	}
	private final static byte[] start_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 145,(byte) 1};
	/**
	 * start
	 * @return started
	 */
	public boolean start() {
		try {
			return sidDoRequest(start_req)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;		}
	}
	private final static byte[] getHardwiredContactGroup_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 1};
	/** Takes TYPE argument (TYPE comes from ContactGroup class) and returns reference to the corresponding hardwired contact group. For example (C++): skype->GetHardwiredContactGroup(ContactGroup.ONLINE_BUDDIES, GroupRef) would return the list of all contacts that are currently online. 
	 * @param type
	 * @return contactGroup
	 */
	public ContactGroup getHardwiredContactGroup(ContactGroup.Type type) {
		try {
			return (ContactGroup) sidDoRequest(getHardwiredContactGroup_req)
			.addEnumParm(1, type)
			.endRequest().getObjectParm(1, 10, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getCustomContactGroups_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 2};
	/** Returns a list of custom contact group references, i.e. all contact groups that are not hardwired. 
	 * @return groups
	 */
	public ContactGroup[] getCustomContactGroups() {
		try {
			return (ContactGroup[]) sidDoRequest(getCustomContactGroups_req)
			.endRequest().getObjectListParm(1, 10, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] createCustomContactGroup_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 3};
	/**
	 * Creates a new empty contact group object and returns a reference to it. The group will then show up in the custom group list that you can get with Skype class GetCustomContactGroups method. Existing contacts can be added to the new group with ContactGroup class AddContact method and a custom name can be given to it with GiveDisplayName method. 
	 * Note that no check is made for existing of displaynames with the same name - if you wish to force uniqueness in custom group names you will have to check that yourself before creating the group. 
	
	 * @return group
	 */
	public ContactGroup createCustomContactGroup() {
		try {
			return (ContactGroup) sidDoRequest(createCustomContactGroup_req)
			.endRequest().getObjectParm(1, 10, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getContactType_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 5};
	/** analyzes the identity for contact type
	 * @param identity
	 * @return type
	 */
	public Contact.Type getContactType(String identity) {
		try {
			return (Contact.Type) sidDoRequest(getContactType_req)
			.addStringParm(1, identity)
			.endRequest().getEnumParm(1, Contact.Type.get(0), true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return Contact.Type.get(0);
		}
	}
	private final static byte[] getContact_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 6};
	/** Returns a Contact object reference. If a matching contact is not found in the existing contact list, a new Contact object will be created. Note that if you pass in a phone number in the identity argument, the type for the newly created Contact will be automatically set to Contact.PSTN (Contact.SKYPE otherwise). 
	 * @param identity Either skypename or a phone number 
	 * @return contact Returns a contact object. 
	 */
	public Contact getContact(String identity) {
		try {
			return (Contact) sidDoRequest(getContact_req)
			.addStringParm(1, identity)
			.endRequest().getObjectParm(2, 2, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] findContactByPstnNumber_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 8};
	public class FindContactByPstnNumberResponse {
		public boolean found;
		public Contact contact;
		public int foundInKey;
	};
	
	/**
	 * findContactByPstnNumber
	 * @param number
	 * @return FindContactByPstnNumberResponse
	 * <br> - found
	 * <br> - contact
	 * <br> - foundInKey type is actually PROPKEY
	 */
	public FindContactByPstnNumberResponse findContactByPstnNumber(String number) {
		try {
			Decoding decoder = sidDoRequest(findContactByPstnNumber_req)
			.addStringParm(1, number)
			.endRequest();
			FindContactByPstnNumberResponse result = new FindContactByPstnNumberResponse();
			result.found = decoder.getBoolParm(1, false);
			result.contact = (Contact) decoder.getObjectParm(2, 2, false);
			result.foundInKey = decoder.getUintParm(3, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getIdentityType_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 19};
	/** This takes skypename or a phone number string as argument and returns corresponding identity type (SKYPE, SKYPE_MYSELF, PSTN, etc.) 
	 * @param identity
	 * @return type
	 */
	public IdentityType getIdentityType(String identity) {
		try {
			return (IdentityType) sidDoRequest(getIdentityType_req)
			.addStringParm(1, identity)
			.endRequest().getEnumParm(1, IdentityType.get(0), true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return IdentityType.get(0);
		}
	}
	private final static byte[] identitiesMatch_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 88};
	/** compares two identities to see if they match
	 * @param identityA
	 * @param identityB
	 * @return result
	 */
	public boolean identitiesMatch(String identityA, String identityB) {
		try {
			return sidDoRequest(identitiesMatch_req)
			.addStringParm(1, identityA)
			.addStringParm(2, identityB)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] normalizeIdentity_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 9};
	public class NormalizeIdentityResponse {
		public NormalizeResult result;
		public String normalized;
	};
	
	/** This method is deprecated. Use ValidateProfileString method instead. 
	 * @param original
	 * @param isNewSkypeName
	 * @return NormalizeIdentityResponse
	 * <br> - result
	 * <br> - normalized
	 */
	public NormalizeIdentityResponse normalizeIdentity(String original, boolean isNewSkypeName) {
		try {
			Decoding decoder = sidDoRequest(normalizeIdentity_req)
			.addStringParm(1, original)
			.addBoolParm(2, isNewSkypeName)
			.endRequest();
			NormalizeIdentityResponse result = new NormalizeIdentityResponse();
			result.result = (NormalizeResult) decoder.getEnumParm(1, NormalizeResult.get(0), false);
			result.normalized = decoder.getStringParm(2, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] normalizePstnWithCountry_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 205,(byte) 1};
	public class NormalizePstnWithCountryResponse {
		public NormalizeResult result;
		public String normalized;
	};
	
	/** NormalizePSTNWithCountry checks if the phone number starts with + if it doesn't, it prefixes the output with +XXX (where XXX is the country code). It also converts letters to numbers based on the standard phone keypad, so that the phone number string 212CALLME1 with country code 372 (Estonia) would be normalized to +3722122255631. If the method cannot normalize the phone number (because it's too long, too short, etc.), it returns an error code in &result. 
	 * @param original
	 * @param countryPrefix
	 * @return NormalizePstnWithCountryResponse
	 * <br> - result
	 * <br> - normalized
	 */
	public NormalizePstnWithCountryResponse normalizePstnWithCountry(String original, int countryPrefix) {
		try {
			Decoding decoder = sidDoRequest(normalizePstnWithCountry_req)
			.addStringParm(1, original)
			.addUintParm(2, countryPrefix)
			.endRequest();
			NormalizePstnWithCountryResponse result = new NormalizePstnWithCountryResponse();
			result.result = (NormalizeResult) decoder.getEnumParm(1, NormalizeResult.get(0), false);
			result.normalized = decoder.getStringParm(2, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getOptimalAgeRanges_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 77};
	/** list of (min,max) pairs
	 * @return rangeList
	 */
	public int[] getOptimalAgeRanges() {
		try {
			return sidDoRequest(getOptimalAgeRanges_req)
			.endRequest().getUintListParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] createContactSearch_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 10};	/** Creates a blank contact search object, in which you can add your custom search terms. For more information how asynchronous contact search works, see ContactSearch class details. 
	 * @return search Returns blank ContactSearch object. 
	 */
	public ContactSearch createContactSearch() {
		try {
			return (ContactSearch) sidDoRequest(createContactSearch_req)
			.endRequest().getObjectParm(1, 1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] createBasicContactSearch_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 11};
	/** searches skypenames, aliases, fullnames, emails. false if not valid
	 * @param text
	 * @return search
	 */
	public ContactSearch createBasicContactSearch(String text) {
		try {
			return (ContactSearch) sidDoRequest(createBasicContactSearch_req)
			.addStringParm(1, text)
			.endRequest().getObjectParm(1, 1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] createIdentitySearch_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 12};
	/** searches skypenames and aliases. returns 0 or 1 results. false if not valid
	 * @param identity
	 * @return search
	 */
	public ContactSearch createIdentitySearch(String identity) {
		try {
			return (ContactSearch) sidDoRequest(createIdentitySearch_req)
			.addStringParm(1, identity)
			.endRequest().getObjectParm(1, 1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] createConference_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 13};
	/** Creates a new empty conversation object and returns a reference to it. 
	 * @return conference
	 */
	public Conversation createConference() {
		try {
			return (Conversation) sidDoRequest(createConference_req)
			.endRequest().getObjectParm(1, 18, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getConversationByIdentity_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 15};
	/** Returns reference tp conversation object by conversation ID string (equivalent of old chat ID). NB! ID here is that of conversation, rather than skypename of dialog partner. If you want to retrieve a conversation object with any particular person, then Skype class GetConversationByParticipants method is what you are looking for. 
	 * @param convoIdentity
	 * @param matchPstn The matchPSTN parameter changes the behaviour of the method for PSTN numbers.                     When matchPSTN is true, the method will look if there is a skypename that has this PSTN number associated                     (in his profile or as user-assigned phone number), and if there is, the method returns the conversation with that skypename.                     When matchPSTN is false, the method will simply return the conversation whose identity is that PSTN number.
	 * @return conversation
	 */
	public Conversation getConversationByIdentity(String convoIdentity, boolean matchPstn) {
		try {
			return (Conversation) sidDoRequest(getConversationByIdentity_req)
			.addStringParm(1, convoIdentity)
			.addBoolParm(2, matchPstn, true)
			.endRequest().getObjectParm(1, 18, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getConversationByParticipants_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 16};
	/** myself not included
	 * @param participantIdentities
	 * @param createIfNonExisting
	 * @param ignoreBookmarkedOrNamed
	 * @return conversation
	 */
	public Conversation getConversationByParticipants(String[] participantIdentities, boolean createIfNonExisting, boolean ignoreBookmarkedOrNamed) {
		try {
			return (Conversation) sidDoRequest(getConversationByParticipants_req)
			.addStringListParm(1, participantIdentities)
			.addBoolParm(2, createIfNonExisting)
			.addBoolParm(3, ignoreBookmarkedOrNamed)
			.endRequest().getObjectParm(1, 18, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getConversationByBlob_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 17};
	/** Retrieves a Conversation object by Public Conversation BLOB. Public conversation blobs are globally unique conversation IDs that provide a method for joining conversation without explicitly being added to the conversation by someone already in it. Programmatically, a Conversation BLOB can be retrieved with Conversation.GetJoinBlob method. In Skype desktop clients, the BLOB can be retrieved by typing "/get uri" in a conversation. The conversation can then be joined by people who have somehow received that BLOB. 
	 * @param joinBlob The BLOB string. 
	 * @param alsoJoin If set to true, automatically joins current user into the Conversation. 
	 * @return conversation Returns Conversation object if successful. 
	 */
	public Conversation getConversationByBlob(String joinBlob, boolean alsoJoin) {
		try {
			return (Conversation) sidDoRequest(getConversationByBlob_req)
			.addStringParm(1, joinBlob)
			.addBoolParm(2, alsoJoin, true)
			.endRequest().getObjectParm(1, 18, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getConversationList_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 18};
	/** Returns a list of Conversation objects by Conversation.LIST_TYPE filter. 
	 * @param type Filter. 
	 * @return conversations List of conversations matching the filter. 
	 */
	public Conversation[] getConversationList(Conversation.ListType type) {
		try {
			return (Conversation[]) sidDoRequest(getConversationList_req)
			.addEnumParm(1, type)
			.endRequest().getObjectListParm(1, 18, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getMessageByGuid_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 21};
	/** Retrieves a Message object by the P_GUID property (globally unique ID, same for all the participants of the conversation, in which this message occured). 
	 * @param guid Globally unique ID of the message. 
	 * @return message Returns a Message object if a match was found. 
	 */
	public Message getMessageByGuid(byte[] guid) {
		try {
			return (Message) sidDoRequest(getMessageByGuid_req)
			.addBinaryParm(1, guid)
			.endRequest().getObjectParm(1, 9, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getMessageListByType_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 136,(byte) 1};
	/** Returns all messages of the given type
	 * @param type Type of messages requested. For POSTED_TEXT or POSTED_EMOTE, returns a list with both types
	 * @param latestPerConvOnly Whether to return only the most recent message per conversation
	 * @param fromTimestampInc Starting timestamp for reqested range, inclusive
	 * @param toTimestampExc Ending timestamp for requested range, exclusive
	 * @return messages
	 */
	public Message[] getMessageListByType(Message.Type type, boolean latestPerConvOnly, long fromTimestampInc, long toTimestampExc) {
		try {
			return (Message[]) sidDoRequest(getMessageListByType_req)
			.addEnumParm(1, type)
			.addBoolParm(2, latestPerConvOnly)
			.addTimestampParm(3, fromTimestampInc)
			.addTimestampParm(4, toTimestampExc)
			.endRequest().getObjectListParm(1, 9, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getAvailableVideoDevices_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 80};
	public class GetAvailableVideoDevicesResponse {
		public String[] deviceNames;
		public String[] devicePaths;
		public int count;
	};
	
	/** This method returns a table in form of two string lists of equal length and an uint argument that returns the count of items i both lists. The first list contains video recording device handles and the second list descriptive names of those devices. NB! This method will return empty lists immediately after account login. It takes several seconds to populate and check the local webcams and until this check is complete, local video remains unavailable. The correct procedure is to handle the Skype.OnAvailableVideoDeviceListChange event, and only enable UI features that require webcam, after you get non-empty webcam lists in this event callback. 	 * @return GetAvailableVideoDevicesResponse
	 * <br> - deviceNames
	 * <br> - devicePaths
	 * <br> - count
	 */
	public GetAvailableVideoDevicesResponse getAvailableVideoDevices() {
		try {
			Decoding decoder = sidDoRequest(getAvailableVideoDevices_req)
			.endRequest();
			GetAvailableVideoDevicesResponse result = new GetAvailableVideoDevicesResponse();
			result.deviceNames = decoder.getStringListParm(1, false);
			result.devicePaths = decoder.getStringListParm(2, false);
			result.count = decoder.getUintParm(3, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] hasVideoDeviceCapability_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 33};
	/** Queries whether the given video device has a specific Video.VIDEO_DEVICE_CAPABILITY. Use Skype.GetAvailableVideoDevices method to retrieve sstring lists with available deviceName and devicePath values. 
	 * @param deviceName Human readable device name. 
	 * @param devicePath Device ID. 
	 * @param cap Any of the Video.VIDEO_DEVICE_CAPABILITY values. 
	 * @return result Returns true if the given device has given capability. 
	 */
	public boolean hasVideoDeviceCapability(String deviceName, String devicePath, Video.VideoDeviceCapability cap) {
		try {
			return sidDoRequest(hasVideoDeviceCapability_req)
			.addStringParm(1, deviceName)
			.addStringParm(2, devicePath)
			.addEnumParm(3, cap)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] displayVideoDeviceTuningDialog_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 34};
	/**
	 * displayVideoDeviceTuningDialog
	 * @param deviceName
	 * @param devicePath
	 */
	public void displayVideoDeviceTuningDialog(String deviceName, String devicePath) {
		try {
			sidDoRequest(displayVideoDeviceTuningDialog_req)
			.addStringParm(1, deviceName)
			.addStringParm(2, devicePath)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] getPreviewVideo_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 35};
	/** Warning: Will be deprecated soon
	 * @param type
	 * @param deviceName name and path to be used only with media type VIDEO
	 * @param devicePath
	 * @return video
	 */
	public Video getPreviewVideo(Video.MediaType type, String deviceName, String devicePath) {
		try {
			return (Video) sidDoRequest(getPreviewVideo_req)
			.addEnumParm(1, type)
			.addStringParm(2, deviceName)
			.addStringParm(3, devicePath)
			.endRequest().getObjectParm(1, 11, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] createLocalVideo_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 143,(byte) 1};
	/**
	 * createLocalVideo
	 * @param type
	 * @param deviceName name and path to be used only with media type VIDEO
	 * @param devicePath
	 * @return video
	 */
	public Video createLocalVideo(Video.MediaType type, String deviceName, String devicePath) {
		try {
			return (Video) sidDoRequest(createLocalVideo_req)
			.addEnumParm(1, type)
			.addStringParm(2, deviceName)
			.addStringParm(3, devicePath)
			.endRequest().getObjectParm(1, 11, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] createPreviewVideo_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 144,(byte) 1};
	/** Will return same object id for same device repeatedly
	 * @param type
	 * @param deviceName name and path to be used only with media type VIDEO
	 * @param devicePath
	 * @return video
	 */
	public Video createPreviewVideo(Video.MediaType type, String deviceName, String devicePath) {
		try {
			return (Video) sidDoRequest(createPreviewVideo_req)
			.addEnumParm(1, type)
			.addStringParm(2, deviceName)
			.addStringParm(3, devicePath)
			.endRequest().getObjectParm(1, 11, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] videoCommand_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 59};
	/** Avaible to Video Engines using the Video RTP API
	 * @param command
	 * @return response
	 */
	public String videoCommand(String command) {
		try {
			return sidDoRequest(videoCommand_req)
			.addStringParm(1, command)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] startMonitoringQuality_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 140,(byte) 1};
	/**
	 * startMonitoringQuality
	 * @param withUser if empty, network test results would reflect status of local node only
	 * @param excludeNetworkTest
	 */
	public void startMonitoringQuality(String withUser, boolean excludeNetworkTest) {
		try {
			sidDoRequest(startMonitoringQuality_req)
			.addStringParm(1, withUser)
			.addBoolParm(2, excludeNetworkTest)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] stopMonitoringQuality_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 141,(byte) 1};
	/**
	 * stopMonitoringQuality
	 * @param withUser
	 * @param justStop
	 * @return result
	 */
	public QualityTestResult stopMonitoringQuality(String withUser, boolean justStop) {
		try {
			return (QualityTestResult) sidDoRequest(stopMonitoringQuality_req)
			.addStringParm(1, withUser)
			.addBoolParm(2, justStop)
			.endRequest().getEnumParm(1, QualityTestResult.get(0), true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return QualityTestResult.get(0);
		}
	}
	private final static byte[] getGreeting_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 45};
	/**
	 * getGreeting
	 * @param skypeName
	 * @return greeting
	 */
	public Voicemail getGreeting(String skypeName) {
		try {
			return (Voicemail) sidDoRequest(getGreeting_req)
			.addStringParm(1, skypeName)
			.endRequest().getObjectParm(1, 7, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] playStart_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 48};
	/** Takes audio data that comes from the sound argument and mixes it into playback or notification device, depending on the value passed in the useCallOutDevice argument. The sound argument contains the audio data in in follwing format: first 4 bytes of the binary contain the sample rate, followed by 16 bit (mono) samples. The soundid argument is an arbitrary ID that you can pass in and then later use as an argument for Skype class PlayStop method. To mix the audio into playback device stream, set useCallOutDevice to true, to mic it into notification stream, set useCallOutDevice to false. 
	 * @param soundid
	 * @param sound
	 * @param loop
	 * @param useCallOutDevice
	 */
	public void playStart(int soundid, byte[] sound, boolean loop, boolean useCallOutDevice) {
		try {
			sidDoRequest(playStart_req)
			.addUintParm(1, soundid)
			.addBinaryParm(2, sound)
			.addBoolParm(3, loop)
			.addBoolParm(4, useCallOutDevice)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] playStartFromFile_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 212,(byte) 1};
	/**
	 * playStartFromFile
	 * @param soundid
	 * @param datafile
	 * @param loop
	 * @param useCallOutDevice
	 * @return result
	 */
	public PrepareSoundResult playStartFromFile(int soundid, String datafile, boolean loop, boolean useCallOutDevice) {
		try {
			return (PrepareSoundResult) sidDoRequest(playStartFromFile_req)
			.addUintParm(1, soundid)
			.addFilenameParm(2, datafile)
			.addBoolParm(3, loop)
			.addBoolParm(4, useCallOutDevice)
			.endRequest().getEnumParm(1, PrepareSoundResult.get(0), true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return PrepareSoundResult.get(0);
		}
	}
	private final static byte[] playStop_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 49};
	/** Stops playback of the soundfile. The argument is the same ID you passed in the Skype class StartPlayback method. 
	 * @param soundid
	 */
	public void playStop(int soundid) {
		try {
			sidDoRequest(playStop_req)
			.addUintParm(1, soundid)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] startRecordingTest_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 50};	/**
	 * startRecordingTest
	 * @param recordAndPlaybackData
	 */
	public void startRecordingTest(boolean recordAndPlaybackData) {
		try {
			sidDoRequest(startRecordingTest_req)
			.addBoolParm(1, recordAndPlaybackData)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] stopRecordingTest_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 51};
	/**
	 * stopRecordingTest
	 */
	public void stopRecordingTest() {
		try {
			sidDoRequest(stopRecordingTest_req)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] getAvailableOutputDevices_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 53};
	public class GetAvailableOutputDevicesResponse {
		public String[] handleList;
		public String[] nameList;
		public String[] productIdList;
	};
	
	/** This method returns a table in form of three string lists of equal lengths. The first list contains audio output device handles ('hw:0,0', 'hw:0,1', etc.) The second list contains descriptive names of those devices (Ensoniq AudioPCI etc.) The third list contains device product IDs. Note that the values in these lists depend on which audio engine you are running (SAL, PCM, RTP). 
	 * @return GetAvailableOutputDevicesResponse
	 * <br> - handleList
	 * <br> - nameList
	 * <br> - productIdList
	 */
	public GetAvailableOutputDevicesResponse getAvailableOutputDevices() {
		try {
			Decoding decoder = sidDoRequest(getAvailableOutputDevices_req)
			.endRequest();
			GetAvailableOutputDevicesResponse result = new GetAvailableOutputDevicesResponse();
			result.handleList = decoder.getStringListParm(1, false);
			result.nameList = decoder.getStringListParm(2, false);
			result.productIdList = decoder.getStringListParm(3, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getAvailableRecordingDevices_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 54};
	public class GetAvailableRecordingDevicesResponse {
		public String[] handleList;
		public String[] nameList;
		public String[] productIdList;
	};
	
	/** This method returns a table in form of three string lists of equal length. The first list contains audio recording device handles ('hw:0,0', 'hw:0,1', etc.) The second list contains descriptive names of those devices (Ensoniq AudioPCI etc.) The third list contains device product IDs. Note that the values in these lists depend on which audio engine you are running (SAL, PCM, RTP). 
	 * @return GetAvailableRecordingDevicesResponse
	 * <br> - handleList
	 * <br> - nameList
	 * <br> - productIdList
	 */
	public GetAvailableRecordingDevicesResponse getAvailableRecordingDevices() {
		try {
			Decoding decoder = sidDoRequest(getAvailableRecordingDevices_req)
			.endRequest();
			GetAvailableRecordingDevicesResponse result = new GetAvailableRecordingDevicesResponse();
			result.handleList = decoder.getStringListParm(1, false);
			result.nameList = decoder.getStringListParm(2, false);
			result.productIdList = decoder.getStringListParm(3, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] selectSoundDevices_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 55};
	/** Sets audio devices given in arguments as active audio devices. This command selects all three devices - microphone, playback and the notification channel. Valid input values for this method come from the first string list you get back from Skype class GetAvailableOutputDevices (handleList). 
	 * @param callInDevice
	 * @param callOutDevice
	 * @param waveOutDevice
	 */
	public void selectSoundDevices(String callInDevice, String callOutDevice, String waveOutDevice) {
		try {
			sidDoRequest(selectSoundDevices_req)
			.addStringParm(1, callInDevice)
			.addStringParm(2, callOutDevice)
			.addStringParm(3, waveOutDevice)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] getAudioDeviceCapabilities_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 56};
	public class GetAudioDeviceCapabilitiesResponse {
		public String interfaceString;
		public int capabilities;
	};
	
	/** The uint argument returns AUDIODEVICE_CAPABILITIES (declared in Skype class) 
	 * @param deviceHandle
	 * @return GetAudioDeviceCapabilitiesResponse
	 * <br> - interfaceString
	 * <br> - capabilities bit set of AUDIODEVICE_CAPABILITIES
	 */
	public GetAudioDeviceCapabilitiesResponse getAudioDeviceCapabilities(String deviceHandle) {
		try {
			Decoding decoder = sidDoRequest(getAudioDeviceCapabilities_req)
			.addStringParm(1, deviceHandle)
			.endRequest();
			GetAudioDeviceCapabilitiesResponse result = new GetAudioDeviceCapabilitiesResponse();
			result.interfaceString = decoder.getStringParm(1, false);
			result.capabilities = decoder.getUintParm(2, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getNrgLevels_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 57};
	public class GetNrgLevelsResponse {
		public int micLevel;
		public int speakerLevel;
	};
	
	/** Returns current audio stream volume for both playback and microphone streams. Useful for displaying visual audio indicators in you UI. See also Skype class OnNrgLevelsChange callback that gets fired each time the these values are changed. 
	 * @return GetNrgLevelsResponse
	 * <br> - micLevel
	 * <br> - speakerLevel
	 */
	public GetNrgLevelsResponse getNrgLevels() {
		try {
			Decoding decoder = sidDoRequest(getNrgLevels_req)
			.endRequest();
			GetNrgLevelsResponse result = new GetNrgLevelsResponse();
			result.micLevel = decoder.getUintParm(1, false);
			result.speakerLevel = decoder.getUintParm(2, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] voiceCommand_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 58};
	/** NB! This command only works if its implemented in external audiohost (RTP or PCM host). The command can be is used for passing custom commands from client UI to the audio implementation. 
	 * @param command
	 * @return response
	 */
	public String voiceCommand(String command) {
		try {
			return sidDoRequest(voiceCommand_req)
			.addStringParm(1, command)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] getSpeakerVolume_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 60};
	/** Returns value of audio playback volume setting (0..100). 
	 * @return volume
	 */
	public int getSpeakerVolume() {
		try {
			return sidDoRequest(getSpeakerVolume_req)
			.endRequest().getUintParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return 0;
		}
	}
	private final static byte[] setSpeakerVolume_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 61};
	/** This method is for setting speaker volume. It will set the level for Skype digital gain control. Skype audio library will not control gain of audio device itself. 
	 * @param volume
	 */
	public void setSpeakerVolume(int volume) {
		try {
			sidDoRequest(setSpeakerVolume_req)
			.addUintParm(1, volume)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] getMicVolume_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 62};
	/** Returns value of microphone volume setting (0..100). It will return the analog gain of audio device set by Skype AGC. For real-time microphone volume, use GetNrgLevels method or OnNrgLevelsChange callback (both are methods of Skype class). 
	 * @return micVolume
	 */
	public int getMicVolume() {
		try {
			return sidDoRequest(getMicVolume_req)
			.endRequest().getUintParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return 0;
		}
	}
	private final static byte[] setMicVolume_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 63};
	/** This method is for setting the microphone volume level. This does not work when Skype AGC (Automatic Gain Control) is enabled, which it is by default. It is currently impossible to disable AGC, so for now this method is here for purpose of future compatibility. 
	 * @param volume
	 */
	public void setMicVolume(int volume) {		try {
			sidDoRequest(setMicVolume_req)
			.addUintParm(1, volume)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] isSpeakerMuted_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 64};
	/** Returns true in &muted argument if the currently selected playback device is muted. 
	 * @return muted
	 */
	public boolean isSpeakerMuted() {
		try {
			return sidDoRequest(isSpeakerMuted_req)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] isMicrophoneMuted_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 65};
	/** Returns true in &muted argument if the currently selected microphone is muted. 
	 * @return muted
	 */
	public boolean isMicrophoneMuted() {
		try {
			return sidDoRequest(isMicrophoneMuted_req)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] muteSpeakers_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 66};
	/** Sets currently selected playback device mute status according to argument. 
	 * @param mute
	 */
	public void muteSpeakers(boolean mute) {
		try {
			sidDoRequest(muteSpeakers_req)
			.addBoolParm(1, mute)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] muteMicrophone_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 67};
	/** Sets currently selected microphone mute status according to argument. 
	 * @param mute
	 */
	public void muteMicrophone(boolean mute) {
		try {
			sidDoRequest(muteMicrophone_req)
			.addBoolParm(1, mute)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] setOperatingMedia_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 255,(byte) 1};
	/**
	 * setOperatingMedia
	 * @param media
	 * @param maxUplinkBps
	 * @param maxDownlinkBps
	 */
	public void setOperatingMedia(OperatingMedia media, int maxUplinkBps, int maxDownlinkBps) {
		try {
			sidDoRequest(setOperatingMedia_req)
			.addEnumParm(1, media)
			.addUintParm(2, maxUplinkBps)
			.addUintParm(3, maxDownlinkBps)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] requestConfirmationCode_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 29};
	/** creates and sends a CONFIRMATION_CODE_REQUEST message                   this sends a confirmation code to the number provided
	 * @param type
	 * @param number
	 * @return sms
	 */
	public Sms requestConfirmationCode(Sms.ConfirmType type, String number) {
		try {
			return (Sms) sidDoRequest(requestConfirmationCode_req)
			.addEnumParm(1, type)
			.addStringParm(2, number)
			.endRequest().getObjectParm(1, 12, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] submitConfirmationCode_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 30};
	/** creates and sends a CONFIRMATION_CODE_SUBMIT message                   this authorizes the number with the server for the purpose given in RequestConfirmationCode
	 * @param number
	 * @param code
	 * @return sms
	 */
	public Sms submitConfirmationCode(String number, String code) {
		try {
			return (Sms) sidDoRequest(submitConfirmationCode_req)
			.addStringParm(1, number)
			.addStringParm(2, code)
			.endRequest().getObjectParm(1, 12, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] createOutgoingSms_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 70};
	/** creates an OUTGOING/COMPOSING SMS message
	 * @return sms
	 */
	public Sms createOutgoingSms() {
		try {
			return (Sms) sidDoRequest(createOutgoingSms_req)
			.endRequest().getObjectParm(1, 12, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getAccount_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 115};
	/** Retrieves an Account object by Skype name (identity). This should normally be one of the first method calls after Skype object initialization. Nearly all the other methods require successful account login in order to work properly. The list of accounts that have been used on the local machine/database can be retrieved with Skype.GetExistingAccounts method. If a matching identity is not found, a new Account object is created. This object can then be used to populate requred fields and then use Account.Register method for new account creation. This method returns false on error. 
	 * @param identity Account skypename. 
	 * @return account Returns account object if successful. 
	 */
	public Account getAccount(String identity) {
		try {
			return (Account) flushCache(sidDoRequest(getAccount_req)
			.addStringParm(1, identity)
			.endRequest().getObjectParm(1, 5, true));
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getExistingAccounts_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 113};
	/** Returns a list of possible profiles used before on this machine
	 * @return accountNameList
	 */
	public String[] getExistingAccounts() {
		try {
			return sidDoRequest(getExistingAccounts_req)
			.endRequest().getStringListParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getDefaultAccountName_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 114};
	/** return most recently used account that has pwd saved. empty string if none
	 * @return account
	 */
	public String getDefaultAccountName() {
		try {
			return sidDoRequest(getDefaultAccountName_req)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] getSuggestedSkypename_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 116};
	/** suggest a nice skypename to go with given fullname
	 * @param fullname
	 * @return suggestedName
	 */
	public String getSuggestedSkypename(String fullname) {
		try {
			return sidDoRequest(getSuggestedSkypename_req)
			.addStringParm(1, fullname)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] validateAvatar_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 119};
	public class ValidateAvatarResponse {
		public Validateresult result;
		public int freeBytesLeft;
	};
	
	/**
	 * validateAvatar
	 * @param value
	 * @return ValidateAvatarResponse
	 * <br> - result
	 * <br> - freeBytesLeft
	 */
	public ValidateAvatarResponse validateAvatar(byte[] value) {
		try {
			Decoding decoder = sidDoRequest(validateAvatar_req)
			.addBinaryParm(1, value)
			.endRequest();
			ValidateAvatarResponse result = new ValidateAvatarResponse();
			result.result = (Validateresult) decoder.getEnumParm(1, Validateresult.get(0), false);
			result.freeBytesLeft = decoder.getIntParm(2, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] validateProfileString_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 102};
	public class ValidateProfileStringResponse {
		public Validateresult result;
		public int freeBytesLeft;
	};
	
	/** This method should be used for validating skypenames before registering new accounts, if the propKey is set to SKYPENAME (Contact class) and forRegistration argument is set to true. If the forRegistration argument is false, only string length check is applied. It is also useful to probe, what the size limits are, for each string property (e.g. 300 characters for moodmessage) 
	 * @param propKey
	 * @param strValue
	 * @param forRegistration
	 * @return ValidateProfileStringResponse
	 * <br> - result
	 * <br> - freeBytesLeft
	 */
	public ValidateProfileStringResponse validateProfileString(int propKey, String strValue, boolean forRegistration) {
		try {
			Decoding decoder = sidDoRequest(validateProfileString_req)
			.addEnumParm(1, propKey)
			.addStringParm(2, strValue)
			.addBoolParm(3, forRegistration)
			.endRequest();
			ValidateProfileStringResponse result = new ValidateProfileStringResponse();
			result.result = (Validateresult) decoder.getEnumParm(1, Validateresult.get(0), false);
			result.freeBytesLeft = decoder.getIntParm(2, true);			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] validatePassword_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 71};
	/** This method is for pre-validating account passwords before account creation or password change. The result will return either VALIDATED_OK or one of many possible reasons the password is unacceptable (too short, too simple, etc.) 
	 * @param username
	 * @param password
	 * @return result
	 */
	public Validateresult validatePassword(String username, String password) {
		try {
			return (Validateresult) sidDoRequest(validatePassword_req)
			.addStringParm(1, username)
			.addStringParm(2, password)
			.endRequest().getEnumParm(1, Validateresult.get(0), true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return Validateresult.get(0);
		}
	}
	private final static byte[] getUsedPort_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 130,(byte) 1};
	/** Port number that the runtime ended up listening to. Usually equal to SETUPKEY_PORT. 0 if none used (disconnected or binding failed). 
	 * @return port
	 */
	public int getUsedPort() {
		try {
			return sidDoRequest(getUsedPort_req)
			.endRequest().getUintParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return 0;
		}
	}
	private final static byte[] getStr_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 120};
	/** This is used for retrieving local setup keys of type string. For more information, see Defines section in the skype-embedded_2.h  
	 * @param key
	 * @return value
	 */
	public String getStr(String key) {
		try {
			return sidDoRequest(getStr_req)
			.addStringParm(1, key)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] getInt_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 121};
	/** This is used for retrieving local setup keys of type int. For more information, see Defines section in the skype-embedded_2.h  
	 * @param key
	 * @return value
	 */
	public int getInt(String key) {
		try {
			return sidDoRequest(getInt_req)
			.addStringParm(1, key)
			.endRequest().getIntParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return 0;
		}
	}
	private final static byte[] getBin_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 122};
	/** This is used for retrieving local setup keys of type binary. For more information, see Defines section in the skype-embedded_2.h  
	 * @param key
	 * @return value
	 */
	public byte[] getBin(String key) {
		try {
			return sidDoRequest(getBin_req)
			.addStringParm(1, key)
			.endRequest().getBinaryParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] setStr_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 123};
	/** This is used for setting local setup keys of type string. For more information, see Defines section in the skype-embedded_2.h  
	 * @param key
	 * @param value
	 */
	public void setStr(String key, String value) {
		try {
			sidDoRequest(setStr_req)
			.addStringParm(1, key)
			.addStringParm(2, value)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] setInt_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 124};
	/** This is used for setting local setup keys of type int. For more information, see Defines section in the skype-embedded_2.h  
	 * @param key
	 * @param value
	 */
	public void setInt(String key, int value) {
		try {
			sidDoRequest(setInt_req)
			.addStringParm(1, key)
			.addIntParm(2, value)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] setBin_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 125};
	/** This is used for setting local setup keys of type binary. For more information, see Defines section in the skype-embedded_2.h  
	 * @param key
	 * @param value
	 */
	public void setBin(String key, byte[] value) {
		try {
			sidDoRequest(setBin_req)
			.addStringParm(1, key)
			.addBinaryParm(2, value)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] isDefined_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 126};
	/** Returns true if the given setup key is defined in local setup. For more information, see Defines section in the skype-embedded_2.h  
	 * @param key
	 * @return value
	 */
	public boolean isDefined(String key) {
		try {
			return sidDoRequest(isDefined_req)
			.addStringParm(1, key)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] delete_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 127};
	/**
	 * delete
	 * @param key
	 */
	public void delete(String key) {
		try {
			sidDoRequest(delete_req)
			.addStringParm(1, key)
			.endOneWay();
		} catch(IOException e) {
			sidOnFatalError(e);
		}
	}
	private final static byte[] getSubKeys_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 128,(byte) 1};
	/**
	 * getSubKeys
	 * @param key
	 * @return value
	 */
	public String[] getSubKeys(String key) {
		try {
			return sidDoRequest(getSubKeys_req)
			.addStringParm(1, key)
			.endRequest().getStringListParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getIsoLanguageInfo_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 207,(byte) 1};
	public class GetIsoLanguageInfoResponse {
		public String[] languageCodeList;
		public String[] languageNameList;
	};
	
	/** Returns two string lists. First of them will contain list of two-letter language codes (ISO 639-1) The second list contains names of corresponding languages. 
	 * @return GetIsoLanguageInfoResponse
	 * <br> - languageCodeList
	 * <br> - languageNameList assumes UI has set correct language (UIPROP_LANGUAGE)
	 */
	public GetIsoLanguageInfoResponse getIsoLanguageInfo() {
		try {
			Decoding decoder = sidDoRequest(getIsoLanguageInfo_req)
			.endRequest();
			GetIsoLanguageInfoResponse result = new GetIsoLanguageInfoResponse();
			result.languageCodeList = decoder.getStringListParm(1, false);
			result.languageNameList = decoder.getStringListParm(2, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getIsoCountryInfo_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 208,(byte) 1};
	public class GetIsoCountryInfoResponse {
		public String[] countryCodeList;
		public String[] countryNameList;
		public int[] countryPrefixList;
		public String[] countryDialExampleList;
	};
	
	/** Returns three string lists and one int array, containing 2-letter country code, country name, dialing prefix and example dial string (not available for all items). This method does currently return 0 for South Georgia and the South Sandwich Islands. 
	 * @return GetIsoCountryInfoResponse
	 * <br> - countryCodeList
	 * <br> - countryNameList assumes UI has set correct language (UIPROP_LANGUAGE)
	 * <br> - countryPrefixList
	 * <br> - countryDialExampleList
	 */
	public GetIsoCountryInfoResponse getIsoCountryInfo() {
		try {
			Decoding decoder = sidDoRequest(getIsoCountryInfo_req)
			.endRequest();
			GetIsoCountryInfoResponse result = new GetIsoCountryInfoResponse();
			result.countryCodeList = decoder.getStringListParm(1, false);
			result.countryNameList = decoder.getStringListParm(2, false);
			result.countryPrefixList = decoder.getUintListParm(3, false);
			result.countryDialExampleList = decoder.getStringListParm(4, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getSupportedUilanguageList_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 135,(byte) 1};
	/** Get list of supported UI languages
	 * @return uiLanguageCodeList
	 */
	public String[] getSupportedUilanguageList() {
		try {
			return sidDoRequest(getSupportedUilanguageList_req)
			.endRequest().getStringListParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getIsoCountryCodeByPhoneNo_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 211,(byte) 1};
	/** Returns 2-letter country code based on PSTN number. The input argument has to be without + in from of it - '37212345678' will return 'ee' while '+37212345678' will return an empty string. 	 * @param number
	 * @return countryCode
	 */
	public String getIsoCountryCodeByPhoneNo(String number) {
		try {
			return sidDoRequest(getIsoCountryCodeByPhoneNo_req)
			.addStringParm(1, number)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] app2AppCreate_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 215,(byte) 1};
	/**
	 * App2AppCreate method constructs a local instance of an App2App application. App2App applications are not abstracted in the SkypeKit API as a separate class. Rather, they can be created with App2AppCreate, supplying their name as ID, and then connected to remote parties using App2AppConnect method. 
	 * 
	 * App2App portion of the SkypeKit API enables you to implement arbitrary data exchange protocols between Skype clients. Basically, if you are ever tempted to use conversation chat messages for something other than actual human-readable chat - you should consider using your own custom App2App protocol instead. 
	 * 
	 * The downside of App2App is that all the participants need to be running a client that supports the same App2App application. Although, it is possible to have one side using a custom SkypeKit client and another side using Skype desktop client - App2App is supported in both, in case of desktop client via Public API - you are still limited to remote side running something that can recognize your protocol and react to connection attempts from your side. 
	 * 
	 * To establish connection between each other, all participants need to create their local instances of the application (with the same ID, and then connect to each other. More than one App2App applications can be active in a local client at the same time. Also, more than two clients can be connected with the same application. 
	 * 
	 * Once connection is established, you can choose between two communication methods - datagrams and stream read/write methods. Overall, there are not much principal difference between the two. Datagram packet size is limited to 1500 bytes and stream packet size to 32 KB of payload data. Implementation-wise, datagrams are probably somewhat easier to deal with. 
	
	 * @param appname Application ID. This ID is used by the rest of the App2App commands to differentiate between applications, should there be more than one app2app applications running on the local system. 
	 * @return result Returns true if the app creation was successful. Returns false when an application with the same name already exists in the local system. 
	 */
	public boolean app2AppCreate(String appname) {
		try {
			return sidDoRequest(app2AppCreate_req)
			.addStringParm(1, appname)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] app2AppDelete_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 216,(byte) 1};
	/** Disconnects and deletes the App2App application. 
	 * @param appname application ID. 
	 * @return result Returns true if the deletion was successful (application with such ID actually existed) 
	 */
	public boolean app2AppDelete(String appname) {
		try {
			return sidDoRequest(app2AppDelete_req)
			.addStringParm(1, appname)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] app2AppConnect_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 217,(byte) 1};
	/** The App2AppConnect connect result does not mean the connection was immediately established. It will return Ok even if the remote party was offline. The actual connection will be established when both parties have fired App2AppConnect with the same application name, at eachother. At that point, OnApp2AppStreamListChange event will fire for both local and remote party (with listType argument set to ALL_STREAMS) and you can start exchanging data, using either App2App datagrams or App2AppRead App2AppWrite methods. 
	 * @param appname Application ID. This needs to match with application ID connecting from the remote side. 
	 * @param skypename Skype Name of the remote party. 
	 * @return result NB! This argument will return true even if the remote party has not yet connected (or is not even online yet) - it merely indicates that the connect command was successfuly processed in runtime. The actual connection success will be indicated when the OnApp2AppStreamListChange event fires, i.e. when App2App stream will be established between connecting parties. 
	 */
	public boolean app2AppConnect(String appname, String skypename) {
		try {
			return sidDoRequest(app2AppConnect_req)
			.addStringParm(1, appname)
			.addStringParm(2, skypename)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] app2AppDisconnect_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 218,(byte) 1};
	/** Disconnects an App2App stream. This is different from App2AppDelete command in that it enables you to disconnect remote parties selectively - in case there are more than two participants in the App2App stream pool. 
	 * @param appname application ID 
	 * @param stream stream ID. 
	 * @return result returns true when the stream disconnect was successful. 
	 */
	public boolean app2AppDisconnect(String appname, String stream) {
		try {
			return sidDoRequest(app2AppDisconnect_req)
			.addStringParm(1, appname)
			.addStringParm(2, stream)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] app2AppWrite_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 219,(byte) 1};
	/** Sends a stream packet to the remote party specified in the stream argument. The max size of stream write packet is 32KB. After calling this method, OnApp2AppStreamListChange will fire for both parties. In local ssytem with listType set to SENDING_STREAMS and on remote system with listType set to RECEIVED_STREAMS. This event can be used to read received packets out of the stream. 
	 * @param appname application ID 
	 * @param stream stream ID 
	 * @param data packet payload 
	 * @return result returns true if the call was successful. Note that this does indicate the packet was actually received by remote party. 
	 */
	public boolean app2AppWrite(String appname, String stream, byte[] data) {
		try {
			return sidDoRequest(app2AppWrite_req)
			.addStringParm(1, appname)
			.addStringParm(2, stream)
			.addBinaryParm(3, data)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] app2AppDatagram_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 220,(byte) 1};
	/** Sends a datagram to the remote party specified in the stream argument. The max size of datagram payload is 1500 bytes. 
	 * @param appname application ID - from App2AppCreate 
	 * @param stream stream ID - either from App2AppGetStreamsList or from OnApp2AppStreamListChange
	 * @param data datagram payload (max 1500 bytes)
	 * @return result returns true on method success. Note that this does mean the remote party has actually received your datagram - that sort of feedback, should you want it, is up to you to implement in your custom protocol.
	 */
	public boolean app2AppDatagram(String appname, String stream, byte[] data) {
		try {
			return sidDoRequest(app2AppDatagram_req)
			.addStringParm(1, appname)
			.addStringParm(2, stream)
			.addBinaryParm(3, data)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] app2AppRead_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 221,(byte) 1};
	public class App2AppReadResponse {
		public boolean result;
		public byte[] data;
	};
	
	/** Reads data from the specified stream. This method should be called from the OnApp2AppStreamListChange event callback, when this callback gets fired with listType argument set to RECEIVED_STREAMS. 
	 * @param appname application ID 
	 * @param stream stream ID 
	 * @return App2AppReadResponse
	 * <br> - result returns true on method success. Note that this does mean the remote party has actually received your packet - that sort of feedback, should you want it, is up to you to implement in your custom protocol. 	 * <br> - data stream packet payload 
	 */
	public App2AppReadResponse app2AppRead(String appname, String stream) {
		try {
			Decoding decoder = sidDoRequest(app2AppRead_req)
			.addStringParm(1, appname)
			.addStringParm(2, stream)
			.endRequest();
			App2AppReadResponse result = new App2AppReadResponse();
			result.result = decoder.getBoolParm(1, false);
			result.data = decoder.getBinaryParm(2, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] app2AppGetConnectableUsers_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 222,(byte) 1};
	public class App2AppGetConnectableUsersResponse {
		public boolean result;
		public String[] users;
	};
	
	/** App2AppGetConnectableUsers returns a list of currently online contacts. It does not return a list of contacts who have an app2app application running. There is currently no way of querying whether an application has been launched on the remote side - other than trying to connect to the remote side and waiting for timeout. NB! if you use App2AppGetConnectableUsers immediately after login - then the online presence of your contact list has not yet updated itself - so this method will most likely return either an empty list or a list with echo123 in it. 
	 * @param appname application ID 
	 * @return App2AppGetConnectableUsersResponse
	 * <br> - result returns true on method success 
	 * <br> - users stringlist with Skype Names of connectable users 
	 */
	public App2AppGetConnectableUsersResponse app2AppGetConnectableUsers(String appname) {
		try {
			Decoding decoder = sidDoRequest(app2AppGetConnectableUsers_req)
			.addStringParm(1, appname)
			.endRequest();
			App2AppGetConnectableUsersResponse result = new App2AppGetConnectableUsersResponse();
			result.result = decoder.getBoolParm(1, false);
			result.users = decoder.getStringListParm(2, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] app2AppGetStreamsList_req = {(byte) 90,(byte) 82,(byte) 0,(byte) 223,(byte) 1};
	public class App2AppGetStreamsListResponse {
		public boolean result;
		public String[] streams;
		public int[] receivedSizes;
	};
	
	/** Takes application ID and list type filter and returns a stringlist with streamID's that pass the filter. 
	 * @param appname application ID 
	 * @param listType list type filter 
	 * @return App2AppGetStreamsListResponse
	 * <br> - result results true if the method call was successful 
	 * <br> - streams string list with stream IDs 
	 * <br> - receivedSizes For RECEIVED_STREAMS, contains the number of bytes in each stream waiting to be read
	 */
	public App2AppGetStreamsListResponse app2AppGetStreamsList(String appname, App2AppStreams listType) {
		try {
			Decoding decoder = sidDoRequest(app2AppGetStreamsList_req)
			.addStringParm(1, appname)
			.addEnumParm(2, listType)
			.endRequest();
			App2AppGetStreamsListResponse result = new App2AppGetStreamsListResponse();
			result.result = decoder.getBoolParm(1, false);
			result.streams = decoder.getStringListParm(2, false);
			result.receivedSizes = decoder.getUintListParm(3, true);
			return result;
		} catch(IOException e) {
			sidOnFatalError(e);
			return null;
		}
	}
	public void sidDispatchEvent(final int modId, final int evId, final Decoding decoder) {
		switch (modId) {
		case 0: {
			if (mSkypeListener!= null) {
				switch (evId) {
				case 1: {
					ContactGroup group;
					try {
						group = (ContactGroup) decoder.getObjectParm(1, 10, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onNewCustomContactGroup(this, group);
					return;
				}
				case 2: {
					Contact contact;
					try {
						contact = (Contact) decoder.getObjectParm(1, 2, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onContactOnlineAppearance(this, contact);
					return;
				}
				case 3: {
					Contact contact;
					try {
						contact = (Contact) decoder.getObjectParm(1, 2, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onContactGoneOffline(this, contact);
					return;
				}
				case 4: {
					Conversation conversation;
					Conversation.ListType type;
					boolean added;
					try {
						conversation = (Conversation) decoder.getObjectParm(1, 18, false);
						type = (Conversation.ListType) decoder.getEnumParm(2, Conversation.ListType.get(0), false);
						added = decoder.getBoolParm(3, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onConversationListChange(this, conversation, type, added);
					return;
				}
				case 5: {
					Message message;
					boolean changesInboxTimestamp;
					Message supersedesHistoryMessage;
					Conversation conversation;
					try {
						message = (Message) decoder.getObjectParm(1, 9, false);
						changesInboxTimestamp = decoder.getBoolParm(2, false);
						supersedesHistoryMessage = (Message) decoder.getObjectParm(3, 9, false);
						conversation = (Conversation) decoder.getObjectParm(4, 18, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onMessage(this, message, changesInboxTimestamp, supersedesHistoryMessage, conversation);
					return;
				}
				case 7: {
					try {
						decoder.skipMessage();
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onAvailableVideoDeviceListChange(this);
					return;
				}
				case 44: {
					try {
						decoder.skipMessage();
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onH264Activated(this);
					return;
				}
				case 28: {
					QualityTestType testType;
					QualityTestResult testResult;
					String withUser;
					String details;
					String xmlDetails;
					try {
						testType = (QualityTestType) decoder.getEnumParm(0, QualityTestType.get(0), false);
						testResult = (QualityTestResult) decoder.getEnumParm(1, QualityTestResult.get(0), false);
						withUser = decoder.getStringParm(2, false);
						details = decoder.getStringParm(3, false);
						xmlDetails = decoder.getXmlParm(4, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onQualityTestResult(this, testType, testResult, withUser, details, xmlDetails);
					return;
				}
				case 10: {
					try {
						decoder.skipMessage();
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onAvailableDeviceListChange(this);
					return;
				}
				case 11: {
					try {
						decoder.skipMessage();
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onNrgLevelsChange(this);
					return;
				}
				case 12: {
					ProxyType type;
					try {
						type = (ProxyType) decoder.getEnumParm(1, ProxyType.get(0), true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onProxyAuthFailure(this, type);
					return;
				}
				case 6: {
					String appname;
					String stream;
					byte[] data;
					try {
						appname = decoder.getStringParm(1, false);
						stream = decoder.getStringParm(2, false);
						data = decoder.getBinaryParm(3, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onApp2AppDatagram(this, appname, stream, data);
					return;
				}
				case 8: {
					String appname;
					App2AppStreams listType;
					String[] streams;
					int[] receivedSizes;
					try {
						appname = decoder.getStringParm(1, false);
						listType = (App2AppStreams) decoder.getEnumParm(2, App2AppStreams.get(0), false);
						streams = decoder.getStringListParm(3, false);
						receivedSizes = decoder.getUintListParm(4, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mSkypeListener.onApp2AppStreamListChange(this, appname, listType, streams, receivedSizes);
					return;
				}
				}
			}
		}
		break;
		case 10:
			if (mContactGroupListener != null) {
				ContactGroup object = (ContactGroup) sidDecodeEventTarget(modId, decoder);				if (object == null) return;
				switch (evId) {
				case 1: {
					Conversation conversation;
					try {
						conversation = (Conversation) decoder.getObjectParm(1, 18, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mContactGroupListener.onChangeConversation(object, conversation);
					return;
				}
				case 2: {
					Contact contact;
					try {
						contact = (Contact) decoder.getObjectParm(1, 2, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mContactGroupListener.onChange(object, contact);
					return;
				}
				}
			}
			break;
		case 1:
			if (mContactSearchListener != null) {
				ContactSearch object = (ContactSearch) sidDecodeEventTarget(modId, decoder);
				if (object == null) return;
				switch (evId) {
				case 1: {
					Contact contact;
					int rankValue;
					try {
						contact = (Contact) decoder.getObjectParm(1, 2, false);
						rankValue = decoder.getUintParm(2, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mContactSearchListener.onNewResult(object, contact, rankValue);
					return;
				}
				}
			}
			break;
		case 19:
			if (mParticipantListener != null) {
				Participant object = (Participant) sidDecodeEventTarget(modId, decoder);
				if (object == null) return;
				switch (evId) {
				case 1: {
					Participant.Dtmf dtmf;
					try {
						dtmf = (Participant.Dtmf) decoder.getEnumParm(1, Participant.Dtmf.get(0), true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mParticipantListener.onIncomingDtmf(object, dtmf);
					return;
				}
				case 146: {
					try {
						decoder.skipMessage();
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mParticipantListener.onLiveSessionVideosChanged(object);
					return;
				}
				}
			}
			break;
		case 18:
			if (mConversationListener != null) {
				Conversation object = (Conversation) sidDecodeEventTarget(modId, decoder);
				if (object == null) return;
				switch (evId) {
				case 1: {
					try {
						decoder.skipMessage();
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mConversationListener.onParticipantListChange(object);
					return;
				}
				case 2: {
					Message message;
					try {
						message = (Message) decoder.getObjectParm(1, 9, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mConversationListener.onMessage(object, message);
					return;
				}
				case 3: {
					Conversation spawned;
					try {
						spawned = (Conversation) decoder.getObjectParm(1, 18, true);
					} catch (IOException e) {
						sidOnFatalError(e);
						return;
					}
					mConversationListener.onSpawnConference(object, spawned);
					return;
				}
				}
			}
			break;
		}
		try {
			decoder.skipMessage();
		} catch (IOException e) {
			sidOnFatalError(e);
		}
	}
	public void registerContactGroupListener(ContactGroupListener listener) {
		mContactGroupListener = listener;
	}
	public void unRegisterContactGroupListener(ContactGroupListener listener) {
		mContactGroupListener = null;
	}
	public ContactGroupListener getContactGroupListener() {
		return mContactGroupListener;
	}
	private ContactGroupListener mContactGroupListener;
	public void registerContactListener(ContactListener listener) {
		mContactListener = listener;
	}
	public void unRegisterContactListener(ContactListener listener) {
		mContactListener = null;
	}
	public ContactListener getContactListener() {
		return mContactListener;
	}
	private ContactListener mContactListener;
	public void registerContactSearchListener(ContactSearchListener listener) {
		mContactSearchListener = listener;
	}
	public void unRegisterContactSearchListener(ContactSearchListener listener) {
		mContactSearchListener = null;
	}
	public ContactSearchListener getContactSearchListener() {
		return mContactSearchListener;
	}
	private ContactSearchListener mContactSearchListener;
	public void registerParticipantListener(ParticipantListener listener) {
		mParticipantListener = listener;
	}
	public void unRegisterParticipantListener(ParticipantListener listener) {
		mParticipantListener = null;
	}
	public ParticipantListener getParticipantListener() {
		return mParticipantListener;
	}
	private ParticipantListener mParticipantListener;
	public void registerConversationListener(ConversationListener listener) {
		mConversationListener = listener;
	}
	public void unRegisterConversationListener(ConversationListener listener) {
		mConversationListener = null;
	}
	public ConversationListener getConversationListener() {
		return mConversationListener;
	}
	private ConversationListener mConversationListener;
	public void registerMessageListener(MessageListener listener) {
		mMessageListener = listener;
	}
	public void unRegisterMessageListener(MessageListener listener) {
		mMessageListener = null;
	}
	public MessageListener getMessageListener() {
		return mMessageListener;
	}
	private MessageListener mMessageListener;
	public void registerVideoListener(VideoListener listener) {
		mVideoListener = listener;
	}
	public void unRegisterVideoListener(VideoListener listener) {
		mVideoListener = null;
	}
	public VideoListener getVideoListener() {
		return mVideoListener;
	}
	private VideoListener mVideoListener;
	public void registerVoicemailListener(VoicemailListener listener) {
		mVoicemailListener = listener;
	}
	public void unRegisterVoicemailListener(VoicemailListener listener) {
		mVoicemailListener = null;
	}
	public VoicemailListener getVoicemailListener() {
		return mVoicemailListener;
	}
	private VoicemailListener mVoicemailListener;
	public void registerSmsListener(SmsListener listener) {
		mSmsListener = listener;
	}
	public void unRegisterSmsListener(SmsListener listener) {
		mSmsListener = null;
	}
	public SmsListener getSmsListener() {
		return mSmsListener;
	}
	private SmsListener mSmsListener;
	public void registerTransferListener(TransferListener listener) {
		mTransferListener = listener;
	}
	public void unRegisterTransferListener(TransferListener listener) {
		mTransferListener = null;
	}
	public TransferListener getTransferListener() {
		return mTransferListener;
	}
	private TransferListener mTransferListener;
	public void registerAccountListener(AccountListener listener) {
		mAccountListener = listener;
	}
	public void unRegisterAccountListener(AccountListener listener) {
		mAccountListener = null;
	}
	public AccountListener getAccountListener() {
		return mAccountListener;
	}
	private AccountListener mAccountListener;
	public void registerSkypeListener(SkypeListener listener) {
		mSkypeListener = listener;
	}
	public void unRegisterSkypeListener(SkypeListener listener) {
		mSkypeListener = null;
	}
	public SkypeListener getSkypeListener() {
		return mSkypeListener;
	}
	private SkypeListener mSkypeListener;
	public SidObject[] sidGetObjects(int modId, int size) {
		switch (modId) {
		case  10: return new ContactGroup[size];
		case   2: return new Contact[size];
		case   1: return new ContactSearch[size];
		case  19: return new Participant[size];
		case  18: return new Conversation[size];
		case   9: return new Message[size];
		case  11: return new Video[size];
		case   7: return new Voicemail[size];
		case  12: return new Sms[size];
		case   6: return new Transfer[size];
		case   5: return new Account[size];
		}
		return null;
	}
	
	protected SidObject sidCreateObject(int modId, int oid) {
		if (oid == 0) return null;
		switch (modId) {
		case  10: return new ContactGroup(oid, this);
		case   2: return new Contact(oid, this);
		case   1: return new ContactSearch(oid, this);
		case  19: return new Participant(oid, this);
		case  18: return new Conversation(oid, this);
		case   9: return new Message(oid, this);
		case  11: return new Video(oid, this);
		case   7: return new Voicemail(oid, this);
		case  12: return new Sms(oid, this);
		case   6: return new Transfer(oid, this);
		case   5: return new Account(oid, this);
		}
		return null;
	}
}
