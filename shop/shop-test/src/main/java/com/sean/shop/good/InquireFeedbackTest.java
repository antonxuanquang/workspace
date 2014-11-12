package com.sean.shop.good;

import com.sean.shop.good.action.InquireFeedbackAction;
import com.sean.shop.good.constant.P;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(InquireFeedbackAction.class)
public interface InquireFeedbackTest
{
	@TestCaseConfig
	@ParameterConfig(name = P.pageNo, value = "1")
	public void testcase1();
}
