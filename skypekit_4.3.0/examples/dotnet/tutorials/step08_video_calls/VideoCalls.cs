/*******************************************************************************************
 
  SkypeKit .NET wrapper tutorial 8 - Video Calls

In this tutorial, we will combine the incoming and outgoing calls tutorials and improve 
on them by adding video to the conversation participant list. Instead of having separate 
video windows, we'll re-use the avatar pictures component, displaying live video instead 
of avatars.

For more information on how the Conversation API side of video works, see Detailed Description 
section of the SktVideo class in the reference manual.

*/
using System;
using System.IO;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using SkypeKit;
using System.Security.Cryptography.X509Certificates;
using tutorials_common;

namespace step08_video_calls
{
    public partial class Form1 : Form
    {
        static string username;
        static string password;

        static SktSkype skype;
        static SktAccount account;

        static SktConversation liveSession = null;
        static SktConversation.List conversationList;
        static SktParticipant.List participants;
        static VideoDevice webcam;

        private Timer convUpdateTimer;

        // Just adding link to the participant object here
        public class Video : SktVideo
        {
            public Participant participant = null;
            public Video(uint objectId, SktSkype skype) : base(objectId, skype) { }
        }

        // Adding FPS and current resolution
        public class VideoRenderer : SktVideoRenderer
        {
            public VideoRenderer(SktSkypeBase skype) : base(skype) { }

            public override void AddCustomGraphics()
            {
                var graphics = Graphics.FromImage(bitmap);
                string customString = bitmap.Width.ToString() + "x" + bitmap.Height.ToString() + " FPS: " + FPS.ToString("F");
                graphics.DrawString(customString, new Font("Tahoma", 16), Brushes.Yellow, bitmap.Width - 200, bitmap.Height - 30);
            }
        }

        public void OnVideoStatus(SktVideo sender, SktEvents.OnVideoStatusArgs e)
        {
            Video video = (Video)sender;
            // Checking if we know already, what participant we are linked to
            // participant and video get linked in OnParticipantVideoStatus
            // and also in CheckIfVideoIsAvailable
            if (video.participant == null) return;

            if (e.value == SktVideo.STATUS.AVAILABLE)
            {
                video.Start();
            }
            else
            {
                if (e.value == SktVideo.STATUS.RUNNING)
                {
                    // just in case.. checking if the participant already has renderer running
                    if (!video.participant.pic.IsRunning) video.participant.SwitchPicModeToVideo();
                }
                else
                {
                    // The video is no longer streaming. Switching participant UI back to displaying avatar
                    video.participant.SwitchPicModeToAvatar();
                }
            }
        }

        void OnParticipantVideoStatus(SktParticipant sender, SktEvents.OnParticipantVideoStatusArgs e)
        {
            if (e.value == SktParticipant.VIDEO_STATUS.VIDEO_AVAILABLE)
            {
                // Linking participant and video objects
                Participant participant = (Participant)sender;
                participant.video = (Video)participant.GetVideo();
                participant.video.participant = participant;
                participant.SwitchPicModeToVideo();
            }
        }

        public class Participant : SktParticipant
        {
            public enum PictureMode
            {
                Avatar,
                Video
            }

            public PictureMode pictureMode = PictureMode.Avatar;

            public Panel gui;
            public ProgressBar voiceVolume;
            public Label nameLabel;
            public VideoRenderer pic;
            public Video video;

            public bool isCachedInClient = false;   // indicates whether displayName and avatarImage have values
            public string displayName = null;
            public Image avatarImage = null;

            public static int picHeight = 120; // 1/4th of 640x480
            public static int piclWidth = 160;
            public static int panelHeight = 155;
            public static int panelWidth = 174;

            public static Color liveColor = SystemColors.GradientActiveCaption;
            public static Color nonLiveColor = SystemColors.GradientInactiveCaption;

            public Participant(uint objectId, SktSkype skype)
                : base(objectId, skype)
            {
                gui = new Panel()
                {
                    Width = panelWidth,
                    Height = panelHeight,
                    BorderStyle = BorderStyle.FixedSingle,
                    BackColor = SystemColors.GradientInactiveCaption
                };
                gui.Controls.Add(pic);

                pic = new VideoRenderer(skypeRef)
                {
                    Top = 5,
                    Left = 5,
                    Height = picHeight,
                    Width = piclWidth,
                    SizeMode = PictureBoxSizeMode.StretchImage
                };
                gui.Controls.Add(pic);

                nameLabel = new Label()
                {
                    Top = picHeight + 5,
                    Left = 10,
                    Height = 11,
                    Width = panelWidth - 20,
                    Text = "<n/a>",
                    TextAlign = ContentAlignment.TopCenter,
                    AutoSize = false
                };
                gui.Controls.Add(nameLabel);

                voiceVolume = new ProgressBar()
                {
                    Top = picHeight + 17,
                    Left = 7,
                    Height = 10,
                    Width = panelWidth - 20,
                    Value = 0,
                    Maximum = 10,
                };
                gui.Controls.Add(voiceVolume);
            }

