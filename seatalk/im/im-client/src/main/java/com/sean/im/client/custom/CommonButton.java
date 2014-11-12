package com.sean.im.client.custom;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingConstants;

import com.sean.im.client.constant.Global;
import com.sean.im.client.util.UIUtil;

/**
 * 自定义按钮
 * @author sean
 */
public class CommonButton extends CommonLabel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	private ActionListener listener;

	private static BufferedImage BG_UP, BG_DOWN, BG_OVER;
	private BufferedImage bg;
	static
	{
		try
		{
			BG_UP = ImageIO.read(new File(Global.Root + "resource/image/common/button_normal.png"));
			BG_OVER = ImageIO.read(new File(Global.Root + "resource/image/common/button_hover.png"));
			BG_DOWN = ImageIO.read(new File(Global.Root + "resource/image/common/button_down.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public CommonButton(String text)
	{
		super(text, SwingConstants.CENTER);
		this.addMouseListener(this);
		this.setCursor(Global.CURSOR_HAND);
		bg = BG_UP;
	}

	public void addActionListener(ActionListener listener)
	{
		this.listener = listener;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		UIUtil.drawBackground(bg, g, this, 10);
		super.paintComponent(g);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (this.listener != null)
		{
			listener.actionPerformed(new ActionEvent(this, 0, ""));
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		bg = BG_DOWN;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		bg = BG_UP;
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		bg = BG_OVER;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		bg = BG_UP;
		repaint();
	}
}
