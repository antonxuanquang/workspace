package com.sean.unittest.parser;

import javax.ejb.Stateful;
import javax.ejb.Stateless;

import com.sean.unittest.entity.EjbEntity;

/**
 * Ejb解析器
 * 
 * @author sean
 * 
 */
public class EjbParser
{
	public EjbEntity parse(Class<?> cls)
	{
		Stateless sl = cls.getAnnotation(Stateless.class);
		if (sl != null)
		{
			String name = sl.name();
			String mappedName = sl.mappedName();
			if (name == null || name.isEmpty())
			{
				String tmp[] = cls.getName().split("\\.");
				name = tmp[tmp.length - 1];
			}
			if (mappedName == null || mappedName.isEmpty())
			{
				mappedName = name;
			}
			return new EjbEntity(name, mappedName, cls);
		}
		else
		{
			Stateful sf = cls.getAnnotation(Stateful.class);
			if (sf != null)
			{
				String name = sf.name();
				String mappedName = sf.mappedName();
				if (name == null || name.isEmpty())
				{
					String tmp[] = cls.getName().split("\\.");
					name = tmp[tmp.length - 1];
				}
				if (mappedName == null || mappedName.isEmpty())
				{
					mappedName = name;
				}
				return new EjbEntity(name, mappedName, cls);
			}
		}
		return null;
	}
}
