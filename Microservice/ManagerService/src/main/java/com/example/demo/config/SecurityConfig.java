package com.example.demo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    protected JwtRequestFilter jwtAuthenticationFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().ignoringAntMatchers("*/manager/**");
        http.cors().and().authorizeRequests().antMatchers("/","/login").permitAll()
                .anyRequest().authenticated().and().formLogin().loginProcessingUrl("/manager/login")
                .toString();
//                .loginPage("/HTML/Demo/LoginDemo.html")
//                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().httpBasic();

        // Thêm một lớp Filter kiểm tra jwt
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
