package com.example.demo.client;

import com.example.demo.dto.DeliveryNoteDTO;
import com.example.demo.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.utils.Constants.AUTHOR;

@FeignClient(name = "deliveryNoteFeignClient", url = "${client.post.baseUrl4}")
public interface DeliveryNoteFeignClient {
    @PostMapping("/delivery-note/")
    List<DeliveryNoteDTO> saveDeliveryNote(@RequestBody List<OrderDTO> orderDTOS, @RequestHeader(AUTHOR) String token);

    @GetMapping("/delivery-note/get/date")
    ResponseEntity<List<DeliveryNoteDTO>> getAllDeliveryNotesByDate(@RequestParam("date")
                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                    LocalDate deliveryDate,
                                                                    @RequestHeader(AUTHOR) String token);
}
