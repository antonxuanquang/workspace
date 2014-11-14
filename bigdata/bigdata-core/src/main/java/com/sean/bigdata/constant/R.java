package com.sean.bigdata.constant;

import com.sean.bigdata.entity.ExecuteEntity;
import com.sean.bigdata.entity.ReportEntity;
import com.sean.service.annotation.FieldsConfig;
import com.sean.service.annotation.ReturnParameterConfig;
import com.sean.service.annotation.ReturnParameterProviderConfig;
import com.sean.service.enums.Format;

@ReturnParameterProviderConfig(module = M.class, descr = "")
public class R
{
	@ReturnParameterConfig(format = Format.Numeric, descr = "报表ID")
	public static final String reportId = "reportId";

	@ReturnParameterConfig(format = Format.String, descr = "sid")
	public static final String sid = "sid";

	@ReturnParameterConfig(format = Format.String, descr = "encryptKey")
	public static final String encryptKey = "encryptKey";

	@ReturnParameterConfig(descr = "报表列表", format = Format.EntityList, entity = ReportEntity.class)
	@FieldsConfig("*")
	public static final String reportList = "reportList";

	@ReturnParameterConfig(descr = "报表", format = Format.Entity, entity = ReportEntity.class)
	@FieldsConfig("*")
	public static final String reportDetail = "reportDetail";

	@ReturnParameterConfig(descr = "报表执行列表", format = Format.EntityList, entity = ExecuteEntity.class)
	@FieldsConfig("*")
	public static final String executeList = "executeList";

	@ReturnParameterConfig(format = Format.Numeric, descr = "总记录数")
	public static final String totalrecord = "totalrecord";
}
