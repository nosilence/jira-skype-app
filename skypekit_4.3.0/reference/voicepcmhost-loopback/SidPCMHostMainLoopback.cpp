#define SID_LOG_MASK 0xff

#include <stdio.h>
#include "sidg_pcmif_server.hpp"
#include "SidPCMInterface.hpp"
#include "SidProtocolEnums.hpp"
#include "sidg_pcmif_cb_client.hpp"
#include "SidDebugging.hpp"

#include "PCMLoopbackOptionsParser.hpp"

PCMLoopbackOptionsParser gParser;

int main(int argc, const char **argv) {
	if (gParser.ParseOptions(argc, argv) < 0 || gParser.m_Help) {
		gParser.Usage(argv[0]);
		return -1;
	}

	Sid::SkypePCMInterfaceServer *pcmif_server = new Sid::SkypePCMInterfaceServer();
	Sid::SkypePCMCallbackInterfaceClient *pcmif_cb_client = new Sid::SkypePCMCallbackInterfaceClient();

	SkypePCMInterface* pcmif = SkypePCMInterfaceGet(pcmif_cb_client);
	pcmif_server->set_if(pcmif);


	Sid::String fromskypekitkey;
	Sid::String toskypekitkey;

	fromskypekitkey.Format( "%spcm_from_skypekit_key", gParser.m_IpcPrefix);
	toskypekitkey.Format( "%spcm_to_skypekit_key", gParser.m_IpcPrefix);

	pcmif_server->Connect(fromskypekitkey.data(), 0);
	pcmif_cb_client->Connect(toskypekitkey.data(), 500);

	Sid::Protocol::Status status;
	do {
		status =pcmif_server->ProcessCommands();
	} while (status == Sid::Protocol::OK);

	SkypePCMInterfaceRelease(pcmif);
	pcmif_server->Disconnect();
	pcmif_cb_client->Disconnect();

	delete pcmif_server;
	delete pcmif_cb_client;

	printf("PCMServerTransport disconnected, exiting from pcmtesthost\n");
}
