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
 * This class encapsulates functionality for looking up contacts on the Skype network. Contacts can be searched by portion of their name, e-mail address, language preferences, etc. 
 * 
 * Contact search is asynchronous. ContactSearch.Submit is a non-blocking function that initiates the search. Upon finding a matching contact, ContactSearch.OnNewResult event gets fired, that gives you the reference to the discovered contact. You can get up to 100 matching contacts per search. Note that you will need to keep a live reference of the ContactSearch object while the search is doing its work.  
 * 
 * So, to perform a contact search:  
 *  - create a contact search object 
 *  - specify search terms and conditions 
 *  - submit search 
 *  - in ContactSearch.OnNewResult callback, update your UI 
 *  - in ContactSearch.OnChange, check for terminal values of P_CONTACT_SEARCH_STATUS and update the UI accordingly. 
 * 
 * When the search has done its job, the ContactSearch.P_CONTACT_SEARCH_STATUS property will go to one of the terminal values. 
 * 
 * The terminal values are: 
 *  - FINISHED - the search has stopped. Note that this does not mean any matches were actually found. 
 *  - FAILED - the search has failed. 
 *  - EXTENDABLE - this state should be considered the same as FINISHED. The feature of extending long search results is about to be deprecated. It is still possible for search objects to occasionally reach that state, so it should be handled in the UI (as FINISHED), but the extending feature itself should not be implemented in your UI. 
 * 
 * When a ContactSearch object reacges a terminal state, ContactSearch.Release has to be called to dispose of the object. 
 * 
 * There are three methods to create the ContactSearch objects. 
 * 
 * A) Skype.CreateIdentitySearch 
 * 
 * This method takes a string argument and looks for exact matches against Contact.P_SKYPENAME property. So for example, identity search for "echo" will return 0 results and search for "echo123" will return exactly one.  
 * 
 * Identity in this case means skypename - contact search does not work with PSTN type contacts. However, it does work for SKYPE type contacts that have supplied P_PHONE_HOME, P_PHONE_OFFICE or P_PHONE_MOBILE values in their account data. To search for those, you will need to use complex search (see below). 
 * 
 * Note that you should always check for boolean return value of the CreateIdentitySearch method. If the user submits a string that is not a valid skypename, the method will return false and the ContactSearchRef argument will return as NULL. 
 * 
 * B) Skype.CreateBasicContactSearch 
 * 
 * This method takes a string argument and looks for non-exact matches against both P_SKYPENAME and P_FULLNAME properties of the contact. If you intend to implement a simple, one-input search feature - this is the best method for you. The non-exact matching operates similarly to the SQL LIKE condition. 
 * 
 * C) Skype.CreateContactSearch 
 * 
 * This method enables you to implement advanced contact search, matching against multiple seach criteria. It takes no input arguments and expects search criteria to be added to the already constructed search object. 
 * 
 * Criteria can be added with ContactSearch.AddStrTerm and ContactSearch.AddIntTerm methods. 
 * 
 * These methods take Contact class porperty ID, condition, and the match pattern as inputs. 
 * 
 * Only the following Contact properties can be used for search: 
 *  - P_SKYPENAME  
 *  - P_FULLNAME 
 *  - P_BIRTHDAY (uint) 
 *  - P_GENDER (uint: 1-male, 2-female) 
 *  - P_LANGUAGES 
 *  - P_COUNTRY 
 *  - P_PROVINCE 
 *  - P_CITY 
 *  - P_PHONE_HOME 
 *  - P_PHONE_OFFICE 
 *  - P_PHONE_MOBILE 
 *  - P_EMAILS 
 *  - P_HOMEPAGE 
 *  - P_ABOUT 
 * 
 * String searches are case insensitive, i.e. search for echo123 also matches ECHO123 
 * 
 * When adding multiple criteria, default behaviour is that the criterions are additive. I.e. a term skypename == "joe" followed by term country == "us" will result in intersection between all joes and everybody in US. 
 * 
 * You can explicitly add an "OR" instead of "AND" between conditions, using the AddOr method. 
 * 
 * By default, AND criteria are grouped together, before OR's, so that: 
 * 
 * AddTerm(condition1) 
 * AddTerm(condition2) 
 * AddOr() 
 * AddTerm(condition3) 
 * AddTerm(condition4) 
 * 
 * will result in the following logical statement: 
 * (condition1 AND condition2) OR (condition3 AND condition4) 
 * 
 * However, you can add "global" critera, by using the add_to_subs argument of the AddXX methods. 
 * 
 * AddTerm(condition1) 
 * AddTerm(condition2) 
 * AddOr() 
 * AddTerm(condition3) 
 * AddTerm(condition4, add_to_subs=true) 
 * 
 * which would result in: 
 * (condition1 AND condition2 AND condition4) OR (condition3 AND condition4) 
 * 
 * 
 * Every one of the contact properties can only be used once, per search. For example, you cannot create a search for two different P_FULLNAME patterns. The &valid argument will still return tue if you do this, but the last criteria for any given property will override all previous ones. So, a search like this: 
 * 
 * cs->AddStrTerm(Contact.P_FULLNAME, ContactSearch.EQ, "John Smith", isValid); 
 * cs->AddOr(); 
 * cs->AddStrTerm(Contact.P_FULLNAME, ContactSearch.EQ, "Ivan Sidorov", isValid); 
 * 
 * will only return matches for "Ivan Sidorov" and none for "John Smith". 
 * 
 * Some of the contact properties are automatically combined for purposes of search. 
 * 
 * A search for P_SKYPENAME also returns matches from the P_FULLNAME property and vice versa. 
 * 
 * So that this: 
 * cs->AddStrTerm(Contact.P_SKYPENAME, ContactSearch.EQ, "john.smith", isValid); 
 * 
 * ..and this: 
 * cs->AddStrTerm(Contact.P_FULLNAME, ContactSearch.EQ, "John Smith", isValid); 
 * 
 * ..and this: 
 * cs->AddStrTerm(Contact.P_SKYPENAME, ContactSearch.EQ, "john.smith", isValid); 
 * cs->AddOr(); 
 * cs->AddStrTerm(Contact.P_FULLNAME, ContactSearch.EQ, "John Smith", isValid); 
 * 
 * ..all search from both the P_FULLNAME and P_SKYPENAME fields. 
 * 
 * 
 * Before using ContactGroup.Submit to start the search, you should always check whether the search criteria ended up being valid. This you can do with ContactSearch.IsValid method. 
 * 
 * As you probably noticed, each of the AddXX methods also return a validity check boolean. However, it is a better practice to do the overall check as well, even if all the individual search criteria ended up looking Ok. 
 * 
 * For example, lets take a search for contact's e-mail. This can be done with two different methods. Firstly we can use the ContactSearch.AddEmailTerm method. This method will actually validate whether the input is a valid e-mail address: 
 * 
 * cs->AddEmailTerm ("test@test@test", isValid);  
 * will return the isValid argument as false. 
 * 
 * However, you can also add the e-mail search criterion as a simple string, like this: 
 * 
 * cs->AddStrTerm(Contact.P_EMAILS, ContactSearch.EQ, "test@test@test@", isValid); 
 * in which case the isValid will return true. 
 * 
 * However, if you then check entire search object with:  
 * 
 * cs->IsValid(isValid); 
 * 
 * the isValid will correctly return false. 
 */
