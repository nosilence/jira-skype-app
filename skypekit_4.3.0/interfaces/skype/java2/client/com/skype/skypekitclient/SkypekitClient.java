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

import java.io.IOException;
import java.util.Scanner;

import com.skype.api.Account;
import com.skype.api.Account.GetStatusWithProgressResponse;
import com.skype.api.Contact;
import com.skype.api.ContactGroup;
import com.skype.api.ContactSearch;
import com.skype.api.Conversation;
import com.skype.api.Conversation.ListType;
import com.skype.api.Conversation.LocalLiveStatus;
import com.skype.api.Message;
import com.skype.api.Participant;
import com.skype.api.Participant.Dtmf;
import com.skype.api.Skype;
import com.skype.api.Sms;
import com.skype.api.Transfer;
import com.skype.api.Video;
import com.skype.api.Voicemail;
import com.skype.ipc.ClientConfiguration;
import com.skype.skypekitclient.SkypekitListeners.jwcObserver;
import com.skype.skypekitclient.command.CommandInvoker;
import com.skype.util.Log;

//import com.skype.ipc.TCPSocketTransport;
//import com.skype.ipc.TLSServerTransport;
//import com.skype.ipc.Transport;

/**
 *
 */
public class SkypekitClient implements jwcObserver
{
    private static final String TAG             = "SkypekitClient";

    private String                inetAddr        = "127.0.0.1";
    private String                pemFileName;
    private int                   portNum         = 8963;

    public Account                account;
    public Conversation           currentConversation;
    public Participant[]          currentConversationParticipants;
    public Contact[]              buddies;
    public Skype                  skype;
    public SkypekitListeners      theListeners;
    volatile public boolean       isConnected;
    public boolean                sendVideo;

    /**
     * Wrap System.out to make clear the different kinds of messages emitted by
     * the client
     * 
     * @param out
     */
    public void out(String out)
    {
        System.out.println(out);
    }

    public void error(String out)
    {
        System.err.println(out);
    }

    public void fatal_exception(Exception e)
    {
        e.printStackTrace();
        System.exit(1);
    }

    protected void usage(String message)
    {
        if (!message.isEmpty())
            error(message);

        System.err.println(
                "Usage: " + TAG + " [CONNECTION][OPTIONS]\n\n"
                + "CONNECTION:\n"
//                + "either tcp\n"
                + "\t-i ip addr \t\t- ip address (default " + inetAddr + ").\n"
                + "\t-p port \t\t- tcp port port (default " + portNum + ").\n"
//                + "or local\n"
//              + "\t-l local_connection_name \n"
                + "OPTIONS:\n"
                + "\t-t app_token_filename \t- file must contain a valid application token.\n"
                + "\t-r log_file_base \t- record transport streams in log path. \"-r ./transport\" will produce ./transport_log_in.1 and  ./transport_log_out.1\n"
                + "\t-a \t- dispatch all event (don't filter on object presence)\n"
                + "\t-S \t- forward string on property change event\n");

        System.exit(1);
    }

