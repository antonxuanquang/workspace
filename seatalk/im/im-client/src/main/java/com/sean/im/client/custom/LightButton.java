package com.sean.im.client.custom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.sean.im.client.constant.Global;
import com.sean.im.client.util.UIUtil;

/**
 * 自定义按钮
 * @author sean
 */
public class LightButton extends JLabel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	private static BufferedImage BG;
	private ActionListener listener;
	private BufferedImage bg;
	static
	{
		try
		{
			BufferedImage tmp = ImageIO.read(new File(Global.Root + "resource/image/common/shadow_mask_large.png"));
			BG = tmp.getSubimage(32, 0, 32, 32);
			tmp = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public LightButton(String iconUrl)
	{
		super(iconUrl, JLabel.CENTER);
		this.setPreferredSize(new Dimension(28, 28));
		this.addMouseListener(this);
		this.setCursor(Global.CURSOR_HAND);
		this.setFocusable(true);
	}

	public LightButton(ImageIcon icon)
	{
		super(icon);
		this.setPreferredSize(new Dimension(28, 28));
		this.addMouseListener(this);
		this.setCursor(Global.CURSOR_HAND);
		this.setFocusable(true);
	}
	
	public LightButton(ImageIcon icon, String text)
	{
		super(icon);
		this.setText(text);
		this.setFont(Global.FONT);
		this.setForeground(Color.WHITE);
		this.setPreferredSize(new Dimension(28, 28));
		this.addMouseListener(this);
		this.setCursor(Global.CURSOR_HAND);
		this.setFocusable(true);
	}

	public void addActionListener(ActionListener listener)
	{
		this.listener = listener;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (bg != null)
		{
			UIUtil.drawBackground(bg, g, this, 5);
		}
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
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		bg = BG;
		this.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		bg = null;
		this.repaint();
	}
}
