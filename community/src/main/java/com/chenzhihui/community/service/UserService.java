package com.chenzhihui.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhihui.community.entity.User;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2023-05-19 23:43:03
 */
public interface UserService extends IService<User> {


    /**
     * 实现用户注册逻辑
     *
     * @param user 用户信息（主要是账号、密码）
     * @return Map 返回一些判断信息
     */
    Map<String, Object> register(User user) throws MessagingException;


    /**
     * 实现用户激活判断
     *
     * @param id,code 用户id、激活码
     * @return int 0、1、2 成功、重复、失败
     */
    int activation(Integer id, String code);


    /**
     * 通过id查找用户
     *
     * @param id int 用户id
     * @return User
     */
    User selectById(int id);

    /**
     * 登录测试
     *
     * @param username,password,expiredSeconds 用户名称、密码、过期时间
     * @return Map 判断消息
     */
    Map<String, Object> login(String username, String password, int expiredSeconds);

    /**
     * 退出登录
     *
     * @param ticket 登录凭证
     */
    void logout(String ticket);


    /** -------------------------------------------------------------------------------------------------------------**/



    /**
     * 通过username查找用户
     *
     * @param username String 用户id
     * @return User
     */
    User selectByName(String username);

    /**
     * 通过email查找用户
     *
     * @param email String 用户id
     * @return User
     */
    User selectByEmail(String email);

    /**
     * 新增用户
     *
     * @param user User 用户信息
     * @return 是否插入成功
     */
    int insertUser(User user);

    /**
     * 修改用户状态
     *
     * @param id,status int 用户id和用户状态
     * @return 是否修改成功
     */
    int updateStatus(int id, int status);

    /**
     * 修改用户头像
     *
     * @param id,headUrl int,String 用户id和用户头像url地址
     * @return 是否修改成功
     */
    int updateHeader(int id, String headerUrl);

    /**
     * 修改用户密码
     *
     * @param id,password int,String 用户id和用户密码
     * @return 是否修改成功
     */
    int updatePassword(int id, String password);

}

