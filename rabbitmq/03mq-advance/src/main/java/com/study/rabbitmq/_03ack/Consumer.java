package com.study.rabbitmq._03ack;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer {

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

    String queue = "02exchange.direct.queue";
    boolean autoAck = false;
    channel.basicConsume(queue, autoAck, new MyConsumer(channel));

  }
}

class MyConsumer extends DefaultConsumer {

  /**
   * Constructs a new instance and records its association to the passed-in channel.
   *
   * @param channel the channel to which this consumer is attached
   */
  public MyConsumer(Channel channel) {
    super(channel);
  }

  @Override
  public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
      byte[] body) throws IOException {

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("consumerTag: " + consumerTag
                    + ",\n\renvelope: " + envelope
                    + ",\n\rproperties: " + properties
                    + ",\n\rbody: " + new String(body));
    Map<String, Object> headers = properties.getHeaders();
    System.out.println(headers);
    if(null == headers){
      headers = new HashMap<String, Object>();
    }
    Integer retryTimes = (Integer) headers.get("retryTimes");
    if(null == retryTimes){
      retryTimes = 0;
    }
    retryTimes++;
    headers.put("retryTimes", retryTimes);
    properties.builder().headers(headers);
    // nack
    getChannel().basicNack(envelope.getDeliveryTag(), false, true);
    // ack
    // getChannel().basicAck(envelope.getDeliveryTag(), false);
  }
}