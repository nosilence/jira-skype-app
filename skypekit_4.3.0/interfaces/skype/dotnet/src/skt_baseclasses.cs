/*! \file skt_baseclasses.cs
 *  \brief 
 *  Contains abstract parent classes for all the classes in the <b><tt>skt_skypekit.cs</tt></b> The <b><tt>SktObject</tt></b> is the common ancestor
 *  to all the SkypeKit classes - for the purpose of being able to typecast all the objects to the common root.
 *  <b><tt>SktSkypeBase</tt></b> is the special ancestor of the <b><tt>SktSkype</tt></b> class - containing everything that did not make sense to put
 *  in the script-generated skt_skypekit.cs Similarly, the <b><tt>SktEventsBase</tt></b> class implements everything for events, that
 *  is specific to .NET wrapper, rather than abstract SkypeKit API.
 */

using System;
using System.IO;
using System.Net;
using System.Threading;
using System.Collections;
using System.Collections.Generic;
using System.Windows.Forms;
using System.Diagnostics;
using System.Security.Cryptography.X509Certificates;


namespace SkypeKit
{
    /*! \class SktObject 
     * \brief 
     * The "root" class of other wrapper classes. Internal. No user-servicable parts inside.
     */
    public abstract class SktObject
    {
        protected internal uint OID; /*!<  Object ID. Internal use only. */

        protected internal SktSkype skypeRef;   /*!< Back-reference to main skype object. Internal use only. */
        protected internal BitArray cache;      /*!< Zero-based BitArray of object's property cache indicators. Each bit indicates if corresponding property is already in cache. */

        /** Returns class ID of the object. */ 
        public abstract uint ClassId { get; }

        /** Returns class name of the object. */ 
        public abstract String ClassName { get; }

        /** Returns true if the object's property at propIndex is cached.
         * Note that the cache index does not correspond to property key.
         */ 
        protected Boolean isCached (int propIndex) { return cache[propIndex]; }

        /** Abstract property update dispatcher. This non-abstract versions get fired when the socket gets a property
         * update event for an object. This object's method is then called, with property ID and the new value.
         * This method will switch to the right prop, assign the new value and fire a property-specific event 
         * for the UI. The actual working version of this method is in the SktSkype class.
         */
        internal abstract void DispatchPropertyUpdate(uint propId, object value, bool hasValue);

        /** Abstract event dispatcher. This gets fired when the socket receives an event for an object.
         * This objects event dispatcher is then called, with event ID. Based on event ID, appropriate
         * data reader function is called, to read data out of the socket. After that, an event for the UI is fired.
         * Note that the data reader functions and UI event related things are all in the SktEvents class.
         * The actual working version is in the SktSkype class.
         */
        internal abstract void DispatchEvent(uint eventId);

        /** If the property cache field of an object is another object, this method will return the 
         * Class ID of that object. This works even if that object is not yet cached.
         */
        internal abstract uint MapPropIdToClassId(uint propId);

        /** Skt base object constructor */
        public SktObject(uint objectId) 
        { 
            OID = objectId; 
        }

        /** Skt base object constructor with a reference to the skype object */
        public SktObject(uint objectId, SktSkype Skype)
        {
            OID = objectId;
            skypeRef = Skype;
        }

    }; // SktObject 


    /** Delegate type for methods that create and return SktObject-based objects.
     * This is used for providing a mechanism to subclass from default SkypeKit classes,
     * i.e. writing a MyMessage class, based on SktMessage. 
     */
    public delegate SktObject SktNewObjectDelegate (uint objectId, SktSkype skype);


    /*! \class SktObjectList
     *  \brief 
     *  Abstract base class for all the Skt<Class>List classes
     */
    public abstract class SktObjectList : List<SktObject> { /* Abstract */ }


    /*! \class SktSkypeBase 
     *  \brief 
     *  The internal intermediate class between SktObject and SktSkype. 
     */
    public abstract class SktSkypeBase : SktObject
    {
        private uint RID;   // global request ID
        private int port;   // TCP port over which we talk with runtime

