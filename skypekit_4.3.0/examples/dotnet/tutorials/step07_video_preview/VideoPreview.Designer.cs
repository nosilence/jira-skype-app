namespace step07_video_preview
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
            this.videoDeviceSelector = new System.Windows.Forms.ComboBox();
            this.previewButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // loginStatusText
            // 
            this.loginStatusText.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.loginStatusText.Location = new System.Drawing.Point(12, 47);
            this.loginStatusText.Name = "loginStatusText";
            this.loginStatusText.Size = new System.Drawing.Size(637, 461);
            this.loginStatusText.TabIndex = 0;
            this.loginStatusText.Text = "n/a";
            this.loginStatusText.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // videoDeviceSelector
            // 
            this.videoDeviceSelector.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.videoDeviceSelector.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.videoDeviceSelector.FormattingEnabled = true;
            this.videoDeviceSelector.Location = new System.Drawing.Point(12, 12);
            this.videoDeviceSelector.Name = "videoDeviceSelector";
            this.videoDeviceSelector.Size = new System.Drawing.Size(556, 21);
            this.videoDeviceSelector.TabIndex = 1;
            // 
            // previewButton
            // 
            this.previewButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.previewButton.Location = new System.Drawing.Point(574, 12);
            this.previewButton.Name = "previewButton";
            this.previewButton.Size = new System.Drawing.Size(75, 23);
            this.previewButton.TabIndex = 2;
            this.previewButton.Text = "On/Off";
            this.previewButton.UseVisualStyleBackColor = true;
            this.previewButton.Click += new System.EventHandler(this.previewButton_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(661, 530);
            this.Controls.Add(this.previewButton);
            this.Controls.Add(this.videoDeviceSelector);
            this.Controls.Add(this.loginStatusText);
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "SkypeKit Video Preview Tutorial";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Label loginStatusText;
        private System.Windows.Forms.ComboBox videoDeviceSelector;
        private System.Windows.Forms.Button previewButton;
    }
}

