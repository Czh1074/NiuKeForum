package com.chenzhihui.community;

import com.chenzhihui.community.util.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

/**
 * Mail测试类
 * @Author: ChenZhiHui
 * @DateTime: 2023/5/30 12:44
 **/
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private TemplateEngine templateEngine;


    // 测试简单发送
    @Test
    public void MailTest() throws MessagingException {

        mailUtil.sendMail("czh1074@163.com", "问候", "你好啊");

    }

    // 测试发送thymeleaf模版
    @Test
    public void testHtmlMail() throws MessagingException {
        Context context = new Context();
        context.setVariable("username","黄雅婷");
        String process = templateEngine.process("/mail/demo", context);
        System.out.println(process);
        mailUtil.sendMail("czh1074@163.com","HTMl", process);


    }
}
