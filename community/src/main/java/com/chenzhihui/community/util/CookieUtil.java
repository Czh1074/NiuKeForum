package com.chenzhihui.community.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Cookie工具类
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/2 00:18
 **/
@Component
public class CookieUtil {

    public static String getValue(HttpServletRequest request, String name) {
        if(request == null || name == null){
            throw new IllegalArgumentException("参数为空！");
        }
        // 通过request中获取所有的cookies
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                // 当cookie的值与参数name（登录凭证为uuid）
                if(cookie.getName().equals(name)){
                    return cookie.getValue();

                }
            }
        }
        return null;
    }
}
