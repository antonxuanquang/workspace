package com.sean.shop.search.bean;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TrackingIndexWriter;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.sean.common.ioc.BeanConfig;
import com.sean.common.ioc.ResourceConfig;
import com.sean.common.util.TimeUtil;
import com.sean.config.core.Config;
import com.sean.log.core.LogFactory;
import com.sean.persist.core.Dao;
import com.sean.persist.core.PageData;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.enums.OrderEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Order;
import com.sean.service.core.BusinessException;
import com.sean.shop.context.core.Good;
import com.sean.shop.good.api.GoodBean;
import com.sean.shop.search.api.SearchBean;
import com.sean.shop.search.constant.L;
import com.sean.shop.search.core.BoostQuery;
import com.sean.shop.search.core.Categorier;
import com.sean.shop.search.core.ICTCLASAnalyzer;
import com.sean.shop.search.core.IndexUtil;
import com.sean.shop.search.entity.BuildLogEntity;

@BeanConfig("商品搜索对象")
public class SearchBeanImpl implements SearchBean
{
	private static final Logger logger = LogFactory.getLogger(L.Search);

	private IndexWriter writer;
	private ControlledRealTimeReopenThread<IndexSearcher> CRTReopenThread;
	private SearcherManager searcherManager;

	// 是否初始化
	private volatile boolean inited = false;
	// 是否正在建立索引
	private volatile boolean buinding = false;
	// 是否正在改变索引目录
	private volatile boolean changing = false;

	@ResourceConfig
	private GoodBean goodBean;

	/**
	 * 初始化搜索引擎
	 * @throws IOException
	 * @throws BusinessException
	 */
	public void init() throws IOException, BusinessException
	{
		if (!inited)
		{
			logger.info("商品搜索引擎开始初始化");

			String idxDir = Config.getProperty("index.good.dir");
			writer = IndexUtil.createIndexWriter(idxDir);

			TrackingIndexWriter trackingIndexWriter = new TrackingIndexWriter(writer);
			searcherManager = new SearcherManager(writer, true, new SearcherFactory());

			CRTReopenThread = new ControlledRealTimeReopenThread<IndexSearcher>(trackingIndexWriter, searcherManager, 10, 3);
			CRTReopenThread.setDaemon(true);
			CRTReopenThread.setPriority(Thread.MIN_PRIORITY);
			CRTReopenThread.setName("商品搜索引擎索引后台刷新服务");
			CRTReopenThread.start();

			changing = false;
			buinding = false;
			inited = true;

			logger.info("商品搜索引擎初始化完成,后台刷新线程启动");
		}
		else
		{
			throw new BusinessException("索引已经初始化", 1);
		}
	}

	/**
	 * 销毁索引
	 * @throws IOException
	 */
	private void destory() throws IOException
	{
		inited = false;
		searcherManager.close();
		CRTReopenThread.close();
		writer.close();
	}

