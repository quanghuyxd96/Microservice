package com.example.demo.service;

import com.example.demo.client.OrderFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.StoreDTO;
import com.example.demo.entity.Payment;
import com.example.demo.repository.PaymentRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Payment saveAndUpdatePaymentOfStore(Payment payment) {
        String token = managerService.generateToken();
        OrderDTO orderDTO = orderFeignClient.getOrderById(payment.getOrderId(), token).getBody();
        if (orderDTO == null) {
            return null;
        } else {
            StoreDTO storeDTO = storeFeignClient.getStoreById(orderDTO.getStoreId(), token).getBody();
//            Payment paymentRepo = paymentRepository.findByOrderId(orderDTO.getId());
            Payment paymentRepo = paymentRepository.getPaymentByOrderId(orderDTO.getId());
            if (paymentRepo == null) {
                payment.setTotalMoney(orderDTO.getTotalPrice());
                payment.setAccumulatedMoney(payment.getMoneyPaid());
                payment.setMoneyUnpaid(orderDTO.getTotalPrice() - payment.getMoneyPaid());
                payment.setStoreUser(storeDTO.getUserName());
                if (payment.getMoneyUnpaid() == 0) {
                    payment.setIsComplete("Completed");
                } else {
                    payment.setIsComplete("Incomplete");
                }
                payment.setPaymentDate(LocalDateTime.now());
                return paymentRepository.save(payment);
            }
            if (paymentRepo.getMoneyUnpaid() > 0) {
                Payment payment1 = new Payment();
                payment1.setTotalMoney(paymentRepo.getTotalMoney());
                payment1.setAccumulatedMoney(paymentRepo.getAccumulatedMoney() + payment.getMoneyPaid());
                payment1.setMoneyPaid(payment.getMoneyPaid());
                payment1.setMoneyUnpaid(paymentRepo.getMoneyUnpaid() - payment.getMoneyPaid());
                if (payment1.getMoneyUnpaid() == 0) {
                    payment1.setIsComplete("Completed");
                } else {
                    payment1.setIsComplete("Incomplete");
                }
                payment1.setStoreUser(storeDTO.getUserName());
                payment1.setOrderId(orderDTO.getId());
                payment1.setPaymentDate(LocalDateTime.now());
                return paymentRepository.save(payment1);
            } else {
                return null;
            }
        }
    }

    @Scheduled(cron = "*/20 * * * * *")
    public void getPaymentToReport(){
        LocalDateTime localDateTime = LocalDateTime.parse("2022-07-29T18:49:30");
        LocalDateTime localDateTime1 =LocalDateTime.parse("2022-07-29T20:00:00");
        List<Payment> payments = paymentRepository.getAllPaymentByDateTime(localDateTime, localDateTime1);
        System.out.println(payments.get(0).getId());
    }


}
