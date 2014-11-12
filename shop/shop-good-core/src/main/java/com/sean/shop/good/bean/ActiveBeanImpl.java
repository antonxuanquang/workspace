package com.sean.shop.good.bean;

import java.util.LinkedList;
import java.util.List;

import com.sean.common.ioc.BeanConfig;
import com.sean.common.util.TimeUtil;
import com.sean.persist.core.Dao;
import com.sean.persist.core.PageData;
import com.sean.persist.enums.ConditionEnum;
import com.sean.persist.enums.OrderEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Order;
import com.sean.shop.good.entity.ActiveEntity;

@BeanConfig("")
public class ActiveBeanImpl
{
	/**
	 * 删除活动
	 * @param activeId
	 * @throws  
	 */
	public void deleteActive(long activeId)
	{
		// 删除活动
		Dao.remove(ActiveEntity.class, activeId);
	}

	/**
	 * 新增活动
	 * @param active
	 */
	public void addActive(ActiveEntity active)
	{
		active.createTime = TimeUtil.getYYYYMMDDHHMMSSTime();
		Dao.persist(ActiveEntity.class, active);
	}

	/**
	 * 分页读取活动列表
	 * @param pageNo
	 * @param pageSize
	 * @param activeChannel		-1代表不区分渠道
	 * @param active			是否正在进行
	 * @return
	 */
	public PageData<ActiveEntity> getActiveList(int pageNo, int pageSize, int activeChannel, boolean active)
	{
		List<Condition> conds = new LinkedList<>();

		if (activeChannel != -1)
		{
			conds.add(new Condition("activeChannel", activeChannel));
		}

		if (active)
		{
			conds.add(new Condition("endDate", ConditionEnum.Greater_Equal, TimeUtil.getYYYYMMDD()));
		}

		if (conds.isEmpty())
		{
			conds.add(new Condition("activeId", ConditionEnum.Not_Equal, 0));
		}
		return Dao.getListByPage(ActiveEntity.class, conds, new Order("activeId", OrderEnum.Desc), pageNo, pageSize, -1);
	}
}
