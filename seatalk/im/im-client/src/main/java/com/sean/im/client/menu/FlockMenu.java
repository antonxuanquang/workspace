package com.sean.im.client.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.RoundPopMenu;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.form.flock.ChatRoomForm;
import com.sean.im.client.form.flock.FlockInfoForm;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Flock;

/**
 * 群右键菜单
 * @author sean
 */
public class FlockMenu extends RoundPopMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Flock flock;
	private JMenuItem item_info, item_send, item_mgr, item_exit, item_dismiss;

	public FlockMenu(Flock flock)
	{
		super(false);
		this.flock = flock;
		item_info = UIUtil.getMenuItem(Global.Lan.get("群信息"), this);
		item_send = UIUtil.getMenuItem(Global.Lan.get("发送群消息"), this);
		item_mgr = UIUtil.getMenuItem(Global.Lan.get("群成员"), this);
		item_exit = UIUtil.getMenuItem(Global.Lan.get("退出该群"), this);
		item_dismiss = UIUtil.getMenuItem(Global.Lan.get("解散该群"), this);

		// 如果登录的人是创建人
		if (ApplicationContext.User.getId() == flock.getCreater())
		{
			this.add(item_info);
			this.add(item_send);
			this.add(item_mgr);
			this.add(item_dismiss);
		}
		// 普通人
		else
		{
			this.add(item_info);
			this.add(item_send);
			this.add(item_mgr);
			this.add(item_exit);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// 群信息
		if (e.getSource() == item_info)
		{
			FlockInfoForm form = new FlockInfoForm(flock);
			form.setVisible(true);
			form.getData();
		}
		// 发送群消息
		else if (e.getSource() == item_send)
		{
			ChatRoomForm room = ChatFormCache.getChatRoomForm(flock);
			room.setVisible(true);
			room.initData();
		}
		// 管理群成员
		else if (e.getSource() == item_mgr)
		{
			FlockInfoForm form = new FlockInfoForm(flock);
			form.setVisible(true);
			form.getData();
			form.setMemberSelected();
		}
		// 退出群
		else if (e.getSource() == item_exit)
		{
			Request request = new Request(Actions.ExitFlockAction);
			request.setParameter("flockId", flock.getId());
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					MainForm.FORM.getFlockList().removeFlock(flock);
					UIUtil.alert(null, Global.Lan.get("已成功退出群"));
				}
			});
		}
		// 解散群
		else if (e.getSource() == item_dismiss)
		{
			Request request = new Request(Actions.DismissFlockAction);
			request.setParameter("flockId", flock.getId());
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					MainForm.FORM.getFlockList().removeFlock(flock);
					UIUtil.alert(null, Global.Lan.get("已经成功解散群"));
				}
			});
		}
	}
}