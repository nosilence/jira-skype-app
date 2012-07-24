namespace step10_public_chat_blobs
{
    partial class PublicChatBlobs
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
            this.feedbackLabel = new System.Windows.Forms.Label();
            this.createChatBtn = new System.Windows.Forms.Button();
            this.htmlViewer = new System.Windows.Forms.WebBrowser();
            this.SuspendLayout();
            // 
            // feedbackLabel
            // 
            this.feedbackLabel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.feedbackLabel.Location = new System.Drawing.Point(4, 12);
            this.feedbackLabel.Name = "feedbackLabel";
            this.feedbackLabel.Size = new System.Drawing.Size(443, 23);
            this.feedbackLabel.TabIndex = 0;
            this.feedbackLabel.Text = "label1";
            // 
            // createChatBtn
            // 
            this.createChatBtn.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.createChatBtn.Location = new System.Drawing.Point(453, 12);
            this.createChatBtn.Name = "createChatBtn";
            this.createChatBtn.Size = new System.Drawing.Size(160, 23);
            this.createChatBtn.TabIndex = 1;
            this.createChatBtn.Text = "Create Public Chat";
            this.createChatBtn.UseVisualStyleBackColor = true;
            this.createChatBtn.Click += new System.EventHandler(this.createChatBtn_Click);
            // 
            // htmlViewer
            // 
            this.htmlViewer.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.htmlViewer.Location = new System.Drawing.Point(7, 50);
            this.htmlViewer.MinimumSize = new System.Drawing.Size(20, 20);
            this.htmlViewer.Name = "htmlViewer";
            this.htmlViewer.Size = new System.Drawing.Size(606, 159);
            this.htmlViewer.TabIndex = 3;
            // 
            // PublicChatBlobs
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(625, 221);
            this.Controls.Add(this.htmlViewer);
            this.Controls.Add(this.createChatBtn);
            this.Controls.Add(this.feedbackLabel);
            this.Name = "PublicChatBlobs";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "SkypeKit Public Chat BLOBs Tutorial";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Label feedbackLabel;
        private System.Windows.Forms.Button createChatBtn;
        private System.Windows.Forms.WebBrowser htmlViewer;
    }
}

