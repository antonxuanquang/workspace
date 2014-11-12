package com.sean.shop.search.action;

import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.search.bean.SearchKeyBeanImpl;
import com.sean.shop.search.constant.M;
import com.sean.shop.search.constant.R;

@ActionConfig(module = M.class, authenticate = false)
@ReturnParamsConfig({ R.hotwordList })
@DescriptConfig("查询搜索热词")
public class InquireHotwordAction extends Action
{
	@ResourceConfig
	private SearchKeyBeanImpl searchKeyBean;

	@Override
	public void execute(Session session) throws Exception
	{
		session.setReturnAttribute(R.hotwordList, searchKeyBean.getHotword());
		session.success();
	}
}
