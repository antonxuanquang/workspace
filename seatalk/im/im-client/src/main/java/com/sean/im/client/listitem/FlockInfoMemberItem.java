package com.sean.im.client.listitem;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomList;
import com.sean.im.client.menu.FlockInfoMemberMenu;
import com.sean.im.client.util.TimeUtil;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.FlockMember;

/**
 * 群成员listitem
 * @author sean
 */
public class FlockInfoMemberItem extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CommonLabel icon, username, nickname, joinTime;
	private HeadComp head;
	private FlockMember member;
	private Flock flock;

	public FlockInfoMemberItem(Flock flock, FlockMember member)
	{
		this.flock = flock;
		this.member = member;
		this.setBackground(Global.DarkYellow);
		this.setPreferredSize(new Dimension(300, 30));
		this.setLayout(null);

		if (member.getUserId() == flock.getCreater())
		{
			icon = new CommonLabel(new ImageIcon(Global.Root + "resource/image/creater.png"));
			icon.setBounds(5, 7, 12, 12);
			this.add(icon);
		}
		else if (member.getIsAdmin() == 1)
		{
			icon = new CommonLabel(new ImageIcon(Global.Root + "resource/image/admin.png"));
			icon.setBounds(5, 7, 12, 12);
			this.add(icon);
		}

		head = new HeadComp(member.getHead(), 25, 25);
		head.setBounds(20, 2, 25, 25);
		this.add(head);

		username = new CommonLabel(member.getUsername());
		username.setBounds(50, 5, 100, 20);
		this.add(username);

		nickname = new CommonLabel(member.getNickname());
		nickname.setBounds(160, 5, 100, 20);
		this.add(nickname);

		joinTime = new CommonLabel(TimeUtil.parseYYYYMMDD(member.getJoinTime()));
		joinTime.setBounds(250, 5, 100, 20);
		this.add(joinTime);
	}

	public void showMenu(CustomList list, int x, int y)
	{
		FlockInfoMemberMenu menu = new FlockInfoMemberMenu(list, flock, member);
		menu.show(list, x, y);
	}

	public void grantAdmin()
	{
		member.setIsAdmin(1);
		icon = new CommonLabel(new ImageIcon(Global.Root + "resource/image/admin.png"));
		icon.setBounds(5, 7, 12, 12);
		this.add(icon);
	}

	public void takeBackAdmin()
	{
		member.setIsAdmin(0);
		this.remove(icon);
		icon = null;
	}

	public FlockMember getMember()
	{
		return member;
	}

	public Flock getFlock()
	{
		return flock;
	}
}