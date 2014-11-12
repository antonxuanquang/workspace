package com.sean.shop.good.action;

import com.alibaba.fastjson.JSON;
import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;
import com.sean.shop.good.bean.CategoryBeanImpl;
import com.sean.shop.good.constant.M;
import com.sean.shop.good.constant.R;

@ActionConfig(module = M.class, authenticate = false)
@ReturnParamsConfig({ R.categoryList })
@DescriptConfig("查询分类列表")
public class InquireCategoryAction extends Action
{
	@ResourceConfig
	private CategoryBeanImpl CategoryBean;

	@Override
	public void execute(Session session) throws Exception
	{
		String json = JSON.toJSONString(CategoryBean.getCategoryList());
		session.setReturnAttribute(R.categoryList, json);
		session.success();
	}
}
