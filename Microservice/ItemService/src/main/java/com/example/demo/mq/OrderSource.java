package com.example.demo.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

import static com.example.demo.utils.Constants.DELIVERY_ITEM_CHANEL;
import static com.example.demo.utils.Constants.ITEM_CHANNEL;

public interface OrderSource {
    String ITEM = ITEM_CHANNEL;

    @Input(ITEM_CHANNEL)
    SubscribableChannel receiveMessage();
}
