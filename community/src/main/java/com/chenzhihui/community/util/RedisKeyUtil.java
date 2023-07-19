package com.chenzhihui.community.util;

/**
 * RedisKeyUtil
 *
 * @Author: ChenZhiHui
 * @DateTime: 2023/7/18 14:17
 **/
public class RedisKeyUtil {

    public static final String SPLIT = ":";
    public static final String  PREFIX_ENTITY_LIKE = "like:entity";
    public static final String  PREFIX_USER_LIKE = "like:user";


    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + entityType + SPLIT + entityId;
    }

    // 用户收到的点赞 like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }


}
