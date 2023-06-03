package com.chenzhihui.community;

import com.chenzhihui.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 敏感词测试类
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/3 20:35
 **/

@SpringBootTest
public class SensitiveFilterTest {


    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void sensitiveTest() {
        String s1 = "我现在在赌博，你在嫖娼，他在吸毒！！！";
        String filter = sensitiveFilter.filter(s1);
        System.out.println(filter);
    }

}
