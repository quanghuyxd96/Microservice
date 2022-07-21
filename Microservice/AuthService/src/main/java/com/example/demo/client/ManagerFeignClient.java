package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "storeFeignClient", url = "${client.post.baseUrl1}")
public interface ManagerFeignClient {
    @PostMapping("/manager/authenticate")
    String getManagerUserName(@RequestParam("userName") String userName,
                              @RequestParam("password") String password);
}
