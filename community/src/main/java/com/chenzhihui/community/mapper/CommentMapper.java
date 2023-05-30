package com.chenzhihui.community.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.chenzhihui.community.entity.Comment;

/**
 * (Comment)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-19 23:46:44
 */
public interface CommentMapper extends BaseMapper<Comment> {

/**
* 批量新增数据（MyBatis原生foreach方法）
*
* @param entities List<Comment> 实例对象列表
* @return 影响行数
*/
int insertBatch(@Param("entities") List<Comment> entities);

/**
* 批量新增或按主键更新数据（MyBatis原生foreach方法）
*
* @param entities List<Comment> 实例对象列表
* @return 影响行数
* @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
*/
int insertOrUpdateBatch(@Param("entities") List<Comment> entities);

}

