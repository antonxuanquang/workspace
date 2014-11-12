package com.sean.service.constant;

import com.sean.service.annotation.ReturnParameterConfig;
import com.sean.service.annotation.ReturnParameterProviderConfig;
import com.sean.service.enums.Format;

@ReturnParameterProviderConfig(description = "框架内置接口返回参数列表")
public class _RP
{
	@ReturnParameterConfig(format = Format.String, description = "链接主机ip")
	public static final String hostname = "hostname";
	@ReturnParameterConfig(format = Format.Numeric, description = "链接主机端口")
	public static final String port = "port";
}
