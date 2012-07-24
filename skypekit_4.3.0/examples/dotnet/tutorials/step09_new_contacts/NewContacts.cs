/*************************************************************************************
 
  SkypeKit .NET wrapper tutorial 9 - New Contacts

  In this tutorial, we will:
 
 1. Display the list of all authorized contacts (SktContactGroup.TYPE.ALL_BUDDIES)
 2. Search for new contacts, by skypename, full name or e-mail.
 3. Send contact authorization requests to found contacts.
 4. React to incoming contact requests from remote parties.
*/

using System;
using System.Drawing;
using System.Windows.Forms;
using System.Security.Cryptography.X509Certificates;
using SkypeKit;
using tutorials_common;

namespace step09_new_contacts
{
    public partial class NewContacts : Form
    {
        static string username;
        static string password;

        static SktSkype skype;
        static SktAccount account;

        SktContact.List contactList;


        private class MyContact : SktContact
        {
            public MyContact(uint objectId, SktSkype skype) : base(objectId, skype) { }
            
            // Prettying up the string to display in our listboxes..
            public override string ToString()
            {
                string authStatus = "";
                if (P_GIVEN_AUTHLEVEL == AUTHLEVEL.AUTHORIZED_BY_ME) authStatus = " (friend)";
                if (P_GIVEN_AUTHLEVEL == AUTHLEVEL.BLOCKED_BY_ME) authStatus = " (blocked)";
                if (P_GIVEN_AUTHLEVEL == AUTHLEVEL.NONE) authStatus = " (unknown)";
                return P_DISPLAYNAME + authStatus;
            }
        }

        // Refreshing the contact list box
        private void UpdateContactListBox()
        {
            SktContactGroup allAuthorizedContacts;
            allAuthorizedContacts = skype.GetHardwiredContactGroup(SktContactGroup.TYPE.ALL_KNOWN_CONTACTS);
            contactList = allAuthorizedContacts.GetContacts();
            contactListBox.Items.Clear();
            foreach (SktContact contact in contactList) { contactListBox.Items.Add(contact); }
        }

        public void OnContactGroupChange(SktContactGroup sender, SktEvents.OnContactGroupChangeArgs e)
        {
            // Our contact list has changed
            if (sender.P_TYPE == SktContactGroup.TYPE.ALL_KNOWN_CONTACTS) UpdateContactListBox();

            // List of unauthorized contacts has changed. This could be a new auth request!
            if (sender.P_TYPE == SktContactGroup.TYPE.CONTACTS_WAITING_MY_AUTHORIZATION)
            {
                // maybe it changed because someone got authorized, and was removed from this list?
                if (!e.contact.IsMemberOfHardwiredGroup(SktContactGroup.TYPE.ALL_BUDDIES)) IncomingAuthRequest(e.contact);
            }
        }

        private void IncomingAuthRequest(SktContact contact)
        {
            DialogResult result = MessageBox.Show(this,
                contact.P_RECEIVED_AUTHREQUEST, "Contact authorization request from " + contact.P_SKYPENAME,
                MessageBoxButtons.YesNoCancel, MessageBoxIcon.Exclamation, MessageBoxDefaultButton.Button1);

            if (result == DialogResult.Yes)
            {
                // Authorizing contact
                contact.SetBuddyStatus(true, true);
                UpdateContactListBox();
                MessageBox.Show(this, contact.P_SKYPENAME + " is now an authorized contact!", "Great Success!",
                    MessageBoxButtons.OK, MessageBoxIcon.Information, MessageBoxDefaultButton.Button1);
            }
        }

        // This callback gets fired every time a new matching contact is found

        public void OnContactSearchNewResult(SktContactSearch sender, SktEvents.OnContactSearchNewResultArgs e)
        {
            this.searchResultListBox.Items.Add(e.contact);
        }

        // This callback could be useful for checking if e.value == SktContactSearch.STATUS.FINISHED
        public void OnContactSearchContactSearchStatus(SktContactSearch sender, SktEvents.OnContactSearchContactSearchStatusArgs e)
        {
            feedbackLabel.Text = "Contact search status: " + e.value.ToString();
            if ((e.value == SktContactSearch.STATUS.FINISHED) || (e.value == SktContactSearch.STATUS.FAILED) || (e.value == SktContactSearch.STATUS.EXTENDABLE))
            {
                sender.Release();
            }
        }

        // Using skype.CreateBasicContactSearch method. This method takes a string argument and looks for non-exact matches 
        // against both P_SKYPENAME and P_FULLNAME properties of the contact. If you intend to implement a simple, 
        // one-input search feature - this is the best method for you. The non-exact matching operates similarly to the SQL LIKE condition. 

