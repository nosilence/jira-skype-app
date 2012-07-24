#ifndef CPPUNIT_ASSERT_PRINTF_Include_Guard
#define CPPUNIT_ASSERT_PRINTF_Include_Guard

#include <cppunit/Asserter.h>
#include <cppunit/SourceLine.h>

#include <string>

#include <stdarg.h>
#include <stdio.h>

#define CPPUNIT_ASSERT_PRINTF( condition, format_string, ... ) \
	SidCppunitHelpers::cppunitAssertPrintf( (condition), CPPUNIT_SOURCELINE(), (format_string), __VA_ARGS__)

class SidCppunitHelpers
{
public:
// ?bwc? Maybe move out of global? Probably doesn't matter much for tests.
static void cppunitAssertPrintf(bool condition,
			const CPPUNIT_NS::SourceLine& line,
			const char* format_string,
			...)
{
	// .bwc. Probably good enough.
	char message[1024];
	va_list args;
	va_start(args, format_string);
	vsnprintf(message, 1024, format_string, args);
	va_end(args);
	message[1023]=0; // .bwc. Just in case
	CPPUNIT_NS::Asserter::failIf(!condition,
					std::string(message),
					line);
}
};

#endif

