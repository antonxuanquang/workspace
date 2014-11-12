package com.sean.im.client.message.handler;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.MessageHandler;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.Message;

/**
 * 被群管理员移除
 * @author sean
 */
public class KickOutFlockHandler implements MessageHandler
{
	@Override
	public void receive(final Message msg)
	{
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
					UIUtil.alert(null, Global.Lan.get("管理员已将你移除群") + flock.getName());	
				}
			}
		});
	}
}
