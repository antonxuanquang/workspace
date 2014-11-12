package com.sean.im.client.push.handler;

import com.alibaba.fastjson.JSON;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.core.PushHandler;
import com.sean.im.client.tray.TrayManager;
import com.sean.im.client.util.MusicUtil;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.Message;
import com.sean.im.commom.entity.UserInfo;

/**
 * 添加好友请求
 * @author sean
 */
public class RequestFriendHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{
		Message msg = JSON.parseObject(notify.getParameter("msg"), Message.class);
		UserInfo userinfo = JSON.parseObject(notify.getParameter("userinfo"), UserInfo.class);
		msg.setSingleParam(userinfo);
		
		// 压入消息队列
		ApplicationContext.CTX.getMessageQueue().add(msg);
		// 提示系统托盘闪烁
		TrayManager.getInstance().startLight(0);
		MusicUtil.play(Global.Root + "resource/sound/msg.wav");
	}
}
