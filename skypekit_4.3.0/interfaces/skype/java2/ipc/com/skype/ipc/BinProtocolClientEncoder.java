package com.skype.ipc;

import java.io.IOException;

final class BinProtocolClientEncoder implements Encoding {

	public BinProtocolClientEncoder(OutputTransporting transport, ClientEncodingListener listener) {
		this.transport = transport;
		this.listener  = listener;
	}

	public Encoding encodeInt(final int value) throws IOException {
		return encodeUint(value >= 0 ? value << 1 : (value << 1) ^ (~0));
	}

	public Encoding encodeUint(final int value) throws IOException {
		int v = value;
		while (v > 0x7f) {
			transport.writeByte((byte)(0x80|(v&0x7f)));
			v = v >> 7;
		}
		transport.writeByte((byte)v);
		return this;
	}

	public Encoding encodeUint64(final long value) throws IOException {
		long v = value;
		while (v > 0x7f) {
			transport.writeByte((byte)(0x80|(v&0x7f)));
			v = v >> 7;
		}
		transport.writeByte((byte)v);
		return this;
	}

	public Encoding encodeString(final String value) throws IOException {
		if (value == null || value.length() == 0) {
			transport.writeByte((byte)0);
		} else {
			byte[] utf8 = value.getBytes("UTF-8");
			encodeUint(utf8.length);
			transport.writeBytes(utf8);
		}
		return this;
	}

	public Encoding encodeBinary(final byte[] value) throws IOException {
		if (value == null || value.length == 0) {
			transport.writeByte((byte)0);
		} else {
			encodeUint(value.length);
			transport.writeBytes(value);
		}
		return this;
	}

	public Encoding addBoolParm(final int tag, final boolean value) throws IOException {
		if (!value) return this;
		transport.writeByte('T');
		return encodeUint(tag);
	}

	public Encoding addBoolParm(final int tag, final boolean value, final boolean defaultValue)  throws IOException {
		if (value == defaultValue) return this; 
		transport.writeByte(value ? (byte)'T' : (byte)'F');
		return encodeUint(tag);
	}
 
	public Encoding addBoolListParm(final int tag, final boolean[] values) throws IOException {
		if (values == null || values.length == 0) return this;
		transport.writeByte('[');
		encodeUint(tag);
		encodeUint(values.length);
		for (boolean b : values) {
			transport.writeByte(b ? (byte)'T' : (byte)'F');
		}
		transport.writeByte(']');
		return this;
	}

	public Encoding addEnumParm(int tag, int value) throws IOException {
		if (value == 0) return this;
		transport.writeByte('e');
		encodeUint(tag);
		return encodeUint(value);
	}

	public Encoding addEnumParm(final int tag, final int value, final int defaultValue) throws IOException {
		if (value == defaultValue) return this;
		transport.writeByte('e');
		encodeUint(tag);
		return encodeUint(value);
	}

	public Encoding addEnumParm(final int tag, final EnumConverting value, final int defaultValue) throws IOException {
		final int v = value.getId();
		if (v == defaultValue) return this;
		transport.writeByte('e');
		encodeUint(tag);
		return encodeUint(v);
	}

	public Encoding addEnumParm(final int tag, final EnumConverting value) throws IOException {
		if (value.getId() == 0) return this;
		transport.writeByte('e');
		encodeUint(tag);
		return encodeUint(value.getId());
	}

	public Encoding addEnumListParm(final int tag, final EnumConverting[] values)  throws IOException {
		if (values == null || values.length == 0) return this;
		transport.writeByte('[');
		encodeUint(tag);
//		encodeUint(values.length);
		for (int i = 0, e = values.length; i < e; i++) {
			transport.writeByte('e');
			encodeUint(values[i].getId());
		}
		transport.writeByte(']');
		return this;
	}

	public Encoding addIntParm(final int tag, final int value) throws IOException {
		if (value == 0) return this;
		transport.writeByte('i');
		encodeUint(tag);
		return encodeInt(value);
	}

	public Encoding addIntParm(final int tag, final int value, final int defaultValue)  throws IOException {
		if (value == defaultValue) return this;
		transport.writeByte('i');
		encodeUint(tag);
		return encodeInt(value);
	}
 
	public Encoding addIntListParm(final int tag, final int[] values) throws IOException {
		if (values == null || values.length == 0) return this;
		transport.writeByte('[');
		encodeUint(tag);
//		encodeUint(values.length);
		for (int i = 0, e = values.length; i < e; i++) {
			transport.writeByte('i');
			encodeInt(values[i]);
		}
		transport.writeByte(']');
		return this;
	}

	public Encoding addUintParm(final int tag, final int value) throws IOException {
		if (value == 0) return this;
		transport.writeByte('u');
		encodeUint(tag);
		return encodeUint(value);
	}

