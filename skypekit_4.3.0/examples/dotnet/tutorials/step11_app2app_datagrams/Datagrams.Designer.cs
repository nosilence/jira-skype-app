namespace step11_app2app_datagrams
{
    partial class Datagrams
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
            this.contactCombo = new System.Windows.Forms.ComboBox();
            this.connectBtn = new System.Windows.Forms.Button();
            this.msgHistory = new System.Windows.Forms.TextBox();
            this.msgEdit = new System.Windows.Forms.TextBox();
            this.sendBtn = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // contactCombo
            // 
            this.contactCombo.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.contactCombo.Enabled = false;
            this.contactCombo.FormattingEnabled = true;
            this.contactCombo.Location = new System.Drawing.Point(15, 14);
            this.contactCombo.Name = "contactCombo";
            this.contactCombo.Size = new System.Drawing.Size(326, 21);
            this.contactCombo.TabIndex = 1;
            // 
            // connectBtn
            // 
            this.connectBtn.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.connectBtn.Location = new System.Drawing.Point(356, 12);
            this.connectBtn.Name = "connectBtn";
            this.connectBtn.Size = new System.Drawing.Size(75, 23);
            this.connectBtn.TabIndex = 2;
            this.connectBtn.Text = "Connect";
            this.connectBtn.UseVisualStyleBackColor = true;
            this.connectBtn.Click += new System.EventHandler(this.connectBtn_Click);
            // 
            // msgHistory
            // 
            this.msgHistory.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.msgHistory.Location = new System.Drawing.Point(15, 40);
            this.msgHistory.Multiline = true;
            this.msgHistory.Name = "msgHistory";
            this.msgHistory.Size = new System.Drawing.Size(413, 181);
            this.msgHistory.TabIndex = 3;
            // 
            // msgEdit
            // 
            this.msgEdit.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.msgEdit.Location = new System.Drawing.Point(15, 229);
            this.msgEdit.Name = "msgEdit";
            this.msgEdit.Size = new System.Drawing.Size(335, 20);
            this.msgEdit.TabIndex = 4;
            // 
            // sendBtn
            // 
            this.sendBtn.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.sendBtn.Enabled = false;
            this.sendBtn.Location = new System.Drawing.Point(356, 227);
            this.sendBtn.Name = "sendBtn";
            this.sendBtn.Size = new System.Drawing.Size(75, 23);
            this.sendBtn.TabIndex = 5;
            this.sendBtn.Text = "Send ";
            this.sendBtn.UseVisualStyleBackColor = true;
            this.sendBtn.Click += new System.EventHandler(this.sendBtn_Click);
            // 
            // Datagrams
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(443, 262);
            this.Controls.Add(this.sendBtn);
            this.Controls.Add(this.msgEdit);
            this.Controls.Add(this.msgHistory);
            this.Controls.Add(this.connectBtn);
            this.Controls.Add(this.contactCombo);
            this.Name = "Datagrams";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "SkypeKit App2app Datagrams";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ComboBox contactCombo;
        private System.Windows.Forms.Button connectBtn;
        private System.Windows.Forms.TextBox msgHistory;
        private System.Windows.Forms.TextBox msgEdit;
        private System.Windows.Forms.Button sendBtn;
    }
}

