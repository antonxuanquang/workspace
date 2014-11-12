package com.sean.im.client.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.RoundMenu;
import com.sean.im.client.custom.RoundPopMenu;
import com.sean.im.client.form.FriendInfoForm;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.listitem.FriendItem;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Group;

/**
 * 好友右键菜单
 * @author sean
 */
public class FriendMenu extends RoundPopMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Friend friend;
	private JMenuItem item_info, item_msg, item_del, item_remark;
	private RoundMenu menu_move;

	public FriendMenu(Friend friend, boolean isRecent)
	{
		super(false);
		this.friend = friend;
		item_info = UIUtil.getMenuItem(Global.Lan.get("好友信息"), this);
		item_msg = UIUtil.getMenuItem(Global.Lan.get("发送消息"), this);
		menu_move = new RoundMenu(Global.Lan.get("移动到"));
		menu_move.setOpaque(false);
		menu_move.setFont(Global.FONT);
		item_remark = UIUtil.getMenuItem(Global.Lan.get("修改备注"), this);
		item_del = UIUtil.getMenuItem(Global.Lan.get("删除好友"), this);

		if (isRecent)
		{
			this.add(item_info);
			this.add(item_msg);
			this.add(item_remark);
		}
		else
		{
			this.add(item_info);
			this.add(item_msg);
			this.add(item_remark);
			this.add(menu_move);
			this.add(item_del);
		}

		for (Group g : MainForm.FORM.getFriendList().getGroups())
		{
			JMenuItem group = UIUtil.getMenuItem(g.getName(), this);
			group.setName(g.getId() + "");
			menu_move.add(group);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// 查看好友信息
		if (e.getSource() == item_info)
		{
			FriendInfoForm fif = new FriendInfoForm(friend.getFriendId());
			fif.setVisible(true);
			fif.getData();
		}
		// 发送消息
		else if (e.getSource() == item_msg)
		{
			ChatFormCache.getChatForm(friend).open();
		}
		// 删除好友
		else if (e.getSource() == item_del)
		{
			if (UIUtil.confirm(null, Global.Lan.get("确定要删除好友吗？")))
			{
				final FriendItem fp = (FriendItem) MainForm.FORM.getFriendList().getSelectedValue();
				Request request = new Request(Actions.DeleteFriendAction);
				request.setParameter("friendId", fp.getFriend().getId());
				HttpUtil.request(request, new RequestHandler()
				{
					@Override
					public void callback(JSONObject data)
					{
						MainForm.FORM.getFriendList().removeFriend(fp.getFriend().getId());
						MainForm.FORM.getRecentFriendList().removeFriend(fp.getFriend());
					}
				});
			}
		}
		// 修改备注
		else if (e.getSource() == item_remark)
		{
			final String remark = UIUtil.input();
			if (remark != null && !remark.isEmpty())
			{
				Request request = new Request(Actions.UpdateRemarkAction);
				request.setParameter("friendId", friend.getId());
				request.setParameter("remarkName", remark);
				HttpUtil.request(request, new RequestHandler()
				{
					@Override
					public void callback(JSONObject data)
					{
						MainForm.FORM.getFriendList().updateFriendName(friend.getFriendId(), remark);
					}
				});
			}
		}
		// 移动好友
		else
		{
			JMenuItem item = (JMenuItem) e.getSource();
			final long groupId = Long.parseLong(item.getName());
			final FriendItem fp = (FriendItem) MainForm.FORM.getFriendList().getSelectedValue();
			Request request = new Request(Actions.MoveFriendAction);
			request.setParameter("friendId", fp.getFriend().getId());
			request.setParameter("groupId", groupId);
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					Friend friend = fp.getFriend();
					MainForm.FORM.getFriendList().moveFriend(friend.getId(), friend.getGroupId(), groupId);
				}
			});
		}
	}
}