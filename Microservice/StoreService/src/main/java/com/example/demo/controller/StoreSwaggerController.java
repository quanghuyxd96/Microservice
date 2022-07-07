package com.example.demo.controller;

import com.example.demo.client.OrderFeignClient;
import com.example.demo.dto.Order;
import com.example.demo.entity.Store;
import com.example.demo.facade.StoreFacade;
import com.example.demo.facade.StoreSwaggerFacade;
import com.example.demo.respond.ResponseObjectEntity;
import io.tej.SwaggerCodgen.api.StoreApi;
import io.tej.SwaggerCodgen.model.ResponseObject;
import io.tej.SwaggerCodgen.model.StoreSwagger;
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
public class StoreSwaggerController implements StoreApi {
    @Autowired
    private StoreSwaggerFacade storeSwaggerFacade;

    @Autowired
    private StoreFacade storeFacade;

    @Autowired
    private OrderFeignClient orderFeignClient;


    @Override
    public ResponseEntity<List<StoreSwagger>> storeAllStoreGet() {
        return new ResponseEntity<>(storeSwaggerFacade.getAllStoreSwagger(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StoreSwagger> storeGetStoreByIdGet(Long id) {
        StoreSwagger storeSwaggerById = storeSwaggerFacade.getStoreSwaggerById(id);
        if (storeSwaggerById == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(storeSwaggerById, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StoreSwagger> storeGetStoreByUserNameGet(String password, String userName) {
        long valid = storeSwaggerFacade.isValid(userName, password);
        if (valid != -1) {
            return null;
        }
        return storeGetStoreByIdGet(valid);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Override
    public ResponseEntity<ResponseObject> storeSavePost(StoreSwagger storeSwagger) {
        Store storeCheck = storeSwaggerFacade.saveStore(storeSwagger);
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
    public ResponseEntity<ResponseObject> storeUpdatePut(StoreSwagger storeSwagger) {
        Store storeCheck = storeSwaggerFacade.updateStore(storeSwagger);
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
        boolean check = storeSwaggerFacade.deleteStore(id);
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
            List<Order> allOrders = orderFeignClient.getAllOrders();
            List<Order> orderList = storeFacade.ordersByStoreId(check, allOrders);
            if (orderList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObjectEntity("True", "Orders: none."));
            }
            return new ResponseEntity<>(storeFacade.ordersByStoreId(check, allOrders), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObjectEntity("False", "Wrong user or password"));
    }

}
