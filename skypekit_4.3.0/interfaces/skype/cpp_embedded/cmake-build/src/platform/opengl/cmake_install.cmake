# Install script for directory: /Users/ncherry/Desktop/Projects/skypekit_4.3.0/interfaces/skype/cpp_embedded/src/platform/opengl

# Set the install prefix
IF(NOT DEFINED CMAKE_INSTALL_PREFIX)
  SET(CMAKE_INSTALL_PREFIX "/usr/local")
ENDIF(NOT DEFINED CMAKE_INSTALL_PREFIX)
STRING(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
IF(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  IF(BUILD_TYPE)
    STRING(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  ELSE(BUILD_TYPE)
    SET(CMAKE_INSTALL_CONFIG_NAME "")
  ENDIF(BUILD_TYPE)
  MESSAGE(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
ENDIF(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)

# Set the component getting installed.
IF(NOT CMAKE_INSTALL_COMPONENT)
  IF(COMPONENT)
    MESSAGE(STATUS "Install component: \"${COMPONENT}\"")
    SET(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  ELSE(COMPONENT)
    SET(CMAKE_INSTALL_COMPONENT)
  ENDIF(COMPONENT)
ENDIF(NOT CMAKE_INSTALL_COMPONENT)

IF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")
  list(APPEND CPACK_ABSOLUTE_DESTINATION_FILES
   "/Users/ncherry/Desktop/Projects/skypekit_4.3.0/interfaces/skype/cpp_embedded/build/libreference_opengl_lib.a")
FILE(INSTALL DESTINATION "/Users/ncherry/Desktop/Projects/skypekit_4.3.0/interfaces/skype/cpp_embedded/build" TYPE STATIC_LIBRARY FILES "/Users/ncherry/Desktop/Projects/skypekit_4.3.0/interfaces/skype/cpp_embedded/cmake-build/src/platform/opengl/libreference_opengl_lib.a")
  IF(EXISTS "$ENV{DESTDIR}/Users/ncherry/Desktop/Projects/skypekit_4.3.0/interfaces/skype/cpp_embedded/build/libreference_opengl_lib.a" AND
     NOT IS_SYMLINK "$ENV{DESTDIR}/Users/ncherry/Desktop/Projects/skypekit_4.3.0/interfaces/skype/cpp_embedded/build/libreference_opengl_lib.a")
    EXECUTE_PROCESS(COMMAND "/usr/bin/ranlib" "$ENV{DESTDIR}/Users/ncherry/Desktop/Projects/skypekit_4.3.0/interfaces/skype/cpp_embedded/build/libreference_opengl_lib.a")
  ENDIF()
ENDIF(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "Unspecified")

