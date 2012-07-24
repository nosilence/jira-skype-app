# Just backends out to the appropriate options file; right now only based on compiler
# technology, but may also take into account the specific platform being used.

if (MSVC)
	include(${CMAKE_CURRENT_LIST_DIR}/msvc/options-default.cmake)
elseif (CMAKE_COMPILER_IS_GNUCXX OR CMAKE_CXX_COMPILER MATCHES g\\+\\+$)
	include(${CMAKE_CURRENT_LIST_DIR}/gnu/options-default.cmake)
else ()
	message(FATAL_ERROR "Unknown compiler technology; have no idea how to pass options that we want.")
endif ()
