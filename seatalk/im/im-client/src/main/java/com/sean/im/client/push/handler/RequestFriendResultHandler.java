package com.sean.im.client.push.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.core.PushHandler;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.tray.TrayManager;
import com.sean.im.client.util.MusicUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.constant.MessageEnum;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Message;

/**
 * 添加好友请求结果
 * @author sean
 */
public class RequestFriendResultHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{
		final Message msg = JSON.parseObject(notify.getParameter("msg"), Message.class);
		// 同意添加
		if (msg.getType() == MessageEnum.Message_AgreeRequestFriend)
		{
			// 读取好友
			Request request = new Request(Actions.InquireFriendAction);
			request.setParameter("userId", msg.getSenderId());
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					Friend friend = JSON.parseObject(data.getString("friend"), Friend.class);
					MainForm.FORM.getFriendList().addFriend(friend.getGroupId(), friend);
					MainForm.FORM.repaint();

					// 压入消息队列
					ApplicationContext.CTX.getMessageQueue().add(msg);
					// 提示系统托盘闪烁
					TrayManager.getInstance().startLight(0);
					MusicUtil.play(Global.Root + "resource/sound/msg.wav");
				}
			});
		}
		// 不同意添加好友
		else if (msg.getType() == MessageEnum.Message_RefuseRequestFriend)
		{
			// 压入消息队列
			ApplicationContext.CTX.getMessageQueue().add(msg);
			// 提示系统托盘闪烁
			TrayManager.getInstance().startLight(0);
			MusicUtil.play(Global.Root + "resource/sound/msg.wav");
		}
	}
}
