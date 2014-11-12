package com.sean.im.commom.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 协议对象
 * @author sean
 */
public class Protocol
{
	public long id;
	public byte actionLen;
	public String action;
	public long sender = 0;
	public long receiver = 0;
	public int paramLen;
	public Map<String, String> param = new HashMap<String, String>();
	public Map<String, String[]> params = new HashMap<String, String[]>();
	public int bindaryLen = 0;
	public byte[] bindaryData;
	public byte flag;
	public byte type;

	public Protocol()
	{
	}

	public Protocol(String action)
	{
		this.action = action;
	}

	@Override
	public String toString()
	{
		return "Protocol [id=" + id + ", actionLen=" + actionLen + ", action=" + action + ", sender=" + sender + ", receiver=" + receiver
				+ ", paramLen=" + paramLen + ", param=" + param + ", bindaryLen=" + bindaryLen + ", bindaryData=" + Arrays.toString(bindaryData)
				+ ", flag=" + flag + ", type=" + type + "]";
	}

	/**
	 * 添加参数
	 * @param name
	 * @param val
	 */
	public void setParameter(String name, String val)
	{
		if (param == null)
		{
			param = new HashMap<String, String>();
		}
		param.put(name, val);
	}

	/**
	 * 添加参数
	 * @param name
	 * @param val
	 */
	public void setParameter(String name, long val)
	{
		if (param == null)
		{
			param = new HashMap<String, String>();
		}
		param.put(name, String.valueOf(val));
	}

	/**
	 * 添加参数
	 * @param name
	 * @param val
	 */
	public void setParameter(String name, int val)
	{
		if (param == null)
		{
			param = new HashMap<String, String>();
		}
		param.put(name, String.valueOf(val));
	}
	
	public void setParameters(String name, long[] vals)
	{
		String[] tmp = new String[vals.length];
		for (int i = 0; i < tmp.length; i++)
		{
			tmp[i] = String.valueOf(vals[i]);
		}
		params.put(name, tmp);
	}

	/**
	 * 清空所有参数
	 */
	public void clearParameter()
	{
		this.param.clear();
		this.params.clear();
	}

	public String getParameter(String name)
	{
		return param.get(name);
	}

	public int getIntParameter(String name)
	{
		return Integer.parseInt(param.get(name));
	}

	public long getLongParameter(String name)
	{
		return Long.parseLong(param.get(name));
	}
}
