package com.sean.im.client.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.sean.im.client.constant.Global;

/**
 * 滑动选项卡
 * @author sean
 */
public class SlideTabComp extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JPanel north, center, panels;
	private List<Tab> tabs;
	private ImageIcon tabBgUp;
	private int tabWidth, tabHeight, currIndex = 0;
	private Timer timer;
	private Rectangle rect;

	public SlideTabComp(int tabWidth, int tabHeight, int size)
	{
		this.tabWidth = tabWidth;
		this.tabHeight = tabHeight - 60;
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		this.north = new JPanel();
		north.setOpaque(false);
		this.north.setLayout(new GridLayout(1, 2, 0, 0));
		this.center = new JPanel();
		center.setOpaque(false);
		this.center.setLayout(null);
		this.add(north, BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.tabs = new LinkedList<Tab>();

		this.panels = new JPanel();
		panels.setOpaque(false);
		panels.setLayout(null);
		panels.setBounds(0, 0, this.tabWidth * size, this.tabHeight);
		center.add(panels);

		tabBgUp = new ImageIcon(Global.Root + "resource/image/main_tab_up.png");
	}

	public void addTabPanel(JPanel panel, ImageIcon norIcon, ImageIcon pressIcon)
	{
		Tab tab = new Tab();
		tab.offset = tabs.size() * tabWidth;
		SwitchButton btn = new SwitchButton(norIcon, pressIcon);
		tab.btn = btn;
		tabs.add(tab);
		north.add(tab.btn);

		panel.setBounds(tab.offset, 0, tabWidth, tabHeight);
		panels.add(panel);
	}

	public void addTabPanel(JPanel panel, String title)
	{
		Tab tab = new Tab();
		tab.offset = tabs.size() * tabWidth;
		SwitchButton btn = new SwitchButton(title);
		tab.btn = btn;
		tabs.add(tab);
		north.add(tab.btn);

		panel.setBounds(tab.offset, 0, tabWidth, tabHeight);
		panels.add(panel);
	}

	private void moveToPane(final int index)
	{
		rect = panels.getBounds();
		final int dis = tabWidth / 5;

		if (index > currIndex)
		{
			final int offset = index * -1 * tabWidth;
			timer = new Timer(20, new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					rect.x -= dis;
					panels.setBounds(rect);
					if (rect.x == offset)
					{
						timer.stop();
						currIndex = index;
					}
				}
			});	
			timer.start();
		}
		else if (index < currIndex)
		{
			final int offset = index * -1 * tabWidth;
			timer = new Timer(20, new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					rect.x += dis;
					panels.setBounds(rect);
					if (rect.x == offset)
					{
						timer.stop();
						currIndex = index;
					}
				}
			});	
			timer.start();
		}
	}

	public void setSelectedTab(int index)
	{
		int offset = index * tabWidth;
		for (Tab tab : tabs)
		{
			if (tab.offset != offset)
			{
				tab.btn.setHightLight(false);
			}
			else
			{
				tab.btn.setHightLight(true);
				moveToPane(index);
			}
		}
	}

	private void otherTabClick(SwitchButton btn)
	{
		for (Tab tab : tabs)
		{
			if (tab.btn != btn)
			{
				tab.btn.setHightLight(false);
			}
			else
			{
				tab.btn.setHightLight(true);
				moveToPane(tab.offset / tabWidth);
			}
		}
	}

	public class SwitchButton extends JPanel implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		private JLabel jlText;
		private ImageIcon up, down;
		private boolean isLight = false;

		public SwitchButton(ImageIcon up, ImageIcon down)
		{
			this.setOpaque(false);
			this.up = up;
			this.down = down;
			this.setLayout(new BorderLayout());
			jlText = new JLabel(up);
			jlText.setCursor(Global.CURSOR_HAND);
			this.add(jlText, BorderLayout.CENTER);
			this.setBackground(Global.DarkBlue);
			this.setPreferredSize(new Dimension(100, 30));
			jlText.addMouseListener(this);
		}

		public SwitchButton(String title)
		{
			this.setOpaque(false);
			this.setLayout(new BorderLayout());
			jlText = new JLabel(title, SwingConstants.CENTER);
			jlText.setFont(new Font("", Font.BOLD, 12));
			jlText.setForeground(Color.WHITE);
			jlText.setCursor(Global.CURSOR_HAND);
			this.add(jlText, BorderLayout.CENTER);
			this.setBackground(Global.DarkBlue);
			this.setPreferredSize(new Dimension(100, 30));
			jlText.addMouseListener(this);
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			if (isLight)
			{
				g.drawImage(tabBgUp.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
			}
		}

		public void setHightLight(boolean isLight)
		{
			this.isLight = isLight;
			jlText.setIcon(isLight ? down : up);
			this.repaint();
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (e.getSource() == jlText)
			{
				otherTabClick(this);
			}
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
		}
	}

	public class Tab
	{
		public SwitchButton btn;
		public int offset;
		public ImageIcon iconUp;
		public ImageIcon iconDown;
	}
}
