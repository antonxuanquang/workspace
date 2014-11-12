package com.sean.im.client.form.flock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.custom.CustomField;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.custom.CustomList;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.form.flock.FlockInfoForm.FlockMemberPanel;
import com.sean.im.client.listitem.FlockInfoMemberItem;
import com.sean.im.client.listitem.SimpleFriendPanel;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;
import com.sean.im.commom.entity.Friend;

/**
 * 添加群成员
 * @author sean
 */
public class AddMemberForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;
	private CustomList src, target;
	private CommonButton btn_save, btn_cancel;
	private CustomField filter;
	private Set<Long> selected = new HashSet<Long>();
	private long flockId;
	private FlockMemberPanel memberPanel;

	public AddMemberForm(long flockId, FlockMemberPanel memberPanel)
	{
		super(550, 500);
		this.flockId = flockId;
		this.memberPanel = memberPanel;
		this.setCustomTitle(Global.Lan.get("添加群成员"), null);
		this.setLocationRelativeTo(null);
		this.setCustomMaxiable(false);
		this.setCustomLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel bg = new JPanel();
		bg.setBackground(Global.LightBlue);
		bg.setLayout(new BorderLayout());
		this.addCustomComponent(bg, BorderLayout.CENTER);

		JPanel west = new JPanel();
		west.setBackground(Color.WHITE);
		west.setLayout(new BorderLayout());

		filter = new CustomField();
		filter.setPreferredSize(new Dimension(200, 30));
		west.add(filter, BorderLayout.NORTH);
		src = new CustomList();
		west.add(src, BorderLayout.CENTER);
		bg.add(west, BorderLayout.WEST);

		target = new CustomList();
		target.setOpaque(true);
		target.setPreferredSize(new Dimension(200, 400));
		target.setBackground(Color.WHITE);
		bg.add(target, BorderLayout.EAST);

		CommonLabel cl = new CommonLabel("=>", SwingConstants.CENTER);
		cl.setFont(new Font("", Font.BOLD, 20));
		cl.setForeground(Color.GRAY);
		bg.add(cl, BorderLayout.CENTER);

		JPanel south = new JPanel();
		south.setOpaque(false);
		south.setLayout(new FlowLayout(FlowLayout.RIGHT));
		south.setPreferredSize(new Dimension(10, 30));
		this.addCustomComponent(south, BorderLayout.SOUTH);
		btn_save = new CommonButton(Global.Lan.get("保存"));
		btn_save.setPreferredSize(new Dimension(65, 25));
		south.add(btn_save);
		btn_cancel = new CommonButton(Global.Lan.get("取消"));
		btn_cancel.setPreferredSize(new Dimension(65, 25));
		south.add(btn_cancel);

		Collection<Friend> fs = MainForm.FORM.getFriendList().getAllFriend();
		for (Friend f : fs)
		{
			this.addMember(f);
		}

		// 添加事件
		Btn_Listener bl = new Btn_Listener();
		btn_cancel.addActionListener(bl);
		btn_save.addActionListener(bl);

		// 过滤事件
		filter.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				String key = filter.getText().toLowerCase();
				src.removeAllElement();
				if (key.isEmpty())
				{
					Collection<Friend> fs = MainForm.FORM.getFriendList().getAllFriend();
					for (Friend f : fs)
					{
						if (!selected.contains(f.getFriendId()))
						{
							addMember(f);
						}
					}
				}
				else
				{
					Collection<Friend> fs = MainForm.FORM.getFriendList().getAllFriend();
					for (Friend f : fs)
					{
						if (!selected.contains(f.getFriendId()))
						{
							if (f.getUsername().toLowerCase().startsWith(key) || f.getNickname().toLowerCase().startsWith(key))
							{
								addMember(f);
							}
						}
					}
				}
			}
		});

		// 列表双击事件
		src.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					SimpleFriendPanel sfp = (SimpleFriendPanel) src.getSelectedValue();
					Friend f = sfp.getFriend();
					src.removeElement(sfp);
					target.addElement(sfp);
					selected.add(f.getFriendId());
				}
			}
		});
		target.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					SimpleFriendPanel sfp = (SimpleFriendPanel) target.getSelectedValue();
					Friend f = sfp.getFriend();
					target.removeElement(sfp);
					src.addElement(sfp);
					selected.remove(f.getFriendId());
				}
			}
		});
	}

	private void addMember(Friend friend)
	{
		CustomList memberList = memberPanel.getMemberList();
		int size = memberList.listSize();
		boolean isMember = false;
		for (int i = 0; i < size; i++)
		{
			if (((FlockInfoMemberItem) memberList.getElementAt(i)).getMember().getUserId() == friend.getFriendId())
			{
				isMember = true;
				break;
			}
		}
		if (!isMember)
		{
			src.addElement(new SimpleFriendPanel(friend));
		}
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_cancel)
			{
				AddMemberForm.this.setVisible(false);
			}
			else if (e.getSource() == btn_save)
			{
				if (!selected.isEmpty())
				{
					Request request = new Request(Actions.AddFlockMemberAction);
					request.setParameter("flockId", flockId);
					long[] ids = new long[selected.size()];
					int i = 0;
					for (long id : selected)
					{
						ids[i] = id;
						i++;
					}
					request.setParameter("flockMemberIdList", ids);
					HttpUtil.request(request, new RequestHandler()
					{
						@Override
						public void callback(JSONObject data)
						{
							UIUtil.alert(MainForm.FORM, Global.Lan.get("请求已经发出等待好友确认..."));
						}
					});
				}
				AddMemberForm.this.setVisible(false);
			}
		}
	}
}
