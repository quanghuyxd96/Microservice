package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
@Setter
@Getter
public class DeliveryItemDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long deliveriedQuantity;
    private long unDeliveriedQuantity;
    private long itemId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "delivery_note_id")
    private DeliveryNote deliveryNote;
}
