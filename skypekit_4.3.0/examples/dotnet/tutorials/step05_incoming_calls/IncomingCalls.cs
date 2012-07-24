/*************************************************************************************
 
  SkypeKit .NET wrapper tutorial 5 - Incoming Calls
  
  In this tutorial, we will write a small client that is capable of accepting
  incoming calls (live sessions), drop the active call, put the call on hold
  and mute the microphone.
 
 Of visual components, we have the following on Form1:
 
 1. Label callDurationText - doubles for login status updates as well
 2. Button acceptCallButton - for accepting and dropping calls
 3. Button holdCallButton - toglles call hold state
 4. Button muteCallButton - toggles microphone mute state.
 5. FlowLayoutPanel participantPanel - that will conveniently hold visual 
    representations of Participant objects in our live session.
    
 What we will do is:
 1. Subclass a our own Participant class - to include GUI representation.
 
 2. Add OnParticipantVoiceStatus callback - that adds and removes participants 
    to the GUI, as they join or leave the live state.
    
 3. Add OnSkypeMessage callback - to handle live session starts and ends,
    in the conversation - by reacting to special types of messages. 
    
 4. Add OnConversationLocalLiveStatus callback - this is where we pick up
    incoming live sessions (SktConversation.P_LOCAL_LIVESTATUS == RINGING_FOR_ME).
    
 5. Add OnConversationSpawnConference callback - to handle cases when we have
    a 1-to-1 call and then a new participant gets added, making it into a 
    conference call. In this case, a new SktConversation object will get created,
    and the live current session will be auto-transferred to the new conversation.
    UI needs to be updated accordingly - especially as the SktParticipant objects
    are no longer valid - participants are always conversation-local.
    
 6. Add OnParticipantSoundLevel to update voice volume indicators of participants,
*/

using System;
using System.IO;
using System.Windows.Forms;
using System.Security.Cryptography.X509Certificates;
using System.Drawing;
using SkypeKit;
using tutorials_common;

namespace step05_incoming_calls
{
    public partial class Form1 : Form
    {
        string username;
        string password;

        SktSkype skype;
        SktAccount account;
        SktConversation liveSession = null;
        SktParticipant.List participants;

        Timer callTimer;
        DateTime callStart;

        public class Participant : SktParticipant
        {
            public ProgressBar voiceVolume; 
            public Label nameLabel;
            public PictureBox avatar;
            public Panel gui;
            public static int panel_height = 150;

            // Here we make a nice little GUI panel for each participant of a live session
            // NB! Remember that we cannot use properties inside a constructor!
            // Thus we cannot assign values to nameLabel and avatar fields.
            public Participant(uint objectId, SktSkype skype) : base(objectId, skype) 
            {
                gui = new Panel() { Width = 180, Height = panel_height };

                avatar = new PictureBox() 
                { Top = 14, Width = 120, Height = 120, Left = 30, SizeMode = PictureBoxSizeMode.StretchImage };
                gui.Controls.Add(avatar);

                nameLabel = new Label() 
                { Text = "<n/a>", Top = 1, Left = 30, Width = 120, TextAlign = ContentAlignment.TopCenter, AutoSize = false };
                gui.Controls.Add(nameLabel);
                
                voiceVolume = new ProgressBar()
                { Maximum = 10, Width = 120, Height = 12, Top = 136, Left = 30, Value = 0 };
                gui.Controls.Add(voiceVolume);
            }

            // Some of the participants may be in a conversation, but not join the live session
            // We dont want those to clutter up our UI
            public bool IsLive()
            {
                return ((P_VOICE_STATUS == SktParticipant.VOICE_STATUS.SPEAKING) |      // in live state
                        (P_VOICE_STATUS == SktParticipant.VOICE_STATUS.VOICE_ON_HOLD) | // in live state but on hold
                        (P_VOICE_STATUS == SktParticipant.VOICE_STATUS.LISTENING));     // in live state but muted
            }
        }

