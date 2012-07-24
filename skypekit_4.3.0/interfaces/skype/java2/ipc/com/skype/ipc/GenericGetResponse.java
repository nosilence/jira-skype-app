package com.skype.ipc;

import java.util.HashMap;

class GenericGetResponse implements SidGetResponding {
	protected GenericGetResponse(SidObject object) { 
		mObject = object; 
		mProp2Value = new HashMap<PropertyEnumConverting, SidVariant>();
	}

	static class SidVariant {
		public boolean   getAsBoolean()   { return false; }
		public SidObject getAsObject()    { return null;  }
		public int       getAsInteger()   { return 0;     }
		public EnumConverting getAsEnum() { return null;  }
		public long      getAsLong()      { return 0;     }
		public String    getAsString()    { return "";    }
		public byte[]    getAsBinary()    { return null;  }
	}

	static class SidIntVariant extends SidVariant {
		protected SidIntVariant(final int v) { value = v; }
		public int getAsInteger() { return value; }
		private int value;
	}

	static class SidBoolVariant extends SidVariant {
		protected SidBoolVariant(final boolean v) { value = v; }
		public boolean getAsBoolean() { return value; }
		private boolean value;
	}

	static class SidObjectVariant extends SidVariant {
		protected SidObjectVariant(final SidObject v) { value = v; }
		public SidObject getAsObject() { return value; }
		private SidObject value;
	}

	static class SidEnumVariant extends SidVariant {
		protected SidEnumVariant(final EnumConverting v) { value = v; }
		public EnumConverting getAsEnum() { return value; }
		private EnumConverting value;
	}

	static class SidLongVariant extends SidVariant {
		protected SidLongVariant(final long v) { value = v; }
		public long getAsLong() { return value; }
		private long value;
	}

	static class SidStringVariant extends SidVariant {
		protected SidStringVariant(final String v) { value = v; }
		public String getAsString() { return value; }
		private String value;
	}

	static class SidBinaryVariant extends SidVariant {
		protected SidBinaryVariant(final byte[] v) { value = v; }
		public byte[] getAsBinary() { return value; }
		private byte[] value;
	}

	public void sidSetProperty(final PropertyEnumConverting property, final int value) {
		if (property.isCached()) {
			mObject.sidSetProperty(property, value);
		} else {
			EnumConverting converter = property.getEnumConverter();
			if (converter != null) {
				mProp2Value.put(property, new SidEnumVariant(converter.convert(value)));
			} else {
				mProp2Value.put(property, new SidIntVariant(value));
			}
		}
	}

	public void sidSetProperty(final PropertyEnumConverting property, final String value) {
		if (property.isCached()) {
			mObject.sidSetProperty(property, value);
		} else {
			mProp2Value.put(property, new SidStringVariant(value));
		}
	}

	public void sidSetProperty(final PropertyEnumConverting property, final SidObject value) {
		if (property.isCached()) {
			mObject.sidSetProperty(property, value);
		} else {
			mProp2Value.put(property, new SidObjectVariant(value));
		}
	}

	public void sidSetProperty(final PropertyEnumConverting property, final long value) {
		if (property.isCached()) {
			mObject.sidSetProperty(property, value);
		} else {
			mProp2Value.put(property, new SidLongVariant(value));
		}
	}

	public void sidSetProperty(final PropertyEnumConverting property, final byte[] value) {
		if (property.isCached()) {
			mObject.sidSetProperty(property, value);
		} else {
			mProp2Value.put(property, new SidBinaryVariant(value));
		}
	}

	public SidObject sidGetObject() {
		return mObject;
	}

	public boolean sidGetBoolProperty(PropertyEnumConverting property) {
		if (property.isCached()) 
			return mObject.sidGetBoolProperty(property);
		return mProp2Value.get(property).getAsBoolean();
	}

	public int sidGetUintProperty(PropertyEnumConverting property) {
		return sidGetIntProperty(property);
	}

	public int sidGetIntProperty(PropertyEnumConverting property) {
		if (property.isCached()) 
			return mObject.sidGetIntProperty(property);
		return mProp2Value.get(property).getAsInteger();
	}

	public long sidGetLongProperty(PropertyEnumConverting property) {
		if (property.isCached()) 
			return mObject.sidGetLongProperty(property);
		return mProp2Value.get(property).getAsLong();
	}

	public SidObject sidGetObjectProperty(PropertyEnumConverting property) {
		if (property.isCached()) 
			return mObject.sidGetObjectProperty(property);
		return mProp2Value.get(property).getAsObject();
	}

	public EnumConverting sidGetEnumProperty(PropertyEnumConverting property) {
		if (property.isCached())
			return mObject.sidGetEnumProperty(property);
		return mProp2Value.get(property).getAsEnum();
	}

	public String sidGetStringProperty(PropertyEnumConverting property) {
		if (property.isCached()) 
			return mObject.sidGetStringProperty(property);
		return mProp2Value.get(property).getAsString();
	}

	public String sidGetXmlProperty(PropertyEnumConverting property) {
		return sidGetStringProperty(property);
	}

	public String sidGetFilenameProperty(PropertyEnumConverting property) {
		return sidGetStringProperty(property);
	}

	public byte[] sidGetBinaryProperty(PropertyEnumConverting property) {
		if (property.isCached()) 
			return mObject.sidGetBinaryProperty(property);
		return mProp2Value.get(property).getAsBinary();
	}

	private SidObject mObject;
	private HashMap<PropertyEnumConverting, SidVariant> mProp2Value; 
}


