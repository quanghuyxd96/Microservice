package com.example.demo.mq.listen;

import com.example.demo.utils.Constants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

import static com.example.demo.utils.Constants.*;

public interface StoreSource {
    String STORE_FORGOT_PASSWORD = Constants.STORE_FORGOT_PASSWORD;
    @Input(STORE_FORGOT_PASSWORD)
    SubscribableChannel receiveMessage();
}
