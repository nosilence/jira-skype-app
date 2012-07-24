
#include "test_string_list.hpp"
#include "cppunit_assert_printf.hpp"

#include <string.h>

CPPUNIT_TEST_SUITE_REGISTRATION(StringListTestCase);

const Sid::String array[5] = { "1", "2", "3", "4", "5" };
const uint array_len = sizeof(array) / sizeof(Sid::String); 

void StringListTestCase::createTest()
{
	Sid::List_String test1; 
	Sid::List_String test2(test1); 
	
	CPPUNIT_ASSERT_EQUAL(0, (int)test1.size());
	CPPUNIT_ASSERT_EQUAL(0, (int)test2.size());
}

void StringListTestCase::appendTest()
{
	Sid::List_String test1;
	for(unsigned int i = 0; i < array_len; i++) { test1.append(array[i]); }
	
	CPPUNIT_ASSERT_EQUAL((size_t)array_len, test1.size());
	for(unsigned int i = 0; i < array_len; i++) { CPPUNIT_ASSERT_EQUAL(array[i], test1[i]); }
}

void StringListTestCase::appendEmptyStringTest()
{
	Sid::List_String test1;
	Sid::String string;
	CPPUNIT_ASSERT(string.isNull());

	test1.append(string);

	CPPUNIT_ASSERT(test1.size() == 1);
	CPPUNIT_ASSERT(!test1[0].isNull());
	CPPUNIT_ASSERT(test1[0] == "");
}

void StringListTestCase::operatorTest()
{
	Sid::List_String test1;
	for(unsigned int i = 0; i < array_len; i++) { test1.append(array[i]); }
	Sid::List_String test2;
	for(unsigned int i = 0; i < array_len; i++) { test2.append(array[i]); }	
	
// 	CPPUNIT_ASSERT(test1 == test2);
// 	CPPUNIT_ASSERT(!(test1 != test2));
		
	for(unsigned int i = 0; i < array_len; i++) { CPPUNIT_ASSERT_EQUAL(test2[i], test1[i]); }		
}

void StringListTestCase::assignTest()
{
	Sid::List_String test1;
	for(unsigned int i = 0; i < array_len; i++) { test1.append(array[i]); }
	Sid::List_String test2 = test1;
	
	CPPUNIT_ASSERT_EQUAL(test2.size(), test1.size());
	for(unsigned int i = 0; i < array_len; i++) { CPPUNIT_ASSERT_EQUAL(test2[i], test1[i]); }
}

class SizeThread : public Sid::Thread
{
	public:
		SizeThread(Sid::List_String list,
					size_t sizeShouldBe) : 
			mList(list), 
			mSizeShouldBe(sizeShouldBe),
			mObservedSize(sizeShouldBe) {}

		virtual void Run()
		{
			mObservedSize = mList.size();
		}

		// .bwc. We could CPPUNIT_ASSERT in Run(), but then the 
		// exception doesn't get caught properly.
		void check()
		{
			CPPUNIT_ASSERT_PRINTF(mObservedSize==mSizeShouldBe,
						"size() is not behaving correctly in the "
						"multithreaded case. Size is reported as "
						" %d, but should be %d.",
						mObservedSize,
						mSizeShouldBe);
		}
	private:
		Sid::List_String mList;
		size_t mSizeShouldBe;
		size_t mObservedSize;
};

void StringListTestCase::threadedSizeTest()
{
	Sid::List_String test1;
	size_t size=10000;
	for(size_t i=0; i<size; ++i)
	{
		test1.append(SEString("foobar"));
	}

	{
	SizeThread t1(test1, size);
	SizeThread t2(test1, size);
	SizeThread t3(test1, size);
	SizeThread t4(test1, size);
	SizeThread t5(test1, size);
	SizeThread t6(test1, size);

	t1.start("test::thread");
	t2.start("test::thread");
	t3.start("test::thread");
	t4.start("test::thread");
	t5.start("test::thread");
	t6.start("test::thread");
	
	t1.Stop();
	t2.Stop();
	t3.Stop();
	t4.Stop();
	t5.Stop();
	t6.Stop();

	t1.check();
	t2.check();
	t3.check();
	t4.check();
	t5.check();
	t6.check();
	}
}

