//package com.example.demo.config;
//
//import java.util.Arrays;
//
//import javax.servlet.Filter;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class LoggingConfiguration {
//
//    @Bean
//    public FilterRegistrationBean registerRequestResponseLoggingFilter() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(requestAndResponseLoggingFilter());
//        registration.setUrlPatterns(Arrays.asList("/api/*"));
//        registration.setName("requestResponseLoggingFilter");
//        registration.setOrder(2);
//        return registration;
//    }
//
//    @Bean
//    public Filter requestAndResponseLoggingFilter() {
//        return new RequestAndResponseLoggingFilter();
//    }
//}