        internal SktTransport transport;                    // does socket communcication (skt_transport.cs)
        internal Dictionary<uint, SktObject> objectCache;   // keeps track of locally cached SkypeKit objects
        internal SktEncoder encoder;                        // does protocol serialisation of data types (skt_codec.cs)
        internal SktDecoder decoder;                        // does protocol de-serialisation of data types (skt_codec.cs)
        public SktEvents events;                            // hosts all the events related things

        /** This turns on wrapper debug. In debug mode, all errors will be thrown as exceptions. 
         *  Without debug mode, errors are written into log instead. For development and QA - debug mode 
         *  is <b>highly recommended</b>. Otherwise many of the error conditions will be hidden from the 
         *  application layer by the wrapper and many of the bugs will be left undiscovered. Some of those 
         *  will come to haunt you later. In production versions, the recommended setting is to turn this 
         *  off - as it makes the code more resilient, handling some of the problematic cases internally,
         *  without causing protocol de-synchronization. */
        public bool debugMode = true; 
        internal bool logging = false;                      // do we do wrapper logging?
        internal bool transportLogging = false;             // do we do transport logging?


        /** Skype class base constructor. Default IPC port is 8963. */
        public SktSkypeBase(X509Certificate2 cert, bool wrapperLog, bool transportLog, int port) : base(0)
        {
            skypeRef = (SktSkype)this;
            RID = 1;
            this.port = port;

            logging = wrapperLog;
            transportLogging = transportLog;

            objectCache = new Dictionary<uint, SktObject>();
            transport = new SktTransport(this, cert);
            encoder = new SktEncoder(this);
            decoder = new SktDecoder(this);
        }

        /** Establishes connection to the SkypeKit runtime. This should be called after the skype object has been constructed
         * and setting up custom event callbacks and subclass registration is complete. I.e. normally somewhere in the
         * tail section of your main form constructor.
         * 
         * After an attempt to handshake with runtime is made, the skype.events.OnConnect event will fire, with <tt>success</tt>
         * in the argument field indicating whether the handshake was successful. If it was, it is then safe to retrieve an 
         * SktAccount object and log in.
         * 
         * If the connection to the runtime goes down at some point later, the skype.events.OnDisconnect will fire.
         * This typically indicates a runtime crash. From the wrapper perspective, this is an unrecoverable error.
         * At this point, you would need to do a complete reload - force-close the runtime if it's not dead already,
         * construct a new SktSkype object (the old one becomes unusable), re-launch the runtime and re-connect.
         */
        public void Connect()
        {
            transport.Connect(IPAddress.Parse("127.0.0.1"), port);
        }

        /** Disconnects your client from the SkypeKit runtime. This method should be called either on your client shutdown 
         * or when your client no longer intends to use Skype. Failure to call this method can cause orphaned SkypeKit
         * runtimes being left running in the system.
         */
        public void Disconnect()
        {
            transport.StopSocketReader();
        }

        /** Adds a line to the wrapper log. This only works when wrapper logging is enabled. To enable wrapper logging, you 
         * will need to set the logging argument true in the SktSkype constructor. At that point, you can either get the log
         * by assigning your own callback to the skype.events.OnLog event, or if no callback is assigned, the log will go
         * into a text file (wrapper.log) in the same directory as your client executable. The wrapper log is mostly used
         * by internal functions of the library. However, you can use this method to inject your own custom markers into the log.
         * Good luck with your debugging efforts.
         */
        public void Log(String msg)
        {
            events.FireOnLog(this, new SktEventsBase.OnLogArgs(msg));
        }
        
        public override uint ClassId { get { return 0; } }

        /** Increments and returns the next class method request ID.
         * Request ID's are used to match method requests (sent to the runtime)
         * and method replies (received from the runtime, asynchronously).
         */
        internal uint GetNextRequestID()
        {
            RID++;
            return RID;
        }

        /** Abstract proprty subscription string method. The actual working version is in the SktSkype class. */
        public abstract string GetPropSubscriptionString();

        /** Abstract wrapper version property. The actual working version is in the SktSkype class. */
        public abstract String WrapperVersion { get; }

        /** Abstract supported runtime version property. The actual working version is in the SktSkype class. */
        public abstract String SupportedRuntimeVersion { get; } 

