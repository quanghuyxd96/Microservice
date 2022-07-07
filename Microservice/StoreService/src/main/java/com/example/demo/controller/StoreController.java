package com.example.demo.controller;

import com.example.demo.client.OrderFeignClient;
import com.example.demo.dto.Order;
import com.example.demo.entity.Store;
import com.example.demo.facade.StoreFacade;
import com.example.demo.respond.ResponseObjectEntity;
import com.example.demo.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/store")
//@CrossOrigin(origins = "*", allowedHeaders = "*")        //thêm cái này vào ko có lỗi cors
public class StoreController {

    @Autowired
    private StoreFacade storeFacade;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private StoreService storeService;

    @GetMapping("all-store")
    public List<Store> getAllStore() {
        return storeFacade.getAllStore();
    }

    //trial demo login
//    @GetMapping("/users")
//    public String listUsers(Model model) {
//        List<Store> stores = storeService.getAllStore();
//        model.addAttribute("stores", stores);
//
//        return "login";
//    }

    @PostMapping(value = "/save") //"application/x-www-form-urlencoded",consumes = "application/json")
    public ResponseEntity<Object> saveStore(@RequestParam("name") String name,@RequestParam("userName") String userName, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword,
    @RequestParam("address") String address, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("email") String email,@RequestHeader("first-request") String header) {
        System.out.println(header);
        Store store = new Store(name,address,phoneNumber,email,userName,password,confirmPassword);
        System.out.println(store.toString());
        Store storeCheck = storeFacade.saveStore(store);
        if (storeCheck == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObjectEntity("False", "Username available"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObjectEntity("OK", "Created"));
    }

//    @PostMapping(value = "/save") //"application/x-www-form-urlencoded",consumes = "application/json")
//    public ResponseEntity<Object> saveStore(@RequestBody Store store) {
//        System.out.println(store.toString());
//        Store storeCheck = storeFacade.saveStore(store);
//        if (storeCheck == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("False", "Username available"));
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Created"));
//    }

//    @PostMapping(value = "/save") //"application/x-www-form-urlencoded",consumes = "application/json")
//    public ResponseEntity<Object> saveStore(@RequestBody Store store) {
//        System.out.println(store.toString());
//        System.out.println(store.toString());
//        Store storeCheck = storeFacade.saveStore(store);
//        if (storeCheck == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("False", "Username available"));
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Created"));
//    }


//    @GetMapping("/stores")
//    public String allStores(Model model) {
//        List<Store> stores = storeService.getAllStore();
//        model.addAttribute("stores", stores);
//        return "index";
//    }

    //    @GetMapping("/login")
//    public String login(Model model) {
////        System.out.println(user);
//        List<Store> stores = storeService.getAllStore();
//        model.addAttribute("stores", stores);
//        return "index";
//    }
//

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<Store> getStore(@PathVariable("id") Long id) {
        return new ResponseEntity<>(storeService.getStoreById(id), HttpStatus.OK);
    }
//
//
//    @RequestMapping(value = "/get/id", method = RequestMethod.GET)
//    public ResponseEntity<Store> getStoreByParam(@RequestParam("id") Long id) {
//        return new ResponseEntity<>(storeService.getStoreById(id), HttpStatus.OK);
//    }
//
//
//    @PutMapping("/update/{id}")
//    public Store updateStoreById(@PathVariable("id") Long id, @RequestBody Store store) {
//        return storeService.updateStoreById(store, id);
//    }
//
//    @DeleteMapping("/delete/{storeId}")
//    public void deleteStore(@PathVariable("storeId") int storeId) {
//        storeService.deleteStoreById(storeId);
//    }


//    @GetMapping("/all-order")        //1st way
//    public List<Object> orders(@RequestParam("user") String user, @RequestParam("password") String password) {
//        long check = storeFacade.isValid(user, password);
//        if (check != -1) {
//            List<Order> allOrders = storeOrderFeignClient.getAllOrders();
//            return Collections.singletonList(storeFacade.ordersByStoreId(check, allOrders));
//        }
//        return Collections.singletonList(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("False", "Wrong user or password")));
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
//thử ví dụ
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        long check = storeFacade.isValid(userName, password);
        if (check != -1) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObjectEntity("True", "Login successfully"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObjectEntity("False", "Wrong user or password"));
    }

    //Thymleaf
//    @GetMapping("")
//    public String viewHomePage() {
//        return "home";
//    }
//
//    @GetMapping("/user-login")
//    public String viewUserLogin(Model model) {
//        model.addAttribute("store", new Store());
//        return "userLogin";
//    }
//
//    @GetMapping("/register")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute("store", new Store());
//        return "signup_form";
//    }
//
//
//    @PostMapping("/update")
//    public String showUpdateForm(Model model, @RequestParam("userName") String userName) {
//        Store store = new Store();
//        store.setUserName(userName);
//        model.addAttribute("store", store);
//        return "information";
//    }
//
//    @PostMapping("/process_register")
//    public String processRegister(@RequestBody Store store) {
//        Store storeCheck = storeFacade.saveStore(store);
//        if (storeCheck != null) {
//            return "register_success";
//        }
//        return "register_faild";
//    }

//    @PostMapping("/process_login")
//    public String processLogin(Store store, Model model) {
//        long idUser = storeFacade.isValid(store);
//        if (idUser != -1) {
//            Store storeById = storeService.getStoreById(idUser);
//            model.addAttribute("store", storeById);
//            return "index";
//        }
//        model.addAttribute("notify", "Wrong user or password");
//        return "userLogin";
//    }
//
//    @PostMapping("/")
//    public String processLogin(Store store, Model model) {
//        long idUser = storeFacade.isValid(store);
//        if (idUser != -1) {
//            Store storeById = storeService.getStoreById(idUser);
//            model.addAttribute("store", storeById);
//            return "index";
//        }
//        model.addAttribute("notify", "Wrong user or password");
//        return "userLogin";
//    }
//
//    @PostMapping("/process_update")
//    public String processUpdate(Store store, Model model) {
//        Store storeInitial = storeFacade.getStoreByUserName(store);
//        String storePassword = storeInitial.getPassword();
//        Store storeUpdate = storeFacade.updateStore(store);
//        if (storeUpdate == null) {
//            model.addAttribute("notify", "Password and Confirm password not equal.");
//            model.addAttribute("store", store);
//            return "index";
//        } else if (!storePassword.equalsIgnoreCase(storeUpdate.getPassword()) && storeUpdate != null) {
//            model.addAttribute("store", storeUpdate);
//            model.addAttribute("notify", "Password Updated!");
//            return "index";
//        } else {
//            model.addAttribute("store", storeUpdate);
//            return "index";
//        }
//    }
//
//    //dang lam
//    @PostMapping("/store-order")
//    public String ordersOfStore(@RequestParam("userName") String userName, @RequestParam("password") String password, Model model) {
//        long idUser = storeFacade.isValid(userName, password);
//        List<Order> ordersByStoreId = storeOrderFeignClient.getOrdersByStoreId(idUser);
//        model.addAttribute("orders", ordersByStoreId);
//        return "store_order";
//    }
}
