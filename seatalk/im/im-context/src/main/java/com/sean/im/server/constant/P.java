package com.sean.im.server.constant;

import com.sean.service.annotation.ParameterConfig;
import com.sean.service.annotation.ParameterProviderConfig;
import com.sean.service.enums.DataType;

@ParameterProviderConfig(description = "IM参数列表")
public class P
{
	@ParameterConfig(dataType = DataType.Long, description = "当前登录用户ID")
	public static final String loginerId = "loginerId";

	@ParameterConfig(dataType = DataType.Long, description = "用户ID")
	public static final String userId = "userId";

	@ParameterConfig(dataType = DataType.String, description = "版本号")
	public static final String version = "version";

	@ParameterConfig(dataType = DataType.Int, description = "页码")
	public static final String pageNo = "pageNo";

	@ParameterConfig(dataType = DataType.Long, description = "文件ID")
	public static final String fileId = "fileId";

	@ParameterConfig(dataType = DataType.String, length = 64, description = "文件名称")
	public static final String filename = "filename";

	@ParameterConfig(dataType = DataType.String, description = "图片地址")
	public static final String imgUrl = "imgUrl";

	@ParameterConfig(dataType = DataType.String, description = "语音文件地址")
	public static final String audioUrl = "audioUrl";
	
	@ParameterConfig(dataType = DataType.String, description = "语音文件地址")
	public static final String adChatformLink = "adChatformLink";
	
	@ParameterConfig(dataType = DataType.String, description = "语音文件地址")
	public static final String adChatformImgUrl = "adChatformImgUrl";

	@ParameterConfig(length = 20, dataType = DataType.String, description = "电话")
	public static final String tel = "tel";

	@ParameterConfig(length = 50, dataType = DataType.String, description = "邮箱")
	public static final String mail = "mail";

	@ParameterConfig(length = 256, dataType = DataType.String, description = "个人说明")
	public static final String description = "description";

	@ParameterConfig(dataType = DataType.Int, description = "性别")
	public static final String sex = "sex";

	@ParameterConfig(dataType = DataType.Int, description = "年龄")
	public static final String age = "age";

	@ParameterConfig(dataType = DataType.String, length = 20, description = "姓名")
	public static final String name = "name";
	
	@ParameterConfig(length = 128, dataType = DataType.String, description = "签名")
	public static final String signature = "signature";
}
