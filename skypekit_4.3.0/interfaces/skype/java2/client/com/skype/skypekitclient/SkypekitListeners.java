/**
 * Copyright (C) 2010, Skype Limited
 *
 * All intellectual property rights, including but not limited to copyrights,
 * trademarks and patents, as well as know how and trade secrets contained in,
 * relating to, or arising from the internet telephony software of
 * Skype Limited (including its affiliates, "Skype"), including without
 * limitation this source code, Skype API and related material of such
 * software proprietary to Skype and/or its licensors ("IP Rights") are and
 * shall remain the exclusive property of Skype and/or its licensors.
 * The recipient hereby acknowledges and agrees that any unauthorized use of
 * the IP Rights is a violation of intellectual property laws.
 *
 * Skype reserves all rights and may take legal action against infringers of
 * IP Rights.
 *
 * The recipient agrees not to remove, obscure, make illegible or alter any
 * notices or indications of the IP Rights and/or Skype's rights and
 * ownership thereof.
 */

package com.skype.skypekitclient;

import com.skype.api.Account;
import com.skype.api.AccountListener;
import com.skype.api.Contact;
import com.skype.api.ContactGroup;
import com.skype.api.ContactGroupListener;
import com.skype.api.ContactListener;
import com.skype.api.ContactSearch;
import com.skype.api.ContactSearchListener;
import com.skype.api.Conversation;
import com.skype.api.Conversation.ListType;
import com.skype.api.ConversationListener;
import com.skype.api.Message;
import com.skype.api.MessageListener;
import com.skype.api.Participant;
import com.skype.api.Participant.Dtmf;
import com.skype.api.ParticipantListener;
import com.skype.api.Skype;
import com.skype.api.Skype.ProxyType;
import com.skype.api.SkypeListener;
import com.skype.api.Sms;
import com.skype.api.SmsListener;
import com.skype.api.Transfer;
import com.skype.api.TransferListener;
import com.skype.api.Video;
import com.skype.api.VideoListener;
import com.skype.api.Voicemail;
import com.skype.api.VoicemailListener;
import com.skype.ipc.ConnectionListener;
import com.skype.util.Log;

/**
 *
 */
