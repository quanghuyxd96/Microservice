package com.example.demo.service;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.DeliveryItemDetail;
import com.example.demo.mq.DeliverySource;
import com.example.demo.repository.DeliveryItemDetailRepository;
import com.example.demo.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
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

    @Autowired
    private DeliverySource deliverySource;

    private static final Logger logger = LoggerFactory.getLogger(DeliveryItemDetailService.class);

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

    @Scheduled(cron = "0 0 1 * * *")
    public void checkDeliveryItemUndelivery() {
        String token = deliveryNoteService.generateToken();
        List<DeliveryItemDetail> deliveryItemDetails = deliveryItemDetailRepository.getItemUndeliveried();
        List<DeliveryItemDetail> itemDetails = new ArrayList<>();
        for (int i = 0; i < deliveryItemDetails.size(); i++) {
            long undeliveriedQuantity = deliveryItemDetails.get(i).getUndeliveriedQuantity();
            for (int j = i + 1; j < deliveryItemDetails.size(); j++) {
                if (deliveryItemDetails.get(i).equals(deliveryItemDetails.get(j))) {
                    undeliveriedQuantity = Math.min(undeliveriedQuantity, deliveryItemDetails.get(j).getUndeliveriedQuantity());
                    deliveryItemDetails.remove(deliveryItemDetails.get(j--));
                }
            }
            deliveryItemDetails.get(i).setUndeliveriedQuantity(undeliveriedQuantity);
            deliveryItemDetails.get(i).setAccumulationQuantity(deliveryItemDetails.get(i).getTotalDeliveriedQuantity()
                    - deliveryItemDetails.get(i).getUndeliveriedQuantity());
            itemDetails.add(deliveryItemDetails.get(i));
        }
        List<ItemDTO> items = new ArrayList<>();
        List<DeliveryItemDetail> deliveryItemDetailsToUpdate = new ArrayList<>();
        for (DeliveryItemDetail deliveryItemDetail : itemDetails) {
            ItemDTO item = itemFeignClient.getItemById(deliveryItemDetail.getItemId());
            if (item.getQuantity() == 0) {
                continue;
            } else {
                if (item.getQuantity() >= deliveryItemDetail.getUndeliveriedQuantity()) {
                    logger.info("Case: Item Quantity >= Item Undeliveried Quantity");
                    deliveryItemDetail.setDeliveriedQuantity(deliveryItemDetail.getUndeliveriedQuantity());
                    deliveryItemDetail.setAccumulationQuantity(deliveryItemDetail.getTotalDeliveriedQuantity());
                    logger.info("Deliveried: " + deliveryItemDetail.getDeliveriedQuantity());
                    deliveryItemDetail.setUndeliveriedQuantity(0);
                    item.setQuantity(item.getQuantity() - deliveryItemDetail.getDeliveriedQuantity());
                    logger.info("Remain: " + deliveryItemDetail.getUndeliveriedQuantity());
                    items.add(item);
                } else {
                    logger.info("Case: Item Quantity < Item Undeliveried Quantity");
                    deliveryItemDetail.setDeliveriedQuantity(item.getQuantity());
                    deliveryItemDetail.setAccumulationQuantity(deliveryItemDetail.getAccumulationQuantity() + deliveryItemDetail.getDeliveriedQuantity());
                    deliveryItemDetail.setUndeliveriedQuantity(deliveryItemDetail.getTotalDeliveriedQuantity() - deliveryItemDetail.getAccumulationQuantity());
                    item.setQuantity(0);
                    logger.info("Deliveried: " + deliveryItemDetail.getDeliveriedQuantity());
                    logger.info("Remain: " + deliveryItemDetail.getUndeliveriedQuantity());
                    items.add(item);
                }
                deliveryItemDetailsToUpdate.add(deliveryItemDetail);
            }
        }
        for (int i = 0; i < deliveryItemDetailsToUpdate.size(); i++) {
            List<DeliveryItemDetail> itemDetails1 = new ArrayList<>();
            itemDetails1.add(itemDetails.get(i));
            for (int j = i + 1; j < deliveryItemDetailsToUpdate.size(); j++) {
                if (deliveryItemDetailsToUpdate.get(i).getDeliveryNote().getId() == deliveryItemDetailsToUpdate.get(j)
                        .getDeliveryNote().getId()) {
                    itemDetails1.add(deliveryItemDetailsToUpdate.get(j));
                    deliveryItemDetailsToUpdate.remove(deliveryItemDetailsToUpdate.get(j--));
                }
            }
            deliveryNoteService.updateOrSaveDeliveryNote(itemDetails1);
        }
        deliverySource.updateItemQuantity().send(MessageBuilder.withPayload(items).build());
        logger.info("End Function");
    }
    
    public void removeDeliveryItemDetail(long itemId, long orderId, List<DeliveryItemDetail> deliveryItemDetails) {
        for (int i = 0; i < deliveryItemDetails.size(); i++) {
            if (deliveryItemDetails.get(i).getItemId() == itemId &&
                    deliveryItemDetails.get(i).getOrderId() == orderId
            ) {
                deliveryItemDetails.remove(deliveryItemDetails);
            }
        }
    }

    public void deleteDeliveryItemDetailById(long id) {
        deliveryItemDetailRepository.deleteById(id);
    }

    public void deleteDeliveryItemDetailByDeliveryNoteId(long id){
        deliveryItemDetailRepository.deleteByDeliveryNoteId(id);
    }

