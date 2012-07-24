
#include "test_string.hpp"
#include "cppunit_assert_printf.hpp"

#include <stdarg.h>
#include <string.h>
#define __STDC_LIMIT_MACROS
#include <stdint.h>

CPPUNIT_TEST_SUITE_REGISTRATION(StringTestCase);
const char* temp = "test12345";

void StringTestCase::testComparisons()
{
    CPPUNIT_ASSERT(Sid::String() == Sid::String());
    CPPUNIT_ASSERT(Sid::String() == 0);
    CPPUNIT_ASSERT(!(Sid::String() == ""));
    CPPUNIT_ASSERT(!(Sid::String() == Sid::String("")));
}

void checkAssignment(const Sid::String& test,
                        const Sid::String& rhs,
                        const Sid::String& expected)
{
    Sid::String copy(test);
    const char* rhsData = rhs.data();
    copy = rhs;
    CPPUNIT_ASSERT_PRINTF(copy == expected,
                            "Assignment operator yielded unexpected result:\n"
                            "\"%s\" (lhs)\n"
                            "\"%s\" (rhs)\n"
                            "\"%s\" (expected)\n"
                            "\"%s\" (got)\n",
                            test.isNull() ? "(NULL)" : test.data(),
                            rhs.isNull() ? "(NULL)" : rhs.data(),
                            expected.isNull() ? "(NULL)" : rhs.data(),
                            copy.isNull() ? "(NULL)" : copy.data());
    CPPUNIT_ASSERT_MESSAGE("Assignment operator caused rhs to relinquish its reference!",
                            rhs.data() == rhsData);
}

void StringTestCase::createTest()
{    
    Sid::String test1(temp); 
    Sid::String test2(test1); 
    
    CPPUNIT_ASSERT(strcmp((const char*)test1, temp)  == 0);
    CPPUNIT_ASSERT(strcmp((const char*)test1, (const char*)test2)  == 0);

    for(unsigned int i = 0; i<20; ++i)
    {
    	Sid::String str(i);
    	CPPUNIT_ASSERT_PRINTF( str.length() <= i,
    				"Sid::String(%d) apparently has length %d!\nHere is what it contains: %s\n",
    				i, 
    				str.length(), 
    				str.data());
    }
}

void StringTestCase::assignTest()
{
    checkAssignment(Sid::String(), Sid::String(), Sid::String());
    checkAssignment("foo", Sid::String(), Sid::String());
    checkAssignment("", Sid::String(), Sid::String());
    checkAssignment(Sid::String(), "", "");
    checkAssignment(Sid::String(4), Sid::String(), Sid::String());
    checkAssignment(Sid::String(4), "", "");
    checkAssignment("foo", "bar", "bar");

    {
        Sid::String test("foo");
        Sid::String copy(test);
        checkAssignment(test, "bar", "bar");
    }

    {
        // Do we check to make sure ownership of the original is retained?W
        Sid::String test("foo");
        checkAssignment(test, test, "foo");
    }

    {
        // Do we check to make sure ownership of the original is retained?W
        Sid::String test("foo");
        Sid::String copy(test);
        checkAssignment(test, copy, "foo");
    }

    Sid::String test1 = temp; 
    Sid::String test2 = test1; 
    Sid::String test3 = "test12345"; 
    
    CPPUNIT_ASSERT(strcmp((const char*)test1, temp) == 0);
    CPPUNIT_ASSERT(strcmp((const char*)test1, (const char*)test2) == 0);
    CPPUNIT_ASSERT(strcmp((const char*)test1, (const char*)test3) == 0);	
}

void StringTestCase::operatorTest()
{
    Sid::String test1 = temp;
    Sid::String test2 = temp;
    
    CPPUNIT_ASSERT(test1 == test2);
    CPPUNIT_ASSERT(!(test1 != test2));
    CPPUNIT_ASSERT(strcmp((const char*)test1, temp)  == 0);
}

void StringTestCase::getBufTest()
{
    Sid::String test1 = temp; 
    Sid::String *test2 = &test1;
    const char* temp2 = (const char*)*test2; 
    
    CPPUNIT_ASSERT(strcmp(temp2, temp) == 0);
    CPPUNIT_ASSERT(strcmp((const char*)test1, temp) == 0);
}

void StringTestCase::getSizeTest()
{
    Sid::String test1 = temp; 
    CPPUNIT_ASSERT_EQUAL((int)test1.size(), (int)strlen(temp));
}

