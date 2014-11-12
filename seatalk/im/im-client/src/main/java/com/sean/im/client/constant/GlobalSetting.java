package com.sean.im.client.constant;

import java.io.Serializable;

/**
 * 全局配置信息
 * @author sean
 */
public class GlobalSetting implements Serializable
{
	private static final long serialVersionUID = 1L;
	// 帐号
	public String Username = "";
	// 是否记住密码
	public int Remember_Password = 0;
}
