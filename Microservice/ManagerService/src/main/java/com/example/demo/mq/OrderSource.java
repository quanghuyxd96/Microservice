package com.example.demo.mq;

import com.example.demo.utils.Constants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface OrderSource {
    String ORDER_CHANEL = Constants.ORDER_CHANEL;

    @Input(ORDER_CHANEL)
    SubscribableChannel receiveMessage();
}
