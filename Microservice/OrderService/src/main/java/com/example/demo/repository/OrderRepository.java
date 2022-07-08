package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(value = "SELECT * FROM Orders WHERE store_id = :store_id", nativeQuery = true)
    List<Order> myStoreQueryByStoreId(@Param("store_id") long store_id);

    List<Order> findByOrderDate(LocalDate localDate);
}
