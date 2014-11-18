package com.sean.bigdata.action;

import com.sean.bigdata.bean.UserBean;
import com.sean.bigdata.constant.A;
import com.sean.bigdata.constant.M;
import com.sean.bigdata.constant.R;
import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(module = M.class, permission = A.Admin)
@ReturnParamsConfig({ R.userList })
@DescriptConfig("查询用户列表")
public class InquireUserListAction extends Action
{
	@ResourceConfig
	private UserBean userBean;

	@Override
	public void execute(Session session) throws Exception
	{
		session.setReturnAttribute(R.userList, userBean.getUserList());
		session.success();
	}
}
