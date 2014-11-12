package com.sean.im.client.message.handler;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.core.MessageHandler;
import com.sean.im.client.form.HandleRequestFriendForm;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Message;
import com.sean.im.commom.entity.UserInfo;

/**
 * 好友请求处理
 * @author sean
 */
public class RequestFriendHandler implements MessageHandler
{
	@Override
	public void receive(final Message msg)
	{
		Request protocol = new Request(Actions.InquireInfoBriefAction);
		protocol.setParameter("userId", msg.getSenderId());
		HttpUtil.request(protocol, new RequestHandler()
		{
			@Override
			public void callback(JSONObject data)
			{
				UserInfo user = data.getObject("usershort", UserInfo.class);
				new HandleRequestFriendForm(user, msg).setVisible(true);
			}
		});
	}
}
