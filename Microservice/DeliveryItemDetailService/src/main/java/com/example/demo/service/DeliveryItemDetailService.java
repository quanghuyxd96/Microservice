package com.example.demo.service;

import com.example.demo.entity.DeliveryItemDetail;
import com.example.demo.repository.DeliveryItemDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DeliveryItemDetailService {
    @Autowired
    private DeliveryItemDetailRepository deliveryItemDetailRepository;

    public List<DeliveryItemDetail> getAllDeliveryItemDetails() {
        return deliveryItemDetailRepository.findAll();
    }

    public DeliveryItemDetail saveDeliveryItemDetail(DeliveryItemDetail deliveryItemDetail){
        return deliveryItemDetailRepository.save(deliveryItemDetail);
    }

    public DeliveryItemDetail getDeliveryItemDetaillById(Long id){
        Optional<DeliveryItemDetail> deliveryItemDetailRepositoryById = deliveryItemDetailRepository.findById(id);
        if(deliveryItemDetailRepositoryById.isPresent()){
            return deliveryItemDetailRepositoryById.get();
        }
        return null;
    }

    public void deleteDeliveryItemDetailById(long id){
        deliveryItemDetailRepository.deleteById(id);
    }

//    DeliveryItemDetail updateDeliveryItemDetailById(DeliveryItemDetail deliveryItemDetail, long id);
}