    protected ClientConfiguration configure(String[] args, ClientConfiguration configuration)
    {
        boolean strict = configuration == null;
        if (strict) configuration = new ClientConfiguration();

        for (int i = 0, numArgs = args.length; i < numArgs; i++) {
            if ((args[i].charAt(0) == '-') || (args[i].charAt(0) == '/')) {
                switch (args[i].charAt(1)) {
                case 'i': { // internet address
                    if ((i + 1 == numArgs) || (args[i + 1].charAt(0) == '-')) {
                        usage("malformed argument list");
                    }
                    else {
                        inetAddr = args[++i].toString();
                    }
                    break;
                }
                case 'p': { // port number
                    if ((i + 1 == numArgs) || (args[i + 1].charAt(0) == '-')) {
                        usage("malformed argument list");
                    }
                    else {
                        portNum = Integer.parseInt(args[++i]);
                    }
                    break;
                }
                case 'r': { // log transport streams?
                    if ((i + 1 == numArgs) || (args[i + 1].charAt(0) == '-')) {
                        usage("malformed argument list");
                    }
                    else {
                        configuration.generateTransportLog(args[++i].toString());
                    }
                    break;
                }
                case 't': { // token
                    if ((i + 1 == numArgs) || (args[i + 1].charAt(0) == '-')) {
                        usage("malformed argument list");
                    }
                    else {
                        pemFileName = configuration.setCertificate(args[++i].toString());
                    }
                    break;
                }
                case 'n': { // internal - no-TLS
                    configuration.dontUseTls();
                    break;
                }
                case 'd': { // internal - enable debug
                    Log.level = Log.Severity.kDebug;
                    break;
                }
                case 'a':
                    configuration.setDispatchAll();
                    break;
                case 'S':
                    configuration.fowardStringChangedValue();
                    break;
                default:
                    if (strict)
                        usage("Unsupported argument found:" + args[i]);
                    break;
                }
            }
            else {
                usage("malformed argument list");
                break;
            }
        }
        if (pemFileName.isEmpty()) {
            usage("-t Certificate file path argument missing.");
        }

        configuration.setTcpTransport(inetAddr, portNum);

        return configuration;
    }

    private void skypeConnect(ClientConfiguration configuration)
    {
        Log.d(TAG, "skypeConnect()");

        skypeCleanup();

        skype = new Skype();

        Log.d(TAG, "create theListeners");
        theListeners = new SkypekitListeners(this, skype);

        if (skype.init(configuration, theListeners)) {

            skype.start();

            Log.d(TAG, "calling GetVersionString");
            String version = skype.getVersionString();

//byte[] sounddata = new byte[16 *1024 -16];
//skype.playStart(5, sounddata, true, true);

            Log.d(TAG, "calling loginDefaultAccount()");
            loginDefaultAccount();
        } else {
            error("\n::: Error connecting to skypekit, enter 'r' to reconnect...\n");
            return;
        }
    }

    public void sidOnDisconnected(String message) {
        error("\ndisconnected:::\n"+message);
        isConnected = false;
    }

    public void sidOnConnected() {
        isConnected = true;
    }

    public void sidOnConnecting() {
    }

    public void loginDefaultAccount()
    {
        String accname = skype.getDefaultAccountName();
        if ((accname.length() != 0) && ((account = skype.getAccount(accname)) != null)) {
            out("Logging in with  default account: " + accname);
            account.login(Contact.Availability.ONLINE);
        }
        else {
            out("Default account is not available, use aL command to login manually");
        }
    }

    public boolean isLoggedIn()
    {
        if (account != null) {
            GetStatusWithProgressResponse r = account.getStatusWithProgress();
            if (r != null && r.status == Account.Status.LOGGED_IN) {
                return true;
            }
        }
            
        return false;
    }

    public boolean notLoggedIn()
    {
        if (isLoggedIn())
            return false;
        error("You are not logged in");
        return true;
    }

    private void skypeCleanup()
    {
        if (theListeners != null)
            theListeners.unRegisterAllListeners();
        theListeners = null;
        if (skype != null)
            skype.stop();
        skype = null;
    }

