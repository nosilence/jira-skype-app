local ok, lfs = pcall(require, "lfs")
if not ok then
   print('You need installing LuaFileSystem module')
   print('First install lua Rocks if needed')
   print('> sudo apt-get install luarocks')
   print('shall do, else refer to ')
   print('http://luarocks.org/en/Download')
   print("then (don't forget sudo!)")
   print('> sudo luarocks install luafilesystem')
   print('then')
   print('> lua migrate2java2 <javaclientcodebasedir>')
   print('or')
   print('> cd <javaclientcodebasedir>')
   print('> lua migrate2java2')
   print('On windows though, better installing LuaForWindows')
   print('http://code.google.com/p/luaforwindows/')
   print('and running this script through SciTE')
   os.exit(1)
end

function dirtree(dir)
  assert(dir and dir ~= "", "directory parameter is missing or empty")
  if string.sub(dir, -1) == "/" then
    dir=string.sub(dir, 1, -2)
  end

  local visited = { dir = true }

  local function yieldtree(dir)
    for entry in lfs.dir(dir) do
      if entry ~= "." and entry ~= ".." then
        entry=dir.."/"..entry
	local attr=lfs.attributes(entry)
	coroutine.yield(entry,attr)
	if attr.mode == "directory" and not visited[entry] then
          visited[entry] = true
	  yieldtree(entry)
	end
      end
    end
  end

  return coroutine.wrap(function() yieldtree(dir) end)
end

function under_lined2lowerCamel(s)
  s = s:gsub('(%l)(%u)', "%1_%2"):gsub('(%d)(%a)', "%1_%2"):lower()
  return s:gsub("_(.)", function(c) return string.upper(c) end)
         :gsub("(_)", "%1%1")
end

-- 'assigned_comment' or 'AssignedComment' or 'assignedComment' or 'assigned_Comment' or ASSIGNED_COMMENT -> 'AssignedComment'
function under_lined2UpperCamel(s)
  s = s:gsub('(%l)(%u)', "%1_%2"):gsub('(%d)(%a)', "%1_%2"):lower()
  return s:gsub(".",    function(c) return string.upper(c) end, 1)
          :gsub("_(.)", function(c) return string.upper(c) end)
          :gsub("(_)", "%1%1")
