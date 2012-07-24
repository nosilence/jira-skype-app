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

import com.skype.api.Conversation;
import com.skype.api.Conversation.GetLastMessagesResponse;
import com.skype.api.Message;
import com.skype.api.Voicemail;
import com.skype.skypekitclient.SkypekitClient;
import com.skype.skypekitclient.utils.Ask;

/**
 *
 */
public class CommandVoiceMail extends ClientCommandInterface {
//    private static final String TAG = "CommandVoiceMail";

    private SkypekitClient skClient;

	protected CommandVoiceMail(SkypekitClient skypekitClient) {
		super();
		skClient = skypekitClient;
	}
    
	@Override
	public String getName() { return "VoiceMail"; }

	public void executeHelp()
	{
        skClient.out("\n[VOICE MAIL] - V\n" +
                "\tVv - start recording new voicemail\n" +
                "\tVV - end recording and send voicemail\n" +
                "\tVw - cancel voicemail recording/send\n" +
                "\tVp - play voicemails (first use 'cc' to select conversation)\n" +
                "\tVs - stop voicemail playback\n"
        );
	}


	public void execute_v()	// start recording new voicemail
    {
        if (skClient.currentConversation != null) {
        	skClient.currentConversation.startVoiceMessage();
        }
        else
            skClient.out("You are not logged in or there is no selected conversation (use cc)");

    }

    public void execute_V()	// end recording and send voicemail
    {
        Conversation conversation = skClient.currentConversation;
        if (conversation != null) {
            Voicemail vm = conversation.getActiveVoicemail();
            skClient.out("CommandVoiceMail.execute_V() - active_vm_id = " + (vm == null ? 0 : vm.getOid()));
            if (vm != null) {
                conversation.postVoiceMessage(vm, "empty body");
            }
            else 
            {
                skClient.out("Unable to get active vm"); 
            }
        }
        else
            skClient.out("You are not logged in or there is no selected conversation (use cc)");
    }

    public void execute_w()	// cancel voicemail recording/send
    {
        Conversation conversation = skClient.currentConversation;
        if (conversation != null) {
            Voicemail vm = conversation.getActiveVoicemail();
            skClient.out("CommandVoiceMail.execute_V() - active_vm_id = " + (vm == null ? 0 : vm.getOid()));
            if (vm != null) {
                     vm.cancel();
            }
            else {
                    skClient.out("Unable to get active vm"); 
            }
        }
        else
            skClient.out("You are not logged in or there is no selected conversation (use cc)");
    }

    public void execute_s()	// stop voicemail playback
    {
        if (skClient.currentConversation == null) {
            skClient.out("You are not logged in or there is no selected conversation (use cc)");
        }
        else {
            int id = Ask.ask_int("select voicemail id : ");

            GetLastMessagesResponse messages;
            messages = skClient.currentConversation.getLastMessages(0);
            if (messages == null) {
                skClient.error("Unable to get messages from selected conversation");
                return;
            }
            Voicemail vm = null;
            for (Message message : messages.unconsumedMessages) {
                if ( message.getOid() == id) {
                    vm = message.getVoiceMessage();
                    break;
                }
            }
            
            if ( vm == null) {
                for (Message message : messages.contextMessages) {
                    if ( message.getOid() == id) {
                        vm = message.getVoiceMessage();
                        break;
                    }
                }
            }
            if (vm != null) {
                vm.stopPlayback();
            }
        }
    }

    public void execute_p()	// play voicemails
    {
        if (skClient.currentConversation != null) {

            GetLastMessagesResponse messages;
            messages = skClient.currentConversation.getLastMessages(0);
            if (messages == null) {
                skClient.error("Unable to get messages from selected conversation");
                return;
            }

            for (Message message : messages.unconsumedMessages) {
                if (message.getType() == Message.Type.POSTED_VOICE_MESSAGE) {
                    skClient.out("id " + message.getOid() + 
                    		", from: " + message.getAuthor());
                }
            }
            for (Message message : messages.contextMessages) {
            	if (message.getType() == Message.Type.POSTED_VOICE_MESSAGE) {
            		skClient.out("id " + message.getOid() + 
            				", from: " + message.getAuthor());
            	}
            }

            int id = Ask.ask_int("select voicemail id : ");
            
            Voicemail vm = null;
            for (Message message : messages.unconsumedMessages) {
            	if ( message.getOid() == id) {
            		vm = message.getVoiceMessage();
            		break;
            	}
            }
            
            if ( vm == null) {
	            for (Message message : messages.contextMessages) {
	            	if ( message.getOid() == id) {
	            		vm = message.getVoiceMessage();
	            		break;
	            	}
	            }
            }

            if (vm != null) {
            	skClient.out("Playing");
            	vm.startPlayback();
            } else {
            	skClient.error("Unable to play voice message");
            }
        }
        else
            skClient.out("You are not logged in or there is no selected conversation (use cc)");

   }
    
}
