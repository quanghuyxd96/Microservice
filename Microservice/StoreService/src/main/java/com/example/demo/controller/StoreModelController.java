package com.example.demo.controller;

import com.example.demo.client.OrderFeignClient;
import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.Store;
import com.example.demo.facade.StoreFacade;
import com.example.demo.facade.StoreModelFacade;
import com.example.demo.respond.ResponseObjectEntity;
import io.tej.SwaggerCodgen.api.StoreApi;
import io.tej.SwaggerCodgen.model.Order;
import io.tej.SwaggerCodgen.model.OrderDetail;
import io.tej.SwaggerCodgen.model.ResponseObject;
import io.tej.SwaggerCodgen.model.StoreModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StoreModelController implements StoreApi {
    @Autowired
    private StoreModelFacade storeModelFacade;

    @Autowired
    private StoreFacade storeFacade;

    @Autowired
    private OrderFeignClient orderFeignClient;


    @Override
    public ResponseEntity<List<StoreModel>> storeAllStoreGet() {
        return new ResponseEntity<>(storeModelFacade.getAllStoreModel(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StoreModel> storeGetStoreByIdGet(Long id) {
        StoreModel StoreModelById = storeModelFacade.getStoreModelById(id);
        if (StoreModelById == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(StoreModelById, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StoreModel> storeGetStoreByUserNameGet(String password, String userName) {
        long valid = storeModelFacade.isValid(userName, password);
        if (valid != -1) {
            return null;
        }
        return storeGetStoreByIdGet(valid);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Override
    public ResponseEntity<ResponseObject> storeSavePost(StoreModel StoreModel) {
        Store storeCheck = storeModelFacade.saveStore(StoreModel);
        ResponseObject responseObject = new ResponseObject();
        if (storeCheck == null) {
            responseObject.setStatus("False");
            responseObject.setMessage("Username available");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObject);
        }
        responseObject.setStatus("OK");
        responseObject.setMessage("Created");
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject> storeUpdatePut(StoreModel StoreModel) {
        Store storeCheck = storeModelFacade.updateStore(StoreModel);
        ResponseObject responseObject = new ResponseObject();
        if (storeCheck == null) {
            responseObject.setStatus("False");
            responseObject.setMessage("Password and Confirm Password are not equal!!!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObject);
        }
        responseObject.setStatus("OK");
        responseObject.setMessage("Updated");
        return new ResponseEntity<>(responseObject, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<ResponseObject> storeDeleteDelete(Long id) {
        boolean check = storeModelFacade.deleteStore(id);
        ResponseObject responseObject = new ResponseObject();
        if (!check) {
            responseObject.setStatus("Faild");
            responseObject.setMessage("No store to delete!!!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);
        }
        responseObject.setStatus("OK");
        responseObject.setMessage("Delete Success");
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    @Override
    public ResponseEntity<Order> storeOrderSavePost(Long id, List<OrderDetail> orderDetail)  {
        Order order = storeModelFacade.saveOrder(id, orderDetail);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }


    //Dung de demo

    //@PostMapping(value = "/save") //"application/x-www-form-urlencoded",consumes = "application/json")
//    public ResponseEntity<Object> saveStore(@RequestBody Store store) {
//        System.out.println(store.toString());
//        Store storeCheck = storeFacade.saveStore(store);
//        if (storeCheck == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("False", "Username available"));
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Created"));
//    }
//    @DeleteMapping("/delete")
//    public ResponseEntity<ResponseObjectEntity> deleteStore(@RequestParam("id") int id) {
//        boolean check = storeService.deleteStoreById(id);
//        if (!check) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObjectEntity("Faild", "No store to delete."));
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObjectEntity("OK", "Delete Success"));
//    }

    //    @PutMapping("/update")
//    public Store updateStoreById(@RequestBody Store store) {
//        return storeService.updateStoreById(store);
//    }


    @PostMapping("/all-order")       //2nd way
    public ResponseEntity<Object> orders(@RequestParam("userName") String user, @RequestParam("password") String password) {
        long check = storeFacade.isValid(user, password);
        if (check != -1) {
            List<OrderDTO> allOrders = orderFeignClient.getAllOrders();
            List<OrderDTO> orderList = storeFacade.ordersByStoreId(check, allOrders);
            if (orderList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObjectEntity("True", "Orders: none."));
            }
            return new ResponseEntity<>(storeFacade.ordersByStoreId(check, allOrders), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObjectEntity("False", "Wrong user or password"));
    }





}
