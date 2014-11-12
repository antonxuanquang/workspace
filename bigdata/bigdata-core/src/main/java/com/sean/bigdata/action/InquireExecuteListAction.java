package com.sean.bigdata.action;

import java.util.List;

import com.sean.bigdata.bean.ExecuteBean;
import com.sean.bigdata.constant.M;
import com.sean.bigdata.constant.P;
import com.sean.bigdata.constant.R;
import com.sean.bigdata.entity.ExecuteEntity;
import com.sean.common.ioc.ResourceConfig;
import com.sean.service.annotation.ActionConfig;
import com.sean.service.annotation.DescriptConfig;
import com.sean.service.annotation.MustParamsConfig;
import com.sean.service.annotation.ReturnParamsConfig;
import com.sean.service.core.Action;
import com.sean.service.core.Session;

@ActionConfig(module = M.class, authenticate = false)
@MustParamsConfig({ P.reportId, P.yearOrMonth })
@ReturnParamsConfig({ R.executeList })
@DescriptConfig("查询报表执行列表")
public class InquireExecuteListAction extends Action
{
	@ResourceConfig
	private ExecuteBean executeBean;

	@Override
	public void execute(Session session) throws Exception
	{
		long reportId = session.getLongParameter(P.reportId);
		long yearOrMonth = session.getLongParameter(P.yearOrMonth);
		List<ExecuteEntity> list = executeBean.getExecuteList(reportId, yearOrMonth);

		session.setReturnAttribute(R.executeList, list);
		session.success();
	}
}
