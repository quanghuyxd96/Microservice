package com.example.demo.repository;

import com.example.demo.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
//    Item findByNameAndSupplierId(String name, long supplierId);
    Optional<Item> findByNameAndSupplierId(String name, long supplierId);

}
