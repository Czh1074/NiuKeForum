package com.chenzhihui.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenzhihui.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (DiscussPost)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-19 23:47:05
 */
public interface DiscussPostMapper extends BaseMapper<DiscussPost> {

    /**
     * 分页查询帖子信息
     *
     * @param userId,offset,limit 用户id、偏移量、每页最大数目
     * @return List<DiscussPost> 实例对象列表
     */
//    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

    /**
     * 查询帖子列表
     *
     * @param userId 用户id
     * @return List<DiscussPost> 实例对象列表
     */
//    List<DiscussPost> selectAllDiscussPosts(int userId);

    /**
     * 发布帖子
     *
     * @param discussPost 帖子信息
     * @return int 插入成功的条数
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 查询帖子信息
     *
     * @param id 帖子id
     * @return DiscussPost 帖子信息
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 修改帖子的评论数量
     *
     * @param discussPostId,count 帖子Id
     * @return int 修改的条数
     */
    int updateDiscussCount(int discussPostId, int count);


    /**
     * 修改帖子的点赞数量
     *
     * @param discussPostId,count 帖子Id
     * @return int 修改的条数
     */
    int updateLikeCount(int discussPostId, int count);

    int updateScore(int postId, double score);

    /**
     * 通过用户id和类型查找评论数量
     *
     * @param userId,entityType 用户id、类型
     * @return int 回帖
     */
//    List<ReplyPostResult> selectReplyDiscussPosts(int userId, int offset, int limit);

    /**--------------------------------------------------------------------------------------------------------------**/


    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<DiscussPost> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<DiscussPost> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<DiscussPost> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<DiscussPost> entities);

}

