package com.example.demo.client;

import com.example.demo.dto.StoreDTO;
import com.example.demo.response.ResponseObjectEntity;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "storeFeignClient",url = "${client.post.baseUrl1}")
public interface StoreFeignClient {
    @GetMapping("/store/all-store")
    List<StoreDTO> getALlStore();

    @GetMapping("/store/get-store-by-id")
    ResponseEntity<StoreDTO> getStoreById(@RequestParam("id") Long id);


    @DeleteMapping("/store/delete")
    ResponseEntity<ResponseObjectEntity> deleteStoreById(@RequestParam("id") Long id);


}
