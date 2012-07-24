/*******************************************************************************************
 
  SkypeKit .NET wrapper tutorial 6 - Outgoing Calls
  
 An important thing to realize, is that despite we keep calling these things
 calls, they are conceptually something else. In the Conversation API, a "call"
 is really a Conversation state, in which one or more Participants just happen
 to have audio going at the time.
 
 You are never placing a call to a Contact. A Contact is that an address book entry.
 Instead, first you will obtain a Conversation, that has Participant objects in it,
 those Participants being representations of Contacts in that Conversation. Then
 you can "ring" those Participants. When they accept the audio session - voila - 
 a "call" is up.
 
 What that means in practice, is in case of initiating live sessions, we should
 make our UI as contact-list based. Instead, it should be conversation-list based.
 From a conversation list, we can get the conversation history (messages and events),
 and Participant objects. Then we can call SktConversation.RingOthers or alternatively,
 call SktParticipant.Ring or SktParticipant.RingIt, if we want to be selective, which
 people we want to talk with.
 
 The UI for all of this, is therefore not very simple. Essentially, it is a 
 combination of what we already did in tutorials 3 (chat) and 5 (incoming calls).
 We will end up with a conversation list in a ListBox, a conversation view in a 
 big multiline TestBox and a FlowLayoutPanel for current conversation's participants.
 
 Adding capability to initiate live sessions will be a relatively small part.
 
 First we override the following three classes:
 1. Participant - to add a GUI parts that will go into participant panel.
 2. Conversation - to add message history caching and ToString override.
 3. Message - to add a GetText method, for different message types. 
    For this tutorial we will handle justa few of those, there are many more, that
    you would need to take care of in a full client. Note that overriding ToString()
    method for the same purpose is not recommended.
 
 Then we add custom callbacks for following events:
 1. OnConnect - the usual.
 2. OnAccountStatus - the usual, also updating conversation list box and audio devices.
 3. OnParticipantSoundLevel - to update participant's sound indicator.
 4. OnParticipantVoiceStatus - to update visuals when participant joins live state;
 5. OnSkypeMessage - to update message history and handle live session starts and ends;
 6. OnConversationLocalLiveStatus - to detect incoming calls ("rings");
 7. OnConversationSpawnConference -  to detect when we need to jump from 1-on-1 
    conversation to a newly spawned conference when a 3rd participant gets added;
 8. OnSkypeConversationListChange - to update conversation list, when new conversations 
    get created.
 9. OnSkypeAvailableDeviceListChange - to update audio device list, when USB headsets 
    get plugged in or out.

 Following visual components were used in the making of this tutorial:
 1. ListBox - convListBox - holds our conversation list.
 2. TextBox msgLog - message history of selected conversation.
 3. FlowLayoutPanel - partPanel- to hold selected conversation's participant list.
 4. ComboBox - speakerSelector - audio output device selectot.
 5. ComboBox - micSelector - audio input device selectot.
 6. TextBox - inputBox - text input for chat messages.
 7. Button - sendButton - for sending out chat messages.
 8. Button - callButton - has three functions, depending on context it can:
    1. initiate an outgoing call;
    2. accept an incoming call when the conversation live state is RINGING_FOR_ME;
    3. drop out from current live call when live call is up.
*/

using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using SkypeKit;
using System.Security.Cryptography.X509Certificates;
using tutorials_common;

namespace step06_outgoing_calls
{
    public partial class Form1 : Form
    {
        string username;
        string password;

        SktSkype skype;
        SktAccount account;

        SktConversation liveSession = null;
        SktConversation.List conversationList;
        SktParticipant.List participants;

        public class Participant : SktParticipant
        {
            public ProgressBar voiceVolume;
            public Label nameLabel;
            public Panel gui;
            
            public static int panelHeight       = 25;
            public static int panelWidth        = 165;
            public static Color liveColor = SystemColors.GradientActiveCaption; // just random nice colors
            public static Color nonLiveColor = SystemColors.GradientInactiveCaption;

