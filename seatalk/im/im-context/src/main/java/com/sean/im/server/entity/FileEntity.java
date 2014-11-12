package com.sean.im.server.entity;

import java.util.HashMap;
import java.util.Map;

import com.sean.im.server.constant.Tables;
import com.sean.im.server.context.IMDataSource;
import com.sean.persist.annotation.ColumnConfig;
import com.sean.persist.annotation.EntityConfig;
import com.sean.persist.core.Entity;

@EntityConfig(tableName = Tables.File, dataSource = IMDataSource.class, description = "文件，图片，语音")
public class FileEntity extends Entity
{
	@ColumnConfig(primaryKey = true, description = "id")
	private long id;
	@ColumnConfig(description = "原文件名")
	private String filename;
	@ColumnConfig(description = "字节长度")
	private long length;
	@ColumnConfig(description = "文件路径")
	private String path;
	@ColumnConfig(description = "1代表文件，2代表图片，3代表音频")
	private int type;

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
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("id", id);
		map.put("filename", filename);
		map.put("length", length);
		map.put("path", path);
		map.put("type", type);
		return map;
	}

	@Override
	public void setValues(Map<String, Object> vals)
	{
		Object o = null;
		if ((o = vals.get("id")) != null)
			this.id = Long.parseLong(o.toString());
		if ((o = vals.get("filename")) != null)
			this.filename = o.toString();
		if ((o = vals.get("length")) != null)
			this.length = Long.parseLong(o.toString());
		if ((o = vals.get("path")) != null)
			this.path = o.toString();
		if ((o = vals.get("type")) != null)
			this.type = Integer.parseInt(o.toString());
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public long getLength()
	{
		return length;
	}

	public void setLength(long length)
	{
		this.length = length;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

}