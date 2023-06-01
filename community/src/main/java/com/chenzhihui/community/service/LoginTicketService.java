package com.chenzhihui.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhihui.community.entity.LoginTicket;

/**
 * 登录凭证接口
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/1 18:04
 **/
public interface LoginTicketService extends IService<LoginTicket> {

    // 修改登录状态
    int updateStatus(String ticket, int status);
}
