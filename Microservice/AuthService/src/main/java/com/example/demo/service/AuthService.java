package com.example.demo.service;

import com.example.demo.client.ManagerFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.StoreDTO;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class AuthService {
    @Autowired
    private ManagerFeignClient managerFeignClient;
    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private JwtUtil jwtUtil;

    public String generateTokenByUserName(String userName, String password) {
        if (userName.startsWith("admin")) {
            String managerUserName = managerFeignClient.getManagerUserName(userName, password);
            if (managerUserName == null) {
                return null;
            }
            return jwtUtil.generateToken(userName);
        }
        ResponseEntity<StoreDTO> store = storeFeignClient.getStoreByUserName(userName, password);
        if (store == null) {
            return null;
        }
        return jwtUtil.generateToken(userName);
    }
}
