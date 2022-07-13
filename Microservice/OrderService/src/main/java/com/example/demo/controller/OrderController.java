package com.example.demo.controller;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.dto.ResponseObjectEntity;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.facade.OrderFacade;
import com.example.demo.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //Order
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public Order saveOrder(@RequestBody Order order) {
//
//        return orderFacade.getOrderService().saveOrder(order);
//    }

    @PostMapping("/save-order")
    public ResponseEntity<Order> saveOrder(@RequestBody List<OrderDetail> orderDetails,@RequestParam("id") Long storeId){
        if(orderDetails==null||storeId==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Order order = orderFacade.getOrderService().saveOrder(orderDetails, storeId);
        List<OrderDetailDTO> orderDetailDTOS = orderFacade.convertListModel(orderDetails,OrderDetailDTO.class);
        rabbitTemplate.convertAndSend("user.exchange", "order.routingkey", orderDetailDTOS);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }


    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderFacade.getOrderService().getAllOrder();
    }

    @GetMapping("/get-order-by-store-id")
    public List<Order> getOrderByStoreId(@RequestParam("id") Long id){
        return orderFacade.getOrderService().getOrderByStoreId(id);
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<Order> getOrder(@RequestParam("id") Integer id) {
        return new ResponseEntity<>(orderFacade.getOrderService().getOrderById(id), HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObjectEntity> deleteOrder(@RequestParam("id") Long id, @RequestParam("id") Long storeId) {
        if(orderFacade.getOrderService().deleteOrderByIdFromStore(id, storeId)){
            return new ResponseEntity<>(new ResponseObjectEntity("OK","Deleted!!!"),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get-by-date")
    public List<Order> getOrdersByOrderDate(@RequestParam("date")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate){
        return orderRepository.findByOrderDate(localDate);
    }
    //Order-detail
    @RequestMapping(value = "/order-detail/save", method = RequestMethod.POST)
    public OrderDetail saveOrderDetail(@RequestBody OrderDetail orderDetail) {
        return orderFacade.getOrderDetailService().saveOrderDetail(orderDetail);
    }

    @GetMapping("/order-details")
    public List<OrderDetail> getAllOrderDetails() {
        return orderFacade.getOrderDetailService().getAllOrderDetails();
    }

    @RequestMapping(value = "/order-detail/get", method = RequestMethod.GET)
    public ResponseEntity<OrderDetail> getOrderById(@RequestParam("id") Long id) {
        return new ResponseEntity<>(orderFacade.getOrderDetailService().getOrderDetailById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<List<OrderDetail>> getAllOrderDetailsByOrderId(@PathVariable("id") Long id){
        List<OrderDetail> orderDetails = orderFacade.getOrderDetailsByOrderId(id);
        if(orderDetails==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderDetails,HttpStatus.OK);
    }


//    @GetMapping("/demo")
//    public List<ItemDTO> getAllItems(){
//        return orderFacade.getItemFeignClient().getAllItem();
//    }

    @GetMapping("/demo")
    public void getOrderByIdDemoRabbit(@RequestParam("id") Long id) {
        orderFacade.getOrderService().getOrderByIdDemoRabbit(id);
    }

    //demo OK
    @GetMapping("/demoDate")
    public List<Order> getOrder(@RequestParam("date")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate){
        return orderRepository.findByOrderDate(localDate);
    }
}
