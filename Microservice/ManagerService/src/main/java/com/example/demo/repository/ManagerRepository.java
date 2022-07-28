package com.example.demo.repository;

import com.example.demo.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager,Long> {
    Manager findByUserName(String userName);
    Manager findByEmailAndUserName(String userName, String password);
}
