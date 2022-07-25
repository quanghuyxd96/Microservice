package com.example.demo.client;

import com.example.demo.dto.StoreDTO;
import com.example.demo.response.ResponseObjectEntity;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.utils.Constant.AUTHOR;

@FeignClient(name = "storeFeignClient",url = "${client.post.baseUrl1}")
public interface StoreFeignClient {
    @GetMapping("/store/all-store")
    List<StoreDTO> getALlStore();

    @GetMapping("/store/get-store-by-id")
    ResponseEntity<StoreDTO> getStoreById(@RequestParam("id") Long id, @RequestHeader(AUTHOR) String token);


    @DeleteMapping("/store/delete")
    ResponseEntity<ResponseObjectEntity> deleteStoreById(@RequestParam("id") Long id);

    @GetMapping("/store/get/{userName}")
    ResponseEntity<StoreDTO> getStoreByUserName(@PathVariable("userName") String userName);

}
