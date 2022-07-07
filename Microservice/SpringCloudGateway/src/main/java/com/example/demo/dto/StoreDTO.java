package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {
    private long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String userName;
    private String password;
    private String confirmPassword;
}
