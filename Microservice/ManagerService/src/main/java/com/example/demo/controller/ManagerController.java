package com.example.demo.controller;

import com.example.demo.entity.Manager;
import com.example.demo.entity.Payment;
import com.example.demo.facade.ManagerFacade;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.ManagerService;
import com.lowagie.text.DocumentException;
import io.tej.SwaggerCodgen.api.ManagerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    private HttpServletRequest request;


    @PostMapping("/manager/payment")
    public Payment savePaymentDemo(@RequestBody Payment payment) {
        return managerFacade.getPaymentService().saveAndUpdatePaymentOfStore(payment);
    }

    @GetMapping("/manager/payment/get")
    public ResponseEntity<Payment> getPaymentDemo(@RequestParam("storeUser") String storeUser, @RequestParam("orderId") Long orderId) {
        return new ResponseEntity<>(paymentRepository.findByStoreUserAndOrderId(storeUser, orderId), HttpStatus.OK);
    }


    @GetMapping("/pdf/items")
    public void generatePdf(HttpServletResponse response) throws DocumentException, IOException {
        managerFacade.getManagerService().exportIntoPdf(response);
    }

    @GetMapping("/excel/items")
    public void exportIntoExcel(HttpServletResponse response) throws IOException {
        managerFacade.getManagerService().exportIntoExcel(response);
    }

    @RequestMapping(value = "/manager/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestParam("userName") String userName,
                                                       @RequestParam("password") String password)
            throws Exception {
        managerFacade.getManagerService().authenticate(userName, password);
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> createManager(@RequestBody Manager manager)
            throws Exception {
        return new ResponseEntity<>(managerService.saveManager(manager), HttpStatus.OK);
    }

    //cái thử demo gửi mail, chưa xài tới
    //    @PostMapping("/manager/send-mail")
//    public String sendEmailToStore(@RequestBody EmailContent emailContent) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(emailContent.getReceiver());
//        message.setSubject(emailContent.getSubject());
//        message.setText(emailContent.getTextContent());
//        managerFacade.getEmailSender().send(message);
//        return "Email Sent!";
//    }


//    @Autowired
//    private JwtUserDetailsService getJwtInMemoryUserDetailsService;
//    @GetMapping("/manager/security")
//    UserDetails get(@RequestParam("userName") String userName){
//        return getJwtInMemoryUserDetailsService.loadUserByUsername(userName);
//    }
//    @PreAuthorize("hasRole('ABC')")
//    @RequestMapping("/manager/hello")
//    public String hello() {
//        return "Làm được rồi đó";
//    }

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

    //Không dùng đến

//    @PostMapping("/manager/get")
//    public String getManagerUserName(@RequestParam("userName") String userName, @RequestParam("password") String password) {
//        System.out.println(userName);
//        String userNameCheck = managerFacade.getUserName(userName, password);
//        System.out.println(userNameCheck);
//        if (userNameCheck == null) {
//            return null;
//        }
//        return userNameCheck;
//    }
//
//    @Override
//    public ResponseEntity<List<Item>> managerManageItemItemsGet() {
//        List<ItemDTO> itemDTOList = managerFacade.getItemFeignClient().getAllItems();
//        if (itemDTOList == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        List<Item> items = managerFacade.convertListModel(itemDTOList, Item.class);
//        return new ResponseEntity<>(items, HttpStatus.OK);
//    }
//
//    @Override
//    public ResponseEntity<Item> managerManageItemSavePost(Item item) {
//        ItemDTO itemDTO = managerFacade.getItemFeignClient().saveItem(managerFacade.convertModel(item, ItemDTO.class));
//        if (itemDTO != null) {
//            Item item1 = managerFacade.convertModel(itemDTO, Item.class);
//            return new ResponseEntity<>(item1, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
//
//    @Override
//    public ResponseEntity<Item> managerManageItemUpdatePut(Long id, Item item) {
//        ItemDTO itemDTO = managerFacade.getItemFeignClient().updateItemById(id, managerFacade.convertModel(item, ItemDTO.class));
//        if (itemDTO != null) {
//            Item itemModel = managerFacade.convertModel(itemDTO, Item.class);
//            return new ResponseEntity<>(itemModel, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
//
//    @Override
//    public ResponseEntity<ResponseObject> managerManageItemDeleteDelete(Long id) {
//        ResponseEntity<ResponseObjectEntity> responseObjectEntity = managerFacade.getItemFeignClient().deleteItemById(id);
//        ResponseObject responseObject = managerFacade.convertModel(responseObjectEntity.getBody(), ResponseObject.class);
//        if (responseObjectEntity.getBody().getStatus().equalsIgnoreCase("OK")) {
//            return new ResponseEntity<>(responseObject, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
//    }
//
//
//    @Override
//    public ResponseEntity<List<Order>> managerManageOrderOrdersGet() {
//        System.out.println(request.isUserInRole("ADMIN"));
//        System.out.println(request.getUserPrincipal());
//        List<OrderDTO> orderDTOList = managerFacade.getOrderFeignClient().getAllOrders();
//        if (orderDTOList == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        List<Order> orders = managerFacade.convertListModel(orderDTOList, Order.class);
//        return new ResponseEntity<>(orders, HttpStatus.OK);
//    }
//
//    @Override
//    public ResponseEntity<Order> managerManageOrderGetGet(Long id) {
//        ResponseEntity<OrderDTO> orderDTO = managerFacade.getOrderFeignClient().getOrderById(id);
//        if (orderDTO.getBody() == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        Order order = managerFacade.convertModel(orderDTO.getBody(), Order.class);
//        return new ResponseEntity<>(order, HttpStatus.OK);
//    }
//
//
//    @Override
//    public ResponseEntity<List<Supplier>> managerManageSupplierSuppliersGet() {
//        List<SupplierDTO> supplierDtos = managerFacade.getItemFeignClient().getAllSupplier();
//        if (supplierDtos == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        List<Supplier> suppliers = managerFacade.convertListModel(supplierDtos, Supplier.class);
//        return new ResponseEntity<>(suppliers, HttpStatus.OK);
//    }
//
//
//    @Override
//    public ResponseEntity<Supplier> managerManageSupplierSavePost(Supplier supplier) {
//        SupplierDTO supplierDTO = managerFacade.getItemFeignClient().saveSupplier(managerFacade.convertModel(supplier, SupplierDTO.class));
//        if (supplierDTO != null) {
//            Supplier supplier1 = managerFacade.convertModel(supplierDTO, Supplier.class);
//            return new ResponseEntity<>(supplier1, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @Override
//    public ResponseEntity<ResponseObject> managerManageSupplierDeleteDelete(Long id) {
//        ResponseEntity<ResponseObjectEntity> responseObjectEntity = managerFacade.getItemFeignClient().deleteSupplierById(id);
//        ResponseObject responseObject = managerFacade.convertModel(responseObjectEntity.getBody(), ResponseObject.class);
//        if (responseObjectEntity.getBody().getStatus().equalsIgnoreCase("OK")) {
//            return new ResponseEntity<>(responseObject, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
//    }
}
