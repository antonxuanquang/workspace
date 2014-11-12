package com.sean.im.friend.constant;

import com.sean.service.annotation.ParameterConfig;
import com.sean.service.annotation.ParameterProviderConfig;
import com.sean.service.enums.DataType;

@ParameterProviderConfig(description = "IM参数列表")
public class Params
{
	@ParameterConfig(length = 20, dataType = DataType.String, description = "分组名称")
	public static final String groupName = "groupName";

	@ParameterConfig(dataType = DataType.Long, description = "分组ID")
	public static final String groupId = "groupId";

	@ParameterConfig(dataType = DataType.Long, description = "好友数据库记录ID")
	public static final String friendId = "friendId";

	@ParameterConfig(dataType = DataType.Long, description = "接受人ID")
	public static final String receiverId = "receiverId";

	@ParameterConfig(length = 256, dataType = DataType.String, description = "消息内容")
	public static final String content = "content";

	@ParameterConfig(length = 256, dataType = DataType.String, description = "消息原文")
	public static final String original = "original";

	@ParameterConfig(dataType = DataType.String, length = 128, description = "请求好友备注")
	public static final String remark = "remark";

	@ParameterConfig(dataType = DataType.String, length = 32, description = "好友备注")
	public static final String remarkName = "remarkName";

	@ParameterConfig(dataType = DataType.Int, description = "处理好友请求，1同意添加，0拒绝添加")
	public static final String requestOperate = "requestOperate";
	
	
	@ParameterConfig(length = 16, dataType = DataType.String, description = "用户名")
	public static final String username = "username";

	@ParameterConfig(length = 16, dataType = DataType.String, description = "昵称")
	public static final String nickname = "nickname";

	@ParameterConfig(length = 32, dataType = DataType.String, description = "用户原始密码md5密文")
	public static final String oldpassword = "oldpassword";
	
	@ParameterConfig(length = 32, dataType = DataType.String, description = "用户密码md5密文")
	public static final String password = "password";

	@ParameterConfig(dataType = DataType.Int, description = "用户状态")
	public static final String status = "status";

	@ParameterConfig(dataType = DataType.Int, description = "国家")
	public static final String country = "country";

	@ParameterConfig(dataType = DataType.Int, description = "语言")
	public static final String language = "language";

	@ParameterConfig(length = 10, dataType = DataType.String, description = "翻译")
	public static final String translator = "translator";

	@ParameterConfig(dataType = DataType.Int, description = "头像")
	public static final String head = "head";
	
	@ParameterConfig(dataType = DataType.Long, description = "头像")
	public static final String iconId = "iconId";
	
	@ParameterConfig(dataType = DataType.String, description = "头像")
	public static final String iconImgUrl = "iconImgUrl";
	
	@ParameterConfig(dataType = DataType.String, description = "头像")
	public static final String iconUrl = "iconUrl";
	
	@ParameterConfig(dataType = DataType.Int, description = "头像")
	public static final String iconVisible = "iconVisible";
}
