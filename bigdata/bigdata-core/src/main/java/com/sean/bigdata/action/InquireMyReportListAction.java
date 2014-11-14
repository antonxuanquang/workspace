package com.sean.bigdata.action;

import com.sean.bigdata.bean.AclBean;
import com.sean.bigdata.bean.ReportBean;
import com.sean.bigdata.constant.M;
import com.sean.bigdata.constant.P;
import com.sean.bigdata.constant.R;
import com.sean.bigdata.entity.AclEntity;
import com.sean.common.ioc.ResourceConfig;
import com.sean.persist.core.PageData;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(module = M.class)
@MustParamsConfig({ P.pageNo })
@ReturnParamsConfig({ R.reportList, R.totalrecord })
@DescriptConfig("查询我的报表列表")
public class InquireMyReportListAction extends Action
{
	@ResourceConfig
	private ReportBean reportBean;
	@ResourceConfig
	private AclBean aclBean;

	@Override
	public void execute(Session session) throws Exception
	{
		int pageNo = session.getIntParameter(P.pageNo);

		PageData<AclEntity> aclList = aclBean.getAclList(session.getUserId(), pageNo, 25);

		session.setReturnAttribute(R.reportList, reportBean.getReportList(aclList.getDatas()));
		session.setReturnAttribute(R.totalrecord, aclList.getTotalrecords());
		session.success();
	}
}
