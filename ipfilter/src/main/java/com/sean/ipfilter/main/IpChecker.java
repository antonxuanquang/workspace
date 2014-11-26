package com.sean.ipfilter.main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class IpChecker
{
	private List<String> list;
	private int count, over = 0;
	private static final Pattern ptn = Pattern.compile("<title>(.*?)</title>");

	public IpChecker(File file)
	{
		try
		{
			list = FileUtils.readLines(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void setIP(boolean isvalid, String ip)
	{
		if (isvalid)
		{
			Main.valid.setText(Main.valid.getText() + ip + "\n");
		}
		else
		{
			Main.invalid.setText(Main.invalid.getText() + ip + "\n");
		}
	}

	public void start(int threads, final int timeout)
	{
		this.count = threads;
		over = 0;

		for (int i = 0; i < threads; i++)
		{
			Thread t = new Thread(new Runnable()
			{
				public void run()
				{
					while (list.size() > 0)
					{
						String line;
						synchronized (list)
						{
							line = list.remove(0);
						}

						String[] tmp = line.replaceAll("#.*", "").split(":");
						try
						{
							boolean rs = check(tmp[0], Integer.parseInt(tmp[1]), timeout);
							if (rs)
							{
								setIP(true, line);
								System.out.println("可用IP:" + line);
							}
							else
							{
								setIP(false, line);
							}
						}
						catch (Exception e)
						{
							setIP(false, line);
						}
					}

					synchronized (list)
					{
						over++;
					}

					if (over >= count)
					{
						JOptionPane.showMessageDialog(null, "测试完成");
					}
				}
			});
			t.start();
		}
	}

	public boolean check(String ip, int port, int timeout) throws ClientProtocolException, IOException
	{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpHost proxy = new HttpHost(ip, port);
		RequestConfig cfg = RequestConfig.custom().setProxy(proxy).setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
				.setRedirectsEnabled(true).setSocketTimeout(timeout).build();

		HttpGet get = new HttpGet("http://www.waps.cn/");
		get.setConfig(cfg);

		HttpResponse response = client.execute(get);
		String html = EntityUtils.toString(response.getEntity());
		html = html.replace("\n", "");
		html = html.replace("\t", "");

		Matcher m = ptn.matcher(html);
		if (m.find())
		{
			String title = m.group(1);
			if (title.contains("万普"))
			{
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws Exception
	{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet("http://www.baidu.com/");

		HttpResponse response = client.execute(get);
		String html = EntityUtils.toString(response.getEntity());
		System.out.println(html);
	}
}
