package com.sean.im.client.custom;

import java.lang.reflect.Field;

import javax.swing.JMenu;

/**
 * 圆角菜单
 * @author sean
 */
public class RoundMenu extends JMenu
{
	private static final long serialVersionUID = 1L;

	public RoundMenu()
	{
		this(null);
	}

	public RoundMenu(String text)
	{
		super(text);
		this.setOpaque(false);
		this.setPopMenu();
	}

	private void setPopMenu()
	{
		RoundPopMenu popupMenu = new RoundPopMenu(false);
		popupMenu.setInvoker(this);
		popupListener = createWinListener(popupMenu);

		try
		{
			Field field = JMenu.class.getDeclaredField("popupMenu");
			field.setAccessible(true);
			field.set(this, popupMenu);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