bool comp(const Sid::List_String& rhs,
			const Sid::List_String& lhs)
{
	if(rhs.size() != lhs.size())
	{
		return false;
	}

	for(size_t i=0; i<rhs.size(); ++i)
	{
		if(rhs[i] != lhs[i])
		{
			return false;
		}
	}

	return true;
}

void splitJoinTest(const char** test, char esc='\\')
{
	const char* testName = test[0];
	++test;

	Sid::String joinExpected;
	Sid::List_String splitExpected;

	for(const char** substr = test; *substr!=0; ++substr)
	{
		if(*substr != test[0])
		{
			joinExpected += ",";
		}
		joinExpected += *substr;
		splitExpected.append(*substr);
	}

	Sid::List_String splitTest = Sid::List_String::split(joinExpected, ',', esc);
	Sid::String joinTest = splitExpected.join(",", false);

	CPPUNIT_ASSERT_PRINTF(joinTest == joinExpected,
							"join failed on test \"%s\"\nExpected: \"%s\"\n Got:      \"%s\"\n",
							testName,
							joinExpected.data(),
							joinTest.data());
	
	// .bwc. Dicey. If join is subtly broken, we could print misleading
	// diagnostic information.
	Sid::String splitTestRejoined(splitTest.join("}, {", false));
	Sid::String splitExpectedRejoined(splitExpected.join("}, {", false));

	CPPUNIT_ASSERT_PRINTF(comp(splitTest, splitExpected),
							"split failed on test \"%s\"\nExpected: {%s}\nGot:      {%s}",
							testName,
							splitExpectedRejoined.data(),
							splitTestRejoined.data());
}

#define SPLIT_JOIN_TEST(...)\
{const char* test[] = {__VA_ARGS__, 0}; ::splitJoinTest(&test[0]);}


#define SPLIT_JOIN_TEST_NOESC(...)\
{const char* test[] = {__VA_ARGS__, 0}; ::splitJoinTest(&test[0], '\0');}

void StringListTestCase::splitJoinTest()
{
	SPLIT_JOIN_TEST("simple test", 			"foo", "bar", "baz", "fizzbuzz")
	SPLIT_JOIN_TEST("simple quotes test", 		"\"foo\"", "bar")
	SPLIT_JOIN_TEST("null string test", 		0)
	SPLIT_JOIN_TEST("quoted delimiters test", 	"\"foo,bar\"","baz\",\"buzz","fooba\",jooba\"","\"wubba,\"bubba", "\",\"")
	SPLIT_JOIN_TEST("quotes and escapes test", 	"foo\\\"", "\"bar, baz\\\",,, fizzbuzz\"", "\\\"", "\\\"")
	SPLIT_JOIN_TEST("quoted empty string test", 	"\"\"", "bar")
	SPLIT_JOIN_TEST("escaped delimiters test", 	"foo\\\\\\,", "bar", "foo\\\\","bar", "foo\\,bar")

	SPLIT_JOIN_TEST_NOESC("simple test", 			"foo", "bar", "baz", "fizzbuzz")
	SPLIT_JOIN_TEST_NOESC("simple quotes test", 		"\"foo\"", "bar")
	SPLIT_JOIN_TEST_NOESC("null string test", 		0)
	// !bwc! This test-case may go away, depending on whether the intent
	// of the original authors was to ignore quotes if no escape character
	// was used.
	SPLIT_JOIN_TEST_NOESC("quoted delimiters test", 	"\"foo","bar\"","baz\"","\"buzz","fooba\"","jooba\"","\"wubba","\"bubba", "\"","\"")
	SPLIT_JOIN_TEST_NOESC("quotes and escapes test", 	"foo\\\"", "\"bar"," baz\\\""," fizzbuzz\"", "\\\"", "\\\"")
	SPLIT_JOIN_TEST_NOESC("quoted empty string test", 	"\"\"", "bar")
	SPLIT_JOIN_TEST_NOESC("escaped delimiters test", 	"foo\\\\\\", "bar", "foo\\\\","bar", "foo\\", "bar")

}

