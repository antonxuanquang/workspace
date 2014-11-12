package com.sean.shop.search.bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import com.sean.common.ioc.BeanConfig;
import com.sean.common.util.TimeUtil;
import com.sean.config.core.Config;
import com.sean.log.core.LogFactory;
import com.sean.persist.core.Dao;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.enums.OrderEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Order;
import com.sean.service.core.BusinessException;
import com.sean.shop.search.api.SearchKeyBean;
import com.sean.shop.search.core.ICTCLASAnalyzer;
import com.sean.shop.search.core.IndexUtil;
import com.sean.shop.search.entity.BuildLogEntity;
import com.sean.shop.search.entity.HotwordEntity;
import com.sean.shop.search.entity.SearchLogEntity;

@BeanConfig("搜索词搜索对象")
public class SearchKeyBeanImpl implements SearchKeyBean
{
	private static final Logger logger = LogFactory.getLogger(SearchKeyBeanImpl.class);

	private IndexWriter writer;
	private SearcherManager searcherManager;

	// 是否初始化
	private volatile boolean inited = false;
	// 是否正在建立索引
	private volatile boolean buinding = false;
	// 是否正在改变索引目录
	private volatile boolean changing = false;

	/**
	 * 初始化搜索引擎
	 * @throws IOException
	 * @throws BusinessException
	 */
	public void init() throws IOException, BusinessException
	{
		if (!inited)
		{
			logger.info("搜索引擎开始初始化");

			String idxDir = Config.getProperty("index.searchkey.dir");
			writer = IndexUtil.createIndexWriter(idxDir);
			searcherManager = new SearcherManager(writer, true, new SearcherFactory());

			changing = false;
			buinding = false;
			inited = true;

			logger.info("搜索引擎初始化完成");
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
					build.type = 2;
					build.startTime = TimeUtil.getYYYYMMDDHHMMSSTime();

					try
					{
						logger.info("搜索引擎开始重建索引");
						buinding = true;

						String path = "/tmp/index_searchkey_tmp";
						File dir = new File(path);
						if (dir.exists() && dir.isDirectory())
						{
							FileUtils.deleteDirectory(dir);
							dir.mkdirs();
						}
						IndexWriter writer = IndexUtil.createIndexWriter(path);

						logger.info("开始读取数据库数据");
						List<String> list = getSearchKeyList();
						for (String it : list)
						{
							Document doc = new Document();
							doc.add(new TextField("key", it.toLowerCase(), Store.YES));
							writer.addDocument(doc);
						}
						writer.commit();
						writer.close();

						logger.info("数据索引完成, 开始切换索引路径");

						changing = true;
						// 销毁索引上下文
						destory();

						// 删除旧索引
						File stale = new File(Config.getProperty("index.searchkey.dir"));
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

	@Override
	public List<String> search(String key) throws BusinessException, IOException
	{
		check();

		IndexSearcher searcher = null;
		try
		{
			searcher = searcherManager.acquire();
			Document doc = null;
			List<String> data = new LinkedList<String>();

			BooleanQuery mulQuery = new BooleanQuery();

			ICTCLASAnalyzer anaylzer = (ICTCLASAnalyzer) writer.getAnalyzer();
			List<String> tokens = anaylzer.token(key.toLowerCase());
			for (String it : tokens)
			{
				mulQuery.add(new TermQuery(new Term("key", it)), Occur.MUST);
			}

			logger.debug("搜索语句:" + mulQuery);

			TopDocs td = searcher.search(mulQuery, 10);
			ScoreDoc[] sd = td.scoreDocs;
			logger.debug("搜索结果:" + Arrays.toString(sd));

			for (int i = 0; i < sd.length; i++)
			{
				doc = searcher.doc(sd[i].doc);
				data.add(doc.get("key"));
			}
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

	/**
	 * 读取搜索词语
	 * @return
	 */
	public List<String> getSearchKeyList()
	{
		String sql = "select distinct searchKey from t_search_log";
		List<Map<String, Object>> list = Dao.executeList(SearchLogEntity.class, sql);

		List<String> data = new ArrayList<>(list.size());
		for (Map<String, Object> it : list)
		{
			data.add(it.get("searchKey").toString());
		}
		return data;
	}

	/**
	 * 读取搜索热词
	 * @return
	 */
	public List<HotwordEntity> getHotword()
	{
		List<Condition> conds = new ArrayList<>(1);
		conds.add(new Condition("hotwordId", ConditionEnum.Not_Equal, 0));

		List<Order> orders = new ArrayList<>(2);
		orders.add(new Order("date", OrderEnum.Desc));
		orders.add(new Order("rank", OrderEnum.Asc));

		return Dao.getListByPage(HotwordEntity.class, conds, orders, 1, 10, 1).getDatas();
	}

	/**
	 * 计算搜索热词
	 */
	public void caculateHotword()
	{
		logger.info("开始计算搜索热词");

		String sql = "select searchKey, count(1) times from t_search_log group by searchKey order by times desc limit 100";
		List<Map<String, Object>> table = Dao.executeList(SearchLogEntity.class, sql);

		int date = TimeUtil.getYYYYMMDD();
		int rank = 1;
		List<HotwordEntity> data = new LinkedList<>();
		for (Map<String, Object> it : table)
		{
			HotwordEntity hot = new HotwordEntity();
			hot.date = date;
			hot.rank = rank;
			hot.hotword = it.get("searchKey").toString();
			data.add(hot);
			rank++;
		}
		Dao.persistBatch(HotwordEntity.class, data);

		logger.info("搜索热词计算完成");
	}
}
