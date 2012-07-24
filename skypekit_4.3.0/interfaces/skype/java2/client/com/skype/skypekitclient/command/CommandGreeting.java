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

package com.skype.skypekitclient.command;


import com.skype.api.Voicemail;
import com.skype.skypekitclient.SkypekitClient;
import com.skype.skypekitclient.utils.Ask;

/**
 *
 */
public class CommandGreeting extends ClientCommandInterface {
//    private static final String TAG = "CommandGreeting";

    private SkypekitClient skClient;

	protected CommandGreeting(SkypekitClient skypekitClient) {
		super();
		skClient = skypekitClient;
	}
    
	@Override
	public String getName() { return "Greeting"; }

	public void executeHelp()
	{
        skClient.out("\n[Greeting] - G\n" +
                "\tp - play\n" +
                "\tr - start record\n" +
                "\ts - stop record\n" +
                "\tz - reset\n"
        );
	}

	
	public void execute_p()  // play voicemail greeting
	{
        Voicemail greeting = skClient.skype.getGreeting(Ask.ask("skypename: "));
        if (greeting == null) {
            skClient.error("no greeting message");
            return;
        }
        greeting.startPlayback();
	}

	public void execute_r()	// start recording voicemail greeting
	{
        Voicemail greeting;
	    if (skClient.account != null) {
            if ((greeting = skClient.skype.getGreeting(skClient.account.getSkypeName())) == null) {
                skClient.error("no greeting message");
                return;
            }
            greeting.startRecording();
        }
	}

	public void execute_s()	// stop recording voicemail greeting
	{
        Voicemail greeting;
	    if (skClient.account != null) {
            if ((greeting = skClient.skype.getGreeting(skClient.account.getSkypeName())) == null) {
                skClient.error("no greeting message");
                return;
            }

            Voicemail.Status status = greeting.getStatus();
            if (status == Voicemail.Status.RECORDING) {
                    greeting.stopRecording();
            } else {
                    skClient.error("not recording\n");       
            }
        }
	}

	public void execute_z()	// Delete voicemail greeting
	{
        Voicemail greeting;
	    if (skClient.account != null) {
            if ((greeting = skClient.skype.getGreeting(skClient.account.getSkypeName())) == null) {
                skClient.error("no greeting message");
                return;
            }
            greeting.delete();
        }
	}

}
