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
import com.skype.api.Contact;
import com.skype.ipc.SidGetResponding;

/**
 * Represents a local account. Encapsulates methods for Skype account creation, login and logout as well as account profile setting properties. NB! Unlike all the other SkypeKit classes, most of the Account class properties are actually read-write. In fact, there are two sorts of Account's read-write properties: server-side properties and local properties. Different setter methods need to be used for those two kinds. 
 * 
 * The subset of server-side properties consists of all the policy properties (everything with _POLICY suffix) that are all of type int and can be set with SetServersideIntProperty method. There is currently only one server-side string property - OFFLINE_CALLFORWARD which can be set with SetServersideStrProperty setter. 
 * 
 * The set of writeable account profile properties (local profile) is as follows; 
 *  - P_FULLNAME, 
 *  - P_BIRTHDAY, 
 *  - P_GENDER, 
 *  - P_LANGUAGES, 
 *  - P_COUNTRY, 
 *  - P_PROVINCE, 
 *  - P_CITY, 
 *  - P_PHONE_HOME, 
 *  - P_PHONE_OFFICE, 
 *  - P_PHONE_MOBILE, 
 *  - P_EMAILS, 
 *  - P_HOMEPAGE, 
 *  - P_ABOUT, 
 *  - P_MOOD_TEXT, 
 *  - P_TIMEZONE, 
 *  - P_AVATAR_IMAGE, 
 *  - P_RICH_MOOD_TEXT 
 * 
 * These can be set with SetIntProperty, SetStrProperty and SetBinProperty setter methods. Note that there are special methods for changing account online status (availability) and enabling/disabling auto-login - SetAvailability and SetSavePwd. 
 * 
 * Note that to set Account properties, you that Account needs to be logged in. Generally, assumption is that the first thing you do with an Account object after you retrieve it with Skype.GetAccount is login, with exception of creating a new account. 
 * 
 * The Account class has no default constructor and creating an Account instance is not the same as creating a Skype account. To create a Skype account: 
 * 
 *  - obtain an Account instance by invoking Skype.GetAccount. This automatically sets P_SKYPENAME. 
 *  - set any additional profile-related properties. Skype recommends that you minimally set the following: 
 *  - their email address so that they can retrieve a lost password (P_EMAILS) 
 *  - the user's full name (P_FULLNAME) 
 *  - at least one phone number (P_PHONE_HOME, P_PHONE_HOME, P_PHONE_OFFICE) 
 *  - invoke Account.Register to actually create the account 
 */
