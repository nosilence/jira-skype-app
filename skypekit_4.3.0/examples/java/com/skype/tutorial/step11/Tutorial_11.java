/**
 * Copyright (C) 2010 - 2012 Skype
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

package com.skype.tutorial.step11;

import java.io.*;

import com.skype.api.Skype;

import com.skype.tutorial.appkeypair.AppKeyPairMgr;
import com.skype.tutorial.util.MySession;

/**
 * Getting Started With SkypeKit: Tutorial Application, Step 11.
 *
 * This example illustrates a simple SkypeKit-based program that:
 * <ol>
 *   <li>Takes a Skype Name, password, target Contact information, and
 *       optional AppKeyPair PEM file pathname as command-line arguments</li>
 *   <li>Initiates login for that user</li>
 *   <li>Waits until the login process is complete</li>
 *	 <li>Uses Skype application to application (app2app) features to make a datagram-based
 *	     command-line chat between two instances of the same SkypeKit client</li>
 *   <li>Initiates logout</li>
 *   <li>Waits until logout is complete</li>
 *   <li>Cleans up and exits</li>
 * </ol>
 * 
 * @author Andrea Drane (ported from existing C++ tutorial code)
 * 
 * @since 1.0
 */
public class Tutorial_11 { 
	/**
	 * The name used to create/identify our chat. Each participating Skype client must use this
	 * name to create/connect to the chat.
	 * 
	 * @since 1.0
	 */
	static final String appName = "TestApp1";
	
	/**
	 * Maximum number of characters to accumulate before sending them off to
	 * whoever we're chatting with. Used both to size our buffers and
	 * as a loop limit.
	 * 
	 * @since 1.0
	 */
	static final int TXT_CHUNK_SZ = 80;

	/**
	 * Command "string" denoting exit from app2app chat.
	 * Used <i>after</i> stream becomes available.
	 * 
	 * @since 1.0
	 */
	static final String QUIT_CMD_STR = "q";

	/**
	 * Info/Debug console output message prefix/identifier tag.
	 * Corresponds to class name.
	 * 
	 * @since 1.0
	 */
    public static final String MY_CLASS_TAG = "Tutorial_11";

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
	 * Index of the Contact name in the command line argument list.
	 * 
	 * @since 1.0
	 */
    public static final int CONTACT_NAME_IDX = 2;

	/**
	 * Number of required arguments in the command line argument list.
	 * 
	 * @since 1.0
	 */
    public static final int REQ_ARG_CNT = 3;

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
	 * Target Contact name.
	 * @since 1.0
	 */
    private static String myContactName;
    
