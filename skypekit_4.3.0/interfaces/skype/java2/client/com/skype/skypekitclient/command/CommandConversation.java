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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.skype.api.Conversation;
import com.skype.api.Conversation.GetLastMessagesResponse;
import com.skype.api.Conversation.ListType;
import com.skype.api.Conversation.ParticipantFilter;
import com.skype.api.Message;
import com.skype.api.Participant;
import com.skype.skypekitclient.SkypekitClient;
import com.skype.skypekitclient.utils.Ask;

/**
 *
 */
public class CommandConversation extends ClientCommandInterface
{
    // private static final String TAG = "CommandConversation";

    private SkypekitClient skClient;

    protected CommandConversation(SkypekitClient skypekitClient)
    {
        super();
        skClient = skypekitClient;
    }

    @Override
    public String getName()
    {
        return "Conversation";
    }

    public void executeHelp()
    {
        skClient.out("\n[CONVERSATION] - c\n" +
                "\tcc - select a conversation as current one\n" +
                "\tcx - select a conversation as current one (filtered list)\n" +
                "\tcu - open conversation using skype name\n" +
                "\tct - set topic of current conversation\n" +
                "\tcp - set picture of current conversation\n" +
                "\tcB - bookmark current conversation\n" +
                "\tcb - unbookmark current conversation\n" +
                "\tcg - set guidelines\n" +
                "\tcn - set display name\n" +
                "\tcl - retire from current conversation\n" +
                "\tco - set rank\n" +
                "\tcw - set text status write\n" +
                "\tcW - set text status angry\n" +
                "\tcr - set text status reading\n" +
                "\tca - set alert string\n" +
                "\tcf - find message with text\n" +
                "\tcm - list messages\n" +
                "\tcP - list properties from selected message\n" +
                "\tcX - list participant properties for current coversation\n"
                );
    }

    boolean hasNoConversation()
    {
        if (skClient.currentConversation == null) {
            skClient.error("No conversation selected.");
            return true;
        }
        return false;
    }

    @Override
    public void execute_u() // Open conversation using skype name
    {
        if (skClient.notLoggedIn())
            return;

        ArrayList<String> names = new ArrayList<String>();
        String name;
        while (!(name = Ask.ask("enter skype name: ")).isEmpty()) {
            names.add(name);
        }
        if (names.size() == 0) {
            skClient.error("No participant entered, unable to get conversation");
            return;
        }

        Conversation conversation = skClient.skype.getConversationByParticipants(names.toArray(new String[names.size()]), true, false);
        if (conversation == null) {
            System.err.println("Error: Unable to get conversation");
            return;
        }

        skClient.currentConversation = conversation;
        skClient.currentConversationParticipants = skClient.currentConversation.getParticipants(ParticipantFilter.ALL);
    }

    @Override
    public void execute_c() // Select a conversation as current one
    {
        if (skClient.notLoggedIn())
            return;

        Conversation conversations[] = skClient.skype.getConversationList(ListType.ALL_CONVERSATIONS);
        if (conversations == null) {
            skClient.error("Unable to get conversation list\n");
            return;
        }

        if (conversations.length == 0) {
            skClient.out("0 conversations\n");
            return;
        }

        HashMap<Integer, Conversation> map = new HashMap<Integer, Conversation>();
        for (Conversation c : conversations) {
            int key = c.getOid();
            skClient.out("\t" + key + " " + c.getDisplayName() +
                    " (" + c.getIdentity() + ")");
            map.put(key, c);
        }

        skClient.currentConversation = null;
        skClient.currentConversationParticipants = null;
        int sel = Ask.ask_int("select conversation id: ");
        if (map.containsKey(sel)) {
            skClient.currentConversation = map.get(sel);

            // create participant objects to receive propchanges
            skClient.currentConversationParticipants = skClient.currentConversation.getParticipants(ParticipantFilter.ALL);
        }
        else {
            skClient.error("unknown conversation id, current conversation unchanged.");
        }
    }

    @Override
    public void execute_x() // Select a conversation as current one (filtered
                            // list)
    {
        if (skClient.notLoggedIn())
            return;

        String labels[] = { "Conversation::ALL_CONVERSATIONS", "Conversation::INBOX_CONVERSATIONS",
                "Conversation::BOOKMARKED_CONVERSATIONS",
                    "Conversation::LIVE_CONVERSATIONS", "" };
        ListType values[] = { ListType.ALL_CONVERSATIONS, ListType.INBOX_CONVERSATIONS,
                ListType.BOOKMARKED_CONVERSATIONS, ListType.LIVE_CONVERSATIONS };

        ListType type = values[Ask.ask_list("select filter:", labels)];
        skClient.out("type " + type);
       Conversation[] conversations = skClient.skype.getConversationList(type);
        if (conversations == null) {
            skClient.error("Unable to get conversation list\n");
            return;
        }

        if (conversations.length == 0) {
            skClient.out("0 conversations\n");
            return;
        }

        int i = 1;
        for (Conversation c : conversations) {
            skClient.out("\t" + (i++) +
                    " " + c.getDisplayName() +
                    " (" + c.getIdentity() + ")");
        }

        skClient.currentConversation = null;
        i = Ask.ask_int("select conversation id: ");
        Conversation conversation = i > 0 ? conversations[i-1] : null;
        if (conversation != null) {
            skClient.currentConversation = conversation;
            // create participant objects to receive propchanges
            skClient.currentConversationParticipants = skClient.currentConversation.getParticipants(ParticipantFilter.ALL);
        } else {
            skClient.currentConversationParticipants = null;
        }
    }