	public Encoding addUintParm(final int tag, final int value, final int defaultValue)  throws IOException {
		if (value == defaultValue) return this;
		transport.writeByte('u');
		encodeUint(tag);
		return encodeUint(value);
	}

	public Encoding addUintListParm(final int tag, final int[] values) throws IOException {
		if (values == null || values.length == 0) return this;
		transport.writeByte('[');
		encodeUint(tag);
//		encodeUint(values.length);
		for (int i = 0, e = values.length; i < e; i++) {
			transport.writeByte('u');
			encodeUint(values[i]);
		}
		transport.writeByte(']');
		return this;
	}

	public Encoding addTimestampParm(final int tag, final long value) throws IOException {
		if (value == 0) return this;
		transport.writeByte('u'); // actually 32bits...
		encodeUint(tag);
		return encodeUint64(value & 0xFFFFFFFFL);
	}

	public Encoding addTimestampParm(final int tag, final long value, final long defaultValue)  throws IOException {
		if (value == defaultValue) return this;
		transport.writeByte('u'); // actually 32bits...
		encodeUint(tag);
		return encodeUint64(value & 0xFFFFFFFFL);
	}
 
	public Encoding addTimestampListParm(final int tag, final long[] values) throws IOException {
		if (values == null || values.length == 0) return this;
		transport.writeByte('[');
		encodeUint(tag);
//		encodeUint(values.length);
		for (int i = 0, e = values.length; i < e; i++) {
			transport.writeByte('u'); // actually 32bits...
			encodeUint64(values[i] & 0xFFFFFFFFL);
		}
		transport.writeByte(']');
		return this;
	}

	public Encoding addUint64Parm(final int tag, final long value) throws IOException {
		if (value == 0) return this;
		transport.writeByte('U');
		encodeUint(tag);
		return encodeUint64(value);
	}

	public Encoding addUint64Parm(final int tag, final long value, final long defaultValue)  throws IOException {
		if (value == defaultValue) return this;
		transport.writeByte('U');
		encodeUint(tag);
		return encodeUint64(value);
	}
 
	public Encoding addUint64ListParm(final int tag, final long[] values) throws IOException {
		if (values == null || values.length == 0) return this;
		transport.writeByte('[');
		encodeUint(tag);
//		encodeUint(values.length);
		for (int i = 0, e = values.length; i < e; i++) {
			transport.writeByte('U');
			encodeUint64(values[i]);
		}
		transport.writeByte(']');
		return this;
	}

	public Encoding addObjectParm(final int tag, final SidObject value) throws IOException {
		if (value == null) return this;
		transport.writeByte('O');
		encodeUint(tag);
		return encodeUint(value.getOid());
	}

	public Encoding addObjectListParm(final int tag, final SidObject[] values) throws IOException {
		if (values == null || values.length == 0) return this;
		transport.writeByte('[');
		encodeUint(tag);
//		encodeUint(values.length);
		for (int i = 0, e = values.length; i < e; i++) {
			transport.writeByte('O');
			encodeUint64(values[i].getOid());
		}
		transport.writeByte(']');
		return this;
	}

	public Encoding addStringParm(final int tag, final String value) throws IOException {
		if (value == null || value.length() == 0) return this;
		transport.writeByte('S');
		encodeUint(tag);
		return encodeString(value);
	}

	public Encoding addStringParm(final int tag, final String value, final String defaultValue)  throws IOException {
		if (value != null && value.equals(defaultValue)) return this;
		transport.writeByte('S');
		encodeUint(tag);
		return encodeString(value);
	}
 
	public Encoding addStringListParm(final int tag, final String[] values) throws IOException {
		return addStringListParm(tag, values, 'S');
	}
 
	private Encoding addStringListParm(final int tag, final String[] values, final int kind) throws IOException {
		if (values == null || values.length == 0) return this;
		transport.writeByte('[');
		encodeUint(tag);
//		encodeUint(values.length);
		for (int i = 0, e = values.length; i < e; i++) {
			transport.writeByte(kind);
			encodeString(values[i]);
		}
		transport.writeByte(']');
		return this;
	}

	public Encoding addFilenameParm(final int tag, final String value) throws IOException {
		if (value == null || value.length() == 0) return this;
		transport.writeByte('f');
		encodeUint(tag);
		return encodeString(value);
	}

	public Encoding addFilenameParm(final int tag, final String value, final String defaultValue)  throws IOException {
		if (value != null && value.equals(defaultValue)) return this;
		transport.writeByte('f');
		encodeUint(tag);
		return encodeString(value);
	}
 
	public Encoding addFilenameListParm(final int tag, final String[] values) throws IOException {
		return addStringListParm(tag, values, 'f');
	}

