
#ifndef CVideo_INCLUDED_HPP
#define CVideo_INCLUDED_HPP

#include "skype-embedded_2.h"
#include "skype-object.h"

class CVideo : public Video
{
public:
        typedef DRef<CVideo, Video> Ref;
        typedef DRefs<CVideo, Video> Refs;

        CVideo(unsigned int oid, SERootObject* root);
        ~CVideo() {}

        void OnChange(int prop);
        bool WaitingToStart();
        void WaitingToStart(bool val);

private:
        bool m_waitingToStart;
};

#endif //CVideo_INCLUDED_HPP

