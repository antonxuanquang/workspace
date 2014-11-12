package com.sean.im.client.listitem;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.sean.im.client.comp.HeadComp;
import com.sean.im.client.constant.Global;
import com.sean.im.client.custom.CommonLabel;
import com.sean.im.client.menu.FlockMenu;
import com.sean.im.commom.entity.Flock;

/**
 * 群面板
 * @author sean
 */
public class FlockItem extends JPanel
{
	private static final long serialVersionUID = 1L;
	private CommonLabel lbl_name, lbl_sigurature;
	private HeadComp jp_head;
	private Flock flock;

	public FlockItem(Flock flock)
	{
		this.flock = flock;
		this.setLayout(null);
		this.setOpaque(false);
		this.setBackground(Global.DarkYellow);

		jp_head = new HeadComp(Global.Root + "resource/image/flock.png", 35, 35);
		jp_head.setBounds(7, 2, 35, 35);
		jp_head.setCursor(Global.CURSOR_HAND);
		this.add(jp_head);

		lbl_name = new CommonLabel(flock.getName());
		lbl_name.setBounds(47, 0, 1000, 25);
		this.add(lbl_name);

		lbl_sigurature = new CommonLabel(flock.getSignature(), Color.GRAY);
		lbl_sigurature.setBounds(47, 15, 1000, 25);
		this.add(lbl_sigurature);

		this.setPreferredSize(new Dimension(10000, 40));
	}

	public void setFlock(Flock flock)
	{
		this.flock = flock;
	}
	
	public Flock getFlock()
	{
		return flock;
	}

	public void notifyChange()
	{
		lbl_name.setText(flock.getName());
		lbl_sigurature.setText(flock.getSignature());
	}

	public void showMenu(JComponent component, int x, int y)
	{
		FlockMenu menu = new FlockMenu(flock);
		menu.show(component, x, y);
	}
}