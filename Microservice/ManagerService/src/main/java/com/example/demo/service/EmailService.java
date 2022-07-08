package com.example.demo.service;

import com.example.demo.client.OrderFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.StoreDTO;
import com.example.demo.entity.Email;
import com.example.demo.entity.Manager;
import com.example.demo.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private JavaMailSender javaMailSender;

    private static Manager manager = new Manager();

    @Scheduled(cron = "0 0 7 * * *")  //check và kiểm tra vào lúc 7h sáng hằng ngày
    public void sendEmailToNotifyDelivery() {
        SimpleMailMessage message = new SimpleMailMessage();
        List<OrderDTO> ordersByOrderDate = orderFeignClient.getOrdersByOrderDate(LocalDate.now().plusDays(-4));
        for(OrderDTO orderDTO : ordersByOrderDate){
            ResponseEntity<StoreDTO> store = storeFeignClient.getStoreById(orderDTO.getStoreId());
            message.setTo(store.getBody().getEmail());
            message.setText("Your order with code "+orderDTO.getId()+" will be shipped on tomorrow");
            message.setSubject("Order "+orderDTO.getId()+" status");
            javaMailSender.send(message);
            Email email = new Email();
            email = convertToEmail(message,orderDTO);
            email.setSentTo(store.getBody().getEmail());
            email.setManager(manager);
            emailRepository.save(email);
        }
    }



    private Email convertToEmail(SimpleMailMessage simpleMailMessage, OrderDTO orderDTO){
        Email email = new Email();
        email.setSubject(simpleMailMessage.getSubject());
        email.setTextContent(simpleMailMessage.getText());
        email.setStoreId(orderDTO.getStoreId());
        email.setSentDateTime(LocalTime.now());
        email.setOrderId(orderDTO.getId());
        return email;
    }
}