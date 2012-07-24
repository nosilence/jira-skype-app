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
import com.skype.api.Conversation.ListType;
import com.skype.api.Conversation.ParticipantFilter;
import com.skype.api.Participant;
import com.skype.api.Skype.GetAvailableOutputDevicesResponse;
import com.skype.api.Skype.GetAvailableVideoDevicesResponse;
import com.skype.api.Video;
import com.skype.api.Video.GetCurrentVideoDeviceResponse;
import com.skype.skypekitclient.SkypekitClient;
import com.skype.skypekitclient.utils.Ask;

/**
 *
 */
public class CommandConversationCall extends ClientCommandInterface {
    //    private static final String TAG = "CommandConversationCall";

    private SkypekitClient skClient;

    protected CommandConversationCall(SkypekitClient skypekitClient) {
        super();
        skClient = skypekitClient;
    }

    @Override
    public String getName() { return "ConversationCall"; }

    public void executeHelp()
    {
        skClient.out(
                "\n[CONVERSATION CALL] - C\n" +
                "\tCp - place a call (selected conversation)\n" +
                "\tCx - place a call (enter skypename)\n" +
                "\tCe - end call\n" +
                "\tCa - answer a call\n" +
                "\tCf - forward a call\n" +
                "\tCm - mute/unmute\n" +
                "\tCh - hold/unhold\n" +
                "\tCd - send dtmf\n" +
                "\tCQ - start monitoring quality for a user\n" +
                "\tCq - stop monitoring quality for a user\n" +
                "\tCv - [VIDEO] place a call (video is enabled/started automatically)\n" +
                "\tCS - [VIDEO] start sending video\n" +
                "\tCs - [VIDEO] stop sending video\n" +
                "\tCR - [VIDEO] start receiving video\n" +
                "\tCV - [VIDEO] get video device list\n" +
                "\tCr - [VIDEO] stop receiving video\n" +
                "\tCc - [VIDEO] send custom command\n" +
                "\tCC - [VOICE] send custom command\n" +
                "\tCD - [VOICE] get devicelist\n" +
                "\tCE - [VOICE] select device\n"
        );
    }


    public void execute_a()	// answer a call
    {
        if (skClient.notLoggedIn()) {
            System.out.println("You are not logged in.");
            return;
        }

        Conversation[] conversations = skClient.skype.getConversationList(ListType.LIVE_CONVERSATIONS);
        if (conversations == null) {
            skClient.error("Unable to get conversation list");
            return;
        }
        
        for (Conversation conversation : conversations) {
            skClient.currentConversation = conversation;
            Conversation.LocalLiveStatus lstatus = conversation.getLocalLiveStatus();
            if (lstatus == Conversation.LocalLiveStatus.RINGING_FOR_ME) {
                
                System.out.printf("found conversation %d\n", conversation.getOid());
                  
                conversation.joinLiveSession("");
                    
                // create participant objects to receive propchanges
                skClient.currentConversationParticipants = conversation.getParticipants(ParticipantFilter.ALL);
                return;
            }
        }
    }

    public void execute_Q()
    {
        skClient.skype.startMonitoringQuality(Ask.ask("user:"), Ask.ask_yesno("network"));
    }

    public void execute_q()
    {
        System.out.printf("stopMonitoringQuality %s\n", skClient.skype.stopMonitoringQuality(Ask.ask("user:"), Ask.ask_yesno("just stop")).toString());
    }

    public void execute_p()	// place a call (selected conversation)
    {
        ring(false);
    }

    public void execute_v()	// [VIDEO] place a call (video is enabled/started automatically)
    {
        ring(true);
    }

    public void execute_e()	// end call
    {
        if (skClient.notLoggedIn())
            return;

        Conversation[] conversations = skClient.skype.getConversationList(ListType.LIVE_CONVERSATIONS);
        if (conversations == null) {
            skClient.error("Unable to get conversation list");
            return;
        }

        if (conversations.length == 0) {
            skClient.out("0 conversations");
            return;
        }

        for (Conversation conversation : conversations) {
            skClient.out("found conversation" + conversation.getOid());
            conversation.leaveLiveSession(false);
        }
    }

