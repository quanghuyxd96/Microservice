package com.example.demo.repository;

import com.example.demo.entity.Payment;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByStoreUserAndOrderId(String storeUser, long orderId);

    Payment findByOrderId(long orderId);

    @Query(value = "SELECT * FROM payment WHERE payment_date = " +
            "(SELECT max(payment_date) FROM payment WHERE order_id = :order_id)", nativeQuery = true)
    Payment getPaymentByOrderId(@Param("order_id") long order_id);


    @Query(value = "SELECT * FROM payment WHERE payment_date IN " +
            "(SELECT max(payment_date) FROM payment GROUP BY order_id)" +
            " ORDER BY payment_date ASC, order_id ASC", nativeQuery = true)
    List<Payment> getAllPaymentPaid();

//    @Query(value = "SELECT * FROM payment WHERE payment_date IN " +
//            "(SELECT MAX(payment_date) FROM payment " +
//            "WHERE (payment_date >= ?1 " +
//            "AND payment_date < ?2 " +
//            "GROUP BY order_id) " +
//            "ORDER BY payment_date ASC, order_id DESC")
//    List<Payment> getAllPaymentByDateTime(Local)

//    @Query(value = "SELECT * FROM payment WHERE payment_date IN " +
//            "(SELECT MAX(payment_date) FROM payment " +
//            "WHERE (payment_date >= ?1 " +
//            "AND payment_date < ?2) " +
//            "GROUP BY order_id) " +
//            "ORDER BY payment_date ASC, order_id DESC",nativeQuery = true)
//    List<Payment> getAllPaymentByDateTime(String startDate, String endDate);
    @Query(value = "select * from payment WHERE (payment_date between ?1\n" +
            "AND ?2)",nativeQuery = true)
    List<Payment> getAllPaymentByDateTime(LocalDateTime startDate, LocalDateTime endDate);


}
