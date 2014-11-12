package com.sean.im.commom.enums;

import java.io.Serializable;

/**
 * 请求结果枚举
 * @author sean
 */
public enum RequestFlag implements Serializable
{
	/***
	 * 成功
	 */
	Success((byte) 1),

	/**
	 * 失败
	 */
	Failure((byte) 2),

	/**
	 * 非法数据
	 */
	Invalid((byte) 3),

	/**
	 * 无权访问
	 */
	Denied((byte) 4),

	/**
	 * 异常
	 */
	Exception((byte) 5);

	private byte code;

	RequestFlag(byte code)
	{
		this.code = code;
	}

	public byte getValue()
	{
		return this.code;
	}
}
