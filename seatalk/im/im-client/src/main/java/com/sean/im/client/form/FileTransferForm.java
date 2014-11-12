package com.sean.im.client.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sean.im.client.comp.FileReceiveComp;
import com.sean.im.client.comp.FileTransferComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.CustomFrame;
import com.sean.im.client.custom.CustomList;
import com.sean.im.client.menu.ReceiveFileMenu;
import com.sean.im.client.menu.SendFileMenu;
import com.sean.im.commom.entity.FileInfo;
import com.sean.im.commom.entity.Friend;

/**
 * 文件传输窗体
 * @author sean
 */
public class FileTransferForm extends CustomFrame
{
	private static final long serialVersionUID = 1L;

	private static FileTransferForm instance = new FileTransferForm();

	private CustomList list;
	private CommonButton btn_close;

	private FileTransferForm()
	{
		super(360, (int)MainForm.FORM.getSize().getHeight());
		this.setCustomTitle(Global.Lan.get("文件传输"), null);
		this.setCustomLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setCustomMaxiable(false);

		list = new CustomList();
		list.setOpaque(true);
		list.setBackground(Color.WHITE);
		list.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				JPanel panel = list.getSelectedValue();
				if (panel instanceof FileReceiveComp)
				{
					FileReceiveComp frc = (FileReceiveComp) panel;
					ReceiveFileMenu menu = frc.getMenu();
					if (menu.isShowing())
					{
						menu.setVisible(false);
					}
					else
					{
						Dimension dimen = menu.getPreferredSize();
						menu.show(list, (360 - dimen.width) / 2, (440 - dimen.height) / 2);
					}
				}
				else if (panel instanceof FileTransferComp)
				{
					FileTransferComp ftc = (FileTransferComp) panel;
					SendFileMenu menu = ftc.getMenu();
					if (menu.isShowing())
					{
						menu.setVisible(false);
					}
					else
					{
						Dimension dimen = menu.getPreferredSize();
						menu.show(list, (360 - dimen.width) / 2, (440 - dimen.height) / 2);
					}
				}
			}
		});

		this.addCustomComponent(list, BorderLayout.CENTER);

		JPanel south = new JPanel();
		south.setPreferredSize(new Dimension(100, 35));
		south.setOpaque(false);
		south.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		this.addCustomComponent(south, BorderLayout.SOUTH);

		btn_close = new CommonButton(Global.Lan.get("关闭"));
		btn_close.setPreferredSize(new Dimension(60, 25));
		south.add(btn_close);

		btn_close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == btn_close)
				{
					FileTransferForm.this.setVisible(false);
				}
			}
		});
	}

	@Override
	public void setVisible(boolean visible)
	{
		if (visible)
		{
			Point point = MainForm.FORM.getLocationOnScreen();
			this.setLocation(point.x + (int)MainForm.FORM.getSize().getWidth(), point.y);
			super.setVisible(true);
		}
		else
		{
			super.setVisible(false);
		}
	}
	
	public static FileTransferForm getInstance()
	{
		return instance;
	}

	public void removeFile(JPanel panel)
	{
		((DefaultListModel<JPanel>) list.getModel()).removeElement(panel);
	}

	/**
	 * 传输文件
	 * @param file
	 */
	public void transferFile(File file, Friend friend)
	{
		FileTransferComp ftc = new FileTransferComp(file, friend);
		((DefaultListModel<JPanel>) list.getModel()).addElement(ftc);
		ftc.upload();
	}

	/**
	 * 接收文件快
	 * @param fb
	 */
	public void receiverFile(FileInfo fi, Friend friend)
	{
		FileReceiveComp frc = new FileReceiveComp(fi, friend);
		((DefaultListModel<JPanel>) list.getModel()).addElement(frc);
	}
}
