package com.sean.shop.good;

import com.sean.shop.good.action.UpdateGoodStatusAction;
import com.sean.shop.good.constant.P;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(UpdateGoodStatusAction.class)
public interface UpdateGoodStatusTest
{
	@TestCaseConfig
	@ParameterConfig(name = P.goodId, value = "3")
	@ParameterConfig(name = P.status, value = "2")
	public void testcase1();
}