    @Override
    public void execute_t() // set topic of current conversation
    {
        if (hasNoConversation())
            return;

        skClient.currentConversation.setTopic(Ask.ask("topic"), false);
    }

    @Override
    public void execute_p() // set picture of current conversation
    {
        if (hasNoConversation())
            return;

        String filename = Ask.ask("filename: (jpg image, size less then 50 kb)");
        byte[] pic = null;
        try {
            pic = load_file(filename);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (pic != null) {
            if (pic.length >= 50000) {
                skClient.error("unable to set picture! pic size too large: " + pic.length);
                return;
            }
            skClient.currentConversation.setPicture(pic);
        }
        else {
            skClient.error("can't load picture " + filename);
        }
    }

    public byte[] load_file(String filename) throws Exception
    {
        InputStream in = null;
        byte[] buf = null; // output buffer
        byte[] tmp = null;
        int bufLen = 20000 * 3;
        try {
            in = new BufferedInputStream(new FileInputStream(filename));
            buf = new byte[bufLen];
            int len = 0;
            while ((len = in.read(buf, 0, bufLen)) != -1) {
                tmp = new byte[len];
                System.arraycopy(buf, 0, tmp, 0, len); // still need to do copy
            }
        }
        finally {
            if (in != null)
                try {
                    in.close();
                }
                catch (Exception e) {
                }
        }
        return tmp;
    }

    @Override
    public void execute_B() // bookmark current conversation
    {
        if (hasNoConversation())
            return;

        skClient.currentConversation.setBookmark(true);
    }

    @Override
    public void execute_b() // unbookmark current conversation
    {
        if (hasNoConversation())
            return;

        skClient.currentConversation.setBookmark(false);
    }

    @Override
    public void execute_g() // set guidelines
    {
        if (hasNoConversation())
            return;

        skClient.currentConversation.setGuidelines(Ask.ask("guideline"), false);
    }

    @Override
    public void execute_n() // set display name
    {
        if (hasNoConversation())
            return;

        skClient.currentConversation.renameTo(Ask.ask("new name: "));
    }

    @Override
    public void execute_l() // retire from current conversation
    {
        if (hasNoConversation())
            return;

        skClient.currentConversation.retireFrom();
        skClient.currentConversation = null;
        skClient.currentConversationParticipants = null;
    }

    @Override
    public void execute_o() // set rank
    {
        if (hasNoConversation())
            return;

        skClient.currentConversation.setOption(Conversation.Property.P_OPT_ENTRY_LEVEL_RANK_VALUE,
                Ask.ask_int("rank: "));
    }

    @Override
    public void execute_w() // set text status to write
    {
        if (hasNoConversation())
            return;

        skClient.currentConversation.setMyTextStatusTo(Participant.TextStatus.WRITING);
    }

    @Override
    public void execute_W() // set text status to write as angry
    {
        if (hasNoConversation())
            return;

        skClient.currentConversation.setMyTextStatusTo(Participant.TextStatus.WRITING_AS_ANGRY);
    }

    @Override
    public void execute_r() // set text status to reading
    {
        if (hasNoConversation())
            return;

        skClient.currentConversation.setMyTextStatusTo(Participant.TextStatus.READING);
    }

    @Override
    public void execute_a() // set alert string
    {
        if (hasNoConversation())
            return;

        skClient.currentConversation.setAlertString(Ask.ask("alert: "));
    }

    @Override
    public void execute_f() // find message
    {
        if (hasNoConversation())
            return;

        Message msg;
        msg = skClient.currentConversation.findMessage(Ask.ask("message: "), 0);
        skClient.out("found " +
                    ((msg != null) ? msg.getType() : "(none)"));
    }

    @Override
    public void execute_m() // list messages
    {
        if (hasNoConversation())
            return;

        skClient.out(
                "UNCONSUMED MESSAGES: SUPPRESSED: " +
                        skClient.currentConversation.getUnconsumedSuppressedMessages() +
                        "; NORMAL: " +
                        skClient.currentConversation.getUnconsumedNormalMessages() +
                        "; ELEVATED: " +
                        skClient.currentConversation.getUnconsumedElevatedMessages() +
                        "; VOICE: " +
                        (skClient.currentConversation.getUnconsumedMessagesVoice() ? "there are some"
                                : "none"));

        GetLastMessagesResponse messages;
        messages = skClient.currentConversation.getLastMessages(0);
        if (messages == null) {
            skClient.error("Unable to get messages from selected conversation");
            return;
        }

        skClient.out("UNCONSUMED MESSAGES:");
        for (Message message : Message.mgetInfo(messages.unconsumedMessages)) {
                skClient.out("id " + message.getOid() +
                        ", from: " + message.getAuthor() +
                        ", type " + message.getType());
        }

        if (messages.unconsumedMessages.length == 0) {
            skClient.out("NONE\n");
        }

        skClient.out("CONTEXT MESSAGES:\n");
        for (Message message : Message.mgetInfo(messages.contextMessages)) {
                skClient.out("id " + message.getOid() +
                        ", from: " + message.getAuthor() +
                        ", type " + message.getType());
        }

        if (messages.contextMessages.length == 0) {
            skClient.out("NONE\n");
        }
    }

    @Override
    public void execute_P() // print properties from selected message
    {
        if (hasNoConversation())
            return;

        GetLastMessagesResponse messages;
        messages = skClient.currentConversation.getLastMessages(0);
        if (messages == null) {
            skClient.error("No messages found in selected conversation");
            return;
        }

        ;

        int oid = Ask.ask_int("select message id: ");
        Message m = null;

        for (Message message : Message.mgetInfo(messages.contextMessages)) {
            if (message.getOid() == oid) {
                m = message;
                break;
            }
        }
        for (Message message : Message.mgetInfo(messages.unconsumedMessages)) {
            if (message.getOid() == oid) {
                m = message;
                break;
            }
        }

        if (m == null) {
            skClient.error("No message selected");
            return;
        }

        skClient.out("\t" + "convo_id\t\t" + m.getConversation().getOid());
        skClient.out("\t" + "convo_guid\t\t" + m.getConvoGuid());
        skClient.out("\t" + "author\t\t\t" + m.getAuthor());
        skClient.out("\t" + "author_displayname\t" + m.getAuthorDisplayName());
        skClient.out("\t" + "guid\t\t\t" + m.getGuid()); 
        skClient.out("\t" + "originally_meant_for\t" + m.getOriginallyMeantFor());
        skClient.out("\t" + "timestamp\t\t" + m.getTimestamp());
        skClient.out("\t" + "type\t\t\t" + m.getType());
        skClient.out("\t" + "sending_status\t\t" + m.getSendingStatus());
        skClient.out("\t" + "consumption_status\t" + m.getConsumptionStatus());
        skClient.out("\t" + "edited_by\t\t" + m.getEditedBy());
        skClient.out("\t" + "edit_timestamp\t\t" + m.getEditTimestamp());
        skClient.out("\t" + "param_key\t\t" + m.getParamKey());
        skClient.out("\t" + "param_value\t\t" + m.getParamValue());
        skClient.out("\t" + "body_xml\t\t" + m.getBodyXml());
        skClient.out("\t" + "identities\t\t" + m.getIdentities());
        skClient.out("\t" + "reason\t\t" + m.getReason());
        skClient.out("\t" + "leavereason\t\t" + m.getLeavereason());
        skClient.out("\t" + "participant_count\t" + m.getParticipantCount());
    }

    @Override
    public void execute_X() // debug SKYPEKIT-351
    {
        if (hasNoConversation())
            return;

        Conversation x = skClient.currentConversation;
        skClient.error("Conversation id:" + x.getOid());
        Participant[] xp = x.getParticipants(ParticipantFilter.OTHER_CONSUMERS);

        Participant.mgetInfo(xp);

        for (Participant p : xp) {
            skClient.error("Particpant identity:" + p.getIdentity());
            skClient.error("... adder:" + p.getAdder());
            skClient.error("... convo_id:" + p.getConversation().getOid());
            skClient.error("... debuginfo:" + p.getDebugInfo());
            skClient.error("... last_voice_error:" + p.getLastVoiceError());
            skClient.error("... live_country:" + p.getLiveCountry());
            skClient.error("... live_fwd_identities:" + p.getLiveFwdIdentities());
            skClient.error("... live_identity:" + p.getLiveIdentity());
            skClient.error("... live_price_for_me:" + p.getLivePriceForMe());
            skClient.error("... live_start_timestamp:" + p.getLiveStartTimestamp());
            skClient.error("... live_type:" + p.getLiveType());
            skClient.error("... quality_problems:" + p.getQualityProblems());
            skClient.error("... rank:" + p.getRank());
            skClient.error("... requested_rank:" + p.getRequestedRank());
            skClient.error("... sound_level:" + p.getSoundLevel());
            skClient.error("... text_status:" + p.getTextStatus());
            skClient.error("... transferred_by:" + p.getTransferredBy());
            skClient.error("... transferred_to:" + p.getTransferredTo());
            skClient.error("... video_status:" + p.getVideoStatus());
            skClient.error("... voice_status:" + p.getVoiceStatus());
        }
    }
}

