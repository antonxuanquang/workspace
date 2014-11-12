package com.sean.im.client.comp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.sean.im.client.constant.Global;

/**
 * 头像面板
 * @author sean
 */
public class RoundHeadComp extends JPanel
{
	private static final long serialVersionUID = 1L;
	private int head, width, height, radis, rw1, rw2;
	private ImageIcon img_head;

	public RoundHeadComp(int head, int width, int height, int radis, int rw1, int rw2)
	{
		this.head = head;
		this.width = width;
		this.height = height;
		this.radis = radis;
		this.rw1 = rw1;
		this.rw2 = rw2;
		this.setLayout(null);
		this.setBackground(Color.WHITE);

		img_head = new ImageIcon(Global.Root + "resource/image/head/" + RoundHeadComp.this.head + ".jpg");
		img_head.setImage(img_head.getImage().getScaledInstance(width - 4, height - 4, Image.SCALE_DEFAULT));
	}

	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.GRAY);
		g2.fillRoundRect(0, 0, width, height, radis, radis);
		g2.setColor(Color.WHITE);
		g2.fillRoundRect(rw1, rw1, width - rw1 * 2, height - rw1 * 2, radis, radis);

		RoundRectangle2D rect = new RoundRectangle2D.Double(rw1 + rw2, rw1 + rw2, width - (rw1 + rw2) * 2, height - (rw1 + rw2) * 2, radis, radis);
		GeneralPath path = new GeneralPath();
		path.append(rect, false);
		g2.setClip(path);
		g2.drawImage(img_head.getImage(), 0, 0, null);
	}

	/**
	 * 设置头像
	 * @param head
	 */
	public void setHead(int head)
	{
		this.head = head;
		ImageIcon tmp = new ImageIcon(Global.Root + "resource/image/head/" + RoundHeadComp.this.head + ".jpg");
		img_head.setImage(tmp.getImage().getScaledInstance(width - 4, height - 4, Image.SCALE_DEFAULT));
		this.repaint();
	}
}
