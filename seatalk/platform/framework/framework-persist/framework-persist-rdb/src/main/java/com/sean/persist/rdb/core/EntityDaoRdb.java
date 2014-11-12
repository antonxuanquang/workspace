package com.sean.persist.rdb.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.sean.log.core.LogFactory;
import com.sean.persist.core.Entity;
import com.sean.persist.core.EntityDao;
import com.sean.persist.entity.EntityEntity;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Order;
import com.sean.persist.ext.Value;
import com.sean.persist.util.EntityDaoUtil;

/**
 * 持久化关系型数据库实现
 * @author sean
 */
@SuppressWarnings("unchecked")
public abstract class EntityDaoRdb<E extends Entity> extends EntityDao<E>
{
	private static final Logger logger = LogFactory.getFrameworkLogger();

	protected DataSource dataSource;
	protected final static int sqlLength = 1024;
	protected String allColumnsStr;
	protected String key;
	protected String table;
	protected String[] allColumns;
	protected Class<?> cls;
	protected boolean showSql;

	public EntityDaoRdb(EntityEntity entity, DataSource dataSource, boolean showSql)
	{
		this.dataSource = dataSource;
		this.showSql = showSql;

		// 获取实体信息
		this.key = entity.getPrimaryKey().getColumn();
		this.table = entity.getTableName();
		this.cls = entity.getCls();

		// 生成所有列
		int length = entity.getColumns().size();
		allColumns = new String[length];
		StringBuilder tmp = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			tmp.append(entity.getColumns().get(i).getColumn()).append(",");
			allColumns[i] = entity.getColumns().get(i).getColumn();
		}
		tmp.deleteCharAt(tmp.length() - 1);
		this.allColumnsStr = tmp.toString();
	}

	@Override
	public int count(List<Condition> conds)
	{
		Connection conn = null;
		try
		{
			conn = this.dataSource.getConnection();

			StringBuilder sql = new StringBuilder(sqlLength);
			sql.append("select count(*) from ").append(table).append(" where ");
			sql.append(EntityDaoUtil.getConditionStr(conds));
			showSql(sql.toString());

			PreparedStatement ps = conn.prepareStatement(sql.toString());
			EntityDaoUtil.setStatementParams(ps, EntityDaoUtil.getConditionsVal(conds), 1);

			ResultSet rs = ps.executeQuery();
			int count = 0;
			if (rs.next())
			{
				count = rs.getInt(1);
				rs.close();
				ps.close();
				return count;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (conn != null && !conn.isClosed())
				{
					conn.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
				logger.error(e2.getMessage(), e2);
			}
		}
		return 0;
	}

	@Override
	protected List<Object> getIdList(List<Condition> conds, List<Order> orders)
	{
		Connection conn = null;
		try
		{
			conn = this.dataSource.getConnection();

			StringBuilder sql = new StringBuilder(sqlLength);
			sql.append("select ").append(key).append(" from ").append(table).append(" where ");
			sql.append(EntityDaoUtil.getConditionStr(conds));
			if (orders != null && !orders.isEmpty())
			{
				sql.append("order by ");
				sql.append(EntityDaoUtil.getOrdersStr(orders));
			}
			showSql(sql.toString());

			PreparedStatement ps = conn.prepareStatement(sql.toString());
			EntityDaoUtil.setStatementParams(ps, EntityDaoUtil.getConditionsVal(conds), 1);

			ResultSet rs = ps.executeQuery();
			List<Object> ids = new LinkedList<Object>();
			while (rs.next())
			{
				ids.add(rs.getObject(1));
			}
			rs.close();
			ps.close();
			return ids;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (conn != null && !conn.isClosed())
				{
					conn.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
				logger.error(e2.getMessage(), e2);
			}
		}
		return null;
	}

	@Override
	public void removeById(List<Object> ids)
	{
		Connection conn = null;
		try
		{
			conn = this.dataSource.getConnection();

			StringBuilder sql = new StringBuilder(sqlLength);
			sql.append("delete from ").append(table).append(" where ").append(key).append(" in(");
			int length = ids.size();
			for (int i = 0; i < length; i++)
			{
				sql.append("?,");
			}
			sql.setCharAt(sql.length() - 1, ')');
			showSql(sql.toString());

			PreparedStatement ps = conn.prepareStatement(sql.toString());
			EntityDaoUtil.setStatementParams(ps, ids, 1);
			ps.executeUpdate();
			ps.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (conn != null && !conn.isClosed())
				{
					conn.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
				logger.error(e2.getMessage(), e2);
			}
		}
	}

	@Override
	public void updateById(List<Object> ids, List<Value> vals)
	{
		Connection conn = null;
		try
		{
			conn = this.dataSource.getConnection();

			StringBuilder sql = new StringBuilder(sqlLength);
			sql.append("update ").append(table).append(" set ");
			sql.append(EntityDaoUtil.getUpdateField(vals)).append(" where ").append(key).append(" in (");
			int length = ids.size();
			for (int i = 0; i < length; i++)
			{
				sql.append("?,");
			}
			sql.setCharAt(sql.length() - 1, ')');
			showSql(sql.toString());

			PreparedStatement ps = conn.prepareStatement(sql.toString());
			List<Object> updateVal = EntityDaoUtil.getUpdateVals(vals);
			EntityDaoUtil.setStatementParams(ps, updateVal, 1);
			EntityDaoUtil.setStatementParams(ps, ids, updateVal.size() + 1);
			ps.executeUpdate();
			ps.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (conn != null && !conn.isClosed())
				{
					conn.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
				logger.error(e2.getMessage(), e2);
			}
		}
	}

	@Override
	public List<E> loadByIds(List<Object> ids, String[] columns)
	{
		Connection conn = null;
		try
		{
			conn = this.dataSource.getConnection();

			StringBuilder sql = new StringBuilder(sqlLength);
			String colStr = columns == null ? allColumnsStr : this.getColumnsString(columns);
			sql.append("select ").append(colStr).append(" from ").append(table).append(" where ").append(key).append(" in(");
			int length = ids.size();
			for (int i = 0; i < length; i++)
			{
				sql.append("?,");
			}
			sql.setCharAt(sql.length() - 1, ')');
			showSql(sql.toString());

			PreparedStatement ps = conn.prepareStatement(sql.toString());
			EntityDaoUtil.setStatementParams(ps, ids, 1);
			ResultSet rs = ps.executeQuery();
			List<E> data = new LinkedList<E>();

			String[] cols = columns == null ? this.allColumns : columns;
			while (rs.next())
			{
				data.add(this.getEntity(rs, cols));
			}
			rs.close();
			ps.close();
			return data;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (conn != null && !conn.isClosed())
				{
					conn.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
				logger.error(e2.getMessage(), e2);
			}
		}
		return null;
	}

	@Override
	public Object executeScalar(Object statement)
	{
		Connection conn = null;
		try
		{
			conn = this.dataSource.getConnection();

			String sql = statement.toString();
			showSql(sql);

			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				return rs.getObject(1);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (conn != null && !conn.isClosed())
				{
					conn.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
				logger.error(e2.getMessage(), e2);
			}
		}
		return null;
	}

	@Override
	public Map<String, Object> executeMap(Object statement)
	{
		Connection conn = null;
		try
		{
			conn = this.dataSource.getConnection();

			String sql = statement.toString();
			showSql(sql);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			Map<String, Object> row = new HashMap<String, Object>(10);
			ResultSetMetaData meta = rs.getMetaData();
			int columnCount = meta.getColumnCount();
			String label;
			if (rs.next())
			{
				for (int i = 0; i < columnCount; i++)
				{
					label = meta.getColumnLabel(i + 1);
					row.put(label, rs.getObject(label));
				}
			}

			st.close();
			return row;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (conn != null && !conn.isClosed())
				{
					conn.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
				logger.error(e2.getMessage(), e2);
			}
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> executeList(Object statement)
	{
		Connection conn = null;
		try
		{
			conn = this.dataSource.getConnection();

			String sql = statement.toString();
			showSql(sql);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			List<Map<String, Object>> table = new LinkedList<Map<String, Object>>();
			ResultSetMetaData meta = rs.getMetaData();
			Map<String, Object> row = null;
			int columnCount = meta.getColumnCount();
			String label;
			while (rs.next())
			{
				row = new HashMap<String, Object>(columnCount);
				for (int i = 0; i < columnCount; i++)
				{
					label = meta.getColumnLabel(i + 1);
					row.put(label, rs.getObject(label));
				}
				table.add(row);
			}

			st.close();
			return table;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (conn != null && !conn.isClosed())
				{
					conn.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
				logger.error(e2.getMessage(), e2);
			}
		}
		return null;
	}

	@Override
	public List<E> executeEntityList(Object statement)
	{
		Connection conn = null;
		try
		{
			conn = this.dataSource.getConnection();

			String sql = statement.toString();
			showSql(sql);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			List<E> data = new LinkedList<E>();
			while (rs.next())
			{
				data.add(this.getEntity(rs, this.allColumns));
			}
			st.close();
			return data;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally
		{
			try
			{
				if (conn != null && !conn.isClosed())
				{
					conn.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
				logger.error(e2.getMessage(), e2);
			}
		}
		return null;
	}

	/**
	 * 获取列字符串
	 * @param columns
	 * @return
	 */
	protected String getColumnsString(String[] columns)
	{
		Arrays.sort(columns);
		StringBuilder sb = new StringBuilder(10 * columns.length);
		for (int i = 0; i < columns.length; i++)
		{
			sb.append(columns[i]).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 从ResultSet读取Entity
	 */
	protected E getEntity(ResultSet rs, String[] columns) throws Exception
	{
		E ent = (E) cls.newInstance();
		Map<String, Object> vals = new HashMap<String, Object>(columns.length);
		for (int i = 0; i < columns.length; i++)
		{
			vals.put(columns[i], rs.getObject(columns[i]));
		}
		ent.setValues(vals);
		return ent;
	}

	protected void showSql(String sql)
	{
		if (showSql)
		{
			logger.info(sql);
		}
	}
}
