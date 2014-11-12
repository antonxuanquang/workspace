package com.sean.im.client.core;

import com.sean.im.commom.core.Protocol;

/**
 * 服务器主动推送消息处理
 * @author sean
 */
public interface PushHandler
{
	public void execute(Protocol notify);
}
