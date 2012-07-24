

import keypair;
import re
import sys;
from time import sleep;


sys.path.append(keypair.distroRoot + '/ipc/python');
sys.path.append(keypair.distroRoot + '/interfaces/skype/python');



try:
	import Skype;
except ImportError:
	raise SystemExit('Program requires Skype and skypekit modules');



if len(sys.argv) != 3:
	print('Usage: python skype-app.py <skypename> <password>');
	sys.exit();



accountName = sys.argv[1];
accountPsw  = sys.argv[2];
loggedIn	= False;



def OnMessage(self, message, changesInboxTimestamp, supersedesHistoryMessage, conversation):
	if message.author == accountName:
		directMessageRecipients = re.findall(r'\@\w+', message.body_xml)
		directMessageRecipients = map(lambda r: r[1:], directMessageRecipients);
		if directMessageRecipients:
			if message.CanEdit():
				message.Edit("");
			newConversation = MySkype.GetConversationByParticipants(directMessageRecipients, True, False);
			newConversationParticipants = newConversation.GetParticipants('ALL');			
			newMessage = newConversation.PostText("DM: " + message.body_xml, False);



def AccountOnChange (self, property_name):
	global loggedIn;
	if property_name == 'status':
		if self.status == 'LOGGED_IN':
			loggedIn = True;
			print('Login complete.');
			


Skype.Account.OnPropertyChange = AccountOnChange;
Skype.Skype.OnMessage = OnMessage;



try:
	MySkype = Skype.GetSkype(keypair.keyFileName);
	MySkype.Start();
except Exception:
	raise SystemExit('Unable to create skype instance.');



account = MySkype.GetAccount(accountName);
print('Logging in with ' + accountName);
account.LoginWithPassword(accountPsw, False, False);



while loggedIn == False:
	sleep(1);



print('Now accepting incoming chat messages.');
print('Press ENTER to quit.');
raw_input('');
print('Exiting..');
MySkype.stop();