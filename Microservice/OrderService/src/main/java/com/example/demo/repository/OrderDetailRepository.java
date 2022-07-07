package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    @Query(value = "SELECT * FROM order_detail WHERE order_id=:order_id",nativeQuery = true)
    List<OrderDetail> orderDetailsByOrderId(@Param("order_id") long order_id);
}
