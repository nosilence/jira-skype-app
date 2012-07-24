package com.skype.api;

import com.skype.ipc.SidRoot;
import com.skype.ipc.SidObject;
import com.skype.ipc.EnumConverting;
import com.skype.ipc.PropertyEnumConverting;
import com.skype.ipc.Decoding;
import com.skype.ipc.Encoding;
import com.skype.ipc.Encoding;
import java.io.IOException;
import com.skype.ipc.PropertyEnumConverting;
import com.skype.ipc.SidGetResponding;

/**
 * This class contains basic video control functionality for live conversations with video. Basically, Video objects represent specific Participant's video state in a live Conversation. The Video class can represent both local (outgoing) and remote (incoming) video streams. Note that as of SkypeKit SDK version 3.2, this class no longer handles video rendering in the UI. Currently available SkypeKit runtimes do not support multi-party video. The API however is designed with future compatibility in mind, so the Video class is attached to Participant class rather than Conversation class. Once multi-party video will become available for SkypeKit, the logic should go like this: 
 * 
 * Let there be 4-way live conversation C and participants P1, P2, P3 and P4. P1 is the local user. Remote participants P2 and P3 are capable of sending video. Remote user P4 is not capable of sending video. You would then end up with 4 video objects: V1, V2, V3 and V0. 
 * 
 *  - C->P1->V1-> outgoing video stream 
 *  - C->P2->V2-> incoming video stream 1 
 *  - C->P3->V3-> incoming video stream 2 
 *  - C->P4-> no video object as participant P4 does not advertise supporting video 
 *  - V0-> local webcam preview - this is not attached to any particular conversation, however the corresponding video object can be retrieved with Skype.GetPreviewVideo method. 
 * 
 * As getting from a live conversation to running video streams involves three classes, it can be somewhat less than obvious. The basic state transition logic goes like this: 
 * 
 * You start out with a Conversation, that suddenly becomes live 
 * 
 * CONVERSATION.LOCAL_LIVESTATUS = IM_LIVE 
 * At this point you have access to participant list of that conversation. The next step will be to catch Partcipant property changes for PARTICIPANT.VIDEO_STATUS to see if any of the people in conversation have Video available. Note that you should not make assumptions on when this availability happens. Remote users may switch their video on-off as they please. 
 * 
 * PARTICIPANT.VIDEO_STATUS = VIDEO_AVAILABLE 
 * If you get to VIDEO_AVAILABLE (not necessarily for all Participants), you can retrieve Video object, with Participant.GetVideo method. 
 * 
 * Now you will need to handle Video.STATUS property changes. In case of successful video call, the sequence of Video.STATUS and Participant.VIDEO_STATUS changes for each Participant and Video object should look like this: 
 * 
 *  - Video.STATUS = AVAILABLE 
 *  - Video.STATUS = STARTING 
 *  - Video.STATUS = CHECKING_SUBSCRIPTION 
 *  - Video.STATUS = STARTING 
 * 
 * Participant.VIDEO_STATUS = VIDEO_CONNECTING 
 *  - Video.STATUS = RUNNING 
 *  - Participant.VIDEO_STATUS = STREAMING 
 * Both Video.STATUS == RUNNING and Participant.VIDEO_STATUS == STREAMING are indicative that the video for that particular participant is up and running, and your UI should update itself accordingly. 
 * 
 * NB! Note that it is not enough to check for Video.STATUS property updates. By the time you obtain the Video object in your client, it may already it may already have progressed to a further status. You should always check the status property immediately after obtaining the Video object. 
 */
