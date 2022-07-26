package com.example.demo.service;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.client.OrderFeignClient;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.DeliveryItemDetail;
import com.example.demo.entity.DeliveryNote;
import com.example.demo.mq.DeliverySource;
import com.example.demo.mq.OrderSource;
import com.example.demo.repository.DeliveryNoteRepository;
import com.example.demo.utils.JwtTokenUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.utils.Constants.ADMIN;

@Service
@Getter
public class DeliveryNoteService {
    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @Autowired
    private DeliveryItemDetailService deliveryItemDetailService;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ItemFeignClient itemFeignClient;

    @Autowired
    private DeliverySource deliverySource;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public DeliveryNote saveDelivery(List<OrderDetailDTO> orderDetailDTOList) {
        String token = generateToken();
        DeliveryNote deliveryNote = new DeliveryNote();
        List<DeliveryItemDetail> deliveryItemDetails = new ArrayList<>();
        List<ItemDTO> items = new ArrayList<>();
        System.out.println(orderDetailDTOList.get(0).toString());
        OrderDTO orderDTO = orderFeignClient.getOrderById(orderDetailDTOList.get(0).getOrderId(), token).getBody();
        deliveryNote.setDeliveryDate(orderDTO.getOrderDate().plusDays(7));
        deliveryNote.setOrderId(orderDTO.getId());
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
            DeliveryItemDetail deliveryItemDetail = new DeliveryItemDetail();
            deliveryItemDetail.setItemId(orderDetailDTO.getItemId());
            ItemDTO item = itemFeignClient.getItemById(orderDetailDTO.getItemId());
            deliveryItemDetail.setTotalDeliveriedQuantity(orderDetailDTO.getItemQuantity());
            deliveryItemDetail.setOrderId(orderDTO.getId());
            long itemQuantityInStorage = item.getQuantity();
            if (orderDetailDTO.getItemQuantity() <= itemQuantityInStorage) {
                deliveryItemDetail.setDeliveriedQuantity(orderDetailDTO.getItemQuantity());
                deliveryItemDetail.setUndeliveriedQuantity(0);
                item.setQuantity(item.getQuantity() - orderDetailDTO.getItemQuantity());
                items.add(item);
            } else {
                deliveryItemDetail.setDeliveriedQuantity(itemQuantityInStorage);
                deliveryItemDetail.setUndeliveriedQuantity(deliveryItemDetail.getTotalDeliveriedQuantity() - deliveryItemDetail.getDeliveriedQuantity());
                item.setQuantity(0);
                items.add(item);
            }
            deliveryItemDetail.setAccumulationQuantity(deliveryItemDetail.getDeliveriedQuantity());
            deliveryItemDetail.setDeliveryNote(deliveryNote);
            deliveryItemDetails.add(deliveryItemDetail);
        }
        deliveryNote.setDeliveryItemDetails(deliveryItemDetails);
        DeliveryNote deliveryNoteSave = deliveryNoteRepository.save(deliveryNote);
        deliverySource.updateItemQuantity().send(MessageBuilder.withPayload(items).build());
        return deliveryNoteSave;
    }


    public List<DeliveryNote> getAllDeliveryNote() {
        List<DeliveryNote> deliveryNotes = deliveryNoteRepository.findAll();
        if (deliveryNotes == null) {
            return null;
        }
        return deliveryNotes;
    }

    public DeliveryNote getDeliveryNoteById(long id) {
        Optional<DeliveryNote> deliveryNote = deliveryNoteRepository.findById(id);
        if (deliveryNote.isPresent()) {
            return deliveryNote.get();
        }
        return null;
    }

    public DeliveryNote updateDeliveryNote(DeliveryNote deliveryNote, long id) {
        Optional<DeliveryNote> deliveryNoteRepositoryById = deliveryNoteRepository.findById(id);
        if (!deliveryNoteRepositoryById.isPresent()) {
            return null;
        }
        deliveryNoteRepositoryById.get().setOrderId(deliveryNote.getOrderId());
        deliveryNoteRepositoryById.get().setDeliveryDate(deliveryNote.getDeliveryDate());
        return deliveryNoteRepository.save(deliveryNoteRepositoryById.get());
    }

    public void updateOrSaveDeliveryNote(DeliveryItemDetail deliveryItemDetail) {
        Optional<DeliveryNote> deliveryNote = deliveryNoteRepository.findById(deliveryItemDetail.getDeliveryNote().getId());
        if (deliveryNote.get().getDeliveryDate().isAfter(LocalDate.now())) {
            deliveryItemDetailService.saveDeliveryItemDetail(deliveryItemDetail);
        } else {
            List<DeliveryItemDetail> deliveryItemDetails = deliveryNote.get().getDeliveryItemDetails();
            DeliveryNote deliveryNote1 = new DeliveryNote();
            deliveryNote1.setDeliveryDate(LocalDate.now().plusDays(1));
            deliveryNote1.setOrderId(deliveryNote.get().getOrderId());
            for (int i = 0; i < deliveryItemDetails.size(); i++) {
                if (deliveryItemDetails.get(i).getId() == deliveryItemDetail.getId()) {
                    deliveryItemDetails.remove(deliveryItemDetails.get(i));
                    break;
                }
            }
            deliveryItemDetail.setId(-1);
            deliveryItemDetails.add(deliveryItemDetail);
            deliveryNote1.setDeliveryItemDetails(deliveryItemDetails);
            deliveryNoteRepository.save(deliveryNote1);
        }
    }

    public void updateOrSaveDeliveryNote(List<DeliveryItemDetail> deliveryItemDetails) {
        Optional<DeliveryNote> deliveryNote = deliveryNoteRepository.findById(deliveryItemDetails.get(0).getDeliveryNote().getId());
        System.out.println(deliveryItemDetails.get(0).getDeliveryNote().getId());
        if (deliveryNote.get().getDeliveryDate().isAfter(LocalDate.now())) {
            deliveryItemDetailService.saveDeliveryItemDetail(deliveryItemDetails);
        } else {
            DeliveryNote deliveryNote1 = new DeliveryNote();
            for (DeliveryItemDetail deliveryItemDetail : deliveryItemDetails) {
                deliveryItemDetail.setId(0);
                deliveryItemDetail.setDeliveryNote(deliveryNote1);
            }
            deliveryNote1.setDeliveryItemDetails(deliveryItemDetails);
            deliveryNote1.setOrderId(deliveryNote.get().getOrderId());
            deliveryNote1.setDeliveryDate(LocalDate.now().plusDays(2));
            deliveryNoteRepository.save(deliveryNote1);
        }
    }

    public boolean deleteDeliveryNoteById(long id) {
        Optional<DeliveryNote> deliveryNoteRepositoryById = deliveryNoteRepository.findById(id);
        if (!deliveryNoteRepositoryById.isPresent()) {
            return false;
        }
        deliveryNoteRepository.deleteById(id);
        return true;
    }

    public boolean deleteDeliveryNoteByOrderId(long id) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findDeliveryNoteByOrderId(id);
        if (deliveryNote == null) {
            return false;
        }
        deliveryNoteRepository.deleteById(deliveryNote.getId());
        return true;
    }

    public String generateToken() {
        return "Bearer " + jwtTokenUtil.generateToken(ADMIN);
    }

    @StreamListener(target = OrderSource.ORDER_DELIVERY_CHANEL)
    public void processHelloChannelGreeting(List<OrderDetailDTO> orderDetails) {
        saveDelivery(orderDetails);
    }


    @StreamListener(target = OrderSource.DELIVERY_CHANNEL)
    public void listenToDeleteDeliveryByOrderId(Long orderId) {
        deleteDeliveryNoteByOrderId(orderId);
    }
}
