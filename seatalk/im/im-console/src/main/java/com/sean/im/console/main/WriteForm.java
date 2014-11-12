package com.sean.im.console.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.alibaba.fastjson.JSONObject;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.core.RequestHandler;

public class WriteForm extends JFrame
{
	private static final long serialVersionUID = 1L;
	private long receiverId;
	private JButton btn_send;
	private JTextArea content;

	public WriteForm(long receiverId)
	{
		this.receiverId = receiverId;
		this.setTitle("发送警告信息");
		this.setSize(300, 200);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);

		JPanel south = new JPanel();
		south.setLayout(new FlowLayout());
		this.add(south, BorderLayout.SOUTH);

		JPanel center = new JPanel();
		center.setLayout(new BorderLayout());
		this.add(center, BorderLayout.CENTER);

		btn_send = new JButton("发送");
		south.add(btn_send);

		content = new JTextArea();
		center.add(content);

		Btn_Listener bl = new Btn_Listener();
		btn_send.addActionListener(bl);
	}

	private class Btn_Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == btn_send)
			{
				String conte = content.getText();
				if (conte.isEmpty())
				{
					return;
				}
				Request request = new Request("SendWarningMsgAction");
				request.setParameter("receiverId", receiverId);
				request.setParameter("content", conte);
				HttpUtil.request(request, new RequestHandler()
				{
					@Override
					public void callback(JSONObject data)
					{
						JOptionPane.showMessageDialog(null, "发送成功", "提示消息", JOptionPane.WARNING_MESSAGE);
					}
				});
			}
		}
	}
}