            public Participant(uint objectId, SktSkype skype)
                : base(objectId, skype)
            {
                gui = new Panel() 
                {
                    Width = panelWidth, Height = panelHeight,
                    BorderStyle = BorderStyle.FixedSingle,
                    BackColor = SystemColors.GradientInactiveCaption
                };

                nameLabel = new Label() 
                {
                    Top = 0, Left = 10,
                    Height = 11, Width = panelWidth - 20, 
                    Text = "<n/a>", TextAlign = ContentAlignment.TopCenter, AutoSize = false 
                };
                gui.Controls.Add(nameLabel);

                voiceVolume = new ProgressBar() 
                { 
                    Top = 12, Left = 10, 
                    Height = 10, Width = panelWidth-20,  
                    Value = 0, Maximum = 10,  
                };
                gui.Controls.Add(voiceVolume);
            }

            public bool IsLive()
            {
                return ((P_VOICE_STATUS == SktParticipant.VOICE_STATUS.SPEAKING) |      // in live state
                        (P_VOICE_STATUS == SktParticipant.VOICE_STATUS.VOICE_ON_HOLD) | // in live state but on hold
                        (P_VOICE_STATUS == SktParticipant.VOICE_STATUS.LISTENING));     // in live state but muted
            }
        }

        // When a participant goes "live" -> update background color
        public void OnParticipantVoiceStatus(SktParticipant sender, SktEvents.OnParticipantVoiceStatusArgs e)
        {
            Participant p = (Participant)sender;
            if (p.IsLive() & (p.gui.BackColor == Participant.nonLiveColor)) p.gui.BackColor = Participant.liveColor;
            if (!p.IsLive() & (p.gui.BackColor == Participant.liveColor)) p.gui.BackColor = Participant.nonLiveColor;
        }

        public void OnParticipantSoundLevel(SktParticipant sender, SktEvents.OnParticipantSoundLevelArgs e)
        {
            Participant p = (Participant)sender;
            p.voiceVolume.Value = (int)e.value;
        }

        public void AddParticipantToConversation(Participant participant)
        {
            SktContact contact;
            contact = skype.GetContact(participant.P_IDENTITY);
            participant.nameLabel.Text = contact.P_DISPLAYNAME;
            partPanel.Controls.Add(participant.gui);            
            if (!participants.Contains(participant)) participants.Add(participant);
        }

        public void AddCurrentParticipantsToNewConv(SktConversation conv)
        {
            participants = conv.GetParticipants(SktConversation.PARTICIPANTFILTER.ALL);
            foreach (Participant person in participants) AddParticipantToConversation(person);
        }

        public void RemoveParticipantFromList(Participant participant)
        {
            partPanel.Controls.Remove(participant.gui);
            if (participants.Contains(participant)) participants.Remove(participant);
        }

        public void RemoveAllParticipants()
        {
            while (partPanel.Controls.Count > 0) partPanel.Controls.RemoveAt(0);
        }

        // Not all messages are text. In a full UI, you would need to add many more.
        public class Message : SktMessage
        {
            public Message(uint objectId, SktSkype skype) : base(objectId, skype) { }

            public string GetText()
            {
                switch (P_TYPE)
                {
                    case TYPE.POSTED_TEXT: return P_AUTHOR_DISPLAYNAME + " : " + P_BODY_XML + "\r\n";
                    case TYPE.STARTED_LIVESESSION: return "-- This conversation has gone live --\r\n";
                    case TYPE.ENDED_LIVESESSION: return "-- This conversation is no longer live --\r\n";
                    case TYPE.SPAWNED_CONFERENCE: return "-- This dialog has moved on into another conversation --\r\n";
                    default: return "-- Unhandled message type: " + P_TYPE.ToString() + "--\r\n";
                }
            }
        }

        // This we already covered in the Chat tutorial.
        public class Conversation : SktConversation
        {
            public string messageHistory; // our message history cache
            public bool messageHistoryLoaded; // indicates whether we have retrieved the cache

            public Conversation(uint objectId, SktSkype skype)
                : base(objectId, skype)
            {
                messageHistory = "";
                messageHistoryLoaded = false;
            }

            public override string ToString()
            {
                return P_DISPLAYNAME;
            }

            // The fetching of message history is now in Conversation class
            public void FetchMessageHistory()
            {
                if (messageHistoryLoaded) return;

                SktMessage.List newMessages; // unread messages
                SktMessage.List oldMessages; // recent history

                DateTime t = DateTime.Now.Subtract(TimeSpan.FromDays(7));

                this.GetLastMessages(out oldMessages, out newMessages, t);
                messageHistory = "";

                foreach (Message msg in oldMessages) messageHistory += msg.GetText();
                foreach (Message msg in newMessages) messageHistory += msg.GetText(); 

                if (messageHistory == "") messageHistory = "No messages in this conversation in last week.";

                messageHistoryLoaded = true;
            }
        }

