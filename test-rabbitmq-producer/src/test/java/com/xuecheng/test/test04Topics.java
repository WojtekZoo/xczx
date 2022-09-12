package com.xuecheng.test;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author： WojtekZoo
 * @date： 2022/8/30 10:21
 * @modifiedBy：
 */
public class test04Topics {
    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    //routingKey
//    private static final String ROUTING_KEY_EMAIL = "routing_key_email";
//    private static final String ROUTING_KEY_SMS = "routing_key_sms";
    //交换机名称
    private static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";

    public static void main(String[] args) {
        //1 创建连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");//使用默认虚拟机，虚拟机相当于一个独立的mq服务器

        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();//创建与RabbitMQ服务的TCP连接
            //2 创建通道
            channel = connection.createChannel();//创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务
            /**
             * 3 声明交换机 String exchange, BuiltinExchangeType type
             *
             * *参数明细
             *  1、交换机名称
             * *2、交换机类型，fanout、topic、direct、headers
             * fanout 发布订阅模式
             * topic 通配符路由模式
             * direct 路由模式
             */
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
            //声明队列
            // (String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String,Object > arguments)
            /**
             * 参数明细：
             * 1、队列名称
             * 2、是否持久化
             * 3、是否独占此队列
             * 4、队列不用是否自动删除
             * 5、参数
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            //发送消息
            //发送邮件消息
            for (int i = 0; i < 10; i++) {
                String message = "email inform to user" + i;
                //向交换机发送消息 String exchange, String routingKey, BasicProperties props,byte[] body
                /**
                 * 参数明细
                 * 1、交换机名称，不指令使用默认交换机名称 Default Exchange
                 * 2、routingKey（路由key），根据key名称将消息转发到具体的队列，这里填写队列名称表示消
                 息将发到此队列
                 * 3、消息属性
                 * 4、消息内容
                 */
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.email", null, message.getBytes(StandardCharsets.UTF_8));
            }
            //发送短信消息
            for (int i = 0; i < 10; i++) {
                String message = "sms inform to user" + i;
                //向交换机发送消息 String exchange, String routingKey, BasicProperties props,byte[] body
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.sms", null, message.getBytes(StandardCharsets.UTF_8));
            }
            for (int i = 0; i < 10; i++) {
                String message = "sms + email inform to user" + i;
                //向交换机发送消息 String exchange, String routingKey, BasicProperties props,byte[] body
                channel.basicPublish(EXCHANGE_TOPICS_INFORM, "inform.sms.email", null, message.getBytes(StandardCharsets.UTF_8));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //生产者 可关闭 通道，连接
            try {
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            try {
                connection.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
