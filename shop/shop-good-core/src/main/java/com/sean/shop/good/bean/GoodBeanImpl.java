package com.sean.shop.good.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sean.common.ioc.BeanConfig;
import com.sean.common.ioc.ResourceConfig;
import com.sean.common.util.TimeUtil;
import com.sean.log.core.LogFactory;
import com.sean.persist.core.Dao;
import com.sean.persist.core.PageData;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.enums.OrderEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Order;
import com.sean.persist.ext.Value;
import com.sean.service.core.BusinessException;
import com.sean.shop.context.core.Good;
import com.sean.shop.good.api.GoodBean;
import com.sean.shop.good.constant.D;
import com.sean.shop.good.entity.GoodEntity;
import com.sean.shop.good.entity.GoodFeedbackEntity;
import com.sean.shop.search.api.SearchBean;

@BeanConfig("商品对象")
public class GoodBeanImpl implements GoodBean
{
	@ResourceConfig
	private SearchBean searchBean;

	private static final Logger logger = LogFactory.getLogger(GoodBeanImpl.class);

	/**
	 * 删除商品
	 * @param goodId
	 * @throws BusinessException 
	 * @throws  
	 */
	public void deleteGood(long goodId) throws BusinessException
	{
		// 删除商品
		Dao.remove(GoodEntity.class, goodId);

		try
		{
			// 删除索引
			searchBean.deleteGood(goodId);
		}
		catch (IOException e)
		{
			logger.error("删除索引异常", e);
			throw new BusinessException("删除索引异常", 1);
		}

	}

	/**
	 * 删除商品反馈
	 * @param feedbackId
	 * @throws  
	 */
	public void deleteFeedback(long feedbackId)
	{
		Dao.remove(GoodFeedbackEntity.class, feedbackId);
	}

	/**
	 * 新增商品
	 * @param good
	 * @throws BusinessException 
	 */
	public void addGood(GoodEntity good) throws BusinessException
	{
		good.createTime = TimeUtil.getYYYYMMDDHHMMSSTime();
		good.showTimes = 0;
		good.status = D.GoodStatus_Visible;
		Dao.persist(GoodEntity.class, good);

		// 添加索引
		Good idxGood = this.good2IndexItem(good);
		try
		{
			searchBean.addGood(idxGood);
		}
		catch (IOException e)
		{
			logger.error("更新索引异常", e);
			throw new BusinessException("更新索引异常", 1);
		}
	}

	/**
	 * 修改商品
	 * @param good
	 * @throws BusinessException 
	 */
	public void updateGood(GoodEntity good) throws BusinessException
	{
		GoodEntity tmp = Dao.loadById(GoodEntity.class, good.goodId);
		if (tmp != null)
		{
			good.showTimes = tmp.showTimes;
			good.status = tmp.status;

			Dao.update(GoodEntity.class, good);

			// 更新索引
			Good idxGood = this.good2IndexItem(good);
			try
			{
				searchBean.updateGood(idxGood);
			}
			catch (IOException e)
			{
				logger.error("更新索引异常", e);
				throw new BusinessException("更新索引异常", 1);
			}
		}
	}

	/**
	 * 修改商品状态
	 * @param goodId
	 * @param status
	 * @throws BusinessException 
	 */
	public void updateGoodStatus(long goodId) throws BusinessException
	{
		GoodEntity good = Dao.loadById(GoodEntity.class, goodId);
		if (good != null)
		{
			int status = good.status == 1 ? 2 : 1;
			good.status = status;
			Dao.update(GoodEntity.class, goodId, new Value("status", status));

			// 更新索引
			Good idxGood = this.good2IndexItem(good);
			try
			{
				searchBean.updateGood(idxGood);
			}
			catch (IOException e)
			{
				logger.error("更新索引异常", e);
				throw new BusinessException("更新索引异常", 1);
			}
		}
	}

	/**
	 * 分页读取商品列表
	 * @param pageNo
	 * @param pageSize
	 * @param channel			-1代表不区分渠道
	 * @param categoryId		-1代表不区分分类
	 * @return
	 */
	public PageData<GoodEntity> getGoodList(int pageNo, int pageSize, int channel, long categoryId)
	{
		List<Condition> conds = new LinkedList<>();

		if (channel != -1)
		{
			conds.add(new Condition("channel", channel));
		}
		if (categoryId != -1)
		{
			conds.add(new Condition("categoryId", categoryId));
		}

		if (conds.isEmpty())
		{
			conds.add(new Condition("goodId", ConditionEnum.Not_Equal, 0));
		}
		return Dao.getListByPage(GoodEntity.class, conds, new Order("showTimes", OrderEnum.Desc), pageNo, pageSize, -1);
	}

	/**
	 * 反馈商品
	 * @param goodId
	 */
	public void feedback(long goodId)
	{
		GoodFeedbackEntity feedback = new GoodFeedbackEntity();
		feedback.goodId = goodId;
		feedback.feedbackTime = TimeUtil.getYYYYMMDDHHMMSSTime();
		Dao.persist(GoodFeedbackEntity.class, feedback);
	}

	/**
	 * 读取商品反馈列表
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageData<GoodFeedbackEntity> getFeedbackList(int pageNo, int pageSize)
	{
		return Dao.getListByPage(GoodFeedbackEntity.class, new Condition("feedbackId", ConditionEnum.Not_Equal, 0), new Order("feedbackId",
				OrderEnum.Desc), pageNo, pageSize, -1);
	}

	/**
	 * 增加商品展示次数
	 * @param goodId
	 * @param time
	 */
	public void incShowTime(long goodId, int times)
	{
		GoodEntity good = Dao.loadById(GoodEntity.class, goodId);
		if (good != null)
		{
			Dao.update(GoodEntity.class, goodId, new Value("showTimes", good.showTimes + times));
		}
	}

	private Good good2IndexItem(GoodEntity good)
	{
		Good g = new Good();
		g.goodName = good.goodName;
		g.categoryId = good.categoryId;
		g.channel = good.channel;
		g.goodId = good.goodId;
		g.keyword = good.keyword;
		g.price = good.price;
		g.boost = good.boost;
		g.status = good.status;
		g.saleCount = good.saleCount;
		return g;
	}

	@Override
	public List<Good> getGoodList(long currId, int size)
	{
		Condition cond = new Condition("goodId", ConditionEnum.Greater, currId);
		Order order = new Order("goodId", OrderEnum.Asc);

		List<GoodEntity> list = Dao.getListByPage(GoodEntity.class, cond, order, 1, size, 1).getDatas();

		List<Good> tmp = new ArrayList<>(list.size());
		for (GoodEntity it : list)
		{
			tmp.add(good2IndexItem(it));
		}
		return tmp;
	}

	@Override
	public void updateGoodCategory(long goodId, long categoryId)
	{
		Dao.update(GoodEntity.class, goodId, new Value("categoryId", categoryId));
	}
}
