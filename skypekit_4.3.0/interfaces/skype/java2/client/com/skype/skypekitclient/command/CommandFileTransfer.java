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

import com.skype.api.Conversation.GetLastMessagesResponse;
import com.skype.api.Conversation.PostFilesResponse;
import com.skype.api.Message;
import com.skype.api.Skype;
import com.skype.api.Transfer;
import com.skype.skypekitclient.SkypekitClient;
import com.skype.skypekitclient.utils.Ask;

/**
 *
 */
public class CommandFileTransfer extends ClientCommandInterface {
//    private static final String TAG = "CommandFileTransfer";

    private SkypekitClient skClient;

	protected CommandFileTransfer(SkypekitClient skypekitClient) {
		super();
		skClient = skypekitClient;
	}
    
	@Override
	public String getName() { return "FileTransfer"; }

	public void executeHelp()
	{
        skClient.out("\n[FILE TRANSFER] - c\n" +
                "\tfs - send a file\n" +
                "\tfr - receive a file\n" +
                "\tfc - cancel\n" +
                "\tfp - pause or resume a transfer\n"
        );
	}

	void execute_s() // send a file
	{
        if (skClient.currentConversation != null) {
            GetLastMessagesResponse messages;
            messages = skClient.currentConversation.getLastMessages(0);
            if (messages == null) {
                skClient.out("no transfers are available in current conversation");
                return;
            }

            ArrayList<String> files = new ArrayList<String>();
            String fnms = "", pref = "";
            for (String fn = Ask.ask("filename: "); (fn.length() != 0); fn = Ask.ask("filename: ")) {
                files.add(fn);
                fnms += pref + fn;
                pref = ", ";
            }
            
            PostFilesResponse pfResult = skClient.currentConversation.postFiles(files.toArray(new String[files.size()]), fnms);
            if (pfResult.errorCode != Skype.TransferSendfileError.TRANSFER_OPEN_SUCCESS)
            	skClient.error("failed to post " + pfResult.errorFile + " (from " + fnms + ") because " + pfResult.errorCode);
            else
            	skClient.out("posting " + pfResult.errorFile);
        }
	}

	private void pauseresume(Message[] messages)
	{
        for (Message msg : messages) {
            if (msg.getType() == Message.Type.POSTED_FILES) {
                for (Transfer transfer :  msg.getTransfers()) {
                    if (transfer.getStatus() == Transfer.Status.PAUSED) {
                        transfer.resume();
                    } else {
                        transfer.pause();
                    }
                }
            }
        }
	}

	private void cancel(Message[] messages)
	{
        for (Message msg : messages) {
            if (msg.getType() == Message.Type.POSTED_FILES) {
                for (Transfer transfer : msg.getTransfers()) {
                	transfer.cancel();
                }
            }
        }
	}

	private void receive(Message[] messages)
	{
        for (Message msg : messages) {
            if (msg.getType() == Message.Type.POSTED_FILES) {
                    for (Transfer transfer : msg.getTransfers()) {
                        Transfer.Type type; 
                        type = transfer.getType();
                        Transfer.Status tStatus;
                        if (type == Transfer.Type.INCOMING && 
                        		(((tStatus = transfer.getStatus()) == Transfer.Status.NEW) ||
                        				(tStatus == Transfer.Status.CONNECTING) ||
                        				(tStatus == Transfer.Status.WAITING_FOR_ACCEPT))) {
                                if (!transfer.accept(Ask.ask("save incoming file to (path + filename): "))) {
                                        skClient.error("failed to accept tranfer\n");       
                                }   
                        }
                    }
            }
        }
	}

	void execute_p()
	{
        if (skClient.currentConversation != null) {
            GetLastMessagesResponse messages;
            messages = skClient.currentConversation.getLastMessages(0);
            if (messages == null) {
                skClient.out("no transfers are available in current conversation");
                return;
            }

            pauseresume(messages.contextMessages);
            pauseresume(messages.unconsumedMessages);
        }
	}

	void execute_r() // receive
	{
        if (skClient.currentConversation != null) {
            GetLastMessagesResponse messages;
            messages = skClient.currentConversation.getLastMessages(0);
            if (messages == null) {
                skClient.out("no transfers are available in current conversation");
                return;
            }

            receive(messages.contextMessages);
            receive(messages.unconsumedMessages);
        }
	}

	void execute_c() // cancel
	{
        if (skClient.currentConversation != null) {
            GetLastMessagesResponse messages;
            messages = skClient.currentConversation.getLastMessages(0);
            if (messages == null) {
                skClient.out("no transfers are available in current conversation");
                return;
            }

            cancel(messages.contextMessages);
            cancel(messages.unconsumedMessages);
        }
	}

}
