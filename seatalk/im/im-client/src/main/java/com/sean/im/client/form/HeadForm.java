package com.sean.im.client.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sean.im.client.constant.Global;

/**
 * 头像
 * @author sean
 */
public class HeadForm extends JDialog
{
	private static final long serialVersionUID = 1L;
	private JPanel headPanel, south;
	private MyInfoForm parent;

	public HeadForm(MyInfoForm parent)
	{
		super(parent, true);
		this.parent = parent;
		this.setMinimumSize(new Dimension(410, 300));
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(parent);
		this.setIconImage(new ImageIcon(Global.Root + "resource/image/icon.png").getImage());
		this.setTitle(Global.Lan.get("更换头像标题"));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		headPanel = new JPanel();
		headPanel.setBackground(Color.white);
		headPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 2, 2));

		Mouse_Listener ml = new Mouse_Listener();
		for (int i = 1; i < 23; i++)
		{
			JLabel lbl = new JLabel(new ImageIcon(Global.Root + "resource/image/head/" + i + ".jpg"));
			lbl.setPreferredSize(new Dimension(56, 56));
			lbl.setCursor(Global.CURSOR_HAND);
			lbl.setName(String.valueOf(i));
			lbl.addMouseListener(ml);
			headPanel.add(lbl);
		}

		south = new JPanel();
		south.setBackground(Global.LightBlue);
		south.setPreferredSize(new Dimension(1000, 35));

		this.add(headPanel);
		this.add(south, BorderLayout.SOUTH);
	}

	private class Mouse_Listener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			int head = Integer.parseInt(((JLabel) e.getSource()).getName());
			Global.tmpHead = head;
			parent.setHead(head);
			HeadForm.this.setVisible(false);
			HeadForm.this.dispose();
			
			System.gc();
		}
	}
}
