package com.sean.shop.good;

import com.sean.service.annotation.DescriptConfig;
import com.sean.shop.good.action.SearchGoodAction;
import com.sean.shop.good.constant.P;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(SearchGoodAction.class)
@DescriptConfig("搜索商品测试套件")
public interface SearchGoodTest
{
	@TestCaseConfig
	@DescriptConfig("搜索商品测试用力1")
	@ParameterConfig(name = P.keyword, value = "美肤宝")
	@ParameterConfig(name = P.pageNo, value = "1")
	@ParameterConfig(name = P.channel, value = "1")
	public void testcase1();

	@TestCaseConfig
	@ParameterConfig(name = P.keyword, value = "美肤宝")
	@ParameterConfig(name = P.pageNo, value = "1")
	@ParameterConfig(name = P.priceStart, value = "5")
	@ParameterConfig(name = P.priceEnd, value = "50")
	public void testcase2();

	@TestCaseConfig
	@ParameterConfig(name = P.keyword, value = "儿童玩具")
	@ParameterConfig(name = P.pageNo, value = "1")
	public void testcase3();

	@TestCaseConfig
	@ParameterConfig(name = P.keyword, value = "儿童玩具美容护肤")
	@ParameterConfig(name = P.pageNo, value = "1")
	public void testcase4();
}