void StringTestCase::substrTest()
{
    // Sunny day
    {
    	Sid::String str("foobajooba");
    	Sid::String sub = str.substr(0,0);

    	CPPUNIT_ASSERT_PRINTF( sub == "f",
    				"Basic substr logic is broken (input was %s, 0, 0)\nSubstring was %s\n",
    				str.data(),
    				sub.data());

    	sub = str.substr(0, 9);
    	CPPUNIT_ASSERT_PRINTF( sub == "foobajooba",
    				"Basic substr logic is broken (input was %s, 0, 9)\nSubstring was %s\n",
    				str.data(),
    				sub.data());

    	sub = str.substr(0, 8);
    	CPPUNIT_ASSERT_PRINTF( sub == "foobajoob",
    				"Basic substr logic is broken (input was %s, 0, 8)\nSubstring was %s\n",
    				str.data(),
    				sub.data());

    	sub = str.substr(1, 9);
    	CPPUNIT_ASSERT_PRINTF( sub == "oobajooba",
    				"Basic substr logic is broken (input was %s, 1, 9)\nSubstring was %s\n",
    				str.data(),
    				sub.data());

    	sub = str.substr(1, 8);
    	CPPUNIT_ASSERT_PRINTF( sub == "oobajoob",
    				"Basic substr logic is broken (input was %s, 1, 8)\nSubstring was %s\n",
    				str.data(),
    				sub.data());
    }

    // Null string
    {
    	Sid::String str;
    	Sid::String sub = str.substr(0,0);
    	CPPUNIT_ASSERT_MESSAGE("Substring (0,0) of a null string somehow is not null. Fascinating!\n",
    				sub.isNull());

    	sub = str.substr(0, -1);
    	CPPUNIT_ASSERT_MESSAGE( "Substring (0,-1) of a null string somehow is not null. Fascinating!\n",
    				sub.isNull());
    }

    // Empty string
    {
    	Sid::String str("");
    	Sid::String sub = str.substr(0,0);
    	CPPUNIT_ASSERT_MESSAGE("Substring (0,0) of an empty string somehow is not empty. Fascinating!\n",
    				sub.isEmpty() && !sub.isNull());

    	sub = str.substr(0, -1);
    	CPPUNIT_ASSERT_MESSAGE( "Substring (0,-1) of an empty string somehow is not empty. Fascinating!\n",
    				sub.isEmpty() && !sub.isNull());
    }

    // boundary fixup
    {
    	Sid::String str("foobajooba");
    	Sid::String sub = str.substr(0, -1);
    	CPPUNIT_ASSERT_PRINTF( sub == "foobajoob",
    				"Negative index code is somehow broken (input was %s, 0, -1)\nSubstring was %s\n",
    				str.data(), 
    				sub.data());

    	sub = str.substr(0, -9);

    	CPPUNIT_ASSERT_PRINTF( sub == "f",
    				"Negative index code is somehow broken (input was %s, 0, -9)\nSubstring was %s\n",
    				str.data(), 
    				sub.data());

    	sub = str.substr(0, -9999999);
    	CPPUNIT_ASSERT_PRINTF( sub == "f",
    			"Negative index modulus code is somehow broken (input was %s, 0, -9999999)\nSubstring was %s\n",
    			str.data(), 
    			sub.data());

    	sub = str.substr(-5, 2);
    	CPPUNIT_ASSERT_PRINTF( sub == "foo",
    				"Negative leading bound fixup seems to not be working (input was %s, -5, 2)\nSubstring was %s\n",
    				str.data(),
    				sub.data());

    	sub = str.substr(5, 2);
    	CPPUNIT_ASSERT_PRINTF( sub == "obaj",
    				"Bound swapping fixup seems to not be working (input was %s, 5, 2)\nSubstring was %s\n",
    				str.data(),
    				sub.data());

    	sub = str.substr(5, 900);
    	CPPUNIT_ASSERT_PRINTF( sub == "jooba",
    				"Large upper bound fixup seems to not be working (input was %s, 5, 900)\nSubstring was %s\n",
    				str.data(),
    				sub.data());

    	sub = str.substr(9, 900);
    	CPPUNIT_ASSERT_PRINTF( sub == "a",
    				"Large upper bound fixup seems to not be working (input was %s, 9, 900)\nSubstring was %s\n",
    				str.data(),
    				sub.data());

    	sub = str.substr(10, 9);
    	CPPUNIT_ASSERT_PRINTF( sub == "foobajooba",
    				"Large lower bound fixup seems to not be working (input was %s, 10, 900)\nSubstring was %s\n",
    				str.data(),
    				sub.data());

    	sub = str.substr(400, 9);
    	CPPUNIT_ASSERT_PRINTF( sub == "foobajooba",
    				"Large lower bound fixup seems to not be working (input was %s, 400, 900)\nSubstring was %s\n",
    				str.data(),
    				sub.data());
    }

}

void checkUnescaping(const Sid::String& test, const Sid::String& expected)
{
    const char* origData = test.data();
    Sid::String unescaped=test.unescape();
    CPPUNIT_ASSERT_PRINTF(unescaped == expected,
    			"Unescaping \"%s\" did not yield \"%s\" as "
    			"expected, instead yielded \"%s\"\n",
    			!test.isNull() ? test.data() : "(NULL)",
    			!expected.isNull() ? expected.data() : "(NULL)",
    			!unescaped.isNull() ? unescaped.data() : "(NULL)");
    CPPUNIT_ASSERT_MESSAGE("unescape() causes object to relinquish its reference!",
                            origData == test.data());
}

void checkEscaping(const Sid::String& test, const Sid::String& expected)
{
    const char* origData = test.data();
    Sid::String escaped=test.escape();
    CPPUNIT_ASSERT_PRINTF(escaped == expected,
    			"Escaping \"%s\" did not yield \"%s\" as "
    			"expected, instead yielded \"%s\"\n",
    			!test.isNull() ? test.data() : "(NULL)",
    			!expected.isNull() ? expected.data() : "(NULL)",
    			!escaped.isNull() ? escaped.data() : "(NULL)");
    checkUnescaping(expected, test);
    CPPUNIT_ASSERT_MESSAGE("escape() causes object to relinquish its reference!",
                            origData == test.data());
}

