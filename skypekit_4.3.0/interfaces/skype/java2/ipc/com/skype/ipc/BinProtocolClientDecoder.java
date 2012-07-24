package com.skype.ipc;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;

final class BinProtocolClientDecoder implements Decoding {

	public BinProtocolClientDecoder(InputTransporting transport, ClientDecodingListener listener, ObjectFactoring factory) {
		mTransport     = transport;
		mListMaxElem   = 100000;
		mListMaxNested = 1;
		mEventBuffer   = new EventBuffer(mListMaxNested, mListMaxElem); 
		mEventDecoding = new BinProtocolClientDecoder(mEventBuffer, listener, factory);
		mPropertyInfo  = new PropertyInfo();
		mFactory       = factory;
		mListener      = listener;
	}

	private BinProtocolClientDecoder(EventBuffer transport, ClientDecodingListener listener, ObjectFactoring factory) {
		mTransport     = transport;
		mListMaxElem   = 100000;
		mListMaxNested = 1;
		mEventBuffer   = null;
		mEventDecoding = null;
		mPropertyInfo  = new PropertyInfo();
		mFactory       = factory;
		mListener      = listener;
	}

	public int decodeInt() throws IOException {
		return uint2int(decodeUint());
        }

	public int uint2int(final int number) {
		if ((1 & number) != 0) {
			// negative
			return (number ^ (~0)) >> 1;
		}
		// positive
		return  number >> 1;
	}

	public int decodeUint() throws IOException {
		int shift = 0;
		int result = 0;
		while (true) {
			int value = mTransport.readByte() & 0xFF;
			result = result | ((value & 0x7f) << shift);
			shift = shift + 7;
			if ((value & 0x80) == 0)
				break;
        	}
	        return result;
	}

	public long decodeUint64() throws IOException {
		int shift = 0;
		long result = 0;
		while (true) {
			int value = mTransport.readByte() & 0xFF;
			result = result | ((value & 0x7f) << shift);
			shift = shift + 7;
			if ((value & 0x80) == 0)
				break;
        	}
	        return result;
	}

	public String decodeString() throws IOException {
		int uft8len = decodeUint();
		if (uft8len > 0) {
			byte[] buf = new byte[uft8len];
			mTransport.readBytes(buf);
			return new String(buf, "UTF-8");
		}
		return new String();
	}

	public byte[] decodeBinary() throws IOException {
		int len = decodeUint();
		if (len > 0) {
			byte[] buf = new byte[len];
			mTransport.readBytes(buf);
			return buf;
		}
		return null;
	}

	public boolean getBoolParm(final int tag, final boolean finalMarker) throws IOException {
		return getBoolParm(tag, false, finalMarker);
	}

	public boolean getBoolParm(final int tag, final boolean defaultValue, final boolean finalMarker) throws IOException {
		boolean value = defaultValue;
		if (expectTag('b', tag)) {
			value = mNextKind == 'T';
		}
		if (finalMarker) {
			skipMessage();
		}
		return value;
	}
 
	public boolean[] getBoolListParm(final int tag, final boolean finalMarker) throws IOException {
		// if the protocol would include size, one may do new boolean[size]...
		ArrayList<Boolean> list = new ArrayList<Boolean>();
		if (expectTag('[', tag)) {
			int kind;
			while ((kind = mTransport.readByte()) !=  ']') {
				if (kind != 'T' && kind != 'F') throw new ProtocolException("unbound list");
				list.add(kind ==  'T');
			}
			if (kind !=  ']') {
				throw new ProtocolException("unbound list");
			}
		}
		if (finalMarker) {
			skipMessage();
		}
		boolean[] rsp = new boolean[list.size()];
		for (int i = 0, e = rsp.length; i < e; i++)
			rsp[i] = list.get(i);
		return rsp;
	}

	public EnumConverting getEnumParm(final int tag, final EnumConverting converter, final int defaultValue, final boolean finalMarker) throws IOException { 
		return converter.convert(getUintParm(tag, defaultValue, finalMarker, 'e'));
	}

