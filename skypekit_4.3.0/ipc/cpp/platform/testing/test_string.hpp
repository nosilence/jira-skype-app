#ifndef test_string_INCLUDED_HPP
#define test_string_INCLUDED_HPP

#include <cppunit/TestCase.h>
#include <cppunit/extensions/HelperMacros.h>
#include "SidPlatform.hpp"

class StringTestCase : public CppUnit::TestCase
{

CPPUNIT_TEST_SUITE(StringTestCase);
CPPUNIT_TEST(testComparisons);
CPPUNIT_TEST(createTest);
CPPUNIT_TEST(assignTest);
CPPUNIT_TEST(operatorTest);
CPPUNIT_TEST(getBufTest);
CPPUNIT_TEST(appendTest);
CPPUNIT_TEST(getSizeTest);
CPPUNIT_TEST(substrTest);
CPPUNIT_TEST(escapingAndUnescapingTest);
CPPUNIT_TEST(testKeyValue);
CPPUNIT_TEST(testConversion);
CPPUNIT_TEST(testStartWith);
CPPUNIT_TEST(testRight);
CPPUNIT_TEST(testTrim);
CPPUNIT_TEST(testFrom);
CPPUNIT_TEST(testTo);
CPPUNIT_TEST(testOwnership);
CPPUNIT_TEST(testHash);
CPPUNIT_TEST(testFind);
CPPUNIT_TEST(testFormat);
CPPUNIT_TEST_SUITE_END();

protected:

	void testComparisons();
	void createTest(); 
	void assignTest();
	void operatorTest();
	void getBufTest();
	void appendTest();
	void getSizeTest();
	void substrTest();
	void escapingAndUnescapingTest();
	void testKeyValue();
	void testConversion();
	void testStartWith();
	void testRight();
	void testTrim();
	void testFrom();
    void testTo();
    void testOwnership();
    void testHash();
	void testFind();
	void testFormat();

};


#endif //test_string_INCLUDED_HPP
