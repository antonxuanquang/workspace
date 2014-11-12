package com.sean.commom.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 加密类
 * @author Sean
 */
public final class SecurityUtil
{
	/**
	 * 采用sean作为密钥
	 */
	private static final DESPlus desplus = new DESPlus("sean");

	/**
	 * MD5加密
	 * @param str				原字符串
	 * @return					加密后密文
	 */
	public static String md5(String str)
	{
		return DigestUtils.md5Hex(str);
	}

	/**
	 * b64加密
	 * @param plainText
	 * @return
	 */
	public static String encodeB64(String source)
	{
		byte[] b = source.getBytes();
		b = Base64.encodeBase64(b, true, true);
		String s = new String(b);
		return s;
	}

	/**
	 * b64解密
	 * @param encodeStr
	 * @return
	 */
	public static String decodeB64(String encodeStr)
	{
		byte[] b = encodeStr.getBytes();
		b = Base64.decodeBase64(encodeStr);
		String s = new String(b);
		return s;
	}

	/**
	 * des加密
	 * @param text
	 * @return				失败返回null
	 */
	public static String desEncrypt(String text)
	{
		try
		{
			return desplus.encrypt(text);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * des解密
	 * @param text
	 * @return				 失败返回null
	 */
	public static String desDecrypt(String text)
	{
		try
		{
			return desplus.decrypt(text);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}