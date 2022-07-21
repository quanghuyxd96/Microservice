package com.example.demo.client;

import com.example.demo.dto.StoreDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "storeFeignClient", url = "${client.post.baseUrl2}")
public interface StoreFeignClient {
    @GetMapping("/store/get-store-by-userName")
    ResponseEntity<StoreDTO> getStoreByUserName(@RequestParam("userName") String userName,
                                                @RequestParam("password") String password);
}
