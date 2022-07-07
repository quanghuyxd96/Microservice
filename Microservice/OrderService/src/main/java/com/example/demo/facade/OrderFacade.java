package com.example.demo.facade;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class OrderFacade {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ItemFeignClient itemFeignClient;
}
