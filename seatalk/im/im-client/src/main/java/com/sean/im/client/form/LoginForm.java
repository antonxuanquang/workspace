package com.sean.im.client.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.comp.RoundHeadComp;
import com.sean.im.client.comp.StatusComp;
import com.sean.im.client.constant.Config;
import com.sean.im.client.constant.Global;
import com.sean.im.client.constant.UserSetting;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CustomCheckBox;
import com.sean.im.client.custom.CustomCombobox;
import com.sean.im.client.custom.CustomField;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.util.IMIoUtil;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.constant.StatusEnum;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.UserInfo;

/**
 * 登录界面
 * @author sean
 */
public class LoginForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;
	private CustomField jf_password;
	private CommonButton btn_login, btn_regist;
	private CustomCheckBox jcb_remember;
	private StatusComp state;
	private RoundHeadComp head;
	private CustomCombobox<String> userSelect;

	private UserInfo user;
	private LoginPane login;
	private boolean isLogin = true;
	private Timer timer;
	private Rectangle retLogin;
	private int moveWidth;

	public LoginForm()
	{
		super(270, 600);
		this.setCustomTitle(Global.Lan.get("登录标题"), null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setCustomLayout(null);
		this.setCustomMaxiable(false);

		// 读取上次登录用户
		if (Config.GlobalSetting.Username != null && !Config.GlobalSetting.Username.isEmpty())
		{
			user = IMIoUtil.readUser(Config.GlobalSetting.Username);
		}
		if (user == null)
		{
			user = new UserInfo();
			user.setHead(1);
			user.setUsername("");
			user.setPassword("");
			user.setStatus(StatusEnum.Online);
		}

		login = new LoginPane();
		login.setBounds(0, 0, this.getWidth() * 2, this.getHeight());
		this.addCustomComponent(login);
	}

	private void toggle()
	{
		final int dis = this.getWidth() / 5;
		if (isLogin)
		{
			isLogin = false;
			retLogin = login.getBounds();
			moveWidth = LoginForm.this.getWidth() * -1;

			timer = new Timer(20, new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					retLogin.x -= dis;
					login.setBounds(retLogin);
					if (retLogin.x <= moveWidth)
					{
						timer.stop();
						doLogin();
					}
				}
			});
			timer.start();
		}
		else
		{
			isLogin = true;
			retLogin = login.getBounds();

			timer = new Timer(20, new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					retLogin.x += dis;
					login.setBounds(retLogin);
					if (retLogin.x >= 0)
					{
						timer.stop();
					}
				}
			});
			timer.start();
		}
	}

	private void login()
	{
		String username = userSelect.getText();
		String password = new String(jf_password.getText());
		int state = this.state.getState();
		if (username == null || username.isEmpty() || password == null || password.isEmpty())
		{
			UIUtil.alert(LoginForm.this, Global.Lan.get("用户名密码错误"));
			return;
		}

		Config.GlobalSetting.Remember_Password = jcb_remember.isSelected() ? 1 : 0;

		user.setUsername(username);
		user.setPassword(password);
		user.setStatus(state);

		toggle();
	}

	private void doLogin()
	{
		try
		{
			Request request = new Request(Actions.LoginAction);
			request.setParameter("username", user.getUsername());
			request.setParameter("password", user.getPassword());
			request.setParameter("status", user.getStatus());
			request.setParameter("version", ApplicationContext.Version);
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					int loginrs = data.getIntValue("loginrs");
					if (loginrs == 2)
					{
						UIUtil.alert(null, Global.Lan.get("该版本已经停用，请下载最新版本"));
						return;
					}
					// 登录成功
					if (loginrs == 1)
					{
						UserInfo user = data.getObject("userbrief", UserInfo.class);
						ApplicationContext.User = user;
						HttpUtil.loginerId = user.getId();

						JSONObject ad = data.getJSONObject("adChatform");
						ChatForm.imgUrl = ad.getString("imgUrl");
						ChatForm.link = ad.getString("link");

						MainForm.icons = data.getJSONArray("icons");

						// 保存字典
						UIUtil.initDic(data);

						// 添加用户信息写入/user/xxx/info
						user.setPassword(LoginForm.this.user.getPassword());
						IMIoUtil.writeUser(user);
						// 读取用户配置信息
						ObjectInputStream ois = null;
						try
						{
							ois = new ObjectInputStream(new FileInputStream(new File(Global.Root + "users/" + user.getUsername() + "/setting")));
							UserSetting setting = (UserSetting) ois.readObject();
							Config.UserSetting = setting;
						}
						catch (Exception e)
						{
							// 默认配置
							Config.UserSetting = new UserSetting();
						}
						finally
						{
							if (ois != null)
							{
								try
								{
									ois.close();
								}
								catch (IOException e)
								{
									e.printStackTrace();
								}
							}
						}
						ois = null;

						MainForm mainForm = new MainForm(user, LoginForm.this);
						// 赋值全局窗体
						MainForm.FORM = mainForm;

						// 关闭登录窗体
						LoginForm.this.setVisible(false);

						CustomFrame.WIN_BG = new ImageIcon(Config.UserSetting.skin);

						LoginForm.this.dispose();
						Config.GlobalSetting.Username = user.getUsername();

						// 注册推送服务
						ApplicationContext.Client.open(ApplicationContext.User.getId());
						System.out.println("连接推送服务器");
						ApplicationContext.Client.addListener(MainForm.FORM);

						// 显示主窗体
						MainForm.FORM.setVisible(true);
						MainForm.FORM.repaint();
						// 初始化
						MainForm.FORM.getData();
					}
					// 登录失败
					else
					{
						Config.GlobalSetting.Username = "";
						UIUtil.alert(null, Global.Lan.get("用户名密码错误"));
						toggle();
						return;
					}
				}
			});
			request = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private class LoginPane extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public LoginPane()
		{
			this.setLayout(null);
			this.setOpaque(false);

			// 添加头像
			head = new RoundHeadComp(user.getHead(), 130, 130, 15, 7, 3);
			head.setPreferredSize(new Dimension(130, 130));
			head.setBounds(70, 30, 130, 130);
			this.add(head);

			// 添加用户名
			userSelect = new CustomCombobox<String>(true);
			userSelect.setBounds(60, 200, 150, 25);
			List<String> allUsers = IMIoUtil.readAllUser();
			int length = allUsers.size();
			for (int i = 0; i < length; i++)
			{
				userSelect.addElement(allUsers.get(i));
				if (allUsers.get(i).equals(user.getUsername()))
				{
					userSelect.setSelectedIndex(i);
				}
			}
			this.add(userSelect);
			userSelect.addItemListener(new ItemListener()
			{
				@Override
				public void itemStateChanged(ItemEvent e)
				{
					String username = userSelect.getSelectedItem().toString();
					UserInfo tmp = (UserInfo) IMIoUtil.readUser(username);
					if (tmp != null && head != null)
					{
						head.setHead(tmp.getHead());
						user = tmp;
					}
				}
			});

			// 添加密码
			jf_password = new CustomField(CustomField.JPASSWORD);
			jf_password.setBounds(60, 230, 150, 25);
			this.add(jf_password);

			// 添加用户状态
			state = new StatusComp(user.getStatus(), null);
			state.setBounds(90, 260, 25, 25);
			this.add(state);

			// 添加记住密码
			jcb_remember = new CustomCheckBox(Global.Lan.get("记住密码"));
			jcb_remember.setOpaque(false);
			jcb_remember.setBounds(120, 265, 140, 15);
			this.add(jcb_remember);

			// 添加登录按钮
			btn_login = new CommonButton(Global.Lan.get("登录"));
			btn_login.setBounds(60, 300, 150, 32);
			this.add(btn_login);

			// 添加注册按钮
			btn_regist = new CommonButton(Global.Lan.get("注册"));
			btn_regist.setBounds(60, 340, 150, 32);
			this.add(btn_regist);

			// 如果勾选记住密码
			if (Config.GlobalSetting.Remember_Password == 1)
			{
				jf_password.setText(user.getPassword());
				jcb_remember.setSelected(true);
			}

			// 注册事件
			Btn_Listener bl = new Btn_Listener();
			btn_login.addActionListener(bl);
			btn_regist.addActionListener(bl);
			Key_Listener kl = new Key_Listener();
			jf_password.addKeyListener(kl);
			userSelect.addKeyListener(kl);

			/* 登录ing */
			// 添加头像
			RoundHeadComp head_bg = new RoundHeadComp(user.getHead(), 130, 130, 15, 7, 3);
			head_bg.setPreferredSize(new Dimension(130, 130));
			head_bg.setBounds(70 + LoginForm.this.getWidth(), 30, 130, 130);
			this.add(head_bg);

			// 添加等待动画gif
			JLabel lbl_waiting = new JLabel(Global.Lan.get("正在登录..."));
			lbl_waiting.setForeground(Color.WHITE);
			lbl_waiting.setFont(new Font("", Font.BOLD, 15));
			lbl_waiting.setBounds(95 + LoginForm.this.getWidth(), 180, 136, 25);
			this.add(lbl_waiting);
		}
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_login)
			{
				login();
			}
			else if (e.getSource() == btn_regist)
			{
				new RegistForm().setVisible(true);
			}
		}
	}

	/**
	 * 键盘事件
	 * @author sean
	 */
	private class Key_Listener extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if (e.getSource() == jf_password.getField())
			{
				if (e.getKeyCode() == 10)
				{
					login();
				}
			}
			else
			{
				if (e.getKeyCode() == 10)
				{
					jf_password.requestFocus();
				}
			}
		}
	}
}