void StringTestCase::escapingAndUnescapingTest()
{
    checkEscaping(Sid::String(), Sid::String());
    checkEscaping(Sid::String(4), "");
    checkEscaping("", "");
    checkEscaping(",", "\\,");
    checkEscaping("\"", "\\\"");
    checkEscaping("\\", "\\\\");
    checkEscaping("foobar", "foobar");
    checkEscaping("foo,bar", "foo\\,bar");
    checkEscaping("foo\"bar", "foo\\\"bar");
    checkEscaping("foo\\bar", "foo\\\\bar");
    checkEscaping(",,", "\\,\\,");
    checkEscaping("\"\"", "\\\"\\\"");
    checkEscaping("\\\\", "\\\\\\\\");

    checkUnescaping("\\", "");
    checkUnescaping("foo\\bar", "foobar");
    checkUnescaping("\\foobar", "foobar");
    checkUnescaping("foobar\\", "foobar");

    // .bwc. This code came from an #ifdef SE_STRING_TEST_PROGRAM block in
    // skype_string.cpp.
    int i;
    Sid::String strings[] = {
    	"Hello World!",
    	"Hel,lo\\ World!",
    	"Hel\"lo Wo\\9rld!",
    	"Hel,lo Wo\\0rld!",
    	Sid::String()
    };
    for (i = 0; !strings[i].isNull(); i++) {
    	Sid::String escaped = strings[i].escape();
    	Sid::String unescaped = escaped.unescape();
    	CPPUNIT_ASSERT_PRINTF( !strcmp((const char*)strings[i], (const char*)unescaped),
    				"String Failed:\n%s\n%s\n%s\n\n", 
    				(const char*)strings[i], 
    				(const char*)escaped, 
    				(const char*)unescaped);
    }

    // 0x5c = '\\', 0x30 = '0', 0x00 = 0, 0x22 = '"'
    const char bins[] = {
    	'1', '2', '3', '4', '5', '6', '7', '8' ,
    	'1', '2', 0x5c, 0x30, '5', '6', '7', '8' ,
    	'1', '2', 0x5c, 0x30, 0x5c, 0x5c, 0x5c, '8' ,
    	'1', '2', 0x5c, 0x30, '5', '6', '7', '8' ,
    	'1', '2', 0x00, '4', '5', '6', '7', '8' ,
    	'1', 0x5c, 0x00, 0x5c, '5', '6', 0x5c, 0x5c ,
    	'1', 0x5c, 0x00, 0x00, '5', 0x00, 0x5c, '8' ,
    	0x00, '2', 0x5c, 0x30, '5', 0x22, 0x22, '8' ,
    	0x5c, '2', 0x5c, 0x30, '5', 0x22, 0x22, 0x00 ,
    	0x00, 0x00, 0x5c, 0x30, '5', 0x22, 0x22, 0x5c ,
    	'E'
    };
    size_t bins_len = 8;
    for (i = 0; bins[i] != 'E'; i += bins_len) {
    	Sid::String escaped = Sid::String::from((char*)(bins + i), bins_len);
    	char *unescaped = (char *)malloc(escaped.length());
    	size_t unescaped_len = escaped.toBinary(unescaped);
    	//printf("%d, escaped.length()=%d unescaped_len=%d\n", i/bins_len, escaped.length(), unescaped_len);
    	CPPUNIT_ASSERT_PRINTF ( !memcmp((char*)(bins + i), unescaped, bins_len) && (unescaped_len == bins_len),
    				"Binary Failed: index %d\n", 
    				i/bins_len);
    	//printf("%s\n", (const char*)escaped.getHexRepresentation());
    	free(unescaped);
    }

    {
    	Sid::String test("foo");
    	test.markAsBinary();
    	CPPUNIT_ASSERT_PRINTF(test.isBinary(), 
    				"%s", "markAsBinary() seems "
    						"to have no effect.\n");
    }
}

void checkFrom(uint64 n, const Sid::String& expected)
{
    Sid::String from = Sid::String::from(n);
    CPPUNIT_ASSERT_PRINTF(from == expected,
                            "from((uint64)%llu) yielded unexpected result \"%s\"\n",
                            n, 
                            from.isNull() ? "(NULL)" : from.data());
}

void checkFrom(unsigned char c, const Sid::String& expected)
{
    Sid::String from = Sid::String::from(c);
    CPPUNIT_ASSERT_PRINTF(from == expected,
                            "from((unsigned char)%c) yielded unexpected result \"%s\"\n",
                            c,
                            from.isNull() ? "(NULL)" : from.data());
}

void checkFrom(char c, const Sid::String& expected)
{
    Sid::String from = Sid::String::from(c);
    CPPUNIT_ASSERT_PRINTF(from == expected,
                            "from((char)%c) yielded unexpected result \"%s\"\n",
                            c,
                            from.isNull() ? "(NULL)" : from.data());
}

void checkFrom(unsigned int u, unsigned int base, const Sid::String& expected)
{
    Sid::String from = Sid::String::from(u, base);
    CPPUNIT_ASSERT_PRINTF(from == expected,
                            "from((unsigned int)%u, (unsigned int)%u) yielded unexpected result \"%s\"\n",
                            u,
                            base,
                            from.isNull() ? "(NULL)" : from.data());
}

