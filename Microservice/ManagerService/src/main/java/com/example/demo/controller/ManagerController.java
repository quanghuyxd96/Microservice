//package com.example.demo.controller;
//
//
//import com.example.demo.dto.ItemDTO;
//import com.example.demo.dto.OrderDTO;
//import com.example.demo.dto.StoreDTO;
//import com.example.demo.dto.SupplierDTO;
//import com.example.demo.entity.Manager;
//import com.example.demo.facade.ManagerFacade;
//import com.example.demo.response.ResponseObjectEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
////@RestController
////@RequestMapping("manager")
//public class ManagerController {
//    @Autowired
//    private ManagerFacade managerFacade;
//
//    //Manager
//    @GetMapping("")
//    public List<Manager> getAllManager(@RequestParam("user") String user, @RequestParam("password") String password) {
//        return managerFacade.getManagerService().getAllManagers();
//    }
//
////    @RequestMapping(value = "/get", method = RequestMethod.GET)
////    public ResponseEntity<Manager> getManagerById(@RequestParam("id") Long id, @RequestParam("user") String user, @RequestParam("password") String password) {
////        return new ResponseEntity<Manager>(managerFacade.getManagerService().getManagerById(id),  HttpStatus.OK);
////    }
//
//    //Store
//    @GetMapping("/manage-store/get-all-store")
//    public List<StoreDTO> getAllStore() {
//        return managerFacade.getStoreFeignClient().getALlStore();
//    }
//
//    @GetMapping("/manage-store/get-store-by-id")
//    public ResponseEntity<StoreDTO> getStoreById(@RequestParam("id") Long id){
//        return managerFacade.getStoreFeignClient().getStoreById(id);
//    }
//
//    @DeleteMapping("/manage-store/delete-store-by-id")
//    public ResponseEntity<ResponseObjectEntity> deleteStoreById(@RequestParam("id") Long id){
//        return managerFacade.getStoreFeignClient().deleteStoreById(id);
//    }
//
//    //Item
//
////    @PostMapping("/manage-item/save")
////    public ResponseEntity<ItemDTO> saveItem(@RequestBody ItemDTO itemDTO){
////        return managerFacade.getItemFeignClient().saveItem(itemDTO);
////    }
//
//    @GetMapping("/manage-item/items")
//    public List<ItemDTO> getAllItems(){
//        return managerFacade.getItemFeignClient().getAllItems();
//    }
//
//    @GetMapping("/manage-item/get")
//    public ResponseEntity<ItemDTO> getItemById(@RequestParam("id") Long id){
//        return managerFacade.getItemFeignClient().getItemById(id);
//    }
//
//    @PutMapping("/manage-item/update")
//    public ItemDTO updateItemById(@RequestParam("id") Long id, @RequestBody ItemDTO itemDTO){
//        return managerFacade.getItemFeignClient().updateItemById(id,itemDTO);
//    }
//
//    @DeleteMapping("/manage-item/delete")
//    public ResponseEntity<ResponseObjectEntity> deleteItemById(@RequestParam("id") Long id){
//        return managerFacade.getItemFeignClient().deleteItemById(id);
//    }
//
//    //Supplier
//    @PostMapping("/manage-supplier/save")
//    public SupplierDTO saveSupplier(@RequestBody SupplierDTO supplierDTO){
//        return managerFacade.getItemFeignClient().saveSupplier(supplierDTO);
//    }
//
//    @GetMapping("/manage-supplier/suppliers")
//    public List<SupplierDTO> getAllSuppliers(){
//        return managerFacade.getItemFeignClient().getAllSupplier();
//    }
//
//    @GetMapping("/manage-supplier/get")
//    public ResponseEntity<SupplierDTO> getSupplierById(@RequestParam("id") Long id){
//        return managerFacade.getItemFeignClient().getSupplierById(id);
//    }
//
//    @PutMapping("/manage-supplier/update")
//    public SupplierDTO updateSupplierById (@RequestParam("id") Long id,@RequestBody SupplierDTO supplierDTO){
//        return managerFacade.getItemFeignClient().updateSupplierById(id,supplierDTO);
//    }
//
//    @DeleteMapping("/manage-supplier/delete")
//    public ResponseEntity<ResponseObjectEntity> deleteSupplierById(@RequestParam("id") Long id){
//        return managerFacade.getItemFeignClient().deleteSupplierById(id);
//    }
//
//    //Order
//    @GetMapping("/manage-order/orders")
//    public List<OrderDTO> getAllOrders(){
//        return managerFacade.getOrderFeignClient().getAllOrders();
//    }
//
//    @GetMapping("/manage-order/get")
//    public ResponseEntity<OrderDTO> getOrderById(@RequestParam("id") Long id){
//        return managerFacade.getOrderFeignClient().getOrderById(id);
//    }
//
//
////    @GetMapping("/manager/login/store")
////    public List<Store> getAllStoreForManager(@RequestParam("user") String user, @RequestParam("password") String password) {
////        return managerFacade.getAllStoreForManager(user, password);
////    }
////
////
////    @PutMapping("/manager/login/update-password")
////    public Manager updateManagerPasswordById(@RequestParam("id") Long id, @RequestBody Manager manager, @RequestParam("user") String user, @RequestParam("password") String password) {
////        return managerFacade.updateManagerPasswordById(id, manager, user, password);
////    }
////
////
////    @RequestMapping(value = "/manager/login/get-store", method = RequestMethod.GET)
////    public ResponseEntity<Store> getStoreForManager(@RequestParam("id") Long id, @RequestParam("user") String user, @RequestParam("password") String password) {
////        return managerFacade.getStoreForManager(id, user, password);
////    }
////
////    @DeleteMapping("/manager/login/delete-store")
////    public void deleteStore(@RequestParam("id") long id, @RequestParam("user") String user, @RequestParam("password") String password) {
////        managerFacade.deleteStoreByOwnerManager(id, user, password);
////    }
//}
