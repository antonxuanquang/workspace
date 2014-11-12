package com.sean.im.client.menu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.sean.im.client.comp.FileTransferComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonButton;
import com.sean.im.client.custom.RoundPopMenu;
import com.sean.im.client.form.FileTransferForm;

/**
 * 文件传输菜单
 * @author sean
 */
public class SendFileMenu extends RoundPopMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private CommonButton stop, del;
	private JPanel panel;
	private FileTransferComp ftc;
	private int status;

	public static final int Sending = 1;
	public static final int AfterSend = 2;
	public static final int StopSend = 3;

	public SendFileMenu(FileTransferComp ftc)
	{
		super(false);
		this.ftc = ftc;
		panel = new JPanel();
		panel.setOpaque(false);

		Dimension dimen = new Dimension(70, 25);

		stop = new CommonButton(Global.Lan.get("中断"));
		stop.setPreferredSize(dimen);
		del = new CommonButton(Global.Lan.get("删除"));
		del.setPreferredSize(dimen);

		this.add(panel);

		stop.addActionListener(this);
		del.addActionListener(this);
	}

	public void setStatus(int status)
	{
		this.status = status;
		panel.removeAll();
		switch (status)
		{
		case Sending:
			panel.add(stop);
			break;
		case AfterSend:
			panel.add(del);
			break;
		case StopSend:
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

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == stop)
		{
			this.ftc.stop();
		}
		else if (e.getSource() == del)
		{
			FileTransferForm.getInstance().removeFile(ftc);
		}
		this.setVisible(false);
	}
}