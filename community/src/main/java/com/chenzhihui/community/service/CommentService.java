package com.chenzhihui.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenzhihui.community.entity.Comment;

import java.util.List;

/**
 * (Comment)表服务接口
 *
 * @author makejava
 * @since 2023-05-19 23:46:44
 */
public interface CommentService extends IService<Comment> {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int addComment(Comment comment);

}

