package com.sean.im.client.comp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.sean.im.client.constant.Global;
import com.sean.im.client.util.ImageUtil;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.StatusEnum;

/**
 * 头像面板
 * @author sean
 */
public class HeadComp extends JPanel
{
	private static final long serialVersionUID = 1L;
	private File file;
	private BufferedImage image, bg;
	private ImageIcon statusImg;
	private static BufferedImage BG, BG_DOWN;
	static
	{
		try
		{
			BufferedImage tmp = ImageIO.read(new File(Global.Root + "resource/image/common/shadow_mask_large.png"));
			BG = tmp.getSubimage(0, 0, 32, 32);
			BG_DOWN = tmp.getSubimage(32, 0, 32, 32);
			tmp = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public HeadComp(int head, int width, int height)
	{
		this(head, width, height, StatusEnum.OffLine);
	}

	public HeadComp(int head, int width, int height, int status)
	{
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(width, height));
		this.setLayout(null);
		this.setBackground(Color.WHITE);

		file = new File(Global.Root + "resource/image/head/" + head + ".jpg");
		image = ImageUtil.getImage(file);

		bg = BG;
		this.setStatus(status);
	}

	public HeadComp(String head, int width, int height)
	{
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(width, height));
		this.setLayout(null);
		this.setBackground(Color.WHITE);

		file = new File(head);
		image = ImageUtil.getImage(file);

		bg = BG;
	}

	public void setHeightLight()
	{
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				bg = BG_DOWN;
				HeadComp.this.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				bg = BG;
				HeadComp.this.repaint();
			}
		});
	}

	public void setGray(boolean isGray)
	{
		image = null;
		if (isGray)
		{
			image = ImageUtil.grayImage(file);
		}
		else
		{
			image = ImageUtil.getImage(file);
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 3, 3, getSize().width - 6, getSize().height - 6, this);
		UIUtil.drawBackground(bg, g, this, 5);

		if (statusImg != null)
		{
			g.drawImage(statusImg.getImage(), this.getWidth() - 13, this.getHeight() - 13, 11, 11, null);
		}
		g.dispose();
	}

	/**
	 * 设置头像
	 * @param head
	 */
	public void setHead(int head)
	{
		file = null;
		file = new File(Global.Root + "resource/image/head/" + head + ".jpg");
		image = null;
		image = ImageUtil.getImage(file);
		this.repaint();
	}

	/**
	 * 设置状态
	 * @param status
	 */
	public void setStatus(int status)
	{
		switch (status)
		{
		case StatusEnum.Online:
			statusImg = null;
			break;
		case StatusEnum.Hide:
			statusImg = null;
			break;
		case StatusEnum.Leave:
			statusImg = StatusComp.getStatusImg(StatusEnum.Leave);
			break;
		case StatusEnum.OffLine:
			statusImg = null;
			break;
		default:
			break;
		}
	}
}
