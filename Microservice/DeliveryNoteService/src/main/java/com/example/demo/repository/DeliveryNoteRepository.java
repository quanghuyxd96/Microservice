package com.example.demo.repository;

import com.example.demo.entity.DeliveryNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote,Long> {
}
