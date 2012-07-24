project(skypekit-videohost_rtp_ipc)
cmake_minimum_required(VERSION 2.8)

aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR} LIBVIDEOHOSTRTP_SRCS)
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/videohost_ipc LIBVIDEOHOSTRTP_SRCS)

include_directories(${SID_CPP_INCLUDES}
                    ${CMAKE_CURRENT_SOURCE_DIR}
                    ${CMAKE_CURRENT_SOURCE_DIR}/videohost_ipc
                    ${CMAKE_CURRENT_SOURCE_DIR}/../)

add_library(skypekit-videohost_rtp_ipc ${LIBVIDEOHOSTRTP_SRCS})