void checkFrom(bool b, const Sid::String& expected)
{
    Sid::String from = Sid::String::from(b);
    CPPUNIT_ASSERT_PRINTF(from == expected,
                            "from((bool)%s) yielded unexpected result \"%s\"\n",
                            b ? "true" : "false",
                            from.isNull() ? "(NULL)" : from.data());
}

void checkToBool(const Sid::String& test, bool expected)
{
    const char* origData = test.data();
    bool res = test.toBool();
    CPPUNIT_ASSERT_PRINTF(res == expected,
                            "toBool() yielded unexpected result for value "
                            "\"%s\", should have been %s\n",
                            test.isNull() ? "(NULL)" : test.data(),
                            expected ? "true" : "false");
    CPPUNIT_ASSERT_MESSAGE("toBool() causes object to relinquish its reference!",
                            origData == test.data());
}

void checkToInt(const Sid::String& test, int expected)
{
    const char* origData = test.data();
    int res = test.toInt();
    CPPUNIT_ASSERT_PRINTF(res == expected,
                            "toInt() yielded unexpected result for value "
                            "\"%s\", should have been %d, instead got %d\n",
                            test.isNull() ? "(NULL)" : test.data(),
                            expected,
                            res);
    CPPUNIT_ASSERT_MESSAGE("toInt() causes object to relinquish its reference!",
                            origData == test.data());
}

void checkToUInt(const Sid::String& test, unsigned int expected)
{
    const char* origData = test.data();
    unsigned int res = test.toUInt();
    CPPUNIT_ASSERT_PRINTF(res == expected,
                            "toUInt() yielded unexpected result for value "
                            "\"%s\", should have been %u, instead got %u\n",
                            test.isNull() ? "(NULL)" : test.data(),
                            expected,
                            res);
    CPPUNIT_ASSERT_MESSAGE("toUInt() causes object to relinquish its reference!",
                            origData == test.data());
}

void checkToUInt64(const Sid::String& test, uint64 expected)
{
    const char* origData = test.data();
    uint64 res = test.toUInt64();
    CPPUNIT_ASSERT_PRINTF(res == expected,
                            "toUInt64() yielded unexpected result for value "
                            "\"%s\", should have been %ull, instead got %ull\n",
                            test.isNull() ? "(NULL)" : test.data(),
                            expected,
                            res);
    CPPUNIT_ASSERT_MESSAGE("toUInt64() causes object to relinquish its reference!",
                            origData == test.data());
}

void checkToBinary(const Sid::String& test, const char* expected, size_t expectedSize)
{
    const char* origData = test.data();
    char* res = new char[test.size()+1];
    size_t size = test.toBinary(res);
    CPPUNIT_ASSERT_PRINTF(size == expectedSize,
                            "toBinary() yielded a result that was too long, "
                            "expected %u, got %u\n",
                            expectedSize,
                            size);
    CPPUNIT_ASSERT_PRINTF(!memcmp(expected, res, size),
                            "%s",
                            "toBinary() yielded unexpected result\n");
    CPPUNIT_ASSERT_MESSAGE("toBinary() causes object to relinquish its reference!",
                            origData == test.data());
}

void checkToBinary(const Sid::String& test, const char* expected, size_t expectedSize, const Sid::String& hex)
{
    const char* origData = test.data();
    checkToBinary(test, expected, expectedSize);
    Sid::String expHex(hex);
    Sid::String resHex = test.getHexRepresentation();
    CPPUNIT_ASSERT_PRINTF(expHex == resHex,
                            "getHexRepresentation() yielded unexpected "
                            "result:\n"
                            "\"%s\" (expected)\n"
                            "\"%s\" (result)\n",
                            expHex.isNull() ? "(NULL)" : expHex.data(),
                            resHex.isNull() ? "(NULL)" : resHex.data());

    CPPUNIT_ASSERT_MESSAGE("toBinary() causes object to relinquish its reference!",
                            origData == test.data());
}

void checkToBinary(const char* test, 
                    const char* expected, 
                    size_t expectedSize)
{
    Sid::String asString(test);
    asString.markAsBinary();
    checkToBinary(asString, expected, expectedSize);
}

void checkToBinary(const char* test, 
                    const char* expected, 
                    size_t expectedSize,
                    const Sid::String& expectedHex)
{
    Sid::String asString(test);
    asString.markAsBinary();
    checkToBinary(asString, expected, expectedSize, expectedHex);
}

void checkHexRepresentation(const Sid::String& test, const Sid::String& expected)
{
    const char* origData = test.data();
    Sid::String res = test.getHexRepresentation();
    CPPUNIT_ASSERT_PRINTF(res == expected,
                            "getHexRepresentation() yielded unexpected result for value "
                            "\"%s\", should have been \"%s\", instead got \"%s\"\n",
                            test.isNull() ? "(NULL)" : test.data(),
                            expected.isNull() ? "(NULL)" : expected.data(),
                            res.isNull() ? "(NULL)" : res.data());    
    CPPUNIT_ASSERT_MESSAGE("getHexRepresentation() causes object to relinquish its reference!",
                            origData == test.data());
}

