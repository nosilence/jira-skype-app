/*! \file skt_videorenderer.cs
 *  \brief 
 *  Contains the <b><tt>SktVideoRenderer</tt></b> video renderer class (descended from <tt>PictureBox</tt>) and the entirety of 
 *  the client side of the shared memory based frame transport mechanism, in two classes: <b><tt>FrameTransport</tt></b> - 
 *  implements the frame transport IPC, including marshalling between .NET managed memory and shared memory. <b><tt>SharedMemoryChannel</tt></b> - 
 *  implements Windows based mapping mechanism to shared memory resources. <b>NB!</b> As this is making liberal use of 
 *  Win API functions imported from Kernel32.dll, it is somewhat unlikely to work too well with Mono.
 */

using System;
using System.IO;
using System.Drawing;
using System.Drawing.Imaging;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using SkypeKit;

namespace SkypeKit
{
    /*! \class SktVideoRenderer
     * \brief 
     * SkypeKit video rendering component. This is a visual control that can be added to your GUI. It's a GUI counterpart
     * of the SktVideo class. It needs an SktVideo object assigned to the VideoObject property in order to work.
     * 
     * When a SktVideo goes into RUNNING state and is associated with a renderer object, it starts sending decoded video 
     * frames to the renderer object, over shared memory. This class is the receiving end of this frame transport. At set 
     * intervals, the renderer polls the shared memory channel for new frames. When a frame is available, it will be fetched
     * into the managed memory space and rendered on screen. 
     * 
     * Default frame poll interval is 50 ms. This caps the renderer at 20 FPS - which should be plenty, as with even
     * local webcam video, the framerates tend to stay below that. You can play with this by modifying the FrameUpdateInterval
     * property. Note that setting the interval too low can cause loss of GUI responsiveness.
     * 
     * The proper use of the renderer is to always assign the SktVideoObject first, the start the video object, then the renderer.
     * Example:
     * @code
     * skype.GetPreviewVideo(out video, SktVideo.MEDIATYPE.MEDIA_VIDEO, device.name, device.path);
     * renderer.VideoObject = video;
     * video.Start();
     * renderer.Start();
     * @endcode
     * 
     * When the video rendering is no longer required, SktRenderer.Stop() must be called. Otherwise, the memory channels
     * used for the video object will not be released from the runtime side.
     * 
     * In addition to the mandatory SktVideo object, an optional SktParticipant object can be assigned to the ParticipantObject
     * property. As of now, the SktVideoRenderer class makes no use of this. It exists in this class purely for your convenience,
     * to make it less likely that you need to subclass your own SktVideoRenderer descendant for the sole purpose of adding
     * Participant reference to this class.
     * 
     * There is, however, another reason to have your own SktVideoRenderer descendant - the AddCustomGraphics method.
     * This is actually something of a rarity. It is an actual virtual method in the .NET wrapper that you can override.
     * 
     * The SktVideoRenderer.AddCustomGraphics is called durining the exciting time, right after a new video frame is 
     * fetched into the bitmap buffer, and right before that bitmap gets drawn in the GUI. By overriding this method you
     * will get your hands on the "pre-release" version of the latest video frame, and you can add your own graphical
     * elements by drawing them on the bitmap field. Or you can store those bitmaps and use them for making a video recorder.
     * 
     *@n <h2>Events</h2>
     *<tt>void <b>SktEvents.OnVideoResolutionChanged</b> (SktVideoRenderer sender, OnVideoResolutionChangedArgs e)</tt>@n
     * @param newWidth new width of the incoming video frames.
     * @param newHeight new height of the incoming video frames.
     */
    public class SktVideoRenderer : PictureBox
    {
        /** Shared memory transport channel ID. This is exposed for informational purposes only. */ 
        public uint Key { get { return key; } }

        /** Current height of video frames received from the runtime. */ 
        public int VideoHeight { get { return videoHeight; } }

        /** Current width of video frames received from the runtime. */ 
        public int VideoWidth { get { return videoWidth; } }

