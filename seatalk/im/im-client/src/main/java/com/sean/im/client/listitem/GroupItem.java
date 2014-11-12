package com.sean.im.client.listitem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sean.im.client.comp.FriendListComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.menu.GroupMenu;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Group;

/**
 * 分组面板
 * @author sean
 */
public class GroupItem extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CommonLabel lbl_group, lbl_img;
	private boolean isOpen = false;
	private ImageIcon close, open;
	private Group group;
	private FriendListComp friendList;

	private Map<Long, FriendItem> friendPanels;// 重用所有面板

	public GroupItem(Group group, FriendListComp friendList)
	{
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(10000, 25));
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		this.setBackground(Global.DarkYellow);
		this.setBorder(new EmptyBorder(0, 3, 0, 0));

		this.friendList = friendList;
		this.group = group;
		friendPanels = new HashMap<Long, FriendItem>(group.getFriends().size());

		try
		{
			BufferedImage bi = ImageIO.read(new File(Global.Root + "resource/image/tree_group.png"));
			close = new ImageIcon(bi.getSubimage(0, 0, 12, 12));
			open = new ImageIcon(bi.getSubimage(25, 0, 12, 12));
			bi = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		lbl_group = new CommonLabel();
		lbl_img = new CommonLabel(close);
		this.add(lbl_img, BorderLayout.WEST);
		this.add(lbl_group, BorderLayout.CENTER);
	}

	/**
	 * 通知修改
	 */
	public void notifyGroupChange()
	{
		DefaultListModel<JPanel> model = (DefaultListModel<JPanel>) friendList.getModel();
		int index = model.indexOf(this);

		int alife = 0;
		FriendItem fp = null;
		for (Friend friend : group.getFriends())
		{
			if (friend.isOnline())
			{
				alife++;
			}

			if (isOpen)
			{
				index++;
				fp = (FriendItem) model.elementAt(index);
				fp.notifyChange();
			}
		}

		if (group.getIsDefault() == 1)
		{
			lbl_group.setText(Global.Lan.get("我的好友") + " [" + alife + "/" + group.getFriends().size() + "]");
		}
		else
		{
			lbl_group.setText(group.getName() + " [" + alife + "/" + group.getFriends().size() + "]");
		}

		MainForm.FORM.repaint();
	}

	/**
	 * 通知修改
	 */
	public void notifyChange(DefaultListModel<JPanel> dlm)
	{
		dlm.addElement(this);

		int alife = 0;
		FriendItem fp = null;
		for (Friend friend : group.getFriends())
		{
			if (friend.isOnline())
			{
				alife++;
			}

			fp = this.friendPanels.get(friend.getId());
			if (fp != null)
			{
				fp.setFriend(friend);
			}
			else
			{
				fp = new FriendItem(friend, false);
				this.friendPanels.put(friend.getId(), fp);
			}
			fp.notifyChange();

			if (isOpen)
			{
				dlm.addElement(fp);
			}
		}
		
		if (group.getIsDefault() == 1)
		{
			lbl_group.setText(Global.Lan.get("我的好友") + " [" + alife + "/" + group.getFriends().size() + "]");
		}
		else
		{
			lbl_group.setText(group.getName() + " [" + alife + "/" + group.getFriends().size() + "]");
		}
	}

	/**
	 * 设置分组
	 */
	public void setGroup(Group group)
	{
		this.group = group;
	}

	/**
	 * 获取分组
	 */
	public Group getGroup()
	{
		return this.group;
	}

	/**
	 * 显示右键菜单
	 */
	public void showMenu(FriendListComp friendList, int x, int y)
	{
		GroupMenu menu = new GroupMenu(group);
		menu.show(friendList, x, y);
	}

	/**
	 * 切换
	 */
	public void toggle()
	{
		DefaultListModel<JPanel> model = (DefaultListModel<JPanel>) friendList.getModel();
		int selectedIndex = friendList.getSelectedIndex() + 1;
		if (!isOpen)
		{
			lbl_img.setIcon(open);
			isOpen = true;
			int length = friendPanels.size();
			List<FriendItem> offline = new ArrayList<FriendItem>(length);
			for (Friend f : group.getFriends())
			{
				this.friendPanels.get(f.getId()).notifyChange();
				if (!f.isOnline())
				{
					offline.add(this.friendPanels.get(f.getId()));
					continue;
				}
				model.add(selectedIndex, this.friendPanels.get(f.getId()));
				selectedIndex++;
			}
			// 添加离线好友
			length = offline.size();
			for (int i = 0; i < length; i++)
			{
				model.add(selectedIndex, offline.get(i));
				selectedIndex++;
			}
		}
		else
		{
			lbl_img.setIcon(close);
			isOpen = false;
			for (FriendItem fp : friendPanels.values())
			{
				model.removeElement(fp);
			}
		}
	}
}