package com.sean.im.client.comp;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.io.input.CountingInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.sean.im.client.constant.Global;
import com.sean.im.client.core.ApplicationContext;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.form.FileTransferForm;
import com.sean.im.client.menu.ReceiveFileMenu;
import com.sean.im.client.util.IMIoUtil;
import com.sean.im.commom.core.HttpUtil;
import com.sean.im.commom.entity.FileInfo;
import com.sean.im.commom.entity.Friend;
import com.sean.im.commom.entity.UserInfo;

/**
 * 文件接收控件
 * @author sean
 */
public final class FileReceiveComp extends JPanel
{
	private static final long serialVersionUID = 1L;

	private HeadComp head;
	private CommonLabel filename, speed, receiving;
	private JPanel progressBg, progress;
	private FileInfo fi;

	private int status = ReceiveFileMenu.PreReceive;
	private boolean rs = false;
	private CountingInputStream cis;
	private HttpClient httpclient;
	private Thread downloader;
	private File file;
	
	private ReceiveFileMenu menu;

	private static final int MaxLength = 230;

	public FileReceiveComp(FileInfo fi, Friend friend)
	{
		this.fi = fi;
		this.setLayout(null);
		this.setBackground(Global.DarkYellow);
		this.setPreferredSize(new Dimension(380, 50));
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		menu = new ReceiveFileMenu(this);

		this.head = new HeadComp(friend.getHead(), 40, 40);
		head.setBounds(5, 5, 40, 40);
		this.add(head);

		receiving = new CommonLabel(new ImageIcon(Global.Root + "resource/image/receive_file.png"));
		receiving.setBounds(55, 10, 32, 32);
		this.add(receiving);

		filename = new CommonLabel(fi.getFilename());
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

		speed = new CommonLabel(Global.Lan.get("是否接收文件") + "?");
		speed.setBounds(100, 30, 100, 25);
		this.add(speed);
	}

	public ReceiveFileMenu getMenu()
	{
		menu.setStatus(status);
		return menu;
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

	/**
	 * 停止传输
	 */
	@SuppressWarnings("deprecation")
	public void stop()
	{
		status = ReceiveFileMenu.StopReceive;
		rs = false;
		if (downloader != null)
		{
			downloader.stop();
			speed.setText("已停止");
			FileTransferForm.getInstance().repaint();
		}
	}
	
	/**
	 * 拒绝
	 */
	public void refuce()
	{
		status = ReceiveFileMenu.RefuceReceive;
		speed.setText("结束传输");
		FileTransferForm.getInstance().repaint();
	}

	/**
	 * 开始下载
	 */
	public void download()
	{
		status = ReceiveFileMenu.Receiving;
		downloader = new Thread(new Downloader());
		downloader.start();
		new Thread(new Monitor()).start();
	}

	public File getFile()
	{
		return file;
	}
	
	private class Downloader implements Runnable
	{
		@Override
		public void run()
		{
			httpclient = new DefaultHttpClient();
			try
			{
				Thread.sleep(500);
				HttpPost httppost = new HttpPost(HttpUtil.Url + fi.getPath());
				HttpResponse response = httpclient.execute(httppost);
				if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode())
				{
					// 检查目录
					UserInfo user = ApplicationContext.User;
					IMIoUtil.checkUserFileDir(user.getUsername());
					file = new File(Global.Root + "/users/" + user.getUsername() + "/recv/file/" + fi.getFilename());
					if (file.exists())
					{
						file.delete();
					}
					FileOutputStream output = new FileOutputStream(file);

					cis = new CountingInputStream(response.getEntity().getContent());
					byte[] buf = new byte[10240];
					int len = 0;
					while ((len = cis.read(buf)) != -1)
					{
						output.write(buf, 0, len);
					}
					rs = true;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				status = ReceiveFileMenu.StopReceive;
				rs = false;
			}
			finally
			{
				httpclient.getConnectionManager().shutdown();
			}
			status = ReceiveFileMenu.AfterReceive;
			if (rs)
			{
				speed.setText(Global.Lan.get("传输完成"));
			}
			else
			{
				speed.setText(Global.Lan.get("传输失败"));
			}
			FileTransferForm.getInstance().repaint();
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
				while (status == ReceiveFileMenu.Receiving)
				{
					if (cis != null)
					{
						tmp = cis.getByteCount();
						bytes = tmp - hasRead;
						hasRead = tmp;
						// 更新进度
						setProgress((float) hasRead / (float) fi.getLength());
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
				if (cis != null)
				{
					hasRead = cis.getByteCount();
					// 更新进度
					setProgress((float) hasRead / (float) fi.getLength());
					FileTransferForm.getInstance().repaint();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
