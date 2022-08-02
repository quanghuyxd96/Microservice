package com.example.demo.mq;


import com.example.demo.utils.Constants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;


public interface OrderSource {

    String DELIVERY_CHANNEL = Constants.DELIVERY_CHANNEL;

    String ORDER_CHANEL = Constants.ORDER_CHANEL;

    @Input(DELIVERY_CHANNEL)
    SubscribableChannel receiveMessageToDeleteDelivery();

    @Input(ORDER_CHANEL)
    SubscribableChannel receiveMessageDemo();

    /**
     * Use new way, dont't use this way
     *
     * @return
     */
//    String ORDER_DELIVERY_CHANEL = Constants.ORDER_DELIVERY_CHANEL;
//    @Input(ORDER_DELIVERY_CHANEL)
//    SubscribableChannel receiveMessage();
}
