package com.sean.persist.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.sean.cache.core.CacheFactory;
import com.sean.commom.util.SecurityUtil;
import com.sean.persist.core.CachePolicy.RemoveType;
import com.sean.persist.entity.ColumnEntity;
import com.sean.persist.entity.EntityEntity;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Order;
import com.sean.persist.ext.Value;
import com.sean.persist.util.EntityDaoUtil;

/**
 * 数据持久层接口提供缓存实现
 * @author Sean
 */
@SuppressWarnings("unchecked")
public class EntityDaoCache<E extends Entity> extends EntityDao<E>
{
	private static final int statementLength = 1024;

	private Cache entityCache;// 实体缓存
	private Cache statementCache;// 语句缓存
	private CachePolicy cachePolicy;// 缓存策略

	private String allColumns;
	private EntityDao<E> dao;

	public EntityDaoCache(EntityEntity entity, EntityDao<E> dao) throws Exception
	{
		this.dao = dao;
		entityCache = CacheFactory.createEhCache("entity_" + entity.getTableName(), entity.getMaxElementsInMemory(), entity.getTimeToLiveSeconds(),
				entity.getTimeToIdleSeconds());
		statementCache = CacheFactory.createEhCache("statement_" + entity.getTableName(), entity.getMaxElementsInMemory(),
				entity.getTimeToLiveSeconds(), entity.getTimeToIdleSeconds());
		cachePolicy = (CachePolicy) entity.getCachePolicy().newInstance();

		/**
		 * 生成所有列
		 */
		StringBuilder tmp = new StringBuilder();
		for (ColumnEntity c : entity.getColumns())
		{
			tmp.append(c.getColumn()).append(",");
		}
		tmp.deleteCharAt(tmp.length() - 1);
		this.allColumns = tmp.toString();
	}

	@Override
	public void persistBatch(List<E> entitys)
	{
		if (entitys.size() > 0)
		{
			dao.persistBatch(entitys);
			// 刷新语句缓存
			this.cachePolicy.removeAllStatementCache(statementCache, RemoveType.Insert);
		}
	}

	@Override
	public void removeById(List<Object> ids)
	{
		if (!ids.isEmpty())
		{
			dao.removeById(ids);

			// 删除实体缓存
			for (Object id : ids)
			{
				this.entityCache.removeQuiet(id);
			}
			// 刷新语句缓存
			this.cachePolicy.removeAllStatementCache(statementCache, RemoveType.Remove);
		}
	}

	@Override
	public void updateById(List<Object> ids, List<Value> vals)
	{
		if (!ids.isEmpty())
		{
			dao.updateById(ids, vals);

			// 删除实体缓存
			for (Object id : ids)
			{
				this.entityCache.removeQuiet(id);
			}
			// 刷新语句缓存
			this.cachePolicy.removeAllStatementCache(statementCache, RemoveType.Update);
		}
	}

	@Override
	public List<E> loadByIds(List<Object> ids, String[] columns)
	{
		if (ids.isEmpty())
		{
			return new ArrayList<E>(0);
		}
		String colstr = columns == null ? this.allColumns : this.getColumnsString(columns);
		int length = ids.size();
		Map<Object, E> hit = new HashMap<Object, E>(length);
		List<Object> lost = new ArrayList<Object>(length);
		E ent = null;

		for (Object id : ids)
		{
			ent = this.getCacheMap(id).get(colstr);
			if (ent != null)
			{
				hit.put(id, ent);
			}
			else
			{
				lost.add(id);
			}
		}

		Map<Object, E> records = null;
		int lostLength = lost.size();
		if (lostLength > 0)
		{
			List<E> tmp = dao.loadByIds(lost, columns);
			records = new HashMap<Object, E>(lostLength);
			for (E item : tmp)
			{
				records.put(item.getKey(), item);
			}
		}

		List<E> datas = new ArrayList<E>(length);
		for (Object id : ids)
		{
			ent = hit.get(id);
			if (ent != null)
			{
				datas.add(ent);
			}
			else
			{
				ent = records.get(id);
				if (ent != null)
				{
					datas.add(ent);
					this.getCacheMap(id).put(colstr, ent);
				}
			}
		}
		return datas;
	}

	/**
	 * 获取缓存Map数据
	 * @param id
	 * @return
	 */
	private Map<String, E> getCacheMap(Object id)
	{
		Element el = entityCache.getQuiet(id);
		if (el == null)
		{
			Map<String, E> map = new HashMap<String, E>();
			entityCache.putQuiet(new Element(id, map));
			return map;
		}
		return (Map<String, E>) el.getObjectValue();
	}

