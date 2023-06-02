package com.chenzhihui.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录判断注解类
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/2 17:11
 **/
// 1、说明是管理方法
// 2、说明时机是在运行的时候
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
