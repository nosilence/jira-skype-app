namespace step06_outgoing_calls
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
            this.convListBox = new System.Windows.Forms.ListBox();
            this.partPanel = new System.Windows.Forms.FlowLayoutPanel();
            this.msgLog = new System.Windows.Forms.TextBox();
            this.inputBox = new System.Windows.Forms.TextBox();
            this.callButton = new System.Windows.Forms.Button();
            this.sendButton = new System.Windows.Forms.Button();
            this.micSelector = new System.Windows.Forms.ComboBox();
            this.speakerSelector = new System.Windows.Forms.ComboBox();
            this.SuspendLayout();
            // 
            // convListBox
            // 
            this.convListBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)));
            this.convListBox.FormattingEnabled = true;
            this.convListBox.Location = new System.Drawing.Point(0, 3);
            this.convListBox.Name = "convListBox";
            this.convListBox.Size = new System.Drawing.Size(167, 251);
            this.convListBox.TabIndex = 0;
            // 
            // partPanel
            // 
            this.partPanel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.partPanel.BackColor = System.Drawing.SystemColors.Control;
            this.partPanel.Location = new System.Drawing.Point(486, 5);
            this.partPanel.Name = "partPanel";
            this.partPanel.Size = new System.Drawing.Size(171, 147);
            this.partPanel.TabIndex = 1;
            // 
            // msgLog
            // 
            this.msgLog.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.msgLog.Location = new System.Drawing.Point(172, 4);
            this.msgLog.Multiline = true;
            this.msgLog.Name = "msgLog";
            this.msgLog.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
            this.msgLog.Size = new System.Drawing.Size(308, 199);
            this.msgLog.TabIndex = 2;
            // 
            // inputBox
            // 
            this.inputBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.inputBox.Location = new System.Drawing.Point(172, 208);
            this.inputBox.Multiline = true;
            this.inputBox.Name = "inputBox";
            this.inputBox.Size = new System.Drawing.Size(308, 39);
            this.inputBox.TabIndex = 3;
            // 
            // callButton
            // 
            this.callButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.callButton.FlatAppearance.BorderSize = 0;
            this.callButton.Location = new System.Drawing.Point(614, 212);
            this.callButton.Name = "callButton";
            this.callButton.Size = new System.Drawing.Size(40, 40);
            this.callButton.TabIndex = 4;
            this.callButton.Text = "Call";
            this.callButton.UseVisualStyleBackColor = true;
            this.callButton.Click += new System.EventHandler(this.callButton_Click);
            // 
            // sendButton
            // 
            this.sendButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.sendButton.FlatAppearance.BorderSize = 0;
            this.sendButton.Location = new System.Drawing.Point(491, 211);
            this.sendButton.Name = "sendButton";
            this.sendButton.Size = new System.Drawing.Size(40, 40);
            this.sendButton.TabIndex = 6;
            this.sendButton.Text = "Send";
            this.sendButton.UseVisualStyleBackColor = true;
            // 
            // micSelector
            // 
            this.micSelector.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.micSelector.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.micSelector.Font = new System.Drawing.Font("Microsoft Sans Serif", 7F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.micSelector.FormattingEnabled = true;
            this.micSelector.Location = new System.Drawing.Point(486, 183);
            this.micSelector.Name = "micSelector";
            this.micSelector.Size = new System.Drawing.Size(171, 20);
            this.micSelector.TabIndex = 7;
            this.micSelector.SelectedIndexChanged += new System.EventHandler(this.micSelector_SelectedIndexChanged);
            // 
            // speakerSelector
            // 
            this.speakerSelector.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.speakerSelector.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.speakerSelector.Font = new System.Drawing.Font("Microsoft Sans Serif", 7F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.speakerSelector.FormattingEnabled = true;
            this.speakerSelector.Location = new System.Drawing.Point(486, 156);
            this.speakerSelector.Name = "speakerSelector";
            this.speakerSelector.Size = new System.Drawing.Size(170, 20);
            this.speakerSelector.TabIndex = 8;
            this.speakerSelector.SelectedIndexChanged += new System.EventHandler(this.speakerSelector_SelectedIndexChanged);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(662, 259);
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
            this.Text = "SkypeKit Outgoing Calls Tutorial";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ListBox convListBox;
        private System.Windows.Forms.FlowLayoutPanel partPanel;
        private System.Windows.Forms.TextBox msgLog;
        private System.Windows.Forms.TextBox inputBox;
        private System.Windows.Forms.Button callButton;
        private System.Windows.Forms.Button sendButton;
        private System.Windows.Forms.ComboBox micSelector;
        private System.Windows.Forms.ComboBox speakerSelector;

    }
}

