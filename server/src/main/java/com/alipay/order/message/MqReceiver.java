package com.alipay.order.message;


import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MqReceiver {

    //1. @RabbitListener(queues = "MyQueue")
    //2.自动创建队列
    @RabbitListener(queuesToDeclare = @Queue("MyQueue"))
    public void process(String message) {
        System.out.println("1." + message);
    }

    @RabbitListener(queuesToDeclare = @Queue("MyQueue"))
    public void process2(String message) {
        System.out.println("2." + message);
    }

    /**
     * 数码供应商
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("myOrder"),
            key = "computer",
            value = @Queue("computer")
    ))
    public void processComputer(String message) {
        System.out.println("超前一个版本" + message);
    }


    /**
     * 水果供应商
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("myOrder"),
            key = "furit",
            value = @Queue("fruit")
    ))
    public void processFruit(String message) {
        System.out.println("超前一个版本" + message);
    }
}
