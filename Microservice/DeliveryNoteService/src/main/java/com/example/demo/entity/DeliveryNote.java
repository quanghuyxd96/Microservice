package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "DeliveryNotes")
public class DeliveryNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate deliveryDate;
    private long orderId;

    @OneToMany(mappedBy = "deliveryNote", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<DeliveryItemDetail> deliveryItemDetails;
}
