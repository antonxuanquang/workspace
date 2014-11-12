package com.sean.im.commom.core;

import java.util.HashMap;
import java.util.Map;

/**
请求ID(long,8个字节)
接口标识字节长度(byte,1个字节)  -128～+127
接口标识(ActionName)
发送人(long,8个字节)
接收人(long,8个字节)
参数字节长度(int,4个字节)
请求参数
二进制参数长度(int,4个字节)
二进制参数(文件,图片等)
flag响应标记(byte,1个字节)
type数据包类型(byte,1个字节)   ---  1代表拉模式，2代表推模式
 */
public class ProtocolUtil
{
	public static byte[] toBytes(Protocol protocol)
	{
		byte[] act = BytesUtil.toBytes(protocol.action);
		byte[] param = BytesUtil.toBytes(getParam(protocol.param));
		int bindaryLen = 0;
		if (protocol.bindaryData != null)
		{
			bindaryLen = protocol.bindaryLen;
		}

		// 生成协议二进制
		byte[] b = new byte[8 + 1 + act.length + 8 + 8 + 4 + param.length + 4 + bindaryLen + 2];
		int index = 0;
		// 接口ID(long,8个字节)
		BytesUtil.toBytes(b, index, protocol.id);
		index += 8;
		// 接口标识字节长度(byte,1个字节)
		BytesUtil.toBytes(b, index, (byte) act.length);
		index += 1;
		// 接口标识(ActionName)
		BytesUtil.toBytes(b, index, act);
		index += act.length;
		// 发送人(long,8个字节)
		BytesUtil.toBytes(b, index, protocol.sender);
		index += 8;
		// 接收人(long,8个字节)
		BytesUtil.toBytes(b, index, protocol.receiver);
		index += 8;
		// 参数字节长度(int,4个字节)
		BytesUtil.toBytes(b, index, param.length);
		index += 4;
		// 请求参数
		BytesUtil.toBytes(b, index, param);
		index += param.length;
		// 二进制参数长度(int,4个字节)
		BytesUtil.toBytes(b, index, bindaryLen);
		index += 4;
		// 二进制参数(文件,图片等)
		if (protocol.bindaryData != null)
		{
			BytesUtil.toBytes(b, index, protocol.bindaryData, bindaryLen);
			index += bindaryLen;
		}
		// flag相应标记
		b[index] = protocol.flag;
		index++;
		// type数据包类型
		b[index] = protocol.type;
		return b;
	}

	public static Protocol fromBytes(byte[] b)
	{
		Protocol protocol = new Protocol();
		int index = 0;

		// 请求ID
		protocol.id = BytesUtil.toLong(b, index);
		index += 8;
		// 接口标识字节长度(byte,1个字节)
		protocol.actionLen = b[index];
		index += 1;
		// 接口标识(ActionName)
		protocol.action = BytesUtil.toString(b, index, protocol.actionLen);
		index += protocol.actionLen;
		// 发送人(long,8个字节)
		protocol.sender = BytesUtil.toLong(b, index);
		index += 8;
		// 接收人(long,8个字节)
		protocol.receiver = BytesUtil.toLong(b, index);
		index += 8;
		// 参数字节长度(int,4个字节)
		protocol.paramLen = BytesUtil.toInt(b, index);
		index += 4;
		// 请求参数
		if (protocol.paramLen > 0)
		{
			String param = BytesUtil.toString(b, index, protocol.paramLen);
			protocol.param = toParam(param);
			index += protocol.paramLen;
		}
		else
		{
			protocol.param = new HashMap<String, String>();
		}
		// 二进制参数长度(int,4个字节)
		protocol.bindaryLen = BytesUtil.toInt(b, index);
		index += 4;
		// 二进制参数(文件,图片等)
		if (protocol.bindaryLen > 0)
		{
			protocol.bindaryData = BytesUtil.subBytes(b, index, protocol.bindaryLen);
			index += protocol.bindaryLen;
		}
		// flag响应标记
		protocol.flag = b[index];
		index++;
		// type数据包类型
		protocol.type = b[index];
		return protocol;
	}

	private static String getParam(Map<String, String> param)
	{
		if (param == null)
		{
			return "";
		}
		StringBuilder sb = new StringBuilder(1024);
		String value = null;
		for (String key : param.keySet())
		{
			value = param.get(key);
			if (value != null)
			{
				value = value.replace("=", "");
				value = value.replace("&", "");
				sb.append(key).append('=').append(param.get(key)).append('&');
			}
		}
		if (sb.length() > 0)
		{
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	private static Map<String, String> toParam(String paramStr)
	{
		String[] kv = paramStr.split("&");
		Map<String, String> param = new HashMap<String, String>(kv.length);
		String[] tmp = null;
		for (int i = 0; i < kv.length; i++)
		{
			if (!kv[i].isEmpty())
			{
				tmp = kv[i].split("=");
				param.put(tmp[0], tmp[1]);
			}
		}
		return param;
	}
}
