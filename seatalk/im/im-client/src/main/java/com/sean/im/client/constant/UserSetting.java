package com.sean.im.client.constant;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.sean.im.client.form.MainForm;
import com.sean.im.commom.entity.Friend;

/**
 * 用户配置信息
 * @author sean
 */
public class UserSetting implements Serializable
{
	private static final long serialVersionUID = 1L;

	// 发送消息方式，1代表Enter，2代表Ctrl + Enter
	public int Send_Mode = 1;
	// 是否显示聊天原文
	public int Is_Show_Original = 0;
	// 最近联系人
	public List<Long> recentFriends;
	// 是否显示翻译提示
	public boolean isShowTraslatorNotify = true;
	// 窗体透明度
	public float opacity = 1.0f;
	// 皮肤
	public String skin = Global.Root + "resource/skin/ubuntu/ubuntu.jpg";

	public UserSetting getSetting()
	{
		if (recentFriends == null)
		{
			recentFriends = new LinkedList<Long>();
		}
		else
		{
			recentFriends.clear();
		}
		for (Friend friend : MainForm.FORM.getRecentFriendList().getFriends())
		{
			recentFriends.add(friend.getId());
		}
		return this;
	}
}
