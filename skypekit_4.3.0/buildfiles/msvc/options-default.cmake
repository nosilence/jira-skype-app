#disable rtti
set(DISABLE_RTTI_OPTION "/GR-")
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${DISABLE_RTTI_OPTION}")
add_definitions(-D_WIN32 -DWIN32 -D__WIN32 -D_CRT_SECURE_NO_DEPRECATE)

#Optimization levels?

option(ENABLE_EXCEPTIONS "Enable exceptions (disabled by default)" no)

if (NOT ENABLE_EXCEPTIONS)
    set(DISABLE_EXCEPTIONS_OPTION "/EH-")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${}")
endif ()

option(ENABLE_DEBUG "Enable debug symbols and assertions(disabled by default)" no)

if (ENABLE_DEBUG)
    add_definitions(-DDEBUG -D_DEBUG /Zl)
    set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} /DEBUG /INCREMENTAL:YES")
else (ENABLE_DEBUG)
    add_definitions(-DNDEBUG)
endif(ENABLE_DEBUG)

