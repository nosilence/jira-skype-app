/**
 * Copyright (C) 2012 Skype
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

package com.skype.tutorial.step13;

import java.io.IOException;
import java.util.ArrayList;

import com.skype.api.Conversation;
import com.skype.api.Sms;
import com.skype.api.Skype;

import com.skype.tutorial.appkeypair.AppKeyPairMgr;
import com.skype.tutorial.util.MySession;
import com.skype.tutorial.util.SignInMgr;

/**
 * Getting Started With SkypeKit: Tutorial Application, Step 13.
 *
 * This example illustrates a simple SkypeKit-based program that:
 * <ol>
 *   <li>Takes a Skype Name, a password, up to four target phone numbers, and
 *       an optional AppKeyPair PEM file pathname as command-line arguments</li>
 *   <li>Initiates login for that user</li>
 *   <li>Waits until the login process is complete</li>
 *   <li>Attempts to send an SMS text message to the specified recipient(s):
 *     <ul>
 *       <li>display the initial Skype Credit balance</li>
 *       <li>normalize the target numbers</li>
 *       <li>analyze the target numbers and determine their price</li>
 *       <li>prompt to send the SMS and incur the specified total cost</li>
 *       <li>send the SMS and wait for delivery confirmation</li>
 *       <li>display the updated Skype Credit balance</li>
 *     </ul>
 *   </li>
 *   <li>Initiates logout when the SMS completes or fails to send</li>
 *   <li>Waits until logout is complete</li>
 *   <li>Cleans up and exits</li>
 * </ol>
 * 
 * @author Andrea Drane (ported from existing C++ tutorial code)
 * 
 * @since 1.0
 */
public class Tutorial_13 { 
    /**
	 * Info/Debug console output message prefix/identifier tag.
	 * Corresponds to class name.
	 * 
	 * @since 1.0
	 */
    public static final String MY_TUTORIAL_13_CLASS_TAG = "Tutorial_13";

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
	 * Index of the first (required) call target argument in the command line argument list.
	 * 
	 * @since 1.0
	 */
    public static final int FIRST_TGT_ARG_IDX = (ACCOUNT_PWORD_IDX + 1);

    /**
	 * Number of required arguments in the command line argument list.
	 * This includes the minimum of <em>one</em> SMS targets (Skype Names). 
	 * 
	 * @since 1.0
	 */
    public static final int REQ_ARG_CNT = (FIRST_TGT_ARG_IDX + 1);

    /**
	 * Key introducing/identifying the pathname of an AppKeyPair file
	 * among the optional command line arguments.
	 * <p>
	 *   <b>Any optional AppKeyPair PEM file pathname <em>must</em> be prefixed with this key string to
	 *      distinguish it from any call target.</b>
	 * </p>
	 * <p>
	 *   For example:
	 *   <pre>
	 *       Tutorial_13 myAccountName myAccountPword smsTgt1 smsTgt2 ... -appKeyPair:/MyHome/MyCredentials/MyCert.pem
	 *   </pre>
	 * </p>
	 * 
	 * @since 1.1
	 */
    public static final String APP_KEY_PAIR_OPT_KEY = "-appKeyPair:";


	/**
	 * Minimum number of SMS recipients (included in REQ_ARG_CNT).
	 * 
	 * @since 1.0
	 */
	static final int SMS_RECIPIENTS_MIN = 1;

	/**
	 * Maximum number of SMS recipients (arbitrary).
	 * 
	 * @since 1.0
	 */
	static final int SMS_RECIPIENTS_MAX = 4;

    /**
	 * Number of <em>optional</em> arguments in the command line argument list
	 * (Optional SMS targets plus the AppKeyPair path).
	 * 
	 * @since 1.0
	 */
    public static final int OPT_ARG_CNT = ((SMS_RECIPIENTS_MAX - SMS_RECIPIENTS_MIN) + 1);

