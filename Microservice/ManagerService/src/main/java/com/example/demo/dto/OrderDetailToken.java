package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.internal.build.AllowPrintStacktrace;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailToken extends OrderDetailDTO {
    private String token;
}
