package com.sean.im.context.spi;

public interface UserSpi
{
	public void initUserStatus();
	
	public void initUserStatus(long userId);
}
