namespace step05_incoming_calls
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
            this.loginStatusText = new System.Windows.Forms.Label();
            this.acceptCallButton = new System.Windows.Forms.Button();
            this.holdCallButton = new System.Windows.Forms.Button();
            this.muteCallButton = new System.Windows.Forms.Button();
            this.participantPanel = new System.Windows.Forms.FlowLayoutPanel();
            this.callDurationText = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // loginStatusText
            // 
            this.loginStatusText.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.loginStatusText.AutoSize = true;
            this.loginStatusText.Location = new System.Drawing.Point(5, 158);
            this.loginStatusText.Name = "loginStatusText";
            this.loginStatusText.Size = new System.Drawing.Size(0, 13);
            this.loginStatusText.TabIndex = 0;
            // 
            // acceptCallButton
            // 
            this.acceptCallButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.acceptCallButton.FlatAppearance.BorderSize = 0;
            this.acceptCallButton.FlatAppearance.CheckedBackColor = System.Drawing.Color.White;
            this.acceptCallButton.ImageKey = "(none)";
            this.acceptCallButton.Location = new System.Drawing.Point(8, 111);
            this.acceptCallButton.Name = "acceptCallButton";
            this.acceptCallButton.Size = new System.Drawing.Size(55, 45);
            this.acceptCallButton.TabIndex = 1;
            this.acceptCallButton.Text = "Answer";
            this.acceptCallButton.UseVisualStyleBackColor = true;
            this.acceptCallButton.Click += new System.EventHandler(this.accceptCallButton_Click);
            // 
            // holdCallButton
            // 
            this.holdCallButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.holdCallButton.FlatAppearance.BorderSize = 0;
            this.holdCallButton.FlatAppearance.CheckedBackColor = System.Drawing.Color.White;
            this.holdCallButton.Location = new System.Drawing.Point(69, 111);
            this.holdCallButton.Name = "holdCallButton";
            this.holdCallButton.Size = new System.Drawing.Size(55, 45);
            this.holdCallButton.TabIndex = 2;
            this.holdCallButton.Text = "Hold";
            this.holdCallButton.UseVisualStyleBackColor = true;
            this.holdCallButton.Click += new System.EventHandler(this.holdCallButton_Click);
            // 
            // muteCallButton
            // 
            this.muteCallButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.muteCallButton.FlatAppearance.BorderSize = 0;
            this.muteCallButton.FlatAppearance.CheckedBackColor = System.Drawing.Color.White;
            this.muteCallButton.Location = new System.Drawing.Point(130, 111);
            this.muteCallButton.Name = "muteCallButton";
            this.muteCallButton.Size = new System.Drawing.Size(55, 45);
            this.muteCallButton.TabIndex = 3;
            this.muteCallButton.Text = "Mute";
            this.muteCallButton.UseVisualStyleBackColor = true;
            this.muteCallButton.Click += new System.EventHandler(this.muteCallButton_Click);
            // 
            // participantPanel
            // 
            this.participantPanel.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.participantPanel.Location = new System.Drawing.Point(201, 12);
            this.participantPanel.Name = "participantPanel";
            this.participantPanel.Size = new System.Drawing.Size(397, 159);
            this.participantPanel.TabIndex = 4;
            // 
            // callDurationText
            // 
            this.callDurationText.AutoSize = true;
            this.callDurationText.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.callDurationText.Location = new System.Drawing.Point(15, 56);
            this.callDurationText.Name = "callDurationText";
            this.callDurationText.Size = new System.Drawing.Size(168, 17);
            this.callDurationText.TabIndex = 5;
            this.callDurationText.Text = "Waiting for incoming calls";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(603, 176);
            this.Controls.Add(this.callDurationText);
            this.Controls.Add(this.participantPanel);
            this.Controls.Add(this.muteCallButton);
            this.Controls.Add(this.holdCallButton);
            this.Controls.Add(this.acceptCallButton);
            this.Controls.Add(this.loginStatusText);
            this.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "SkypeKit Incoming Calls Tutorial";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label loginStatusText;
        private System.Windows.Forms.Button acceptCallButton;
        private System.Windows.Forms.Button holdCallButton;
        private System.Windows.Forms.Button muteCallButton;
        private System.Windows.Forms.FlowLayoutPanel participantPanel;
        private System.Windows.Forms.Label callDurationText;
    }
}

