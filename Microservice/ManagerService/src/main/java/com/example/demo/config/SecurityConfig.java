//package com.example.demo.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().ignoringAntMatchers("*/manager/**");
//        http.authorizeRequests().antMatchers("/manager/manage-item/items","/manager/manage-order/orders").permitAll()
//                .anyRequest().authenticated();
//
//        return http.build();
//    }
//}
