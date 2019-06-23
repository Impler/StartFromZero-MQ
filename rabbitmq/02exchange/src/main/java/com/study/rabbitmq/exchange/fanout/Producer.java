package com.study.rabbitmq.exchange.fanout;

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

    String exchange = "exchange.fanout";
    // 声明exchange
    channel.exchangeDeclare(exchange, "fanout");
    // 声明queue
    String queue = "02exchange.fanout.queue";
    channel.queueDeclare(queue, true, false, false, null);
    // 绑定, rk随便指定
    String rk = "xxxx";
    channel.queueBind(queue, exchange, rk);
    String msg = "hello rabbitmq -- fanout";
    channel.basicPublish(exchange, rk, null, msg.getBytes());
    System.out.println("发送消息：" + msg);
    channel.close();
    connection.close();
  }
}
