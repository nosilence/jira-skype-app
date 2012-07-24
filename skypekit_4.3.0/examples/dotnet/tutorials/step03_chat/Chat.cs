/*************************************************************************************
 
  SkypeKit .NET wrapper tutorial 3 - Chat
  
 In this tutorial, we will:
 
 1. Make our conversations responsive to incoming chat messages, by making the 
    TextEdit that dispays or message history update itself when new messages arrive.
    
 2. Add another TextBox and a send button for sending messages into currently
    selected conversation.
    
 3. On selecting a conversation, we will "mark the messages read", which in
    SkypeKit API is done by updating the Consumption Horizon of a conversation.
    
 4. In case another conversation will become active (someone posts a message
    in an old conversation), the conversation will be added to the INBOX.
    We will need to make sure our conversationList and conversationListBox
    are synchronized with SkypeKit inbox.
    
 5. Finally, (mostly for purpose of making it easier to test the inbox sync),
    we will make it so that doubleclicking on an item in the conversation
    list will remove the clicked conversation from the inbox.
 
 Also, while we are at it, two more things. Firstly, lets descend our own Message 
 class - to avoid composing the same message GetText manually in multiple places.
 Note that overriding ToString method of the Message class is not recommended.
 
 Secondly, let's optimize our retrieving and adding-up the message history.
 Now that we actually react to incoming message events, we can retrieve the messages 
 just once, for each conversation. After that, when a new message arrives, we just 
 add the message text to the cached history string.
 
 Note that it would be bad idea to retrieve all the messages for all the 
 conversations all at once. That would lock up the UI for considerable time. 
 We'll just load the history as we did in previous tutorial. But intead 
 of discarding it, we will save it in Conversation objects.
*/

using System;
using System.Windows.Forms;
using SkypeKit;
using System.Security.Cryptography.X509Certificates;
using tutorials_common;

namespace step03_chat
{
    public partial class Form1 : Form
    {
        string username;
        string password;

        SktSkype skype;
        SktAccount account;

        SktConversation.List conversationList;

        public class Message : SktMessage
        { 
            public Message(uint objectId, SktSkype skype) : base(objectId, skype) {}

            public string GetText()
            {
                return P_TIMESTAMP.ToString() + " " + P_AUTHOR_DISPLAYNAME + ": " + P_BODY_XML + "\r\n";
            }
        }

        public class Conversation : SktConversation
        {
            public string messageHistory; // our message history cache
            public bool   messageHistoryLoaded; // indicates whether we have retrieved the cache

            public Conversation(uint objectId, SktSkype skype) : base(objectId, skype) 
            { 
                messageHistory = "";
                messageHistoryLoaded = false;
            }

            public override string ToString()
            {
                return P_DISPLAYNAME;
            }

            // The fetching of message history is now in Conversation class
            public void FetchMessageHistory ()
            {
                if (messageHistoryLoaded) return;

                SktMessage.List newMessages; // unread messages
                SktMessage.List oldMessages; // recent history

                DateTime t = DateTime.Now.Subtract(TimeSpan.FromDays(7));

                this.GetLastMessages(out oldMessages, out newMessages, t);
                messageHistory = "";
                foreach (Message msg in oldMessages) { messageHistory += msg.GetText(); }
                foreach (Message msg in newMessages) { messageHistory += msg.GetText(); }
                if (messageHistory == "") messageHistory = "No messages in this conversation in last week.";
                
                // After that, we will not re-load the history again, just add to it.
                messageHistoryLoaded = true;
            }
        }

