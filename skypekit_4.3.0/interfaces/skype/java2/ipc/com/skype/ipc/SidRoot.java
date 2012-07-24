package com.skype.ipc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.SoftReference;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

/***
 * Base class for a SID interface. Most of function are internal even when public. The functions to be used in the application are documented ones.
 */
public abstract class SidRoot implements ClientEncodingListener, ClientDecodingListener, ObjectFactoring {

    /*
     * There is an event thread that listen to the connection for event or responses
     * when waiting a response, events are buffered
     * if the event thread has buffered events, it can process them in parallel with the other reader
     * thread waiting for a response
     */

    protected SidRoot() {
        mSidTimestamp = 0;
    }

    protected class EventThread extends Thread {
        public EventThread(SidRoot root) {
            super();
            mRoot = root;
        }

        public void run() {
            mRoot.sidPollEvent();
        }

        private SidRoot mRoot;
    }

    protected void sidPollEvent() {
        try {
            while (!mSidStopped) {
                Decoding decoder = mSidDecoder.decodeEvent();
                switch (decoder.getCommand()) {
                case 'C': 
//System.out.println("### sidPollEvent(): sidOnChangedProperty "+(decoder==mSidDecoder));
                    sidOnChangedProperty(decoder);
                    break;
                case 'E':
//System.out.println("### sidPollEvent(): sidOnChangedProperty "+(decoder==mSidDecoder));
                    int mid  = decoder.decodeUint();
                    int evid = decoder.decodeUint();
                    sidDispatchEvent(mid, evid, decoder);
                    break;
                case 'g':
                    sidAddPendingGetResponse();
                    break;
                case 'r':
                    sidAddPendingResponse(decoder.decodeUint());
                    break;
                }
            }
        } catch (IOException e) {
            sidOnFatalError(e);
        }
    }

    public abstract void sidDispatchEvent(int modId, int evId, Decoding decoder);

    /***
     * initialize the connection to the runtime, and the interface cache accordingly to the configuration
     * @param configuration defining the connection parameters to the runtime
     * @param listener callbacks to check the status of the connection
     * return success or failure to connect
     */
    public boolean init(ClientConfiguration configuration, ConnectionListener listener) {
        registerConnectionListener(listener);

        TransportFactory factory = configuration.getTransportFactory();
        TransportFactory.Result transport = factory.init(configuration, listener);
        if (transport != null) {
            mSidInput           = transport.in;
            mSidOutput          = transport.out;
            mSidDispatchAll     = configuration.isDispatchAll();
            mSidDecoder         = new BinProtocolClientDecoder(transport.in, this, this);
            mSidEncoder         = new BinProtocolClientEncoder(transport.out, this);
            flushCache(null);
            mSidEncoderLock     = new ReentrantLock();
            mSidEncoding        = false;
            mSidPendingRequests = new HashMap<Integer, Thread>(2);
            mSidPendingOneWayRequests = new HashSet<Integer>(2);
            mSidPendingGets     = new ArrayDeque<Thread>(2);
            mSidEventThread     = new EventThread(this);
            mSidReader          = mSidEventThread;
            mSidPrevReader      = null;
            mSidEventThread.start();
        }
        return mSidObjects != null;
    }

    protected SidObject sidDecodeEventTarget(int mid, Decoding decoder)
    {
        SidObject target = null;
        try {
            int b = decoder.decodeTag(); // shall be 'O'
            int zero = decoder.decodeUint();
            int oid = decoder.decodeUint();
            target = mSidDispatchAll ? sidGetObject(mid, oid) : sidGetObjectIfPresent(mid, oid);
            if (target == null) {
                decoder.skipMessage();
            }
        }
        catch (IOException e) {
            sidOnFatalError(e);
        }
        return target;
    }

    /***
     * start the session after init, once all the runtime setups have been done
     */
    public abstract boolean start();

