package com.sean.bigdata.bean;

import java.util.ArrayList;
import java.util.List;

import com.sean.bigdata.entity.AclEntity;
import com.sean.common.ioc.BeanConfig;
import com.sean.common.util.TimeUtil;
import com.sean.persist.core.Dao;
import com.sean.persist.core.PageData;
import com.sean.persist.enums.OrderEnum;
import com.sean.persist.ext.Condition;
import com.sean.persist.ext.Order;

@BeanConfig("")
public class AclBean
{
	public PageData<AclEntity> getAclList(long userId, int pageNo, int pageSize)
	{
		return Dao.getListByPage(AclEntity.class, new Condition("userId", userId), new Order("aclId", OrderEnum.Desc), pageNo, pageSize, -1);
	}

	public List<AclEntity> getAclList(long reportId)
	{
		return Dao.getListByCond(AclEntity.class, new Condition("reportId", reportId));
	}

	public void updateAclList(long reportId, long[] userList)
	{
		List<AclEntity> aclList = new ArrayList<>(userList.length);
		for (long userId : userList)
		{
			AclEntity acl = new AclEntity();
			acl.authTime = TimeUtil.getYYYYMMDDHHMMSSTime();
			acl.userId = userId;
			acl.reportId = reportId;
			aclList.add(acl);
		}
		Dao.remove(AclEntity.class, "reportId", reportId);

		Dao.persistBatch(AclEntity.class, aclList);
	}
}
