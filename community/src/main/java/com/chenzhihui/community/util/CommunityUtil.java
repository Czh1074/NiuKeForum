package com.chenzhihui.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

/**
 * 社区工具类
 * @Author: ChenZhiHui
 * @DateTime: 2023/5/30 15:41
 **/

@Component
public class CommunityUtil {

    // 1、生成随机字符串：主要用在验证码生成以及MD5加密时随机字符串的处理
    // 生成的随机字符串，将"-"替换为""
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    // 2、MD5加密
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            // 如果key为空，直接返回null
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    // 3、实现信息转换成Json字符串
    public static String getJsonString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map != null) {
            for (String key : map.keySet()){
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    // 4、重构方法3，实现参数（code、msg）、（code）
    public static String getJsonString(int code, String msg) {
        return getJsonString(code, msg, null);
    }

    public static String getJsonString(int code) {
        return getJsonString(code, null, null);
    }





}
