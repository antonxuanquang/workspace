package com.sean.service.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sean.service.enums.Format;

/**
 * return parameter
 * @author sean
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ReturnParameterConfig
{
	/**
	 * parameter format
	 */
	Format format();

	/**
	 * class of entity this is aimed at when format is Entity or EntityList
	 */
	Class<?> entity() default Object.class;

	/**
	 * <p>return fields</p>
	 * <p>1.aimed at Entity or EntityList format to filter fields</p>
	 * <p>2.aimed at Map format fo filter fields</p>
	 */
	String[] fields() default {};

	/**
	 * <p>define dics to use</p>
	 * <p>this is aimed at Entity or EntityList</p>
	 */
	UseDicConfig[] dics() default {};

	/**
	 * description
	 */
	String description();
}