void checkAppend(const Sid::String& lhs, 
                    const Sid::String& rhs, 
                    const Sid::String& expected)
{
    const char* origData = rhs.data();
    Sid::String test(lhs);
    test += rhs;
    CPPUNIT_ASSERT_PRINTF(test == expected,
                            "operator+=(const Sid::String&) yielded unexpected"
                            " result:\n"
                            "lhs was  \"%s\"\n"
                            "rhs was  \"%s\"\n"
                            "expected \"%s\"\n"
                            "got      \"%s\"\n",
                            lhs.isNull() ? "(NULL)" : lhs.data(),
                            rhs.isNull() ? "(NULL)" : rhs.data(),
                            expected.isNull() ? "(NULL)" : expected.data(),
                            test.isNull() ? "(NULL)" : test.data());
    CPPUNIT_ASSERT_MESSAGE("operator+=() causes rhs to relinquish its reference!",
                            origData == rhs.data());
}

void checkAppend(const Sid::String& lhs, 
                    const char* rhs, 
                    const Sid::String& expected)
{
    Sid::String test(lhs);
    CPPUNIT_ASSERT(test == lhs);
    test += rhs;
    CPPUNIT_ASSERT_PRINTF(test == expected,
                            "operator+=(const char*) yielded unexpected"
                            " result:\n"
                            "lhs was  \"%s\"\n"
                            "rhs was  \"%s\"\n"
                            "expected \"%s\"\n"
                            "got      \"%s\"\n",
                            lhs.isNull() ? "(NULL)" : lhs.data(),
                            rhs ?  rhs : "(NULL)",
                            expected.isNull() ? "(NULL)" : expected.data(),
                            test.isNull() ? "(NULL)" : test.data());

    checkAppend(lhs, Sid::String(rhs), expected);
}

void checkKeyValue(const Sid::String& key,
    		const Sid::String& value,
    		const Sid::String& expected)
{
    const char* keyData = key.data();
    const char* valueData = value.data();
    Sid::String keyValue=Sid::String::keyValue(key, value);
    CPPUNIT_ASSERT_PRINTF(keyValue == expected,
    			"keyValue(\"%s\", \"%s\") did not yield \"%s\","
    			" instead got \"%s\"\n",
    			key.isNull() ? "(NULL)" : key.data(),
    			value.isNull() ? "(NULL)" : value.data(),
    			expected.isNull() ? "(NULL)" : expected.data(), 
    			keyValue.isNull() ? "(NULL)" : keyValue.data());
    CPPUNIT_ASSERT_MESSAGE("keyValue() causes key to relinquish its reference!",
                            keyData == key.data());
    CPPUNIT_ASSERT_MESSAGE("keyValue() causes value to relinquish its reference!",
                            valueData == value.data());
}

void StringTestCase::testKeyValue()
{
    checkKeyValue(Sid::String(), Sid::String(), Sid::String());
    checkKeyValue(Sid::String(), "", Sid::String());
    checkKeyValue("", Sid::String(), "=\"\" ");
    checkKeyValue("","","=\"\" ");
    checkKeyValue(Sid::String(), "foo", Sid::String());
    checkKeyValue("", "foo", "=\"foo\" ");
    checkKeyValue("foo", Sid::String(), "foo=\"\" ");
    checkKeyValue("foo", "", "foo=\"\" ");
    checkKeyValue("foo", "bar", "foo=\"bar\" ");
}

void checkConversion(const Sid::String& test, const char* expected)
{
    const char* origData = test.data();
    CPPUNIT_ASSERT_PRINTF(!strcmp(test, expected),
    			"operator const char* did not yield \"%s\" as "
    			"expected, got \"%s\" instead.\n",
    			test.isNull() ? "(NULL)" : (const char*)test,
    			expected);
    CPPUNIT_ASSERT_MESSAGE("operator const char*() causes object to relinquish its reference!",
                            origData == test.data());
}

void checkConversion(const char* expected)
{
    checkConversion(Sid::String(expected), expected);
}

void StringTestCase::testConversion()
{
    checkConversion(Sid::String(), "");
    checkConversion(Sid::String(4), "");
    checkConversion("");
    checkConversion("foo");

}

void checkStartWith(const Sid::String& test, const Sid::String& prefix)
{
    const char* origData = test.data();
    CPPUNIT_ASSERT_PRINTF(test.startWith(prefix),
    			"startsWith() did not return true for "
    			"\n%s\n%s\n",
    			prefix.isNull() ? "(NULL)" : prefix.data(),
    			test.isNull() ? "(NULL)" : test.data());
    CPPUNIT_ASSERT_MESSAGE("startWith() causes object to relinquish its reference!",
                            origData == test.data());
}

void checkNotStartWith(const Sid::String& test, const Sid::String& prefix)
{
    const char* origData = test.data();
    CPPUNIT_ASSERT_PRINTF(!test.startWith(prefix),
    			"startsWith() did not return false for "
    			"\n%s\n%s\n",
    			prefix.isNull() ? "(NULL)" : prefix.data(),
    			test.isNull() ? "(NULL)" : test.data());
    CPPUNIT_ASSERT_MESSAGE("startWith() causes object to relinquish its reference!",
                            origData == test.data());
}

