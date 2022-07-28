package com.example.demo.repository;

import com.example.demo.entity.DeliveryNote;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote,Long> {
    DeliveryNote findDeliveryNoteByOrderId(long id);
    @Query(value = "SELECT * FROM delivery_notes WHERE order_id = :order_id", nativeQuery = true)
    List<DeliveryNote> findAllDeliveryNoteByOrderId(@Param("order_id") long order_id);
}
