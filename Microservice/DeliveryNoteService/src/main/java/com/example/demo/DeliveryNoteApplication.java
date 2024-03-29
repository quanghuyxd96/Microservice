package com.example.demo;

import com.example.demo.mq.DeliverySource;
import com.example.demo.mq.OrderSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableBinding({OrderSource.class,DeliverySource.class})
public class DeliveryNoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryNoteApplication.class, args);
	}

}
