package com.sean.bigdata.action;

import com.sean.bigdata.bean.ReportBean;
import com.sean.bigdata.constant.A;
import com.sean.bigdata.constant.M;
import com.sean.bigdata.constant.P;
import com.sean.bigdata.entity.ReportEntity;
import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.OptionalParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(module = M.class, permission = A.Admin)
@MustParamsConfig({ P.reportName, P.xAxis, P.yAxis, P.type, P.countType })
@OptionalParamsConfig({ P.conditions })
@DescriptConfig("创建报表")
public class CreateReportAction extends Action
{
	@ResourceConfig
	private ReportBean reportBean;

	@Override
	public void execute(Session session) throws Exception
	{
		ReportEntity report = new ReportEntity();
		session.fillSingleEntity(report);
		reportBean.createReport(report, session.getUserId());
		session.success();
	}
}