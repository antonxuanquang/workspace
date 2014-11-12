package com.sean.im.client.bubble;

public interface Bubble
{
	public boolean needBefore();

	public boolean needAfter();

	public void beforeDisplay();

	public void drawBubble();

	public void afterDisplay();
}
