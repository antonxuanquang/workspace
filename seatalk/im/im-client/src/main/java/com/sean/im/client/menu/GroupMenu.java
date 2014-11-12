package com.sean.im.client.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.RoundPopMenu;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Group;

/**
 * 分组右键菜单
 * @author sean
 */
public class GroupMenu extends RoundPopMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Group group;
	private JMenuItem item_add, item_rename, item_del;

	public GroupMenu(Group group)
	{
		super(false);
		this.group = group;
		item_add = UIUtil.getMenuItem(Global.Lan.get("添加分组"), this);
		item_rename = UIUtil.getMenuItem(Global.Lan.get("重命名分组"), this);
		item_del = UIUtil.getMenuItem(Global.Lan.get("删除分组"), this);

		this.add(item_add);
		this.add(item_rename);
		this.add(item_del);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// 添加分组
		if (e.getSource() == item_add)
		{
			final String name = JOptionPane.showInputDialog(null, Global.Lan.get("请输入分组名称"), Global.Lan.get("提示消息"), JOptionPane.INFORMATION_MESSAGE);
			if (name != null && !name.isEmpty())
			{
				Request request = new Request(Actions.AddGroupAction);
				request.setParameter("groupName", name);
				HttpUtil.request(request, new RequestHandler()
				{
					@Override
					public void callback(JSONObject data)
					{
						long groupId = data.getLongValue("groupId");
						Group g = new Group();
						g.setId(groupId);
						g.setName(name);
						g.setIsDefault(0);
						g.setFriends(new ArrayList<Friend>());
						MainForm.FORM.getFriendList().addGroup(g);
					}
				});
			}
		}
		// 重命名分组
		else if (e.getSource() == item_rename)
		{
			if (group.getIsDefault() != 1)
			{
				final String name = JOptionPane.showInputDialog(null, Global.Lan.get("请输入分组名称"), Global.Lan.get("提示消息"), JOptionPane.INFORMATION_MESSAGE);
				if (name != null && !name.isEmpty())
				{
					Request request = new Request(Actions.RenameGroupAction);
					request.setParameter("groupId", this.group.getId());
					request.setParameter("groupName", name);
					HttpUtil.request(request, new RequestHandler()
					{
						@Override
						public void callback(JSONObject data)
						{
							MainForm.FORM.getFriendList().updateGroup(group.getId(), name);
						}
					});
				}
			}
		}
		// 删除分组
		else if (e.getSource() == item_del)
		{
			if (group.getIsDefault() != 1)
			{
				if (group.getFriends().isEmpty())
				{
					Request request = new Request(Actions.DeleteGroupAction);
					request.setParameter("groupId", group.getId());
					HttpUtil.request(request, new RequestHandler()
					{
						@Override
						public void callback(JSONObject data)
						{
							MainForm.FORM.getFriendList().removeGroup(group.getId());
						}
					});
				}
				else
				{
					UIUtil.alert(null, Global.Lan.get("请将好友移动到其他分组再删除"));
					return;
				}
			}
		}
	}
}