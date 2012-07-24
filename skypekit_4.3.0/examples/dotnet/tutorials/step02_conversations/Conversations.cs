/*************************************************************************************
 
  SkypeKit .NET wrapper tutorial 2 - Conversations
 
 This tutorial demonstrate how to descend your own classes from SkypeKit classes,
 retrieve conversation list, and messages of a conversation.
 
  Here we will:
  
  1. Retrieve inbox conversation list into a ListBox, with SktConversation.P_DISPLAYNAME 
     property displayed for each item.
     
  2. Then we will assign our own ListBox.SelectedIndexChanged callback,
     that retrieves message history for the last week for ćurrently selected 
     conversation, composes a string out of it, and puts it into a multilne TextEdit.
     
  Note that the conversations in this tutorial are quite static - there is no reaction
  to incoming messages. Let's keep things simple for now and make the conversations
  more lively in the next tutorial..
*/

using System;
using System.Windows.Forms;
using SkypeKit;
using System.Security.Cryptography.X509Certificates;
using tutorials_common;

namespace step02_conversations
{
    public partial class Form1 : Form
    {
        string username;
        string password;

        SktSkype skype;
        SktAccount account;

        SktConversation.List conversationList;

        // overriding SktConversation.ToString so we can insert Conversation objects
        // directly into our conversationListBox. We will need to register this new
        // class in our Form constructor.
        public class MyConversation : SktConversation
        {
            public MyConversation(uint objectId, SktSkype skype) :base (objectId, skype) { }
            
            public override string ToString()
            {
                return P_DISPLAYNAME;
            }
        }

        // Connection to runtime came up. Prompting user for login
        public void OnConnect(object sender, SktEvents.OnConnectArgs e)
        {
            if (e.success)
            {                
                this.messageLog.AppendText("Connection to runtime is up. Lets see if we can log in..\r\n");

                LoginRec login = tutorials.ShowLoginDialog(this);
                username = login.username;
                password = login.password;
                if (!login.abort)
                {
                    // This will cause OnAccountStatus to fire, some time later
                    account = skype.GetAccount(username);
                    account.LoginWithPassword(password, false, false);
                }
            }
            else
            {
                this.messageLog.AppendText("IPC handshake failed with: " + e.handshakeResult + "\r\n");
            }
        }

        // Logging account status, and when it goes LOGGED_IN, populate our listbox.
        public void OnAccountStatus(SktAccount sender, SktEvents.OnAccountStatusArgs e)
        {
            this.messageLog.AppendText(e.value.ToString() + "\r\n");

            if (e.value == SktAccount.STATUS.LOGGED_IN)
            {
                this.messageLog.AppendText("Retrieving conversation list..\r\n");

                conversationList = skype.GetConversationList(SktConversation.LIST_TYPE.INBOX_CONVERSATIONS);

                foreach (SktConversation conversation in conversationList)
                {
                    // Because we overrided SktConverstaion.ToString, we can add
                    // Conversation objects directly, to a standard ListBox.
                    this.conversationListBox.Items.Add(conversation);
                }
            }

            if (e.value == SktAccount.STATUS.LOGGED_OUT)
            {
                this.messageLog.AppendText("Login failed because of " + sender.P_LOGOUTREASON + "\r\n");
            }
        }

        // Re-composing entire history on every conv. selection change is quite inefficient,
        // but let's keep things relatively simple for now.
        private void listBox1_SelectedIndexChanged(object sender, System.EventArgs e)
        {
            messageLog.Clear();

            var conv = (MyConversation)conversationListBox.Items[conversationListBox.SelectedIndex];
            SktMessage.List newMessages;
            SktMessage.List oldMessages;

            // let's get history of now minus one week
            DateTime t = DateTime.Now.Subtract(TimeSpan.FromDays(7));

            // This returns two lists - recent message history and unread messages.
            conv.GetLastMessages(out oldMessages, out newMessages, t);
            
            // Composing message history into log string
            string log = "";           
            foreach (SktMessage msg in oldMessages)
            {
                log = log + msg.P_TIMESTAMP.ToLocalTime() + " " + msg.P_AUTHOR_DISPLAYNAME + " " + msg.P_BODY_XML + "\r\n"; 
            }

            // Note that undread (unconsumed) messages will be in this list regardless 
            // of how old they are. So, the 7 days limit does not apply here.
            foreach (SktMessage msg in newMessages)
            {
                log = log + "NEW " + msg.P_TIMESTAMP.ToLocalTime() + " " + msg.P_AUTHOR_DISPLAYNAME + " " + msg.P_BODY_XML + "\r\n"; 
            }
                        
            if (log == "") log = "No messages in this conversation in last week.";
            
            // and here goes, huge string to our TextEdit..
            messageLog.Text = log;
            messageLog.SelectionStart = messageLog.Text.Length;
            messageLog.ScrollToCaret();            
        }

        public Form1()
        {
            InitializeComponent();

            if (!System.IO.File.Exists(tutorials.path + tutorials.keyfilename))
            {
                throw new Exception(String.Format(
                    "The keyfile (.pfx) path or filename {0} is incorrect?", tutorials.path + tutorials.keyfilename));
            }

            X509Certificate2 cert = new X509Certificate2(tutorials.path + tutorials.keyfilename, tutorials.keypassword);

            skype = new SktSkype(this, cert, false, false, 8963);

            // Registration of our MyConversation class
            skype.NewConversation = (oid, skp) => { return new MyConversation(oid, skp); };

            // skype callbacks
            skype.events.OnConnect += OnConnect;
            skype.events.OnAccountStatus += OnAccountStatus;

            // other necessary callbacks
            conversationListBox.SelectedIndexChanged += listBox1_SelectedIndexChanged;
            this.FormClosing += Form1_FormClosing;

            // this will cause OnConnect to fire, some time later..
            skype.LaunchRuntime(tutorials.path + tutorials.runtime, true, "-d timezone");
            skype.Connect();
        }

        // Need to disconnect gracefully, otherwise runtime will keep running
        // and subsequent connections to runtimes will fail.
        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            skype.Disconnect();
        }
    }
}
