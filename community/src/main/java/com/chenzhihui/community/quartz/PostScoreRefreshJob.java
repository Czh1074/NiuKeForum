package com.chenzhihui.community.quartz;

import com.chenzhihui.community.constant.CommunityConstant;
import com.chenzhihui.community.entity.DiscussPost;
import com.chenzhihui.community.service.DiscussPostService;
import com.chenzhihui.community.service.LikeService;
import com.chenzhihui.community.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务：刷新帖子分数
 *
 * @Author: ChenZhiHui
 * @DateTime: 2024/2/29 19:59
 **/
public class PostScoreRefreshJob implements Job, CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(PostScoreRefreshJob.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private LikeService likeService;

    // 牛客纪元
    private static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化牛客纪元失败!", e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 获取需要刷新分数的帖子
        String redisKey = RedisKeyUtil.getPostScoreKey();
        // 获取redis操作对象
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);

        if (operations.size() == 0) {
            logger.info("[任务取消] 没有需要刷新的帖子!");
            return;
        }

        logger.info("[任务开始] 正在刷新帖子分数: " + operations.size());
        while (operations.size() > 0) {
            this.refresh((Integer) operations.pop());
        }
        logger.info("[任务结束] 帖子分数刷新完毕!");
    }

    private void refresh(int postId) {
        // 从数据库中查询帖子
        DiscussPost post = discussPostService.selectDiscussPostById(postId);
        if (post == null) {
            logger.error("该帖子不存在: id = " + postId);
            return;
        }
        // 是否精华
//         boolean wonderful = post.getStatus() == 1;
        // 评论数量
        int commentCount = post.getCommentCount();
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);
        // 计算权重
        double w = commentCount * 10 + likeCount * 2;
        // 分数 = 帖子权重 + 发帖时间
        double score = Math.log10(Math.max(w, 1))
                + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);
        // 更新帖子分数
        discussPostService.updateScore(postId, score);
        // todo : es同步搜索数据
//         post.setScore(score);
//         elasticsearchService.saveDiscussPost(post);
    }
}
