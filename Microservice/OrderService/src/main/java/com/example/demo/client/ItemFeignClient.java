package com.example.demo.client;

import com.example.demo.dto.ItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "itemFeignClient", url = "${client.post.baseUrl}")
public interface ItemFeignClient {

    @GetMapping("/item/items")
    public List<ItemDTO> getAllItem() ;

//    @GetMapping("/item/items-by-order-detail-id")
//    public List<ItemDTO> getItemByOrderDetail(@RequestParam("id") Long id);
}
