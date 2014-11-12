package com.sean.im.client.custom;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.sean.im.client.constant.Global;

/**
 * 自定义滚动条
 * @author sean
 */
public class CustomScrollPane extends JScrollPane
{
	private static final long serialVersionUID = 1L;

	private static final ImageIcon bg = new ImageIcon(Global.Root + "resource/image/common/scrollbar.png");

	private int barWidth = 6;

	public CustomScrollPane(Component component)
	{
		super(component);
		this.setOpaque(false);
		this.getViewport().setOpaque(false);
		this.setBorder(null);
		this.setViewportBorder(null);

		this.getVerticalScrollBar().setUI(new MyScrollBarUI());
		this.getHorizontalScrollBar().setUI(new MyScrollBarUI());
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.getVerticalScrollBar().setPreferredSize(new Dimension(barWidth, 10));
		this.getHorizontalScrollBar().setPreferredSize(new Dimension(10, barWidth));

		this.getVerticalScrollBar().setUnitIncrement(50);
		this.getHorizontalScrollBar().setUnitIncrement(50);
	}
	
	public CustomScrollPane()
	{
		this.setOpaque(false);
		this.getViewport().setOpaque(false);
		this.setBorder(null);
		this.setViewportBorder(null);

		this.getVerticalScrollBar().setUI(new MyScrollBarUI());
		this.getHorizontalScrollBar().setUI(new MyScrollBarUI());
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.getVerticalScrollBar().setPreferredSize(new Dimension(barWidth, 10));
		this.getHorizontalScrollBar().setPreferredSize(new Dimension(10, barWidth));

		this.getVerticalScrollBar().setUnitIncrement(50);
		this.getHorizontalScrollBar().setUnitIncrement(50);
	}

	private class MyScrollBarUI extends BasicScrollBarUI
	{	
		@Override
		protected void installComponents()
		{
			switch (scrollbar.getOrientation())
			{
			case JScrollBar.VERTICAL:
				incrButton = createIncreaseButton(SOUTH);
				decrButton = createDecreaseButton(NORTH);
				break;

			case JScrollBar.HORIZONTAL:
				if (scrollbar.getComponentOrientation().isLeftToRight())
				{
					incrButton = createIncreaseButton(EAST);
					decrButton = createDecreaseButton(WEST);
				}
				else
				{
					incrButton = createIncreaseButton(WEST);
					decrButton = createDecreaseButton(EAST);
				}
				break;
			}
			scrollbar.setEnabled(scrollbar.isEnabled());
		}

		@Override
		protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
		{
			g.drawImage(bg.getImage(), thumbBounds.x, thumbBounds.y - 16, thumbBounds.width, thumbBounds.height + 32, null);
		}
	}
}
