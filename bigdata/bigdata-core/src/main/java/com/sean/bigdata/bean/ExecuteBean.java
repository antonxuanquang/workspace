package com.sean.bigdata.bean;

import java.util.ArrayList;
import java.util.List;

import com.sean.bigdata.entity.ExecuteEntity;
import com.sean.bigdata.entity.ReportEntity;
import com.sean.common.ioc.BeanConfig;
import com.sean.common.ioc.ResourceConfig;
import com.sean.persist.core.Dao;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.enums.OrderEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Order;

@BeanConfig("")
public class ExecuteBean
{
	@ResourceConfig
	private ReportBean reportBean;

	public List<ExecuteEntity> getExecuteList(long reportId, long yearOrMonth)
	{
		ReportEntity report = reportBean.getReportById(reportId);
		if (report != null)
		{
			long start = 0, end = 0;
			// 日统计
			if (report.countType == 1)
			{
				long month = yearOrMonth;
				start = month * 100000000L;
				end = (month + 1) * 100000000L;	
			}
			// 月统计
			else if (report.countType == 2)
			{
				long year = yearOrMonth;
				start = year * 10000000000L;
				end = (year + 1) * 10000000000L;	
			}
			
			List<Condition> conds = new ArrayList<>(3);
			conds.add(new Condition("reportId", reportId));
			conds.add(new Condition("executeTime", ConditionEnum.Greater_Equal, start));
			conds.add(new Condition("executeTime", ConditionEnum.Less, end));
			return Dao.getListByCond(ExecuteEntity.class, conds, new Order("executeId", OrderEnum.Desc));	
		}
		return null;
	}
}
