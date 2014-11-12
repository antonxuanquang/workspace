package com.sean.im.client.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.comp.FlockListComp;
import com.sean.im.client.comp.FriendListComp;
import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.comp.RecentFriendComp;
import com.sean.im.client.comp.SearchFriendComp;
import com.sean.im.client.comp.SlideTabComp;
import com.sean.im.client.comp.StatusComp;
import com.sean.im.client.comp.StatusComp.StateListener;
import com.sean.im.client.constant.Config;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.core.Client.ClientListener;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.custom.LightButton;
import com.sean.im.client.custom.TransparentPanel;
import com.sean.im.client.form.flock.AddFlockForm;
import com.sean.im.client.tray.TrayManager;
import com.sean.im.client.util.Browser;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.client.util.MusicUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.constant.StatusEnum;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Group;
import com.sean.im.commom.entity.Message;
import com.sean.im.commom.entity.UserInfo;

/**
 * 主界面
 * @author sean
 */
public class MainForm extends CustomFrame implements ClientListener
{
	private static final long serialVersionUID = 1L;

	public static MainForm FORM;

	private JPanel north, center, south, center2, center3;;
	private CommonLabel lbl_username, lbl_signature;
	private StatusComp state;
	private LightButton btn_addfriend, btn_skin, btn_record, btn_file, btn_addflock;
	private SlideTabComp tab;
	private HeadComp head;
	private FriendListComp friendList;
	private RecentFriendComp recentFriendList;
	private FlockListComp flockList;
	private SearchFriendComp search;

	public static JSONArray icons;

