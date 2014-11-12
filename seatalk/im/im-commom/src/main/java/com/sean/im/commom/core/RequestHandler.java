package com.sean.im.commom.core;

import com.alibaba.fastjson.JSONObject;

/**
 * 请求回调接口
 * @author Sean
 *
 */
public interface RequestHandler
{
	/**
	 * 请求回调
	 * @param data						服务端返回的数据包
	 */
	public void callback(JSONObject data);
}
