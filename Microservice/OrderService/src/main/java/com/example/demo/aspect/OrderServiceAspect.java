package com.example.demo.aspect;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.OrderDetail;
import com.example.demo.mq.OrderSource;
import com.example.demo.utils.Convert;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;


@Aspect
@Component
public class OrderServiceAspect {
    @Autowired
    private OrderSource orderSource;

    @Autowired
    private Convert convert;

    private Logger logger = LoggerFactory.getLogger(OrderServiceAspect.class);


    @After(value = "execution(* com.example.demo.service.OrderService.saveOrder(..)) and args(orderDetails)")
    public void afterCheckDeliveryItemUndelivery(JoinPoint joinPoint, List<OrderDetail> orderDetails) {
        logger.info("After method: " +joinPoint.getSignature());
        List<OrderDetailDTO> orderDetailDTOList = convert.convertListModel(orderDetails, OrderDetailDTO.class);
        orderSource.order().send(MessageBuilder.withPayload(orderDetailDTOList).build());
        logger.info("Finished send list order details to send email");
    }


//    @Before(value = "execution(* com.example.demo.service.DeliveryNoteService.saveDelivery(..))")
//    public void beforeAdvice(JoinPoint joinPoint) {
//        logger.info("Before method:" + joinPoint.getSignature());
//    }
}
