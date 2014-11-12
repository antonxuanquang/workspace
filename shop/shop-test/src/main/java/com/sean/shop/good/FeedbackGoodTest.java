package com.sean.shop.good;

import com.sean.shop.good.action.FeedbackGoodAction;
import com.sean.shop.good.constant.P;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(FeedbackGoodAction.class)
public interface FeedbackGoodTest
{
	@TestCaseConfig
	@ParameterConfig(name = P.goodId, value = "1")
	public void testcase1();
}
