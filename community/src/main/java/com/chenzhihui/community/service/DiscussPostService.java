package com.chenzhihui.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhihui.community.entity.DiscussPost;

import java.util.List;

/**
 * (DiscussPost)表服务接口
 *
 * @author makejava
 * @since 2023-05-19 23:47:05
 */
public interface DiscussPostService extends IService<DiscussPost> {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

    List<DiscussPost> selectAllDiscussPosts(int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateDiscussPostLikeCount(int postId, int likeCount);





}