        private void BasicContactSearch()
        {
            SktContactSearch seeker = skype.CreateBasicContactSearch(skypeNameTextBox.Text);
            if (!seeker.IsValid())
            {
                DialogResult result = MessageBox.Show(this, "Invalid skype name search term!", "Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Exclamation, MessageBoxDefaultButton.Button1, MessageBoxOptions.RightAlign);
                return;
            }
            seeker.Submit();
        }

        // Creating a blank contact search object and then adding custom terms. For more information about composing
        // complex search objects, see SktContactSearch section in the reference manual.

        private void ComplexContactSearch()
        {
            SktContactSearch seeker = skype.CreateContactSearch();
           
            if (skypeNameTextBox.Text != "")
                if (!seeker.AddStrTerm((int)SktContact.PropKeys.P_SKYPENAME, SktContactSearch.CONDITION.CONTAINS_WORDS, skypeNameTextBox.Text, false))
                    { feedbackLabel.Text = "Invalid Skype name search term"; };
            
            if (fullNameTextBox.Text != "")
                if (!seeker.AddStrTerm((int)SktContact.PropKeys.P_FULLNAME, SktContactSearch.CONDITION.CONTAINS_WORDS, fullNameTextBox.Text, false))
                    { feedbackLabel.Text = "Invalid full name search term"; };

            if (emailTextBox.Text != "")
                if (!seeker.AddEmailTerm(emailTextBox.Text, false))
                    { feedbackLabel.Text = "Invalid e-mail search term"; };
            
            if (!seeker.IsValid())
            {
                DialogResult result = MessageBox.Show(this, "Invalid search term!", "Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Exclamation, MessageBoxDefaultButton.Button1, MessageBoxOptions.RightAlign);
                return;
            } 

            seeker.Submit();
        }

        private void searchButton_Click(object sender, EventArgs e)
        {
            searchResultListBox.Items.Clear();
            if (skypeNameTextBox.Text.Equals("") & fullNameTextBox.Text.Equals("") & emailTextBox.Text.Equals("")) return; // all fields are empty
            if (fullNameTextBox.Text.Equals("") & emailTextBox.Text.Equals("")) { BasicContactSearch(); return; }; // only skypename field is used
            ComplexContactSearch(); // multiple fields were used
        }

        // Mouse double-click on a contact search result will attempt to add the new contact to our buddy list

        private void searchResultListBox_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            if (searchResultListBox.Items.Count == 0) return;

            MyContact contact = (MyContact)searchResultListBox.Items[searchResultListBox.SelectedIndex];
            
            // The contact we found, may already be in our contact list..
            if (contact.IsMemberOfHardwiredGroup(SktContactGroup.TYPE.ALL_BUDDIES))
            {
                DialogResult result = MessageBox.Show(this,
                    contact.P_DISPLAYNAME + " is already in our contact list.", "Already an authorized contact!",
                    MessageBoxButtons.OK, MessageBoxIcon.Exclamation, MessageBoxDefaultButton.Button1);
                return;
            }

            // Sending out the authorization request. Note that you will need to set the authorization status (BuddyStatus) before
            // you can proceed to send out the authorization message. Also, your UI should -always- provide the user an opportunity
            // to add a custom greeting message (the string arumenyt of contact.SendAuthRequest). Many people tend to ignore
            // incoming auth requests that come with default text.
            try
            {
                contact.SetBuddyStatus(true, true);
                contact.SendAuthRequest("If you can read this, then you have just received an auth. request.", 0);
            }
            catch
            {
                DialogResult result = MessageBox.Show(this,
                    contact.P_DISPLAYNAME + " cannot be added.", "Cannot add!",
                    MessageBoxButtons.OK, MessageBoxIcon.Exclamation, MessageBoxDefaultButton.Button1);
                return;
            }   
        }

        public NewContacts()
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

            skype.NewContact = (oid, skp) => { return new MyContact(oid, skp); };

            skype.events.OnConnect += OnConnect;
            skype.events.OnAccountStatus += OnAccountStatus;
            skype.events.OnContactGroupChange += OnContactGroupChange;

            skype.events.OnContactSearchContactSearchStatus += OnContactSearchContactSearchStatus;
            skype.events.OnContactSearchNewResult += OnContactSearchNewResult;

            // Adding enter key to execute search for all the input lines
            skypeNameTextBox.KeyDown += new System.Windows.Forms.KeyEventHandler(SearchFieldKeyDown);
            fullNameTextBox.KeyDown += new System.Windows.Forms.KeyEventHandler(SearchFieldKeyDown);
            emailTextBox.KeyDown += new System.Windows.Forms.KeyEventHandler(SearchFieldKeyDown);

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
            if (e.value == SktAccount.STATUS.LOGGED_IN)
            {
                feedbackLabel.Text = "";
                UpdateContactListBox();
            }

            if (e.value == SktAccount.STATUS.LOGGED_OUT)
            {
                DialogResult result = MessageBox.Show(this,
                    "Login failed because of " + sender.P_LOGOUTREASON.ToString(), "Login has failed",
                    MessageBoxButtons.OK, MessageBoxIcon.Exclamation, MessageBoxDefaultButton.Button1,MessageBoxOptions.RightAlign);
            }
        }

        private void SearchFieldKeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter) searchButton_Click(sender, e);
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            skype.Disconnect();
        }

    }
}
