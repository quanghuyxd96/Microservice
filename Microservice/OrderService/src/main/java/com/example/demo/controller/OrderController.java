package com.example.demo.controller;

import com.example.demo.facade.OrderFacade;
import com.example.demo.repository.OrderRepository;
import io.tej.SwaggerCodgen.api.OrderApi;
import io.tej.SwaggerCodgen.model.OrderDetailModel;
import io.tej.SwaggerCodgen.model.OrderModel;
import io.tej.SwaggerCodgen.model.ResponseObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class OrderController implements OrderApi {
    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseEntity<OrderModel> orderSaveOrderPost(Long id, List<OrderDetailModel> orderDetailModels) {
        OrderModel orderModel = orderFacade.saveOrder(orderDetailModels, id);
        if (orderModel == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(orderModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<OrderModel>> orderOrdersGet() {
        List<OrderModel> orders = orderFacade.getAllOrders();
        if (orders == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderModel> orderGetGet(Long id) {
        OrderModel orderModel = orderFacade.getOrderById(id);
        if (orderModel == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(orderModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<OrderModel>> orderStoreGet(Long id) {
        List<OrderModel> orders = orderFacade.getAllOrdersByStoreId(id);
        if (orders == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<OrderModel>> orderGetByDateGet(LocalDate date) {
        List<OrderModel> orders = orderFacade.getOrdersByDate(date);
        if (orders == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject> orderDeleteDelete(Long id) {
        return orderFacade.deleteOrderById(id);
    }

    //    @PostMapping("/save-order")
//    public ResponseEntity<Order> saveOrder(@RequestBody List<OrderDetail> orderDetails, @RequestParam("id") Long storeId) {
//        if (orderDetails == null || storeId == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        Order order = orderFacade.getOrderService().saveOrder(orderDetails, storeId);
//        List<OrderDetailDTO> orderDetailDTOS = orderFacade.convertListModel(orderDetails, OrderDetailDTO.class);
//        rabbitTemplate.convertAndSend("user.exchange", "order.routingkey", orderDetailDTOS);
//        return new ResponseEntity<>(order, HttpStatus.OK);
//    }
//
    //Order-detail

    @Override
    public ResponseEntity<List<OrderDetailModel>> orderOrderDetailsGet() {
        List<OrderDetailModel> orderDetailModels = orderFacade.getAllOrderDetails();
        if (orderDetailModels == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderDetailModels, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<OrderDetailModel>> orderOrderDetailsByOrderGet(Long id) {
        List<OrderDetailModel> orderDetailModels = orderFacade.getOrderDetailsByOrderId(id);
        if (orderDetailModels == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderDetailModels, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderDetailModel> orderOrderDetailGetGet(Long id) {
        OrderDetailModel orderDetailModel = orderFacade.getOrderDetaisById(id);
        if (orderDetailModel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderDetailModel, HttpStatus.OK);
    }


//    @GetMapping("/demo")
//    public void getOrderByIdDemoRabbit(@RequestParam("id") Long id) {
//        orderFacade.getOrderService().getOrderByIdDemoRabbit(id);
//    }
//
}
