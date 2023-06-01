package com.chenzhihui.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhihui.community.entity.LoginTicket;
import com.chenzhihui.community.mapper.LoginTicketMapper;
import com.chenzhihui.community.service.LoginTicketService;
import org.springframework.stereotype.Service;

/**
 * 登录凭证实现类
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/1 18:05
 **/
@Service("loginTicketServiceImpl")
public class LoginTicketServiceImpl extends ServiceImpl<LoginTicketMapper, LoginTicket> implements  LoginTicketService{


    @Override
    public int updateStatus(String ticket, int status) {
        return 0;
    }
}