	@Override
	protected List<Object> getIdList(List<Condition> conds, List<Order> orders)
	{
		StringBuilder tmp = new StringBuilder(statementLength);
		tmp.append(this.statementToString(conds, orders)).append('_');
		for (Condition cond : conds)
		{
			tmp.append(cond.getValue()).append(',');
		}
		String key = this.cachePolicy.wrapStatmentKey(SecurityUtil.md5(tmp.toString()));

		Element el = this.statementCache.getQuiet(key);
		if (el != null)
		{
			return (List<Object>) el.getObjectValue();
		}
		else
		{
			List<Object> ids = dao.getIdList(conds, orders);
			statementCache.putQuiet(new Element(key, ids));
			if (ids.isEmpty())
			{
				return new ArrayList<Object>(0);
			}
			return ids;
		}
	}

	@Override
	protected List<Object> getIdList(List<Condition> conds, List<Order> orders, int start, int limit)
	{
		StringBuilder tmp = new StringBuilder(statementLength);
		tmp.append(this.limitStatementToString(conds, orders)).append('_');
		for (Condition cond : conds)
		{
			tmp.append(cond.getValue()).append(',');
		}
		tmp.append(start).append(',').append(limit);
		String key = this.cachePolicy.wrapStatmentKey(SecurityUtil.md5(tmp.toString()));

		Element el = this.statementCache.getQuiet(key);
		if (el != null)
		{
			return (List<Object>) el.getObjectValue();
		}
		else
		{
			List<Object> ids = dao.getIdList(conds, orders);
			statementCache.putQuiet(new Element(key, ids));
			if (ids.isEmpty())
			{
				return new ArrayList<Object>(0);
			}
			return ids;
		}
	}

	@Override
	protected int count(List<Condition> conds)
	{
		StringBuilder tmp = new StringBuilder(statementLength);
		tmp.append(this.countStatementToString(conds)).append('_');
		for (Condition cond : conds)
		{
			tmp.append(cond.getValue()).append(',');
		}
		String key = this.cachePolicy.wrapStatmentKey(SecurityUtil.md5(tmp.toString()));

		Element el = this.statementCache.getQuiet(key);
		if (el != null)
		{
			return Integer.parseInt(el.getObjectValue().toString());
		}
		else
		{
			int count = dao.count(conds);
			statementCache.putQuiet(new Element(key, count));
			return count;
		}
	}

	@Override
	public Object executeScalar(Object statement)
	{
		return dao.executeScalar(statement);
	}

	@Override
	public Map<String, Object> executeMap(Object statement)
	{
		return dao.executeMap(statement);
	}

	@Override
	public List<Map<String, Object>> executeList(Object statement)
	{
		return dao.executeList(statement);
	}
	
	@Override
	public List<E> executeEntityList(Object statement)
	{
		return dao.executeEntityList(statement);
	}

	/**
	 * 获取列字符串
	 * @param columns
	 * @return
	 */
	private String getColumnsString(String[] columns)
	{
		Arrays.sort(columns);
		StringBuilder sb = new StringBuilder(10 * columns.length);
		for (int i = 0; i < columns.length; i++)
		{
			sb.append(columns[i]).append("_");
		}
		return sb.toString();
	}

	/**
	 * 查询语句转化成String
	 * @param statement					数据库查询语句
	 * @return
	 */
	private String statementToString(List<Condition> conds, List<Order> orders)
	{
		StringBuilder stat = new StringBuilder(statementLength);
		stat.append("select id").append(" where ");
		stat.append(EntityDaoUtil.getConditionStr(conds));
		if (orders != null && !orders.isEmpty())
		{
			stat.append(EntityDaoUtil.getOrdersStr(orders));
		}
		return stat.toString();
	}

	/**
	 * 查询语句转化成String
	 * @param statement					数据库查询语句
	 * @return
	 */
	private String limitStatementToString(List<Condition> conds, List<Order> orders)
	{
		StringBuilder stat = new StringBuilder(statementLength);
		stat.append(this.statementToString(conds, orders));
		stat.append(" limit");
		return stat.toString();
	}

	/**
	 * 统计语句转化成String
	 * @param conds
	 * @return
	 */
	private String countStatementToString(List<Condition> conds)
	{
		StringBuilder stat = new StringBuilder(statementLength);
		stat.append("select count ").append(" where ");
		stat.append(EntityDaoUtil.getConditionStr(conds));
		return stat.toString();
	}
}
