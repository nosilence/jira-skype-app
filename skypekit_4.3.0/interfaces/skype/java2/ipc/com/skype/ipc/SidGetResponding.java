package com.skype.ipc;

public interface SidGetResponding {
	SidObject      sidGetObject();

	String         sidGetStringProperty(final PropertyEnumConverting property);
	String         sidGetXmlProperty(final PropertyEnumConverting property);
	String         sidGetFilenameProperty(final PropertyEnumConverting property);
	boolean        sidGetBoolProperty(final PropertyEnumConverting property);
	int            sidGetIntProperty(final PropertyEnumConverting property);
	int            sidGetUintProperty(final PropertyEnumConverting property);
	long           sidGetLongProperty(final PropertyEnumConverting property);
	SidObject      sidGetObjectProperty(final PropertyEnumConverting property);
	EnumConverting sidGetEnumProperty(final PropertyEnumConverting property);
	byte[]         sidGetBinaryProperty(final PropertyEnumConverting property);

	void sidSetProperty(final PropertyEnumConverting property, final SidObject value); 
	void sidSetProperty(final PropertyEnumConverting property, final int value); // boolean and enums too...
	void sidSetProperty(final PropertyEnumConverting property, final String value);
	void sidSetProperty(final PropertyEnumConverting property, final byte[] value);
	void sidSetProperty(final PropertyEnumConverting property, final long value);
}

