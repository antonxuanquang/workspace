package com.sean.igo.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 加密工具
 * @author sean
 */
public class SecurityUtil
{
	public static String md5(String string)
	{
		byte[] hash;
		try
		{
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash)
		{
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	/**
	 * des加密
	 *
	 * @param text
	 * @param key
	 * @return
	 */
	public static byte[] encryptByDes(String text, String key)
	{
		byte[] resultByte = {};
		try
		{
			byte[] keyByte = key.getBytes();
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(keyByte);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(keyByte);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			byte[] textByte = text.getBytes();
			resultByte = cipher.doFinal(textByte);
		}
		catch (Exception e)
		{
		}
		return resultByte;
	}

	/**
	 * des解密
	 * @param text
	 * @param key
	 * @return
	 */
	public static String dencryptByDes(String text, String key)
	{
		try
		{
			byte[] keyByte = key.getBytes();
			byte[] resultByte = hexStringToByte(text);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

			DESKeySpec desKeySpec = new DESKeySpec(keyByte);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(keyByte);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			resultByte = cipher.doFinal(resultByte);
			text = new String(resultByte);
			return text;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * byte转16进制字符
	 *
	 * @param textByte
	 * @return
	 */
	public static String byteToHexString(byte[] textByte)
	{
		StringBuilder hexString = new StringBuilder(32);
		int byteValue;
		for (byte bt : textByte)
		{
			byteValue = 0xFF & bt;
			if (byteValue < 16)
			{
				hexString.append('0').append(Integer.toHexString(byteValue));
			}
			else
			{
				hexString.append(Integer.toHexString(byteValue));
			}
		}
		return hexString.toString();
	}

	private static byte[] hexStringToByte(String hexString)
	{
		byte[] result = new byte[hexString.length() / 2];
		String str;
		int byteValue;
		for (int index = 0; index < hexString.length(); index = index + 2)
		{
			str = hexString.substring(index, index + 2);
			byteValue = Integer.parseInt(str, 16);
			result[index / 2] = (byte) byteValue;
		}
		return result;
	}
}