public final class Account extends SidObject {
	public enum Status implements EnumConverting {
		LOGGED_OUT              (1),
		/** the account is logged out, but password is not needed for re-login */
		LOGGED_OUT_AND_PWD_SAVED(2),
		/** connecting to P2P network */
		CONNECTING_TO_P2P       (3),
		/** connecting to login server */
		CONNECTING_TO_SERVER    (4),
		/** waiting for response from server */
		LOGGING_IN              (5),
		/** response OK. initialising account-specific lib structures */
		INITIALIZING            (6),
		/** alright, we're good to go! */
		LOGGED_IN               (7),
		/** Logout() has been called but not processed yet */
		LOGGING_OUT             (8);
		private final int key;
		Status(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return LOGGED_OUT; }
		public EnumConverting convert(int from) { return Status.get(from); }
		public EnumConverting[] getArray(final int size) { return new Status[size]; }
		public static Status get(int from) {
			switch (from) {
			case 1: return LOGGED_OUT;
			case 2: return LOGGED_OUT_AND_PWD_SAVED;
			case 3: return CONNECTING_TO_P2P;
			case 4: return CONNECTING_TO_SERVER;
			case 5: return LOGGING_IN;
			case 6: return INITIALIZING;
			case 7: return LOGGED_IN;
			case 8: return LOGGING_OUT;
			}
			return LOGGED_OUT;
		}
		public static final int LOGGED_OUT_VALUE               = 1;
		public static final int LOGGED_OUT_AND_PWD_SAVED_VALUE = 2;
		public static final int CONNECTING_TO_P2P_VALUE        = 3;
		public static final int CONNECTING_TO_SERVER_VALUE     = 4;
		public static final int LOGGING_IN_VALUE               = 5;
		public static final int INITIALIZING_VALUE             = 6;
		public static final int LOGGED_IN_VALUE                = 7;
		public static final int LOGGING_OUT_VALUE              = 8;
	}
	/**
	 * Recognized values for the P_CBLSYNCSTATUS property. CBL stands for Central Buddy List. In principle, this property and its states can be ignored by most UI developers. However, it can help to optimize UI buildup and behaviour, particularly in case of limited resources (such as mobile devices). 
	 * 
	 * CBL is used to backup your contact list, contact groups, and profile information, and also used to synchronize this information with other Skype instances of your account (i.e. on another device). CBL sync can occur both during login and during normal operation. Note that CBL synchronization does not take place immediately after an Account property is changed. A delay between the first property change and CBL sync initiation enables the client to accumulate changes and do the synchronization in bulk. 
	 * 
	 * Clients with limited resources might want to wait for CBL_IN_SYNC status before generating their UI's contact list representation. Otherwise it might be forced to redraw the contact list multiple times, as new updates get retrieved from the server-side. Similarly, applications that modify an account's mood message might want to know when the P_MOOD_TEXT or P_RICH_MOOD_TEXT property is synchronized to the server. Note that this sync is only for CBL and other logged in instances of the same account - other contacts will receive the mood message update directly. 
	 */
	public enum CblSyncStatus implements EnumConverting {
		/** status is not clear (yet) */
		CBL_INITIALIZING        (0),
		/** first sync with empty profile */
		CBL_INITIAL_SYNC_PENDING(1),
		/** Account properties are considered to be out of sync with CBL - attempt at synchronization is imminent. You might wish to wait with updating UI components that display the data that is about to change anyway. */
		CBL_SYNC_PENDING        (2),
		/** CBL synchronization is currently taking place. */
		CBL_SYNC_IN_PROGRESS    (3),
		/** Account properties are up-to-date. */
		CBL_IN_SYNC             (4),
		/** CBL sync has. Another attempt will be made after several minutes. If a second attempt also fails, subsequent attempts at synchronization will be made at ever increasing intervals. */
		CBL_SYNC_FAILED         (5),
		/** we have received a hint that there is a remote data change in CBL */
		CBL_REMOTE_SYNC_PENDING (6);
		private final int key;
		CblSyncStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return CBL_INITIALIZING; }
		public EnumConverting convert(int from) { return CblSyncStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new CblSyncStatus[size]; }
		public static CblSyncStatus get(int from) {
			switch (from) {
			case 0: return CBL_INITIALIZING;
			case 1: return CBL_INITIAL_SYNC_PENDING;
			case 2: return CBL_SYNC_PENDING;
			case 3: return CBL_SYNC_IN_PROGRESS;
			case 4: return CBL_IN_SYNC;
			case 5: return CBL_SYNC_FAILED;
			case 6: return CBL_REMOTE_SYNC_PENDING;
			}
			return CBL_INITIALIZING;
		}
		public static final int CBL_INITIALIZING_VALUE         = 0;
		public static final int CBL_INITIAL_SYNC_PENDING_VALUE = 1;
		public static final int CBL_SYNC_PENDING_VALUE         = 2;
		public static final int CBL_SYNC_IN_PROGRESS_VALUE     = 3;
		public static final int CBL_IN_SYNC_VALUE              = 4;
		public static final int CBL_SYNC_FAILED_VALUE          = 5;
		public static final int CBL_REMOTE_SYNC_PENDING_VALUE  = 6;
	}
	/** Recognized values for the Account.P_LOGOUTREASON. Note that this property should only be examined when Account.P_STATUS is LOGGED_OUT or LOGGED_OUT_AND_PWD_SAVED. That is, you should not monitor changes to this property in Account.OnChange callback, other than after already having checked that P_STATUS property has appropriate value. The reason for this is that the LOGOUTREASON does not get reset before the next login attempt. For example: if a user tried to log in with a wrong password, the LOGOUTREASON gets set to INCORRECT_PASSWORD. Now, if the user tries to log in again, and yet again submits an incorrect password, the value of the LOGOUTREASON does not change anymore, because it already is set to INCORRECT_PASSWORD. Consequently, Account.OnChange will not fire in this case.  */	public enum LogoutReason implements EnumConverting {
		/** manual logout (or unknown reason from previous session) */
		LOGOUT_CALLED             (1),
		/** sync errors at login/registration */
		HTTPS_PROXY_AUTH_FAILED   (2),
		/** sync errors at login/registration */
		SOCKS_PROXY_AUTH_FAILED   (3),
		/** sync errors at login/registration */
		P2P_CONNECT_FAILED        (4),
		/** sync errors at login/registration */
		SERVER_CONNECT_FAILED     (5),
		/** sync errors at login/registration */
		SERVER_OVERLOADED         (6),
		/** sync errors at login/registration */
		DB_IN_USE                 (7),
		/** sync errors at registration */
		INVALID_SKYPENAME         (8),
		/** sync errors at registration */
		INVALID_EMAIL             (9),
		/** sync errors at registration */
		UNACCEPTABLE_PASSWORD     (10),
		/** sync errors at registration */
		SKYPENAME_TAKEN           (11),
		/** sync errors at registration */
		REJECTED_AS_UNDERAGE      (12),
		/** sync errors at login */
		NO_SUCH_IDENTITY          (13),
		/** sync errors at login */
		INCORRECT_PASSWORD        (14),
		/** sync errors at login */
		TOO_MANY_LOGIN_ATTEMPTS   (15),
		/** async errors (can happen anytime while logged in) */
		PASSWORD_HAS_CHANGED      (16),
		/** async errors (can happen anytime while logged in) */
		PERIODIC_UIC_UPDATE_FAILED(17),
		/** async errors (can happen anytime while logged in) */
		DB_DISK_FULL              (18),
		/** async errors (can happen anytime while logged in) */
		DB_IO_ERROR               (19),
		/** async errors (can happen anytime while logged in) */
		DB_CORRUPT                (20),
		/** deprecated (superceded by more detailed DB_* errors) */
		DB_FAILURE                (21),
		/** platform sdk */
		INVALID_APP_ID            (22),
		/** platform sdk */
		APP_ID_FAILURE            (23),
		/** forced upgrade/discontinuation */
		UNSUPPORTED_VERSION       (24),
		/** ATO (Account TakeOver) detected, account blocked */
		ATO_BLOCKED               (25),
		/** logout from another instance */
		REMOTE_LOGOUT             (26);
		private final int key;
		LogoutReason(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return LOGOUT_CALLED; }
		public EnumConverting convert(int from) { return LogoutReason.get(from); }
		public EnumConverting[] getArray(final int size) { return new LogoutReason[size]; }
		public static LogoutReason get(int from) {
			switch (from) {
			case  1: return LOGOUT_CALLED;
			case  2: return HTTPS_PROXY_AUTH_FAILED;
			case  3: return SOCKS_PROXY_AUTH_FAILED;
			case  4: return P2P_CONNECT_FAILED;
			case  5: return SERVER_CONNECT_FAILED;
			case  6: return SERVER_OVERLOADED;
			case  7: return DB_IN_USE;
			case  8: return INVALID_SKYPENAME;
			case  9: return INVALID_EMAIL;
			case 10: return UNACCEPTABLE_PASSWORD;
			case 11: return SKYPENAME_TAKEN;
			case 12: return REJECTED_AS_UNDERAGE;
			case 13: return NO_SUCH_IDENTITY;
			case 14: return INCORRECT_PASSWORD;
			case 15: return TOO_MANY_LOGIN_ATTEMPTS;
			case 16: return PASSWORD_HAS_CHANGED;
			case 17: return PERIODIC_UIC_UPDATE_FAILED;
			case 18: return DB_DISK_FULL;
			case 19: return DB_IO_ERROR;
			case 20: return DB_CORRUPT;
			case 21: return DB_FAILURE;
			case 22: return INVALID_APP_ID;
			case 23: return APP_ID_FAILURE;
			case 24: return UNSUPPORTED_VERSION;
			case 25: return ATO_BLOCKED;
			case 26: return REMOTE_LOGOUT;
			}
			return LOGOUT_CALLED;
		}
		public static final int LOGOUT_CALLED_VALUE              =  1;
		public static final int HTTPS_PROXY_AUTH_FAILED_VALUE    =  2;
		public static final int SOCKS_PROXY_AUTH_FAILED_VALUE    =  3;
		public static final int P2P_CONNECT_FAILED_VALUE         =  4;
		public static final int SERVER_CONNECT_FAILED_VALUE      =  5;
		public static final int SERVER_OVERLOADED_VALUE          =  6;
		public static final int DB_IN_USE_VALUE                  =  7;
		public static final int INVALID_SKYPENAME_VALUE          =  8;
		public static final int INVALID_EMAIL_VALUE              =  9;
		public static final int UNACCEPTABLE_PASSWORD_VALUE      = 10;
		public static final int SKYPENAME_TAKEN_VALUE            = 11;
		public static final int REJECTED_AS_UNDERAGE_VALUE       = 12;
		public static final int NO_SUCH_IDENTITY_VALUE           = 13;
		public static final int INCORRECT_PASSWORD_VALUE         = 14;
		public static final int TOO_MANY_LOGIN_ATTEMPTS_VALUE    = 15;
		public static final int PASSWORD_HAS_CHANGED_VALUE       = 16;
		public static final int PERIODIC_UIC_UPDATE_FAILED_VALUE = 17;
		public static final int DB_DISK_FULL_VALUE               = 18;
		public static final int DB_IO_ERROR_VALUE                = 19;
		public static final int DB_CORRUPT_VALUE                 = 20;
		public static final int DB_FAILURE_VALUE                 = 21;
		public static final int INVALID_APP_ID_VALUE             = 22;
		public static final int APP_ID_FAILURE_VALUE             = 23;
		public static final int UNSUPPORTED_VERSION_VALUE        = 24;
		public static final int ATO_BLOCKED_VALUE                = 25;
		public static final int REMOTE_LOGOUT_VALUE              = 26;
	}
	/**
	 * Recognized values for the P_PWDCHANGESTATUS property that provides information on whether a password change succeeded or failed, giving detailed failure reason. After successful return from the Change Password method, clients should monitor the P_PWDCHANGESTATUS property changes. 
	 *  - PWD_CHANGING - consider displaying an "in progress" indicator and continue polling 
	 *  - PWD_OK - consider displaying an updated indicator and stop polling 
	 *  - PWD_OK_BUT_CHANGE_SUGGESTED - consider displaying an updated indicator, along with a recommendation to change again to a stronger password. 
	 */
	public enum PwdChangeStatus implements EnumConverting {
		/** Password change succeeded.  */
		PWD_OK                     (0),
		/** Password change is in progress.  */
		PWD_CHANGING               (1),
		/** Old password was incorrect.  */
		PWD_INVALID_OLD_PASSWORD   (2),
		/** Failed to verify password because of no connection to server.  */
		PWD_SERVER_CONNECT_FAILED  (3),
		/** Password was set but server didn't like it much.  */
		PWD_OK_BUT_CHANGE_SUGGESTED(4),
		/** New password was exactly the same as old one.  */
		PWD_MUST_DIFFER_FROM_OLD   (5),
		/** The new password was unacceptable. (too short, too simple, etc.)  */
		PWD_INVALID_NEW_PWD        (6),
		/** Account was currently not logged in.  */
		PWD_MUST_LOG_IN_TO_CHANGE  (7);
		private final int key;
		PwdChangeStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return PWD_OK; }
		public EnumConverting convert(int from) { return PwdChangeStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new PwdChangeStatus[size]; }
		public static PwdChangeStatus get(int from) {
			switch (from) {
			case 0: return PWD_OK;
			case 1: return PWD_CHANGING;
			case 2: return PWD_INVALID_OLD_PASSWORD;
			case 3: return PWD_SERVER_CONNECT_FAILED;
			case 4: return PWD_OK_BUT_CHANGE_SUGGESTED;
			case 5: return PWD_MUST_DIFFER_FROM_OLD;
			case 6: return PWD_INVALID_NEW_PWD;
			case 7: return PWD_MUST_LOG_IN_TO_CHANGE;
			}
			return PWD_OK;
		}
		public static final int PWD_OK_VALUE                      = 0;
		public static final int PWD_CHANGING_VALUE                = 1;
		public static final int PWD_INVALID_OLD_PASSWORD_VALUE    = 2;
		public static final int PWD_SERVER_CONNECT_FAILED_VALUE   = 3;
		public static final int PWD_OK_BUT_CHANGE_SUGGESTED_VALUE = 4;
		public static final int PWD_MUST_DIFFER_FROM_OLD_VALUE    = 5;
		public static final int PWD_INVALID_NEW_PWD_VALUE         = 6;
		public static final int PWD_MUST_LOG_IN_TO_CHANGE_VALUE   = 7;
	}
	/** The list of possible values of Account class COMMITSTATUS property. Note that this property and its values have nothing to do with (automatic) CBL synchronization. Rather, the COMMITSTATUS reflects commit status to account's server side properties initiated with calls to Account class SetServersideIntProperty and Account class SetServersideStrProperty methods. After those methods, your client UI may want to wait until the COMMITSTATUS becomes COMMITTING_TO_SERVER followed by COMMITTED and inform the user if the value becomes COMMIT_FAILED. SetServerside<type>Property methods are used for writing privacy policy related and call forwarding related Account properties to the server. Unlike CBL synchronization, those updates are executed immediately.  */	public enum CommitStatus implements EnumConverting {
		/** No pending updates to the server.  */
		COMMITTED           (1),
		/** Update to the server in progress.  */
		COMMITTING_TO_SERVER(2),
		/** Server update has failed.  */
		COMMIT_FAILED       (3);
		private final int key;
		CommitStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return COMMITTED; }
		public EnumConverting convert(int from) { return CommitStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new CommitStatus[size]; }
		public static CommitStatus get(int from) {
			switch (from) {
			case 1: return COMMITTED;
			case 2: return COMMITTING_TO_SERVER;
			case 3: return COMMIT_FAILED;
			}
			return COMMITTED;
		}
		public static final int COMMITTED_VALUE            = 1;
		public static final int COMMITTING_TO_SERVER_VALUE = 2;
		public static final int COMMIT_FAILED_VALUE        = 3;
	}
	/** Recognized values for the P_CHAT_POLICY property that controls whether non-authorized users can initiate text chat with the currently logged in account. Note that since this set of values is associated with a server-side property, you must set that property using Account.SetServersideIntProperty  */
	public enum ChatPolicy implements EnumConverting {
		/** Unauthorized contacts can initiate text chat.  */
		EVERYONE_CAN_ADD             (0),
		/** Only authorized contacts can initiate chat (default policy).  */
		BUDDIES_OR_AUTHORIZED_CAN_ADD(2);
		private final int key;
		ChatPolicy(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return EVERYONE_CAN_ADD; }
		public EnumConverting convert(int from) { return ChatPolicy.get(from); }
		public EnumConverting[] getArray(final int size) { return new ChatPolicy[size]; }
		public static ChatPolicy get(int from) {
			switch (from) {
			case 0: return EVERYONE_CAN_ADD;
			case 2: return BUDDIES_OR_AUTHORIZED_CAN_ADD;
			}
			return EVERYONE_CAN_ADD;
		}
		public static final int EVERYONE_CAN_ADD_VALUE              = 0;
		public static final int BUDDIES_OR_AUTHORIZED_CAN_ADD_VALUE = 2;
	}
	/** Recognized values for the P_SKYPECALLPOLICY property that controls acceptance of incoming Skype calls. Note that since this set of values is associated with a server-side property, you must set that property using Account.SetServersideIntPropertyserver-side.  */
	public enum SkypeCallPolicy implements EnumConverting {
		/** Skype calls accepted from unauthorized contacts. */
		EVERYONE_CAN_CALL             (0),
		/** Skype calls not accepted from unauthorized contacts. */
		BUDDIES_OR_AUTHORIZED_CAN_CALL(2);
		private final int key;
		SkypeCallPolicy(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return EVERYONE_CAN_CALL; }
		public EnumConverting convert(int from) { return SkypeCallPolicy.get(from); }
		public EnumConverting[] getArray(final int size) { return new SkypeCallPolicy[size]; }
		public static SkypeCallPolicy get(int from) {
			switch (from) {
			case 0: return EVERYONE_CAN_CALL;
			case 2: return BUDDIES_OR_AUTHORIZED_CAN_CALL;
			}
			return EVERYONE_CAN_CALL;
		}
		public static final int EVERYONE_CAN_CALL_VALUE              = 0;
		public static final int BUDDIES_OR_AUTHORIZED_CAN_CALL_VALUE = 2;
	}
	/** Recognized values for the P_PSTNCALLPOLICY property that controls whether (and from whom) this account accepts incoming PSTN calls. Note that since this set of values is associated with a server-side property, you must set that property using Account.SetServersideIntProperty  */
	public enum PstnCallPolicy implements EnumConverting {
		/** All incoming PSTN calls are accepted. */
		ALL_NUMBERS_CAN_CALL      (0),
		/** Only PSTN calls that report caller ID are accepted. */
		DISCLOSED_NUMBERS_CAN_CALL(1),
		/** Only calls from PSTN numbers found in local contact list are accepted. */
		BUDDY_NUMBERS_CAN_CALL    (2);
		private final int key;
		PstnCallPolicy(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return ALL_NUMBERS_CAN_CALL; }
		public EnumConverting convert(int from) { return PstnCallPolicy.get(from); }
		public EnumConverting[] getArray(final int size) { return new PstnCallPolicy[size]; }
		public static PstnCallPolicy get(int from) {
			switch (from) {
			case 0: return ALL_NUMBERS_CAN_CALL;
			case 1: return DISCLOSED_NUMBERS_CAN_CALL;
			case 2: return BUDDY_NUMBERS_CAN_CALL;
			}
			return ALL_NUMBERS_CAN_CALL;
		}
		public static final int ALL_NUMBERS_CAN_CALL_VALUE       = 0;
		public static final int DISCLOSED_NUMBERS_CAN_CALL_VALUE = 1;
		public static final int BUDDY_NUMBERS_CAN_CALL_VALUE     = 2;
	}
	/**
	 * Recognized values for the P_AVATAR_POLICY property that controls whether remote contacts can view local account's avatar image. Note that since this set of values is associated with a server-side property, you must set that property using Account.SetServersideIntPropertyserver-side. 
	 * Note that setting account's AVATAR_POLICY to BUDDIES_OR_AUTHORIZED_CAN_SEE does not guarantee that remote users will be able to immediately retrieve the avatar picture via corresponding Contact object. Avatar changes propagate between clients only when direct sessions between clients are established. Direct sessions are established during live sessions or whilst online contacts are engaged in chat. 
	 */
	public enum AvatarPolicy implements EnumConverting {
		/** Only authorized remote users can see this user's avatar image */
		BUDDIES_OR_AUTHORIZED_CAN_SEE(0),
		/** Everyone can see this user's avatar image, once the contact/account avatar property has been synchronized during a direct session. The avatar image may also become viewable on some Skype Web-based services. */
		EVERYONE_CAN_SEE             (2);
		private final int key;
		AvatarPolicy(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return BUDDIES_OR_AUTHORIZED_CAN_SEE; }
		public EnumConverting convert(int from) { return AvatarPolicy.get(from); }
		public EnumConverting[] getArray(final int size) { return new AvatarPolicy[size]; }
		public static AvatarPolicy get(int from) {
			switch (from) {
			case 0: return BUDDIES_OR_AUTHORIZED_CAN_SEE;
			case 2: return EVERYONE_CAN_SEE;
			}
			return BUDDIES_OR_AUTHORIZED_CAN_SEE;
		}
		public static final int BUDDIES_OR_AUTHORIZED_CAN_SEE_VALUE = 0;
		public static final int EVERYONE_CAN_SEE_VALUE              = 2;
	}
	/**
	 * Recognized values for the P_BUDDYCOUNT_POLICY property that controls whether the number of this user's authorized contacts is visible to other users, either through Account.GetPropNrofAuthedBuddies or Contact.GetPropNrofAuthedBuddies when those instances reference this user. Note that since this set of values is associated with a server-side property, you must set that property using Account.SetServersideIntProperty, like this: 
	 * account->SetServersideIntProperty(Account.P_BUDDYCOUNT_POLICY, Account.DISCLOSE_TO_AUTHORIZED); 
	 * account->SetServersideIntProperty(Account.P_BUDDYCOUNT_POLICY, Account.DISCLOSE_TO_NOONE ); 
	 */
	public enum BuddyCountPolicy implements EnumConverting {
		/** Authorized remote users can retrieve the number of this user's authorized contacts (Contact.P_NROF_AUTHED_BUDDIES) */
		DISCLOSE_TO_AUTHORIZED(0),
		/** No remote user - regardless their authorization status - can retrieve the number of this user's authorized contacts. Account.GetPropNrofAuthedBuddies and Contact.GetPropNrofAuthedBuddies will always return 0 */
		DISCLOSE_TO_NOONE     (1);
		private final int key;
		BuddyCountPolicy(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return DISCLOSE_TO_AUTHORIZED; }
		public EnumConverting convert(int from) { return BuddyCountPolicy.get(from); }
		public EnumConverting[] getArray(final int size) { return new BuddyCountPolicy[size]; }
		public static BuddyCountPolicy get(int from) {
			switch (from) {
			case 0: return DISCLOSE_TO_AUTHORIZED;
			case 1: return DISCLOSE_TO_NOONE;			}
			return DISCLOSE_TO_AUTHORIZED;
		}
		public static final int DISCLOSE_TO_AUTHORIZED_VALUE = 0;
		public static final int DISCLOSE_TO_NOONE_VALUE      = 1;
	}
	/** Recognized values for the P_TIMEZONEPOLICY property that sets the rules for timezone offset so remote clients can determine your local time. Note that since this set of values is associated with a server-side property, you must set that property using Account.SetServersideIntPropertyserver-side. */
	public enum TimezonePolicy implements EnumConverting {
		/** Account's timezone setting is determined automatically.  */
		TZ_AUTOMATIC  (0),
		/** Account's timezone setting is set manually.  */
		TZ_MANUAL     (1),
		/** Remote users will have no idea what your local time is.  */
		TZ_UNDISCLOSED(2);
		private final int key;
		TimezonePolicy(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return TZ_AUTOMATIC; }
		public EnumConverting convert(int from) { return TimezonePolicy.get(from); }
		public EnumConverting[] getArray(final int size) { return new TimezonePolicy[size]; }
		public static TimezonePolicy get(int from) {
			switch (from) {
			case 0: return TZ_AUTOMATIC;
			case 1: return TZ_MANUAL;
			case 2: return TZ_UNDISCLOSED;
			}
			return TZ_AUTOMATIC;
		}
		public static final int TZ_AUTOMATIC_VALUE   = 0;
		public static final int TZ_MANUAL_VALUE      = 1;
		public static final int TZ_UNDISCLOSED_VALUE = 2;
	}
	/** Recognized values for the P_WEBPRESENCEPOLICY property that controls whether your online status (presence) can be seen using the "Skype buttons" ( http://www.skype.com/share/buttons/ ) embedded in web pages. Note that since this set of values is associated with a server-side property, you must set that property using Account.SetServersideIntPropertyserver-side.  */
	public enum WebPresencePolicy implements EnumConverting {
		/** Disable displaying online status on web for this account. */
		WEBPRESENCE_DISABLED(0),
		/** Enable displaying online status on web for this account. */
		WEBPRESENCE_ENABLED (1);
		private final int key;
		WebPresencePolicy(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return WEBPRESENCE_DISABLED; }
		public EnumConverting convert(int from) { return WebPresencePolicy.get(from); }
		public EnumConverting[] getArray(final int size) { return new WebPresencePolicy[size]; }
		public static WebPresencePolicy get(int from) {
			switch (from) {
			case 0: return WEBPRESENCE_DISABLED;
			case 1: return WEBPRESENCE_ENABLED;
			}
			return WEBPRESENCE_DISABLED;
		}
		public static final int WEBPRESENCE_DISABLED_VALUE = 0;
		public static final int WEBPRESENCE_ENABLED_VALUE  = 1;
	}
	/** Recognized values for the P_PHONENUMBERSPOLICY property that controls whether unauthorized remote users can see associated phone numbers in their UI (for reference, see the different phone number tabs in Windows desktop Client contact view). Note that since this set of values is associated with a server-side property, you must set that property using Account.SetServersideIntProperty  */
	public enum PhoneNumbersPolicy implements EnumConverting {
		/** Only authorized contacts can see the phone numbers. */
		PHONENUMBERS_VISIBLE_FOR_BUDDIES (0),
		/** Everyone can see the phone numbers. */
		PHONENUMBERS_VISIBLE_FOR_EVERYONE(1);
		private final int key;
		PhoneNumbersPolicy(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return PHONENUMBERS_VISIBLE_FOR_BUDDIES; }
		public EnumConverting convert(int from) { return PhoneNumbersPolicy.get(from); }
		public EnumConverting[] getArray(final int size) { return new PhoneNumbersPolicy[size]; }
		public static PhoneNumbersPolicy get(int from) {
			switch (from) {
			case 0: return PHONENUMBERS_VISIBLE_FOR_BUDDIES;
			case 1: return PHONENUMBERS_VISIBLE_FOR_EVERYONE;
			}
			return PHONENUMBERS_VISIBLE_FOR_BUDDIES;
		}
		public static final int PHONENUMBERS_VISIBLE_FOR_BUDDIES_VALUE  = 0;
		public static final int PHONENUMBERS_VISIBLE_FOR_EVERYONE_VALUE = 1;
	}
	/** Recognized values for the P_VOICEMAILPOLICY property that controls acceptance of incoming voicemail messages. Note that since this set of values is associated with a server-side property, you must set that property using Account.SetServersideIntPropertyserver-side.  */
	public enum VoicemailPolicy implements EnumConverting {
		/** Incoming voicemails enabled. */
		VOICEMAIL_ENABLED (0),
		/** Incoming voicemails disabled. */
		VOICEMAIL_DISABLED(1);
		private final int key;
		VoicemailPolicy(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return VOICEMAIL_ENABLED; }
		public EnumConverting convert(int from) { return VoicemailPolicy.get(from); }
		public EnumConverting[] getArray(final int size) { return new VoicemailPolicy[size]; }
		public static VoicemailPolicy get(int from) {
			switch (from) {
			case 0: return VOICEMAIL_ENABLED;
			case 1: return VOICEMAIL_DISABLED;
			}
			return VOICEMAIL_ENABLED;
		}
		public static final int VOICEMAIL_ENABLED_VALUE  = 0;
		public static final int VOICEMAIL_DISABLED_VALUE = 1;
	}
	/** Account capabability statuses are possible values of Contact class CAPABILITY enumerator, when that enumerator is used in context of account. Compared to Contact class CAPABILITYSTATUS enums, Account class CAPABILITYSTATUS has additional items for subscription expiration warnings.  */
	public enum CapabilityStatus implements EnumConverting {
		/** Capability is not supported by the currently logged in SkypeKit client. */
		NO_CAPABILITY        (0),
		/** Capability is supported by the currently logged in SkypeKit client.  */
		CAPABILITY_EXISTS    (1),
		/** Support for this capability ends this month (within 30 days)  */
		FIRST_EXPIRY_WARNING (2),
		/** Support for this capability ends this week (within 7 days)   */
		SECOND_EXPIRY_WARNING(3),
		/** Support for this capability ends today  */
		FINAL_EXPIRY_WARNING (4);
		private final int key;
		CapabilityStatus(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return NO_CAPABILITY; }
		public EnumConverting convert(int from) { return CapabilityStatus.get(from); }
		public EnumConverting[] getArray(final int size) { return new CapabilityStatus[size]; }
		public static CapabilityStatus get(int from) {
			switch (from) {
			case 0: return NO_CAPABILITY;
			case 1: return CAPABILITY_EXISTS;
			case 2: return FIRST_EXPIRY_WARNING;
			case 3: return SECOND_EXPIRY_WARNING;
			case 4: return FINAL_EXPIRY_WARNING;
			}
			return NO_CAPABILITY;
		}
		public static final int NO_CAPABILITY_VALUE         = 0;
		public static final int CAPABILITY_EXISTS_VALUE     = 1;
		public static final int FIRST_EXPIRY_WARNING_VALUE  = 2;
		public static final int SECOND_EXPIRY_WARNING_VALUE = 3;
		public static final int FINAL_EXPIRY_WARNING_VALUE  = 4;
	}
	private final static byte[] P_STATUS_req = {(byte) 90,(byte) 71,(byte) 70,(byte) 93,(byte) 5};
	private final static byte[] P_PWD_CHANGE_STATUS_req = {(byte) 90,(byte) 71,(byte) 71,(byte) 93,(byte) 5};
	private final static byte[] P_LOGOUT_REASON_req = {(byte) 90,(byte) 71,(byte) 73,(byte) 93,(byte) 5};
	private final static byte[] P_COMMIT_STATUS_req = {(byte) 90,(byte) 71,(byte) 78,(byte) 93,(byte) 5};
	private final static byte[] P_SUGGESTED_SKYPE_NAME_req = {(byte) 90,(byte) 71,(byte) 72,(byte) 93,(byte) 5};
	private final static byte[] P_SKYPEOUT_BALANCE_CURRENCY_req = {(byte) 90,(byte) 71,(byte) 74,(byte) 93,(byte) 5};
	private final static byte[] P_SKYPEOUT_BALANCE_req = {(byte) 90,(byte) 71,(byte) 75,(byte) 93,(byte) 5};
	private final static byte[] P_SKYPEOUT_PRECISION_req = {(byte) 90,(byte) 71,(byte) 164,(byte) 6,(byte) 93,(byte) 5};
	private final static byte[] P_SKYPEIN_NUMBERS_req = {(byte) 90,(byte) 71,(byte) 76,(byte) 93,(byte) 5};
	private final static byte[] P_CBL_SYNC_STATUS_req = {(byte) 90,(byte) 71,(byte) 79,(byte) 93,(byte) 5};
	private final static byte[] P_OFFLINE_CALL_FORWARD_req = {(byte) 90,(byte) 71,(byte) 77,(byte) 93,(byte) 5};	private final static byte[] P_CHAT_POLICY_req = {(byte) 90,(byte) 71,(byte) 160,(byte) 1,(byte) 93,(byte) 5};
	private final static byte[] P_SKYPE_CALL_POLICY_req = {(byte) 90,(byte) 71,(byte) 161,(byte) 1,(byte) 93,(byte) 5};
	private final static byte[] P_PSTN_CALL_POLICY_req = {(byte) 90,(byte) 71,(byte) 162,(byte) 1,(byte) 93,(byte) 5};
	private final static byte[] P_AVATAR_POLICY_req = {(byte) 90,(byte) 71,(byte) 163,(byte) 1,(byte) 93,(byte) 5};
	private final static byte[] P_BUDDY_COUNT_POLICY_req = {(byte) 90,(byte) 71,(byte) 164,(byte) 1,(byte) 93,(byte) 5};
	private final static byte[] P_TIMEZONE_POLICY_req = {(byte) 90,(byte) 71,(byte) 165,(byte) 1,(byte) 93,(byte) 5};
	private final static byte[] P_WEB_PRESENCE_POLICY_req = {(byte) 90,(byte) 71,(byte) 166,(byte) 1,(byte) 93,(byte) 5};
	private final static byte[] P_PHONE_NUMBERS_POLICY_req = {(byte) 90,(byte) 71,(byte) 168,(byte) 1,(byte) 93,(byte) 5};
	private final static byte[] P_VOICEMAIL_POLICY_req = {(byte) 90,(byte) 71,(byte) 169,(byte) 1,(byte) 93,(byte) 5};
	private final static byte[] P_PARTNER_OPTEDOUT_req = {(byte) 90,(byte) 71,(byte) 133,(byte) 6,(byte) 93,(byte) 5};
	private final static byte[] P_SERVICE_PROVIDER_INFO_req = {(byte) 90,(byte) 71,(byte) 160,(byte) 6,(byte) 93,(byte) 5};
	private final static byte[] P_REGISTRATION_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 161,(byte) 6,(byte) 93,(byte) 5};
	private final static byte[] P_OTHER_INSTANCES_COUNT_req = {(byte) 90,(byte) 71,(byte) 162,(byte) 6,(byte) 93,(byte) 5};
	private final static byte[] P_SKYPE_NAME_req = {(byte) 90,(byte) 71,(byte) 4,(byte) 93,(byte) 5};
	private final static byte[] P_FULL_NAME_req = {(byte) 90,(byte) 71,(byte) 5,(byte) 93,(byte) 5};
	private final static byte[] P_BIRTHDAY_req = {(byte) 90,(byte) 71,(byte) 7,(byte) 93,(byte) 5};
	private final static byte[] P_GENDER_req = {(byte) 90,(byte) 71,(byte) 8,(byte) 93,(byte) 5};
	private final static byte[] P_LANGUAGES_req = {(byte) 90,(byte) 71,(byte) 9,(byte) 93,(byte) 5};
	private final static byte[] P_COUNTRY_req = {(byte) 90,(byte) 71,(byte) 10,(byte) 93,(byte) 5};
	private final static byte[] P_PROVINCE_req = {(byte) 90,(byte) 71,(byte) 11,(byte) 93,(byte) 5};
	private final static byte[] P_CITY_req = {(byte) 90,(byte) 71,(byte) 12,(byte) 93,(byte) 5};
	private final static byte[] P_PHONE_HOME_req = {(byte) 90,(byte) 71,(byte) 13,(byte) 93,(byte) 5};
	private final static byte[] P_PHONE_OFFICE_req = {(byte) 90,(byte) 71,(byte) 14,(byte) 93,(byte) 5};
	private final static byte[] P_PHONE_MOBILE_req = {(byte) 90,(byte) 71,(byte) 15,(byte) 93,(byte) 5};
	private final static byte[] P_EMAILS_req = {(byte) 90,(byte) 71,(byte) 16,(byte) 93,(byte) 5};
	private final static byte[] P_HOMEPAGE_req = {(byte) 90,(byte) 71,(byte) 17,(byte) 93,(byte) 5};
	private final static byte[] P_ABOUT_req = {(byte) 90,(byte) 71,(byte) 18,(byte) 93,(byte) 5};
	private final static byte[] P_PROFILE_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 19,(byte) 93,(byte) 5};
	private final static byte[] P_MOOD_TEXT_req = {(byte) 90,(byte) 71,(byte) 26,(byte) 93,(byte) 5};
	private final static byte[] P_TIMEZONE_req = {(byte) 90,(byte) 71,(byte) 27,(byte) 93,(byte) 5};
	private final static byte[] P_NROF_AUTHED_BUDDIES_req = {(byte) 90,(byte) 71,(byte) 28,(byte) 93,(byte) 5};
	private final static byte[] P_AVAILABILITY_req = {(byte) 90,(byte) 71,(byte) 34,(byte) 93,(byte) 5};
	private final static byte[] P_AVATAR_IMAGE_req = {(byte) 90,(byte) 71,(byte) 37,(byte) 93,(byte) 5};
	private final static byte[] P_AVATAR_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 182,(byte) 1,(byte) 93,(byte) 5};
	private final static byte[] P_MOOD_TIMESTAMP_req = {(byte) 90,(byte) 71,(byte) 183,(byte) 1,(byte) 93,(byte) 5};
	private final static byte[] P_RICH_MOOD_TEXT_req = {(byte) 90,(byte) 71,(byte) 205,(byte) 1,(byte) 93,(byte) 5};
	/** Properties of the Account class */
	public enum Property implements PropertyEnumConverting {
		P_UNKNOWN                  (0,0,null,0,null),
		P_STATUS                   (70, 1, P_STATUS_req, 0, Status.get(0)),
		P_PWD_CHANGE_STATUS        (71, 2, P_PWD_CHANGE_STATUS_req, 0, PwdChangeStatus.get(0)),
		P_LOGOUT_REASON            (73, 3, P_LOGOUT_REASON_req, 0, LogoutReason.get(0)),
		P_COMMIT_STATUS            (78, 4, P_COMMIT_STATUS_req, 0, CommitStatus.get(0)),
		P_SUGGESTED_SKYPE_NAME     (72, 5, P_SUGGESTED_SKYPE_NAME_req, 0, null),
		P_SKYPEOUT_BALANCE_CURRENCY(74, 6, P_SKYPEOUT_BALANCE_CURRENCY_req, 0, null),
		P_SKYPEOUT_BALANCE         (75, 7, P_SKYPEOUT_BALANCE_req, 0, null),
		P_SKYPEOUT_PRECISION       (804, 8, P_SKYPEOUT_PRECISION_req, 0, null),
		P_SKYPEIN_NUMBERS          (76, 9, P_SKYPEIN_NUMBERS_req, 0, null),
		P_CBL_SYNC_STATUS          (79, 10, P_CBL_SYNC_STATUS_req, 0, CblSyncStatus.get(0)),
		P_OFFLINE_CALL_FORWARD     (77, 11, P_OFFLINE_CALL_FORWARD_req, 0, null),
		P_CHAT_POLICY              (160, 12, P_CHAT_POLICY_req, 0, ChatPolicy.get(0)),
		P_SKYPE_CALL_POLICY        (161, 13, P_SKYPE_CALL_POLICY_req, 0, SkypeCallPolicy.get(0)),
		P_PSTN_CALL_POLICY         (162, 14, P_PSTN_CALL_POLICY_req, 0, PstnCallPolicy.get(0)),
		P_AVATAR_POLICY            (163, 15, P_AVATAR_POLICY_req, 0, AvatarPolicy.get(0)),
		P_BUDDY_COUNT_POLICY       (164, 16, P_BUDDY_COUNT_POLICY_req, 0, BuddyCountPolicy.get(0)),
		P_TIMEZONE_POLICY          (165, 17, P_TIMEZONE_POLICY_req, 0, TimezonePolicy.get(0)),
		P_WEB_PRESENCE_POLICY      (166, 18, P_WEB_PRESENCE_POLICY_req, 0, WebPresencePolicy.get(0)),
		P_PHONE_NUMBERS_POLICY     (168, 19, P_PHONE_NUMBERS_POLICY_req, 0, PhoneNumbersPolicy.get(0)),
		P_VOICEMAIL_POLICY         (169, 20, P_VOICEMAIL_POLICY_req, 0, VoicemailPolicy.get(0)),
		P_PARTNER_OPTEDOUT         (773, 21, P_PARTNER_OPTEDOUT_req, 0, null),
		P_SERVICE_PROVIDER_INFO    (800, 22, P_SERVICE_PROVIDER_INFO_req, 0, null),
		P_REGISTRATION_TIMESTAMP   (801, 23, P_REGISTRATION_TIMESTAMP_req, 0, null),
		P_OTHER_INSTANCES_COUNT    (802, 24, P_OTHER_INSTANCES_COUNT_req, 0, null),
		P_SKYPE_NAME               (4, 25, P_SKYPE_NAME_req, 0, null),
		P_FULL_NAME                (5, 26, P_FULL_NAME_req, 0, null),
		P_BIRTHDAY                 (7, 27, P_BIRTHDAY_req, 0, null),
		P_GENDER                   (8, 28, P_GENDER_req, 0, null),
		P_LANGUAGES                (9, 29, P_LANGUAGES_req, 0, null),
		P_COUNTRY                  (10, 30, P_COUNTRY_req, 0, null),
		P_PROVINCE                 (11, 31, P_PROVINCE_req, 0, null),
		P_CITY                     (12, 32, P_CITY_req, 0, null),
		P_PHONE_HOME               (13, 33, P_PHONE_HOME_req, 0, null),
		P_PHONE_OFFICE             (14, 34, P_PHONE_OFFICE_req, 0, null),
		P_PHONE_MOBILE             (15, 35, P_PHONE_MOBILE_req, 0, null),
		P_EMAILS                   (16, 36, P_EMAILS_req, 0, null),
		P_HOMEPAGE                 (17, 37, P_HOMEPAGE_req, 0, null),
		P_ABOUT                    (18, 38, P_ABOUT_req, 0, null),
		P_PROFILE_TIMESTAMP        (19, 39, P_PROFILE_TIMESTAMP_req, 0, null),
		P_MOOD_TEXT                (26, 40, P_MOOD_TEXT_req, 0, null),
		P_TIMEZONE                 (27, 41, P_TIMEZONE_req, 0, null),
		P_NROF_AUTHED_BUDDIES      (28, 42, P_NROF_AUTHED_BUDDIES_req, 0, null),
		P_AVAILABILITY             (34, 43, P_AVAILABILITY_req, 0, Contact.Availability.get(0)),
		P_AVATAR_IMAGE             (37, 44, P_AVATAR_IMAGE_req, 0, null),
		P_AVATAR_TIMESTAMP         (182, 45, P_AVATAR_TIMESTAMP_req, 0, null),
		P_MOOD_TIMESTAMP           (183, 46, P_MOOD_TIMESTAMP_req, 0, null),
		P_RICH_MOOD_TEXT           (205, 47, P_RICH_MOOD_TEXT_req, 0, null);
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
		public int      getModuleId() { return mod;       }		public EnumConverting getEnumConverter()    { return enumConverter;   }
		public EnumConverting convert(final int from) { return Property.get(from); }
		public EnumConverting[] getArray(final int size) { return new Property[size]; }
		public static Property get(final int from) {
			switch (from) {
			case  70: return P_STATUS;
			case  71: return P_PWD_CHANGE_STATUS;
			case  73: return P_LOGOUT_REASON;
			case  78: return P_COMMIT_STATUS;
			case  72: return P_SUGGESTED_SKYPE_NAME;
			case  74: return P_SKYPEOUT_BALANCE_CURRENCY;
			case  75: return P_SKYPEOUT_BALANCE;
			case 804: return P_SKYPEOUT_PRECISION;
			case  76: return P_SKYPEIN_NUMBERS;
			case  79: return P_CBL_SYNC_STATUS;
			case  77: return P_OFFLINE_CALL_FORWARD;
			case 160: return P_CHAT_POLICY;
			case 161: return P_SKYPE_CALL_POLICY;
			case 162: return P_PSTN_CALL_POLICY;
			case 163: return P_AVATAR_POLICY;
			case 164: return P_BUDDY_COUNT_POLICY;
			case 165: return P_TIMEZONE_POLICY;
			case 166: return P_WEB_PRESENCE_POLICY;
			case 168: return P_PHONE_NUMBERS_POLICY;
			case 169: return P_VOICEMAIL_POLICY;
			case 773: return P_PARTNER_OPTEDOUT;
			case 800: return P_SERVICE_PROVIDER_INFO;
			case 801: return P_REGISTRATION_TIMESTAMP;
			case 802: return P_OTHER_INSTANCES_COUNT;
			case   4: return P_SKYPE_NAME;
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
			case  19: return P_PROFILE_TIMESTAMP;
			case  26: return P_MOOD_TEXT;
			case  27: return P_TIMEZONE;
			case  28: return P_NROF_AUTHED_BUDDIES;
			case  34: return P_AVAILABILITY;
			case  37: return P_AVATAR_IMAGE;
			case 182: return P_AVATAR_TIMESTAMP;
			case 183: return P_MOOD_TIMESTAMP;
			case 205: return P_RICH_MOOD_TEXT;
			}
			return P_UNKNOWN;
		}
		public static final int P_STATUS_VALUE                    =  70;
		public static final int P_PWD_CHANGE_STATUS_VALUE         =  71;
		public static final int P_LOGOUT_REASON_VALUE             =  73;
		public static final int P_COMMIT_STATUS_VALUE             =  78;
		public static final int P_SUGGESTED_SKYPE_NAME_VALUE      =  72;
		public static final int P_SKYPEOUT_BALANCE_CURRENCY_VALUE =  74;
		public static final int P_SKYPEOUT_BALANCE_VALUE          =  75;
		public static final int P_SKYPEOUT_PRECISION_VALUE        = 804;
		public static final int P_SKYPEIN_NUMBERS_VALUE           =  76;
		public static final int P_CBL_SYNC_STATUS_VALUE           =  79;
		public static final int P_OFFLINE_CALL_FORWARD_VALUE      =  77;
		public static final int P_CHAT_POLICY_VALUE               = 160;
		public static final int P_SKYPE_CALL_POLICY_VALUE         = 161;
		public static final int P_PSTN_CALL_POLICY_VALUE          = 162;
		public static final int P_AVATAR_POLICY_VALUE             = 163;
		public static final int P_BUDDY_COUNT_POLICY_VALUE        = 164;
		public static final int P_TIMEZONE_POLICY_VALUE           = 165;
		public static final int P_WEB_PRESENCE_POLICY_VALUE       = 166;
		public static final int P_PHONE_NUMBERS_POLICY_VALUE      = 168;
		public static final int P_VOICEMAIL_POLICY_VALUE          = 169;
		public static final int P_PARTNER_OPTEDOUT_VALUE          = 773;
		public static final int P_SERVICE_PROVIDER_INFO_VALUE     = 800;
		public static final int P_REGISTRATION_TIMESTAMP_VALUE    = 801;
		public static final int P_OTHER_INSTANCES_COUNT_VALUE     = 802;
		public static final int P_SKYPE_NAME_VALUE                =   4;
		public static final int P_FULL_NAME_VALUE                 =   5;
		public static final int P_BIRTHDAY_VALUE                  =   7;
		public static final int P_GENDER_VALUE                    =   8;
		public static final int P_LANGUAGES_VALUE                 =   9;
		public static final int P_COUNTRY_VALUE                   =  10;
		public static final int P_PROVINCE_VALUE                  =  11;
		public static final int P_CITY_VALUE                      =  12;
		public static final int P_PHONE_HOME_VALUE                =  13;
		public static final int P_PHONE_OFFICE_VALUE              =  14;
		public static final int P_PHONE_MOBILE_VALUE              =  15;
		public static final int P_EMAILS_VALUE                    =  16;
		public static final int P_HOMEPAGE_VALUE                  =  17;
		public static final int P_ABOUT_VALUE                     =  18;
		public static final int P_PROFILE_TIMESTAMP_VALUE         =  19;
		public static final int P_MOOD_TEXT_VALUE                 =  26;
		public static final int P_TIMEZONE_VALUE                  =  27;
		public static final int P_NROF_AUTHED_BUDDIES_VALUE       =  28;
		public static final int P_AVAILABILITY_VALUE              =  34;
		public static final int P_AVATAR_IMAGE_VALUE              =  37;
		public static final int P_AVATAR_TIMESTAMP_VALUE          = 182;
		public static final int P_MOOD_TIMESTAMP_VALUE            = 183;
		public static final int P_RICH_MOOD_TEXT_VALUE            = 205;
		public static final Property[] mget_profile_mreq = { P_SKYPE_NAME, P_FULL_NAME, P_MOOD_TEXT, P_EMAILS, P_PHONE_HOME, P_PHONE_OFFICE, P_PHONE_MOBILE, P_BIRTHDAY, P_GENDER, P_LANGUAGES, P_COUNTRY, P_PROVINCE, P_CITY, P_HOMEPAGE, P_ABOUT, P_TIMEZONE };
	}
	private final static byte[] getStatusWithProgress_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 1};
	public class GetStatusWithProgressResponse {
		public Status status;
		public int progress;
	};
	
	/**
	 * getStatusWithProgress
	 * @return GetStatusWithProgressResponse
	 * <br> - status Current value of this account's P_STATUS property
	 * <br> - progress This argument returns 0. 
	 */
	public GetStatusWithProgressResponse getStatusWithProgress() {
		try {
			Decoding decoder = sidDoRequest(getStatusWithProgress_req)
			.endRequest();
			GetStatusWithProgressResponse result = new GetStatusWithProgressResponse();
			result.status = (Status) decoder.getEnumParm(1, Status.get(0), false);
			result.progress = decoder.getUintParm(2, true);
			return result;
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] login_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 5};
	/**
	 * Login an auto-login enabled account (P_STATUS is LOGGED_OUT_AND_PWD_SAVED) and optionally set the availability, for example login in as Contact.DO_NOT_DISTURB. To find out whether there is an auto-login enabled account available, you can use Skype.GetDefaultAccountName to retrieve the skypename of that account. You can then get an Account instance for login by invoking Skype.GetAccount. 
	 * 
	 * If no accounts with stored login credentials are available (GetDefaultAccountName returns an empty string), then you will have to prompt the user for account name and password and then use LoginWithPassword. Account name field in the UI can be pre-populated with strings retrieved with Skype.GetExistingAccounts 
	
	 * @param setAvailabilityTo force this account's initial online status to the specified Contact.AVAILABILITY value.
	 */
	public void login(Contact.Availability setAvailabilityTo) {
		try {
			sidDoRequest(login_req)
			.addEnumParm(1, setAvailabilityTo)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] loginWithPassword_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 6};
	/**
	 *   
	 * Login in an account by specifying its password. To retrieve an account instance for login, invoke Skype.GetAccount. 
	
	 * @param password Password string (plaintext) for this account, which should be pre-validated (Skype.ValidatePassword) 
	 * @param savePwd 
	 *  - true: Saves the password, ensuring that auto-login is enabled. 
	 *  - false (default): Does not save the password, and so the user might not be able to effect auto-login until they explicitly invoke Account.SetPasswordSaved(true).
	
	 * @param saveDataLocally For internal use only.
	 */
	public void loginWithPassword(String password, boolean savePwd, boolean saveDataLocally) {		try {
			sidDoRequest(loginWithPassword_req)
			.addStringParm(1, password)
			.addBoolParm(2, savePwd)
			.addBoolParm(3, saveDataLocally, true)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] register_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 7};
	/**
	 * This command can be used to create a new Skype account, based on the Account object. When successful, this command will also log in with the newly created account. If the new account registration was not successful, Account status property will change to LOGGED_OUT. A common reason for registration failures is that the an account with that name already exists. In that case, Account LOGOUT_REASON will be set to SKYPENAME_TAKEN. Also, Account SUGGESTED_SKYPENAME property will be set to a list of similar but still available skypenames that could be used instead. The property will contain up to 5 semicolon-separated alternative skypenames. In if no suggested skypenames were available, then this property will contain an empty string. 
	 * NB! You should pre-validate your P_SKYPENAME value and any email string (Skype.ValidateProfileString) prior to invoking this method. 
	
	 * @param password Password string (plaintext) for this account, which should be pre-validated (Skype.ValidatePassword)
	 * @param savePwd 
	 *  - true: Saves the password and enables auto-login. 
	 *  - false (default): Does not save the password, and the user needs to be prompted for password on the next login attempt.
	
	 * @param saveDataLocally For internal use only.
	 * @param email An email address for retrieving lost passwords and receiving news and information from Skype.
	 * @param allowSpam enable/disable news and information from Skype being sent to account's e-mail.
	 */
	public void register(String password, boolean savePwd, boolean saveDataLocally, String email, boolean allowSpam) {
		try {
			sidDoRequest(register_req)
			.addStringParm(1, password)
			.addBoolParm(2, savePwd)
			.addBoolParm(3, saveDataLocally, true)
			.addStringParm(4, email)
			.addBoolParm(5, allowSpam)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] logout_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 8};
	/** Logs out current account. Note that calling this on client application exit is optional. 
	 * @param clearSavedPwd 
	 *  - true: Clears any saved password use with auto-login and so disables auto-login until you explicitly invoke Account.SetPasswordSaved(true). 
	 *  - false (default): Does not clear any saved password and so does not affect existing auto-login behavior.
	
	 */
	public void logout(boolean clearSavedPwd) {
		try {
			sidDoRequest(logout_req)
			.addBoolParm(1, clearSavedPwd)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] changePassword_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 11};
	/** Changes this account's password. Returns false if the change failed. NB! You should pre-validate your password strings (Skype.ValidatePassword) and ensure that they are different prior to invoking this method. 
	 * @param oldPassword "Current password string (plaintext) for this account
	 * @param newPassword New password string (plaintext) for this account
	 * @param savePwd 
	 *  - true: Saves the new password and enables auto-login. 
	 *  - false (default): Clears any existing saved password and so the user cannot effect auto-login until they explicitly invoke Account.SetPasswordSaved(true)
	
	 */
	public void changePassword(String oldPassword, String newPassword, boolean savePwd) {
		try {
			sidDoRequest(changePassword_req)
			.addStringParm(1, oldPassword)
			.addStringParm(2, newPassword)
			.addBoolParm(3, savePwd)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setPasswordSaved_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 25};
	/** save or clear credentials for auto-login whilst already logged in
	 * @param savePwd
	 */
	public void setPasswordSaved(boolean savePwd) {
		try {
			sidDoRequest(setPasswordSaved_req)
			.addBoolParm(1, savePwd)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setServerSideIntProperty_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 12};
	/** Setter for integer (and enum) server-side properties. For a list of writeable server-side properties, see the detailed description of the Account class. 
	 * @param propKey
	 * @param value
	 */
	public void setServerSideIntProperty(int propKey, int value) {
		try {
			sidDoRequest(setServerSideIntProperty_req)
			.addEnumParm(1, propKey)
			.addUintParm(2, value)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setServerSideStrProperty_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 13};
	/** Setter for String server-side properties. For a list of writeable server-side properties, see the detailed description of the Account class. 
	 * @param propKey
	 * @param value
	 */
	public void setServerSideStrProperty(int propKey, String value) {
		try {
			sidDoRequest(setServerSideStrProperty_req)
			.addEnumParm(1, propKey)
			.addStringParm(2, value)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] cancelServerCommit_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 15};
	/** Cancels an attempt to commit a server-side P_XXX_POLICY or the P_OFFLINE_CALLFORWARD server-side property. Invoking this cancellation only makes sense whilst the P_COMMITTSTATUS is in COMMITTING_TO_SERVER state. 
	 */
	public void cancelServerCommit() {
		try {
			sidDoRequest(cancelServerCommit_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setIntProperty_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 16};
	/** Setter for integer properties. For a list of writeable account profile properties, see the detailed description of the Account class. 
	 * @param propKey
	 * @param value
	 */
	public void setIntProperty(int propKey, int value) {
		try {
			sidDoRequest(setIntProperty_req)
			.addEnumParm(1, propKey)
			.addUintParm(2, value)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setStrProperty_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 17};
	/** Setter for String properties. For a list of writeable account profile properties, see the detailed description of the Account class. NB! You should pre-validate your about and mood message strings (Skype.ValidateProfileString) prior to invoking this method. 
	 * @param propKey
	 * @param value
	 */
	public void setStrProperty(int propKey, String value) {
		try {
			sidDoRequest(setStrProperty_req)
			.addEnumParm(1, propKey)
			.addStringParm(2, value)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setBinProperty_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 18};
	/** Setter for BLOB properties, such as its avatar image. For a list of writeable account profile properties, see the detailed description of the Account class. NB! You should pre-validate your avatar image (Skype.ValidateAvatar) prior to invoking this method. 
	 * @param propKey
	 * @param value
	 */
	public void setBinProperty(int propKey, byte[] value) {
		try {
			sidDoRequest(setBinProperty_req)
			.addEnumParm(1, propKey)
			.addBinaryParm(2, value)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setAvailability_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 19};
	/** Sets online status of the currently logged in account to one of the values from Contact class AVAILABILITY enumerator. 
	 * @param availability only subset of all contact availabilities allowed
	 */
	public void setAvailability(Contact.Availability availability) {
		try {
			sidDoRequest(setAvailability_req)
			.addEnumParm(1, availability)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setStandby_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 10};	/** Transitions the availability of this account's associated user, who is assumed to be logged in. 
	 * @param standby 
	 *  - true: Saves the user's current availability, then sets it to CONTACT.AVAILABILITY.OFFLINE 
	 *  - false: Reconnects the user and restores their previous availability
	
	 */
	public void setStandby(boolean standby) {
		try {
			sidDoRequest(setStandby_req)
			.addBoolParm(1, standby)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] getCapabilityStatus_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 21};
	public class GetCapabilityStatusResponse {
		public CapabilityStatus status;
		public long expiryTimestamp;
	};
	
	/** Returns state of a given account capability. Takes Contact class CAPABILITY property as input argument and returns its state and expiration timestamp where appropriate. For example (C++ wrapper, with other wrappers the syntax may vary but the idea is the same) MyAccount.GetCapabilityStatus(Contact.CAPABILITY_SKYPEOUT, Cap, T); will return Account.CAPABILITY_EXISTS if local account has SkypeOut enabled. 
	 * @param capability
	 * @return GetCapabilityStatusResponse
	 * <br> - status
	 * <br> - expiryTimestamp
	 */
	public GetCapabilityStatusResponse getCapabilityStatus(Contact.Capability capability) {
		try {
			Decoding decoder = sidDoRequest(getCapabilityStatus_req)
			.addEnumParm(1, capability)
			.endRequest();
			GetCapabilityStatusResponse result = new GetCapabilityStatusResponse();
			result.status = (CapabilityStatus) decoder.getEnumParm(1, CapabilityStatus.get(0), false);
			result.expiryTimestamp = decoder.getTimestampParm(2, true);
			return result;
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] getSkypeNameHash_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 22};
	/** Response is empty when called with an inactive or invalid account
	 * @return skypenameHash
	 */
	public String getSkypeNameHash() {
		try {
			return sidDoRequest(getSkypeNameHash_req)
			.endRequest().getStringParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return "";
		}
	}
	private final static byte[] getVerifiedEmail_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 2};
	/** returns verified-by-Skype e-mail for this account if exists and verifiable
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
	private final static byte[] getVerifiedCompany_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 3};
	/** returns verified-by-Skype company for this account if exists and verifiable
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
	private final static byte[] delete_req = {(byte) 90,(byte) 82,(byte) 5,(byte) 24};
	/** Deletes all account data stored locally. Does not remove any account data from the server! 
	 */
	public void delete() {
		try {
			sidDoRequest(delete_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	/***
	 * generic multiget of a list of Property
	 * @param requested the list of requested properties of Account
	 * @return SidGetResponding
	 */
	public SidGetResponding sidMultiGet(Property[] requested) {
		return super.sidMultiGet(requested);
	}
	/***
	 * generic multiget of list of Property for a list of Account
	 * @param requested the list of requested properties
	 * @return SidGetResponding[] can be casted to (Account[]) if all properties are cached
	 */
	static public SidGetResponding[] sidMultiGet(Property[] requested, Account[] objects) {
		return SidObject.sidMultiGet(requested, objects);
	}
	/*** multiget the following properties
	 * - P_SKYPE_NAME
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
	public Account mgetProfile() {
		return (Account) super.sidMultiGet(Property.mget_profile_mreq, this);
	}
	/** Account.STATUS */
	public Status getStatus() {
		synchronized(this) {
			if ((mSidCached & 0x1) != 0)
				return mStatus;
		}
		return (Status) sidRequestEnumProperty(Property.P_STATUS);
	}
	/** Account.PWDCHANGESTATUS */
	public PwdChangeStatus getPwdChangeStatus() {
		synchronized(this) {
			if ((mSidCached & 0x2) != 0)
				return mPwdChangeStatus;
		}
		return (PwdChangeStatus) sidRequestEnumProperty(Property.P_PWD_CHANGE_STATUS);
	}
	/** This property should only be examined when Account.P_STATUS is LOGGED_OUT or LOGGED_OUT_AND_PWD_SAVED. That is, you should not monitor changes to this property in Account.OnChange callback, other than after already having checked that P_STATUS property has appropriate value. The reason for this is that the LOGOUTREASON does not get reset before the next login attempt. For example: if a user tried to log in with a wrong password, the LOGOUTREASON gets set to INCORRECT_PASSWORD. Now, if the user tries to log in again, and yet again submits an incorrect password, the value of the LOGOUTREASON does not change anymore, because it already is set to INCORRECT_PASSWORD. Consequently, Account.OnChange will not fire in this case.  */
	public LogoutReason getLogoutReason() {
		synchronized(this) {
			if ((mSidCached & 0x4) != 0)
				return mLogoutReason;
		}
		return (LogoutReason) sidRequestEnumProperty(Property.P_LOGOUT_REASON);
	}
	/** Account.COMMITSTATUS */
	public CommitStatus getCommitStatus() {
		synchronized(this) {
			if ((mSidCached & 0x8) != 0)
				return mCommitStatus;
		}
		return (CommitStatus) sidRequestEnumProperty(Property.P_COMMIT_STATUS);
	}
	/** suggested skypenames, semi-colon separated. present if logoutreason==SKYPENAME_TAKEN */
	public String getSuggestedSkypeName() {
		synchronized(this) {
			if ((mSidCached & 0x10) != 0)
				return mSuggestedSkypeName;
		}
		return sidRequestStringProperty(Property.P_SUGGESTED_SKYPE_NAME);
	}
	/** 'EUR', 'USD', etc. */
	public String getSkypeoutBalanceCurrency() {
		synchronized(this) {
			if ((mSidCached & 0x20) != 0)
				return mSkypeoutBalanceCurrency;
		}
		return sidRequestStringProperty(Property.P_SKYPEOUT_BALANCE_CURRENCY);
	}
	/** balance in 'cents' */
	public int getSkypeoutBalance() {
		synchronized(this) {
			if ((mSidCached & 0x40) != 0)
				return mSkypeoutBalance;
		}
		return sidRequestUintProperty(Property.P_SKYPEOUT_BALANCE);
	}
	/** decimal points in ACCOUNT_SKYPEOUT_BALANCE */
	public int getSkypeoutPrecision() {
		synchronized(this) {
			if ((mSidCached & 0x80) != 0)
				return mSkypeoutPrecision;
		}
		return sidRequestUintProperty(Property.P_SKYPEOUT_PRECISION);
	}
	/** space-separated list of skypein numbers */
	public String getSkypeinNumbers() {
		synchronized(this) {
			if ((mSidCached & 0x100) != 0)
				return mSkypeinNumbers;
		}
		return sidRequestStringProperty(Property.P_SKYPEIN_NUMBERS);
	}
	/** Account.CBLSYNCSTATUS */
	public CblSyncStatus getCblSyncStatus() {
		synchronized(this) {
			if ((mSidCached & 0x200) != 0)
				return mCblSyncStatus;
		}
		return (CblSyncStatus) sidRequestEnumProperty(Property.P_CBL_SYNC_STATUS);
	}
	/** space-separated list of 'begSecond,endSecond,identity' tokens */
	public String getOfflineCallForward() {
		synchronized(this) {
			if ((mSidCached & 0x400) != 0)
				return mOfflineCallForward;
		}
		return sidRequestStringProperty(Property.P_OFFLINE_CALL_FORWARD);
	}
	/** Server-side account property, use SetServerside*Property() to set */
	public ChatPolicy getChatPolicy() {
		synchronized(this) {
			if ((mSidCached & 0x800) != 0)
				return mChatPolicy;
		}
		return (ChatPolicy) sidRequestEnumProperty(Property.P_CHAT_POLICY);
	}
	/** Server-side account property, use SetServerside*Property() to set */
	public SkypeCallPolicy getSkypeCallPolicy() {
		synchronized(this) {
			if ((mSidCached & 0x1000) != 0)				return mSkypeCallPolicy;
		}
		return (SkypeCallPolicy) sidRequestEnumProperty(Property.P_SKYPE_CALL_POLICY);
	}
	/** Server-side account property, use SetServerside*Property() to set */
	public PstnCallPolicy getPstnCallPolicy() {
		synchronized(this) {
			if ((mSidCached & 0x2000) != 0)
				return mPstnCallPolicy;
		}
		return (PstnCallPolicy) sidRequestEnumProperty(Property.P_PSTN_CALL_POLICY);
	}
	/** Server-side account property, use SetServerside*Property() to set */
	public AvatarPolicy getAvatarPolicy() {
		synchronized(this) {
			if ((mSidCached & 0x4000) != 0)
				return mAvatarPolicy;
		}
		return (AvatarPolicy) sidRequestEnumProperty(Property.P_AVATAR_POLICY);
	}
	/** Server-side account property, use SetServerside*Property() to set */
	public BuddyCountPolicy getBuddyCountPolicy() {
		synchronized(this) {
			if ((mSidCached & 0x8000) != 0)
				return mBuddyCountPolicy;
		}
		return (BuddyCountPolicy) sidRequestEnumProperty(Property.P_BUDDY_COUNT_POLICY);
	}
	/** Server-side account property, use SetServerside*Property() to set */
	public TimezonePolicy getTimezonePolicy() {
		synchronized(this) {
			if ((mSidCached & 0x10000) != 0)
				return mTimezonePolicy;
		}
		return (TimezonePolicy) sidRequestEnumProperty(Property.P_TIMEZONE_POLICY);
	}
	/** Server-side account property, use SetServerside*Property() to set */
	public WebPresencePolicy getWebPresencePolicy() {
		synchronized(this) {
			if ((mSidCached & 0x20000) != 0)
				return mWebPresencePolicy;
		}
		return (WebPresencePolicy) sidRequestEnumProperty(Property.P_WEB_PRESENCE_POLICY);
	}
	/** Server-side account property, use SetServerside*Property() to set */
	public PhoneNumbersPolicy getPhoneNumbersPolicy() {
		synchronized(this) {
			if ((mSidCached & 0x40000) != 0)
				return mPhoneNumbersPolicy;
		}
		return (PhoneNumbersPolicy) sidRequestEnumProperty(Property.P_PHONE_NUMBERS_POLICY);
	}
	/** Server-side account property, use SetServerside*Property() to set */
	public VoicemailPolicy getVoicemailPolicy() {
		synchronized(this) {
			if ((mSidCached & 0x80000) != 0)
				return mVoicemailPolicy;
		}
		return (VoicemailPolicy) sidRequestEnumProperty(Property.P_VOICEMAIL_POLICY);
	}
	/** Alerts: opted out partner id's, space separated */
	public String getPartnerOptedout() {
		synchronized(this) {
			if ((mSidCached & 0x100000) != 0)
				return mPartnerOptedout;
		}
		return sidRequestStringProperty(Property.P_PARTNER_OPTEDOUT);
	}
	/** service information if the user is a paid service provider */
	public String getServiceProviderInfo() {
		synchronized(this) {
			if ((mSidCached & 0x200000) != 0)
				return mServiceProviderInfo;
		}
		return sidRequestStringProperty(Property.P_SERVICE_PROVIDER_INFO);
	}
	/** NB! Unlike your common UNIX timestamps, the registration_timestamp is special, as it counts MINUTES rather than seconds, from Epoch (January 1, 1970)  */
	public long getRegistrationTimestamp() {
		synchronized(this) {
			if ((mSidCached & 0x400000) != 0)
				return mRegistrationTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_REGISTRATION_TIMESTAMP);
	}
	/** number of times this user is logged in from other computers */
	public int getOtherInstancesCount() {
		synchronized(this) {
			if ((mSidCached & 0x800000) != 0)
				return mOtherInstancesCount;
		}
		return sidRequestUintProperty(Property.P_OTHER_INSTANCES_COUNT);
	}
	public String getSkypeName() {
		synchronized(this) {
			if ((mSidCached & 0x1000000) != 0)
				return mSkypeName;
		}
		return sidRequestStringProperty(Property.P_SKYPE_NAME);
	}
	public String getFullName() {
		synchronized(this) {
			if ((mSidCached & 0x2000000) != 0)
				return mFullName;
		}
		return sidRequestStringProperty(Property.P_FULL_NAME);
	}
	/** YYYYMMDD */
	public int getBirthday() {
		synchronized(this) {
			if ((mSidCached & 0x4000000) != 0)
				return mBirthday;
		}
		return sidRequestUintProperty(Property.P_BIRTHDAY);
	}
	/** 1-male, 2-female */
	public int getGender() {
		synchronized(this) {
			if ((mSidCached & 0x8000000) != 0)
				return mGender;
		}
		return sidRequestUintProperty(Property.P_GENDER);
	}
	/** ISO language codes, space-separated */
	public String getLanguages() {
		synchronized(this) {
			if ((mSidCached & 0x10000000) != 0)
				return mLanguages;
		}
		return sidRequestStringProperty(Property.P_LANGUAGES);
	}
	/** ISO country code */
	public String getCountry() {
		synchronized(this) {
			if ((mSidCached & 0x20000000) != 0)
				return mCountry;
		}
		return sidRequestStringProperty(Property.P_COUNTRY);
	}
	public String getProvince() {
		synchronized(this) {
			if ((mSidCached & 0x40000000) != 0)
				return mProvince;
		}
		return sidRequestStringProperty(Property.P_PROVINCE);
	}
	public String getCity() {
		synchronized(this) {
			if ((mSidCached & 0x80000000) != 0)
				return mCity;
		}
		return sidRequestStringProperty(Property.P_CITY);
	}
	/** NB! string not integer */
	public String getPhoneHome() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x1) != 0)
				return mPhoneHome;
		}
		return sidRequestStringProperty(Property.P_PHONE_HOME);
	}
	public String getPhoneOffice() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x2) != 0)
				return mPhoneOffice;
		}
		return sidRequestStringProperty(Property.P_PHONE_OFFICE);
	}
	public String getPhoneMobile() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x4) != 0)
				return mPhoneMobile;
		}
		return sidRequestStringProperty(Property.P_PHONE_MOBILE);
	}
	/** This is a string property, that contains space-separated list of email addresses. When surfacing this property in your UI, you will need to take into account that there may be more than one email addresses in this property (i.e. split the value at spaces and display them as list). Similarly, when modifying this property with SetStrProperty method, your editor should allow editing of component email addresses separately and add them all up again, before submitting back to the account.  */
	public String getEmails() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x8) != 0)
				return mEmails;
		}
		return sidRequestStringProperty(Property.P_EMAILS);
	}
	public String getHomepage() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x10) != 0)
				return mHomepage;
		}
		return sidRequestStringProperty(Property.P_HOMEPAGE);
	}
	/** arbitrary introductory text */
	public String getAbout() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x20) != 0)
				return mAbout;
		}
		return sidRequestStringProperty(Property.P_ABOUT);
	}
	/** UNIX timestamp of last profile change */
	public long getProfileTimestamp() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x40) != 0)
				return mProfileTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_PROFILE_TIMESTAMP);
	}
	/** Personal mood text (visible to authorised users only). Max length 300 bytes.  */
	public String getMoodText() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x80) != 0)
				return mMoodText;
		}
		return sidRequestStringProperty(Property.P_MOOD_TEXT);
	}
	/** 24*3600+diff_to_UTC_in_seconds. nb! changes with DST */
	public int getTimezone() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x100) != 0)
				return mTimezone;
		}
		return sidRequestUintProperty(Property.P_TIMEZONE);
	}
	/** Count of this user's authorized contacts.  */
	public int getNrofAuthedBuddies() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x200) != 0)
				return mNrofAuthedBuddies;
		}
		return sidRequestUintProperty(Property.P_NROF_AUTHED_BUDDIES);
	}
	/** Contact.AVAILABILITY */
	public Contact.Availability getAvailability() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x400) != 0)
				return mAvailability;
		}
		return (Contact.Availability) sidRequestEnumProperty(Property.P_AVAILABILITY);
	}
	/**
	 * Account avatar picture can be set with Account.SetBinProperty method. The contact avatar picture is limited to max 96x96 pixels and 32000 bytes. If the submitted avatar picture exceeds either of these size limits, it is the responsibility of your client to scale the image down to appropriate size. 
	 * 
	 * The avatar pictures have to be in JPG format. A SkypeKit client can enable the user to set the Account avatar in other picture formats, in which case the picture should be converted to JPG before submitting it.  	 * 
	 * In any case, the avatar picture should be pre-validated with the Skype.ValidateAvatar method. 
	 */
	public byte[] getAvatarImage() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x800) != 0)
				return mAvatarImage;
		}
		return sidRequestBinaryProperty(Property.P_AVATAR_IMAGE);
	}
	/** UNIX timestamp of when current avatar was set */
	public long getAvatarTimestamp() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x1000) != 0)
				return mAvatarTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_AVATAR_TIMESTAMP);
	}
	/** UNIX timestamp of when current mood was set */
	public long getMoodTimestamp() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x2000) != 0)
				return mMoodTimestamp;
		}
		return sidRequestTimestampProperty(Property.P_MOOD_TIMESTAMP);
	}
	/** XML version of CONTACT_MOOD_TEXT. Max length 1000 bytes.  */
	public String getRichMoodText() {
		synchronized(this) {
			if ((mSidExtraCached[0] & 0x4000) != 0)
				return mRichMoodText;
		}
		return sidRequestXmlProperty(Property.P_RICH_MOOD_TEXT);
	}
	public String sidGetStringProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 72:
			return mSuggestedSkypeName;
		case 74:
			return mSkypeoutBalanceCurrency;
		case 76:
			return mSkypeinNumbers;
		case 77:
			return mOfflineCallForward;
		case 773:
			return mPartnerOptedout;
		case 800:
			return mServiceProviderInfo;
		case 4:
			return mSkypeName;
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
		}
		return "";
	}
	public int sidGetIntProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 75:
			return mSkypeoutBalance;
		case 804:
			return mSkypeoutPrecision;
		case 802:
			return mOtherInstancesCount;
		case 7:
			return mBirthday;
		case 8:
			return mGender;
		case 27:
			return mTimezone;
		case 28:
			return mNrofAuthedBuddies;
		}
		return 0;
	}
	public EnumConverting sidGetEnumProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 70:
			return mStatus;
		case 71:
			return mPwdChangeStatus;
		case 73:
			return mLogoutReason;
		case 78:
			return mCommitStatus;
		case 79:
			return mCblSyncStatus;
		case 160:
			return mChatPolicy;
		case 161:
			return mSkypeCallPolicy;
		case 162:
			return mPstnCallPolicy;
		case 163:
			return mAvatarPolicy;
		case 164:
			return mBuddyCountPolicy;
		case 165:
			return mTimezonePolicy;
		case 166:
			return mWebPresencePolicy;
		case 168:
			return mPhoneNumbersPolicy;
		case 169:
			return mVoicemailPolicy;
		case 34:
			return mAvailability;
		}
		return null;
	}
	public byte[] sidGetBinaryProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 37);
		return mAvatarImage;
	}
	public String getPropertyAsString(final int prop) {
		switch (prop) {
		case 70: return getStatus().toString();
		case 71: return getPwdChangeStatus().toString();
		case 73: return getLogoutReason().toString();
		case 78: return getCommitStatus().toString();
		case 72: return getSuggestedSkypeName();
		case 74: return getSkypeoutBalanceCurrency();
		case 75: return Integer.toString(getSkypeoutBalance());
		case 804: return Integer.toString(getSkypeoutPrecision());
		case 76: return getSkypeinNumbers();
		case 79: return getCblSyncStatus().toString();
		case 77: return getOfflineCallForward();
		case 160: return getChatPolicy().toString();
		case 161: return getSkypeCallPolicy().toString();
		case 162: return getPstnCallPolicy().toString();
		case 163: return getAvatarPolicy().toString();
		case 164: return getBuddyCountPolicy().toString();
		case 165: return getTimezonePolicy().toString();
		case 166: return getWebPresencePolicy().toString();
		case 168: return getPhoneNumbersPolicy().toString();
		case 169: return getVoicemailPolicy().toString();
		case 773: return getPartnerOptedout();
		case 800: return getServiceProviderInfo();
		case 802: return Integer.toString(getOtherInstancesCount());
		case 4: return getSkypeName();
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
		case 26: return getMoodText();
		case 27: return Integer.toString(getTimezone());
		case 28: return Integer.toString(getNrofAuthedBuddies());
		case 34: return getAvailability().toString();
		case 37: return "<binary>";
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
				case 70: mStatus = Status.get(value); break;
				case 71: mPwdChangeStatus = PwdChangeStatus.get(value); break;
				case 73: mLogoutReason = LogoutReason.get(value); break;
				case 78: mCommitStatus = CommitStatus.get(value); break;
				case 72:
					if (svalue != null) mSuggestedSkypeName = svalue;
					else mSidCached &=~bit;
					break;
				case 74:
					if (svalue != null) mSkypeoutBalanceCurrency = svalue;
					else mSidCached &=~bit;
					break;
				case 75: mSkypeoutBalance = value; break;
				case 804: mSkypeoutPrecision = value; break;
				case 76:
					if (svalue != null) mSkypeinNumbers = svalue;
					else mSidCached &=~bit;
					break;
				case 79: mCblSyncStatus = CblSyncStatus.get(value); break;
				case 77:
					if (svalue != null) mOfflineCallForward = svalue;
					else mSidCached &=~bit;
					break;
				case 160: mChatPolicy = ChatPolicy.get(value); break;
				case 161: mSkypeCallPolicy = SkypeCallPolicy.get(value); break;
				case 162: mPstnCallPolicy = PstnCallPolicy.get(value); break;
				case 163: mAvatarPolicy = AvatarPolicy.get(value); break;
				case 164: mBuddyCountPolicy = BuddyCountPolicy.get(value); break;
				case 165: mTimezonePolicy = TimezonePolicy.get(value); break;
				case 166: mWebPresencePolicy = WebPresencePolicy.get(value); break;
				case 168: mPhoneNumbersPolicy = PhoneNumbersPolicy.get(value); break;
				case 169: mVoicemailPolicy = VoicemailPolicy.get(value); break;
				case 773:
					if (svalue != null) mPartnerOptedout = svalue;
					else mSidCached &=~bit;
					break;
				case 800:
					if (svalue != null) mServiceProviderInfo = svalue;
					else mSidCached &=~bit;
					break;
				case 802: mOtherInstancesCount = value; break;
				case 4:
					if (svalue != null) mSkypeName = svalue;
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
					else mSidExtraCached[0] &=~bit;
					break;
				case 15:
					if (svalue != null) mPhoneMobile = svalue;					else mSidExtraCached[0] &=~bit;
					break;
				case 16:
					if (svalue != null) mEmails = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 17:
					if (svalue != null) mHomepage = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 18:
					if (svalue != null) mAbout = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 26:
					if (svalue != null) mMoodText = svalue;
					else mSidExtraCached[0] &=~bit;
					break;
				case 27: mTimezone = value; break;
				case 28: mNrofAuthedBuddies = value; break;
				case 34: mAvailability = Contact.Availability.get(value); break;
				default: if (word == 0) mSidCached &= ~bit; else mSidExtraCached[word-1] &= ~bit; break;
				}
			}
		}
		AccountListener listener = ((Skype) mSidRoot).getAccountListener();
		if (listener != null)
			listener.onPropertyChange(this, property, value, svalue);
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final String newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 72:
			mSidCached |= 0x10;
			mSuggestedSkypeName=  newValue;
			break;
		case 74:
			mSidCached |= 0x20;
			mSkypeoutBalanceCurrency=  newValue;
			break;
		case 76:
			mSidCached |= 0x100;
			mSkypeinNumbers=  newValue;
			break;
		case 77:
			mSidCached |= 0x400;
			mOfflineCallForward=  newValue;
			break;
		case 773:
			mSidCached |= 0x100000;
			mPartnerOptedout=  newValue;
			break;
		case 800:
			mSidCached |= 0x200000;
			mServiceProviderInfo=  newValue;
			break;
		case 4:
			mSidCached |= 0x1000000;
			mSkypeName=  newValue;
			break;
		case 5:
			mSidCached |= 0x2000000;
			mFullName=  newValue;
			break;
		case 9:
			mSidCached |= 0x10000000;
			mLanguages=  newValue;
			break;
		case 10:
			mSidCached |= 0x20000000;
			mCountry=  newValue;
			break;
		case 11:
			mSidCached |= 0x40000000;
			mProvince=  newValue;
			break;
		case 12:
			mSidCached |= 0x80000000;
			mCity=  newValue;
			break;
		case 13:
			mSidExtraCached[0] |= 0x1;
			mPhoneHome=  newValue;
			break;
		case 14:
			mSidExtraCached[0] |= 0x2;
			mPhoneOffice=  newValue;
			break;
		case 15:
			mSidExtraCached[0] |= 0x4;
			mPhoneMobile=  newValue;
			break;
		case 16:
			mSidExtraCached[0] |= 0x8;
			mEmails=  newValue;
			break;
		case 17:
			mSidExtraCached[0] |= 0x10;
			mHomepage=  newValue;
			break;
		case 18:
			mSidExtraCached[0] |= 0x20;
			mAbout=  newValue;
			break;
		case 26:
			mSidExtraCached[0] |= 0x80;
			mMoodText=  newValue;
			break;
		case 205:
			mSidExtraCached[0] |= 0x4000;
			mRichMoodText=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final int newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 70:
			mSidCached |= 0x1;
			mStatus= Status.get(newValue);
			break;
		case 71:
			mSidCached |= 0x2;
			mPwdChangeStatus= PwdChangeStatus.get(newValue);
			break;
		case 73:
			mSidCached |= 0x4;
			mLogoutReason= LogoutReason.get(newValue);
			break;
		case 78:
			mSidCached |= 0x8;
			mCommitStatus= CommitStatus.get(newValue);
			break;
		case 75:
			mSidCached |= 0x40;
			mSkypeoutBalance=  newValue;
			break;
		case 804:
			mSidCached |= 0x80;
			mSkypeoutPrecision=  newValue;
			break;
		case 79:
			mSidCached |= 0x200;
			mCblSyncStatus= CblSyncStatus.get(newValue);
			break;
		case 160:
			mSidCached |= 0x800;
			mChatPolicy= ChatPolicy.get(newValue);
			break;
		case 161:
			mSidCached |= 0x1000;
			mSkypeCallPolicy= SkypeCallPolicy.get(newValue);
			break;
		case 162:
			mSidCached |= 0x2000;
			mPstnCallPolicy= PstnCallPolicy.get(newValue);
			break;
		case 163:
			mSidCached |= 0x4000;
			mAvatarPolicy= AvatarPolicy.get(newValue);
			break;
		case 164:
			mSidCached |= 0x8000;
			mBuddyCountPolicy= BuddyCountPolicy.get(newValue);
			break;
		case 165:
			mSidCached |= 0x10000;
			mTimezonePolicy= TimezonePolicy.get(newValue);
			break;
		case 166:
			mSidCached |= 0x20000;
			mWebPresencePolicy= WebPresencePolicy.get(newValue);
			break;
		case 168:
			mSidCached |= 0x40000;
			mPhoneNumbersPolicy= PhoneNumbersPolicy.get(newValue);
			break;
		case 169:
			mSidCached |= 0x80000;
			mVoicemailPolicy= VoicemailPolicy.get(newValue);
			break;
		case 802:
			mSidCached |= 0x800000;
			mOtherInstancesCount=  newValue;
			break;
		case 7:
			mSidCached |= 0x4000000;
			mBirthday=  newValue;
			break;
		case 8:
			mSidCached |= 0x8000000;
			mGender=  newValue;
			break;
		case 27:
			mSidExtraCached[0] |= 0x100;
			mTimezone=  newValue;
			break;
		case 28:
			mSidExtraCached[0] |= 0x200;
			mNrofAuthedBuddies=  newValue;
			break;
		case 34:
			mSidExtraCached[0] |= 0x400;
			mAvailability= Contact.Availability.get(newValue);
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final byte[] newValue) {
		final int propId = prop.getId();
		assert(propId == 37);
		mSidExtraCached[0] |= 0x800;
		mAvatarImage=  newValue;
	}
	public Status               mStatus;
	public PwdChangeStatus      mPwdChangeStatus;
	public LogoutReason         mLogoutReason;
	public CommitStatus         mCommitStatus;
	public String               mSuggestedSkypeName;
	public String               mSkypeoutBalanceCurrency;
	public int                  mSkypeoutBalance;
	public int                  mSkypeoutPrecision;
	public String               mSkypeinNumbers;
	public CblSyncStatus        mCblSyncStatus;
	public String               mOfflineCallForward;
	public ChatPolicy           mChatPolicy;
	public SkypeCallPolicy      mSkypeCallPolicy;
	public PstnCallPolicy       mPstnCallPolicy;
	public AvatarPolicy         mAvatarPolicy;
	public BuddyCountPolicy     mBuddyCountPolicy;
	public TimezonePolicy       mTimezonePolicy;
	public WebPresencePolicy    mWebPresencePolicy;
	public PhoneNumbersPolicy   mPhoneNumbersPolicy;
	public VoicemailPolicy      mVoicemailPolicy;
	public String               mPartnerOptedout;
	public String               mServiceProviderInfo;
	public long                 mRegistrationTimestamp;
	public int                  mOtherInstancesCount;
	public String               mSkypeName;
	public String               mFullName;
	public int                  mBirthday;
	public int                  mGender;
	public String               mLanguages;
	public String               mCountry;
	public String               mProvince;
	public String               mCity;
	public String               mPhoneHome;
	public String               mPhoneOffice;
	public String               mPhoneMobile;
	public String               mEmails;
	public String               mHomepage;
	public String               mAbout;
	public long                 mProfileTimestamp;
	public String               mMoodText;
	public int                  mTimezone;
	public int                  mNrofAuthedBuddies;
	public Contact.Availability mAvailability;
	public byte[]               mAvatarImage;
	public long                 mAvatarTimestamp;
	public long                 mMoodTimestamp;
	public String               mRichMoodText;
	/***
	 * invalidateCache: the next time the property is get, it will be querried to the runtime, meanwhile it can be discarded.
	 * This allows fine grained cache management. Note that this doesn't delete the property, you still have to set it to null
	 * to get a chance having this behavior. The rationale if that the generated properties being public, you can directly assign it to null
	 * whilst a generated invalidateCache would require switching on the values to do so.
	 * Account o; o.invalidate(Account.Property.P_MY_PROP); o.mMyProp = null;
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
	
	protected boolean hasCached() {		if (mSidCached != 0) return true;
		for (int i = 0, e = mSidExtraCached.length; i < e; i++)
			if (mSidExtraCached[i] != 0) return true;
		return false;
	}
	private int[] mSidExtraCached = new int[1];
	public int moduleId() {
		return 5;
	}
	
	public Account(final int oid, final SidRoot root) {
		super(oid, root, 47);
	}
}