        // Updating the voiceVolume ProgressBar when someone makes noise
        public void OnParticipantSoundLevel(SktParticipant sender, SktEvents.OnParticipantSoundLevelArgs e)
        {
            Participant p = (Participant)sender;
            p.voiceVolume.Value = (int)e.value;
        }

        public void AddParticipantToLiveSession(Participant participant)
        {
            // Mapping the participant to corresponding contact is neeeded because
            // the Participant object does not have a P_DISPLAYNAME property,
            // using plain P_IDENTITY does not look nice in the UI.
            SktContact contact;
            contact = skype.GetContact(participant.P_IDENTITY);

            participant.nameLabel.Text = contact.P_DISPLAYNAME;

            if (contact.P_AVATAR_IMAGE != null)
            {
                MemoryStream stream = new MemoryStream(contact.P_AVATAR_IMAGE);
                participant.avatar.Image = Image.FromStream(stream);
            }

            participantPanel.Controls.Add(participant.gui);

            // participant may already be in the participants list
            if (!participants.Contains(participant)) participants.Add(participant);
            AdjustMainWindowHeight();
        }

        public void RemoveParticipantFromLiveSession(Participant participant)
        {
            participantPanel.Controls.Remove(participant.gui);
            // participant may already be in the participants list
            if (participants.Contains(participant)) participants.Remove(participant);
            AdjustMainWindowHeight();
        }

        // This manages adding and removal of participants to the UI, as they
        // join or leave the live state (call) in our live conversation
        public void OnParticipantVoiceStatus(SktParticipant sender, SktEvents.OnParticipantVoiceStatusArgs e)
        {
            if (sender.P_CONVO_ID != liveSession) return;

            // Sadly, a typecast is needed here, because the callback signature does not 
            // match our own Participant class.
            Participant p = (Participant)sender;

            // Someone dropped off
            if ((!p.IsLive()) & participants.Contains(sender)) RemoveParticipantFromLiveSession(p);

            // Someone joined
            if (p.IsLive() & (!participants.Contains(sender))) AddParticipantToLiveSession(p); 
        }

        // This is where we handle live session starts and ends. This can be most easily and reliably
        // done via monitoring special non-text messages in our live conversation.
        public void OnSkypeMessage(SktSkype sender, SktEvents.OnSkypeMessageArgs e)
        {
            if (e.conversation != liveSession) return;

            switch (e.message.P_TYPE)
            {
                // Live session goes up - we switch UI into live session mode
                case SktMessage.TYPE.STARTED_LIVESESSION:
                    UiToInCallMode();
                    liveSession = e.conversation;
                    break;

                // Live session goes down - we switch UI into "wait for next call" mode
                // NB! this will fire only when the entire conversation goes off-live!
                // Should we leave before others, by clicking the drop call button,
                // then we will need to handle the UI state change there as well.
                case SktMessage.TYPE.ENDED_LIVESESSION:
                    liveSession = null;
                    UiToWaitingMode();
                    break;
            };
        }

        // Now, this is the tricky bit! What happens when you first start out with an 1-on-1 call,
        // and then someone adds more people, making it into a conference call? Well, what happens is this:
        // 1. a new conversation is created, both people from old 1-on-1 call and the new ones get added in;
        // 2. the new conversation goes live;
        // 3. the old 1-on-1 conversation goes OFF live;
        // 4. in the old 1-on-1, a message gets posted with type SktMessage.TYPE.SPAWNED_CONFERENCE
        //    and this event will fire with new conversation in the e.spawned field.
        // At that point, what we need to do is to switcheverything over to that one. 
        void OnConversationSpawnConference(SktConversation sender, SktEvents.OnConversationSpawnConferenceArgs e)
        {
            if (sender == liveSession)
            {
                // As participants are conversation-specific, our participants list is now invalid as well.
                // At this point we will need to do a *complete* re-load of both participants and the UI!
                participants.Clear();
                while (participantPanel.Controls.Count > 0) participantPanel.Controls.RemoveAt(0);

                liveSession = e.spawned;
                participants = liveSession.GetParticipants(SktConversation.PARTICIPANTFILTER.ALL);
                int participantsInNewConv = participants.Count;
                for (int i = 0; i < participantsInNewConv; i++)
                    AddParticipantToLiveSession((Participant)participants[i]);
            }
        }

