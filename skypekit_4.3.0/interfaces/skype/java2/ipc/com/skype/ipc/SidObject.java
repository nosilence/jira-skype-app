package com.skype.ipc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class SidObject implements SidGetResponding {
    protected SidObject(final Integer oid, final SidRoot session, final int numProps) {
        mSidOid       = oid;
        mSidRoot      = session;
        mSidTimestamp = session.mSidTimestamp;
    }

    protected void finalize() {
        if (mSidRoot.mSidTimestamp == mSidTimestamp)
            mSidRoot.sidRemove(mSidOid);
    }

    // SidGetResponding interface
    public SidObject      sidGetObject()                                                     { return this;  }
    public boolean        sidGetBoolProperty(final PropertyEnumConverting property)          { return false; }
    public String         sidGetStringProperty(final PropertyEnumConverting property)        { return "";    }
    public String         sidGetXmlProperty(final PropertyEnumConverting property)           { return sidGetStringProperty(property); }
    public String         sidGetFilenameProperty(final PropertyEnumConverting property)      { return sidGetStringProperty(property); }
    public int            sidGetIntProperty(final PropertyEnumConverting property)           { return 0;     }
    public int            sidGetUintProperty(final PropertyEnumConverting property)          { return sidGetIntProperty(property); }
    public long           sidGetLongProperty(final PropertyEnumConverting property)          { return 0;     }
    public long           sidGetTimestampProperty(final PropertyEnumConverting property)     { return 0;     }
    public SidObject      sidGetObjectProperty(final PropertyEnumConverting property)        { return null;  }
    public EnumConverting sidGetEnumProperty(final PropertyEnumConverting property)          { return null;  }
    public byte[]         sidGetBinaryProperty(final PropertyEnumConverting property)        { return null;  }

    public void sidSetProperty(final PropertyEnumConverting property, final SidObject value) { } 
    public void sidSetProperty(final PropertyEnumConverting property, final int value)       { }
    public void sidSetProperty(final PropertyEnumConverting property, final String value)    { }
    public void sidSetProperty(final PropertyEnumConverting property, final byte[] value)    { }
    public void sidSetProperty(final PropertyEnumConverting property, final long value)      { }
    // end SidGetResponding interface

    protected SidObject sidRequestObjectProperty(final PropertyEnumConverting property) {
//        int expected = 1;
        SidObject value = null;
        try {
            int  oid        = 0;
            Decoding decoder = mSidRoot.sidDoGetRequest(property.getRequest(), mSidOid);
            while (decoder.hasNextProperty()) {
                PropertyInfo info = decoder.getNextProperty();
                int kind = info.kind;
//                assert (   expected-- > 0 
//                      && info.moduleId == moduleId()
//                      && info.propertyId == property.getId()
//                      && info.objectId == mSidOid
//                      && (kind == 'N' || kind == 'O')
//                     );
                if (kind != 'N') {
                    oid = decoder.decodeUint();
                }
            }
            if (oid != 0) {
                value = mSidRoot.sidGetObject(property.getModuleId(), oid);
            }
            if (property.isCached()) {
                sidSetProperty(property, value); // synchronized
            }
        } catch (IOException e) {
            mSidRoot.sidOnFatalError(e);
        }
        return value;
    }
    protected String sidRequestStringProperty(final PropertyEnumConverting property) {
        String value = "";
//      int expected = 1;
        try {
            Decoding decoder = mSidRoot.sidDoGetRequest(property.getRequest(), mSidOid);
            while (decoder.hasNextProperty()) {
                PropertyInfo info = decoder.getNextProperty();
                int kind = info.kind;
//              assert (   expected-- > 0 
//                        && info.moduleId == moduleId()
//                        && info.propertyId == property.getId()
//                        && info.objectId == mSidOid
//                        && (kind == 'N' || kind == 'S' || kind == 'f' || kind == 'X')
//                       );
                if (kind != 'N') {
                    value = decoder.decodeString();
                }
            }
            if (property.isCached()) {
                sidSetProperty(property, value); // synchronized
            }
        } catch (IOException e) {
            mSidRoot.sidOnFatalError(e);
        }
        return value;
    }
    protected byte[] sidRequestBinaryProperty(final PropertyEnumConverting property) {
        byte[] value = null;
        try {
            Decoding decoder = mSidRoot.sidDoGetRequest(property.getRequest(), mSidOid);
//        int expected = 1;
            while (decoder.hasNextProperty()) {
                PropertyInfo info = decoder.getNextProperty();
                int kind = info.kind;
//                assert (   expected-- > 0 
//                        && info.moduleId == moduleId()
//                        && info.propertyId == property.getId()
//                        && info.objectId == mSidOid
//                        && (kind == 'N' || kind == 'B')
//                       );
                if (kind != 'N') {
                    value = decoder.decodeBinary();
                }
            }
            if (property.isCached()) {
                sidSetProperty(property, value); // synchronized
            }
        } catch (IOException e) {
            mSidRoot.sidOnFatalError(e);
        }
        return value;
    }
    protected int sidRequestIntProperty(final PropertyEnumConverting property) {
       return sidRequestProperty(property);
    }
    protected int sidRequestUintProperty(final PropertyEnumConverting property) {
       return sidRequestProperty(property);
    }
    protected long sidRequestTimestampProperty(final PropertyEnumConverting property) {
       return (long) sidRequestProperty(property) & 0xFFFFFFFFL;
    }
    protected int sidRequestProperty(final PropertyEnumConverting property) {
//        int expected = 1;
        int value    = 0;
        try {
            Decoding decoder = mSidRoot.sidDoGetRequest(property.getRequest(), mSidOid);
            while (decoder.hasNextProperty()) {
                PropertyInfo info = decoder.getNextProperty();
                int kind = info.kind;
//                assert (   expected-- > 0 
//                        && info.moduleId == moduleId()
//                        && info.propertyId == property.getId()
//                        && info.objectId == mSidOid
//                        && (kind == 'N' || kind == 'T' || kind == 'F' || kind == 'u' || kind == 'i' || kind == 'e')
//                       );
                switch (kind) {
                case 'i':
                    value = decoder.decodeInt();
                    break;
                case 'u': case 'e':
                    value = decoder.decodeUint();
                    break;
                case 'T':
                    value = 1;
                    break;
                }
            }
            if (property.isCached()) {
                sidSetProperty(property, value); // synchronized
            }
        } catch (IOException e) {
            mSidRoot.sidOnFatalError(e);
        }
        return value;
    }
    protected boolean sidRequestBoolProperty(final PropertyEnumConverting property) {
        return sidRequestProperty(property) != 0;
    }
    protected EnumConverting sidRequestEnumProperty(final PropertyEnumConverting property) {
        EnumConverting converter = property.getEnumConverter();
        return converter.convert(sidRequestProperty(property));
    }
    protected String sidRequestXmlProperty(final PropertyEnumConverting property) {
        return sidRequestStringProperty(property);
    }
    protected String sidRequestFilenameProperty(final PropertyEnumConverting property) {
        return sidRequestStringProperty(property);
    }
    abstract protected void sidOnChangedProperty(final int propertyId, final int value, final String svalue);
    abstract public int moduleId();
    public int  getOid() {
        return mSidOid;
    }
    protected Encoding sidDoRequest(final byte[] header) throws IOException {
        return mSidRoot.sidDoRequest(header, mSidOid);
    }

    public static SidGetResponding[] sidMultiGet(final PropertyEnumConverting[] properties, final SidObject[] objects) {
        // normally would like to write
        // SidGetResponding[] response = objects;
        // which works as expected with regular java but dalvik VM raises a VerifyError...
        SidGetResponding[] response = new SidGetResponding[objects.length];
        boolean allCached = true;
        for (PropertyEnumConverting p : properties) {
            if (!p.isCached()) {
                allCached = false;
                response = new GenericGetResponse[objects.length];
                for (int i = 0, e = objects.length; i < e; i++) { 
                    response[i] = new GenericGetResponse(objects[i]); 
                }
                break;
            }
        }
        if (allCached) {
            for (int i = 0, e = objects.length; i < e; i++) { 
                response[i] = objects[i]; 
            }
        }
        return sidMultiGet(properties, objects, response); 
    }

    /***
     * invalidateCache: the next time the property is get, it will be querried to the runtime, meanwhile it can be discarded.
     * This allows fine grained cache management. Note that this doesn't delete the property, you still have to set it to null
     * to get a chance having this behavior. The rationale if that the generated properties being public, you can directly assign it to null
     * whilst a generated invalidateCache would require switching on the values to do so. 
     * C o; o.invalidate(C.Property.P_MY_PROP); o.mMyProp = null;
     * @param property the property to be invalidated
     */
    public void invalidateCache(final PropertyEnumConverting property) {
        int idx = property.getIdx();
        if (idx-- > 0) {
            mSidCached&=~(1<<(idx%32));
        }
    }

    protected boolean isCached(final PropertyEnumConverting property) {
        int idx = property.getIdx();
        if (idx-- > 0) {
            return (mSidCached&(1<<(idx%32))) != 0;
        }
        return false;
    }

    protected boolean hasCached() {
        return mSidCached != 0;
    }

    private PropertyInfo sidDecodeMultiGet(PropertyInfo info, final PropertyEnumConverting converter, final SidGetResponding response, final Decoding decoder) throws IOException {
        synchronized(this) {
            do {
                int kind = info.kind;
                PropertyEnumConverting property = (PropertyEnumConverting) converter.convert(info.propertyId); 
                switch (kind) {
                case 'i': response.sidSetProperty(property, decoder.decodeInt()); break;
                case 'u': case 'e': response.sidSetProperty(property, decoder.decodeUint()); break;
                case 'O': response.sidSetProperty(property, decoder.decodeUint()); break;
                case 'F': response.sidSetProperty(property, 0); break;
                case 'T': response.sidSetProperty(property, 1); break;
                case 'X': case 'S': case 'f': response.sidSetProperty(property, decoder.decodeString()); break;
                case 'B': response.sidSetProperty(property, decoder.decodeBinary()); break;
                }
                if (!decoder.hasNextProperty())
                    return null;
                info = decoder.getNextProperty();
           } while (info.objectId == mSidOid); // shall add moduleId comparison as soon as objectId definition changes 
       }
       return info;
    }

    protected SidGetResponding sidMultiGet(PropertyEnumConverting[] requested) {
        SidGetResponding response = this;
        for (PropertyEnumConverting p : requested) {
            if (!p.isCached()) {
                response = new GenericGetResponse(this);
                break;
            }
        }
        return sidMultiGet(requested, response);
    }

    protected SidGetResponding sidMultiGet(PropertyEnumConverting[] requested, SidGetResponding response) {
        PropertyEnumConverting[] to_be_querried = sidComputeToBeQuerried(requested);
        if (to_be_querried != null) {
            try {
                Decoding decoder = mSidRoot.sidBeginMultiGet(to_be_querried, moduleId(), mSidOid).endMultiGet();
                if (decoder.hasNextProperty()) { // shall always be true...
                    PropertyEnumConverting converter = to_be_querried[0];
                    sidDecodeMultiGet(decoder.getNextProperty(), converter, response, decoder);
                }
            } catch (IOException e) {
                mSidRoot.sidOnFatalError(e);
            }
        }
        return response;
    }

    private PropertyEnumConverting[] sidComputeToBeQuerried(PropertyEnumConverting[] requested) {
        PropertyEnumConverting[] to_be_querried = null;
        if (hasCached()) {
            ArrayList<PropertyEnumConverting> missed = null; 
            for (PropertyEnumConverting p : requested) { 
                if (!isCached(p)) {
                    if (missed == null)
                        missed = new ArrayList<PropertyEnumConverting>(requested.length);
                    missed.add(p);
                }
            }
            if (missed != null) {
                to_be_querried = missed.size() == requested.length ? requested : missed.toArray(new PropertyEnumConverting[missed.size()]);
            }
        } else {
            to_be_querried = requested;
        }
        return to_be_querried;
    }

    protected static SidGetResponding[] sidMultiGet(PropertyEnumConverting[] requested, SidObject[] objects, SidGetResponding[] response) {
        if (objects.length == 0 || requested.length == 0) return response;
        PropertyEnumConverting[] previous_to_be_querried = null;
        PropertyEnumConverting converter = requested[0];
        Encoding encoder = null;
        SidRoot root = objects[0].mSidRoot;
        for (SidObject o : objects) {
            PropertyEnumConverting[] to_be_querried = o.sidComputeToBeQuerried(requested);
            if (to_be_querried != null) {
                try {
                    if (previous_to_be_querried == null) {
                        encoder = root.sidBeginMultiGet(to_be_querried, o.moduleId(), o.getOid());
                    } else if (Arrays.equals(previous_to_be_querried, to_be_querried)) {
                        encoder.addMultiGet(o.getOid());
                        continue;
                    } else {
                        encoder.addMultiGet(to_be_querried, o.moduleId(), o.getOid());
                    }
                } catch (IOException e) {
                    root.sidOnFatalError(e);
                }
                previous_to_be_querried = to_be_querried;
            }
        }
        if (encoder != null) {
            try {
                Decoding decoder = encoder.endMultiGet();
                int r = 0;
                if (decoder.hasNextProperty()) { // shall always be true...
                    PropertyInfo info = decoder.getNextProperty();
                    do {
                       SidObject o = response[r].sidGetObject(); 
                       while (o.getOid() != info.objectId) {
                          o = response[++r].sidGetObject(); // some responses may already be processed.
                       }
                       info = o.sidDecodeMultiGet(info, converter, response[r++], decoder);
                    } while (info != null);
                }
            } catch (IOException e) { // catch NullPointerException?
                root.sidOnFatalError(e);
            }
        }
        return response;
    }

    protected int     mSidCached;
    protected int     mSidOid;
    protected SidRoot mSidRoot;
    protected int     mSidTimestamp;
};