    public void execute_f()	// forward a call
    {
        skClient.error("TBD");
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
            Conversation.LocalLiveStatus lstatus = skClient.currentConversation.getLocalLiveStatus();
            if (lstatus != Conversation.LocalLiveStatus.ON_HOLD_LOCALLY) {
                skClient.currentConversation.holdMyLiveSession();
            } else {
                skClient.currentConversation.resumeMyLiveSession();
            }
        }
    }

    public void execute_d()	// send dtmf
    {
        if (skClient.currentConversation != null) {
            String dtmfstr = Ask.ask("Enter dtmf (1,2,3,4,5,6,7,8,9,0,*,#):");
            Participant.Dtmf dtmf;
            String star = "*";
            String pound = "#";
            if (dtmfstr.equals(star)) {
                dtmf = Participant.Dtmf.DTMF_STAR;
            } else if (dtmfstr.equals(pound)) {
                dtmf = Participant.Dtmf.DTMF_POUND;
            } else {
            	int dtn = Integer.valueOf(dtmfstr);
                dtmf = Participant.Dtmf.get(dtn);
            }
            skClient.currentConversation.sendDtmf(dtmf, 260); //TODO: using 260 <used for lengthInMS in CommandConversationConference> next make it a const
        }
    }

    public void execute_S()	// [VIDEO] start sending video
    {
        if (skClient.currentConversation != null)
        {
            Conversation.LocalLiveStatus lstatus = skClient.currentConversation.getLocalLiveStatus();

            Video video = get_send_video();

            if (lstatus == Conversation.LocalLiveStatus.IM_LIVE) {
                boolean proceed = false;

                if(video == null) {
                    String device_name = null;
                    String device_path = null;
                    GetAvailableVideoDevicesResponse videoDevices = skClient.skype.getAvailableVideoDevices();

                    // by default, use the first one
                    if (videoDevices.count > 0)
                    {
                        device_name = videoDevices.deviceNames[0];
                        device_path = videoDevices.devicePaths[0];
                    }

                    video = skClient.skype.createLocalVideo(Video.MediaType.MEDIA_VIDEO, device_name, device_path);

                    if (video != null) {
                        if (skClient.currentConversation != null)
                        {
                            skClient.sendVideo = true;
                            proceed = true;
                            skClient.currentConversation.attachVideoToLiveSession(video);
                        }
                    }
                } else { // pre-existing video
                    skClient.out("Pre-existing video in state " + video.getStatus());

                    if(video.getStatus() == Video.Status.AVAILABLE)
                    {
                        skClient.sendVideo = true;
                        proceed = true;
                        video.start();
                    }
                }

                if(proceed) {
                    GetCurrentVideoDeviceResponse videoDevice = video.getCurrentVideoDevice();
                    Video preview = skClient.skype.createPreviewVideo(videoDevice.mediatype, videoDevice.deviceName, videoDevice.devicePath);

                    if (preview != null) {
                        preview.start();
                    }
                }
            }
        }
    }

    public void execute_s()	// [VIDEO] stop sending video
    {
        Video video = get_send_video();
        if (video != null) {
            skClient.sendVideo = false;
            video.stop();
        }
    }

    public void execute_R()	// [VIDEO] start receiving video
    {
        Video video = get_receive_video();
        if (video != null) {
            video.start(); 
        }
    }

    public void execute_r() // [VIDEO] stop receiving video
    {
        Video video = get_receive_video();
        if (video != null) {
            video.stop();
        }
    }

    public void execute_c()	// [VIDEO] send custom command
    {
        String command, response;
        command = Ask.ask("enter command:");

        skClient.out("VideoCommand: sending " + command);
        response = skClient.skype.videoCommand(command);
        skClient.out("VideoCommand: response: " + response);
    }

    public void execute_C()	// [VIDEO] send custom command

    {
        String command, response;
        command = Ask.ask("enter command:");

        skClient.out("VoiceCommand: sending " + command);
        response = skClient.skype.voiceCommand(command);
        skClient.out("VoiceCommand: response: " + response);
    }

    private void ring(boolean enable_video)
    {
        if (skClient.currentConversation != null) {
            //get participants
            Participant[] participants = skClient.currentConversation.getParticipants(Conversation.ParticipantFilter.OTHER_CONSUMERS);
            if (participants == null) {
                skClient.error("Unable to get participant list");
                return;
            }
            if (participants.length == 0) {
                skClient.out("No participants");
                return;
            }

            //ring participants
            for (Participant participant : participants) {
                participant.ring("", enable_video, 0, 0, false, "");
            }
        }
        else
            skClient.out("You are not logged in or there is no selected conversation (use cc)");
    }

    private Video get_send_video()
    {
        Video video = null;
        if (skClient.currentConversation != null) {
            Conversation.Type ctype = skClient.currentConversation.getType();
            if (ctype != Conversation.Type.DIALOG) {
                skClient.error("Selected conversation is not 1-1 dialog call");
                return video;      
            }

            //get participants
            Participant[] participants = skClient.currentConversation.getParticipants(Conversation.ParticipantFilter.MYSELF);
            if (participants == null) {
                skClient.error("Unable to get participant");
                return video;
            }

            //get video
            if (participants.length > 0) {
                Participant participant = participants[0];
                Video[] videos = participant.getLiveSessionVideos();
                if (videos == null) {
                    skClient.error("Unable to get videos from participant: " + participant.getIdentity());
                    return video;
                }
                if (videos.length > 0) {
                    video = videos[0];
                    if (video != null) {
                        return video;
                    }
                }
            } else {
                skClient.out("No participants received");
            }
        }
        return video;
    }

    private Video get_receive_video()
    {
        Video video = null;
        if (skClient.currentConversation != null) {
            Conversation.Type ctype = skClient.currentConversation.getType();
            if (ctype != Conversation.Type.DIALOG) {
                skClient.error("Selected conversation is not 1-1 dialog call");
                return video;      
            }

            //get participants
            Participant[] participants = skClient.currentConversation.getParticipants(Conversation.ParticipantFilter.OTHER_CONSUMERS);
            if (participants == null) {
                skClient.error("Unable to get participant");
                return video;
            }

            //get video
            if (participants.length > 0) {
                Participant participant = participants[0];
                Video[] videos = participant.getLiveSessionVideos();
                if (videos == null) {
                    skClient.error("Unable to get videos from participant: " + participant.getIdentity());
                    return video;
                }
                if (videos.length > 0) {
                    video = videos[0];
                    if (video != null) {
                        skClient.out("Got video from " + participant.getIdentity());
                        return video;
                    } else {
                        skClient.error("Unable to get video from participant: " + participant.getIdentity());
                    }
                }
            } else {
                skClient.out("No participants received");
            }
        }
        return video;
    }

    public void execute_x()	// place a call (enter skypename)
    {
        if (skClient.notLoggedIn())
            return;

        ArrayList<String> names = new ArrayList<String>();
        names.add(Ask.ask("call to (enter skypename):"));
        if ( names.size() == 0) {
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
        ring(true);
    }

    public void execute_V()	// [VIDEO] get video device list
    {
        GetAvailableVideoDevicesResponse videoDevices =	skClient.skype.getAvailableVideoDevices();
        skClient.out("video devices:");
        for(int i = 0; i < videoDevices.count; i++)
            skClient.out(videoDevices.deviceNames[i] + " \t " + 
                    videoDevices.devicePaths[i]);
        return;
    }

    public void execute_D()	// [VOICE] get devicelist
    {
        GetAvailableOutputDevicesResponse outputDevices = skClient.skype.getAvailableOutputDevices();
        skClient.out("devices:");
        for(int i = 0; i < outputDevices.handleList.length; i++)
            skClient.out(outputDevices.handleList[i] + " \t " + 
                    outputDevices.nameList[i] + " \t " +
                    outputDevices.productIdList[i]);
        return;
    }

    public void execute_E()	// [VOICE] select device
    {
        String in,out,notification;
        in = Ask.ask("input device:");
        out = Ask.ask("output device:");
        notification = Ask.ask("notification device:");
        skClient.skype.selectSoundDevices(in,out,notification);
        //			skClient.out("Setting sound devices successful");
        //		else
        //			skClient.error("Setting sound devices failed");
    }

}
