package com.study.rabbitmq.exchange.topic;

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

    // 创建通道
    Channel channel = connection.createChannel();

    String exchange = "exchange.topic";
    String exchangeType = "topic";
    // 声明交换机
    channel.exchangeDeclare(exchange, exchangeType, true, false, null);

    // 声明队列
    String queueName = "02exchange.topic.queue";
    channel.queueDeclare(queueName, true, false, false, null);

    // 绑定队列与交换机
    String routingKey = "02exchange.topic.*";
    channel.queueBind(queueName, exchange, routingKey);

    String msg = "Hello RabbitMQ";
    channel.basicPublish(exchange, routingKey, null, msg.getBytes());
    System.out.println("发送消息：" + msg);

    channel.close();
    connection.close();
  }
}
