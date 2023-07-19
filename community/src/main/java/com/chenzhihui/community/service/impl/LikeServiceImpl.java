package com.chenzhihui.community.service.impl;

import com.chenzhihui.community.service.LikeService;
import com.chenzhihui.community.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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

    public void like(int userId, int entityType, int entityId, int entityUserId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 设置key
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                // 判断是否当前用户是否点赞
                Boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);
                // 执行事务，保证isMember能够成功执行方法得到查询 -> 数据一致性
                operations.multi();
                if (isMember) {
                    // 执行点赞和用户得到点赞数操作
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });
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

    // 查询某个用户获得的赞
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        log.info("当前查询用户组合redis键值key是：" + userLikeKey);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        log.info("输出查询到的count = " + count);
        return count == null ? 0 : count.intValue();
    }

}