void StringTestCase::testStartWith()
{
    checkStartWith("", Sid::String());
    checkStartWith("", Sid::String(4));
    checkStartWith("","");
    checkStartWith("foo", Sid::String());
    checkStartWith("foo", Sid::String(4));
    checkStartWith("foo", "");
    checkStartWith("foo", "f");
    checkStartWith("foo", "foo");

    checkNotStartWith(Sid::String(), Sid::String());
    checkNotStartWith(Sid::String(), "");
    checkNotStartWith(Sid::String(), Sid::String(4));
    checkNotStartWith(Sid::String(), "foo");
    checkNotStartWith("", "foo");
    checkNotStartWith("f", "foo");
    checkNotStartWith("fo", "foo");
    checkNotStartWith("foo", "b");
    checkNotStartWith("foo", "o");
}

void checkRight(const Sid::String& test, 
    	unsigned int len,
    	const Sid::String& expected)
{
    const char* origData = test.data();
    Sid::String right = test.right(len);
    CPPUNIT_ASSERT_PRINTF(right == expected,
    			"right(%d) did not yield expected result for:\n"
    			"%s\n%*s (got)\n%*s (expected)\n",
    			len,
    			test.isNull() ? "(NULL)" : test.data(),
    			test.size(),
    			right.isNull() ? "(NULL)" : right.data(),
    			test.size(),
    			expected.isNull() ? "(NULL)" : expected.data());
    CPPUNIT_ASSERT_MESSAGE("right() causes object to relinquish its reference!",
                            origData == test.data());
}

void StringTestCase::testRight()
{
    checkRight(Sid::String(), 0, Sid::String());
    checkRight(Sid::String(), 1, Sid::String());
    checkRight(Sid::String(), -1, Sid::String());
    checkRight(Sid::String(4), 0, "");
    checkRight(Sid::String(4), 1, "");
    checkRight(Sid::String(4), -1, "");
    checkRight("f", 0, "");
    checkRight("f", 1, "f");
    checkRight("f", -1, "f");
    checkRight("f", 999, "f");
    checkRight("foobajooba", 2, "ba");
    checkRight("foobajooba", 7, "bajooba");
    checkRight("foobajooba", -1, "foobajooba");
    checkRight("foobajooba", 10, "foobajooba");
}

void checkTrim(const Sid::String& test,
    	const Sid::String& toTrim,
    	const Sid::String& expected)
{
    const char* origData = test.data();
    Sid::String trimmed = test.trim(toTrim);
    CPPUNIT_ASSERT_PRINTF(trimmed == expected,
    			"trim(\"%s\") did not yield expected result:\n"
    			"%s (prefix to remove)\n"
    			"%*s (expected result)\n"
    			"%s (string to trim)\n"
    			"%*s (instead got)\n",
    			toTrim.isNull() ? "(NULL)" : toTrim.data(),
    			toTrim.isNull() ? "(NULL)" : toTrim.data(),
    			test.size(),
    			expected.isNull() ? "(NULL)" : expected.data(),
    			test.isNull() ? "(NULL)" : test.data(),
    			test.size(),
    			trimmed.isNull() ? "(NULL)" : trimmed.data());
    CPPUNIT_ASSERT_MESSAGE("trim() causes object to relinquish its reference!",
                            origData == test.data());
}

void StringTestCase::testTrim()
{
    checkTrim(Sid::String(), Sid::String(), Sid::String());
    checkTrim(Sid::String(), "", Sid::String());
    checkTrim(Sid::String(), "foo", Sid::String());
    checkTrim("", Sid::String(), "");
    checkTrim("", "", "");
    checkTrim("", "foo", "");
    checkTrim("foo", "", "foo");
    checkTrim("foo", Sid::String(), "foo");
    checkTrim("foo", "f", "oo");
    checkTrim("foo", "foo", "");
    checkTrim("foo", "fooba", "foo");
}

void checkFrom(int n, const Sid::String& expected)
{
    Sid::String from=Sid::String::from(n);
    CPPUNIT_ASSERT_PRINTF(from == expected,
    			"from((int)%d) did not yield expected result:\n"
    			"got  %s\n",
    			expected.data());
}

void StringTestCase::testFrom()
{
    checkFrom(0, "0");
    checkFrom(1, "1");
    checkFrom(-1, "-1");
    checkFrom(2147483647, "2147483647");
    checkFrom(1ULL, "1");
    checkFrom(0ULL, "0");
    checkFrom((uint64)UINT64_MAX, "18446744073709551615");
 
    checkFrom('\0', "");
    checkFrom('0', "0");
    checkFrom('\xFF', "\xFF");
    checkFrom('\n', "\n");
    
    checkFrom(UINT32_MAX, 10, "4294967295");
    checkFrom(UINT32_MAX, 16, "FFFFFFFF");
    // This stuff is debatable; probably should return empty string for 
    // unsupported bases.
    checkFrom(UINT32_MAX, 8, "4294967295");
    checkFrom(UINT32_MAX, 2, "4294967295");
    checkFrom(UINT32_MAX, 1, "4294967295");
    checkFrom(UINT32_MAX, 0, "4294967295");
    checkFrom(UINT32_MAX, -1, "4294967295");
    checkFrom(UINT32_MAX, -2, "4294967295");
    checkFrom(0, 10, "0");
    checkFrom(0, 16, "0");
    checkFrom(0, 0, "0");
    checkFrom(1829304657, 10, "1829304657");
    checkFrom(1829304657, 16, "6D08F951");
    
    checkFrom(true, "1");
    checkFrom(false, "0");
    
}

