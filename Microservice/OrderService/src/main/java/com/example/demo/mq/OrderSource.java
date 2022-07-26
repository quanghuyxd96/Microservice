package com.example.demo.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import static com.example.demo.utils.Constants.*;

public interface OrderSource {
    @Output(ORDER_CHANEL)
    MessageChannel order();
    @Output(ORDER_DELIVERY_CHANEL)
    MessageChannel orderDelivery();

//    @Output(ORDER_DELIVERY_UPDATE_CHANEL)
//    MessageChannel orderDeliveryUpdate();

    @Output(DELIVERY_CHANNEL)
    MessageChannel deleteDelivery();

}