	/**
	 * Maximum size of an SMS text message, <i>i.e.</i>, the maximum number of
	 * <i>7-bit characters</i> the user can type before sending the text message.
	 * 
	 * @since 1.0
	 */
	static final int SMS_CHARS_MAX = 160;
	
    /**
  	 * SMS send notification polling limit (iterations).
  	 * Results in a maximum total delay of <code>SMS_SEND_TIMEOUT_LIMIT * SigInMgr.DELAY_INTERVAL</code>
  	 * <em>milliseconds</em> before giving up and exiting.
  	 * 
  	 * @since 1.0
  	 */
    public static final int SMS_SEND_TIMEOUT_LIMIT = 30;

    /**
  	 * SMS delivery/failure notification polling limit (iterations).
  	 * Results in a maximum total delay of <code>SMS_SEND_TIMEOUT_LIMIT * SigInMgr.DELAY_INTERVAL</code>
  	 * <em>milliseconds</em> before giving up and exiting.
  	 * 
  	 * @since 1.0
  	 */
    public static final int SMS_DELIVERY_FAILURE_LIMIT = 180;

	
    /**
	 * SMS targets (PSTN).
	 * 
	 * @since 1.0
	 */
    private static ArrayList<String> mySmsTargets = new ArrayList<String>(SMS_RECIPIENTS_MAX);

    private static AppKeyPairMgr myAppKeyPairMgr = new AppKeyPairMgr();
    private static MySession mySession = new MySession();

    /**
     * "Extraneous" instance of this tutorial so we can invoke our business logic
     * method from {@link #main(String[])} without having to declare it as being "static".
     * 
     * @since 1.0
     */
    private static Tutorial_13 myTutorial = new Tutorial_13(); 

	/**
	 * Main loop
	 * 
	 * @param args
	 * <ol>
	 *   <li>Name of the target Skype account.</li>
	 *   <li>Password for the target Skype account.</li>
	 *   <li>PSTN number of the of the person to text.</li>
     *   <li>Optional pathname of an AppKeyPair PEM file.</li>
	 * </ol>
	 * 
	 * @since 1.0
	 */
	public static void main(String[] args) {

	    boolean foundAppKeyPairOptKey = false;

	    if (args.length < REQ_ARG_CNT) {
			MySession.myConsole.printf("Usage is %s accountName accountPassword callTarget1 ... callTargetN [%s:appKeyPairPathname]%n%n",
					MY_TUTORIAL_13_CLASS_TAG, APP_KEY_PAIR_OPT_KEY);
			return;
		}
		MySession.myConsole.printf("%s: main - Parsing command line arguments:%n", MY_TUTORIAL_13_CLASS_TAG);
		int i = args.length - FIRST_TGT_ARG_IDX;	// Call target names (and any AppToken file
		int j = FIRST_TGT_ARG_IDX;					// pathname) follow...
		int k = 1;
		String argStr;
		while (--i >= 0) {
			argStr = args[j++];
			if (argStr.startsWith(APP_KEY_PAIR_OPT_KEY)) {
				foundAppKeyPairOptKey = true;
				// AppKeyPairMgrmethods will issue all appropriate status and/or error messages!
				if ((!myAppKeyPairMgr.resolveAppKeyPairPath(argStr.substring(APP_KEY_PAIR_OPT_KEY.length())) ||
					(!myAppKeyPairMgr.isValidCertificate()))) {
					return;
				}
			}
			else {
				MySession.myConsole.printf("\tCall target name %d: %s%n", k++, argStr);
				mySmsTargets.add(argStr);
			}
		}
		if (!foundAppKeyPairOptKey) {
			// AppKeyPairMgrmethods will issue all appropriate status and/or error messages!
			if ((!myAppKeyPairMgr.resolveAppKeyPairPath()) ||
				(!myAppKeyPairMgr.isValidCertificate())) {
				return;
			}
		}

		MySession.myConsole.printf("%s: main - Creating session - Account = %s%n",
							MY_TUTORIAL_13_CLASS_TAG, args[ACCOUNT_NAME_IDX]);
		mySession.doCreateSession(MY_TUTORIAL_13_CLASS_TAG, args[ACCOUNT_NAME_IDX], myAppKeyPairMgr.getPemFilePathname());

		MySession.myConsole.printf("%s: main - Logging in w/ password %s%n",
				MY_TUTORIAL_13_CLASS_TAG, args[ACCOUNT_PWORD_IDX]);
		if (mySession.mySignInMgr.Login(MY_TUTORIAL_13_CLASS_TAG, mySession, args[ACCOUNT_PWORD_IDX])) {
			double actualBalance = mySession.myAccount.getSkypeoutBalance() / StrictMath.pow(10, mySession.myAccount.getSkypeoutPrecision());
			MySession.myConsole.printf("%nCurrent SkypeOut balance is: %.3f %s   [%d / (10 ^ %d)]", 
					actualBalance, mySession.myAccount.getSkypeoutBalanceCurrency(),
					mySession.myAccount.getSkypeoutBalance(), mySession.myAccount.getSkypeoutPrecision());
			myTutorial.doSendSms(mySession, mySmsTargets);
			MySession.myConsole.printf("%s: main - Logging out...%n", MY_TUTORIAL_13_CLASS_TAG);
			mySession.mySignInMgr.Logout(MY_TUTORIAL_13_CLASS_TAG, mySession);
		}
		// SkypeKitListeners, SignInMgr, and MySession will have logged/written
		// all appropriate diagnostics if login is not successful
		
		MySession.myConsole.printf("%s: Cleaning up...%n", MY_TUTORIAL_13_CLASS_TAG);
		if (mySession != null) {
			mySession.doTearDownSession();
		}
		MySession.myConsole.printf("%s: Done!%n", MY_TUTORIAL_13_CLASS_TAG);
	}


