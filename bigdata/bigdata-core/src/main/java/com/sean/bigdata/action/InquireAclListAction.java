package com.sean.bigdata.action;

import com.sean.bigdata.bean.AclBean;
import com.sean.bigdata.constant.A;
import com.sean.bigdata.constant.M;
import com.sean.bigdata.constant.P;
import com.sean.bigdata.constant.R;
import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(module = M.class, permission = A.Admin)
@MustParamsConfig({ P.reportId })
@ReturnParamsConfig({ R.aclList })
@DescriptConfig("查询报表权限")
public class InquireAclListAction extends Action
{
	@ResourceConfig
	private AclBean aclBean;

	@Override
	public void execute(Session session) throws Exception
	{
		long reportId = session.getLongParameter(P.reportId);
		session.setReturnAttribute(R.aclList, aclBean.getAclList(reportId));
		session.success();
	}
}
