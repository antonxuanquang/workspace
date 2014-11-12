package com.sean.im.client.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.LinkLabel;

/**
 * 聊天窗体上方通知控件
 * @author sean
 */
public class NoticeComp extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private LinkLabel close;
	private JLabel lbText;
	private NoticeListener listener;

	public NoticeComp(String text)
	{
		this.setBackground(Global.LightYellow);
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(0, 2, 0, 5));
		close = new LinkLabel("X");
		lbText = new JLabel(text);
		lbText.setFont(Global.FONT);
		lbText.setPreferredSize(new Dimension(100000, 25));
		this.add(close, BorderLayout.EAST);
		this.add(lbText, BorderLayout.WEST);
		close.addActionListener(this);
	}
	
	public void addNoticeListener(NoticeListener listener)
	{
		this.listener = listener;
	}

	public interface NoticeListener
	{
		public void onClose();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (listener != null)
		{
			listener.onClose();
		}
	}
}
