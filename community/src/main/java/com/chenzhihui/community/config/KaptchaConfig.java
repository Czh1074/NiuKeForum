package com.chenzhihui.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import java.util.Properties;

/**
 * 图像验证码生成类
 * @Author: ChenZhiHui
 * @DateTime: 2023/6/1 15:16
 **/

@Configuration
public class KaptchaConfig {

    // 这个Bean的作用，主要是用来进行对验证码的一些参数配置
    @Bean
    public Producer kaptchaProducer() {

        // 设置如：图像宽高、文字大小、字符范围、字符长度、要不要加噪声
        Properties properties = new Properties();
        properties.setProperty("kaptcha.iamge.width", "100");
        properties.setProperty("kaptcha.iamge.height", "40");
        properties.setProperty("kaptcha.textproducer.font.size", "32");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ZXCVBNMASDFGHJKLQWERTYUIOP");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;

    }
}
