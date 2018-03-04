package com.study.rabbitmq.helloworld;

import java.io.IOException;
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
        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            
            Consumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
                        byte[] body) throws IOException {
                    String msg = new String(body);
                    System.out.println(receiverName + " receive msg: " + msg);
                }
                
            };
            // 消费消息
            /**
             * queue: 队列名称
             * autoAck: auto acknowledgement, 消息接收确认, true表示自动接收确认，并从队列中删除消息
             * callback: 消费回调
             */
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            // 不能关闭连接，调用消费方法后，消费者会一直连着MQ
            /*if (null != connection) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }
}
