package com.xuecheng.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * @author： WojtekZoo
 * @date： 2022/8/30 10:21
 * @modifiedBy：
 */
public class test01 {
    public static final String QUEUE = "helloWorld";

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
             * 3 声明队列，如果Rabbit中没有此队列将自动创建
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此连接
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数
             */
            channel.queueDeclare(QUEUE, true, false, false, null);
            /**
             * 4 消息发布
             * param1：Exchange的名称，如果没有指定，则使用Default Exchange
             * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列
             * param3:消息包含的属性
             * param4：消息体
             * 这里没有指定交换机，消息将发送给默认交换机，每个队列也会绑定那个默认的交换机，但是不能显示绑定或解除绑定
             * 默认的交换机，routingKey等于队列名称
             */
            String message = "测试mq消息发布..." + new Date().getTime();
            channel.basicPublish("", QUEUE, null, message.getBytes(StandardCharsets.UTF_8));

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
