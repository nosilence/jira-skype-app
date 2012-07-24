# Itty bitty library, but not sure of this code's ultimate fate, so isolating it

project(skypekit-avtransport)

include_directories(${SID_CPP_INCLUDES})

set(SRCS SocketTransport.cpp)

if (WIN32)
	set(SRCS ${SRCS} 
		NamedPipes.cpp)
else ()
	set(SRCS ${SRCS}
		UnixSocket.cpp)
endif ()

add_library(skypekit-avtransport ${SRCS})

