package com.example.demo.client;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@FeignClient(name = "orderFeignClient",url = "localhost:8083")
public interface OrderFeignClient {

    @GetMapping("/order/orders")
    List<OrderDTO> getAllOrders();

    @GetMapping("/order/order-by-store-id/{storeId}")
    List<OrderDTO> getOrdersByStoreId(@PathVariable("storeId") long id);

    @PostMapping("/order/save-order")
    ResponseEntity<OrderDTO>  saveOrder(@RequestBody List<OrderDetailDTO> orderDetails, @RequestParam("id") Long storeId);

    @GetMapping("/order/demo")
    int demo(@RequestHeader("Authorization") String a);
}
