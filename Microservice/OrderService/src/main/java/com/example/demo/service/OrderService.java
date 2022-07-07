package com.example.demo.service;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ItemFeignClient itemFeignClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    public Order updateOrderById(Order order, long id) {
//        orderRepository.findById(id);
//        order.setStore(order.getStore());
//        order.setDeliveryNotes(order.getDeliveryNotes());
//        order.setTotalPrice(order.getTotalPrice());
//        order.setOrderDate(order.getOrderDate());
//        order.setOrderDetails(order.getOrderDetails());
//        return orderRepository.save(order);
//    }


    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public List<Order> getAllOrderByStoreId(long id) {
        return orderRepository.myStoreQueryByStoreId(id);
    }

//    public Order saveOrder(Order order) {
//        List<OrderDetail> orderDetails = orderDetailRepository.orderDetailsByOrderId(order.getId());
//        List<ItemDTO> allItem = itemFeignClient.getAllItem();
//        orderRepository.save(order);
//        double totalPrice = 0;
//        for (OrderDetail orderDetail : orderDetails) {
//            for (ItemDTO itemDTO : allItem) {
//                if (orderDetail.getItemId() == itemDTO.getId()) {
//                    totalPrice+=orderDetail.getItemQuantity()*itemDTO.getPrice();
//                }
//            }
//        }
//        order.setOrderDate(LocalDate.now());
//        System.out.println(order.getOrderDate());
//        order.setTotalPrice(totalPrice);
//        return orderRepository.save(order);
//    }

    public Order getOrderById(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return order.get();
        }
        return null;
    }

    public void deleteOrderById(long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getOrderByStoreId(long id) {
        return orderRepository.myStoreQueryByStoreId(id);
    }

    public void getOrderByIdDemorabbit(Long id){
        System.out.println(orderRepository.findById(id).get());
        rabbitTemplate.convertAndSend("user.exchange","order.routingkey",orderRepository.findById(id).get());
    }
}
