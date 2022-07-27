package com.example.demo.mq;


import com.example.demo.utils.Constants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

import static com.example.demo.utils.Constants.DELIVERY_ITEM_CHANEL;
import static com.example.demo.utils.Constants.ITEM_CHANNEL;


public interface DeliverySource {
    String DELIVERY_ITEM_CHANNEL = DELIVERY_ITEM_CHANEL;

    String ITEM = ITEM_CHANNEL;

    @Input(DELIVERY_ITEM_CHANNEL)
    SubscribableChannel receiveMessage();



    @Input(ITEM_CHANNEL)
    SubscribableChannel receiveMessToUpdateByDeleteOrder();
}
