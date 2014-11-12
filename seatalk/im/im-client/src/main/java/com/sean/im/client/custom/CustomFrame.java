package com.sean.im.client.custom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sean.im.client.constant.Config;
import com.sean.im.client.constant.Global;

/**
 * 自定义窗体
 * @author sean
 */
public class CustomFrame extends JFrame implements MouseListener, MouseMotionListener, CustomButton.CustomButtonListener
{
	private static final long serialVersionUID = 1L;

	private Point loc = new Point();
	private JPanel main, north, center, north_left, north_right;
	private RoundRectangle2D.Float rrf;
	private CustomButton btnMax, btnMin, btnClose;
	private JLabel title, icon;
	private boolean maxiable = true;
	private boolean gc = true;

	public static ImageIcon WIN_BG = new ImageIcon(Global.Root + "resource/skin/ubuntu/ubuntu.jpg");

	public CustomFrame(int width, int height)
	{
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		rrf = new RoundRectangle2D.Float(0, 0, width, height, 5, 5);
		this.setShape(rrf);
		this.setLayout(new BorderLayout());
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		if (Config.UserSetting != null)
		{
			this.setOpacity(Config.UserSetting.opacity);
		}

		main = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g)
			{
				g.setColor(Global.BorderColor2);
				g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 5, 5);
				g.drawImage(WIN_BG.getImage(), 2, 2, this.getWidth() - 4, this.getHeight() - 4, this);
			}

			@Override
			public void paintComponents(Graphics g)
			{
				super.paintComponents(g);
				g.dispose();
			}

			@Override
			public void paintBorder(Graphics g)
			{
				g.setColor(Global.BorderColor);
				g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);
			}
		};
		this.add(main, BorderLayout.CENTER);
		main.setLayout(new BorderLayout());
		main.setBorder(new EmptyBorder(0, 1, 1, 1));
		main.setOpaque(false);

		north = new JPanel();
		north.setPreferredSize(new Dimension(width, 25));
		north.setLayout(new BorderLayout());
		north.setOpaque(false);

		center = new JPanel();
		center.setLayout(null);
		center.setOpaque(false);

		main.add(north, BorderLayout.NORTH);
		main.add(center, BorderLayout.CENTER);

		north_left = new JPanel();
		north_left.setOpaque(false);
		north_left.setBorder(new EmptyBorder(3, 3, 0, 0));
		north_left.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		north.add(north_left, BorderLayout.CENTER);

		north_right = new JPanel();
		north_right.setOpaque(false);
		north_right.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		north.add(north_right, BorderLayout.EAST);

		this.setIconImage(Global.ICON.getImage());
		icon = new JLabel(Global.ICON);
		north_left.add(icon);

		title = new JLabel("frame");
		north_left.add(title);

		btnClose = new CustomButton(Global.Root + "resource/image/common/btn_close_normal.png", Global.Root
				+ "resource/image/common/btn_close_highlight.png", Global.Root + "resource/image/common/btn_close_down.png");
		btnClose.addClickListener(this);

		btnMax = new CustomButton(Global.Root + "resource/image/common/btn_max_normal.png", Global.Root
				+ "resource/image/common/btn_max_highlight.png", Global.Root + "resource/image/common/btn_max_down.png");
		btnMax.addClickListener(this);

		btnMin = new CustomButton(Global.Root + "resource/image/common/btn_mini_normal.png", Global.Root
				+ "resource/image/common/btn_mini_highlight.png", Global.Root + "resource/image/common/btn_mini_down.png");
		btnMin.addClickListener(this);

		north_right.add(btnMin);
		north_right.add(btnMax);
		north_right.add(btnClose);
	}

	public void setClosedGC(boolean gc)
	{
		this.gc = gc;
	}

	@Override
	public void dispose()
	{
		super.dispose();
		if (gc)
		{
			System.gc();
		}
	}

	/**
	 * 设置背景图
	 * @param img
	 */
	public void setBackgroundImage(ImageIcon img)
	{
		WIN_BG = img;
		main.repaint();
	}

	/**
	 * 设置窗体大小
	 * @param width
	 * @param height
	 */
	public void setCustomSize(int width, int height)
	{
		// this.width = width;
		//
		// btnClose.setLocation(width - 39, -1);
		// btnMax.setLocation(width - 67, -1);
		// btnMin.setLocation(width - 95, -1);
		//
		// this.setSize(width, height);
		this.maxSize();
	}

	/**
	 * 设置布局
	 * @param manager
	 */
	public void setCustomLayout(LayoutManager manager)
	{
		center.setLayout(manager);
	}

	/**
	 * 设置是否可以最大化
	 * @param maxiable
	 */
	public void setCustomMaxiable(boolean maxiable)
	{
		this.maxiable = maxiable;
		if (!maxiable)
		{
			north_right.remove(btnMax);
		}
	}

	/**
	 * 添加控件
	 * @param comp
	 * @param constraints
	 */
	public void addCustomComponent(Component comp, Object constraints)
	{
		this.center.add(comp, constraints);
	}

	/**
	 * 添加控件
	 * @param comp
	 * @return
	 */
	public Component addCustomComponent(Component comp)
	{
		return this.center.add(comp);
	}

	/**
	 * 设置标题
	 * @param text
	 * @param font
	 */
	public void setCustomTitle(String text, Font font)
	{
		this.setTitle(text);
		title.setText(text);
		title.setForeground(Color.WHITE);
		if (font != null)
		{
			title.setFont(font);
		}
		else
		{
			title.setFont(new Font("", Font.BOLD, 12));
		}
	}

	/**
	 * 最大化
	 */
	public void maxSize()
	{
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setShape(null);
		this.removeMouseMotionListener(this);
	}

	/**
	 * 正常大小
	 */
	private void normalSize()
	{
		this.setExtendedState(JFrame.NORMAL);
		this.setShape(rrf);
		this.addMouseMotionListener(this);
	}

	/**
	 * 最小化
	 */
	private void minSize()
	{
		this.setExtendedState(JFrame.ICONIFIED);
	}

	/**
	 * 抖动
	 */
	public synchronized void shake()
	{
		this.setVisible(true);
		this.setExtendedState(JFrame.NORMAL);
		Point p = this.getLocationOnScreen();
		for (int i = 0; i < 20; i++)
		{
			if (i % 2 == 0)
			{
				p.x += 3;
				this.setLocation(p);
			}
			else
			{
				p.x -= 3;
				this.setLocation(p);
			}
			try
			{
				Thread.sleep(30);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		this.setLocation(this.getLocation().x + e.getX() - loc.x, this.getLocation().y + e.getY() - loc.y);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() == 2)
		{
			if (maxiable)
			{
				if (this.getExtendedState() == JFrame.NORMAL)
				{
					this.maxSize();
				}
				else if (this.getExtendedState() == JFrame.MAXIMIZED_BOTH)
				{
					this.normalSize();
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		loc.setLocation(e.getX(), e.getY());
		this.setCursor(Global.CURSOR_MOVE);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		this.setCursor(Global.CURSOR_DEF);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void click(MouseEvent e)
	{
		if (e.getSource() == btnMax)
		{
			if (maxiable)
			{
				if (this.getExtendedState() == JFrame.MAXIMIZED_BOTH)
				{
					this.normalSize();
				}
				else
				{
					this.maxSize();
				}
			}
		}
		else if (e.getSource() == btnMin)
		{
			this.minSize();
		}
		else if (e.getSource() == btnClose)
		{
			this.setVisible(false);
			for (WindowListener wl : this.getWindowListeners())
			{
				wl.windowClosing(null);
			}
			if (this.getDefaultCloseOperation() == JFrame.EXIT_ON_CLOSE)
			{
				System.exit(0);
			}
			else if (this.getDefaultCloseOperation() == JFrame.DISPOSE_ON_CLOSE)
			{
				this.dispose();
			}
		}
	}
}