    private void eventLoop(ClientConfiguration configuration)
    {
        boolean quitEventLoop = false;
        Scanner scan = new Scanner(System.in);
        String scanLine;
        Character command;
        Character subCommand;

        CommandInvoker invoker = new CommandInvoker(this);

            while (!quitEventLoop) {
                
                if ( !isConnected) {
                    error("\n::: Disconnected from skypekit, enter 'r' to reconnect...\n\n");
                }
                
                if (theListeners.loggedIn)
                    out("Currently Logged In as: " + account.getSkypeName());

                scanLine = scan.nextLine();
                int scanLength = scanLine.length();
                if (scanLength > 0) {
                    if (scanLength != 2) {
                        switch (scanLine.charAt(0)) {
                        case '?':
                            invoker.executeHelp(isConnected);
                            break;

                        case 'd':
                            Log.cycleDebugLevel();
                            out("\n::: setting log level to " + Log.debugLevel());
                            break;

                        case 'q':
                            out("\n::: Quiting " + TAG);
                            quitEventLoop = true;
                            break;
                        
                        case 'r':
                            out("\n::: Reconnecting...\n");
                            skypeConnect(configuration);
                            break;
                        
                        default:
                            out("\n::: Type ? for list of available commands\n");
                        }
                    }
                    else {
                        command = scanLine.charAt(0);
                        subCommand = scanLine.charAt(1);
                        invoker.executeCommand(command, subCommand);
                    }
                }
                else {
                    out("\n::: Type ? for list of available commands\n");
                }
            }
    }

    public SkypekitClient(String[] args, ClientConfiguration cfg) throws IOException
    {
        ClientConfiguration configuration = configure(args, cfg);

        skypeConnect(configuration);

        eventLoop(configuration);

        skypeCleanup();

        System.exit(0);
    }

    /**
     * Java command line client for SkypeKit
     * 
     * @param args
     */
    public static void main(String[] args) throws IOException
    {
        // System.setProperty("javax.net.debug","all");
        new SkypekitClient(args, null);
    }

    @Override
    public void onAccountStatusChange()
    {
        Log.d(TAG, "onAccountStatusChange()");
    }

    @Override
    public void onLiveStatusChange(Conversation conv, LocalLiveStatus status)
    {
        Log.d(TAG, "onLiveStatusChange(" + conv + ", " + status + ")");
    }

    @Override
    public void onVideoErrorChange(Video v, String error)
    {
        Log.d(TAG, "onVideoErrorChange(" + v + ", error:" + error + ")");
    }

    @Override
    public void onConversationListChange(Conversation conversation, ListType type, boolean added)
    {
        if (added)
            out("onConversationListChange:" + conversation.getDisplayName() + ", list_type " + type + ", was added");
        else
            out("onConversationListChange:" + conversation.getDisplayName() + ", list_type " + type + ", was removed");
    }

    @Override
    public void onVideoDimensionsChange(Video v, String value)
    {
        Log.d(TAG, "onVideoDimensionsChange(" + v.getDevicePath() + ", " + value + ")");
    }

    @Override
    public void onNewCustomContactGroup(ContactGroup group)
    {
        Log.d(TAG, "OnNewCustomContactGroup(" + group.getGivenDisplayName() + ")");
    }

    @Override
    public void onPropertyChange(Contact c, Contact.Property prop, int value, String svalue)
    {
        String skypeName = c.getIdentity();
        switch (prop) {
        case P_AVAILABILITY: {
            out("CONTACT." + skypeName + ":AVAILABILITY = " + Contact.Availability.get(value));
            break;
        }
        case P_MOOD_TEXT: { 
            out("CONTACT." + skypeName + ":MOOD = " + (svalue == null ? c.getMoodText() : svalue));
        }
        case P_AVATAR_IMAGE: { 
            out("CONTACT." + skypeName + ":AVATAR = " + (c.getAvatarImage() != null ?  c.getAvatarImage().length  : 0) + "bytes");
        }
        }
    }

    @Override
    public void onPropertyChange(Voicemail vm, Voicemail.Property prop, int value, String svalue)
    {
        String partnerHandle;
        String partnerDispName;
        partnerHandle = vm.getPartnerHandle();
        partnerDispName = vm.getPartnerDisplayName();
        Log.d(TAG, "OnPropertyChange(" + vm + ", (Voicemail)" + prop + ", " + (svalue == null ? value : svalue) + ")");
        Log.d(TAG, "Partner_handle = " + partnerHandle + ", Partner_DisplayName = " + partnerDispName);

        out("VOICEMAIL." + vm.getOid() + ":" + prop + " = " +  (svalue == null ? value : svalue));
        out("Voicemail.onPropertyChange - partner_handle = " + partnerHandle + ", Partner_DisplayName = " + partnerDispName);
    }

