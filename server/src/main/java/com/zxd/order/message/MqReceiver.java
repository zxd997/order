package com.zxd.order.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接受mq消息
 */
@Slf4j
@Component
public class MqReceiver {
    //1.需要手动创建queue
    //@RabbitListener(queues = "myQueue")
    //2.自动创建队列
   /* @RabbitListener(queuesToDeclare = @Queue("myQueue"))*/
    //3.自动创建队列 Exchange和Queue绑定

    /**
     * 数码供应商服务 接受消息
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("myOrder"),
            key = "computer",
            value = @Queue("computerOrder")
    ))
    public void process(String msg){
        log.info("computer MqReceiver:{}",msg);
    }
    /**
     * 水果供应商服务 接受消息
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("myOrder"),
            key = "fruit",
            value = @Queue("fruitOrder")
    ))
    public void process1(String msg){
        log.info("fruit MqReceiver:{}",msg);
    }
}
