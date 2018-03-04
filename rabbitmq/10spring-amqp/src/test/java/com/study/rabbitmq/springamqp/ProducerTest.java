package com.study.rabbitmq.springamqp;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ProducerTest {

    @Autowired
    private Producer producer;
    
    @Test
    public void test() {
        String exchange_key = "myq_exchange";
        String queue_key = "my_patt";
        producer.sendQueue(exchange_key, queue_key, "hello spring-amqp".getBytes());
    }

}
