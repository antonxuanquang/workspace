package com.sean.im.client.push.handler;

import javax.swing.JFrame;

import com.alibaba.fastjson.JSON;
import com.sean.im.client.comp.FriendListComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.core.PushHandler;
import com.sean.im.client.form.ChatForm;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.tray.TrayManager;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.client.util.MusicUtil;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Message;

/**
 * 接受好友信息
 * @author sean
 */
public class ReceiveMsgHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{
		Message msg = JSON.parseObject(notify.getParameter("msg"), Message.class);

		FriendListComp friendList = MainForm.FORM.getFriendList();
		Friend friend = friendList.getFriendByUserId(msg.getSenderId());
		ChatForm form = ChatFormCache.getChatForm(friend);

		// 如果聊天窗体已经打开 ，立即显示
		if (form.isVisible())
		{
			form.appendRightMessage(msg, ApplicationContext.getSender(friend));
			if (form.getState() == JFrame.ICONIFIED)
			{
				form.addUnReadMsg();
			}
		}
		// 压如消息队列
		else
		{
			ApplicationContext.CTX.getMessageQueue().add(msg);
			// 提示系统托盘闪烁
			TrayManager.getInstance().startLight(friend.getHead());
		}
		MusicUtil.play(Global.Root + "resource/sound/msg.wav");
	}
}
