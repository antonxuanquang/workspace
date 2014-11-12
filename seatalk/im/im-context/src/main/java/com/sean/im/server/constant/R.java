package com.sean.im.server.constant;

import com.sean.service.annotation.ReturnParameterConfig;
import com.sean.service.annotation.ReturnParameterProviderConfig;
import com.sean.service.enums.Format;

@ReturnParameterProviderConfig(description = "模块1返回参数列表")
public class R
{
	@ReturnParameterConfig(format = Format.Numeric, description = "总记录数")
	public static final String totalrecords = "totalrecords";
}
