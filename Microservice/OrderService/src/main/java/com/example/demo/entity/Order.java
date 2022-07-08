package com.example.demo.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.Store;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Data
@Getter
@Setter
@Table(name = "Orders")
public class Order  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double totalPrice;
    private LocalDate orderDate;
    private long managerId;
    private long storeId;
}

