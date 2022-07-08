package com.example.demo.client;

import com.example.demo.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "orderFeignClient", url = "${client.post.baseUrl2}")
public interface OrderFeignClient {

    @GetMapping("/order/orders")
    List<OrderDTO> getAllOrders();

    @GetMapping("/order/get")
    ResponseEntity<OrderDTO> getOrderById(@RequestParam("id") Long id);

    @GetMapping("order/get-by-date")
    List<OrderDTO> getOrdersByOrderDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate);


}
