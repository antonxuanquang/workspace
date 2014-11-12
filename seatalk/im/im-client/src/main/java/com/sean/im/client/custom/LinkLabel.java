package com.sean.im.client.custom;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 带链接的Label
 * @author sean
 */
public class LinkLabel extends CommonLabel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	private ActionListener listener;

	public LinkLabel(String text)
	{
		super(text);
		this.setForeground(Color.BLUE);
		this.addMouseListener(this);
	}

	public void addActionListener(ActionListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (this.listener != null)
		{
			ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), null);
			listener.actionPerformed(ae);
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
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

}
