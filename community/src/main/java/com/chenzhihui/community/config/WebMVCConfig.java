package com.chenzhihui.community.config;

import com.chenzhihui.community.controller.interceptor.LoginRequiredInterceptor;
import com.chenzhihui.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器配置类
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/2 00:38
 **/
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Resource
    private LoginTicketInterceptor loginTicketInterceptor;

    @Resource
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/**/*.css", "/**/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/**/*.css", "/**/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
    }

}
