package com.zxd.order.message;

import com.zxd.order.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * 接受stream的
 */
@Component
@EnableBinding(StreamClient.class)
@Slf4j
public class StreamReceiver {
    /**
     * 接受到信息 并打印
     * @param msg
     */
    @StreamListener(StreamClient.OUTPUT)
    @SendTo(StreamClient.OUTPUT2)
    public String process(Object msg){
      log.info("StreamReceiver:{}",msg);
      return "received";
    }
    @StreamListener(StreamClient.OUTPUT2)
    public void process1(Object msg){
        log.info("StreamReceiver2:{}",msg);
    }
}
