package com.sean.shop.search.constant;

import com.sean.service.annotation.ParameterConfig;
import com.sean.service.annotation.ParameterProviderConfig;
import com.sean.service.enums.DataType;

@ParameterProviderConfig(module = M.class, descr = "搜索参数")
public class P
{
	@ParameterConfig(dataType = DataType.Int, descr = "页码")
	public static final String pageNo = "pageNo";

	@ParameterConfig(dataType = DataType.Long, descr = "索引ID")
	public static final String buildId = "buildId";

	@ParameterConfig(dataType = DataType.String, descr = "密码")
	public static final String password = "password";

	@ParameterConfig(dataType = DataType.String, length = 255, descr = "搜索引擎索引关键字")
	public static final String keyword = "keyword";

	@ParameterConfig(dataType = DataType.Enum, enumVals = { "1", "2" }, descr = "索引类型:1-商品索引, 2-搜索词索引")
	public static final String indexType = "indexType";

	@ParameterConfig(dataType = DataType.Enum, enumVals = { "1", "0" }, descr = "是否强制计算分类")
	public static final String forceUpdate = "forceUpdate";
}