        /** Current frames per second, calculated by measuring interval between last two frames. */ 
        public double FPS { get { return fps; } }

        /** Frame transport poll interval, in milliseconds. This is how often the renderer goes checking if there is a new frame from the runtime. Defaults to 50ms. */ 
        public int FrameUpdateInterval { get { return GetTimerInterval(); } set { SetTimerInterval(value); } }
        
        /** Returns true if the rendering running. False otherwise. */
        public bool IsRunning { get { return isRunning; } set { SetRunning(value); } }

        /** Reference to associated SktVideo object. This must be set before rendering can start. */
        public SktVideo VideoObject { get { return videoObject; } set { SetVideoObject(value); } }

        /** Reference to associated SktParticipant object. This property is optional. */
        public SktParticipant ParticipantObject { get { return participantObject; } set { participantObject = value; } }

        protected DateTime          lastFrameTimestamp; /*!< Timestamp of when the last video bitmap was rendered */
        protected Rectangle         rect;               /*!< Used in MoveFrameToBitmap to lock bits in the bitmap */ 
        protected Timer             timer;              /*!< Frame poll timer */
        protected BitmapData        bitmapData;         /*!< Used in MoveFrameToBitmap as the target of moving in the frame from the shared memory */
        protected Bitmap            bitmap = null;      /*!< This is the bitmap that will be drawn on screen */

        private SktVideo            videoObject = null;
        private SktParticipant      participantObject = null;
        private bool                isRunning = false;
        private int                 updateInterval = 50; // default frame poll interval is 20 FPS
        private uint                key = 0;
        private int                 videoWidth = 0;
        private int                 videoHeight = 0;        
        private SktSkypeBase        skypeRef;
        private FrameTransport      frameTransport;
        private double              fps = 0;


        /** SktVideoRenderer constructor. The skype reference is used to fire the OnVideoResolutionChanged event. */
        public SktVideoRenderer(SktSkypeBase skype) 
        {
            this.skypeRef = skype;
        }

        /** Associates SktVideo object with the renderer. Throws exception if the renderer is already running.
         * This method is only usable from within descendants of the SktVideoRenderer class. Normally, you should
         * be assigning the SktVideo object directly to the SktVideoRenderer.VideoObject property.
         */
        protected void SetVideoObject(SktVideo video)
        {
            if (isRunning) throw new Exception("Error: cannot re-set video object while video is running."); ;
            videoObject = video;
        }

        /** Starts video rendering. 
         * <b>NB!</b> Calling SktVideo.SetRemoteRendererId method to associate the video object with renderer is <b>not neccessary</b>. 
         * SktVideoRenderer.Start does this for you. That part of the SktVideo documentation applies to the C++ wrapper (the docs
         * are generated from the same base source file, mostly).
         */
        public void Start()
        {
            if (isRunning) return;
            if (videoObject == null) throw new Exception("Error: cannot start rendering when the associated video object is null.");
            isRunning = true;
            
            frameTransport = new FrameTransport();

            timer = new Timer();
            timer.Interval = updateInterval;
            timer.Enabled = false;
            timer.Tick += TimerTick;

            Int32[] preferences = new Int32[1];
            preferences[0] = MakeFourcc('B', 'I', '2', '4');
            frameTransport.SetPreferences(1, preferences);
            key = frameTransport.Key();            
            videoObject.SetRemoteRendererId(key);
            lastFrameTimestamp = DateTime.Now;
            timer.Start();
        }

        /** Returns FOURCC pixel format as int. The SkypeKit runtime supports a list of up to 10 different pixel formats. 
         * In this renderer we are  only using one of them - 24 bit RGB - which in FOURCC is "BI24".
         * For more information: http://www.fourcc.org/ 
         */
        protected Int32 MakeFourcc(char ch0, char ch1, char ch2, char ch3)
        {
            return ((Int32)(byte)(ch0) | ((byte)(ch1) << 8) | ((byte)(ch2) << 16) | ((byte)(ch3) << 24));
        }

