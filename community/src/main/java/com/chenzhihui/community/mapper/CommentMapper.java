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
     * 通过类型和id来查找对应的评论列表
     *
     * @param entityType,entityId,offset,limit 帖子类型、帖子id、便宜量、每页最大值
     * @return List<Comment> Comment列表
     */
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);


    /**
     * 通过类型和id查找回复数量
     *
     * @param entityType,entityId 帖子类型、帖子id
     * @return int 结果条数
     */
    int selectCountByEntity(int entityType, int entityId);

    /**
     * 通过类型和帖子id、评论内容添加评论
     *
     * @param discussPostId,content 帖子Id、评论内容
     * @return int 插入成功条数
     */
    int addComment(Comment comment);

    /**--------------------------------------------------------------------------------------------------------------**/


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

