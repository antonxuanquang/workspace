package com.sean.brush;

import android.content.Context;

public abstract class Platform
{
	public abstract void clearCache(Context context);
	
	public abstract void onUserChanged(Context context, User user) throws Exception;
	
	public abstract void openOfferwall(Context context);
	
	public abstract void closeOfferwall(Context context) throws Exception;
	
	public abstract void checkPoints(Context context) throws Exception;
}
