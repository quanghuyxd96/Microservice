package com.example.demo.service;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.DeliveryItemDetail;
import com.example.demo.repository.DeliveryItemDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryItemDetailService {
    @Autowired
    private DeliveryItemDetailRepository deliveryItemDetailRepository;

    @Autowired
    private DeliveryNoteService deliveryNoteService;
    @Autowired
    private ItemFeignClient itemFeignClient;

    public List<DeliveryItemDetail> getAllDeliveryItemDetails() {
        return deliveryItemDetailRepository.findAll();
    }

    public DeliveryItemDetail saveDeliveryItemDetail(DeliveryItemDetail deliveryItemDetail) {
        return deliveryItemDetailRepository.save(deliveryItemDetail);
    }

    public List<DeliveryItemDetail> saveDeliveryItemDetail(List<DeliveryItemDetail> deliveryItemDetails) {
        return deliveryItemDetailRepository.saveAll(deliveryItemDetails);
    }

    public DeliveryItemDetail getDeliveryItemDetaillById(Long id) {
        Optional<DeliveryItemDetail> deliveryItemDetailRepositoryById = deliveryItemDetailRepository.findById(id);
        if (deliveryItemDetailRepositoryById.isPresent()) {
            return deliveryItemDetailRepositoryById.get();
        }
        return null;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void checkDeliveryItemUndelivery() {
        List<DeliveryItemDetail> deliveryItemDetails = deliveryItemDetailRepository.getItemUndeliveried();
        List<ItemDTO> items = new ArrayList<>();
        List<DeliveryItemDetail> deliveryItemDetailsToUpdate = new ArrayList<>();
        for (DeliveryItemDetail deliveryItemDetail : deliveryItemDetails) {
            ItemDTO item = itemFeignClient.getItemById(deliveryItemDetail.getItemId());
            if (item.getQuantity() == 0) {
                continue;
            } else {
                if (item.getQuantity() >= deliveryItemDetail.getUndeliveriedQuantity()) {
                    deliveryItemDetail.setDeliveriedQuantity(deliveryItemDetail.getUndeliveriedQuantity());
                    deliveryItemDetail.setAccumulationQuantity(deliveryItemDetail.getTotalDeliveriedQuantity());
                    System.out.println("Đã giao đủ: " + deliveryItemDetail.getDeliveriedQuantity());
                    deliveryItemDetail.setUndeliveriedQuantity(0);
                    item.setQuantity(item.getQuantity() - deliveryItemDetail.getDeliveriedQuantity());
                    System.out.println("Chưa giao đủ: " + deliveryItemDetail.getUndeliveriedQuantity());
                    items.add(item);
                } else {
                    deliveryItemDetail.setDeliveriedQuantity(item.getQuantity());
                    deliveryItemDetail.setAccumulationQuantity(deliveryItemDetail.getAccumulationQuantity() + deliveryItemDetail.getDeliveriedQuantity());
                    deliveryItemDetail.setUndeliveriedQuantity(deliveryItemDetail.getTotalDeliveriedQuantity() - deliveryItemDetail.getAccumulationQuantity());
                    item.setQuantity(0);
                    System.out.println("Đã giao thiếu: " + deliveryItemDetail.getDeliveriedQuantity());
                    System.out.println("Chưa giao thiếu: " + deliveryItemDetail.getUndeliveriedQuantity());
                    items.add(item);
                }
                deliveryItemDetailsToUpdate.add(deliveryItemDetail);
            }
        }
        for (int i = 0; i < deliveryItemDetailsToUpdate.size(); i++) {
            List<DeliveryItemDetail> itemDetails = new ArrayList<>();
            itemDetails.add(deliveryItemDetails.get(i));
            for (int j = i + 1; j < deliveryItemDetailsToUpdate.size(); j++) {
                if (deliveryItemDetailsToUpdate.get(i).getDeliveryNote().getId() == deliveryItemDetailsToUpdate.get(j).getDeliveryNote().getId()) {
                    itemDetails.add(deliveryItemDetailsToUpdate.get(j));
                    deliveryItemDetailsToUpdate.remove(deliveryItemDetailsToUpdate.get(j--));
                }
            }
            deliveryNoteService.updateOrSaveDeliveryNote(itemDetails);
        }
        itemFeignClient.updateItemQuantity(items);
        System.out.println("end");
    }

    public void demoAOP(){
        System.out.println("demo");
    }


    public void deleteDeliveryItemDetailById(long id) {
        deliveryItemDetailRepository.deleteById(id);
    }

//    DeliveryItemDetail updateDeliveryItemDetailById(DeliveryItemDetail deliveryItemDetail, long id);
}
