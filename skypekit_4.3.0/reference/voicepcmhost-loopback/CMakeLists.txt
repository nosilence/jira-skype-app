project(voicepcmhost-loopback)
cmake_minimum_required(VERSION 2.8)

set(SRCS PCMLoopback.cpp
	SidPCMHostMainLoopback.cpp)

include_directories(${SID_CPP_INCLUDES})

link_libraries(skypekit-voicepcm skypekit-ipccpp skypekit-avtransport skypekit-cppwrapper_2_lib skypekit-cyassl_lib)

if (NOT WIN32)
	link_libraries(pthread)
endif ()

add_executable(voicepcmhost-loopback EXCLUDE_FROM_ALL ${SRCS})

install(TARGETS voicepcmhost-loopback DESTINATION bin OPTIONAL)

