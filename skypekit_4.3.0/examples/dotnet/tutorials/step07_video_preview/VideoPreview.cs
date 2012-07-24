/*************************************************************************************
 * 
 * SkypeKit .NET wrapper tutorial 7 - Video Preview
 * 
 * In this tutorial, we will be dealing with retrieving and rendering video from the
 * local webcam. Normally, this special video object is used for webcam test in
 * the configuration part of the UI.
 *  
 * The UI for this one is quite simple:
 * 1. ComboBox videoDeviceSelector - in case you have more than one webcam
 * 2. Button previewButton - for switcthing on and off the video.
 * 
 * NB! Selecting the webcam is not just for fancying up the UI. Video device
 * list is -not- available immediately after login. It will take several seconds to
 * populate itself. Thus you cannot start video immediately after login.
 * Instead, you will need to add a OnSkypeAvailableVideoDeviceListChange callback,
 * that gets fired when the video device list changes. When you get a list in there
 * that has more than zero items, you can start doing video.
*/

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Security.Cryptography.X509Certificates;
using SkypeKit;
using tutorials_common;

namespace step07_video_preview
{
    public partial class Form1 : Form
    {
        string username;
        string password;

        SktSkype skype;
        SktAccount account;
        SktVideo video;
        SktVideoRenderer renderer;

        bool isRunning = false;
        bool autoStart = true;

        // Just creating a control and inserting it into our main form
        public void MakeVideoRendererControl()
        {
            renderer = new VideoRendererWithFPS(skype)
            {
                Top = 40, Left = 10, Width = 640, Height = 480,
                Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right,
                SizeMode = PictureBoxSizeMode.StretchImage
            };
            renderer.FrameUpdateInterval = 1;
            this.Controls.Add(renderer);
            renderer.BringToFront();
        }

        // The only reason we actually need to subclass this, is to add a nice SkypeKit logo
        // and frame resolution plus FPS info on  each bitmap.
        class VideoRendererWithFPS : SktVideoRenderer
        {
            public VideoRendererWithFPS(SktSkypeBase skype) : base(skype) { }

            public override void AddCustomGraphics()
            {
                // The bitmap property of the SktVideoRenderer contains the latest video frame, not
                // yet drawn on the screen. You can use a Graphics object to draw additional things on it,
                // before it gets displayed. NB! The bitmap is in the -video frame native- resolution.
                // Different webcams can send out frames with different resolutions, at different times.
                // If you resize the frame - or just set SktRenderer.SizeMode to PictureBoxSizeMode.StretchImage,
                // the things you have drawn on the bitmap will be resized as well. 
                var graphics = Graphics.FromImage(bitmap);
                
                string customString = bitmap.Width.ToString() + "x" + bitmap.Height.ToString() + " FPS: " + FPS.ToString("F");                
                graphics.DrawString(customString, new Font("Tahoma", 20), Brushes.Yellow, 640-250, 480-40);
                graphics.DrawImage(skypekitLogo, bitmap.Width - 80, 5, skypekitLogo.Width, skypekitLogo.Height);
            }
        }

        // This will autostart the video when a video device becomes available and swicth it ON/OFF
        // on subsequent calls.

        public void ToggleVideo()
        {
            if (isRunning)
            {
                video.Stop();
                renderer.Stop();
                isRunning = false;
                return;
            }

            isRunning = true;

            // no webcam
            if (videoDeviceSelector.Items.Count == 0) return;

            VideoDevice device = (VideoDevice)videoDeviceSelector.SelectedItem;

            // This retrieves a video object for testing local webcam.
            // Unlike normal video objects, this one does not take place in context of a conversation.
            video = skype.CreatePreviewVideo(SktVideo.MEDIATYPE.MEDIA_VIDEO, device.name, device.path);

            // Associating the video object with renderer and starting both.
            renderer.VideoObject = video;
            
            video.Start();
            renderer.Start();
        }

        // Convenient sotrage for our video device combo box.
        public class VideoDevice
        {
            public string name;
            public string path;
            public VideoDevice(string name, string path) { this.name = name; this.path = path; }
            public override string ToString() { return name; }
        }

        // The following deeals with updating our webcam list, in response to the
        // OnSkypeAvailableVideoDeviceListChange event.
        public void UpdateVideoDevices()
        {
            List<String> deviceNames;
            List<String> deviceIds;
            uint devCount;
            skype.GetAvailableVideoDevices(out deviceNames, out deviceIds, out devCount);

            videoDeviceSelector.Items.Clear();

            if (devCount == 0)
            {
                videoDeviceSelector.Enabled = false;
                return;
            }

            for (int i = 0; i < devCount; i++)
            {
                videoDeviceSelector.Items.Add(new VideoDevice(deviceNames[i], deviceIds[i]));
            }
            videoDeviceSelector.Enabled = true;
            videoDeviceSelector.SelectedIndex = 0;
            if (autoStart) ToggleVideo();
            autoStart = false;
        }

        public void OnSkypeAvailableVideoDeviceListChange(SktSkype sender, SktEvents.OnSkypeAvailableVideoDeviceListChangeArgs e)
        {
            UpdateVideoDevices();
        }



        public Form1()
        {
            InitializeComponent();
            LoadIcons();
            videoDeviceSelector.Enabled = false;            

            if (!System.IO.File.Exists(tutorials.path + tutorials.keyfilename))
            {
                throw new Exception(String.Format(
                    "The keyfile (.pfx) path or filename {0} is incorrect?", tutorials.path + tutorials.keyfilename));
            }

            X509Certificate2 cert = new X509Certificate2(tutorials.path + tutorials.keyfilename, tutorials.keypassword);
            skype = new SktSkype(this, cert, false, false, 8963);

            // Note that MakeVideoRendererControl uses our skype field.
            // Thus it needs come after constructing skype.
            MakeVideoRendererControl();

            skype.events.OnConnect += OnConnect;
            skype.events.OnAccountStatus += OnAccountStatus;
            skype.events.OnSkypeAvailableVideoDeviceListChange += OnSkypeAvailableVideoDeviceListChange;

            this.FormClosing += Form1_FormClosing;

            skype.LaunchRuntime(tutorials.path + tutorials.runtime, true);
            skype.Connect();
        }

        private void previewButton_Click(object sender, EventArgs e)
        {
            ToggleVideo();
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
            loginStatusText.Text = (e.value.ToString());
            if (e.value == SktAccount.STATUS.LOGGED_IN) 
            {
                loginStatusText.Text = "Now what?";
            }

            if (e.value == SktAccount.STATUS.LOGGED_OUT)
            {
                if (e.value == SktAccount.STATUS.LOGGED_OUT)
                {
                    DialogResult result = MessageBox.Show(this,
                        "Login failed because of " + sender.P_LOGOUTREASON.ToString(), "Login has failed",
                        MessageBoxButtons.OK, MessageBoxIcon.Exclamation,
                        MessageBoxDefaultButton.Button1, MessageBoxOptions.RightAlign);
                }
            }
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            skype.Disconnect();
        }

        static Bitmap skypekitLogo;

        private void LoadIcons()
        {
            skypekitLogo = new Bitmap(tutorials.gfxPath + "skypekit_logo.png");
            Color backColor = skypekitLogo.GetPixel(1, 1);
            skypekitLogo.MakeTransparent(backColor);
        }

    }
}
