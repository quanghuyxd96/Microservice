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

    @GetMapping("/delivery-notes")
    public ResponseEntity<List<DeliveryNote>> getAllDeliveryNotes() {
        return new ResponseEntity<>(deliveryNoteService.getAllDeliveryNote(),HttpStatus.OK);
    }

    @GetMapping("/delivery-notes/order")
    public ResponseEntity<List<DeliveryNote>> getAllDeliveryNotesByOrderId(@RequestParam("id") long orderId){
        List<DeliveryNote> deliveryNotes = deliveryNoteFacade.getAllDeliveryNotesByOrderId(orderId);
        if(deliveryNotes == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deliveryNotes,HttpStatus.OK);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<DeliveryNote> getDeliveryNote(@RequestParam("id") Long id) {
        return new ResponseEntity<>(deliveryNoteService.getDeliveryNoteById(id), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<DeliveryNote> updateDeliveryNoteById(@RequestParam("id") Long id, @RequestBody DeliveryNote deliveryNote) {
        return new ResponseEntity<>(deliveryNoteService.updateDeliveryNote(deliveryNote, id),HttpStatus.OK) ;
    }

    @DeleteMapping("/delete")
    public void deleteDeliveryNote(@RequestParam("id") long id) {
        deliveryNoteService.deleteDeliveryNoteById(id);
    }
}
