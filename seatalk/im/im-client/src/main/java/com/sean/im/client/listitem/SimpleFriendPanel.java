package com.sean.im.client.listitem;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.commom.entity.Friend;

/**
 * 简单的好友面板
 * @author sean
 */
public class SimpleFriendPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Friend friend;

	public SimpleFriendPanel(Friend friend)
	{
		this.friend = friend;
		this.setLayout(null);
		this.setBackground(Global.DarkYellow);
		this.setPreferredSize(new Dimension(170, 30));

		HeadComp head = new HeadComp(friend.getHead(), 25, 25);
		head.setBounds(5, 2, 25, 25);
		this.add(head);

		CommonLabel nickname = null;
		if (friend.getRemark() != null && !friend.getRemark().isEmpty())
		{
			nickname = new CommonLabel(friend.getRemark());
		}
		else
		{
			nickname = new CommonLabel(friend.getNickname());
		}
		nickname.setBounds(35, 5, 200, 20);
		this.add(nickname);
	}

	public Friend getFriend()
	{
		return friend;
	}
}
