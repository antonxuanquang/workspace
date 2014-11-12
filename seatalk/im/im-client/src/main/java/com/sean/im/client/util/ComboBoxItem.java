package com.sean.im.client.util;

public class ComboBoxItem
{
	private int id;
	private String text;

	public ComboBoxItem(int id, String text)
	{
		this.id = id;
		this.text = text;
	}

	public int getId()
	{
		return id;
	}

	public String getText()
	{
		return text;
	}

	@Override
	public String toString()
	{
		return this.text;
	}
}
