package com.example.demo.facade;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderService;
import io.tej.SwaggerCodgen.model.OrderDetailModel;
import io.tej.SwaggerCodgen.model.OrderModel;
import io.tej.SwaggerCodgen.model.ResponseObject;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class OrderFacade {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ItemFeignClient itemFeignClient;

    public OrderModel saveOrder(List<OrderDetailModel> orderDetailModels, Long storeId) {
        if (orderDetailModels == null || storeId == null) {
            return null;
        }
        Order order = orderService.saveOrder(convertListModel(orderDetailModels, OrderDetail.class), storeId);
        return convertModel(order, OrderModel.class);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(long id) {
        List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetailsByOrderId(id);
        if (orderDetails == null) {
            return null;
        }
        return orderDetails;
    }


    public List<OrderModel> getAllOrders() {
        List<Order> orders = orderService.getAllOrder();
        if (orders == null) {
            return null;
        }
        return convertListModel(orders, OrderModel.class);
    }

    public List<OrderModel> getAllOrdersByStoreId(long id) {
        List<Order> orders = orderService.getAllOrderByStoreId(id);
        if (orders == null) {
            return null;
        }
        return convertListModel(orders, OrderModel.class);
    }

    public List<OrderModel> getOrdersByDate(LocalDate localDate) {
        List<Order> orders = orderService.getOrdersByDate(localDate);
        if (orders == null) {
            return null;
        }
        return convertListModel(orders, OrderModel.class);
    }


    public OrderModel getOrderById(long id){
        Order order = orderService.getOrderById(id);
        if(order ==null){
            return null;
        }
        return convertModel(order,OrderModel.class);
    }

    public ResponseEntity<ResponseObject> deleteOrderById(long id){
        boolean check = orderService.deleteOrderById(id);
        ResponseObject responseObject = new ResponseObject();
        if (!check) {
            responseObject.setStatus("False");
            responseObject.setMessage("No supplier to delete!!!");
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        }
        responseObject.setStatus("OK");
        responseObject.setMessage("Deleted");
        return new ResponseEntity<>(responseObject, HttpStatus.OK);

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
}