        /** Stops video rendering. 
         * <b>NB!</b> Calling SktVideo.SetRemoteRendererId(0) method to reset the frame transport is <b>not neccessary</b>. 
         * SktVideoRenderer.Stop does this for you. That part of the SktVideo documentation applies to the C++ wrapper (the docs
         * are generated from the same base source file, mostly).
         */
        public void Stop()

        {
            if (!isRunning) return;
            isRunning = false;
            timer.Stop();
            timer = null;
            videoObject.SetRemoteRendererId(0);
            frameTransport = null;
        }

        private void SetRunning (bool running)
        {
            if (running) Start();
            if (!running) Stop();
        }

        private void TimerTick(object sender, EventArgs e)
        {
            if (frameTransport.IsNewFrameAvailable())
            {
                bool frameOk = frameTransport.GetFrame();
                if (frameOk)
                {
                    bool bitmapOk = MoveFrameToBitmap();
                    if (bitmapOk)
                    {
                        AddCustomGraphics();
                        DrawBitmap();
                        double msSinceLastFrame = (Int32)DateTime.Now.Subtract(lastFrameTimestamp).TotalMilliseconds;
                        fps = 1000 / msSinceLastFrame;
                        lastFrameTimestamp = DateTime.Now;
                    }
                }
            }
        }

        private int GetTimerInterval()
        {
            return updateInterval;
        }

        private void SetTimerInterval(int newInterval)
        {
            updateInterval = newInterval;
            if (timer == null) return;
            bool wasEnabled = timer.Enabled;
            if (wasEnabled) timer.Stop();
            timer.Interval = newInterval;
            if (wasEnabled) timer.Start();
        }

        /** This marshals the current frame transport bitmap buffer from fixed memory into the local bitmap field.
         * If the bitmap is unassigned, or the video resolution changes, new bitmap object (and the bitmapData object)
         * need to be created.
         */
        internal bool MoveFrameToBitmap()
        {
            if (frameTransport.bitmapDataSize == 0) return false;

            bool ResolutionHasChanged = ((videoWidth != frameTransport.width) | (videoHeight != frameTransport.height));
            
            // No bitmap or resolution change -> need to reallocate bitmap and bitmapData
            if ((bitmap == null) | ResolutionHasChanged)
            {
                if (bitmap != null) bitmap.Dispose();
                videoHeight             = frameTransport.height;
                videoWidth              = frameTransport.width;
                bitmapData              = null;
                bitmap                  = new Bitmap(videoWidth, videoHeight);
                bitmapData              = new BitmapData();
                bitmapData.Width        = videoWidth;
                bitmapData.Height       = videoHeight;
                bitmapData.PixelFormat  = PixelFormat.Format24bppRgb;
                rect                    = new Rectangle(0, 0, videoWidth, videoHeight);
            }

            // Locking the bitmap and marshalling in data from the shared memory into the managed memory
            bitmap.LockBits(rect, ImageLockMode.ReadWrite, PixelFormat.Format24bppRgb, bitmapData);
            IntPtr ptr = bitmapData.Scan0;
            Marshal.Copy(frameTransport.bitmapData, 0, ptr, frameTransport.bitmapDataSize);
            bitmap.UnlockBits(bitmapData);
            if (ResolutionHasChanged) skypeRef.events.FireOnVideoResolutionChanged(this, 
                new SktEventsBase.OnVideoResolutionChangedArgs(videoWidth, videoHeight));
            return true;
        }