        /** Abstract SktObject creator. The actual working version is in the SktSkype class. */
        internal abstract SktObject CreateObject(uint classId, uint objectId);

        /** Abstract SktObjectList creator. The actual working version is in the SktSkype class. */
        internal abstract SktObjectList CreateObjectList(uint classId);

        /** Abstract runtime version check. The actual working version is in the SktSkype class. 
         * Returns false if version number reported by the runtime on connect and the wrapper
         * SupportedRuntimeVersion property fail to match */
        public abstract Boolean CheckVersion();

        /** This function provides access to the SkypeKit objects, by objectId.
         * It also maintains the wrapper's object cache. If an object is requested, it is first searched 
         * for in the objectCache list, and if none match, a new object is created (this is why the classId is needed).
         * The new object is then added to the factory.
         */
        internal SktObject GetObject(uint classId, uint objectId)
        {
            SktObject value;
            if (classId == 0) return this;
            if (!objectCache.TryGetValue(objectId, out value))
            {
                value = CreateObject(classId, objectId);
                objectCache.Add(objectId, value);
                if (logging) { Log(value.ClassName + " object created, ID = " + objectId + " total objects " + objectCache.Count.ToString()); }
            };
            return value;
        }

        /** Kills a runtime process. Used by LaunchRuntime method.
         */
        private void KillExistingRuntimeInstance(String command)
        {
            string processName = System.IO.Path.GetFileNameWithoutExtension(command);
            Process[] processlist = Process.GetProcesses();

            foreach (Process proc in processlist)
            {
                if (proc.ProcessName == processName)
                {
                    proc.Kill();
                    proc.WaitForExit();
                    break;
                }
            }
        }

        /** Launches runtime process. Used by LaunchRuntime method.
         */
        private void ExecuteCommandSync(object command)
        {
            try
            {
                string[] args = (string[])command;
                System.Diagnostics.ProcessStartInfo procStartInfo = new System.Diagnostics.ProcessStartInfo(args[0], args[1]);

                procStartInfo.RedirectStandardOutput = false;
                procStartInfo.UseShellExecute = false;
                procStartInfo.CreateNoWindow = true;
                System.Diagnostics.Process proc = new System.Diagnostics.Process();
                proc.StartInfo = procStartInfo;
                proc.Start();
            }
            catch (Exception objException)
            {
                Log(objException.Message);
            }
        }

        /** Launches SkypeKit runtime in a parallel process. The runtime will be listening on local TCP port that was given
         *  in the SktSkype constructor. Use SktSkype.Connect method to establish connection. 
         *  @param [in] runtimeWithPath SkypeKit runtime filename with path, e.g. "..\\..\\bin\\windows-x86-skypekit.exe"
         *  @param [in] killExistingRuntimeProcess If true, this will scan the process list for existing instances of the
         *  SkypeKit runtime and terminate them. <b>NB!</b> Use this with caution and only during debugging your client.
         * The purpose of this switch is to auto-terminate orphaned SkypeKit runtimes. Orphaned runtime processes can happen 
         * when your client crashes without properly shutting down the socket connection. Never leave this on in published 
         * products as there can be other legitimate SkypeKit runtimes present in the user's environment.
         * @param [in] runtimeParams Additional command line parameters to the runtime. For list of runtime paramaters, see https://developer.skype.com/skypekit/development-guide/skype-kit-runtime-versions
         * <b>NB!</b> Note that the -p parameter (port number) is already added to the parameter list, with the value taken from the SktSkype constructor. 
         * Therefore you dont need to add the -p parameter yourself, to make it match with the wrapper side of the IPC socket.
         */
        public void LaunchRuntime(string runtimeWithPath, Boolean killExistingRuntimeProcess, string runtimeParams)
        {
            if (!System.IO.File.Exists(runtimeWithPath)) throw new Exception("Invalid runtime filename or path: " + runtimeWithPath);
            if (killExistingRuntimeProcess) KillExistingRuntimeInstance(runtimeWithPath);
            try
            {
                Thread objThread = new Thread(new ParameterizedThreadStart(ExecuteCommandSync));
                objThread.IsBackground = true;
                objThread.Priority = ThreadPriority.Normal;

                string[] args = new string[2];
                args[0] = runtimeWithPath;
                args[1] = "-p " + port.ToString();
                if (runtimeParams != "") args[1] = args[1] + " " + runtimeParams;                
                // You can add " -r runtime_transport.log" to cause the runtime to generate it's side of the transport log

                objThread.Start(args);
            }
            catch (ThreadStartException objException) { Log(objException.Message); }
            catch (ThreadAbortException objException) { Log(objException.Message); }
            catch (Exception objException) { Log(objException.Message); }
        }

