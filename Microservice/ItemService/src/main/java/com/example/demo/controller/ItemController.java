package com.example.demo.controller;

import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.Item;
import com.example.demo.entity.Supplier;
import com.example.demo.facade.ItemFacade;
import com.example.demo.response.ResponseObjectEntity;
import com.example.demo.service.ItemService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@CrossOrigin("*")
public class ItemController {
    @Autowired
    private ItemFacade itemFacade;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Item saveItem(@RequestBody ItemDTO itemDTO) {
        return itemFacade.getItemService().saveItem(itemFacade.convertItemDTOToItem(itemDTO));
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public List<ItemDTO> getAllItem() {
        List<Item> allItem = itemFacade.getItemService().getAllItem();
        return itemFacade.convertListItemToListItemDTO(allItem);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<ItemDTO> getItemById(@RequestParam("id") Long id) {
        Item item = itemFacade.getItemService().getItemById(id);
        if (item == null) {
            return null;
        }
        return new ResponseEntity<>(itemFacade.convertItemToItemDTO(item), HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Item updateItemById(@RequestParam("id") Long id, @RequestBody Item item) {
        return itemFacade.getItemService().updateItemById(item, id);
    }

    @PostMapping("/update-quantity")
    public List<Item> updateItemQuantity(@RequestBody List<Item> items){
        return itemFacade.updateItemsQuantity(items);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ResponseObjectEntity> deleteItemById(@RequestParam("id") Long id) {
        boolean check = itemFacade.getItemService().deleteItemById(id);
        ResponseObjectEntity responseObject = new ResponseObjectEntity();
        if (!check) {
            responseObject.setStatus("Faild");
            responseObject.setMessage("No item to delete!!!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);
        }
        responseObject.setStatus("OK");
        responseObject.setMessage("Delete Success");
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

    //demo
    @PostMapping("/delete/demo")
    public ResponseEntity<ResponseObjectEntity> delete(@RequestParam("id") Long id) {
        System.out.println(id);
        ResponseObjectEntity responseObject = new ResponseObjectEntity();
        if (itemFacade.getItemService().deleteItemById(id)) {
            responseObject.setStatus("OK");
            responseObject.setMessage("Delete Success");
            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
        }
        responseObject.setStatus("Faild");
        responseObject.setMessage("No item to delete!!!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);
    }

    @RequestMapping(value = "/supplier/save", method = RequestMethod.POST)
    public Supplier saveSupplier(@RequestBody Supplier supplier) {
        return itemFacade.getSupplierService().saveSupplier(supplier);
    }

    @GetMapping("/suppliers")
    public List<Supplier> getAllSupplier() {
        return itemFacade.getSupplierService().getAllSuppliers();
    }

    @RequestMapping(value = "/supplier/get", method = RequestMethod.GET)
    public ResponseEntity<Supplier> getSupplier(@RequestParam("id") Long id) {
        return new ResponseEntity<>(itemFacade.getSupplierService().getSupplierById(id), HttpStatus.OK);
    }

    @PutMapping("/supplier/update")
    public Supplier updateSupplierById(@RequestParam("id") Long id, @RequestBody Supplier supplier) {
        return itemFacade.getSupplierService().updateSupplierById(supplier, id);
    }

    @DeleteMapping("/supplier/delete")
    public ResponseEntity<ResponseObjectEntity> deleteSupplier(@RequestParam("id") Long id) {
        boolean check = itemFacade.getSupplierService().deleteSupplierById(id);
        ResponseObjectEntity responseObject = new ResponseObjectEntity();
        if (!check) {
            responseObject.setStatus("Faild");
            responseObject.setMessage("No supplier to delete!!!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);
        }
        responseObject.setStatus("OK");
        responseObject.setMessage("Delete Success");
        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }

//    @GetMapping("/demo")
//    public Item getAllItemsDemoRabbit(@RequestParam("id" id)){
//        itemFacade.getItemService().getAllItemDemoRabbit();
//        System.out.println();
//        return itemFacade.getItemService().getAl
//    }

    @GetMapping("/demo")
    @RabbitListener(queues = "item.queue")
    public Item getItemByIdDemoRabbit(@RequestParam("id") Long id) {
        itemFacade.getItemService().getItemByIdDemorabbit(id);
        return itemFacade.getItemService().getItemById(id);
    }
}
