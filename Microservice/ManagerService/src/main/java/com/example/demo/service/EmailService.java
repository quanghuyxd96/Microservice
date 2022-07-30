package com.example.demo.service;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.client.OrderFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.*;
import com.example.demo.entity.Email;
import com.example.demo.entity.Manager;
import com.example.demo.mq.listen.StoreSource;
import com.example.demo.repository.EmailRepository;
import com.example.demo.utils.SendEmail;
import com.example.demo.utils.jwt.JwtTokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private ItemFeignClient itemFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SendEmail sendEmail;

    private static final Logger logger = LogManager.getLogger(EmailService.class);

    private static Manager manager = new Manager();

    @Scheduled(cron = "0 0 7 * * *")  //check và kiểm tra vào lúc 7h sáng hằng ngày
    public void sendEmailToNotifyDelivery() {
        SimpleMailMessage message = new SimpleMailMessage();
        String token = managerService.generateToken();
        List<OrderDTO> ordersByOrderDate = orderFeignClient.getOrdersByOrderDate(LocalDate.now().plusDays(-6),
                token);
        for (OrderDTO orderDTO : ordersByOrderDate) {
            ResponseEntity<StoreDTO> store = storeFeignClient.getStoreById(orderDTO.getStoreId(), token);
            message.setTo(store.getBody().getEmail());
            message.setText("Your order with code " + orderDTO.getId() + " will be shipped on tomorrow");
            message.setSubject("ORDER " + orderDTO.getId() + " STATUS");
            javaMailSender.send(message);
            Email email = new Email();
            email = convertToEmail(message, orderDTO);
            email.setSentTo(store.getBody().getEmail());
            email.setManager(manager);
            emailRepository.save(email);
        }
    }


    public void sendEmailToNotifyOrdered(List<OrderDetailToken> orderDetailDTOS) {
        Email email = new Email();
        OrderDTO orderDTO = orderFeignClient.getOrderById(orderDetailDTOS.get(0).getOrderId(), orderDetailDTOS.get(0).getToken()).getBody();
        ResponseEntity<StoreDTO> store = storeFeignClient.getStoreById(orderDTO.getStoreId(), orderDetailDTOS.get(0).getToken());
        email.setSentTo(store.getBody().getEmail());
        email.setSubject("Order " + orderDTO.getId() + " status");
        email.setTextContent("The order has been placed with detail: ");
        String textContent = "";
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOS) {
            ItemDTO item = itemFeignClient.getItemById(orderDetailDTO.getItemId()).getBody();
            textContent += item.getName() + "\t" + orderDetailDTO.getItemQuantity() + "\n";
        }
        textContent +="\nOrder price: " +orderDTO.getTotalPrice();
        email.setTextContent(email.getTextContent() + "\n" + textContent);
        try {
            boolean check = sendEmail.doSendEmail(email);
            if (check) {
                email.setManager(manager);
                emailRepository.save(email);
            }
        } catch (Exception e) {
            logger.throwing(e);
        }
    }

    private Email convertToEmail(SimpleMailMessage simpleMailMessage, OrderDTO orderDTO) {
        Email email = new Email();
        email.setSubject(simpleMailMessage.getSubject());
        email.setTextContent(simpleMailMessage.getText());
        email.setStoreId(orderDTO.getStoreId());
        email.setSentDateTime(LocalTime.now());
        email.setOrderId(orderDTO.getId());
        return email;
    }

    private Email convertOrderDetailDtoToEmail(SimpleMailMessage simpleMailMessage, OrderDTO orderDTO) {
        Email email = new Email();
        email.setSubject(simpleMailMessage.getSubject());
        email.setTextContent(simpleMailMessage.getText());
        email.setStoreId(orderDTO.getStoreId());
        email.setSentDateTime(LocalTime.now());
        email.setOrderId(orderDTO.getId());
        return email;
    }

    @StreamListener(target = StoreSource.STORE_FORGOT_PASSWORD)
    public void processSendEmailToResetPassword(HashMap<String, String> hashMap) {
        sendEmailToResetStorePassword(hashMap);
    }

    public void sendEmailToResetStorePassword(HashMap<String, String> hashMap) {
        Email email = new Email();
        String username = jwtTokenUtil.getUsernameFromToken(hashMap.get("token"));
        email.setSentTo(hashMap.get("email"));
        email.setSubject("Link Reset Store Password");
        String linkReset = "http://localhost:8080/store/reset-password?token=" + hashMap.get("token");
        email.setTextContent("This is link reset password: \n" + linkReset);
        try {
            boolean check = sendEmail.doSendEmail(email);
            if (check) {
                email.setManager(manager);
                emailRepository.save(email);
            }
        } catch (Exception e) {
            logger.throwing(e);
        }
    }

    public void sendEmailToResetAdminPassword(String linkReset, String emailToSend) {
        Email email = new Email();
        email.setSentTo(emailToSend);
        email.setSubject("Link Reset Manager Password");
        email.setTextContent("This is link reset password: \n" + linkReset);
        try {
            boolean check = sendEmail.doSendEmail(email);
            if (check) {
                email.setManager(manager);
                emailRepository.save(email);
            }
        } catch (Exception e) {
            logger.throwing(e);
        }
    }
}
