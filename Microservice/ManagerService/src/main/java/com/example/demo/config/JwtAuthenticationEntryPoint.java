//package com.example.demo.config;
//
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.Serializable;
//
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
//            throws IOException, ServletException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
//    }
//}
