package com.sean.im.client.listitem;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.FlockMember;

/**
 * 聊天室群成员listitem
 * @author sean
 */
public class ChatRoomMemberItem extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CommonLabel icon, name;
	private HeadComp head;
	private FlockMember member;

	public ChatRoomMemberItem(Flock flock, FlockMember member)
	{
		this.member = member;
		this.setBackground(Global.DarkYellow);
		this.setPreferredSize(new Dimension(1, 25));
		this.setLayout(null);

		if (member.getId() == flock.getCreater())
		{
			icon = new CommonLabel(new ImageIcon(Global.Root + "resource/image/creater.png"));
			icon.setBounds(4, 5, 12, 12);
			this.add(icon);
		}
		else if (member.getIsAdmin() == 1)
		{
			icon = new CommonLabel(new ImageIcon(Global.Root + "resource/image/admin.png"));
			icon.setBounds(4, 5, 12, 12);
			this.add(icon);
		}

		head = new HeadComp(member.getHead(), 25, 25);
		head.setBounds(18, 0, 25, 25);
		if (!member.isOnline())
		{
			head.setGray(true);
		}
		this.add(head);

		name = new CommonLabel(member.getNickname());
		name.setBounds(45, 2, 100, 20);
		this.add(name);
	}

	public FlockMember getMember()
	{
		return member;
	}
}