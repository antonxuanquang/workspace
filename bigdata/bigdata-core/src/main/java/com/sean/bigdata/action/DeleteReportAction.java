package com.sean.bigdata.action;

import com.sean.bigdata.bean.ReportBean;
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
@MustParamsConfig({ P.reportId })
@DescriptConfig("删除报表")
public class DeleteReportAction extends Action
{
	@ResourceConfig
	private ReportBean reportBean;

	@Override
	public void execute(Session session) throws Exception
	{
		long reportId = session.getLongParameter(P.reportId);
		reportBean.deleteReport(reportId);
		session.success();
	}
}
