package com.sean.ipfilter.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

	public void start(int threads)
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
							boolean rs = check(tmp[0], Integer.parseInt(tmp[1]), 3000);
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

		HttpGet get = new HttpGet("http://seanzwx.github.io/test.html");
		get.setConfig(cfg);

		HttpResponse response = client.execute(get);
		String html = EntityUtils.toString(response.getEntity());
		html = html.replace("\n", "");
		html = html.replace("\t", "");
		html = html.replace(" ", "");
		if (html.equals("success"))
		{
			System.out.println(html);
			return true;
		}
		return false;
	}
}
