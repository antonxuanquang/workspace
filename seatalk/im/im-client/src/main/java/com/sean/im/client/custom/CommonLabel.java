package com.sean.im.client.custom;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.sean.im.client.constant.Global;

/**
 * 通用label
 * @author sean
 */
public class CommonLabel extends JLabel
{
	private static final long serialVersionUID = 1L;

	public CommonLabel(String text)
	{
		super(text);
		this.setFont(Global.FONT);
	}
	
	public CommonLabel()
	{
		this.setFont(Global.FONT);
	}
	
	public CommonLabel(String text, Color foreColor)
	{
		super(text);
		this.setFont(Global.FONT);
		this.setForeground(foreColor);
	}
	
	public CommonLabel(ImageIcon icon)
	{
		super(icon);
		this.setFont(Global.FONT);
	}
	
	public CommonLabel(String text, ImageIcon icon, int layout)
	{
		super(text, icon, layout);
		this.setFont(Global.FONT);
	}
	
	public CommonLabel(String text, int horizontalAlignment)
	{
		super(text, horizontalAlignment);
		this.setFont(Global.FONT);
	}
}
