package com.sean.shop.spider.alimama;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import com.alibaba.fastjson.JSON;
import com.sean.common.util.JsonUtil;
import com.sean.common.util.TimeUtil;
import com.sean.config.core.Config;
import com.sean.persist.core.Dao;
import com.sean.persist.core.PersistLaucher;
import com.sean.persist.ext.Value;
import com.sean.shop.good.entity.GoodEntity;
import com.sean.shop.spider.alimama.Try.TryMethod;

public class GoodSpider
{
	public static void main(String[] args) throws Exception
	{
		Config.readConfiguration();
		PersistLaucher.getInstance().launch(new String[] { "com.sean.shop" });

		GoodSpider main = new GoodSpider();

		// 天天特价
		Thread tiantiantejia = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// 计算状态
//				long count = (long) Dao.executeScalar(GoodEntity.class, "select count(1) from t_good where channel=1");
				Status status = new Status();
				status.channel = 1;
//				status.offset = count % 40;
//				status.pageNo = count / 40 + 1;
				status.offset = 0;
				status.pageNo = 85;
				
				while (true)
				{
					try
					{
						// 佣金倒序
						// String url1 =
						// "http://pub.alimama.com/myunion.htm?spm=a219t.7473494.1998155389.3.1ehyL9#!/promo/self/taokequn_detail?spm=a219t.7473494.1998155389.3.1ehyL9&groupId=1375950175666&toPage=1&sort=_totalfee";
						// 销量倒序
						String url1 = "http://pub.alimama.com/myunion.htm?spm=a219t.7473494.1998155389.3.RF31Ai#!/promo/self/taokequn_detail?spm=a219t.7473494.1998155389.3.RF31Ai&groupId=1375950175666&toPage=1&sort=_totalnum";

						main.brush(1, url1, status);
					}
					catch (Exception e)
					{
						e.printStackTrace();
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
//				long count = (long) Dao.executeScalar(GoodEntity.class, "select count(1) from t_good where channel=2");
				Status status = new Status();
				status.channel = 2;
//				status.offset = count % 40;
//				status.pageNo = count / 40 + 1;
				status.offset = 0;
				status.pageNo = 74;
				
				while (true)
				{
					try
					{
						// String url1 =
						// "http://pub.alimama.com/myunion.htm?spm=a219t.7473494.1998155389.3.HHQX0Q#!/promo/self/taokequn_detail?spm=a219t.7473494.1998155389.3.HHQX0Q&groupId=1375950030867&toPage=1&sort=_totalfee";
						String url1 = "http://pub.alimama.com/myunion.htm?spm=a219t.7473494.1998155389.3.RF31Ai#!/promo/self/taokequn_detail?spm=a219t.7473494.1998155389.3.RF31Ai&groupId=1375950030867&toPage=1&sort=_totalnum";

						main.brush(2, url1, status);
					}
					catch (Exception e)
					{
						e.printStackTrace();
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
		});
		jingpinfushi.setPriority(Thread.MIN_PRIORITY);
		jingpinfushi.start();
		System.out.println("精品服饰爬虫启动");

		System.in.read();
	}

	public void brush(int channel, String url, Status status) throws Exception
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
			Thread.sleep(3000);

			driver.findElementByLinkText("隐藏").click();
			Thread.sleep(500);

			goToPage(driver, status.pageNo);

			long initOffset = status.offset;

			while (true)
			{
				try
				{
					System.out.println("开始爬第" + status.pageNo + "页");
					status.offset = 0;

					// 开始爬
					WebElement table = driver.findElementByXPath("//*[@id=\"J_data_table\"]/tbody");
					List<WebElement> trs = table.findElements(By.tagName("tr"));
					for (int i = 0; i < trs.size(); i++)
					{
						WebElement it = trs.get(i);

						status.offset++;
						System.out.println("当前状态为:" + status);

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
						WebElement div = driver.findElementByLinkText("确定");
						div.click();
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
					driver.findElementsByClassName("page-next").get(0).click();
					status.pageNo++;
					System.gc();
					Thread.sleep(3000);
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
}
