package com.chenzhihui.community.util;

import com.chenzhihui.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户的信息，用户代替session对象
 * 主要包括用户设置和用户获取以及用户清除三部分
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/2 00:33
 **/
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
