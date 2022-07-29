package com.example.demo.service;

import com.example.demo.client.DeliveryNoteFeignClient;
import com.example.demo.client.ItemFeignClient;
import com.example.demo.client.OrderFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.DeliveryNoteDTO;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailToken;
import com.example.demo.entity.Manager;
import com.example.demo.mq.listen.OrderSource;
import com.example.demo.repository.ManagerRepository;
import com.example.demo.utils.jwt.JwtTokenUtil;
import com.example.demo.utils.report.ExcelGenerator;
import com.example.demo.utils.report.PDFGenerator;
import com.lowagie.text.DocumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
    private ItemFeignClient itemFeignClient;
    @Autowired
    private EmailService emailService;

    @Autowired
    private DeliveryNoteFeignClient deliveryNoteFeignClient;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private static final Logger logger = LogManager.getLogger(ManagerService.class);

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
        manager.setPassword(endCodePassword(manager.getPassword()));
        return managerRepository.save(manager);
    }

    public Manager updateManager(Manager manager) {
        Manager managerRepo = managerRepository.findByUserName(manager.getUserName());
        if (managerRepo == null) {
            return null;
        }
        managerRepo.setPassword(manager.getPassword());
        managerRepo.setEmail(manager.getEmail());
        return managerRepository.save(managerRepo);
    }

    private String endCodePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode(password);
        return password;
    }

    public Manager getManagerById(Long id) {
        Optional<Manager> manager = managerRepository.findById(id);
        if (manager.isPresent()) {
            return manager.get();
        }
        return null;
    }

    public Manager getManagerByUserName(String userName) {
        Manager manager = managerRepository.findByUserName(userName);
        if (manager == null) {
            return null;
        }
        return manager;
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
        String token = generateToken();
        List<OrderDTO> ordersByOrderDate = orderFeignClient.getOrdersByOrderDate(LocalDate.now().plusDays(-7), token);
        return deliveryNoteFeignClient.saveDeliveryNote(ordersByOrderDate, token);
    }

    public Manager checkManager(Manager manager) {
        List<Manager> managers = managerRepository.findAll();
        for (Manager manager1 : managers) {
            if (manager.getUserName().equalsIgnoreCase(manager1.getUserName()) && manager
                    .getPassword().equalsIgnoreCase(manager1.getPassword())) {
                return manager1;
            }
        }
        return null;
    }

    public Manager checkManager(String userName, String password) {
        Manager manager = managerRepository.findByUserName(userName);
        System.out.println(manager.getPassword());
        System.out.println(endCodePassword(password));
        if (manager == null) {
            return null;
        }
        if (manager.getPassword().equals(endCodePassword(password))) {
            return manager;
        }
        return null;
    }

    public boolean authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            e.printStackTrace();
            return false;
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
        }
        return true;
    }

    public String forgotPassword(String email, String username) {
        Manager manager = managerRepository.findByEmailAndUserName(email, username);
        if (manager == null) {
            return "Invalid email and username!!!";
        }
        String token = jwtTokenUtil.generateToken(username);
        String linkReset = "http://localhost:8080/manager/reset-password?token=" + token;
        emailService.sendEmailToResetAdminPassword(linkReset, email);
        return "We sent email to you!!!";
    }

    public String resetPassword(String token, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            logger.info("Password and confirm password are not equal.");
            return "Password and confirm password are not equal.";
        }
        if (!jwtTokenUtil.validateToken(token)) {
            logger.info("Invalid token");
            return "Invalid token";
        }
        Manager manager = getManagerByToken(token, true);
        if (manager == null) {
            logger.info("No username to reset password!!!");
            return "No username to reset password!!!";
        }
        manager.setPassword(endCodePassword(password));
        managerRepository.save(manager);
        return "Your password successfully updated.";
    }

    public String generateToken() {
        String token = "Bearer " + jwtTokenUtil.generateToken("admin");
        System.out.println(token);
        return token;
    }

    public Manager getManagerByToken(String token, boolean check) {
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        Manager manager = managerRepository.findByUserName(userName);
        if (manager == null) {
            return null;
        }
        return manager;
    }

    public void exportIntoPdf(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        List<ItemDTO> items = itemFeignClient.getAllItems();
        PDFGenerator generator = new PDFGenerator();
        generator.setItems(items);
        generator.generate(response);
    }

    public void exportIntoExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Item_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<ItemDTO> items = itemFeignClient.getAllItems();
        ExcelGenerator generator = new ExcelGenerator(items);
        generator.generate(response);
    }

    @StreamListener(target = OrderSource.ORDER_CHANEL)
    public void processSendEmailToNotifyOrdered(List<OrderDetailToken> orderDetails) {
        emailService.sendEmailToNotifyOrdered(orderDetails);
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

//    @RabbitListener(queues = "item.queue")
//    public void receivedMessageItem(ItemDTO item) {
//        System.out.println("Item: " + item.getName());
////        return item;
//    }

//    @RabbitListener(queues = "order.queue")
//    public void receivedMessageOrder(List<OrderDetailDTO> orderDetails) {
//        for (OrderDetailDTO orderDetailDTO : orderDetails) {
//            System.out.println(orderDetails.toString());
//        }
//        emailService.sendEmailToNotifyOrdered(orderDetails);
//    }


}
