package com.sean.im.client.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.LightButton;
import com.sean.im.client.custom.RoundPopMenu;
import com.sean.im.client.tray.TrayManager;
import com.sean.im.commom.constant.StatusEnum;

/**
 * 状态面板
 * @author sean
 */
public class StatusComp extends LightButton implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private int state;
	private RoundPopMenu menu;
	private JMenuItem online, leave, hide;
	private StateListener listener;

	public StatusComp(int state, StateListener listener)
	{
		super(getStatusImg(StatusEnum.Online));
		this.state = state;
		this.listener = listener;
		this.setCursor(Global.CURSOR_HAND);
		this.setStateIcon();

		this.menu = new RoundPopMenu(true);
		
		online = new JMenuItem(Global.Lan.get("在线"), new ImageIcon(Global.Root + "resource/image/status/online.png"));
		online.setFont(Global.FONT);
		online.setOpaque(false);
		leave = new JMenuItem(Global.Lan.get("离开"), new ImageIcon(Global.Root + "resource/image/status/leave.png"));
		leave.setFont(Global.FONT);
		leave.setOpaque(false);
		hide = new JMenuItem(Global.Lan.get("隐身"), new ImageIcon(Global.Root + "resource/image/status/hide.png"));
		hide.setFont(Global.FONT);
		hide.setOpaque(false);
		menu.add(online);
		menu.add(leave);
		menu.add(hide);

		online.addActionListener(this);
		leave.addActionListener(this);
		hide.addActionListener(this);

		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				menu.show(StatusComp.this, 5, 5);
			}
		});
	}

	private void setStateIcon()
	{
		switch (state)
		{
		case StatusEnum.Online:
			this.setIcon(new ImageIcon(Global.Root + "resource/image/status/online.png"));
			break;
		case StatusEnum.Leave:
			this.setIcon(new ImageIcon(Global.Root + "resource/image/status/leave.png"));
			break;
		case StatusEnum.Hide:
			this.setIcon(new ImageIcon(Global.Root + "resource/image/status/hide.png"));
			break;
		case StatusEnum.OffLine:
			this.setIcon(new ImageIcon(Global.Root + "resource/image/status/offline.png"));
			break;
		}
		
		TrayManager.setStatus(state);
	}

	/**
	 * 获取状态图片
	 * @return
	 */
	public static ImageIcon getStatusImg(int status)
	{
		switch (status)
		{
		case StatusEnum.Online:
			return new ImageIcon(Global.Root + "resource/image/status/online.png");
		case StatusEnum.Leave:
			return new ImageIcon(Global.Root + "resource/image/status/leave.png");
		case StatusEnum.Hide:
			return new ImageIcon(Global.Root + "resource/image/status/hide.png");
		case StatusEnum.OffLine:
			return new ImageIcon(Global.Root + "resource/image/status/offline.png");
		default:
			return null;
		}
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
		this.setStateIcon();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == online)
		{
			state = StatusEnum.Online;
		}
		else if (e.getSource() == leave)
		{
			state = StatusEnum.Leave;
		}
		else if (e.getSource() == hide)
		{
			state = StatusEnum.Hide;
		}

		if (listener != null)
		{
			listener.stateChanged(state);
		}

		this.setStateIcon();
	}

	public interface StateListener
	{
		public void stateChanged(int state);
	}
}
