package com.sean.bigdata.constant;

import com.sean.service.annotation.PermissionConfig;
import com.sean.service.annotation.PermissionProviderConfig;
import com.sean.service.core.PermissionProvider;

@PermissionProviderConfig(description = "")
public class A extends PermissionProvider
{
	@PermissionConfig(description = "管理员权限")
	public static final int Admin = 1;

	@PermissionConfig(description = "报表访问权限")
	public static final int ReportACL = 2;
}
