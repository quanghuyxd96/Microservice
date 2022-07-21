package com.example.demo.controllers;

import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {
    //
//	@Autowired
//	private JwtUtil jwtUtil;
//
//	@PostMapping("/auth/login")
//	public ResponseEntity<String> login(@RequestParam String userName) {
//		String token = jwtUtil.generateToken(userName);
//
//		return new ResponseEntity<String>(token, HttpStatus.OK);
//	}
//
//	@PostMapping("/auth/register")
//	public ResponseEntity<String> register(@RequestParam String userName) {
//		// Persist user to some persistent storage
//		System.out.println("Info saved...");
//
//		return new ResponseEntity<String>("Registered", HttpStatus.OK);
//	}
    @Autowired
    private AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        String tokenByUserName = authService.generateTokenByUserName(userName, password);
        if (tokenByUserName == null) {
            return null;
        }
        return new ResponseEntity<String>(tokenByUserName, HttpStatus.OK);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestParam String userName) {
        // Persist user to some persistent storage
        System.out.println("Info saved...");

        return new ResponseEntity<String>("Registered", HttpStatus.OK);
    }

}
