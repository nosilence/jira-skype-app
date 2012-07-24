project(skypekit-voicepcm)

aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR} LIBVOICEPCM_SRCS)

include_directories(${SID_CPP_INCLUDES})

add_library(skypekit-voicepcm ${LIBVOICEPCM_SRCS})

