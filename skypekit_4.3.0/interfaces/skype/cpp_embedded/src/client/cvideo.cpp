
#include "cvideo.h"

CVideo::CVideo(unsigned int oid, SERootObject* rootobj)
                : Video(oid, rootobj), m_waitingToStart(false)
{
        fprintf(stdout,"New video oid %d\n", getOID());
        fflush(stdout);
}

void CVideo::OnChange(int prop)
{
        String value = GetProp(prop);
        List_String dbg = getPropDebug(prop, value);
        
        fprintf(stdout,"Video.%s = %s\n", 
                (const char*)dbg[1], 
                (const char*)dbg[2]);
        fflush(stdout);

        if (prop == P_STATUS) {
                if ((Video::STATUS)GetUintProp(Video::P_STATUS) == Video::AVAILABLE && WaitingToStart()) {
                        Start();
                        WaitingToStart(false);
                }
        }
}

bool CVideo::WaitingToStart()
{
        return m_waitingToStart;
}

void CVideo::WaitingToStart(bool val)
{
        m_waitingToStart = val;
}
