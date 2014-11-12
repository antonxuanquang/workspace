package com.sean.shop.good;

import com.sean.shop.good.action.InquireCategoryAction;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(InquireCategoryAction.class)
public interface InquireCategoryTest
{
	@TestCaseConfig
	public void testcase1();
}
