package com.sean.shop.good;

import com.sean.shop.good.action.UpdateGoodAction;
import com.sean.shop.good.constant.P;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(UpdateGoodAction.class)
public interface UpdateGoodTest
{
	@TestCaseConfig
	@ParameterConfig(name = P.goodId, value = "3")
	@ParameterConfig(name = P.goodName, value = "key wordsdfsdf")
	@ParameterConfig(name = P.price, value = "6.5")
	@ParameterConfig(name = P.categoryId, value = "1")
	@ParameterConfig(name = P.imageUrl, value = "http://www.baidu.com")
	@ParameterConfig(name = P.goodUrl, value = "http://www.baidu.com")
	@ParameterConfig(name = P.channel, value = "1")
	@ParameterConfig(name = P.keyword, value = "key wordsdfsdf")
	@ParameterConfig(name = P.boost, value = "1.2")
	public void testcase1();
}
