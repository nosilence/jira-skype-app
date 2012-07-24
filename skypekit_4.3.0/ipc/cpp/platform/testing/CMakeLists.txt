find_package(CppUnit)

if(CPPUNIT_FOUND)

if(NOT ENABLE_RTTI)
    message(WARNING "ENABLE_RTTI is NOT set to yes; unit-tests will not build!")
endif (NOT ENABLE_RTTI)

if(NOT ENABLE_EXCEPTIONS)
    message(WARNING "ENABLE_EXCEPTIONS is NOT set to yes; unit-tests will build, but any errors will halt the cppunit-based tests, preventing subsequent tests from running.")
endif (NOT ENABLE_EXCEPTIONS)
set(CTEST_CUSTOM_COVERAGE_EXCLUDE "${CTEST_CUSTOM_COVERAGE_EXCLUDE} test_.* main.cpp")

set(TARGET sid_types_test)
set(SRCS main.cpp 
    test_binary.cpp
    test_dict_obj.cpp
    test_int_list.cpp
    test_string.cpp
    test_string_list.cpp
    test_thread.cpp
    )

set(sid_include ${DISTRO_ROOT}/interfaces/skype/cpp_embedded/src)

include_directories(
    ${CPPUNIT_INCLUDE_DIR}
    ${SID_CPP_INCLUDES})

link_libraries(cppunit)

if (FRAMEWORK MATCHES "SE")
    link_libraries(skypekit-cppwrapper_2_lib)
    link_libraries(pthread)
elseif (FRAMEWORK matches "POCO")
    link_libraries(PocoFoundation)
elseif (FRAMEWORK matches "QT")
    link_libraries(QtCore)
else ()
    MESSAGE(ERROR "Invalid framework specified")
endif ()

add_executable(${TARGET} ${SRCS})

# TODO
# pthread is linux-only
#target_link_libraries(${TARGET} ${CPPUNIT_LIBRARY})

if(WIN32)
    set_target_properties(${TARGET} PROPERTIES COMPILE_FLAGS "/Zc:wchar_t- /vmg")
endif(WIN32)

add_test(NAME check_types COMMAND ${TARGET})

# Add this test build to "make tests"; will implicitly be added to "make check"
add_dependencies(tests ${TARGET})

# Create a dedicated check_types target
add_custom_target(check_types COMMAND ${CMAKE_CTEST_COMMAND} -VR check_types DEPENDS ${TARGET} VERBATIM)

#GTEST_ADD_TESTS(${TARGET} ""  ${SRCS})
else(CPPUNIT_FOUND)
MESSAGE(WARNING "cppunit not found, unit-tests disabled!")
endif(CPPUNIT_FOUND)

