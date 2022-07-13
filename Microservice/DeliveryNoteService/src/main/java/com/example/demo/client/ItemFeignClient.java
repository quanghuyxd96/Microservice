package com.example.demo.client;

import com.example.demo.dto.ItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "itemFeignClient", url = "${client.post.baseUrl2}")
public interface ItemFeignClient {
    @GetMapping("/item/get")
    ItemDTO getItemById(@RequestParam("id") Long id);

    @PostMapping("/item/update-quantity")
    List<ItemDTO> updateItemQuantity(@RequestBody List<ItemDTO> items);
}
