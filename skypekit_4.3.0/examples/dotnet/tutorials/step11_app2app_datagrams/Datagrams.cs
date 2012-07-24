/*************************************************************************************
 
  SkypeKit .NET wrapper tutorial 11 - App2app Datagrams

The App2app feature in Skype allows two Skype clients to exchange information without 
resorting to "special chat messages". An example would be a SkypeKit-based chess game, 
where moves are sent over a custom protocol, using app2app for move transport. 
 
There are two ways for sending app2app data packets - datagrams and streams. In this 
example we will look into datagrams.

For app2app connection to work, all the clients in an app2app session will need to create 
an identically named "application" and then connect to eachother. For a connection between any 
two participants to work, connect has to happen on both sides.

In this tutorial, we will write a little chat client, that uses app2app datagrams for 
messages, rather than the Conversation API. 
 
Note that to test this tutorial, you will need to have two instances - to communicate with 
eachother. The easiest way would be to run this tutorial in two separate machines (virtual 
machine would do, if you have a virtual Windows XP). 

Another way would be to run two copies of this tutorial on the same machine. In that case 
however, you would need to rename the SkypeKit tutorial for one of the instances, and use 
a non-default IPC port in skype instance constructor. Otherwise, the second instance 
of the tutorial would kill the 1st one's runtime, and even if it didnät do that - 
there would be a port conflict. Each client-runtime pair needs its own port for communication.

Yet another way is to use standard Skype desktop client in combination with Desktop API 
terminal. App2app works in Desktop API as well, and as long as you use the same application 
name, you can send datagrams between Desktop API based plugin and a SkypeKit-based client.

A Desktop API command terminal can be downloaded from http://developer.skype.com/accessories/tracer

-> create application TestApp1                      // application name has to match with counter-party
-> alter application TestApp1 connect <skypename>   // connecting the app to counter-party
<- ALTER APPLICATION TestApp1 CONNECT <skypename>   // feedback message..
<- APPLICATION TestApp1 CONNECTING <skypename>      // another feedback message..
<- APPLICATION TestApp1 STREAMS <streamid>          // and another, in this message we will get the stream ID, 
                                                    // in form of <skypename>:<id>, for example, "mybuddy:1"

After that, we can now send datagrams from the Desktop API terminal side like that:
-> alter application TestApp1 datagram mybuddy:1 this_will_be_the_datagram_text

*/

using System;
using System.Text;
using System.Windows.Forms;
using System.Security.Cryptography.X509Certificates;
using SkypeKit;
using tutorials_common;

namespace step11_app2app_datagrams
{
    public partial class Datagrams : Form
    {
        static string username;
        static string password;

        static SktSkype skype;
        static SktAccount account;

        SktContact.List contactList;

        string appName = "TestApp1";    // Application name has to be the same for all participants
        string streamName = "";         // A stream ID is used for sending messages A stream name 
                                        // is of form <skypename>:<n> where n is the stream number ("echo123:1").
                                        // Stream names can be obtained in OnSkypeApp2AppStreamListChange callback.

        private class MyContact : SktContact
        {
            public MyContact(uint objectId, SktSkype skype) : base(objectId, skype) { }
            public override string ToString() { return P_SKYPENAME; }
        }

        // Note that we are using SKYPE_BUDDIES filter for our connectable contact list.
        // The reason not to use ONLINE_BUDDIES filter is that online statuses take rather
        // long time to update. As with calls, it is better to attempt connection to an 
        // apparently offline contact and then handle failure, rather than to only allow
        // connections to people who are known for sure to be online.
        private void UpdateContactCombo()
        {
            SktContactGroup allAuthorizedContacts;
            allAuthorizedContacts = skype.GetHardwiredContactGroup(SktContactGroup.TYPE.SKYPE_BUDDIES);
            contactList = allAuthorizedContacts.GetContacts();
            contactCombo.Items.Clear();
            foreach (SktContact contact in contactList) { contactCombo.Items.Add(contact); }
            contactCombo.Enabled = (contactCombo.Items.Count != 0);
        }

        public void OnContactGroupChange(SktContactGroup sender, SktEvents.OnContactGroupChangeArgs e)
        {
            if (sender.P_TYPE == SktContactGroup.TYPE.SKYPE_BUDDIES) UpdateContactCombo();
        }

