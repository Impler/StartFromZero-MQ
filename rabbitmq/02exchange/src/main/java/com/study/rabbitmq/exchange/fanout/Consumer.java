package com.study.rabbitmq.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

  public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
    // 创建连接工厂
    ConnectionFactory connFactory = new ConnectionFactory();
    connFactory.setHost("192.168.32.10");
    connFactory.setPort(5672);
    connFactory.setUsername("admin");
    connFactory.setPassword("admin");
    connFactory.setVirtualHost("/rmq_study");


    // 创建连接
    Connection connection = connFactory.newConnection();

    Channel channel = connection.createChannel();

    String queue = "02exchange.fanout.queue";
    channel.queueDeclare(queue, true, false, false, null);

    QueueingConsumer consumer = new QueueingConsumer(channel);

    channel.basicConsume(queue, true, consumer);

    while(true){
      Delivery delivery = consumer.nextDelivery();
      System.out.println("接收到消息：" + new String(delivery.getBody()));
    }
  }
}
