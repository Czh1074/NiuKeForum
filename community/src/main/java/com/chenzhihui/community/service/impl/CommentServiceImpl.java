package com.chenzhihui.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhihui.community.mapper.CommentMapper;
import com.chenzhihui.community.entity.Comment;
import com.chenzhihui.community.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-05-19 23:46:44
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

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
}

