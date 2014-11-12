package com.sean.im.client.listitem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.menu.FriendMenu;
import com.sean.im.commom.entity.Friend;

/**
 * 好友面板
 * @author sean
 */
public class FriendItem extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CommonLabel lbl_nickname, lbl_sigurature;
	private HeadComp head;
	private Friend friend;
	private boolean isRecent;

	public FriendItem(Friend friend, boolean isRecent)
	{
		this.friend = friend;
		this.isRecent = isRecent;
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
		this.setOpaque(false);
		this.setBackground(Global.DarkYellow);
		this.setPreferredSize(new Dimension(10000, 31));

		head = new HeadComp(friend.getHead(), 29, 29, friend.getStatus());
		head.setCursor(Global.CURSOR_HAND);
		if (!friend.isOnline())
		{
			head.setGray(true);
		}
		this.add(head);

		lbl_nickname = new CommonLabel();
		String show;
		if (friend.getRemark() != null && !friend.getRemark().isEmpty())
		{
			show = friend.getRemark();
		}
		else
		{
			show = friend.getNickname();
		}
		lbl_nickname.setText(show);
		this.add(lbl_nickname);

		lbl_sigurature = new CommonLabel(friend.getSignature());
		lbl_sigurature.setForeground(Color.GRAY);
		this.add(lbl_sigurature);
	}

	public void notifyChange()
	{
		head.setHead(friend.getHead());

		if (friend.getRemark() != null && !friend.getRemark().isEmpty())
		{
			lbl_nickname.setText(friend.getRemark());
		}
		else
		{
			lbl_nickname.setText(friend.getNickname());
		}

		lbl_sigurature.setText(friend.getSignature());

		head.setGray(!friend.isOnline());
		head.setStatus(friend.getStatus());
	}

	public void setFriend(Friend friend)
	{
		this.friend = friend;
	}

	public Friend getFriend()
	{
		return friend;
	}

	/**
	 * 设置好友状态
	 */
	public void setStatus(int status)
	{
		this.friend.setStatus(status);
		head.setStatus(status);
	}

	public void showMenu(JComponent component, int x, int y)
	{
		FriendMenu menu = new FriendMenu(friend, isRecent);
		menu.show(component, x, y);
	}
}