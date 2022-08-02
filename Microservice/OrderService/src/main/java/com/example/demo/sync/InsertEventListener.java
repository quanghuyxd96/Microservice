package com.example.demo.sync;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.dto.OrderDetailDTOToken;
import com.example.demo.entity.Order;
import com.example.demo.mq.OrderSource;
import com.example.demo.utils.Convert;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static com.example.demo.utils.Constants.AUTHOR;


@Component
public class InsertEventListener implements PostInsertEventListener {
    Logger logger = LoggerFactory.getLogger(InsertEventListener.class);
    @Autowired
    private Convert convert;

    @Autowired
    private OrderSource orderSource;

    @Autowired
    private HttpServletRequest request;

    //cách 2 lưu deliveryNote
    @Override
    public void onPostInsert(PostInsertEvent postInsertEvent) {
        System.out.println("The Event comes here with data: " + Arrays.toString(postInsertEvent.getState()));
        Object entity = postInsertEvent.getEntity();
        if (entity instanceof Order) {
            Order order = (Order) entity;
            List<OrderDetailDTOToken> orderDetailDTOList = convert.convertListModel(order.getOrderDetails(), OrderDetailDTOToken.class);
            orderDetailDTOList.get(0).setToken(request.getHeader(AUTHOR));
            orderSource.order().send(MessageBuilder.withPayload(orderDetailDTOList).build());
        } else {
            logger.info("OK Order details");
        }
    }


    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return true;
    }
}
