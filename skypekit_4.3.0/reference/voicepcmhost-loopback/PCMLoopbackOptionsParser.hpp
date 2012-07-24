#ifndef __PCMLOOPBACK_OPTIONS_PARSER_HPP__
#define __PCMLOOPBACK_OPTIONS_PARSER_HPP__


#include "sidg_pcmif_server.hpp"
#include "SidDebugging.hpp"


struct PCMLoopbackOptionsParser {
	PCMLoopbackOptionsParser() :
		m_IpcPrefix(IPC_PREFIX_DEFAULT),
		m_forceInputChannels(-1),
		m_forceOutputChannels(-1),
		m_forceSampleRate(-1),
		m_delay(-1),
		m_Help(false) {
	}
	void Usage(const char *executable) {
		SID_INFO("usage:");
		SID_INFO("	%s [options]", executable);
		SID_INFO("	options:");
		SID_INFO("		-h           - print help");
		SID_INFO("		-s prefix    - path prefix for ipc key. default %s", IPC_PREFIX_DEFAULT);
		SID_INFO("		-f frequency - force sampling frequency");
		SID_INFO("		-ci channels - force number of input channels");
		SID_INFO("		-co channels - force number of output channels");
		SID_INFO("		-d delay     - delay of the loopback signal, possible values: 0 - 2000 ms, default: 200 ms");
	}
	int ParseOptions(int argc, const char **argv) {
		for (int i = 1; i < argc; i++) {
			const char c = argv[i][0];
			if (c == '-' || c == '/') {
				switch (argv[i][1]) {
				case 'h': {
					m_Help = true;
					break;
				}
				case 's': {
					if (i + 1 >= argc)
						return -1;
					m_IpcPrefix = argv[++i];
					break;
				}
				case 'f': {
					if (i + 1 >= argc)
						return -1;
					m_forceSampleRate = atoi(argv[++i]);
					if(m_forceSampleRate > 48000 || m_forceSampleRate < 100 || m_forceSampleRate % 100)
						return -1;
					break;
				}
				case 'c': {
					int* p;
					if(argv[i][2] == 'i')
						p = &m_forceInputChannels;
					else if(argv[i][2] == 'o')
						p = &m_forceOutputChannels;
					else
						return -1;
					if (i + 1 >= argc)
						return -1;
					*p = atoi(argv[++i]);
					if(*p != 1 && *p != 2)
						return -1;
					break;
					*p = atoi(argv[++i]);
					if(*p != 1 && *p != 2)
						return -1;
					break;
				}
				case 'd': {
					if (i + 1 >= argc)
						return -1;
					m_delay = atoi(argv[++i]);
					if(m_delay > 2000 || m_delay < 0)
						return -1;
					break;
				}
				default: {
					return -1;
					break;
				}
				}
			} else {
				return -1;
			}
		}

		return 0;
	}
	const char *m_IpcPrefix;
	int m_forceInputChannels;
	int m_forceOutputChannels;
	int m_forceSampleRate;
	int m_delay;
	bool m_Help;
};


extern PCMLoopbackOptionsParser gParser;

#endif
