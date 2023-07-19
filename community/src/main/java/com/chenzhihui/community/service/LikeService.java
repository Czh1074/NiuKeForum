package com.chenzhihui.community.service;

/**
 * 点赞实现接口
 *
 * @Author: ChenZhiHui
 * @DateTime: 2023/7/18 14:21
 **/

public interface LikeService {

    public void like(int userId, int entityType, int entityId, int entityUserId);

    public long findEntityLikeCount(int entityType, int entityId);

    public int findEntityLikeStatus(int userId, int entityType, int entityId);

    public int findUserLikeCount(int userId);


}
