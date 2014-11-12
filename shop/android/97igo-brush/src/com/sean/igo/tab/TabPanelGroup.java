package com.sean.igo.tab;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

public class TabPanelGroup implements OnClickListener
{
	private List<TabPanel> tabs;

	public TabPanelGroup(List<TabPanel> tabs)
	{
		this.tabs = tabs;

		for (TabPanel it : tabs)
		{
			it.init();
			it.getTabButton().setOnClickListener(this);
		}
	}

	public void setSelectTab(int index)
	{
		for (int i = 0; i < tabs.size(); i++)
		{
			TabPanel it = tabs.get(i);
			if (i == index)
			{
				it.getPanel().setVisibility(View.VISIBLE);
				it.onHighlight(true);
			}
			else
			{
				it.onHighlight(false);
				it.getPanel().setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v)
	{
		for (TabPanel it : tabs)
		{
			if (it.getTabButton() == v)
			{
				it.getPanel().setVisibility(View.VISIBLE);
				it.onHighlight(true);
			}
			else
			{
				it.onHighlight(false);
				it.getPanel().setVisibility(View.GONE);
			}
		}
	}
}
