#include "SidPlatform.hpp"
#include "SidField.hpp"
#include "skype-embedded_2.h"
#include "sidg_msgs_Skype.hpp"

#ifdef SE_USE_NAMESPACE
namespace Skype {
#endif
        static const Sid::String d1="";
        static bool d1field_equals_default(const void* p) { return d1 == *reinterpret_cast<const Sid::String*>(p);}
        static void d1set_field_to_default(void* p) { *reinterpret_cast<Sid::String*>(p) = d1;}
        static const uint d2=0;
        static bool d2field_equals_default(const void* p) { return d2 == *reinterpret_cast<const uint*>(p);}
        static void d2set_field_to_default(void* p) { *reinterpret_cast<uint*>(p) = d2;}
        static const bool d3=false;
        static bool d3field_equals_default(const void* p) { return d3 == *reinterpret_cast<const bool*>(p);}
        static void d3set_field_to_default(void* p) { *reinterpret_cast<bool*>(p) = d3;}
        static const ContactGroupRef d4=0;
        static bool d4field_equals_default(const void* p) { return d4 == *reinterpret_cast<const ContactGroupRef*>(p);}
        static void d4set_field_to_default(void* p) { *reinterpret_cast<ContactGroupRef*>(p) = d4;}
        static bool d5field_equals_default(const void* p) { return reinterpret_cast<const ConversationRefs*>(p)->size()==0; }
        static void d5set_field_to_default(void* p) { reinterpret_cast<ConversationRefs*>(p)->clear(); }
        static void d5append(void* list, uint size, void*& elem) {
                ConversationRefs* l=reinterpret_cast<ConversationRefs*>(list);
                l->append(ConversationRef());
                elem = (void*) &(((const ConversationRefs*)l)->operator[](size));
        };
        static void* d5iterate(void* list, uint count) {
                ConversationRefs* l=reinterpret_cast<ConversationRefs*>(list);
                if (count >= (uint)l->size()) return 0;
                return (void*) &(((const ConversationRefs*)l)->operator[](count));
        };
        static const ConversationRef d6=0;
        static bool d6field_equals_default(const void* p) { return d6 == *reinterpret_cast<const ConversationRef*>(p);}
        static void d6set_field_to_default(void* p) { *reinterpret_cast<ConversationRef*>(p) = d6;}
        static bool d7field_equals_default(const void* p) { return reinterpret_cast<const ContactRefs*>(p)->size()==0; }
        static void d7set_field_to_default(void* p) { reinterpret_cast<ContactRefs*>(p)->clear(); }
        static void d7append(void* list, uint size, void*& elem) {
                ContactRefs* l=reinterpret_cast<ContactRefs*>(list);
                l->append(ContactRef());
                elem = (void*) &(((const ContactRefs*)l)->operator[](size));
        };
        static void* d7iterate(void* list, uint count) {
                ContactRefs* l=reinterpret_cast<ContactRefs*>(list);
                if (count >= (uint)l->size()) return 0;
                return (void*) &(((const ContactRefs*)l)->operator[](count));
        };
        static const ContactRef d8=0;
        static bool d8field_equals_default(const void* p) { return d8 == *reinterpret_cast<const ContactRef*>(p);}
        static void d8set_field_to_default(void* p) { *reinterpret_cast<ContactRef*>(p) = d8;}
        static const int d9=0;
        static bool d9field_equals_default(const void* p) { return d9 == *reinterpret_cast<const int*>(p);}
        static void d9set_field_to_default(void* p) { *reinterpret_cast<int*>(p) = d9;}
        static bool d10field_equals_default(const void* p) { return reinterpret_cast<const ContactGroupRefs*>(p)->size()==0; }
        static void d10set_field_to_default(void* p) { reinterpret_cast<ContactGroupRefs*>(p)->clear(); }
        static void d10append(void* list, uint size, void*& elem) {
                ContactGroupRefs* l=reinterpret_cast<ContactGroupRefs*>(list);
                l->append(ContactGroupRef());
                elem = (void*) &(((const ContactGroupRefs*)l)->operator[](size));
        };
        static void* d10iterate(void* list, uint count) {
                ContactGroupRefs* l=reinterpret_cast<ContactGroupRefs*>(list);
                if (count >= (uint)l->size()) return 0;
                return (void*) &(((const ContactGroupRefs*)l)->operator[](count));
        };
        static const Sid::Binary d11=Sid::Binary();
        static bool d11field_equals_default(const void* p) { return d11 == *reinterpret_cast<const Sid::Binary*>(p);}
        static void d11set_field_to_default(void* p) { *reinterpret_cast<Sid::Binary*>(p) = d11;}
        static const bool d12=0;
        static bool d12field_equals_default(const void* p) { return d12 == *reinterpret_cast<const bool*>(p);}
        static void d12set_field_to_default(void* p) { *reinterpret_cast<bool*>(p) = d12;}
        static const bool d13=1;
        static bool d13field_equals_default(const void* p) { return d13 == *reinterpret_cast<const bool*>(p);}
        static void d13set_field_to_default(void* p) { *reinterpret_cast<bool*>(p) = d13;}
        static const ContactSearchRef d14=0;
        static bool d14field_equals_default(const void* p) { return d14 == *reinterpret_cast<const ContactSearchRef*>(p);}
        static void d14set_field_to_default(void* p) { *reinterpret_cast<ContactSearchRef*>(p) = d14;}
        static const uint d15=MAX_UINT;
        static bool d15field_equals_default(const void* p) { return d15 == *reinterpret_cast<const uint*>(p);}
        static void d15set_field_to_default(void* p) { *reinterpret_cast<uint*>(p) = d15;}
        static bool d16field_equals_default(const void* p) { return reinterpret_cast<const Sid::List_uint*>(p)->size()==0; }
        static void d16set_field_to_default(void* p) { reinterpret_cast<Sid::List_uint*>(p)->clear(); }
        static void d16append(void* list, uint size, void*& elem) {
                Sid::List_uint* l=reinterpret_cast<Sid::List_uint*>(list);
                l->append(uint());
                elem = (void*) &(((const Sid::List_uint*)l)->operator[](size));
        };
        static void* d16iterate(void* list, uint count) {
                Sid::List_uint* l=reinterpret_cast<Sid::List_uint*>(list);
                if (count >= (uint)l->size()) return 0;
                return (void*) &(((const Sid::List_uint*)l)->operator[](count));
        };
        static const ParticipantRef d17=0;
        static bool d17field_equals_default(const void* p) { return d17 == *reinterpret_cast<const ParticipantRef*>(p);}
        static void d17set_field_to_default(void* p) { *reinterpret_cast<ParticipantRef*>(p) = d17;}
        static const VideoRef d18=0;
        static bool d18field_equals_default(const void* p) { return d18 == *reinterpret_cast<const VideoRef*>(p);}
        static void d18set_field_to_default(void* p) { *reinterpret_cast<VideoRef*>(p) = d18;}
        static bool d19field_equals_default(const void* p) { return reinterpret_cast<const VideoRefs*>(p)->size()==0; }
        static void d19set_field_to_default(void* p) { reinterpret_cast<VideoRefs*>(p)->clear(); }
        static void d19append(void* list, uint size, void*& elem) {
                VideoRefs* l=reinterpret_cast<VideoRefs*>(list);
                l->append(VideoRef());
                elem = (void*) &(((const VideoRefs*)l)->operator[](size));
        };
        static void* d19iterate(void* list, uint count) {
                VideoRefs* l=reinterpret_cast<VideoRefs*>(list);
                if (count >= (uint)l->size()) return 0;
                return (void*) &(((const VideoRefs*)l)->operator[](count));
        };
        static bool d20field_equals_default(const void* p) { return reinterpret_cast<const Sid::List_String*>(p)->size()==0; }
        static void d20set_field_to_default(void* p) { reinterpret_cast<Sid::List_String*>(p)->clear(); }
        static void d20append(void* list, uint size, void*& elem) {
                Sid::List_String* l=reinterpret_cast<Sid::List_String*>(list);
                l->append(Sid::String());
                elem = (void*) &(((const Sid::List_String*)l)->operator[](size));
        };
        static void* d20iterate(void* list, uint count) {
                Sid::List_String* l=reinterpret_cast<Sid::List_String*>(list);
                if (count >= (uint)l->size()) return 0;
                return (void*) &(((const Sid::List_String*)l)->operator[](count));
        };
        static const uint d21=260;
        static bool d21field_equals_default(const void* p) { return d21 == *reinterpret_cast<const uint*>(p);}
        static void d21set_field_to_default(void* p) { *reinterpret_cast<uint*>(p) = d21;}
        static const MessageRef d22=0;
        static bool d22field_equals_default(const void* p) { return d22 == *reinterpret_cast<const MessageRef*>(p);}
        static void d22set_field_to_default(void* p) { *reinterpret_cast<MessageRef*>(p) = d22;}
        static bool d23field_equals_default(const void* p) { return reinterpret_cast<const Sid::List_Filename*>(p)->size()==0; }
        static void d23set_field_to_default(void* p) { reinterpret_cast<Sid::List_Filename*>(p)->clear(); }
        static void d23append(void* list, uint size, void*& elem) {
                Sid::List_Filename* l=reinterpret_cast<Sid::List_Filename*>(list);
                l->append(Sid::Filename());
                elem = (void*) &(((const Sid::List_Filename*)l)->operator[](size));
        };
        static void* d23iterate(void* list, uint count) {
                Sid::List_Filename* l=reinterpret_cast<Sid::List_Filename*>(list);
                if (count >= (uint)l->size()) return 0;
                return (void*) &(((const Sid::List_Filename*)l)->operator[](count));
        };
        static const Sid::Filename d24="";
        static bool d24field_equals_default(const void* p) { return d24 == *reinterpret_cast<const Sid::Filename*>(p);}
        static void d24set_field_to_default(void* p) { *reinterpret_cast<Sid::Filename*>(p) = d24;}
        static const VoicemailRef d25=0;
        static bool d25field_equals_default(const void* p) { return d25 == *reinterpret_cast<const VoicemailRef*>(p);}
        static void d25set_field_to_default(void* p) { *reinterpret_cast<VoicemailRef*>(p) = d25;}
        static const SmsRef d26=0;
        static bool d26field_equals_default(const void* p) { return d26 == *reinterpret_cast<const SmsRef*>(p);}
        static void d26set_field_to_default(void* p) { *reinterpret_cast<SmsRef*>(p) = d26;}
        static bool d27field_equals_default(const void* p) { return reinterpret_cast<const ParticipantRefs*>(p)->size()==0; }
        static void d27set_field_to_default(void* p) { reinterpret_cast<ParticipantRefs*>(p)->clear(); }
        static void d27append(void* list, uint size, void*& elem) {
                ParticipantRefs* l=reinterpret_cast<ParticipantRefs*>(list);
                l->append(ParticipantRef());
                elem = (void*) &(((const ParticipantRefs*)l)->operator[](size));
        };
        static void* d27iterate(void* list, uint count) {
                ParticipantRefs* l=reinterpret_cast<ParticipantRefs*>(list);
                if (count >= (uint)l->size()) return 0;
                return (void*) &(((const ParticipantRefs*)l)->operator[](count));
        };
        static bool d28field_equals_default(const void* p) { return reinterpret_cast<const MessageRefs*>(p)->size()==0; }
        static void d28set_field_to_default(void* p) { reinterpret_cast<MessageRefs*>(p)->clear(); }
        static void d28append(void* list, uint size, void*& elem) {
                MessageRefs* l=reinterpret_cast<MessageRefs*>(list);
                l->append(MessageRef());
                elem = (void*) &(((const MessageRefs*)l)->operator[](size));
        };
        static void* d28iterate(void* list, uint count) {
                MessageRefs* l=reinterpret_cast<MessageRefs*>(list);
                if (count >= (uint)l->size()) return 0;
                return (void*) &(((const MessageRefs*)l)->operator[](count));
        };
        static bool d29field_equals_default(const void* p) { return reinterpret_cast<const TransferRefs*>(p)->size()==0; }
        static void d29set_field_to_default(void* p) { reinterpret_cast<TransferRefs*>(p)->clear(); }
        static void d29append(void* list, uint size, void*& elem) {
                TransferRefs* l=reinterpret_cast<TransferRefs*>(list);
                l->append(TransferRef());
                elem = (void*) &(((const TransferRefs*)l)->operator[](size));
        };
        static void* d29iterate(void* list, uint count) {
                TransferRefs* l=reinterpret_cast<TransferRefs*>(list);
                if (count >= (uint)l->size()) return 0;
                return (void*) &(((const TransferRefs*)l)->operator[](count));
        };
        static const TransferRef d30=0;
        static bool d30field_equals_default(const void* p) { return d30 == *reinterpret_cast<const TransferRef*>(p);}
        static void d30set_field_to_default(void* p) { *reinterpret_cast<TransferRef*>(p) = d30;}
        static const AccountRef d31=0;
        static bool d31field_equals_default(const void* p) { return d31 == *reinterpret_cast<const AccountRef*>(p);}
        static void d31set_field_to_default(void* p) { *reinterpret_cast<AccountRef*>(p) = d31;}
        static char* base_address=0;
        Sid::Field SkypeFields[] = {
                /*** MsgconfigGetVersionStringResponse */
                /*0*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgconfigGetVersionStringResponse::END,0} } },
                /*** MsgconfigGetUnixTimestampResponse */
                /*1*/ { { {&d2field_equals_default,&d2set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_UINT, MsgconfigGetUnixTimestampResponse::END,0} } },
                /*** MsgconfigStartResponse */
                /*2*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgconfigStartResponse::END,0} } },
                /*** MsgcontactContactGroupGiveDisplayNameRequest */
                /*3*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupGiveDisplayNameRequest::END,10} } },
                /*4*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroupGiveDisplayNameRequest*>(base_address)->name,0} } },
                /*** MsgcontactContactGroupDeleteRequest */
                /*5*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupDeleteRequest::END,10} } },
                /*** MsgcontactContactGroupDeleteResponse */
                /*6*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactContactGroupDeleteResponse::END,0} } },
                /*** MsgcontactContactGroupGetConversationsRequest */
                /*7*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupGetConversationsRequest::END,10} } },
                /*** MsgcontactContactGroupGetConversationsResponse */
                /*8*/ { { {&d5field_equals_default,&d5set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupGetConversationsResponse::END,18} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d5append),reinterpret_cast<void(*)(void*)>(&d5iterate),0,0,0,0,0}}},
                /*** MsgcontactContactGroupCanAddConversationRequest */
                /*10*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupCanAddConversationRequest::END,10} } },
                /*11*/ { { {&d6field_equals_default,&d6set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroupCanAddConversationRequest*>(base_address)->conversation,18} } },
                /*** MsgcontactContactGroupCanAddConversationResponse */
                /*12*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactContactGroupCanAddConversationResponse::END,0} } },
                /*** MsgcontactContactGroupAddConversationRequest */
                /*13*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupAddConversationRequest::END,10} } },
                /*14*/ { { {&d6field_equals_default,&d6set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroupAddConversationRequest*>(base_address)->conversation,18} } },
                /*** MsgcontactContactGroupCanRemoveConversationRequest */
                /*15*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupCanRemoveConversationRequest::END,10} } },
                /*** MsgcontactContactGroupCanRemoveConversationResponse */
                /*16*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactContactGroupCanRemoveConversationResponse::END,0} } },
                /*** MsgcontactContactGroupRemoveConversationRequest */
                /*17*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupRemoveConversationRequest::END,10} } },
                /*18*/ { { {&d6field_equals_default,&d6set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroupRemoveConversationRequest*>(base_address)->conversation,18} } },
                /*** MsgcontactContactGroupOnChangeConversation */
                /*19*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupOnChangeConversation::END,10} } },
                /*20*/ { { {&d6field_equals_default,&d6set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroupOnChangeConversation*>(base_address)->conversation,18} } },
                /*** MsgcontactContactGroupGetContactsRequest */
                /*21*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupGetContactsRequest::END,10} } },
                /*** MsgcontactContactGroupGetContactsResponse */
                /*22*/ { { {&d7field_equals_default,&d7set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupGetContactsResponse::END,2} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d7append),reinterpret_cast<void(*)(void*)>(&d7iterate),0,0,0,0,0}}},
                /*** MsgcontactContactGroupCanAddContactRequest */
                /*24*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupCanAddContactRequest::END,10} } },
                /*25*/ { { {&d8field_equals_default,&d8set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroupCanAddContactRequest*>(base_address)->contact,2} } },
                /*** MsgcontactContactGroupCanAddContactResponse */
                /*26*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactContactGroupCanAddContactResponse::END,0} } },
                /*** MsgcontactContactGroupAddContactRequest */
                /*27*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupAddContactRequest::END,10} } },
                /*28*/ { { {&d8field_equals_default,&d8set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroupAddContactRequest*>(base_address)->contact,2} } },
                /*** MsgcontactContactGroupCanRemoveContactRequest */
                /*29*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupCanRemoveContactRequest::END,10} } },
                /*** MsgcontactContactGroupCanRemoveContactResponse */
                /*30*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactContactGroupCanRemoveContactResponse::END,0} } },
                /*** MsgcontactContactGroupRemoveContactRequest */
                /*31*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupRemoveContactRequest::END,10} } },
                /*32*/ { { {&d8field_equals_default,&d8set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroupRemoveContactRequest*>(base_address)->contact,2} } },
                /*** MsgcontactContactGroupOnChange */
                /*33*/ { { {&d4field_equals_default,&d4set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGroupOnChange::END,10} } },
                /*34*/ { { {&d8field_equals_default,&d8set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroupOnChange*>(base_address)->contact,2} } },                /*** MsgcontactContactGroup */
                /*35*/ { { {&d1field_equals_default,&d1set_field_to_default,151,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactContactGroup::END,0} } },
                /*36*/ { { {&d2field_equals_default,&d2set_field_to_default,152,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroup*>(base_address)->nrofcontacts,0} } },
                /*37*/ { { {&d2field_equals_default,&d2set_field_to_default,153,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroup*>(base_address)->nrofcontacts_online,0} } },
                /*38*/ { { {&d2field_equals_default,&d2set_field_to_default,154,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroup*>(base_address)->custom_group_id,0} } },
                /*39*/ { { {&d9field_equals_default,&d9set_field_to_default,155,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGroup*>(base_address)->type,0} } },
                /*** MsgcontactGetHardwiredContactGroupRequest */
                /*40*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgcontactGetHardwiredContactGroupRequest::END,0} } },
                /*** MsgcontactGetHardwiredContactGroupResponse */
                /*41*/ { { {&d4field_equals_default,&d4set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactGetHardwiredContactGroupResponse::END,10} } },
                /*** MsgcontactGetCustomContactGroupsResponse */
                /*42*/ { { {&d10field_equals_default,&d10set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, MsgcontactGetCustomContactGroupsResponse::END,10} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d10append),reinterpret_cast<void(*)(void*)>(&d10iterate),0,0,0,0,0}}},
                /*** MsgcontactCreateCustomContactGroupResponse */
                /*44*/ { { {&d4field_equals_default,&d4set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactCreateCustomContactGroupResponse::END,10} } },
                /*** MsgcontactOnNewCustomContactGroup */
                /*45*/ { { {&d4field_equals_default,&d4set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactOnNewCustomContactGroup::END,10} } },
                /*** MsgcontactContactGetIdentityRequest */
                /*46*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGetIdentityRequest::END,2} } },
                /*** MsgcontactContactGetIdentityResponse */
                /*47*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactContactGetIdentityResponse::END,0} } },
                /*** MsgcontactContactGetAvatarRequest */
                /*48*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGetAvatarRequest::END,2} } },
                /*** MsgcontactContactGetAvatarResponse */
                /*49*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactContactGetAvatarResponse::END,0} } },
                /*50*/ { { {&d11field_equals_default,&d11set_field_to_default,2,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGetAvatarResponse*>(base_address)->avatar,0} } },
                /*** MsgcontactContactGetVerifiedEmailRequest */
                /*51*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGetVerifiedEmailRequest::END,2} } },
                /*** MsgcontactContactGetVerifiedEmailResponse */
                /*52*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactContactGetVerifiedEmailResponse::END,0} } },
                /*** MsgcontactContactGetVerifiedCompanyRequest */
                /*53*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGetVerifiedCompanyRequest::END,2} } },
                /*** MsgcontactContactGetVerifiedCompanyResponse */
                /*54*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactContactGetVerifiedCompanyResponse::END,0} } },
                /*** MsgcontactContactIsMemberOfRequest */
                /*55*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactIsMemberOfRequest::END,2} } },
                /*56*/ { { {&d4field_equals_default,&d4set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactIsMemberOfRequest*>(base_address)->group,10} } },
                /*** MsgcontactContactIsMemberOfResponse */
                /*57*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactContactIsMemberOfResponse::END,0} } },
                /*** MsgcontactContactIsMemberOfHardwiredGroupRequest */
                /*58*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactIsMemberOfHardwiredGroupRequest::END,2} } },
                /*59*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactIsMemberOfHardwiredGroupRequest*>(base_address)->groupType,0} } },
                /*** MsgcontactContactIsMemberOfHardwiredGroupResponse */
                /*60*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactContactIsMemberOfHardwiredGroupResponse::END,0} } },
                /*** MsgcontactContactSetBlockedRequest */
                /*61*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactSetBlockedRequest::END,2} } },
                /*62*/ { { {&d3field_equals_default,&d3set_field_to_default,1,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactSetBlockedRequest*>(base_address)->blocked,0} } },
                /*63*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactSetBlockedRequest*>(base_address)->abuse,0} } },
                /*** MsgcontactContactIgnoreAuthRequestRequest */
                /*64*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactIgnoreAuthRequestRequest::END,2} } },
                /*** MsgcontactContactGiveDisplayNameRequest */
                /*65*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGiveDisplayNameRequest::END,2} } },
                /*66*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGiveDisplayNameRequest*>(base_address)->name,0} } },
                /*** MsgcontactContactSetBuddyStatusRequest */
                /*67*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactSetBuddyStatusRequest::END,2} } },
                /*68*/ { { {&d3field_equals_default,&d3set_field_to_default,1,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactSetBuddyStatusRequest*>(base_address)->isMyBuddy,0} } },
                /*69*/ { { {&d13field_equals_default,&d13set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactSetBuddyStatusRequest*>(base_address)->syncAuth,0} } },
                /*** MsgcontactContactSendAuthRequestRequest */
                /*70*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactSendAuthRequestRequest::END,2} } },                /*71*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactSendAuthRequestRequest*>(base_address)->message,0} } },
                /*72*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactSendAuthRequestRequest*>(base_address)->extras_bitmask,0} } },
                /*** MsgcontactContactHasAuthorizedMeRequest */
                /*73*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactHasAuthorizedMeRequest::END,2} } },
                /*** MsgcontactContactHasAuthorizedMeResponse */
                /*74*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactContactHasAuthorizedMeResponse::END,0} } },
                /*** MsgcontactContactSetPhoneNumberRequest */
                /*75*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactSetPhoneNumberRequest::END,2} } },
                /*76*/ { { {&d2field_equals_default,&d2set_field_to_default,1,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactSetPhoneNumberRequest*>(base_address)->num,0} } },
                /*77*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactSetPhoneNumberRequest*>(base_address)->label,0} } },
                /*78*/ { { {&d1field_equals_default,&d1set_field_to_default,3,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactSetPhoneNumberRequest*>(base_address)->number,0} } },
                /*** MsgcontactContactOpenConversationRequest */
                /*79*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactOpenConversationRequest::END,2} } },
                /*** MsgcontactContactOpenConversationResponse */
                /*80*/ { { {&d6field_equals_default,&d6set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactOpenConversationResponse::END,18} } },
                /*** MsgcontactContactHasCapabilityRequest */
                /*81*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactHasCapabilityRequest::END,2} } },
                /*82*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactHasCapabilityRequest*>(base_address)->capability,0} } },
                /*83*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactHasCapabilityRequest*>(base_address)->queryServer,0} } },
                /*** MsgcontactContactHasCapabilityResponse */
                /*84*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactContactHasCapabilityResponse::END,0} } },
                /*** MsgcontactContactGetCapabilityStatusRequest */
                /*85*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactGetCapabilityStatusRequest::END,2} } },
                /*86*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGetCapabilityStatusRequest*>(base_address)->capability,0} } },
                /*87*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContactGetCapabilityStatusRequest*>(base_address)->queryServer,0} } },
                /*** MsgcontactContactGetCapabilityStatusResponse */
                /*88*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgcontactContactGetCapabilityStatusResponse::END,0} } },
                /*** MsgcontactContactRefreshProfileRequest */
                /*89*/ { { {&d8field_equals_default,&d8set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactContactRefreshProfileRequest::END,2} } },
                /*** MsgcontactContact */
                /*90*/ { { {&d1field_equals_default,&d1set_field_to_default,4,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactContact::END,0} } },
                /*91*/ { { {&d1field_equals_default,&d1set_field_to_default,5,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->fullname,0} } },
                /*92*/ { { {&d1field_equals_default,&d1set_field_to_default,6,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->pstnnumber,0} } },
                /*93*/ { { {&d2field_equals_default,&d2set_field_to_default,7,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->birthday,0} } },
                /*94*/ { { {&d2field_equals_default,&d2set_field_to_default,8,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->gender,0} } },
                /*95*/ { { {&d1field_equals_default,&d1set_field_to_default,9,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->languages,0} } },
                /*96*/ { { {&d1field_equals_default,&d1set_field_to_default,10,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->country,0} } },
                /*97*/ { { {&d1field_equals_default,&d1set_field_to_default,11,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->province,0} } },
                /*98*/ { { {&d1field_equals_default,&d1set_field_to_default,12,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->city,0} } },
                /*99*/ { { {&d1field_equals_default,&d1set_field_to_default,13,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->phone_home,0} } },
                /*100*/ { { {&d1field_equals_default,&d1set_field_to_default,14,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->phone_office,0} } },
                /*101*/ { { {&d1field_equals_default,&d1set_field_to_default,15,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->phone_mobile,0} } },
                /*102*/ { { {&d1field_equals_default,&d1set_field_to_default,16,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->emails,0} } },
                /*103*/ { { {&d1field_equals_default,&d1set_field_to_default,17,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->homepage,0} } },
                /*104*/ { { {&d1field_equals_default,&d1set_field_to_default,18,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->about,0} } },
                /*105*/ { { {&d2field_equals_default,&d2set_field_to_default,19,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->profile_timestamp,0} } },
                /*106*/ { { {&d1field_equals_default,&d1set_field_to_default,20,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->received_authrequest,0} } },
                /*107*/ { { {&d1field_equals_default,&d1set_field_to_default,21,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->displayname,0} } },
                /*108*/ { { {&d3field_equals_default,&d3set_field_to_default,22,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->refreshing,0} } },                /*109*/ { { {&d9field_equals_default,&d9set_field_to_default,23,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->given_authlevel,0} } },
                /*110*/ { { {&d2field_equals_default,&d2set_field_to_default,25,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->authreq_timestamp,0} } },
                /*111*/ { { {&d1field_equals_default,&d1set_field_to_default,26,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->mood_text,0} } },
                /*112*/ { { {&d2field_equals_default,&d2set_field_to_default,27,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->timezone,0} } },
                /*113*/ { { {&d2field_equals_default,&d2set_field_to_default,28,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->nrof_authed_buddies,0} } },
                /*114*/ { { {&d1field_equals_default,&d1set_field_to_default,29,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->ipcountry,0} } },
                /*115*/ { { {&d1field_equals_default,&d1set_field_to_default,33,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->given_displayname,0} } },
                /*116*/ { { {&d9field_equals_default,&d9set_field_to_default,34,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->availability,0} } },
                /*117*/ { { {&d2field_equals_default,&d2set_field_to_default,35,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->lastonline_timestamp,0} } },
                /*118*/ { { {&d11field_equals_default,&d11set_field_to_default,36,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->capabilities,0} } },
                /*119*/ { { {&d11field_equals_default,&d11set_field_to_default,37,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->avatar_image,0} } },
                /*120*/ { { {&d2field_equals_default,&d2set_field_to_default,39,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->lastused_timestamp,0} } },
                /*121*/ { { {&d2field_equals_default,&d2set_field_to_default,41,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->authrequest_count,0} } },
                /*122*/ { { {&d2field_equals_default,&d2set_field_to_default,42,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->popularity_ord,0} } },
                /*123*/ { { {&d1field_equals_default,&d1set_field_to_default,180,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->assigned_comment,0} } },
                /*124*/ { { {&d2field_equals_default,&d2set_field_to_default,182,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->avatar_timestamp,0} } },
                /*125*/ { { {&d2field_equals_default,&d2set_field_to_default,183,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->mood_timestamp,0} } },
                /*126*/ { { {&d1field_equals_default,&d1set_field_to_default,184,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->assigned_phone1,0} } },
                /*127*/ { { {&d1field_equals_default,&d1set_field_to_default,185,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->assigned_phone1_label,0} } },
                /*128*/ { { {&d1field_equals_default,&d1set_field_to_default,186,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->assigned_phone2,0} } },
                /*129*/ { { {&d1field_equals_default,&d1set_field_to_default,187,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->assigned_phone2_label,0} } },
                /*130*/ { { {&d1field_equals_default,&d1set_field_to_default,188,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->assigned_phone3,0} } },
                /*131*/ { { {&d1field_equals_default,&d1set_field_to_default,189,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->assigned_phone3_label,0} } },
                /*132*/ { { {&d9field_equals_default,&d9set_field_to_default,202,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->type,0} } },
                /*133*/ { { {&d1field_equals_default,&d1set_field_to_default,205,0,Sid::Field::KIND_XML, (unsigned short) (size_t) &reinterpret_cast<MsgcontactContact*>(base_address)->rich_mood_text,0} } },
                /*** MsgcontactGetContactTypeRequest */
                /*134*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactGetContactTypeRequest::END,0} } },
                /*** MsgcontactGetContactTypeResponse */
                /*135*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgcontactGetContactTypeResponse::END,0} } },
                /*** MsgcontactGetContactRequest */
                /*136*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactGetContactRequest::END,0} } },
                /*** MsgcontactGetContactResponse */
                /*137*/ { { {&d8field_equals_default,&d8set_field_to_default,2,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactGetContactResponse::END,2} } },
                /*** MsgcontactFindContactByPstnNumberRequest */
                /*138*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactFindContactByPstnNumberRequest::END,0} } },
                /*** MsgcontactFindContactByPstnNumberResponse */
                /*139*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactFindContactByPstnNumberResponse::END,0} } },
                /*140*/ { { {&d8field_equals_default,&d8set_field_to_default,2,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgcontactFindContactByPstnNumberResponse*>(base_address)->contact,2} } },
                /*141*/ { { {&d2field_equals_default,&d2set_field_to_default,3,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactFindContactByPstnNumberResponse*>(base_address)->foundInKey,0} } },
                /*** MsgcontactGetIdentityTypeRequest */
                /*142*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactGetIdentityTypeRequest::END,0} } },
                /*** MsgcontactGetIdentityTypeResponse */
                /*143*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgcontactGetIdentityTypeResponse::END,0} } },
                /*** MsgcontactIdentitiesMatchRequest */
                /*144*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactIdentitiesMatchRequest::END,0} } },
                /*145*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactIdentitiesMatchRequest*>(base_address)->identityB,0} } },
                /*** MsgcontactIdentitiesMatchResponse */
                /*146*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactIdentitiesMatchResponse::END,0} } },                /*** MsgcontactNormalizeIdentityRequest */
                /*147*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactNormalizeIdentityRequest::END,0} } },
                /*148*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactNormalizeIdentityRequest*>(base_address)->isNewSkypeName,0} } },
                /*** MsgcontactNormalizeIdentityResponse */
                /*149*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgcontactNormalizeIdentityResponse::END,0} } },
                /*150*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactNormalizeIdentityResponse*>(base_address)->normalized,0} } },
                /*** MsgcontactNormalizePSTNWithCountryRequest */
                /*151*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactNormalizePSTNWithCountryRequest::END,0} } },
                /*152*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactNormalizePSTNWithCountryRequest*>(base_address)->countryPrefix,0} } },
                /*** MsgcontactNormalizePSTNWithCountryResponse */
                /*153*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgcontactNormalizePSTNWithCountryResponse::END,0} } },
                /*154*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactNormalizePSTNWithCountryResponse*>(base_address)->normalized,0} } },
                /*** MsgcontactOnContactOnlineAppearance */
                /*155*/ { { {&d8field_equals_default,&d8set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactOnContactOnlineAppearance::END,2} } },
                /*** MsgcontactOnContactGoneOffline */
                /*156*/ { { {&d8field_equals_default,&d8set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactOnContactGoneOffline::END,2} } },
                /*** MsgcontactsearchContactSearchAddMinAgeTermRequest */
                /*157*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchAddMinAgeTermRequest::END,1} } },
                /*158*/ { { {&d2field_equals_default,&d2set_field_to_default,1,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddMinAgeTermRequest*>(base_address)->min_age_in_years,0} } },
                /*159*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddMinAgeTermRequest*>(base_address)->add_to_subs,0} } },
                /*** MsgcontactsearchContactSearchAddMinAgeTermResponse */
                /*160*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactsearchContactSearchAddMinAgeTermResponse::END,0} } },
                /*** MsgcontactsearchContactSearchAddMaxAgeTermRequest */
                /*161*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchAddMaxAgeTermRequest::END,1} } },
                /*162*/ { { {&d2field_equals_default,&d2set_field_to_default,1,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddMaxAgeTermRequest*>(base_address)->max_age_in_years,0} } },
                /*163*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddMaxAgeTermRequest*>(base_address)->add_to_subs,0} } },
                /*** MsgcontactsearchContactSearchAddMaxAgeTermResponse */
                /*164*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactsearchContactSearchAddMaxAgeTermResponse::END,0} } },
                /*** MsgcontactsearchContactSearchAddEmailTermRequest */
                /*165*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchAddEmailTermRequest::END,1} } },
                /*166*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddEmailTermRequest*>(base_address)->email,0} } },
                /*167*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddEmailTermRequest*>(base_address)->add_to_subs,0} } },
                /*** MsgcontactsearchContactSearchAddEmailTermResponse */
                /*168*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactsearchContactSearchAddEmailTermResponse::END,0} } },
                /*** MsgcontactsearchContactSearchAddLanguageTermRequest */
                /*169*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchAddLanguageTermRequest::END,1} } },
                /*170*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddLanguageTermRequest*>(base_address)->language,0} } },
                /*171*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddLanguageTermRequest*>(base_address)->add_to_subs,0} } },
                /*** MsgcontactsearchContactSearchAddLanguageTermResponse */
                /*172*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactsearchContactSearchAddLanguageTermResponse::END,0} } },
                /*** MsgcontactsearchContactSearchAddStrTermRequest */
                /*173*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchAddStrTermRequest::END,1} } },
                /*174*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddStrTermRequest*>(base_address)->prop,0} } },
                /*175*/ { { {&d9field_equals_default,&d9set_field_to_default,2,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddStrTermRequest*>(base_address)->cond,0} } },
                /*176*/ { { {&d1field_equals_default,&d1set_field_to_default,3,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddStrTermRequest*>(base_address)->value,0} } },
                /*177*/ { { {&d12field_equals_default,&d12set_field_to_default,4,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddStrTermRequest*>(base_address)->add_to_subs,0} } },
                /*** MsgcontactsearchContactSearchAddStrTermResponse */
                /*178*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactsearchContactSearchAddStrTermResponse::END,0} } },
                /*** MsgcontactsearchContactSearchAddIntTermRequest */
                /*179*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchAddIntTermRequest::END,1} } },
                /*180*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddIntTermRequest*>(base_address)->prop,0} } },                /*181*/ { { {&d9field_equals_default,&d9set_field_to_default,2,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddIntTermRequest*>(base_address)->cond,0} } },
                /*182*/ { { {&d2field_equals_default,&d2set_field_to_default,3,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddIntTermRequest*>(base_address)->value,0} } },
                /*183*/ { { {&d12field_equals_default,&d12set_field_to_default,4,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchAddIntTermRequest*>(base_address)->add_to_subs,0} } },
                /*** MsgcontactsearchContactSearchAddIntTermResponse */
                /*184*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactsearchContactSearchAddIntTermResponse::END,0} } },
                /*** MsgcontactsearchContactSearchAddOrRequest */
                /*185*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchAddOrRequest::END,1} } },
                /*** MsgcontactsearchContactSearchIsValidRequest */
                /*186*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchIsValidRequest::END,1} } },
                /*** MsgcontactsearchContactSearchIsValidResponse */
                /*187*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgcontactsearchContactSearchIsValidResponse::END,0} } },
                /*** MsgcontactsearchContactSearchSubmitRequest */
                /*188*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchSubmitRequest::END,1} } },
                /*** MsgcontactsearchContactSearchExtendRequest */
                /*189*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchExtendRequest::END,1} } },
                /*** MsgcontactsearchContactSearchReleaseRequest */
                /*190*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchReleaseRequest::END,1} } },
                /*** MsgcontactsearchContactSearchGetResultsRequest */
                /*191*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchGetResultsRequest::END,1} } },
                /*192*/ { { {&d2field_equals_default,&d2set_field_to_default,1,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchGetResultsRequest*>(base_address)->from,0} } },
                /*193*/ { { {&d15field_equals_default,&d15set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchGetResultsRequest*>(base_address)->count,0} } },
                /*** MsgcontactsearchContactSearchGetResultsResponse */
                /*194*/ { { {&d7field_equals_default,&d7set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchGetResultsResponse::END,2} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d7append),reinterpret_cast<void(*)(void*)>(&d7iterate),0,0,0,0,0}}},
                /*** MsgcontactsearchContactSearchOnNewResult */
                /*196*/ { { {&d14field_equals_default,&d14set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchContactSearchOnNewResult::END,1} } },
                /*197*/ { { {&d8field_equals_default,&d8set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchOnNewResult*>(base_address)->contact,2} } },
                /*198*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgcontactsearchContactSearchOnNewResult*>(base_address)->rankValue,0} } },
                /*** MsgcontactsearchContactSearch */
                /*199*/ { { {&d9field_equals_default,&d9set_field_to_default,200,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgcontactsearchContactSearch::END,0} } },
                /*** MsgcontactsearchGetOptimalAgeRangesResponse */
                /*200*/ { { {&d16field_equals_default,&d16set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_UINT, MsgcontactsearchGetOptimalAgeRangesResponse::END,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d16append),reinterpret_cast<void(*)(void*)>(&d16iterate),0,0,0,0,0}}},
                /*** MsgcontactsearchCreateContactSearchResponse */
                /*202*/ { { {&d14field_equals_default,&d14set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchCreateContactSearchResponse::END,1} } },
                /*** MsgcontactsearchCreateBasicContactSearchRequest */
                /*203*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactsearchCreateBasicContactSearchRequest::END,0} } },
                /*** MsgcontactsearchCreateBasicContactSearchResponse */
                /*204*/ { { {&d14field_equals_default,&d14set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchCreateBasicContactSearchResponse::END,1} } },
                /*** MsgcontactsearchCreateIdentitySearchRequest */
                /*205*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgcontactsearchCreateIdentitySearchRequest::END,0} } },
                /*** MsgcontactsearchCreateIdentitySearchResponse */
                /*206*/ { { {&d14field_equals_default,&d14set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgcontactsearchCreateIdentitySearchResponse::END,1} } },
                /*** MsgconversationParticipantCanSetRankToRequest */
                /*207*/ { { {&d17field_equals_default,&d17set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantCanSetRankToRequest::END,19} } },
                /*208*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipantCanSetRankToRequest*>(base_address)->rank,0} } },
                /*** MsgconversationParticipantCanSetRankToResponse */
                /*209*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgconversationParticipantCanSetRankToResponse::END,0} } },
                /*** MsgconversationParticipantSetRankToRequest */
                /*210*/ { { {&d17field_equals_default,&d17set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantSetRankToRequest::END,19} } },
                /*211*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipantSetRankToRequest*>(base_address)->rank,0} } },
                /*** MsgconversationParticipantRingRequest */
                /*212*/ { { {&d17field_equals_default,&d17set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantRingRequest::END,19} } },
                /*213*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipantRingRequest*>(base_address)->identityToUse,0} } },
                /*214*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipantRingRequest*>(base_address)->videoCall,0} } },
                /*215*/ { { {&d2field_equals_default,&d2set_field_to_default,3,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipantRingRequest*>(base_address)->nrofRedials,0} } },                /*216*/ { { {&d2field_equals_default,&d2set_field_to_default,4,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipantRingRequest*>(base_address)->redialPeriod,0} } },
                /*217*/ { { {&d12field_equals_default,&d12set_field_to_default,5,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipantRingRequest*>(base_address)->autoStartVM,0} } },
                /*218*/ { { {&d1field_equals_default,&d1set_field_to_default,6,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipantRingRequest*>(base_address)->origin,0} } },
                /*** MsgconversationParticipantRingItRequest */
                /*219*/ { { {&d17field_equals_default,&d17set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantRingItRequest::END,19} } },
                /*** MsgconversationParticipantSetLiveIdentityToUseRequest */
                /*220*/ { { {&d17field_equals_default,&d17set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantSetLiveIdentityToUseRequest::END,19} } },
                /*221*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipantSetLiveIdentityToUseRequest*>(base_address)->identityToUse,0} } },
                /*** MsgconversationParticipantGetVideoRequest */
                /*222*/ { { {&d17field_equals_default,&d17set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantGetVideoRequest::END,19} } },
                /*** MsgconversationParticipantGetVideoResponse */
                /*223*/ { { {&d18field_equals_default,&d18set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantGetVideoResponse::END,11} } },
                /*** MsgconversationParticipantHangupRequest */
                /*224*/ { { {&d17field_equals_default,&d17set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantHangupRequest::END,19} } },
                /*** MsgconversationParticipantRetireRequest */
                /*225*/ { { {&d17field_equals_default,&d17set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantRetireRequest::END,19} } },
                /*** MsgconversationParticipantGetLiveSessionVideosRequest */
                /*226*/ { { {&d17field_equals_default,&d17set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantGetLiveSessionVideosRequest::END,19} } },
                /*** MsgconversationParticipantGetLiveSessionVideosResponse */
                /*227*/ { { {&d19field_equals_default,&d19set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantGetLiveSessionVideosResponse::END,11} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d19append),reinterpret_cast<void(*)(void*)>(&d19iterate),0,0,0,0,0}}},
                /*** MsgconversationParticipantOnIncomingDTMF */
                /*229*/ { { {&d17field_equals_default,&d17set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantOnIncomingDTMF::END,19} } },
                /*230*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipantOnIncomingDTMF*>(base_address)->dtmf,0} } },
                /*** MsgconversationParticipantOnLiveSessionVideosChanged */
                /*231*/ { { {&d17field_equals_default,&d17set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipantOnLiveSessionVideosChanged::END,19} } },
                /*** MsgconversationParticipant */
                /*232*/ { { {&d6field_equals_default,&d6set_field_to_default,930,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationParticipant::END,18} } },
                /*233*/ { { {&d1field_equals_default,&d1set_field_to_default,931,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->identity,0} } },
                /*234*/ { { {&d9field_equals_default,&d9set_field_to_default,932,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->rank,0} } },
                /*235*/ { { {&d9field_equals_default,&d9set_field_to_default,933,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->requested_rank,0} } },
                /*236*/ { { {&d9field_equals_default,&d9set_field_to_default,934,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->text_status,0} } },
                /*237*/ { { {&d9field_equals_default,&d9set_field_to_default,935,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->voice_status,0} } },
                /*238*/ { { {&d9field_equals_default,&d9set_field_to_default,936,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->video_status,0} } },
                /*239*/ { { {&d1field_equals_default,&d1set_field_to_default,938,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->live_price_for_me,0} } },
                /*240*/ { { {&d2field_equals_default,&d2set_field_to_default,939,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->live_start_timestamp,0} } },
                /*241*/ { { {&d2field_equals_default,&d2set_field_to_default,941,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->sound_level,0} } },
                /*242*/ { { {&d1field_equals_default,&d1set_field_to_default,942,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->debuginfo,0} } },
                /*243*/ { { {&d1field_equals_default,&d1set_field_to_default,943,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->live_identity,0} } },
                /*244*/ { { {&d1field_equals_default,&d1set_field_to_default,947,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->last_voice_error,0} } },
                /*245*/ { { {&d1field_equals_default,&d1set_field_to_default,948,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->live_fwd_identities,0} } },
                /*246*/ { { {&d1field_equals_default,&d1set_field_to_default,949,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->quality_problems,0} } },
                /*247*/ { { {&d9field_equals_default,&d9set_field_to_default,950,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->live_type,0} } },
                /*248*/ { { {&d1field_equals_default,&d1set_field_to_default,951,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->live_country,0} } },
                /*249*/ { { {&d1field_equals_default,&d1set_field_to_default,952,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->transferred_by,0} } },
                /*250*/ { { {&d1field_equals_default,&d1set_field_to_default,953,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->transferred_to,0} } },
                /*251*/ { { {&d1field_equals_default,&d1set_field_to_default,954,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->adder,0} } },                /*252*/ { { {&d9field_equals_default,&d9set_field_to_default,955,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationParticipant*>(base_address)->last_leavereason,0} } },
                /*** MsgconversationConversationSetOptionRequest */
                /*253*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSetOptionRequest::END,18} } },
                /*254*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetOptionRequest*>(base_address)->propKey,0} } },
                /*255*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetOptionRequest*>(base_address)->value,0} } },
                /*** MsgconversationConversationSetTopicRequest */
                /*256*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSetTopicRequest::END,18} } },
                /*257*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetTopicRequest*>(base_address)->topic,0} } },
                /*258*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetTopicRequest*>(base_address)->isXML,0} } },
                /*** MsgconversationConversationSetGuidelinesRequest */
                /*259*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSetGuidelinesRequest::END,18} } },
                /*260*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetGuidelinesRequest*>(base_address)->guidelines,0} } },
                /*261*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetGuidelinesRequest*>(base_address)->isXML,0} } },
                /*** MsgconversationConversationSetPictureRequest */
                /*262*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSetPictureRequest::END,18} } },
                /*263*/ { { {&d11field_equals_default,&d11set_field_to_default,1,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetPictureRequest*>(base_address)->jpeg,0} } },
                /*** MsgconversationConversationSpawnConferenceRequest */
                /*264*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSpawnConferenceRequest::END,18} } },
                /*265*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSpawnConferenceRequest*>(base_address)->identitiesToAdd,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*** MsgconversationConversationSpawnConferenceResponse */
                /*267*/ { { {&d6field_equals_default,&d6set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSpawnConferenceResponse::END,18} } },
                /*** MsgconversationConversationAddConsumersRequest */
                /*268*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationAddConsumersRequest::END,18} } },
                /*269*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationAddConsumersRequest*>(base_address)->identities,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*** MsgconversationConversationAssimilateRequest */
                /*271*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationAssimilateRequest::END,18} } },
                /*272*/ { { {&d6field_equals_default,&d6set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationAssimilateRequest*>(base_address)->otherConversation,18} } },
                /*** MsgconversationConversationAssimilateResponse */
                /*273*/ { { {&d6field_equals_default,&d6set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationAssimilateResponse::END,18} } },
                /*** MsgconversationConversationJoinLiveSessionRequest */
                /*274*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationJoinLiveSessionRequest::END,18} } },
                /*275*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationJoinLiveSessionRequest*>(base_address)->accessToken,0} } },
                /*** MsgconversationConversationRingOthersRequest */
                /*276*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationRingOthersRequest::END,18} } },
                /*277*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationRingOthersRequest*>(base_address)->identities,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*279*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationRingOthersRequest*>(base_address)->videoCall,0} } },
                /*280*/ { { {&d1field_equals_default,&d1set_field_to_default,3,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationRingOthersRequest*>(base_address)->origin,0} } },
                /*** MsgconversationConversationMuteMyMicrophoneRequest */
                /*281*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationMuteMyMicrophoneRequest::END,18} } },
                /*** MsgconversationConversationUnmuteMyMicrophoneRequest */
                /*282*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationUnmuteMyMicrophoneRequest::END,18} } },
                /*** MsgconversationConversationHoldMyLiveSessionRequest */
                /*283*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationHoldMyLiveSessionRequest::END,18} } },
                /*** MsgconversationConversationResumeMyLiveSessionRequest */
                /*284*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationResumeMyLiveSessionRequest::END,18} } },
                /*** MsgconversationConversationLeaveLiveSessionRequest */
                /*285*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationLeaveLiveSessionRequest::END,18} } },
                /*286*/ { { {&d12field_equals_default,&d12set_field_to_default,1,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationLeaveLiveSessionRequest*>(base_address)->postVoiceAutoresponse,0} } },                /*** MsgconversationConversationStartVoiceMessageRequest */
                /*287*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationStartVoiceMessageRequest::END,18} } },
                /*** MsgconversationConversationTransferLiveSessionRequest */
                /*288*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationTransferLiveSessionRequest::END,18} } },
                /*289*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationTransferLiveSessionRequest*>(base_address)->identities,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*291*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationTransferLiveSessionRequest*>(base_address)->transferTopic,0} } },
                /*292*/ { { {&d11field_equals_default,&d11set_field_to_default,3,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationTransferLiveSessionRequest*>(base_address)->context,0} } },
                /*** MsgconversationConversationCanTransferLiveSessionRequest */
                /*293*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationCanTransferLiveSessionRequest::END,18} } },
                /*294*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationCanTransferLiveSessionRequest*>(base_address)->identity,0} } },
                /*** MsgconversationConversationCanTransferLiveSessionResponse */
                /*295*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgconversationConversationCanTransferLiveSessionResponse::END,0} } },
                /*** MsgconversationConversationSendDTMFRequest */
                /*296*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSendDTMFRequest::END,18} } },
                /*297*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSendDTMFRequest*>(base_address)->dtmf,0} } },
                /*298*/ { { {&d21field_equals_default,&d21set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSendDTMFRequest*>(base_address)->lengthInMS,0} } },
                /*** MsgconversationConversationStopSendDTMFRequest */
                /*299*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationStopSendDTMFRequest::END,18} } },
                /*** MsgconversationConversationSetMyTextStatusToRequest */
                /*300*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSetMyTextStatusToRequest::END,18} } },
                /*301*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetMyTextStatusToRequest*>(base_address)->status,0} } },
                /*** MsgconversationConversationPostTextRequest */
                /*302*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationPostTextRequest::END,18} } },
                /*303*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationPostTextRequest*>(base_address)->text,0} } },
                /*304*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationPostTextRequest*>(base_address)->isXML,0} } },
                /*** MsgconversationConversationPostTextResponse */
                /*305*/ { { {&d22field_equals_default,&d22set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationPostTextResponse::END,9} } },
                /*** MsgconversationConversationPostContactsRequest */
                /*306*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationPostContactsRequest::END,18} } },
                /*307*/ { { {&d7field_equals_default,&d7set_field_to_default,1,Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationPostContactsRequest*>(base_address)->contacts,2} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d7append),reinterpret_cast<void(*)(void*)>(&d7iterate),0,0,0,0,0}}},
                /*** MsgconversationConversationPostFilesRequest */
                /*309*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationPostFilesRequest::END,18} } },
                /*310*/ { { {&d23field_equals_default,&d23set_field_to_default,1,Sid::Field::FLD_LIST,Sid::Field::KIND_FILENAME, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationPostFilesRequest*>(base_address)->paths,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d23append),reinterpret_cast<void(*)(void*)>(&d23iterate),0,0,0,0,0}}},
                /*312*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationPostFilesRequest*>(base_address)->body,0} } },
                /*** MsgconversationConversationPostFilesResponse */
                /*313*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgconversationConversationPostFilesResponse::END,0} } },
                /*314*/ { { {&d24field_equals_default,&d24set_field_to_default,2,0,Sid::Field::KIND_FILENAME, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationPostFilesResponse*>(base_address)->error_file,0} } },
                /*** MsgconversationConversationPostVoiceMessageRequest */
                /*315*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationPostVoiceMessageRequest::END,18} } },
                /*316*/ { { {&d25field_equals_default,&d25set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationPostVoiceMessageRequest*>(base_address)->voicemail,7} } },
                /*317*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationPostVoiceMessageRequest*>(base_address)->body,0} } },
                /*** MsgconversationConversationPostSMSRequest */
                /*318*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationPostSMSRequest::END,18} } },
                /*319*/ { { {&d26field_equals_default,&d26set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationPostSMSRequest*>(base_address)->sms,12} } },
                /*320*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationPostSMSRequest*>(base_address)->body,0} } },
                /*** MsgconversationConversationGetJoinBlobRequest */
                /*321*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationGetJoinBlobRequest::END,18} } },                /*** MsgconversationConversationGetJoinBlobResponse */
                /*322*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgconversationConversationGetJoinBlobResponse::END,0} } },
                /*** MsgconversationConversationJoinRequest */
                /*323*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationJoinRequest::END,18} } },
                /*** MsgconversationConversationEnterPasswordRequest */
                /*324*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationEnterPasswordRequest::END,18} } },
                /*325*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationEnterPasswordRequest*>(base_address)->password,0} } },
                /*** MsgconversationConversationSetPasswordRequest */
                /*326*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSetPasswordRequest::END,18} } },
                /*327*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetPasswordRequest*>(base_address)->password,0} } },
                /*328*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetPasswordRequest*>(base_address)->hint,0} } },
                /*** MsgconversationConversationRetireFromRequest */
                /*329*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationRetireFromRequest::END,18} } },
                /*** MsgconversationConversationDeleteRequest */
                /*330*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationDeleteRequest::END,18} } },
                /*** MsgconversationConversationRenameToRequest */
                /*331*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationRenameToRequest::END,18} } },
                /*332*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationRenameToRequest*>(base_address)->name,0} } },
                /*** MsgconversationConversationSetBookmarkRequest */
                /*333*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSetBookmarkRequest::END,18} } },
                /*334*/ { { {&d3field_equals_default,&d3set_field_to_default,1,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetBookmarkRequest*>(base_address)->bookmark,0} } },
                /*** MsgconversationConversationSetAlertStringRequest */
                /*335*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSetAlertStringRequest::END,18} } },
                /*336*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetAlertStringRequest*>(base_address)->alertString,0} } },
                /*** MsgconversationConversationRemoveFromInboxRequest */
                /*337*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationRemoveFromInboxRequest::END,18} } },
                /*** MsgconversationConversationAddToInboxRequest */
                /*338*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationAddToInboxRequest::END,18} } },
                /*339*/ { { {&d2field_equals_default,&d2set_field_to_default,1,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationAddToInboxRequest*>(base_address)->timestamp,0} } },
                /*** MsgconversationConversationSetConsumedHorizonRequest */
                /*340*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationSetConsumedHorizonRequest::END,18} } },
                /*341*/ { { {&d2field_equals_default,&d2set_field_to_default,1,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetConsumedHorizonRequest*>(base_address)->timestamp,0} } },
                /*342*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationSetConsumedHorizonRequest*>(base_address)->also_unconsume,0} } },
                /*** MsgconversationConversationMarkUnreadRequest */
                /*343*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationMarkUnreadRequest::END,18} } },
                /*** MsgconversationConversationIsMemberOfRequest */
                /*344*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationIsMemberOfRequest::END,18} } },
                /*345*/ { { {&d4field_equals_default,&d4set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationIsMemberOfRequest*>(base_address)->group,10} } },
                /*** MsgconversationConversationIsMemberOfResponse */
                /*346*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgconversationConversationIsMemberOfResponse::END,0} } },
                /*** MsgconversationConversationGetParticipantsRequest */
                /*347*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationGetParticipantsRequest::END,18} } },
                /*348*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationGetParticipantsRequest*>(base_address)->filter,0} } },
                /*** MsgconversationConversationGetParticipantsResponse */
                /*349*/ { { {&d27field_equals_default,&d27set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, MsgconversationConversationGetParticipantsResponse::END,19} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d27append),reinterpret_cast<void(*)(void*)>(&d27iterate),0,0,0,0,0}}},
                /*** MsgconversationConversationGetLastMessagesRequest */
                /*351*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationGetLastMessagesRequest::END,18} } },
                /*352*/ { { {&d2field_equals_default,&d2set_field_to_default,1,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationGetLastMessagesRequest*>(base_address)->requireTimestamp,0} } },
                /*** MsgconversationConversationGetLastMessagesResponse */
                /*353*/ { { {&d28field_equals_default,&d28set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, MsgconversationConversationGetLastMessagesResponse::END,9} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d28append),reinterpret_cast<void(*)(void*)>(&d28iterate),0,0,0,0,0}}},
                /*355*/ { { {&d28field_equals_default,&d28set_field_to_default,2,Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationGetLastMessagesResponse*>(base_address)->unconsumedMessages,9} } },                { { { reinterpret_cast<bool(*)(const void*)>(&d28append),reinterpret_cast<void(*)(void*)>(&d28iterate),0,0,0,0,0}}},
                /*** MsgconversationConversationFindMessageRequest */
                /*357*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationFindMessageRequest::END,18} } },
                /*358*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationFindMessageRequest*>(base_address)->text,0} } },
                /*359*/ { { {&d15field_equals_default,&d15set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationFindMessageRequest*>(base_address)->fromTimestampUp,0} } },
                /*** MsgconversationConversationFindMessageResponse */
                /*360*/ { { {&d22field_equals_default,&d22set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationFindMessageResponse::END,9} } },
                /*** MsgconversationConversationAttachVideoToLiveSessionRequest */
                /*361*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationAttachVideoToLiveSessionRequest::END,18} } },
                /*362*/ { { {&d18field_equals_default,&d18set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationAttachVideoToLiveSessionRequest*>(base_address)->sendVideo,11} } },
                /*** MsgconversationConversationOnParticipantListChange */
                /*363*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationOnParticipantListChange::END,18} } },
                /*** MsgconversationConversationOnMessage */
                /*364*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationOnMessage::END,18} } },
                /*365*/ { { {&d22field_equals_default,&d22set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationOnMessage*>(base_address)->message,9} } },
                /*** MsgconversationConversationOnSpawnConference */
                /*366*/ { { {&d6field_equals_default,&d6set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationConversationOnSpawnConference::END,18} } },
                /*367*/ { { {&d6field_equals_default,&d6set_field_to_default,1,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversationOnSpawnConference*>(base_address)->spawned,18} } },
                /*** MsgconversationConversation */
                /*368*/ { { {&d9field_equals_default,&d9set_field_to_default,902,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgconversationConversation::END,0} } },
                /*369*/ { { {&d1field_equals_default,&d1set_field_to_default,903,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->creator,0} } },
                /*370*/ { { {&d2field_equals_default,&d2set_field_to_default,904,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->creation_timestamp,0} } },
                /*371*/ { { {&d9field_equals_default,&d9set_field_to_default,906,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->opt_entry_level_rank,0} } },
                /*372*/ { { {&d3field_equals_default,&d3set_field_to_default,907,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->opt_disclose_history,0} } },
                /*373*/ { { {&d9field_equals_default,&d9set_field_to_default,909,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->opt_admin_only_activities,0} } },
                /*374*/ { { {&d1field_equals_default,&d1set_field_to_default,910,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->meta_name,0} } },
                /*375*/ { { {&d1field_equals_default,&d1set_field_to_default,911,0,Sid::Field::KIND_XML, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->meta_topic,0} } },
                /*376*/ { { {&d1field_equals_default,&d1set_field_to_default,913,0,Sid::Field::KIND_XML, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->meta_guidelines,0} } },
                /*377*/ { { {&d11field_equals_default,&d11set_field_to_default,914,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->meta_picture,0} } },
                /*378*/ { { {&d6field_equals_default,&d6set_field_to_default,915,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->spawned_from_convo_id,18} } },
                /*379*/ { { {&d1field_equals_default,&d1set_field_to_default,918,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->live_host,0} } },
                /*380*/ { { {&d9field_equals_default,&d9set_field_to_default,919,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->my_status,0} } },
                /*381*/ { { {&d1field_equals_default,&d1set_field_to_default,920,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->alert_string,0} } },
                /*382*/ { { {&d3field_equals_default,&d3set_field_to_default,921,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->is_bookmarked,0} } },
                /*383*/ { { {&d3field_equals_default,&d3set_field_to_default,922,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->opt_joining_enabled,0} } },
                /*384*/ { { {&d1field_equals_default,&d1set_field_to_default,924,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->displayname,0} } },
                /*385*/ { { {&d1field_equals_default,&d1set_field_to_default,925,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->given_displayname,0} } },
                /*386*/ { { {&d9field_equals_default,&d9set_field_to_default,927,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->local_livestatus,0} } },
                /*387*/ { { {&d2field_equals_default,&d2set_field_to_default,928,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->inbox_timestamp,0} } },
                /*388*/ { { {&d3field_equals_default,&d3set_field_to_default,970,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->unconsumed_messages_voice,0} } },
                /*389*/ { { {&d25field_equals_default,&d25set_field_to_default,971,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->active_vm_id,7} } },
                /*390*/ { { {&d1field_equals_default,&d1set_field_to_default,972,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->identity,0} } },
                /*391*/ { { {&d22field_equals_default,&d22set_field_to_default,973,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->inbox_message_id,9} } },
                /*392*/ { { {&d2field_equals_default,&d2set_field_to_default,974,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->live_start_timestamp,0} } },                /*393*/ { { {&d2field_equals_default,&d2set_field_to_default,975,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->unconsumed_suppressed_messages,0} } },
                /*394*/ { { {&d2field_equals_default,&d2set_field_to_default,976,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->unconsumed_normal_messages,0} } },
                /*395*/ { { {&d2field_equals_default,&d2set_field_to_default,977,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->unconsumed_elevated_messages,0} } },
                /*396*/ { { {&d2field_equals_default,&d2set_field_to_default,979,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->consumption_horizon,0} } },
                /*397*/ { { {&d1field_equals_default,&d1set_field_to_default,980,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->passwordhint,0} } },
                /*398*/ { { {&d2field_equals_default,&d2set_field_to_default,981,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->last_activity_timestamp,0} } },
                /*399*/ { { {&d3field_equals_default,&d3set_field_to_default,996,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationConversation*>(base_address)->live_is_muted,0} } },
                /*** MsgconversationCreateConferenceResponse */
                /*400*/ { { {&d6field_equals_default,&d6set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationCreateConferenceResponse::END,18} } },
                /*** MsgconversationGetConversationByIdentityRequest */
                /*401*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgconversationGetConversationByIdentityRequest::END,0} } },
                /*402*/ { { {&d13field_equals_default,&d13set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationGetConversationByIdentityRequest*>(base_address)->matchPSTN,0} } },
                /*** MsgconversationGetConversationByIdentityResponse */
                /*403*/ { { {&d6field_equals_default,&d6set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationGetConversationByIdentityResponse::END,18} } },
                /*** MsgconversationGetConversationByParticipantsRequest */
                /*404*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, MsgconversationGetConversationByParticipantsRequest::END,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*406*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationGetConversationByParticipantsRequest*>(base_address)->createIfNonExisting,0} } },
                /*407*/ { { {&d12field_equals_default,&d12set_field_to_default,3,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationGetConversationByParticipantsRequest*>(base_address)->ignoreBookmarkedOrNamed,0} } },
                /*** MsgconversationGetConversationByParticipantsResponse */
                /*408*/ { { {&d6field_equals_default,&d6set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationGetConversationByParticipantsResponse::END,18} } },
                /*** MsgconversationGetConversationByBlobRequest */
                /*409*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgconversationGetConversationByBlobRequest::END,0} } },
                /*410*/ { { {&d13field_equals_default,&d13set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationGetConversationByBlobRequest*>(base_address)->alsoJoin,0} } },
                /*** MsgconversationGetConversationByBlobResponse */
                /*411*/ { { {&d6field_equals_default,&d6set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationGetConversationByBlobResponse::END,18} } },
                /*** MsgconversationGetConversationListRequest */
                /*412*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgconversationGetConversationListRequest::END,0} } },
                /*** MsgconversationGetConversationListResponse */
                /*413*/ { { {&d5field_equals_default,&d5set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, MsgconversationGetConversationListResponse::END,18} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d5append),reinterpret_cast<void(*)(void*)>(&d5iterate),0,0,0,0,0}}},
                /*** MsgconversationOnConversationListChange */
                /*415*/ { { {&d6field_equals_default,&d6set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationOnConversationListChange::END,18} } },
                /*416*/ { { {&d9field_equals_default,&d9set_field_to_default,2,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationOnConversationListChange*>(base_address)->type,0} } },
                /*417*/ { { {&d3field_equals_default,&d3set_field_to_default,3,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationOnConversationListChange*>(base_address)->added,0} } },
                /*** MsgconversationMessageCanEditRequest */
                /*418*/ { { {&d22field_equals_default,&d22set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationMessageCanEditRequest::END,9} } },
                /*** MsgconversationMessageCanEditResponse */
                /*419*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgconversationMessageCanEditResponse::END,0} } },
                /*** MsgconversationMessageGetPermissionsRequest */
                /*420*/ { { {&d22field_equals_default,&d22set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationMessageGetPermissionsRequest::END,9} } },
                /*** MsgconversationMessageGetPermissionsResponse */
                /*421*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgconversationMessageGetPermissionsResponse::END,0} } },
                /*** MsgconversationMessageEditRequest */
                /*422*/ { { {&d22field_equals_default,&d22set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationMessageEditRequest::END,9} } },
                /*423*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessageEditRequest*>(base_address)->newText,0} } },
                /*424*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessageEditRequest*>(base_address)->isXML,0} } },
                /*425*/ { { {&d12field_equals_default,&d12set_field_to_default,3,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessageEditRequest*>(base_address)->undo,0} } },
                /*426*/ { { {&d1field_equals_default,&d1set_field_to_default,4,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessageEditRequest*>(base_address)->legacyPrefix,0} } },
                /*** MsgconversationMessageGetContactsRequest */
                /*427*/ { { {&d22field_equals_default,&d22set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationMessageGetContactsRequest::END,9} } },                /*** MsgconversationMessageGetContactsResponse */
                /*428*/ { { {&d7field_equals_default,&d7set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, MsgconversationMessageGetContactsResponse::END,2} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d7append),reinterpret_cast<void(*)(void*)>(&d7iterate),0,0,0,0,0}}},
                /*** MsgconversationMessageGetTransfersRequest */
                /*430*/ { { {&d22field_equals_default,&d22set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationMessageGetTransfersRequest::END,9} } },
                /*** MsgconversationMessageGetTransfersResponse */
                /*431*/ { { {&d29field_equals_default,&d29set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, MsgconversationMessageGetTransfersResponse::END,6} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d29append),reinterpret_cast<void(*)(void*)>(&d29iterate),0,0,0,0,0}}},
                /*** MsgconversationMessageGetVoiceMessageRequest */
                /*433*/ { { {&d22field_equals_default,&d22set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationMessageGetVoiceMessageRequest::END,9} } },
                /*** MsgconversationMessageGetVoiceMessageResponse */
                /*434*/ { { {&d25field_equals_default,&d25set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationMessageGetVoiceMessageResponse::END,7} } },
                /*** MsgconversationMessageGetSMSRequest */
                /*435*/ { { {&d22field_equals_default,&d22set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationMessageGetSMSRequest::END,9} } },
                /*** MsgconversationMessageGetSMSResponse */
                /*436*/ { { {&d26field_equals_default,&d26set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationMessageGetSMSResponse::END,12} } },
                /*** MsgconversationMessageDeleteLocallyRequest */
                /*437*/ { { {&d22field_equals_default,&d22set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationMessageDeleteLocallyRequest::END,9} } },
                /*** MsgconversationMessage */
                /*438*/ { { {&d1field_equals_default,&d1set_field_to_default,120,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgconversationMessage::END,0} } },
                /*439*/ { { {&d2field_equals_default,&d2set_field_to_default,121,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->timestamp,0} } },
                /*440*/ { { {&d1field_equals_default,&d1set_field_to_default,122,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->author,0} } },
                /*441*/ { { {&d1field_equals_default,&d1set_field_to_default,123,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->author_displayname,0} } },
                /*442*/ { { {&d1field_equals_default,&d1set_field_to_default,125,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->identities,0} } },
                /*443*/ { { {&d9field_equals_default,&d9set_field_to_default,126,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->leavereason,0} } },
                /*444*/ { { {&d1field_equals_default,&d1set_field_to_default,127,0,Sid::Field::KIND_XML, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->body_xml,0} } },
                /*445*/ { { {&d1field_equals_default,&d1set_field_to_default,222,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->edited_by,0} } },
                /*446*/ { { {&d2field_equals_default,&d2set_field_to_default,223,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->edit_timestamp,0} } },
                /*447*/ { { {&d1field_equals_default,&d1set_field_to_default,790,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->originally_meant_for,0} } },
                /*448*/ { { {&d11field_equals_default,&d11set_field_to_default,792,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->guid,0} } },
                /*449*/ { { {&d6field_equals_default,&d6set_field_to_default,960,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->convo_id,18} } },
                /*450*/ { { {&d9field_equals_default,&d9set_field_to_default,961,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->type,0} } },
                /*451*/ { { {&d9field_equals_default,&d9set_field_to_default,962,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->sending_status,0} } },
                /*452*/ { { {&d2field_equals_default,&d2set_field_to_default,963,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->param_key,0} } },
                /*453*/ { { {&d2field_equals_default,&d2set_field_to_default,964,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->param_value,0} } },
                /*454*/ { { {&d1field_equals_default,&d1set_field_to_default,966,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->reason,0} } },
                /*455*/ { { {&d9field_equals_default,&d9set_field_to_default,968,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->consumption_status,0} } },
                /*456*/ { { {&d2field_equals_default,&d2set_field_to_default,982,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationMessage*>(base_address)->participant_count,0} } },
                /*** MsgconversationGetMessageByGuidRequest */
                /*457*/ { { {&d11field_equals_default,&d11set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BINARY, MsgconversationGetMessageByGuidRequest::END,0} } },
                /*** MsgconversationGetMessageByGuidResponse */
                /*458*/ { { {&d22field_equals_default,&d22set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationGetMessageByGuidResponse::END,9} } },
                /*** MsgconversationGetMessageListByTypeRequest */
                /*459*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgconversationGetMessageListByTypeRequest::END,0} } },
                /*460*/ { { {&d3field_equals_default,&d3set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationGetMessageListByTypeRequest*>(base_address)->latestPerConvOnly,0} } },
                /*461*/ { { {&d2field_equals_default,&d2set_field_to_default,3,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationGetMessageListByTypeRequest*>(base_address)->fromTimestampInc,0} } },
                /*462*/ { { {&d15field_equals_default,&d15set_field_to_default,4,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgconversationGetMessageListByTypeRequest*>(base_address)->toTimestampExc,0} } },
                /*** MsgconversationGetMessageListByTypeResponse */
                /*463*/ { { {&d28field_equals_default,&d28set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_OBJECTID, MsgconversationGetMessageListByTypeResponse::END,9} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d28append),reinterpret_cast<void(*)(void*)>(&d28iterate),0,0,0,0,0}}},
                /*** MsgconversationOnMessage */                /*465*/ { { {&d22field_equals_default,&d22set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgconversationOnMessage::END,9} } },
                /*466*/ { { {&d3field_equals_default,&d3set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgconversationOnMessage*>(base_address)->changesInboxTimestamp,0} } },
                /*467*/ { { {&d22field_equals_default,&d22set_field_to_default,3,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationOnMessage*>(base_address)->supersedesHistoryMessage,9} } },
                /*468*/ { { {&d6field_equals_default,&d6set_field_to_default,4,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgconversationOnMessage*>(base_address)->conversation,18} } },
                /*** MsgvideoVideoStartRequest */
                /*469*/ { { {&d18field_equals_default,&d18set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvideoVideoStartRequest::END,11} } },
                /*** MsgvideoVideoStopRequest */
                /*470*/ { { {&d18field_equals_default,&d18set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvideoVideoStopRequest::END,11} } },
                /*** MsgvideoVideoSetScreenCaptureRectangleRequest */
                /*471*/ { { {&d18field_equals_default,&d18set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvideoVideoSetScreenCaptureRectangleRequest::END,11} } },
                /*472*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_INT, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoSetScreenCaptureRectangleRequest*>(base_address)->x0,0} } },
                /*473*/ { { {&d9field_equals_default,&d9set_field_to_default,2,0,Sid::Field::KIND_INT, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoSetScreenCaptureRectangleRequest*>(base_address)->y0,0} } },
                /*474*/ { { {&d2field_equals_default,&d2set_field_to_default,3,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoSetScreenCaptureRectangleRequest*>(base_address)->width,0} } },
                /*475*/ { { {&d2field_equals_default,&d2set_field_to_default,4,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoSetScreenCaptureRectangleRequest*>(base_address)->height,0} } },
                /*476*/ { { {&d9field_equals_default,&d9set_field_to_default,5,0,Sid::Field::KIND_INT, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoSetScreenCaptureRectangleRequest*>(base_address)->monitorNumber,0} } },
                /*477*/ { { {&d2field_equals_default,&d2set_field_to_default,6,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoSetScreenCaptureRectangleRequest*>(base_address)->windowHandle,0} } },
                /*** MsgvideoVideoSetRemoteRendererIdRequest */
                /*478*/ { { {&d18field_equals_default,&d18set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvideoVideoSetRemoteRendererIdRequest::END,11} } },
                /*479*/ { { {&d2field_equals_default,&d2set_field_to_default,1,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoSetRemoteRendererIdRequest*>(base_address)->id,0} } },
                /*** MsgvideoVideoSelectVideoSourceRequest */
                /*480*/ { { {&d18field_equals_default,&d18set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvideoVideoSelectVideoSourceRequest::END,11} } },
                /*481*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoSelectVideoSourceRequest*>(base_address)->mediaType,0} } },
                /*482*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoSelectVideoSourceRequest*>(base_address)->webcamName,0} } },
                /*483*/ { { {&d1field_equals_default,&d1set_field_to_default,3,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoSelectVideoSourceRequest*>(base_address)->devicePath,0} } },
                /*484*/ { { {&d12field_equals_default,&d12set_field_to_default,4,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoSelectVideoSourceRequest*>(base_address)->updateSetup,0} } },
                /*** MsgvideoVideoGetCurrentVideoDeviceRequest */
                /*485*/ { { {&d18field_equals_default,&d18set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvideoVideoGetCurrentVideoDeviceRequest::END,11} } },
                /*** MsgvideoVideoGetCurrentVideoDeviceResponse */
                /*486*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgvideoVideoGetCurrentVideoDeviceResponse::END,0} } },
                /*487*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoGetCurrentVideoDeviceResponse*>(base_address)->deviceName,0} } },
                /*488*/ { { {&d1field_equals_default,&d1set_field_to_default,3,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideoGetCurrentVideoDeviceResponse*>(base_address)->devicePath,0} } },
                /*** MsgvideoVideo */
                /*489*/ { { {&d9field_equals_default,&d9set_field_to_default,130,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgvideoVideo::END,0} } },
                /*490*/ { { {&d1field_equals_default,&d1set_field_to_default,131,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideo*>(base_address)->error,0} } },
                /*491*/ { { {&d1field_equals_default,&d1set_field_to_default,132,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideo*>(base_address)->debuginfo,0} } },
                /*492*/ { { {&d1field_equals_default,&d1set_field_to_default,133,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideo*>(base_address)->dimensions,0} } },
                /*493*/ { { {&d9field_equals_default,&d9set_field_to_default,134,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideo*>(base_address)->media_type,0} } },
                /*494*/ { { {&d6field_equals_default,&d6set_field_to_default,1104,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideo*>(base_address)->convo_id,18} } },
                /*495*/ { { {&d1field_equals_default,&d1set_field_to_default,1105,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoVideo*>(base_address)->device_path,0} } },
                /*** MsgvideoGetAvailableVideoDevicesResponse */
                /*496*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, MsgvideoGetAvailableVideoDevicesResponse::END,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*498*/ { { {&d20field_equals_default,&d20set_field_to_default,2,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoGetAvailableVideoDevicesResponse*>(base_address)->devicePaths,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*500*/ { { {&d2field_equals_default,&d2set_field_to_default,3,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgvideoGetAvailableVideoDevicesResponse*>(base_address)->count,0} } },
                /*** MsgvideoHasVideoDeviceCapabilityRequest */
                /*501*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgvideoHasVideoDeviceCapabilityRequest::END,0} } },
                /*502*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoHasVideoDeviceCapabilityRequest*>(base_address)->devicePath,0} } },                /*503*/ { { {&d9field_equals_default,&d9set_field_to_default,3,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgvideoHasVideoDeviceCapabilityRequest*>(base_address)->cap,0} } },
                /*** MsgvideoHasVideoDeviceCapabilityResponse */
                /*504*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgvideoHasVideoDeviceCapabilityResponse::END,0} } },
                /*** MsgvideoDisplayVideoDeviceTuningDialogRequest */
                /*505*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgvideoDisplayVideoDeviceTuningDialogRequest::END,0} } },
                /*506*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoDisplayVideoDeviceTuningDialogRequest*>(base_address)->devicePath,0} } },
                /*** MsgvideoGetPreviewVideoRequest */
                /*507*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgvideoGetPreviewVideoRequest::END,0} } },
                /*508*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoGetPreviewVideoRequest*>(base_address)->deviceName,0} } },
                /*509*/ { { {&d1field_equals_default,&d1set_field_to_default,3,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoGetPreviewVideoRequest*>(base_address)->devicePath,0} } },
                /*** MsgvideoGetPreviewVideoResponse */
                /*510*/ { { {&d18field_equals_default,&d18set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvideoGetPreviewVideoResponse::END,11} } },
                /*** MsgvideoCreateLocalVideoRequest */
                /*511*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgvideoCreateLocalVideoRequest::END,0} } },
                /*512*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoCreateLocalVideoRequest*>(base_address)->deviceName,0} } },
                /*513*/ { { {&d1field_equals_default,&d1set_field_to_default,3,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoCreateLocalVideoRequest*>(base_address)->devicePath,0} } },
                /*** MsgvideoCreateLocalVideoResponse */
                /*514*/ { { {&d18field_equals_default,&d18set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvideoCreateLocalVideoResponse::END,11} } },
                /*** MsgvideoCreatePreviewVideoRequest */
                /*515*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgvideoCreatePreviewVideoRequest::END,0} } },
                /*516*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoCreatePreviewVideoRequest*>(base_address)->deviceName,0} } },
                /*517*/ { { {&d1field_equals_default,&d1set_field_to_default,3,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvideoCreatePreviewVideoRequest*>(base_address)->devicePath,0} } },
                /*** MsgvideoCreatePreviewVideoResponse */
                /*518*/ { { {&d18field_equals_default,&d18set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvideoCreatePreviewVideoResponse::END,11} } },
                /*** MsgvideoVideoCommandRequest */
                /*519*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgvideoVideoCommandRequest::END,0} } },
                /*** MsgvideoVideoCommandResponse */
                /*520*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgvideoVideoCommandResponse::END,0} } },
                /*** MsgqualitymonitorStartMonitoringQualityRequest */
                /*521*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgqualitymonitorStartMonitoringQualityRequest::END,0} } },
                /*522*/ { { {&d3field_equals_default,&d3set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgqualitymonitorStartMonitoringQualityRequest*>(base_address)->excludeNetworkTest,0} } },
                /*** MsgqualitymonitorStopMonitoringQualityRequest */
                /*523*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgqualitymonitorStopMonitoringQualityRequest::END,0} } },
                /*524*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgqualitymonitorStopMonitoringQualityRequest*>(base_address)->justStop,0} } },
                /*** MsgqualitymonitorStopMonitoringQualityResponse */
                /*525*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgqualitymonitorStopMonitoringQualityResponse::END,0} } },
                /*** MsgqualitymonitorOnQualityTestResult */
                /*526*/ { { {&d9field_equals_default,&d9set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgqualitymonitorOnQualityTestResult::END,0} } },
                /*527*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgqualitymonitorOnQualityTestResult*>(base_address)->testResult,0} } },
                /*528*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgqualitymonitorOnQualityTestResult*>(base_address)->withUser,0} } },
                /*529*/ { { {&d1field_equals_default,&d1set_field_to_default,3,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgqualitymonitorOnQualityTestResult*>(base_address)->details,0} } },
                /*530*/ { { {&d1field_equals_default,&d1set_field_to_default,4,0,Sid::Field::KIND_XML, (unsigned short) (size_t) &reinterpret_cast<MsgqualitymonitorOnQualityTestResult*>(base_address)->xmlDetails,0} } },
                /*** MsgvmVoicemailStartRecordingRequest */
                /*531*/ { { {&d25field_equals_default,&d25set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvmVoicemailStartRecordingRequest::END,7} } },
                /*** MsgvmVoicemailStopRecordingRequest */
                /*532*/ { { {&d25field_equals_default,&d25set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvmVoicemailStopRecordingRequest::END,7} } },
                /*** MsgvmVoicemailStartPlaybackRequest */
                /*533*/ { { {&d25field_equals_default,&d25set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvmVoicemailStartPlaybackRequest::END,7} } },
                /*** MsgvmVoicemailStopPlaybackRequest */
                /*534*/ { { {&d25field_equals_default,&d25set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvmVoicemailStopPlaybackRequest::END,7} } },
                /*** MsgvmVoicemailDeleteRequest */
                /*535*/ { { {&d25field_equals_default,&d25set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvmVoicemailDeleteRequest::END,7} } },
                /*** MsgvmVoicemailCancelRequest */
                /*536*/ { { {&d25field_equals_default,&d25set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvmVoicemailCancelRequest::END,7} } },
                /*** MsgvmVoicemailCheckPermissionRequest */
                /*537*/ { { {&d25field_equals_default,&d25set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvmVoicemailCheckPermissionRequest::END,7} } },
                /*** MsgvmVoicemailCheckPermissionResponse */
                /*538*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgvmVoicemailCheckPermissionResponse::END,0} } },                /*** MsgvmVoicemail */
                /*539*/ { { {&d9field_equals_default,&d9set_field_to_default,100,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgvmVoicemail::END,0} } },
                /*540*/ { { {&d1field_equals_default,&d1set_field_to_default,101,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvmVoicemail*>(base_address)->partner_handle,0} } },
                /*541*/ { { {&d1field_equals_default,&d1set_field_to_default,102,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvmVoicemail*>(base_address)->partner_dispname,0} } },
                /*542*/ { { {&d9field_equals_default,&d9set_field_to_default,103,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgvmVoicemail*>(base_address)->status,0} } },
                /*543*/ { { {&d9field_equals_default,&d9set_field_to_default,104,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgvmVoicemail*>(base_address)->failurereason,0} } },
                /*544*/ { { {&d1field_equals_default,&d1set_field_to_default,105,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgvmVoicemail*>(base_address)->subject,0} } },
                /*545*/ { { {&d2field_equals_default,&d2set_field_to_default,106,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgvmVoicemail*>(base_address)->timestamp,0} } },
                /*546*/ { { {&d2field_equals_default,&d2set_field_to_default,107,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgvmVoicemail*>(base_address)->duration,0} } },
                /*547*/ { { {&d2field_equals_default,&d2set_field_to_default,108,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgvmVoicemail*>(base_address)->allowed_duration,0} } },
                /*548*/ { { {&d2field_equals_default,&d2set_field_to_default,109,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgvmVoicemail*>(base_address)->playback_progress,0} } },
                /*549*/ { { {&d6field_equals_default,&d6set_field_to_default,830,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgvmVoicemail*>(base_address)->convo_id,18} } },
                /*550*/ { { {&d11field_equals_default,&d11set_field_to_default,831,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgvmVoicemail*>(base_address)->chatmsg_guid,0} } },
                /*** MsgvmGetGreetingRequest */
                /*551*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgvmGetGreetingRequest::END,0} } },
                /*** MsgvmGetGreetingResponse */
                /*552*/ { { {&d25field_equals_default,&d25set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgvmGetGreetingResponse::END,7} } },
                /*** MsgaudioPlayStartRequest */
                /*553*/ { { {&d2field_equals_default,&d2set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_UINT, MsgaudioPlayStartRequest::END,0} } },
                /*554*/ { { {&d11field_equals_default,&d11set_field_to_default,2,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgaudioPlayStartRequest*>(base_address)->sound,0} } },
                /*555*/ { { {&d3field_equals_default,&d3set_field_to_default,3,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaudioPlayStartRequest*>(base_address)->loop,0} } },
                /*556*/ { { {&d12field_equals_default,&d12set_field_to_default,4,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaudioPlayStartRequest*>(base_address)->useCallOutDevice,0} } },
                /*** MsgaudioPlayStartFromFileRequest */
                /*557*/ { { {&d2field_equals_default,&d2set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_UINT, MsgaudioPlayStartFromFileRequest::END,0} } },
                /*558*/ { { {&d24field_equals_default,&d24set_field_to_default,2,0,Sid::Field::KIND_FILENAME, (unsigned short) (size_t) &reinterpret_cast<MsgaudioPlayStartFromFileRequest*>(base_address)->datafile,0} } },
                /*559*/ { { {&d3field_equals_default,&d3set_field_to_default,3,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaudioPlayStartFromFileRequest*>(base_address)->loop,0} } },
                /*560*/ { { {&d12field_equals_default,&d12set_field_to_default,4,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaudioPlayStartFromFileRequest*>(base_address)->useCallOutDevice,0} } },
                /*** MsgaudioPlayStartFromFileResponse */
                /*561*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgaudioPlayStartFromFileResponse::END,0} } },
                /*** MsgaudioPlayStopRequest */
                /*562*/ { { {&d2field_equals_default,&d2set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_UINT, MsgaudioPlayStopRequest::END,0} } },
                /*** MsgaudioStartRecordingTestRequest */
                /*563*/ { { {&d12field_equals_default,&d12set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgaudioStartRecordingTestRequest::END,0} } },
                /*** MsgaudioGetAvailableOutputDevicesResponse */
                /*564*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, MsgaudioGetAvailableOutputDevicesResponse::END,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*566*/ { { {&d20field_equals_default,&d20set_field_to_default,2,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaudioGetAvailableOutputDevicesResponse*>(base_address)->nameList,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*568*/ { { {&d20field_equals_default,&d20set_field_to_default,3,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaudioGetAvailableOutputDevicesResponse*>(base_address)->productIdList,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*** MsgaudioGetAvailableRecordingDevicesResponse */
                /*570*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, MsgaudioGetAvailableRecordingDevicesResponse::END,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*572*/ { { {&d20field_equals_default,&d20set_field_to_default,2,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaudioGetAvailableRecordingDevicesResponse*>(base_address)->nameList,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*574*/ { { {&d20field_equals_default,&d20set_field_to_default,3,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaudioGetAvailableRecordingDevicesResponse*>(base_address)->productIdList,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*** MsgaudioSelectSoundDevicesRequest */
                /*576*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaudioSelectSoundDevicesRequest::END,0} } },
                /*577*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaudioSelectSoundDevicesRequest*>(base_address)->callOutDevice,0} } },
                /*578*/ { { {&d1field_equals_default,&d1set_field_to_default,3,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaudioSelectSoundDevicesRequest*>(base_address)->waveOutDevice,0} } },                /*** MsgaudioGetAudioDeviceCapabilitiesRequest */
                /*579*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaudioGetAudioDeviceCapabilitiesRequest::END,0} } },
                /*** MsgaudioGetAudioDeviceCapabilitiesResponse */
                /*580*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaudioGetAudioDeviceCapabilitiesResponse::END,0} } },
                /*581*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaudioGetAudioDeviceCapabilitiesResponse*>(base_address)->capabilities,0} } },
                /*** MsgaudioGetNrgLevelsResponse */
                /*582*/ { { {&d2field_equals_default,&d2set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_UINT, MsgaudioGetNrgLevelsResponse::END,0} } },
                /*583*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaudioGetNrgLevelsResponse*>(base_address)->speakerLevel,0} } },
                /*** MsgaudioVoiceCommandRequest */
                /*584*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaudioVoiceCommandRequest::END,0} } },
                /*** MsgaudioVoiceCommandResponse */
                /*585*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaudioVoiceCommandResponse::END,0} } },
                /*** MsgaudioGetSpeakerVolumeResponse */
                /*586*/ { { {&d2field_equals_default,&d2set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_UINT, MsgaudioGetSpeakerVolumeResponse::END,0} } },
                /*** MsgaudioSetSpeakerVolumeRequest */
                /*587*/ { { {&d2field_equals_default,&d2set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_UINT, MsgaudioSetSpeakerVolumeRequest::END,0} } },
                /*** MsgaudioGetMicVolumeResponse */
                /*588*/ { { {&d2field_equals_default,&d2set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_UINT, MsgaudioGetMicVolumeResponse::END,0} } },
                /*** MsgaudioSetMicVolumeRequest */
                /*589*/ { { {&d2field_equals_default,&d2set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_UINT, MsgaudioSetMicVolumeRequest::END,0} } },
                /*** MsgaudioIsSpeakerMutedResponse */
                /*590*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgaudioIsSpeakerMutedResponse::END,0} } },
                /*** MsgaudioIsMicrophoneMutedResponse */
                /*591*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgaudioIsMicrophoneMutedResponse::END,0} } },
                /*** MsgaudioMuteSpeakersRequest */
                /*592*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgaudioMuteSpeakersRequest::END,0} } },
                /*** MsgaudioMuteMicrophoneRequest */
                /*593*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgaudioMuteMicrophoneRequest::END,0} } },
                /*** MsgbwmSetOperatingMediaRequest */
                /*594*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgbwmSetOperatingMediaRequest::END,0} } },
                /*595*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgbwmSetOperatingMediaRequest*>(base_address)->maxUplinkBps,0} } },
                /*596*/ { { {&d2field_equals_default,&d2set_field_to_default,3,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgbwmSetOperatingMediaRequest*>(base_address)->maxDownlinkBps,0} } },
                /*** MsgsmsSmsGetTargetStatusRequest */
                /*597*/ { { {&d26field_equals_default,&d26set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgsmsSmsGetTargetStatusRequest::END,12} } },
                /*598*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSmsGetTargetStatusRequest*>(base_address)->target,0} } },
                /*** MsgsmsSmsGetTargetStatusResponse */
                /*599*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgsmsSmsGetTargetStatusResponse::END,0} } },
                /*** MsgsmsSmsGetTargetReplyNumberRequest */
                /*600*/ { { {&d26field_equals_default,&d26set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgsmsSmsGetTargetReplyNumberRequest::END,12} } },
                /*601*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSmsGetTargetReplyNumberRequest*>(base_address)->target,0} } },
                /*** MsgsmsSmsGetTargetReplyNumberResponse */
                /*602*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsmsSmsGetTargetReplyNumberResponse::END,0} } },
                /*** MsgsmsSmsGetTargetReplyTypeRequest */
                /*603*/ { { {&d26field_equals_default,&d26set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgsmsSmsGetTargetReplyTypeRequest::END,12} } },
                /*604*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSmsGetTargetReplyTypeRequest*>(base_address)->target,0} } },
                /*** MsgsmsSmsGetTargetReplyTypeResponse */
                /*605*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgsmsSmsGetTargetReplyTypeResponse::END,0} } },
                /*** MsgsmsSmsGetTargetPriceRequest */
                /*606*/ { { {&d26field_equals_default,&d26set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgsmsSmsGetTargetPriceRequest::END,12} } },
                /*607*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSmsGetTargetPriceRequest*>(base_address)->target,0} } },
                /*** MsgsmsSmsGetTargetPriceResponse */
                /*608*/ { { {&d2field_equals_default,&d2set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_UINT, MsgsmsSmsGetTargetPriceResponse::END,0} } },
                /*** MsgsmsSmsSetTargetsRequest */
                /*609*/ { { {&d26field_equals_default,&d26set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgsmsSmsSetTargetsRequest::END,12} } },
                /*610*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSmsSetTargetsRequest*>(base_address)->numbers,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*** MsgsmsSmsSetTargetsResponse */
                /*612*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgsmsSmsSetTargetsResponse::END,0} } },
                /*** MsgsmsSmsSetBodyRequest */
                /*613*/ { { {&d26field_equals_default,&d26set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgsmsSmsSetBodyRequest::END,12} } },
                /*614*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSmsSetBodyRequest*>(base_address)->text,0} } },
                /*** MsgsmsSmsSetBodyResponse */
                /*615*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgsmsSmsSetBodyResponse::END,0} } },                /*616*/ { { {&d20field_equals_default,&d20set_field_to_default,2,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSmsSetBodyResponse*>(base_address)->chunks,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*618*/ { { {&d2field_equals_default,&d2set_field_to_default,3,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSmsSetBodyResponse*>(base_address)->charsUntilNextChunk,0} } },
                /*** MsgsmsSmsGetBodyChunksRequest */
                /*619*/ { { {&d26field_equals_default,&d26set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgsmsSmsGetBodyChunksRequest::END,12} } },
                /*** MsgsmsSmsGetBodyChunksResponse */
                /*620*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, MsgsmsSmsGetBodyChunksResponse::END,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*622*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSmsGetBodyChunksResponse*>(base_address)->charsUntilNextChunk,0} } },
                /*** MsgsmsSms */
                /*623*/ { { {&d3field_equals_default,&d3set_field_to_default,48,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgsmsSms::END,0} } },
                /*624*/ { { {&d2field_equals_default,&d2set_field_to_default,49,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->price_precision,0} } },
                /*625*/ { { {&d9field_equals_default,&d9set_field_to_default,190,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->type,0} } },
                /*626*/ { { {&d9field_equals_default,&d9set_field_to_default,191,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->status,0} } },
                /*627*/ { { {&d9field_equals_default,&d9set_field_to_default,192,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->failurereason,0} } },
                /*628*/ { { {&d2field_equals_default,&d2set_field_to_default,193,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->price,0} } },
                /*629*/ { { {&d1field_equals_default,&d1set_field_to_default,194,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->price_currency,0} } },
                /*630*/ { { {&d1field_equals_default,&d1set_field_to_default,195,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->target_numbers,0} } },
                /*631*/ { { {&d11field_equals_default,&d11set_field_to_default,196,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->target_statuses,0} } },
                /*632*/ { { {&d1field_equals_default,&d1set_field_to_default,197,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->body,0} } },
                /*633*/ { { {&d2field_equals_default,&d2set_field_to_default,198,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->timestamp,0} } },
                /*634*/ { { {&d1field_equals_default,&d1set_field_to_default,199,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->reply_to_number,0} } },
                /*635*/ { { {&d22field_equals_default,&d22set_field_to_default,840,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->chatmsg_id,9} } },
                /*636*/ { { {&d9field_equals_default,&d9set_field_to_default,1211,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSms*>(base_address)->outgoing_reply_type,0} } },
                /*** MsgsmsRequestConfirmationCodeRequest */
                /*637*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgsmsRequestConfirmationCodeRequest::END,0} } },
                /*638*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsRequestConfirmationCodeRequest*>(base_address)->number,0} } },
                /*** MsgsmsRequestConfirmationCodeResponse */
                /*639*/ { { {&d26field_equals_default,&d26set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgsmsRequestConfirmationCodeResponse::END,12} } },
                /*** MsgsmsSubmitConfirmationCodeRequest */
                /*640*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsmsSubmitConfirmationCodeRequest::END,0} } },
                /*641*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsmsSubmitConfirmationCodeRequest*>(base_address)->code,0} } },
                /*** MsgsmsSubmitConfirmationCodeResponse */
                /*642*/ { { {&d26field_equals_default,&d26set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgsmsSubmitConfirmationCodeResponse::END,12} } },
                /*** MsgsmsCreateOutgoingSmsResponse */
                /*643*/ { { {&d26field_equals_default,&d26set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgsmsCreateOutgoingSmsResponse::END,12} } },
                /*** MsgftTransferAcceptRequest */
                /*644*/ { { {&d30field_equals_default,&d30set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgftTransferAcceptRequest::END,6} } },
                /*645*/ { { {&d24field_equals_default,&d24set_field_to_default,1,0,Sid::Field::KIND_FILENAME, (unsigned short) (size_t) &reinterpret_cast<MsgftTransferAcceptRequest*>(base_address)->filenameWithPath,0} } },
                /*** MsgftTransferAcceptResponse */
                /*646*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgftTransferAcceptResponse::END,0} } },
                /*** MsgftTransferPauseRequest */
                /*647*/ { { {&d30field_equals_default,&d30set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgftTransferPauseRequest::END,6} } },
                /*** MsgftTransferResumeRequest */
                /*648*/ { { {&d30field_equals_default,&d30set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgftTransferResumeRequest::END,6} } },
                /*** MsgftTransferCancelRequest */
                /*649*/ { { {&d30field_equals_default,&d30set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgftTransferCancelRequest::END,6} } },
                /*** MsgftTransfer */
                /*650*/ { { {&d9field_equals_default,&d9set_field_to_default,80,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgftTransfer::END,0} } },
                /*651*/ { { {&d1field_equals_default,&d1set_field_to_default,81,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->partner_handle,0} } },
                /*652*/ { { {&d1field_equals_default,&d1set_field_to_default,82,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->partner_dispname,0} } },
                /*653*/ { { {&d9field_equals_default,&d9set_field_to_default,83,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->status,0} } },
                /*654*/ { { {&d9field_equals_default,&d9set_field_to_default,84,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->failurereason,0} } },
                /*655*/ { { {&d2field_equals_default,&d2set_field_to_default,85,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->starttime,0} } },                /*656*/ { { {&d2field_equals_default,&d2set_field_to_default,86,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->finishtime,0} } },
                /*657*/ { { {&d1field_equals_default,&d1set_field_to_default,87,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->filepath,0} } },
                /*658*/ { { {&d1field_equals_default,&d1set_field_to_default,88,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->filename,0} } },
                /*659*/ { { {&d1field_equals_default,&d1set_field_to_default,89,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->filesize,0} } },
                /*660*/ { { {&d1field_equals_default,&d1set_field_to_default,90,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->bytestransferred,0} } },
                /*661*/ { { {&d2field_equals_default,&d2set_field_to_default,91,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->bytespersecond,0} } },
                /*662*/ { { {&d11field_equals_default,&d11set_field_to_default,92,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->chatmsg_guid,0} } },
                /*663*/ { { {&d2field_equals_default,&d2set_field_to_default,93,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->chatmsg_index,0} } },
                /*664*/ { { {&d6field_equals_default,&d6set_field_to_default,98,0,Sid::Field::KIND_OBJECTID, (unsigned short) (size_t) &reinterpret_cast<MsgftTransfer*>(base_address)->convo_id,18} } },
                /*** MsgaccountAccountGetStatusWithProgressRequest */
                /*665*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountGetStatusWithProgressRequest::END,5} } },
                /*** MsgaccountAccountGetStatusWithProgressResponse */
                /*666*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgaccountAccountGetStatusWithProgressResponse::END,0} } },
                /*667*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountGetStatusWithProgressResponse*>(base_address)->progress,0} } },
                /*** MsgaccountAccountLoginRequest */
                /*668*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountLoginRequest::END,5} } },
                /*669*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountLoginRequest*>(base_address)->setAvailabilityTo,0} } },
                /*** MsgaccountAccountLoginWithPasswordRequest */
                /*670*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountLoginWithPasswordRequest::END,5} } },
                /*671*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountLoginWithPasswordRequest*>(base_address)->password,0} } },
                /*672*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountLoginWithPasswordRequest*>(base_address)->savePwd,0} } },
                /*673*/ { { {&d13field_equals_default,&d13set_field_to_default,3,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountLoginWithPasswordRequest*>(base_address)->saveDataLocally,0} } },
                /*** MsgaccountAccountRegisterRequest */
                /*674*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountRegisterRequest::END,5} } },
                /*675*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountRegisterRequest*>(base_address)->password,0} } },
                /*676*/ { { {&d12field_equals_default,&d12set_field_to_default,2,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountRegisterRequest*>(base_address)->savePwd,0} } },
                /*677*/ { { {&d13field_equals_default,&d13set_field_to_default,3,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountRegisterRequest*>(base_address)->saveDataLocally,0} } },
                /*678*/ { { {&d1field_equals_default,&d1set_field_to_default,4,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountRegisterRequest*>(base_address)->email,0} } },
                /*679*/ { { {&d12field_equals_default,&d12set_field_to_default,5,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountRegisterRequest*>(base_address)->allowSpam,0} } },
                /*** MsgaccountAccountLogoutRequest */
                /*680*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountLogoutRequest::END,5} } },
                /*681*/ { { {&d12field_equals_default,&d12set_field_to_default,1,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountLogoutRequest*>(base_address)->clearSavedPwd,0} } },
                /*** MsgaccountAccountChangePasswordRequest */
                /*682*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountChangePasswordRequest::END,5} } },
                /*683*/ { { {&d1field_equals_default,&d1set_field_to_default,1,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountChangePasswordRequest*>(base_address)->oldPassword,0} } },
                /*684*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountChangePasswordRequest*>(base_address)->newPassword,0} } },
                /*685*/ { { {&d12field_equals_default,&d12set_field_to_default,3,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountChangePasswordRequest*>(base_address)->savePwd,0} } },
                /*** MsgaccountAccountSetPasswordSavedRequest */
                /*686*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountSetPasswordSavedRequest::END,5} } },
                /*687*/ { { {&d3field_equals_default,&d3set_field_to_default,1,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetPasswordSavedRequest*>(base_address)->savePwd,0} } },
                /*** MsgaccountAccountSetServersideIntPropertyRequest */
                /*688*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountSetServersideIntPropertyRequest::END,5} } },
                /*689*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetServersideIntPropertyRequest*>(base_address)->propKey,0} } },
                /*690*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetServersideIntPropertyRequest*>(base_address)->value,0} } },
                /*** MsgaccountAccountSetServersideStrPropertyRequest */
                /*691*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountSetServersideStrPropertyRequest::END,5} } },
                /*692*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetServersideStrPropertyRequest*>(base_address)->propKey,0} } },                /*693*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetServersideStrPropertyRequest*>(base_address)->value,0} } },
                /*** MsgaccountAccountCancelServerCommitRequest */
                /*694*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountCancelServerCommitRequest::END,5} } },
                /*** MsgaccountAccountSetIntPropertyRequest */
                /*695*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountSetIntPropertyRequest::END,5} } },
                /*696*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetIntPropertyRequest*>(base_address)->propKey,0} } },
                /*697*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetIntPropertyRequest*>(base_address)->value,0} } },
                /*** MsgaccountAccountSetStrPropertyRequest */
                /*698*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountSetStrPropertyRequest::END,5} } },
                /*699*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetStrPropertyRequest*>(base_address)->propKey,0} } },
                /*700*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetStrPropertyRequest*>(base_address)->value,0} } },
                /*** MsgaccountAccountSetBinPropertyRequest */
                /*701*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountSetBinPropertyRequest::END,5} } },
                /*702*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetBinPropertyRequest*>(base_address)->propKey,0} } },
                /*703*/ { { {&d11field_equals_default,&d11set_field_to_default,2,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetBinPropertyRequest*>(base_address)->value,0} } },
                /*** MsgaccountAccountSetAvailabilityRequest */
                /*704*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountSetAvailabilityRequest::END,5} } },
                /*705*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetAvailabilityRequest*>(base_address)->availability,0} } },
                /*** MsgaccountAccountSetStandbyRequest */
                /*706*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountSetStandbyRequest::END,5} } },
                /*707*/ { { {&d3field_equals_default,&d3set_field_to_default,1,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountSetStandbyRequest*>(base_address)->standby,0} } },
                /*** MsgaccountAccountGetCapabilityStatusRequest */
                /*708*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountGetCapabilityStatusRequest::END,5} } },
                /*709*/ { { {&d9field_equals_default,&d9set_field_to_default,1,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountGetCapabilityStatusRequest*>(base_address)->capability,0} } },
                /*** MsgaccountAccountGetCapabilityStatusResponse */
                /*710*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgaccountAccountGetCapabilityStatusResponse::END,0} } },
                /*711*/ { { {&d2field_equals_default,&d2set_field_to_default,2,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccountGetCapabilityStatusResponse*>(base_address)->expiryTimestamp,0} } },
                /*** MsgaccountAccountGetSkypenameHashRequest */
                /*712*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountGetSkypenameHashRequest::END,5} } },
                /*** MsgaccountAccountGetSkypenameHashResponse */
                /*713*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaccountAccountGetSkypenameHashResponse::END,0} } },
                /*** MsgaccountAccountGetVerifiedEmailRequest */
                /*714*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountGetVerifiedEmailRequest::END,5} } },
                /*** MsgaccountAccountGetVerifiedEmailResponse */
                /*715*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaccountAccountGetVerifiedEmailResponse::END,0} } },
                /*** MsgaccountAccountGetVerifiedCompanyRequest */
                /*716*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountGetVerifiedCompanyRequest::END,5} } },
                /*** MsgaccountAccountGetVerifiedCompanyResponse */
                /*717*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaccountAccountGetVerifiedCompanyResponse::END,0} } },
                /*** MsgaccountAccountDeleteRequest */
                /*718*/ { { {&d31field_equals_default,&d31set_field_to_default,0,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountAccountDeleteRequest::END,5} } },
                /*** MsgaccountAccount */
                /*719*/ { { {&d1field_equals_default,&d1set_field_to_default,4,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaccountAccount::END,0} } },
                /*720*/ { { {&d1field_equals_default,&d1set_field_to_default,5,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->fullname,0} } },
                /*721*/ { { {&d2field_equals_default,&d2set_field_to_default,7,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->birthday,0} } },
                /*722*/ { { {&d2field_equals_default,&d2set_field_to_default,8,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->gender,0} } },
                /*723*/ { { {&d1field_equals_default,&d1set_field_to_default,9,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->languages,0} } },
                /*724*/ { { {&d1field_equals_default,&d1set_field_to_default,10,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->country,0} } },
                /*725*/ { { {&d1field_equals_default,&d1set_field_to_default,11,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->province,0} } },
                /*726*/ { { {&d1field_equals_default,&d1set_field_to_default,12,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->city,0} } },
                /*727*/ { { {&d1field_equals_default,&d1set_field_to_default,13,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->phone_home,0} } },
                /*728*/ { { {&d1field_equals_default,&d1set_field_to_default,14,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->phone_office,0} } },
                /*729*/ { { {&d1field_equals_default,&d1set_field_to_default,15,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->phone_mobile,0} } },                /*730*/ { { {&d1field_equals_default,&d1set_field_to_default,16,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->emails,0} } },
                /*731*/ { { {&d1field_equals_default,&d1set_field_to_default,17,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->homepage,0} } },
                /*732*/ { { {&d1field_equals_default,&d1set_field_to_default,18,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->about,0} } },
                /*733*/ { { {&d2field_equals_default,&d2set_field_to_default,19,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->profile_timestamp,0} } },
                /*734*/ { { {&d1field_equals_default,&d1set_field_to_default,26,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->mood_text,0} } },
                /*735*/ { { {&d2field_equals_default,&d2set_field_to_default,27,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->timezone,0} } },
                /*736*/ { { {&d2field_equals_default,&d2set_field_to_default,28,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->nrof_authed_buddies,0} } },
                /*737*/ { { {&d9field_equals_default,&d9set_field_to_default,34,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->availability,0} } },
                /*738*/ { { {&d11field_equals_default,&d11set_field_to_default,37,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->avatar_image,0} } },
                /*739*/ { { {&d9field_equals_default,&d9set_field_to_default,70,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->status,0} } },
                /*740*/ { { {&d9field_equals_default,&d9set_field_to_default,71,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->pwdchangestatus,0} } },
                /*741*/ { { {&d1field_equals_default,&d1set_field_to_default,72,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->suggested_skypename,0} } },
                /*742*/ { { {&d9field_equals_default,&d9set_field_to_default,73,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->logoutreason,0} } },
                /*743*/ { { {&d1field_equals_default,&d1set_field_to_default,74,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->skypeout_balance_currency,0} } },
                /*744*/ { { {&d2field_equals_default,&d2set_field_to_default,75,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->skypeout_balance,0} } },
                /*745*/ { { {&d1field_equals_default,&d1set_field_to_default,76,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->skypein_numbers,0} } },
                /*746*/ { { {&d1field_equals_default,&d1set_field_to_default,77,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->offline_callforward,0} } },
                /*747*/ { { {&d9field_equals_default,&d9set_field_to_default,78,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->commitstatus,0} } },
                /*748*/ { { {&d9field_equals_default,&d9set_field_to_default,79,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->cblsyncstatus,0} } },
                /*749*/ { { {&d9field_equals_default,&d9set_field_to_default,160,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->chat_policy,0} } },
                /*750*/ { { {&d9field_equals_default,&d9set_field_to_default,161,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->skype_call_policy,0} } },
                /*751*/ { { {&d9field_equals_default,&d9set_field_to_default,162,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->pstn_call_policy,0} } },
                /*752*/ { { {&d9field_equals_default,&d9set_field_to_default,163,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->avatar_policy,0} } },
                /*753*/ { { {&d9field_equals_default,&d9set_field_to_default,164,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->buddycount_policy,0} } },
                /*754*/ { { {&d9field_equals_default,&d9set_field_to_default,165,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->timezone_policy,0} } },
                /*755*/ { { {&d9field_equals_default,&d9set_field_to_default,166,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->webpresence_policy,0} } },
                /*756*/ { { {&d9field_equals_default,&d9set_field_to_default,168,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->phonenumbers_policy,0} } },
                /*757*/ { { {&d9field_equals_default,&d9set_field_to_default,169,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->voicemail_policy,0} } },
                /*758*/ { { {&d2field_equals_default,&d2set_field_to_default,182,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->avatar_timestamp,0} } },
                /*759*/ { { {&d2field_equals_default,&d2set_field_to_default,183,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->mood_timestamp,0} } },
                /*760*/ { { {&d1field_equals_default,&d1set_field_to_default,205,0,Sid::Field::KIND_XML, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->rich_mood_text,0} } },
                /*761*/ { { {&d1field_equals_default,&d1set_field_to_default,773,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->partner_optedout,0} } },
                /*762*/ { { {&d1field_equals_default,&d1set_field_to_default,800,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->service_provider_info,0} } },
                /*763*/ { { {&d2field_equals_default,&d2set_field_to_default,801,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->registration_timestamp,0} } },
                /*764*/ { { {&d2field_equals_default,&d2set_field_to_default,802,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->nr_of_other_instances,0} } },
                /*765*/ { { {&d2field_equals_default,&d2set_field_to_default,804,0,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountAccount*>(base_address)->skypeout_precision,0} } },
                /*** MsgaccountGetAccountRequest */
                /*766*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaccountGetAccountRequest::END,0} } },
                /*** MsgaccountGetAccountResponse */
                /*767*/ { { {&d31field_equals_default,&d31set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_OBJECTID, MsgaccountGetAccountResponse::END,5} } },
                /*** MsgaccountGetExistingAccountsResponse */
                /*768*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, MsgaccountGetExistingAccountsResponse::END,0} } },                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*** MsgaccountGetDefaultAccountNameResponse */
                /*770*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaccountGetDefaultAccountNameResponse::END,0} } },
                /*** MsgaccountGetSuggestedSkypenameRequest */
                /*771*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaccountGetSuggestedSkypenameRequest::END,0} } },
                /*** MsgaccountGetSuggestedSkypenameResponse */
                /*772*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaccountGetSuggestedSkypenameResponse::END,0} } },
                /*** MsgaccountValidateAvatarRequest */
                /*773*/ { { {&d11field_equals_default,&d11set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BINARY, MsgaccountValidateAvatarRequest::END,0} } },
                /*** MsgaccountValidateAvatarResponse */
                /*774*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgaccountValidateAvatarResponse::END,0} } },
                /*775*/ { { {&d9field_equals_default,&d9set_field_to_default,2,0,Sid::Field::KIND_INT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountValidateAvatarResponse*>(base_address)->freeBytesLeft,0} } },
                /*** MsgaccountValidateProfileStringRequest */
                /*776*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgaccountValidateProfileStringRequest::END,0} } },
                /*777*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountValidateProfileStringRequest*>(base_address)->strValue,0} } },
                /*778*/ { { {&d12field_equals_default,&d12set_field_to_default,3,0,Sid::Field::KIND_BOOLEAN, (unsigned short) (size_t) &reinterpret_cast<MsgaccountValidateProfileStringRequest*>(base_address)->forRegistration,0} } },
                /*** MsgaccountValidateProfileStringResponse */
                /*779*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgaccountValidateProfileStringResponse::END,0} } },
                /*780*/ { { {&d9field_equals_default,&d9set_field_to_default,2,0,Sid::Field::KIND_INT, (unsigned short) (size_t) &reinterpret_cast<MsgaccountValidateProfileStringResponse*>(base_address)->freeBytesLeft,0} } },
                /*** MsgaccountValidatePasswordRequest */
                /*781*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgaccountValidatePasswordRequest::END,0} } },
                /*782*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgaccountValidatePasswordRequest*>(base_address)->password,0} } },
                /*** MsgaccountValidatePasswordResponse */
                /*783*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgaccountValidatePasswordResponse::END,0} } },
                /*** MsgconnectionOnProxyAuthFailure */
                /*784*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_ENUM, MsgconnectionOnProxyAuthFailure::END,0} } },
                /*** MsgconnectionGetUsedPortResponse */
                /*785*/ { { {&d2field_equals_default,&d2set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_UINT, MsgconnectionGetUsedPortResponse::END,0} } },
                /*** MsgsetupGetStrRequest */
                /*786*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsetupGetStrRequest::END,0} } },
                /*** MsgsetupGetStrResponse */
                /*787*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsetupGetStrResponse::END,0} } },
                /*** MsgsetupGetIntRequest */
                /*788*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsetupGetIntRequest::END,0} } },
                /*** MsgsetupGetIntResponse */
                /*789*/ { { {&d9field_equals_default,&d9set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_INT, MsgsetupGetIntResponse::END,0} } },
                /*** MsgsetupGetBinRequest */
                /*790*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsetupGetBinRequest::END,0} } },
                /*** MsgsetupGetBinResponse */
                /*791*/ { { {&d11field_equals_default,&d11set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BINARY, MsgsetupGetBinResponse::END,0} } },
                /*** MsgsetupSetStrRequest */
                /*792*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsetupSetStrRequest::END,0} } },
                /*793*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgsetupSetStrRequest*>(base_address)->value,0} } },
                /*** MsgsetupSetIntRequest */
                /*794*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsetupSetIntRequest::END,0} } },
                /*795*/ { { {&d9field_equals_default,&d9set_field_to_default,2,0,Sid::Field::KIND_INT, (unsigned short) (size_t) &reinterpret_cast<MsgsetupSetIntRequest*>(base_address)->value,0} } },
                /*** MsgsetupSetBinRequest */
                /*796*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsetupSetBinRequest::END,0} } },
                /*797*/ { { {&d11field_equals_default,&d11set_field_to_default,2,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<MsgsetupSetBinRequest*>(base_address)->value,0} } },
                /*** MsgsetupIsDefinedRequest */
                /*798*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsetupIsDefinedRequest::END,0} } },
                /*** MsgsetupIsDefinedResponse */
                /*799*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, MsgsetupIsDefinedResponse::END,0} } },
                /*** MsgsetupDeleteRequest */
                /*800*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsetupDeleteRequest::END,0} } },
                /*** MsgsetupGetSubKeysRequest */
                /*801*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgsetupGetSubKeysRequest::END,0} } },
                /*** MsgsetupGetSubKeysResponse */
                /*802*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, MsgsetupGetSubKeysResponse::END,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*** MsgisoGetISOLanguageInfoResponse */
                /*804*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, MsgisoGetISOLanguageInfoResponse::END,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*806*/ { { {&d20field_equals_default,&d20set_field_to_default,2,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgisoGetISOLanguageInfoResponse*>(base_address)->languageNameList,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},                /*** MsgisoGetISOCountryInfoResponse */
                /*808*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, MsgisoGetISOCountryInfoResponse::END,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*810*/ { { {&d20field_equals_default,&d20set_field_to_default,2,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgisoGetISOCountryInfoResponse*>(base_address)->countryNameList,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*812*/ { { {&d16field_equals_default,&d16set_field_to_default,3,Sid::Field::FLD_LIST,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<MsgisoGetISOCountryInfoResponse*>(base_address)->countryPrefixList,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d16append),reinterpret_cast<void(*)(void*)>(&d16iterate),0,0,0,0,0}}},
                /*814*/ { { {&d20field_equals_default,&d20set_field_to_default,4,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<MsgisoGetISOCountryInfoResponse*>(base_address)->countryDialExampleList,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*** MsgisoGetSupportedUILanguageListResponse */
                /*816*/ { { {&d20field_equals_default,&d20set_field_to_default,1,Sid::Field::FLD_FIRST|Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, MsgisoGetSupportedUILanguageListResponse::END,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*** MsgisoGetISOCountryCodebyPhoneNoRequest */
                /*818*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgisoGetISOCountryCodebyPhoneNoRequest::END,0} } },
                /*** MsgisoGetISOCountryCodebyPhoneNoResponse */
                /*819*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, MsgisoGetISOCountryCodebyPhoneNoResponse::END,0} } },
                /*** Msgapp2appApp2AppCreateRequest */
                /*820*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, Msgapp2appApp2AppCreateRequest::END,0} } },
                /*** Msgapp2appApp2AppCreateResponse */
                /*821*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, Msgapp2appApp2AppCreateResponse::END,0} } },
                /*** Msgapp2appApp2AppDeleteRequest */
                /*822*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, Msgapp2appApp2AppDeleteRequest::END,0} } },
                /*** Msgapp2appApp2AppDeleteResponse */
                /*823*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, Msgapp2appApp2AppDeleteResponse::END,0} } },
                /*** Msgapp2appApp2AppConnectRequest */
                /*824*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, Msgapp2appApp2AppConnectRequest::END,0} } },
                /*825*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppConnectRequest*>(base_address)->skypename,0} } },
                /*** Msgapp2appApp2AppConnectResponse */
                /*826*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, Msgapp2appApp2AppConnectResponse::END,0} } },
                /*** Msgapp2appApp2AppDisconnectRequest */
                /*827*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, Msgapp2appApp2AppDisconnectRequest::END,0} } },
                /*828*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppDisconnectRequest*>(base_address)->stream,0} } },
                /*** Msgapp2appApp2AppDisconnectResponse */
                /*829*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, Msgapp2appApp2AppDisconnectResponse::END,0} } },
                /*** Msgapp2appApp2AppWriteRequest */
                /*830*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, Msgapp2appApp2AppWriteRequest::END,0} } },
                /*831*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppWriteRequest*>(base_address)->stream,0} } },
                /*832*/ { { {&d11field_equals_default,&d11set_field_to_default,3,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppWriteRequest*>(base_address)->data,0} } },
                /*** Msgapp2appApp2AppWriteResponse */
                /*833*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, Msgapp2appApp2AppWriteResponse::END,0} } },
                /*** Msgapp2appApp2AppDatagramRequest */
                /*834*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, Msgapp2appApp2AppDatagramRequest::END,0} } },
                /*835*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppDatagramRequest*>(base_address)->stream,0} } },
                /*836*/ { { {&d11field_equals_default,&d11set_field_to_default,3,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppDatagramRequest*>(base_address)->data,0} } },
                /*** Msgapp2appApp2AppDatagramResponse */
                /*837*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, Msgapp2appApp2AppDatagramResponse::END,0} } },
                /*** Msgapp2appApp2AppReadRequest */
                /*838*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, Msgapp2appApp2AppReadRequest::END,0} } },
                /*839*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppReadRequest*>(base_address)->stream,0} } },
                /*** Msgapp2appApp2AppReadResponse */
                /*840*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, Msgapp2appApp2AppReadResponse::END,0} } },
                /*841*/ { { {&d11field_equals_default,&d11set_field_to_default,2,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppReadResponse*>(base_address)->data,0} } },
                /*** Msgapp2appApp2AppGetConnectableUsersRequest */
                /*842*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, Msgapp2appApp2AppGetConnectableUsersRequest::END,0} } },
                /*** Msgapp2appApp2AppGetConnectableUsersResponse */
                /*843*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, Msgapp2appApp2AppGetConnectableUsersResponse::END,0} } },
                /*844*/ { { {&d20field_equals_default,&d20set_field_to_default,2,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppGetConnectableUsersResponse*>(base_address)->users,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},                /*** Msgapp2appApp2AppGetStreamsListRequest */
                /*846*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, Msgapp2appApp2AppGetStreamsListRequest::END,0} } },
                /*847*/ { { {&d9field_equals_default,&d9set_field_to_default,2,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppGetStreamsListRequest*>(base_address)->listType,0} } },
                /*** Msgapp2appApp2AppGetStreamsListResponse */
                /*848*/ { { {&d3field_equals_default,&d3set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_BOOLEAN, Msgapp2appApp2AppGetStreamsListResponse::END,0} } },
                /*849*/ { { {&d20field_equals_default,&d20set_field_to_default,2,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppGetStreamsListResponse*>(base_address)->streams,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*851*/ { { {&d16field_equals_default,&d16set_field_to_default,3,Sid::Field::FLD_LIST,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appApp2AppGetStreamsListResponse*>(base_address)->receivedSizes,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d16append),reinterpret_cast<void(*)(void*)>(&d16iterate),0,0,0,0,0}}},
                /*** Msgapp2appOnApp2AppDatagram */
                /*853*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, Msgapp2appOnApp2AppDatagram::END,0} } },
                /*854*/ { { {&d1field_equals_default,&d1set_field_to_default,2,0,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appOnApp2AppDatagram*>(base_address)->stream,0} } },
                /*855*/ { { {&d11field_equals_default,&d11set_field_to_default,3,0,Sid::Field::KIND_BINARY, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appOnApp2AppDatagram*>(base_address)->data,0} } },
                /*** Msgapp2appOnApp2AppStreamListChange */
                /*856*/ { { {&d1field_equals_default,&d1set_field_to_default,1,Sid::Field::FLD_FIRST,Sid::Field::KIND_STRING, Msgapp2appOnApp2AppStreamListChange::END,0} } },
                /*857*/ { { {&d9field_equals_default,&d9set_field_to_default,2,0,Sid::Field::KIND_ENUM, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appOnApp2AppStreamListChange*>(base_address)->listType,0} } },
                /*858*/ { { {&d20field_equals_default,&d20set_field_to_default,3,Sid::Field::FLD_LIST,Sid::Field::KIND_STRING, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appOnApp2AppStreamListChange*>(base_address)->streams,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d20append),reinterpret_cast<void(*)(void*)>(&d20iterate),0,0,0,0,0}}},
                /*860*/ { { {&d16field_equals_default,&d16set_field_to_default,4,Sid::Field::FLD_LIST,Sid::Field::KIND_UINT, (unsigned short) (size_t) &reinterpret_cast<Msgapp2appOnApp2AppStreamListChange*>(base_address)->receivedSizes,0} } },
                { { { reinterpret_cast<bool(*)(const void*)>(&d16append),reinterpret_cast<void(*)(void*)>(&d16iterate),0,0,0,0,0}}},
        };
Sid::Field* Skype::get_property_desc(const uint& modid, const uint& propid) {
        switch (modid) {
        case 10:
                switch (propid) {
                case 151: return &SkypeFields[35];
                case 152: return &SkypeFields[36];
                case 153: return &SkypeFields[37];
                case 154: return &SkypeFields[38];
                case 155: return &SkypeFields[39];
                default: break;
                }
                break;
        case 2:
                switch (propid) {
                case 4: return &SkypeFields[90];
                case 5: return &SkypeFields[91];
                case 6: return &SkypeFields[92];
                case 7: return &SkypeFields[93];
                case 8: return &SkypeFields[94];
                case 9: return &SkypeFields[95];
                case 10: return &SkypeFields[96];
                case 11: return &SkypeFields[97];
                case 12: return &SkypeFields[98];
                case 13: return &SkypeFields[99];
                case 14: return &SkypeFields[100];
                case 15: return &SkypeFields[101];
                case 16: return &SkypeFields[102];
                case 17: return &SkypeFields[103];
                case 18: return &SkypeFields[104];
                case 19: return &SkypeFields[105];
                case 20: return &SkypeFields[106];
                case 21: return &SkypeFields[107];
                case 22: return &SkypeFields[108];
                case 23: return &SkypeFields[109];
                case 25: return &SkypeFields[110];
                case 26: return &SkypeFields[111];
                case 27: return &SkypeFields[112];
                case 28: return &SkypeFields[113];
                case 29: return &SkypeFields[114];
                case 33: return &SkypeFields[115];
                case 34: return &SkypeFields[116];
                case 35: return &SkypeFields[117];
                case 36: return &SkypeFields[118];
                case 37: return &SkypeFields[119];
                case 39: return &SkypeFields[120];
                case 41: return &SkypeFields[121];
                case 42: return &SkypeFields[122];
                case 180: return &SkypeFields[123];
                case 182: return &SkypeFields[124];
                case 183: return &SkypeFields[125];
                case 184: return &SkypeFields[126];
                case 185: return &SkypeFields[127];
                case 186: return &SkypeFields[128];
                case 187: return &SkypeFields[129];
                case 188: return &SkypeFields[130];
                case 189: return &SkypeFields[131];
                case 202: return &SkypeFields[132];
                case 205: return &SkypeFields[133];
                default: break;
                }
                break;
        case 1:
                switch (propid) {
                case 200: return &SkypeFields[199];
                default: break;
                }
                break;
        case 19:
                switch (propid) {
                case 930: return &SkypeFields[232];
                case 931: return &SkypeFields[233];
                case 932: return &SkypeFields[234];
                case 933: return &SkypeFields[235];
                case 934: return &SkypeFields[236];
                case 935: return &SkypeFields[237];
                case 936: return &SkypeFields[238];
                case 938: return &SkypeFields[239];
                case 939: return &SkypeFields[240];
                case 941: return &SkypeFields[241];
                case 942: return &SkypeFields[242];
                case 943: return &SkypeFields[243];
                case 947: return &SkypeFields[244];
                case 948: return &SkypeFields[245];
                case 949: return &SkypeFields[246];
                case 950: return &SkypeFields[247];
                case 951: return &SkypeFields[248];
                case 952: return &SkypeFields[249];
                case 953: return &SkypeFields[250];
                case 954: return &SkypeFields[251];
                case 955: return &SkypeFields[252];
                default: break;
                }
                break;
        case 18:
                switch (propid) {
                case 902: return &SkypeFields[368];
                case 903: return &SkypeFields[369];
                case 904: return &SkypeFields[370];
                case 906: return &SkypeFields[371];
                case 907: return &SkypeFields[372];
                case 909: return &SkypeFields[373];
                case 910: return &SkypeFields[374];
                case 911: return &SkypeFields[375];
                case 913: return &SkypeFields[376];
                case 914: return &SkypeFields[377];
                case 915: return &SkypeFields[378];
                case 918: return &SkypeFields[379];
                case 919: return &SkypeFields[380];
                case 920: return &SkypeFields[381];
                case 921: return &SkypeFields[382];
                case 922: return &SkypeFields[383];
                case 924: return &SkypeFields[384];
                case 925: return &SkypeFields[385];
                case 927: return &SkypeFields[386];
                case 928: return &SkypeFields[387];
                case 970: return &SkypeFields[388];
                case 971: return &SkypeFields[389];
                case 972: return &SkypeFields[390];
                case 973: return &SkypeFields[391];
                case 974: return &SkypeFields[392];
                case 975: return &SkypeFields[393];
                case 976: return &SkypeFields[394];
                case 977: return &SkypeFields[395];
                case 979: return &SkypeFields[396];
                case 980: return &SkypeFields[397];
                case 981: return &SkypeFields[398];
                case 996: return &SkypeFields[399];
                default: break;
                }
                break;
        case 9:
                switch (propid) {
                case 120: return &SkypeFields[438];
                case 121: return &SkypeFields[439];
                case 122: return &SkypeFields[440];
                case 123: return &SkypeFields[441];
                case 125: return &SkypeFields[442];
                case 126: return &SkypeFields[443];
                case 127: return &SkypeFields[444];
                case 222: return &SkypeFields[445];
                case 223: return &SkypeFields[446];
                case 790: return &SkypeFields[447];
                case 792: return &SkypeFields[448];
                case 960: return &SkypeFields[449];
                case 961: return &SkypeFields[450];
                case 962: return &SkypeFields[451];
                case 963: return &SkypeFields[452];
                case 964: return &SkypeFields[453];
                case 966: return &SkypeFields[454];
                case 968: return &SkypeFields[455];
                case 982: return &SkypeFields[456];
                default: break;
                }
                break;
        case 11:
                switch (propid) {
                case 130: return &SkypeFields[489];
                case 131: return &SkypeFields[490];
                case 132: return &SkypeFields[491];
                case 133: return &SkypeFields[492];
                case 134: return &SkypeFields[493];
                case 1104: return &SkypeFields[494];
                case 1105: return &SkypeFields[495];
                default: break;
                }
                break;
        case 7:
                switch (propid) {
                case 100: return &SkypeFields[539];
                case 101: return &SkypeFields[540];
                case 102: return &SkypeFields[541];
                case 103: return &SkypeFields[542];
                case 104: return &SkypeFields[543];
                case 105: return &SkypeFields[544];
                case 106: return &SkypeFields[545];
                case 107: return &SkypeFields[546];
                case 108: return &SkypeFields[547];
                case 109: return &SkypeFields[548];                case 830: return &SkypeFields[549];
                case 831: return &SkypeFields[550];
                default: break;
                }
                break;
        case 12:
                switch (propid) {
                case 48: return &SkypeFields[623];
                case 49: return &SkypeFields[624];
                case 190: return &SkypeFields[625];
                case 191: return &SkypeFields[626];
                case 192: return &SkypeFields[627];
                case 193: return &SkypeFields[628];
                case 194: return &SkypeFields[629];
                case 195: return &SkypeFields[630];
                case 196: return &SkypeFields[631];
                case 197: return &SkypeFields[632];
                case 198: return &SkypeFields[633];
                case 199: return &SkypeFields[634];
                case 840: return &SkypeFields[635];
                case 1211: return &SkypeFields[636];
                default: break;
                }
                break;
        case 6:
                switch (propid) {
                case 80: return &SkypeFields[650];
                case 81: return &SkypeFields[651];
                case 82: return &SkypeFields[652];
                case 83: return &SkypeFields[653];
                case 84: return &SkypeFields[654];
                case 85: return &SkypeFields[655];
                case 86: return &SkypeFields[656];
                case 87: return &SkypeFields[657];
                case 88: return &SkypeFields[658];
                case 89: return &SkypeFields[659];
                case 90: return &SkypeFields[660];
                case 91: return &SkypeFields[661];
                case 92: return &SkypeFields[662];
                case 93: return &SkypeFields[663];
                case 98: return &SkypeFields[664];
                default: break;
                }
                break;
        case 5:
                switch (propid) {
                case 4: return &SkypeFields[719];
                case 5: return &SkypeFields[720];
                case 7: return &SkypeFields[721];
                case 8: return &SkypeFields[722];
                case 9: return &SkypeFields[723];
                case 10: return &SkypeFields[724];
                case 11: return &SkypeFields[725];
                case 12: return &SkypeFields[726];
                case 13: return &SkypeFields[727];
                case 14: return &SkypeFields[728];
                case 15: return &SkypeFields[729];
                case 16: return &SkypeFields[730];
                case 17: return &SkypeFields[731];
                case 18: return &SkypeFields[732];
                case 19: return &SkypeFields[733];
                case 26: return &SkypeFields[734];
                case 27: return &SkypeFields[735];
                case 28: return &SkypeFields[736];
                case 34: return &SkypeFields[737];
                case 37: return &SkypeFields[738];
                case 70: return &SkypeFields[739];
                case 71: return &SkypeFields[740];
                case 72: return &SkypeFields[741];
                case 73: return &SkypeFields[742];
                case 74: return &SkypeFields[743];
                case 75: return &SkypeFields[744];
                case 76: return &SkypeFields[745];
                case 77: return &SkypeFields[746];
                case 78: return &SkypeFields[747];
                case 79: return &SkypeFields[748];
                case 160: return &SkypeFields[749];
                case 161: return &SkypeFields[750];
                case 162: return &SkypeFields[751];
                case 163: return &SkypeFields[752];
                case 164: return &SkypeFields[753];
                case 165: return &SkypeFields[754];
                case 166: return &SkypeFields[755];
                case 168: return &SkypeFields[756];
                case 169: return &SkypeFields[757];
                case 182: return &SkypeFields[758];
                case 183: return &SkypeFields[759];
                case 205: return &SkypeFields[760];
                case 773: return &SkypeFields[761];
                case 800: return &SkypeFields[762];
                case 801: return &SkypeFields[763];
                case 802: return &SkypeFields[764];
                case 804: return &SkypeFields[765];
                default: break;
                }
                break;
        }
        return 0;
};
#       ifdef SKYPE_SID_ADDLABELS_IN_DESCRIPTORS
        const char* SkypeNames[] = {
                "version",
                "timestamp",
                "started",
                "objectID",
                "name",
                "objectID",
                "result",
                "objectID",
                "conversations",
                "objectID",
                "conversation",
                "result",
                "objectID",
                "conversation",
                "objectID",
                "result",
                "objectID",
                "conversation",
                "objectID",
                "conversation",
                "objectID",
                "contacts",
                "objectID",
                "contact",
                "result",
                "objectID",
                "contact",
                "objectID",
                "result",
                "objectID",
                "contact",
                "objectID",
                "contact",
                "given_displayname",
                "nrofcontacts",
                "nrofcontacts_online",
                "custom_group_id",
                "type",
                "type",
                "contactGroup",
                "groups",
                "group",
                "group",
                "objectID",
                "identity",
                "objectID",
                "present",
                "avatar",
                "objectID",
                "email",
                "objectID",
                "company",
                "objectID",
                "group",
                "result",
                "objectID",
                "groupType",
                "result",
                "objectID",
                "blocked",
                "abuse",
                "objectID",
                "objectID",
                "name",
                "objectID",
                "isMyBuddy",
                "syncAuth",
                "objectID",
                "message",
                "extras_bitmask",
                "objectID",
                "result",
                "objectID",
                "num",
                "label",
                "number",
                "objectID",
                "conversation",
                "objectID",
                "capability",
                "queryServer",
                "result",
                "objectID",
                "capability",
                "queryServer",
                "status",
                "objectID",
                "skypename",
                "fullname",
                "pstnnumber",
                "birthday",
                "gender",
                "languages",
                "country",
                "province",
                "city",
                "phone_home",
                "phone_office",
                "phone_mobile",
                "emails",
                "homepage",
                "about",
                "profile_timestamp",
                "received_authrequest",
                "displayname",
                "refreshing",
                "given_authlevel",
                "authreq_timestamp",
                "mood_text",
                "timezone",
                "nrof_authed_buddies",
                "ipcountry",
                "given_displayname",
                "availability",
                "lastonline_timestamp",
                "capabilities",
                "avatar_image",
                "lastused_timestamp",
                "authrequest_count",
                "popularity_ord",
                "assigned_comment",
                "avatar_timestamp",
                "mood_timestamp",
                "assigned_phone1",
                "assigned_phone1_label",
                "assigned_phone2",
                "assigned_phone2_label",
                "assigned_phone3",
                "assigned_phone3_label",
                "type",
                "rich_mood_text",
                "identity",
                "type",
                "identity",
                "contact",
                "number",
                "found",
                "contact",
                "foundInKey",
                "identity",
                "type",
                "identityA",
                "identityB",
                "result",
                "original",
                "isNewSkypeName",
                "result",
                "normalized",
                "original",
                "countryPrefix",
                "result",
                "normalized",
                "contact",
                "contact",
                "objectID",
                "min_age_in_years",
                "add_to_subs",
                "valid",
                "objectID",
                "max_age_in_years",
                "add_to_subs",
                "valid",
                "objectID",
                "email",
                "add_to_subs",
                "valid",
                "objectID",
                "language",
                "add_to_subs",
                "valid",
                "objectID",
                "prop",
                "cond",
                "value",
                "add_to_subs",
                "valid",
                "objectID",
                "prop",
                "cond",
                "value",
                "add_to_subs",
                "valid",
                "objectID",
                "objectID",
                "result",
                "objectID",
                "objectID",
                "objectID",
                "objectID",
                "from",
                "count",
                "contacts",
                "objectID",
                "contact",
                "rankValue",
                "contact_search_status",
                "rangeList",
                "search",
                "text",
                "search",
                "identity",
                "search",
                "objectID",
                "rank",
                "result",
                "objectID",
                "rank",
                "objectID",
                "identityToUse",
                "videoCall",
                "nrofRedials",
                "redialPeriod",
                "autoStartVM",
                "origin",
                "objectID",
                "objectID",
                "identityToUse",
                "objectID",
                "video",
                "objectID",
                "objectID",
                "objectID",
                "videos",
                "objectID",
                "dtmf",
                "objectID",
                "convo_id",
                "identity",
                "rank",
                "requested_rank",
                "text_status",
                "voice_status",
                "video_status",
                "live_price_for_me",
                "live_start_timestamp",
                "sound_level",
                "debuginfo",
                "live_identity",
                "last_voice_error",
                "live_fwd_identities",
                "quality_problems",
                "live_type",
                "live_country",
                "transferred_by",
                "transferred_to",
                "adder",
                "last_leavereason",
                "objectID",
                "propKey",
                "value",
                "objectID",
                "topic",
                "isXML",
                "objectID",
                "guidelines",
                "isXML",
                "objectID",
                "jpeg",
                "objectID",
                "identitiesToAdd",
                "conference",
                "objectID",
                "identities",
                "objectID",
                "otherConversation",
                "conversation",
                "objectID",
                "accessToken",
                "objectID",
                "identities",
                "videoCall",
                "origin",
                "objectID",
                "objectID",
                "objectID",
                "objectID",
                "objectID",
                "postVoiceAutoresponse",
                "objectID",                "objectID",
                "identities",
                "transferTopic",
                "context",
                "objectID",
                "identity",
                "result",
                "objectID",
                "dtmf",
                "lengthInMS",
                "objectID",
                "objectID",
                "status",
                "objectID",
                "text",
                "isXML",
                "message",
                "objectID",
                "contacts",
                "objectID",
                "paths",
                "body",
                "error_code",
                "error_file",
                "objectID",
                "voicemail",
                "body",
                "objectID",
                "sms",
                "body",
                "objectID",
                "blob",
                "objectID",
                "objectID",
                "password",
                "objectID",
                "password",
                "hint",
                "objectID",
                "objectID",
                "objectID",
                "name",
                "objectID",
                "bookmark",
                "objectID",
                "alertString",
                "objectID",
                "objectID",
                "timestamp",
                "objectID",
                "timestamp",
                "also_unconsume",
                "objectID",
                "objectID",
                "group",
                "result",
                "objectID",
                "filter",
                "participants",
                "objectID",
                "requireTimestamp",
                "contextMessages",
                "unconsumedMessages",
                "objectID",
                "text",
                "fromTimestampUp",
                "message",
                "objectID",
                "sendVideo",
                "objectID",
                "objectID",
                "message",
                "objectID",
                "spawned",
                "type",
                "creator",
                "creation_timestamp",
                "opt_entry_level_rank",
                "opt_disclose_history",
                "opt_admin_only_activities",
                "meta_name",
                "meta_topic",
                "meta_guidelines",
                "meta_picture",
                "spawned_from_convo_id",
                "live_host",
                "my_status",
                "alert_string",
                "is_bookmarked",
                "opt_joining_enabled",
                "displayname",
                "given_displayname",
                "local_livestatus",
                "inbox_timestamp",
                "unconsumed_messages_voice",
                "active_vm_id",
                "identity",
                "inbox_message_id",
                "live_start_timestamp",
                "unconsumed_suppressed_messages",
                "unconsumed_normal_messages",
                "unconsumed_elevated_messages",
                "consumption_horizon",
                "passwordhint",
                "last_activity_timestamp",
                "live_is_muted",
                "conference",
                "convoIdentity",
                "matchPSTN",
                "conversation",
                "participantIdentities",
                "createIfNonExisting",
                "ignoreBookmarkedOrNamed",
                "conversation",
                "joinBlob",
                "alsoJoin",
                "conversation",
                "type",
                "conversations",
                "conversation",
                "type",
                "added",
                "objectID",
                "result",
                "objectID",
                "result",
                "objectID",
                "newText",
                "isXML",
                "undo",
                "legacyPrefix",
                "objectID",
                "contacts",
                "objectID",
                "transfers",
                "objectID",
                "voicemail",
                "objectID",
                "sms",
                "objectID",
                "convo_guid",
                "timestamp",
                "author",
                "author_displayname",
                "identities",
                "leavereason",
                "body_xml",
                "edited_by",
                "edit_timestamp",
                "originally_meant_for",
                "guid",
                "convo_id",
                "type",
                "sending_status",
                "param_key",
                "param_value",
                "reason",
                "consumption_status",
                "participant_count",
                "guid",
                "message",
                "type",
                "latestPerConvOnly",
                "fromTimestampInc",
                "toTimestampExc",
                "messages",
                "message",
                "changesInboxTimestamp",
                "supersedesHistoryMessage",
                "conversation",
                "objectID",
                "objectID",
                "objectID",
                "x0",
                "y0",
                "width",
                "height",
                "monitorNumber",
                "windowHandle",
                "objectID",
                "id",
                "objectID",
                "mediaType",
                "webcamName",
                "devicePath",
                "updateSetup",
                "objectID",
                "mediatype",
                "deviceName",
                "devicePath",
                "status",
                "error",
                "debuginfo",
                "dimensions",
                "media_type",
                "convo_id",
                "device_path",
                "deviceNames",
                "devicePaths",
                "count",
                "deviceName",
                "devicePath",
                "cap",
                "result",
                "deviceName",
                "devicePath",
                "type",
                "deviceName",
                "devicePath",
                "video",
                "type",
                "deviceName",
                "devicePath",
                "video",
                "type",
                "deviceName",
                "devicePath",
                "video",
                "command",
                "response",
                "withUser",
                "excludeNetworkTest",
                "withUser",
                "justStop",
                "result",
                "testType",
                "testResult",
                "withUser",
                "details",
                "xmlDetails",
                "objectID",
                "objectID",
                "objectID",
                "objectID",
                "objectID",
                "objectID",
                "objectID",
                "result",
                "type",
                "partner_handle",
                "partner_dispname",
                "status",
                "failurereason",
                "subject",
                "timestamp",
                "duration",
                "allowed_duration",
                "playback_progress",
                "convo_id",
                "chatmsg_guid",
                "skypeName",
                "greeting",
                "soundid",
                "sound",
                "loop",
                "useCallOutDevice",
                "soundid",
                "datafile",
                "loop",
                "useCallOutDevice",
                "result",
                "soundid",
                "recordAndPlaybackData",
                "handleList",
                "nameList",
                "productIdList",
                "handleList",
                "nameList",
                "productIdList",
                "callInDevice",
                "callOutDevice",
                "waveOutDevice",
                "deviceHandle",
                "interfaceString",
                "capabilities",                "micLevel",
                "speakerLevel",
                "command",
                "response",
                "volume",
                "volume",
                "micVolume",
                "volume",
                "muted",
                "muted",
                "mute",
                "mute",
                "media",
                "maxUplinkBps",
                "maxDownlinkBps",
                "objectID",
                "target",
                "status",
                "objectID",
                "target",
                "number",
                "objectID",
                "target",
                "type",
                "objectID",
                "target",
                "price",
                "objectID",
                "numbers",
                "success",
                "objectID",
                "text",
                "result",
                "chunks",
                "charsUntilNextChunk",
                "objectID",
                "textChunks",
                "charsUntilNextChunk",
                "is_failed_unseen",
                "price_precision",
                "type",
                "status",
                "failurereason",
                "price",
                "price_currency",
                "target_numbers",
                "target_statuses",
                "body",
                "timestamp",
                "reply_to_number",
                "chatmsg_id",
                "outgoing_reply_type",
                "type",
                "number",
                "sms",
                "number",
                "code",
                "sms",
                "sms",
                "objectID",
                "filenameWithPath",
                "success",
                "objectID",
                "objectID",
                "objectID",
                "type",
                "partner_handle",
                "partner_dispname",
                "status",
                "failurereason",
                "starttime",
                "finishtime",
                "filepath",
                "filename",
                "filesize",
                "bytestransferred",
                "bytespersecond",
                "chatmsg_guid",
                "chatmsg_index",
                "convo_id",
                "objectID",
                "status",
                "progress",
                "objectID",
                "setAvailabilityTo",
                "objectID",
                "password",
                "savePwd",
                "saveDataLocally",
                "objectID",
                "password",
                "savePwd",
                "saveDataLocally",
                "email",
                "allowSpam",
                "objectID",
                "clearSavedPwd",
                "objectID",
                "oldPassword",
                "newPassword",
                "savePwd",
                "objectID",
                "savePwd",
                "objectID",
                "propKey",
                "value",
                "objectID",
                "propKey",
                "value",
                "objectID",
                "objectID",
                "propKey",
                "value",
                "objectID",
                "propKey",
                "value",
                "objectID",
                "propKey",
                "value",
                "objectID",
                "availability",
                "objectID",
                "standby",
                "objectID",
                "capability",
                "status",
                "expiryTimestamp",
                "objectID",
                "skypenameHash",
                "objectID",
                "email",
                "objectID",
                "company",
                "objectID",
                "skypename",
                "fullname",
                "birthday",
                "gender",
                "languages",
                "country",
                "province",
                "city",
                "phone_home",
                "phone_office",
                "phone_mobile",
                "emails",
                "homepage",
                "about",
                "profile_timestamp",
                "mood_text",
                "timezone",
                "nrof_authed_buddies",
                "availability",
                "avatar_image",
                "status",
                "pwdchangestatus",
                "suggested_skypename",
                "logoutreason",
                "skypeout_balance_currency",
                "skypeout_balance",
                "skypein_numbers",
                "offline_callforward",
                "commitstatus",
                "cblsyncstatus",
                "chat_policy",
                "skype_call_policy",
                "pstn_call_policy",
                "avatar_policy",
                "buddycount_policy",
                "timezone_policy",
                "webpresence_policy",
                "phonenumbers_policy",
                "voicemail_policy",
                "avatar_timestamp",
                "mood_timestamp",
                "rich_mood_text",
                "partner_optedout",
                "service_provider_info",
                "registration_timestamp",
                "nr_of_other_instances",
                "skypeout_precision",
                "identity",
                "account",
                "accountNameList",
                "account",
                "fullname",
                "suggestedName",
                "value",
                "result",
                "freeBytesLeft",
                "propKey",
                "strValue",
                "forRegistration",
                "result",
                "freeBytesLeft",
                "username",
                "password",
                "result",
                "type",
                "port",
                "key",
                "value",
                "key",
                "value",
                "key",
                "value",
                "key",
                "value",
                "key",
                "value",
                "key",
                "value",
                "key",
                "value",
                "key",
                "key",
                "value",
                "languageCodeList",
                "languageNameList",
                "countryCodeList",
                "countryNameList",
                "countryPrefixList",
                "countryDialExampleList",
                "uiLanguageCodeList",
                "number",
                "countryCode",
                "appname",
                "result",
                "appname",
                "result",
                "appname",
                "skypename",
                "result",
                "appname",
                "stream",
                "result",
                "appname",
                "stream",
                "data",
                "result",
                "appname",
                "stream",
                "data",
                "result",
                "appname",
                "stream",
                "result",
                "data",
                "appname",
                "result",
                "users",
                "appname",
                "listType",
                "result",
                "streams",
                "receivedSizes",
                "appname",
                "stream",
                "data",
                "appname",
                "listType",
                "streams",
                "receivedSizes",
        };
#       endif /* SKYPE_SID_ADDLABELS_IN_DESCRIPTORS*/
#ifdef SE_USE_NAMESPACE
} // Skype
#endif
