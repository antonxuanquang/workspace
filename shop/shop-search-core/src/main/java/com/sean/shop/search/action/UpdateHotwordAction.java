package com.sean.shop.search.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.search.bean.SearchKeyBeanImpl;
import com.sean.shop.search.constant.M;
import com.sean.shop.search.constant.P;

@ActionConfig(module = M.class, authenticate = false, password = "97igo")
@MustParamsConfig({ P.password })
@DescriptConfig("重新计算搜索热词")
public class UpdateHotwordAction extends Action
{
	@ResourceConfig
	private SearchKeyBeanImpl searchKeyBean;

	@Override
	public void execute(Session session) throws Exception
	{
		searchKeyBean.caculateHotword();
		session.success();
	}
}
