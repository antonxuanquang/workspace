package com.sean.im.flock.constant;

import com.sean.service.annotation.ParameterConfig;
import com.sean.service.annotation.ParameterProviderConfig;
import com.sean.service.enums.DataType;
import com.sean.service.enums.ParameterType;

@ParameterProviderConfig(description = "IM群模块参数列表")
public class Params
{
	@ParameterConfig(length = 20, dataType = DataType.String, description = "群名称")
	public static final String flockName = "flockName";

	@ParameterConfig(length = 128, dataType = DataType.String, description = "群签名")
	public static final String flockSignature = "flockSignature";

	@ParameterConfig(length = 512, dataType = DataType.String, description = "群简介")
	public static final String flockDescr = "flockDescr";

	@ParameterConfig(dataType = DataType.Long, description = "群ID")
	public static final String flockId = "flockId";

	@ParameterConfig(dataType = DataType.Long, description = "群成员ID")
	public static final String flockMemberId = "flockMemberId";

	@ParameterConfig(dataType = DataType.Long, description = "群成员ID列表", type = ParameterType.Batch)
	public static final String flockMemberIdList = "flockMemberIdList";

	@ParameterConfig(length = 256, dataType = DataType.String, description = "群消息内容")
	public static final String flockMessageContent = "flockMessageContent";
}
