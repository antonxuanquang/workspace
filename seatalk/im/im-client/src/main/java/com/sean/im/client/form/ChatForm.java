package com.sean.im.client.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import com.sean.im.client.bubble.Sender;
import com.sean.im.client.comp.ChatMessageComp;
import com.sean.im.client.comp.CountryComp;
import com.sean.im.client.comp.EmotionComp;
import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.comp.InputComp;
import com.sean.im.client.comp.NoticeComp;
import com.sean.im.client.comp.NoticeComp.NoticeListener;
import com.sean.im.client.comp.RecordComp;
import com.sean.im.client.comp.RecordComp.RecordListener;
import com.sean.im.client.constant.Config;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomCheckBox;
import com.sean.im.client.custom.CustomCombobox;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.custom.LightButton;
import com.sean.im.client.util.Browser;
import com.sean.im.client.util.CatchScreen;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.client.util.TimeUtil;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.constant.Loggers;
import com.sean.im.commom.constant.MessageEnum;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Message;
import com.sean.log.core.LogFactory;

/**
 * 聊天窗体
 * @author sean
 */
public class ChatForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;

	private JPanel north, center, east, south;
	private JLabel ad;
	private CommonLabel lbl_signature;
	private LightButton btn_record, btn_clean, btn_upload, btn_image, btn_screenshot, btn_tremble;
	private CommonButton btn_close, btn_send;
	private CustomCombobox<String> jcb_sendType;
	private CustomCheckBox jcb_showori;
	private Friend friend;
	private CountryComp countryComp;
	private EmotionComp emotionComp;
	private int unReadMsgs = 0;
	private InputComp input;
	private ChatMessageComp chatMessage;
	private RecordComp recordComp;
	private HeadComp head;

	public static String imgUrl;
	public static String link;

	private static final Logger logger = LogFactory.getLogger(Loggers.IM);

	public ChatForm(final Friend f)
	{
		super(600, 550);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				ChatFormCache.removeChatForm(f);
			}
		});

		this.friend = f;
		if (friend.getRemark() != null && !friend.getRemark().isEmpty())
		{
			this.setCustomTitle(friend.getRemark(), null);
		}
		else
		{
			this.setCustomTitle(friend.getNickname(), null);
		}
		this.setLocationRelativeTo(null);
		this.setCustomLayout(new BorderLayout());

		north = new JPanel();
		north.setOpaque(false);
		north.setLayout(null);
		north.setPreferredSize(new Dimension(500, 60));

		south = new JPanel();
		south.setOpaque(false);
		south.setLayout(new BorderLayout());
		south.setPreferredSize(new Dimension(500, 130));

		center = new JPanel();
		center.setOpaque(false);
		center.setLayout(new BorderLayout());

		east = new JPanel();
		east.setOpaque(false);
		east.setLayout(new BorderLayout());

		this.addCustomComponent(north, BorderLayout.NORTH);
		this.addCustomComponent(center, BorderLayout.CENTER);
		this.addCustomComponent(south, BorderLayout.SOUTH);
		this.addCustomComponent(east, BorderLayout.EAST);

		// 开始绘制控件
		// =====================添加north面板=========================
		// 添加头像
		HeadComp head_bg = new HeadComp(friend.getHead(), 50, 50);
		head_bg.setHeightLight();
		head_bg.setBounds(5, 0, 50, 50);
		north.add(head_bg);
		head_bg.setCursor(Global.CURSOR_HAND);
		head_bg.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				FriendInfoForm fif = new FriendInfoForm(friend.getFriendId());
				fif.setVisible(true);
				fif.getData();
			}
		});

		if (imgUrl != null && !imgUrl.isEmpty())
		{
			ad = new JLabel();
			ad.setText("<html><img src='" + imgUrl + "' width=200 height=50 /></html>");
			ad.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					Browser.openURL(link, null);
				}
			});
			ad.setOpaque(true);
			ad.setCursor(new Cursor(Cursor.HAND_CURSOR));
			ad.setBackground(Color.BLACK);
			ad.setBounds(this.getWidth() - 205, 0, 200, 50);
			north.add(ad);
		}

		// 添加个性签名
		lbl_signature = new CommonLabel(friend.getSignature(), Color.WHITE);
		lbl_signature.setBounds(65, 0, 500, 25);
		north.add(lbl_signature);

		// =========================绘制south面板============================
		JPanel inputContent = new JPanel();
		inputContent.setOpaque(false);
		inputContent.setPreferredSize(new Dimension(500, 140));
		inputContent.setLayout(new BorderLayout());
		// 工具栏
		JPanel toolPanel = new JPanel();
		toolPanel.setOpaque(false);
		toolPanel.setPreferredSize(new Dimension(500, 40));
		toolPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		// 按钮栏
		JPanel btnPanel = new JPanel();
		btnPanel.setOpaque(false);
		btnPanel.setPreferredSize(new Dimension(500, 35));
		btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		// 输入栏
		JPanel inputPanel = new JPanel();
		inputPanel.setOpaque(false);
		inputPanel.setLayout(new BorderLayout());
		// 头像栏
		JPanel headPanel = new JPanel();
		headPanel.setOpaque(false);
		headPanel.setLayout(null);
		headPanel.setPreferredSize(new Dimension(60, 60));

		inputContent.add(toolPanel, BorderLayout.NORTH);
		inputContent.add(inputPanel, BorderLayout.CENTER);
		inputContent.add(btnPanel, BorderLayout.SOUTH);
		inputContent.add(headPanel, BorderLayout.WEST);

		// =====绘制头像=================
		head = new HeadComp(ApplicationContext.User.getHead(), 50, 50);
		head.setHeightLight();
		head.setBounds(5, 5, 50, 50);
		headPanel.add(head);

		// =====添加输入栏=======
		// 添加输入框
		input = new InputComp();
		inputPanel.add(input);

		south.add(inputContent);

		// ======绘制工具栏===============
		// 添加表情按钮
		emotionComp = new EmotionComp(input);
		toolPanel.add(emotionComp);
		// 添加翻译语言下拉框
		countryComp = new CountryComp();
		toolPanel.add(countryComp);
		// 添加发抖按钮
		btn_tremble = new LightButton(new ImageIcon(Global.Root + "resource/image/chatform/tremble.png"));
		toolPanel.add(btn_tremble);
		btn_tremble.setToolTipText(Global.Lan.get("发送抖动"));
		// 添加聊天记录按钮
		btn_record = new LightButton(new ImageIcon(Global.Root + "resource/image/chatform/record.png"));
		toolPanel.add(btn_record);
		btn_record.setToolTipText(Global.Lan.get("聊天记录"));
		// 添加清空聊天记录
		btn_clean = new LightButton(new ImageIcon(Global.Root + "resource/image/chatform/clean.png"));
		toolPanel.add(btn_clean);
		btn_clean.setToolTipText(Global.Lan.get("清屏"));
		// 添加发送图片
		btn_image = new LightButton(new ImageIcon(Global.Root + "resource/image/chatform/image.png"));
		toolPanel.add(btn_image);
		btn_image.setToolTipText(Global.Lan.get("发送图片"));
		// 添加截图
		btn_screenshot = new LightButton(new ImageIcon(Global.Root + "resource/image/chatform/screenshot.png"));
		toolPanel.add(btn_screenshot);
		btn_screenshot.setToolTipText(Global.Lan.get("截图"));
		// 添加上传文件
		btn_upload = new LightButton(new ImageIcon(Global.Root + "resource/image/chatform/upload.png"));
		toolPanel.add(btn_upload);
		btn_upload.setToolTipText(Global.Lan.get("发送文件"));
		// 显示原文
		jcb_showori = new CustomCheckBox(Global.Lan.get("显示原文"));
		jcb_showori.setForeground(Color.WHITE);
		toolPanel.add(jcb_showori);

		// =======绘制按钮栏========
		// 添加录音按钮
		recordComp = new RecordComp(new Record_Listener());
		recordComp.setPreferredSize(new Dimension(80, 25));
		btnPanel.add(recordComp);
		// 添加关闭按钮
		btn_close = new CommonButton(Global.Lan.get("关闭"));
		btn_close.setPreferredSize(new Dimension(60, 25));
		btnPanel.add(btn_close);
		// 添加发送按钮
		btn_send = new CommonButton(Global.Lan.get("发送"));
		btn_send.setPreferredSize(new Dimension(60, 25));
		btnPanel.add(btn_send);
		// 添加发送快捷键下拉框
		jcb_sendType = new CustomCombobox<String>(false);
		jcb_sendType.setShowLocation(new Point(0, -50));
		jcb_sendType.addElement("Enter");
		jcb_sendType.setPreferredSize(new Dimension(100, 22));
		jcb_sendType.addElement("Ctrl+Enter");
		btnPanel.add(jcb_sendType);
		if (Config.UserSetting.Send_Mode == 2)
		{
			jcb_sendType.setSelectedIndex(1);
		}
		else
		{
			jcb_sendType.setSelectedIndex(0);
		}
		jcb_sendType.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				Config.UserSetting.Send_Mode = jcb_sendType.getSelectedIndex() + 1;
			}
		});

		// ====================================开始绘制center==================================
		// 添加聊天内容框
		chatMessage = new ChatMessageComp(10);
		center.add(chatMessage, BorderLayout.CENTER);

		// 添加翻译提示
		if (Config.UserSetting.isShowTraslatorNotify)
		{
			final NoticeComp notice = new NoticeComp(Global.Lan.get("选择工具栏上的字典可以将对方的消息自动翻译成你选择的国家的语言"));
			notice.addNoticeListener(new NoticeListener()
			{
				@Override
				public void onClose()
				{
					center.remove(notice);
					center.setVisible(false);
					center.setVisible(true);
					Config.UserSetting.isShowTraslatorNotify = false;
				}
			});
			center.add(notice, BorderLayout.NORTH);
		}

		// 添加事件
		Btn_Listener bl = new Btn_Listener();
		btn_close.addActionListener(bl);
		btn_send.addActionListener(bl);
		btn_record.addActionListener(bl);
		jcb_showori.addActionListener(bl);
		btn_clean.addActionListener(bl);
		btn_upload.addActionListener(bl);
		btn_image.addActionListener(bl);
		btn_tremble.addActionListener(bl);
		btn_screenshot.addActionListener(bl);
		Key_Listener kl = new Key_Listener();
		input.getInput().addKeyListener(kl);
		this.addWindowStateListener(new Window_Listener());

		if (Config.UserSetting.Is_Show_Original == 1)
		{
			jcb_showori.setSelected(true);
		}
	}

	/**
	 * 设置头像
	 * @param head
	 */
	public void setHead(int head)
	{
		this.head.setHead(head);
	}

	public InputComp getInput()
	{
		return input;
	}

	/**
	 * 发送消息
	 */
	private void sendMsg()
	{
		String msg = input.getInputMsg();
		if (msg != null && !msg.isEmpty())
		{
			Request request = new Request(Actions.SendMessageAction);
			request.setParameter("receiverId", friend.getFriendId());
			request.setParameter("content", msg);
			HttpUtil.request(request, null);

			Message message = new Message();
			message.setSenderId(ApplicationContext.User.getId());
			message.setReceiverId(friend.getFriendId());
			message.setSendTime(TimeUtil.getYYYYMMDDHHMMSSTime());
			message.setContent(msg);
			message.setType(MessageEnum.Message_Text);
			message.setContent(msg);

			this.chatMessage.addLeftTxtMessage(message, ApplicationContext.getSender());
			input.getInput().setText("");
			input.getInput().requestFocus();
		}
	}

	public void appendRightMessage(Message msg, Sender sender)
	{
		switch (msg.getType())
		{
		case MessageEnum.Message_Text:
			chatMessage.addRightTxtMessage(msg, sender, this.jcb_showori.isSelected());
			break;
		case MessageEnum.Message_Image:
			chatMessage.addRightImageMessage(msg, sender);
			break;
		case MessageEnum.Message_Audio:
			chatMessage.addRightAudioMessage(msg, sender);
			break;
		case MessageEnum.Message_Video:

			break;
		default:
			break;
		}
	}

	/**
	 * 显示窗体
	 */
	public void open()
	{
		if (!this.isVisible())
		{
			this.setVisible(true);
			this.setExtendedState(JFrame.NORMAL);
			// 添加到最近联系人
			MainForm.FORM.getRecentFriendList().addFriend(friend);
		}
	}

	/**
	 * 增加未读消息个数
	 */
	public void addUnReadMsg()
	{
		this.unReadMsgs++;
		this.setTitle(" (" + unReadMsgs + ") " + Global.Lan.get("新消息") + " " + friend.getNickname());
	}

	/**
	 * 普通按钮事件
	 * @author sean
	 */
	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// 关闭
			if (e.getSource() == btn_close)
			{
				ChatFormCache.removeChatForm(friend);
				ChatForm.this.setVisible(false);
				ChatForm.this.dispose();
			}
			// 发送
			else if (e.getSource() == btn_send)
			{
				sendMsg();
			}
			// 聊天记录
			else if (e.getSource() == btn_record)
			{
				ChatRecordForm form = new ChatRecordForm(friend);
				form.setVisible(true);
				form.getData(1);
			}
			// 显示原文
			else if (e.getSource() == jcb_showori)
			{
				Config.UserSetting.Is_Show_Original = jcb_showori.isSelected() ? 1 : 0;
			}
			// 清屏
			else if (e.getSource() == btn_clean)
			{
				chatMessage.clear();
			}
			// 发送抖动
			else if (e.getSource() == btn_tremble)
			{
				shake();
				if (friend.isOnline())
				{
					Request request = new Request(Actions.SendTrembleAction);
					request.setParameter("receiverId", friend.getFriendId());
					HttpUtil.request(request, null);
				}
			}
			// 文件传输
			else if (e.getSource() == btn_upload)
			{
				if (friend.isOnline())
				{
					JFileChooser chooser = new JFileChooser();
					int returnVal = chooser.showOpenDialog(ChatForm.this);
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						File file = chooser.getSelectedFile();
						// 最大50M
						if (file.length() > (50 * 1024 * 1024))
						{
							UIUtil.alert(ChatForm.this, Global.Lan.get("文件最大不能超过50M"));
						}
						else
						{
							FileTransferForm.getInstance().transferFile(file, friend);
							FileTransferForm.getInstance().setVisible(true);
						}
					}
				}
			}
			// 发送图片
			else if (e.getSource() == btn_image)
			{
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "jpeg");
				chooser.setFileFilter(filter);
				chooser.setAcceptAllFileFilterUsed(false);
				int returnVal = chooser.showOpenDialog(ChatForm.this);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					File file = chooser.getSelectedFile();
					// 最大2M
					if (file.length() > (1024 * 1024 * 2))
					{
						UIUtil.alert(ChatForm.this, Global.Lan.get("图片最大不能超过2M"));
					}
					else
					{
						Message msg = new Message();
						msg.setSenderId(ApplicationContext.User.getId());
						msg.setReceiverId(friend.getFriendId());
						msg.setType(MessageEnum.Message_Image);
						msg.setSingleParam(file);

						Request request = new Request(Actions.SendImageAction);
						request.setParameter("receiverId", friend.getFriendId());
						chatMessage.addLeftImageMessage(msg, ApplicationContext.getSender(), request);
					}
				}
			}
			// 截图
			else if (e.getSource() == btn_screenshot)
			{
				CatchScreen sc = new CatchScreen(new CatchScreen.ScreenCatchListener()
				{
					@Override
					public void catched(File img)
					{
						Message msg = new Message();
						msg.setSenderId(ApplicationContext.User.getId());
						msg.setType(MessageEnum.Message_Image);
						msg.setReceiverId(friend.getFriendId());
						msg.setSingleParam(img);
						Request request = new Request(Actions.SendImageAction);
						request.setParameter("receiverId", friend.getFriendId());
						chatMessage.addLeftImageMessage(msg, ApplicationContext.getSender(), request);
					}
				});
				sc.setVisible(true);
				sc = null;
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
			if (e.getKeyCode() == 10 && e.isControlDown())
			{
				if (Config.UserSetting.Send_Mode == 2)
				{
					sendMsg();
				}
			}
			else if (e.getKeyCode() == 10)
			{
				if (Config.UserSetting.Send_Mode == 1)
				{
					sendMsg();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			if (e.getKeyCode() == 10)
			{
				if (Config.UserSetting.Send_Mode == 1)
				{
					input.getInput().setText("");
					input.getInput().requestFocus();
				}
			}
		}
	}

	/**
	 * 窗体状态事件
	 * @author sean
	 */
	private class Window_Listener implements WindowStateListener
	{
		@Override
		public void windowStateChanged(WindowEvent e)
		{
			if (e.getNewState() == NORMAL)
			{
				unReadMsgs = 0;
				if (friend.getRemark() != null && !friend.getRemark().isEmpty())
				{
					setTitle(friend.getRemark());
				}
				else
				{
					setTitle(friend.getNickname());
				}
			}
		}
	}

	/**
	 * 录音事件
	 * @author sean
	 */
	private class Record_Listener implements RecordListener
	{
		@Override
		public void beforeRecord()
		{
			logger.debug("start record...");
		}

		@Override
		public void afterRecord(File audioFile)
		{
			logger.debug("finish record : " + audioFile.getAbsolutePath());
			Message msg = new Message();
			msg.setSenderId(ApplicationContext.User.getId());
			msg.setReceiverId(friend.getFriendId());
			msg.setType(MessageEnum.Message_Audio);
			msg.setSingleParam(audioFile);

			Request request = new Request(Actions.SendAudioAction);
			request.setParameter("receiverId", friend.getFriendId());
			chatMessage.addLeftAudioMessage(msg, ApplicationContext.getSender(), request);
		}
	}
}