            public void UpdateAvatar(SktContact contact)
            {
                if (avatarImage != null)
                {
                    pic.Image = avatarImage;
                    return;
                };

                if (contact.P_AVATAR_IMAGE != null)
                {
                    MemoryStream stream = new MemoryStream(contact.P_AVATAR_IMAGE);
                    avatarImage = Image.FromStream(stream);
                }
                else
                {
                    if (contact.P_GENDER == 0) avatarImage = defaultMaleAvatar;
                    if (contact.P_GENDER == 1) avatarImage = defaultMaleAvatar;
                    if (contact.P_GENDER == 2) avatarImage = defaultFemaleAvatar;
                }
                pic.Image = avatarImage;
            }

            public void CheckIfVideoIsAvailable()
            {
                if (P_VIDEO_STATUS == SktParticipant.VIDEO_STATUS.VIDEO_AVAILABLE)
                {
                    video = (Video)this.GetVideo();
                    video.participant = this;
                    video.Start();
                }
            }

            public void UpdateGuiPanel()
            {
                if (isCachedInClient)
                {
                    nameLabel.Text = displayName;
                    pic.Image = avatarImage;
                }
                else
                {
                    SktContact contact;
                    contact = skypeRef.GetContact(P_IDENTITY);
                    nameLabel.Text = contact.P_DISPLAYNAME;
                    if (pictureMode == PictureMode.Avatar) UpdateAvatar(contact);
                    isCachedInClient = true;
                };
                CheckIfVideoIsAvailable();
            }

            public void SwitchPicModeToAvatar()
            {
                if (pictureMode != PictureMode.Avatar) pictureMode = PictureMode.Avatar;

                if (pic.IsRunning) pic.Stop();

                SktContact contact;
                contact = skypeRef.GetContact(P_IDENTITY);
                UpdateAvatar(contact);
            }

