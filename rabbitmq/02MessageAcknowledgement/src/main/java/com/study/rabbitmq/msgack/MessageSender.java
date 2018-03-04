package com.study.rabbitmq.msgack;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MessageSender {

    public static final String QNAME = "MSGACK";

    public void sendMsg(String msg) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            
            Channel channel = connection.createChannel();
            DeclareOk ok = channel.queueDeclare(QNAME, false, false, false, null);
            System.out.println("declareOk : " + ok);
            channel.basicPublish("", QNAME, null, msg.getBytes());
            System.out.println("send msg: " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
