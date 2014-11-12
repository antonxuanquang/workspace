package com.sean.im.client.push.handler;

import com.alibaba.fastjson.JSON;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.core.PushHandler;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.tray.TrayManager;
import com.sean.im.client.util.MusicUtil;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.Message;

/**
 * 移除群成员
 * @author sean
 */
public class KickOutFlockHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{		
		Message msg = JSON.parseObject(notify.getParameter("msg"), Message.class);
		
		// 界面上删除群
		long flockId = Long.parseLong(msg.getContent());
		MainForm.FORM.getFlockList().removeFlock(flockId);
		
		// 压入消息队列
		ApplicationContext.CTX.getMessageQueue().add(msg);
		// 提示系统托盘闪烁
		TrayManager.getInstance().startLight(0);
		MusicUtil.play(Global.Root + "resource/sound/msg.wav");
	}
}
