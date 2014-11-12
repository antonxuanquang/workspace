package com.sean.im.client.comp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sean.im.client.custom.CustomList;
import com.sean.im.client.form.ChatForm;
import com.sean.im.client.listitem.FriendItem;
import com.sean.im.client.listitem.GroupItem;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Group;

/**
 * 好友列表
 * @author sean
 */
public class FriendListComp extends CustomList
{
	private static final long serialVersionUID = 1L;

	private Map<Long, Friend> friends = new HashMap<Long, Friend>();// 存放所有好友
	private Map<Long, Group> groups = new HashMap<Long, Group>();// 存放所有分组

	private List<GroupItem> groupPanels = new ArrayList<GroupItem>(16);// 重用所有分组面板

	public FriendListComp()
	{
		Mouse_Listener ml = new Mouse_Listener();
		this.addMouseListener(ml);
	}
	
	public void setGroup(List<Group> gs)
	{
		DefaultListModel<JPanel> dlm = new DefaultListModel<JPanel>();
		this.setModel(dlm);
		// 保存所有分组和好友
		for (Group g : gs)
		{
			groups.put(g.getId(), g);
			for (Friend f : g.getFriends())
			{
				friends.put(f.getId(), f);
			}
			GroupItem gp = new GroupItem(g, this);
			gp.notifyChange(dlm);
			groupPanels.add(gp);
		}
	}

	/**
	 * 通知修改列表
	 */
	private synchronized void notifyChange()
	{
		DefaultListModel<JPanel> dlm = new DefaultListModel<JPanel>();

		int i = 0;
		GroupItem gp = null;
		for (Group g : groups.values())
		{
			if (this.groupPanels.size() > i)
			{
				gp = groupPanels.get(i);
				gp.setGroup(g);
			}
			else
			{
				gp = new GroupItem(g, this);
				groupPanels.add(gp);
			}
			gp.notifyChange(dlm);
			i++;
		}
		this.setModel(dlm);
	}

	/**
	 * 添加分组
	 */
	public void addGroup(Group group)
	{
		if (group.getFriends() == null)
		{
			group.setFriends(new ArrayList<Friend>());
		}
		this.groups.put(group.getId(), group);
		this.notifyChange();
	}

	/**
	 * 删除分组
	 */
	public void removeGroup(long groupId)
	{
		groups.remove(groupId);
		this.notifyChange();
	}

	/**
	 * 修改分组
	 */
	public void updateGroup(long groupId, String groupName)
	{
		Group g = this.groups.get(groupId);
		g.setName(groupName);
		this.notifyChange();
	}

	/**
	 * 读取所有分组
	 */
	public Collection<Group> getGroups()
	{
		return this.groups.values();
	}

	/**
	 * 添加好友
	 */
	public void addFriend(long groupId, Friend friend)
	{
		this.friends.put(friend.getId(), friend);
		Group g = this.groups.get(groupId);
		g.getFriends().add(friend);
		this.notifyChange();
	}

	/**
	 * 删除好友
	 */
	public void removeFriend(long friendId)
	{
		for (Group g : groups.values())
		{
			g.removeFriend(friendId);
		}
		this.notifyChange();
	}

	/**
	 * 移动好友
	 */
	public void moveFriend(long friendId, long fromGroup, long toGroup)
	{
		Group from = this.groups.get(fromGroup);
		Group to = this.groups.get(toGroup);
		Friend f = from.removeFriend(friendId);
		to.getFriends().add(f);
		f.setGroupId(toGroup);
		this.notifyChange();
	}

	/**
	 * 读取好友
	 */
	public Friend getFriendByUserId(long userId)
	{
		for (Friend f : this.friends.values())
		{
			if (f.getFriendId() == userId)
			{
				return f;
			}
		}
		return null;
	}

	/**
	 * 读取好友
	 */
	public Friend getFriend(long id)
	{
		return this.friends.get(id);
	}

	/**
	 * 获取所有好友
	 */
	public Collection<Friend> getAllFriend()
	{
		return this.friends.values();
	}

	/**
	 * 修改好友状态
	 */
	public void updateFriendStatus(long userId, int status)
	{
		Friend friend = this.getFriendByUserId(userId);
		friend.setStatus(status);
		// 刷新分组
		for (GroupItem gp : this.groupPanels)
		{
			if (gp.getGroup().getId() == friend.getGroupId())
			{
				gp.notifyGroupChange();
				break;
			}
		}
	}
	
	/**
	 * 修改好友名称
	 */
	public void updateFriendName(long userId, String remarkName)
	{
		Friend friend = this.getFriendByUserId(userId);
		friend.setRemark(remarkName);
		// 刷新分组
		for (GroupItem gp : this.groupPanels)
		{
			if (gp.getGroup().getId() == friend.getGroupId())
			{
				gp.notifyGroupChange();
				break;
			}
		}
	}

	/**
	 * 鼠标事件
	 */
	private class Mouse_Listener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			JPanel jpl = FriendListComp.this.getSelectedValue();
			if (jpl != null)
			{
				// 如果是右键
				if (e.isMetaDown())
				{
					// 右键分组
					if (jpl instanceof GroupItem)
					{
						GroupItem gp = (GroupItem) jpl;
						gp.showMenu(FriendListComp.this, e.getX(), e.getY());
					}
					// 右键好友
					else
					{
						FriendItem fp = (FriendItem) jpl;
						fp.showMenu(FriendListComp.this, e.getX(), e.getY());
					}
				}
				else
				{
					// 左键单击
					if (e.getClickCount() == 1)
					{
						if (jpl instanceof GroupItem)
						{
							GroupItem group = (GroupItem) jpl;
							group.toggle();
						}
					}
					// 左键双击
					else if (e.getClickCount() == 2)
					{
						if (jpl instanceof FriendItem)
						{
							ChatForm form = ChatFormCache.getChatForm(((FriendItem) jpl).getFriend());
							if (form.isVisible())
							{
								form.setExtendedState(JFrame.NORMAL);
							}
							else
							{
								form.open();
							}
							form.getInput().requestFocus();
						}
					}
				}
			}
		}
	}
}
