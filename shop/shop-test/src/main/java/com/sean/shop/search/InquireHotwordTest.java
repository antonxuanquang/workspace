package com.sean.shop.search;

import com.sean.shop.search.action.InquireHotwordAction;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(InquireHotwordAction.class)
public interface InquireHotwordTest
{
	@TestCaseConfig
	public void testcase1();
}
