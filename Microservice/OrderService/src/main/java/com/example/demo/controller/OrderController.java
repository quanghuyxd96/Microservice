package com.example.demo.controller;

import com.example.demo.facade.OrderFacade;
import com.example.demo.repository.OrderRepository;
import io.tej.SwaggerCodgen.api.ApiUtil;
import io.tej.SwaggerCodgen.api.OrderApi;
import io.tej.SwaggerCodgen.model.OrderDetailModel;
import io.tej.SwaggerCodgen.model.OrderModel;
import io.tej.SwaggerCodgen.model.ResponseObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
//
//
//    @GetMapping("/order/store")
//    public List<Order> getOrderByStoreId(@RequestParam("id") Long id) {
//        return orderFacade.getOrderService().getOrderByStoreId(id);
//    }
//
//
//    @RequestMapping(value = "/get", method = RequestMethod.GET)
//    public ResponseEntity<Order> getOrder(@RequestParam("id") Integer id) {
//        return new ResponseEntity<>(orderFacade.getOrderService().getOrderById(id), HttpStatus.OK);
//    }
//
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<ResponseObjectEntity> deleteOrder(@RequestParam("id") Long id, @RequestParam("id") Long storeId) {
//        if (orderFacade.getOrderService().deleteOrderByIdFromStore(id, storeId)) {
//            return new ResponseEntity<>(new ResponseObjectEntity("OK", "Deleted!!!"), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
//
//
//    @GetMapping("/get-by-date")
//    public List<Order> getOrdersByOrderDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
//        return orderRepository.findByOrderDate(localDate);
//    }
//
//    //Order-detail
//    @RequestMapping(value = "/order-detail/save", method = RequestMethod.POST)
//    public OrderDetail saveOrderDetail(@RequestBody OrderDetail orderDetail) {
//        return orderFacade.getOrderDetailService().saveOrderDetail(orderDetail);
//    }
//
//    @GetMapping("/order-details")
//    public List<OrderDetail> getAllOrderDetails() {
//        return orderFacade.getOrderDetailService().getAllOrderDetails();
//    }
//
//    @RequestMapping(value = "/order-detail/get", method = RequestMethod.GET)
//    public ResponseEntity<OrderDetail> getOrderById(@RequestParam("id") Long id) {
//        return new ResponseEntity<>(orderFacade.getOrderDetailService().getOrderDetailById(id), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}/details")
//    public ResponseEntity<List<OrderDetail>> getAllOrderDetailsByOrderId(@PathVariable("id") Long id) {
//        List<OrderDetail> orderDetails = orderFacade.getOrderDetailsByOrderId(id);
//        if (orderDetails == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
//    }
//
//
////    @GetMapping("/demo")
////    public List<ItemDTO> getAllItems(){
////        return orderFacade.getItemFeignClient().getAllItem();
////    }
//
//    @GetMapping("/demo")
//    public void getOrderByIdDemoRabbit(@RequestParam("id") Long id) {
//        orderFacade.getOrderService().getOrderByIdDemoRabbit(id);
//    }
//
//    //demo OK
//    @GetMapping("/demoDate")
//    public List<Order> getOrder(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
//        return orderRepository.findByOrderDate(localDate);
//    }
}
