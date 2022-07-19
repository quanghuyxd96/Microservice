package com.example.demo;

import com.example.demo.dto.ItemDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableFeignClients(basePackageClasses=com.example.demo.client.StoreFeignClient.class)
@SpringBootApplication
@EnableFeignClients
@EnableScheduling

public class ManagerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagerServiceApplication.class, args);

	}


}
