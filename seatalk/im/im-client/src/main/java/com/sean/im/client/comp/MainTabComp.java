package com.sean.im.client.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.sean.im.client.constant.Global;

/**
 * 固定选项卡
 * @author sean
 */
public class MainTabComp extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JPanel north, center;
	private List<Tab> tabs;
	private ImageIcon friendTabIcon_down, friendTabIcon_up;
	private ImageIcon recentTabIcon_down, recentTabIcon_up;
	private ImageIcon flockTabIcon_down, flockTabIcon_up;
	private ImageIcon tabBgUp;

	public MainTabComp()
	{
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		this.north = new JPanel();
		north.setOpaque(false);
		this.north.setLayout(new GridLayout(1, 2, 0, 0));
		this.center = new JPanel();
		center.setOpaque(false);
		this.center.setLayout(new BorderLayout());
		this.add(north, BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.tabs = new LinkedList<Tab>();

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

			tabBgUp = new ImageIcon(Global.Root + "resource/image/main_tab_up.png");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addTabPanel(JPanel panel, int index)
	{
		if (index == 1)
		{
			Tab tab = new Tab();
			tab.panel = panel;
			SwitchButton btn = new SwitchButton(friendTabIcon_up, friendTabIcon_down);
			tab.btn = btn;
			tabs.add(tab);
			north.add(tab.btn);
			this.setSelectedTab(tab.panel);
		}
		else if (index == 2)
		{
			Tab tab = new Tab();
			tab.panel = panel;
			SwitchButton btn = new SwitchButton(recentTabIcon_up, recentTabIcon_down);
			tab.btn = btn;
			tabs.add(tab);
			north.add(tab.btn);
		}
		else if (index == 3)
		{
			Tab tab = new Tab();
			tab.panel = panel;
			SwitchButton btn = new SwitchButton(flockTabIcon_up, flockTabIcon_down);
			tab.btn = btn;
			tabs.add(tab);
			north.add(tab.btn);
		}
	}
	
	public void addTabPanel(JPanel panel, String title)
	{
		Tab tab = new Tab();
		tab.panel = panel;
		SwitchButton btn = new SwitchButton(title);
		tab.btn = btn;
		tabs.add(tab);
		north.add(tab.btn);
	}

	public void setSelectedTab(JPanel panel)
	{
		center.removeAll();
		for (Tab tab : tabs)
		{
			if (tab.panel != panel)
			{
				tab.btn.setHightLight(false);
			}
			else
			{
				tab.btn.setHightLight(true);
				center.add(tab.panel, BorderLayout.CENTER);
				center.setVisible(false);
				center.setVisible(true);
			}
		}
	}

	private void otherTabClick(SwitchButton btn)
	{
		center.removeAll();
		for (Tab tab : tabs)
		{
			if (tab.btn != btn)
			{
				tab.btn.setHightLight(false);
			}
			else
			{
				tab.btn.setHightLight(true);
				center.add(tab.panel, BorderLayout.CENTER);
				center.setVisible(false);
				center.setVisible(true);
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
		public JPanel panel;
		public ImageIcon iconUp;
		public ImageIcon iconDown;
	}
}
