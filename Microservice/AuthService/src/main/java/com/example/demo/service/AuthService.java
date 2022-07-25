package com.example.demo.service;

import com.example.demo.client.ManagerFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.ResponseObjectEntity;
import com.example.demo.dto.StoreDTO;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        String store = storeFeignClient.getStoreUserName(userName, password);
        if (store == null) {
            return null;
        }
        return jwtUtil.generateToken(userName);
    }

    public ResponseEntity<ResponseObjectEntity> saveStore(StoreDTO storeDTO){
        if(storeDTO.getUserName().startsWith("admin")){
            return new ResponseEntity<>(new ResponseObjectEntity("False","The account is not valid"),HttpStatus.BAD_REQUEST);
        }

        System.out.println(storeDTO.getConfirmPassword());
        return storeFeignClient.saveStore(storeDTO);
    }
}
