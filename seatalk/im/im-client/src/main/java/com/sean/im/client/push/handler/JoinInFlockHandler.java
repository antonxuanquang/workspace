package com.sean.im.client.push.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.core.PushHandler;
import com.sean.im.client.tray.TrayManager;
import com.sean.im.client.util.MusicUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.Message;

/**
 * 加入群
 * @author sean
 */
public class JoinInFlockHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{
		final Message msg = JSON.parseObject(notify.getParameter("msg"), Message.class);

		// 界面上添加群
		long flockId = Long.parseLong(msg.getContent());
		Request request = new Request(Actions.InquireFlockBriefAction);
		request.setParameter("flockId", flockId);
		HttpUtil.request(request, new RequestHandler()
		{
			@Override
			public void callback(JSONObject data)
			{
				Flock flock = data.getObject("flockBrief", Flock.class);
				if (flock != null)
				{
					// 压入消息队列
					ApplicationContext.CTX.getMessageQueue().add(msg);
					// 提示系统托盘闪烁
					TrayManager.getInstance().startLight(0);
					MusicUtil.play(Global.Root + "resource/sound/msg.wav");
				}
			}
		});
	}
}
