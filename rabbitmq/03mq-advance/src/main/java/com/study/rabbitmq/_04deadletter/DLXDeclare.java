package com.study.rabbitmq._04deadletter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class DLXDeclare {
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

    // 死信交换机exchange 名称
    String dlxExchange = "exchange.direct.dlx";
    channel.exchangeDeclare(dlxExchange, "direct");

    // 声明死信队列
    String dlxQueue = "03dlx.queue";
    channel.queueDeclare(dlxQueue, true, false,false, null);
    channel.queueBind(dlxQueue, dlxExchange, dlxQueue);

    // 普通交换机
    String exchange = "exchange.direct";
    Map<String, Object> queueArgus = new HashMap<>();
    queueArgus.put("x-dead-letter-exchange", dlxExchange);

    // 声明ttl队列
    String ttlQueue = "03ttl.queue";
    channel.queueDeclare(ttlQueue, true, false,false, queueArgus);
    channel.queueBind(ttlQueue, exchange, ttlQueue);

    // 声明maxlength队列
    String maxLengthQueue = "03maxLen.queue";
    channel.queueDeclare(maxLengthQueue, true, false,false, queueArgus);
    channel.queueBind(maxLengthQueue, exchange, maxLengthQueue);

    // 声明reject队列
    String rejectQueue = "03reject.queue";
    channel.queueDeclare(rejectQueue, true, false,false, queueArgus);
    channel.queueBind(rejectQueue, exchange, rejectQueue);

  }
}
