package com.sean.im.client.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CustomList;
import com.sean.im.client.custom.RoundPopMenu;
import com.sean.im.client.form.MainForm;
import com.sean.im.client.listitem.FriendItem;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.commom.entity.Friend;

/**
 * 搜索好友控件
 * @author sean
 */
public class SearchFriendComp extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JTextField jt_key;
	private SearchResult searchResult = new SearchResult(267, 200);

	public SearchFriendComp()
	{
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		JLabel icon = new JLabel(new ImageIcon(Global.Root + "resource/image/main/search.png"));
		icon.setBorder(new EmptyBorder(0, 0, 0, 5));
		this.add(icon, BorderLayout.EAST);
		jt_key = new JTextField();
		jt_key.setBorder(new EmptyBorder(0, 5, 0, 0));
		this.add(jt_key, BorderLayout.CENTER);
		jt_key.setVisible(false);

		jt_key.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				int code = e.getKeyCode();

				if (jt_key.getText().isEmpty())
				{
					jt_key.setVisible(false);
				}

				// 向上
				if (code == 38)
				{
					searchResult.setSelectIndex(searchResult.getSelectIndex() - 1);
				}
				// 向下
				else if (code == 40)
				{
					searchResult.setSelectIndex(searchResult.getSelectIndex() + 1);
				}
				// 回车
				else if (code == 10)
				{
					FriendItem fp = searchResult.getSelectValue();
					if (fp != null)
					{
						ChatFormCache.getChatForm(fp.getFriend()).open();
					}
				}
				else
				{
					String key = jt_key.getText().toLowerCase().trim();
					if (key.isEmpty())
					{
						searchResult.clear();
						searchResult.setVisible(false);
						return;
					}

					List<Friend> friends = new LinkedList<Friend>();
					for (Friend f : MainForm.FORM.getFriendList().getAllFriend())
					{
						if (f.getUsername().toLowerCase().startsWith(key) || f.getNickname().toLowerCase().startsWith(key))
						{
							friends.add(f);
						}
					}
					if (friends.isEmpty())
					{
						searchResult.clear();
						searchResult.setVisible(false);
						return;
					}

					searchResult.clear();
					searchResult.addItems(friends);

					searchResult.show(jt_key, 0, 25);
					searchResult.setSelectIndex(0);
					jt_key.requestFocus();

				}
			}
		});

		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				jt_key.setVisible(true);
				SearchFriendComp.this.setVisible(false);
				SearchFriendComp.this.setVisible(true);
				jt_key.setFocusable(true);
				jt_key.requestFocus();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.drawImage(new ImageIcon(Global.Root + "resource/image/white_bg.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}

	/**
	 * 搜索结果
	 * @author sean
	 */
	private class SearchResult extends RoundPopMenu
	{
		private static final long serialVersionUID = 1L;
		private CustomList list;

		public SearchResult(int width, int height)
		{
			super(true);
			this.setPreferredSize(new Dimension(width, height));
			this.setBorder(new EmptyBorder(7, 7, 7, 7));
			list = new CustomList();
			this.add(list);

			list.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount() == 2)
					{
						FriendItem fp = (FriendItem) list.getSelectedValue();
						ChatFormCache.getChatForm(fp.getFriend()).open();
					}
				}
			});
		}

		public void addItems(List<Friend> friends)
		{
			for (Friend f : friends)
			{
				FriendItem fp = new FriendItem(f, true);
				((DefaultListModel<JPanel>) list.getModel()).addElement(fp);
			}
		}

		public void clear()
		{
			((DefaultListModel<JPanel>) list.getModel()).clear();
			list.repaint();
		}

		public void setSelectIndex(int index)
		{
			list.setSelectedIndex(index);
		}

		public int getSelectIndex()
		{
			return list.getSelectedIndex();
		}

		public FriendItem getSelectValue()
		{
			return (FriendItem) list.getSelectedValue();
		}
	}

}
