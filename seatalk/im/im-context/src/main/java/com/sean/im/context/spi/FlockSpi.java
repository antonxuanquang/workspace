package com.sean.im.context.spi;

import java.util.List;

public interface FlockSpi
{
	public List<Long> getFlockMembers(long flockId);
}
