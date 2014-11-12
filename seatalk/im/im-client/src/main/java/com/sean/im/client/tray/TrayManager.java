package com.sean.im.client.tray;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.form.ChatForm;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.form.flock.ChatRoomForm;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.commom.constant.StatusEnum;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.entity.Flock;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Message;

/**
 * 系统托盘管理
 * @author sean
 */
@SuppressWarnings("static-access")
public class TrayManager
{
	private static TrayIcon trayIcon;
	private SystemTray tray;
	private TrayThread trayThread;
	private boolean isLight = false;
	private int MSG = 0;
	private MenuItem show, exit;

	private static ImageIcon online = new ImageIcon(Global.Root + "resource/image/icon.png");
	private static ImageIcon leave = new ImageIcon(Global.Root + "resource/image/leave.png");
	private static ImageIcon offline = new ImageIcon(Global.Root + "resource/image/offline.png");
	private static ImageIcon curr = online;

	private static TrayManager instance = new TrayManager();

	private TrayManager()
	{
	}

	public static TrayManager getInstance()
	{
		return instance;
	}

	public static void setStatus(int state)
	{
		switch (state)
		{
		case StatusEnum.Online:
			curr = online;
			break;
		case StatusEnum.Leave:
			curr = leave;
			break;
		case StatusEnum.Hide:
			curr = offline;
			break;
		case StatusEnum.OffLine:
			curr = offline;
			break;
		}

		if (trayIcon != null)
		{
			trayIcon.setImage(curr.getImage());
		}
	}

	public void init()
	{
		tray = SystemTray.getSystemTray();
		PopupMenu pop = new PopupMenu();
		show = new MenuItem("show main");
		exit = new MenuItem("exit");
		MenuItem_Listener ml = new MenuItem_Listener();
		show.addActionListener(ml);
		exit.addActionListener(ml);
		pop.add(show);
		pop.add(exit);
		trayIcon = new TrayIcon(curr.getImage(), "Seatalk", pop);
		trayIcon.setImageAutoSize(true);
		try
		{
			tray.add(trayIcon);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		trayIcon.addMouseListener(new MouseClickListener());
	}

	public void startLight(long headId)
	{
		if (!isLight)
		{
			isLight = true;
			try
			{
				Thread.currentThread().sleep(600);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			if (this.trayThread == null)
			{
				trayThread = new TrayThread(headId);
				new Thread(trayThread).start();
			}
		}
	}

	public void startLight(String headPath)
	{
		if (!isLight)
		{
			isLight = true;
			try
			{
				Thread.currentThread().sleep(600);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			if (this.trayThread == null)
			{
				trayThread = new TrayThread(headPath);
				new Thread(trayThread).start();
			}
		}
	}

	public void shutdownLight()
	{
		if (isLight)
		{
			isLight = false;
			if (this.trayThread != null)
			{
				trayThread.shutdown();
			}
		}
	}

	private class MenuItem_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == show)
			{
				MainForm.FORM.setVisible(true);
				MainForm.FORM.setState(JFrame.NORMAL);
			}
			else if (e.getSource() == exit)
			{
				Request request = new Request("ExitAction");
				HttpUtil.requestBlock(request, null);
				System.exit(0);
			}
		}
	}

	private class MouseClickListener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (e.getClickCount() == 2)
			{
				List<Message> msgs = ApplicationContext.CTX.getMessageQueue();
				List<Message> topMsgs = new ArrayList<Message>(msgs.size());

				Message tmp;
				// 如果有未读消息
				if (!msgs.isEmpty())
				{
					final Message top = msgs.remove(0);
					topMsgs.add(top);

					// 如果是普通聊天信息或者图片或者语音
					if (top.isChatMessage())
					{
						// 继续读取相同人发送的聊天消息
						for (int i = 0; i < msgs.size(); i++)
						{
							tmp = msgs.get(i);
							if (tmp.getSenderId() == top.getSenderId() && (tmp.isChatMessage()))
							{
								topMsgs.add(msgs.remove(i));
								i--;
							}
						}
						// 打开聊天窗体，显示消息
						Friend friend = MainForm.FORM.getFriendList().getFriendByUserId(top.getSenderId());
						ChatForm chatForm = ChatFormCache.getChatForm(friend);
						chatForm.open();
						for (Message item : topMsgs)
						{
							chatForm.appendRightMessage(item, ApplicationContext.getSender(friend));
						}
					}
					// 如果是群消息
					else if (top.isFlockChatMessage())
					{
						// 继续读取相同群发送的聊天消息
						for (int i = 0; i < msgs.size(); i++)
						{
							tmp = msgs.get(i);
							if (tmp.getFlockId() == top.getFlockId() && (tmp.isChatMessage()))
							{
								topMsgs.add(msgs.remove(i));
								i--;
							}
						}
						// 打开聊天窗体，显示消息
						Flock flock = MainForm.FORM.getFlockList().getFlock(top.getFlockId());
						ChatRoomForm room = ChatFormCache.getChatRoomForm(flock);
						room.open();
						room.initData();
						for (Message item : topMsgs)
						{
							room.appendRightMessage(item, ApplicationContext.getSender(room.getFlockMember(item.getSenderId())));
						}
					}
					else
					{
						ApplicationContext.CTX.doMessage(top);
					}

					// 停止闪耀
					shutdownLight();

					if (msgs.size() > 0)
					{
						Message top1 = msgs.get(0);
						if (top1.isChatMessage())
						{
							Friend friend = MainForm.FORM.getFriendList().getFriendByUserId(top1.getSenderId());
							startLight(friend.getHead());
						}
						else
						{
							startLight(0);
						}
					}
				}
				else
				{
					MainForm.FORM.setVisible(true);
					MainForm.FORM.setState(JFrame.NORMAL);
				}
			}
		}
	}

	private class TrayThread implements Runnable
	{
		private int flag = 0;
		private boolean run = true;
		private ImageIcon head, bg;

		public TrayThread(long headId)
		{
			if (headId == MSG)
			{
				head = new ImageIcon(Global.Root + "resource/image/msg.png");
			}
			else
			{
				head = new ImageIcon(Global.Root + "resource/image/head/" + headId + ".jpg");
			}
			bg = new ImageIcon(Global.Root + "resource/image/tray_bg.png");
		}

		public TrayThread(String headPath)
		{
			head = new ImageIcon(headPath);
			bg = new ImageIcon(Global.Root + "resource/image/tray_bg.png");
		}

		public void shutdown()
		{
			run = false;
		}

		@Override
		public void run()
		{
			while (run)
			{
				if (flag % 2 == 0)
				{
					trayIcon.setImage(head.getImage());
				}
				else
				{
					trayIcon.setImage(bg.getImage());
				}
				try
				{
					Thread.currentThread().sleep(500);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				flag++;
			}
			trayThread = null;
			trayIcon.setImage(curr.getImage());
		}
	}
}