public class SkypekitListeners implements AccountListener, SkypeListener, ContactListener, ContactGroupListener,
    ContactSearchListener, ConversationListener, MessageListener, ParticipantListener, SmsListener, TransferListener,
    VideoListener, VoicemailListener, ConnectionListener
{
    private static final String TAG      = "SkypekitListeners";

    public boolean              loggedIn = false;

    private Skype               skype;

    private jwcObserver         mObserver;
    private SkypekitClient      mClient;

    public interface jwcObserver
    {
        public abstract void onConversationListChange(Conversation conversation, ListType type, boolean added);
        public abstract void onIncomingDtmf(Participant obj, Dtmf dtmf);
        public abstract void onLiveSessionVideosChanged(Participant obj);
        public abstract void onMessage(Message message, boolean changesInboxTimestamp, Message supersedesHistoryMessage, Conversation conversation);
        public abstract void onNewCustomContactGroup(ContactGroup group);
        public abstract void onPropertyChange(Account obj, Account.Property prop, int value, String svalue);
        public abstract void onPropertyChange(Contact obj, Contact.Property prop, int value, String svalue);
        public abstract void onPropertyChange(ContactGroup obj, ContactGroup.Property prop, int value, String svalue);
        public abstract void onPropertyChange(ContactSearch obj, ContactSearch.Property prop, int value, String svalue);
        public abstract void onPropertyChange(Conversation obj, Conversation.Property prop, int value, String svalue);
        public abstract void onPropertyChange(Message obj, Message.Property prop, int value, String svalue);
        public abstract void onPropertyChange(Participant obj, Participant.Property prop, int value, String svalue);
        public abstract void onPropertyChange(Sms obj, Sms.Property prop, int value, String svalue);
        public abstract void onPropertyChange(Transfer obj, Transfer.Property prop, int value, String svalue);
        public abstract void onPropertyChange(Video obj, Video.Property prop, int value, String svalue);
        public abstract void onPropertyChange(Voicemail obj, Voicemail.Property prop, int value, String svalue);
        public abstract void onAccountStatusChange();
        public abstract void onLiveStatusChange(Conversation conv, Conversation.LocalLiveStatus status);
        public abstract void onVideoDimensionsChange(Video v, String value);
        public abstract void onVideoErrorChange(Video v, String error);
	public abstract void onH264Activated(Skype obj);
	public abstract void onQualityTestResult(Skype object, Skype.QualityTestType testType, Skype.QualityTestResult testResult, String withUser, String details, String xmlDetails);
        public void sidOnDisconnected(String message);
        public void sidOnConnected();
        public void sidOnConnecting();
    }

    public SkypekitListeners(SkypekitClient observer, Skype theSkype)
    {
        this.skype = theSkype;
        this.mObserver = observer;
        this.mClient = observer;
        Log.d(TAG, "ctor time to register the listeners");
        registerAllListeners();
        
    }

    public void registerAllListeners()
    {
        skype.registerConnectionListener(this);
        skype.registerSkypeListener(this);
        skype.registerAccountListener(this);
        skype.registerContactListener(this);
        skype.registerContactGroupListener(this);
        skype.registerContactSearchListener(this);
        skype.registerConversationListener(this);
        skype.registerMessageListener(this);
        skype.registerParticipantListener(this);
        skype.registerSmsListener(this);
        skype.registerTransferListener(this);
        skype.registerVideoListener(this);
        skype.registerVoicemailListener(this);
    }

    public void unRegisterAllListeners()
    {
        skype.unRegisterSkypeListener(this);
        skype.unRegisterAccountListener(this);
        skype.unRegisterContactListener(this);
        skype.unRegisterContactGroupListener(this);
        skype.unRegisterContactSearchListener(this);
        skype.unRegisterConversationListener(this);
        skype.unRegisterMessageListener(this);
        skype.unRegisterParticipantListener(this);
        skype.unRegisterSmsListener(this);
        skype.unRegisterTransferListener(this);
        skype.unRegisterVideoListener(this);
        skype.unRegisterVoicemailListener(this);
        skype.unRegisterConnectionListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Voicemail.VoicemailListener#onPropertyChange(com.skype.
     * api.SkypeObject, com.skype.api.Voicemail.Property, java.lang.Object)
     */
    public void onPropertyChange(Voicemail obj, Voicemail.Property prop, int value, String svalue)
    {
        mObserver.onPropertyChange(obj, prop, value, svalue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Video.VideoListener#onPropertyChange(com.skype.api.SkypeObject
     * , com.skype.api.Video.Property, java.lang.Object)
     */
    public void onPropertyChange(Video obj, Video.Property prop, int value, String svalue)
    {
        mObserver.onPropertyChange(obj, prop, value, svalue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.skype.api.Video.VideoListener#OnCaptureRequestCompleted(int,
     * boolean, byte[], int, int)
     */
    public void onCaptureRequestCompleted(Video obj, int requestId, boolean isSuccessful, byte[] image, int width, int height)
    {
        Log.d(TAG, "OnCaptureRequestCompleted(" + requestId + ", " + isSuccessful + ", image.length:" + image.length
                + ", " + width + ", " + height + ")");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Transfer.TransferListener#onPropertyChange(com.skype.api
     * .SkypeObject, com.skype.api.Transfer.Property, java.lang.Object)
     */
    public void onPropertyChange(Transfer obj, Transfer.Property prop, int value, String svalue)
    {
        mObserver.onPropertyChange(obj, prop, value, svalue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Sms.SmsListener#onPropertyChange(com.skype.api.SkypeObject,
     * com.skype.api.Sms.Property, java.lang.Object)
     */
    public void onPropertyChange(Sms obj, Sms.Property prop, int value, String svalue)
    {
        mObserver.onPropertyChange(obj, prop, value, svalue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Participant.ParticipantListener#onPropertyChange(com.skype
     * .api.SkypeObject, com.skype.api.Participant.Property, java.lang.Object)
     */
    public void onPropertyChange(Participant obj, Participant.Property prop, int value, String svalue)
    {
        mObserver.onPropertyChange(obj, prop, value, svalue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Participant.ParticipantListener#OnIncomingDTMF(com.skype
     * .api.Participant.DTMF)
     */
    public void onIncomingDtmf(Participant obj, Dtmf dtmf)
    {
        mObserver.onIncomingDtmf(obj, dtmf);
    }

    public void onLiveSessionVideosChanged(Participant obj)
    {
        mObserver.onLiveSessionVideosChanged(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Message.MessageListener#onPropertyChange(com.skype.api.
     * SkypeObject, com.skype.api.Message.Property, java.lang.Object)
     */
    public void onPropertyChange(Message obj, Message.Property prop, int value, String svalue)
    {
        mObserver.onPropertyChange(obj, prop, value, svalue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.ConversationListener#onPropertyChange(Conversation, Conversation.Property, int)
     */
    public void onPropertyChange(Conversation obj, Conversation.Property prop, int value, String svalue)
    {
        mObserver.onPropertyChange(obj, prop, value, svalue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.ConversationListener#OnParticipantListChange()
     */
    public void onParticipantListChange(Conversation obj)
    {
        Log.d(TAG, "onParticipantListChange(" + obj.getOid() + ")");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Conversation.ConversationListener#OnMessage(com.skype.api
     * .Message)
     */
    public void onMessage(Conversation obj, Message message)
    {
        Log.d(TAG, "OnMessage(" + message + ") in conversation:" + obj.getOid());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Conversation.ConversationListener#OnSpawnConference(com
     * .skype.api.Conversation)
     */
    public void onSpawnConference(Conversation obj, Conversation spawned)
    {
        Log.d(TAG, "OnSpawnConference(" + spawned + ")");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.ContactSearch.ContactSearchListener#onPropertyChange(com
     * .skype.api.SkypeObject, com.skype.api.ContactSearch.Property,
     * java.lang.Object)
     */
    public void onPropertyChange(ContactSearch obj, ContactSearch.Property prop, int value, String svalue)
    {
        mObserver.onPropertyChange(obj, prop, value, svalue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.ContactSearch.ContactSearchListener#OnNewResult(com.skype
     * .api.Contact, int)
     */
    public void onNewResult(ContactSearch obj, Contact contact, int rankValue)
    {
		String identity = contact.getIdentity();
		System.out.printf("ContactSearch.onNewResult (%d): %s\n", rankValue, identity);;
        Log.d(TAG, "onNewResult(" + contact + ", " + rankValue + ")");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.ContactGroup.ContactGroupListener#onPropertyChange(com.
     * skype.api.SkypeObject, com.skype.api.ContactGroup.Property,
     * java.lang.Object)
     */
    public void onPropertyChange(ContactGroup obj, ContactGroup.Property prop, int value, String svalue)
    {
        mObserver.onPropertyChange(obj, prop, value, svalue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.ContactGroup.ContactGroupListener#OnChangeConversation(
     * com.skype.api.Conversation)
     */
    public void onChangeConversation(Skype obj, Conversation conversation)
    {
        Log.d(TAG, "onChangeConversation(" + conversation + ")");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.ContactGroup.ContactGroupListener#OnChange(com.skype.api
     * .Contact)
     */
    public void onChange(Skype obj, Contact contact)
    {
        Log.d(TAG, "onChange(" + contact + ")");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Contact.ContactListener#onPropertyChange(com.skype.api.
     * SkypeObject, com.skype.api.Contact.Property, java.lang.Object)
     */
    public void onPropertyChange(Contact obj, Contact.Property prop, int value, String svalue)
    {
         mObserver.onPropertyChange(obj, prop, value, svalue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Skype.SkypeListener#OnNewCustomContactGroup(com.skype.api
     * .ContactGroup)
     */
    public void onNewCustomContactGroup(Skype obj, ContactGroup group)
    {
        Log.d(TAG, "onNewCustomContactGroup(" + group + ")");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Skype.SkypeListener#OnContactOnlineAppearance(com.skype
     * .api.Contact)
     */
    public void onContactOnlineAppearance(Skype obj, Contact contact)
    {
        mClient.out("onContactOnlineAppearance: " + contact.getSkypeName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Skype.SkypeListener#OnContactGoneOffline(com.skype.api.
     * Contact)
     */
    public void onContactGoneOffline(Skype obj, Contact contact)
    {
        mClient.out("SKYPE.onContactGoneOffline: " + contact.getSkypeName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Skype.SkypeListener#OnConversationListChange(com.skype.
     * api.Conversation, com.skype.api.Conversation.LIST_TYPE, boolean)
     */
    public void onConversationListChange(Skype obj, Conversation conversation, ListType type, boolean added)
    {
        mObserver.onConversationListChange(conversation, type, added);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.skype.api.Skype.SkypeListener#OnMessage(com.skype.api.Message,
     * boolean, com.skype.api.Message, com.skype.api.Conversation)
     */
    public void onMessage(Skype obj, Message message, boolean changesInboxTimestamp, Message supersedesHistoryMessage,
            Conversation conversation)
    {
        mObserver.onMessage(message, changesInboxTimestamp, supersedesHistoryMessage, conversation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.skype.api.Skype.SkypeListener#OnAvailableVideoDeviceListChange()
     */
    public void onAvailableVideoDeviceListChange(Skype obj)
    {
        Log.d(TAG, "OnAvailableVideoDeviceListChange()");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.skype.api.Skype.SkypeListener#OnAvailableDeviceListChange()
     */
    public void onAvailableDeviceListChange(Skype obj)
    {
        Log.d(TAG, "OnAvailableDeviceListChange()");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.skype.api.Skype.SkypeListener#OnNrgLevelsChange()
     */
    public void onNrgLevelsChange(Skype obj)
    {
        Log.d(TAG, "OnNrgLevelsChange()");
    }

    /**
     * This event can get fired on P2P connection failure during account login.
     * Note that by default, direct connection to the network is attempted. If
     * that fails, the fallback will be to connect via proxy. If that fails as
     * well, this event will get fired.
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.skype.api.Skype.SkypeListener#OnProxyAuthFailure(PROXYTYPE type)
     */
    public void onProxyAuthFailure(Skype obj, ProxyType type)
    {
        mClient.error("Proxy Authorization Failure:" + type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.skype.api.Account.AccountListener#onPropertyChange(com.skype.api.
     * SkypeObject, com.skype.api.Account.Property, java.lang.Object)
     */
    public void onPropertyChange(Account obj, Account.Property prop, int value, String svalue)
    {
        mObserver.onPropertyChange(obj, prop, value, svalue);
    }

    public void onFatalError()
    {
        mClient.error("SkypeKit fatal error reported.  Continue at your own risk.");
        mClient.error("Real applications should shut down at this point.");
    }

    public void onH264Activated(Skype obj)
    {
    }

    public void onQualityTestResult(Skype object, Skype.QualityTestType testType, Skype.QualityTestResult testResult, String withUser, String details, String xmlDetails) {
        mObserver.onQualityTestResult(object, testType, testResult, withUser, details, xmlDetails);
    }

    public void onApp2AppDatagram(Skype object, String appname, String stream, byte[] data) {
    }

    public void onApp2AppStreamListChange(Skype object, String appname, Skype.App2AppStreams listType, String[] streams, int[] receivedSizes) {
    }

    public void onChangeConversation(ContactGroup object, Conversation conversation) {
    }

    public void onChange(ContactGroup object, Contact contact) {
    }

    public void sidOnDisconnected(String message) {
        mClient.error("The connection to the SkypeKit runtime has closed.");
        mClient.error("Recovery is possible is the runtime resumes and the user reconnects to it.");
        mObserver.sidOnDisconnected(message);
    }

    public void sidOnConnected() {
        mObserver.sidOnConnected();
    }

    public void sidOnConnecting() {
        mObserver.sidOnConnecting();
    }
}

