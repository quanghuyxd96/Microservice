package com.example.demo.entity;

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
    private long deliveryNoteId;
}
