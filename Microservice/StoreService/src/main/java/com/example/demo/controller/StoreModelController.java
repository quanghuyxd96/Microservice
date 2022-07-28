package com.example.demo.controller;

import com.example.demo.client.OrderFeignClient;
import com.example.demo.entity.Store;
import com.example.demo.facade.StoreModelFacade;
import com.example.demo.repository.StoreRepository;
import io.tej.SwaggerCodgen.api.StoreApi;
import io.tej.SwaggerCodgen.model.ResponseObject;
import io.tej.SwaggerCodgen.model.StoreModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.demo.utils.Constants.AUTHOR;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StoreModelController implements StoreApi {
    @Autowired
    private StoreModelFacade storeModelFacade;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private StoreRepository storeRepository;


    @Autowired
    private HttpServletRequest request;


    @Override
    public ResponseEntity<List<StoreModel>> storeAllStoreGet() {
        List<StoreModel> stores = storeModelFacade.getAllStoreModel();
        if(stores==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(stores, HttpStatus.OK);
    }

//    @Override
//    public ResponseEntity<StoreModel> storeGetStoreByIdGet(Long id) {
//        StoreModel StoreModelById = storeModelFacade.getStoreModelById(id);
//        if (StoreModelById == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(StoreModelById, HttpStatus.OK);
//    }

    @GetMapping("/store/get-store-by-id")
    public ResponseEntity<StoreModel> getStoreById(@RequestParam("id") Long id, @RequestHeader(AUTHOR) String token) {
        System.out.println(request.getUserPrincipal());
        StoreModel StoreModelById = storeModelFacade.getStoreModelById(id);
        if (StoreModelById == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(StoreModelById, HttpStatus.OK);
    }

    @GetMapping("/store/get")
    public StoreModel getStoreByToken(@RequestHeader("Authorization") String token) {
        StoreModel store = storeModelFacade.getStoreByToken(token);
        if (store == null) {
            return null;
        }
        return store;
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
    public ResponseEntity<ResponseObject> storeSavePost(StoreModel storeModel) {
        System.out.println(storeModel.getConfirmPassword());
        Store storeCheck = storeModelFacade.saveStore(storeModel);
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

    @RequestMapping(value = "/store/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestParam("userName") String userName,
                                                       @RequestParam("password") String password)
            throws Exception {
        storeModelFacade.getStoreService().authenticate(userName, password);
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @PostMapping("/store/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, @RequestParam("username") String username){
        return storeModelFacade.forgotPassword(email,username);
    }


    //reset chưa được
    @PutMapping("/store/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword){
        return storeModelFacade.resetPassword(token,password,confirmPassword);
    }

    //Dung de demo

//    @Override
//    public ResponseEntity<Order> storeOrderSavePost(Long id, List<OrderDetail> orderDetail) {
//        Order order = storeModelFacade.saveOrder(id, orderDetail);
//        if (order == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(order, HttpStatus.OK);
//    }
//
//    @GetMapping("/store/get/{userName}")
//    public ResponseEntity<Store> getStoreByUserName(@PathVariable("userName") String userName) {
//        return new ResponseEntity<>(storeRepository.findByUserName(userName), HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/save") //"application/x-www-form-urlencoded",consumes = "application/json")
//    public ResponseEntity<Object> saveStore(@RequestBody Store store) {
//        System.out.println(store.toString());
//        Store storeCheck = storeFacade.saveStore(store);
//        if (storeCheck == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("False", "Username available"));
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Created"));
//    }
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<ResponseObjectEntity> deleteStore(@RequestParam("id") int id) {
//        boolean check = storeService.deleteStoreById(id);
//        if (!check) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObjectEntity("Faild", "No store to delete."));
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObjectEntity("OK", "Delete Success"));
//    }
//
//    @PutMapping("/update")
//    public Store updateStoreById(@RequestBody Store store) {
//        return storeService.updateStoreById(store);
//    }
//
//
//    @PostMapping("/all-order")       //2nd way
//    public ResponseEntity<Object> orders(@RequestParam("userName") String user, @RequestParam("password") String password) {
//        long check = storeFacade.isValid(user, password);
//        if (check != -1) {
//            List<OrderDTO> allOrders = orderFeignClient.getAllOrders();
//            List<OrderDTO> orderList = storeFacade.ordersByStoreId(check, allOrders);
//            if (orderList == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObjectEntity("True", "Orders: none."));
//            }
//            return new ResponseEntity<>(storeFacade.ordersByStoreId(check, allOrders), HttpStatus.OK);
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObjectEntity("False", "Wrong user or password"));
//    }


    //demo truyền header
//    @GetMapping("/store/demo-trial")
//    public int demo() {
//        System.out.println(request.getHeader("Authorization"));
//        return orderFeignClient.demo(request.getHeader("Authorization"));
//    }
}
