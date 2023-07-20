package com.chenzhihui.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhihui.community.entity.LoginTicket;
import com.chenzhihui.community.mapper.LoginTicketMapper;
import com.chenzhihui.community.service.LoginTicketService;
import com.chenzhihui.community.util.RedisKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录凭证实现类
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/1 18:05
 **/
@Service("loginTicketServiceImpl")
public class LoginTicketServiceImpl extends ServiceImpl<LoginTicketMapper, LoginTicket> implements  LoginTicketService{

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public int updateStatus(String ticket, int status) {
        return 0;
    }

    public LoginTicket findLoginTicket(String ticket) {
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
    }
}
