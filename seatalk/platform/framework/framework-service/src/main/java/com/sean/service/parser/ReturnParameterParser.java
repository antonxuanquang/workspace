package com.sean.service.parser;

import com.sean.persist.core.Entity;
import com.sean.persist.dictionary.DictionaryConfig;
import com.sean.service.annotation.ReturnParameterConfig;
import com.sean.service.annotation.UseDicConfig;
import com.sean.service.entity.ReturnParameterEntity;
import com.sean.service.entity.UseDicEntity;
import com.sean.service.enums.Format;
import com.sean.service.writer.FieldWriter;
import com.sean.service.writer.FieldWriterEntity;
import com.sean.service.writer.FieldWriterEntityList;
import com.sean.service.writer.FieldWriterJson;
import com.sean.service.writer.FieldWriterMap;
import com.sean.service.writer.FieldWriterNumeric;
import com.sean.service.writer.FieldWriterString;

/**
 * 返回参数
 * @author sean
 */
public class ReturnParameterParser
{
	public ReturnParameterEntity parse(ReturnParameterConfig pc, String name) throws Exception
	{
		UseDicConfig[] tmp = pc.dics();
		UseDicEntity[] dics = new UseDicEntity[tmp.length];
		if (pc.format() == Format.Map && pc.fields().length == 0)
		{
			throw new RuntimeException("the map defined in return parameter " + name + ", witch's fields must not be empty");
		}
		// 如果是实体，检查配置项
		if (pc.format() == Format.Entity || pc.format() == Format.EntityList)
		{
			if (!Entity.class.isAssignableFrom(pc.entity()))
			{
				throw new RuntimeException("the entity defined in return parameter " + name + " is not an entity");
			}
			for (int i = 0; i < dics.length; i++)
			{
				if (tmp[i].dic().getAnnotation(DictionaryConfig.class) == null)
				{
					throw new RuntimeException("the dictionary class defined in return parameter " + name + " " + tmp[i].dic().getName()
							+ " is not a dynamic dictionary");
				}
				dics[i] = new UseDicEntity(tmp[i].field(), tmp[i].dic().getName());
				if (!check(pc.fields(), dics[i]))
				{
					throw new RuntimeException("the dictionary field " + dics[i].getField() + " was not found in the fields of return parameter "
							+ name);
				}
			}
		}

		FieldWriter fieldWriter = null;
		switch (pc.format())
		{
		case Numeric:
			fieldWriter = FieldWriterNumeric.getInstance();
			break;
		case String:
			fieldWriter = FieldWriterString.getInstance();
			break;
		case Json:
			fieldWriter = FieldWriterJson.getInstance();
			break;
		case Map:
			fieldWriter = FieldWriterMap.getInstance();
			break;
		case Entity:
			fieldWriter = FieldWriterEntity.getInstance();
			break;
		case EntityList:
			fieldWriter = FieldWriterEntityList.getInstance();
			break;
		default:
			break;
		}

		ReturnParameterEntity pe = new ReturnParameterEntity(name, pc.format(), pc.entity(), pc.fields(), dics, pc.description(),
				fieldWriter);
		return pe;
	}

	private boolean check(String[] fields, UseDicEntity dic)
	{
		for (int i = 0; i < fields.length; i++)
		{
			if (dic.getField().equals(fields[i]))
			{
				return true;
			}
		}
		return false;
	}
}
