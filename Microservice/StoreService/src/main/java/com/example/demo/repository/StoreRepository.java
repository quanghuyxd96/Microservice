package com.example.demo.repository;

import com.example.demo.entity.Store;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "SELECT * FROM Store WHERE user_name = :user_name", nativeQuery = true)
    Store findByUserName(@Param("user_name") String user_name);
}
