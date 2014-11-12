package com.sean.shop.search;

import com.sean.shop.search.action.UpdateCategoryAction;
import com.sean.shop.search.constant.P;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(UpdateCategoryAction.class)
public interface UpdateCategoryTest
{
	@TestCaseConfig
	@ParameterConfig(name = P.password, value = "97igo")
	@ParameterConfig(name = P.forceUpdate, value = "1")
	public void testcase1();
}
