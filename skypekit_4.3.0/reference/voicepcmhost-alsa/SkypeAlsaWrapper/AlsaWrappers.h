/* kate: replace-tabs on; tab-size 4; */
// RAII wrappers around ALSA interfaces.
#pragma once

#include <alsa/asoundlib.h>

/**
 * FIXME document me!
 */
class SndHctl
{
    snd_hctl_t *p;

public:
    SndHctl() : p(0) {}
    ~SndHctl() {
        if (p) snd_hctl_close(p);
    }

    inline bool open(const char *ctlname) {
        return snd_hctl_open(&p, ctlname, SND_CTL_NONBLOCK) >= 0;
    }

    inline bool load() {
        if (!p) return false;
        return snd_hctl_load(p) >= 0;
    }

    inline int elementCount() {
        return snd_hctl_get_count(p);
    }

    inline operator snd_hctl_t*() { return p; }
    inline operator const snd_hctl_t*() const { return p; }
};

/**
* FIXME document me!
*/
class SndHctlElem
{
    snd_hctl_elem_t *p;

public:
    inline void first(SndHctl& hctl) { p = snd_hctl_first_elem(hctl); }
    inline void next() { p = snd_hctl_elem_next(p); }

    inline operator snd_hctl_elem_t*() { return p; }
    inline operator const snd_hctl_elem_t*() const { return p; }
};

/**
* FIXME document me!
*/
class SndCtlElemId
{
    snd_ctl_elem_id_t *p;
    int *ref;

public:
    inline SndCtlElemId() : p(0), ref(new int) { *ref = 1; snd_ctl_elem_id_malloc(&p); clear(); }
    inline ~SndCtlElemId() { if (0 == --*ref) { delete ref; snd_ctl_elem_id_free(p); } }
    inline SndCtlElemId(const SndCtlElemId &rhs) : p(rhs.p), ref(rhs.ref) { ++*ref; }
    inline SndCtlElemId &operator=(const SndCtlElemId &rhs) { if (0 == --*ref) { delete ref; snd_ctl_elem_id_free(p); } p = rhs.p; ref = rhs.ref; ++*ref; return *this; }
    inline operator snd_ctl_elem_id_t*() { return p; }
    inline operator const snd_ctl_elem_id_t*() const { return p; }

    inline void clear() { snd_ctl_elem_id_clear(p); }
    inline void setName(const char *val) { snd_ctl_elem_id_set_name(p, val); }
    inline void setInterface(snd_ctl_elem_iface_t val) { snd_ctl_elem_id_set_interface(p, val); }

    inline void copy(SndCtlElemId *id) const { snd_ctl_elem_id_copy(*id, p); }

    inline unsigned int numid() const { return snd_ctl_elem_id_get_numid(p); }
    inline snd_ctl_elem_iface_t interface() const { return snd_ctl_elem_id_get_interface(p); }
    inline unsigned int device() const { return snd_ctl_elem_id_get_device(p); }
    inline unsigned int subdevice() const { return snd_ctl_elem_id_get_subdevice(p); }
    inline const char *name() const { return snd_ctl_elem_id_get_name(p); }
    inline unsigned int index() const { return snd_ctl_elem_id_get_index(p); }

    inline void setNumid(unsigned int val) { snd_ctl_elem_id_set_numid(p, val); }
    inline void setDevice(unsigned int val) { snd_ctl_elem_id_set_device(p, val); }
    inline void setSubdevice(unsigned int val) { snd_ctl_elem_id_set_subdevice(p, val); }
    inline void setIndex(unsigned int val) { snd_ctl_elem_id_set_index(p, val); }
};

/**
* FIXME document me!
*/
class SndCtlElemValue
{
    snd_ctl_elem_value_t *p;
    int *ref;

public:
    inline SndCtlElemValue() : p(0), ref(new int) { *ref = 1; snd_ctl_elem_value_malloc(&p); clear(); }
    inline ~SndCtlElemValue() { if (0 == --*ref) { delete ref; snd_ctl_elem_value_free(p); } }
    inline SndCtlElemValue(const SndCtlElemValue &o) : p(o.p), ref(o.ref) { ++*ref; }
    inline SndCtlElemValue& operator=(const SndCtlElemValue& o) { if (0 == --*ref) { delete ref; snd_ctl_elem_value_free(p); } p = o.p; ref = o.ref; ++*ref; return *this; }
    inline operator snd_ctl_elem_value_t*() { return p; }
    inline operator const snd_ctl_elem_value_t*() const { return p; }

    inline void clear() { snd_ctl_elem_value_clear(p); }
    inline long integer(unsigned int idx) const { return snd_ctl_elem_value_get_integer(p, idx); }
    inline void setInteger(unsigned int idx, long val) { snd_ctl_elem_value_set_integer(p, idx, val); }
};

/**
* FIXME document me!
*/
class SndCtlElemInfo
{
    snd_ctl_elem_info_t *p;
    int *ref;

public:
    inline SndCtlElemInfo() : p(0), ref(new int) { *ref = 1; snd_ctl_elem_info_malloc(&p); clear(); }
    inline ~SndCtlElemInfo() { if (0 == --*ref) { delete ref; snd_ctl_elem_info_free(p); } }
    inline SndCtlElemInfo(const SndCtlElemInfo& rhs) : p(rhs.p), ref(rhs.ref) { ++*ref; }
    inline SndCtlElemInfo& operator=(const SndCtlElemInfo& rhs) { if (0 == --*ref) { delete ref; snd_ctl_elem_info_free(p); } p = rhs.p; ref = rhs.ref; ++*ref; return *this; }
    inline operator snd_ctl_elem_info_t*() { return p; }
    inline operator const snd_ctl_elem_info_t*() const { return p; }

    inline void clear() { snd_ctl_elem_info_clear(p); }
    inline snd_ctl_elem_type_t type() const { return snd_ctl_elem_info_get_type(p); }
    inline const char *name() const { return snd_ctl_elem_info_get_name(p); }
    inline long min() const { return snd_ctl_elem_info_get_min(p); }
    inline long max() const { return snd_ctl_elem_info_get_max(p); }
    inline long step() const { return snd_ctl_elem_info_get_step(p); }
    inline bool isReadable() const { return snd_ctl_elem_info_is_readable(p); }
    inline unsigned int count() const { return snd_ctl_elem_info_get_count(p); }
};

/**
 * Wrapper around mixer functions.
 */
class SndMixer
{
    snd_mixer_t *p;

public:
    SndMixer() : p(0) { snd_mixer_open(&p, 0); }
    ~SndMixer() { if (p) snd_mixer_close(p); }
};
