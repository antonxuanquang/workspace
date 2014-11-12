package com.sean.im.client.message.handler;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.MessageHandler;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.Message;

/**
 * 加入群
 * @author sean
 */
public class JoinInFlockHandler implements MessageHandler
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
				final Flock flock = data.getObject("flockBrief", Flock.class);
				if (flock != null)
				{
					if (UIUtil.confirm(MainForm.FORM, Global.Lan.get("群管理员请求将您拉入群") + ":" + flock.getName()))
					{
						Request req = new Request("JoinFlockAction");
						req.setParameter("flockId", flock.getId());
						HttpUtil.request(req, new RequestHandler()
						{
							@Override
							public void callback(JSONObject data)
							{
								MainForm.FORM.getFlockList().addFlock(flock);
							}
						});
					}
				}
			}
		});
	}
}