//    DeliveryItemDetail updateDeliveryItemDetailById(DeliveryItemDetail deliveryItemDetail, long id);
    //    @Scheduled(cron = "0 */1 * * * *")
//    public void checkDeliveryItemUndelivery() {
//        List<DeliveryItemDetail> deliveryItemDetails = deliveryItemDetailRepository.getItemUndeliveried();
//        List<DeliveryItemDetail> itemDetails = new ArrayList<>();
//        for (int i = 0; i < deliveryItemDetails.size(); i++) {
//            long undeliveriedQuantity = deliveryItemDetails.get(i).getUndeliveriedQuantity();
//            for (int j = i + 1; j < deliveryItemDetails.size(); j++) {
//                if (deliveryItemDetails.get(i).equals(deliveryItemDetails.get(j))) {
//                    undeliveriedQuantity = Math.min(undeliveriedQuantity, deliveryItemDetails.get(j).getUndeliveriedQuantity());
//                    deliveryItemDetails.remove(deliveryItemDetails.get(j--));
//                }
//            }
//            deliveryItemDetails.get(i).setUndeliveriedQuantity(undeliveriedQuantity);
//            deliveryItemDetails.get(i).setAccumulationQuantity(deliveryItemDetails.get(i).getTotalDeliveriedQuantity()
//                    - deliveryItemDetails.get(i).getUndeliveriedQuantity());
//            itemDetails.add(deliveryItemDetails.get(i));
//        }
//        List<ItemDTO> items = new ArrayList<>();
//        List<DeliveryItemDetail> deliveryItemDetailsToUpdate = new ArrayList<>();
//        for (DeliveryItemDetail deliveryItemDetail : deliveryItemDetails) {
//            ItemDTO item = itemFeignClient.getItemById(deliveryItemDetail.getItemId());
//            if (item.getQuantity() == 0) {
//                continue;
//            } else {
//                if (item.getQuantity() >= deliveryItemDetail.getUndeliveriedQuantity()) {
//                    deliveryItemDetail.setDeliveriedQuantity(deliveryItemDetail.getUndeliveriedQuantity());
//                    deliveryItemDetail.setAccumulationQuantity(deliveryItemDetail.getTotalDeliveriedQuantity());
//                    System.out.println("Đã giao đủ: " + deliveryItemDetail.getDeliveriedQuantity());
//                    deliveryItemDetail.setUndeliveriedQuantity(0);
//                    item.setQuantity(item.getQuantity() - deliveryItemDetail.getDeliveriedQuantity());
//                    System.out.println("Chưa giao đủ: " + deliveryItemDetail.getUndeliveriedQuantity());
//                    items.add(item);
//                } else {
//                    deliveryItemDetail.setDeliveriedQuantity(item.getQuantity());
//                    deliveryItemDetail.setAccumulationQuantity(deliveryItemDetail.getAccumulationQuantity() + deliveryItemDetail.getDeliveriedQuantity());
//                    deliveryItemDetail.setUndeliveriedQuantity(deliveryItemDetail.getTotalDeliveriedQuantity() - deliveryItemDetail.getAccumulationQuantity());
//                    item.setQuantity(0);
//                    System.out.println("Đã giao thiếu: " + deliveryItemDetail.getDeliveriedQuantity());
//                    System.out.println("Chưa giao thiếu: " + deliveryItemDetail.getUndeliveriedQuantity());
//                    items.add(item);
//                }
//                deliveryItemDetailsToUpdate.add(deliveryItemDetail);
//            }
//        }
//        for (int i = 0; i < deliveryItemDetailsToUpdate.size(); i++) {
//            List<DeliveryItemDetail> itemDetails1 = new ArrayList<>();
//            itemDetails1.add(deliveryItemDetails.get(i));
//            for (int j = i + 1; j < deliveryItemDetailsToUpdate.size(); j++) {
//                if (deliveryItemDetailsToUpdate.get(i).getDeliveryNote().getId() == deliveryItemDetailsToUpdate.get(j)
//                        .getDeliveryNote().getId()) {
//                    itemDetails1.add(deliveryItemDetailsToUpdate.get(j));
//                    deliveryItemDetailsToUpdate.remove(deliveryItemDetailsToUpdate.get(j--));
//                }
//            }
//            deliveryNoteService.updateOrSaveDeliveryNote(itemDetails1);
//        }
//        itemFeignClient.updateItemQuantity(items);
//        System.out.println("end");
//    }
}
