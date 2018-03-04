package com.study.rabbitmq.helloworld;

import org.junit.Test;

public class HelloworldTest {

    private static String queueName = MessageSender.QNAME;
    
    @Test
    public void testSendMsg() throws InterruptedException {
        MessageSender sender = new MessageSender();
        while(true) {
            sender.sendMsg("hello, world" + System.currentTimeMillis());
            Thread.sleep(500);
        }
    }
    
    
    public static void main(String[] args) {
        // 消费者创建后会一直等待消息进来，因此此处使用main方法调用
        for(int i=0; i<5; i++) {
            MessageReceiver receiver = new MessageReceiver("R" + i);
            receiver.receiveMsg(queueName);
        }
    }
}