	private static ImageIcon friendTabIcon_down, friendTabIcon_up;
	private static ImageIcon recentTabIcon_down, recentTabIcon_up;
	private static ImageIcon flockTabIcon_down, flockTabIcon_up;
	static
	{
		try
		{
			BufferedImage friendTabIcon = ImageIO.read(new File(Global.Root + "resource/image/main/friendtabicon.png"));
			friendTabIcon_down = new ImageIcon(friendTabIcon.getSubimage(20, 0, 20, 20));
			friendTabIcon_up = friendTabIcon_down;
			friendTabIcon = null;
			BufferedImage recentTabIcon = ImageIO.read(new File(Global.Root + "resource/image/main/recenttabicon.png"));
			recentTabIcon_down = new ImageIcon(recentTabIcon.getSubimage(20, 0, 20, 20));
			recentTabIcon_up = recentTabIcon_down;
			recentTabIcon = null;
			BufferedImage flockTabIcon = ImageIO.read(new File(Global.Root + "resource/image/main/flocktabicon.png"));
			flockTabIcon_down = new ImageIcon(flockTabIcon.getSubimage(20, 0, 20, 20));
			flockTabIcon_up = flockTabIcon_down;
			flockTabIcon = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public MainForm(UserInfo userinfo, LoginForm loginForm)
	{
		super(270, 600);
		this.setCustomTitle(Global.Lan.get("主窗体标题"), null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setCustomLayout(new BorderLayout());
		this.setCustomMaxiable(false);
		this.setLocation(loginForm.getLocation());

		north = new JPanel();
		north.setOpaque(false);
		north.setLayout(null);
		north.setPreferredSize(new Dimension(280, 105));

		center = new TransparentPanel();
		center.setLayout(new BorderLayout());

		center2 = new TransparentPanel();
		center2.setLayout(new BorderLayout());

		center3 = new TransparentPanel();
		center3.setLayout(new BorderLayout());

		south = new JPanel();
		south.setOpaque(false);
		south.setPreferredSize(new Dimension(280, 60));
		south.setLayout(null);

		tab = new SlideTabComp(this.getWidth(), this.getHeight() - north.getPreferredSize().height - south.getPreferredSize().height, 3);
		tab.addTabPanel(center, friendTabIcon_up, friendTabIcon_down);
		tab.addTabPanel(center2, flockTabIcon_up, flockTabIcon_down);
		tab.addTabPanel(center3, recentTabIcon_up, recentTabIcon_down);
		tab.setSelectedTab(0);

		this.addCustomComponent(north, BorderLayout.NORTH);
		this.addCustomComponent(tab, BorderLayout.CENTER);
		this.addCustomComponent(south, BorderLayout.SOUTH);

		// 添加头像
		head = new HeadComp(userinfo.getHead(), 42, 42);
		head.setHeightLight();
		head.setBounds(3, 3, 42, 42);
		head.setCursor(Global.CURSOR_HAND);
		north.add(head);
		head.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				MyInfoForm mif = new MyInfoForm();
				mif.setVisible(true);
				mif.getData();
			}
		});

		// 添加状态下拉框
		state = new StatusComp(userinfo.getStatus(), new StateListener()
		{
			@Override
			public void stateChanged(final int state)
			{
				// 如果状态有改变
				if (state != ApplicationContext.User.getStatus())
				{
					Request request = new Request(Actions.ChangeStatusAction);
					request.setParameter("status", state);
					HttpUtil.request(request, new RequestHandler()
					{
						@Override
						public void callback(JSONObject data)
						{
							ApplicationContext.User.setStatus(state);
						}
					});
				}
			}
		});
		state.setBounds(45, 0, 25, 25);
		north.add(state);

		// 添加用户帐号信息
		lbl_username = new CommonLabel(userinfo.getNickname(), Color.WHITE);
		lbl_username.setBounds(70, 0, 1000, 25);
		north.add(lbl_username);

		// 添加用户签名
		lbl_signature = new CommonLabel(userinfo.getSignature(), Color.WHITE);
		lbl_signature.setBounds(55, 20, 1000, 25);
		north.add(lbl_signature);

		JPanel toolbar = new JPanel();
		toolbar.setOpaque(false);
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		toolbar.setBounds(5, 45, 300, 30);
		north.add(toolbar);

		// 好友搜索
		search = new SearchFriendComp();
		search.setBounds(1, 75, 266, 30);
		north.add(search);

		// 好友列表
		this.friendList = new FriendListComp();
		center.add(friendList, BorderLayout.CENTER);

		// 群列表
		this.flockList = new FlockListComp();
		center2.add(flockList, BorderLayout.CENTER);

		// 最近联系人
		this.recentFriendList = new RecentFriendComp();
		center3.add(recentFriendList, BorderLayout.CENTER);

		JPanel bbar = new JPanel();
		bbar.setOpaque(false);
		bbar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		bbar.setBounds(5, 15, 300, 30);
		south.add(bbar);

		// 添加皮肤按钮
		btn_skin = new LightButton(new ImageIcon(Global.Root + "resource/image/main/skin.png"));
		btn_skin.setToolTipText(Global.Lan.get("皮肤"));
		toolbar.add(btn_skin);

		for (int i = 0; i < icons.size(); i++)
		{
			final JSONObject item = icons.getJSONObject(i);

			LightButton btn = new LightButton("<html><img src='" + item.getString("imgUrl") + "' /></html>");
			toolbar.add(btn);

			btn.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Browser.openURL(item.getString("link"), null);
				}
			});
		}

		// 添加好友按钮
		btn_addfriend = new LightButton(new ImageIcon(Global.Root + "resource/image/main/addfriend.png"));
		btn_addfriend.setToolTipText(Global.Lan.get("添加好友"));
		bbar.add(btn_addfriend);

		// 添加群
		btn_addflock = new LightButton(new ImageIcon(Global.Root + "resource/image/main/addflock.png"));
		btn_addflock.setToolTipText(Global.Lan.get("添加群"));
		bbar.add(btn_addflock);

		// 添加聊天记录
		btn_record = new LightButton(new ImageIcon(Global.Root + "resource/image/main/record.png"));
		btn_record.setToolTipText(Global.Lan.get("聊天记录"));
		bbar.add(btn_record);

		// 文件传输
		btn_file = new LightButton(new ImageIcon(Global.Root + "resource/image/main/upload.png"));
		btn_file.setToolTipText(Global.Lan.get("文件传输"));
		bbar.add(btn_file);

		// 创建系统托盘
		TrayManager.getInstance().init();

		// 添加事件
		Btn_Listener bl = new Btn_Listener();
		btn_addfriend.addActionListener(bl);
		btn_skin.addActionListener(bl);
		btn_record.addActionListener(bl);
		btn_file.addActionListener(bl);
		btn_addflock.addActionListener(bl);
		this.addWindowListener(new Win_Listener());
	}

	/**
	 * 设置用户信息
	 */
	public void reSetUserInfo()
	{
		UserInfo userinfo = ApplicationContext.User;
		this.head.setHead(userinfo.getHead());
		lbl_username.setText(userinfo.getNickname());
		lbl_signature.setText(userinfo.getSignature());
	}

	/**
	 * 读取好友列表
	 */
	public FriendListComp getFriendList()
	{
		return this.friendList;
	}

	/**
	 * 读取最近联系人
	 */
	public RecentFriendComp getRecentFriendList()
	{
		return this.recentFriendList;
	}

	public FlockListComp getFlockList()
	{
		return this.flockList;
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// 添加好友
			if (e.getSource() == btn_addfriend)
			{
				new SearchForm().setVisible(true);
			}
			// 换皮肤
			else if (e.getSource() == btn_skin)
			{
				new SkinForm().setVisible(true);
			}
			// 聊天记录
			else if (e.getSource() == btn_record)
			{
				new ChatRecordForm(null).setVisible(true);
			}
			// 文件传输
			else if (e.getSource() == btn_file)
			{
				FileTransferForm.getInstance().setVisible(true);
			}
			// 添加群
			else if (e.getSource() == btn_addflock)
			{
				new AddFlockForm().setVisible(true);
			}
		}
	}

	private class Win_Listener extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			Request request = new Request("ExitAction");
			HttpUtil.requestBlock(request, null);

			// 保存设置信息
			try
			{
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Global.Root + "config/setting")));
				oos.writeObject(Config.GlobalSetting);
				oos.close();

				oos = new ObjectOutputStream(new FileOutputStream(new File(Global.Root + "users/" + ApplicationContext.User.getUsername()
						+ "/setting")));
				oos.writeObject(Config.UserSetting.getSetting());
				oos.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 初始化
	 */
	public void getData()
	{
		// 读取好友列表
		Request request = new Request(Actions.InquireFriendListAction);
		HttpUtil.request(request, new RequestHandler()
		{
			@Override
			public void callback(JSONObject data)
			{
				// 显示好友列表
				List<Group> groups = JSON.parseArray(data.getString("grouplist"), Group.class);
				List<Friend> friends = JSON.parseArray(data.getString("friendlist"), Friend.class);
				for (Friend f : friends)
				{
					for (Group g : groups)
					{
						if (f.getGroupId() == g.getId())
						{
							g.getFriends().add(f);
						}
					}
				}
				friendList.setGroup(groups);

				// 显示最近联系人
				List<Friend> fs = new LinkedList<Friend>();
				if (Config.UserSetting.recentFriends != null)
				{
					for (long id : Config.UserSetting.recentFriends)
					{
						fs.add(friendList.getFriend(id));
					}
				}
				recentFriendList.setFriend(fs);

				// 初始化聊天窗体缓存
				if (Global.Optmized)
				{
					ChatFormCache.init();
				}

				// 读取未读消息
				Request request = new Request(Actions.InquireUnReadMsgAction);
				HttpUtil.request(request, new RequestHandler()
				{
					@Override
					public void callback(JSONObject data)
					{
						List<Message> msgs = JSON.parseArray(data.getString("msgs"), Message.class);
						if (msgs != null && !msgs.isEmpty())
						{
							ApplicationContext.CTX.getMessageQueue().addAll(msgs);
							Message msg = msgs.get(0);
							if (msg.isChatMessage())
							{
								Friend friend = MainForm.FORM.getFriendList().getFriendByUserId(msg.getSenderId());
								TrayManager.getInstance().startLight(friend.getHead());
							}
							else
							{
								TrayManager.getInstance().startLight(0);
							}
							MusicUtil.play(Global.Root + "resource/sound/msg.wav");
						}

						// gc
						System.gc();
					}
				});
			}
		});

		// 读取群列表
		request = new Request(Actions.InquireFlockListAction);
		HttpUtil.request(request, new RequestHandler()
		{
			@Override
			public void callback(JSONObject data)
			{
				List<Flock> flocks = JSON.parseArray(data.getString("flockList"), Flock.class);
				MainForm.this.flockList.setFlock(flocks);
			}
		});
	}

	@Override
	public void disconnect()
	{
		this.state.setState(StatusEnum.OffLine);
	}

	@Override
	public void connect()
	{
		this.state.setState(StatusEnum.Online);
	}
}