            // Checking iv renderer is already running, if it's not, linking
            // video object with renderer and starting both
            public void SwitchPicModeToVideo()
            {
                if (pic.IsRunning) return;
                if (pictureMode != PictureMode.Video) pictureMode = PictureMode.Video;
                pic.VideoObject = video;
                video.Start();
                pic.Start();
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
            participant.UpdateGuiPanel();
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

            switch (e.message.P_TYPE)
            {
                case SktMessage.TYPE.STARTED_LIVESESSION:
                    UiToInCallMode();
                    liveSession = e.conversation;
                    break;

                case SktMessage.TYPE.ENDED_LIVESESSION:
                    // This is a temporary workaround for releasing the webcam after live session ends.
                    SktVideo temp = skype.GetPreviewVideo(SktVideo.MEDIATYPE.MEDIA_VIDEO, webcam.name, webcam.path);
                    temp.SetRemoteRendererId(0);
                    // Resetting UI to not-in-live state
                    liveSession = null;
                    UiToWaitingMode();
                    break;
            }

            if (conv.messageHistoryLoaded)
            {
                Message msg = (Message)e.message;
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
        }

        // Checkig for incoming calls here
        public void OnConversationLocalLiveStatus(SktConversation sender, SktEvents.OnConversationLocalLivestatusArgs e)
        {
            switch (e.value)
            {
                case SktConversation.LOCAL_LIVESTATUS.RINGING_FOR_ME:
                    if (liveSession != null) return; // busy..
                    liveSession = sender;
                    // forcibly switching the UI to the ringing conversation
                    if (convListBox.SelectedItem != sender) convListBox.SelectedItem = sender;
                    UiToRinginggMode();
                    break;
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

        // This updates the conversation GUI, after 300ms delay.
        // The delay enables the GUI to "skip" conversations when the user is changing
        // conversation list selection rapidly, making the UI more responsive.
        private void OnDelayedUpdate(Object o, EventArgs e)
        {
            convUpdateTimer.Stop();
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

        // When another conversation gets selected in the UI
        private void listBox1_SelectedIndexChanged(object sender, System.EventArgs e)
        {
            if (convListBox.SelectedItem == null) return;
            convUpdateTimer.Stop();
            convUpdateTimer.Start();
        }

        // Keeping conversation list and listbox up-to-date
        public void OnSkypeConversationListChange(SktSkype sender, SktEvents.OnSkypeConversationListChangeArgs e)
        {
            if (e.type == SktConversation.LIST_TYPE.INBOX_CONVERSATIONS)
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
            conv.RingOthers(names, true, "");
            liveSession = conv;
        }

        public Form1()
        {
            InitializeComponent();
            LoadImages();
            UiToWaitingMode();
            this.AcceptButton = sendButton;

            if (!System.IO.File.Exists(tutorials.path + tutorials.keyfilename))
            {
                throw new Exception(String.Format(
                    "The keyfile (.pfx) path or filename {0} is incorrect?", tutorials.path + tutorials.keyfilename));
            }

            X509Certificate2 cert = new X509Certificate2(tutorials.path + tutorials.keyfilename, tutorials.keypassword);
            skype = new SktSkype(this, cert, true, false, 8963);

            skype.NewVideo = (oid, skp) => { return new Video(oid, skp); };
            skype.NewParticipant = (oid, skp) => { return new Participant(oid, skp); };
            skype.NewConversation = (oid, skp) => { return new Conversation(oid, skp); };
            skype.NewMessage = (oid, skp) => { return new Message(oid, skp); };

            skype.events.OnConnect += OnConnect;
            skype.events.OnAccountStatus += OnAccountStatus;
            skype.events.OnParticipantSoundLevel += OnParticipantSoundLevel;
            skype.events.OnParticipantVoiceStatus += OnParticipantVoiceStatus;
            skype.events.OnSkypeMessage += OnSkypeMessage;
            skype.events.OnConversationLocalLivestatus += OnConversationLocalLiveStatus;
            skype.events.OnConversationSpawnConference += OnConversationSpawnConference;
            skype.events.OnSkypeConversationListChange += OnSkypeConversationListChange;
            skype.events.OnSkypeAvailableDeviceListChange += OnSkypeAvailableDeviceListChange;
            skype.events.OnSkypeAvailableVideoDeviceListChange += OnSkypeAvailableVideoDeviceListChange;
            skype.events.OnParticipantVideoStatus += OnParticipantVideoStatus;
            skype.events.OnVideoStatus += OnVideoStatus;

            convListBox.SelectedIndexChanged += listBox1_SelectedIndexChanged;
            this.FormClosing += Form1_FormClosing;
            sendButton.Click += sendButton_Click;
            callButton.Click += callButton_Click;

            skype.LaunchRuntime(tutorials.path + tutorials.runtime, true);
            skype.Connect();

            convUpdateTimer = new System.Windows.Forms.Timer();
            convUpdateTimer.Interval = 300;
            convUpdateTimer.Tick += OnDelayedUpdate;

            //tutorials_common.tutorials.ShowLogWindow(this, skype);
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
                UpdateVideoDevices();
            }

            if (e.value == SktAccount.STATUS.LOGGED_OUT)
            {
                this.msgLog.AppendText("Login failed because of " + sender.P_LOGOUTREASON + "\r\n");
            }
        }

        public class VideoDevice
        {
            public string name;
            public string path;
            public VideoDevice(string name, string path) { this.name = name; this.path = path; }
            public override string ToString() { return name; }
        }

        public void UpdateVideoDevices()
        {
            List<String> deviceNames;
            List<String> deviceIds;
            uint devCount;
            skype.GetAvailableVideoDevices(out deviceNames, out deviceIds, out devCount);

            webcamSelector.Items.Clear();

            if (devCount == 0)
            {
                webcamSelector.Enabled = false;
                return;
            }

            for (int i = 0; i < devCount; i++)
            {
                webcamSelector.Items.Add(new VideoDevice(deviceNames[i], deviceIds[i]));
            }
            webcamSelector.Enabled = true;
            webcamSelector.SelectedIndex = 0;
        }

        public void OnSkypeAvailableVideoDeviceListChange(SktSkype sender, SktEvents.OnSkypeAvailableVideoDeviceListChangeArgs e)
        {
            UpdateVideoDevices();
        }

        private void webcamSelector_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (webcamSelector.SelectedItem == null) return;
            webcam = (VideoDevice)webcamSelector.SelectedItem;
            
            if (liveSession == null) return;
            SktParticipant.List me = liveSession.GetParticipants(SktConversation.PARTICIPANTFILTER.MYSELF);
            SktVideo mirror = me[0].GetVideo();
            mirror.SelectVideoSource(SktVideo.MEDIATYPE.MEDIA_VIDEO, webcam.name, webcam.path, false);
            this.Text = webcam.name;

        }

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

        private void micSelector_SelectedIndexChanged_1(object sender, EventArgs e)
        {
            ReselectAudioDevices();
        }

        private void speakerSelector_SelectedIndexChanged_1(object sender, EventArgs e)
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

        static Bitmap startCallIcon;
        static Bitmap joinCallIcon;
        static Bitmap dropCallIcon;
        static Bitmap sendMessageIcon;

        static Bitmap defaultMaleAvatar;
        static Bitmap defaultFemaleAvatar;

        public void LoadImages()
        {
            startCallIcon = new Bitmap(tutorials.gfxPath + "StartCall.png");
            dropCallIcon = new Bitmap(tutorials.gfxPath + "DropCall.png");
            joinCallIcon = new Bitmap(tutorials.gfxPath + "JoinCall.png");
            sendMessageIcon = new Bitmap(tutorials.gfxPath + "SendMessage.png");
            defaultMaleAvatar = new Bitmap(tutorials.gfxPath + "avatar_m.png");
            defaultFemaleAvatar = new Bitmap(tutorials.gfxPath + "avatar_f.png");

            sendButton.Image = sendMessageIcon;
            callButton.Text = "";
            sendButton.Text = "";
        }
    }
}
