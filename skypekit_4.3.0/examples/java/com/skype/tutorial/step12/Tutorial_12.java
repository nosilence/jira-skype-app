/**
 * Copyright (C) 2010, 2011 Skype Limited
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

package com.skype.tutorial.step12;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

import com.skype.api.Conversation;
import com.skype.api.Message;
import com.skype.api.Sms;

import com.skype.tutorial.appkeypair.AppKeyPairMgr;
import com.skype.tutorial.util.MySession;
import com.skype.tutorial.util.XmlStrMgr;

 /**
  * Getting Started With SkypeKit: Tutorial Application, Step 12.
  *
  * This example illustrates a simple SkypeKit-based program that:
  * <ol>
  *   <li>Takes a Skype Name, password, and optional AppKeyPair PEM file pathname as command-line arguments</li>
  *   <li>Initiates login for that user</li>
  *   <li>Waits until the login process is complete</li>
  *   <li>assembles and displays an event history of:
  *     <ul>
  *       <li>incoming calls</li>
  *       <li>outgoing calls</li>
  *       <li>authorization requests</li>
  *       <li>authorizations granted</li>
  *     </ul>
  *   </li>
  *   <li>Initiates logout</li>
  *   <li>Waits until logout is complete</li>
  *   <li>Cleans up and exits</li>
  * </ol>
  * 
  * @author Andrea Drane (ported/refactored from existing C++ tutorial code)
  * 
  * @since 1.0
  */
public class Tutorial_12 { 
	/**
	 * Info/Debug console output message prefix/identifier tag.
	 * Corresponds to class name.
	 * 
	 * @since 1.0
	 */
    public static final String MY_CLASS_TAG = "Tutorial_12";

	/**
	 * Index of the account name in the command line argument list.
	 * 
	 * @since 1.0
	 */
    public static final int ACCOUNT_NAME_IDX = 0;

	/**
	 * Index of the account password in the command line argument list.
	 * 
	 * @since 1.0
	 */
    public static final int ACCOUNT_PWORD_IDX = 1;
    
	/**
	 * Number of required arguments in the command line argument list.
	 * 
	 * @since 1.0
	 */
    public static final int REQ_ARG_CNT = 2;

    /**
	 * Number of <em>optional</em> arguments in the command line argument list.
	 * 
	 * @since 1.0
	 */
    public static final int OPT_ARG_CNT = 1;

	/**
	 * Index of the <em>optional</em> AppKeyPair PEM file pathname in
	 * the command line argument list, which is always last.
	 * 
	 * @since 1.0
	 */
    public static final int APP_KEY_PAIR_IDX = ((REQ_ARG_CNT + OPT_ARG_CNT) - 1);
    
	/**
	 * ParsePosition instance representing the beginning of a target string.
	 * 
	 * @since 1.0
	 */
    public static final ParsePosition ZERO_POS = new ParsePosition(0);

    private static AppKeyPairMgr myAppKeyPairMgr = new AppKeyPairMgr();
    private static MySession mySession = new MySession();
    private static XmlStrMgr myXmlStrMgr = new XmlStrMgr();

    /**
     * "Extraneous" instance of this tutorial so we can invoke our business logic
     * method from {@link #main(String[])} without having to declare it as being "static".
     * 
     * @since 1.0
     */
    private static Tutorial_12 myTutorial = new Tutorial_12(); 