        /** This method gets executed right after a new video frame is fetched into the bitmap buffer and 
         * before it is drawin in the UI. By overriding this method, you can draw your own custom graphics
         * on the bitmap, before it gets drawn. Example:
         * @code
         *  public override void AddCustomGraphics()
         *  {
         *      var graphics = Graphics.FromImage(bitmap);               
         *      graphics.DrawString("This text will appear in the upper left corner of the video frame", 
         *          new Font("Tahoma", 20), Brushes.Yellow, 0, 0);
         *  }
         * @endcode
         * 
         *  <b>NB!</b> The bitmap is in the <b>video frame native resolution</b>. Different webcams can send out frames with 
         *  different resolutions, at different times. If you resize the frame - or just set <tt>SktRenderer.SizeMode 
         *  to PictureBoxSizeMode.StretchImage</tt>, the things you have drawn on the bitmap will be resized as well. 
         *  I.e. you cannot assume, that if you add a text with size 16 font to the bitmap, that the text will
         *  stay that size on all circumstances. If someone has an older webcam - with lower resolution, your text 
         *  will get scaled bigger. If someone has higher resolution webcam - your custom text eill get smaller.
         *  
         *  So, this way of adding custom graphics is only useful if you actually want to scale the additional 
         *  graphics. If you want your custom things to stay at constant and predictable size, you will just need
         *  to make them transparent and anchor them on top of the renderer control.
         */
        public virtual void AddCustomGraphics()
        {
            // abstract
        }

        /** Drawing the bitmap. Just one line. Took ca 600 other lines to get that bitmap tho.. */
        internal void DrawBitmap()
        {
            Image = bitmap;
        }


    } // SktVideoRenderer


    /****************************************************************************************************
     * Only internal classes below this line.                                                   
    *****************************************************************************************************/

    /*! \class SharedMemoryChannel
     * \brief 
     * Shared memory interface class for use by FrameTransport. The FrameTransport crates three shared 
     * memory channel per renderer:
     * @li control channel for buffer states
     * @li one channel each for both of the alternating frame buffers.
     */
    internal class SharedMemoryChannel
    {
        internal IntPtr data;    // This points to the start of our mapped space shared memory.
        internal IntPtr file;    // Shared memory file.
        internal int key = 0;    // Channel ID.
        internal string keyname; // Channel name, formed as String.Format("skypekit-surface-{0}", key)

        /* Don't panic. The actual bufsize is determined by pixel format and video resolution. */
        internal const uint bufsize = 27648001;

        public SharedMemoryChannel()
        {
            data = IntPtr.Zero;
            file = IntPtr.Zero;
        }

        ~SharedMemoryChannel()
        {
            UnMap();
        }

        /** Releases the mapping/channel in the shared memory */
        internal void UnMap()
        {
            if (data != IntPtr.Zero) Win32.UnmapViewOfFile(data);
            if (file != IntPtr.Zero) Win32.CloseHandle(file);
            data = IntPtr.Zero;
            file = IntPtr.Zero;
        }

        /** Creates a new mapping/channel in the shared memory */
        internal bool CreateMapping(uint size)
        {
            int error = 0;
            bool success = false;
            key = 0;
            while (!success)
            {
                key++;
                // Key value sanity check.
                if (key > 500) throw new Exception("Shared memory key overflow. The memory does not want to be shared.");
                keyname = String.Format("skypekit-surface-{0}", key);
                file = Win32.CreateFileMapping(0, IntPtr.Zero, Win32.PAGE_READWRITE, 0, size, keyname);
                error = Win32.GetLastError();
                if (error == Win32.ERROR_ALREADY_EXISTS)
                {
                    Win32.CloseHandle(file);
                    file = IntPtr.Zero;
                }
                success = (error == 0);
            }
            if (error != 0)
            {
                data = Win32.MapViewOfFile(file, Win32.FILE_MAP_ALL_ACCESS, 0, 0, 0);
                error = Win32.GetLastError();
                if (error != 0) throw new Exception("Error while creating mapping for video buffer");
            }
            return (error == 0);
        }

