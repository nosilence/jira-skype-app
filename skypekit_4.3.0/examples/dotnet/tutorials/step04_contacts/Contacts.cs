/*************************************************************************************
 
  SkypeKit .NET wrapper tutorial 4 - Chat
  
 In this tutorial, we will:
 
 1. Display the list of all authorized contacts (SktContactGroup.TYPE.ALL_BUDDIES)
 2. Catch online status events of those contacts (ONLINE, AWAY, etc.)
 3. Make our contact listbox display and update nice online status icons.
 4. For the currently selected contact, we will also display:
    4.1 Contact's avatar picture
    4.2 Timestamp of last known appearance online
    4.3 Moodmessage (SktContact.P_MOOD_TEXT)
    
 Of visual components, we have the following on our Form1:
 
 1. ListBox contactListbox - for our contactlist
 2. PictureBox avatarBox - for avatar picture
 3. Label onlineStatusText - for online status as text
 4. Label lastOnlineText - for last online timestamp
 5. Label moodText - for the mood message
 6. Label lognFeedback - for SktAccount.P_STATUS updates during login
 
 Unlike in the last tutorial with Conversations, this time it makes little sense
 to make our own descendant of SktContact class. Adding ToString() override
 is of no use - if we want to display icons in our listbox, we will need to
 make our own ListBox DrawItem function anyway.
 
 The actual icons we can get from the Skype Developer website, under the
 Design section. At this point it becomes apparent that there are quite a bit
 more possible online statuses, than there are icons. Thus, the set of internal
 statuses needs to be mapped to available visuals.
 
 The rest - with possible exception of ListBox item drawing, is fairly
 strightforward.
*/

using System;
using System.IO;
using System.Drawing;
using System.Windows.Forms;
using System.Security.Cryptography.X509Certificates;
using SkypeKit;
using tutorials_common;

namespace step04_contacts
{
    public partial class Form1 : Form
    {
        string username;
        string password;

        SktSkype skype;
        SktAccount account;

        SktContact.List contactList;

        // With each listbox selection change - we update avatar picture, status as text,
        // and last online timestamp labels
        private void contactListBox_SelectedIndexChanged(object sender, System.EventArgs e)
        {
            if (contactListBox.SelectedItem == null) return;
            var contact = (SktContact)contactListBox.SelectedItem;

            moodText.Text = contact.P_MOOD_TEXT;

            // P_AVAILABILITY and P_LASTONLINE_TIMESTAMP make very little
            // sense for PSTN contacts..
            if (contact.P_TYPE == SktContact.TYPE.SKYPE)
            {
                onlineStatusText.Text = contact.P_AVAILABILITY.ToString();
                lastOnlineText.Text = contact.P_LASTONLINE_TIMESTAMP.ToString();
            }
            else
            {
                onlineStatusText.Text = "n/a";
                lastOnlineText.Text = "n/a";
            }
            
            // Allways check for nullness of byte[] properties!
            if (contact.P_AVATAR_IMAGE != null)
            {
                // displaying avatar picture
                MemoryStream stream = new MemoryStream(contact.P_AVATAR_IMAGE);
                avatarBox.Image = Image.FromStream(stream);
            }
            else
            {
                // if the contact has no avatar image, we can use a default one
                if (contact.P_GENDER == 0) avatarBox.Image = defaultMaleAvatar;
                if (contact.P_GENDER == 1) avatarBox.Image = defaultMaleAvatar;
                if (contact.P_GENDER == 2) avatarBox.Image = defaultFemaleAvatar;
            }
        }

        // when contact online status changes
        public void OnContactAvailability(SktContact sender, SktEvents.OnContactAvailabilityArgs e)
        {
            contactListBox.Refresh();
            if (contactListBox.SelectedItem == sender) onlineStatusText.Text = e.value.ToString();
        }

        // when contact mood message changes
        public void OnContactMoodText(SktContact sender, SktEvents.OnContactMoodTextArgs e)
        {
            if (contactListBox.SelectedItem == sender) moodText.Text = e.value.ToString();
        }

