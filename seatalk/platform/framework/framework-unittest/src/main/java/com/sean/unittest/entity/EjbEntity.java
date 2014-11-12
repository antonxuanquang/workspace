package com.sean.unittest.entity;

/**
 * Ejb实体
 * @author sean
 *
 */
public class EjbEntity
{
	private String name;
	private String mappedName;
	private Class<?> cls;

	public EjbEntity(String name, String mappedName, Class<?> cls)
	{
		this.name = name;
		this.mappedName = mappedName;
		this.cls = cls;
	}

	public String getName()
	{
		return name;
	}

	public Class<?> getCls()
	{
		return cls;
	}

	public String getMappedName()
	{
		return mappedName;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(cls.getName()).append(" name is ").append(name).append(", mappedName is ").append(mappedName);
		return sb.toString();
	}

}
