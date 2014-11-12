package com.sean.service.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sean.service.core.PermissionProvider;
import com.sean.service.core.V1;
import com.sean.service.enums.ReturnType;

/**
 * request action config
 * @author sean
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActionConfig
{
	/**
	 * enable transaction, default by false
	 */
	boolean transaction() default false;

	/**
	 * return parameters
	 */
	String[] returnParams() default {};

	/**
	 * must parameters
	 */
	String[] mustParams() default {};

	/**
	 * optional parameters
	 */
	String[] optionalParams() default {};

	/**
	 * description
	 */
	String description();

	/**
	 * permissionId, default by 0, that is no permission
	 */
	int permission() default PermissionProvider.None;

	/**
	 * enable authenticate, default by true
	 * <p>it is recommended to set false in login or regist action which dosen't need identify user infomation</p>
	 */
	boolean authenticate() default true;

	/**
	 * return type, default by json
	 */
	ReturnType returnType() default ReturnType.Json;

	/**
	 * version, default by V1
	 */
	Class<?> version() default V1.class;
}
