package com.sean.im.client.comp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import com.sean.im.client.custom.CustomList;
import com.sean.im.client.listitem.FriendItem;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.commom.entity.Friend;

/**
 * 最近好友列表
 * @author sean
 */
public class RecentFriendComp extends CustomList
{
	private static final long serialVersionUID = 1L;

	private List<Friend> friends;
	private List<FriendItem> friendPanels;

	public RecentFriendComp()
	{
		Mouse_Listener ml = new Mouse_Listener();
		this.addMouseListener(ml);
		friendPanels = new LinkedList<FriendItem>();
	}

	public void setFriend(List<Friend> fs)
	{
		this.friends = fs;
		this.notifyChange();
	}

	/**
	 * 修改好友状态
	 */
	public void updateFriendStatus(long userId, int status)
	{
		for (FriendItem fp : friendPanels)
		{
			if (fp.getFriend().getFriendId() == userId)
			{
				fp.setStatus(status);
			}
		}
	}

	/**
	 * 通知修改列表
	 */
	private synchronized void notifyChange()
	{
		try
		{
			DefaultListModel<JPanel> dlm = new DefaultListModel<JPanel>();

			int i = 0;
			FriendItem fp = null;
			for (Friend f : friends)
			{
				if (f != null)
				{
					if (this.friendPanels.size() > i)
					{
						fp = friendPanels.get(i);
						fp.setFriend(f);
					}
					else
					{
						fp = new FriendItem(f, true);
						friendPanels.add(fp);
					}
					fp.notifyChange();
					dlm.addElement(fp);
					i++;
				}
			}
			this.setModel(dlm);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 添加好友
	 */
	public void addFriend(Friend friend)
	{
		if (this.friends.contains(friend))
		{
			friends.remove(friend);
			friends.add(0, friend);
		}
		else
		{
			this.friends.add(0, friend);
			if (friends.size() > 32)
			{
				friends.remove(friends.size() - 1);
			}
		}
		this.notifyChange();
	}

	/**
	 * 删除好友
	 */
	public void removeFriend(Friend friend)
	{
		this.friends.remove(friend);
		this.notifyChange();
	}

	/**
	 * 获取好友
	 */
	public List<Friend> getFriends()
	{
		return this.friends;
	}

	/**
	 * 鼠标事件
	 */
	private class Mouse_Listener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			JPanel jpl = RecentFriendComp.this.getSelectedValue();
			if (jpl != null)
			{
				// 如果是右键
				if (e.isMetaDown())
				{
					FriendItem fp = (FriendItem) jpl;
					fp.showMenu(RecentFriendComp.this, e.getX(), e.getY());
				}
				else
				{
					if (e.getClickCount() == 2)
					{
						ChatFormCache.getChatForm(((FriendItem) jpl).getFriend()).open();
					}
				}
			}
		}
	}
}