	/**
	 * 重建索引
	 * @throws BusinessException
	 */
	public void rebuild() throws BusinessException
	{
		if (!buinding)
		{
			Thread t = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					BuildLogEntity build = new BuildLogEntity();
					build.type = 1;
					build.startTime = TimeUtil.getYYYYMMDDHHMMSSTime();

					try
					{
						logger.info("搜索引擎开始重建索引");
						buinding = true;

						String path = "/tmp/index_tmp";
						File dir = new File(path);
						if (dir.exists() && dir.isDirectory())
						{
							FileUtils.deleteDirectory(dir);
							dir.mkdirs();
						}
						IndexWriter writer = IndexUtil.createIndexWriter(path);

						logger.info("开始读取数据库数据");
						long currId = 0;
						while (true)
						{
							List<Good> list = goodBean.getGoodList(currId, 1000);
							if (!list.isEmpty())
							{
								for (Good it : list)
								{
									writer.addDocument(good2Doc(it));
								}
								currId = list.get(list.size() - 1).goodId;
							}
							if (list.size() < 1000)
							{
								break;
							}
						}
						writer.commit();
						writer.close();

						logger.info("数据索引完成, 开始切换索引路径");

						changing = true;
						// 销毁索引上下文
						destory();

						// 删除旧索引
						File stale = new File(Config.getProperty("index.good.dir"));
						if (stale.exists())
						{
							FileUtils.deleteDirectory(stale);
						}
						// 移动新索引到目标路径
						FileUtils.moveDirectory(new File(path), stale);
						changing = false;

						// 重新初始化索引上下文
						init();

						logger.info("搜索引擎重建完成");
					}
					catch (Exception e)
					{
						logger.error("重建索引错误:" + e.getMessage(), e);

						build.endTime = TimeUtil.getYYYYMMDDHHMMSSTime();
						build.buildResult = 0;
						Dao.persist(BuildLogEntity.class, build);
						return;
					}
					finally
					{
						buinding = false;
					}

					build.endTime = TimeUtil.getYYYYMMDDHHMMSSTime();
					build.buildResult = 1;
					Dao.persist(BuildLogEntity.class, build);
				}
			});
			t.setPriority(Thread.MIN_PRIORITY);
			t.start();
		}
		else
		{
			throw new BusinessException("索引已经在重建", 1);
		}
	}

	/**
	 * 计算商品分类
	 * @param force				是否强制计算, 否则只计算未分类的商品
	 * @param block				是否阻塞
	 * @throws BusinessException
	 */
	public void category(boolean force, boolean block) throws BusinessException
	{
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				BuildLogEntity build = new BuildLogEntity();
				build.type = 3;
				build.startTime = TimeUtil.getYYYYMMDDHHMMSSTime();

				try
				{
					logger.info("开始计算商品分类");
					long currId = 0;
					while (true)
					{
						List<Good> list = goodBean.getGoodList(currId, 1000);
						if (!list.isEmpty())
						{
							for (Good it : list)
							{
								if (force)
								{
									long categoryId = Categorier.category(it.goodName);
									goodBean.updateGoodCategory(it.goodId, categoryId);
								}
								else
								{
									if (it.categoryId <= 0)
									{
										long categoryId = Categorier.category(it.goodName);
										goodBean.updateGoodCategory(it.goodId, categoryId);
									}
								}
							}
							currId = list.get(list.size() - 1).goodId;
						}
						if (list.size() < 1000)
						{
							break;
						}
					}

					logger.info("商品分类计算完成");
				}
				catch (Exception e)
				{
					logger.error("商品分类计算错误:" + e.getMessage(), e);

					build.endTime = TimeUtil.getYYYYMMDDHHMMSSTime();
					build.buildResult = 0;
					Dao.persist(BuildLogEntity.class, build);
					return;
				}

				build.endTime = TimeUtil.getYYYYMMDDHHMMSSTime();
				build.buildResult = 1;
				Dao.persist(BuildLogEntity.class, build);
			}
		});
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();

		if (block)
		{
			try
			{
				t.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 分词
	 * @param analyzer
	 * @return
	 * @throws IOException 
	 */
	private List<String> token(Analyzer analyzer, String txt) throws IOException
	{
		if (analyzer instanceof IKAnalyzer)
		{
			List<String> tokens = new LinkedList<>();
			StringReader reader = new StringReader(txt.toLowerCase());
			IKSegmenter ik = new IKSegmenter(reader, true);
			Lexeme lexeme = null;
			while ((lexeme = ik.next()) != null)
			{
				tokens.add(lexeme.getLexemeText());
			}
			return tokens;
		}
		else
		{
			ICTCLASAnalyzer anaylzer = (ICTCLASAnalyzer) writer.getAnalyzer();
			return anaylzer.token(txt.toLowerCase());
		}
	}

	@Override
	public List<Object> search(com.sean.shop.search.api.Query query) throws BusinessException, IOException
	{
		check();

		IndexSearcher searcher = null;
		try
		{
			searcher = searcherManager.acquire();
			int start = (query.pageNo - 1) * query.pageSize;
			Document doc = null;
			List<Object> data = new LinkedList<Object>();

			BooleanQuery mulQuery = new BooleanQuery();

			// 商品名称全文搜索
			if (query.keyword != null && !query.keyword.isEmpty())
			{
				List<String> tokens = this.token(writer.getAnalyzer(), query.keyword.toLowerCase());
				for (String it : tokens)
				{
					mulQuery.add(new TermQuery(new Term("keyword", it)), Occur.MUST);
				}
			}

			// 渠道搜索
			if (query.channel != -1)
			{
				mulQuery.add(new TermQuery(new Term("channel", String.valueOf(query.channel))), Occur.MUST);
			}

			// 分类
			if (query.categoryId != -1)
			{
				mulQuery.add(new TermQuery(new Term("categoryId", String.valueOf(query.categoryId))), Occur.MUST);
			}

			// 价格
			float priceStart = query.priceStart == -1 ? 0 : query.priceStart;
			float priceEnd = query.priceEnd == -1 ? Integer.MAX_VALUE : query.priceEnd;
			if (priceStart != 0 && priceEnd != Integer.MAX_VALUE)
			{
				mulQuery.add(NumericRangeQuery.newFloatRange("price", priceStart, priceEnd, true, false), Occur.MUST);
			}

			// 状态
			mulQuery.add(new TermQuery(new Term("status", String.valueOf(query.status))), Occur.MUST);

			logger.debug("搜索语句:" + mulQuery);

			Sort sort = null;
			// 创建加权因子查询
			switch (query.ranking)
			{
			// 综合排序
			case 1:
				sort = null;
				break;
			// 销量
			case 2:
				sort = new Sort(new SortField("saleCount", Type.INT, true));
				break;
			// 价格
			case 3:
				sort = new Sort(new SortField("price", Type.FLOAT, false));
				break;
			// 人气
			case 4:
				sort = new Sort(new SortField("showTimes", Type.INT, true));
				break;

			default:
				break;
			}

			TopDocs td = null;
			// 使用综合排名
			if (sort == null)
			{
				// 创建加权因子查询
				Query boostQuery = new BoostQuery(mulQuery);
				td = searcher.search(boostQuery, query.pageSize * query.pageNo);
			}
			else
			{
				td = searcher.search(mulQuery, query.pageSize * query.pageNo, sort);
			}
			ScoreDoc[] sd = td.scoreDocs;
			logger.debug("搜索结果:" + Arrays.toString(sd));

			for (int i = start; i < sd.length; i++)
			{
				doc = searcher.doc(sd[i].doc);
				data.add(Long.parseLong(doc.get("goodId")));
			}

			query.totalrecord = td.totalHits;
			return data;
		}
		finally
		{
			if (searcher != null)
			{
				searcherManager.release(searcher);
			}
		}
	}

	@Override
	public void addGood(Good good) throws IOException, BusinessException
	{
		check();
		Document doc = good2Doc(good);
		writer.addDocument(doc);
		writer.commit();
	}

	private Document good2Doc(Good good)
	{
		Document doc = new Document();
		doc.add(new LongField("goodId", good.goodId, Store.YES));

		// 计算加权因子 = (log(max(10,月销售量)) + sqrt(log(max(10, 展示量)))) * docBoost
		float saleScore = (float) Math.log(Math.max(10, good.saleCount));
		float showScore = (float) Math.sqrt(Math.log(Math.max(10, good.showTimes)));
		float boost = (saleScore + showScore) * good.boost;
		doc.add(new FloatField("boost", boost, Store.YES));

		doc.add(new TextField("keyword", good.keyword.toLowerCase(), Store.NO));
		doc.add(new StringField("channel", String.valueOf(good.channel), Store.NO));
		doc.add(new StringField("categoryId", String.valueOf(good.categoryId), Store.NO));
		doc.add(new StringField("status", String.valueOf(good.status), Store.NO));
		doc.add(new FloatField("price", good.price, Store.YES));
		doc.add(new IntField("saleCount", good.saleCount, Store.YES));
		doc.add(new IntField("showTimes", (int) good.showTimes, Store.YES));

		return doc;
	}

	@Override
	public void updateGood(Good good) throws IOException, BusinessException
	{
		check();

		// 删除
		writer.deleteDocuments(new Term("goodId", String.valueOf(good.goodId)));
		// 新增
		this.addGood(good);
	}

	@Override
	public void deleteGood(long goodId) throws IOException, BusinessException
	{
		check();

		// 删除
		writer.deleteDocuments(new Term("goodId", String.valueOf(goodId)));
		writer.commit();
	}

	/**
	 * 读取索引建立日志
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageData<BuildLogEntity> getBuildList(int pageNo, int pageSize)
	{
		return Dao.getListByPage(BuildLogEntity.class, new Condition("buildId", ConditionEnum.Not_Equal, 0), new Order("buildId", OrderEnum.Desc),
				pageNo, pageSize, -1);
	}

	/**
	 * 删除索引建立日志
	 * @param buildId
	 */
	public void deleteBuild(long buildId)
	{
		Dao.remove(BuildLogEntity.class, buildId);
	}

	private void check() throws BusinessException
	{
		if (!inited)
		{
			throw new BusinessException("索引没有初始化", 1);
		}

		if (changing)
		{
			throw new BusinessException("索引正在切换", 2);
		}
	}
}