        // In this conversation level callback, we just monitor for incoming call requests.        
        public void OnConversationLocalLiveStatus(SktConversation sender, SktEvents.OnConversationLocalLivestatusArgs e)
        {
            if (e.value == SktConversation.LOCAL_LIVESTATUS.RINGING_FOR_ME)
            {
                if (liveSession != null) return; // busy..
                acceptCallButton.Enabled = true;
                liveSession = sender;
            }
        }

        // If live - drop the call, if "ringing", accept the call
        private void accceptCallButton_Click(object sender, EventArgs e)
        {
            if (liveSession == null) return;

            if (liveSession.P_LOCAL_LIVESTATUS == SktConversation.LOCAL_LIVESTATUS.RINGING_FOR_ME)
            {
                liveSession.JoinLiveSession("");
                callTimer.Start();
            }
            else
            {
                // if we leave volountarily, while other people are still in a live session,
                // the SktMessage.TYPE.ENDED_LIVESESSION will not fire in the conversation.
                // So, we need to switch UI mode to not-in-call state here as well.
                liveSession.LeaveLiveSession(false);
                liveSession = null;
                UiToWaitingMode();
            }
        }

        // Swith call ON and OFF local hold.
        private void holdCallButton_Click(object sender, EventArgs e)
        {
            if (liveSession == null) return;

            if (liveSession.P_LOCAL_LIVESTATUS == SktConversation.LOCAL_LIVESTATUS.IM_LIVE)
            {
                liveSession.HoldMyLiveSession();
                holdCallButton.Image = resumeCallIcon;
            }

            if (liveSession.P_LOCAL_LIVESTATUS == SktConversation.LOCAL_LIVESTATUS.ON_HOLD_LOCALLY)
            {
                liveSession.ResumeMyLiveSession();
                holdCallButton.Image = holdCallIcon;
            }
        }

        // Swith call ON and OFF microphone mute.
        private void muteCallButton_Click(object sender, EventArgs e)
        {
            if (liveSession == null) return;

            // Finding out which one of the participants in the conversation is us.. is a bit tricky.
            SktParticipant.List myselfAsList;
            myselfAsList = liveSession.GetParticipants(SktConversation.PARTICIPANTFILTER.MYSELF);
            SktParticipant myself = myselfAsList[0];

            if (myself.P_VOICE_STATUS == SktParticipant.VOICE_STATUS.SPEAKING) 
            {
                liveSession.MuteMyMicrophone();                
                muteCallButton.Image = unmuteCallIcon;
            }

            if (myself.P_VOICE_STATUS == SktParticipant.VOICE_STATUS.LISTENING)
            {
                liveSession.UnmuteMyMicrophone();
                muteCallButton.Image = muteCallIcon;
            }
        }

        // This is fired from the callTimer, once per second.
        private void OnCallDurationUpdate(object sender, EventArgs e)
        {
            if (liveSession == null)
            {
                callDurationText.Text = "Waiting for incoming calls";
                callTimer.Stop();
                return;
            }
            TimeSpan duration = DateTime.Now.Subtract(callStart);
            callDurationText.Text = "Call duration: " + duration.Minutes.ToString() + ":" + duration.Seconds.ToString();            
        }

