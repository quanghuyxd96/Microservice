package com.example.demo.client;

import com.example.demo.dto.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "orderFeignClient",url = "localhost:8083")
public interface OrderFeignClient {
    @GetMapping("/order/orders")
    List<Order> getAllOrders();

    @GetMapping("/order/order-by-store-id/{storeId}")
    List<Order> getOrdersByStoreId(@PathVariable("storeId") long id);
}
