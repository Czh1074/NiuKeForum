package com.chenzhihui.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhihui.community.constant.CommunityConstant;
import com.chenzhihui.community.entity.Comment;
import com.chenzhihui.community.entity.DiscussPost;
import com.chenzhihui.community.entity.User;
import com.chenzhihui.community.mapper.CommentMapper;
import com.chenzhihui.community.mapper.DiscussPostMapper;
import com.chenzhihui.community.service.CommentService;
import com.chenzhihui.community.util.HostHolder;
import com.chenzhihui.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-05-19 23:46:44
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService, CommunityConstant {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        IPage<Comment> page = new Page<>(offset, limit);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entity_type", entityType);
        queryWrapper.eq("entity_id", entityId);
        IPage<Comment> comments = commentMapper.selectPage(page, queryWrapper);
        // 将comment从数据里提取出来
        ArrayList<Comment> commentLists = new ArrayList<>();
        for(Comment comment : comments.getRecords()) {
            commentLists.add(comment);
        }
        return commentLists;
    }

    @Override
    public int selectCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public int addComment(Comment comment) {
        // 1、查找帖子、将帖子回帖数量+1
        DiscussPost discussPost = discussPostMapper.selectDiscussPostById(comment.getTargetId());
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            discussPostMapper.updateDiscussCount(comment.getTargetId(), discussPost.getCommentCount() + 1);
        }
        discussPostMapper.updateDiscussCount(comment.getTargetId(), discussPost.getCommentCount() + 1);
        // 2、通过hostHolder查找当前用户，获取用户id，实现comment信息的添加
        User user = hostHolder.getUser();
        if(user == null){
            throw new IllegalArgumentException("现在还未登录！");
        }
        comment.setUserId(user.getId());
        comment.setEntityType(comment.getEntityType());
//        comment.setEntityId(comment.getEntityId());
//        if (comment.getEntityId() == ENTITY_TYPE_COMMENT) {
//            comment.setTargetId(comment.getEntityId());
//        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        comment.setCreateTime(new Date());
        System.out.println("comment的用户id = "  + comment.getUserId());
        return commentMapper.addComment(comment);
    }

    @Override
    public Comment findCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }
}

