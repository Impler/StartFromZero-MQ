package com.study.rabbitmq.springamqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService implements MessageListener {
    public void onMessage(Message message) {
        System.out.println("消息消费者 = " + message.toString());
    }
}