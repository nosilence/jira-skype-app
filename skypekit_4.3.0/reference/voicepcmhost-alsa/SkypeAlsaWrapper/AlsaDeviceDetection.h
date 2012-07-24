#pragma once

#include <alsa/asoundlib.h>
#include "AlsaBackend.h"

/* For new detection */
int sc_errcheck(int retval, const char *doingwhat, int cardnr, int devnr);

int errcheck(int retval, const char *doingwhat);

/* Disregard some standard plugins - for classic detection */
int IgnorePlugin(const char* plugin);

bool PcmPreProbe(DeviceInfo& info, StreamDirection dir);

/* Probe hardware device for INPUT or OUTPUT */
bool DevProbe(snd_ctl_t* handle, snd_pcm_info_t* pcminfo, int card, int dev, StreamDirection dir);