    /***
     * stop and terminate the connection to a runtime
     */
    public synchronized boolean stop() {
        if (mSidEncoding) {
            try {
                mSidEncoderLock.unlock();
                mSidEncoding = false;
            } catch (java.lang.IllegalMonitorStateException e) {
            }
        }
        if (mSidStopped) return true;
        mSidStopped = true;
        try {
            if (mSidInput != null) mSidInput.close();
        } catch (IOException e) {
        }
        try {
            if (mSidOutput != null) mSidOutput.close();
        } catch (IOException e) {
        }
        if (mSidPrevReader != null)
            mSidPrevReader.interrupt(); 
        if (mSidReader != null && mSidReader != mSidPrevReader)
            mSidReader.interrupt(); 
        if (mSidPendingRequests != null)
            for (Thread thread : mSidPendingRequests.values()) {
                thread.interrupt();
            }
        if (mSidPendingGets != null)
            for (Thread thread : mSidPendingGets) {
                thread.interrupt();
            }
        return false;
    }

    public void sidOnFatalError(IOException e) {
        if (!stop()) {
            if (mSidConnectionListener != null) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                mSidConnectionListener.sidOnDisconnected(sw.toString());
            } else {
                e.printStackTrace();
                System.exit(-1);
            }
        }// else
//                e.printStackTrace();
    }

    //
    // Object Cache Management
    //

    public SidObject sidGetObjectIfPresent(int mid, int oid) {
        Integer key = oid; //new Integer(oid);
        SidObject o = null;
        synchronized (mSidObjects) {
            SoftReference<SidObject> r = mSidObjects.get(key); 
            if (r != null) {
                o = r.get();
                if (o == null) {
                    o = sidCreateObject(mid, oid);
                    mSidObjects.put(key, new SoftReference<SidObject>(o));
                }
            }
        }
        return o;
    }

    public SidObject sidGetObject(int mid, int oid) {
        Integer key = oid; //new Integer(oid);
        SidObject o = null;
        synchronized (mSidObjects) {
            SoftReference<SidObject> r = mSidObjects.get(key); 
            if (r != null) {
                o = r.get();
            }
            if (o == null) { 
                o = sidCreateObject(mid, oid);
                mSidObjects.put(key, new SoftReference<SidObject>(o));
            }
        }
        return o;
    }
    protected abstract SidObject sidCreateObject(int mid, int oid);

    protected void sidRemove(Integer oid) {
        synchronized (mSidObjects) {
            mSidObjects.remove(oid);
        }
    }

    protected Encoding sidBeginMultiGet(PropertyEnumConverting[] properties, int modid, int oid) throws IOException {
        mSidEncoderLock.lock();
        mSidEncoding = true;
        return mSidEncoder.beginMultiGet(properties, modid, oid, this);
    }
    
    protected Decoding sidDoGetRequest(byte[] request, int oid) throws IOException { // case of multi request?
        mSidEncoderLock.lock();
        mSidEncoding = true;
        return mSidEncoder.doGetRequest(request, oid, this);
    }

    protected Encoding sidDoRequest(byte[] request) throws IOException {
        mSidEncoderLock.lock();
        mSidEncoding = true;
        return mSidEncoder.beginRequest(request, this);
    }

    protected Encoding sidDoRequest(byte[] request, int oid) throws IOException {
        mSidEncoderLock.lock();
        mSidEncoding = true;
        return mSidEncoder.beginRequest(request, oid, this);
    }
    
    public Decoding sidOnGetRequestEncoded() throws IOException {
        // current thread has posted a get request and no other can post one for now
        Thread current   = Thread.currentThread();
        boolean iamreader = false;
        boolean responded = false;
        synchronized (this) {
            responded = mSidPendingGetResponse;
            if (responded) { 
                // assert(mSidPendingGets.size() == 0 && mSidPrevReader == mSidPendingGetResponse);
                // my response is already here... and was catched by mSidPrevReader
                // we will read it, and resume mSidReader once we have read it
                mSidPendingGetResponse = false;
                mSidReader = current;
//System.out.println("### sidOnGetRequestEncoded, reader already got my answer "+current+ " Prev:"+mSidPrevReader);
            } else {
                // add us to the get request pending queue 
                mSidPendingGets.add(current); 
                // if no one is reading, current is promoted as reader
                if (mSidReader == null) {
                    mSidReader = current;
                    iamreader  = true;
//System.out.println("### sidOnGetRequestEncoded, no reader electing myself "+current);
                }
            }
        }
        mSidEncoding = false;
        mSidEncoderLock.unlock();
        // we released the encoding lock...  there can be some race condition here,
        // but if the response is here, or the current thread is promoted reader
        if (!responded) {
            // the response wasn't here, we have to wait for it
            if (!iamreader) {
                // someone else is reading, we have to wait to be waken up,
                // either because no one reads anymore, or because mSidReader
                // catched our response
                synchronized (current) {
                    try {
                        while (mSidReader != current)
                            current.wait();
                        iamreader = mSidPrevReader == null;
                    } catch (InterruptedException ie) {
                        mSidReader = null;
                    }
                }
            }
            if (iamreader) {
                // current thread became the new reader and shall wait for a response
                sidWaitResponse();
            }
        }
        // response is here, mine and needs being decoded (and once decoded, we must resume mSidReader)
        return mSidDecoder;
    }
    
    public Decoding sidOnRequestEncoded(int requestId) throws IOException {
        mSidEncoding = false;
        mSidEncoderLock.unlock();
        Thread current = Thread.currentThread();
        boolean iamreader = false;
        Integer rid    = requestId;
        synchronized (this) {
            Thread reader = mSidPendingRequests.get(rid);
            if (reader != null) {
                // response already here and was catched by reader
                // assert(reader == mSidReader == mSidPrevReader);
                mSidPendingRequests.remove(rid);
                mSidReader = current; 
//System.out.println("### sidOnRequestEncoded "+requestId+", reader already got my answer "+current+ " Prev:"+mSidPrevReader);
                return mSidDecoder;
            } else
//System.out.println("### sidOnRequestEncoded"+requestId+", by "+current);
            mSidPendingRequests.put(rid, current);
            if (mSidReader == null) {
//System.out.println("### sidOnRequestEncoded"+requestId+", no reader electing myself "+current);
                mSidReader = current;
                iamreader = true;
            }
        }
        if (!iamreader) {
            synchronized (current) {
                try {
                    while (mSidReader != current)
                        current.wait();
                    // 2 cases => either there was no more reader and we were promoted reader
                    //         => or the reader got our response, we must decode it
                    iamreader = mSidPrevReader == null;
                } catch (InterruptedException ie) {
                    mSidReader = null;
                }
            }
        }
        if (iamreader) { // we were wakenup to read or there was no reader
            sidWaitResponse();
        }
        return mSidDecoder;
    }

    public Decoding sidOnOneWayRequestEncoded(int requestId) {
        mSidEncoding = false;
        mSidEncoderLock.unlock();

        Thread responder = null;
        Thread current   = Thread.currentThread();
        Integer rid      = requestId;

        synchronized (this) {
            responder = mSidPendingRequests.get(rid);
            if (responder != null) { 
                // the response is already here and was read by the thread "mSidPrevReader"
                // the responder is waiting that we are parsing our response to resume
                // reading
                // assert(mSidPrevReader != null && mSidPrevReader == mSidReader)
                mSidPendingRequests.remove(rid);
                mSidReader = current; 
//System.out.println("### sidOnOneWayRequestEncoded"+requestId+", read signaled response, taking reading "+current+" prev:"+mSidPrevReader);
            } else { 
                // the response is not here, mark it as one way so that the reader thread
                // can skip it
                mSidPendingRequests.put(rid, current); 
                mSidPendingOneWayRequests.add(rid);
//System.out.println("### sidOnOneWayRequestEncoded"+requestId+", by "+current);
            }
        }

        if (responder != null) {
            // so, another thread catched our response and didn know it was one way,
            // thus it notified us to read it
            // we must ignore the rest of the response and tell the other thread
            // it can resume reading
            try {
//System.out.println("### sidOnOneWayRequestEncoded"+requestId+", skipping "+current);
                mSidDecoder.skipMessage();
            } catch (IOException e) {
                sidOnFatalError(e);
            }
        }

        return null;
    }

    void sidOnChangedProperty(Decoding decoder) {
        // currently only 1 notified at once and only int-alike properties are valued, else design shall be changed to ensure that all properties are decoded
        // before calling the callbacks
        int expected = 1;
        int kind;
        int oid       = 0;
        int moduleId  = 0;
        int value     = 0;
        String svalue = null;
        int propertyId= -1;
        try {
            while (decoder.hasNextProperty()) {
                assert(expected-- > 0);
                PropertyInfo info = decoder.getNextProperty();
                kind       = info.kind; 
                moduleId   = info.moduleId;
                oid        = info.objectId;
                propertyId = info.propertyId;
                switch (kind) {
                case 'F': case 'N': break;
                case 'T': value = 1; break;
                case 'O': case 'e': case 'u': value = decoder.decodeUint(); break;
                case 'i': value = decoder.decodeInt(); break;
                case 'S': case 'f': case 'X': svalue =  decoder.decodeString(); break;
                default:
                    decoder.skipValue(kind);
                    break;
                }
            }
        } catch (IOException e) {
            sidOnFatalError(e);
        }
        if (oid>0) {
            SidObject o = mSidDispatchAll ? sidGetObject(moduleId, oid) : sidGetObjectIfPresent(moduleId, oid);
            if (o != null) {
                o.sidOnChangedProperty(propertyId, value, svalue);
            }
        }
    }

    public boolean sidWantRead() {
        synchronized (this) {
           if ((mSidReader == null || mSidReader == mSidEventThread) && mSidPrevReader == null) {
//System.out.println("### sidWantRead() elected because "+mSidReader);
                 mSidReader = mSidEventThread;
                 return true;
            } else {
//System.out.println("### sidWantRead() event thread ready ");
                mSidReadyEventThread = true;
            }
        }
        synchronized (mSidEventThread) { // we may have been elected reader while locking on this
            try {
                while (mSidReader != mSidEventThread && mSidReadyEventThread)
                    mSidEventThread.wait();
//System.out.println("### sidWantRead() running "+mSidReader);
            } catch (InterruptedException ie) {
                return false;
            }
        }
        return true;
    }

    public void sidOnEventBuffered() {
         synchronized (this) {
            if (mSidReadyEventThread) {
//System.out.println("### sidOnEventBuffered, wake up event thread ");
                mSidReadyEventThread = false;
            } else {
//System.out.println("### sidOnEventBuffered, event thread not ready ");
               return;
            }
         }
         synchronized (mSidEventThread) {
            mSidEventThread.notify();
         }
    }

    public void sidOnMessageDecoded() {
        Thread newReader = null;
        synchronized (this) {
            newReader      = mSidPrevReader;
            mSidPrevReader = null;
            if (newReader == null) {
                // elect a requester if any, provided that it waits for a response
                if (!mSidPendingGets.isEmpty()) {
                    newReader = mSidPendingGets.peek(); 
                } else if (!mSidPendingRequests.isEmpty()) {
                    // can be oneway... then better ignoring
                    for (Entry<Integer, Thread> entry : mSidPendingRequests.entrySet()) {
                        Integer rid = entry.getKey();
                        if (!mSidPendingOneWayRequests.contains(rid)) {
                            newReader = entry.getValue();
                            break;
                        }
                    }
                }
                // else take the event listener if it is ready
                if (newReader == null && mSidReadyEventThread) {
                    newReader = mSidEventThread; 
                    mSidReadyEventThread = false;
                }
            } else if (mSidReader == newReader) {
//System.out.println("### sidOnMessageDecoded, mSidReader == newReader ");
                return;
            }
//System.out.println("### sidOnMessageDecoded, elected "+newReader);
            mSidReader = newReader;
        }
        if (newReader != null) {
            synchronized (newReader) {
                newReader.notify();
            }
        }
    }

    boolean sidAddPendingGetResponse()  throws IOException {
        Thread current   = mSidReader;
        Thread requester = null;
        boolean got_my_response = false;
        synchronized (this) {
            if (mSidPendingGets.isEmpty()) {
                mSidPendingGetResponse = true;
            } else {
                requester = mSidPendingGets.pop();
                got_my_response = requester == current;
            }
//System.out.println("### sidAddPendingGetResponse(): curr"+current+" known poster, "+requester);
            if (!got_my_response) {
                mSidPrevReader = current; // tell the requester to wake us up after
                mSidReader = requester;
            }
        }
        if (!got_my_response) {
            if (requester != null) {
                // wake up the requester
                synchronized (requester) {
                    requester.notify();
                }
            }
            // wait that the requester wakes us up
            synchronized (current) {
                try {
                    while (mSidReader != current)
                        current.wait();
                } catch (InterruptedException ie) {
                    throw new IOException("Connection was closed");
                }
            }
        }
        return got_my_response;
    }

    boolean sidAddPendingResponse(int requestId)  throws IOException {
        Thread current   = mSidReader;
        Thread requester = null;
        Integer rid      = requestId;
        boolean oneway   = false;
        boolean got_my_response = false;
        synchronized (this) {
            requester = mSidPendingRequests.get(rid);
            if (requester == null) {
                // the requester hasn't yet waited for the response
                mSidPendingRequests.put(rid, current);
            } else {
                mSidPendingRequests.remove(rid);
                oneway = mSidPendingOneWayRequests.remove(rid);
            }
//System.out.println("### sidAddPendingResponse() "+requestId+": curr"+current+" known poster, "+requester+ " oneway="+oneway);
            got_my_response = requester == current;
            if (!got_my_response || oneway) {
                mSidPrevReader = current; // tell the requester to wake us up after
                if (!oneway)
                    mSidReader = requester;
            }
        }
        if (oneway) {
            // we have found that the requester wasn't waiting for the response, skip it ourselves
            try {
                mSidDecoder.skipMessage(); // will call OnMessageDecoded, but we want going on
            } catch (IOException e) {
                sidOnFatalError(e);
            }
            return false;
        }
        if (!got_my_response) {
            // tell the requester to read the response if we know who it is
            if (requester != null) {
                synchronized (requester) {
                    requester.notify();
                }
            } else {
            } 
            // wait that the requester resumes us
            synchronized (current) {
                try {
                    while (mSidReader != current)
                        current.wait();
                } catch (InterruptedException ie) {
                    throw new IOException("Connection was closed");
                }
            }
        }
        return got_my_response;
    }
    
    void sidWaitResponse() throws IOException {
        Decoding decoder = mSidDecoder;
        while (true) {
            int r;
            try {
                r = decoder.decodeResponse();
            } catch (IOException e) {
                 return;
            }
            switch (r) {
            case 'r':
                int rid;
                try {
                    rid = decoder.decodeUint();
                } catch (IOException e) {
                    return;
                }
                if (sidAddPendingResponse(rid)) return;
                break;
            case 'g':
                if (sidAddPendingGetResponse()) return;
                break;
            }
        }
    }

    public void registerConnectionListener(ConnectionListener listener) {
        mSidConnectionListener = listener;
    }
    public void unRegisterConnectionListener(ConnectionListener listener) {
        mSidConnectionListener = null;
    }
    public ConnectionListener getConnectionListener() {
        return mSidConnectionListener;
    }

    public SidObject flushCache(SidObject keep) {
        mSidTimestamp++;
        mSidObjects = new HashMap<Integer, SoftReference<SidObject>>(512);
        if (keep != null) {
            mSidObjects.put(keep.getOid(), new SoftReference<SidObject>(keep));
            keep.mSidTimestamp = mSidTimestamp;
        }
        return keep;
    }    

    protected int                                      mSidTimestamp;
    private ConnectionListener                         mSidConnectionListener;
    private Thread                                     mSidReader;        // reader thread
    private Thread                                     mSidPrevReader;    // previous reader that routed the response to the current reader, because it was not its expected response
    private Thread                                     mSidEventThread;
    private boolean                                    mSidReadyEventThread;
    private ReentrantLock                              mSidEncoderLock;
    private boolean                                    mSidEncoding;
    protected BinProtocolClientEncoder                 mSidEncoder;
    protected BinProtocolClientDecoder                 mSidDecoder;
    private HashMap<Integer, Thread>                   mSidPendingRequests;
    private HashSet<Integer>                           mSidPendingOneWayRequests;
    private ArrayDeque<Thread>                         mSidPendingGets;
    private boolean                                    mSidPendingGetResponse;
    private boolean                                    mSidDispatchAll;
    private boolean                                    mSidStopped;
    private HashMap<Integer, SoftReference<SidObject>> mSidObjects;
    private InputTransporting                          mSidInput;
    private OutputTransporting                         mSidOutput;
}

