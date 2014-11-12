package com.sean.shop.good;

import com.sean.shop.good.action.DeleteGoodAction;
import com.sean.shop.good.constant.P;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(DeleteGoodAction.class)
public interface DeleteGoodTest
{
	@TestCaseConfig
	@ParameterConfig(name = P.goodId, value = "1")
	public void testcase1();
}
