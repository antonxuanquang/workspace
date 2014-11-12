package com.sean.im.client.push.handler;

import com.alibaba.fastjson.JSON;
import com.sean.im.client.core.PushHandler;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.Message;

/**
 * 接受警告信息
 * @author sean
 */
public class ReceiveWarnMsgHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{
		Message msg = JSON.parseObject(notify.getParameter("msg"), Message.class);
		UIUtil.warn(MainForm.FORM, msg.getContent());
	}
}
