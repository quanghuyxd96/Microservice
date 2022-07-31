package com.example.demo.facade;

import com.example.demo.response.Token;
import com.example.demo.service.AuthService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AuthFacade {
    @Autowired
    private AuthService authService;

    public ResponseEntity<Token> login(String userName, String password) {
        String tokenByUserName = authService.generateTokenByUserName(userName, password);
        if (tokenByUserName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Token>(new Token(tokenByUserName), HttpStatus.OK);
    }
}
