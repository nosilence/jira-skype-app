package com.skype.api;

import com.skype.api.Conversation;
import com.skype.api.Skype;


public interface SkypeListener{
	/**
	 * onNewCustomContactGroup
	 * @param object
	 * @param group eg, new contact group loaded from CBL
	 */
	public void onNewCustomContactGroup(Skype object, ContactGroup group);
	/** This contact has appeared online. display alert * @param contact
	 */
	public void onContactOnlineAppearance(Skype object, Contact contact);
	/** This contact has gone offline. display alert * @param contact
	 */
	public void onContactGoneOffline(Skype object, Contact contact);
	/** This event gets fired when a Conversation item is added or removed from the list specified in the type argument. The primary use of this event is to detect creation of new Conversation objects. It can also be used for detecting occurance of live sessions - by monitoring added = true in Conversation.LIVE_CONVERSATIONS. Note that this method is not entirely sufficient for detecting live session termination (added = false and type = Conversation.LIVE_CONVERSATIONS). When the live session goes down, the default behaviour is that the Conversation object remains in the LIVE_CONVERSATIONS list for approximately 10 seconds. When another live session comes up within the same Conversation, the OnConversationListChange event will not fire - because the conversation was already in that list. There are two ways of getting around that. Firstly you can have all the conversations referenced at all times and then monitor Conversation.P_LOCAL_LIVESTATUS property changes, in which case you can pick up incoming live sessions from there. Alternatively, you can remove the delay between live session termination and conversation's removal from the LIVE_CONVERSATIONS list. This delay is controlled by the SETUPKEY_RECENTLY_LIVE_TIMEOUT setup key. To remove the delay, use Skype.SetInt(SETUPKEY_RECENTLY_LIVE_TIMEOUT, 0). Note that this setup key is account-based. You will need to have an account logged in in order to modify its value.  * @param conversation Conversation object that was added or removed to a list specified in the type argument. 
	 * @param type Specifies the list, into which the conversation was added or removed from. 
	 * @param added Specifies whether the conversation was added or removed. For ALL_CONVERSATIONS list, the removed event is only fired when the conversation is actually deleted. 
	 */
	public void onConversationListChange(Skype object, Conversation conversation, Conversation.ListType type, boolean added);
	/**
	 * onMessage
	 * @param object
	 * @param message
	 * @param changesInboxTimestamp if changesInboxTimestamp==true is a hint that tray alert should probably be displayed
	 * @param supersedesHistoryMessage DEPRECATED, not set anymore
	 * @param conversation
	 */
	public void onMessage(Skype object, Message message, boolean changesInboxTimestamp, Message supersedesHistoryMessage, Conversation conversation);
	/** This callback gets fired when there are changes in the system video device list (USB webcam gets plugged in or is detached.) Note that local webcams are unavailable immediately after account login. It takes several seconds to populate and verify the webcam list. Thus, if your UI makes use of local video, you should always use this event for enabling video-related features in your UI.  */
	public void onAvailableVideoDeviceListChange(Skype object);
	/** Event is implemented only in SkypeKit builds. Fired when Skype video library uses software H264 codec for the first time on the particular hardware by particular SkypeKit-based application */
	public void onH264Activated(Skype object);
	/**
	 * onQualityTestResult
	 * @param object
	 * @param testType
	 * @param testResult
	 * @param withUser
	 * @param details
	 * @param xmlDetails
	 */
	public void onQualityTestResult(Skype object, Skype.QualityTestType testType, Skype.QualityTestResult testResult, String withUser, String details, String xmlDetails);
	/** This callback gets fired when there are changes in the system audio device list (USB headset gets plugged in or is detached.)  */
	public void onAvailableDeviceListChange(Skype object);
	/** This callback gets fired when the audio strength changes in either playback or recording audio streams. Useful for providing visual indicators of audio activity in your UI.  */
	public void onNrgLevelsChange(Skype object);
	/**
	 * onProxyAuthFailure
	 * @param object
	 * @param type
	 */
	public void onProxyAuthFailure(Skype object, Skype.ProxyType type);
	/** This event gets fired on incoming app2app datagram.  * @param appname App2app application ID. 
	 * @param stream App2app stream ID - see OnApp2AppStreamListChange event for obtaining stream IDs. 
	 * @param data Datagram payload - limited to 1500 bytes. 
	 */
	public void onApp2AppDatagram(Skype object, String appname, String stream, byte[] data);
	/**
	 * In context of datagrams, this event will fire when: 
	 *  - Connection is established between two app2app applications. That is, when both parties have an app up with the same name and -both- used App2AppConnect In that case, both parties get this event, with listType ALL_STREAMS 
	 *  - When a datagram is sent, the sender will get this event with listType SENDING_STREAMS Receiver of the datagram will get OnApp2AppDatagram event instead. 
	 *  - When the remote party drops app2app connection, the local user will get OnApp2AppStreamListChange with listType ALL_STREAMS and streams.size() zero. 
	 * In context of stream reads/writes, this event will fire for both the sender (listType == SENDING_STREAMS)and the receiver (listType == RECEIVED_STREAMS). For receiver side, this is the place to put your reading code - App2AppRead. 
	 * @param appname application ID - the name you supplied in App2AppCreate. 
	 * @param listType application list type (read/write/all) 
	 * @param streams SEStringlist with affected stream IDs. 
	 * @param receivedSizes For RECEIVED_STREAMS, contains the number of bytes in each stream waiting to be read
	 */
	public void onApp2AppStreamListChange(Skype object, String appname, Skype.App2AppStreams listType, String[] streams, int[] receivedSizes);
}
