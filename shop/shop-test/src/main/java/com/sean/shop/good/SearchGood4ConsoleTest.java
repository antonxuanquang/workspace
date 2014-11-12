package com.sean.shop.good;

import com.sean.service.annotation.DescriptConfig;
import com.sean.shop.good.action.SearchGood4ConsoleAction;
import com.sean.shop.good.constant.P;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(SearchGood4ConsoleAction.class)
@DescriptConfig("搜索商品测试套件")
public interface SearchGood4ConsoleTest
{
	@TestCaseConfig
	@DescriptConfig("搜索商品测试用力1")
	@ParameterConfig(name = P.keyword, value = "美肤宝")
	@ParameterConfig(name = P.pageNo, value = "1")
	@ParameterConfig(name = P.channel, value = "1")
	public void testcase1();
}
