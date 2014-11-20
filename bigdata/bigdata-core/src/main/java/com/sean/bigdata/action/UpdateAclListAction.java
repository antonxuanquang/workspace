package com.sean.bigdata.action;

import com.sean.bigdata.bean.AclBean;
import com.sean.bigdata.constant.A;
import com.sean.bigdata.constant.M;
import com.sean.bigdata.constant.P;
import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(module = M.class, permission = A.Admin)
@MustParamsConfig({ P.reportId, P.userList })
@DescriptConfig("修改报表访问权限")
public class UpdateAclListAction extends Action
{
	@ResourceConfig
	private AclBean aclBean;

	@Override
	public void execute(Session session) throws Exception
	{
		long reportId = session.getLongParameter(P.reportId);
		long[] userList = session.getLongParameters(P.userList);
		aclBean.updateAclList(reportId, userList);
		session.success();
	}
}
