package com.example.demo.controller;

import com.example.demo.dto.ResponseObjectEntity;
import com.example.demo.dto.StoreDTO;
import com.example.demo.facade.AuthFacade;
import com.example.demo.response.Token;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {

    @Autowired
    private AuthFacade authFacade;

    @PostMapping("/auth/login")
    public ResponseEntity<Token> login(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        return authFacade.login(userName, password);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ResponseObjectEntity> register(@RequestBody StoreDTO storeDTO) {
        return authFacade.getAuthService().saveStore(storeDTO);
    }
}
