package com.example.demo.service;

import com.example.demo.client.OrderFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.StoreDTO;
import com.example.demo.entity.Payment;
import com.example.demo.repository.PaymentRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Payment saveAndUpdatePaymentOfStore(Payment payment) {
        OrderDTO orderDTO = orderFeignClient.getOrderById(payment.getOrderId()).getBody();
        if (orderDTO == null) {
            return null;
        } else {
            StoreDTO storeDTO = storeFeignClient.getStoreById(orderDTO.getStoreId()).getBody();
            Payment paymentRepo = paymentRepository.findByOrderId(orderDTO.getId());
            if (paymentRepo == null) {
                payment.setMoneyUnpaid(orderDTO.getTotalPrice() - payment.getMoneyPaid());
                payment.setStoreUser(storeDTO.getUserName());
                return paymentRepository.save(payment);
            }
            paymentRepo.setMoneyPaid(paymentRepo.getMoneyPaid() + payment.getMoneyPaid());
            paymentRepo.setMoneyUnpaid(orderDTO.getTotalPrice() - paymentRepo.getMoneyPaid());
            return paymentRepository.save(paymentRepo);
        }
    }

}