        // And this we already covered in the Incoming Calls tutorial..
        public void OnSkypeMessage(SktSkype sender, SktEvents.OnSkypeMessageArgs e)
        {
            if (!convListBox.Items.Contains(e.conversation)) return;
            var conv = (Conversation)e.conversation;

            Message msg = (Message)e.message;

            switch (msg.P_TYPE)
            {
                case SktMessage.TYPE.STARTED_LIVESESSION:
                    UiToInCallMode();
                    liveSession = e.conversation;
                    break;

                case SktMessage.TYPE.ENDED_LIVESESSION:
                    liveSession = null;
                    UiToWaitingMode();
                    break;
            }
                
            if (conv.messageHistoryLoaded)
            {
                string newLine = msg.GetText();
                conv.messageHistory = conv.messageHistory + newLine;

                if (conv == convListBox.SelectedItem)
                {
                    msgLog.AppendText(newLine);
                    conv.SetConsumedHorizon(DateTime.Now, false);
                }
            }
        }

        // Checkig for incoming calls here
        public void OnConversationLocalLiveStatus(SktConversation sender, SktEvents.OnConversationLocalLivestatusArgs e)
        {
            if (e.value == SktConversation.LOCAL_LIVESTATUS.RINGING_FOR_ME)
            {
                if (liveSession != null) return; // busy..
                liveSession = sender;
                // forcibly switching the UI to the ringing conversation
                if (convListBox.SelectedItem != sender) convListBox.SelectedItem = sender;
                UiToRinginggMode();
            }
        }

        // Checking for "migration" from 1-on-1 to a conference. Note that 
        // the reloading participant list and UI is conveniently done for us
        // by what we already have in the listbox selected index change callback.
        void OnConversationSpawnConference(SktConversation sender, SktEvents.OnConversationSpawnConferenceArgs e)
        {
            if (convListBox.SelectedItem == sender) convListBox.SelectedItem = e.spawned;
            if (sender == liveSession) liveSession = e.spawned;
        }

        // When another conversation gets selected in the UI
        private void listBox1_SelectedIndexChanged(object sender, System.EventArgs e)
        {
            if (convListBox.SelectedItem == null) return;

            msgLog.Clear();
            RemoveAllParticipants();

            var conv = (Conversation)convListBox.SelectedItem;
            if (!conv.messageHistoryLoaded) conv.FetchMessageHistory();
            msgLog.Text = conv.messageHistory;

            msgLog.SelectionStart = msgLog.Text.Length;
            msgLog.ScrollToCaret();
            conv.SetConsumedHorizon(DateTime.Now, false);
            AddCurrentParticipantsToNewConv(conv);
        }

        // Keeping conversation list and listbox up-to-date
        public void OnSkypeConversationListChange(SktSkype sender, SktEvents.OnSkypeConversationListChangeArgs e)
        {
            if (e.type.Equals(SktConversation.LIST_TYPE.INBOX_CONVERSATIONS))
            {
                if (e.added)
                {
                    conversationList.Insert(0, e.conversation);
                    convListBox.Items.Insert(0, e.conversation);
                }
                else
                {
                    conversationList.Remove(e.conversation);
                    convListBox.Items.Remove(e.conversation);
                }
            }
        }

        private void convListBox_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            if (convListBox.SelectedItem == null) return;
            Conversation conv = (Conversation)convListBox.Items[convListBox.SelectedIndex];
            conv.RemoveFromInbox();
        }

        private void sendButton_Click(object sender, EventArgs e)
        {
            if (convListBox.SelectedItem == null) return;
            if (inputBox.Text == "") return;

            var conv = (Conversation)convListBox.Items[convListBox.SelectedIndex];
            conv.PostText(inputBox.Text, false);
            inputBox.Clear();
        }

