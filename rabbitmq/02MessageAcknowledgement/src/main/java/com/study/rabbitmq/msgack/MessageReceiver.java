package com.study.rabbitmq.msgack;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class MessageReceiver {

    private String receiverName;

    public MessageReceiver(String receiverName) {
        super();
        this.receiverName = receiverName;
    }

    public void receiveMsg(String queueName) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);

            // 在同一时间最多处理一条消息，直到消息确认
            channel.basicQos(1);
            Consumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
                        byte[] body) throws IOException {
                    String msg = new String(body);
                    try {
                        System.out.println(receiverName + " receive msg: " + msg);
                        int r = new Random().nextInt(10);
                        if(r % 7 == 0) {
                            throw new Exception("test exp");
                        }
                        Thread.sleep(1000);
                        // 手动确认消息接收
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(receiverName + " exception with msg: " + msg);
                    } finally{
                    }
                }
            };

            // 采用手动消息确认
            channel.basicConsume(queueName, false, consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
