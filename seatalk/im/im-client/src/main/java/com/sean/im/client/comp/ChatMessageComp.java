package com.sean.im.client.comp;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.sean.im.client.bubble.Bubble;
import com.sean.im.client.bubble.ChatAudioLeft;
import com.sean.im.client.bubble.ChatAudioRight;
import com.sean.im.client.bubble.ChatImageLeft;
import com.sean.im.client.bubble.ChatImageRight;
import com.sean.im.client.bubble.ChatTextLeft;
import com.sean.im.client.bubble.ChatTextRight;
import com.sean.im.client.bubble.Sender;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CustomScrollPane;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.entity.Message;

/**
 * 聊天信息控件
 * @author sean
 */
public class ChatMessageComp extends CustomScrollPane
{
	private static final long serialVersionUID = 1L;
	private JPanel main;
	private int currHeight = 0;

	public ChatMessageComp(int maxMsgItems)
	{
		main = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g)
			{
				g.drawImage(new ImageIcon(Global.Root + "resource/image/white_bg_80.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		main.setOpaque(false);
		main.setLayout(null);

		this.setViewportView(main);
	}

	/**
	 * 清空
	 */
	public void clear()
	{
		main.removeAll();
		currHeight = 0;
		main.setPreferredSize(new Dimension(1, 0));
		main.repaint();
	}

	public void addLeftTxtMessage(Message msg, Sender sender)
	{
		Bubble bubble = new ChatTextLeft(msg, sender);
		this.addMessage(bubble);
	}

	public void addRightTxtMessage(Message msg, Sender sender, boolean isOriginal)
	{
		Bubble bubble = new ChatTextRight(msg, sender, isOriginal);
		this.addMessage(bubble);
	}

	public void addLeftImageMessage(Message msg, Sender sender, Request request)
	{
		Bubble bubble = new ChatImageLeft(msg, sender, request);
		this.addMessage(bubble);
	}

	public void addRightImageMessage(Message msg, Sender sender)
	{
		Bubble bubble = new ChatImageRight(msg, sender);
		this.addMessage(bubble);
	}

	public void addLeftAudioMessage(Message msg, Sender sender, Request request)
	{
		Bubble bubble = new ChatAudioLeft(msg, sender, request);
		this.addMessage(bubble);
	}

	public void addRightAudioMessage(Message msg, Sender sender)
	{
		Bubble bubble = new ChatAudioRight(msg, sender);
		this.addMessage(bubble);
	}

	/**
	 * 添加消息
	 * @param msg
	 */
	private void addMessage(final Bubble bubble)
	{
		if (!bubble.needBefore())
		{
			// 画气泡
			bubble.drawBubble();
			addBubble((JPanel) bubble);
			if (bubble.needAfter())
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						bubble.afterDisplay();
					}
				}).start();
			}
		}
		else
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					bubble.beforeDisplay();
					bubble.drawBubble();
					addBubble((JPanel) bubble);
					if (bubble.needAfter())
					{
						new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								bubble.afterDisplay();
							}
						}).start();
					}
				}
			}).start();
		}
	}

	/**
	 * 添加气泡
	 */
	private void addBubble(JPanel bubble)
	{
		Dimension dimen = bubble.getPreferredSize();
		bubble.setBounds(5, currHeight, dimen.width, dimen.height);
		currHeight += 5 + dimen.height;
		main.add(bubble);
		main.setPreferredSize(new Dimension(1, currHeight));
		Rectangle ret = new Rectangle(0, currHeight, 1, currHeight + 5);
		this.getViewport().scrollRectToVisible(ret);
	}
}
