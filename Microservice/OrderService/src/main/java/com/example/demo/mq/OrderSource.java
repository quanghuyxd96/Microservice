package com.example.demo.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import static com.example.demo.utils.Constant.ORDER_CHANEL;

public interface OrderSource {
    @Output(ORDER_CHANEL)
    MessageChannel order();
}
