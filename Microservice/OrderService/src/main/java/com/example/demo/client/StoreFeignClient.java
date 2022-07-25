package com.example.demo.client;

import com.example.demo.dto.StoreDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.example.demo.utils.Constant.AUTHOR;

@FeignClient(name = "storeFeignClient", url = "${client.post.baseUrl1}")
public interface StoreFeignClient {
    @GetMapping("/store/get")
    StoreDTO getStoreByToken(@RequestHeader(AUTHOR) String token);
}
