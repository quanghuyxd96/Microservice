package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Store")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String userName;
    private String password;
    private double payment;
    @Transient
    private String confirmPassword;

    public Store(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
