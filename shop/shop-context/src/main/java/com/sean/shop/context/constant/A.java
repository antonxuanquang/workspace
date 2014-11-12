package com.sean.shop.context.constant;

import com.sean.service.annotation.PermissionConfig;
import com.sean.service.annotation.PermissionProviderConfig;
import com.sean.service.core.PermissionProvider;

/**
 * 权限定义, Authority缩写
 * @author sean
 */
@PermissionProviderConfig(description = "全局权限配置")
public class A extends PermissionProvider
{
	@PermissionConfig(description = "是否需要管理员")
	public static final int Admin = 1;
}
