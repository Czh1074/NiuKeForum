package com.chenzhihui.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenzhihui.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Update;

/**
 * 登录链接类
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/1 17:58
 **/

public interface LoginTicketMapper extends BaseMapper<LoginTicket> {

    // 增加一条登录凭证

    // 通过ticket查找LoginTicket对象


    // 通过登录凭证，修改用户状态
    @Update({
            "update login_ticket set status = #{status} where ticket = #{ticket}"
    })
    int updateStatus(String ticket, int status);
}