void StringTestCase::testTo()
{
    checkToBool("1", true);
    checkToBool("", false);
    checkToBool(Sid::String(), false);
    checkToBool("0", false);
    checkToBool("18446744073709551616", true);

    checkToInt(Sid::String(), 0);
    checkToInt("", 0);
    checkToInt("a", 0);
    checkToInt("0", 0);
    checkToInt("0a", 0);
    checkToInt("1a", 1);
    checkToInt("-1", -1);
    checkToInt("-1a", -1);
    checkToInt("2147483647", 2147483647);
    checkToInt("2147483647a", 2147483647);
// .bwc. Appears to be platform-specific. Is -1 on linux (which is what I
// expected initially), but INT_MAX on Windows.
//        checkToInt("4294967295", 2147483647);

        checkToUInt(Sid::String(), 0);
        checkToUInt("", 0);
        checkToUInt("a", 0);
        checkToUInt("0", 0);
        checkToUInt("0a", 0);
        checkToUInt("1a", 1);
        checkToUInt("-1", -1);
        checkToUInt("-1a", -1);
        checkToUInt("2147483647", 2147483647);
        checkToUInt("4294967295", 4294967295u);
        checkToUInt("2147483647a", 2147483647);
        checkToUInt("4294967295a", 4294967295u);

        checkToUInt64(Sid::String(), 0);
        checkToUInt64("", 0);
        checkToUInt64("a", 0);
        checkToUInt64("0", 0);
        checkToUInt64("0a", 0);
        checkToUInt64("1a", 1);
        checkToUInt64("-1", -1);
        checkToUInt64("-1a", -1);
        checkToUInt64("2147483647", 2147483647);
        checkToUInt64("4294967295", 4294967295u);
        checkToUInt64("2147483647a", 2147483647);
        checkToUInt64("4294967295a", 4294967295u);
        checkToUInt64("18446744073709551615", 18446744073709551615ul);
        checkToUInt64("18446744073709551615a", 18446744073709551615ul);

        checkToBinary(Sid::String(), "", 0, Sid::String());
        checkToBinary(Sid::String(4), "", 0, Sid::String());
        checkToBinary("", "", 0, "");
        checkToBinary(Sid::String("foo"), "", 0, Sid::String());
        checkToBinary("foo", "foo", 3);
        checkToBinary("fo\\,o", "fo,o", 4);
        checkToBinary("fo\\\\o", "fo\\o", 4);
        checkToBinary("fo\\o", "foo", 3);
        checkToBinary("fo\\0o", "fo\0o", 4);
        checkToBinary("\xff\xff\\0\x18\x12\xab\xad\xbe\xef", 
    		    "\xff\xff\x00\x18\x12\xab\xad\xbe\xef", 
    		    9, 
    		    "ffff001812abadbeef");
        
    }

    void StringTestCase::appendTest()
    {
        checkAppend(Sid::String(), Sid::String(), Sid::String());
        checkAppend(Sid::String(), "", "");
        checkAppend("", Sid::String(), "");
        checkAppend("", "", "");
        checkAppend(Sid::String(), 0, Sid::String());
        checkAppend("", 0, "");
        checkAppend(Sid::String(), "foo", "foo");
        checkAppend("", "foo", "foo");
        checkAppend("foo", Sid::String(), "foo");
        checkAppend("foo", 0, "foo");
        checkAppend("foo", "bar", "foobar");
        
        {
    	Sid::String test("foo\\,bar");
    	test.markAsBinary();
    	checkAppend(test, 0, test);
    	checkAppend(test, Sid::String(), test);
    	checkAppend(test, "", test);
    	{
    	    Sid::String exp("foo\\,barbaz");
    	    exp.markAsBinary();
    	    checkAppend(test, "baz", exp);
    	}
    	{
    	    // Strange corner case; appending a non-binary with unescaped stuff
    	    // to a binary with escaped stuff. My opinion is that the behavior
    	    // below is not correct, but the test characterizes it nonetheless.
    	    Sid::String exp("foo\\,bar,bar");
    	    exp.markAsBinary();
    	    checkAppend(test, ",bar", exp);
    	}
        }
        {
    	// Strange corner case; appending a binary with escaped stuff to a 
    	// non-binary with non-escaped stuff. My opinion is that the behavior
    	// below is not correct, but the test characterizes it nonetheless.
    	Sid::String append("bar\\,baz");
        append.markAsBinary();
        checkAppend("foo,bar", append, "foo,barbar\\,baz");
    }
}

