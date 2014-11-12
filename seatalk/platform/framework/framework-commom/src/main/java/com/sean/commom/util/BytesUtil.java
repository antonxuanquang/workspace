package com.sean.commom.util;

import java.io.UnsupportedEncodingException;

/**
 * 字节工具
 * @author sean
 */
public class BytesUtil
{
	public static byte[] toBytes(long number)
	{
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++)
		{
			b[i] = new Long(temp & 0xff).byteValue();
			temp = temp >> 8;
		}
		return b;
	}

	public static long toLong(byte[] b)
	{
		long s = 0;
		long s0 = b[0] & 0xff;
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff;

		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

	public static byte[] toBytes(int number)
	{
		int temp = number;
		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++)
		{
			b[i] = new Integer(temp & 0xff).byteValue();
			temp = temp >> 8;
		}
		return b;
	}

	public static int toInt(byte[] b)
	{
		int s = 0;
		int s0 = b[0] & 0xff;
		int s1 = b[1] & 0xff;
		int s2 = b[2] & 0xff;
		int s3 = b[3] & 0xff;
		s3 <<= 24;
		s2 <<= 16;
		s1 <<= 8;
		s = s0 | s1 | s2 | s3;
		return s;
	}

	public static byte[] toBytes(short number)
	{
		int temp = number;
		byte[] b = new byte[2];
		for (int i = 0; i < b.length; i++)
		{
			b[i] = new Integer(temp & 0xff).byteValue();
			temp = temp >> 8;
		}
		return b;
	}

	public static short toShort(byte[] b)
	{
		short s = 0;
		short s0 = (short) (b[0] & 0xff);
		short s1 = (short) (b[1] & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}

	public static byte[] toBytes(float f)
	{
		int fbit = Float.floatToIntBits(f);
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++)
		{
			b[i] = (byte) (fbit >> (24 - i * 8));
		}
		int len = b.length;
		byte[] dest = new byte[len];
		System.arraycopy(b, 0, dest, 0, len);
		byte temp;
		for (int i = 0; i < len / 2; ++i)
		{
			temp = dest[i];
			dest[i] = dest[len - i - 1];
			dest[len - i - 1] = temp;
		}
		return dest;
	}

	public static float toFloat(byte[] b)
	{
		int l;
		l = b[0];
		l &= 0xff;
		l |= ((long) b[1] << 8);
		l &= 0xffff;
		l |= ((long) b[2] << 16);
		l &= 0xffffff;
		l |= ((long) b[3] << 24);
		return Float.intBitsToFloat(l);
	}

	public static byte[] toBytes(String str)
	{
		return str.getBytes();
	}

	public static String toString(byte[] b)
	{
		try
		{
			return new String(b, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		long curr = System.currentTimeMillis();
		System.out.println(toInt(toBytes(20)));
		System.out.println(toLong(toBytes(20L)));
		System.out.println(toFloat(toBytes(20.3f)));
		System.out.println(toString(toBytes("Sean")));
		
		System.out.println(System.currentTimeMillis() - curr);
	}
}
