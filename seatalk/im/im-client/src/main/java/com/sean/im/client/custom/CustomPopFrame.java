package com.sean.im.client.custom;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;

import com.sean.im.client.constant.Global;

/**
 * 自定义窗体
 * @author sean
 */
public class CustomPopFrame extends JWindow
{
	private static final long serialVersionUID = 1L;

	private JPanel main, center;
	private RoundRectangle2D.Float rrf;

	public CustomPopFrame(int width, int height)
	{
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		rrf = new RoundRectangle2D.Float(0, 0, width, height, 5, 5);
		this.setShape(rrf);
		this.setLayout(new BorderLayout());

		main = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void paintBorder(Graphics g)
			{
				g.setColor(Global.DarkGreen);
				g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);
//				g.drawImage(CustomFrame.WIN_BG.getImage(), 2, 2, this.getWidth() - 4, this.getHeight() - 4, this);
			}
		};
		this.add(main, BorderLayout.CENTER);
		main.setOpaque(false);
		main.setLayout(new BorderLayout());
		main.setBorder(new EmptyBorder(1, 1, 1, 1));

		center = new JPanel();
		center.setFocusable(true);
		center.setOpaque(false);
		center.setBorder(new EmptyBorder(1, 1, 1, 1));
		center.setLayout(new BorderLayout());

		main.add(center, BorderLayout.CENTER);
	}
	
	public JPanel getMainPanel()
	{
		return center;
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

}
