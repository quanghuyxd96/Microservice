package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private long id;
    private long itemQuantity;
    private long itemId;
    private long orderId;

    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "id=" + id +
                ", itemQuantity=" + itemQuantity +
                ", itemId=" + itemId +
                ", orderId=" + orderId +
                '}';
    }
}
