package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private long id;
    private double totalPrice;
    private LocalDate orderDate;
    private long storeId;
    private List<OrderDetailDTO> orderDetailDTOS;
}
