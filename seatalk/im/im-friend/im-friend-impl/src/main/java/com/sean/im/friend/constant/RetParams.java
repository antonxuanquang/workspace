package com.sean.im.friend.constant;

import com.sean.im.account.dic.UserDic;
import com.sean.im.friend.entity.FriendEntity;
import com.sean.im.friend.entity.GroupEntity;
import com.sean.im.friend.entity.IconEntity;
import com.sean.im.friend.entity.MessageEntity;
import com.sean.im.friend.entity.UserInfoEntity;
import com.sean.service.annotation.ReturnParameterConfig;
import com.sean.service.annotation.ReturnParameterProviderConfig;
import com.sean.service.annotation.UseDicConfig;
import com.sean.service.enums.Format;

@ReturnParameterProviderConfig(description = "模块1返回参数列表")
public class RetParams
{
	@ReturnParameterConfig(format = Format.Numeric, description = "请求添加好友结果，1成功，0重复添加")
	public static final String requestrs = "requestrs";

	@ReturnParameterConfig(format = Format.EntityList, entity = GroupEntity.class, description = "用户分组列表", fields = { "id", "name", "isDefault" })
	public static final String grouplist = "grouplist";

	@ReturnParameterConfig(format = Format.EntityList, entity = FriendEntity.class, description = "用户好友列表", fields = { "id", "friendId", "groupId",
			"username", "nickname", "remark", "head", "status", "signature" }, dics = { @UseDicConfig(dic = UserDic.class, field = "friendId") })
	public static final String friendlist = "friendlist";

	@ReturnParameterConfig(format = Format.Numeric, description = "分组ID")
	public static final String groupId = "groupId";

	@ReturnParameterConfig(format = Format.EntityList, entity = MessageEntity.class, description = "消息", fields = { "id", "senderId", "receiverId",
			"sendTime", "content", "type" })
	public static final String msgs = "msgs";

	@ReturnParameterConfig(format = Format.Entity, entity = FriendEntity.class, description = "好友信息", fields = { "id", "friendId", "groupId",
			"username", "nickname", "remark", "head", "status", "signature" }, dics = { @UseDicConfig(dic = UserDic.class, field = "friendId") })
	public static final String friend = "friend";

	@ReturnParameterConfig(format = Format.Numeric, description = "登录结果，1成功，0失败，2版本过期")
	public static final String loginrs = "loginrs";

	@ReturnParameterConfig(format = Format.Numeric, description = "注册结果，1成功，0失败")
	public static final String registrs = "registrs";

	@ReturnParameterConfig(format = Format.Json, description = "聊天界面广告链接")
	public static final String adChatform = "adChatform";

	@ReturnParameterConfig(format = Format.EntityList, entity = IconEntity.class, description = "聊天界面广告链接", fields = { "id", "imgUrl", "link" })
	public static final String icons = "icons";

	@ReturnParameterConfig(format = Format.Numeric, description = "修改密码结果，1成功，0原始密码错误")
	public static final String changePasswordRs = "changePasswordRs";

	@ReturnParameterConfig(format = Format.Entity, entity = UserInfoEntity.class, description = "用户简短信息", fields = { "id", "nickname", "username",
			"head" })
	public static final String usershort = "usershort";

	@ReturnParameterConfig(format = Format.Entity, entity = UserInfoEntity.class, description = "用户简要信息", fields = { "id", "nickname", "username",
			"signature", "head", "status", "translator" })
	public static final String userbrief = "userbrief";

	@ReturnParameterConfig(format = Format.Entity, entity = UserInfoEntity.class, description = "用户完整信息", fields = { "id", "nickname", "username",
			"signature", "head", "country", "sex", "age", "tel", "mail", "language", "description" })
	public static final String userFull = "userFull";

	@ReturnParameterConfig(format = Format.EntityList, entity = UserInfoEntity.class, description = "用户列表完整信息", fields = { "id", "nickname",
			"username", "signature", "head", "country", "sex", "age", "tel", "mail", "language", "description", "status" })
	public static final String userlist = "userlist";

	@ReturnParameterConfig(format = Format.EntityList, entity = UserInfoEntity.class, description = "搜索好友结果用户", fields = { "id", "head", "username",
			"nickname", "signature" })
	public static final String searchUsers = "searchUsers";

	@ReturnParameterConfig(format = Format.Numeric, description = "在线总数")
	public static final String onlineCount = "onlineCount";

	@ReturnParameterConfig(format = Format.Json, description = "国家")
	public static final String country = "country";

	@ReturnParameterConfig(format = Format.Json, description = "语言")
	public static final String lan = "lan";

	@ReturnParameterConfig(format = Format.Json, description = "性别")
	public static final String gender = "gender";

	@ReturnParameterConfig(format = Format.Json, description = "age")
	public static final String age = "age";
}
