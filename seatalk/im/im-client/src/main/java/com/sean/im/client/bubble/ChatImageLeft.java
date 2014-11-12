package com.sean.im.client.bubble;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
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

import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.comp.ImageComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.entity.Message;

/**
 * 聊天图片信息，左边
 * @author sean
 */
public class ChatImageLeft extends JPanel implements Bubble
{
	private static final long serialVersionUID = 1L;
	private Sender sender;
	private File file;
	private Message msg;
	private Request request;

	public ChatImageLeft(Message msg, Sender sender, Request request)
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
		center.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		center.setBorder(new EmptyBorder(10, 15, 10, 10));
		this.add(center, BorderLayout.CENTER);
		
		this.file = (File) msg.getSingleParam();
		ImageIcon img = new ImageIcon(file.getAbsolutePath());
		if (img.getIconHeight() > 260 || img.getIconWidth() > 350)
		{
			int[] size = this.getScaleSize(img.getIconWidth(), img.getIconHeight());
			center.add(new ImageComp(img, size[0], size[1]));
		}
		else
		{
			center.add(new ImageComp(file, img.getIconWidth(), img.getIconHeight()));
		}
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				try
				{
					Desktop.getDesktop().open(file);
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		});
	}

	@Override
	public void afterDisplay()
	{
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(HttpUtil.Url + "/UploadImage");
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
				request.setParameter("imgUrl", url);
				HttpUtil.request(request, null);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private int[] getScaleSize(int width, int height)
	{
		int max = width > height ? width : height;
		float percent = (float) max / 260f;
		int[] rs = new int[2];
		rs[0] = (int) (width / percent);
		rs[1] = (int) (height / percent);
		return rs;
	}
}