    @Override
    public void onPropertyChange(Video v, Video.Property prop, int value, String svalue)
    {
        out("Video.convid = " + v.getConvoId()  + ", " + prop + " = " + (svalue == null ? value : svalue));

        Participant[] participants = currentConversation.getParticipants(Conversation.ParticipantFilter.MYSELF);

        if(participants != null && participants.length > 0 && participants[0].getIdentity().equals(account.getSkypeName()) && 
            prop.equals(Video.Property.P_STATUS) && Video.Status.get(value) == Video.Status.AVAILABLE && sendVideo) {
            v.start();
            sendVideo = false;
        }
    }

    @Override
    public void onPropertyChange(Transfer t, Transfer.Property prop, int value, String svalue)
    {
        if (prop == Transfer.Property.P_STATUS) {
            out("TRANSFER." + t.getFilePath() + ", " + t.getPartnerDisplayName() + ", STATUS = " + Transfer.Status.get(value));
        }
    }

    @Override
    public void onPropertyChange(Sms obj, Sms.Property prop, int value, String svalue)
    {
        out("SMS." + obj.getOid() + ":" + prop + " = " + (svalue == null ? value : svalue));
    }

    @Override
    public void onPropertyChange(Participant obj, Participant.Property prop, int value, String svalue)
    {
        // Disabling output since these notifications are mostly noise.
        Log.d(TAG, "PARTICIPANT." + obj.getIdentity() + ": " + prop + " = " + (svalue == null ? value : svalue));
    }

    @Override
    public void onPropertyChange(Message obj, Message.Property prop, int value, String svalue)
    {
        out("MESSAGE." + obj.getOid() + ":" + obj.getAuthor() + ", " + prop + " = " + (svalue == null ? value : svalue));
// or        out("MESSAGE." + obj.getOid() + ":" + obj.getAuthor() + ", " + prop + " = " + obj.getAsString(prop));
// drawback may send a request to get the value...
    }

    @Override
    public void onPropertyChange(Conversation obj, Conversation.Property prop, int value, String svalue)
    {
        out("CONVERSATION." + obj.getOid() + ":\"" + obj.getDisplayName() + "\":" + prop + " = " + (svalue == null ? value : svalue));
    }

    @Override
    public void onPropertyChange(ContactSearch obj, ContactSearch.Property prop, int value, String svalue)
    {
        if (prop == ContactSearch.Property.P_CONTACT_SEARCH_STATUS) {
            switch (ContactSearch.Status.get(value)) {
            case PENDING:
                out("CONTACTSEARCH.STARTING");
                break;
            case FINISHED:
                out("CONTACTSEARCH.FINISHED");
                break;
            }
        }
    }

    @Override
    public void onPropertyChange(ContactGroup obj, ContactGroup.Property prop, int value, String svalue)
    {
        // TODO - figure out how to implement whether contact was added or removed  
        Log.d(TAG, "onPropertyChange (ContactGroup)" + obj.getGivenDisplayName() + ", " + prop + ", " + (svalue == null ? value : svalue) + ")");
    }

