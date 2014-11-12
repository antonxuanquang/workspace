package com.sean.im.client.comp;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.io.output.CountingOutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.form.FileTransferForm;
import com.sean.im.client.menu.SendFileMenu;
import com.sean.im.commom.constant.Actions;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.core.Request;
import com.sean.im.commom.entity.Friend;

/**
 * 文件传输控件
 * @author sean
 */
@SuppressWarnings("deprecation")
public final class FileTransferComp extends JPanel
{
	private static final long serialVersionUID = 1L;

	private HeadComp head;
	private CommonLabel filename, speed, sending;
	private JPanel progressBg, progress;
	private Friend friend;
	private File file;
	private int status;

	private HttpClient httpclient;
	private CountingOutputStream cos;
	private boolean rs = false;

	private SendFileMenu menu;

	private Thread uploader;

	private static final int MaxLength = 230;

	public FileTransferComp(File file, Friend friend)
	{
		this.file = file;
		this.friend = friend;
		this.setLayout(null);
		this.setBackground(Global.DarkYellow);
		this.setPreferredSize(new Dimension(380, 50));
		this.setBorder(new EmptyBorder(5, 5, 5, 5));

		menu = new SendFileMenu(this);

		this.head = new HeadComp(friend.getHead(), 40, 40);
		head.setBounds(5, 5, 40, 40);
		this.add(head);

		sending = new CommonLabel(new ImageIcon(Global.Root + "resource/image/send_file.png"));
		sending.setBounds(55, 10, 32, 32);
		this.add(sending);

		filename = new CommonLabel(file.getName());
		filename.setBounds(100, 0, 150, 25);
		this.add(filename);

		progressBg = new JPanel();
		progressBg.setBorder(new LineBorder(Global.DarkBlue));
		progressBg.setLayout(null);
		progressBg.setBackground(Color.WHITE);
		progressBg.setBounds(100, 25, MaxLength, 8);
		progress = new JPanel();
		progress.setBackground(Global.DarkBlue);
		progressBg.add(progress);
		this.add(progressBg);

		speed = new CommonLabel("0k/s");
		speed.setBounds(100, 30, 100, 25);
		this.add(speed);
	}

	public SendFileMenu getMenu()
	{
		menu.setStatus(status);
		return menu;
	}

	/**
	 * 开始传输
	 */
	public void upload()
	{
		status = SendFileMenu.Sending;
		uploader = new Thread(new Uploader());
		uploader.start();
		new Thread(new Monitor()).start();
	}

	/**
	 * 停止传输
	 */
	public void stop()
	{
		status = SendFileMenu.StopSend;
		rs = false;
		if (uploader != null)
		{
			uploader.stop();
			speed.setText(Global.Lan.get("已停止"));
			FileTransferForm.getInstance().repaint();
		}
	}

	/**
	 * 更新进度条
	 * @param percent
	 */
	private void setProgress(float percent)
	{
		int width = (int) (MaxLength * percent);
		this.progress.setBounds(1, 1, width, 6);
	}

	/**
	 * 更新速度
	 * @param bytes			100毫秒传输的字节数
	 */
	private void setSpeed(long bytes)
	{
		int oneSecondK = (int) (bytes / 1000 * 10);
		speed.setText(oneSecondK + "k/s");
	}

	private class Uploader implements Runnable
	{
		@Override
		public void run()
		{
			httpclient = new DefaultHttpClient();
			long fileId = 0;
			try
			{
				Thread.sleep(500);
				HttpPost httppost = new HttpPost(HttpUtil.Url + "UploadFile");
				httppost.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");
				httppost.getParams().setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "utf-8");
				FileBody fb = new FileBody(file);
				CountMultipartEntity reqEntity = new CountMultipartEntity();
				reqEntity.addPart(file.getName(), fb);
				httppost.setEntity(reqEntity);
				HttpResponse response = httpclient.execute(httppost);
				if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode())
				{
					int len = (int) response.getEntity().getContentLength();
					byte[] b = new byte[len];
					response.getEntity().getContent().read(b);
					fileId = Long.parseLong(new String(b));
					rs = true;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				status = SendFileMenu.StopSend;
				rs = false;
			}
			finally
			{
				httpclient.getConnectionManager().shutdown();
			}
			status = SendFileMenu.AfterSend;
			if (rs)
			{
				speed.setText(Global.Lan.get("传输完成"));
				// 通知对方接收文件
				Request request = new Request(Actions.SendFileAction);
				request.setParameter("receiverId", friend.getFriendId());
				request.setParameter("fileId", fileId);
				request.setParameter("filename", file.getName());
				HttpUtil.request(request, null);
			}
			else
			{
				speed.setText(Global.Lan.get("传输失败"));
			}
		}
	}

	private class Monitor implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				int time = 0;
				long hasRead = 0;
				long bytes = 0;
				long tmp = 0;
				while (status == SendFileMenu.Sending)
				{
					if (cos != null)
					{
						tmp = cos.getByteCount();
						bytes = tmp - hasRead;
						hasRead = tmp;
						// 更新进度
						setProgress((float) hasRead / (float) file.length());
						// 每秒更新速度
						if (time % 20 == 0)
						{
							setSpeed(bytes);
						}
					}
					FileTransferForm.getInstance().repaint();
					Thread.sleep(50);
					time++;
				}
				if (cos != null)
				{
					hasRead = cos.getByteCount();
					// 更新进度
					setProgress((float) hasRead / (float) file.length());
					FileTransferForm.getInstance().repaint();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private class CountMultipartEntity extends MultipartEntity
	{
		@Override
		public void writeTo(OutputStream outstream) throws IOException
		{
			cos = new CountingOutputStream(outstream);
			super.writeTo(cos);
		}
	}
}
