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

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + entityType + SPLIT + entityId;
    }

}
