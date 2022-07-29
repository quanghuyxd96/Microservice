package com.example.demo.repository;

import com.example.demo.entity.Payment;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Payment findByStoreUserAndOrderId(String storeUser, long orderId);
    Payment findByOrderId(long orderId);

    @Query(value = "SELECT * FROM payment WHERE order_id = :order_id AND money_unpaid = (SELECT min(money_unpaid) FROM " +
            "payment WHERE order_id = :order_id)",nativeQuery = true)
    Payment getPaymentByOrderId(@Param("order_id") long order_id);
}
