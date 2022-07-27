package com.example.demo.client;

import com.example.demo.dto.ItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.utils.Constants.AUTHOR;

@FeignClient(name = "itemFeignClient", url = "${client.post.baseUrl2}")
public interface ItemFeignClient {
    @GetMapping("/item/get")
    ItemDTO getItemById(@RequestParam("id") Long id);

    @PutMapping("/item/update/update-quantity")
    ResponseEntity<List<ItemDTO>> updateItemQuantity(@RequestBody List<ItemDTO> items, @RequestHeader(AUTHOR) String token);


}
