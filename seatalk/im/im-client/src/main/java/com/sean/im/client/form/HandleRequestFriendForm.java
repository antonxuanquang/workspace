package com.sean.im.client.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Message;
import com.sean.im.commom.entity.UserInfo;

/**
 * 好友请求
 * @author sean
 */
public class HandleRequestFriendForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel bg, south;
	private HeadComp head;
	private CommonLabel lbl_username, lbl_remark;
	private CommonButton btn_agree, btn_refuse;
	private UserInfo userinfo;

	public HandleRequestFriendForm(UserInfo ui, Message msg)
	{
		super(300, 200);
		this.userinfo = ui;
		this.setCustomLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setCustomMaxiable(false);
		this.setCustomTitle(Global.Lan.get("好友添加请求"), null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		bg = new JPanel();
		bg.setBackground(Color.white);
		bg.setLayout(null);

		south = new JPanel();
		south.setBackground(Global.LightBlue);
		south.setPreferredSize(new Dimension(1000, 35));
		south.setLayout(new FlowLayout(FlowLayout.RIGHT));

		head = new HeadComp(userinfo.getHead(), 60, 60);
		head.setBounds(10, 10, 60, 60);
		head.setCursor(Global.CURSOR_HAND);
		head.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				FriendInfoForm fif = new FriendInfoForm(userinfo.getId());
				fif.setVisible(true);
				fif.getData();
			}
		});
		bg.add(head);
		lbl_username = new CommonLabel(userinfo.getNickname() + "(" + userinfo.getUsername() + ")");
		lbl_username.setBounds(80, 10, 200, 25);
		bg.add(lbl_username);
		lbl_remark = new CommonLabel(Global.Lan.get("备注") + " : " + msg.getContent());
		lbl_remark.setBounds(80, 25, 200, 50);
		bg.add(lbl_remark);

		btn_agree = new CommonButton(Global.Lan.get("同意"));
		btn_agree.setPreferredSize(new Dimension(60, 25));
		south.add(btn_agree);
		btn_refuse = new CommonButton(Global.Lan.get("拒绝"));
		btn_refuse.setPreferredSize(new Dimension(60, 25));
		south.add(btn_refuse);

		this.addCustomComponent(bg, BorderLayout.CENTER);
		this.addCustomComponent(south, BorderLayout.SOUTH);

		Btn_Listener bl = new Btn_Listener();
		btn_agree.addActionListener(bl);
		btn_refuse.addActionListener(bl);
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Request request = new Request(Actions.HandleRequestFriendAction);
			request.setParameter("userId", userinfo.getId());

			// 同意添加
			if (e.getSource() == btn_agree)
			{
				request.setParameter("requestOperate", 1);

				HttpUtil.request(request, new RequestHandler()
				{
					@Override
					public void callback(JSONObject data)
					{
						// 读取好友
						Request request = new Request(Actions.InquireFriendAction);
						request.setParameter("userId", userinfo.getId());
						HttpUtil.request(request, new RequestHandler()
						{
							@Override
							public void callback(JSONObject data2)
							{
								Friend friend = JSON.parseObject(data2.getString("friend"), Friend.class);
								MainForm.FORM.getFriendList().addFriend(friend.getGroupId(), friend);
								MainForm.FORM.repaint();

								HandleRequestFriendForm.this.setVisible(false);
								HandleRequestFriendForm.this.dispose();
							}
						});
					}
				});
			}
			// 拒绝添加
			else if (e.getSource() == btn_refuse)
			{
				request.setParameter("requestOperate", 0);
				HttpUtil.request(request, new RequestHandler()
				{
					@Override
					public void callback(JSONObject data)
					{
						HandleRequestFriendForm.this.setVisible(false);
						HandleRequestFriendForm.this.dispose();
					}
				});
			}
		}
	}
}
