
#include "cparticipant.h"

CParticipant::CParticipant(unsigned int oid, SERootObject* rootobj)
                : Participant(oid, rootobj)
{
//         fprintf(stdout,"New participant oid %d\n", getOID());
}

void CParticipant::OnChange(int prop)
{
        String value = GetProp(prop);
        List_String dbg = getPropDebug(prop, value);
        String identity = GetStrProp(Participant::P_IDENTITY);
        fprintf(stdout,"PARTICIPANT.%s:%s = %s\n", 
                (const char*)identity, 
                (const char*)dbg[1], 
                (const char*)dbg[2]);
        fflush(stdout);
}

void CParticipant::OnIncomingDTMF(const DTMF& dtmf)
{
        String identity = GetStrProp(Participant::P_IDENTITY);
        fprintf(stdout,"PARTICIPANT.%s.OnIncomingDTMF dtmf = %d\n", 
               (const char*)identity, (int)dtmf);
        fflush(stdout);
}

void CParticipant::OnLiveSessionVideosChanged()
{
        String identity = GetStrProp(Participant::P_IDENTITY);
        fprintf(stdout,"PARTICIPANT.%s.OnLiveSessionVideosChanged()\n",
               (const char*)identity);
        fflush(stdout);

        VideoRefs videos;
        if(GetLiveSessionVideos(videos))
        {
                // only allowing one video at a time
                m_video = videos[0];
                m_video.fetch();
        }
}
