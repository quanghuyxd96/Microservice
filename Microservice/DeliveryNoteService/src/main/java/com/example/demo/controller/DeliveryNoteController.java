package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.DeliveryNote;
import com.example.demo.facade.DeliveryNoteFacade;
import com.example.demo.service.DeliveryNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/delivery-note")
public class DeliveryNoteController {
    @Autowired
    private DeliveryNoteService deliveryNoteService;

    @Autowired
    private DeliveryNoteFacade deliveryNoteFacade;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public DeliveryNote saveDeliveryNote(@RequestBody DeliveryNote deliveryNote) {
        return deliveryNoteService.save(deliveryNote);
    }

    @GetMapping("/delivery-notes")
    public List<DeliveryNote> getAllDeliveryNotes() {
        return deliveryNoteService.getAllDeliveryNote();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<DeliveryNote> getDeliveryNote(@PathVariable("id") Long id) {
        return new ResponseEntity<>(deliveryNoteService.getDeliveryNoteById(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public DeliveryNote updateDeliveryNoteById(@PathVariable("id") Long id, @RequestBody DeliveryNote deliveryNote) {
        return deliveryNoteService.updateDeliveryNote(deliveryNote, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDeliveryNote(@PathVariable("id") long id) {
        deliveryNoteService.deleteDeliveryNoteById(id);
    }

    @GetMapping("/order/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrder(){
        System.out.println(deliveryNoteFacade.getDeliveryNoteService().getOrderFeignClient().getALlOrders());
        return new ResponseEntity<>(deliveryNoteFacade.getDeliveryNoteService().getOrderFeignClient().getALlOrders(),HttpStatus.OK);
    }
}
