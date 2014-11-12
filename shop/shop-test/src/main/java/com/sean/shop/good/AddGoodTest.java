package com.sean.shop.good;

import com.sean.service.annotation.DescriptConfig;
import com.sean.shop.good.action.AddGoodAction;
import com.sean.shop.good.constant.P;
import com.sean.unittest.annotation.ParameterConfig;
import com.sean.unittest.annotation.TestCaseConfig;
import com.sean.unittest.annotation.TestSuiteConfig;

@TestSuiteConfig(AddGoodAction.class)
@DescriptConfig("添加商品测试")
public interface AddGoodTest
{
	@TestCaseConfig
	@ParameterConfig(name = P.goodName, value = "美肤宝 专柜正品 熊果苷嫩白面膜贴1片 美白 淡斑")
	@ParameterConfig(name = P.price, value = "6.5")
	@ParameterConfig(name = P.categoryId, value = "1")
	@ParameterConfig(name = P.imageUrl, value = "http://img03.taobaocdn.com/imgextra/i3/760929072/T2DpPqXBlXXXXXXXXX-760929072.jpg")
	@ParameterConfig(name = P.goodUrl, value = "http://detail.tmall.com/item.htm?id=12783717027&mt=")
	@ParameterConfig(name = P.channel, value = "1")
	@ParameterConfig(name = P.keyword, value = "美肤宝 专柜正品 熊果苷嫩白面膜贴1片 美白 淡斑 美容护肤")
	@ParameterConfig(name = P.boost, value = "1.2")
	public void testcase1();

	@TestCaseConfig
	@ParameterConfig(name = P.goodName, value = "TCL罗格朗开关插座面板五孔 二三插 仕界墙壁电源插旗舰店正品")
	@ParameterConfig(name = P.price, value = "7.9")
	@ParameterConfig(name = P.categoryId, value = "1")
	@ParameterConfig(name = P.imageUrl, value = "http://img03.taobaocdn.com/imgextra/i3/760929072/T2DpPqXBlXXXXXXXXX-760929072.jpg")
	@ParameterConfig(name = P.goodUrl, value = "http://detail.tmall.com/item.htm?id=12783717027&mt=")
	@ParameterConfig(name = P.channel, value = "1")
	@ParameterConfig(name = P.keyword, value = "TCL罗格朗开关插座面板五孔 二三插 仕界墙壁电源插旗舰店正品")
	@ParameterConfig(name = P.boost, value = "1.2")
	public void testcase2();

	@TestCaseConfig
	@ParameterConfig(name = P.goodName, value = "猫贝乐凹凸水晶有声挂图幼儿童玩具早教宝宝启蒙发声语音识字全套")
	@ParameterConfig(name = P.price, value = "16.5")
	@ParameterConfig(name = P.categoryId, value = "1")
	@ParameterConfig(name = P.imageUrl, value = "http://img02.taobaocdn.com/imgextra/i2/1629272459/TB2TEe7aXXXXXaTXpXXXXXXXXXX-1629272459.jpg")
	@ParameterConfig(name = P.goodUrl, value = "http://detail.tmall.com/item.htm?id=40584762511&spm=a1z02.1.1998049143.d4919530.vrbr0r&scm=1007.10157.1680.100200300000001&pvid=814f9d1c-4eff-4008-99a3-4764882f205f&mt=")
	@ParameterConfig(name = P.channel, value = "1")
	@ParameterConfig(name = P.keyword, value = "猫贝乐凹凸水晶有声挂图幼儿童玩具早教宝宝启蒙发声语音识字全套")
	@ParameterConfig(name = P.boost, value = "1.2")
	public void testcase3();
}
