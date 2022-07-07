package com.example.demo.controller;

import com.example.demo.entity.OrderDetail;
import com.example.demo.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("order-detail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public OrderDetail saveOrderDetail(@RequestBody OrderDetail orderDetail) {
        return orderDetailService.saveOrderDetail(orderDetail);
    }

    @GetMapping("/order-details")
    public List<OrderDetail> getAllOrder() {
        return orderDetailService.getAllOrderDetails();
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<OrderDetail> getOrderById(@RequestParam("id") Long id) {
        return new ResponseEntity<>(orderDetailService.getOrderDetailById(id), HttpStatus.OK);
    }
}
