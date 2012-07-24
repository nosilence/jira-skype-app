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

import java.util.ArrayList;

import com.skype.api.Conversation;
import com.skype.api.Conversation.LocalLiveStatus;
import com.skype.api.Participant;
import com.skype.skypekitclient.SkypekitClient;
import com.skype.skypekitclient.utils.Ask;

/**
 *
 */
public class CommandConversationConference extends ClientCommandInterface {
//    private static final String TAG = "CommandConversationConference";

    private SkypekitClient skClient;

	protected CommandConversationConference(SkypekitClient skypekitClient) {
		super();
		skClient = skypekitClient;
	}
    
	@Override
	public String getName() { return "ConversationConference"; }

	public void executeHelp()
	{
        skClient.out("\n[CONVERSATION CONFERENCE] - M\n" +
                "\tMs - spawn a conf\n" +
                "\tMp - add a participant\n" +
                "\tMm - mute/unmute\n" +
                "\tMh - hold/unhold\n" +
                "\tMd - dtmf\n" +
                "\tMj - join live conversation"
        );
	}
	
	
	public void execute_s()	// spawn a conf
	{
        if (skClient.currentConversation != null) {
            ArrayList<String> participants = new ArrayList<String>();
            
            String participant;
            while (true) {
                participant = Ask.ask("Add participant:");
                participants.add(participant);
                if (!Ask.ask_yesno("Add another?")) break;
            }

            Conversation conference = skClient.currentConversation.spawnConference(participants.toArray(new String[participants.size()]));
            if (conference != null) skClient.currentConversation = conference;
        }
	}

	public void execute_p()	// add a participant
	{
        if (skClient.currentConversation != null) {
            Conversation.Type ctype = skClient.currentConversation.getType();
            if (ctype == Conversation.Type.DIALOG) {
                skClient.error("Conversation is not a conference, choose 'ms - spawn a conf' instead");
                return;
            }

            ArrayList<String> participants = new ArrayList<String>();
            String participant;
            while (true) {
                participant = Ask.ask("Add participant:");
                participants.add(participant);
                if (!Ask.ask_yesno("Add another?")) break;
            }

            skClient.currentConversation.addConsumers(participants.toArray(new String[participants.size()]));
        }
	}

	public void execute_m()	// mute/unmute
	{
        if (skClient.currentConversation != null) {
            if (!skClient.currentConversation.getLiveIsMuted()) {
            	skClient.currentConversation.muteMyMicrophone();
            } else {
            	skClient.currentConversation.unmuteMyMicrophone();
            }
        }
	}

	public void execute_h()	// hold/unhold
	{
        if (skClient.currentConversation != null) {
            if (skClient.currentConversation.getLocalLiveStatus() 
            		!= LocalLiveStatus.ON_HOLD_LOCALLY) {
            	skClient.currentConversation.holdMyLiveSession();
            } else {
            	skClient.currentConversation.resumeMyLiveSession();
            }
        }
	}

	public void execute_d()	// dtmf
	{
        if (skClient.currentConversation != null) {
            String dtmfstr = Ask.ask("Enter dtmf (1,2,3,4,5,6,7,8,9,0,*,#):");
            Participant.Dtmf dtmf;
            if (dtmfstr == "*") {
            	dtmf = Participant.Dtmf.DTMF_STAR;
            } else if (dtmfstr == "#") {
            	dtmf = Participant.Dtmf.DTMF_POUND;
            } else {
            	dtmf = Participant.Dtmf.valueOf(dtmfstr);
            }
            skClient.currentConversation.sendDtmf(dtmf, 260); 
        }
	}

	public void execute_j()	// join live convesation
	{
        if (skClient.currentConversation != null) {
        	skClient.currentConversation.joinLiveSession("");
        }
	}

}
