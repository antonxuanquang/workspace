package com.sean.im.client.comp;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * 自动拉伸图片控件
 * @author sean
 */
public class ImageComp extends JPanel
{
	private static final long serialVersionUID = 1L;
	private ImageIcon img;
	private int iWidth, iHeight;

	public ImageComp(File file, int width, int height)
	{
		this(new ImageIcon(file.getAbsolutePath()), width, height);
	}

	public ImageComp(ImageIcon img, int width, int height)
	{
		this.img = img;
		this.iWidth = width;
		this.iHeight = height;
		this.setPreferredSize(new Dimension(width, height));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.drawImage(img.getImage(), 0, 0, iWidth, iHeight, null);
	}

}
