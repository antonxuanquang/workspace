package com.sean.commom.translate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 翻译网络机器人
 * @author sean
 *
 */
public class BaiduTranslator
{
	private static String URL = "http://fanyi.baidu.com/v2transapi?to=${to}&query=${query}";

	public static void main(String[] args)
	{
		System.out.println(unicodeToString("what are you doing"));
		HttpURLConnection urlConnection = null;
		try
		{
			String urlTxt = URL.replace("${to}", "zh");
			urlTxt = urlTxt.replace("${query}", "what are you doing");
			URL url = new URL(urlTxt);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setConnectTimeout(5000);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");

			InputStream in = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			StringBuffer temp = new StringBuffer(1024);
			String line = bufferedReader.readLine();
			while (line != null)
			{
				temp.append(line).append("/r/n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			String newStr = temp.toString();
			System.out.println(newStr);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	public static String unicodeToString(String str)
	{
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find())
		{
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}

	/**
	 * 翻译接口
	 * @param text					翻译文本
	 * @param source				源语言
	 * @param target				目标语言
	 * @return
	 */
	public static String translate(String text, Language source, Language target)
	{
		if (source == target)
		{
			return text;
		}
		try
		{
			// 处理参数
			String urlTxt = URL.replace("{text}", URLEncoder.encode(text, "utf-8"));
			urlTxt = urlTxt.replace("{source}", source.toString());
			urlTxt = urlTxt.replace("{target}", target.toString());
			return doTranslate(text, urlTxt);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return text;
		}
	}

	/**
	 * 翻译接口
	 * @param text					翻译文本
	 * @param source				源语言标识符
	 * @param target				目标语言标识符
	 * @return
	 */
	public static String translate(String text, String source, String target)
	{
		if (source.equals(target))
		{
			return text;
		}
		try
		{
			// 处理参数
			String urlTxt = URL.replace("{text}", URLEncoder.encode(text, "utf-8"));
			urlTxt = urlTxt.replace("{source}", source);
			urlTxt = urlTxt.replace("{target}", target);
			return doTranslate(text, urlTxt);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return text;
		}
	}

	/**
	 * 翻译接口（自动检测语言）
	 * @param text					翻译文本			
	 * @param target				目标语言标识符
	 * @return
	 */
	public static String translate(String text, String target)
	{
		try
		{
			// 处理参数
			String urlTxt = URL.replace("{text}", URLEncoder.encode(text, "utf-8"));
			urlTxt = urlTxt.replace("{source}", "auto");
			urlTxt = urlTxt.replace("{target}", target);
			return doTranslate(text, urlTxt);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return text;
		}
	}

	/**
	 * 翻译接口
	 * @param text					翻译文本
	 * @param target				目标语言
	 * @return
	 */
	public static String translate(String text, Language target)
	{
		try
		{
			// 处理参数
			String urlTxt = URL.replace("{text}", URLEncoder.encode(text, "utf-8"));
			urlTxt = urlTxt.replace("{source}", "auto");
			urlTxt = urlTxt.replace("{target}", target.toString());
			return doTranslate(text, urlTxt);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return text;
		}
	}

	/**
	 * 翻译接口（自动检测语言）
	 * @param text					翻译文本			
	 * @param target				目标语言标识符
	 * @return
	 */
	public static String translate(String url, String text, String target, int type)
	{
		try
		{
			// 处理参数
			String urlTxt = url.replace("{text}", URLEncoder.encode(text, "utf-8"));
			urlTxt = urlTxt.replace("{source}", "auto");
			urlTxt = urlTxt.replace("{target}", target);
			return doTranslate(text, urlTxt);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return text;
		}
	}

	private static String doTranslate(String text, String urlTxt)
	{
		HttpURLConnection urlConnection = null;
		try
		{
			URL url = new URL(urlTxt);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setConnectTimeout(5000);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");

			InputStream in = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			StringBuffer temp = new StringBuffer(1024);
			String line = bufferedReader.readLine();
			while (line != null)
			{
				temp.append(line).append("/r/n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			String newStr = temp.toString();
			if (newStr.startsWith("TranslateApiException"))
			{
				return text;
			}
			return parse(temp.toString());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return text;
		}
		finally
		{
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	/**
	 * 解析Google返回的翻译字符串
	 * @param text
	 * @return
	 */
	private static String parse(String text)
	{
		try
		{
			String tmp = text.substring(3);
			tmp = tmp.split("\",\"")[0];
			tmp = tmp.substring(1, tmp.length());
			return tmp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return text;
		}
	}
}
