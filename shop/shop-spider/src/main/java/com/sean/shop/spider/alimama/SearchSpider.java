package com.sean.shop.spider.alimama;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import com.alibaba.fastjson.JSON;
import com.sean.common.util.JsonUtil;
import com.sean.common.util.TimeUtil;
import com.sean.config.core.Config;
import com.sean.log.core.LogFactory;
import com.sean.persist.core.Dao;
import com.sean.persist.core.PersistLaucher;
import com.sean.persist.ext.Value;
import com.sean.shop.good.entity.GoodEntity;
import com.sean.shop.spider.alimama.Try.TryMethod;

public class SearchSpider
{
	private static Logger logger = LogFactory.getLogger(SearchSpider.class.getSimpleName());

	public static void main(String[] args) throws Exception
	{
		Config.readConfiguration();
		PersistLaucher.getInstance().launch(new String[] { "com.sean.shop" });

		SearchSpider main = new SearchSpider();

		// 天天特价
		Thread tiantiantejia = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				List<String> categorys = loadQuery("/expensive.txt");

				// 计算状态
				Status status = new Status();
				status.channel = 1;
				status.pageNo = 0;
				status.queryIndex = 0;

				for (int i = status.queryIndex; i < categorys.size(); i++)
				{
					status.queryIndex = i;
					status.query = categorys.get(i);

					while (true)
					{
						try
						{
							// 销量倒序
							String url1 = "http://pub.alimama.com/myunion.htm?spm=a219t.7473494.1998155389.3.EYOwGA#!/promo/self/taokequn_detail?spm=a219t.7473494.1998155389.3.EYOwGA&groupId=1375950175666&toPage=1&sort=_totalnum";

							if (main.brush(1, url1, status))
							{
								status.pageNo = 0;
								System.gc();
								break;
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
							System.gc();
							try
							{
								Thread.sleep(1000);
							}
							catch (InterruptedException e1)
							{
								e1.printStackTrace();
							}
						}
					}
				}
				System.out.println("包邮爬虫结束");
			}
		});
		tiantiantejia.setPriority(Thread.MIN_PRIORITY);
		tiantiantejia.start();
		System.out.println("包邮爬虫启动");

		// 精品服饰
		Thread jingpinfushi = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// 计算状态
				Status status = new Status();
				status.channel = 2;
				status.pageNo = 0;
				status.queryIndex = 0;

				List<String> categorys = loadQuery("/closth.txt");
				for (int i = status.queryIndex; i < categorys.size(); i++)
				{
					status.queryIndex = i;
					status.query = categorys.get(i);

					while (true)
					{
						try
						{
							String url1 = "http://pub.alimama.com/myunion.htm?spm=a219t.7473494.1998155389.3.EYOwGA#!/promo/self/taokequn_detail?spm=a219t.7473494.1998155389.3.EYOwGA&groupId=1375950030867&toPage=1&sort=_totalnum";

							if (main.brush(2, url1, status))
							{
								status.pageNo = 0;
								System.gc();
								break;
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
							System.gc();
							try
							{
								Thread.sleep(1000);
							}
							catch (InterruptedException e1)
							{
								e1.printStackTrace();
							}
						}
					}
				}
				System.out.println("精品服饰爬虫结束");
			}
		});
		jingpinfushi.setPriority(Thread.MIN_PRIORITY);
		jingpinfushi.start();
		System.out.println("精品服饰爬虫启动");

		System.in.read();
	}

	public boolean brush(int channel, String url, Status status) throws Exception
	{
		FirefoxDriver driver = new FirefoxDriver();
		try
		{
			driver.get("https://login.taobao.com/member/login.jhtml?style=minisimple&from=alimama&redirectURL=http%3A%2F%2Flogin.taobao.com%2Fmember%2Ftaobaoke%2Flogin.htm%3Fis_login%3d1&full_redirect=true&disableQuickLogin=true");
			Thread.sleep(1000);

			driver.executeScript("document.getElementById('TPL_username_1').value='sean_zwx';", new Object[] {});
			driver.executeScript("document.getElementById('TPL_password_1').value='qqgameboys123';", new Object[] {});

			driver.findElementById("J_SubmitStatic").click();
			Thread.sleep(3000);

			driver.get(url);
			Thread.sleep(1000);

//			Try.doTry(new TryMethod()
//			{
//				@Override
//				public void invoke()
//				{
//					driver.findElementByLinkText("隐藏").click();
//				}
//			}, 10);
//			Thread.sleep(500);

			long initOffset = status.offset;

			// 开始搜索
			WebElement input = driver.findElementByXPath("//*[@id=\"J_table_list\"]/div[1]/div/div/div/div/input");
			input.clear();
			input.sendKeys(status.query);
			driver.executeScript("arguments[0].value='" + status.query + "';", input);
			driver.findElementByXPath("//*[@id=\"J_table_list\"]/div[1]/div/div/div/a").click();
			Thread.sleep(2000);

			// 跳到指定页面
			goToPage(driver, status.pageNo);

			while (true)
			{
				try
				{
					logger.info("开始爬第" + status.pageNo + "页");
					status.offset = 0;

					// 开始爬
					WebElement table = driver.findElementByXPath("//*[@id=\"J_data_table\"]/tbody");
					List<WebElement> trs = table.findElements(By.tagName("tr"));
					if (trs.size() <= 1)
					{
						return true;
					}

					for (int i = 0; i < trs.size(); i++)
					{
						WebElement it = trs.get(i);

						status.offset++;
						logger.info("当前状态为:" + status);

						if (status.currFirst && initOffset > 0)
						{
							initOffset--;
							continue;
						}
						if (status.currFirst && initOffset <= 0)
						{
							status.currFirst = false;
						}

						GoodEntity good = new GoodEntity();

						List<WebElement> td = it.findElements(By.tagName("td"));

						// 商品名称
						WebElement name = td.get(0);
						good.goodName = name.findElement(By.xpath("div/ul/li[1]/a")).getText();

						GoodEntity tmp = Dao.loadByColumn(GoodEntity.class, "goodName", good.goodName);
						if (tmp != null)
						{
							continue;
						}

						Actions builder = new Actions(driver);
						builder.moveToElement(it).perform();
						((JavascriptExecutor) driver).executeScript("arguments[0].className='hover';", it);
						Thread.sleep(100);

						// 商品价格
						WebElement price = td.get(1);
						good.price = Float.parseFloat(price.getText().replace(",", "").substring(1));

						// 商品图片url
						WebElement imageUrl = td.get(0);
						good.imageUrl = imageUrl.findElement(By.xpath("div/a/img")).getAttribute("src").replaceAll("_80x80.jpg", "");

						// 30天销售量
						WebElement saleCount = td.get(4);
						good.saleCount = Integer.parseInt(saleCount.getText().replace("件", ""));

						// 佣金比率
						WebElement rate = td.get(2);
						good.commissionRate = Float.parseFloat(rate.getText().replace("%", "")) / 100f;

						// 30天佣金总额
						WebElement commissionCount = td.get(5);
						good.commissionCount = Float.parseFloat(commissionCount.getText().replace(",", "").substring(1));

						// 其他默认值
						good.boost = 1.0f;
						good.categoryId = 0;
						good.channel = channel;
						good.createTime = TimeUtil.getYYYYMMDDHHMMSSTime();
						good.status = 1;

						// 获取推广链接
						WebElement tuiguang = td.get(6);
						WebElement tuiguang_a = tuiguang.findElement(By.xpath("p/a"));
						tuiguang_a.click();
						Thread.sleep(500);
						Try.doTry(new TryMethod()
						{
							@Override
							public void invoke()
							{
								WebElement div = driver.findElementByLinkText("确定");
								div.click();
							}
						}, 10);

						Thread.sleep(1000);

						Try.doTry(new TryMethod()
						{
							@Override
							public void invoke()
							{
								WebElement target = driver.findElementByTagName("textarea");
								good.goodUrl = target.getText();
							}
						}, 10);

						// 关闭弹窗
						driver.findElementByClassName("dialog-ext-close-x").click();
						Thread.sleep(1000);

						// 获取索引关键字
						String itemurl = name.findElement(By.xpath("div/ul/li[1]/a")).getAttribute("href");
						good.keyword = good.goodName + HtmlSpider.getContent(itemurl);
						if (good.keyword.length() > 2048)
						{
							good.keyword = good.keyword.substring(0, 2048);
						}

						// 添加商品
						GoodEntity cache = Dao.loadByColumn(GoodEntity.class, "goodName", good.goodName);
						if (cache == null)
						{
							Dao.persist(GoodEntity.class, good);
							System.out.println(JsonUtil.formatJson(JSON.toJSONString(good.getValues()), "    "));
						}
						// 修改商品
						else
						{
							List<Value> vals = new LinkedList<>();
							vals.add(new Value("price", good.price));
							vals.add(new Value("imageUrl", good.imageUrl));
							vals.add(new Value("goodUrl", good.goodUrl));
							vals.add(new Value("keyword", good.keyword));
							vals.add(new Value("saleCount", good.saleCount));
							vals.add(new Value("commissionRate", good.commissionRate));
							vals.add(new Value("commissionCount", good.commissionCount));
							Dao.update(GoodEntity.class, cache.goodId, vals);
						}
					}

					// 下一页
					if (driver.findElementsByClassName("page-next").isEmpty())
					{
						return true;
					}
					driver.findElementsByClassName("page-next").get(0).click();
					status.pageNo++;

					if (status.pageNo > 15)
					{
						return true;
					}
					Thread.sleep(1000);
				}
				catch (Exception e)
				{
					// 退出firefox
					driver.quit();
					throw e;
				}
			}
		}
		catch (Exception e)
		{
			// 退出firefox
			driver.quit();
			throw e;
		}
		finally
		{
			try
			{
				driver.quit();
			}
			catch (Exception e)
			{

			}
		}
	}

	private static void goToPage(FirefoxDriver driver, long pageNo) throws InterruptedException
	{
		// 跳转到指定页数
		WebElement page = driver.findElementByXPath("//*[@id=\"J_item_pagination\"]/div[2]/div[3]/input");
		page.clear();
		page.sendKeys(pageNo + "");
		// 开始跳转
		driver.findElementByXPath("//*[@id=\"J_item_pagination\"]/div[2]/div[3]/a").click();
		Thread.sleep(2000);
		driver.executeScript("document.body.scrollTop=0;");
		Thread.sleep(500);
	}

	private static List<String> loadQuery(String file)
	{
		try
		{
			InputStreamReader input = new InputStreamReader(SearchSpider.class.getResourceAsStream(file));
			BufferedReader reader = new BufferedReader(input);
			String line = null;

			Set<String> set = new HashSet<>();
			while ((line = reader.readLine()) != null)
			{
				String[] tmp = line.split(" ");
				for (String it : tmp)
				{
					if (!it.trim().isEmpty())
					{
						set.add(it.trim());
					}
				}
			}
			List<String> list = new ArrayList<>(set.size());
			list.addAll(set);
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
