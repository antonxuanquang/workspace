package com.sean.shop.search.constant;

import com.sean.service.annotation.FieldsConfig;
import com.sean.service.annotation.ReturnParameterConfig;
import com.sean.service.annotation.ReturnParameterProviderConfig;
import com.sean.service.enums.Format;
import com.sean.shop.search.entity.HotwordEntity;
import com.sean.shop.search.entity.BuildLogEntity;

@ReturnParameterProviderConfig(module = M.class, descr = "")
public class R
{
	@ReturnParameterConfig(descr = "索引建立列表", format = Format.EntityList, entity = BuildLogEntity.class)
	@FieldsConfig("*")
	public static final String buildList = "buildList";

	@ReturnParameterConfig(format = Format.Numeric, descr = "总记录数")
	public static final String totalrecord = "totalrecord";

	@ReturnParameterConfig(descr = "搜索热词列表", format = Format.EntityList, entity = HotwordEntity.class)
	@FieldsConfig({ "hotword" })
	public static final String hotwordList = "hotwordList";

	@ReturnParameterConfig(descr = "匹配的关键字, 多个用;隔开", format = Format.String)
	public static final String searchKeys = "searchKeys";
}
