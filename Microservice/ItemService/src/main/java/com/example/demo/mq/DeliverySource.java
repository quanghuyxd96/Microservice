package com.example.demo.mq;


import com.example.demo.utils.Constants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

import static com.example.demo.utils.Constants.DELIVERY_ITEM_CHANEL;


public interface DeliverySource {
    String DELIVERY_ITEM_CHANNEL = DELIVERY_ITEM_CHANEL;

    @Input(DELIVERY_ITEM_CHANNEL)
    SubscribableChannel receiveMessage();
}
