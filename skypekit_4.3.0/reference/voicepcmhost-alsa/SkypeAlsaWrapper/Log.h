#pragma once
#ifndef __ALSA_LOG_H__
#define __ALSA_LOG_H__
#ifdef _DEBUG
#define Log(...) fprintf(stderr, __VA_ARGS__); fprintf(stderr, "\n");
#else
#define Log(...) ;
#endif
#endif



