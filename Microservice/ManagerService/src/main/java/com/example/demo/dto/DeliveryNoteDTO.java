package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DeliveryNoteDTO {
    private long id;
    private LocalDate deliveryDate;
    private long orderId;
    private List<DeliveryItemDetailDTO> deliveryItemDetails;
}
