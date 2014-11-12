package com.sean.im.client.bubble;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.log4j.Logger;

import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.util.MusicUtil;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.constant.Loggers;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.entity.Message;
import com.sean.log.core.LogFactory;

/**
 * 聊天语音信息，左边
 * @author sean
 */
public class ChatAudioLeft extends JPanel implements Bubble
{
	private static final long serialVersionUID = 1L;
	private File file;
	private Message msg;
	private Sender sender;
	private Request request;
	private static final Logger logger = LogFactory.getLogger(Loggers.IM);

	public ChatAudioLeft(Message msg, Sender sender, Request request)
	{
		this.request = request;
		this.msg = msg;
		this.sender = sender;
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(10, 0, 0, 0));
		this.setCursor(Global.CURSOR_HAND);
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
		return true;
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
				UIUtil.drawBubble(Global.Bubble_Left, g, this);
			}
		};
		center.setOpaque(false);
		center.setBorder(new EmptyBorder(10, 15, 10, 10));
		center.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		this.add(center, BorderLayout.CENTER);

		this.file = (File) msg.getSingleParam();
		JLabel txt = new JLabel(file.length() / 1024 + "K");
		txt.setFont(Global.FONT);
		center.add(txt);
		center.add(new JLabel(new ImageIcon(Global.Root + "resource/image/chatform/audio.png")));
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				MusicUtil.play(file);
			}
		});
	}

	@Override
	public void afterDisplay()
	{
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(HttpUtil.Url + "/UploadAudio");
			httppost.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");
			httppost.getParams().setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "utf-8");
			FileBody fb = new FileBody(file);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart(file.getName(), fb);
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode())
			{
				int len = (int) response.getEntity().getContentLength();
				byte[] b = new byte[len];
				response.getEntity().getContent().read(b);
				String url = new String(b);

				// 通知对方接受
				request.setParameter("audioUrl", url);
				HttpUtil.request(request, null);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}
}