	public EnumConverting getEnumParm(final int tag, final EnumConverting converter, final boolean finalMarker) throws IOException { 
		return converter.convert(getUintParm(tag, 0, finalMarker, 'e'));
	}

	public <T extends EnumConverting> T[] getEnumListParm(final int tag, final T converter, final boolean finalMarker) throws IOException {
		ArrayList<EnumConverting> list = new ArrayList<EnumConverting>();
		if (expectTag('[', tag)) {
			int kind;
			while ((kind = mTransport.readByte()) == 'e') {
				list.add(converter.convert(decodeUint()));
			}
			if (kind != ']') throw new ProtocolException("unbound list");
		}
		if (finalMarker) {
			skipMessage();
		}
		converter.getArray(list.size());
		T[] asarray = null;
		return list.toArray(asarray);
	}

	public int getIntParm(final int tag, final boolean finalMarker) throws IOException {
		return uint2int(getUintParm(tag, 0, finalMarker, 'i'));
	}

	public int getIntParm(final int tag, final int defaultValue, final boolean finalMarker) throws IOException {
		return uint2int(getUintParm(tag, defaultValue, finalMarker, 'i'));
	}
 
	public int[] getIntListParm(final int tag, final boolean finalMarker) throws IOException {
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (expectTag('[', tag)) {
			int kind;
			while ((kind = mTransport.readByte()) == 'i') {
				list.add(decodeInt());
			}
			if (kind != ']') throw new ProtocolException("unbound list");
		}
		int[] a = new int[list.size()];
		for (int i = 0, e = list.size(); i < e; i++)
			a[i] = list.get(i);
		if (finalMarker) {
			skipMessage();
		}
		return a;
	}

	public int getUintParm(final int tag, final boolean finalMarker) throws IOException {
		return getUintParm(tag, 0, finalMarker, 'u');
	}
 
	public int getUintParm(final int tag, final int defaultValue, final boolean finalMarker) throws IOException {
		return getUintParm(tag, defaultValue, finalMarker, 'u');
	}

	public int getUintParm(final int tag, final int defaultValue, final boolean finalMarker, final int kind) throws IOException {
		int value = defaultValue;
		if (expectTag(kind, tag)) {
			value = decodeUint();
		}
		if (finalMarker) {
			skipMessage();
		}
		return value;
	}
 
	public int[] getUintListParm(final int tag, final boolean finalMarker) throws IOException {
		return getUintListParm(tag, finalMarker, 'u');
	}

	public int[] getUintListParm(final int tag, final boolean finalMarker, final int kind) throws IOException {
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (expectTag('[', tag)) {
			int streamKind;
			while ((streamKind = mTransport.readByte()) == kind) {
				list.add(decodeUint());
			}
			if (streamKind != ']') throw new ProtocolException("unbound list");
		}
		int[] a = new int[list.size()];
		for (int i = 0, e = list.size(); i < e; i++)
			a[i] = list.get(i);
		if (finalMarker) {
			skipMessage();
		}
		return a;
	}

	public long getUint64Parm(final int tag, final boolean finalMarker) throws IOException {
		return getUint64Parm(tag, 0, finalMarker, 'U');
	}
 
	public long getUint64Parm(final int tag, final long defaultValue, final boolean finalMarker) throws IOException {
		return getUint64Parm(tag, defaultValue, finalMarker, 'U');
	}
 
	public long getUint64Parm(final int tag, final long defaultValue, final boolean finalMarker, final int kind) throws IOException {
		long value = defaultValue;
		if (expectTag(kind, tag)) {
			value = decodeUint64();
		}
		if (finalMarker) {
			skipMessage();
		}
		return value;
	}
 
	public long[] getUint64ListParm(final int tag, final boolean finalMarker) throws IOException {
		return getUint64ListParm(tag, finalMarker, 'U');
	}
 
