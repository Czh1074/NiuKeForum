package com.chenzhihui.community.controller.interceptor;

import com.chenzhihui.community.annotation.LoginRequired;
import com.chenzhihui.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.Header;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 登录拦截设置
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/2 17:14
 **/
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod) { // 意思 -> 判断handler对象是否是HandlerMethod类型，因为拦截器可以拦截不同的请求处理器，我们只对方法进行处理
            HandlerMethod handlerMethod=  (HandlerMethod) handler; // 强转
            Method method = handlerMethod.getMethod(); // 获取当前对方法
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class); // 判断这个方法有没有加注解
            if (loginRequired != null && hostHolder.getUser() == null){ // 如果需要用LoginRequired进行登录验证并且用户未登录
                response.sendRedirect(request.getContextPath()); // 重定向到首页
                return false; // 表示拦截请求
            }
        }

        return true;
    }

}
