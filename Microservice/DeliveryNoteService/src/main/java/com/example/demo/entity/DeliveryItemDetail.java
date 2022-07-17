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
    private long accumulationQuantity;
    private long undeliveriedQuantity;
    private long totalDeliveriedQuantity;
    private long itemId;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_note_id")
    private DeliveryNote deliveryNote;
}
