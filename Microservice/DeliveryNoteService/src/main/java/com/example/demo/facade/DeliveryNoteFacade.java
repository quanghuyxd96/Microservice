package com.example.demo.facade;

import com.example.demo.entity.DeliveryNote;
import com.example.demo.service.DeliveryNoteService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Getter
@Component
public class DeliveryNoteFacade {
    @Autowired
    private DeliveryNoteService deliveryNoteService;

    //    public List<DeliveryNote> getAllDeliveryNotes() {
//        List<DeliveryNote> deliveryNotes = deliveryNoteService.getAllDeliveryNote();
//        if (deliveryNotes == null) {
//            return null;
//        }
//        return convertListModel(orders, OrderModel.class);
//    }
    public List<DeliveryNote> getAllDeliveryNotesByOrderId(long id) {
        List<DeliveryNote> deliveryNotes = deliveryNoteService.getAllDeliveryNoteByoOrderId(id);
        if (deliveryNotes == null) {
            return null;
        }
        return deliveryNotes;

    }

    public ResponseEntity<List<DeliveryNote>> getAllDeliveryNotesByDate(LocalDate deliveryDate) {
        List<DeliveryNote> deliveryNotes = deliveryNoteService.getAllDeliveryNoteByDate(deliveryDate);
        if (deliveryNotes == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(deliveryNotes, HttpStatus.OK);
    }
}