	public long[] getUint64ListParm(final int tag, final boolean finalMarker, final int kind) throws IOException {
		ArrayList<Long> list = new ArrayList<Long>();
		if (expectTag('[', tag)) {
			int streamKind;
			while ((streamKind = mTransport.readByte()) == kind) {
				// assert(kind == 'U');
				list.add(decodeUint64());
			}
			if (streamKind != ']') throw new ProtocolException("unbound list");
		}
		long[] a = new long[list.size()];
		for (int i = 0, e = list.size(); i < e; i++)
			a[i] = list.get(i);
		if (finalMarker) {
			skipMessage();
		}
		return a;
	}

	public long getTimestampParm(final int tag, final boolean finalMarker) throws IOException {
		return getUint64Parm(tag, 0, finalMarker, 'u');
	}

	public long getTimestampParm(final int tag, final long defaultValue, final boolean finalMarker) throws IOException {
		return getUint64Parm(tag, defaultValue, finalMarker, 'u');
	}
 
	public long[] getTimestampListParm(final int tag, final boolean finalMarker) throws IOException {
		return getUint64ListParm(tag, finalMarker, 'u'); // trick...
	}

	public SidObject getObjectParm(final int tag, final int moduleId, final boolean finalMarker) throws IOException { 
		return mFactory.sidGetObject(moduleId, getUintParm(tag, 0, finalMarker, 'O'));
	}

	public SidObject[] getObjectListParm(final int tag, final int moduleId, final boolean finalMarker) throws IOException {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		if (expectTag('[', tag)) {
			int kind;
			while ((kind = mTransport.readByte()) == 'O') {
				ids.add(decodeUint());
			}
			if (kind != ']') throw new ProtocolException("unbound list");
		}
		SidObject[] list = mFactory.sidGetObjects(moduleId, ids.size());
		for (int i = 0, e = list.length; i < e; i++) {
			list[i] = mFactory.sidGetObject(moduleId, ids.get(i));
		}
		if (finalMarker) {
			skipMessage();
		}
		return list;
	}

	public String getStringParm(final int tag, final boolean finalMarker) throws IOException {
		return getStringParm(tag, "", finalMarker, 'S');
	}
 
	public String getStringParm(final int tag, final String defaultValue, final boolean finalMarker) throws IOException { 
		return getStringParm(tag, defaultValue, finalMarker, 'S');
	}

	private String getStringParm(final int tag, final String defaultValue, final boolean finalMarker, final int kind) throws IOException { 
		String value = defaultValue;
		if (expectTag(kind, tag)) {
			value = decodeString();
		}
		if (finalMarker) {
			skipMessage();
		}
		return value;
	}

	public String[] getStringListParm(final int tag, final boolean finalMarker) throws IOException {
		return getStringListParm(tag, finalMarker, 'S');
	}

	public String[] getStringListParm(final int tag, final boolean finalMarker, final int kind) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		if (expectTag('[', tag)) {
			int streamKind;
			while ((streamKind = mTransport.readByte()) == kind) {
				list.add(decodeString());
			}
			if (streamKind != ']') throw new ProtocolException("unbound list");
		}
		if (finalMarker) {
			skipMessage();
		}
		return list.toArray(new String[list.size()]);
	}


	public String getFilenameParm(final int tag, final boolean finalMarker) throws IOException {
		return getStringParm(tag, "", finalMarker, 'f');
	}
 
	public String getFilenameParm(final int tag, final String defaultValue, final boolean finalMarker) throws IOException { 
		return getStringParm(tag, defaultValue, finalMarker, 'f');
	}

	public String[] getFilenameListParm(final int tag, final boolean finalMarker) throws IOException {
		return getStringListParm(tag, finalMarker, 'f');
	}

	public String getXmlParm(int tag, boolean finalMarker) throws IOException {
		return getStringParm(tag, "", finalMarker, 'X');
	}
 
	public String getXmlParm(final int tag, final String defaultValue, final boolean finalMarker) throws IOException {
		return getStringParm(tag, defaultValue, finalMarker, 'X');
	}

	public String[] getXmlListParm(final int tag, final boolean finalMarker) throws IOException {
		return getStringListParm(tag, finalMarker, 'X');
	}

	public byte[] getBinaryParm(final int tag, final boolean finalMarker) throws IOException {
		return getBinaryParm(tag, null, finalMarker);
	}
 
	public byte[] getBinaryParm(final int tag, final byte[] defaultValue, final boolean finalMarker) throws IOException {
		byte[] value = defaultValue;
		if (expectTag('B', tag)) {
			value = decodeBinary();
		}
		if (finalMarker) {
			skipMessage();
		}
		return value == null ? new byte[0]: value;
	}

