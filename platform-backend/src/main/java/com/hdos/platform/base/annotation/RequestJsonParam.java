package com.hdos.platform.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Json 对象参数注解
 * @author chenyang
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJsonParam {
	/**
	 * The name of the request parameter to bind to.
	 */
	String value() default "";
	
	/**
	 * 参数是否为必填项，默认为必填
	 * 
	 * @return
	 */
	boolean required() default true;
	
	
	/**
	 * 如果为可选字段，当入参为空，则返回指定字符串json值
	 * 
	 * @return
	 */
	String defaultValue() default "";
}