void StringTestCase::testOwnership()
{
    // Test shallow copy semantics
    {
        Sid::String orig("foo bar");
        Sid::String shallowCopy(orig);
        CPPUNIT_ASSERT_MESSAGE("Shallow copy semantics for copy c'tor not working!",
                                    orig.data() == shallowCopy.data());
        Sid::String assigned;
        assigned = orig;
        CPPUNIT_ASSERT_MESSAGE("Shallow copy semantics for assignment operator"
                                    " not working!",
                                    orig.data() == assigned.data());
    }

    // Test copy-on-write semantics
    {
        Sid::String orig("foo bar");
        {
            Sid::String copy(orig);
            copy+=" baz";
            CPPUNIT_ASSERT_MESSAGE("Copy-on-write semantics not working for "
                                    "operator+=(const char*).",
                                    copy != orig);
        }
        {
            Sid::String copy(orig);
            copy+=Sid::String("baz");
            CPPUNIT_ASSERT_MESSAGE("Copy-on-write semantics not working for "
                                    "operator+=(const Sid::String&).",
                                    copy != orig);
        }
        {
            Sid::String copy(orig);
            copy=Sid::String("baz");
            CPPUNIT_ASSERT_MESSAGE("Copy-on-write semantics not working for "
                                    "operator=(const Sid::String&).",
                                    copy != orig);
        }
        {
            Sid::String copy(orig);
            copy="baz";
            CPPUNIT_ASSERT_MESSAGE("Copy-on-write semantics not working for "
                                    "operator=(const char*).",
                                    copy != orig);
        }
    }
    
    // Test deepCopy()
    {
        Sid::String orig("foobar");
        const char* origData = orig.data();
        Sid::String dCopy = orig.deepCopy();
        CPPUNIT_ASSERT_MESSAGE("deepCopy() yielded a shallow copy!",
                                orig.data() != dCopy.data());
        CPPUNIT_ASSERT_MESSAGE("deepCopy() caused original to relinquish its reference!",
                                orig.data() == origData);
        Sid::String copy(orig);
        CPPUNIT_ASSERT_MESSAGE("deepCopy() caused original to relinquish its reference!",
                                orig.data() == origData);
    }

}

void StringTestCase::testHash()
{
    CPPUNIT_ASSERT(Sid::String().hash() == 0);
    CPPUNIT_ASSERT(Sid::String().hash(16) == 0);
    CPPUNIT_ASSERT(Sid::String("").hash() == 0);
    CPPUNIT_ASSERT(Sid::String("").hash(16) == 0);
    CPPUNIT_ASSERT(Sid::String("fOo").hash(1024) != Sid::String("Foo").hash(1024));
    {
        Sid::String test = Sid::String::from("\xff\xf0\x00\x90",4);
        Sid::String test2 = Sid::String::from("\xfb\xf0\x00\x90",4);
    	// !bwc! This hash seems to fare poorly with small differences, this is the
    	// smallest modulus (that is a power of two) that yields different results.
    	// The 19 least significant bits are identical. Might be worth considering
    	// updating the hash function with something a little nicer.
    	CPPUNIT_ASSERT(test.hash(0x080000) != test2.hash(0x080000));
    }
}

void checkFind(const Sid::String& test, 
    			char toFind,
    			int expected)
{
    int res = test.find(toFind);
    CPPUNIT_ASSERT_PRINTF(res == expected,
    						"find(%u) yielded unexpected result %d for string:\n"
    						"%s\n"
    						"expected %d\n",
    						toFind,
    						res,
    						test.isNull() ? "(NULL)" : test.data(),
    						expected);
}

void checkFind(const Sid::String& test,
    			int start,
    			char toFind,
    			int expected)
{
    int res = test.find(start, toFind);
    CPPUNIT_ASSERT_PRINTF(res == expected,
    						"find(%d, %u) yielded unexpected result %d for string:\n"
    						"%s\n"
    						"expected %d\n",
    						start,
    						toFind,
    						res,
    						test.isNull() ? "(NULL)" : test.data(),
    						expected);
}

void StringTestCase::testFind()
{
    checkFind(Sid::String(), '\0', -1);
    checkFind(Sid::String(), 'a', -1);
    checkFind("", '\0', 0);
    checkFind("", 'a', -1);
    checkFind("foobar", '\0', 6);
    checkFind("foobar", 'f', 0);
    checkFind("foobar", 'o', 1);
    checkFind("foobar", 'b', 3);
    checkFind("foobar", 'a', 4);
    checkFind("foobar", 'r', 5);

    checkFind(Sid::String(), 0, '\0', -1);
    checkFind(Sid::String(), 0, 'a', -1);
    checkFind(Sid::String(), 3, '\0', -1);
    checkFind(Sid::String(), 3, 'a', -1);
    checkFind("foobar", 3, '\0', 6);
    checkFind("foobar", 3, 'f',  -1);
    checkFind("foobar", 2, 'o',  2);
    checkFind("foobar", 3, 'o',  -1);
    checkFind("foobar", 3, 'b',  3);
    checkFind("foobar", 3, 'a',  4);
    checkFind("foobar", 3, 'r',  5);

}

#define checkFormat(expected, format, ...) \
    { \
    Sid::String test; \
    CPPUNIT_ASSERT(test.Format(format, __VA_ARGS__) == expected);\
    }

void StringTestCase::testFormat()
{
    checkFormat("10", "%d", 10);
    checkFormat("foobar", "%s", (const char*)"foobar");
    checkFormat("foo:15:a", "%s:%d:%c", "foo", 15, 'a');
    {
    	static int size = 2048;
    	char buf[size];
    	memset(buf, 'f', sizeof(buf));
    	buf[size-1]=0;
    	checkFormat(buf, "%s", (const char*)buf);
    }
}
