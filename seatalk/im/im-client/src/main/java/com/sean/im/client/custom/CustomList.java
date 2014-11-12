package com.sean.im.client.custom;

import java.awt.Component;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

/**
 * 自定义列表
 * @author sean
 */
public class CustomList extends CustomScrollPane
{
	private static final long serialVersionUID = 1L;
	private JList<JPanel> list;
	private DefaultListModel<JPanel> model;

	public CustomList()
	{
		model = new DefaultListModel<JPanel>();
		list = new JList<JPanel>(model);
		list.setOpaque(false);
		list.setBorder(null);
		list.setCellRenderer(new CellRenderer());
		this.setViewportView(list);
	}
	
	public int listSize()
	{
		return model.size();
	}
	
	public JPanel getElementAt(int index)
	{
		return model.getElementAt(index);
	}

	public void addElement(JPanel panel)
	{
		model.addElement(panel);
	}

	public void removeAllElement()
	{
		model.removeAllElements();
	}

	public void removeElement(JPanel panel)
	{
		model.removeElement(panel);
	}

	@Override
	public void addMouseListener(MouseListener listener)
	{
		list.addMouseListener(listener);
	}

	public void setModel(DefaultListModel<JPanel> model)
	{
		list.setModel(model);
	}

	public ListModel<JPanel> getModel()
	{
		return list.getModel();
	}

	public JPanel getSelectedValue()
	{
		return list.getSelectedValue();
	}

	public void setSelectedIndex(int index)
	{
		list.setSelectedIndex(index);
	}

	public int getSelectedIndex()
	{
		return list.getSelectedIndex();
	}

	/**
	 * 单元格绘制
	 */
	private class CellRenderer implements ListCellRenderer<JPanel>
	{
		@Override
		public Component getListCellRendererComponent(JList<? extends JPanel> list, JPanel value, int index, boolean isSelected, boolean cellHasFocus)
		{
			JPanel fp = value;
			if (isSelected)
			{
				fp.setOpaque(true);
			}
			else
			{
				fp.setOpaque(false);
			}
			return fp;
		}
	}
}
