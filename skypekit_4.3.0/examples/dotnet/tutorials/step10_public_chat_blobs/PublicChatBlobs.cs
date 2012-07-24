/*************************************************************************************
 
  SkypeKit .NET wrapper tutorial 10 - Public Chat Blobs

Normally, to join an already existing Skype conversation, one needs to get added to 
it by someone already in that conversation. The public chat mechanism provides a way 
to join a conversation remotely, without explicit invitation.

The basic problem with joining a chat that exists somewhere - but not in the local 
machine - is that the chat needs to be identified somehow. Public chats do this 
identification with join BLOBs. A join BLOB is a globally unique string identifier 
of a conversation. Once a join BLOB is obtained, it can be used to retrieve matching 
conversation object and join it. For example, Windows desktop client provides following 
URI syntax for joining a public chat:

<a href="skype:?chat&blob=BLOB">Click to join my public chat!!</a>

where BLOB is the actual globally unique conversation ID.

In Windows desktop client, a conversation BLOB can be had by typing /get uri in the chat. 
In SkypeKit, you can use SktConversation.GetJoinBlob method.

But first there are a few limitations how public chats can be made. SkypeKit does not 
support converting one-on-one dialogs into public chats. To create a new public chat, 
you will have to create a new conversation. Also, before asking for the join BLOB, you will 
need to set conversation options. Those options are stored in Conversation class properties 
that start with P_OPT_ prefix.

  In this tutorial, we will:
 
  1. Create a new Public Chat
  2. Set all the necessary chat options to enable joining without explicit invitation.
  3. Compose a html link for joining the chat.
  4. Display that link in a web browser.

*/

using System;
using System.Drawing;
using System.Windows.Forms;
using System.Security.Cryptography.X509Certificates;
using SkypeKit;
using tutorials_common;

namespace step10_public_chat_blobs
{
    public partial class PublicChatBlobs : Form
    {
        static string username;
        static string password;

        static SktSkype skype;
        static SktAccount account;

        private void createChatBtn_Click(object sender, EventArgs e)
        {
            // Creating a blank conversation
            SktConversation ourChat = skype.CreateConference();

            // Setting chat options. See other Conversation class P_OPT-prefixed properties for more information
            ourChat.SetOption((int)SktConversation.PropKeys.P_OPT_JOINING_ENABLED, Convert.ToByte(true));
            ourChat.SetOption((int)SktConversation.PropKeys.P_OPT_ENTRY_LEVEL_RANK, (uint)SktParticipant.RANK.SPEAKER);
            ourChat.SetOption((int)SktConversation.PropKeys.P_OPT_DISCLOSE_HISTORY, Convert.ToByte(true));

            // Retrieving the ID BLOB and composing a link
            string blob = ourChat.GetJoinBlob();
            string msg = "<p>New Public Chat has been created. The chat ID (BLOB) is:\n<br>" + blob + "</p>\n";
            string link = "<p><a href=\"skype:?chat&blob=" + blob + "\">Click here to join the new public chat.</a></p>\n";

            // Displaying the link in a web browser component
            htmlViewer.Navigate("about:blank");
            htmlViewer.Document.Write(string.Empty);
            htmlViewer.DocumentText = "<html>\n<body>\n<pre>" + msg + link + "</pre>\n</body>\n</html>";
        }


        public PublicChatBlobs()
        {
            InitializeComponent();
            feedbackLabel.Text = "";

            if (!System.IO.File.Exists(tutorials.path + tutorials.keyfilename))
            {
                throw new Exception(String.Format(
                    "The keyfile (.pfx) path or filename {0} is incorrect?", tutorials.path + tutorials.keyfilename));
            }

            X509Certificate2 cert = new X509Certificate2(tutorials.path + tutorials.keyfilename, tutorials.keypassword);
            skype = new SktSkype(this, cert, false, false, 8963);

            skype.events.OnConnect += OnConnect;
            skype.events.OnAccountStatus += OnAccountStatus;

            this.FormClosing += Form1_FormClosing;
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
            feedbackLabel.Text = "Login in progress.. " + sender.P_STATUS.ToString();
            if (e.value == SktAccount.STATUS.LOGGED_IN) feedbackLabel.Text = "";

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

    }
}