        // Here we catch both incoming and outgoing messages.
        public void OnSkypeMessage(SktSkype sender, SktEvents.OnSkypeMessageArgs e)
        {
            if (conversationListBox.Items.Contains(e.conversation))
            {
                var conv = (Conversation)e.conversation;
                var msg = (Message)e.message;

                // if history is loaded, we append the new message to it
                if (conv.messageHistoryLoaded)
                {
                    string newLine = msg.GetText();
                    conv.messageHistory = conv.messageHistory + newLine;

                    if (conv == conversationListBox.SelectedItem)
                    {
                        // updating our TextBox with new text..
                        messageLog.AppendText(newLine);
                        // and this is how we set the conversation as "read"
                        conv.SetConsumedHorizon(DateTime.Now, false);
                    }
                }
                // if history is not loaded - we don't care. This message will be fetched
                // together with others, when needed.
            }
        }

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
                    account = skype.GetAccount(username);
                    account.LoginWithPassword(password, false, false);
                }
            }
            else
            {
                this.messageLog.AppendText("IPC handshake failed with: " + e.handshakeResult + "\r\n");
            }
        }

        public void OnAccountStatus(SktAccount sender, SktEvents.OnAccountStatusArgs e)
        {
            this.messageLog.AppendText(e.value.ToString() + "\r\n");

            if (e.value == SktAccount.STATUS.LOGGED_IN)
            {
                this.messageLog.AppendText("Retrieving conversation list..\r\n");
                conversationList = skype.GetConversationList(SktConversation.LIST_TYPE.INBOX_CONVERSATIONS);

                foreach (SktConversation conversation in conversationList)
                { 
                    this.conversationListBox.Items.Add(conversation); 
                }
            }

            if (e.value == SktAccount.STATUS.LOGGED_OUT)
            {
                this.messageLog.AppendText("Login failed because of " + sender.P_LOGOUTREASON + "\r\n");
            }
        }

        // Instead of fetching message history every time, like we did in the
        // previous tutorial, we will call Conversation.FetchMessageHistory here
        // and let the Conversation object decide, what it needs to do for us here
        private void listBox1_SelectedIndexChanged(object sender, System.EventArgs e)
        {
            messageLog.Clear();

            if (conversationListBox.SelectedItem == null) return;

            var conv = (Conversation)conversationListBox.SelectedItem;

            if (!conv.messageHistoryLoaded) conv.FetchMessageHistory();
            messageLog.Text = conv.messageHistory;

            messageLog.SelectionStart = messageLog.Text.Length;
            messageLog.ScrollToCaret();            
            conv.SetConsumedHorizon(DateTime.Now, false);
        }

        // Sending messages is really easy
        private void sendButton_Click(object sender, EventArgs e)
        {
            if (conversationListBox.SelectedItem == null) return;
            if (entryTextBox.Text == "") return;

            var conv = (Conversation)conversationListBox.Items[conversationListBox.SelectedIndex];
            conv.PostText(entryTextBox.Text, false);
            entryTextBox.Clear();
        }

        // When the inbox list gets changed, we will need to update our list and listbox
        public void OnSkypeConversationListChange (SktSkype sender, SktEvents.OnSkypeConversationListChangeArgs e)
        {
            if (e.type.Equals(SktConversation.LIST_TYPE.INBOX_CONVERSATIONS))
            {
                if (e.added)
                {
                    conversationList.Insert(0, e.conversation);
                    conversationListBox.Items.Insert(0, e.conversation);
                }
                else
                {
                    conversationList.Remove(e.conversation);
                    conversationListBox.Items.Remove(e.conversation);
                }
            }
        }

        private void conversationListBox_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            if (conversationListBox.SelectedItem == null) return;
            Conversation conv = (Conversation)conversationListBox.Items[conversationListBox.SelectedIndex];
            conv.RemoveFromInbox();
            // no need to update the UI here, all the necessary work will be done
            // in the OnSkypeConversationListChange callback (with e.added == false).
        }


        private void OnParticipantTextStatus(object o, SktEvents.OnParticipantTextStatusArgs e)
        {
            this.Text = e.value.ToString();
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

            skype.NewConversation   = (oid, skp) => { return new Conversation(oid, skp); };
            skype.NewMessage        = (oid, skp) => { return new Message(oid, skp); };

            skype.events.OnConnect += OnConnect;
            skype.events.OnAccountStatus += OnAccountStatus;
            skype.events.OnSkypeMessage += OnSkypeMessage;
            skype.events.OnSkypeConversationListChange += OnSkypeConversationListChange;

            skype.events.OnParticipantTextStatus += OnParticipantTextStatus;

            conversationListBox.SelectedIndexChanged += listBox1_SelectedIndexChanged;
            conversationListBox.MouseDoubleClick += conversationListBox_MouseDoubleClick;

            this.FormClosing += Form1_FormClosing;

            this.AcceptButton = sendButton;

            skype.LaunchRuntime(tutorials.path + tutorials.runtime, true);
            skype.Connect();
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            skype.Disconnect();
        }
    }
}
