package com.study.rabbitmq._01producerconsumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

  public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {


    // 创建ConnectionFactory
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("192.168.32.10");
    connectionFactory.setPort(5672);
    connectionFactory.setUsername("admin");
    connectionFactory.setPassword("admin");
    connectionFactory.setVirtualHost("/rmq_study");

    // 创建Connection
    Connection connection = connectionFactory.newConnection();

    // 创建Channel
    Channel channel = connection.createChannel();

    // 声明队列
    String queue = "01producerconsumer";
    channel.queueDeclare(queue, true, false, false, null);

    // 创建消费者
    QueueingConsumer consumer = new QueueingConsumer(channel);

    // 设置channel
    channel.basicConsume(queue, true, consumer);

    while(true){
      Delivery delivery = consumer.nextDelivery();
      String msg = new String(delivery.getBody());

      System.out.println("消费消息：" + msg);
    }

  }
}
