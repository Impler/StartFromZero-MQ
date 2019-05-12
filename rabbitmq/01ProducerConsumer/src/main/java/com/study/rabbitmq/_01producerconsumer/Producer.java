package com.study.rabbitmq._01producerconsumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

  public static void main(String[] args) throws IOException, TimeoutException {

    // 创建连接工厂
    ConnectionFactory connFactory = new ConnectionFactory();
    connFactory.setHost("192.168.32.10");
    connFactory.setPort(5672);
    connFactory.setUsername("admin");
    connFactory.setPassword("admin");
    connFactory.setVirtualHost("/rmq_study");
    // 创建连接
    Connection connection = connFactory.newConnection();

    // 创建channel
    Channel channel = connection.createChannel();

    // 通过channel发送消息
    String exchange = "";
    String routingKey = "01producerconsumer";
    AMQP.BasicProperties prop = null;
    String msg = "Hello World";
    channel.basicPublish(exchange, routingKey, prop, msg.getBytes());
    System.out.println("发送消息：" + msg);
    channel.close();
    connection.close();
  }
}
