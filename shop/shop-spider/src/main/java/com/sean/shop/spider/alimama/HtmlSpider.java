package com.sean.shop.spider.alimama;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HtmlSpider
{
	private static Pattern header = Pattern.compile("<head.*?>.*?</head>", Pattern.CASE_INSENSITIVE);
	private static Pattern header_title = Pattern.compile("<title.*?>(.*?)</title>", Pattern.CASE_INSENSITIVE);
	private static Pattern header_attr = Pattern.compile("=\"(.*?)\"", Pattern.CASE_INSENSITIVE);

	private static Pattern body = Pattern.compile("<.*?>(.*?)</.*?>", Pattern.CASE_INSENSITIVE);

	public static void main(String[] args) throws ClientProtocolException, IOException
	{
		String url = "http://detail.tmall.com/item.htm?id=19400746342&spm=0.0.0.0.dDOx9L&mt=";
//		String url = "http://s.click.taobao.com/t?e=m%3D2%26s%3DK%2FeGB2tBvAocQipKwQzePOeEDrYVVa64K7Vc7tFgwiFRAdhuF14FMdS%2BOY9qBINGxq3IhSJN6GQ98dNItt5D1NJ%2B2rT9mhHom7NRzLkJct1ZMMSKz2P7eaBRHSMq1BTyoN9VT49nZyyPvZXA1Aw7XCfW3gN274kBgWlBWwGxO1o%3D";
		// String url = "http://detail.tmall.com/item.htm?id=40584762511";

		System.out.println(HtmlSpider.getContent(url));
	}
	
	public static String getContent(String url) throws ClientProtocolException, IOException
	{
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpGet get = new HttpGet(url);
		Builder builder = RequestConfig.custom();
		builder.setCircularRedirectsAllowed(true);
		get.setConfig(builder.build());
		
		HttpResponse response = client.execute(get);
		String html = EntityUtils.toString(response.getEntity());
		html = html.replace("\n", "");
		html = html.replace("\r", "");
		html = html.replace("\t", "");
		html = html.replaceAll("<script.*?/script>", "");
		html = html.replaceAll("<style.*?/style>", "");
		html = html.replaceAll("<link.*?>", "");
		html = html.replaceAll("<!--.*?-->", "");
		html = html.replaceAll("&.*?;", "");

		StringBuilder rs = new StringBuilder();
		// 匹配header
		Matcher m = header.matcher(html);
		while (m.find())
		{
			String header = m.group();
			Matcher m1 = header_title.matcher(header);
			while (m1.find())
			{
				// System.out.println(m1.group(1));
				rs.append(m1.group(1)).append(" ");
			}

			Matcher m2 = header_attr.matcher(header);
			while (m2.find())
			{
				// System.out.println(m2.group(1));
				rs.append(m2.group(1)).append(" ");
			}
		}

		// 匹配body
		Matcher m1 = body.matcher(html);
		while (m1.find())
		{
			String tmp = m1.group(1).replace(" ", "");
			if (tmp.contains("<") || tmp.length() <= 1)
			{
				continue;
			}
			else
			{
				// System.out.println(tmp);
				rs.append(tmp).append(" ");
			}
		}
		client.close();
		return rs.toString();
	}
}
