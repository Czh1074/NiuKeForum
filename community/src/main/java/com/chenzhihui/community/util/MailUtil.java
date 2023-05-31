package com.chenzhihui.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * 邮件发送工具类
 * @Author: ChenZhiHui
 * @DateTime: 2023/5/30 12:23
 **/
@Component
public class MailUtil {

    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;


    // 大致的过程：1、MimeMessage充当MimeMessageHelper的信息发送类
    //           2、通过对helper设置发送方、接收方、发送标题、内容
    //           3、由于内容使用模版，故需要通过context进行封装，使用templateEngine进行处理
    //           4、最后使用setText将封装好对模版，用helper进行接收
    //           5、最最后，使用mailSender发送helper的信息
    public void sendMail(String to, String subject, String content) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true); // 开启html格式发送
        // 内容使用Thymeleaf需要封装
        mailSender.send(mimeMessageHelper.getMimeMessage());
    }



}
