namespace step03_chat
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
            this.conversationListBox = new System.Windows.Forms.ListBox();
            this.messageLog = new System.Windows.Forms.TextBox();
            this.entryTextBox = new System.Windows.Forms.TextBox();
            this.sendButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // conversationListBox
            // 
            this.conversationListBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)));
            this.conversationListBox.FormattingEnabled = true;
            this.conversationListBox.Location = new System.Drawing.Point(2, 1);
            this.conversationListBox.Name = "conversationListBox";
            this.conversationListBox.Size = new System.Drawing.Size(220, 381);
            this.conversationListBox.TabIndex = 0;
            // 
            // messageLog
            // 
            this.messageLog.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.messageLog.BackColor = System.Drawing.SystemColors.Window;
            this.messageLog.Location = new System.Drawing.Point(228, 1);
            this.messageLog.Multiline = true;
            this.messageLog.Name = "messageLog";
            this.messageLog.ReadOnly = true;
            this.messageLog.Size = new System.Drawing.Size(393, 337);
            this.messageLog.TabIndex = 1;
            this.messageLog.TabStop = false;
            this.messageLog.WordWrap = false;
            // 
            // entryTextBox
            // 
            this.entryTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.entryTextBox.Location = new System.Drawing.Point(228, 344);
            this.entryTextBox.Multiline = true;
            this.entryTextBox.Name = "entryTextBox";
            this.entryTextBox.Size = new System.Drawing.Size(320, 38);
            this.entryTextBox.TabIndex = 2;
            // 
            // sendButton
            // 
            this.sendButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.sendButton.Location = new System.Drawing.Point(554, 344);
            this.sendButton.Name = "sendButton";
            this.sendButton.Size = new System.Drawing.Size(67, 38);
            this.sendButton.TabIndex = 3;
            this.sendButton.TabStop = false;
            this.sendButton.Text = "Send";
            this.sendButton.UseVisualStyleBackColor = true;
            this.sendButton.Click += new System.EventHandler(this.sendButton_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.SystemColors.ButtonFace;
            this.ClientSize = new System.Drawing.Size(633, 391);
            this.Controls.Add(this.sendButton);
            this.Controls.Add(this.entryTextBox);
            this.Controls.Add(this.messageLog);
            this.Controls.Add(this.conversationListBox);
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "SkypeKit Chat Tutorial";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ListBox conversationListBox;
        private System.Windows.Forms.TextBox messageLog;
        private System.Windows.Forms.TextBox entryTextBox;
        private System.Windows.Forms.Button sendButton;
    }
}

