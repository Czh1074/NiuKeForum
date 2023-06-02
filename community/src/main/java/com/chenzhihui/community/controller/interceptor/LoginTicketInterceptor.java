package com.chenzhihui.community.controller.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chenzhihui.community.entity.LoginTicket;
import com.chenzhihui.community.entity.User;
import com.chenzhihui.community.service.LoginTicketService;
import com.chenzhihui.community.service.UserService;
import com.chenzhihui.community.util.CookieUtil;
import com.chenzhihui.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 登录凭证拦截器
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/2 00:16
 **/
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {


    @Resource
    private UserService userService;

    @Resource
    private LoginTicketService loginTicketService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if(ticket != null) {
            // 查询凭证
            QueryWrapper<LoginTicket> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ticket", ticket);
            LoginTicket loginTicket = loginTicketService.getOne(queryWrapper);
            // 检查凭证是否有效
            if(loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                // 符合条件的是凭证有效 -> 查询用户
                User user = userService.selectById(loginTicket.getUserId());
                // 得到在本次请求中持有的用户
                // 因为我们是一个服务器对应多个浏览器，所需需要用多线程的知识来存储这些用户信息
                // -> 使用ThreadLocal -> 封装在HostHolder中
                hostHolder.setUser(user);

            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 相当于在当前线程，向服务器发起请求，得到当前线程中的对象
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null){
            modelAndView.addObject("loginUser", user);

        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
