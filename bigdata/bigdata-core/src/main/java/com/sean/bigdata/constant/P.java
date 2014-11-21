package com.sean.bigdata.constant;

import com.sean.service.annotation.ParameterConfig;
import com.sean.service.annotation.ParameterProviderConfig;
import com.sean.service.enums.DataType;
import com.sean.service.enums.ParameterType;

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

	@ParameterConfig(dataType = DataType.Long, type = ParameterType.Batch, descr = "用户ID列表")
	public static final String userList = "userList";

	@ParameterConfig(dataType = DataType.String, descr = "报表名称")
	public static final String reportName = "reportName";

	@ParameterConfig(dataType = DataType.String, descr = "x轴说明")
	public static final String xAxis = "xAxis";

	@ParameterConfig(dataType = DataType.String, descr = "y轴说明")
	public static final String yAxis = "yAxis";

	@ParameterConfig(dataType = DataType.String, descr = "条件分组, 只用于列表报表, 多个用;隔开")
	public static final String conditions = "conditions";

	@ParameterConfig(dataType = DataType.Enum, enumVals = { "1", "2", "3" }, descr = "报表类型:1-单值, 2-数值, 3-列表")
	public static final String type = "type";

	@ParameterConfig(dataType = DataType.Enum, enumVals = { "1", "2" }, descr = "统计类型:1-日统计, 2-月统计")
	public static final String countType = "countType";
}