        /** Launches SkypeKit runtime in a parallel process. The runtime will be listening on local TCP port that was given
         *  in the SktSkype constructor. Use SktSkype.Connect method to establish connection. 
         *  @param [in] runtimeWithPath SkypeKit runtime filename with path, e.g. "..\\..\\bin\\windows-x86-skypekit.exe"
         *  @param [in] killExistingRuntimeProcess If true, this will scan the process list for existing instances of the
         *  SkypeKit runtime and terminate them. <b>NB!</b> Use this with caution and only during debugging your client.
         * The purpose of this switch is to auto-terminate orphaned SkypeKit runtimes. Orphaned runtime processes can happen 
         * when your client crashes without properly shutting down the socket connection. Never leave this on in published 
         * products as there can be other legitimate SkypeKit runtimes present in the user's environment.
         */
        public void LaunchRuntime(string runtimeWithPath, Boolean killExistingRuntimeProcess)
        {
            LaunchRuntime(runtimeWithPath, killExistingRuntimeProcess, "");
        }

        /** SkypeKit internal time format is Unix timestamps (seconds since 1970.01.01) 
         * This method converts .NET DateTime objects to Unix timestamps.
         */
        private DateTime epoch = new DateTime(1970, 1, 1);

        public uint DateTimeToUnixTimestamp(DateTime time)
        {
            if (time <= epoch) return 0;
            return (uint)(time - epoch).TotalSeconds;
        }

        /** SkypeKit internal time format is Unix timestamps (seconds since 1970.01.01) 
         * This method converts Unix timestamps tp .NET DateTime object.
         */
        public DateTime UnixTimestampToDateTime(uint unixTimeStamp)
        {
            return epoch.AddSeconds(unixTimeStamp).ToLocalTime();
        }

        internal void Error(string msg)
        {
            if (debugMode)
            {
                throw new Exception(msg);
            }
            else
            {
                if (logging) Log("ERROR: " + msg);
            }
        }

    } // SktSkypeBase


    /*! \class SktEventsBase
     *  \brief 
     *  Abstract base class for SktEvents. Implements basic OnConnection and OnLog events and default callbacks.
     */
    public abstract class SktEventsBase
    {
        public SktSkype     skypeRef;  /*!< Back-reference to the skype object */  
        public Form         gui;       /*!< If present, all the events will be executed with BeginInvoke in the GUI thread. If not, in individual threads. */


        internal bool           logToFile = false;  /*!< skype was launched with wrapper log enabled, but no callback was assigned to OnLog */
        internal object         wrapperLogFileLock; /*!< Lock for the file object as multiple threads can write into it. */
        internal StreamWriter   wrapperLogFile;     /*!< Logfile stream */
        internal String         wrapperLogFileName = "wrapper.log";     /*!< Logfile name. Cannot be changed. */
        internal DateTime       lastLogEntryTimepstamp = DateTime.Now;  /*!<  Used for measuring intervals between log entries. */ 

        public SktEventsBase(Form form, SktSkype skype)
        {
            this.skypeRef = skype;
            this.gui = form;
        }

        /** This is used to launch events and property updates in console mode. 
         * @param [in] sender - object for which the event occured.
         * @param [in] func - event callback.
         */
        internal void FireCallbackInSeparateThread(object sender, ParameterizedThreadStart func)
        {
            Thread thread = new Thread(func);
            thread.IsBackground = true;
            thread.Start(sender);
        }

        /** Fired when connection between runtime and the wrapper gets established. 
         * This is a good place to detect, when it's safe to start login proccess.
         * This event is also used internally, for starting the socket reader thread
         * and checking for runtime version compatibility (in OnConnectInternalCallback).
         */
        public event OnConnectHandler OnConnect;
        public delegate void OnConnectHandler(object sender, OnConnectArgs e);