        // Here we do one of three things:
        // 1. live conversation is up and ringing - we join
        // 2. live conversation is up and NOT ringing - we frop out
        // 3. no live conversation - we go live in current one and ring others.
        private void callButton_Click(object sender, EventArgs e)
        {
            if (liveSession != null)
            {
                if (liveSession.P_LOCAL_LIVESTATUS == SktConversation.LOCAL_LIVESTATUS.RINGING_FOR_ME)
                {
                    liveSession.JoinLiveSession("");
                }
                else liveSession.LeaveLiveSession(false);
                return;
            }

            if (convListBox.SelectedItem == null) return;

            // Here we actually make the outgoing call
            Conversation conv = (Conversation)convListBox.Items[convListBox.SelectedIndex];

            // Fetching target list from conv and converting to string list
            SktParticipant.List parts;
            parts = conv.GetParticipants(SktConversation.PARTICIPANTFILTER.OTHER_CONSUMERS);
            List<String> names = new List<string>();
            foreach (SktParticipant part in parts) names.Add(part.P_IDENTITY);

            // Causing others to ring
            conv.RingOthers(names, false, "");
            liveSession = conv;
        }

        public Form1()
        {
            InitializeComponent();
            LoadCallIcons();
            UiToWaitingMode();
            this.AcceptButton = sendButton;

            if (!System.IO.File.Exists(tutorials.path + tutorials.keyfilename))
            {
                throw new Exception(String.Format(
                    "The keyfile (.pfx) path or filename {0} is incorrect?", tutorials.path + tutorials.keyfilename));
            }

            X509Certificate2 cert = new X509Certificate2(tutorials.path + tutorials.keyfilename, tutorials.keypassword);
            skype = new SktSkype(this, cert, true, false, 8963);

            skype.NewParticipant    = (oid, skp) => { return new Participant(oid, skp); };
            skype.NewConversation   = (oid, skp) => { return new Conversation(oid, skp); };
            skype.NewMessage        = (oid, skp) => { return new Message(oid, skp); };

            skype.events.OnConnect                          += OnConnect;
            skype.events.OnAccountStatus                    += OnAccountStatus;
            skype.events.OnParticipantSoundLevel            += OnParticipantSoundLevel;
            skype.events.OnParticipantVoiceStatus           += OnParticipantVoiceStatus;
            skype.events.OnSkypeMessage                     += OnSkypeMessage;            
            skype.events.OnConversationLocalLivestatus      += OnConversationLocalLiveStatus;
            skype.events.OnConversationSpawnConference      += OnConversationSpawnConference;
            skype.events.OnSkypeConversationListChange      += OnSkypeConversationListChange;
            skype.events.OnSkypeAvailableDeviceListChange   += OnSkypeAvailableDeviceListChange;

            convListBox.SelectedIndexChanged    += listBox1_SelectedIndexChanged;
            convListBox.MouseDoubleClick        += convListBox_MouseDoubleClick;
            this.FormClosing                    += Form1_FormClosing;
            sendButton.Click                    += sendButton_Click;

            skype.LaunchRuntime(tutorials.path + tutorials.runtime, true);
            skype.Connect();
            //tutorials.ShowLogWindow(this, skype);
        }

        public void OnConnect(object sender, SktEvents.OnConnectArgs e)
        {
            if (e.success)
            {
                this.msgLog.AppendText("Connection to runtime is up. Lets see if we can log in..\r\n");

                LoginRec login = tutorials.ShowLoginDialog(this);
                username = login.username;
                password = login.password;
                if (!login.abort)
                {
                    account = skype.GetAccount(username);
                    account.LoginWithPassword(password, false, false);
                }
            }
            else
            {
                this.msgLog.AppendText("IPC handshake failed with: " + e.handshakeResult + "\r\n");
            }
        }

        public void OnAccountStatus(SktAccount sender, SktEvents.OnAccountStatusArgs e)
        {
            this.msgLog.AppendText(e.value.ToString() + "\r\n");

            if (e.value == SktAccount.STATUS.LOGGED_IN)
            {
                this.msgLog.AppendText("Retrieving conversation list..\r\n");
                conversationList = skype.GetConversationList(SktConversation.LIST_TYPE.INBOX_CONVERSATIONS);

                foreach (SktConversation conversation in conversationList)
                {
                    this.convListBox.Items.Add(conversation);
                }
                UpdateAudioDevices();
            }

            if (e.value == SktAccount.STATUS.LOGGED_OUT)
            {
                this.msgLog.AppendText("Login failed because of " + sender.P_LOGOUTREASON + "\r\n");
            }
        }

