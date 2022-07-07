package com.example.demo.controller;

import com.example.demo.entity.DeliveryNote;
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
}