        public class OnConnectArgs : EventArgs
        {
            public Boolean success;
            public String handshakeResult;

            public OnConnectArgs() { }

            public OnConnectArgs(Boolean success, string handshakeResult)
            {
                this.success = success;
                this.handshakeResult = handshakeResult;
            }
        }

        internal void FireOnConnect(object sender, OnConnectArgs e)
        {
            if (OnConnect == null) return;
            if (gui == null) { OnConnect(sender, e); return; };
            gui.Invoke(OnConnect, new object[] { sender, e } );
        }

        internal void OnConnectInternalCallback(object sender, OnConnectArgs e)
        {
            if (e.success)
            {
                if ((skypeRef.events.OnLog == null) & (skypeRef.logging)) DirectWrapperLogToFile();
                if (skypeRef.logging) skypeRef.Log("Starting socket reader thread.");
                skypeRef.transport.StartSocketReader();
                skypeRef.Start();
                Thread.Sleep(200);
                skypeRef.CheckVersion();
            }
            else
            {
                throw new Exception("IPC handshake with runtime failed with error code " + e.handshakeResult);
            }
        }

        /** Fired when the socket connection to the runtime goes down unexpectedly - i.e. when either a socket read or write failed.  */
        public event OnDisconnectHandler OnDisconnect;
        public delegate void OnDisconnectHandler(SktSkype sender, EventArgs e);

        public void FireOnDisconnect(object sender, EventArgs e)
        {
            if (OnDisconnect == null) return;
            if ((gui == null)) { OnDisconnect((SktSkype)sender, e); return; };
            gui.BeginInvoke(OnDisconnect, new object[] { sender, e });
        }

        /** Fired when a line gets appended to the wrapper log (SktSkype.Log gets called).
         * If no callback is assigned to this, default callback (OnLogToFile) will be used.
         */
        public event OnLogHandler OnLog;
        public delegate void OnLogHandler(object sender, OnLogArgs e);

        public class OnLogArgs : EventArgs
        {
            public String message;
            public OnLogArgs() { }
            public OnLogArgs(string message) { this.message = message; }
        }

        public void FireOnLog(object sender, OnLogArgs e)
        {
            if (OnLog == null) return;
            if ((gui == null) | (logToFile)) { OnLog(sender, e); return; };
            gui.BeginInvoke(OnLog, new object[] { sender, e });
        }

        internal void DirectWrapperLogToFile()
        {
            wrapperLogFileLock = new object();
            wrapperLogFile = new StreamWriter(wrapperLogFileName);
            wrapperLogFile.AutoFlush = true;
            OnLog = OnLogToFile;
            logToFile = true;
        }

        internal void OnLogToFile(object sender, OnLogArgs e)
        {
            lock (wrapperLogFileLock)
            {
                TimeSpan diff = DateTime.Now.Subtract(lastLogEntryTimepstamp);
                lastLogEntryTimepstamp = DateTime.Now;
                wrapperLogFile.WriteLine(diff.TotalMilliseconds.ToString("00000") + "  " + e.message);
            }
        }

        /** Fired when a SktVideoRenderer detects resolution change in incoming video stream. */
        public event OnVideoResolutionChangedHandler OnVideoResolutionChanged;
        public delegate void OnVideoResolutionChangedHandler(SktVideoRenderer sender, OnVideoResolutionChangedArgs e);

        public class OnVideoResolutionChangedArgs : EventArgs
        {
            public int newWidth;
            public int newHeight;
            public OnVideoResolutionChangedArgs() { }
            public OnVideoResolutionChangedArgs(int width, int height) 
            { 
                newWidth = width;
                newHeight = height;
            }
        }

        public void FireOnVideoResolutionChanged(object sender, OnVideoResolutionChangedArgs e)
        {
            if (OnVideoResolutionChanged == null) return;
            if ((gui == null)) { OnVideoResolutionChanged((SktVideoRenderer)sender, e); return; };
            gui.BeginInvoke(OnVideoResolutionChanged, new object[] { sender, e });
        }

    } // SktEventsBase
} // SkypeKit
