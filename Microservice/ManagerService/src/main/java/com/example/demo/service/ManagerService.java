package com.example.demo.service;

import com.example.demo.client.DeliveryNoteFeignClient;
import com.example.demo.client.OrderFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.DeliveryNoteDTO;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.Manager;
import com.example.demo.repository.ManagerRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private EmailService emailService;

    @Autowired
    private DeliveryNoteFeignClient deliveryNoteFeignClient;


    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Manager saveManager(Manager manager) {
        List<Manager> managers = getAllManagers();
        for (Manager managerCheck : managers) {
            if (managerCheck.getUserName().equalsIgnoreCase(manager.getUserName())) {
                System.out.println("Existed user");
                return null;
            }
        }
        return managerRepository.save(manager);
    }


    public Manager getManagerById(Long id) {
        Optional<Manager> manager = managerRepository.findById(id);
        if (manager.isPresent()) {
            return manager.get();
        }
        return null;
    }


    public void deleteManagerById(long id) {
        managerRepository.deleteById(id);
    }


    public Manager updateManagerPasswordById(Manager manager, long id) {
        Optional<Manager> managerRepositoryById = managerRepository.findById(id);
        if (managerRepositoryById.isPresent()) {
            managerRepositoryById.get().setPassword(manager.getPassword());
            return managerRepository.save(managerRepositoryById.get());
        }
        return null;
    }

    @Scheduled(cron = "0 0 7 * * *")
    public List<DeliveryNoteDTO> saveDeliveryNote() {
        List<OrderDTO> ordersByOrderDate = orderFeignClient.getOrdersByOrderDate(LocalDate.now().plusDays(-7));
        return deliveryNoteFeignClient.saveDeliveryNote(ordersByOrderDate);
    }

//    public ItemSwagger convertAllItemToAllItemSwagger(ItemDTO itemDTO) {
//        ItemSwagger itemSwagger = new ItemSwagger();
//        itemSwagger.setId(itemDTO.getId());
//        itemSwagger.setName(itemDTO.getName());
//        BigDecimal bigDecimal = new BigDecimal(itemDTO.getPrice());
//        itemSwagger.setPrice(bigDecimal.setScale(3, RoundingMode.FLOOR));
//        itemSwagger.setQuantity(itemDTO.getQuantity());
//        System.out.println(itemDTO.getSupplierId());
//        itemSwagger.setSupplierId(itemDTO.getSupplierId());
//        return itemSwagger;
//    }
//@RabbitListener(queues = "${spring.rabbitmq.queue}")
//public void receivedMessage(List<ItemDTO> itemDTOList) {
//    System.out.println(itemDTOList);
//}

    @RabbitListener(queues = "item.queue")
    public void receivedMessageItem(ItemDTO item) {
        System.out.println("Item: " + item.getName());
//        return item;
    }

    @RabbitListener(queues = "order.queue")
    public void receivedMessageOrder(List<OrderDetailDTO> orderDetails) {
        emailService.sendEmailToNotifyOrdered(orderDetails);
    }


}
