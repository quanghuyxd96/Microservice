package com.example.demo.controller;

import com.example.demo.response.Token;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {
    @Autowired
    private AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<Token> login(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        String tokenByUserName = authService.generateTokenByUserName(userName, password);
        if (tokenByUserName == null) {
            return null;
        }
        return new ResponseEntity<Token>(new Token(tokenByUserName), HttpStatus.OK);
    }


}
