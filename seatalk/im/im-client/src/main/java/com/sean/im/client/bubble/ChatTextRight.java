package com.sean.im.client.bubble;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.util.Translator;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.entity.Message;

/**
 * 聊天文本信息，右边
 * @author sean
 */
public class ChatTextRight extends JPanel implements Bubble
{
	private static final long serialVersionUID = 1L;
	private MutableAttributeSet attr_gray;

	private Message msg;
	private Sender sender;
	private boolean isOriginal;

	public ChatTextRight(Message msg, Sender sender, boolean isOriginal)
	{
		this.msg = msg;
		this.sender = sender;
		this.isOriginal = isOriginal;
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(10, 0, 0, 0));
		attr_gray = new SimpleAttributeSet();
		StyleConstants.setForeground(attr_gray, Color.GRAY);
		this.setOpaque(false);
	}

	@Override
	public boolean needBefore()
	{
		return false;
	}

	@Override
	public boolean needAfter()
	{
		return false;
	}

	@Override
	public void beforeDisplay()
	{
	}

	@Override
	public void drawBubble()
	{
		
		JPanel west = new JPanel();
		west.setOpaque(false);
		west.setLayout(null);
		west.setPreferredSize(new Dimension(35, 35));
		HeadComp head = new HeadComp(sender.head, 35, 35);
		head.setBounds(0, 0, 35, 35);
		west.add(head);
		this.add(west, BorderLayout.WEST);

		JPanel center = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				UIUtil.drawBubble(Global.Bubble_Right, g, this);
			}
		};
		center.setOpaque(false);
		center.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		center.setBorder(new EmptyBorder(10, 15, 10, 10));
		this.add(center, BorderLayout.CENTER);

		JTextPane txt = new JTextPane();
		txt.setEditable(false);
		txt.setOpaque(false);
		appendMsg(txt, msg, isOriginal);
		center.add(txt);
	}

	@Override
	public void afterDisplay()
	{
	}

	/**
	 * 追加消息显示
	 * @param message
	 */
	private void appendMsg(JTextPane panel, Message message, boolean isOrignal)
	{
		String msg = message.getContent();
		String dic = ApplicationContext.User.getTranslator();
		if (dic != null && !dic.isEmpty())
		{
			msg = Translator.translate(msg, dic);
		}
		this.appendToContent(panel, msg, null);

		if (isOrignal)
		{
			String orignal = "\n原文：" + message.getContent();
			this.appendToContent(panel, orignal, attr_gray);
		}
	}

	private void appendToContent(JTextPane panel, String msg, MutableAttributeSet attr)
	{
		int length = msg.length();
		int charLen = 0;
		for (int i = 0; i < length; i++)
		{
			if (msg.charAt(i) == '/')
			{
				String path = msg.substring(i + 1, i + 8);
				path = path.trim();
				ImageIcon icon = new ImageIcon(Global.Root + "resource/image/emotion/" + path);

				if (icon != null)
				{
					panel.select(panel.getDocument().getLength(), panel.getDocument().getLength());
					panel.insertIcon(icon);
					i = i + 7;
					charLen += 2;
					continue;
				}
			}
			else
			{
				charLen += 1;
			}
			try
			{
				Document doc = panel.getDocument();
				doc.insertString(doc.getLength(), "" + msg.charAt(i), attr);

				if (charLen >= 40)
				{
					charLen = 0;
					doc.insertString(doc.getLength(), "\r\n", attr);
				}
			}
			catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}
	}
}
