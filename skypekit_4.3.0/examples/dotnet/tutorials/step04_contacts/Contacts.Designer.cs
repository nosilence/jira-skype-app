namespace step04_contacts
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.contactListBox = new System.Windows.Forms.ListBox();
            this.moodText = new System.Windows.Forms.Label();
            this.lastOnlineText = new System.Windows.Forms.Label();
            this.lastOnlineLabel = new System.Windows.Forms.Label();
            this.onlineStatusText = new System.Windows.Forms.Label();
            this.onlineStatusLabel = new System.Windows.Forms.Label();
            this.avatarBox = new System.Windows.Forms.PictureBox();
            this.loginFeedback = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.avatarBox)).BeginInit();
            this.SuspendLayout();
            // 
            // contactListBox
            // 
            this.contactListBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.contactListBox.DrawMode = System.Windows.Forms.DrawMode.OwnerDrawFixed;
            this.contactListBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.contactListBox.FormattingEnabled = true;
            this.contactListBox.ItemHeight = 16;
            this.contactListBox.Location = new System.Drawing.Point(5, 13);
            this.contactListBox.Name = "contactListBox";
            this.contactListBox.Size = new System.Drawing.Size(283, 180);
            this.contactListBox.TabIndex = 0;
            // 
            // moodText
            // 
            this.moodText.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.moodText.Location = new System.Drawing.Point(294, 101);
            this.moodText.Name = "moodText";
            this.moodText.Size = new System.Drawing.Size(212, 68);
            this.moodText.TabIndex = 7;
            this.moodText.Text = "n/a";
            // 
            // lastOnlineText
            // 
            this.lastOnlineText.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.lastOnlineText.AutoSize = true;
            this.lastOnlineText.Location = new System.Drawing.Point(383, 82);
            this.lastOnlineText.Name = "lastOnlineText";
            this.lastOnlineText.Size = new System.Drawing.Size(24, 13);
            this.lastOnlineText.TabIndex = 6;
            this.lastOnlineText.Text = "n/a";
            // 
            // lastOnlineLabel
            // 
            this.lastOnlineLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.lastOnlineLabel.AutoSize = true;
            this.lastOnlineLabel.Location = new System.Drawing.Point(383, 59);
            this.lastOnlineLabel.Name = "lastOnlineLabel";
            this.lastOnlineLabel.Size = new System.Drawing.Size(91, 13);
            this.lastOnlineLabel.TabIndex = 5;
            this.lastOnlineLabel.Text = "Last Seen Online:";
            // 
            // onlineStatusText
            // 
            this.onlineStatusText.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.onlineStatusText.AutoSize = true;
            this.onlineStatusText.Location = new System.Drawing.Point(383, 36);
            this.onlineStatusText.Name = "onlineStatusText";
            this.onlineStatusText.Size = new System.Drawing.Size(24, 13);
            this.onlineStatusText.TabIndex = 4;
            this.onlineStatusText.Text = "n/a";
            // 
            // onlineStatusLabel
            // 
            this.onlineStatusLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.onlineStatusLabel.AutoSize = true;
            this.onlineStatusLabel.Location = new System.Drawing.Point(383, 13);
            this.onlineStatusLabel.Name = "onlineStatusLabel";
            this.onlineStatusLabel.Size = new System.Drawing.Size(73, 13);
            this.onlineStatusLabel.TabIndex = 3;
            this.onlineStatusLabel.Text = "Online Status:";
            // 
            // avatarBox
            // 
            this.avatarBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.avatarBox.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.avatarBox.Location = new System.Drawing.Point(294, 13);
            this.avatarBox.Name = "avatarBox";
            this.avatarBox.Size = new System.Drawing.Size(86, 82);
            this.avatarBox.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
            this.avatarBox.TabIndex = 1;
            this.avatarBox.TabStop = false;
            // 
            // loginFeedback
            // 
            this.loginFeedback.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.loginFeedback.AutoSize = true;
            this.loginFeedback.Location = new System.Drawing.Point(294, 176);
            this.loginFeedback.Name = "loginFeedback";
            this.loginFeedback.Size = new System.Drawing.Size(77, 13);
            this.loginFeedback.TabIndex = 0;
            this.loginFeedback.Text = "login feedback";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(518, 197);
            this.Controls.Add(this.lastOnlineText);
            this.Controls.Add(this.moodText);
            this.Controls.Add(this.lastOnlineLabel);
            this.Controls.Add(this.contactListBox);
            this.Controls.Add(this.onlineStatusText);
            this.Controls.Add(this.onlineStatusLabel);
            this.Controls.Add(this.avatarBox);
            this.Controls.Add(this.loginFeedback);
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "SkypeKit Contacts Tutorial";
            ((System.ComponentModel.ISupportInitialize)(this.avatarBox)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ListBox contactListBox;
        private System.Windows.Forms.Label loginFeedback;
        private System.Windows.Forms.PictureBox avatarBox;
        private System.Windows.Forms.Label lastOnlineText;
        private System.Windows.Forms.Label lastOnlineLabel;
        private System.Windows.Forms.Label onlineStatusText;
        private System.Windows.Forms.Label onlineStatusLabel;
        private System.Windows.Forms.Label moodText;
    }
}

