package com.example.demo.client;

import com.example.demo.dto.DeliveryNoteDTO;
import com.example.demo.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "deliveryNoteFeignClient", url = "${client.post.baseUrl4}")
public interface DeliveryNoteFeignClient {
    @PostMapping("/delivery-note/")
    List<DeliveryNoteDTO> saveDeliveryNote(@RequestBody List<OrderDTO> orderDTOS);
}
