package com.example.demo.service;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.StoreDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.utils.Constant.AUTHOR;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ItemFeignClient itemFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HttpServletRequest request;

    public Order saveOrder(List<OrderDetail> orderDetails) {
        Order order = new Order();
        orderRepository.save(order);
        order.setOrderDate(LocalDate.now());
        StoreDTO store = storeFeignClient.getStoreByToken(request.getHeader(AUTHOR));
        order.setStoreId(store.getId());
        double totalPrice = 0;
        for (int i = 0; i < orderDetails.size(); i++) {
            OrderDetail orderDetail = orderDetails.get(i);
            orderDetail.setId(0);
            ItemDTO item = itemFeignClient.getItemById(orderDetail.getItemId()).getBody();
            System.out.println(item.getPrice());
            if (item == null) {
                orderDetails.remove(i--);
                continue;
            }
            totalPrice += item.getPrice() * orderDetail.getItemQuantity();
            orderDetail.setOrder(order);
        }
        if (totalPrice >= 10000) {
            totalPrice = totalPrice * 0.95;
        }
        order.setTotalPrice(totalPrice);
        order.setOrderDetails(orderDetails);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        if (orders == null) {
            return null;
        }
        return orders;
    }

    public List<Order> getAllOrderByStoreId(long id) {
        return orderRepository.myStoreQueryByStoreId(id);
    }

    public Order getOrderById(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return order.get();
        }
        return null;
    }

    public boolean deleteOrderByIdFromStore(long id, long storeId) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            if (order.get().getOrderDate().equals(LocalDate.now())) {
                orderRepository.deleteById(id);
                return true;
            }
            return false;
        }
        return false;
    }

    public List<Order> getOrderByStoreId(long id) {
        List<Order> orders = orderRepository.findByStoreId(id);
        if (orders == null) {
            return null;
        }
        return orders;
    }

    public List<Order> getOrdersByDate(LocalDate localDate) {
        List<Order> orders = orderRepository.findByOrderDate(localDate);
        if (orders == null) {
            return null;
        }
        return orders;
    }

    public boolean deleteOrderById(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

//    public void getOrderByIdDemoRabbit(Long id) {
//        Order order = orderRepository.findById(id).get();
//        LocalDate orderDate = order.getOrderDate();
//        System.out.println(orderDate);
//        rabbitTemplate.convertAndSend("user.exchange", "order.routingkey", order);
//    }

    //    public Order updateOrderById(Order order, long id) {
//
//        orderRepository.findById(id);
//        order.setStore(order.getStore());
//        order.setDeliveryNotes(order.getDeliveryNotes());
//        order.setTotalPrice(order.getTotalPrice());
//        order.setOrderDate(order.getOrderDate());
//        order.setOrderDetails(order.getOrderDetails());
//        return orderRepository.save(order);
//    }

    private <T, D> T convertModel(D obj, Class<T> classT) {
        ModelMapper modelMapper = new ModelMapper();
        T obj1 = modelMapper.map(obj, classT);
        return obj1;
    }

    private <T, D> List<T> convertListModel(List<D> objList, Class<T> classT) {
        List<T> objResults = new ArrayList<T>();
        for (D obj : objList) {
            ModelMapper modelMapper = new ModelMapper();
            T objResult = modelMapper.map(obj, classT);
            objResults.add(objResult);
        }
        return objResults;
    }
}