public final class Video extends SidObject {
	public enum Status implements EnumConverting {
		NOT_AVAILABLE             (0),
		AVAILABLE                 (1),
		STARTING                  (2),
		REJECTED                  (3),
		RUNNING                   (4),
		STOPPING                  (5),
		PAUSED                    (6),
		NOT_STARTED               (7),
		HINT_IS_VIDEOCALL_RECEIVED(8),
		UNKNOWN                   (9),
		RENDERING                 (10),
		CHECKING_SUBSCRIPTION     (11),
		SWITCHING_DEVICE          (12);
		private final int key;
		Status(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return NOT_AVAILABLE; }
		public EnumConverting convert(int from) { return Status.get(from); }
		public EnumConverting[] getArray(final int size) { return new Status[size]; }
		public static Status get(int from) {
			switch (from) {
			case  0: return NOT_AVAILABLE;
			case  1: return AVAILABLE;
			case  2: return STARTING;
			case  3: return REJECTED;
			case  4: return RUNNING;
			case  5: return STOPPING;
			case  6: return PAUSED;
			case  7: return NOT_STARTED;
			case  8: return HINT_IS_VIDEOCALL_RECEIVED;
			case  9: return UNKNOWN;
			case 10: return RENDERING;
			case 11: return CHECKING_SUBSCRIPTION;
			case 12: return SWITCHING_DEVICE;
			}
			return NOT_AVAILABLE;
		}
		public static final int NOT_AVAILABLE_VALUE              =  0;
		public static final int AVAILABLE_VALUE                  =  1;
		public static final int STARTING_VALUE                   =  2;
		public static final int REJECTED_VALUE                   =  3;
		public static final int RUNNING_VALUE                    =  4;
		public static final int STOPPING_VALUE                   =  5;
		public static final int PAUSED_VALUE                     =  6;
		public static final int NOT_STARTED_VALUE                =  7;
		public static final int HINT_IS_VIDEOCALL_RECEIVED_VALUE =  8;
		public static final int UNKNOWN_VALUE                    =  9;
		public static final int RENDERING_VALUE                  = 10;
		public static final int CHECKING_SUBSCRIPTION_VALUE      = 11;
		public static final int SWITCHING_DEVICE_VALUE           = 12;
	}
	public enum MediaType implements EnumConverting {
		MEDIA_SCREENSHARING(1),
		MEDIA_VIDEO        (0);
		private final int key;
		MediaType(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return MEDIA_SCREENSHARING; }
		public EnumConverting convert(int from) { return MediaType.get(from); }
		public EnumConverting[] getArray(final int size) { return new MediaType[size]; }
		public static MediaType get(int from) {
			switch (from) {
			case 1: return MEDIA_SCREENSHARING;
			case 0: return MEDIA_VIDEO;
			}
			return MEDIA_SCREENSHARING;
		}
		public static final int MEDIA_SCREENSHARING_VALUE = 1;
		public static final int MEDIA_VIDEO_VALUE         = 0;
	}
	public enum VideoDeviceCapability implements EnumConverting {
		VIDEOCAP_HQ_CAPABLE      (0),
		VIDEOCAP_HQ_CERTIFIED    (1),
		VIDEOCAP_REQ_DRIVERUPDATE(2),
		VIDEOCAP_USB_HIGHSPEED   (3);
		private final int key;
		VideoDeviceCapability(int key) {
			this.key = key;
		};
		public int getId()   { return key; }
		public EnumConverting getDefault() { return VIDEOCAP_HQ_CAPABLE; }
		public EnumConverting convert(int from) { return VideoDeviceCapability.get(from); }
		public EnumConverting[] getArray(final int size) { return new VideoDeviceCapability[size]; }
		public static VideoDeviceCapability get(int from) {
			switch (from) {
			case 0: return VIDEOCAP_HQ_CAPABLE;
			case 1: return VIDEOCAP_HQ_CERTIFIED;
			case 2: return VIDEOCAP_REQ_DRIVERUPDATE;
			case 3: return VIDEOCAP_USB_HIGHSPEED;
			}
			return VIDEOCAP_HQ_CAPABLE;
		}
		public static final int VIDEOCAP_HQ_CAPABLE_VALUE       = 0;
		public static final int VIDEOCAP_HQ_CERTIFIED_VALUE     = 1;
		public static final int VIDEOCAP_REQ_DRIVERUPDATE_VALUE = 2;
		public static final int VIDEOCAP_USB_HIGHSPEED_VALUE    = 3;
	}
	private final static byte[] P_STATUS_req = {(byte) 90,(byte) 71,(byte) 130,(byte) 1,(byte) 93,(byte) 11};
	private final static byte[] P_ERROR_req = {(byte) 90,(byte) 71,(byte) 131,(byte) 1,(byte) 93,(byte) 11};
	private final static byte[] P_DEBUG_INFO_req = {(byte) 90,(byte) 71,(byte) 132,(byte) 1,(byte) 93,(byte) 11};
	private final static byte[] P_DIMENSIONS_req = {(byte) 90,(byte) 71,(byte) 133,(byte) 1,(byte) 93,(byte) 11};
	private final static byte[] P_MEDIA_TYPE_req = {(byte) 90,(byte) 71,(byte) 134,(byte) 1,(byte) 93,(byte) 11};
	private final static byte[] P_CONVO_ID_req = {(byte) 90,(byte) 71,(byte) 208,(byte) 8,(byte) 93,(byte) 11};
	private final static byte[] P_DEVICE_PATH_req = {(byte) 90,(byte) 71,(byte) 209,(byte) 8,(byte) 93,(byte) 11};
	/** Properties of the Video class */
	public enum Property implements PropertyEnumConverting {
		P_UNKNOWN    (0,0,null,0,null),
		P_STATUS     (130, 1, P_STATUS_req, 0, Status.get(0)),		P_ERROR      (131, 2, P_ERROR_req, 0, null),
		P_DEBUG_INFO (132, 3, P_DEBUG_INFO_req, 0, null),
		P_DIMENSIONS (133, 4, P_DIMENSIONS_req, 0, null),
		P_MEDIA_TYPE (134, 5, P_MEDIA_TYPE_req, 0, MediaType.get(0)),
		P_CONVO_ID   (1104, 6, P_CONVO_ID_req, 18, null),
		P_DEVICE_PATH(1105, 7, P_DEVICE_PATH_req, 0, null);
		private final int    key;
		private final int    idx;
		private final byte[] req;
		private final int    mod;
		private final EnumConverting enumConverter;
		Property(int key, int idx, byte[] req, int mod, EnumConverting converter) {
			this.key = key;
			this.idx = idx;
			this.req = req;
			this.mod = mod;
			this.enumConverter = converter;
		};
		public boolean  isCached()    { return idx > 0;   }
		public int      getIdx()      { return idx;       }
		public int      getId()       { return key;       }
		public byte[]   getRequest()  { return req;       }
		public EnumConverting getDefault()  { return P_UNKNOWN; }
		public int      getModuleId() { return mod;       }
		public EnumConverting getEnumConverter()    { return enumConverter;   }
		public EnumConverting convert(final int from) { return Property.get(from); }
		public EnumConverting[] getArray(final int size) { return new Property[size]; }
		public static Property get(final int from) {
			switch (from) {
			case  130: return P_STATUS;
			case  131: return P_ERROR;
			case  132: return P_DEBUG_INFO;
			case  133: return P_DIMENSIONS;
			case  134: return P_MEDIA_TYPE;
			case 1104: return P_CONVO_ID;
			case 1105: return P_DEVICE_PATH;
			}
			return P_UNKNOWN;
		}
		public static final int P_STATUS_VALUE      =  130;
		public static final int P_ERROR_VALUE       =  131;
		public static final int P_DEBUG_INFO_VALUE  =  132;
		public static final int P_DIMENSIONS_VALUE  =  133;
		public static final int P_MEDIA_TYPE_VALUE  =  134;
		public static final int P_CONVO_ID_VALUE    = 1104;
		public static final int P_DEVICE_PATH_VALUE = 1105;
	}
	/** Setupkey SETUPKEY_VIDEO_DEVICE type:string  <br>Selected video device name <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String VIDEO_DEVICE = "Lib/Video/Device";
	
	/** Setupkey SETUPKEY_VIDEO_DEVICE_PATH type:string  <br>Currently selected video device path. <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String VIDEO_DEVICE_PATH = "Lib/Video/DevicePath";
	
	/** Setupkey SETUPKEY_VIDEO_AUTOSEND type:int  <br>Setting this to 1 starts sending video automatically when call starts <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String VIDEO_AUTOSEND = "Lib/Video/AutoSend";
	
	/** Setupkey SETUPKEY_VIDEO_DISABLE type:int  <br>Setting this to 1 disables all video functionality. <br>This setup key is machine-specific and affects all local accounts. <br> */
	public static final String VIDEO_DISABLE = "*Lib/Video/Disable";
	
