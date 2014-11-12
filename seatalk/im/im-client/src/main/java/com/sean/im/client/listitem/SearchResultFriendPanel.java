package com.sean.im.client.listitem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.form.FriendInfoForm;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.UserInfo;

/**
 * 搜索好友结果好友选项
 * @author sean
 */
public class SearchResultFriendPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CommonLabel lbl_nickname, lbl_sigurature;
	private CommonButton btn_add, btn_look;
	private HeadComp head;
	private UserInfo userinfo;

	public SearchResultFriendPanel(int index, UserInfo userinfo)
	{
		this.userinfo = userinfo;
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(600, 40));
		if (index % 2 == 0)
		{
			this.setBackground(new Color(244, 242, 240));
		}

		head = new HeadComp(userinfo.getHead(), 30, 30);
		head.setBounds(5, 5, 30, 30);
		this.add(head);
		lbl_nickname = new CommonLabel(userinfo.getNickname() + "(" + userinfo.getUsername() + ")");
		lbl_nickname.setBounds(40, 0, 250, 20);
		this.add(lbl_nickname);
		lbl_sigurature = new CommonLabel(userinfo.getSignature(), Color.GRAY);
		lbl_sigurature.setBounds(40, 15, 250, 20);
		this.add(lbl_sigurature);

		btn_add = new CommonButton(Global.Lan.get("添加好友"));
		btn_add.setBounds(320, 5, 80, 25);
		this.add(btn_add);
		btn_look = new CommonButton(Global.Lan.get("资料信息"));
		btn_look.setBounds(410, 5, 80, 25);
		this.add(btn_look);

		// 添加事件
		Btn_Listener bl = new Btn_Listener();
		btn_look.addActionListener(bl);
		btn_add.addActionListener(bl);
	}

	@Override
	public void paintBorder(Graphics g)
	{
		g.setColor(Global.BorderColor);
		g.drawLine(0, 0, 0, this.getHeight());
		g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight());
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_look)
			{
				FriendInfoForm fif = new FriendInfoForm(userinfo.getId());
				fif.setVisible(true);
				fif.getData();
			}
			else if (e.getSource() == btn_add)
			{
				if (ApplicationContext.User.getId() == userinfo.getId())
				{
					return;
				}
				final String remark = UIUtil.input();
				if (remark != null && !remark.isEmpty())
				{
					Request request = new Request(Actions.RequestFriendAction);
					request.setParameter("userId", userinfo.getId());
					request.setParameter("remark", remark);
					HttpUtil.request(request, new RequestHandler()
					{
						@Override
						public void callback(JSONObject data)
						{
							int requestrs = data.getIntValue("requestrs");
							if (requestrs == 1)
							{
								UIUtil.alert(null, "请求已经发送，等待对方确认...");
							}
							else
							{
								UIUtil.alert(null, "该好友已经是你的好友");
							}
						}
					});
				}
				else
				{
					UIUtil.alert(null, "请输入验证信息");
				}
			}
		}
	}
}