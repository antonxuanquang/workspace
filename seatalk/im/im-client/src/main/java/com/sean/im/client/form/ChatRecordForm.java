package com.sean.im.client.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.custom.CustomList;
import com.sean.im.client.custom.CustomScrollPane;
import com.sean.im.client.listitem.SimpleFriendPanel;
import com.sean.im.client.util.TimeUtil;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.Message;

/**
 * 聊天记录窗体
 * @author sean
 */
public class ChatRecordForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;
	private CustomList friendlist;
	private CommonButton btn_previous, btn_next, btn_del;
	private JTextPane jtp_content;
	private JLabel lbl_pageNo;
	private Friend friend;
	private int pageNo = 1;

	public ChatRecordForm(Friend friend)
	{
		super(800, 600);
		this.friend = friend;
		this.setCustomTitle(Global.Lan.get("聊天记录"), null);
		this.setCustomLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		friendlist = new CustomList();
		friendlist.setOpaque(true);
		friendlist.setBackground(Color.WHITE);
		friendlist.setPreferredSize(new Dimension(170, 0));
		friendlist.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					SimpleFriendPanel fp = (SimpleFriendPanel) friendlist.getSelectedValue();
					ChatRecordForm.this.friend = fp.getFriend();
					getData(1);
				}
			}
		});

		Collection<Friend> friends = MainForm.FORM.getFriendList().getAllFriend();
		for (Friend f : friends)
		{
			((DefaultListModel<JPanel>) friendlist.getModel()).addElement(new SimpleFriendPanel(f));
		}

		CustomScrollPane jsp = new CustomScrollPane(friendlist);
		this.addCustomComponent(jsp, BorderLayout.WEST);

		JPanel r_south = new JPanel();
		r_south.setOpaque(false);
		r_south.setPreferredSize(new Dimension(550, 35));
		r_south.setLayout(new FlowLayout());
		this.addCustomComponent(r_south, BorderLayout.SOUTH);

		jtp_content = new JTextPane();
		jtp_content.setEditable(false);
		CustomScrollPane jsp2 = new CustomScrollPane(jtp_content);
		jsp2.setBorder(new EmptyBorder(0, 10, 0, 0));
		this.addCustomComponent(jsp2);

		lbl_pageNo = new JLabel("第1页");
		btn_previous = new CommonButton("上一页");
		btn_previous.setPreferredSize(new Dimension(60, 25));
		btn_next = new CommonButton("下一页");
		btn_next.setPreferredSize(new Dimension(60, 25));
		btn_del = new CommonButton("删除记录");
		btn_del.setPreferredSize(new Dimension(60, 25));
		r_south.add(lbl_pageNo);
		r_south.add(btn_previous);
		r_south.add(btn_next);
		r_south.add(btn_del);

		// 添加事件
		Btn_Listener bl = new Btn_Listener();
		btn_previous.addActionListener(bl);
		btn_next.addActionListener(bl);
		btn_del.addActionListener(bl);
	}

	/**
	 * 追加消息显示
	 * @param message
	 */
	private void appendMsg(Message message)
	{
		boolean isMine = false;
		if (message.getSenderId() == ApplicationContext.User.getId())
		{
			isMine = true;
		}

		String msg = message.getContent();
		if (jtp_content.getText() == null || jtp_content.getText().isEmpty())
		{
			if (isMine)
			{
				msg = ApplicationContext.User.getUsername() + " " + TimeUtil.parseYYYYMMDDHHMMSS(message.getSendTime()) + "  : \n   " + msg + "\n";
			}
			else
			{
				msg = friend.getNickname() + " " + TimeUtil.parseYYYYMMDDHHMMSS(message.getSendTime()) + "  : \n   " + msg + "\n";
			}
		}
		else
		{
			if (isMine)
			{
				msg = " \n" + ApplicationContext.User.getUsername() + " " + TimeUtil.parseYYYYMMDDHHMMSS(message.getSendTime()) + "  : \n   " + msg
						+ "\n";
			}
			else
			{
				msg = "\n" + friend.getNickname() + " " + TimeUtil.parseYYYYMMDDHHMMSS(message.getSendTime()) + "  : \n   " + msg + "\n";
			}
		}

		int length = msg.length();
		for (int i = 0; i < length; i++)
		{
			if (msg.charAt(i) == '/')
			{
				String path = msg.substring(i + 1, i + 8);
				path = path.trim();
				ImageIcon icon = new ImageIcon(Global.Root + "resource/image/emotion/" + path);
				if (icon != null)
				{
					jtp_content.select(jtp_content.getDocument().getLength(), jtp_content.getDocument().getLength());
					jtp_content.insertIcon(icon);
					i = i + 7;
					continue;
				}
			}
			try
			{
				Document doc = jtp_content.getDocument();
				doc.insertString(doc.getLength(), "" + msg.charAt(i), null);
			}
			catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}
		jtp_content.select(jtp_content.getDocument().getLength(), jtp_content.getDocument().getLength());
	}

	public void getData(int page)
	{
		if (this.friend != null)
		{
			if (page <= 0)
			{
				return;
			}
			jtp_content.setText("");
			Request request = new Request(Actions.InquireChatRecordAction);
			request.setParameter("userId", this.friend.getFriendId());
			request.setParameter("pageNo", page);
			HttpUtil.request(request, new RequestHandler()
			{
				@Override
				public void callback(JSONObject data)
				{
					List<Message> msgs = JSON.parseArray(data.getString("msgs"), Message.class);
					if (msgs != null)
					{
						int length = msgs.size();
						if (length != 0)
						{
							lbl_pageNo.setText("第" + pageNo + "页");
							for (int i = 0; i < length; i++)
							{
								appendMsg(msgs.get(i));
							}
						}
						else
						{
							pageNo--;
						}
					}
				}
			});
		}
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_previous)
			{
				if (pageNo != 1)
				{
					pageNo--;
					getData(pageNo);
				}
			}
			else if (e.getSource() == btn_next)
			{
				pageNo++;
				getData(pageNo);
			}
			else if (e.getSource() == btn_del)
			{
				if (friend != null)
				{
					if (UIUtil.confirm(null, Global.Lan.get("确定要该好友的聊天记录吗?")))
					{
						Request request = new Request(Actions.DeleteChatRecordAction);
						request.setParameter("userId", friend.getFriendId());
						HttpUtil.request(request, new RequestHandler()
						{
							@Override
							public void callback(JSONObject data)
							{
								UIUtil.alert(null, Global.Lan.get("删除成功"));
								jtp_content.setText("");
							};
						});

					}
				}
			}
		}
	}

}
