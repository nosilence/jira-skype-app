/*************************************************************************************
 
  SkypeKit .NET wrapper tutorial 1 - Login
 
  This is a SkypeKit equivalent to basic "Hello world".
  
  Here we will:
  
  1. Create a SktSkype instance;
  
  2. In OnConnect event, we initiate login, if connection was successful;
  
  3. In OnAccountStatus, we will log SktAccount status progression during
     the login process, and if the login succeeded, display a "hello" message.
*/

using System;
using System.Windows.Forms;
using SkypeKit;
using System.Security.Cryptography.X509Certificates;
using tutorials_common;

namespace step01_login
{
    public partial class Form1 : Form
    {
        string username;
        string password;

        SktSkype skype;
        SktAccount account;

        // Fired when connection to the runtime is established (or fails)
        // If it's up, we can initiate account login.
        // Once we have called account.LoginWithPassword, we can expect
        // OnAccountStatus events firing, in near future..
        public void OnConnect(object sender, SktEvents.OnConnectArgs e)
        {
            if (e.success)
            {
                textBox1.AppendText("Connection to runtime is up. Lets see if we can log in..\n");
                LoginRec login = tutorials.ShowLoginDialog(this);
                username = login.username;
                password = login.password;
                if (!login.abort)
                {
                    account = skype.GetAccount(username);
                    account.LoginWithPassword(password, false, true);
                }
            }
            else
            {
                textBox1.AppendText("IPC handshake failed with: " + e.handshakeResult + "\n");
            }
        }

        // This will fire whenever account (login) status changes value.
        public void OnAccountStatus(SktAccount sender, SktEvents.OnAccountStatusArgs e)
        {
            textBox1.AppendText(e.value.ToString() + '\n');

            if (e.value == SktAccount.STATUS.LOGGED_IN)
            {
                textBox1.AppendText(String.Format("Hello {0}! You should see this account as online on Skype, in a few seconds.\n", username));
            }

            if (e.value == SktAccount.STATUS.LOGGED_OUT)
            {
                textBox1.AppendText("Login failed because of " + sender.P_LOGOUTREASON + '\n');
            }
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

            skype = new SktSkype(this, cert, true, true, 8963);
            skype.events.OnConnect += OnConnect;
            skype.events.OnAccountStatus += OnAccountStatus;

            this.FormClosing += Form1_FormClosing;

            skype.LaunchRuntime(tutorials.path + tutorials.runtime, true, "-x");

            // Once connection happens, OnConnect will fire
            skype.Connect();
        }

        // Disconnecting, so that our runtime shuts down gracefully
        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            skype.Disconnect();
        }

    }
}
