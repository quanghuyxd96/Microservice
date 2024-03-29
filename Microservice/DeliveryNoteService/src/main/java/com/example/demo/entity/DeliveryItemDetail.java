package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

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
    private long orderId;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_note_id")
    private DeliveryNote deliveryNote;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryItemDetail that = (DeliveryItemDetail) o;
        return itemId == that.itemId && orderId == that.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, orderId);
    }
}
