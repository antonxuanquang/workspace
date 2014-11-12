package com.sean.im.client.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CustomList;
import com.sean.im.client.custom.RoundPopMenu;
import com.sean.im.client.form.FriendInfoForm;
import com.sean.im.client.listitem.FlockInfoMemberItem;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.FlockMember;

/**
 * 群信息成员右键菜单
 * @author sean
 */
public class FlockInfoMemberMenu extends RoundPopMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Flock flock;
	private FlockMember member;
	private CustomList list;
	private JMenuItem item_info, item_admin, item_unadmin, item_exit;

	public FlockInfoMemberMenu(CustomList list, Flock flock, FlockMember member)
	{
		super(false);
		this.list = list;
		this.flock = flock;
		this.member = member;

		item_info = UIUtil.getMenuItem(Global.Lan.get("成员信息"), this);
		item_admin = UIUtil.getMenuItem(Global.Lan.get("设为管理员"), this);
		item_unadmin = UIUtil.getMenuItem(Global.Lan.get("取消管理员"), this);
		item_exit = UIUtil.getMenuItem(Global.Lan.get("移出该群"), this);

		long loginerId = ApplicationContext.User.getId();

		// 登录的是创建人
		if (flock.getCreater() == loginerId)
		{
			this.add(item_info);
			if (flock.getCreater() != member.getUserId() && member.getIsAdmin() == 1)
			{
				this.add(item_unadmin);
			}
			if (flock.getCreater() != member.getUserId() && member.getIsAdmin() == 0)
			{
				this.add(item_admin);
			}
			if (flock.getCreater() != member.getUserId())
			{
				this.add(item_exit);
			}
		}
		// 登录的是管理员
		else if (isAdmin())
		{
			this.add(item_info);
			if (flock.getCreater() != member.getUserId())
			{
				if (flock.getCreater() != member.getUserId() && member.getIsAdmin() == 1)
				{
					this.add(item_unadmin);
				}
				if (flock.getCreater() != member.getUserId() && member.getIsAdmin() == 0)
				{
					this.add(item_admin);
				}
				if (flock.getCreater() != member.getUserId())
				{
					this.add(item_exit);
				}
			}
		}
		// 普通成员
		else
		{
			this.add(item_info);
		}
	}

	private boolean isAdmin()
	{
		int length = list.listSize();
		for (int i = 0; i < length; i++)
		{
			FlockInfoMemberItem member = (FlockInfoMemberItem) list.getElementAt(i);
			if (member.getMember().getUserId() == ApplicationContext.User.getId() && member.getMember().getIsAdmin() == 1)
			{
				return true;
			}
		}
		return false;
	}

	public Flock getFlock()
	{
		return flock;
	}

	public FlockMember getMember()
	{
		return member;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// 设置为管理员
		if (e.getSource() == item_admin)
		{
			Request request = new Request(Actions.GrantFlockAdminAction);
			request.setParameter("flockMemberId", member.getId());
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					FlockInfoMemberItem item = (FlockInfoMemberItem) list.getSelectedValue();
					item.grantAdmin();
				}
			});
		}
		// 取消管理员
		else if (e.getSource() == item_unadmin)
		{
			Request request = new Request(Actions.TakeBackFlockAdminAction);
			request.setParameter("flockMemberId", member.getId());
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					FlockInfoMemberItem item = (FlockInfoMemberItem) list.getSelectedValue();
					item.takeBackAdmin();
				}
			});
		}
		// 移出群
		else if (e.getSource() == item_exit)
		{
			Request request = new Request(Actions.KickOutFlockAction);
			request.setParameter("flockMemberId", member.getId());
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					FlockInfoMemberItem item = (FlockInfoMemberItem) list.getSelectedValue();
					list.removeElement(item);
				}
			});
		}
		// 成员信息
		else if (e.getSource() == item_info)
		{
			FriendInfoForm fif = new FriendInfoForm(member.getUserId());
			fif.setVisible(true);
			fif.getData();
		}
	}

}