/*	byte[][] getBinaryListParm(int tag, boolean finalMarker) throws IOException {
		List<ByteArray> list = new ArrayList<ByteArray>();
		if (expectTag('[', tag)) {
			int kind;
			while ((kind = mTransport.readByte()) == 'B') {
				list.add(decodeBinary());
			}
			if (kind != ']') throw new ProtocolException("unbound list");
		}
		if (finalMarker) {
			skipMessage();
		}
		return list;
	}
*/
	private boolean expectTag(final int kind, final int expected) throws IOException {
		do {
			if (mRead) {
				mNextKind = mTransport.readByte();
				if (mNextKind != 'z') {
					if (mNextKind != 'N') {
						mNextTag = decodeUint();
					} else {
						// a bit tricky, but the bool parm will have expect tag
						// to false and thus default to false...
						mNextKind = mTransport.readByte();
						if (mNextKind != 'z')
							throw new ProtocolException("end of message expected");
					}
				}
			}
			if (mNextKind == 'z') { mRead = false; return false; } 
			if (mNextTag == expected) {
				if (!(kind == mNextKind || (kind == 'b' && (mNextKind == 'T' ||  mNextKind == 'F'))))
					throw new ProtocolException("unknown kind"+mNextKind);
				mRead = true;
				return true;
			} else if (mNextTag > expected) {
				mRead = false;
				return false;
			} else if (mNextTag < expected) {
				mRead = true;
				skipValue(mNextKind);
			}
		} while (true);
	}

	public void skipValue(final int kind)  throws IOException {
		switch (kind) {
		case '[':
			if (mListDepth++ > mListMaxNested) throw new ProtocolException("mListDepth");
			int k = mTransport.readByte();
			int ne = 0;
			while (k != ']') {
				skipValue(k);
				if (ne++ > mListMaxElem) throw new ProtocolException("list too large");
			}
			return;
		case 'i': case 'O': case 'u': case 'e': case 'b':
			decodeUint();
			break;
		case 'U':
			decodeUint64();
			break;
		case 'S': case 'X': case 'f': case 'B': {
			mTransport.skipBytes(decodeUint());
			break;
		}
		case 'T': case 'F': case 'N':
			break;
		default:
			throw new ProtocolException("unknown kind "+kind);
		}
	}

	public void skipMessage() throws IOException {
		if (!mRead) {
			if (mEventBuffer != null)
				mListener.sidOnMessageDecoded();
			return; 
		}
		do {
			int kind = mTransport.readByte();
			if (kind == 'N') {
				 kind = mTransport.readByte();
				 if (kind != 'z') throw new ProtocolException("");
			}
			if (kind == 'z') { 
				mRead = false; 
				if (mEventBuffer != null)
					mListener.sidOnMessageDecoded();
				return; 
			}  
			decodeUint();
			skipValue(kind);
		} while (true);
	}

	public int decodeTag() throws IOException {
		return decodeUint();
	}

	private enum PropertyState { FINISHED, BEGIN, CONTINUE };

	public boolean hasNextProperty() throws IOException {
		switch (mNextPropertyState) {
		case FINISHED:
			return false;
		case BEGIN:
			mPropertyInfo.moduleId   = decodeUint();
			mPropertyInfo.objectId   = decodeUint();
			mPropertyInfo.kind       = mTransport.readByte();
			mPropertyInfo.propertyId = decodeUint();
			mNextPropertyState       = PropertyState.CONTINUE; 
			return true;
		case CONTINUE:
			int sign = mTransport.readByte();
			if (sign != ']') { // next property
				mPropertyInfo.kind       = mTransport.readByte();
				mPropertyInfo.propertyId = decodeUint();
				return true;
			} else { // if (sign == ']') { // end of object
				sign = mTransport.readByte();
				if (sign == ',') { // next object
					mPropertyInfo.objectId   = decodeUint();
					mPropertyInfo.kind       = mTransport.readByte();
					mPropertyInfo.propertyId = decodeUint();
					return true;
				} else if (sign == ']') { // end of module
					sign = mTransport.readByte();
					if (sign == ']') { // end of message
						sign = mTransport.readByte();
						if (sign == 'z') {
							mNextPropertyState = PropertyState.FINISHED;
							if (mEventBuffer != null)
								mListener.sidOnMessageDecoded();
							return false;
						}
					} else if (sign == ',') { // next module
						mPropertyInfo.moduleId   = decodeUint();
						mPropertyInfo.objectId   = decodeUint();
						mPropertyInfo.kind       = mTransport.readByte();
						mPropertyInfo.propertyId = decodeUint();
						return true;
					}
				}
			}
			mNextPropertyState = PropertyState.FINISHED; 
			throw new ProtocolException("unexpected marker");
		}
		return true;
	}

	public PropertyInfo getNextProperty() throws IOException {
		return mPropertyInfo;
	}

	public int readCommand() throws IOException {
		while (mNextKind != 'Z') {
			mNextKind = mTransport.readByte();
		}
		mNextKind = mTransport.readByte();
		mRead = true; 
		return mNextKind;
	}

	public int decodeResponse() throws IOException {
		assert(mEventBuffer != null);
		do {
			int kind = readCommand();
			switch (kind) {
			case 'g': 
				mNextPropertyState = PropertyState.BEGIN;
				return kind;
			case 'r': return kind;
			case 'E': mEventBuffer.bufferEvent(mTransport); 
				  mListener.sidOnEventBuffered();
                                  break;
			case 'C': mEventBuffer.bufferChange(mTransport);
				  mListener.sidOnEventBuffered();
                                  break; 
			}
		} while (true);
	}

	public Decoding decodeEvent() throws IOException {
		if (mEventBuffer == null) {
			// we have been called from the top level decoder, because it knows,
			// there are data
			mEventKind = readCommand();
			if (mEventKind == 'C' || mEventKind == 'g') 
				mNextPropertyState = PropertyState.BEGIN;
			return this;
		}
		if (!mEventBuffer.isEmpty())
			return mEventDecoding.decodeEvent(); 
		// need ensuring here there is no other competing reader...
		while (mListener.sidWantRead()) {
			// thus we may block on read and the competing reader may have added event in the buffer meanwhile
			if (!mEventBuffer.isEmpty()) {
				return mEventDecoding.decodeEvent(); 
			}
			mEventKind = readCommand();
			if (mEventKind == 'C' || mEventKind == 'g') 
				mNextPropertyState = PropertyState.BEGIN;
			return this;
		}
		return this;
	}

	public int getCommand() {
		return mEventKind;
	}

	private int                    mEventKind;
	private int                    mNextKind;
	private int                    mNextTag;
	private boolean                mRead;
	private PropertyState          mNextPropertyState;
	private InputTransporting      mTransport;
	private ClientDecodingListener mListener;
	private PropertyInfo           mPropertyInfo;
	private EventBuffer            mEventBuffer;
	private Decoding               mEventDecoding;
	private ObjectFactoring        mFactory;
	private int                    mListMaxElem;
	private int                    mListMaxNested;
	private int                    mListDepth;
}

