package com.skype.ipc;

import java.io.IOException;
//import java.util.ArrayList;

public interface Encoding {
	Encoding encodeInt(final int value) throws IOException;
	Encoding encodeUint(final int value) throws IOException;
	Encoding encodeUint64(final long value) throws IOException;
	Encoding encodeString(final String value) throws IOException;
	Encoding encodeBinary(final byte[] value) throws IOException;

	Encoding addBoolParm(final int tag, final boolean value) throws IOException; 
	Encoding addBoolParm(final int tag, final boolean value, final boolean defaultValue) throws IOException; 
	Encoding addBoolListParm(final int tag, final boolean[] values) throws IOException;

	Encoding addEnumParm(final int tag, final int value) throws IOException; 
	Encoding addEnumParm(final int tag, final int value, final int defval) throws IOException; 
	Encoding addEnumParm(final int tag, final EnumConverting value) throws IOException; 
	Encoding addEnumParm(final int tag, final EnumConverting value, final int defaultValue) throws IOException; 
	Encoding addEnumListParm(final int tag, final EnumConverting[] values) throws IOException;

	Encoding addIntParm(final int tag, final int value) throws IOException; 
	Encoding addIntParm(final int tag, final int value, final int defaultValue) throws IOException; 
	Encoding addIntListParm(final int tag, final int[] values) throws IOException;

	Encoding addUintParm(final int tag, final int value) throws IOException; 
	Encoding addUintParm(final int tag, final int value, final int defaultValue) throws IOException; 
	Encoding addUintListParm(final int tag, final int[] values) throws IOException;

	Encoding addUint64Parm(final int tag, final long value) throws IOException; 
	Encoding addUint64Parm(final int tag, final long value, final long default_value) throws IOException; 
	Encoding addUint64ListParm(final int tag, final long[] values) throws IOException;

	Encoding addTimestampParm(final int tag, final long value) throws IOException; 
	Encoding addTimestampParm(final int tag, final long value, final long default_value) throws IOException; 
	Encoding addTimestampListParm(final int tag, final long[] values) throws IOException;

	Encoding addObjectParm(final int tag, final SidObject value)                     throws IOException; 
	Encoding addObjectListParm(final int tag, final SidObject[] values)        throws IOException;

	Encoding addStringParm(final int tag, final String value)                        throws IOException; 
	Encoding addStringParm(final int tag, final String value, final String defaultValue)   throws IOException; 
	Encoding addStringListParm(final int tag, final String[] values)           throws IOException;

	Encoding addFilenameParm(final int tag, final String value)                      throws IOException; 
	Encoding addFilenameParm(final int tag, final String value, final String defaultValue) throws IOException; 
	Encoding addFilenameListParm(final int tag, final String[] values)         throws IOException;

	Encoding addXmlParm(final int tag, final String value) throws IOException; 
	Encoding addXmlParm(final int tag, final String value, final String defaultValue)      throws IOException; 
	Encoding addXmlListParm(final int tag, final String[] values)              throws IOException;

	Encoding addBinaryParm(final int tag, final byte[] value) throws IOException; 
	Encoding addBinaryParm(final int tag, final byte[] value, final byte[] defaultValue)   throws IOException; 
	Encoding addBinaryListParm(final int tag, final byte[][] values)                 throws IOException;

	Encoding beginRequest(final byte[] header, final ClientEncodingListener listener)                     throws IOException;
	Encoding beginRequest(final byte[] header, final int target, final ClientEncodingListener listener)         throws IOException;
	Decoding endRequest()                                                                     throws IOException;
	Decoding endOneWay()                                                                      throws IOException;

	Decoding doGetRequest(final byte[] header, final SidObject[] objs, final ClientEncodingListener listener)     throws IOException;
	Decoding doGetRequest(final byte[] header, final int oid, final ClientEncodingListener listener)        throws IOException;

	Encoding beginMultiGet(final PropertyEnumConverting[] properties, final int modid, final int oid, final ClientEncodingListener listener_) throws IOException;
	Encoding addMultiGet(final PropertyEnumConverting[] properties, final int modid, final int oid) throws IOException;
	Encoding addMultiGet(int oid) throws IOException;
	public Decoding endMultiGet() throws IOException;
}


