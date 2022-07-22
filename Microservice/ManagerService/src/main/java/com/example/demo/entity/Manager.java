package com.example.demo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class Manager {

    @Id
    private final long id=1;
    private String userName;
    private String password;
    private String email;

    public Manager(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
