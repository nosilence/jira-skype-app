#ifndef __SIDG_MSGS_SKYPEHPP_INCLUDED__
#define __SIDG_MSGS_SKYPEHPP_INCLUDED__

#include "sidg_Skyperefs.hpp"

#ifdef SE_USE_NAMESPACE
namespace Skype {
#endif
        extern Sid::Field SkypeFields[];
        struct MsgconfigGetVersionStringResponse {
                Sid::String version;
                enum { BEGIN = 0, END = 1 };
        };
        struct MsgconfigGetUnixTimestampResponse {
                uint timestamp;
                enum { BEGIN = 1, END = 2 };
        };
        struct MsgconfigStartResponse {
                bool started;
                enum { BEGIN = 2, END = 3 };
        };
        struct MsgcontactContactGroupGiveDisplayNameRequest {
                ContactGroupRef objectID;
                Sid::String     name;
                enum { BEGIN = 3, END = 5 };
        };
        struct MsgcontactContactGroupDeleteRequest {
                ContactGroupRef objectID;
                enum { BEGIN = 5, END = 6 };
        };
        struct MsgcontactContactGroupDeleteResponse {
                bool result;
                enum { BEGIN = 6, END = 7 };
        };
        struct MsgcontactContactGroupGetConversationsRequest {
                ContactGroupRef objectID;
                enum { BEGIN = 7, END = 8 };
        };
        struct MsgcontactContactGroupGetConversationsResponse {
                ConversationRefs conversations;
                enum { BEGIN = 8, END = 10 };
        };
        struct MsgcontactContactGroupCanAddConversationRequest {
                ContactGroupRef objectID;
                ConversationRef conversation;
                enum { BEGIN = 10, END = 12 };
        };
        struct MsgcontactContactGroupCanAddConversationResponse {
                bool result;
                enum { BEGIN = 12, END = 13 };
        };
        struct MsgcontactContactGroupAddConversationRequest {
                ContactGroupRef objectID;
                ConversationRef conversation;
                enum { BEGIN = 13, END = 15 };
        };
        struct MsgcontactContactGroupCanRemoveConversationRequest {
                ContactGroupRef objectID;
                enum { BEGIN = 15, END = 16 };
        };
        struct MsgcontactContactGroupCanRemoveConversationResponse {
                bool result;
                enum { BEGIN = 16, END = 17 };
        };
        struct MsgcontactContactGroupRemoveConversationRequest {
                ContactGroupRef objectID;
                ConversationRef conversation;
                enum { BEGIN = 17, END = 19 };
        };
        struct MsgcontactContactGroupOnChangeConversation {
                ContactGroupRef objectID;
                ConversationRef conversation;
                enum { BEGIN = 19, END = 21 };
        };
        struct MsgcontactContactGroupGetContactsRequest {
                ContactGroupRef objectID;
                enum { BEGIN = 21, END = 22 };
        };
        struct MsgcontactContactGroupGetContactsResponse {
                ContactRefs contacts;
                enum { BEGIN = 22, END = 24 };
        };
        struct MsgcontactContactGroupCanAddContactRequest {
                ContactGroupRef objectID;
                ContactRef      contact;
                enum { BEGIN = 24, END = 26 };
        };
        struct MsgcontactContactGroupCanAddContactResponse {
                bool result;
                enum { BEGIN = 26, END = 27 };
        };
        struct MsgcontactContactGroupAddContactRequest {
                ContactGroupRef objectID;
                ContactRef      contact;
                enum { BEGIN = 27, END = 29 };
        };
        struct MsgcontactContactGroupCanRemoveContactRequest {
                ContactGroupRef objectID;
                enum { BEGIN = 29, END = 30 };
        };
        struct MsgcontactContactGroupCanRemoveContactResponse {
                bool result;
                enum { BEGIN = 30, END = 31 };
        };
        struct MsgcontactContactGroupRemoveContactRequest {
                ContactGroupRef objectID;
                ContactRef      contact;
                enum { BEGIN = 31, END = 33 };
        };
        struct MsgcontactContactGroupOnChange {
                ContactGroupRef objectID;
                ContactRef      contact;
                enum { BEGIN = 33, END = 35 };
        };
        struct MsgcontactContactGroup {
                Sid::String given_displayname;
                uint        nrofcontacts;
                uint        nrofcontacts_online;
                uint        custom_group_id;
                int         type;
                Sid::List_uint cachedProps;
                enum { BEGIN = 35, END = 40 };
        };
        struct MsgcontactGetHardwiredContactGroupRequest {
                int type;
                enum { BEGIN = 40, END = 41 };
        };
        struct MsgcontactGetHardwiredContactGroupResponse {
                ContactGroupRef contactGroup;
                enum { BEGIN = 41, END = 42 };
        };
        struct MsgcontactGetCustomContactGroupsResponse {
                ContactGroupRefs groups;
                enum { BEGIN = 42, END = 44 };
        };
        struct MsgcontactCreateCustomContactGroupResponse {
                ContactGroupRef group;
                enum { BEGIN = 44, END = 45 };
        };
        struct MsgcontactOnNewCustomContactGroup {
                ContactGroupRef group;
                enum { BEGIN = 45, END = 46 };
        };
        struct MsgcontactContactGetIdentityRequest {
                ContactRef objectID;
                enum { BEGIN = 46, END = 47 };
        };
        struct MsgcontactContactGetIdentityResponse {
                Sid::String identity;
                enum { BEGIN = 47, END = 48 };
        };
        struct MsgcontactContactGetAvatarRequest {
                ContactRef objectID;
                enum { BEGIN = 48, END = 49 };
        };
        struct MsgcontactContactGetAvatarResponse {
                bool        present;
                Sid::Binary avatar;
                enum { BEGIN = 49, END = 51 };
        };
        struct MsgcontactContactGetVerifiedEmailRequest {
                ContactRef objectID;
                enum { BEGIN = 51, END = 52 };
        };
        struct MsgcontactContactGetVerifiedEmailResponse {
                Sid::String email;
                enum { BEGIN = 52, END = 53 };
        };
        struct MsgcontactContactGetVerifiedCompanyRequest {
                ContactRef objectID;
                enum { BEGIN = 53, END = 54 };
        };
        struct MsgcontactContactGetVerifiedCompanyResponse {
                Sid::String company;
                enum { BEGIN = 54, END = 55 };
        };
        struct MsgcontactContactIsMemberOfRequest {
                ContactRef      objectID;
                ContactGroupRef group;
                enum { BEGIN = 55, END = 57 };
        };
        struct MsgcontactContactIsMemberOfResponse {
                bool result;
                enum { BEGIN = 57, END = 58 };
        };
        struct MsgcontactContactIsMemberOfHardwiredGroupRequest {
                ContactRef objectID;
                int        groupType;
                enum { BEGIN = 58, END = 60 };
        };
        struct MsgcontactContactIsMemberOfHardwiredGroupResponse {
                bool result;
                enum { BEGIN = 60, END = 61 };
        };
        struct MsgcontactContactSetBlockedRequest {
                ContactRef objectID;
                bool       blocked;
                bool       abuse;
                enum { BEGIN = 61, END = 64 };
        };
        struct MsgcontactContactIgnoreAuthRequestRequest {
                ContactRef objectID;
                enum { BEGIN = 64, END = 65 };
        };
        struct MsgcontactContactGiveDisplayNameRequest {
                ContactRef  objectID;
                Sid::String name;
                enum { BEGIN = 65, END = 67 };
        };
        struct MsgcontactContactSetBuddyStatusRequest {
                ContactRef objectID;
                bool       isMyBuddy;
                bool       syncAuth;
                enum { BEGIN = 67, END = 70 };
        };
        struct MsgcontactContactSendAuthRequestRequest {
                ContactRef  objectID;
                Sid::String message;
                uint        extras_bitmask;                enum { BEGIN = 70, END = 73 };
        };
        struct MsgcontactContactHasAuthorizedMeRequest {
                ContactRef objectID;
                enum { BEGIN = 73, END = 74 };
        };
        struct MsgcontactContactHasAuthorizedMeResponse {
                bool result;
                enum { BEGIN = 74, END = 75 };
        };
        struct MsgcontactContactSetPhoneNumberRequest {
                ContactRef  objectID;
                uint        num;
                Sid::String label;
                Sid::String number;
                enum { BEGIN = 75, END = 79 };
        };
        struct MsgcontactContactOpenConversationRequest {
                ContactRef objectID;
                enum { BEGIN = 79, END = 80 };
        };
        struct MsgcontactContactOpenConversationResponse {
                ConversationRef conversation;
                enum { BEGIN = 80, END = 81 };
        };
        struct MsgcontactContactHasCapabilityRequest {
                ContactRef objectID;
                int        capability;
                bool       queryServer;
                enum { BEGIN = 81, END = 84 };
        };
        struct MsgcontactContactHasCapabilityResponse {
                bool result;
                enum { BEGIN = 84, END = 85 };
        };
        struct MsgcontactContactGetCapabilityStatusRequest {
                ContactRef objectID;
                int        capability;
                bool       queryServer;
                enum { BEGIN = 85, END = 88 };
        };
        struct MsgcontactContactGetCapabilityStatusResponse {
                int status;
                enum { BEGIN = 88, END = 89 };
        };
        struct MsgcontactContactRefreshProfileRequest {
                ContactRef objectID;
                enum { BEGIN = 89, END = 90 };
        };
        struct MsgcontactContact {
                Sid::String skypename;
                Sid::String fullname;
                Sid::String pstnnumber;
                uint        birthday;
                uint        gender;
                Sid::String languages;
                Sid::String country;
                Sid::String province;
                Sid::String city;
                Sid::String phone_home;
                Sid::String phone_office;
                Sid::String phone_mobile;
                Sid::String emails;
                Sid::String homepage;
                Sid::String about;
                uint        profile_timestamp;
                Sid::String received_authrequest;
                Sid::String displayname;
                bool        refreshing;
                int         given_authlevel;
                uint        authreq_timestamp;
                Sid::String mood_text;
                uint        timezone;
                uint        nrof_authed_buddies;
                Sid::String ipcountry;
                Sid::String given_displayname;
                int         availability;
                uint        lastonline_timestamp;
                Sid::Binary capabilities;
                Sid::Binary avatar_image;
                uint        lastused_timestamp;
                uint        authrequest_count;
                uint        popularity_ord;
                Sid::String assigned_comment;
                uint        avatar_timestamp;
                uint        mood_timestamp;
                Sid::String assigned_phone1;
                Sid::String assigned_phone1_label;
                Sid::String assigned_phone2;
                Sid::String assigned_phone2_label;
                Sid::String assigned_phone3;
                Sid::String assigned_phone3_label;
                int         type;
                Sid::String rich_mood_text;
                Sid::List_uint cachedProps;
                enum { BEGIN = 90, END = 134 };
        };
        struct MsgcontactGetContactTypeRequest {
                Sid::String identity;
                enum { BEGIN = 134, END = 135 };
        };
        struct MsgcontactGetContactTypeResponse {
                int type;
                enum { BEGIN = 135, END = 136 };
        };
        struct MsgcontactGetContactRequest {
                Sid::String identity;
                enum { BEGIN = 136, END = 137 };
        };
        struct MsgcontactGetContactResponse {
                ContactRef contact;
                enum { BEGIN = 137, END = 138 };
        };
        struct MsgcontactFindContactByPstnNumberRequest {
                Sid::String number;
                enum { BEGIN = 138, END = 139 };
        };
        struct MsgcontactFindContactByPstnNumberResponse {
                bool       found;
                ContactRef contact;
                uint       foundInKey;
                enum { BEGIN = 139, END = 142 };
        };
        struct MsgcontactGetIdentityTypeRequest {
                Sid::String identity;
                enum { BEGIN = 142, END = 143 };
        };
        struct MsgcontactGetIdentityTypeResponse {
                int type;
                enum { BEGIN = 143, END = 144 };
        };
        struct MsgcontactIdentitiesMatchRequest {
                Sid::String identityA;
                Sid::String identityB;
                enum { BEGIN = 144, END = 146 };
        };
        struct MsgcontactIdentitiesMatchResponse {
                bool result;
                enum { BEGIN = 146, END = 147 };
        };
        struct MsgcontactNormalizeIdentityRequest {
                Sid::String original;
                bool        isNewSkypeName;
                enum { BEGIN = 147, END = 149 };
        };
        struct MsgcontactNormalizeIdentityResponse {
                int         result;
                Sid::String normalized;
                enum { BEGIN = 149, END = 151 };
        };
        struct MsgcontactNormalizePSTNWithCountryRequest {
                Sid::String original;
                uint        countryPrefix;
                enum { BEGIN = 151, END = 153 };
        };
        struct MsgcontactNormalizePSTNWithCountryResponse {
                int         result;
                Sid::String normalized;
                enum { BEGIN = 153, END = 155 };
        };
        struct MsgcontactOnContactOnlineAppearance {
                ContactRef contact;
                enum { BEGIN = 155, END = 156 };
        };
        struct MsgcontactOnContactGoneOffline {
                ContactRef contact;
                enum { BEGIN = 156, END = 157 };
        };
        struct MsgcontactsearchContactSearchAddMinAgeTermRequest {
                ContactSearchRef objectID;
                uint             min_age_in_years;
                bool             add_to_subs;
                enum { BEGIN = 157, END = 160 };
        };
        struct MsgcontactsearchContactSearchAddMinAgeTermResponse {
                bool valid;
                enum { BEGIN = 160, END = 161 };
        };
        struct MsgcontactsearchContactSearchAddMaxAgeTermRequest {
                ContactSearchRef objectID;
                uint             max_age_in_years;
                bool             add_to_subs;
                enum { BEGIN = 161, END = 164 };
        };
        struct MsgcontactsearchContactSearchAddMaxAgeTermResponse {
                bool valid;
                enum { BEGIN = 164, END = 165 };
        };
        struct MsgcontactsearchContactSearchAddEmailTermRequest {
                ContactSearchRef objectID;
                Sid::String      email;
                bool             add_to_subs;
                enum { BEGIN = 165, END = 168 };
        };
        struct MsgcontactsearchContactSearchAddEmailTermResponse {
                bool valid;
                enum { BEGIN = 168, END = 169 };
        };
        struct MsgcontactsearchContactSearchAddLanguageTermRequest {
                ContactSearchRef objectID;
                Sid::String      language;
                bool             add_to_subs;
                enum { BEGIN = 169, END = 172 };
        };
        struct MsgcontactsearchContactSearchAddLanguageTermResponse {
                bool valid;
                enum { BEGIN = 172, END = 173 };
        };
        struct MsgcontactsearchContactSearchAddStrTermRequest {                ContactSearchRef objectID;
                int              prop;
                int              cond;
                Sid::String      value;
                bool             add_to_subs;
                enum { BEGIN = 173, END = 178 };
        };
        struct MsgcontactsearchContactSearchAddStrTermResponse {
                bool valid;
                enum { BEGIN = 178, END = 179 };
        };
        struct MsgcontactsearchContactSearchAddIntTermRequest {
                ContactSearchRef objectID;
                int              prop;
                int              cond;
                uint             value;
                bool             add_to_subs;
                enum { BEGIN = 179, END = 184 };
        };
        struct MsgcontactsearchContactSearchAddIntTermResponse {
                bool valid;
                enum { BEGIN = 184, END = 185 };
        };
        struct MsgcontactsearchContactSearchAddOrRequest {
                ContactSearchRef objectID;
                enum { BEGIN = 185, END = 186 };
        };
        struct MsgcontactsearchContactSearchIsValidRequest {
                ContactSearchRef objectID;
                enum { BEGIN = 186, END = 187 };
        };
        struct MsgcontactsearchContactSearchIsValidResponse {
                bool result;
                enum { BEGIN = 187, END = 188 };
        };
        struct MsgcontactsearchContactSearchSubmitRequest {
                ContactSearchRef objectID;
                enum { BEGIN = 188, END = 189 };
        };
        struct MsgcontactsearchContactSearchExtendRequest {
                ContactSearchRef objectID;
                enum { BEGIN = 189, END = 190 };
        };
        struct MsgcontactsearchContactSearchReleaseRequest {
                ContactSearchRef objectID;
                enum { BEGIN = 190, END = 191 };
        };
        struct MsgcontactsearchContactSearchGetResultsRequest {
                ContactSearchRef objectID;
                uint             from;
                uint             count;
                enum { BEGIN = 191, END = 194 };
        };
        struct MsgcontactsearchContactSearchGetResultsResponse {
                ContactRefs contacts;
                enum { BEGIN = 194, END = 196 };
        };
        struct MsgcontactsearchContactSearchOnNewResult {
                ContactSearchRef objectID;
                ContactRef       contact;
                uint             rankValue;
                enum { BEGIN = 196, END = 199 };
        };
        struct MsgcontactsearchContactSearch {
                int contact_search_status;
                Sid::List_uint cachedProps;
                enum { BEGIN = 199, END = 200 };
        };
        struct MsgcontactsearchGetOptimalAgeRangesResponse {
                Sid::List_uint rangeList;
                enum { BEGIN = 200, END = 202 };
        };
        struct MsgcontactsearchCreateContactSearchResponse {
                ContactSearchRef search;
                enum { BEGIN = 202, END = 203 };
        };
        struct MsgcontactsearchCreateBasicContactSearchRequest {
                Sid::String text;
                enum { BEGIN = 203, END = 204 };
        };
        struct MsgcontactsearchCreateBasicContactSearchResponse {
                ContactSearchRef search;
                enum { BEGIN = 204, END = 205 };
        };
        struct MsgcontactsearchCreateIdentitySearchRequest {
                Sid::String identity;
                enum { BEGIN = 205, END = 206 };
        };
        struct MsgcontactsearchCreateIdentitySearchResponse {
                ContactSearchRef search;
                enum { BEGIN = 206, END = 207 };
        };
        struct MsgconversationParticipantCanSetRankToRequest {
                ParticipantRef objectID;
                int            rank;
                enum { BEGIN = 207, END = 209 };
        };
        struct MsgconversationParticipantCanSetRankToResponse {
                bool result;
                enum { BEGIN = 209, END = 210 };
        };
        struct MsgconversationParticipantSetRankToRequest {
                ParticipantRef objectID;
                int            rank;
                enum { BEGIN = 210, END = 212 };
        };
        struct MsgconversationParticipantRingRequest {
                ParticipantRef objectID;
                Sid::String    identityToUse;
                bool           videoCall;
                uint           nrofRedials;
                uint           redialPeriod;
                bool           autoStartVM;
                Sid::String    origin;
                enum { BEGIN = 212, END = 219 };
        };
        struct MsgconversationParticipantRingItRequest {
                ParticipantRef objectID;
                enum { BEGIN = 219, END = 220 };
        };
        struct MsgconversationParticipantSetLiveIdentityToUseRequest {
                ParticipantRef objectID;
                Sid::String    identityToUse;
                enum { BEGIN = 220, END = 222 };
        };
        struct MsgconversationParticipantGetVideoRequest {
                ParticipantRef objectID;
                enum { BEGIN = 222, END = 223 };
        };
        struct MsgconversationParticipantGetVideoResponse {
                VideoRef video;
                enum { BEGIN = 223, END = 224 };
        };
        struct MsgconversationParticipantHangupRequest {
                ParticipantRef objectID;
                enum { BEGIN = 224, END = 225 };
        };
        struct MsgconversationParticipantRetireRequest {
                ParticipantRef objectID;
                enum { BEGIN = 225, END = 226 };
        };
        struct MsgconversationParticipantGetLiveSessionVideosRequest {
                ParticipantRef objectID;
                enum { BEGIN = 226, END = 227 };
        };
        struct MsgconversationParticipantGetLiveSessionVideosResponse {
                VideoRefs videos;
                enum { BEGIN = 227, END = 229 };
        };
        struct MsgconversationParticipantOnIncomingDTMF {
                ParticipantRef objectID;
                int            dtmf;
                enum { BEGIN = 229, END = 231 };
        };
        struct MsgconversationParticipantOnLiveSessionVideosChanged {
                ParticipantRef objectID;
                enum { BEGIN = 231, END = 232 };
        };
        struct MsgconversationParticipant {
                ConversationRef convo_id;
                Sid::String     identity;
                int             rank;
                int             requested_rank;
                int             text_status;
                int             voice_status;
                int             video_status;
                Sid::String     live_price_for_me;
                uint            live_start_timestamp;
                uint            sound_level;
                Sid::String     debuginfo;
                Sid::String     live_identity;
                Sid::String     last_voice_error;
                Sid::String     live_fwd_identities;
                Sid::String     quality_problems;
                int             live_type;
                Sid::String     live_country;
                Sid::String     transferred_by;
                Sid::String     transferred_to;
                Sid::String     adder;
                int             last_leavereason;
                Sid::List_uint cachedProps;
                enum { BEGIN = 232, END = 253 };
        };
        struct MsgconversationConversationSetOptionRequest {
                ConversationRef objectID;
                int             propKey;
                uint            value;
                enum { BEGIN = 253, END = 256 };
        };
        struct MsgconversationConversationSetTopicRequest {
                ConversationRef objectID;
                Sid::String     topic;
                bool            isXML;
                enum { BEGIN = 256, END = 259 };
        };
        struct MsgconversationConversationSetGuidelinesRequest {
                ConversationRef objectID;
                Sid::String     guidelines;
                bool            isXML;
                enum { BEGIN = 259, END = 262 };        };
        struct MsgconversationConversationSetPictureRequest {
                ConversationRef objectID;
                Sid::Binary     jpeg;
                enum { BEGIN = 262, END = 264 };
        };
        struct MsgconversationConversationSpawnConferenceRequest {
                ConversationRef  objectID;
                Sid::List_String identitiesToAdd;
                enum { BEGIN = 264, END = 267 };
        };
        struct MsgconversationConversationSpawnConferenceResponse {
                ConversationRef conference;
                enum { BEGIN = 267, END = 268 };
        };
        struct MsgconversationConversationAddConsumersRequest {
                ConversationRef  objectID;
                Sid::List_String identities;
                enum { BEGIN = 268, END = 271 };
        };
        struct MsgconversationConversationAssimilateRequest {
                ConversationRef objectID;
                ConversationRef otherConversation;
                enum { BEGIN = 271, END = 273 };
        };
        struct MsgconversationConversationAssimilateResponse {
                ConversationRef conversation;
                enum { BEGIN = 273, END = 274 };
        };
        struct MsgconversationConversationJoinLiveSessionRequest {
                ConversationRef objectID;
                Sid::String     accessToken;
                enum { BEGIN = 274, END = 276 };
        };
        struct MsgconversationConversationRingOthersRequest {
                ConversationRef  objectID;
                Sid::List_String identities;
                bool             videoCall;
                Sid::String      origin;
                enum { BEGIN = 276, END = 281 };
        };
        struct MsgconversationConversationMuteMyMicrophoneRequest {
                ConversationRef objectID;
                enum { BEGIN = 281, END = 282 };
        };
        struct MsgconversationConversationUnmuteMyMicrophoneRequest {
                ConversationRef objectID;
                enum { BEGIN = 282, END = 283 };
        };
        struct MsgconversationConversationHoldMyLiveSessionRequest {
                ConversationRef objectID;
                enum { BEGIN = 283, END = 284 };
        };
        struct MsgconversationConversationResumeMyLiveSessionRequest {
                ConversationRef objectID;
                enum { BEGIN = 284, END = 285 };
        };
        struct MsgconversationConversationLeaveLiveSessionRequest {
                ConversationRef objectID;
                bool            postVoiceAutoresponse;
                enum { BEGIN = 285, END = 287 };
        };
        struct MsgconversationConversationStartVoiceMessageRequest {
                ConversationRef objectID;
                enum { BEGIN = 287, END = 288 };
        };
        struct MsgconversationConversationTransferLiveSessionRequest {
                ConversationRef  objectID;
                Sid::List_String identities;
                Sid::String      transferTopic;
                Sid::Binary      context;
                enum { BEGIN = 288, END = 293 };
        };
        struct MsgconversationConversationCanTransferLiveSessionRequest {
                ConversationRef objectID;
                Sid::String     identity;
                enum { BEGIN = 293, END = 295 };
        };
        struct MsgconversationConversationCanTransferLiveSessionResponse {
                bool result;
                enum { BEGIN = 295, END = 296 };
        };
        struct MsgconversationConversationSendDTMFRequest {
                ConversationRef objectID;
                int             dtmf;
                uint            lengthInMS;
                enum { BEGIN = 296, END = 299 };
        };
        struct MsgconversationConversationStopSendDTMFRequest {
                ConversationRef objectID;
                enum { BEGIN = 299, END = 300 };
        };
        struct MsgconversationConversationSetMyTextStatusToRequest {
                ConversationRef objectID;
                int             status;
                enum { BEGIN = 300, END = 302 };
        };
        struct MsgconversationConversationPostTextRequest {
                ConversationRef objectID;
                Sid::String     text;
                bool            isXML;
                enum { BEGIN = 302, END = 305 };
        };
        struct MsgconversationConversationPostTextResponse {
                MessageRef message;
                enum { BEGIN = 305, END = 306 };
        };
        struct MsgconversationConversationPostContactsRequest {
                ConversationRef objectID;
                ContactRefs     contacts;
                enum { BEGIN = 306, END = 309 };
        };
        struct MsgconversationConversationPostFilesRequest {
                ConversationRef    objectID;
                Sid::List_Filename paths;
                Sid::String        body;
                enum { BEGIN = 309, END = 313 };
        };
        struct MsgconversationConversationPostFilesResponse {
                int           error_code;
                Sid::Filename error_file;
                enum { BEGIN = 313, END = 315 };
        };
        struct MsgconversationConversationPostVoiceMessageRequest {
                ConversationRef objectID;
                VoicemailRef    voicemail;
                Sid::String     body;
                enum { BEGIN = 315, END = 318 };
        };
        struct MsgconversationConversationPostSMSRequest {
                ConversationRef objectID;
                SmsRef          sms;
                Sid::String     body;
                enum { BEGIN = 318, END = 321 };
        };
        struct MsgconversationConversationGetJoinBlobRequest {
                ConversationRef objectID;
                enum { BEGIN = 321, END = 322 };
        };
        struct MsgconversationConversationGetJoinBlobResponse {
                Sid::String blob;
                enum { BEGIN = 322, END = 323 };
        };
        struct MsgconversationConversationJoinRequest {
                ConversationRef objectID;
                enum { BEGIN = 323, END = 324 };
        };
        struct MsgconversationConversationEnterPasswordRequest {
                ConversationRef objectID;
                Sid::String     password;
                enum { BEGIN = 324, END = 326 };
        };
        struct MsgconversationConversationSetPasswordRequest {
                ConversationRef objectID;
                Sid::String     password;
                Sid::String     hint;
                enum { BEGIN = 326, END = 329 };
        };
        struct MsgconversationConversationRetireFromRequest {
                ConversationRef objectID;
                enum { BEGIN = 329, END = 330 };
        };
        struct MsgconversationConversationDeleteRequest {
                ConversationRef objectID;
                enum { BEGIN = 330, END = 331 };
        };
        struct MsgconversationConversationRenameToRequest {
                ConversationRef objectID;
                Sid::String     name;
                enum { BEGIN = 331, END = 333 };
        };
        struct MsgconversationConversationSetBookmarkRequest {
                ConversationRef objectID;
                bool            bookmark;
                enum { BEGIN = 333, END = 335 };
        };
        struct MsgconversationConversationSetAlertStringRequest {
                ConversationRef objectID;
                Sid::String     alertString;
                enum { BEGIN = 335, END = 337 };
        };
        struct MsgconversationConversationRemoveFromInboxRequest {
                ConversationRef objectID;
                enum { BEGIN = 337, END = 338 };
        };
        struct MsgconversationConversationAddToInboxRequest {
                ConversationRef objectID;
                uint            timestamp;
                enum { BEGIN = 338, END = 340 };
        };
        struct MsgconversationConversationSetConsumedHorizonRequest {
                ConversationRef objectID;
                uint            timestamp;
                bool            also_unconsume;
                enum { BEGIN = 340, END = 343 };
        };
        struct MsgconversationConversationMarkUnreadRequest {                ConversationRef objectID;
                enum { BEGIN = 343, END = 344 };
        };
        struct MsgconversationConversationIsMemberOfRequest {
                ConversationRef objectID;
                ContactGroupRef group;
                enum { BEGIN = 344, END = 346 };
        };
        struct MsgconversationConversationIsMemberOfResponse {
                bool result;
                enum { BEGIN = 346, END = 347 };
        };
        struct MsgconversationConversationGetParticipantsRequest {
                ConversationRef objectID;
                int             filter;
                enum { BEGIN = 347, END = 349 };
        };
        struct MsgconversationConversationGetParticipantsResponse {
                ParticipantRefs participants;
                enum { BEGIN = 349, END = 351 };
        };
        struct MsgconversationConversationGetLastMessagesRequest {
                ConversationRef objectID;
                uint            requireTimestamp;
                enum { BEGIN = 351, END = 353 };
        };
        struct MsgconversationConversationGetLastMessagesResponse {
                MessageRefs contextMessages;
                MessageRefs unconsumedMessages;
                enum { BEGIN = 353, END = 357 };
        };
        struct MsgconversationConversationFindMessageRequest {
                ConversationRef objectID;
                Sid::String     text;
                uint            fromTimestampUp;
                enum { BEGIN = 357, END = 360 };
        };
        struct MsgconversationConversationFindMessageResponse {
                MessageRef message;
                enum { BEGIN = 360, END = 361 };
        };
        struct MsgconversationConversationAttachVideoToLiveSessionRequest {
                ConversationRef objectID;
                VideoRef        sendVideo;
                enum { BEGIN = 361, END = 363 };
        };
        struct MsgconversationConversationOnParticipantListChange {
                ConversationRef objectID;
                enum { BEGIN = 363, END = 364 };
        };
        struct MsgconversationConversationOnMessage {
                ConversationRef objectID;
                MessageRef      message;
                enum { BEGIN = 364, END = 366 };
        };
        struct MsgconversationConversationOnSpawnConference {
                ConversationRef objectID;
                ConversationRef spawned;
                enum { BEGIN = 366, END = 368 };
        };
        struct MsgconversationConversation {
                int             type;
                Sid::String     creator;
                uint            creation_timestamp;
                int             opt_entry_level_rank;
                bool            opt_disclose_history;
                int             opt_admin_only_activities;
                Sid::String     meta_name;
                Sid::String     meta_topic;
                Sid::String     meta_guidelines;
                Sid::Binary     meta_picture;
                ConversationRef spawned_from_convo_id;
                Sid::String     live_host;
                int             my_status;
                Sid::String     alert_string;
                bool            is_bookmarked;
                bool            opt_joining_enabled;
                Sid::String     displayname;
                Sid::String     given_displayname;
                int             local_livestatus;
                uint            inbox_timestamp;
                bool            unconsumed_messages_voice;
                VoicemailRef    active_vm_id;
                Sid::String     identity;
                MessageRef      inbox_message_id;
                uint            live_start_timestamp;
                uint            unconsumed_suppressed_messages;
                uint            unconsumed_normal_messages;
                uint            unconsumed_elevated_messages;
                uint            consumption_horizon;
                Sid::String     passwordhint;
                uint            last_activity_timestamp;
                bool            live_is_muted;
                Sid::List_uint cachedProps;
                enum { BEGIN = 368, END = 400 };
        };
        struct MsgconversationCreateConferenceResponse {
                ConversationRef conference;
                enum { BEGIN = 400, END = 401 };
        };
        struct MsgconversationGetConversationByIdentityRequest {
                Sid::String convoIdentity;
                bool        matchPSTN;
                enum { BEGIN = 401, END = 403 };
        };
        struct MsgconversationGetConversationByIdentityResponse {
                ConversationRef conversation;
                enum { BEGIN = 403, END = 404 };
        };
        struct MsgconversationGetConversationByParticipantsRequest {
                Sid::List_String participantIdentities;
                bool             createIfNonExisting;
                bool             ignoreBookmarkedOrNamed;
                enum { BEGIN = 404, END = 408 };
        };
        struct MsgconversationGetConversationByParticipantsResponse {
                ConversationRef conversation;
                enum { BEGIN = 408, END = 409 };
        };
        struct MsgconversationGetConversationByBlobRequest {
                Sid::String joinBlob;
                bool        alsoJoin;
                enum { BEGIN = 409, END = 411 };
        };
        struct MsgconversationGetConversationByBlobResponse {
                ConversationRef conversation;
                enum { BEGIN = 411, END = 412 };
        };
        struct MsgconversationGetConversationListRequest {
                int type;
                enum { BEGIN = 412, END = 413 };
        };
        struct MsgconversationGetConversationListResponse {
                ConversationRefs conversations;
                enum { BEGIN = 413, END = 415 };
        };
        struct MsgconversationOnConversationListChange {
                ConversationRef conversation;
                int             type;
                bool            added;
                enum { BEGIN = 415, END = 418 };
        };
        struct MsgconversationMessageCanEditRequest {
                MessageRef objectID;
                enum { BEGIN = 418, END = 419 };
        };
        struct MsgconversationMessageCanEditResponse {
                bool result;
                enum { BEGIN = 419, END = 420 };
        };
        struct MsgconversationMessageGetPermissionsRequest {
                MessageRef objectID;
                enum { BEGIN = 420, END = 421 };
        };
        struct MsgconversationMessageGetPermissionsResponse {
                int result;
                enum { BEGIN = 421, END = 422 };
        };
        struct MsgconversationMessageEditRequest {
                MessageRef  objectID;
                Sid::String newText;
                bool        isXML;
                bool        undo;
                Sid::String legacyPrefix;
                enum { BEGIN = 422, END = 427 };
        };
        struct MsgconversationMessageGetContactsRequest {
                MessageRef objectID;
                enum { BEGIN = 427, END = 428 };
        };
        struct MsgconversationMessageGetContactsResponse {
                ContactRefs contacts;
                enum { BEGIN = 428, END = 430 };
        };
        struct MsgconversationMessageGetTransfersRequest {
                MessageRef objectID;
                enum { BEGIN = 430, END = 431 };
        };
        struct MsgconversationMessageGetTransfersResponse {
                TransferRefs transfers;
                enum { BEGIN = 431, END = 433 };
        };
        struct MsgconversationMessageGetVoiceMessageRequest {
                MessageRef objectID;
                enum { BEGIN = 433, END = 434 };
        };
        struct MsgconversationMessageGetVoiceMessageResponse {
                VoicemailRef voicemail;
                enum { BEGIN = 434, END = 435 };
        };
        struct MsgconversationMessageGetSMSRequest {
                MessageRef objectID;
                enum { BEGIN = 435, END = 436 };
        };
        struct MsgconversationMessageGetSMSResponse {                SmsRef sms;
                enum { BEGIN = 436, END = 437 };
        };
        struct MsgconversationMessageDeleteLocallyRequest {
                MessageRef objectID;
                enum { BEGIN = 437, END = 438 };
        };
        struct MsgconversationMessage {
                Sid::String     convo_guid;
                uint            timestamp;
                Sid::String     author;
                Sid::String     author_displayname;
                Sid::String     identities;
                int             leavereason;
                Sid::String     body_xml;
                Sid::String     edited_by;
                uint            edit_timestamp;
                Sid::String     originally_meant_for;
                Sid::Binary     guid;
                ConversationRef convo_id;
                int             type;
                int             sending_status;
                uint            param_key;
                uint            param_value;
                Sid::String     reason;
                int             consumption_status;
                uint            participant_count;
                Sid::List_uint cachedProps;
                enum { BEGIN = 438, END = 457 };
        };
        struct MsgconversationGetMessageByGuidRequest {
                Sid::Binary guid;
                enum { BEGIN = 457, END = 458 };
        };
        struct MsgconversationGetMessageByGuidResponse {
                MessageRef message;
                enum { BEGIN = 458, END = 459 };
        };
        struct MsgconversationGetMessageListByTypeRequest {
                int  type;
                bool latestPerConvOnly;
                uint fromTimestampInc;
                uint toTimestampExc;
                enum { BEGIN = 459, END = 463 };
        };
        struct MsgconversationGetMessageListByTypeResponse {
                MessageRefs messages;
                enum { BEGIN = 463, END = 465 };
        };
        struct MsgconversationOnMessage {
                MessageRef      message;
                bool            changesInboxTimestamp;
                MessageRef      supersedesHistoryMessage;
                ConversationRef conversation;
                enum { BEGIN = 465, END = 469 };
        };
        struct MsgvideoVideoStartRequest {
                VideoRef objectID;
                enum { BEGIN = 469, END = 470 };
        };
        struct MsgvideoVideoStopRequest {
                VideoRef objectID;
                enum { BEGIN = 470, END = 471 };
        };
        struct MsgvideoVideoSetScreenCaptureRectangleRequest {
                VideoRef objectID;
                int      x0;
                int      y0;
                uint     width;
                uint     height;
                int      monitorNumber;
                uint     windowHandle;
                enum { BEGIN = 471, END = 478 };
        };
        struct MsgvideoVideoSetRemoteRendererIdRequest {
                VideoRef objectID;
                uint     id;
                enum { BEGIN = 478, END = 480 };
        };
        struct MsgvideoVideoSelectVideoSourceRequest {
                VideoRef    objectID;
                int         mediaType;
                Sid::String webcamName;
                Sid::String devicePath;
                bool        updateSetup;
                enum { BEGIN = 480, END = 485 };
        };
        struct MsgvideoVideoGetCurrentVideoDeviceRequest {
                VideoRef objectID;
                enum { BEGIN = 485, END = 486 };
        };
        struct MsgvideoVideoGetCurrentVideoDeviceResponse {
                int         mediatype;
                Sid::String deviceName;
                Sid::String devicePath;
                enum { BEGIN = 486, END = 489 };
        };
        struct MsgvideoVideo {
                int             status;
                Sid::String     error;
                Sid::String     debuginfo;
                Sid::String     dimensions;
                int             media_type;
                ConversationRef convo_id;
                Sid::String     device_path;
                Sid::List_uint cachedProps;
                enum { BEGIN = 489, END = 496 };
        };
        struct MsgvideoGetAvailableVideoDevicesResponse {
                Sid::List_String deviceNames;
                Sid::List_String devicePaths;
                uint             count;
                enum { BEGIN = 496, END = 501 };
        };
        struct MsgvideoHasVideoDeviceCapabilityRequest {
                Sid::String deviceName;
                Sid::String devicePath;
                int         cap;
                enum { BEGIN = 501, END = 504 };
        };
        struct MsgvideoHasVideoDeviceCapabilityResponse {
                bool result;
                enum { BEGIN = 504, END = 505 };
        };
        struct MsgvideoDisplayVideoDeviceTuningDialogRequest {
                Sid::String deviceName;
                Sid::String devicePath;
                enum { BEGIN = 505, END = 507 };
        };
        struct MsgvideoGetPreviewVideoRequest {
                int         type;
                Sid::String deviceName;
                Sid::String devicePath;
                enum { BEGIN = 507, END = 510 };
        };
        struct MsgvideoGetPreviewVideoResponse {
                VideoRef video;
                enum { BEGIN = 510, END = 511 };
        };
        struct MsgvideoCreateLocalVideoRequest {
                int         type;
                Sid::String deviceName;
                Sid::String devicePath;
                enum { BEGIN = 511, END = 514 };
        };
        struct MsgvideoCreateLocalVideoResponse {
                VideoRef video;
                enum { BEGIN = 514, END = 515 };
        };
        struct MsgvideoCreatePreviewVideoRequest {
                int         type;
                Sid::String deviceName;
                Sid::String devicePath;
                enum { BEGIN = 515, END = 518 };
        };
        struct MsgvideoCreatePreviewVideoResponse {
                VideoRef video;
                enum { BEGIN = 518, END = 519 };
        };
        struct MsgvideoVideoCommandRequest {
                Sid::String command;
                enum { BEGIN = 519, END = 520 };
        };
        struct MsgvideoVideoCommandResponse {
                Sid::String response;
                enum { BEGIN = 520, END = 521 };
        };
        struct MsgqualitymonitorStartMonitoringQualityRequest {
                Sid::String withUser;
                bool        excludeNetworkTest;
                enum { BEGIN = 521, END = 523 };
        };
        struct MsgqualitymonitorStopMonitoringQualityRequest {
                Sid::String withUser;
                bool        justStop;
                enum { BEGIN = 523, END = 525 };
        };
        struct MsgqualitymonitorStopMonitoringQualityResponse {
                int result;
                enum { BEGIN = 525, END = 526 };
        };
        struct MsgqualitymonitorOnQualityTestResult {
                int         testType;
                int         testResult;
                Sid::String withUser;
                Sid::String details;
                Sid::String xmlDetails;
                enum { BEGIN = 526, END = 531 };
        };
        struct MsgvmVoicemailStartRecordingRequest {
                VoicemailRef objectID;
                enum { BEGIN = 531, END = 532 };
        };
        struct MsgvmVoicemailStopRecordingRequest {
                VoicemailRef objectID;
                enum { BEGIN = 532, END = 533 };
        };
        struct MsgvmVoicemailStartPlaybackRequest {
                VoicemailRef objectID;
                enum { BEGIN = 533, END = 534 };
        };
        struct MsgvmVoicemailStopPlaybackRequest {
                VoicemailRef objectID;
                enum { BEGIN = 534, END = 535 };
        };
        struct MsgvmVoicemailDeleteRequest {
                VoicemailRef objectID;
                enum { BEGIN = 535, END = 536 };
        };
        struct MsgvmVoicemailCancelRequest {
                VoicemailRef objectID;
                enum { BEGIN = 536, END = 537 };
        };        struct MsgvmVoicemailCheckPermissionRequest {
                VoicemailRef objectID;
                enum { BEGIN = 537, END = 538 };
        };
        struct MsgvmVoicemailCheckPermissionResponse {
                bool result;
                enum { BEGIN = 538, END = 539 };
        };
        struct MsgvmVoicemail {
                int             type;
                Sid::String     partner_handle;
                Sid::String     partner_dispname;
                int             status;
                int             failurereason;
                Sid::String     subject;
                uint            timestamp;
                uint            duration;
                uint            allowed_duration;
                uint            playback_progress;
                ConversationRef convo_id;
                Sid::Binary     chatmsg_guid;
                Sid::List_uint cachedProps;
                enum { BEGIN = 539, END = 551 };
        };
        struct MsgvmGetGreetingRequest {
                Sid::String skypeName;
                enum { BEGIN = 551, END = 552 };
        };
        struct MsgvmGetGreetingResponse {
                VoicemailRef greeting;
                enum { BEGIN = 552, END = 553 };
        };
        struct MsgaudioPlayStartRequest {
                uint        soundid;
                Sid::Binary sound;
                bool        loop;
                bool        useCallOutDevice;
                enum { BEGIN = 553, END = 557 };
        };
        struct MsgaudioPlayStartFromFileRequest {
                uint          soundid;
                Sid::Filename datafile;
                bool          loop;
                bool          useCallOutDevice;
                enum { BEGIN = 557, END = 561 };
        };
        struct MsgaudioPlayStartFromFileResponse {
                int result;
                enum { BEGIN = 561, END = 562 };
        };
        struct MsgaudioPlayStopRequest {
                uint soundid;
                enum { BEGIN = 562, END = 563 };
        };
        struct MsgaudioStartRecordingTestRequest {
                bool recordAndPlaybackData;
                enum { BEGIN = 563, END = 564 };
        };
        struct MsgaudioGetAvailableOutputDevicesResponse {
                Sid::List_String handleList;
                Sid::List_String nameList;
                Sid::List_String productIdList;
                enum { BEGIN = 564, END = 570 };
        };
        struct MsgaudioGetAvailableRecordingDevicesResponse {
                Sid::List_String handleList;
                Sid::List_String nameList;
                Sid::List_String productIdList;
                enum { BEGIN = 570, END = 576 };
        };
        struct MsgaudioSelectSoundDevicesRequest {
                Sid::String callInDevice;
                Sid::String callOutDevice;
                Sid::String waveOutDevice;
                enum { BEGIN = 576, END = 579 };
        };
        struct MsgaudioGetAudioDeviceCapabilitiesRequest {
                Sid::String deviceHandle;
                enum { BEGIN = 579, END = 580 };
        };
        struct MsgaudioGetAudioDeviceCapabilitiesResponse {
                Sid::String interfaceString;
                uint        capabilities;
                enum { BEGIN = 580, END = 582 };
        };
        struct MsgaudioGetNrgLevelsResponse {
                uint micLevel;
                uint speakerLevel;
                enum { BEGIN = 582, END = 584 };
        };
        struct MsgaudioVoiceCommandRequest {
                Sid::String command;
                enum { BEGIN = 584, END = 585 };
        };
        struct MsgaudioVoiceCommandResponse {
                Sid::String response;
                enum { BEGIN = 585, END = 586 };
        };
        struct MsgaudioGetSpeakerVolumeResponse {
                uint volume;
                enum { BEGIN = 586, END = 587 };
        };
        struct MsgaudioSetSpeakerVolumeRequest {
                uint volume;
                enum { BEGIN = 587, END = 588 };
        };
        struct MsgaudioGetMicVolumeResponse {
                uint micVolume;
                enum { BEGIN = 588, END = 589 };
        };
        struct MsgaudioSetMicVolumeRequest {
                uint volume;
                enum { BEGIN = 589, END = 590 };
        };
        struct MsgaudioIsSpeakerMutedResponse {
                bool muted;
                enum { BEGIN = 590, END = 591 };
        };
        struct MsgaudioIsMicrophoneMutedResponse {
                bool muted;
                enum { BEGIN = 591, END = 592 };
        };
        struct MsgaudioMuteSpeakersRequest {
                bool mute;
                enum { BEGIN = 592, END = 593 };
        };
        struct MsgaudioMuteMicrophoneRequest {
                bool mute;
                enum { BEGIN = 593, END = 594 };
        };
        struct MsgbwmSetOperatingMediaRequest {
                int  media;
                uint maxUplinkBps;
                uint maxDownlinkBps;
                enum { BEGIN = 594, END = 597 };
        };
        struct MsgsmsSmsGetTargetStatusRequest {
                SmsRef      objectID;
                Sid::String target;
                enum { BEGIN = 597, END = 599 };
        };
        struct MsgsmsSmsGetTargetStatusResponse {
                int status;
                enum { BEGIN = 599, END = 600 };
        };
        struct MsgsmsSmsGetTargetReplyNumberRequest {
                SmsRef      objectID;
                Sid::String target;
                enum { BEGIN = 600, END = 602 };
        };
        struct MsgsmsSmsGetTargetReplyNumberResponse {
                Sid::String number;
                enum { BEGIN = 602, END = 603 };
        };
        struct MsgsmsSmsGetTargetReplyTypeRequest {
                SmsRef      objectID;
                Sid::String target;
                enum { BEGIN = 603, END = 605 };
        };
        struct MsgsmsSmsGetTargetReplyTypeResponse {
                int type;
                enum { BEGIN = 605, END = 606 };
        };
        struct MsgsmsSmsGetTargetPriceRequest {
                SmsRef      objectID;
                Sid::String target;
                enum { BEGIN = 606, END = 608 };
        };
        struct MsgsmsSmsGetTargetPriceResponse {
                uint price;
                enum { BEGIN = 608, END = 609 };
        };
        struct MsgsmsSmsSetTargetsRequest {
                SmsRef           objectID;
                Sid::List_String numbers;
                enum { BEGIN = 609, END = 612 };
        };
        struct MsgsmsSmsSetTargetsResponse {
                bool success;
                enum { BEGIN = 612, END = 613 };
        };
        struct MsgsmsSmsSetBodyRequest {
                SmsRef      objectID;
                Sid::String text;
                enum { BEGIN = 613, END = 615 };
        };
        struct MsgsmsSmsSetBodyResponse {
                int              result;
                Sid::List_String chunks;
                uint             charsUntilNextChunk;
                enum { BEGIN = 615, END = 619 };
        };
        struct MsgsmsSmsGetBodyChunksRequest {
                SmsRef objectID;
                enum { BEGIN = 619, END = 620 };
        };
        struct MsgsmsSmsGetBodyChunksResponse {
                Sid::List_String textChunks;
                uint             charsUntilNextChunk;
                enum { BEGIN = 620, END = 623 };
        };
        struct MsgsmsSms {
                bool        is_failed_unseen;
                uint        price_precision;
                int         type;
                int         status;
                int         failurereason;
                uint        price;
                Sid::String price_currency;
                Sid::String target_numbers;
                Sid::Binary target_statuses;
                Sid::String body;
                uint        timestamp;
                Sid::String reply_to_number;
                MessageRef  chatmsg_id;
                int         outgoing_reply_type;
                Sid::List_uint cachedProps;
                enum { BEGIN = 623, END = 637 };
        };
        struct MsgsmsRequestConfirmationCodeRequest {                int         type;
                Sid::String number;
                enum { BEGIN = 637, END = 639 };
        };
        struct MsgsmsRequestConfirmationCodeResponse {
                SmsRef sms;
                enum { BEGIN = 639, END = 640 };
        };
        struct MsgsmsSubmitConfirmationCodeRequest {
                Sid::String number;
                Sid::String code;
                enum { BEGIN = 640, END = 642 };
        };
        struct MsgsmsSubmitConfirmationCodeResponse {
                SmsRef sms;
                enum { BEGIN = 642, END = 643 };
        };
        struct MsgsmsCreateOutgoingSmsResponse {
                SmsRef sms;
                enum { BEGIN = 643, END = 644 };
        };
        struct MsgftTransferAcceptRequest {
                TransferRef   objectID;
                Sid::Filename filenameWithPath;
                enum { BEGIN = 644, END = 646 };
        };
        struct MsgftTransferAcceptResponse {
                bool success;
                enum { BEGIN = 646, END = 647 };
        };
        struct MsgftTransferPauseRequest {
                TransferRef objectID;
                enum { BEGIN = 647, END = 648 };
        };
        struct MsgftTransferResumeRequest {
                TransferRef objectID;
                enum { BEGIN = 648, END = 649 };
        };
        struct MsgftTransferCancelRequest {
                TransferRef objectID;
                enum { BEGIN = 649, END = 650 };
        };
        struct MsgftTransfer {
                int             type;
                Sid::String     partner_handle;
                Sid::String     partner_dispname;
                int             status;
                int             failurereason;
                uint            starttime;
                uint            finishtime;
                Sid::String     filepath;
                Sid::String     filename;
                Sid::String     filesize;
                Sid::String     bytestransferred;
                uint            bytespersecond;
                Sid::Binary     chatmsg_guid;
                uint            chatmsg_index;
                ConversationRef convo_id;
                Sid::List_uint cachedProps;
                enum { BEGIN = 650, END = 665 };
        };
        struct MsgaccountAccountGetStatusWithProgressRequest {
                AccountRef objectID;
                enum { BEGIN = 665, END = 666 };
        };
        struct MsgaccountAccountGetStatusWithProgressResponse {
                int  status;
                uint progress;
                enum { BEGIN = 666, END = 668 };
        };
        struct MsgaccountAccountLoginRequest {
                AccountRef objectID;
                int        setAvailabilityTo;
                enum { BEGIN = 668, END = 670 };
        };
        struct MsgaccountAccountLoginWithPasswordRequest {
                AccountRef  objectID;
                Sid::String password;
                bool        savePwd;
                bool        saveDataLocally;
                enum { BEGIN = 670, END = 674 };
        };
        struct MsgaccountAccountRegisterRequest {
                AccountRef  objectID;
                Sid::String password;
                bool        savePwd;
                bool        saveDataLocally;
                Sid::String email;
                bool        allowSpam;
                enum { BEGIN = 674, END = 680 };
        };
        struct MsgaccountAccountLogoutRequest {
                AccountRef objectID;
                bool       clearSavedPwd;
                enum { BEGIN = 680, END = 682 };
        };
        struct MsgaccountAccountChangePasswordRequest {
                AccountRef  objectID;
                Sid::String oldPassword;
                Sid::String newPassword;
                bool        savePwd;
                enum { BEGIN = 682, END = 686 };
        };
        struct MsgaccountAccountSetPasswordSavedRequest {
                AccountRef objectID;
                bool       savePwd;
                enum { BEGIN = 686, END = 688 };
        };
        struct MsgaccountAccountSetServersideIntPropertyRequest {
                AccountRef objectID;
                int        propKey;
                uint       value;
                enum { BEGIN = 688, END = 691 };
        };
        struct MsgaccountAccountSetServersideStrPropertyRequest {
                AccountRef  objectID;
                int         propKey;
                Sid::String value;
                enum { BEGIN = 691, END = 694 };
        };
        struct MsgaccountAccountCancelServerCommitRequest {
                AccountRef objectID;
                enum { BEGIN = 694, END = 695 };
        };
        struct MsgaccountAccountSetIntPropertyRequest {
                AccountRef objectID;
                int        propKey;
                uint       value;
                enum { BEGIN = 695, END = 698 };
        };
        struct MsgaccountAccountSetStrPropertyRequest {
                AccountRef  objectID;
                int         propKey;
                Sid::String value;
                enum { BEGIN = 698, END = 701 };
        };
        struct MsgaccountAccountSetBinPropertyRequest {
                AccountRef  objectID;
                int         propKey;
                Sid::Binary value;
                enum { BEGIN = 701, END = 704 };
        };
        struct MsgaccountAccountSetAvailabilityRequest {
                AccountRef objectID;
                int        availability;
                enum { BEGIN = 704, END = 706 };
        };
        struct MsgaccountAccountSetStandbyRequest {
                AccountRef objectID;
                bool       standby;
                enum { BEGIN = 706, END = 708 };
        };
        struct MsgaccountAccountGetCapabilityStatusRequest {
                AccountRef objectID;
                int        capability;
                enum { BEGIN = 708, END = 710 };
        };
        struct MsgaccountAccountGetCapabilityStatusResponse {
                int  status;
                uint expiryTimestamp;
                enum { BEGIN = 710, END = 712 };
        };
        struct MsgaccountAccountGetSkypenameHashRequest {
                AccountRef objectID;
                enum { BEGIN = 712, END = 713 };
        };
        struct MsgaccountAccountGetSkypenameHashResponse {
                Sid::String skypenameHash;
                enum { BEGIN = 713, END = 714 };
        };
        struct MsgaccountAccountGetVerifiedEmailRequest {
                AccountRef objectID;
                enum { BEGIN = 714, END = 715 };
        };
        struct MsgaccountAccountGetVerifiedEmailResponse {
                Sid::String email;
                enum { BEGIN = 715, END = 716 };
        };
        struct MsgaccountAccountGetVerifiedCompanyRequest {
                AccountRef objectID;
                enum { BEGIN = 716, END = 717 };
        };
        struct MsgaccountAccountGetVerifiedCompanyResponse {
                Sid::String company;
                enum { BEGIN = 717, END = 718 };
        };
        struct MsgaccountAccountDeleteRequest {
                AccountRef objectID;
                enum { BEGIN = 718, END = 719 };
        };
        struct MsgaccountAccount {
                Sid::String skypename;
                Sid::String fullname;
                uint        birthday;
                uint        gender;
                Sid::String languages;
                Sid::String country;
                Sid::String province;
                Sid::String city;
                Sid::String phone_home;
                Sid::String phone_office;
                Sid::String phone_mobile;
                Sid::String emails;
                Sid::String homepage;
                Sid::String about;
                uint        profile_timestamp;
                Sid::String mood_text;
                uint        timezone;
                uint        nrof_authed_buddies;
                int         availability;
                Sid::Binary avatar_image;
                int         status;
                int         pwdchangestatus;
                Sid::String suggested_skypename;                int         logoutreason;
                Sid::String skypeout_balance_currency;
                uint        skypeout_balance;
                Sid::String skypein_numbers;
                Sid::String offline_callforward;
                int         commitstatus;
                int         cblsyncstatus;
                int         chat_policy;
                int         skype_call_policy;
                int         pstn_call_policy;
                int         avatar_policy;
                int         buddycount_policy;
                int         timezone_policy;
                int         webpresence_policy;
                int         phonenumbers_policy;
                int         voicemail_policy;
                uint        avatar_timestamp;
                uint        mood_timestamp;
                Sid::String rich_mood_text;
                Sid::String partner_optedout;
                Sid::String service_provider_info;
                uint        registration_timestamp;
                uint        nr_of_other_instances;
                uint        skypeout_precision;
                Sid::List_uint cachedProps;
                enum { BEGIN = 719, END = 766 };
        };
        struct MsgaccountGetAccountRequest {
                Sid::String identity;
                enum { BEGIN = 766, END = 767 };
        };
        struct MsgaccountGetAccountResponse {
                AccountRef account;
                enum { BEGIN = 767, END = 768 };
        };
        struct MsgaccountGetExistingAccountsResponse {
                Sid::List_String accountNameList;
                enum { BEGIN = 768, END = 770 };
        };
        struct MsgaccountGetDefaultAccountNameResponse {
                Sid::String account;
                enum { BEGIN = 770, END = 771 };
        };
        struct MsgaccountGetSuggestedSkypenameRequest {
                Sid::String fullname;
                enum { BEGIN = 771, END = 772 };
        };
        struct MsgaccountGetSuggestedSkypenameResponse {
                Sid::String suggestedName;
                enum { BEGIN = 772, END = 773 };
        };
        struct MsgaccountValidateAvatarRequest {
                Sid::Binary value;
                enum { BEGIN = 773, END = 774 };
        };
        struct MsgaccountValidateAvatarResponse {
                int result;
                int freeBytesLeft;
                enum { BEGIN = 774, END = 776 };
        };
        struct MsgaccountValidateProfileStringRequest {
                int         propKey;
                Sid::String strValue;
                bool        forRegistration;
                enum { BEGIN = 776, END = 779 };
        };
        struct MsgaccountValidateProfileStringResponse {
                int result;
                int freeBytesLeft;
                enum { BEGIN = 779, END = 781 };
        };
        struct MsgaccountValidatePasswordRequest {
                Sid::String username;
                Sid::String password;
                enum { BEGIN = 781, END = 783 };
        };
        struct MsgaccountValidatePasswordResponse {
                int result;
                enum { BEGIN = 783, END = 784 };
        };
        struct MsgconnectionOnProxyAuthFailure {
                int type;
                enum { BEGIN = 784, END = 785 };
        };
        struct MsgconnectionGetUsedPortResponse {
                uint port;
                enum { BEGIN = 785, END = 786 };
        };
        struct MsgsetupGetStrRequest {
                Sid::String key;
                enum { BEGIN = 786, END = 787 };
        };
        struct MsgsetupGetStrResponse {
                Sid::String value;
                enum { BEGIN = 787, END = 788 };
        };
        struct MsgsetupGetIntRequest {
                Sid::String key;
                enum { BEGIN = 788, END = 789 };
        };
        struct MsgsetupGetIntResponse {
                int value;
                enum { BEGIN = 789, END = 790 };
        };
        struct MsgsetupGetBinRequest {
                Sid::String key;
                enum { BEGIN = 790, END = 791 };
        };
        struct MsgsetupGetBinResponse {
                Sid::Binary value;
                enum { BEGIN = 791, END = 792 };
        };
        struct MsgsetupSetStrRequest {
                Sid::String key;
                Sid::String value;
                enum { BEGIN = 792, END = 794 };
        };
        struct MsgsetupSetIntRequest {
                Sid::String key;
                int         value;
                enum { BEGIN = 794, END = 796 };
        };
        struct MsgsetupSetBinRequest {
                Sid::String key;
                Sid::Binary value;
                enum { BEGIN = 796, END = 798 };
        };
        struct MsgsetupIsDefinedRequest {
                Sid::String key;
                enum { BEGIN = 798, END = 799 };
        };
        struct MsgsetupIsDefinedResponse {
                bool value;
                enum { BEGIN = 799, END = 800 };
        };
        struct MsgsetupDeleteRequest {
                Sid::String key;
                enum { BEGIN = 800, END = 801 };
        };
        struct MsgsetupGetSubKeysRequest {
                Sid::String key;
                enum { BEGIN = 801, END = 802 };
        };
        struct MsgsetupGetSubKeysResponse {
                Sid::List_String value;
                enum { BEGIN = 802, END = 804 };
        };
        struct MsgisoGetISOLanguageInfoResponse {
                Sid::List_String languageCodeList;
                Sid::List_String languageNameList;
                enum { BEGIN = 804, END = 808 };
        };
        struct MsgisoGetISOCountryInfoResponse {
                Sid::List_String countryCodeList;
                Sid::List_String countryNameList;
                Sid::List_uint   countryPrefixList;
                Sid::List_String countryDialExampleList;
                enum { BEGIN = 808, END = 816 };
        };
        struct MsgisoGetSupportedUILanguageListResponse {
                Sid::List_String uiLanguageCodeList;
                enum { BEGIN = 816, END = 818 };
        };
        struct MsgisoGetISOCountryCodebyPhoneNoRequest {
                Sid::String number;
                enum { BEGIN = 818, END = 819 };
        };
        struct MsgisoGetISOCountryCodebyPhoneNoResponse {
                Sid::String countryCode;
                enum { BEGIN = 819, END = 820 };
        };
        struct Msgapp2appApp2AppCreateRequest {
                Sid::String appname;
                enum { BEGIN = 820, END = 821 };
        };
        struct Msgapp2appApp2AppCreateResponse {
                bool result;
                enum { BEGIN = 821, END = 822 };
        };
        struct Msgapp2appApp2AppDeleteRequest {
                Sid::String appname;
                enum { BEGIN = 822, END = 823 };
        };
        struct Msgapp2appApp2AppDeleteResponse {
                bool result;
                enum { BEGIN = 823, END = 824 };
        };
        struct Msgapp2appApp2AppConnectRequest {
                Sid::String appname;
                Sid::String skypename;
                enum { BEGIN = 824, END = 826 };
        };
        struct Msgapp2appApp2AppConnectResponse {
                bool result;
                enum { BEGIN = 826, END = 827 };
        };
        struct Msgapp2appApp2AppDisconnectRequest {
                Sid::String appname;
                Sid::String stream;
                enum { BEGIN = 827, END = 829 };
        };
        struct Msgapp2appApp2AppDisconnectResponse {
                bool result;
                enum { BEGIN = 829, END = 830 };
        };
        struct Msgapp2appApp2AppWriteRequest {
                Sid::String appname;
                Sid::String stream;
                Sid::Binary data;
                enum { BEGIN = 830, END = 833 };
        };
        struct Msgapp2appApp2AppWriteResponse {
                bool result;
                enum { BEGIN = 833, END = 834 };
        };
        struct Msgapp2appApp2AppDatagramRequest {
                Sid::String appname;
                Sid::String stream;
                Sid::Binary data;
                enum { BEGIN = 834, END = 837 };        };
        struct Msgapp2appApp2AppDatagramResponse {
                bool result;
                enum { BEGIN = 837, END = 838 };
        };
        struct Msgapp2appApp2AppReadRequest {
                Sid::String appname;
                Sid::String stream;
                enum { BEGIN = 838, END = 840 };
        };
        struct Msgapp2appApp2AppReadResponse {
                bool        result;
                Sid::Binary data;
                enum { BEGIN = 840, END = 842 };
        };
        struct Msgapp2appApp2AppGetConnectableUsersRequest {
                Sid::String appname;
                enum { BEGIN = 842, END = 843 };
        };
        struct Msgapp2appApp2AppGetConnectableUsersResponse {
                bool             result;
                Sid::List_String users;
                enum { BEGIN = 843, END = 846 };
        };
        struct Msgapp2appApp2AppGetStreamsListRequest {
                Sid::String appname;
                int         listType;
                enum { BEGIN = 846, END = 848 };
        };
        struct Msgapp2appApp2AppGetStreamsListResponse {
                bool             result;
                Sid::List_String streams;
                Sid::List_uint   receivedSizes;
                enum { BEGIN = 848, END = 853 };
        };
        struct Msgapp2appOnApp2AppDatagram {
                Sid::String appname;
                Sid::String stream;
                Sid::Binary data;
                enum { BEGIN = 853, END = 856 };
        };
        struct Msgapp2appOnApp2AppStreamListChange {
                Sid::String      appname;
                int              listType;
                Sid::List_String streams;
                Sid::List_uint   receivedSizes;
                enum { BEGIN = 856, END = 862 };
        };
#ifdef SE_USE_NAMESPACE
} // Skype
#endif

#endif // __SIDG_MSGS_SKYPEHPP_INCLUDED____
