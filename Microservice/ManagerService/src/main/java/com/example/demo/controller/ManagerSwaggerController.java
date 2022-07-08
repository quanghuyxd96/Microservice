package com.example.demo.controller;

import com.example.demo.dto.EmailContent;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.SupplierDTO;
import com.example.demo.facade.ManagerFacade;
import com.example.demo.response.ResponseObjectEntity;
import io.tej.SwaggerCodgen.api.ManagerApi;
import io.tej.SwaggerCodgen.model.Item;
import io.tej.SwaggerCodgen.model.Order;
import io.tej.SwaggerCodgen.model.ResponseObject;
import io.tej.SwaggerCodgen.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")
public class ManagerSwaggerController implements ManagerApi {
    @Autowired
    private ManagerFacade managerFacade;

    @Override
    public ResponseEntity<List<Item>> managerManageItemItemsGet() {
        List<ItemDTO> itemDTOList = managerFacade.getItemFeignClient().getAllItems();
        if(itemDTOList==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Item> items = managerFacade.convertListModel(itemDTOList,Item.class);
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
        if(orderDTOList==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Order> orders = managerFacade.convertListModel(orderDTOList,Order.class);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Order> managerManageOrderGetGet(Long id) {
        ResponseEntity<OrderDTO> orderDTO = managerFacade.getOrderFeignClient().getOrderById(id);
        if(orderDTO.getBody()==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Order order = managerFacade.convertModel(orderDTO.getBody(),Order.class);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<List<Supplier>> managerManageSupplierSuppliersGet() {
        List<SupplierDTO> supplierDtos = managerFacade.getItemFeignClient().getAllSupplier();
        if(supplierDtos==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Supplier> suppliers = managerFacade.convertListModel(supplierDtos,Supplier.class);
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Supplier> managerManageSupplierSavePost(Supplier supplier) {
        SupplierDTO supplierDTO = managerFacade.getItemFeignClient().saveSupplier(managerFacade.convertModel(supplier,SupplierDTO.class));
        if (supplierDTO != null) {
            Supplier supplier1 = managerFacade.convertModel(supplierDTO,Supplier.class);
            return new ResponseEntity<>(supplier1, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<ResponseObject> managerManageSupplierDeleteDelete(Long id) {
        ResponseEntity<ResponseObjectEntity> responseObjectEntity = managerFacade.getItemFeignClient().deleteSupplierById(id);
        ResponseObject responseObject = managerFacade.convertModel(responseObjectEntity.getBody(),ResponseObject.class);
        if (responseObjectEntity.getBody().getStatus().equalsIgnoreCase("OK")) {
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/manager/send-mail")
    public String sendEmailToStore(@RequestBody EmailContent emailContent){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailContent.getReceiver());
        message.setSubject(emailContent.getSubject());
        message.setText(emailContent.getTextContent());
        managerFacade.getEmailSender().send(message);
        return "Email Sent!";
    }

//    @GetMapping("/manager/manage-order/get/date")
//    public List<OrderDTO> getOrdersByOrderDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate){
//        return null;
//    }
}
