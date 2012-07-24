cmake_minimum_required(VERSION 2.8)

project(skypealsawrapperlib)
set(ALSAWRAPPERSRCS ${DISTRO_ROOT}/reference/voicepcmhost-alsa/SkypeAlsaWrapper/AlsaDeviceDetection.cpp
	${DISTRO_ROOT}/reference/voicepcmhost-alsa/SkypeAlsaWrapper/AlsaFormat.cpp
	${DISTRO_ROOT}/reference/voicepcmhost-alsa/SkypeAlsaWrapper/AlsaBackend.cpp
	${DISTRO_ROOT}/reference/voicepcmhost-alsa/SkypeAlsaWrapper/AlsaHardwareParams.cpp
	${DISTRO_ROOT}/reference/voicepcmhost-alsa/SkypeAlsaWrapper/AlsaSoftwareParams.cpp
	${DISTRO_ROOT}/reference/voicepcmhost-alsa/SkypeAlsaWrapper/AlsaVolumeControl.cpp
    ${DISTRO_ROOT}/reference/voicepcmhost-alsa/SkypeAlsaWrapper/AudioStream.cpp
	${DISTRO_ROOT}/reference/voicepcmhost-alsa/SkypeAlsaWrapper/DeviceInfo.cpp
	${DISTRO_ROOT}/reference/voicepcmhost-alsa/SkypeAlsaWrapper/StreamSpec.cpp
)
add_library(SkypeAlsaWrapper SHARED ${ALSAWRAPPERSRCS})

project(voicepcmhost-alsa)
   
set(SRCS PCMHostAlsa.cpp)

include_directories(${SID_CPP_INCLUDES} ${DISTRO_ROOT}/reference/voicepcmhost-alsa/SkypeAlsaWrapper ${DISTRO_ROOT}/ipc/cpp/ssl/cyassl/include)

link_libraries(skypekit-voicepcm skypekit-ipccpp skypekit-avtransport skypekit-cppwrapper_2_lib skypekit-cyassl_lib SkypeAlsaWrapper)

if (NOT WIN32)
    set(__LINUX_ALSA__ TRUE)
	link_libraries(pthread asound)
else()
    set(__WINDOWS_DS__ TRUE)
    link_libraries(dsound.lib)
endif ()

add_executable(voicepcmhost-alsa ${SRCS})

install(TARGETS voicepcmhost-alsa DESTINATION bin OPTIONAL)

