package com.sean.bigdata.test;

import com.sean.bigdata.action.InquireExecuteListAction;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(InquireExecuteListAction.class)
public interface InquireExecuteListTest
{
	@TestCaseConfig
	@ParameterConfig(name = "reportId", value = "1")
	@ParameterConfig(name = "yearOrMonth", value = "201411")
	public void testcase1();
}
