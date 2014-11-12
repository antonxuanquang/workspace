package com.sean.shop.spider.alimama;

public class Try
{
	public static void doTry(TryMethod m, int times) throws TryException
	{
		int time = 0;
		while (true)
		{
			try
			{
				m.invoke();
				break;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				time++;
				if (time >= times)
				{
					throw new TryException();
				}
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}

	public interface TryMethod
	{
		public void invoke();
	}
}
