package com.example.demo.service;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.Order;
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

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemFeignClient itemFeignClient;


    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }


    public OrderDetail saveOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    public List<OrderDetail> getAllOrderDetailsByOrderId(long id){
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(id);
        if(orderDetails==null){
            return null;
        }
        return orderDetails;
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
    public List<OrderDetail> saveOrderDetail(List<OrderDetail> orderDetails){
        return orderDetailRepository.saveAll(orderDetails);
    }


}
