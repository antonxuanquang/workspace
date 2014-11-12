package com.sean.im.context.spi;

import java.util.List;

public interface FriendSpi
{
	public List<Long> getFriendIdOfUser(long userId);
}
