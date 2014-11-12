package com.sean.http.core;

import com.alibaba.fastjson.JSONObject;

/**
 * 请求回调接口
 * @author Sean
 *
 */
public abstract class RequestHandler
{
	protected boolean cared = false;
	
	public RequestHandler()
	{
	}
	
	public RequestHandler(boolean cared)
	{
		this.cared = cared;
	}
	
	/**
	 * 请求回调
	 * @param data						服务端返回的数据包
	 * @param code						业务错误代码
	 * @param msg						错误信息
	 */
	public abstract void callback(JSONObject data, int code, String msg);
}
