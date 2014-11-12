package com.sean.im.client.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.UserInfo;

/**
 * 好友信息窗体
 * @author sean
 */
public class FriendInfoForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;

	private JPanel bg, left, right, south;
	private CommonButton btn_cancel;
	private CommonLabel lbl_username, lbl_nickname, lbl_country, lbl_signature, lbl_sex, lbl_age, lbl_tel, lbl_mail, lbl_lan, lbl_descr;
	private HeadComp head;
	private long userId;

	public FriendInfoForm(long userId)
	{
		super(600, 450);
		this.setCustomTitle(Global.Lan.get("个人资料"), null);
		this.setLocationRelativeTo(null);
		this.setCustomLayout(new BorderLayout());
		this.setCustomMaxiable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.userId = userId;

		bg = new JPanel();
		bg.setOpaque(false);
		bg.setLayout(new BorderLayout());

		left = new JPanel();
		left.setOpaque(false);
		left.setLayout(null);
		left.setPreferredSize(new Dimension(140, 10000));
		right = new JPanel();
		right.setBackground(Color.WHITE);
		right.setLayout(null);
		south = new JPanel();
		south.setOpaque(false);
		south.setPreferredSize(new Dimension(1000, 35));
		south.setLayout(new FlowLayout(FlowLayout.RIGHT));

		bg.add(south, BorderLayout.SOUTH);
		bg.add(left, BorderLayout.WEST);
		bg.add(right, BorderLayout.CENTER);

		// 添加取消按钮
		btn_cancel = new CommonButton(Global.Lan.get("取消"));
		btn_cancel.setPreferredSize(new Dimension(60, 25));
		south.add(btn_cancel);

		// 添加头像
		head = new HeadComp(1, 100, 100);
		head.setHeightLight();
		head.setBounds(20, 20, 100, 100);
		left.add(head);

		// 添加用户名label
		lbl_username = new CommonLabel(Global.Lan.get("帐号") + "：");
		lbl_username.setBounds(20, 10, 200, 25);
		right.add(lbl_username);

		// 添加昵称label
		lbl_nickname = new CommonLabel(Global.Lan.get("昵称") + "：");
		lbl_nickname.setBounds(220, 10, 200, 25);
		right.add(lbl_nickname);

		// 添加个性签名label
		lbl_signature = new CommonLabel(Global.Lan.get("个性签名") + "：");
		lbl_signature.setBounds(20, 30, 400, 50);
		right.add(lbl_signature);

		// 添加国家label
		lbl_country = new CommonLabel(Global.Lan.get("国家") + "：");
		lbl_country.setBounds(20, 90, 100, 25);
		right.add(lbl_country);

		// 添加性别
		lbl_sex = new CommonLabel(Global.Lan.get("性别") + "：");
		lbl_sex.setBounds(200, 90, 70, 25);
		right.add(lbl_sex);

		// 添加年龄
		lbl_age = new CommonLabel(Global.Lan.get("年龄") + "：");
		lbl_age.setBounds(315, 90, 100, 25);
		right.add(lbl_age);

		// 添加电话
		lbl_tel = new CommonLabel(Global.Lan.get("电话") + "：");
		lbl_tel.setBounds(20, 130, 200, 25);
		right.add(lbl_tel);

		// 添加邮箱
		lbl_mail = new CommonLabel(Global.Lan.get("邮箱") + "：");
		lbl_mail.setBounds(200, 130, 250, 25);
		right.add(lbl_mail);

		// 添加语言
		lbl_lan = new CommonLabel(Global.Lan.get("语言") + "：");
		lbl_lan.setBounds(20, 170, 100, 25);
		right.add(lbl_lan);

		// 添加个人说明
		lbl_descr = new CommonLabel(Global.Lan.get("个人说明") + "：");
		lbl_descr.setBounds(20, 205, 400, 50);
		right.add(lbl_descr);

		this.addCustomComponent(bg, BorderLayout.CENTER);

		// 添加事件
		BtnListener bl = new BtnListener();
		btn_cancel.addActionListener(bl);
	}

	public void getData()
	{
		Request request = new Request(Actions.InquireInfoAction);
		request.setParameter("userId", userId);
		HttpUtil.request(request, new RequestHandler()
		{
			@Override
			public void callback(JSONObject data)
			{
				UserInfo user = data.getObject("userFull", UserInfo.class);
				if (user != null)
				{
					head.setHead(user.getHead());
					lbl_username.setText(lbl_username.getText() + user.getUsername());
					lbl_nickname.setText(lbl_nickname.getText() + user.getNickname());
					lbl_signature.setText(lbl_signature.getText() + user.getSignature());
					lbl_country.setText(lbl_country.getText() + UIUtil.country.get(user.getCountry()));
					lbl_sex.setText(lbl_sex.getText() + UIUtil.gender.get(user.getSex()));
					lbl_age.setText(lbl_age.getText() + user.getAge());
					lbl_tel.setText(lbl_tel.getText() + user.getTel());
					lbl_mail.setText(lbl_mail.getText() + user.getMail());
					lbl_lan.setText(lbl_lan.getText() + UIUtil.lan.get(user.getLanguage()));
					lbl_descr.setText(lbl_descr.getText() + user.getDescription());
				}
			}
		});
	}

	private class BtnListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_cancel)
			{
				FriendInfoForm.this.setVisible(false);
				FriendInfoForm.this.dispose();
			}
		}
	}
}
