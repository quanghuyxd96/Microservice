package com.example.demo.service;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.StoreDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.mq.OrderSource;
import com.example.demo.repository.OrderRepository;
import com.example.demo.utils.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.utils.Constants.AUTHOR;

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
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OrderSource orderSource;


    public Order saveOrder(List<OrderDetail> orderDetails) {
        Order order = new Order();
        orderRepository.save(order);
        order.setOrderDate(LocalDate.now());
        StoreDTO store = storeFeignClient.getStoreByToken(request.getHeader(AUTHOR));
        order.setStoreId(store.getId());
        double totalPrice = 0;
        double totalWeight = 0;
        for (int i = 0; i < orderDetails.size(); i++) {
            OrderDetail orderDetail = orderDetails.get(i);
            orderDetail.setId(0);
            ItemDTO item = itemFeignClient.getItemById(orderDetail.getItemId()).getBody();
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

//    public Order saveOrderTrial(List<OrderDetail> orderDetails) {
//        Order order = new Order();
//        orderRepository.save(order);
//        order.setOrderDate(LocalDate.now());
//        StoreDTO store = storeFeignClient.getStoreByToken(request.getHeader(AUTHOR));
//        order.setStoreId(store.getId());
//        double totalPrice = 0;
//        double totalWeight = 0;
//        for (int i = 0; i < orderDetails.size(); i++) {
//            OrderDetail orderDetail = orderDetails.get(i);
//            orderDetail.setId(0);
//            ItemDTO item = itemFeignClient.getItemById(orderDetail.getItemId()).getBody();
//            if (item == null || item.getQuantity() <= 0) {
//                orderDetails.remove(i--);
//                continue;
//            }
//            if (item.getQuantity() < orderDetail.getItemQuantity()) {
//                orderDetail.setItemQuantity(item.getQuantity());
//            }
//            totalPrice += item.getPrice() * orderDetail.getItemQuantity();
//            totalWeight += item.getWeight() * orderDetail.getItemQuantity();
//            orderDetail.setOrder(order);
//        }
//        if (totalPrice >= 10000) {
//            totalPrice = totalPrice * 0.95;
//        }
//        order.setTotalPrice(totalPrice);
//        order.setOrderDetails(orderDetails);
//        return orderRepository.save(order);
//    }


    public Order updateOrder(List<OrderDetail> orderDetails, long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()
                && (order.get().getOrderDate().plusDays(1).isAfter(LocalDate.now())
                || order.get().getOrderDate().plusDays(1).isEqual(LocalDate.now()))) {
            double totalPrice = 0;
            for (int i = 0; i < orderDetails.size(); i++) {
                OrderDetail orderDetail = orderDetails.get(i);
                orderDetail.setId(0);
                ItemDTO item = itemFeignClient.getItemById(orderDetail.getItemId()).getBody();
                if (item == null) {
                    orderDetails.remove(i--);
                    continue;
                }
                totalPrice += item.getPrice() * orderDetail.getItemQuantity();
                orderDetail.setOrder(order.get());
            }
            if (totalPrice >= 10000) {
                totalPrice = totalPrice * 0.95;
            }
            order.get().setTotalPrice(totalPrice);
            order.get().setOrderDetails(orderDetails);
            return orderRepository.save(order.get());
        }
        return null;
    }


    public List<Order> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        if (orders == null) {
            return null;
        }
        return orders;
    }

    public List<Order> getAllOrderByStoreId(long id) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader(AUTHOR));
        List<Order> orders = orderRepository.myStoreQueryByStoreId(id);
        if (orders == null) {
            return null;
        }
        if (username.startsWith("admin")) {
            return orders;
        } else {
            StoreDTO store = storeFeignClient.getStoreByToken(request.getHeader(AUTHOR));
            if (store == null) {
                return null;
            }
            return orderRepository.findByStoreId(store.getId());
        }
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

    public boolean processDeleteOrderById(long id, List<OrderDetail> orderDetails) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()
                && (order.get().getOrderDate().plusDays(1).isAfter(LocalDate.now())
                || order.get().getOrderDate().plusDays(1).isEqual(LocalDate.now()))) {
            if (isAdmin(request.getHeader(AUTHOR).substring(7))) {
                orderDetails.addAll(order.get().getOrderDetails());
                orderRepository.deleteById(id);
                return true;
            }
            StoreDTO store = getStoreByToken(request.getHeader(AUTHOR));
            System.out.println(store.toString());
            if (store == null) {
                return false;
            }
            if (store.getId() == order.get().getStoreId()) {
                orderDetails.addAll(order.get().getOrderDetails());
                orderRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

    /**
     * Function to send Mess from Order to Delivery Note to delete Delivery
     *
     * @param id
     * @return
     */
    public boolean deleteOrderById(long id) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        if (processDeleteOrderById(id, orderDetails)) {
            orderSource.deleteDelivery().send(MessageBuilder
                    .withPayload(id).build());
            return true;
        }
        return false;
    }

    public StoreDTO getStoreByToken(String token) {
        StoreDTO store = storeFeignClient.getStoreByToken(token);
        if (store == null) {
            return null;
        }
        return store;
    }

    public boolean isAdmin(String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        if (username.startsWith("admin")) {
            return true;
        }
        return false;
    }

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
}
