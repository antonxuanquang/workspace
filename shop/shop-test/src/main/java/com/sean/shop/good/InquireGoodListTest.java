package com.sean.shop.good;

import com.sean.shop.good.action.InquireGoodListAction;
import com.sean.shop.good.constant.P;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(InquireGoodListAction.class)
public interface InquireGoodListTest
{
	@TestCaseConfig
	@ParameterConfig(name = P.channel, value = "1")
	@ParameterConfig(name = P.pageNo, value = "1")
	public void testcase1();
}
