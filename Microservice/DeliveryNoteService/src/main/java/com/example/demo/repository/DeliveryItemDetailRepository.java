package com.example.demo.repository;

import com.example.demo.entity.DeliveryItemDetail;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryItemDetailRepository extends JpaRepository<DeliveryItemDetail, Long> {

    @Query(value = "SELECT * FROM delivery_item_detail WHERE undeliveried_quantity > 0 ORDER BY order_id ASC,id ASC", nativeQuery = true)
    List<DeliveryItemDetail> getItemUndeliveried();

    @Query(value = "DELETE FROM delivery_item_detail WHERE delivery_note_id = :delivery_note_id", nativeQuery = true)
    void deleteByDeliveryNoteId(@Param("delivery_note_id") long delivery_note_id);

}
