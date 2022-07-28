package com.example.demo.facade;

import com.example.demo.entity.DeliveryNote;
import com.example.demo.service.DeliveryNoteService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public List<DeliveryNote> getAllDeliveryNotesByOrderId (long id){
        List<DeliveryNote> deliveryNotes = deliveryNoteService.getAllDeliveryNoteByoOrderId(id);
        if(deliveryNotes == null){
            return null;
        }
        return deliveryNotes;

    }
}
