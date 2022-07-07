package com.example.demo.entity;

import lombok.*;
import org.checkerframework.common.aliasing.qual.NonLeaked;

import javax.persistence.*;
import javax.persistence.criteria.Order;
import java.util.List;

@Entity
@Table(name = "Store")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    private long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String userName;
    private String password;
    @Transient
    private String confirmPassword;

    public Store(String name, String address, String phoneNumber, String email, String userName, String password, String confirmPassword) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
