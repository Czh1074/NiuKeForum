package com.chenzhihui.community.service.impl;

import com.chenzhihui.community.service.LikeService;
import com.chenzhihui.community.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 点赞业务实现类
 *
 * @Author: ChenZhiHui
 * @DateTime: 2023/7/18 14:21
 **/

@Service
@Slf4j
public class LikeServiceImpl implements LikeService {

    @Resource
    private RedisTemplate redisTemplate;

    // 点赞
    public void like(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        // 判断是否存在
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if (isMember) {
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }

    // 查询实体点赞的数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    // 查询某人对实体对点赞状态
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        log.info("查询点赞状态是否存在：" + redisTemplate.opsForSet().isMember(entityLikeKey, userId));
        int isIn = redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
        log.info("点赞信息转换：" + isIn);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

}
