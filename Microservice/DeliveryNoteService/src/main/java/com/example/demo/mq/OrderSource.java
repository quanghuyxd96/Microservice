package com.example.demo.mq;


import com.example.demo.utils.Constants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;


public interface OrderSource {
    String ORDER_DELIVERY_CHANEL = Constants.ORDER_DELIVERY_CHANEL;
    String DELIVERY_CHANNEL = Constants.DELIVERY_CHANNEL;

    @Input(ORDER_DELIVERY_CHANEL)
    SubscribableChannel receiveMessage();

    @Input(DELIVERY_CHANNEL)
    SubscribableChannel receiveMessageToDeleteDelivery();
}
