package com.zxd.order.controller;

import com.zxd.order.dto.OrderDto;
import com.zxd.order.message.StreamClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestController {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StreamClient streamClient;
    @RequestMapping("/test")
    public String get1(){
        amqpTemplate.convertAndSend("myQueue","now"+new Date());
        return "success";
    }
    @RequestMapping("/test1")
    public String get2(){
        amqpTemplate.convertAndSend("myOrder","computer","now"+new Date());
        return "success";
    }
    @RequestMapping("/test2")
    public String get3(){
        amqpTemplate.convertAndSend("myQueue","now"+new Date());
        return "success";
    }
    @RequestMapping("/test3")
    public String get4(){
        String message = "now "+new Date();
        streamClient.output().send(MessageBuilder.withPayload(message).build());
        return "success";
    }
    @RequestMapping("/test4")
    public Boolean get5(){
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId("987654");
        boolean send = streamClient.output().send(MessageBuilder.withPayload(orderDto).build());
        return send;
    }
}
