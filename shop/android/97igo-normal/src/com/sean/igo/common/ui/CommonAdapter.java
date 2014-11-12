package com.sean.igo.common.ui;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 通用适配
 * @author sean
 * @param <E>
 */
public abstract class CommonAdapter<E> extends BaseAdapter
{
	protected Context context;
	protected LayoutInflater mInflater;
	protected List<E> myList;

	public CommonAdapter(Context ctx)
	{
		this.context = ctx;
		this.myList = new LinkedList<E>();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addItems(final List<E> items, boolean refresh)
	{
		myList.addAll(items);
		if (refresh)
		{
			notifyDataSetChanged();
		}
	}

	public void addItem(final E item, boolean refresh)
	{
		myList.add(item);
		if (refresh)
		{
			notifyDataSetChanged();
		}
	}
	
	public void clear(boolean refresh)
	{
		myList.clear();
		if (refresh)
		{
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount()
	{
		return myList.size();
	}

	@Override
	public Object getItem(int index)
	{
		return myList.get(index);
	}

	@Override
	public long getItemId(int index)
	{
		return index;
	}
}