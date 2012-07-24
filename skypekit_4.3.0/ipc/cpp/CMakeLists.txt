project(skypekit-ipccpp)

set(IPCCPP_SRCS SidApi.cpp
	SidAVDummyHelpers.cpp
	SidAVServer.cpp
	SidAVTransportWrapper.cpp
	SidClientConnection.cpp
	SidClientSession.cpp
	SidCommandProcessor.cpp
	SidConcurrentCircularBuffer.cpp
	SidConnection.cpp
	SidDebugging.cpp
	SidField.cpp
	SidListener.cpp
	SidMsgQueueConnection.cpp
	SidProtocolBinClient.cpp
	SidProtocolBinCommon.cpp
	SidProtocolBinServer.cpp
	SidProtocolFactory.cpp
	SidServerConnection.cpp
	SidServerSession.cpp
	SidSession.cpp
	SidTLSEncryption.cpp
	SidTransportLog.cpp
)

include_directories(${SID_CPP_INCLUDES})

add_subdirectory(platform/testing EXCLUDE_FROM_ALL)
add_subdirectory(AVTransport)

add_library(skypekit-ipccpp ${IPCCPP_SRCS})

