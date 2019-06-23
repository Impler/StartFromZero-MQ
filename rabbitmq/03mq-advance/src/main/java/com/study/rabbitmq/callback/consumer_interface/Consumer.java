package com.study.rabbitmq.callback.consumer_interface;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
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
    channel.basicConsume(queue, new MyConsumer(channel));

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

    System.out.println("consumerTag: " + consumerTag
                    + ",\n\renvelope: " + envelope
                    + ",\n\rproperties: " + properties
                    + ",\n\rbody: " + new String(body));
    getChannel().basicAck(envelope.getDeliveryTag(), false);
  }
}