package com.example.demo.controller;

import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.facade.OrderFacade;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderFacade orderFacade;

    //Order
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public Order saveOrder(@RequestBody Order order) {
//
//        return orderFacade.getOrderService().saveOrder(order);
//    }

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
    public void deleteOrder(@RequestParam("id") Long id) {
        orderFacade.getOrderService().deleteOrderById(id);
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

//    @GetMapping("/demo")
//    public List<ItemDTO> getAllItems(){
//        return orderFacade.getItemFeignClient().getAllItem();
//    }

    @GetMapping("/demo")
    public void getOrderByIdDemoRabbit(@RequestParam("id") Long id) {

        orderFacade.getOrderService().getOrderByIdDemorabbit(id);
    }
}
