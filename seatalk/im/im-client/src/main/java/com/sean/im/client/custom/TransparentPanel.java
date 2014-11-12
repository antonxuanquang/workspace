package com.sean.im.client.custom;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.sean.im.client.constant.Global;

/**
 * 半透明panel
 * @author sean
 */
public class TransparentPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public TransparentPanel()
	{
		this.setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.drawImage(new ImageIcon(Global.Root + "resource/image/white_bg_90.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
