package com.zxd.order.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 定义 myMessage
 */
public interface StreamClient {
    String OUTPUT="myMessageOutput";
    String INPUT="myMessageInput";

    String OUTPUT2="myMessageOutput2";
    String INPUT2="myMessageInput2";

    @Input(StreamClient.INPUT)
    SubscribableChannel input();
    @Output(StreamClient.OUTPUT)
    MessageChannel output();

    @Input(StreamClient.INPUT2)
    SubscribableChannel input2();
    @Output(StreamClient.OUTPUT2)
    MessageChannel output2();
}
