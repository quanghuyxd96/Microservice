package com.example.demo.mq;

import com.example.demo.utils.Constant;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

import static com.example.demo.utils.Constant.ORDER_CHANEL;

public interface OrderSource {
    String ORDER_CHANEL = Constant.ORDER_CHANEL;

    @Input(ORDER_CHANEL)
    SubscribableChannel receiveMessage();
}