        // when contact last online timestamp changes
        public void OnContactLastOnlineTimestamp(SktContact sender, SktEvents.OnContactLastonlineTimestampArgs e)
        {
            if (contactListBox.SelectedItem == sender) lastOnlineText.Text = e.value.ToString();            
        }

        // These cuties will hold our pre-loaded online status icons and default avatars
        Bitmap iconAway;
        Bitmap iconBlocked;
        Bitmap iconDoNotDisturb;
        Bitmap iconInvisible;
        Bitmap iconOffline;
        Bitmap iconOnline;
        Bitmap defaultMaleAvatar;
        Bitmap defaultFemaleAvatar;

        public void LoadPresenceIcons()
        {
            iconAway            = new Bitmap(tutorials.gfxPath + "PresenceAway_14x14_s1.png");            
            iconBlocked         = new Bitmap(tutorials.gfxPath + "PresenceBlocked_14x14_s1.png");
            iconDoNotDisturb    = new Bitmap(tutorials.gfxPath + "PresenceDoNotDisturb_14x14_s1.png");
            iconInvisible       = new Bitmap(tutorials.gfxPath + "PresenceInvisible_14x14_s1.png");
            iconOffline         = new Bitmap(tutorials.gfxPath + "PresenceOffline_14x14_s1.png");
            iconOnline          = new Bitmap(tutorials.gfxPath + "PresenceOnline_14x14_s1.png");
            defaultMaleAvatar   = new Bitmap(tutorials.gfxPath + "avatar_m.png");
            defaultFemaleAvatar = new Bitmap(tutorials.gfxPath + "avatar_f.png");
        }


        // Ok, now things will get a bit hairy. We will need to "manually" draw a listbox item
        private int mouseIndex = -1;

        private void contactListBox_DrawItem(object sender, DrawItemEventArgs e)
        {
            if (e.Index == -1) return;
            SktContact contact = (SktContact)contactListBox.Items[e.Index];
            
            Brush textBrush = SystemBrushes.WindowText;
            if (e.Index > -1)
            {
                // Drawing the frame
                if (e.Index == mouseIndex)
                {
                    e.Graphics.FillRectangle(SystemBrushes.HotTrack, e.Bounds);
                    textBrush = SystemBrushes.HighlightText;
                }
                else
                {
                    if ((e.State & DrawItemState.Selected) == DrawItemState.Selected)
                    {
                        e.Graphics.FillRectangle(SystemBrushes.Highlight, e.Bounds);
                        textBrush = SystemBrushes.HighlightText;
                    }
                    else
                        e.Graphics.FillRectangle(SystemBrushes.Window, e.Bounds);
                }

                // Drawing the text
                e.Graphics.DrawString(contact.P_DISPLAYNAME, 
                    e.Font, textBrush, e.Bounds.Left + 20, e.Bounds.Top);

                // Mapping contact status to available icons and drawing it
                switch (contact.P_AVAILABILITY)
                {
                    case SktContact.AVAILABILITY.NOT_AVAILABLE:
                    case SktContact.AVAILABILITY.NOT_AVAILABLE_FROM_MOBILE:
                    case SktContact.AVAILABILITY.AWAY_FROM_MOBILE:
                    case SktContact.AVAILABILITY.AWAY:
                        e.Graphics.DrawImage(iconAway, e.Bounds.Left + 2, e.Bounds.Top + 1);
                        break;

                    case SktContact.AVAILABILITY.BLOCKED_SKYPEOUT:
                    case SktContact.AVAILABILITY.BLOCKED: 
                        e.Graphics.DrawImage(iconBlocked, e.Bounds.Left + 2, e.Bounds.Top + 1);
                        break;

                    case SktContact.AVAILABILITY.DO_NOT_DISTURB:
                    case SktContact.AVAILABILITY.DO_NOT_DISTURB_FROM_MOBILE:
                        e.Graphics.DrawImage(iconDoNotDisturb, e.Bounds.Left + 2, e.Bounds.Top + 1);
                        break;

                    case SktContact.AVAILABILITY.SKYPEOUT:
                    case SktContact.AVAILABILITY.INVISIBLE:
                    case SktContact.AVAILABILITY.CONNECTING:
                        e.Graphics.DrawImage(iconInvisible, e.Bounds.Left + 2, e.Bounds.Top + 1);
                        break;

                    case SktContact.AVAILABILITY.OFFLINE_BUT_VM_ABLE:
                    case SktContact.AVAILABILITY.OFFLINE_BUT_CF_ABLE:
                    case SktContact.AVAILABILITY.OFFLINE:
                        e.Graphics.DrawImage(iconOffline, e.Bounds.Left + 2, e.Bounds.Top + 1);
                        break;

                    case SktContact.AVAILABILITY.SKYPE_ME:
                    case SktContact.AVAILABILITY.SKYPE_ME_FROM_MOBILE:
                    case SktContact.AVAILABILITY.ONLINE_FROM_MOBILE:
                    case SktContact.AVAILABILITY.ONLINE:
                        e.Graphics.DrawImage(iconOnline, e.Bounds.Left + 2, e.Bounds.Top + 1);
                        break;

                    default:
                        e.Graphics.DrawImage(iconInvisible, e.Bounds.Left + 2, e.Bounds.Top + 1);
                        break;
                }                
            }
        }