        /** Opens an already created shared memory mapping/channel for read-write. */
        internal bool OpenMapping(int mapKey)
        {
            key = mapKey;
            keyname = String.Format("skypekit-surface-{0}", key);
            file = Win32.OpenFileMapping(Win32.FILE_MAP_WRITE | Win32.FILE_MAP_READ, false, keyname);
            int error = Win32.GetLastError();
            if (file == IntPtr.Zero) throw new Exception("Error: OpenFileMapping returned empty file");
            data = Win32.MapViewOfFile(file, Win32.FILE_MAP_READ | Win32.FILE_MAP_WRITE, 0, 0, 0);
            error = Win32.GetLastError();
            return true;
        }

    } // SharedMemoryChannel        


    /*! \class FrameTransport
     * \brief 
     * Frame transport, used by the SktVideorenderer. This class maps three packed structs into shared memory.
     * ControlChannelStruct is the control struct that contains, amongst other things:
     * @li bufferstates - 3 lowest bits of which reflect what buffer (if any) has new data in it.
     * @li two frame buffers - buffer1 and buffer2. For both these buffers, a new memory channel will
     * be created - with channel ID set to buffer struct's bufid.
     * 
     * Once the memory from channel (matching the buffer's bufid) is fetched, the control channel struct
     * must be written back to the shared memory - to inform the other side that we are done with that
     * buffer.
     */
    internal class FrameTransport
    {
        internal enum BufferType
        {
            SysVBuffers,
            PosixBuffers,
            WinBuffers
        };

        /** Frame buffer struct. Two of these will be mapped to shared memory channels, with channel key set to bufid */
        [StructLayout(LayoutKind.Sequential, Pack = 1)]
        internal struct FrameChannelStruct
        {
            public Int32 bufid;
            public Int32 size;
            public Int32 width;
            public Int32 height;
            public Int32 bitsperpixel;
            public Int32 fourcc;
            public Int32 orientation;
            public Int64 clientpointer;
        };

        /** Control channel struct. Contains two frame buffer structs. And most importantly - the bufferstates. */
        [StructLayout(LayoutKind.Sequential, Pack = 1)]
        internal struct ControlChannelStruct
        {
            public Int32                bufferstates;
            public FrameChannelStruct   buffer1;
            public FrameChannelStruct   buffer2;
            public BufferType           buftype;
            public Int32                configuration;
            public Int32                fourcccount;
            public Int32                fourcc01;   // Easier this way than to mess with fixed array.
            public Int32                fourcc02;   // Really, it is.
            public Int32                fourcc03;
            public Int32                fourcc04;
            public Int32                fourcc05;
            public Int32                fourcc06;
            public Int32                fourcc07;
            public Int32                fourcc08;
            public Int32                fourcc09;
            public Int32                fourcc10;
        };

        internal SharedMemoryChannel controlChannel; // main shared memory channel, for controlStruct
        internal ControlChannelStruct controlStruct; // frame transport control data struct

        internal SharedMemoryChannel frameChannel1; // shared memory channel for controlStruct.buffer1
        internal SharedMemoryChannel frameChannel2; // shared memory channel for controlStruct.buffer2

        internal byte[] bitmapData;  // internal bitmap data buffer, in managed memory
        internal int bitmapDataSize; // current size of bitmap data buffer

        internal uint sharedInfoSize; // sizeof(ControlChannelStruct)
        internal bool haveFrame;

        internal int height;
        internal int width;

        public FrameTransport()
        {
            sharedInfoSize = (uint)Marshal.SizeOf((Type)typeof(ControlChannelStruct));

            haveFrame = false;

            bitmapData = null;
            bitmapDataSize = 0;

            frameChannel1 = null;
            frameChannel2 = null;

            controlStruct = new ControlChannelStruct();
            controlStruct.bufferstates = 1;
            controlStruct.buffer1.bufid = -1;
            controlStruct.buffer2.bufid = -1;
            controlStruct.buffer1.clientpointer = 1;
            controlStruct.buffer2.clientpointer = 2;
            controlStruct.fourcccount = 0;
            controlStruct.buftype = BufferType.WinBuffers;

            // Mapping the control channel
            controlChannel = new SharedMemoryChannel();
            bool success = controlChannel.CreateMapping(sharedInfoSize);
            if (!success) throw new Exception("Unable to create memory mapping for video frame transport");

            // Opening the control channel for read-write
            controlChannel.OpenMapping(controlChannel.key);
            int error = Win32.GetLastError();
            if (error != 0) throw new Exception("Unable to map memory for video frame transport");
        }

