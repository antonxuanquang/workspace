package com.sean.igo.common.http;

import com.alibaba.fastjson.JSONObject;

/**
 * 请求结果
 * @author sean
 */
public class Result
{
	public String state;
	public String msg;
	public int code = 0;
	public JSONObject data;

	public boolean isSuccess()
	{
		return state.equals("SUCCESS");
	}
}