        // Here we keep our contactList up-to-date, in case contacts get added or removed to the
        // ALL_BUDDIES list. Not going to happen in this tutorial but in a real application, it will.
        public void OnContactGroupChange(SktContactGroup sender, SktEvents.OnContactGroupChangeArgs e)
        {
            if (sender.P_TYPE != SktContactGroup.TYPE.ALL_BUDDIES) return;
            if (e.contact == null) return; // It can be null sometimes.

            // if it was already in, a change means it was removed
            if (contactList.Contains(sender))
            {
                contactList.Remove(sender);
                contactListBox.Items.Remove(sender);
            }
            else // if it wasn't already in, it must have been added
            {
                contactList.Add(sender);
                contactListBox.Items.Add(sender);
            };
        }

        public Form1()
        {
            InitializeComponent();
            loginFeedback.Text = "";

            LoadPresenceIcons();

            if (!System.IO.File.Exists(tutorials.path + tutorials.keyfilename))
            {
                throw new Exception(String.Format(
                    "The keyfile (.pfx) path or filename {0} is incorrect?", tutorials.path + tutorials.keyfilename));
            }

            X509Certificate2 cert = new X509Certificate2(tutorials.path + tutorials.keyfilename, tutorials.keypassword);
            skype = new SktSkype(this, cert, false, false, 8963);

            skype.events.OnConnect += OnConnect;
            skype.events.OnAccountStatus += OnAccountStatus;
            skype.events.OnContactAvailability += OnContactAvailability;
            skype.events.OnContactMoodText += OnContactMoodText;
            skype.events.OnContactLastonlineTimestamp += OnContactLastOnlineTimestamp;
            skype.events.OnContactGroupChange += OnContactGroupChange;
            
            contactListBox.SelectedIndexChanged += contactListBox_SelectedIndexChanged;
            contactListBox.DrawItem += contactListBox_DrawItem;

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
            else
            {
                throw new Exception("IPC handshake failed with: " + e.handshakeResult);
            }
        }

        public void OnAccountStatus(SktAccount sender, EventArgs e)
        {
            loginFeedback.Text = "Login in progress.. " + sender.P_STATUS.ToString();
            if (sender.P_STATUS == SktAccount.STATUS.LOGGED_IN)
            {
                loginFeedback.Text = "";

                SktContactGroup allAuthorizedContacts;
                allAuthorizedContacts = skype.GetHardwiredContactGroup(SktContactGroup.TYPE.ALL_BUDDIES);

                contactList = allAuthorizedContacts.GetContacts();

                foreach (SktContact contact in contactList) { contactListBox.Items.Add(contact); }
            }

            if (sender.P_STATUS == SktAccount.STATUS.LOGGED_OUT)
            {
                DialogResult result = MessageBox.Show(this,
                    "Login failed because of " + sender.P_LOGOUTREASON.ToString(),
                    "Login has failed",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Exclamation,
                    MessageBoxDefaultButton.Button1,
                    MessageBoxOptions.RightAlign);
            }
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            skype.Disconnect();
        }
    }
}
