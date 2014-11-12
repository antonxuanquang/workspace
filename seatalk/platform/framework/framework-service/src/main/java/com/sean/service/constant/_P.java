package com.sean.service.constant;

import com.sean.service.annotation.ParameterConfig;
import com.sean.service.annotation.ParameterProviderConfig;
import com.sean.service.enums.DataType;

/**
 * 框架内置参数，外部不得使用
 * @author sean
 */
@ParameterProviderConfig(description = "框架内置接口参数")
public class _P
{
	@ParameterConfig(dataType = DataType.String, description = "需要加载的css文件路径，多个文件用逗号隔开，路径需要从项目根路径开始") 
	public static final String css = "css";
	@ParameterConfig(dataType = DataType.String, description = "需要加载的javascript文件路径，多个文件用逗号隔开，路径需要从项目根路径开始") 
	public static final String js = "js";
	@ParameterConfig(dataType = DataType.Int, description = "客户端类型，1-sock，2-websock")
	public static final String clientType = "clientType";
}