public final class ContactSearch extends SidObject {
	/** Possible values for the ContactSearch.P_STATUS property.  */
	public enum Status implements EnumConverting {
		/** Transient state, obtained after submission and actually initiating the search on the network.  */
		CONSTRUCTION(1),
		/** Waiting for results to show up. This is a transient state.  */
		PENDING     (2),
		/** Enough matches are found. No more OnNewResult events will fire. The feature of extending long search results is about to be deprecated. It is still possible for search objects to occasionally reach that state, so it should be handled in the UI (as FINISHED), but the extending feature itself should not be implemented in your UI.   */
		EXTENDABLE  (3),
		/** The search is finished. No more matches are expected. This is a terminal state.  */
		FINISHED    (4),
		/** ContactSearch failed. Better check if the search terms made any sense, with ContactSearch.IsValid. This is a terminal state.  */
		FAILED      (5);		private final int key;
		Status(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return CONSTRUCTION; }
		public EnumConverting convert(int from) { return Status.get(from); }
		public EnumConverting[] getArray(final int size) { return new Status[size]; }
		public static Status get(int from) {
			switch (from) {
			case 1: return CONSTRUCTION;
			case 2: return PENDING;
			case 3: return EXTENDABLE;
			case 4: return FINISHED;
			case 5: return FAILED;
			}
			return CONSTRUCTION;
		}
		public static final int CONSTRUCTION_VALUE = 1;
		public static final int PENDING_VALUE      = 2;
		public static final int EXTENDABLE_VALUE   = 3;
		public static final int FINISHED_VALUE     = 4;
		public static final int FAILED_VALUE       = 5;
	}
	/** List of available  matching conditions that can be used in AddTerm methods.  */
	public enum Condition implements EnumConverting {
		/** Equals  */
		EQ                    (0),
		/** Is greater than  */
		GT                    (1),
		/** Is greater or equal.  */
		GE                    (2),
		/** Is less than  */
		LT                    (3),
		/** Less or equal  */
		LE                    (4),
		/** Start of a word macthes exactly (string properties only).  */
		PREFIX_EQ             (5),
		/** Start of a word is greater or equal (string properties only).  */
		PREFIX_GE             (6),
		/** Start of a word is less or equal (string properties only).  */
		PREFIX_LE             (7),
		/** Contains the word (string properties only).  */
		CONTAINS_WORDS        (8),
		/** One of the words starts with searched value (string properties only).  */
		CONTAINS_WORD_PREFIXES(9);
		private final int key;
		Condition(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return EQ; }
		public EnumConverting convert(int from) { return Condition.get(from); }
		public EnumConverting[] getArray(final int size) { return new Condition[size]; }
		public static Condition get(int from) {
			switch (from) {
			case 0: return EQ;
			case 1: return GT;
			case 2: return GE;
			case 3: return LT;
			case 4: return LE;
			case 5: return PREFIX_EQ;
			case 6: return PREFIX_GE;
			case 7: return PREFIX_LE;
			case 8: return CONTAINS_WORDS;
			case 9: return CONTAINS_WORD_PREFIXES;
			}
			return EQ;
		}
		public static final int EQ_VALUE                     = 0;
		public static final int GT_VALUE                     = 1;
		public static final int GE_VALUE                     = 2;
		public static final int LT_VALUE                     = 3;
		public static final int LE_VALUE                     = 4;
		public static final int PREFIX_EQ_VALUE              = 5;
		public static final int PREFIX_GE_VALUE              = 6;
		public static final int PREFIX_LE_VALUE              = 7;
		public static final int CONTAINS_WORDS_VALUE         = 8;
		public static final int CONTAINS_WORD_PREFIXES_VALUE = 9;
	}
	private final static byte[] P_CONTACT_SEARCH_STATUS_req = {(byte) 90,(byte) 71,(byte) 200,(byte) 1,(byte) 93,(byte) 1};
	/** Properties of the ContactSearch class */
	public enum Property implements PropertyEnumConverting {
		P_UNKNOWN              (0,0,null,0,null),
		P_CONTACT_SEARCH_STATUS(200, 1, P_CONTACT_SEARCH_STATUS_req, 0, Status.get(0));
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
			case 200: return P_CONTACT_SEARCH_STATUS;
			}
			return P_UNKNOWN;
		}
		public static final int P_CONTACT_SEARCH_STATUS_VALUE = 200;
	}
	private final static byte[] addMinAgeTerm_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 1};
	/** construct CONTACT_BIRTHDAY term based on current time
	 * @param minAgeInYears
	 * @param addToSubs
	 * @return valid
	 */
	public boolean addMinAgeTerm(int minAgeInYears, boolean addToSubs) {
		try {
			return sidDoRequest(addMinAgeTerm_req)
			.addUintParm(1, minAgeInYears)
			.addBoolParm(2, addToSubs)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] addMaxAgeTerm_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 2};
	/** construct CONTACT_BIRTHDAY term based on current time
	 * @param maxAgeInYears
	 * @param addToSubs
	 * @return valid
	 */
	public boolean addMaxAgeTerm(int maxAgeInYears, boolean addToSubs) {
		try {
			return sidDoRequest(addMaxAgeTerm_req)
			.addUintParm(1, maxAgeInYears)
			.addBoolParm(2, addToSubs)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] addEmailTerm_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 3};
	/** Adds a search term against Contact.P_EMAILS property and pre-validates the value given in the email argument. 
	 * @param email e-mail addres to search for. 
	 * @param addToSubs This argument enables you to group conditions. See ContactSearch class details for more information. 
	 * @return valid Returns false if the value in email property did not look like a valid email address. 
	 */
	public boolean addEmailTerm(String email, boolean addToSubs) {
		try {
			return sidDoRequest(addEmailTerm_req)
			.addStringParm(1, email)
			.addBoolParm(2, addToSubs)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] addLanguageTerm_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 4};
	/**
	 * addLanguageTerm
	 * @param language
	 * @param addToSubs
	 * @return valid
	 */
	public boolean addLanguageTerm(String language, boolean addToSubs) {
		try {
			return sidDoRequest(addLanguageTerm_req)
			.addStringParm(1, language)
			.addBoolParm(2, addToSubs)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] addStrTerm_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 5};
	/** Adds a string search term to a custom contact search object.  
	 * @param prop Following Contact class string propkeys can be used for Contact search: 
	 *  - Contact.P_SKYPENAME 
	 *  - Contact.P_FULLNAME 
	 *  - Contact.P_LANGUAGES 
	 *  - Contact.P_COUNTRY 
	 *  - Contact.P_PROVINCE 
	 *  - Contact.P_CITY 
	 *  - Contact.P_PHONE_HOME 
	 *  - Contact.P_PHONE_OFFICE 
	 *  - Contact.P_PHONE_MOBILE 
	 *  - Contact.P_EMAILS 
	 *  - Contact.P_HOMEPAGE 
	 *  - Contact.P_ABOUT 
	 * Note that while Contact.P_EMAILS is technically a string and can be used in this method, it is recommended that you use ContactSearch.AddEmailTerm method instead. 
	
	 * @param cond Search condition (ContactSearch.CONDITION) 
	 * @param value Value to match against. 
	 * @param addToSubs This argument enables you to group conditions. See ContactSearch class details for more information. 
	 * @return valid Returns true if the ContactSearch term-set remains valid after adding this term. 
	 */
	public boolean addStrTerm(int prop, Condition cond, String value, boolean addToSubs) {
		try {
			return sidDoRequest(addStrTerm_req)
			.addEnumParm(1, prop)
			.addEnumParm(2, cond)
			.addStringParm(3, value)
			.addBoolParm(4, addToSubs)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;		}
	}
	private final static byte[] addIntTerm_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 6};
	/** Adds a search term to a custom contact search object. For now, there are only two searchable Contact properties that are integers, so this can oly be used for Contact.P_BIRTHDAY and Contact.P_GENDER. 
	 * @param prop Propkey to search for. Either Contact.P_BIRTHDAY or Contact.P_GENDER 
	 * @param cond Search condition (ContactSearch.CONDITION) 
	 * @param value Value to match against. 
	 * @param addToSubs This argument enables you to group conditions. See ContactSearch class details for more information. 
	 * @return valid Returns true if the ContactSearch term-set remains valid after adding this term. 
	 */
	public boolean addIntTerm(int prop, Condition cond, int value, boolean addToSubs) {
		try {
			return sidDoRequest(addIntTerm_req)
			.addEnumParm(1, prop)
			.addEnumParm(2, cond)
			.addUintParm(3, value)
			.addBoolParm(4, addToSubs)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] addOr_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 7};
	/** used to group terms (AddTerm(1), AddTerm(2), Or(), AddTerm(3), AddTerm(4), etc)
	 */
	public void addOr() {
		try {
			sidDoRequest(addOr_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] isValid_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 8};
	/** checks that terms list is non-empty and does not contain unsupported keys
	 * @return result
	 */
	public boolean isValid() {
		try {
			return sidDoRequest(isValid_req)
			.endRequest().getBoolParm(1, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return false;
		}
	}
	private final static byte[] submit_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 9};
	/** launch search
	 */
	public void submit() {
		try {
			sidDoRequest(submit_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] extend_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 10};
	/** extend if search is EXTENDABLE
	 */
	public void extend() {
		try {
			sidDoRequest(extend_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] release_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 12};
	/** Releases and disposes of the ContactSearch object. This needs to be called when the ContactSearch object reaches a terminal state (FINISHED, FAILED, EXTENDABLE). 
	 */
	public void release() {
		try {
			sidDoRequest(release_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] getResults_req = {(byte) 90,(byte) 82,(byte) 1,(byte) 11};
	/** result list is dynamically updated
	 * @param from
	 * @param count
	 * @return contacts
	 */
	public Contact[] getResults(int from, int count) {
		try {
			return (Contact[]) sidDoRequest(getResults_req)
			.addUintParm(1, from)
			.addUintParm(2, count, Integer.MIN_VALUE)
			.endRequest().getObjectListParm(1, 2, true);
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	/***
	 * generic multiget of a list of Property
	 * @param requested the list of requested properties of ContactSearch
	 * @return SidGetResponding
	 */
	public SidGetResponding sidMultiGet(Property[] requested) {
		return super.sidMultiGet(requested);
	}
	/***
	 * generic multiget of list of Property for a list of ContactSearch
	 * @param requested the list of requested properties
	 * @return SidGetResponding[] can be casted to (ContactSearch[]) if all properties are cached
	 */
	static public SidGetResponding[] sidMultiGet(Property[] requested, ContactSearch[] objects) {
		return SidObject.sidMultiGet(requested, objects);
	}
	public Status getContactSearchStatus() {
		synchronized(this) {
			if ((mSidCached & 0x1) != 0)
				return mContactSearchStatus;
		}
		return (Status) sidRequestEnumProperty(Property.P_CONTACT_SEARCH_STATUS);
	}
	public EnumConverting sidGetEnumProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 200);
		return mContactSearchStatus;
	}
	public String getPropertyAsString(final int prop) {
		switch (prop) {
		case 200: return getContactSearchStatus().toString();
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
				case 200: mContactSearchStatus = Status.get(value); break;
				default: mSidCached&=~bit; break;
				}
			}
		}
		ContactSearchListener listener = ((Skype) mSidRoot).getContactSearchListener();
		if (listener != null)
			listener.onPropertyChange(this, property, value, svalue);
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final int newValue) {
		final int propId = prop.getId();
		assert(propId == 200);
		mSidCached |= 0x1;
		mContactSearchStatus= Status.get(newValue);
	}
	public Status mContactSearchStatus;
	public int moduleId() {
		return 1;
	}
	
	public ContactSearch(final int oid, final SidRoot root) {
		super(oid, root, 1);
	}
}
