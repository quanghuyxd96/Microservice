package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryItemDetailDTO {
    private long id;
    private long deliveriedQuantity;
    private long accumulationQuantity;
    private long undeliveriedQuantity;
    private long totalDeliveriedQuantity;
    private long itemId;
}
