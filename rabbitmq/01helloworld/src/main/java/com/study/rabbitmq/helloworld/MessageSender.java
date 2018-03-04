package com.study.rabbitmq.helloworld;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MessageSender {

    public static final String QNAME = "helloworld";

    public void sendMsg(String msg) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            // 声明一个队列
            /**
             * queue: 队列名称
             * durable: 是否持久化
             * exclusive: 是否是排他性队列，只对当前连接有效，断开后队列删除
             * autoDelete: 自动删除，在最后一个连接断开后删除队列
             * arguments: 其他参数
             */
            channel.queueDeclare(QNAME, false, false, false, null);
            channel.basicPublish("", QNAME, null, msg.getBytes());
            System.out.println("sendMsg:" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (null != channel) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
