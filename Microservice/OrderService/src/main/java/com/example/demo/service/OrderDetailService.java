package com.example.demo.service;

import com.example.demo.entity.OrderDetail;
import com.example.demo.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;


    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }


    public OrderDetail saveOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }


    public OrderDetail getOrderDetailById(Long id) {
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(id);
        if (orderDetail.isPresent()) {
            return orderDetail.get();
        }
        System.out.println("No order detail to get!!!");
        return null;
    }

    public void deleteOrderDetailById(long id) {
        orderDetailRepository.deleteById(id);
    }


}