	private BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));


    private static AppKeyPairMgr myAppKeyPairMgr = new AppKeyPairMgr();
    private static MySession mySession = new MySession();

    /**
     * "Extraneous" instance of this tutorial so we can invoke our business logic
     * method from {@link #main(String[])} without having to declare it as being "static".
     * 
     * @since 1.0
     */
    private static Tutorial_11 myTutorial = new Tutorial_11(); 

	/**
	 * Main loop - App2App Datagram
	 * 
	 * @param args
	 * <ol>
	 *   <li>Name of the target Skype account.</li>
	 *   <li>Password for the target Skype account.</li>
	 *   <li>Skype Name of the target chat participant.</li>
     *   <li>Pathname of a PEM file.</li>
	 * </ol>
	 * 
	 * @since 1.0
	 */
	public static void main(String[] args) {

		if (args.length < REQ_ARG_CNT) {
			MySession.myConsole.printf("Usage is %s accountName accountPassword contactName [appTokenPathname]%n%n", MY_CLASS_TAG);
			return;
		}
		if (args.length > (REQ_ARG_CNT + OPT_ARG_CNT)) {
			MySession.myConsole.printf("%s: Ignoring %d extraneous arguments.%n", MY_CLASS_TAG, (args.length - REQ_ARG_CNT));
		}

	    myContactName = args[CONTACT_NAME_IDX].toString();
		MySession.myConsole.printf("%s: Contact name = %s%n", MY_CLASS_TAG, myContactName);

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
			myTutorial.doApp2AppDatagram(mySession, myContactName);
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
	 * Make a datagram-based command-line chat between two instances of the same SkypeKit client.
	 * 
	 * @param mySession
	 *	Populated session object
	 *	@param myContactName
	 *	 Skype Name of person to connect with.
	 * 
   	 * @since 1.0
   	 */
   	void doApp2AppDatagram(MySession mySession, String myContactName) {
   		Skype.App2AppGetConnectableUsersResponse appContacts;
   		String chatText = "";
  		
   	    // Application creation should pretty much always be successful - unless there
   	    // already is an application with the same name.
  		if (!mySession.mySkype.app2AppCreate(appName)) {
  			MySession.myConsole.printf("%s: Could not create App2App %s%n", MY_CLASS_TAG, appName);
  			return;
  		}
  		MySession.myConsole.printf("%s: Created App2App application %s%n", MY_CLASS_TAG, appName);

   	    // The App2AppConnect connect result does -NOT- mean the connection was actually
   	    // established. It will return Ok even if the remote party was offline.
   	    // The actual connection will be only established when both parties have fired
   	    // app2AppConnect with the same application name, at each other. At that point,
   	    // onApp2AppStreamListChange event will fire and you can start exchanging data.
   	    if (!mySession.mySkype.app2AppConnect(appName, myContactName)) {
			MySession.myConsole.printf("%s: Could not connect to application %s for Contact %s%n", MY_CLASS_TAG, appName, myContactName);
			return;
   	    }
		MySession.myConsole.printf("%s: Connected to application %s for Contact %s%n", MY_CLASS_TAG, appName, myContactName);

  		mySession.quitDatagram = false;
   	    MySession.myConsole.println("Waiting for app2app connection to come up.");
   	    do {
   	   	    // app2AppGetConnectableUsers returns a list of currently online contacts.
   	   	    // It does not return a list of contacts who have an app2app application running.
   	   	    // There is currently no way of querying whether an application has been launched
   	   	    // on the remote side - other than trying to connect to the remote side and waiting
   	   	    // for timeout. NB! if you use app2AppGetConnectableUsers immediately after login -
   	   	    // then the online presence of your contact list has not yet updated itself - so this
   	   	    // method will most likely return either an empty list or a list with echo123 in it.

   	   	    appContacts = mySession.mySkype.app2AppGetConnectableUsers(appName);
   	   	    if (!appContacts.result) {
   	   	    	MySession.myConsole.printf("app2AppGetConnectableUsers failed for appName %s%n.", appName);
   	   	    }
   	   	    else {
   	   	    	int i;
   	   	    	int j = appContacts.users.length;
   	   	    	MySession.myConsole.printf("app2AppGetConnectableUsers returned %d users.%n", j);
   	   	    	for (i = 0; i < j; i++) {
   	   	    		MySession.myConsole.printf("%d - %s%n", (i + 1), appContacts.users[i]);
   	   	    	}
   	   	    }
   	   	    
   	   	    if (!mySession.appConnected) {
   	   	    	MySession.myConsole.printf("%s: Wait for a connection, t", mySession.myTutorialTag);
   	   	    }
   	   	    else {
   	   	    	MySession.myConsole.printf("%s: T", mySession.myTutorialTag);
   	   	    }
   	   	    MySession.myConsole.printf("ype your chat message, then press Enter.%n\t\tType q to quit.%n%n");
   	    	try {
   	    		chatText = keyboardReader.readLine();
   	    	}
   	    	catch (IOException e) {
  				// TODO Auto-generated catch block
   				e.printStackTrace();
   	    	}
   			MySession.myConsole.printf("%n%s: Captured chat message of %d characters:%n\t%s%n",
   					mySession.myTutorialTag,  chatText.length(), chatText);

   	    	if (chatText.compareToIgnoreCase(QUIT_CMD_STR) == 0) {
   	    		mySession.quitDatagram = true;
   	    	}
   	    	else if (mySession.streamName.length() == 0) {
   	    		MySession.myConsole.println("No app2app streams connected; datagram not sent.");
   	    	}
   	    	else if (chatText.length() != 0) {
   	    		MySession.myConsole.printf("Sending datagram... %d bytes%n\t%s%n", chatText.length(), chatText.toString());
   	    		mySession.mySkype.app2AppDatagram(appName, mySession.streamName, chatText.getBytes());
   	    	}
   	    } while (!mySession.quitDatagram);

   	    MySession.myConsole.printf("Disconnecting and deleting %s%n", appName);
   	    mySession.mySkype.app2AppDisconnect(appName, mySession.streamName);
   	    mySession.mySkype.app2AppDelete(appName);
  	}
}