        public Form1()
        {
            InitializeComponent();            
            LoadCallIcons();
            callTimer = new Timer();
            callTimer.Interval = 1000;
            callTimer.Tick += OnCallDurationUpdate;
            participants = new SktParticipant.List();
            UiToWaitingMode();

            if (!System.IO.File.Exists(tutorials.path + tutorials.keyfilename))
            {
                throw new Exception(String.Format(
                    "The keyfile (.pfx) path or filename {0} is incorrect?", tutorials.path + tutorials.keyfilename));
            }
            X509Certificate2 cert = new X509Certificate2(tutorials.path + tutorials.keyfilename, tutorials.keypassword);

            skype = new SktSkype(this, cert, false, false, 8963);

            skype.events.OnParticipantSoundLevel        += OnParticipantSoundLevel;
            skype.events.OnParticipantVoiceStatus       += OnParticipantVoiceStatus;
            skype.events.OnSkypeMessage                 += OnSkypeMessage;
            skype.events.OnConversationLocalLivestatus  += OnConversationLocalLiveStatus;
            skype.events.OnConversationSpawnConference  += OnConversationSpawnConference; 
                
            skype.events.OnConnect                      += OnConnect;
            skype.events.OnAccountStatus                += OnAccountStatus;

            skype.NewParticipant = (oid, skp) => { return new Participant(oid, skp); };

            this.FormClosing += Form1_FormClosing;
            skype.LaunchRuntime(tutorials.path + tutorials.runtime, true);
            skype.Connect();
        }

        public void OnConnect(object sender, SktEvents.OnConnectArgs e)
        {
            if (e.success)
            {
                LoginRec login = tutorials.ShowLoginDialog(this);
                username = login.username;
                password = login.password;
                if (!login.abort)
                {
                    account = skype.GetAccount(username);
                    account.LoginWithPassword(password, false, false);
                }
            }
            else throw new Exception("IPC handshake failed with: " + e.handshakeResult);
        }

        public void OnAccountStatus(SktAccount sender, SktEvents.OnAccountStatusArgs e)
        {
            callDurationText.Text = (e.value.ToString());
            if (e.value == SktAccount.STATUS.LOGGED_IN) callDurationText.Text = "Waiting for incoming calls";
            if (e.value == SktAccount.STATUS.LOGGED_OUT)
            {
                DialogResult result = MessageBox.Show(this,
                    "Login failed because of " + sender.P_LOGOUTREASON.ToString(), "Login has failed",
                    MessageBoxButtons.OK, MessageBoxIcon.Exclamation, 
                    MessageBoxDefaultButton.Button1, MessageBoxOptions.RightAlign);
            }
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            skype.Disconnect();
        }

        public void AdjustMainWindowHeight()
        {
            int rows = (participants.Count / 2) + (participants.Count % 2);
            if (rows == 0) rows = 1;
            this.Height = 60 + (rows * Participant.panel_height);
        }

        Image acceptCallIcon;
        Image dropCallIcon;
        Image holdCallIcon;
        Image resumeCallIcon;
        Image muteCallIcon;
        Image unmuteCallIcon;

        public void LoadCallIcons()
        {
            acceptCallIcon      = new Bitmap(tutorials.gfxPath + "AcceptCall.png");
            dropCallIcon        = new Bitmap(tutorials.gfxPath + "DropCall.png");
            holdCallIcon        = new Bitmap(tutorials.gfxPath + "HoldCall.png");
            resumeCallIcon      = new Bitmap(tutorials.gfxPath + "ResumeCall.png");
            muteCallIcon        = new Bitmap(tutorials.gfxPath + "MuteCall.png");
            unmuteCallIcon      = new Bitmap(tutorials.gfxPath + "UnmuteCall.png");
        }

        public void UiToInCallMode()
        {
            acceptCallButton.Image = dropCallIcon;
            muteCallButton.Enabled = true;
            holdCallButton.Enabled = true;
            callStart = DateTime.Now;
        }

        public void UiToWaitingMode()
        {
            participants.Clear();
            while (participantPanel.Controls.Count > 0) participantPanel.Controls.RemoveAt(0);
            acceptCallButton.Image      = acceptCallIcon;
            muteCallButton.Image        = muteCallIcon;
            holdCallButton.Image        = holdCallIcon;
            acceptCallButton.Enabled    = false;
            muteCallButton.Enabled      = false;
            holdCallButton.Enabled      = false;
            acceptCallButton.Text       = "";
            muteCallButton.Text         = "";
            holdCallButton.Text         = "";
            callTimer.Enabled           = false;
            callDurationText.Text = "Waiting for incoming calls";
        }
    }
}
