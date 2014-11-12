package com.sean.im.client.push.handler;

import javax.swing.JFrame;

import com.alibaba.fastjson.JSON;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.core.PushHandler;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.form.flock.ChatRoomForm;
import com.sean.im.client.tray.TrayManager;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.client.util.MusicUtil;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.FlockMember;
import com.sean.im.commom.entity.Message;

/**
 * 接受群信息
 * @author sean
 */
public class ReceiveFlockMsgHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{
		Message msg = JSON.parseObject(notify.getParameter("msg"), Message.class);
		Flock flock = MainForm.FORM.getFlockList().getFlock(msg.getFlockId());
		ChatRoomForm form = ChatFormCache.getChatRoomForm(flock);

		// 如果聊天窗体已经打开 ，立即显示
		if (form.isVisible())
		{
			FlockMember fm = form.getFlockMember(msg.getSenderId());
			form.appendRightMessage(msg, ApplicationContext.getSender(fm));

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
			TrayManager.getInstance().startLight(Global.Root + "resource/image/flock.png");
		}
		MusicUtil.play(Global.Root + "resource/sound/msg.wav");
	}
}
