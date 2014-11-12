package com.sean.im.client.custom;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 自定义图片按钮
 * @author sean
 */
public class CustomButton extends JLabel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	private ImageIcon leaveImg, overImg, pressImg;
	private CustomButtonListener listener;

	public CustomButton(String leaveImgPath, String overImgPath, String pressImgPath)
	{
		leaveImg = new ImageIcon(leaveImgPath);
		overImg = new ImageIcon(overImgPath);
		pressImg = new ImageIcon(pressImgPath);
		this.setIcon(leaveImg);
		this.addMouseListener(this);
	}

	public void addClickListener(CustomButtonListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		this.setIcon(pressImg);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		this.setIcon(leaveImg);
		if (listener != null)
		{
			listener.click(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		this.setIcon(overImg);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		this.setIcon(leaveImg);
	}

	public interface CustomButtonListener
	{
		public void click(MouseEvent e);
	}
}
