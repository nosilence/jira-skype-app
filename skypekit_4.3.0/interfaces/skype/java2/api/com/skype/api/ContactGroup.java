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
 * Collects and manages Contacts related by type, status, or some other arbitrary criteria. SkypeKit recognizes two distinct ContactGroup flavors - "hardwired" and "custom". SkypeKit both defines the criteria for and dynamically manages all "hardwired" ContactGroups. Individual users explicitly create and manage all "custom" ContactGroups.  
 * 
 * "Hardwired" groups are primarily organizational tools, for example, they enable you to display a list of all Contacts awaiting authorization by you. "Custom" groups are also organizational tools, for example, they enable you to display a list of all Contacts in a particular geographical area or belonging to a particular professional association, social clubs, and so forth. Primarily, though, "custom" groups are functional tools that enable you to establish conference calls, group chats, and so forth. 
 * 
 * "Hardwired" ContactGroups are defined for and available to all users. SkypeKit determines membership in a particular "hardwired" group dynamically whenever a user invokes Skype.GetHardwiredContactGroup for that group. Subsequent changes to a Contact's status might result in its being added to (for example, the Contact is now authorized) or removed from (for example, the Contact is now removed or blocked) one or more "hardwired" groups. 
 * 
 * SkypeKit fires OnChange events for all affected ContractGroup instances. Essentially all ContactGroup methods related to explicitly adding and removing members and conversations from the group return false, and CanAdd and CanRemove additionally return a false result. 
 * 
 * "Custom" ContactGroups can be defined by a particular Skype user through the UI. Your UI should implement Creation, deletion and filtering contact list by custom contact groups, as well as adding and removing contacts in those groups. 
 * 
 * A Contact can belong to multiple non-mutually exclusive "hardwired" groups at the same time, for example, an authorized contact is typically in your "buddy" group, but a Contact cannot belong to CONTACTS_AUTHORIZED_BY_ME if they are awaiting authorization. Similarly, a Contact can belong to multiple "custom" groups and mutual exclusivity is typically not an issue. 
 */
