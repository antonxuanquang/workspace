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
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CheckCode;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomField;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;

/**
 * 用户注册
 * @author sean
 */
public class RegistForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel bg, south;
	private CommonLabel lbl_username, lbl_nickname, lbl_password, lbl_cfm_password;
	private CommonButton btn_regist, btn_cancel;
	private CustomField jf_pwd, jf_confirm, jf_username, jf_nickname, jf_code;
	private CheckCode code;

	public RegistForm()
	{
		super(300, 250);
		this.setCustomTitle(Global.Lan.get("注册标题"), null);
		this.setLocationRelativeTo(null);
		this.setCustomMaxiable(false);
		this.setCustomLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		bg = new JPanel();
		bg.setBackground(Color.WHITE);
		bg.setLayout(null);
		this.addCustomComponent(bg, BorderLayout.CENTER);

		south = new JPanel();
		south.setOpaque(false);
		south.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.addCustomComponent(south, BorderLayout.SOUTH);

		lbl_username = new CommonLabel(Global.Lan.get("帐号") + " : ");
		lbl_username.setBounds(20, 10, 100, 25);
		bg.add(lbl_username);
		jf_username = new CustomField();
		jf_username.setBounds(100, 10, 150, 25);
		bg.add(jf_username);

		lbl_nickname = new CommonLabel(Global.Lan.get("昵称") + " : ");
		lbl_nickname.setBounds(20, 40, 100, 25);
		bg.add(lbl_nickname);
		jf_nickname = new CustomField();
		jf_nickname.setBounds(100, 40, 150, 25);
		bg.add(jf_nickname);

		lbl_password = new CommonLabel(Global.Lan.get("密码") + " : ");
		lbl_password.setBounds(20, 70, 100, 25);
		bg.add(lbl_password);
		jf_pwd = new CustomField(CustomField.JPASSWORD);
		jf_pwd.setBounds(100, 70, 150, 25);
		bg.add(jf_pwd);

		lbl_cfm_password = new CommonLabel(Global.Lan.get("确认密码") + " : ");
		lbl_cfm_password.setBounds(20, 100, 100, 25);
		bg.add(lbl_cfm_password);
		jf_confirm = new CustomField(CustomField.JPASSWORD);
		jf_confirm.setBounds(100, 100, 150, 25);
		bg.add(jf_confirm);

		code = new CheckCode();
		code.setBounds(20, 130, 100, 50);
		bg.add(code);
		jf_code = new CustomField();
		jf_code.setBounds(100, 140, 150, 25);
		bg.add(jf_code);

		btn_regist = new CommonButton(Global.Lan.get("注册"));
		btn_regist.setPreferredSize(new Dimension(65, 25));
		south.add(btn_regist);
		btn_cancel = new CommonButton(Global.Lan.get("取消"));
		btn_cancel.setPreferredSize(new Dimension(65, 25));
		south.add(btn_cancel);

		// 添加事件
		Btn_Listener bl = new Btn_Listener();
		btn_cancel.addActionListener(bl);
		btn_regist.addActionListener(bl);
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_cancel)
			{
				RegistForm.this.setVisible(false);
				RegistForm.this.dispose();
			}
			else if (e.getSource() == btn_regist)
			{
				String codeTxt = jf_code.getText();
				String username = jf_username.getText();
				String nickname = jf_nickname.getText();
				String password = new String(jf_pwd.getText());
				String cfmpwd = new String(jf_confirm.getText());

				if (codeTxt.isEmpty() || username.isEmpty() || nickname.isEmpty() || password.isEmpty() || cfmpwd.isEmpty())
				{
					UIUtil.alert(null, Global.Lan.get("请输入完整信息后再注册"));
					return;
				}
				if (!password.equals(cfmpwd))
				{
					UIUtil.alert(null, Global.Lan.get("两次密码不一致"));
					return;
				}
				if (!code.check(codeTxt))
				{
					UIUtil.alert(null, Global.Lan.get("验证码错误"));
					return;
				}

				Request request = new Request(Actions.RegistAction);
				request.setParameter("username", username);
				request.setParameter("nickname", nickname);
				request.setParameter("password", password);
				HttpUtil.request(request, new RequestHandler()
				{
					@Override
					public void callback(JSONObject data)
					{
						int rs = data.getIntValue("registrs");
						if (rs == 1)
						{
							UIUtil.alert(null, Global.Lan.get("注册成功"));
							RegistForm.this.setVisible(false);
							RegistForm.this.dispose();
						}
						else
						{
							UIUtil.alert(null, Global.Lan.get("该用户名已经存在"));
						}
					}
				});
			}
		}
	}
}