        /** As we are operating in managed memory space, the "mappings" to shared memory are not quite 
         * direct mappings. Each time we want to write something to shared memory, we will need to
         * explicitly send the struct from managed memory space via marshalling.
         */ 
        internal void SendControlData()
        {
            Marshal.StructureToPtr(controlStruct, controlChannel.data, false);
        }

        /** ..and each time we want to read, data, we must explicitly fetch the struct. */
        internal void GetControlData()
        {
            controlStruct.bufferstates = -1;
            controlStruct = (ControlChannelStruct)Marshal.PtrToStructure(controlChannel.data, typeof(ControlChannelStruct));
        }

        /** Communicates supported pixel formats to the runtime. */
        internal void SetPreferences(int count, Int32[] fourCcs)
        {
            if (fourCcs.Length != 1) throw new Exception("For now im assuming only one fourcc here. sorry.");
            controlStruct.fourcccount = count;
            controlStruct.fourcc01 = fourCcs[0];
            controlStruct.fourcc02 = 0;
            controlStruct.fourcc03 = 0;
            controlStruct.fourcc04 = 0;
            controlStruct.fourcc05 = 0;
            controlStruct.fourcc06 = 0;
            controlStruct.fourcc07 = 0;
            controlStruct.fourcc08 = 0;
            controlStruct.fourcc09 = 0;
            controlStruct.fourcc10 = 0;
            SendControlData();
        }

        internal int GetBufStates()
        {
            GetControlData();
            return controlStruct.bufferstates;
        }

        internal uint Key()
        {
            return (uint)controlChannel.key;
        }

        internal bool IsNewFrameAvailable()
        {
            GetControlData();
            int bufferState = (controlStruct.bufferstates & 0x3);
            if ((bufferState != 0x00) & (bufferState != 0x3)) return false;

            return true;
        }

        /** Now, this is a bit complicated. Once you understand the follwing code, you will 
         * understand the Tao of SkypeKit video transport.
         */
        internal bool GetFrame()
        {
            // At this point, we already have fresh control struct in our memory.
            // This is because we have just called IsNewFrameAvailable and that 
            // used GetControlData() to fetch in new data.

            // The following will deterime, which buffer is available for reading,
            // and write the new states back into the control struct. The struct
            // will not be sent back to the shared memory until the end of this method.

            int bufferState = (controlStruct.bufferstates & 0x03);
            if (!haveFrame & bufferState == 0x01) { return false; }
            if (!haveFrame) haveFrame = true;
            if (bufferState == 0x00) controlStruct.bufferstates |= 0x02;
            if (bufferState == 0x03) controlStruct.bufferstates &= ~0x02;
            bufferState = (controlStruct.bufferstates & 0x03);

            // Now we map ourselves to either buffer1 or buffer2
            FrameChannelStruct buffer;
            SharedMemoryChannel channel;
            if (bufferState == 0x1)
            {
                buffer = controlStruct.buffer1;
                channel = frameChannel1;
            }
            else if (bufferState == 0x2)
            {
                buffer = controlStruct.buffer2;
                channel = frameChannel2;
            }
            else throw new Exception("Error: unexpected video control buffer state");

            // If there was no frame buffer channel opened for this buffer, we open it.
            if (channel == null)
            {
                channel = new SharedMemoryChannel();
                bool success = channel.OpenMapping(buffer.bufid);
                if (!success) throw new Exception("Unable to map frame bitmap channel.");
            };

            // If there was an open channel, we check if the IDs match
            if (channel != null)
            {
                if (channel.key != buffer.bufid)
                {
                    return false;
                    //throw new Exception("Error: video control buffer ID and frame buffer ID mismatch");
                };
            };

            // Calculating the video frame size
            width = buffer.width;
            height = buffer.height;
            int bytesPerPixel = buffer.bitsperpixel >> 3;
            int newFrameSize = width * height * bytesPerPixel;

            // If it mismatches with our previous frame size, we need to reallocate buffers.
            if (newFrameSize != bitmapDataSize)
            {
                bitmapData = null;
                bitmapDataSize = newFrameSize;
                bitmapData = new byte[bitmapDataSize];
            }

            // And now we marshal the frame into our bitmapData
            Marshal.Copy(channel.data, bitmapData, 0, bitmapDataSize);

            if (bufferState == 0x1)
            {
                controlStruct.buffer1 = buffer;
                frameChannel1 = channel;
            }
            else if (bufferState == 0x2)
            {
                controlStruct.buffer2 = buffer;
                frameChannel2 = channel;
            }

            // And finally, sending the modified control struct back to the shared memory.
            // Remember - we modified the buffer states at the behinning of this method.
            // We now need to let the other side know that we have finished with current 
            // buffer and are basically ready for the next one.
            SendControlData();
            return true;
        }

    } // FrameTransport


