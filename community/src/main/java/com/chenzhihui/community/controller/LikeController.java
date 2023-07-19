package com.chenzhihui.community.controller;

import com.chenzhihui.community.entity.User;
import com.chenzhihui.community.service.LikeService;
import com.chenzhihui.community.util.CommunityUtil;
import com.chenzhihui.community.util.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Priority;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 点赞实现控制层
 *
 * @Author: ChenZhiHui
 * @DateTime: 2023/7/18 14:31
 **/

@Controller
public class LikeController {

    @Resource
    private LikeService likeService;

    @Resource
    private HostHolder hostHolder;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId) {
        User user = hostHolder.getUser();

        // 实现点赞
        likeService.like(user.getId(), entityType, entityId);
        // 数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
        // 将数量和状态进行封装，然后传递给前端
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        return CommunityUtil.getJsonString(0,null, map);

    }

}
