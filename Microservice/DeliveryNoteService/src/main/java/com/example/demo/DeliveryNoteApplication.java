package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class DeliveryNoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryNoteApplication.class, args);
	}

}
