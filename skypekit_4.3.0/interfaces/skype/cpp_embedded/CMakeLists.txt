cmake_minimum_required(VERSION 2.8)

project(skypekitclient)

# we include OpenGl support files for older versions of cmake.
if (NOT DISTRO_ROOT)
    SET(DISTRO_ROOT ${CMAKE_CURRENT_LIST_DIR}/../../../)
    SET(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${DISTRO_ROOT}/cmake_modules)
endif()

set(USE_OPENGL FALSE)
if (NO_VIDEO)
   message(STATUS "Video calling not supported")
else()
    find_package(OpenGL)

    if (OPENGL_FOUND)
        if (NOT WIN32)
            find_package(GLUT)
            if (GLUT_FOUND)
                message(STATUS "OpenGL and GLUT found, video calling supported.")
                set(USE_OPENGL TRUE)
            else()
                message(WARNING "GLUT - OpenGL Utility Toolkit - not found, video calling not supported.")
            endif()
        else(NOT WIN32)
            message(STATUS "OpenGL found, video calling supported.")
            set(USE_OPENGL TRUE)
        endif()
    else()
        message(WARNING "OpenGL not found, video calling not supported.")
    endif()
endif()

if (UNIX AND NOT CYGWIN)
   SET(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -pthread")
   SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -pthread")
endif()

# set(CMAKE_OSX_ARCHITECTURES i386)

set(CPPWRAPPER_DIR ${CMAKE_CURRENT_SOURCE_DIR})
set(CPPWRAPPER_INSTALL_DIR ${CPPWRAPPER_DIR}/build/)

set(SID_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../)
set(SID_IPC_DIR ${SID_DIR}/ipc/cpp/)
set(SID_PLATFORM_DIR ${SID_IPC_DIR}/platform/se)


# build skypekit version of the cyassl library
ADD_SUBDIRECTORY(src/ssl)

# build main cpp interface library
ADD_SUBDIRECTORY(src/api)

if (USE_OPENGL)
    # build reusable opengl library
    ADD_SUBDIRECTORY(src/platform/opengl)
endif()

# build command line skypekitclient
ADD_SUBDIRECTORY(src/client)
