package com.sean.igo.tab;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class TabPanel
{
	protected ViewGroup panel;
	protected FragmentActivity main;

	protected LinearLayout tab;
	protected TextView txt;
	protected ImageView img;

	public TabPanel(FragmentActivity main, int panelId)
	{
		this.main = main;
		this.panel = (ViewGroup) main.findViewById(panelId);
	}

	public ViewGroup getPanel()
	{
		return panel;
	}

	public View getTabButton()
	{
		return tab;
	}

	public abstract void init();

	public abstract void onHighlight(boolean highlight);
}
