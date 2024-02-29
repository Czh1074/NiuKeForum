package com.chenzhihui.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhihui.community.entity.Comment;
import com.chenzhihui.community.entity.DiscussPost;
import com.chenzhihui.community.entity.Pages;
import com.chenzhihui.community.entity.resp.ReplyPostResult;
import com.chenzhihui.community.mapper.CommentMapper;
import com.chenzhihui.community.mapper.DiscussPostMapper;
import com.chenzhihui.community.service.DiscussPostService;
import com.chenzhihui.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (DiscussPost)表服务实现类
 *
 * @author makejava
 * @since 2023-05-19 23:47:05
 */
@Service("discussPostService")
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost> implements DiscussPostService {

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Resource
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode) {
        IPage<DiscussPost> page = new Page<>(offset, limit);
        QueryWrapper<DiscussPost> queryWrapper = new QueryWrapper<>();
        if(userId != 0){
            queryWrapper.eq("user_id",userId);
        }
        if (orderMode == 0) { // 最新，按照时间排序首页数据
            queryWrapper.orderByDesc("create_time");
        } else {
            queryWrapper.orderByDesc("score");
        }
        IPage<DiscussPost> discussPostIPage = discussPostMapper.selectPage(page, queryWrapper);
        // 将discussPostIPage里的数据提提取出来
        List<DiscussPost> discussPosts = new ArrayList<>();
        int count = 0;
        for (DiscussPost discussPost : discussPostIPage.getRecords()){
            discussPosts.add(discussPost);
            count++;
        }
        System.out.println("总共查出来几条数据：" + count);
        return discussPosts;
    }

    @Override
    public List<DiscussPost> selectAllDiscussPosts(int userId) {
        // 获得userID下的所有信息
        QueryWrapper<DiscussPost> queryWrapper = new QueryWrapper<>();
        if (userId != 0) {
            queryWrapper.eq("user_id", userId);
        }
        List<DiscussPost> discussPosts = discussPostMapper.selectList(queryWrapper);
        return discussPosts;
    }

    @Override
    public int insertDiscussPost(DiscussPost discussPost) {
        // 1、判断传递进来的discussPost是否为空
        if (discussPost == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        // 2、转义HTML标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        // 3、过滤敏感词
        String title = sensitiveFilter.filter(discussPost.getTitle());
        String content = sensitiveFilter.filter(discussPost.getContent());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        // 4、执行插入
        return discussPostMapper.insertDiscussPost(discussPost);
    }

    @Override
    public DiscussPost selectDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    @Override
    public int updateDiscussPostLikeCount(int postId, int likeCount) {
        return discussPostMapper.updateLikeCount(postId, likeCount);
    }

    @Override
    public List<ReplyPostResult> findReplyDiscussPosts(int userId, int offset, int limit) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("entity_type", 1);
        queryWrapper.ne("status", 2);
        Pages pages = new Pages();
        pages.setCurrent(offset);
        pages.setLimit(limit);
        List<Comment> commentList = commentMapper.selectList(queryWrapper);

        List<ReplyPostResult> replyPostResults = new ArrayList<>();

        for (Comment comment : commentList) {
            ReplyPostResult replyPostResult = new ReplyPostResult();
            replyPostResult.setReplyContent(comment.getContent());
            DiscussPost discussPost = discussPostMapper.selectDiscussPostById(comment.getEntityId());
            replyPostResult.setReplyCreateTime(comment.getCreateTime());
            replyPostResult.setPostTitle(discussPost.getTitle());
            replyPostResult.setPostId(discussPost.getId());
            replyPostResults.add(replyPostResult);
        }
        return replyPostResults;

    }
}

