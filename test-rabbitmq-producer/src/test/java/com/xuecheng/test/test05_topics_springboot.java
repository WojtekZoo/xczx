package com.xuecheng.test;

import com.xuecheng.manage_cms_client.config.RabbitConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author： WojtekZoo
 * @date： 2022/8/30 16:53
 * @modifiedBy：
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class test05_topics_springboot {
    @Resource
    RabbitTemplate rabbitTemplate;

    @Test
    public void testSendByTopics(){
        for (int i = 0; i < 5; i++) {
            String message = "sms email inform to user"+i;
            //发送消息
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPICS_INFORM, "inform.sms.email", message);
        }
    }

}
