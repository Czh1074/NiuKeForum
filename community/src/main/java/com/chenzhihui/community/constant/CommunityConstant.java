package com.chenzhihui.community.constant;

/**
 * 激活状态接口
 * @Author: ChenZhiHui
 * @DateTime: 2023/5/30 22:03
 **/
public interface CommunityConstant {

    // 激活成功
    int ACTIVATION_SUCCESS = 0;

    // 重复激活
    int ACTIVATION_REPEAT = 1;

    // 激活失败
    int ACTIVATION_FAILURE = 2;

    // 默认状态的登录凭证的超时时间
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    // 记住我状态下的登录凭证超时时间
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 5;

    // 实体类型：帖子
    int ENTITY_TYPE_POST = 1;

    // 实体类型：评论
    int ENTITY_TYPE_COMMENT = 2;

    // 实体类型：用户
    int ENTITY_TYPE_USER = 3;

    // 事件类型：评论
    String TOPIC_COMMENT = "comment";

    // 事件类型：点赞
    String TOPIC_LIKE = "like";

    // 事件类型：关注
    String TOPIC_FOLLOW = "follow";

    // 系统用户
    int SYSTEM_USER_ID = 1;


}