	/** Setupkey SETUPKEY_VIDEO_RECVPOLICY type:string default value:"contacts" <br>noone | contacts | callpolicy <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String VIDEO_RECVPOLICY = "Lib/Video/RecvPolicy";
	
	/** Setupkey SETUPKEY_VIDEO_ADVERTPOLICY type:string default value:"contacts" <br>noone | contacts | everyone <br>This is account-specific setup key. It can only be used while an account is logged in. <br> */
	public static final String VIDEO_ADVERTPOLICY = "Lib/Video/AdvertPolicy";
	
	private final static byte[] start_req = {(byte) 90,(byte) 82,(byte) 11,(byte) 2};
	/** This method starts either video send or video receive, depending on whether the video object is sender or receiver. In case of desktop video, the receiver side needs to instantiate a renderer object and associate it with the receiveing video (Video.SetRemoteRendererId).  
	 */
	public void start() {
		try {
			sidDoRequest(start_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] stop_req = {(byte) 90,(byte) 82,(byte) 11,(byte) 3};
	/** This method stops either video send or video receive, depending on whether the video object is sender or receiver. In case of desktop video, the receiver side needs to dis-associate the video object from the renderer, by calling Video.SetRemoteRendererId(0).  
	 */
	public void stop() {
		try {
			sidDoRequest(stop_req)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setScreenCaptureRectangle_req = {(byte) 90,(byte) 82,(byte) 11,(byte) 5};
	/** This method has no known effect in current version. 
	 * @param x0
	 * @param y0
	 * @param width
	 * @param height
	 * @param monitorNumber
	 * @param windowHandle
	 */
	public void setScreenCaptureRectangle(int x0, int y0, int width, int height, int monitorNumber, int windowHandle) {
		try {
			sidDoRequest(setScreenCaptureRectangle_req)
			.addIntParm(1, x0)
			.addIntParm(2, y0)
			.addUintParm(3, width)
			.addUintParm(4, height)
			.addIntParm(5, monitorNumber)
			.addUintParm(6, windowHandle)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] setRemoteRendererId_req = {(byte) 90,(byte) 82,(byte) 11,(byte) 14};
	/**
	 * This method is specific to working with desktop video runtimes. In case of desktop video, this method associates a Video object with a rendering object. Once this association is in place, the Video object will start sending decoded video frames to the renderer object, over shared memory IPC transport. The renderer then periodically reads the shared memory buffer and draws bitmaps on the screen.  
	 * 
	 * The "remote renderer ID" value you are expected to pass in this method comes from the renderer. The renderer class is normally implemented by you (for a specific GUI framework). However, the shared memory transport mechanism, for receiving decoded frames, is independant of GUI framework and are provided with the SDK for all three supporeted operating systems.  
	 * 
	 * Your renderer class can include instance of the IPC transport as VideoTransportClient <WinShm> ipc or VideoTransportClient <PosixShm>  ipc; The ipc object then exposes ipc.key() function that returns the IPC channel ID. This ID can then be passed to the Video.SetRemoteRendererId method. A reference implementation of desktop video is included in the SDK (examples/cpp/qt_videocalls). 
	 * 
	 * When the incoming video stream stops (or rendering is stopped by the user), your client should explicitly stop the frame transport by calling Video.SetRemoteRendererId(0). 
	
	 * @param id IPC channel ID retrieved from the shared memory transport class (see ipc/cpp/VideoBuffers in the SDK). 
	 */
	public void setRemoteRendererId(int id) {
		try {
			sidDoRequest(setRemoteRendererId_req)
			.addUintParm(1, id)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] selectVideoSource_req = {(byte) 90,(byte) 82,(byte) 11,(byte) 7};
	/**
	 * Switches between local webcams. This only works for video objects representing the local user. It is possible to switch to a different webcam on the fly, during a live session, i.e. you don't need to call Video.Stop and Video.Start while doing it. The easiest way to retrieve the local user is to call current live session's GetParticipants with PARTICIPANTFILTER.MYSELF. To get values for the deviceName and devicePath arguments, use Skype.GetAvailableVideoDevices method. Note that the Skype.GetAvailableVideoDevices should not be used immedately after account login. It will take several seconds to populate the video device list. You should handle the Skype.OnSkypeAvailableVideoDeviceListChange event to detect when the video device list is ready. 
	 * 
	 * Note that the device switching will only work with webcams that are not already in use by some other application. For example, if you have a Skype desktop client with video running in parallel, while testing, the desktop client will "claim" its default webcam. That webcam will still show up in the video device list in SkypeKit-based app, but switching to it will not work. 	
	 * @param mediaType Screensharing or webcam. 
	 * @param webcamName Displayname of the webcam. 
	 * @param devicePath Device ID (from the 2nd list you get back from the Skype.GetAvailableVideoDevices method) 
	 * @param updateSetup Set true if you want to change the default value. 
	 */
	public void selectVideoSource(MediaType mediaType, String webcamName, String devicePath, boolean updateSetup) {
		try {
			sidDoRequest(selectVideoSource_req)
			.addEnumParm(1, mediaType)
			.addStringParm(2, webcamName)
			.addStringParm(3, devicePath)
			.addBoolParm(4, updateSetup)
			.endOneWay();
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
		}
	}
	private final static byte[] getCurrentVideoDevice_req = {(byte) 90,(byte) 82,(byte) 11,(byte) 10};
	public class GetCurrentVideoDeviceResponse {
		public MediaType mediatype;
		public String deviceName;
		public String devicePath;
	};
	
	/**
	 * getCurrentVideoDevice
	 * @return GetCurrentVideoDeviceResponse
	 * <br> - mediatype
	 * <br> - deviceName
	 * <br> - devicePath
	 */
	public GetCurrentVideoDeviceResponse getCurrentVideoDevice() {
		try {
			Decoding decoder = sidDoRequest(getCurrentVideoDevice_req)
			.endRequest();
			GetCurrentVideoDeviceResponse result = new GetCurrentVideoDeviceResponse();
			result.mediatype = (MediaType) decoder.getEnumParm(1, MediaType.get(0), false);
			result.deviceName = decoder.getStringParm(2, false);
			result.devicePath = decoder.getStringParm(3, true);
			return result;
		} catch(IOException e) {
			mSidRoot.sidOnFatalError(e);
			return null;
		}
	}
	/***
	 * generic multiget of a list of Property
	 * @param requested the list of requested properties of Video
	 * @return SidGetResponding
	 */
	public SidGetResponding sidMultiGet(Property[] requested) {
		return super.sidMultiGet(requested);
	}
	/***
	 * generic multiget of list of Property for a list of Video
	 * @param requested the list of requested properties
	 * @return SidGetResponding[] can be casted to (Video[]) if all properties are cached
	 */
	static public SidGetResponding[] sidMultiGet(Property[] requested, Video[] objects) {
		return SidObject.sidMultiGet(requested, objects);
	}
	/** Video.STATUS */
	public Status getStatus() {
		synchronized(this) {
			if ((mSidCached & 0x1) != 0)
				return mStatus;
		}
		return (Status) sidRequestEnumProperty(Property.P_STATUS);
	}
	/** 'errorcode errortext'  */
	public String getError() {
		synchronized(this) {
			if ((mSidCached & 0x2) != 0)
				return mError;
		}
		return sidRequestStringProperty(Property.P_ERROR);
	}
	/** space-separated string of tokens */
	public String getDebugInfo() {
		synchronized(this) {
			if ((mSidCached & 0x4) != 0)
				return mDebugInfo;
		}
		return sidRequestStringProperty(Property.P_DEBUG_INFO);
	}
	/** This property does not currently work, always containing an empty string. For desktop video, you can get the frame dimensions from the video frame buffers API instead - the buffer struct retrieved with ipc.getFrame() or ipc.getNewFrame() has width and height fields, which you can then use in your UI. With RTP video solutions, you already have the frame sizes in your videohost code. Communicating these to the UI process is currently up to you.  */
	public String getDimensions() {
		synchronized(this) {
			if ((mSidCached & 0x8) != 0)
				return mDimensions;
		}
		return sidRequestStringProperty(Property.P_DIMENSIONS);
	}
	/** Indicates whether the video object is streaming webcam video or screensharing session, values: MEDIA_SCREENSHARING, MEDIA_VIDEO  */
	public MediaType getMediaType() {
		synchronized(this) {
			if ((mSidCached & 0x10) != 0)
				return mMediaType;
		}
		return (MediaType) sidRequestEnumProperty(Property.P_MEDIA_TYPE);
	}
	/** conference id to be able to identify remote/local video in the same call */
	public Conversation getConvoId() {
		synchronized(this) {
			if ((mSidCached & 0x20) != 0)
				return mConvoId;
		}
		return (Conversation) sidRequestObjectProperty(Property.P_CONVO_ID);
	}
	/** device path used by video object */
	public String getDevicePath() {
		synchronized(this) {
			if ((mSidCached & 0x40) != 0)
				return mDevicePath;
		}
		return sidRequestStringProperty(Property.P_DEVICE_PATH);
	}
	public String sidGetStringProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 131:
			return mError;
		case 132:
			return mDebugInfo;
		case 133:
			return mDimensions;
		case 1105:
			return mDevicePath;
		}
		return "";
	}
	public SidObject sidGetObjectProperty(final PropertyEnumConverting prop) {
		assert(prop.getId() == 1104);
		return mConvoId;
	}
	public EnumConverting sidGetEnumProperty(final PropertyEnumConverting prop) {
		switch(prop.getId()) {
		case 130:
			return mStatus;
		case 134:
			return mMediaType;
		}
		return null;
	}
	public String getPropertyAsString(final int prop) {
		switch (prop) {
		case 130: return getStatus().toString();
		case 131: return getError();
		case 132: return getDebugInfo();
		case 133: return getDimensions();
		case 134: return getMediaType().toString();
		case 1104: return getConvoId() != null ? Integer.toString(getConvoId().getOid()) : "(null)";
		case 1105: return getDevicePath();
		}
		return "<unkown>";
	}
	public String getPropertyAsString(final Property prop) {
		return getPropertyAsString(prop.getId());
	}
	protected void sidOnChangedProperty(final int propertyId, final int value, final String svalue) {
		final Property property = Property.get(propertyId);
		if (property == Property.P_UNKNOWN) return;
		final int idx = property.getIdx();
		if (idx != 0) {
			int bit  = 1<<((idx-1)%32);
			synchronized (this) {
				mSidCached|=bit;
				switch (propertyId) {
				case 130: mStatus = Status.get(value); break;
				case 131:
					if (svalue != null) mError = svalue;
					else mSidCached &=~bit;
					break;
				case 132:
					if (svalue != null) mDebugInfo = svalue;
					else mSidCached &=~bit;
					break;
				case 133:
					if (svalue != null) mDimensions = svalue;
					else mSidCached &=~bit;
					break;
				case 134: mMediaType = MediaType.get(value); break;
				case 1104:
					if (value != 0)
						mConvoId = (Conversation) mSidRoot.sidGetObject(property.getModuleId(), value);
					else {
						mConvoId = null;
						mSidCached &=~bit;
					}
					break;
				case 1105:
					if (svalue != null) mDevicePath = svalue;
					else mSidCached &=~bit;
					break;
				default: mSidCached&=~bit; break;
				}
			}
		}
		VideoListener listener = ((Skype) mSidRoot).getVideoListener();
		if (listener != null)
			listener.onPropertyChange(this, property, value, svalue);
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final String newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 131:
			mSidCached |= 0x2;
			mError=  newValue;
			break;
		case 132:
			mSidCached |= 0x4;
			mDebugInfo=  newValue;
			break;
		case 133:
			mSidCached |= 0x8;
			mDimensions=  newValue;
			break;
		case 1105:
			mSidCached |= 0x40;
			mDevicePath=  newValue;
			break;
		}
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final SidObject newValue) {
		final int propId = prop.getId();
		assert(propId == 1104);
		mSidCached |= 0x20;
		mConvoId= (Conversation) newValue;
	}
	public void sidSetProperty(final PropertyEnumConverting prop, final int newValue) {
		final int propId = prop.getId();
		switch(propId) {
		case 130:
			mSidCached |= 0x1;
			mStatus= Status.get(newValue);
			break;
		case 134:
			mSidCached |= 0x10;
			mMediaType= MediaType.get(newValue);
			break;
		}
	}
	public Status       mStatus;
	public String       mError;
	public String       mDebugInfo;
	public String       mDimensions;
	public MediaType    mMediaType;
	public Conversation mConvoId;
	public String       mDevicePath;
	public int moduleId() {
		return 11;
	}
	
	public Video(final int oid, final SidRoot root) {
		super(oid, root, 7);
	}
}
