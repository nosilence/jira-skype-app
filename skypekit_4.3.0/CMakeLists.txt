CMAKE_MINIMUM_REQUIRED(VERSION 2.8.2)

PROJECT(skypekit)

ENABLE_TESTING()
INCLUDE(CTest)

SET(DISTRO_ROOT ${CMAKE_CURRENT_LIST_DIR})
SET(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${DISTRO_ROOT}/cmake_modules)

# !bwc! A bit of a hack; below we are setting the valid values of this variable,
# and this information is automatically displayed by the cmake gui, but is
# ignored by cmake -i and ccmake. So, we manually insert this info into the help
# text. I have filed a feature request (13030) in cmake's bug-tracker, and 
# perhaps they will implement it. If so, we can simplify this.

set(VALID_APPS all wrappers cppwrapper javawrapper uikit)
SET(APPS_TO_BUILD "all" CACHE STRING "Select apps to build. (Valid values include ${VALID_APPS}, you can select more than one)")

IF (NOT OUTPUT_DIR)
    SET(OUTPUT_DIR ${CMAKE_CURRENT_SOURCE_DIR})
ENDIF()

INCLUDE(${CMAKE_CURRENT_LIST_DIR}/buildfiles/options.cmake)
INCLUDE(OptionalAddSubdirectory)

#MACRO_OPTIONAL_ADD_SUBDIRECTORY(runtime)

# Ugly. CMake's "test" target is not very useful; it does not actually build 
# any tests. So, we add an autotools-like "check" target that will build the
# test code, and then run it.
add_custom_target(check COMMAND ${CMAKE_CTEST_COMMAND})
add_custom_target(memcheck COMMAND ${CMAKE_CTEST_COMMAND} -T memcheck)
add_custom_target(tests)
add_dependencies(check tests)
add_dependencies(memcheck tests)

SET(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${DISTRO_ROOT}/bin/${CMAKE_SYSTEM_NAME}-${CMAKE_SYSTEM_PROCESSOR})
SET(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${DISTRO_ROOT}/lib/${CMAKE_SYSTEM_NAME}-${CMAKE_SYSTEM_PROCESSOR})

set(VALID_FRAMEWORKS SE POCO QT)
set(FRAMEWORK SE CACHE STRING "Select types framework to use when building cpp_wrapper (experimental, valid values include ${VALID_FRAMEWORKS})")
set_property(CACHE FRAMEWORK PROPERTY STRINGS ${VALID_FRAMEWORKS})
mark_as_advanced(FRAMEWORK)

