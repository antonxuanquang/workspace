package com.sean.im.client.comp;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import com.sean.im.client.custom.CustomList;
import com.sean.im.client.form.flock.ChatRoomForm;
import com.sean.im.client.listitem.FlockItem;
import com.sean.im.client.util.ChatFormCache;
import com.sean.im.commom.entity.Flock;

/**
 * 群列表
 * @author sean
 */
public class FlockListComp extends CustomList
{
	private static final long serialVersionUID = 1L;

	private List<Flock> flocks;
	private List<FlockItem> flockItems;

	public FlockListComp()
	{
		Mouse_Listener ml = new Mouse_Listener();
		this.addMouseListener(ml);
		flocks = new LinkedList<Flock>();
		flockItems = new LinkedList<FlockItem>();
	}

	public void setFlock(List<Flock> fs)
	{
		this.flocks = fs;
		this.notifyChange();
	}
	
	public Flock getFlock(long flockId)
	{
		for(Flock f : flocks)
		{
			if (f.getId() == flockId)
			{
				return f;
			}
		}
		return null;
	}
	
	public synchronized void notifyChange(long flockId)
	{
		for(FlockItem fi : flockItems)
		{
			if (fi.getFlock().getId() == flockId)
			{
				fi.notifyChange();
				return;
			}
		}
	}

	/**
	 * 通知修改列表
	 */
	private synchronized void notifyChange()
	{
		DefaultListModel<JPanel> dlm = new DefaultListModel<JPanel>();

		int i = 0;
		FlockItem fp = null;
		for (Flock f : flocks)
		{
			if (this.flockItems.size() > i)
			{
				fp = flockItems.get(i);
				fp.setFlock(f);
			}
			else
			{
				fp = new FlockItem(f);
				flockItems.add(fp);
			}
			fp.notifyChange();
			dlm.addElement(fp);
			i++;
		}
		this.setModel(dlm);
	}

	/**
	 * 添加群
	 */
	public void addFlock(Flock flock)
	{
		if (this.flocks.contains(flock))
		{
			flocks.remove(flock);
			flocks.add(0, flock);
		}
		else
		{
			this.flocks.add(0, flock);
			if (flocks.size() > 32)
			{
				flocks.remove(flocks.size() - 1);
			}
		}
		this.notifyChange();
	}

	/**
	 * 删除群
	 */
	public void removeFlock(Flock flock)
	{
		this.flocks.remove(flock);
		this.notifyChange();
	}
	
	/**
	 * 删除群
	 */
	public void removeFlock(long flockId)
	{
		for(Flock f : flocks)
		{
			if (f.getId() == flockId)
			{
				this.flocks.remove(f);
				this.notifyChange();
				break;
			}
		}
	}

	/**
	 * 获取群
	 */
	public List<Flock> getFlocks()
	{
		return this.flocks;
	}

	/**
	 * 鼠标事件
	 */
	private class Mouse_Listener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			FlockItem fp = (FlockItem) FlockListComp.this.getSelectedValue();
			if (fp != null)
			{
				// 如果是右键
				if (e.isMetaDown())
				{
					fp.showMenu(FlockListComp.this, e.getX(), e.getY());
				}
				else
				{
					if (e.getClickCount() == 2)
					{
						ChatRoomForm room = ChatFormCache.getChatRoomForm(fp.getFlock());
						room.setVisible(true);
						room.initData();
					}
				}
			}
		}
	}
}
