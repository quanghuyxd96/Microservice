package com.example.demo.client;

import com.example.demo.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.utils.Constant.AUTHOR;

@FeignClient(name = "orderFeignClient", url = "${client.post.baseUrl2}")
public interface OrderFeignClient {

    @GetMapping("/order/orders")
    List<OrderDTO> getAllOrders(@RequestHeader(AUTHOR) String token);

    @GetMapping("/order/get")
    ResponseEntity<OrderDTO> getOrderById(@RequestParam("id") Long id, @RequestHeader(AUTHOR) String token);

    @GetMapping("/order/get-by-date")
    List<OrderDTO> getOrdersByOrderDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate,
                                        @RequestHeader(AUTHOR) String token);


}
