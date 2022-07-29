package com.example.demo;

import com.example.demo.mq.StoreSourceSend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableFeignClients
@EnableBinding(StoreSourceSend.class)
public class StoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreServiceApplication.class, args);
    }
//    @Bean
//    public Docket productApi() {
//        return new Docket(DocumentationType.SWAGGER_2).select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.demo")).build();
    //    }
    }
