package com.sean.im.client.bubble;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.comp.ImageComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.util.IMIoUtil;
import com.sean.im.client.util.UIUtil;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.entity.Message;
import com.sean.im.commom.entity.UserInfo;

/**
 * 聊天图片信息，右边
 * @author sean
 */
public class ChatImageRight extends JPanel implements Bubble
{
	private static final long serialVersionUID = 1L;
	private File file;
	private Message msg;
	private Sender sender;

	public ChatImageRight(Message msg, Sender sender)
	{
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
		return true;
	}

	@Override
	public boolean needAfter()
	{
		return false;
	}

	@Override
	public void beforeDisplay()
	{
		try
		{
			String url = msg.getContent();
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(HttpUtil.Url + url);
			HttpResponse response = httpclient.execute(httppost);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode())
			{
				UserInfo user = ApplicationContext.User;
				IMIoUtil.checkUserImageDir(user.getUsername());
				file = new File(Global.Root + "users/" + user.getUsername() + "/recv/image/" + UUID.randomUUID().toString() + ".jpg");
				FileOutputStream output = new FileOutputStream(file);

				InputStream input = response.getEntity().getContent();
				byte[] buf = new byte[10240];
				int len = 0;
				while ((len = input.read(buf)) != -1)
				{
					output.write(buf, 0, len);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
