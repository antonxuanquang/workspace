package com.sean.igo.common.http;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * 请求对象
 * 
 * @author sean
 */
public class Request
{
	private int server = 0;
	private String action;
	private List<NameValuePair> nvps;

	public Request(String action)
	{
		this(0, action);
	}

	public Request(int server, String action)
	{
		this.server = server;
		this.action = action;
		nvps = new LinkedList<NameValuePair>();
	}

	public void removeParameter(String key)
	{
		int len = nvps.size();
		for (int i = 0; i < len; i++)
		{
			if (nvps.get(i).getName().equals(key))
			{
				nvps.remove(i);
				i--;
				len--;
			}
		}
	}

	public void setParameter(String key, int val)
	{
		this.setParameter(key, new int[] { val });
	}

	public void setParameter(String key, int[] val)
	{
		for (int i = 0; i < val.length; i++)
		{
			this.nvps.add(new BasicNameValuePair(key, String.valueOf(val[i])));
		}
	}

	public void setParameter(String key, long val)
	{
		this.setParameter(key, new long[] { val });
	}

	public void setParameter(String key, long[] val)
	{
		for (int i = 0; i < val.length; i++)
		{
			this.nvps.add(new BasicNameValuePair(key, String.valueOf(val[i])));
		}
	}

	public void setParameter(String key, String val)
	{
		this.setParameter(key, new String[] { val });
	}

	public void setParameter(String key, String[] val)
	{
		for (int i = 0; i < val.length; i++)
		{
			this.nvps.add(new BasicNameValuePair(key, val[i]));
		}
	}

	public void setParameter(String key, float val)
	{
		this.setParameter(key, new float[] { val });
	}

	public void setParameter(String key, float[] val)
	{
		for (int i = 0; i < val.length; i++)
		{
			this.nvps.add(new BasicNameValuePair(key, String.valueOf(val[i])));
		}
	}

	public String getAction()
	{
		return action;
	}

	public int getServer()
	{
		return this.server;
	}

	public List<NameValuePair> getParameter()
	{
		return this.nvps;
	}

	@Override
	public String toString()
	{
		return "Request [action=" + action + ", nvps=" + nvps + "]";
	}

}
