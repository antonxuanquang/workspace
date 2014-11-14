package com.sean.bigdata.constant;

import com.sean.service.annotation.ParameterConfig;
import com.sean.service.annotation.ParameterProviderConfig;
import com.sean.service.enums.DataType;

@ParameterProviderConfig(module = M.class, descr = "商品参数")
public class P
{
	@ParameterConfig(dataType = DataType.Int, descr = "页码")
	public static final String pageNo = "pageNo";
	
	@ParameterConfig(dataType = DataType.Long, descr = "报表id")
	public static final String reportId = "reportId";
	
	@ParameterConfig(dataType = DataType.Long, descr = "年份或者月份, yyyyMM, yyyy")
	public static final String yearOrMonth = "yearOrMonth";
	
	@ParameterConfig(dataType = DataType.String, descr = "帐号")
	public static final String username = "username";
	
	@ParameterConfig(dataType = DataType.String, descr = "密码")
	public static final String password = "password";
}
