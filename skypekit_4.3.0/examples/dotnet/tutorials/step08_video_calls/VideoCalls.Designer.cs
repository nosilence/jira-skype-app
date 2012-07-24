namespace step08_video_calls
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
            this.speakerSelector = new System.Windows.Forms.ComboBox();
            this.micSelector = new System.Windows.Forms.ComboBox();
            this.sendButton = new System.Windows.Forms.Button();
            this.callButton = new System.Windows.Forms.Button();
            this.inputBox = new System.Windows.Forms.TextBox();
            this.msgLog = new System.Windows.Forms.TextBox();
            this.partPanel = new System.Windows.Forms.FlowLayoutPanel();
            this.convListBox = new System.Windows.Forms.ListBox();
            this.webcamSelector = new System.Windows.Forms.ComboBox();
            this.SuspendLayout();
            // 
            // speakerSelector
            // 
            this.speakerSelector.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.speakerSelector.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.speakerSelector.Font = new System.Drawing.Font("Microsoft Sans Serif", 7F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.speakerSelector.FormattingEnabled = true;
            this.speakerSelector.Location = new System.Drawing.Point(492, 352);
            this.speakerSelector.Name = "speakerSelector";
            this.speakerSelector.Size = new System.Drawing.Size(183, 20);
            this.speakerSelector.TabIndex = 16;
            this.speakerSelector.SelectedIndexChanged += new System.EventHandler(this.speakerSelector_SelectedIndexChanged_1);
            // 
            // micSelector
            // 
            this.micSelector.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.micSelector.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.micSelector.Font = new System.Drawing.Font("Microsoft Sans Serif", 7F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.micSelector.FormattingEnabled = true;
            this.micSelector.Location = new System.Drawing.Point(492, 379);
            this.micSelector.Name = "micSelector";
            this.micSelector.Size = new System.Drawing.Size(184, 20);
            this.micSelector.TabIndex = 15;
            this.micSelector.SelectedIndexChanged += new System.EventHandler(this.micSelector_SelectedIndexChanged_1);
            // 
            // sendButton
            // 
            this.sendButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.sendButton.FlatAppearance.BorderSize = 0;
            this.sendButton.Location = new System.Drawing.Point(492, 405);
            this.sendButton.Name = "sendButton";
            this.sendButton.Size = new System.Drawing.Size(40, 40);
            this.sendButton.TabIndex = 14;
            this.sendButton.Text = "Send";
            this.sendButton.UseVisualStyleBackColor = true;
            // 
            // callButton
            // 
            this.callButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.callButton.FlatAppearance.BorderSize = 0;
            this.callButton.Location = new System.Drawing.Point(633, 406);
            this.callButton.Name = "callButton";
            this.callButton.Size = new System.Drawing.Size(40, 40);
            this.callButton.TabIndex = 13;
            this.callButton.Text = "Call";
            this.callButton.UseVisualStyleBackColor = true;
            // 
            // inputBox
            // 
            this.inputBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.inputBox.Location = new System.Drawing.Point(175, 404);
            this.inputBox.Multiline = true;
            this.inputBox.Name = "inputBox";
            this.inputBox.Size = new System.Drawing.Size(311, 39);
            this.inputBox.TabIndex = 12;
            // 
            // msgLog
            // 
            this.msgLog.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.msgLog.Location = new System.Drawing.Point(175, 5);
            this.msgLog.Multiline = true;
            this.msgLog.Name = "msgLog";
            this.msgLog.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.msgLog.Size = new System.Drawing.Size(311, 394);
            this.msgLog.TabIndex = 11;
            // 
            // partPanel
            // 
            this.partPanel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.partPanel.BackColor = System.Drawing.SystemColors.Control;
            this.partPanel.Location = new System.Drawing.Point(492, 6);
            this.partPanel.Name = "partPanel";
            this.partPanel.Size = new System.Drawing.Size(183, 313);
            this.partPanel.TabIndex = 10;
            // 
            // convListBox
            // 
            this.convListBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)));
            this.convListBox.FormattingEnabled = true;
            this.convListBox.Location = new System.Drawing.Point(3, 4);
            this.convListBox.Name = "convListBox";
            this.convListBox.Size = new System.Drawing.Size(167, 446);
            this.convListBox.TabIndex = 9;
            // 
            // webcamSelector
            // 
            this.webcamSelector.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.webcamSelector.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.webcamSelector.Font = new System.Drawing.Font("Microsoft Sans Serif", 7F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.webcamSelector.FormattingEnabled = true;
            this.webcamSelector.Location = new System.Drawing.Point(492, 326);
            this.webcamSelector.Name = "webcamSelector";
            this.webcamSelector.Size = new System.Drawing.Size(184, 20);
            this.webcamSelector.TabIndex = 17;
            this.webcamSelector.SelectedIndexChanged += new System.EventHandler(this.webcamSelector_SelectedIndexChanged);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(678, 454);
            this.Controls.Add(this.webcamSelector);
            this.Controls.Add(this.speakerSelector);
            this.Controls.Add(this.micSelector);
            this.Controls.Add(this.sendButton);
            this.Controls.Add(this.callButton);
            this.Controls.Add(this.inputBox);
            this.Controls.Add(this.msgLog);
            this.Controls.Add(this.partPanel);
            this.Controls.Add(this.convListBox);
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "SkypeKit Video Calls Tutorial";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ComboBox speakerSelector;
        private System.Windows.Forms.ComboBox micSelector;
        private System.Windows.Forms.Button sendButton;
        private System.Windows.Forms.Button callButton;
        private System.Windows.Forms.TextBox inputBox;
        private System.Windows.Forms.TextBox msgLog;
        private System.Windows.Forms.FlowLayoutPanel partPanel;
        private System.Windows.Forms.ListBox convListBox;
        private System.Windows.Forms.ComboBox webcamSelector;
    }
}

