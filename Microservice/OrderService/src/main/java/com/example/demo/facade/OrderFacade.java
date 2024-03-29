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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HttpServletRequest request;

    public OrderModel saveOrder(List<OrderDetailModel> orderDetailModels) {
        if (orderDetailModels == null) {
            return null;
        }
        Order order = orderService.saveOrder(convertListModel(orderDetailModels, OrderDetail.class));
        return convertModel(order, OrderModel.class);
    }


    public ResponseEntity<OrderModel> updateOrder(List<OrderDetailModel> orderDetailModels, long orderId) {
        if (orderDetailModels == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Order order = orderService.updateOrder(convertListModel(orderDetailModels, OrderDetail.class), orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(convertModel(order, OrderModel.class), HttpStatus.OK);
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


    public OrderModel getOrderById(long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return null;
        }
        return convertModel(order, OrderModel.class);
    }

    public ResponseEntity<ResponseObject> deleteOrderById(long id) {
        boolean check = orderService.deleteOrderById(id);
        ResponseObject responseObject = new ResponseObject();
        if (!check) {
            responseObject.setStatus("False");
            responseObject.setMessage("No order to delete!!!");
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        }
        responseObject.setStatus("OK");
        responseObject.setMessage("Deleted");
        return new ResponseEntity<>(responseObject, HttpStatus.OK);

    }

    //Order details
    public List<OrderDetailModel> getAllOrderDetails() {
        List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetails();
        if (orderDetails == null) {
            return null;
        }
        return convertListModel(orderDetails, OrderDetailModel.class);
    }

    public List<OrderDetailModel> getOrderDetailsByOrderId(long id) {
        List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetailsByOrderId(id);
        if (orderDetails == null) {
            return null;
        }
        return convertListModel(orderDetails, OrderDetailModel.class);
    }

    public OrderDetailModel getOrderDetaisById(long id) {
        OrderDetail orderDetail = orderDetailService.getOrderDetailById(id);
        if (orderDetail == null) {
            return null;
        }
        return convertModel(orderDetail, OrderDetailModel.class);
    }


    private <T, D> T convertModel(D obj, Class<T> classT) {
        ModelMapper modelMapper = new ModelMapper();
        T obj1 = modelMapper.map(obj, classT);
        return obj1;
    }

    public <T, D> List<T> convertListModel(List<D> objList, Class<T> classT) {
        List<T> objResults = new ArrayList<T>();
        for (D obj : objList) {
            ModelMapper modelMapper = new ModelMapper();
            T objResult = modelMapper.map(obj, classT);
            objResults.add(objResult);
        }
        return objResults;
    }
}
