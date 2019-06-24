package com.study.rabbitmq._01callback.return_listener;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;
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

    String exchange = "exchange.direct";

    // 指定路由不了的rk
    String routingKey = "foo";
    String msg = "Hello RabbitMQ";
    // mandatory必须指定为true，表示路由不了的消息返回给生产者，否则broker会自动删除该消息
    boolean mandatory = true;
    channel.basicPublish(exchange, routingKey, mandatory, null, msg.getBytes());
    System.out.println("发送消息：" + msg);

    channel.addReturnListener(
        new ReturnListener() {
          @Override
          public void handleReturn(
              int replyCode,
              String replyText,
              String exchange,
              String routingKey,
              BasicProperties properties,
              byte[] body)
              throws IOException {

            System.out.println("replyCode: " + replyCode
                + ", replyText: " + replyText
                + ", body: " + new String(body));
          }
        });
  }
}
