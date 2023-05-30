package com.chenzhihui.community.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.chenzhihui.community.entity.User;

/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-19 23:43:01
 */
public interface UserMapper extends BaseMapper<User> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<User> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<User> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<User> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<User> entities);

//    /**
//     * 通过id查找用户
//     *
//     * @param id int 用户id
//     * @return User
//     */
//    User selectById(int id);

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

