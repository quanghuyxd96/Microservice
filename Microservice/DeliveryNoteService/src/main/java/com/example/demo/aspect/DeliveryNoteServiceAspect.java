package com.example.demo.aspect;

import com.example.demo.dto.OrderDetailDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Aspect
@Component
public class DeliveryNoteServiceAspect {
    private Logger logger = LoggerFactory.getLogger(DeliveryNoteServiceAspect.class);

    @Before(value = "execution(* com.example.demo.service.DeliveryItemDetailService.checkDeliveryItemUndelivery(..))")
    public void beforeCheckDeliveryItemUndelivery(JoinPoint joinPoint) {
        logger.info("Before method:" + joinPoint.getSignature());
    }

    @After(value = "execution(* com.example.demo.service.DeliveryItemDetailService.checkDeliveryItemUndelivery(..))")
    public void afterCheckDeliveryItemUndelivery(JoinPoint joinPoint) {
        logger.info("After method: " +joinPoint.getSignature());
    }

}
