package com.study.rabbitmq.msgack;

import org.junit.Test;

public class MessageAcknowledgementTest {

    private static final String QNAME = MessageSender.QNAME;

    @Test
    public void sendMsg() throws InterruptedException {
        int n = 0;
        MessageSender sender = new MessageSender();

        while (true) {
            sender.sendMsg("hello, " + n++);
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            MessageReceiver receiver = new MessageReceiver("R" + i);
            receiver.receiveMsg(QNAME);
        }

    }
}
