package com.alipay.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageTest {

    //发送端
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void send() {

        amqpTemplate.convertAndSend("MyQueue", "now" + new Date());
        amqpTemplate.convertAndSend("MyQueue", "now" + new Date());
        amqpTemplate.convertAndSend("MyQueue", "now" + new Date());
        amqpTemplate.convertAndSend("MyQueue", "now" + new Date());

    }

    @Test
    public void sendOrder() {

        amqpTemplate.convertAndSend("myOrder", "furit", "now" + new Date());

    }
}
