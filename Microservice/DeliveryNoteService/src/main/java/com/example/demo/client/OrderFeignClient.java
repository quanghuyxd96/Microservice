package com.example.demo.client;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.example.demo.utils.Constants.AUTHOR;

@FeignClient(name = "orderFeignClient", url = "${client.post.baseUrl1}")
public interface OrderFeignClient {
    @GetMapping("/order/orders")
    List<OrderDTO> getALlOrders();

    @GetMapping("/order/order-details-by-order")
    List<OrderDetailDTO> getAllOrderDetailsByOrderId(@RequestParam("id") Long id);

    @GetMapping("/order/get")
    ResponseEntity<OrderDTO> getOrderById(@RequestParam("id") Long id, @RequestHeader(AUTHOR) String token);
}
