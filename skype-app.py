VALID_SKYPE_USERS = {
	'nickcherryjiggz',
	'vini.dy'
}

from jira.client import JIRA
import keypair
import re
import sys
from time import sleep



jira_options = { 'server': 'https://thewonderfactory.atlassian.net' }
jira = JIRA(basic_auth=('ncherry', 'ncherry'), options=jira_options)



sys.path.append(keypair.distroRoot + '/ipc/python')
sys.path.append(keypair.distroRoot + '/interfaces/skype/python')



try:
	import Skype
except ImportError:
	raise SystemExit('Program requires Skype and skypekit modules')



if len(sys.argv) != 3:
	print('Usage: python skype-app.py <skypename> <password>')
	sys.exit()



skype_username = sys.argv[1]
skype_password  = sys.argv[2]
logged_into_skype	= False



def OnMessage(self, message, changesInboxTimestamp, supersedesHistoryMessage, conversation):
	if message.author != skype_username:
		issues
		direct_message_recipients = re.findall(r'\@\w+', message.body_xml)
		direct_message_recipients = map(lambda r: r[1:], direct_message_recipients)



def AccountOnChange (self, property_name):
	global logged_into_skype
	if property_name == 'status':
		if self.status == 'LOGGED_IN':
			logged_into_skype = True
			print('Login complete.')
			


Skype.Account.OnPropertyChange = AccountOnChange
Skype.Skype.OnMessage = OnMessage



try:
	MySkype = Skype.GetSkype(keypair.keyFileName)
	MySkype.Start()
except Exception:
	raise SystemExit('Unable to create skype instance.')



account = MySkype.GetAccount(skype_username)
print('Logging in with ' + skype_username)
account.LoginWithPassword(skype_password, False, False)



while logged_into_skype == False:
	sleep(1)



print('Now accepting incoming chat messages.')
print('Press ENTER to quit.')
raw_input('')
print('Exiting..')
MySkype.stop()