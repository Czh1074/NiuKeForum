package com.chenzhihui.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 关注接口
 *
 * @Author: ChenZhiHui
 * @DateTime: 2023/7/19 14:43
 **/

public interface FollowService {

    public void follow(int userId, int entityType, int entityId);

    public void unfollow(int userId, int entityType, int entityId);

    public long findFolloweeCount(int userId, int entityType);

    public long findFollowerCount(int entityType, int entityId);

    public boolean hasFollowed(int userId, int entityType, int entityId);

    public List<Map<String, Object>> findFollowees(int userId, int offset, int limit);

    public List<Map<String, Object>> findFollowers(int userId, int offset, int limit);

}
