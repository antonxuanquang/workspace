package com.sean.im.commom.constant;

/**
 * 消息类型
 * @author sean
 */
public class MessageEnum
{
	// 文本消息
	public static final int Message_Text = 1;
	// 图片消息
	public static final int Message_Image = 2;
	// 语音消息
	public static final int Message_Audio = 3;
	// 语音视频
	public static final int Message_Video = 4;

	// 请求添加好友
	public static final int Message_RequestFriend = 11;
	// 同意添加好友
	public static final int Message_AgreeRequestFriend = 12;
	// 拒绝添加好友
	public static final int Message_RefuseRequestFriend = 13;
	// 设为管理员
	public static final int Message_GrantFlockAdmin = 14;
	// 收回管理员
	public static final int Message_TakeBackFlockAdmin = 15;
	// 移除群
	public static final int Message_KickOutFlock = 16;
	// 加入群
	public static final int Message_JoinInFlock = 17;
	// 解散群
	public static final int Message_DismissFlock = 18;
}
