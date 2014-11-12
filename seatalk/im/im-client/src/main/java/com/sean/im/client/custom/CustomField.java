package com.sean.im.client.custom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import com.sean.im.client.constant.Global;
import com.sean.im.client.util.UIUtil;

/**
 * 自定义输入框
 * @author sean
 */
public class CustomField extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JTextComponent field;
	private boolean isFocus = false;

	public static final int JTEXTFIELD = 1;
	public static final int JPASSWORD = 2;
	public static final int JTEXTAREA = 3;

	private static BufferedImage BG_UP, BG_DOWN;
	static
	{
		try
		{
			BufferedImage img = ImageIO.read(new File(Global.Root + "resource/image/common/edit.png"));
			BG_UP = img.getSubimage(0, 0, 24, 23);
			BG_DOWN = img.getSubimage(24, 0, 24, 24);
			img = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public CustomField()
	{
		this(JTEXTFIELD);
	}

	public CustomField(int type)
	{
		this.setOpaque(false);
		this.setBorder(new EmptyBorder(3, 3, 3, 3));
		this.setLayout(new BorderLayout());

		switch (type)
		{
		case JTEXTFIELD:
			this.field = new JTextField();
			break;
		case JPASSWORD:
			this.field = new JPasswordField();
			break;
		case JTEXTAREA:
			this.field = new JTextArea();
			break;
		default:
			break;
		}
		field.setBorder(new EmptyBorder(0, 0, 0, 0));
		field.setBackground(Color.WHITE);
		field.setFont(Global.FONT);
		this.add(field, BorderLayout.CENTER);

		field.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				isFocus = true;
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				isFocus = false;
				repaint();
			}

		});
	}

	public void setEditable(boolean editable)
	{
		this.field.setEditable(editable);
	}

	public String getText()
	{
		return field.getText();
	}
	
	@Override
	public void requestFocus()
	{
		field.requestFocus();
	}

	public void setText(String text)
	{
		field.setText(text);
	}
	
	public JTextComponent getField()
	{
		return field;
	}

	@Override
	public void addKeyListener(KeyListener kl)
	{
		field.addKeyListener(kl);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (isFocus)
		{
			UIUtil.drawBackground(BG_DOWN, g, this, 5);
		}
		else
		{
			UIUtil.drawBackground(BG_UP, g, this, 5);
		}
	}

}
