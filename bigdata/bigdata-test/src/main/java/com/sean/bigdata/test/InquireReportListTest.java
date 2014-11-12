package com.sean.bigdata.test;

import com.sean.bigdata.action.InquireReportListAction;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(InquireReportListAction.class)
public interface InquireReportListTest
{
	@TestCaseConfig
	@ParameterConfig(name = "pageNo", value = "1")
	public void testcase1();
}
