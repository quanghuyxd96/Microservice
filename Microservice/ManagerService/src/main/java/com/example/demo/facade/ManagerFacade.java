package com.example.demo.facade;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.client.OrderFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.SupplierDTO;
import com.example.demo.entity.Manager;
import com.example.demo.entity.Payment;
import com.example.demo.response.ResponseObjectEntity;
import com.example.demo.service.ManagerService;
import com.example.demo.service.PaymentService;
import io.tej.SwaggerCodgen.model.Item;
import io.tej.SwaggerCodgen.model.Order;
import io.tej.SwaggerCodgen.model.Supplier;
import lombok.Getter;
import net.bytebuddy.description.type.TypeList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ManagerFacade {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private ItemFeignClient itemFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    public JavaMailSender emailSender;


    //Manager
    public String forgotPassword(String email, String username) {
        return managerService.forgotPassword(email, username);
    }

    public String resetPassword(String token, String password, String confirmPassword) {
        return managerService.resetPassword(token, password, confirmPassword);
    }

    public ResponseEntity<?> createAuthenticationToken(String userName, String password)
            throws Exception {
        if (!managerService.authenticate(userName, password)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    public ResponseEntity<Manager> updateManager(Manager manager) {
        Manager manager1 = managerService.updateManager(manager);
        if (manager1 == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(manager1, HttpStatus.OK);
    }

    //Payment

    public ResponseEntity<Payment> savePayment(Payment payment) {
        Payment payment1 = paymentService.saveAndUpdatePaymentOfStore(payment);
        if (payment1 == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(payment1, HttpStatus.OK);
    }

    public ResponseEntity<List<Payment>> getAllPayment() {
        List<Payment> payments = paymentService.getAllPayment();
        if (payments == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    public ResponseEntity<Payment> getPaymentById(long id) {
        Payment payment = paymentService.getPaymentById(id);
        if (payment == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    public ResponseEntity<List<Payment>> getAllPaymentByOrderId(long orderId) {
        List<Payment> payments = paymentService.getAllPaymentByOrderId(orderId);
        if (payments == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    public ResponseEntity<ResponseObjectEntity> deletePaymentById(long id) {
        boolean checkDeletePayment = paymentService.deletePayment(id);
        if (!checkDeletePayment) {
            return new ResponseEntity<>(new ResponseObjectEntity("False", "No payment to delete"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ResponseObjectEntity("OK", "Deleted"), HttpStatus.OK);
    }

    public <T, D> T convertModel(D obj, Class<T> classT) {
        ModelMapper modelMapper = new ModelMapper();
        T obj1 = modelMapper.map(obj, classT);
        return obj1;
    }

    public <T, D> List<T> convertListModel(List<D> objList, Class<T> classT) {
        List<T> objResults = new ArrayList<T>();
        for (D obj : objList) {
            ModelMapper modelMapper = new ModelMapper();
            T objResult = modelMapper.map(obj, classT);
            objResults.add(objResult);
        }
        return objResults;
    }

    public String getUserName(String userName, String password) {
        Manager manager = managerService.checkManager(userName, password);
        if (manager == null) {
            return null;
        }
        return manager.getUserName();
    }


}
