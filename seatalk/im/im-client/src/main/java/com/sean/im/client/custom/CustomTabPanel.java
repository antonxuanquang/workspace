package com.sean.im.client.custom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sean.im.client.constant.Global;

/**
 * 自定义选项卡
 * @author sean
 */
public class CustomTabPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JPanel north, center;
	private List<Tab> tabs;
	private boolean closable = true;
	private int tabWidth;
	private TabListener listener;

	public CustomTabPanel(boolean closable, int tabWidth)
	{
		this.closable = closable;
		this.tabWidth = tabWidth;
		this.setLayout(new BorderLayout());
		this.north = new JPanel();
		this.north.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.center = new JPanel();
		this.center.setLayout(new BorderLayout());
		this.add(north, BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.tabs = new LinkedList<Tab>();
	}

	public CustomTabPanel(boolean closable)
	{
		this(closable, 100);
	}

	public void addTabListener(TabListener listener)
	{
		this.listener = listener;
	}

	public int getTabCount()
	{
		return this.tabs.size();
	}

	public void addTabPanel(JPanel panel, String title)
	{
		for(Tab tab : tabs)
		{
			if (tab.panel == panel)
			{
				return;
			}
		}
		Tab tab = new Tab();
		tab.panel = panel;
		SwitchButton btn = new SwitchButton(title, closable);
		tab.btn = btn;
		tabs.add(tab);

		north.add(tab.btn);
		this.setSelectedTab(tab.panel);

		if (tabs.size() == 1)
		{
			if (this.listener != null)
			{
				listener.firstTabAdded(tab);
			}
		}
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

	private void otherTabClose(SwitchButton btn)
	{
		center.removeAll();
		for (Tab tab : tabs)
		{
			if (tab.btn == btn)
			{
				this.tabs.remove(tab);
				north.remove(tab.btn);
				if (tabs.size() > 0)
				{
					this.setSelectedTab(tabs.get(0).panel);
				}
				north.repaint();
				break;
			}
		}
		if (tabs.size() == 0)
		{
			if (listener != null)
			{
				listener.allTabClosed();
			}
		}
	}

	public class SwitchButton extends JPanel implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		private JLabel jlText;
		private JLabel jlClose;

		public SwitchButton(String text, boolean closable)
		{
			this.setBorder(new EmptyBorder(2, 15, 2, 10));
			this.setLayout(new BorderLayout());
			jlText = new JLabel(text);
			jlText.setPreferredSize(new Dimension(tabWidth, 20));
			jlText.setCursor(Global.CURSOR_HAND);
			this.add(jlText, BorderLayout.CENTER);
			if (closable)
			{
				jlClose = new JLabel("X");
				jlClose.setCursor(Global.CURSOR_HAND);
				this.add(jlClose, BorderLayout.EAST);
				jlClose.addMouseListener(this);
			}
			this.setBackground(Global.DarkBlue);
			jlText.addMouseListener(this);
		}

		public void setHightLight(boolean isLight)
		{
			this.setBackground(isLight ? Global.LightBlue : Global.DarkBlue);
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (e.getSource() == jlText)
			{
				otherTabClick(this);
			}
			else if (e.getSource() == jlClose)
			{
				otherTabClose(this);
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

	public interface TabListener
	{
		public void allTabClosed();

		public void firstTabAdded(Tab tab);
	}

	public class Tab
	{
		public SwitchButton btn;
		public JPanel panel;
	}

	public static void main(String[] args)
	{
		JFrame fram = new JFrame();
		fram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fram.setSize(600, 500);

		CustomTabPanel tab = new CustomTabPanel(true);
		fram.add(tab, BorderLayout.CENTER);

		JPanel tab1 = new JPanel();
		tab1.setBackground(Color.BLACK);
		tab1.add(new JLabel("111"));

		JPanel tab2 = new JPanel();
		tab2.setBackground(Color.RED);

		JPanel tab3 = new JPanel();
		tab3.setBackground(Color.BLUE);
		tab3.add(new JLabel("333"));

		tab.addTabPanel(tab1, "tab1");
		tab.addTabPanel(tab2, "tab2");
		tab.addTabPanel(tab3, "tab3");

		fram.setVisible(true);
	}
}
