package cn.domob.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 收藏工具
 * @author sean
 */
public class SqlLiteUtil
{
	private static class CacheDBOpneHelper extends SQLiteOpenHelper
	{
		public CacheDBOpneHelper(Context context)
		{
			super(context, "Cache.db", null, 10);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
		}
	}
	
	private static class FreqDBOpneHelper extends SQLiteOpenHelper
	{
		public FreqDBOpneHelper(Context context)
		{
			super(context, "Freq.db", null, 10);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
		}
	}

	private CacheDBOpneHelper CacheDBOpneHelper;
	private FreqDBOpneHelper FreqDBOpneHelper;

	public SqlLiteUtil(Context context)
	{
		CacheDBOpneHelper = new CacheDBOpneHelper(context);
		FreqDBOpneHelper = new FreqDBOpneHelper(context);
	}

	/**
	 * 删，通过id删除数据
	 */
	public void clear()
	{
		SQLiteDatabase db = CacheDBOpneHelper.getWritableDatabase();
		try
		{
			db.delete("resourceGroup", "1=1", new String[] {});
		}
		catch(Exception e)
		{	
		}
		try
		{
			db.delete("resource", "1=1", new String[] {});
		}
		catch(Exception e)
		{	
		}
		db.close();
		
		SQLiteDatabase db1 = FreqDBOpneHelper.getWritableDatabase();
		try
		{
			db1.delete("freq", "1=1", new String[] {});	
		}
		catch(Exception e)
		{	
		}
		db1.close();
	}
}
