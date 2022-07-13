package com.example.demo.service;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.client.OrderFeignClient;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.DeliveryItemDetail;
import com.example.demo.entity.DeliveryNote;
import com.example.demo.repository.DeliveryNoteRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Getter
public class DeliveryNoteService {
    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ItemFeignClient itemFeignClient;

    public List<DeliveryNote> getAllDeliveryNote() {
        return deliveryNoteRepository.findAll();
    }

    //chờ xí
    public DeliveryNote saveDelivery(OrderDTO orderDTO) {
        DeliveryNote deliveryNote = new DeliveryNote();

        System.out.println("Ban đầu: "+deliveryNoteRepository.save(deliveryNote).getId());
        List<DeliveryItemDetail> deliveryItemDetails = new ArrayList<>();
        List<OrderDetailDTO> orderDetails = orderFeignClient.getAllOrderDetailsByOrderId(orderDTO.getId());
        List<ItemDTO> items = new ArrayList<>();
        deliveryNote.setDeliveryDate(orderDTO.getOrderDate().plusDays(7));
        deliveryNote.setOrderId(orderDTO.getId());
        for (OrderDetailDTO orderDetailDTO : orderDetails) {
            DeliveryItemDetail deliveryItemDetail = new DeliveryItemDetail();
            deliveryItemDetail.setItemId(orderDetailDTO.getItemId());
            ItemDTO item = itemFeignClient.getItemById(orderDetailDTO.getItemId());
            long itemQuantityInStorage = item.getQuantity();
            if (orderDetailDTO.getItemQuantity() <= itemQuantityInStorage) {
                deliveryItemDetail.setDeliveriedQuantity(orderDetailDTO.getItemQuantity());
                deliveryItemDetail.setUnDeliveriedQuantity(0);
                item.setQuantity(item.getQuantity()-orderDetailDTO.getItemQuantity());
                items.add(item);
            } else {
                deliveryItemDetail.setDeliveriedQuantity(itemQuantityInStorage);
                deliveryItemDetail.setUnDeliveriedQuantity(orderDetailDTO.getItemQuantity() - itemQuantityInStorage);
                item.setQuantity(0);
                items.add(item);
            }
            deliveryItemDetail.setDeliveryNote(deliveryNote);
            deliveryItemDetails.add(deliveryItemDetail);
        }
        deliveryNote.setDeliveryItemDetails(deliveryItemDetails);
        DeliveryNote deliveryNoteSave = deliveryNoteRepository.save(deliveryNote);
        System.out.println("Lúc sau"+deliveryNoteSave.getId());
        itemFeignClient.updateItemQuantity(items);

        return deliveryNoteSave;
    }

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
