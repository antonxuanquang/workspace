package com.sean.im.client.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import com.sean.im.client.constant.Global;
import com.sean.im.client.util.UIUtil;

/**
 * 圆角菜单
 * @author sean
 */
public class RoundPopMenu extends JPopupMenu
{
	private static final long serialVersionUID = 1L;
	private static BufferedImage bg;
	private boolean fade;

	public RoundPopMenu(boolean fade)
	{
		this(null, fade);
	}

	public RoundPopMenu(String text, boolean fade)
	{
		super(text);
		this.fade = fade;
		this.setBackground(Color.WHITE);
		try
		{
			bg = ImageIO.read(new File(Global.Root + "resource/image/common/win_bk_alpha.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (fade)
		{
			this.setOpaque(false);
			this.setBorder(new EmptyBorder(10, 10, 10, 10));
		}
		else
		{
			this.setBorder(new EmptyBorder(5, 5, 5, 5));
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		if (fade)
		{
			UIUtil.drawBackground(bg, g, this, 10);
		}
		else
		{
			super.paintComponent(g);
			g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		}
	}
}
