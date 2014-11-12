package com.sean.im.client.custom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.sean.im.client.constant.Global;

/**
 * 自定义复选框
 * @author sean
 */
public class CustomCheckBox extends CommonLabel
{
	private static final long serialVersionUID = 1L;
	private static ImageIcon up = new ImageIcon(Global.Root + "resource/image/common/checkbox_up.png");
	private static ImageIcon down = new ImageIcon(Global.Root + "resource/image/common/checkbox_down.png");
	private boolean isChecked = false;
	private ActionListener listener;

	public CustomCheckBox(String text)
	{
		super(text, up, JLabel.LEFT);
		this.setCursor(Global.CURSOR_HAND);
		this.setOpaque(false);
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				isChecked = !isChecked;
				if (isChecked)
				{
					CustomCheckBox.this.setIcon(down);
				}
				else
				{
					CustomCheckBox.this.setIcon(up);
				}

				if (listener != null)
				{
					listener.actionPerformed(new ActionEvent(CustomCheckBox.this, 0, null));
				}
			}
		});
	}

	public void addActionListener(ActionListener al)
	{
		this.listener = al;
	}

	public boolean isSelected()
	{
		return isChecked;
	}

	public void setSelected(boolean check)
	{
		this.isChecked = check;
		if (isChecked)
		{
			CustomCheckBox.this.setIcon(down);
		}
		else
		{
			CustomCheckBox.this.setIcon(up);
		}
	}
}
