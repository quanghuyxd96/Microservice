package com.example.demo.aspect;

import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.dto.OrderDetailDTOToken;
import com.example.demo.entity.OrderDetail;
import com.example.demo.mq.OrderSource;
import com.example.demo.utils.Convert;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.demo.utils.Constant.AUTHOR;


@Aspect
@Component
public class OrderServiceAspect {
    @Autowired
    private OrderSource orderSource;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private Convert convert;

    private Logger logger = LoggerFactory.getLogger(OrderServiceAspect.class);
    StreamBridge streamBridge;


    @After(value = "execution(* com.example.demo.service.OrderService.saveOrder(..)) and args(orderDetails)")
    public void afterCheckDeliveryItemUndelivery(JoinPoint joinPoint, List<OrderDetail> orderDetails) {
        logger.info("After method: " + joinPoint.getSignature());
        List<OrderDetailDTOToken> orderDetailDTOList = convert.convertListModel(orderDetails, OrderDetailDTOToken.class);
        orderDetailDTOList.get(0).setToken(request.getHeader(AUTHOR));
        orderSource.order().send(MessageBuilder.withPayload(orderDetailDTOList).build());
        orderSource.orderDelivery().send(MessageBuilder.withPayload(
                convert.convertListModel(orderDetails, OrderDetailDTO.class)).build());
        logger.info("Finished send list order details to send email");
    }


//    @Before(value = "execution(* com.example.demo.service.DeliveryNoteService.saveDelivery(..))")
//    public void beforeAdvice(JoinPoint joinPoint) {
//        logger.info("Before method:" + joinPoint.getSignature());
//    }
}
