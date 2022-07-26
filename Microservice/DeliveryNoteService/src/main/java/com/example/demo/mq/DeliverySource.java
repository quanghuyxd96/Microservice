package com.example.demo.mq;

import com.example.demo.utils.Constants.*;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import static com.example.demo.utils.Constants.DELIVERY_ITEM_CHANEL;
import static com.example.demo.utils.Constants.ITEM_CHANNEL;

@EnableBinding(DeliverySource.class)
public interface DeliverySource {
    @Output(DELIVERY_ITEM_CHANEL)
    MessageChannel updateItemQuantity();

    @Output(ITEM_CHANNEL)
    MessageChannel updateOrderItemQuantity();
}
