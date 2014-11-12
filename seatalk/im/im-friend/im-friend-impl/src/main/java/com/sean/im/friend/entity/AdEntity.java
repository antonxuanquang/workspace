package com.sean.im.friend.entity;

import java.util.HashMap;
import java.util.Map;

import com.sean.im.server.context.IMDataSource;
import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;

@EntityConfig(tableName = "t_ad", dataSource = IMDataSource.class, description = "广告")
public class AdEntity extends Entity
{
	@ColumnConfig(primaryKey = true, description = "id")
	private long id;
	@ColumnConfig(description = "图片")
	private String imgUrl;
	@ColumnConfig(description = "链接")
	private String link;

	@Override
	public Object getKey()
	{
		return id;
	}

	@Override
	public void setKey(Object key)
	{
		this.id = (long) key;
	}

	@Override
	public Map<String, Object> getValues()
	{
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("id", id);
		map.put("imgUrl", imgUrl);
		map.put("link", link);
		return map;
	}

	@Override
	public void setValues(Map<String, Object> vals)
	{
		Object o = null;
		if ((o = vals.get("id")) != null)
			this.id = Long.parseLong(o.toString());
		if ((o = vals.get("link")) != null)
			this.link = o.toString();
		if ((o = vals.get("imgUrl")) != null)
			this.imgUrl = o.toString();
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getImgUrl()
	{
		return imgUrl;
	}

	public void setImgUrl(String imgUrl)
	{
		this.imgUrl = imgUrl;
	}

}