FUNCTION(BUILD_SKYPEKIT_CLIENT_CPP)
    # Perhaps it would be best to put this path-setup stuff in cmake_modules somewhere?
    # Ideally, everything in this source tree would include from the root, so
    # we would need very little include setup. This is not the case however, so
    # we try to minimize the damage here. 
    SET(SID_CPP_INCLUDES ${DISTRO_ROOT}/ipc/cpp
            ${DISTRO_ROOT}/ipc/cpp/AVTransport
            ${DISTRO_ROOT}/ipc/cpp/ssl/cyassl/include
            ${DISTRO_ROOT}/ipc/cpp/ssl/cyassl/ctaocrypt/include
            ${DISTRO_ROOT}/interfaces/skype/cpp_embedded/src/platform/threading
            ${DISTRO_ROOT}/interfaces/voicepcm)

    if (WIN32)
        set(SID_CPP_INCLUDES ${SID_CPP_INCLUDES} ${DISTRO_ROOT}/interfaces/skype/cpp_embedded/src/platform/threading/win)
    else (WIN32)
        set(SID_CPP_INCLUDES ${SID_CPP_INCLUDES} ${DISTRO_ROOT}/interfaces/skype/cpp_embedded/src/platform/threading/pthread)
    endif (WIN32)
    
    if (FRAMEWORK MATCHES "SE")
        set(SID_CPP_INCLUDES ${SID_CPP_INCLUDES}
                             ${DISTRO_ROOT}/interfaces/skype/cpp_embedded/src/types
                             ${DISTRO_ROOT}/ipc/cpp/platform/se)
    elseif (FRAMEWORK matches "POCO")
        set(SID_CPP_INCLUDES ${SID_CPP_INCLUDES} ${DISTRO_ROOT}/ipc/cpp/poco)
    elseif (FRAMEWORK matches "QT")
        set(SID_CPP_INCLUDES ${SID_CPP_INCLUDES} 
                            ${DISTRO_ROOT}/ipc/qt
                            /usr/inlcude/qt4)
    else ()
        MESSAGE(ERROR "Invalid framework specified")
    endif ()

    ADD_SUBDIRECTORY(interfaces/skype/cpp_embedded)

    # Not strictly necessary for the cpp client; none of this will kick in
    # unless we try to make something in reference that needs this stuff
    # (right now, the only such thing is voicepcm-loopback) 
    ADD_SUBDIRECTORY(ipc/cpp EXCLUDE_FROM_ALL)
    ADD_SUBDIRECTORY(interfaces/voicepcm EXCLUDE_FROM_ALL)
    ADD_SUBDIRECTORY(interfaces/videortp/videohost_ipc EXCLUDE_FROM_ALL)
    ADD_SUBDIRECTORY(reference/voicepcmhost-loopback)

    # passthrough to the premake-based reference engine builds; we will be 
    # migrating these to cmake in the near future, this is just so they're
    # invoked from a common point, otherwise our ANT build scripts get messy.
    SET(PREMAKE_REFERENCE_ENGINES
        videortphost-imx27
        videortphost-loopback
        videortphost-test
        voicepcmhost-alsa
        voicepcmhost-file
        voicepcmhost-rtaudio
        voicertphost-loopback
        gstreamer-media-engine)

    FOREACH(engine ${PREMAKE_REFERENCE_ENGINES})
        ADD_CUSTOM_TARGET(${engine} make -C ${DISTRO_ROOT}/reference REF_ENGINE_NAMES=\"${engine}\")
        ADD_CUSTOM_TARGET(${engine}_install make -C ${DISTRO_ROOT}/reference REF_ENGINE_NAMES=\"${engine}\" install
                          DEPENDS ${engine})
        ADD_CUSTOM_TARGET(${engine}_clean make -C ${DISTRO_ROOT}/reference REF_ENGINE_NAMES=\"${engine}\")
    ENDFOREACH(engine)

    ADD_CUSTOM_TARGET(premake_reference_stage make -C ${DISTRO_ROOT}/reference REF_ENGINE_NAMES=\"${PREMAKE_REFERENCE_ENGINES}\" install)
    ADD_CUSTOM_TARGET(reference DEPENDS voicepcmhost-loopback ${PREMAKE_REFERENCE_ENGINES} premake_reference_stage)

ENDFUNCTION()

FUNCTION(BUILD_UIKIT)
    ADD_SUBDIRECTORY(examples/cpp/uikit)
    ADD_DEPENDENCIES(qtsimpleclient skypekit-cppwrapper_2_lib skypekit-cyassl_lib reference_opengl_lib)
ENDFUNCTION()


FUNCTION(BUILD_SKYPEKIT_CLIENT_JAVA)
    FIND_PROGRAM(ANT_EXECUTABLE ant PATHS $ENV{ANT_HOME} ENV PATH )
    ADD_CUSTOM_TARGET(javawrapper ALL
        COMMAND ${ANT_EXECUTABLE} install
        WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/interfaces/skype/java2/api
        COMMENT "Running Ant to build java wrapper api...")
    ADD_CUSTOM_TARGET(java_client ALL
        COMMAND ${ANT_EXECUTABLE}
        WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/interfaces/skype/java2/client
        COMMENT "Running Ant to install java client...")
ENDFUNCTION()

IF (APPS_TO_BUILD MATCHES "cppwrapper")
    BUILD_SKYPEKIT_CLIENT_CPP()
ELSEIF (APPS_TO_BUILD MATCHES "uikit")
    BUILD_UIKIT()
ELSEIF (APPS_TO_BUILD MATCHES "javawrapper")
    BUILD_SKYPEKIT_CLIENT_JAVA()
ELSEIF (APPS_TO_BUILD MATCHES "wrappers")
    BUILD_SKYPEKIT_CLIENT_CPP()
    BUILD_SKYPEKIT_CLIENT_JAVA()
ELSEIF (APPS_TO_BUILD MATCHES "all")
    BUILD_SKYPEKIT_CLIENT_CPP()    
    BUILD_UIKIT()
    BUILD_SKYPEKIT_CLIENT_JAVA()
ENDIF()

