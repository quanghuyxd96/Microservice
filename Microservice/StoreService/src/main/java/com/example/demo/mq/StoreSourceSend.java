package com.example.demo.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import static com.example.demo.utils.Constants.*;

public interface StoreSourceSend {
    @Output(STORE_FORGOT_PASSWORD)
    MessageChannel sendToGetNewPassword();
}