public final class ContactGroup extends SidObject {
	/** The list of all possible ContactGroup types. A value of this type can be passed to Skype class GetHardwiredContactGroup to retrieve the relevant ContactGroup object.  */
	public enum Type implements EnumConverting {
		/** The superset of all "hardwired" contact groups.  */
		ALL_KNOWN_CONTACTS               (1),
		/** The set of all authorized contacts, that is, contacts that were last the target of Contact.SetBuddyStatus(false) plus all SkypeOut contacts.  */
		ALL_BUDDIES                      (2),
		/**
		 * The set of all authorized Skype contacts (Contact:_SKYPENAME is non-null).  
		 * Note that this excludes Skype contacts that have either never been the target of Contact.SetBuddyStatus(true) or were last the target of Contactact.SetBuddyStatus(false). 
		 */
		SKYPE_BUDDIES                    (3),
		/** The set of all SkypeOut contacts (Contact:_PSTNNUMBER is non-null). PSTN contacts can be added to the contact list by retrieving a new contact object with Skype.GetContact, passing in the phone number as string, and then either using Contact.SetBuddyStatus(true) or adding the contact to the SKYPEOUT_BUDDIES group with ContactGroup.AddContact.  */
		SKYPEOUT_BUDDIES                 (4),
		/** The subset of ALL_BUDDIES that are currently online, including those currently marked as DO_NOT_DISTURBED and AWAY.  */
		ONLINE_BUDDIES                   (5),
		/** The set of all contacts whose Contact:_TYPE reflects UNRECOGNIZED OR have not authorized the local user yet.  */
		UNKNOWN_OR_PENDING_AUTH_BUDDIES  (6),
		/** This filter returns top 10 most recently contacted contacts, based on Contact.P_LASTUSED_TIMESTAMP property values. This is not configurable. Note that the P_LASTUSED_TIMESTAMP property does not propagate between different Skype instances - thus this filter only works in context of the local database. Recent contacts that were in touch with the user on some other Skype installation will not show up in this filter.  */
		RECENTLY_CONTACTED_CONTACTS      (7),
		/** Contacts to whose authorization request the user has not responded yet. The UI should enable the user to accept, decline the authorization request and in case of decline, optionally block further incoming communication from the contact. See: Contact.SetBuddyStatus, Contact.SetBlocked and Contact.IgnoreAuthRequest for more information.  */
		CONTACTS_WAITING_MY_AUTHORIZATION(8),
		/** All contacts authorized by the user.  */
		CONTACTS_AUTHORIZED_BY_ME        (9),
		/** Group of contacts the user has blocked from further incoming communications. If the UI enables contact blocking, it should also provide interface for the user to unblock the blocked contacts. Note that a contact can simultaneously be in both CONTACTS_BLOCKED_BY_ME and CONTACTS_AUTHORIZED_BY_ME groups.  */
		CONTACTS_BLOCKED_BY_ME           (10),
		/** The set of all "buddies" that are not also a member of a custom group.  */
		UNGROUPED_BUDDIES                (11),
		/** A custom group defined by user.  */
		CUSTOM_GROUP                     (12),
		/** The shared contact group functionality is no longer supported. This contact group type can be ignored.  */
		PROPOSED_SHARED_GROUP            (13),
		/** The shared contact group functionality is no longer supported. This contact group type can be ignored.  */
		SHARED_GROUP                     (14),
		/** The set of all contacts that were originally imported from an external address book.  */
		EXTERNAL_CONTACTS                (15);
		private final int key;
		Type(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return ALL_KNOWN_CONTACTS; }
		public EnumConverting convert(int from) { return Type.get(from); }
		public EnumConverting[] getArray(final int size) { return new Type[size]; }
		public static Type get(int from) {
			switch (from) {
			case  1: return ALL_KNOWN_CONTACTS;
			case  2: return ALL_BUDDIES;
			case  3: return SKYPE_BUDDIES;
			case  4: return SKYPEOUT_BUDDIES;
			case  5: return ONLINE_BUDDIES;
			case  6: return UNKNOWN_OR_PENDING_AUTH_BUDDIES;
			case  7: return RECENTLY_CONTACTED_CONTACTS;
			case  8: return CONTACTS_WAITING_MY_AUTHORIZATION;
			case  9: return CONTACTS_AUTHORIZED_BY_ME;
			case 10: return CONTACTS_BLOCKED_BY_ME;
			case 11: return UNGROUPED_BUDDIES;
			case 12: return CUSTOM_GROUP;
			case 13: return PROPOSED_SHARED_GROUP;
			case 14: return SHARED_GROUP;
			case 15: return EXTERNAL_CONTACTS;
			}
			return ALL_KNOWN_CONTACTS;
		}
		public static final int ALL_KNOWN_CONTACTS_VALUE                =  1;
		public static final int ALL_BUDDIES_VALUE                       =  2;
		public static final int SKYPE_BUDDIES_VALUE                     =  3;
		public static final int SKYPEOUT_BUDDIES_VALUE                  =  4;
		public static final int ONLINE_BUDDIES_VALUE                    =  5;
		public static final int UNKNOWN_OR_PENDING_AUTH_BUDDIES_VALUE   =  6;
		public static final int RECENTLY_CONTACTED_CONTACTS_VALUE       =  7;
		public static final int CONTACTS_WAITING_MY_AUTHORIZATION_VALUE =  8;
		public static final int CONTACTS_AUTHORIZED_BY_ME_VALUE         =  9;
		public static final int CONTACTS_BLOCKED_BY_ME_VALUE            = 10;
		public static final int UNGROUPED_BUDDIES_VALUE                 = 11;
		public static final int CUSTOM_GROUP_VALUE                      = 12;
		public static final int PROPOSED_SHARED_GROUP_VALUE             = 13;
		public static final int SHARED_GROUP_VALUE                      = 14;
		public static final int EXTERNAL_CONTACTS_VALUE                 = 15;
	}
	private final static byte[] P_TYPE_req = {(byte) 90,(byte) 71,(byte) 155,(byte) 1,(byte) 93,(byte) 10};
	private final static byte[] P_CUSTOM_GROUP_ID_req = {(byte) 90,(byte) 71,(byte) 154,(byte) 1,(byte) 93,(byte) 10};
	private final static byte[] P_GIVEN_DISPLAY_NAME_req = {(byte) 90,(byte) 71,(byte) 151,(byte) 1,(byte) 93,(byte) 10};
	private final static byte[] P_CONTACT_COUNT_req = {(byte) 90,(byte) 71,(byte) 152,(byte) 1,(byte) 93,(byte) 10};	private final static byte[] P_ONLINE_CONTACT_COUNT_req = {(byte) 90,(byte) 71,(byte) 153,(byte) 1,(byte) 93,(byte) 10};
	/** Properties of the ContactGroup class */
	public enum Property implements PropertyEnumConverting {
		P_UNKNOWN             (0,0,null,0,null),
		P_TYPE                (155, 1, P_TYPE_req, 0, Type.get(0)),
		P_CUSTOM_GROUP_ID     (154, 2, P_CUSTOM_GROUP_ID_req, 0, null),
		P_GIVEN_DISPLAY_NAME  (151, 3, P_GIVEN_DISPLAY_NAME_req, 0, null),
		P_CONTACT_COUNT       (152, 4, P_CONTACT_COUNT_req, 0, null),
		P_ONLINE_CONTACT_COUNT(153, 5, P_ONLINE_CONTACT_COUNT_req, 0, null);
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
			case 155: return P_TYPE;
			case 154: return P_CUSTOM_GROUP_ID;
			case 151: return P_GIVEN_DISPLAY_NAME;
			case 152: return P_CONTACT_COUNT;
			case 153: return P_ONLINE_CONTACT_COUNT;
			}
			return P_UNKNOWN;
		}
		public static final int P_TYPE_VALUE                 = 155;
		public static final int P_CUSTOM_GROUP_ID_VALUE      = 154;
		public static final int P_GIVEN_DISPLAY_NAME_VALUE   = 151;
		public static final int P_CONTACT_COUNT_VALUE        = 152;
		public static final int P_ONLINE_CONTACT_COUNT_VALUE = 153;
	}
	private final static byte[] giveDisplayName_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 1};
	/** Setter for ContactGroup class GIVEN_DISPLAYNAME property. 
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
	private final static byte[] delete_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 2};
	/** Removes the contact group. This is synced across instances logged in with the same account - which can take several minutes for the sync to happen. 
	 * @return result
	 */
	public boolean delete() {
		try {
			return sidDoRequest(delete_req)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] getConversations_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 3};
	/** Returns list of conversations in the ContactGroup. 
	 * @return conversations
	 */
	public Conversation[] getConversations() {
		try {
			return (Conversation[]) sidDoRequest(getConversations_req)
			.endRequest().getObjectListParm(1, 18, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	private final static byte[] canAddConversation_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 4};
	/** Checks if the current user can add given conversation to the ContactGroup. Returns false for most of the hardwired contact groups for example. 
	 * @param conversation Conversation to be checked. 
	 * @return result Returns true if Conversation can be added to this ContactGroup. 
	 */
	public boolean canAddConversation(Conversation conversation) {
		try {
			return sidDoRequest(canAddConversation_req)
			.addObjectParm(1, conversation)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] addConversation_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 5};
	/** Adds given conversation to the ContactGroup. 
	 * @param conversation
	 */
	public void addConversation(Conversation conversation) {
		try {
			sidDoRequest(addConversation_req)
			.addObjectParm(1, conversation)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] canRemoveConversation_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 6};
	/** Checks if the current user can remove given conversation from the ContactGroup. Again, returns false for most hardwired contact groups. 
	 * @return result true if RemoveConversation(contact) works on this group
	 */
	public boolean canRemoveConversation() {
		try {
			return sidDoRequest(canRemoveConversation_req)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] removeConversation_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 7};
	/** Removes given conversation from the ContactGroup. 
	 * @param conversation
	 */
	public void removeConversation(Conversation conversation) {
		try {
			sidDoRequest(removeConversation_req)
			.addObjectParm(1, conversation)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] getContacts_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 8};
	/** Retrieves contact list. 
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
	private final static byte[] canAddContact_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 9};
	/** Checks if the current user can add given contact to the ContactGroup.  
	 * @param contact Contact to be checked. 
	 * @return result returns true if AddContact(contact) works on this group. 
	 */
	public boolean canAddContact(Contact contact) {
		try {
			return sidDoRequest(canAddContact_req)
			.addObjectParm(1, contact)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] addContact_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 10};
	/** Adds contact to a contact group. This only works for non-hardwired contact groups. 
	 * @param contact
	 */
	public void addContact(Contact contact) {
		try {
			sidDoRequest(addContact_req)
			.addObjectParm(1, contact)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] canRemoveContact_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 11};
	/** Checks if the current user can remove given contact from the ContactGroup. 
	 * @return result true if RemoveContact(contact) works on this group
	 */
	public boolean canRemoveContact() {
		try {
			return sidDoRequest(canRemoveContact_req)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] removeContact_req = {(byte) 90,(byte) 82,(byte) 10,(byte) 12};
	/** Removes contact from the ContactGroup. 
	 * @param contact
	 */
	public void removeContact(Contact contact) {
		try {
			sidDoRequest(removeContact_req)
			.addObjectParm(1, contact)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	/***
	 * generic multiget of a list of Property
	 * @param requested the list of requested properties of ContactGroup
	 * @return SidGetResponding
	 */
	public SidGetResponding sidMultiGet(Property[] requested) {
		return super.sidMultiGet(requested);
	}
	/***
	 * generic multiget of list of Property for a list of ContactGroup
	 * @param requested the list of requested properties
	 * @return SidGetResponding[] can be casted to (ContactGroup[]) if all properties are cached
	 */
	static public SidGetResponding[] sidMultiGet(Property[] requested, ContactGroup[] objects) {
		return SidObject.sidMultiGet(requested, objects);
	}
	/** ContactGroup.TYPE */
	public Type getType() {		synchronized(this) {
			if ((mSidCached & 0x1) != 0)
				return mType;
		}
		return (Type) sidRequestEnumProperty(Property.P_TYPE);
	}
	/** unique 32-bit ID for custom groups */
	public int getCustomGroupId() {
		synchronized(this) {
			if ((mSidCached & 0x2) != 0)
				return mCustomGroupId;
		}
		return sidRequestUintProperty(Property.P_CUSTOM_GROUP_ID);
	}
	/** change via ContactGroup.GiveDisplayname() */
	public String getGivenDisplayName() {
		synchronized(this) {
			if ((mSidCached & 0x4) != 0)
				return mGivenDisplayName;
		}
		return sidRequestStringProperty(Property.P_GIVEN_DISPLAY_NAME);
	}
	/** Number of contacts in the group. NB! The value of this property does not get updated until 5 seconds after account login. During these initial 5 seconds, the value of this property remains 0. The reason for this 5 second delay is to reduce the flurry of property update traffic that occurs during the CBL synchronization phase, following successful login. Note that if you absolutely need to have this value immediately after login, you can still get it by retrieving the contact list with ContactGroup.GetContacts method and examining its size.   */
	public int getContactCount() {
		synchronized(this) {
			if ((mSidCached & 0x8) != 0)
				return mContactCount;
		}
		return sidRequestUintProperty(Property.P_CONTACT_COUNT);
	}
	/** number of contacts online in the group */
	public int getOnlineContactCount() {
		synchronized(this) {
			if ((mSidCached & 0x10) != 0)
				return mOnlineContactCount;
		}
		return sidRequestUintProperty(Property.P_ONLINE_CONTACT_COUNT);
	}
	public String sidGetStringProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 151);
		return mGivenDisplayName;
	}
	public int sidGetIntProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 154:
			return mCustomGroupId;
		case 152:
			return mContactCount;
		case 153:
			return mOnlineContactCount;
		}
		return 0;
	}
	public EnumConverting sidGetEnumProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 155);
		return mType;
	}
	public String getPropertyAsString(final int prop) {
		switch (prop) {
		case 155: return getType().toString();
		case 154: return Integer.toString(getCustomGroupId());
		case 151: return getGivenDisplayName();
		case 152: return Integer.toString(getContactCount());
		case 153: return Integer.toString(getOnlineContactCount());
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
				case 155: mType = Type.get(value); break;
				case 154: mCustomGroupId = value; break;
				case 151:
					if (svalue != null) mGivenDisplayName = svalue;
					else mSidCached &=~bit;
					break;
				case 152: mContactCount = value; break;
				case 153: mOnlineContactCount = value; break;
				default: mSidCached&=~bit; break;
				}
			}
		}
		ContactGroupListener listener = ((Skype) mSidRoot).getContactGroupListener();
		if (listener != null)
			listener.onPropertyChange(this, property, value, svalue);
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final String newValue) {
		final int propId = prop.getId();
		assert(propId == 151);
		mSidCached |= 0x4;
		mGivenDisplayName=  newValue;
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final int newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 155:
			mSidCached |= 0x1;
			mType= Type.get(newValue);
			break;
		case 154:
			mSidCached |= 0x2;
			mCustomGroupId=  newValue;
			break;
		case 152:
			mSidCached |= 0x8;
			mContactCount=  newValue;
			break;
		case 153:
			mSidCached |= 0x10;
			mOnlineContactCount=  newValue;
			break;
		}
	}
	public Type   mType;
	public int    mCustomGroupId;
	public String mGivenDisplayName;
	public int    mContactCount;
	public int    mOnlineContactCount;
	public int moduleId() {
		return 10;
	}
	
	public ContactGroup(final int oid, final SidRoot root) {
		super(oid, root, 5);
	}
}
