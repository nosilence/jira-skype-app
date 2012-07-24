package com.skype.ipc;

import java.io.IOException;
//import java.util.ArrayList;
//import java.lang.Boolean;
//import java.lang.Integer;
//import java.lang.Long;
import java.lang.String;
//import java.lang.ByteArray;

public interface Decoding {
	int decodeInt() throws IOException;
	int decodeUint() throws IOException;
	long decodeUint64() throws IOException;
	String decodeString() throws IOException;
	byte[] decodeBinary() throws IOException;
	void skipValue(final int kind) throws IOException;

	byte[] getBinaryParm(final int tag, final boolean finalMarker) throws IOException; 
	byte[] getBinaryParm(final int tag, final byte[] defaultValue, final boolean finalMarker) throws IOException; 
//	byte[][] getBinaryListParm(final int tag, final boolean finalMarker) throws IOException;

	boolean getBoolParm(final int tag, final boolean finalMarker) throws IOException; 
	boolean getBoolParm(final int tag, final boolean defaultValue, final boolean finalMarker) throws IOException; 
	boolean[] getBoolListParm(int tag, boolean finalMarker) throws IOException;

	EnumConverting getEnumParm(final int tag, final EnumConverting converter, final boolean finalMarker) throws IOException; 
	EnumConverting getEnumParm(int tag, EnumConverting converter, int defaultValue, boolean finalMarker) throws IOException; 
	<T extends EnumConverting> T[] getEnumListParm(final int tag, T converter, final boolean finalMarker) throws IOException;

	int getIntParm(final int tag, final boolean finalMarker) throws IOException; 
	int getIntParm(final int tag, final int defaultValue, final boolean finalMarker) throws IOException; 
	int[] getIntListParm(final int tag, final boolean finalMarker) throws IOException;

	int getUintParm(final int tag, final boolean finalMarker) throws IOException; 
	int getUintParm(final int tag, final int defaultValue, final boolean finalMarker) throws IOException; 
	int[] getUintListParm(final int tag, final boolean finalMarker) throws IOException;


	long getTimestampParm(final int tag, final boolean finalMarker) throws IOException; 
	long getTimestampParm(final int tag, final long defaultValue, final boolean finalMarker) throws IOException; 
	long[] getTimestampListParm(final int tag, final boolean finalMarker) throws IOException;

	long getUint64Parm(final int tag, final boolean finalMarker) throws IOException; 
	long getUint64Parm(final int tag, final long defaultValue, final boolean finalMarker) throws IOException; 
	long[] getUint64ListParm(final int tag, final boolean finalMarker) throws IOException;

	SidObject getObjectParm(final int tag, final int module_id, final boolean finalMarker) throws IOException; 
	SidObject[] getObjectListParm(final int tag, final int moduleId, final boolean finalMarker) throws IOException;

	String getStringParm(final int tag, final boolean finalMarker) throws IOException; 
	String getStringParm(final int tag, final String defaultValue, final boolean finalMarker) throws IOException; 
	String[] getStringListParm(final int tag, final boolean finalMarker) throws IOException;

	String getFilenameParm(final int tag, final boolean finalMarker) throws IOException; 
	String getFilenameParm(final int tag, final String defaultValue, final boolean finalMarker) throws IOException; 
	String[] getFilenameListParm(final int tag, final boolean finalMarker) throws IOException;

	String getXmlParm(final int tag, final boolean finalMarker) throws IOException; 
	String getXmlParm(final int tag, final String defaultValue, final boolean finalMarker) throws IOException; 
	String[] getXmlListParm(final int tag, final boolean finalMarker) throws IOException;

	void skipMessage() throws IOException;

	int decodeTag() throws IOException;

	boolean hasNextProperty() throws IOException;
	PropertyInfo getNextProperty() throws IOException;

	int decodeResponse() throws IOException;
	Decoding decodeEvent() throws IOException;
	int getCommand();
}


