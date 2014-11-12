package com.sean.im.client.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.comp.SlideTabComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomField;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.client.util.ComboBoxItem;
import com.sean.im.client.util.IMIoUtil;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.UserInfo;

/**
 * 个人信息
 * @author sean
 */
public class MyInfoForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;

	private CommonLabel lbl_username;
	private JPanel south, north;
	private SlideTabComp tab;
	private CommonButton btn_close, btn_change_head;
	private HeadComp head;
	private InfoPanel ip;
	private PasswordPanel pp;

	public MyInfoForm()
	{
		super(450, (int) MainForm.FORM.getSize().getHeight());
		Global.tmpHead = 0;
		this.setCustomTitle(Global.Lan.get("个人资料"), null);
		this.setCustomMaxiable(false);
		this.setCustomLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Point point = MainForm.FORM.getLocationOnScreen();
		this.setLocation(point.x - this.getWidth(), point.y);

		north = new JPanel();
		north.setOpaque(false);
		north.setLayout(null);
		north.setPreferredSize(new Dimension(300, 80));
		this.addCustomComponent(north, BorderLayout.NORTH);

		south = new JPanel();
		south.setOpaque(false);
		south.setLayout(new FlowLayout(FlowLayout.RIGHT));
		south.setPreferredSize(new Dimension(300, 35));
		this.addCustomComponent(south, BorderLayout.SOUTH);

		head = new HeadComp(1, 70, 70);
		head.setHeightLight();
		head.setBounds(10, 5, 70, 70);
		north.add(head);

		// 添加用户名label
		lbl_username = new CommonLabel(ApplicationContext.User.getUsername());
		lbl_username.setForeground(Color.WHITE);
		lbl_username.setBounds(90, 10, 200, 25);
		north.add(lbl_username);

		// 添加修改头像按钮
		btn_change_head = new CommonButton(Global.Lan.get("更换头像"));
		btn_change_head.setBounds(85, 50, 80, 25);
		north.add(btn_change_head);

		tab = new SlideTabComp(this.getWidth(), this.getHeight() - north.getPreferredSize().height - south.getPreferredSize().height, 3);
		ip = new InfoPanel();
		tab.addTabPanel(ip, Global.Lan.get("个人资料"));
		pp = new PasswordPanel();
		tab.addTabPanel(pp, Global.Lan.get("修改密码"));
		tab.setSelectedTab(0);
		this.addCustomComponent(tab, BorderLayout.CENTER);

		btn_close = new CommonButton(Global.Lan.get("关闭"));
		btn_close.setPreferredSize(new Dimension(60, 25));
		south.add(btn_close);

		// 添加事件
		BtnListener bl = new BtnListener();
		btn_change_head.addActionListener(bl);
		btn_close.addActionListener(bl);
	}

	public void getData()
	{
		ip.getData();
	}

	public void setHead(int headId)
	{
		head.setHead(headId);
	}

	/**
	 * 个人资料
	 * @author sean
	 */
	private class InfoPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private CommonButton btn_save;
		private CommonLabel lbl_nickname, lbl_country, lbl_signature, lbl_sex, lbl_age, lbl_tel, lbl_mail, lbl_lan, lbl_descr;
		private CustomField jf_nickname, jf_tel, jf_mail, jf_signature, jf_descr;
		private JComboBox<ComboBoxItem> jcb_country, jcb_sex, jcb_lan;
		private JComboBox<Integer> jcb_age;

		private InfoPanel()
		{
			this.setLayout(null);
			this.setBackground(Color.WHITE);

			// 添加昵称
			lbl_nickname = new CommonLabel(Global.Lan.get("昵称"));
			lbl_nickname.setBounds(10, 10, 80, 25);
			this.add(lbl_nickname);
			jf_nickname = new CustomField();
			jf_nickname.setBounds(50, 10, 130, 25);
			this.add(jf_nickname);

			// 添加个性签名
			lbl_signature = new CommonLabel(Global.Lan.get("个性签名"));
			lbl_signature.setBounds(10, 40, 200, 25);
			this.add(lbl_signature);
			jf_signature = new CustomField(CustomField.JTEXTAREA);
			jf_signature.setBounds(10, 70, 430, 60);
			this.add(jf_signature);

			// 添加国家
			lbl_country = new CommonLabel(Global.Lan.get("国家"));
			lbl_country.setBounds(10, 140, 80, 25);
			this.add(lbl_country);
			jcb_country = UIUtil.getCountryComboBox();
			jcb_country.setBounds(50, 140, 100, 25);
			this.add(jcb_country);

			// 添加性别
			lbl_sex = new CommonLabel(Global.Lan.get("性别"));
			lbl_sex.setBounds(170, 145, 50, 15);
			this.add(lbl_sex);
			jcb_sex = UIUtil.getSexComboBox();
			jcb_sex.setBounds(210, 140, 70, 25);
			this.add(jcb_sex);

			// 添加年龄
			lbl_age = new CommonLabel(Global.Lan.get("年龄"));
			lbl_age.setBounds(300, 145, 50, 15);
			this.add(lbl_age);
			jcb_age = UIUtil.getAgeComboBox();
			jcb_age.setBounds(330, 140, 60, 25);
			this.add(jcb_age);

			// 添加电话
			lbl_tel = new CommonLabel(Global.Lan.get("电话"));
			lbl_tel.setBounds(10, 185, 50, 15);
			this.add(lbl_tel);
			jf_tel = new CustomField();
			jf_tel.setBounds(50, 180, 100, 25);
			this.add(jf_tel);

			// 添加邮箱
			lbl_mail = new CommonLabel(Global.Lan.get("邮箱"));
			lbl_mail.setBounds(170, 185, 50, 15);
			this.add(lbl_mail);
			jf_mail = new CustomField();
			jf_mail.setBounds(210, 180, 180, 25);
			this.add(jf_mail);

			// 添加语言
			lbl_lan = new CommonLabel(Global.Lan.get("语言"));
			lbl_lan.setBounds(10, 225, 80, 15);
			this.add(lbl_lan);
			jcb_lan = UIUtil.getLanguageComboBox();
			jcb_lan.setBounds(50, 220, 80, 25);
			this.add(jcb_lan);

			// 添加个人说明
			lbl_descr = new CommonLabel(Global.Lan.get("个人说明"));
			lbl_descr.setBounds(10, 265, 200, 15);
			this.add(lbl_descr);
			jf_descr = new CustomField(CustomField.JTEXTAREA);
			jf_descr.setBounds(10, 290, 430, 80);
			this.add(jf_descr);

			// 添加确定按钮
			btn_save = new CommonButton(Global.Lan.get("保存"));
			btn_save.setBounds(380, 400, 60, 25);
			this.add(btn_save);

			btn_save.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String nickname = jf_nickname.getText();
					if (nickname == null || nickname.isEmpty())
					{
						UIUtil.alert(MyInfoForm.this, Global.Lan.get("昵称不能为空"));
						return;
					}
					UserInfo userinfo = ApplicationContext.User;
					userinfo.setNickname(nickname);
					userinfo.setSignature(jf_signature.getText());
					userinfo.setCountry(((ComboBoxItem) jcb_country.getSelectedItem()).getId());
					userinfo.setSex(((ComboBoxItem) jcb_sex.getSelectedItem()).getId());
					userinfo.setAge(Integer.parseInt(jcb_age.getSelectedItem().toString()));
					userinfo.setTel(jf_tel.getText());
					userinfo.setMail(jf_mail.getText());
					userinfo.setLanguage(((ComboBoxItem) jcb_lan.getSelectedItem()).getId());
					userinfo.setDescription(jf_descr.getText());
					if (Global.tmpHead != 0)
					{
						userinfo.setHead(Global.tmpHead);
					}

					Request request = new Request(Actions.UpdateInfoAction);
					request.setParameter("nickname", userinfo.getNickname());
					request.setParameter("signature", userinfo.getSignature());
					request.setParameter("country", userinfo.getCountry());
					request.setParameter("sex", userinfo.getSex());
					request.setParameter("age", userinfo.getAge());
					request.setParameter("tel", userinfo.getTel());
					request.setParameter("mail", userinfo.getMail());
					request.setParameter("language", userinfo.getLanguage());
					request.setParameter("description", userinfo.getDescription());
					if (Global.tmpHead != 0)
					{
						userinfo.setHead(Global.tmpHead);
						request.setParameter("head", userinfo.getHead());
						ChatFormCache.setHead(Global.tmpHead);
					}
					HttpUtil.request(request, new RequestHandler()
					{
						@Override
						public void callback(JSONObject data)
						{
							UIUtil.alert(MyInfoForm.this, Global.Lan.get("保存成功"));
							MainForm.FORM.reSetUserInfo();

							IMIoUtil.writeUser(ApplicationContext.User);
						}
					});
				}
			});
		}

		public void getData()
		{
			Request protocol = new Request(Actions.InquireInfoAction);
			protocol.setParameter("userId", ApplicationContext.User.getId());
			HttpUtil.request(protocol, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					UserInfo userinfo = data.getObject("userFull", UserInfo.class);
					head.setHead(userinfo.getHead());
					jf_nickname.setText(userinfo.getNickname());
					jf_signature.setText(userinfo.getSignature());

					for (int i = 0; i < jcb_country.getItemCount(); i++)
					{
						if (jcb_country.getItemAt(i).getId() == userinfo.getCountry())
						{
							jcb_country.setSelectedIndex(i);
							break;
						}
					}

					for (int i = 0; i < jcb_sex.getItemCount(); i++)
					{
						if (jcb_sex.getItemAt(i).getId() == userinfo.getSex())
						{
							jcb_sex.setSelectedIndex(i);
							break;
						}
					}

					for (int i = 0; i < jcb_lan.getItemCount(); i++)
					{
						if (jcb_lan.getItemAt(i).getId() == userinfo.getLanguage())
						{
							jcb_lan.setSelectedIndex(i);
							break;
						}
					}

					for (int i = 0; i < jcb_age.getItemCount(); i++)
					{
						if (jcb_age.getItemAt(i) == userinfo.getAge())
						{
							jcb_age.setSelectedIndex(i);
							break;
						}
					}

					jf_tel.setText(userinfo.getTel());
					jf_mail.setText(userinfo.getMail());
					jf_descr.setText(userinfo.getDescription());
				}
			});
		}
	}

	/**
	 * 修改密码
	 * @author sean
	 */
	private class PasswordPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private CommonLabel lbl_old, lbl_new, lbl_confirm;
		private CommonButton btn_save;
		private CustomField jf_old, jf_new, jf_confirm;

		private PasswordPanel()
		{
			this.setLayout(null);
			this.setBackground(Color.WHITE);

			lbl_old = new CommonLabel(Global.Lan.get("旧密码") + " : ");
			lbl_old.setBounds(20, 10, 100, 25);
			this.add(lbl_old);
			jf_old = new CustomField(CustomField.JPASSWORD);
			jf_old.setBounds(100, 10, 150, 25);
			this.add(jf_old);

			lbl_new = new CommonLabel(Global.Lan.get("新密码") + " : ");
			lbl_new.setBounds(20, 40, 100, 25);
			this.add(lbl_new);
			jf_new = new CustomField(CustomField.JPASSWORD);
			jf_new.setBounds(100, 40, 150, 25);
			this.add(jf_new);

			lbl_confirm = new CommonLabel(Global.Lan.get("确认密码") + " : ");
			lbl_confirm.setBounds(20, 70, 100, 25);
			this.add(lbl_confirm);
			jf_confirm = new CustomField(CustomField.JPASSWORD);
			jf_confirm.setBounds(100, 70, 150, 25);
			this.add(jf_confirm);

			btn_save = new CommonButton(Global.Lan.get("保存"));
			btn_save.setBounds(330, 400, 60, 25);
			this.add(btn_save);

			btn_save.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					String old_pd = new String(jf_old.getText());
					final String new_pd = new String(jf_new.getText());
					String cfm_pd = new String(jf_confirm.getText());

					if (old_pd.isEmpty() || new_pd.isEmpty() || cfm_pd.isEmpty())
					{
						UIUtil.alert(null, Global.Lan.get("请输入完整密码"));
						return;
					}

					if (!new_pd.equals(cfm_pd))
					{
						UIUtil.alert(null, Global.Lan.get("两次密码不一致"));
						return;
					}

					Request request = new Request(Actions.ChangePasswordAction);
					request.setParameter("password", new_pd);
					request.setParameter("oldpassword", old_pd);
					HttpUtil.request(request, new RequestHandler()
					{
						@Override
						public void callback(JSONObject data)
						{
							int changePasswordRs = data.getIntValue("changePasswordRs");
							if (changePasswordRs == 1)
							{
								UIUtil.alert(null, Global.Lan.get("保存成功"));
								ApplicationContext.User.setPassword(new_pd);
							}
							else
							{
								UIUtil.alert(null, Global.Lan.get("旧密码错误"));
							}
						}
					});
				}
			});
		}
	}

	private class BtnListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_change_head)
			{
				new HeadForm(MyInfoForm.this).setVisible(true);
			}
			else if (e.getSource() == btn_close)
			{
				MyInfoForm.this.setVisible(false);
			}
		}
	}
}
