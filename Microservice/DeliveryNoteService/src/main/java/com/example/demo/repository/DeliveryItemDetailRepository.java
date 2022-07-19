package com.example.demo.repository;

import com.example.demo.entity.DeliveryItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryItemDetailRepository extends JpaRepository<DeliveryItemDetail, Long> {

    @Query(value = "SELECT * FROM delivery_item_detail WHERE undeliveried_quantity > 0 ORDER BY order_id ASC,id ASC", nativeQuery = true)
    List<DeliveryItemDetail> getItemUndeliveried();

}
