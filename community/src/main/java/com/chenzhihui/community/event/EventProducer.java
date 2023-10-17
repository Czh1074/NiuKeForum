package com.chenzhihui.community.event;

import com.alibaba.fastjson.JSONObject;
import com.chenzhihui.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 事件生成者
 *
 * @Author: ChenZhiHui
 * @DateTime: 2023/10/17 14:54
 **/
@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    // 处理事件
    public void fireEvent(Event event) {
        // 将事件发布到制定到主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }


}