end
-- migration rules
rules = { }
function rules:add(label, pattern, transform)
    self[#self+1] =
      function(lines)
          if self.current ~= label then
              print('applying',label)
              self.current = label
          end
          return lines:gsub(pattern, transform)
      end
end

function rules:apply(t)
  for r=1,#self do t = self[r](t) end
  for k,v in pairs(self.renamed) do
    t = t:gsub('([%.%s])'..k, '%1'..v)
  end
  return t
end

function rules:migrate(base_dir)
    local modified = {}
    for filename, attr in dirtree(base_dir or ".") do
      if attr.mode == "file" and filename:match('%.java$') then
         print('migrating', filename)
         local f = io.open(filename, 'r')
         local t = f:read('*all')
         f:close()
         m = self:apply(t)
         if t ~= m then
            filename = filename..'.migrated'
            print('migrated to '..filename)
            f = io.open(filename, 'w')
            f:write(m)
            f:close()
            modified[#modified+1]=filename
         else
            print('migration introduced no changes to '..filename)
         end
      end
    end
    if #modified > 0 then
        print('Summary of modified files')
        for i=1,#modified do
           print(modified[i])
        end
    end
end

rules.renamed = {
P_GIVEN_DISPLAYNAME= "P_GIVEN_DISPLAY_NAME",
getGivenDisplayname= "getGivenDisplayName",
P_NROFCONTACTS= "P_CONTACT_COUNT",
getNrofcontacts= "getContactCount",
P_NROFCONTACTS_ONLINE= "P_ONLINE_CONTACT_COUNT",
getNrofcontactsOnline= "getOnlineContactCount",
UNKNOWN_OR_PENDINGAUTH_BUDDIES= "UNKNOWN_OR_PENDING_AUTH_BUDDIES",
P_SKYPENAME= "P_SKYPE_NAME",
getSkypename= "getSkypeName",
P_PSTNNUMBER= "P_PSTN_NUMBER",
getPstnnumber= "getPstnNumber",
P_FULLNAME= "P_FULL_NAME",
getFullname= "getFullName",
P_IPCOUNTRY= "P_IP_COUNTRY",
getIpcountry= "getIpCountry",
P_RECEIVED_AUTHREQUEST= "P_RECEIVED_AUTH_REQUEST",
getReceivedAuthrequest= "getReceivedAuthRequest",
P_AUTHREQ_TIMESTAMP= "P_AUTH_REQUEST_TIMESTAMP",
getAuthreqTimestamp= "getAuthRequestTimestamp",
P_LASTONLINE_TIMESTAMP= "P_LAST_ONLINE_TIMESTAMP",
getLastonlineTimestamp= "getLastOnlineTimestamp",
P_DISPLAYNAME= "P_DISPLAY_NAME",
getDisplayname= "getDisplayName",
P_GIVEN_AUTHLEVEL= "P_GIVEN_AUTH_LEVEL",
getGivenAuthlevel= "getGivenAuthLevel",
P_GIVEN_DISPLAYNAME= "P_GIVEN_DISPLAY_NAME",
getGivenDisplayname= "getGivenDisplayName",
P_LASTUSED_TIMESTAMP= "P_LAST_USED_TIMESTAMP",
getLastusedTimestamp= "getLastUsedTimestamp",
P_AUTHREQUEST_COUNT= "P_AUTH_REQUEST_COUNT",
getAuthrequestCount= "getAuthRequestCount",
Authlevel= "AuthLevel",
AUTHLEVEL= "AuthLevel",
ExtraAuthreqFields= "ExtraAuthReqFields",
EXTRA_AUTHREQ_FIELDS= "ExtraAuthReqFields",
Identitytype= "IdentityType",
IDENTITYTYPE= "IdentityType",
Normalizeresult= "NormalizeResult",
NORMALIZERESULT= "NormalizeResult",
normalizePstnwithCountry= "normalizePstnWithCountry",
P_CONVO_ID= "P_CONVERSATION",
getConvoId= "getConversation",
P_DEBUGINFO= "P_DEBUG_INFO",
getDebuginfo= "getDebugInfo",
P_GIVEN_DISPLAYNAME= "P_GIVEN_DISPLAY_NAME",
getGivenDisplayname= "getGivenDisplayName",
P_DISPLAYNAME= "P_DISPLAY_NAME",
getDisplayname= "getDisplayName",
P_LOCAL_LIVESTATUS= "P_LOCAL_LIVE_STATUS",
getLocalLivestatus= "getLocalLiveStatus",
P_ACTIVE_VM_ID= "P_ACTIVE_VOICEMAIL",
getActiveVmId= "getActiveVoicemail",
P_PASSWORDHINT= "P_PASSWORD_HINT",
getPasswordhint= "getPasswordHint",
LocalLivestatus= "LocalLiveStatus",
LOCAL_LIVESTATUS= "LocalLiveStatus",
Participantfilter= "ParticipantFilter",
PARTICIPANTFILTER= "ParticipantFilter",
P_CONVO_ID= "P_CONVERSATION",
getConvoId= "getConversation",
P_AUTHOR_DISPLAYNAME= "P_AUTHOR_DISPLAY_NAME",
getAuthorDisplayname= "getAuthorDisplayName",
STARTED_LIVESESSION= "STARTED_LIVE_SESSION",
ENDED_LIVESESSION= "ENDED_LIVE_SESSION",
P_DEBUGINFO= "P_DEBUG_INFO",
getDebuginfo= "getDebugInfo",
Mediatype= "MediaType",
MEDIATYPE= "MediaType",
Qualitytesttype= "QualityTestType",
QUALITYTESTTYPE= "QualityTestType",
Qualitytestresult= "QualityTestResult",
QUALITYTESTRESULT= "QualityTestResult",
P_PARTNER_DISPNAME= "P_PARTNER_DISPLAY_NAME",
getPartnerDispname= "getPartnerDisplayName",
P_FAILUREREASON= "P_FAILURE_REASON",
getFailurereason= "getFailureReason",
P_CONVO_ID= "P_CONVERSATION",
getConvoId= "getConversation",
P_CHATMSG_GUID= "P_CHAT_MSG_GUID",
getChatmsgGuid= "getChatMsgGuid",
Failurereason= "FailureReason",
FAILUREREASON= "FailureReason",
Preparesoundresult= "PrepareSoundResult",
PREPARESOUNDRESULT= "PrepareSoundResult",
AUDIODEVICE_CAPABILITIES= "AUDIO_DEVICE_CAPABILITIES",
P_FAILUREREASON= "P_FAILURE_REASON",
getFailurereason= "getFailureReason",
P_CHATMSG_ID= "P_CHAT_MSG",
getChatmsgId= "getChatMsg",
Failurereason= "FailureReason",
FAILUREREASON= "FailureReason",
Targetstatus= "TargetStatus",
TARGETSTATUS= "TargetStatus",
Setbodyresult= "SetBodyResult",
SETBODYRESULT= "SetBodyResult",
P_PARTNER_DISPNAME= "P_PARTNER_DISPLAY_NAME",
getPartnerDispname= "getPartnerDisplayName",
P_FAILUREREASON= "P_FAILURE_REASON",
getFailurereason= "getFailureReason",
P_STARTTIME= "P_START_TIME",
getStarttime= "getStartTime",
P_FINISHTIME= "P_FINISH_TIME",
getFinishtime= "getFinishTime",
P_FILEPATH= "P_FILE_PATH",
getFilepath= "getFilePath",
P_FILENAME= "P_FILE_NAME",
getFilename= "getFileName",
P_FILESIZE= "P_FILE_SIZE",
getFilesize= "getFileSize",
P_BYTESTRANSFERRED= "P_BYTES_TRANSFERRED",
getBytestransferred= "getBytesTransferred",
P_BYTESPERSECOND= "P_BYTES_PER_SECOND",
getBytespersecond= "getBytesPerSecond",
P_CHATMSG_GUID= "P_CHAT_MSG_GUID",
getChatmsgGuid= "getChatMsgGuid",
P_CHATMSG_INDEX= "P_CHAT_MSG_INDEX",
getChatmsgIndex= "getChatMsgIndex",
P_CONVO_ID= "P_CONVERSATION",
getConvoId= "getConversation",
Failurereason= "FailureReason",
FAILUREREASON= "FailureReason",
P_PWDCHANGESTATUS= "P_PWD_CHANGE_STATUS",
getPwdchangestatus= "getPwdChangeStatus",
P_LOGOUTREASON= "P_LOGOUT_REASON",
getLogoutreason= "getLogoutReason",
P_COMMITSTATUS= "P_COMMIT_STATUS",
getCommitstatus= "getCommitStatus",
P_SUGGESTED_SKYPENAME= "P_SUGGESTED_SKYPE_NAME",
getSuggestedSkypename= "getSuggestedSkypeName",
P_CBLSYNCSTATUS= "P_CBL_SYNC_STATUS",
getCblsyncstatus= "getCblSyncStatus",
P_OFFLINE_CALLFORWARD= "P_OFFLINE_CALL_FORWARD",
getOfflineCallforward= "getOfflineCallForward",
P_BUDDYCOUNT_POLICY= "P_BUDDY_COUNT_POLICY",
getBuddycountPolicy= "getBuddyCountPolicy",
P_WEBPRESENCE_POLICY= "P_WEB_PRESENCE_POLICY",
getWebpresencePolicy= "getWebPresencePolicy",
P_PHONENUMBERS_POLICY= "P_PHONE_NUMBERS_POLICY",
getPhonenumbersPolicy= "getPhoneNumbersPolicy",
P_PARTNER_OPTEDOUT= "P_PARTNER_OPTED_OUT",
getPartnerOptedout= "getPartnerOptedOut",
P_NR_OF_OTHER_INSTANCES= "P_OTHER_INSTANCES_COUNT",
getNrOfOtherInstances= "getOtherInstancesCount",
P_SKYPENAME= "P_SKYPE_NAME",
getSkypename= "getSkypeName",
P_FULLNAME= "P_FULL_NAME",
getFullname= "getFullName",
Cblsyncstatus= "CblSyncStatus",
CBLSYNCSTATUS= "CblSyncStatus",
Logoutreason= "LogoutReason",
LOGOUTREASON= "LogoutReason",
Pwdchangestatus= "PwdChangeStatus",
PWDCHANGESTATUS= "PwdChangeStatus",
setServersideIntProperty= "setServerSideIntProperty",
setServersideStrProperty= "setServerSideStrProperty",
Commitstatus= "CommitStatus",
COMMITSTATUS= "CommitStatus",
Chatpolicy= "ChatPolicy",
CHATPOLICY= "ChatPolicy",
Skypecallpolicy= "SkypeCallPolicy",
SKYPECALLPOLICY= "SkypeCallPolicy",
Pstncallpolicy= "PstnCallPolicy",
PSTNCALLPOLICY= "PstnCallPolicy",
Avatarpolicy= "AvatarPolicy",
AVATARPOLICY= "AvatarPolicy",
Buddycountpolicy= "BuddyCountPolicy",
BUDDYCOUNTPOLICY= "BuddyCountPolicy",
Timezonepolicy= "TimezonePolicy",
TIMEZONEPOLICY= "TimezonePolicy",
Webpresencepolicy= "WebPresencePolicy",
WEBPRESENCEPOLICY= "WebPresencePolicy",
Phonenumberspolicy= "PhoneNumbersPolicy",
PHONENUMBERSPOLICY= "PhoneNumbersPolicy",
Voicemailpolicy= "VoicemailPolicy",
VOICEMAILPOLICY= "VoicemailPolicy",
Capabilitystatus= "CapabilityStatus",
CAPABILITYSTATUS= "CapabilityStatus",
getSkypenameHash= "getSkypeNameHash",
Proxytype= "ProxyType",
PROXYTYPE= "ProxyType",
getIsolanguageInfo= "getIsoLanguageInfo",
getIsocountryInfo= "getIsoCountryInfo",
getIsocountryCodebyPhoneNo= "getIsoCountryCodeByPhoneNo",
}

modules = {
  Skype = true,
  ContactGroup = true,
  Contact = true,
  ContactSearch = true,
  Participant = true,
  Conversation = true,
  Message = true,
  Video = true,
  Voicemail = true,
  Sms = true,
  Transfer = true,
  Account = true,
}

rules:add('getter refactoring', '%.%s*[gG]et[^P%p%s]+Property%(([^%)]+)%s*%)',
          function (property)
              local b,e = property:find('P[rR][oO][pP][eE][rR][tT][yY]%.')
              if b then
                  property = property:sub(e+1)
                  property = under_lined2UpperCamel(property)
              end
              return '.get'..property..'()'
          end)

rules:add('enum .getId() suppressing', '(%u%a+)(%.[%u_]+%.[%a_]+)%.getId%(%)',
		  function (class,enum)
		    if modules[class] then return class..enum end
			return class..enum..'.getid()'
		  end)
rules:add('enum .name() suppressing', '([%.%a_]+)(%b())',
          function(call, parm)
             local last = ''
             for n in call:gmatch'%.[%a_]+' do last = n end
             if last == '.name' then return parm:sub(2,#parm-1) end
             return call..parm
          end)

rules:add('enum conversion suppressing', '(%u%a+)%.([%u_]+)%.get(%b())',
		  function (class,enum,quote)
		    if modules[class] then return quote:sub(2,#quote-1) end
			return class..'.'..enum..'.'..quote
		  end)

rules:add('enum constant refactoring', '(%u%a+)%.([%u_]+)%.([%u_]+)',
           function (class, enumtype, enumval)
		      if modules[class] then
                  return class..'.'..under_lined2UpperCamel(enumtype)..'.'..enumval
              end
			  return class..'.'..enumtype..'.'..enumval
		   end)
rules:add('property constant refactoring 2', '(%u%a+)%.Property%.([%u_]+)',
           function (mod, enumval)
               if enumval:sub(1,2) ~= 'P_' then
                   return mod..'.Property.P_'..enumval:upper()
               end
               return mod..'.Property.'..enumval
           end)

rules:add('property constant refactoring', '(%u%a+)%.PROPERTY%.([%l_]+)',
           function (mod, enumval)
               return mod..'.Property.P_'..enumval:upper()
           end)

rules:add('property change signature refactoring',
          'void%s+[oO]nPropertyChange%s*%(([%l%.%s]*)SkypeObject%s([%w_]+)%s*,([^,]*),([%s%l]*)Object%s+([%w_]+)',
          function (prefix, objparmname, property, valueprefix, valueparmname)
              local b,e = property:find('%.P[rR][oO][pP][eE][rR][tT][yY]')
              local objtypename = '<too difficult guessing>'
              if b then
                 for id in property:sub(1, b-1):gmatch('[^%.]+') do
                     objtypename = id
                 end
              end
              local propertyname = 'prop'
              for p in property:gmatch'[%w_]+' do propertyname = p end
              return 'void onPropertyChange('..prefix..objtypename..' '..objparmname..','
                                             ..prefix..objtypename .. '.Property '..propertyname..','
                                             ..valueprefix..'int '..valueparmname..','
                                             ..valueprefix..'String s'..valueparmname
          end)

methods = {
Start= "start",
GetVersionString= "getVersionString",
GetUnixTimestamp= "getUnixTimestamp",
GiveDisplayName= "giveDisplayName",
Delete= "delete",
GetConversations= "getConversations",
CanAddConversation= "canAddConversation",
AddConversation= "addConversation",
CanRemoveConversation= "canRemoveConversation",
RemoveConversation= "removeConversation",
OnChangeConversation= "onChangeConversation",
GetContacts= "getContacts",
CanAddContact= "canAddContact",
AddContact= "addContact",
CanRemoveContact= "canRemoveContact",
RemoveContact= "removeContact",
OnChange= "onChange",
GetHardwiredContactGroup= "getHardwiredContactGroup",
GetCustomContactGroups= "getCustomContactGroups",
CreateCustomContactGroup= "createCustomContactGroup",
OnNewCustomContactGroup= "onNewCustomContactGroup",
GetIdentity= "getIdentity",
GetAvatar= "getAvatar",
GetVerifiedEmail= "getVerifiedEmail",
GetVerifiedCompany= "getVerifiedCompany",
IsMemberOf= "isMemberOf",
IsMemberOfHardwiredGroup= "isMemberOfHardwiredGroup",
SetBlocked= "setBlocked",
IgnoreAuthRequest= "ignoreAuthRequest",
GiveDisplayName= "giveDisplayName",
SetBuddyStatus= "setBuddyStatus",
SendAuthRequest= "sendAuthRequest",
HasAuthorizedMe= "hasAuthorizedMe",
SetPhoneNumber= "setPhoneNumber",
OpenConversation= "openConversation",
HasCapability= "hasCapability",
GetCapabilityStatus= "getCapabilityStatus",
RefreshProfile= "refreshProfile",
GetContactType= "getContactType",
GetContact= "getContact",
FindContactByPstnNumber= "findContactByPstnNumber",
GetIdentityType= "getIdentityType",
IdentitiesMatch= "identitiesMatch",
NormalizeIdentity= "normalizeIdentity",
NormalizePstnwithCountry= "normalizePstnWithCountry",
OnContactOnlineAppearance= "onContactOnlineAppearance",
OnContactGoneOffline= "onContactGoneOffline",
AddMinAgeTerm= "addMinAgeTerm",
AddMaxAgeTerm= "addMaxAgeTerm",
AddEmailTerm= "addEmailTerm",
AddLanguageTerm= "addLanguageTerm",
AddStrTerm= "addStrTerm",
AddIntTerm= "addIntTerm",
AddOr= "addOr",
IsValid= "isValid",
Submit= "submit",
Extend= "extend",
Release= "release",
GetResults= "getResults",
OnNewResult= "onNewResult",
GetOptimalAgeRanges= "getOptimalAgeRanges",
CreateContactSearch= "createContactSearch",
CreateBasicContactSearch= "createBasicContactSearch",
CreateIdentitySearch= "createIdentitySearch",
CanSetRankTo= "canSetRankTo",
SetRankTo= "setRankTo",
Ring= "ring",
RingIt= "ringIt",
SetLiveIdentityToUse= "setLiveIdentityToUse",
GetVideo= "getVideo",
Hangup= "hangup",
Retire= "retire",
OnIncomingDtmf= "onIncomingDtmf",
SetOption= "setOption",
SetTopic= "setTopic",
SetGuidelines= "setGuidelines",
SetPicture= "setPicture",
SpawnConference= "spawnConference",
AddConsumers= "addConsumers",
Assimilate= "assimilate",
JoinLiveSession= "joinLiveSession",
RingOthers= "ringOthers",
MuteMyMicrophone= "muteMyMicrophone",
UnmuteMyMicrophone= "unmuteMyMicrophone",
HoldMyLiveSession= "holdMyLiveSession",
ResumeMyLiveSession= "resumeMyLiveSession",
LeaveLiveSession= "leaveLiveSession",
StartVoiceMessage= "startVoiceMessage",
TransferLiveSession= "transferLiveSession",
CanTransferLiveSession= "canTransferLiveSession",
SendDtmf= "sendDtmf",
StopSendDtmf= "stopSendDtmf",
SetMyTextStatusTo= "setMyTextStatusTo",
PostText= "postText",
PostContacts= "postContacts",
PostFiles= "postFiles",
PostVoiceMessage= "postVoiceMessage",
PostSms= "postSms",
GetJoinBlob= "getJoinBlob",
Join= "join",
EnterPassword= "enterPassword",
SetPassword= "setPassword",
RetireFrom= "retireFrom",
Delete= "delete",
RenameTo= "renameTo",
SetBookmark= "setBookmark",
SetAlertString= "setAlertString",
RemoveFromInbox= "removeFromInbox",
AddToInbox= "addToInbox",
SetConsumedHorizon= "setConsumedHorizon",
MarkUnread= "markUnread",
IsMemberOf= "isMemberOf",
GetParticipants= "getParticipants",
GetLastMessages= "getLastMessages",
FindMessage= "findMessage",
OnParticipantListChange= "onParticipantListChange",
OnMessage= "onMessage",
OnSpawnConference= "onSpawnConference",
CreateConference= "createConference",
GetConversationByIdentity= "getConversationByIdentity",
GetConversationByParticipants= "getConversationByParticipants",
GetConversationByBlob= "getConversationByBlob",
GetConversationList= "getConversationList",
OnConversationListChange= "onConversationListChange",
CanEdit= "canEdit",
Edit= "edit",
GetContacts= "getContacts",
GetTransfers= "getTransfers",
GetVoiceMessage= "getVoiceMessage",
GetSms= "getSms",
DeleteLocally= "deleteLocally",
GetMessageByGuid= "getMessageByGuid",
GetMessageListByType= "getMessageListByType",
OnMessage= "onMessage",
SetScreen= "setScreen",
Start= "start",
Stop= "stop",
SubmitCaptureRequest= "submitCaptureRequest",
OnCaptureRequestCompleted= "onCaptureRequestCompleted",
SetScreenCaptureRectangle= "setScreenCaptureRectangle",
SetRenderRectangle= "setRenderRectangle",
SetRemoteRendererId= "setRemoteRendererId",
SelectVideoSource= "selectVideoSource",
GetCurrentVideoDevice= "getCurrentVideoDevice",
GetAvailableVideoDevices= "getAvailableVideoDevices",
HasVideoDeviceCapability= "hasVideoDeviceCapability",
DisplayVideoDeviceTuningDialog= "displayVideoDeviceTuningDialog",
GetPreviewVideo= "getPreviewVideo",
VideoCommand= "videoCommand",
OnAvailableVideoDeviceListChange= "onAvailableVideoDeviceListChange",
OnH264Activated= "onH264Activated",
StartMonitoringQuality= "startMonitoringQuality",
StopMonitoringQuality= "stopMonitoringQuality",
OnQualityTestResult= "onQualityTestResult",
StartRecording= "startRecording",
StopRecording= "stopRecording",
StartPlayback= "startPlayback",
StopPlayback= "stopPlayback",
Delete= "delete",
Cancel= "cancel",
CheckPermission= "checkPermission",
GetGreeting= "getGreeting",
PlayStart= "playStart",
PlayStartFromFile= "playStartFromFile",
PlayStop= "playStop",
StartRecordingTest= "startRecordingTest",
StopRecordingTest= "stopRecordingTest",
GetAvailableOutputDevices= "getAvailableOutputDevices",
GetAvailableRecordingDevices= "getAvailableRecordingDevices",
SelectSoundDevices= "selectSoundDevices",
GetAudioDeviceCapabilities= "getAudioDeviceCapabilities",
GetNrgLevels= "getNrgLevels",
VoiceCommand= "voiceCommand",
GetSpeakerVolume= "getSpeakerVolume",
SetSpeakerVolume= "setSpeakerVolume",
GetMicVolume= "getMicVolume",
SetMicVolume= "setMicVolume",
IsSpeakerMuted= "isSpeakerMuted",
IsMicrophoneMuted= "isMicrophoneMuted",
MuteSpeakers= "muteSpeakers",
MuteMicrophone= "muteMicrophone",
OnAvailableDeviceListChange= "onAvailableDeviceListChange",
OnNrgLevelsChange= "onNrgLevelsChange",
SetOperatingMedia= "setOperatingMedia",
GetTargetStatus= "getTargetStatus",
GetTargetPrice= "getTargetPrice",
SetTargets= "setTargets",
SetBody= "setBody",
GetBodyChunks= "getBodyChunks",
RequestConfirmationCode= "requestConfirmationCode",
SubmitConfirmationCode= "submitConfirmationCode",
CreateOutgoingSms= "createOutgoingSms",
Accept= "accept",
Pause= "pause",
Resume= "resume",
Cancel= "cancel",
GetStatusWithProgress= "getStatusWithProgress",
Login= "login",
LoginWithPassword= "loginWithPassword",
Register= "register",
Logout= "logout",
ChangePassword= "changePassword",
SetPasswordSaved= "setPasswordSaved",
SetServersideIntProperty= "setServerSideIntProperty",
SetServersideStrProperty= "setServerSideStrProperty",
CancelServerCommit= "cancelServerCommit",
SetIntProperty= "setIntProperty",
SetStrProperty= "setStrProperty",
SetBinProperty= "setBinProperty",
SetAvailability= "setAvailability",
SetStandby= "setStandby",
GetCapabilityStatus= "getCapabilityStatus",
GetSkypenameHash= "getSkypeNameHash",
GetVerifiedEmail= "getVerifiedEmail",
GetVerifiedCompany= "getVerifiedCompany",
Delete= "delete",
GetAccount= "getAccount",
GetExistingAccounts= "getExistingAccounts",
GetDefaultAccountName= "getDefaultAccountName",
GetSuggestedSkypename= "getSuggestedSkypename",
ValidateAvatar= "validateAvatar",
ValidateProfileString= "validateProfileString",
ValidatePassword= "validatePassword",
OnProxyAuthFailure= "onProxyAuthFailure",
GetUsedPort= "getUsedPort",
GetStr= "getStr",
GetInt= "getInt",
GetBin= "getBin",
SetStr= "setStr",
SetInt= "setInt",
SetBin= "setBin",
IsDefined= "isDefined",
Delete= "delete",
GetSubKeys= "getSubKeys",
GetIsolanguageInfo= "getIsoLanguageInfo",
GetIsocountryInfo= "getIsoCountryInfo",
GetIsocountryCodebyPhoneNo= "getIsoCountryCodeByPhoneNo",
App2AppCreate= "app2AppCreate",
App2AppDelete= "app2AppDelete",
App2AppConnect= "app2AppConnect",
App2AppDisconnect= "app2AppDisconnect",
App2AppWrite= "app2AppWrite",
App2AppDatagram= "app2AppDatagram",
App2AppRead= "app2AppRead",
App2AppGetConnectableUsers= "app2AppGetConnectableUsers",
App2AppGetStreamsList= "app2AppGetStreamsList",
OnApp2AppDatagram= "onApp2AppDatagram",
OnApp2AppStreamListChange= "onApp2AppStreamListChange",}
rules:add('method refactoring', '%.%s*(%u[%w_]+)%s*%(', function (name) return '.'..(methods[name] or name)..'(' end)
rules:add('listener refactoring', '(%u[%a]+)%.(%u[%a]+)',
          function (class, listener)
		     if listener == class..'Listener' and modules[class] then return class..'Listener' end
			 return class..'.'..listener
		  end)
event_class = {
onChangeConversation= "ContactGroup",
onChange= "ContactGroup",
onNewResult= "ContactSearch",
onParticipantListChange= "Conversation",
onMessage= "Conversation",
onSpawnConference= "Conversation",
onIncomingDtmf= "Participant",
onCaptureRequestCompleted= "Video",
}
rules:add('event handler refactoring', 'void%s+On([%w]+)%s*%(%s*SkypeObject',
           function (name)
               local target = event_class['on'..under_lined2UpperCamel(name)] or '<too difficult guessing>'
		       return 'void on'..under_lined2UpperCamel(name)..'('..target
		   end)
rules:add('global event handler refactoring', 'void%s*On([%w]+)%s*%(',
           function (name) return 'void on'..under_lined2UpperCamel(name)..'(Skype obj, ' end)

results = {
PostFilesResult = "PostFilesResponse",
GetLastMessagesResult = "GetLastMessagesResponse",
FindContactByPstnNumberResult = "FindContactByPstnNumberResponse",
NormalizeIdentityResult = "NormalizeIdentityResponse",
NormalizePSTNWithCountryResult = "NormalizePSTNWithCountryResponse",
GetAvailableVideoDevicesResult = "GetAvailableVideoDevicesResponse",
GetAvailableOutputDevicesResult = "GetAvailableOutputDevicesResponse",
GetAvailableRecordingDevicesResult = "GetAvailableRecordingDevicesResponse",
GetAudioDeviceCapabilitiesResult = "GetAudioDeviceCapabilitiesResponse",
GetNrgLevelsResult = "GetNrgLevelsResponse",
ValidateAvatarResult = "ValidateAvatarResponse",
ValidateProfileStringResult = "ValidateProfileStringResponse",
GetISOLanguageInfoResult = "GetISOLanguageInfoResponse",
GetISOCountryInfoResult = "GetISOCountryInfoResponse",
SetSMSBodyResult = "SetSMSBodyResponse",
GetBodyChunksResult = "GetBodyChunksResponse",
SubmitCaptureRequestResult = "SubmitCaptureRequestResponse",
GetCurrentVideoDeviceResult = "GetCurrentVideoDeviceResponse",
}
rules:add('XxxResult renaming', '(%u%a+)(%s+%a)',
		  function (t, v) t = results[t] or t
		      return t..v
		  end)


local ok, iup = pcall(require, "iuplua")
if ok then

console = {}

console.prompt = iup.text{expand="Horizontal", dragdrop = "Yes"}
console.prompt.value = arg and arg[1] or ''

console.output = iup.text{expand="Yes",
                  readonly="Yes",
                  bgcolor="232 232 232",
                  font = "Courier, 11",
                  appendnewline = "No",
                  multiline = "Yes"}

console.prompt.tip = "type the directory path of the source to be converted\n"..
                     "you may as well drop the directory from the file manager\n"..
                     "Ctrl+O - opens the file selector\n"

console.orig_output = io.output
console.orig_write = io.write
console.orig_print = print

function io.output(filename)
  console.orig_output(filename)
  if (filename) then
    io.write = console.orig_write
  else
    io.write = console.new_write
  end
end

function console.new_write(...)
  -- Try to simulate the same behavior of the standard io.write
  local arg = {...}
  local str -- allow to print a nil value
  for i,v in ipairs(arg) do
    if (str) then
      str = str .. tostring(v)
    else
      str = tostring(v)
    end
  end
  console.print2output(str, true)
end
io.write = console.new_write

function print(...)
  -- Try to simulate the same behavior of the standard print
  local arg = {...}
  local str -- allow to print a nil value
  for i,v in ipairs(arg) do
    if (i > 1) then
      str = str .. "\t"  -- only add Tab for more than 1 parameters
    end
    if (str) then
      str = str .. tostring(v)
    else
      str = tostring(v)
    end
  end
  console.print2output(str)
end

function console.print2output(s, no_newline)
  if (no_newline) then
    console.output.append = tostring(s)
    console.no_newline = no_newline
  else
    if (console.no_newline) then
      -- if io.write was called, then a print is called, must add a new line before
      console.output.append = "\n" .. tostring(s) .. "\n"
      console.no_newline = nil
    else
      console.output.append = tostring(s) .. "\n"
    end
  end
end


function console.do_string(cmd)
  rules:migrate(cmd)
end

function console.open_file()
  local fd=iup.filedlg{dialogtype="DIR", title="Select java source directory",
                       nochangedir="NO", directory=console.last_directory,}
  fd:popup(iup.CENTER, iup.CENTER)
  local status = fd.status
  local filename = fd.value
  console.last_directory = fd.directory -- save the previous directory
  fd:destroy()
  if (status == "-1") or (status == "1") then
    if (status == "1") then
      error ("Cannot load file: "..filename)
    end
  else
    rules:migrate(filename)
  end
end

function console.prompt:dropfiles_cb(filename)
  -- will execute all dropped files, can drop more than one at once
  -- works in Windows and in Linux
  rules:migrate(filename)
end

function console.prompt:k_any(key)
  if (key == iup.K_CR) then  -- Enter executes the string
    rules:migrate(self.value)
    self.value = ""
  end
  if (key == iup.K_ESC) then  -- Esc clears console.prompt
    self.value = ""
  end
  if (key == iup.K_cO) then  -- Ctrl+O selects a file and execute it
    console.open_file()
  end
  if (key == iup.K_cX) then  -- Ctrl+X exits the console
    console.dialog:close_cb()
  end
  if (key == iup.K_cDEL) then  -- Ctrl+Del clears console.output
    console.output.value = ""
  end
end

console.dialog = iup.dialog
{
  iup.vbox
  {
    iup.frame
    {
      iup.hbox -- use it to inherit margins
      {
        console.prompt,
      },
      title = "Base java dir:",
    },
    iup.frame
    {
      iup.hbox -- use it to inherit margins
      {
        console.output
      },
      title = "Migration:",
    },
    margin = "5x5",
    gap = "5",
  },
  title="Java2 migrator",
  size="250x180", -- initial size
  icon=0, -- use the Lua icon from the executable in Windows
}

function console.dialog:close_cb()
  print = console.orig_print  -- restore print and io.write
  io.write = console.orig_write
  iup.ExitLoop()  -- should be removed if used inside a bigger application
  console.dialog:destroy()
  return iup.IGNORE
end

console.dialog:show()
console.dialog.size = nil -- reset initial size, allow resize to smaller values
iup.SetFocus(console.prompt)


if (iup.MainLoopLevel() == 0) then
  iup.MainLoop()
end

else
  rules:migrate(assert(arg and arg[1] or '.'))
end

