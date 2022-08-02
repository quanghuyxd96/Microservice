package com.example.demo.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import static com.example.demo.utils.Constants.*;

public interface OrderSource {
    @Output(ORDER_CHANEL)
    MessageChannel order();

    /**
     * Không xài cái này nữa do xài 1 publisher bắn ra 1 event
     * @return
     */
//    @Output(ORDER_DELIVERY_CHANEL)
//    MessageChannel orderDelivery();



    @Output(DELIVERY_CHANNEL)
    MessageChannel deleteDelivery();

}