        // Creating app2app application and connecting to a contact.
        private void connectBtn_Click(object sender, EventArgs e)
        {
            if (contactCombo.Items.Count == 0) return;
            if (contactCombo.SelectedIndex == -1) return;

            SktContact contact = (SktContact)contactCombo.Items[contactCombo.SelectedIndex];

            try
            {
                msgHistory.AppendText("Creating application " + appName + "\r\n");
                skype.App2AppCreate(appName);
            }
            catch (Exception ex)
            {
                msgHistory.AppendText(ex.Message);
                return;
            }

            msgHistory.AppendText("Connecting " + appName+ " to  " + contact.P_SKYPENAME + "\r\n");
            skype.App2AppConnect(appName, contact.P_SKYPENAME);
        }

        // Here we check for three things..
        void OnSkypeApp2AppStreamListChange(SktSkype sender, SktEvents.OnSkypeApp2AppStreamListChangeArgs e)
        {
            // ALL_STREAMS & stream list is non-null -> a new app2app connection is established.
            // This gets fired when both sides have called App2AppConnect. This is also the place to
            // get and remember the stream name.
            if ((e.listType == SktSkype.APP2APP_STREAMS.ALL_STREAMS) & (e.streams != null))
            {
                msgHistory.AppendText("App2app connection established.\r\n");
                streamName = e.streams[0];
                sendBtn.Enabled = true;
                return;            
            }

            // ALL_STREAMS & stream list is null -> connection just went down. This happens when the remote
            // participant calls App2appDisconnect or closes the client.
            if ((e.listType == SktSkype.APP2APP_STREAMS.ALL_STREAMS) & (e.streams == null))
            {
                msgHistory.AppendText("App2app connection closed.\r\n");
                sendBtn.Enabled = false;
                return;
            }

            // SENDING_STREAMS & streams list is not null -> we have just sent some data.
            if ((e.listType == SktSkype.APP2APP_STREAMS.SENDING_STREAMS) & (e.streams != null))
            {
                msgHistory.AppendText("Datagram sent.\r\n");
                return;
            }
        }

        // This gets fired when we receive a datagram
        void OnSkypeApp2AppDatagram(SktSkype sender, SktEvents.OnSkypeApp2AppDatagramArgs e)
        {
            string msg = Encoding.UTF8.GetString(e.data, 0, e.data.Length);
            msgHistory.AppendText("Incoming datagram: " + msg + "\r\n");
        }

        // .. and this we use for sending a Datagram
        void SendDatagram()
        {
            if (msgEdit.Text == "") return;

            byte[] buffer = Encoding.UTF8.GetBytes(msgEdit.Text);
            skype.App2AppDatagram(appName, streamName, buffer);
            msgEdit.Clear();
        }

        public Datagrams()
        {
            InitializeComponent();

            if (!System.IO.File.Exists(tutorials.path + tutorials.keyfilename))
            {
                throw new Exception(String.Format(
                    "The keyfile (.pfx) path or filename {0} is incorrect?", tutorials.path + tutorials.keyfilename));
            }

            X509Certificate2 cert = new X509Certificate2(tutorials.path + tutorials.keyfilename, tutorials.keypassword);
            skype = new SktSkype(this, cert, false, false, 8963);

            skype.NewContact = (oid, skp) => { return new MyContact(oid, skp); };

            skype.events.OnConnect += OnConnect;
            skype.events.OnAccountStatus += OnAccountStatus;

            skype.events.OnContactGroupChange += OnContactGroupChange;

            skype.events.OnSkypeApp2AppStreamListChange += OnSkypeApp2AppStreamListChange;
            skype.events.OnSkypeApp2AppDatagram += OnSkypeApp2AppDatagram;

            this.FormClosing += Form1_FormClosing;
            this.msgEdit.KeyDown += new System.Windows.Forms.KeyEventHandler(editKeyDown);
            
            skype.LaunchRuntime(tutorials.path + tutorials.runtime, true, "");
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

        public void OnAccountStatus(SktAccount sender, SktEvents.OnAccountStatusArgs e)
        {
            msgHistory.AppendText(e.value.ToString() + "\r\n");
            if (e.value == SktAccount.STATUS.LOGGED_IN)
            {
                this.UpdateContactCombo();
                this.Text = this.Text + " (" + account.P_SKYPENAME + ")";
            }

            if (e.value == SktAccount.STATUS.LOGGED_OUT)
            {
                DialogResult result = MessageBox.Show(this,
                    "Login failed because of " + sender.P_LOGOUTREASON.ToString(), "Login has failed",
                    MessageBoxButtons.OK, MessageBoxIcon.Exclamation, MessageBoxDefaultButton.Button1, MessageBoxOptions.RightAlign);
            }
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            skype.Disconnect();
        }

        private void sendBtn_Click(object sender, EventArgs e)
        {
            SendDatagram();
        }

        private void editKeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter) this.SendDatagram();
        }
    }
}