	/**
	 * Send an SMS text message to one or more recipients.
	 * <ol>
	 *   <li>Normalize the list of my PSTN numbers specified on the command line.</li>
	 *   <li>Create an SMS instance, and capture/set its actual message text.</li>
	 *   <li>Create a Conversation instance that includes each of the recipients</li>
	 *   <li>Post the SMS using that Conversation instance</li>
	 *   <li>Wait until the SMS post succeeds or fails (which could take several minutes...)</li>
	 * </ol>
	 * 
	 * @param mySession
	 *	Populated session object
	 * @param pstn
	 * 	The PSTN numbers of the people to SMS.
	 *  
	 * @since 1.0
	 */
	void doSendSms(MySession mySession, ArrayList<String> pstn) {
		int	i;
		int j;
		int numberOfRecipients;

		mySession.smsReadyToSend = 0;
 		mySession.smsWasSent = false;
	    mySession.smsHasFailed = false;
	    mySession.smsWasDelivered = false;

	    Sms mySms = mySession.mySkype.createOutgoingSms();

 		// Set the message body in our SMS
 		if (!composeSmsMessage(mySms)) {
 			MySession.myConsole.println("Error! Could not compose SMS message.");
  			return;
 		}
 		
 		// Compose and set the target list in our SMS
		ArrayList<String> normalizedPstn = new ArrayList<String>(SMS_RECIPIENTS_MAX);
		Skype.NormalizeIdentityResponse nrmlResponse = null;

		// Ensure that the pstn argument contains valid contact identities
		numberOfRecipients = 0;
		j = pstn.size();
		for (i = 0; i < j; i++) {
			nrmlResponse = mySession.getNormalizationStr(pstn.get(i));
			if (nrmlResponse.result != Skype.NormalizeResult.IDENTITY_OK) {
				MySession.myConsole.printf("\t...skipping %s%n", pstn.get(i));
				continue;
			}
			MySession.myConsole.printf("Adding recipient #%d: %s%n", (i + 1), nrmlResponse.normalized);
			normalizedPstn.add(nrmlResponse.normalized);
			numberOfRecipients++;
		}

/*
  		// Dump the list of successfully normalized PSTN numbers to the console
  		String [] normalizedPstnStrArray = normalizedPstn.toArray(new String[0]);
  		j = normalizedPstnStrArray.length;
 		MySession.myConsole.printf("Attempting to set %d (%d) targets:%n", j, numberOfRecipients);
 		for (i = 0; i < j; i++) {
 			MySession.myConsole.printf("\t%d: %s%n", (i + 1), normalizedPstnStrArray[i]);
 		}
 		MySession.myConsole.printf("%n%n", (Object [])null);
*/

		// Sms.setTargets will not fail when presented with an empty list, but
		// Conversation getConversationByParticipants will recognize a NullPointerException
		// when we get to it a bit further down the line...
 		if ((numberOfRecipients <= 0) || (!mySms.setTargets(normalizedPstn.toArray(new String[0])))) {
 	    	MySession.myConsole.println("Error! Could not set SMS targets.");
   			return;
 	    }

 		// smsReadyToSend gets set from SmsListener.onPropertyChange
	    i = 0;
		while ((i < SMS_SEND_TIMEOUT_LIMIT) &&
 			   (mySession.smsReadyToSend < numberOfRecipients)) {
 			try {
 				Thread.sleep(SignInMgr.DELAY_INTERVAL);
 			}
 			catch (InterruptedException e) {
 				// TODO Auto-generated catch block#
 				e.printStackTrace();
 				return;
 			}
 			MySession.myConsole.printf("\t%d...%n", i++);
 		}

 		MySession.myConsole.printf("%n%nPress ENTER to send SMS%n", (Object [])null);
 		int keyboardChar;
 		try {
 			while (true) {
				keyboardChar = System.in.read();
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
 		}

		// Send our Sms and wait for send confirmation
 		if (!sendSmsMessage(mySms, normalizedPstn.toArray(new String[0]))) {
 			// sendSmsMessage will have posted all error messages
  			return;
 		}

		// Loop until it's delivered -- to all recipients -- or fails
		i = 0;
		while ((i < SMS_DELIVERY_FAILURE_LIMIT) &&
			   (!mySession.smsHasFailed) && (!mySession.smsWasDelivered)) {
			try {
				Thread.sleep(SignInMgr.DELAY_INTERVAL);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			MySession.myConsole.printf("\t %d...%n", i++);
		}
		if (mySession.smsWasDelivered) {
			MySession.myConsole.println("SMS received by all recipients!");
		}
		else if (i == SMS_DELIVERY_FAILURE_LIMIT) {
			MySession.myConsole.printf("Notification polling period timed out!%nCould not confirm that SMS was delivered to all recipients.%n",
					(Object[])null);
		}
		else {
			MySession.myConsole.println("Error! Could not deliver SMS to all recipients.");
		}
	}
	
	/**
	 * Compose the SMS message to send.
	 * <ol>
	 *   <li>Captures up to the SMS maximum number of characters.</li>
	 *   <li>Set that captured text as the body of our SMS instance.</li>
	 *   <li>Display what was captured and the number of chunks used to the console.</li>
	 * </ol>
	 * 
	 * @param mySms
	 *	Target SMS instance.
	 *
	 * @since 1.0
	 */
	boolean composeSmsMessage(Sms mySms) {
		char[] msgBuffer = new char[(SMS_CHARS_MAX + 1)];
		int i = 0;
		int keyboardChar = 0x00;
		
		MySession.myConsole.printf("%n%nCompose your SMS text, then press ENTER%n", (Object [])null);
		try {
			while (i < SMS_CHARS_MAX) {
				keyboardChar = System.in.read();
				// Some platforms think ENTER is 0x0D; some think it's 0x0A; still others think it's the two-character sequence 0x0D0A...
				if ((keyboardChar == 0x0D) || (keyboardChar == 0x0A)) {
					while (System.in.available() > 0) {
						System.in.read();
					}
					break;
				}
				msgBuffer[i++] = (char)keyboardChar;
	   	   	}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return(false);
		}

		// End capturing chat text
		String smsString = new String(msgBuffer).trim();
/*
 	    MySession.myConsole.printf("Captured SMS text: %s (%d characters)%n",
	    		smsString, smsString.length());
 */
		MySession.myConsole.println();
		Sms.SetBodyResponse mySmsBodyResponse = mySms.setBody(smsString);
	    if (mySmsBodyResponse.result != Sms.SetBodyResult.BODY_OK)
	    {
	    	MySession.myConsole.println("Error! Could not set SMS body property.");
	    	return(false);
	    }

	    MySession.myConsole.printf("Captured %d characters; number of SMS chunks used: %d, free characters until next chunk: %d%n",
	    		smsString.length(), mySmsBodyResponse.chunks.length, mySmsBodyResponse.charsUntilNextChunk);

	    return(true);
	}
	
	/**
	 * Send the SMS message, and wait for confirmation.
	 * <ol>
	 *   <li>Create a Conversation instance that includes all recipients.</li>
	 *   <li>Post the Sms using that Conversation.</li>
	 *   <li>Wait a reasonable amount of time to determine if it succeeds or fails
	 *       (assume success on timeout).</li>
	 * </ol>
	 * 
	 * @param mySms
	 *	Target SMS instance.
	 *
	 * @param smsTargets
	 *	PSTN numbers to send the SMS to.
	 *
	 * @return
	 * <ul>
	 *   <li>true: Sms transferred to the server and sent to all recipient(s),
	 *       or polling timed out without a failure indication</li>
	 *   <li>false: Send to all recipients failed - reason written to the console</li>
	 * </ul>
	 *
	 * @since 1.0
	 */
	boolean sendSmsMessage(Sms mySms, String[] smsTargets) {
		int i = 0;
        Sms.Status smsStatus = null;

 		Conversation myConversation =
				mySession.mySkype.getConversationByParticipants(smsTargets, true, true);
		myConversation.postSms(mySms, "unused");

		// Loop until we can confirm that the send has completed, has failed, or our polling has timed out
		i = 0;
		while ((i < SMS_SEND_TIMEOUT_LIMIT) &&
				(!mySession.smsHasFailed) && (!mySession.smsWasSent)) {
			try {
				Thread.sleep(SignInMgr.DELAY_INTERVAL);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return(false);
			}
			MySession.myConsole.printf("\t %d...%n", i++);
		}
		if (mySession.smsWasSent) {
			MySession.myConsole.println("SMS sent!");
		}
		else if (i == SMS_SEND_TIMEOUT_LIMIT) {
			MySession.myConsole.printf("Notification polling period timed out!%nCould not confirm that SMS was sent to all recipients.%n",
					(Object[])null);
		}
		else {
        	Sms.FailureReason smsFailureReason = mySms.getFailureReason();
        	MySession.myConsole.printf("Error! Could not send SMS message: %s%n", smsFailureReason.toString());
			return(false);
		}
		MySession.myConsole.println();

		// Loop until we can confirm that delivery has completed, has failed, or our polling has timed out
        do {
			try {
				Thread.sleep(SignInMgr.DELAY_INTERVAL);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return(false);
			}
			MySession.myConsole.printf("\t%d...%n", i++);
			smsStatus = mySms.getStatus();
		} while ((smsStatus != Sms.Status.DELIVERED) && (smsStatus != Sms.Status.SOME_TARGETS_FAILED) && (smsStatus != Sms.Status.FAILED));

    	if (smsStatus != Sms.Status.DELIVERED) {
        	Sms.FailureReason smsFailureReason = mySms.getFailureReason();
        	MySession.myConsole.printf("Error! Could not send SMS message: %s%n", smsFailureReason.toString());
        	return(false);
        }
        
        return(true);
	}
}

