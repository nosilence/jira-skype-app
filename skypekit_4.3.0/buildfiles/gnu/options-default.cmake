set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wextra -Wall -Wshadow -fno-use-cxa-atexit")

option(ENABLE_RTTI "Enable rtti (disabled by default)" no)

if(NOT ENABLE_RTTI)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fno-rtti")
endif (NOT ENABLE_RTTI)

set(GNU_OPT_LEVELS s 0 1 2 3)
set(GNU_OPT_LEVEL 's' CACHE STRING "GCC optimization level (${GNU_OPT_LEVELS}). Ignored when ENABLE_COVERAGE is set.")
set_property(CACHE GNU_OPT_LEVEL PROPERTY STRINGS ${GNU_OPT_LEVELS})

option(ENABLE_COVERAGE "Enable gcc coverage options" NO)
if (ENABLE_COVERAGE)
    add_definitions(-g -O0 --coverage -W -Wunused-variable -Wunused-parameter -Wunused-function -Wunused -Wno-system-headers -Wwrite-strings -Wno-deprecated )
    SET(CMAKE_CXX_FLAGS "-Woverloaded-virtual ")
    SET(CMAKE_EXE_LINKER_FLAGS "--coverage")
    add_custom_target(coverage COMMAND ${CMAKE_CTEST_COMMAND} -T coverage)
    add_dependencies(coverage tests)
else ()
    add_definitions(-O${GNU_OPT_LEVEL})
endif ()

option(ENABLE_EXCEPTIONS "Enable exceptions (disabled by default)" no)
option(ENABLE_DEBUG "Enable debug symbols and assertions (disabled by default)" no)

if (NOT ENABLE_EXCEPTIONS)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fno-exceptions")
endif ()

if (ENABLE_DEBUG)
    add_definitions(-g)
else (ENABLE_DEBUG)
    add_definitions(-DNDEBUG)
endif (ENABLE_DEBUG)


