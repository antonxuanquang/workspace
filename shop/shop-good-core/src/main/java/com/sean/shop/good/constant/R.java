package com.sean.shop.good.constant;

import com.sean.service.annotation.FieldsConfig;
import com.sean.service.annotation.ReturnParameterConfig;
import com.sean.service.annotation.ReturnParameterProviderConfig;
import com.sean.service.annotation.UseDicConfig;
import com.sean.service.enums.Format;
import com.sean.shop.good.api.GoodDic;
import com.sean.shop.good.entity.ActiveEntity;
import com.sean.shop.good.entity.GoodEntity;
import com.sean.shop.good.entity.GoodFeedbackEntity;
import com.sean.shop.good.entity.OrderEntity;
import com.sean.shop.good.entity.UserEntity;

@ReturnParameterProviderConfig(module = M.class, descr = "")
public class R
{
	@ReturnParameterConfig(format = Format.String, descr = "sid")
	public static final String sid = "sid";

	@ReturnParameterConfig(format = Format.String, descr = "加密key")
	public static final String encryptKey = "encryptKey";

	@ReturnParameterConfig(format = Format.Numeric, descr = "商品ID")
	public static final String goodId = "goodId";

	@ReturnParameterConfig(descr = "商品列表", format = Format.EntityList, entity = GoodEntity.class)
	@FieldsConfig({ "goodId", "goodName", "price", "imageUrl", "goodUrl", "channel", "saleCount" })
	public static final String goodList = "goodList";

	@ReturnParameterConfig(descr = "商品", format = Format.Entity, entity = GoodEntity.class)
	@FieldsConfig({ "goodId", "goodName", "price", "imageUrl", "goodUrl", "saleCount" })
	public static final String goodDetail = "goodDetail";

	@ReturnParameterConfig(descr = "商品反馈", format = Format.EntityList, entity = GoodFeedbackEntity.class)
	@FieldsConfig("*")
	@UseDicConfig(field = "goodId", dic = GoodDic.class)
	public static final String feedbackList = "feedbackList";

	@ReturnParameterConfig(descr = "商品列表", format = Format.EntityList, entity = GoodEntity.class)
	@FieldsConfig("*")
	public static final String goodList4Console = "goodList4Console";

	@ReturnParameterConfig(descr = "分类列表", format = Format.Json)
	public static final String categoryList = "categoryList";

	@ReturnParameterConfig(format = Format.Numeric, descr = "总记录数")
	public static final String totalrecord = "totalrecord";

	@ReturnParameterConfig(format = Format.Numeric, descr = "活动ID")
	public static final String activeId = "activeId";

	@ReturnParameterConfig(descr = "活动列表", format = Format.EntityList, entity = ActiveEntity.class)
	@FieldsConfig("*")
	public static final String activeList = "activeList";

	@ReturnParameterConfig(descr = "用户信息", format = Format.Entity, entity = UserEntity.class)
	@FieldsConfig({ "username", "integration", "registTime" })
	public static final String userinfo = "userinfo";

	@ReturnParameterConfig(descr = "订单列表", format = Format.EntityList, entity = OrderEntity.class)
	@FieldsConfig({ "goodId", "orderId", "createTime", "integration", "status", "goodName", "goodUrl" })
	@UseDicConfig(dic = GoodDic.class, field = "goodId")
	public static final String orderList = "orderList";
}