        // The following bits deal with retrieving, switching and keeping up-to-date the list
        // of audio devices. There are three devices involved in Skype, and each is distinguished 
        // with no less than three different strings that describe it.
        // AudioIn device - the microphone;
        // AudioOut device -  speakers/headphones, basically, where you can hear voices in your head, during a call.
        // WaveOut device - also speakers/headphones, but for ringtones and other audio notifications. 
        // This enables the user to set it up so that call audio goes to the headset and incoming ring
        // notifications go to loudspeakers.
        //
        // Each device has three "names".
        // 1. Human readable name - that you can display in the UI.
        // 2. Device handle - that you can use for selecting between devices.
        // 3. Product ID - that you can totally ignore.

        // This class is just for convenient storage of the devices and adding ToString.
        public class AudioDevice
        {
            public string name;
            public string handle;
            public AudioDevice(string name, string handle) { this.name = name; this.handle = handle; }
            public override string ToString() { return name; }
        }

        public void UpdateAudioDevices()
        {
            List<String> deviceNames, deviceHandles, deviceProductIds;

            // Fetching available mics.
            skype.GetAvailableRecordingDevices(out deviceHandles, out deviceNames, out deviceProductIds);
            micSelector.Items.Clear();

            if (deviceNames.Count == 0)
            {
                micSelector.Enabled = false;
                return;
            }

            for (int i = 0; i < deviceNames.Count; i++)
            {
                micSelector.Items.Add(new AudioDevice(deviceNames[i], deviceHandles[i]));
            }
            micSelector.Enabled = true;
            micSelector.SelectedIndex = 0;

            // Fetching available speakers/headphones.
            skype.GetAvailableOutputDevices(out deviceHandles, out deviceNames, out deviceProductIds);
            speakerSelector.Items.Clear();

            if (deviceNames.Count == 0)
            {
                speakerSelector.Enabled = false;
                return;
            }

            for (int i = 0; i < deviceNames.Count; i++)
            {
                speakerSelector.Items.Add(new AudioDevice(deviceNames[i], deviceHandles[i]));
            }
            speakerSelector.Enabled = true;
            speakerSelector.SelectedIndex = 0;
        }

        // In this case we use what is selected in speakerSelector for both "call audio"
        // and "ringtone" audio.
        private void ReselectAudioDevices()
        {
            if ((micSelector.SelectedItem == null) | (speakerSelector.SelectedItem == null)) return;
            AudioDevice mic = (AudioDevice)micSelector.SelectedItem;
            AudioDevice speaker = (AudioDevice)speakerSelector.SelectedItem;
            skype.SelectSoundDevices(mic.handle, speaker.handle, speaker.handle);
        }

        private void micSelector_SelectedIndexChanged(object sender, EventArgs e)
        {
            ReselectAudioDevices();
        }

        private void speakerSelector_SelectedIndexChanged(object sender, EventArgs e)
        {
            ReselectAudioDevices();
        }

        // This event will keep our audio device lists up to date, even if some
        // USB devices get plugged in or out.
        void OnSkypeAvailableDeviceListChange(SktSkype sender, SktEvents.OnSkypeAvailableDeviceListChangeArgs e)
        {
            UpdateAudioDevices();
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            skype.Disconnect();
        }

        // UI switching and loading of icons.

        public void UiToInCallMode()
        {
            callButton.Image = dropCallIcon;
            callButton.Text = "";
        }

        public void UiToWaitingMode()
        {
            callButton.Image = startCallIcon;
            callButton.Text = "";
        }

        public void UiToRinginggMode()
        {
            callButton.Image = joinCallIcon;            
        }

        Bitmap startCallIcon;
        Bitmap joinCallIcon;
        Bitmap dropCallIcon;
        Bitmap sendMessageIcon;

        public void LoadCallIcons()
        {
            startCallIcon   = new Bitmap(tutorials.gfxPath + "StartCall.png");
            dropCallIcon    = new Bitmap(tutorials.gfxPath + "DropCall.png");
            joinCallIcon    = new Bitmap(tutorials.gfxPath + "JoinCall.png");
            sendMessageIcon = new Bitmap(tutorials.gfxPath + "SendMessage.png");                        
            sendButton.Image    = sendMessageIcon;
            callButton.Text = "";
            sendButton.Text = "";
        }
    }
}
