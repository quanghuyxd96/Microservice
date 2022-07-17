package com.example.demo.repository;

import com.example.demo.entity.DeliveryItemDetail;
import org.checkerframework.framework.qual.EnsuresQualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryItemDetailRepository extends JpaRepository<DeliveryItemDetail, Long> {

    @Query(value = "SELECT * FROM delivery_item_detail WHERE undeliveried_quantity > 0 ORDER BY id ASC", nativeQuery = true)
    List<DeliveryItemDetail> getItemUndeliveried();
}
