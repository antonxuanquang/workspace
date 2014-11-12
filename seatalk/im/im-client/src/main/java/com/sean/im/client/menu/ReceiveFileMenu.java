package com.sean.im.client.menu;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JPanel;

import com.sean.im.client.comp.FileReceiveComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.RoundPopMenu;
import com.sean.im.client.form.FileTransferForm;

/**
 * 文件传输菜单
 * @author sean
 */
public class ReceiveFileMenu extends RoundPopMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private CommonButton agree, refuce, stop, openfile, opendir, del;
	private JPanel panel;
	private FileReceiveComp frc;
	private int status;

	public static final int PreReceive = 1;
	public static final int Receiving = 2;
	public static final int AfterReceive = 3;
	public static final int StopReceive = 4;
	public static final int RefuceReceive = 5;

	public ReceiveFileMenu(FileReceiveComp frc)
	{
		super(false);
		this.frc = frc;
		panel = new JPanel();
		panel.setOpaque(false);

		Dimension dimen = new Dimension(70, 25);
		Dimension dimen2 = new Dimension(100, 25);

		agree = new CommonButton(Global.Lan.get("同意"));
		agree.setPreferredSize(dimen);
		refuce = new CommonButton(Global.Lan.get("拒绝"));
		refuce.setPreferredSize(dimen);
		stop = new CommonButton(Global.Lan.get("中断"));
		stop.setPreferredSize(dimen);
		del = new CommonButton(Global.Lan.get("删除"));
		del.setPreferredSize(dimen);
		openfile = new CommonButton(Global.Lan.get("打开文件"));
		openfile.setPreferredSize(dimen2);
		opendir = new CommonButton(Global.Lan.get("打开文件夹"));
		opendir.setPreferredSize(dimen2);

		this.add(panel);

		agree.addActionListener(this);
		refuce.addActionListener(this);
		stop.addActionListener(this);
		openfile.addActionListener(this);
		opendir.addActionListener(this);
		del.addActionListener(this);
	}

	public void setStatus(int status)
	{
		this.status = status;
		panel.removeAll();
		switch (status)
		{
		case PreReceive:
			panel.add(agree);
			panel.add(refuce);
			break;
		case Receiving:
			panel.add(stop);
			break;
		case AfterReceive:
			panel.add(openfile);
			panel.add(opendir);
			panel.add(del);
			break;
		case StopReceive:
			panel.add(del);
			break;
		case RefuceReceive:
			panel.add(del);
			break;
		default:
			break;
		}
		panel.repaint();
	}

	public int getStatus()
	{
		return this.status;
	}

	public boolean isEmpty()
	{
		return (status != RefuceReceive && status != StopReceive);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == agree)
		{
			this.frc.download();
		}
		else if (e.getSource() == refuce)
		{
			this.frc.refuce();
		}
		else if (e.getSource() == del)
		{
			FileTransferForm.getInstance().removeFile(frc);
		}
		else if (e.getSource() == stop)
		{
			this.frc.stop();
		}
		// 打开文件夹
		else if (e.getSource() == opendir)
		{
			try
			{
				String[] execString = new String[2];
				String osName = System.getProperty("os.name");
				if (osName.toLowerCase().startsWith("windows"))
				{
					Desktop.getDesktop().open(frc.getFile().getParentFile());
				}
				else
				{
					execString[0] = "nautilus";
					execString[1] = frc.getFile().getParentFile().getAbsolutePath();
					Runtime.getRuntime().exec(execString);
				}
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		// 打开文件
		else if (e.getSource() == openfile)
		{
			try
			{
				Desktop.getDesktop().open(frc.getFile());
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		this.setVisible(false);
	}

	public static void browsePath(String strPath)
	{
		String[] execString = new String[2];
		String filePath = null;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().startsWith("windows"))
		{
			// Window System;
			execString[0] = "explorer";
			try
			{
				filePath = strPath.replace("/", "\\");
			}
			catch (Exception ex)
			{
				filePath = strPath;
			}
		}
		else
		{
			// Unix or Linux;
			execString[0] = "nautilus";
			filePath = strPath;
		}

		execString[1] = filePath;
		try
		{
			Runtime.getRuntime().exec(execString);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println("异常啦...");
		}
	}
}