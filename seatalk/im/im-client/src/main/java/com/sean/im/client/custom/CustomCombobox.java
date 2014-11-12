package com.sean.im.client.custom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

import com.sean.im.client.constant.Global;

/**
 * 自定义下拉列表
 * @author sean
 */
public class CustomCombobox<E> extends CustomField implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JLabel icon;
	private RoundPopMenu menu;
	private List<E> list = new LinkedList<E>();
	private ItemListener listener;
	private int selectedIndex = 0;
	private Point point;

	public CustomCombobox(boolean editable)
	{
		icon = new JLabel(new ImageIcon(Global.Root + "resource/image/common/combobox.png"));
		icon.setBackground(Color.WHITE);
		icon.setOpaque(true);
		this.add(icon, BorderLayout.EAST);
		this.setEditable(editable);

		menu = new RoundPopMenu(false);
		menu.setMaximumSize(new Dimension(this.getWidth(), 200));

		icon.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				menu.setPreferredSize(new Dimension(CustomCombobox.this.getWidth() - 5, menu.getPreferredSize().height));
				if (point != null)
				{
					menu.show(CustomCombobox.this, point.x, point.y);
				}
				else
				{
					menu.show(CustomCombobox.this, 0, CustomCombobox.this.getHeight() - 5);
				}
			}
		});
	}

	public List<E> getList()
	{
		return list;
	}

	public void addItemListener(ItemListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void addKeyListener(KeyListener listener)
	{
		super.addKeyListener(listener);
	}

	public E getSelectedItem()
	{
		return list.get(selectedIndex);
	}

	public int getSelectedIndex()
	{
		return this.selectedIndex;
	}

	public void setSelectedIndex(int index)
	{
		this.selectedIndex = index;
		this.setText(list.get(index).toString());
	}

	public void setSelectedItem(E item)
	{
		int idx = 0;
		for (E it : list)
		{
			if (it == item)
			{
				this.setSelectedIndex(idx);
				return;
			}
			idx++;
		}
	}

	public void addElement(E e)
	{
		JMenuItem item = new JMenuItem(e.toString());
		list.add(e);
		item.setName(list.size() - 1 + "");
		item.setFont(Global.FONT);
		item.setBackground(Color.WHITE);
		item.addActionListener(this);
		menu.add(item);
	}

	public void setShowLocation(Point point)
	{
		this.point = point;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JMenuItem item = (JMenuItem) e.getSource();
		selectedIndex = Integer.parseInt(item.getName());
		this.setText(list.get(selectedIndex).toString());
		if (this.listener != null)
		{
			listener.itemStateChanged(null);
		}
	}
}