	public Encoding addXmlParm(final int tag, final String value) throws IOException {
		if (value == null || value.length() == 0) return this;
		transport.writeByte('X');
		encodeUint(tag);
		return encodeString(value);
	}

	public Encoding addXmlParm(final int tag, final String value, final String defaultValue)  throws IOException {
		if (value != null && value.equals(defaultValue)) return this;
		transport.writeByte('X');
		encodeUint(tag);
		return encodeString(value);
	}
 
	public Encoding addXmlListParm(final int tag, final String[] values) throws IOException {
		return addStringListParm(tag, values, 'X');
	}

	public Encoding addBinaryParm(final int tag, final byte[] value) throws IOException {
		if (value == null || value.length == 0) return this;
		transport.writeByte('B');
		encodeUint(tag);
		return encodeBinary(value);
	}

	public Encoding addBinaryParm(final int tag, final byte[] value, final byte[] defaultValue)  throws IOException {
		if (value == null || value.equals(defaultValue)) return this;
		transport.writeByte('B');
		encodeUint(tag);
		return encodeBinary(value);
	}
 
	public Encoding addBinaryListParm(final int tag, final byte[][] values) throws IOException {
		if (values == null || values.length == 0) return this;
		transport.writeByte('[');
		encodeUint(tag);
//		encodeUint(values.length);
		for (int i = 0, e = values.length; i < e; i++) {
			transport.writeByte('B');
			encodeBinary(values[i]);
		}
		transport.writeByte(']');
		return this;
	}


	public Encoding beginRequest(final byte[] header, final ClientEncodingListener listener) throws IOException {
		transport.writeBytes(header);
		encodeUint(requestId++);
		this.listener = listener;
		return this;
	}

	public Encoding beginRequest(final byte[] header, final int target, final ClientEncodingListener listener) throws IOException {
		transport.writeBytes(header);
		encodeUint(requestId++);
		transport.writeByte('O');
		transport.writeByte((byte) 0);
		encodeUint(target);
		this.listener = listener;
		return this;
	}

	public Decoding endRequest() throws IOException {
		transport.writeByteAndFlush((byte) 'z');
		return listener.sidOnRequestEncoded(requestId-1);
	}

	public Decoding endOneWay() throws IOException {
		transport.writeByteAndFlush((byte) 'z');
		return listener.sidOnOneWayRequestEncoded(requestId-1);
	}

	private static final byte[] END_GET_REQUEST = { ']' , ']' , 'z' };
	public Decoding doGetRequest(final byte[] header, final SidObject[] objs, final ClientEncodingListener listener) throws IOException {
		assert(objs != null && objs.length > 0);
		transport.writeBytes(header);
		byte prefix = 0;
		for (int i = 0, e = objs.length; i < e; i++) {
			if (prefix > 0) transport.writeByte(prefix);
			encodeUint(objs[i].getOid());
			prefix = ',';
		}
		transport.writeBytesAndFlush(END_GET_REQUEST);
		return listener.sidOnGetRequestEncoded();
	}

	public Decoding doGetRequest(final byte[] header, final int oid, final ClientEncodingListener listener) throws IOException {
		transport.writeBytes(header);
		encodeUint(oid);
		transport.writeBytesAndFlush(END_GET_REQUEST);
		return listener.sidOnGetRequestEncoded();
	}

	public Encoding beginMultiGet(final PropertyEnumConverting[] properties, final int modid, final int oid, final ClientEncodingListener listener_) throws IOException {
		listener = listener_;
		transport.writeByte('Z');
		transport.writeByte('G');
		return addGetRequestItem(properties, modid, oid);
	}

	public Encoding addMultiGet(final PropertyEnumConverting[] properties, final int modid, final int oid) throws IOException {
		transport.writeByte(']');
		transport.writeByte(',');
		return addGetRequestItem(properties, modid, oid);
	}

	private Encoding addGetRequestItem(final PropertyEnumConverting[] properties, final int modid, final int oid) throws IOException{
		boolean first = true;
		for (PropertyEnumConverting p : properties) {
			if (first) {
				first = false;
			} else {
				transport.writeByte(',');
			}
			encodeUint(p.getId());
		}
		transport.writeByte(']');
		encodeUint(modid);
		encodeUint(oid);
		return this;
	}

	public Encoding addMultiGet(int oid) throws IOException {
		transport.writeByte(',');
		encodeUint(oid);
		return this;
	}

	public Decoding endMultiGet() throws IOException {
		transport.writeBytesAndFlush(END_GET_REQUEST);
		return listener.sidOnGetRequestEncoded();
	}

	private OutputTransporting     transport;
	private ClientEncodingListener listener;
	private int                    requestId;
}

