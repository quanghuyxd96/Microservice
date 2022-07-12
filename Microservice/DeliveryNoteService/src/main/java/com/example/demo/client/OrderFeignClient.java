package com.example.demo.client;

import com.example.demo.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "orderFeignClient", url = "${client.post.baseUrl1}")
public interface OrderFeignClient {
    @GetMapping("/order/orders")
    List<OrderDTO> getALlOrders();
}
