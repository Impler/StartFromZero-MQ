package com.study.rabbitmq.callback.receive_confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
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

    // 指定消息确认模式
    channel.confirmSelect();

    String exchange = "exchange.direct";
    String routingKey = "02exchange.direct.rk";

    String msg = "Hello RabbitMQ";
    channel.basicPublish(exchange, routingKey, null, msg.getBytes());
    System.out.println("发送消息：" + msg);

    // 添加消息确认监听
    channel.addConfirmListener(
        new ConfirmListener() {
          @Override
          public void handleAck(long deliveryTag, boolean multiple) throws IOException {
            System.out.println("ack--" + deliveryTag);
          }

          @Override
          public void handleNack(long deliveryTag, boolean multiple) throws IOException {
            System.out.println("nack--" + deliveryTag);
          }
        });

  }
}
