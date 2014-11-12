package com.sean.im.client.push.handler;

import com.alibaba.fastjson.JSON;
import com.sean.im.client.comp.FriendListComp;
import com.sean.im.client.core.PushHandler;
import com.sean.im.client.form.FileTransferForm;
import com.sean.im.client.form.MainForm;
import com.sean.im.commom.core.Protocol;
import com.sean.im.commom.entity.FileInfo;
import com.sean.im.commom.entity.Friend;

/**
 * 接受文件
 * @author sean
 */
public class ReceiveFileHandler implements PushHandler
{
	@Override
	public void execute(Protocol notify)
	{
		FileInfo fi = JSON.parseObject(notify.getParameter("file"), FileInfo.class);

		FriendListComp friendList = MainForm.FORM.getFriendList();
		Friend friend = friendList.getFriendByUserId(notify.sender);
		FileTransferForm.getInstance().receiverFile(fi, friend);
		FileTransferForm.getInstance().setVisible(true);
	}
}
