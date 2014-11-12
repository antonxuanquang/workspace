package com.sean.im.friend.service;

import com.sean.im.commom.entity.Message;

public interface MessageService
{
	/**
	 * insert message
	 * @param msg
	 * @param isRead
	 */
	public void insertMessage(Message msg, boolean isRead);
}