    @Override
    public void onPropertyChange(Account obj, Account.Property prop, int value, String svalue)
    {
        Log.d(TAG, "OnPropertyChange" + obj + ", (Account)" + prop + ", " + (svalue == null ? value : svalue) + ")");

        String skypeName = "(not logged in)";
        if (account != null) {
            skypeName = account.getSkypeName();
        }

        switch (prop) {
        case P_STATUS: {
            if (account != null) {
                GetStatusWithProgressResponse loginStatus = account.getStatusWithProgress();
                out("Status: " + Account.Status.get(value) + " Progress: " + loginStatus.progress);

                switch (loginStatus.status) {
                case LOGGED_IN: {
                    if (value == Account.Status.LOGGED_IN_VALUE) {
                        out("Login complete.");

                        // (re)create CONTACTS_WAITING_MY_AUTHORIZATION upon login so
                        // the new authrequest events will be received
                        ContactGroup auth_reqs_group = skype
                            .getHardwiredContactGroup(ContactGroup.Type.CONTACTS_WAITING_MY_AUTHORIZATION);
                        if (auth_reqs_group != null) {
                            int waiting = auth_reqs_group.getContactCount();
                            if (waiting == 0)
                                out("No pending contact authorizations.");
                            else if (waiting == 1)
                                 out("One pending contact authorization.");
                            else
                                 out(waiting + " pending contact authorizations.");
                        }
                    }
                    break;
                }
                case LOGGED_OUT:
                case LOGGED_OUT_AND_PWD_SAVED: {
                    out("Logout complete.");
                    break;
                }
                default: {
                   break;
                }
                }
            } else {
               out("ACCOUNT." + skypeName + ":" + prop.name() + " = " + (svalue == null ? value : svalue));
            }
            break;
        }
        case P_AVAILABILITY: {
            out("ACCOUNT." + skypeName + ":" + prop + " = " + Contact.Availability.get(value));
            break;
        }
        case P_SUGGESTED_SKYPE_NAME: {
            out("ACCOUNT." + skypeName + ":" + prop + " = " + account.getSuggestedSkypeName());
            break;
        }
        case P_LOGOUT_REASON: {
            out("ACCOUNT." + skypeName + ":LOGOUT_REASON" + " = " + Account.LogoutReason.get(value));
            break;
        }
        default: {
            out("ACCOUNT." + skypeName + ":" + prop + " = " + (svalue == null ? value : svalue));
            break;
        }
        }
    }

    public void onQualityTestResult(Skype object, Skype.QualityTestType testType, Skype.QualityTestResult testResult, String withUser, String details, String xmlDetails) {
        out("onQualityTestResult " + testType + " " + testResult + " withUser " + withUser + "\n" + details + "\n" + xmlDetails);
    }

    public void onIncomingDtmf(Participant obj, Dtmf dtmf)
    {
        out("OnIncomingDTMF(" + dtmf + ")");
    }

    public void onLiveSessionVideosChanged(Participant obj)
    {
        out("OnLiveSessionVideosChanged()");
    }

    @Override
    public void onMessage(Message message, boolean changesInboxTimestamp, Message supersedesHistoryMessage,
            Conversation conversation)
    {
        Log.d(TAG, "OnMessage(message:" + message.getOid() + ", " + changesInboxTimestamp + ", " + supersedesHistoryMessage + ", conversationq:"
                + conversation.getOid() + ")");

        message.mgetInfo();

        String author = message.getAuthorDisplayName();
        Message.Type type = message.getType();
        out("SKYPE.OnMessage." + author + " Message::TYPE = " + type);
        
        switch (type) {
        case POSTED_FILES: {
            Transfer[] transfers = message.getTransfers();
            for (Transfer t : transfers) {
                out("POSTED." + author + ":" + t.getFileName());
            }
            break;
        }
        case POSTED_TEXT: {
            out("CHAT." + author + ":" + conversation.getOid() + ":" + message.getBodyXml());
            break;
        }
        case POSTED_SMS: {
            out("SMS." + author + ":" + conversation.getOid() + ":" + message.getBodyXml());
            break;
        }
        case ADDED_CONSUMERS: {
            out("CONVERSATION." + author + ": Added Participants to conversation:" + conversation.getOid());
            break;
        }
        case RETIRED_OTHERS: {
            out("CONVERSATION." + author + ": Removed Participants from conversation:" + conversation.getOid());
            break;
        }
        case POSTED_VOICE_MESSAGE: {
            out("Voicemail from " + author);
            break;
        }
        }
    }
	
    @Override
    public void onH264Activated(Skype obj)
    {
        out("onH264Activated");
    }
	
}

