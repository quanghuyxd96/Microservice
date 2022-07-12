package com.example.demo.service;

import com.example.demo.client.OrderFeignClient;
import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.DeliveryNote;
import com.example.demo.repository.DeliveryNoteRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
public class DeliveryNoteService {
    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @Autowired
    private OrderFeignClient orderFeignClient;

    public List<DeliveryNote> getAllDeliveryNote() {
        return deliveryNoteRepository.findAll();
    }


    //chỗ này
//    public DeliveryNote save(OrderDTO orderDTO) {
//        return deliveryNoteRepository.save(deliveryNote);
//    }


    public DeliveryNote updateDeliveryNote(DeliveryNote deliveryNote, long id) {
        Optional<DeliveryNote> deliveryNoteRepositoryById = deliveryNoteRepository.findById(id);
        deliveryNoteRepositoryById.get().setOrderId(deliveryNote.getOrderId());
        deliveryNoteRepositoryById.get().setDeliveryDate(deliveryNote.getDeliveryDate());
        return deliveryNoteRepository.save(deliveryNoteRepositoryById.get());
    }


    public DeliveryNote getDeliveryNoteById(long id) {
        Optional<DeliveryNote> deliveryNote = deliveryNoteRepository.findById(id);
        if (deliveryNote.isPresent()) {
            return deliveryNote.get();
        }
        return null;
    }


    public void deleteDeliveryNoteById(long id) {
        deliveryNoteRepository.deleteById(id);
    }

}
