package com.sean.igo.common.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class DisplayUtil
{
	private static int widthPixels, heightPixels;
	private static int widthDp, heightDp;
	private static DisplayMetrics dm;

	public static void initDisplayParams(Context context)
	{
		dm = context.getResources().getDisplayMetrics();
		widthPixels = dm.widthPixels;
		heightPixels = dm.heightPixels;

		widthDp = (int) ((float) widthPixels / dm.density + 0.5f);
		heightDp = (int) ((float) heightPixels / dm.density + 0.5f);

		Log.d("debug", "widthPixels=" + dm.widthPixels + ",heightPixels=" + dm.heightPixels);
	}

	public static int getScreenWidthPixels()
	{
		return widthPixels;
	}

	public static int getScreenHeightPixels()
	{
		return heightPixels;
	}

	public static int getScreenWidthDp()
	{
		return widthDp;
	}

	public static int getScreenHeightDp()
	{
		return heightDp;
	}

	public static int dp2Pixels(float dp)
	{
		return (int) ((dp - 0.5f) * dm.density);
	}

	public static int pixel2Dp(float pixel)
	{
		return (int) (pixel / dm.density + 0.5f);
	}
}