	/**
	 * Main loop - Event History
	 * 
	 * @param args
	 * <ol>
	 *   <li>Name of the target Skype account.</li>
	 *   <li>Password for the target Skype account.</li>
     *   <li>Pathname of a PEM file.</li>
	 * </ol>
	 * 
	 * @since 1.0
	 */
	public static void main(String[] args) {

		if (args.length < REQ_ARG_CNT) {
			MySession.myConsole.printf("Usage is %s accountName accountPassword [appTokenPathname]%n%n", MY_CLASS_TAG);
			return;
		}
		if (args.length > (REQ_ARG_CNT + OPT_ARG_CNT)) {
			MySession.myConsole.printf("%s: Ignoring %d extraneous arguments.%n", MY_CLASS_TAG, (args.length - REQ_ARG_CNT));
		}

		// Ensure our certificate file name and contents are valid
		if (args.length > REQ_ARG_CNT) {
			// AppKeyPairMgrmethods will issue all appropriate status and/or error messages!
			if ((!myAppKeyPairMgr.resolveAppKeyPairPath(args[APP_KEY_PAIR_IDX])) ||
				(!myAppKeyPairMgr.isValidCertificate())) {
				return;
			}
		}
		else {
			if ((!myAppKeyPairMgr.resolveAppKeyPairPath()) ||
				(!myAppKeyPairMgr.isValidCertificate())) {
				return;
			}
		}

		MySession.myConsole.printf("%s: main - Creating session - Account = %s%n",
							MY_CLASS_TAG, args[ACCOUNT_NAME_IDX]);
		mySession.doCreateSession(MY_CLASS_TAG, args[ACCOUNT_NAME_IDX], myAppKeyPairMgr.getPemFilePathname());

		MySession.myConsole.printf("%s: main - Logging in w/ password %s%n",
				MY_CLASS_TAG, args[ACCOUNT_PWORD_IDX]);
		if (mySession.mySignInMgr.Login(MY_CLASS_TAG, mySession, args[ACCOUNT_PWORD_IDX])) {
			myTutorial.doEventHistory(mySession);
			mySession.mySignInMgr.Logout(MY_CLASS_TAG, mySession);
		}
		// SkypeKitListeners, SignInMgr, and MySession will have logged/written
		// all appropriate diagnostics if login is not successful

		MySession.myConsole.printf("%s: Cleaning up...%n", MY_CLASS_TAG);
		if (mySession != null) {
			mySession.doTearDownSession();
		}
		MySession.myConsole.printf("%s: Done!%n", MY_CLASS_TAG);
	}

	
	/**
	 * Handles event history.
	 * <ol>
	 * 	<li>Obtains an unfiltered list of all Conservations</li>
	 * 	<li>For each conversation:
	 * 	  <ol style="list-style-type:lower-alpha">
	 * 	    <li>Lists associated context messages</li>
	 * 	    <li>Lists associated unconsumed messages</li>
	 * 	  </ol>
	 *   </li>
	 * </ol>
	 * 
	 * @param mySession
	 *	Populated session object
	 * 
	 * @since 1.0
	 */
	void doEventHistory(MySession mySession) {

		Conversation[] myConversationList =
			mySession.mySkype.getConversationList(Conversation.ListType.REALLY_ALL_CONVERSATIONS);
        int conversationListCount = myConversationList.length;

        for (int i = 0; i < conversationListCount; i++) {
        	Conversation currConv = myConversationList[i];
    		MySession.myConsole.printf("%n%s: Processing Conversation \"%s\" (%d of %d)...%n",
    							MY_CLASS_TAG, currConv.getDisplayName(),
    							(i + 1), conversationListCount);
    		// Get messages after midnight 01 December, 2010 local time
    		// Replace timestamp argument with '0' to get only messages within the last 24 hours
    		Calendar calTimeStamp = Calendar.getInstance();
    		calTimeStamp.set(2010, Calendar.DECEMBER, 1, 0, 0, 0);
    		Long l_calTimeStamp = calTimeStamp.getTimeInMillis() / 1000L;
    		Conversation.GetLastMessagesResponse currConvMsgList = currConv.getLastMessages(l_calTimeStamp.intValue());
/*
 *     		Conversation.GetLastMessagesResponse currConvMsgList = currConv.GetLastMessages(0);
 */
    		MySession.myConsole.printf("%s: Rendering context messages... found ", MY_CLASS_TAG);
        	doRenderHistory(mySession, currConvMsgList.contextMessages);
    		MySession.myConsole.printf("%s: Rendering unconsumed messages... found ", MY_CLASS_TAG);
        	doRenderHistory(mySession, currConvMsgList.unconsumedMessages);
        }

        MySession.myConsole.printf("%n%s: Waiting for additional events...%nPress Enter to quit.%n%n", mySession.myTutorialTag);
		try {
			while (true) {
				int keyboardChar = System.in.read();
				// Some platforms think ENTER is 0x0D; some think it's 0x0A; still others think it's the two-character sequence 0x0D0A...
				if ((keyboardChar == 0x0D) || (keyboardChar == 0x0A)) {
					while (System.in.available() > 0) {
						System.in.read();
					}
					break;
				}
	   	   	}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Processes messages associated with a Conversation.
	 * Processes <em>only</em> messages of type:
	 * <ul>
	 *   <li>STARTED_LIVESESSION&#8212;lists details of in-coming and out-going calls</li>
	 *   <li>POSTED_VOICE_MESSAGE&#8212;lists details of in-coming and out-going voicemails</li>
	 *   <li>POSTED_SMS&#8212;lists details of out-going SMSs</li>
	 *   <li>REQUESTED_AUTH&#8212;lists Contact authorization requests</li>
	 *   <li>GRANTED_AUTH&#8212;lists Contacts granted authorization</li>
	 * </ul>
	 * 
	 * @param mySession
	 *	Populated session object
	 * @param myMessages
	 *	Array of message strings to process.
	 * 
	 * @since 1.0
	 */
	void doRenderHistory(MySession mySession, Message[] myMessages) {
		ParsePosition s_pos;
		ParsePosition e_pos;

		Date dateTimeStamp;
        DateFormat dateFmt = DateFormat.getDateTimeInstance();
        
        String author;
        String bodyXml;
        int bodyXmlLength;
        
/*
 *         myXmlStrMgr.setVerboseDebug(true);
 */

		int msgCount = myMessages.length;
		Message currMsg;
		Message.Type currMsgType;
		MySession.myConsole.printf("%d ...%n", msgCount);
		for (int i = 0; i < msgCount; i++) {
			currMsg = myMessages[i];
			currMsgType = currMsg.getType();
			switch (currMsgType) {
            case STARTED_LIVE_SESSION:
            	MySession.myConsole.printf("%nProcessing message of type %s%n", currMsgType.toString());
                // Message.Property.P_AUTHOR tells who initiated the call.
                author = currMsg.getAuthor();

                // For duration we unfortunately have to parse the XML
                // The duration we're interested in is
                // <part identity="&me">%n...<duration>x</duration>...
                //
                // Real implementation should use a proper XML-parser here!
                StringBuffer partTag = new StringBuffer("<part identity=\"");
                partTag.append(mySession.myAccountName + "\">");
                s_pos = myXmlStrMgr.getXmlSubStrPos(currMsg.getBodyXml(),
                									partTag.toString(), ZERO_POS);
                if (s_pos == null) {
            		MySession.myConsole.printf("%s: Could not find \"%s\" in xmlDoc%n%s%n%nSkipping...%n%n",
            				MY_CLASS_TAG, partTag.toString(),
            				currMsg.getBodyXml());
            		break;
                }

                int duration =
                	myXmlStrMgr.getXmlValueNum(currMsg.getBodyXml(),
                        						"<duration>", s_pos);
 
                // Ditto for counting how many parts the body has...
                int num_parts = 0;
                s_pos.setIndex(0);
                do {
                    e_pos = myXmlStrMgr.getXmlSubStrPos(currMsg.getBodyXml(),
                    									"<part ", s_pos);
                    if (e_pos != null) {
                    	num_parts++;
                    	s_pos.setIndex(e_pos.getIndex());
                    }
                }
                while (e_pos != null);

                // Get timestamp -- it's in seconds, and the Date constructor needs milliseconds!
                dateTimeStamp = new Date((currMsg.getTimestamp() * 1000L));
                MySession.myConsole.printf("[%s] ", dateFmt.format(dateTimeStamp));
                // Last part is to fetch message reason
                String reason = currMsg.getReason();
                
                if (author.equals(mySession.myAccountName)) {
                    // I initiated the call
                    MySession.myConsole.print("outgoing call to ");
                }
                else {
                    // Somebody else called me
                    MySession.myConsole.print("incoming call from ");
                }
                
                // List identities
                doListIdentities(currMsg, (Sms)null); 

                if (duration >= 0) {
                    MySession.myConsole.printf("duration %d seconds", duration);
                }
                else if (num_parts > 1) {
                    if (reason.equals("manual")) {
                        MySession.myConsole.printf("refused");
                    }
                    else {
                        MySession.myConsole.printf("failed (%s)", reason);
                    }
                }
                else {
                    MySession.myConsole.printf("missed");
                }

                MySession.myConsole.printf(" (%d parts).%n", num_parts);
                break;

            case POSTED_VOICE_MESSAGE:
            	MySession.myConsole.printf("%nProcessing message of type %s%n", currMsgType.toString());
                author = currMsg.getAuthor();
                // XML parsing again...
                bodyXml = currMsg.getBodyXml();
                bodyXmlLength = myXmlStrMgr.getXmlValueNum(bodyXml,	"<length>", ZERO_POS);
                // Get timestamp -- it's in seconds, and the Date constructor needs milliseconds!
                dateTimeStamp = new Date((currMsg.getTimestamp() * 1000L));
                MySession.myConsole.printf("[%s] ", dateFmt.format(dateTimeStamp));
                if (author.equals(mySession.myAccountName)) {
                    // I initiated the call
                    MySession.myConsole.print("Sent voicemail to ");
                }
                else {
                    // Somebody else called me
                    MySession.myConsole.print("Got voicemail from ");
                }
                // List identities
                doListIdentities(currMsg, (Sms)null); 
                MySession.myConsole.printf("duration %d%n",bodyXmlLength);
                break;
                
            case POSTED_SMS:
            	Sms mySms = currMsg.getSms();
            	Sms.Status mySmsStatus = mySms.getStatus();
                String statusString = null;

                author = currMsg.getAuthor();

                dateTimeStamp = new Date((currMsg.getTimestamp() * 1000L));
                MySession.myConsole.printf("[%s] ", dateFmt.format(dateTimeStamp));
                if (author.equals(mySession.myAccountName)) {
                  	// I initiated the SMS
                   	MySession.myConsole.print("Sent SMS to: ");
                   	doListIdentities((Message)null, mySms);
                }
                else {
                  	// Somebody else texted me
                   	MySession.myConsole.printf("Got SMS from %s%n", author);
                }

                MySession.myConsole.printf("%nText: %s%n",mySms.getBody());
 
/*
 *              MySession.myConsole.printf("%nText: %s%n",currMsg.getBodyXml());
 */
                
                switch (mySmsStatus) {
                case DELIVERED:
                 	statusString = "Delivered";
                  	break;
                case SOME_TARGETS_FAILED:
                   	statusString = "Some targets failed";
                   	break;
                case FAILED:
                   	statusString = "Failed";
                   	break;
                default:
                   	statusString = mySmsStatus.toString();
                    break;
                }
                MySession.myConsole.printf("Status: %s%n", statusString);
                    
                if ((mySmsStatus == Sms.Status.SOME_TARGETS_FAILED) || (mySmsStatus == Sms.Status.FAILED)) {
                	Sms.FailureReason failureReason = mySms.getFailureReason();
                	String failureString;
                    switch (failureReason) {
                    case INSUFFICIENT_FUNDS:
                    	failureString = "Insufficient funds";
                    	break;
                    case SERVER_CONNECT_FAILED:
                    	failureString = "Server connect failed";
                    	break;
                    case INVALID_CONFIRMATION_CODE:
                    	failureString = "Invalid confirmation code";
                    	break;
            		case USER_BLOCKED:
            			failureString = "User blocked";
                    	break;
                    case NODE_BLOCKED:
                    	failureString = "Node blocked";
                    	break;
                    case IP_BLOCKED:
                    	failureString = "IP blocked";
                    	break;
            		case NO_SMS_CAPABILITY:
            			failureString = "No SMS capability";
                    	break;
            		case NO_SENDERID_CAPABILITY:
            			failureString = "No Sender ID capability";
                    	break;
                    case MISC_ERROR:
                    	failureString = "Misc. error";
                    	break;
                    default:
                    	failureString = failureReason.toString();
                    	break;
                    }
                    MySession.myConsole.printf("Failure reason = %s%n", failureString);
            	}
            	break;

            case REQUESTED_AUTH:
            	MySession.myConsole.printf("%nProcessing message of type %s%n", currMsgType.toString());
            // Please note that REQUESTED_AUTH is not used to request authorization
            // ALERT is used for that. REQUESTED_AUTH is used only for history
                author = currMsg.getAuthor();
                // Get timestamp -- it's in seconds, and the Date constructor needs milliseconds!
                dateTimeStamp = new Date((currMsg.getTimestamp() * 1000L));
                MySession.myConsole.printf("[%s] ", dateFmt.format(dateTimeStamp));
                MySession.myConsole.printf("Authorization request from %s to ", author);
                // List identities
                doListIdentities(currMsg, (Sms)null);
                MySession.myConsole.println("");
                break;

            case GRANTED_AUTH:
            	MySession.myConsole.printf("%nProcessing message of type %s%n", currMsgType.toString());
                author = currMsg.getAuthor();
                // Get timestamp -- it's in seconds, and the Date constructor needs milliseconds!
                dateTimeStamp = new Date((currMsg.getTimestamp() * 1000L));
                MySession.myConsole.printf("[%s] ", dateFmt.format(dateTimeStamp));
                MySession.myConsole.printf("%s granted authorization to ", author);
                // List identities
                doListIdentities(currMsg, (Sms)null);
                MySession.myConsole.println("");
                break;

            default:
             	MySession.myConsole.printf("%nIgnoring message of type %s%n", currMsgType.toString());
            	break;
			}
        }
	}


	/**
	 * Writes identities associated with a Message or targets associated with an SMS to the console as a single string.
	 * <br /><br />
	 * If the target's <code>P_IDENTITIES</code> or <code>P_TARGET_NUMBERS</code> property is empty,
	 * writes <code>--NONE--</code>. Otherwise, writes the individual identity/target number values
	 * <em>separated by commas</em>.
	 * <br /><br />
	 * Makes no assumptions about any preceding characters already written to the console.
	 * Terminates the string with a colon followed by a space ("code>": "</code>). Does
	 * <em>not</em> include any carriage return/line feed!
	 * <br /><br />
	 * For example:
	 * <pre>
	 *     (empty)     => --NONE--
	 *                    ********
	 *     abc         => abc:
	 *                    *****
	 *     abc def ghi => abc, def, ghi:
	 *                    ***************
	 * </pre>
	 * <br><br>
	 * One of currMsg and currSms must be non-null!
	 * 
	 * @param currMsg
	 *	Target Message instance (if formatting a message identity list).
	 * @param currSms
	 *	Target Sms instance (if formatting an SMS target number list).
	 * 
	 * @since 1.0
	 */
	void doListIdentities(Message currMsg, Sms currSms) {
		String[] identities = null;
		String listTermination = null;
        
        if (currMsg != null) {
        	identities = currMsg.getIdentities().split(" ");
        	listTermination = ": ";
        }
        else if (currSms != null) {
        	identities = currSms.getTargetNumbers().split(" ");
        	listTermination = "";
        }
        else {
        	MySession.myConsole.println("doListIdentities: both arguments are null?!?"); 
        	return;
        }
        
		int i = 0;
        int j = identities.length;
        if (j == 0) {
        	MySession.myConsole.printf("--NONE--%s", listTermination);
        	return;
        }

        while (--j >= 0) {
        	MySession.myConsole.printf("\"%s\"", identities[i++]); 
        	if (j > 0) {
        		MySession.myConsole.print(", ");
        	}
        }

        MySession.myConsole.printf("%s", listTermination);
	}

}