    /*! \class Win32
     * \brief 
     * Separate static class to host the Windows API bits we need to import from Kernel32.dll,
     * so that they don't clutter the rest of our classes.
     */ 
    internal static class Win32
    {
        public const int ERROR_ALREADY_EXISTS   = 183;
        public const int ERROR_ACCESS_DENIED    = 5;

        public const Int32 SECTION_MAP_WRITE    = 0x0002;
        public const Int32 SECTION_MAP_READ     = 0x0004;
        public const Int32 SECTION_MAP_READ_WRITE = SECTION_MAP_WRITE | SECTION_MAP_READ;

        public const Int32 PAGE_READWRITE       = 0x0004;
        public const Int32 FILE_MAP_WRITE       = 0x0002;
        public const Int32 FILE_MAP_READ        = 0x0004;
        public const Int32 FILE_MAP_ALL_ACCESS  = 0x001F;
        public const Int32 FILE_MAP_READ_WRITE  = FILE_MAP_READ | FILE_MAP_WRITE;

        [DllImport("Kernel32.dll", EntryPoint = "CreateFileMapping", SetLastError = true, CharSet = CharSet.Unicode)]
        internal static extern IntPtr CreateFileMapping(
            uint hFile,
            IntPtr lpAttributes,
            uint flProtect,
            uint dwMaximumSizeHigh,
            uint dwMaximumSizeLow,
            string lpName);

        [DllImport("Kernel32", EntryPoint = "OpenFileMapping", CharSet = CharSet.Auto, SetLastError = true)]
        internal static extern IntPtr OpenFileMapping(
            int dwDesiredAccess,
            [MarshalAs(UnmanagedType.Bool)] bool bInheritHandle,
            string lpName);

        [DllImport("Kernel32.dll", EntryPoint = "MapViewOfFile", SetLastError = true, CharSet = CharSet.Unicode)]
        internal static extern IntPtr MapViewOfFile(
            IntPtr hFileMappingObject,
            uint dwDesiredAccess,
            uint dwFileOffsetHigh,
            uint dwFileOffsetLow,
            uint dwNumberOfBytesToMap);

        [DllImport("Kernel32.dll", EntryPoint = "UnmapViewOfFile", SetLastError = true, CharSet = CharSet.Unicode)]
        [return: MarshalAs(UnmanagedType.VariantBool)]
        internal static extern bool UnmapViewOfFile(IntPtr lpBaseAddress);

        [DllImport("kernel32.dll", EntryPoint = "GetLastError", CharSet = CharSet.Auto)]
        internal static extern int GetLastError();

        [DllImport("kernel32", EntryPoint = "CloseHandle", CharSet = CharSet.Auto, SetLastError = true, ExactSpelling = true)]
        internal static extern int CloseHandle(IntPtr hObject);

    } // Win32
}
