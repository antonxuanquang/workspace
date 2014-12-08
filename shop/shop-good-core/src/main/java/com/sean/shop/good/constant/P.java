package com.sean.shop.good.constant;

import com.sean.service.annotation.ParameterConfig;
import com.sean.service.annotation.ParameterProviderConfig;
import com.sean.service.enums.DataType;

@ParameterProviderConfig(module = M.class, descr = "商品参数")
public class P
{
	@ParameterConfig(dataType = DataType.Long, descr = "商品id")
	public static final String goodId = "goodId";

	@ParameterConfig(dataType = DataType.Enum, enumVals = { "1", "2" }, descr = "商品状态: 1-上架, 2-下架")
	public static final String status = "status";

	@ParameterConfig(dataType = DataType.String, length = 255, descr = "商品名")
	public static final String goodName = "goodName";

	@ParameterConfig(dataType = DataType.Float, descr = "商品价格")
	public static final String price = "price";

	@ParameterConfig(dataType = DataType.Long, descr = "商品分类")
	public static final String categoryId = "categoryId";

	@ParameterConfig(dataType = DataType.String, length = 512, descr = "商品图片Url")
	public static final String imageUrl = "imageUrl";

	@ParameterConfig(dataType = DataType.String, length = 512, descr = "淘宝商品Url")
	public static final String goodUrl = "goodUrl";

	@ParameterConfig(dataType = DataType.Enum, enumVals = { "1", "2" }, descr = "商品来源渠道: 1-天天9块九, 2-精品推荐")
	public static final String channel = "channel";

	@ParameterConfig(dataType = DataType.String, length = 255, descr = "搜索引擎索引关键字")
	public static final String keyword = "keyword";

	@ParameterConfig(dataType = DataType.Enum, enumVals = { "1", "2", "3", "4" }, descr = "1-综合排序, 2-销量, 3-价格, 4-人气")
	public static final String ranking = "ranking";

	@ParameterConfig(dataType = DataType.Float, descr = "搜索排名得分调整参数, 默认为1")
	public static final String boost = "boost";

	@ParameterConfig(dataType = DataType.Int, descr = "一个月销售量")
	public static final String saleCount = "saleCount";

	@ParameterConfig(dataType = DataType.Int, descr = "页码")
	public static final String pageNo = "pageNo";

	@ParameterConfig(dataType = DataType.Int, descr = "价格下限")
	public static final String priceStart = "priceStart";

	@ParameterConfig(dataType = DataType.Int, descr = "价格上限")
	public static final String priceEnd = "priceEnd";

	@ParameterConfig(dataType = DataType.String, descr = "搜索关键字")
	public static final String searchKey = "searchKey";

	@ParameterConfig(dataType = DataType.String, descr = "用户名")
	public static final String username = "username";

	@ParameterConfig(dataType = DataType.String, descr = "密码")
	public static final String password = "password";

	@ParameterConfig(dataType = DataType.String, descr = "密码")
	public static final String oldpassword = "oldpassword";

	@ParameterConfig(dataType = DataType.Long, descr = "商品反馈id")
	public static final String feedbackId = "feedbackId";

	@ParameterConfig(dataType = DataType.String, length = 255, descr = "活动名")
	public static final String activeName = "activeName";

	@ParameterConfig(dataType = DataType.Int, descr = "开始时间")
	public static final String startDate = "startDate";

	@ParameterConfig(dataType = DataType.Int, descr = "结束时间")
	public static final String endDate = "endDate";

	@ParameterConfig(dataType = DataType.String, length = 512, descr = "活动连接")
	public static final String activeUrl = "activeUrl";

	@ParameterConfig(dataType = DataType.Enum, enumVals = { "1", "2" }, descr = "活动渠道, 1-pc, 2-mobile")
	public static final String activeChannel = "activeChannel";

	@ParameterConfig(dataType = DataType.Long, descr = "活动id")
	public static final String activeId = "activeId";

	@ParameterConfig(dataType = DataType.Enum, enumVals = { "1", "0" }, descr = "是否正在进行")
	public static final String activing = "activing";

	@ParameterConfig(dataType = DataType.String, length = 16, descr = "手机号码")
	public static final String tel = "tel";

	@ParameterConfig(dataType = DataType.String, length = 16, descr = "qq号码")
	public static final String qq = "qq";

	@ParameterConfig(dataType = DataType.String, length = 512, descr = "收货地址")
	public static final String address = "address";

	@ParameterConfig(dataType = DataType.String, length = 512, descr = "订单备注")
	public static final String remark = "remark";
}
