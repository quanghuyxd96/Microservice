package com.example.demo.client;

import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.SupplierDTO;
import com.example.demo.response.ResponseObjectEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@FeignClient(name = "itemFeignClient", url = "${client.post.baseUrl3}")
public interface ItemFeignClient {
    @PostMapping("/item/save")
    ItemDTO saveItem(@RequestBody ItemDTO itemDTO);

    @GetMapping("/item/items")
    List<ItemDTO> getAllItems();

    @GetMapping("/item/get")
    ResponseEntity<ItemDTO> getItemById(@RequestParam("id") Long id);

    @PutMapping("/item/update")
    ItemDTO updateItemById(@RequestParam("id") Long id, @RequestBody ItemDTO itemDTO);

    @DeleteMapping("item/delete")
    ResponseEntity<ResponseObjectEntity> deleteItemById(@RequestParam("id") Long id);


    //demo delete su dung post
    @PostMapping("/item/delete/demo")
    ResponseEntity<ResponseObjectEntity> delete(@RequestParam("id") Long id);

    @PostMapping("/item/supplier/save")
    SupplierDTO saveSupplier(@RequestBody SupplierDTO supplierDTO);

    @GetMapping("/item/suppliers")
    List<SupplierDTO> getAllSupplier();

    @GetMapping("/item/supplier/get")
    ResponseEntity<SupplierDTO> getSupplierById(@RequestParam("id") Long id);

    @PutMapping("/item/supplier/update")
    SupplierDTO updateSupplierById(@RequestParam("id") Long id, @RequestBody SupplierDTO supplierDTO);

    @DeleteMapping("item/supplier/delete")
    ResponseEntity<ResponseObjectEntity> deleteSupplierById(@RequestParam("id") Long id);
}
