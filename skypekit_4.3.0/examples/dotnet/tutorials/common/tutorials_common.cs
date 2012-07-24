
using System;
using System.IO;
using System.Windows.Forms;
using SkypeKit;


namespace tutorials_common
{
    static class tutorials
    {
        // Path to runtime and keypair - both are assumed to be in the same directory.
        // NB! This path is relative to where the client application executable is.
        // You will need to modify this path, according to where you placed your
        // keypair and runtime.
        // Default value below assumes that your runtime and keypair are in the
        // SDK distro bin directory.

        public static string path = "..\\..\\..\\..\\..\\..\\..\\bin\\";
        
        
        public static string runtime = "windows-x86-skypekit.exe";
        public static string keyfilename = "tkclient.pfx";        
     
        public static string keypassword = "";

        public static string gfxPath = "..\\..\\..\\..\\common\\graphics\\";

        public static LoginRec ShowLoginDialog(Form form)
        {
            Form loginDialog = new Form()
            {
                Width = 350,
                Height = 180,
                Text = "Login",
                StartPosition = FormStartPosition.CenterScreen,
                FormBorderStyle = FormBorderStyle.FixedDialog,
                MaximizeBox = false,
                MinimizeBox = false
            };

            var skypeName = new TextBox()
            {
                Top = 20,
                Left = 90,
                Width = 200,
                AcceptsReturn = false
            };
            loginDialog.Controls.Add(skypeName);

            var nameLabel = new Label()
            {
                Top = 20,
                Left = 20,
                Width = 200,
                Text = "Skypename:"
            };
            loginDialog.Controls.Add(nameLabel );

            var password = new TextBox()
            {
                Top = 60,
                Left = 90,
                Width = 200,
                UseSystemPasswordChar = true,
                AcceptsReturn = false
            };
            loginDialog.Controls.Add(password);

            var passwordLabel = new Label()
            {
                Top = 60,
                Left = 20,
                Width = 200,
                Text = "Password:"
            };
            loginDialog.Controls.Add(passwordLabel);


            Button okButton = new Button()
            {
                Top = 100,
                Left = 110,
                Text = "Ok",
                DialogResult = DialogResult.OK
            };
            loginDialog.Controls.Add(okButton);

            Button cancelButton = new Button()
            {
                Top = 100,
                Left = 200,
                Text = "Cancel"
            };
            loginDialog.Controls.Add(cancelButton);

            loginDialog.AcceptButton = okButton;
            loginDialog.CancelButton = cancelButton;

            LoginRec loginRec = new LoginRec();

            string lastLoginFileName = "..\\..\\..\\..\\lastlogin.txt";
            try
            {
                TextReader loadLogin = new StreamReader(lastLoginFileName);
                skypeName.Text = loadLogin.ReadLine();
                loadLogin.Close();
            }
            catch { };

            loginRec.abort = (loginDialog.ShowDialog(form) != DialogResult.OK);
            loginRec.username = skypeName.Text;
            loginRec.password = password.Text;
            if (!loginRec.abort)
            {
                TextWriter savelogin = new StreamWriter(lastLoginFileName);
                savelogin.Write(loginRec.username);
                savelogin.Close();
            }

            return loginRec;
        }


        //-----------------------------------------------------------------------------
        // use tutorials.ShowLogWindow(this, skype) in your main form to get a
        // quick & dirty logging window for debugging.

        class LogForm : Form
        {
            public TextBox logBox;
        }

        static LogForm logWindow = null;

        static public void CloseLogWindow(object sender, EventArgs e)
        {
            if (logWindow != null)
            {
                logWindow.Close();
            }
        }

        static DateTime lastLogEntryTimepstamp = DateTime.Now;

        static public void OnLog(object sender, SktEvents.OnLogArgs e)
        {
            if (logWindow == null) return;
            if (!logWindow.Visible) return;
            TimeSpan diff = DateTime.Now.Subtract(lastLogEntryTimepstamp);
            lastLogEntryTimepstamp = DateTime.Now;
            logWindow.logBox.AppendText(diff.TotalMilliseconds.ToString("00000") + "  " + e.message + "\r\n");
        }

        public static void ShowLogWindow(Form mainform, SktSkype skype)
        {
            if (logWindow == null)
            {
                logWindow = new LogForm()
                {
                    Width = 480,
                    Height = 720,
                    Text = "Wrapper log"
                };

                logWindow.logBox = new TextBox()
                {
                    Top = 1,
                    Left = 1,
                    Width = logWindow.ClientSize.Width,
                    Height = logWindow.ClientSize.Height,
                    Multiline = true,
                    WordWrap = false,
                    ScrollBars = System.Windows.Forms.ScrollBars.Vertical,
                    Anchor = AnchorStyles.Left | AnchorStyles.Top | AnchorStyles.Right | AnchorStyles.Bottom
                };
                logWindow.Controls.Add(logWindow.logBox);
                mainform.FormClosing += CloseLogWindow;
                skype.events.OnLog += OnLog;
            }
            logWindow.Show();
        }

    } // tutorials

    public class LoginRec
    {
        public string username;
        public string password;
        public bool abort;
        public LoginRec() { }
    }
} // tutorials_common
