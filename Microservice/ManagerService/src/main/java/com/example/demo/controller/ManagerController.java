package com.example.demo.controller;

import com.example.demo.utils.jwt.JwtTokenUtil;
import com.example.demo.dto.EmailContent;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.SupplierDTO;
import com.example.demo.entity.Manager;
import com.example.demo.entity.Payment;
import com.example.demo.facade.ManagerFacade;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.response.ResponseObjectEntity;
import com.example.demo.service.ManagerService;
import com.example.demo.utils.report.ExcelGenerator;
import com.example.demo.utils.report.PDFGenerator;
import com.lowagie.text.DocumentException;
import io.tej.SwaggerCodgen.api.ManagerApi;
import io.tej.SwaggerCodgen.model.Item;
import io.tej.SwaggerCodgen.model.Order;
import io.tej.SwaggerCodgen.model.ResponseObject;
import io.tej.SwaggerCodgen.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin("*")
public class ManagerController implements ManagerApi {
    @Autowired
    private ManagerFacade managerFacade;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;





    @Override
    public ResponseEntity<List<Item>> managerManageItemItemsGet() {
        List<ItemDTO> itemDTOList = managerFacade.getItemFeignClient().getAllItems();
        if (itemDTOList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Item> items = managerFacade.convertListModel(itemDTOList, Item.class);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Item> managerManageItemSavePost(Item item) {
        ItemDTO itemDTO = managerFacade.getItemFeignClient().saveItem(managerFacade.convertModel(item, ItemDTO.class));
        if (itemDTO != null) {
            Item item1 = managerFacade.convertModel(itemDTO, Item.class);
            return new ResponseEntity<>(item1, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Item> managerManageItemUpdatePut(Long id, Item item) {
        ItemDTO itemDTO = managerFacade.getItemFeignClient().updateItemById(id, managerFacade.convertModel(item, ItemDTO.class));
        if (itemDTO != null) {
            Item itemModel = managerFacade.convertModel(itemDTO, Item.class);
            return new ResponseEntity<>(itemModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseObject> managerManageItemDeleteDelete(Long id) {
        ResponseEntity<ResponseObjectEntity> responseObjectEntity = managerFacade.getItemFeignClient().deleteItemById(id);
        ResponseObject responseObject = managerFacade.convertModel(responseObjectEntity.getBody(), ResponseObject.class);
        if (responseObjectEntity.getBody().getStatus().equalsIgnoreCase("OK")) {
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<Order>> managerManageOrderOrdersGet() {
        List<OrderDTO> orderDTOList = managerFacade.getOrderFeignClient().getAllOrders();
        if (orderDTOList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Order> orders = managerFacade.convertListModel(orderDTOList, Order.class);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Order> managerManageOrderGetGet(Long id) {
        ResponseEntity<OrderDTO> orderDTO = managerFacade.getOrderFeignClient().getOrderById(id);
        if (orderDTO.getBody() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Order order = managerFacade.convertModel(orderDTO.getBody(), Order.class);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<List<Supplier>> managerManageSupplierSuppliersGet() {
        List<SupplierDTO> supplierDtos = managerFacade.getItemFeignClient().getAllSupplier();
        if (supplierDtos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Supplier> suppliers = managerFacade.convertListModel(supplierDtos, Supplier.class);
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Supplier> managerManageSupplierSavePost(Supplier supplier) {
        SupplierDTO supplierDTO = managerFacade.getItemFeignClient().saveSupplier(managerFacade.convertModel(supplier, SupplierDTO.class));
        if (supplierDTO != null) {
            Supplier supplier1 = managerFacade.convertModel(supplierDTO, Supplier.class);
            return new ResponseEntity<>(supplier1, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<ResponseObject> managerManageSupplierDeleteDelete(Long id) {
        ResponseEntity<ResponseObjectEntity> responseObjectEntity = managerFacade.getItemFeignClient().deleteSupplierById(id);
        ResponseObject responseObject = managerFacade.convertModel(responseObjectEntity.getBody(), ResponseObject.class);
        if (responseObjectEntity.getBody().getStatus().equalsIgnoreCase("OK")) {
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/manager/payment")
    public Payment savePaymentDemo(@RequestBody Payment payment) {
        return managerFacade.getPaymentService().saveAndUpdatePaymentOfStore(payment);
    }


    @GetMapping("/manager/payment/get")
    public ResponseEntity<Payment> getPaymentDemo(@RequestParam("storeUser") String storeUser, @RequestParam("orderId") Long orderId) {
        return new ResponseEntity<>(paymentRepository.findByStoreUserAndOrderId(storeUser, orderId), HttpStatus.OK);
    }

    @PostMapping("/manager/send-mail")
    public String sendEmailToStore(@RequestBody EmailContent emailContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailContent.getReceiver());
        message.setSubject(emailContent.getSubject());
        message.setText(emailContent.getTextContent());
        managerFacade.getEmailSender().send(message);
        return "Email Sent!";
    }

    @GetMapping("/pdf/items")
    public void generatePdf(HttpServletResponse response) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        List<ItemDTO> items = managerFacade.getItemFeignClient().getAllItems();

        PDFGenerator generator = new PDFGenerator();
        generator.setItems(items);
        generator.generate(response);

    }

    @GetMapping("/excel/items")
    public void exportIntoExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Item_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<ItemDTO> items = managerFacade.getItemFeignClient().getAllItems();

        ExcelGenerator generator = new ExcelGenerator(items);

        generator.generate(response);
    }

    @RequestMapping(value = "/manager/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestParam("userName") String userName,
                                                       @RequestParam("password") String password)
            throws Exception {

        authenticate(userName, password);

        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(userName);

        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> createManager(@RequestBody Manager manager)
            throws Exception {


//        return ResponseEntity.ok(new JwtResponse(token));
        return new ResponseEntity<>(managerService.saveManager(manager),HttpStatus.OK);
    }
    private void authenticate(String username, String password) throws Exception {
        System.out.println(username);
        System.out.println(password);
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @RequestMapping( "/manager/hello" )
    public String hello() {
        return "Làm được rồi đó";
    }

//    @PostMapping("/manager/check")
//    public String getManagerByToken(@RequestHeader("token") String token) {
//        return jwtUtil.getUserNameFromJWT(token);
//    }
//
//    @GetMapping("/manager/get-items")
//    public List<ItemDTO> getItems(@RequestHeader("token") String token) {
//        boolean checkToken = jwtUtil.validateToken(token);
//        if (checkToken) {
//            return managerFacade.getItemFeignClient().getAllItems();
//        }
//        return null;
//    }

    @PostMapping("/manager/get")
    public String getManagerUserName(@RequestParam("userName") String userName, @RequestParam("password") String password){
        System.out.println(userName);
        String userNameCheck = managerFacade.getUserName(userName,password);
        System.out.println(userNameCheck);
        if(userNameCheck == null){
            return null;
        }
        return userNameCheck;
    }
}
