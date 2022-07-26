package com.example.demo.controller;

import com.example.demo.facade.ItemFacade;
import com.example.demo.response.ResponseObjectEntity;
import io.tej.SwaggerCodgen.api.ApiUtil;
import io.tej.SwaggerCodgen.api.ItemApi;
import io.tej.SwaggerCodgen.model.ItemModel;
import io.tej.SwaggerCodgen.model.ResponseObject;
import io.tej.SwaggerCodgen.model.SupplierModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.utils.Constants.AUTHOR;

@RestController
@CrossOrigin("*")
public class ItemSwaggerController implements ItemApi {
    @Autowired
    private ItemFacade itemFacade;

    @Override
    public ResponseEntity<ItemModel> itemSavePost(ItemModel itemModel) {
        ItemModel itemResp = itemFacade.saveItem(itemModel);
        if (itemResp == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(itemResp, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ItemModel>> itemItemsGet() {
        List<ItemModel> itemModels = itemFacade.getAllItems();
        if (itemModels == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(itemModels, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ItemModel> itemGetGet(Long id) {
        ItemModel itemModel = itemFacade.getItemById(id);
        if (itemModel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(itemModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ItemModel> itemUpdatePriceOrQuantityPut(Long supplierId, ItemModel itemModel) {
        ItemModel itemModelResp = itemFacade.updatePriceOrQuantity(itemModel, supplierId);
        if (itemModelResp == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(itemModelResp, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ItemModel> itemUpdatePut(Long id, ItemModel itemModel) {
        ItemModel itemModelResp = itemFacade.updateItem(itemModel, id);
        if (itemModelResp == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(itemModelResp, HttpStatus.OK);
    }

//    @Override
//    public ResponseEntity<List<ItemModel>> itemUpdateQuantityPost(List<ItemModel> itemModel) {
//        return new ResponseEntity<>(itemFacade.updateItemsQuantity(itemModel), HttpStatus.OK);
//    }


//    //su dung MQ nen khong dung den
//    @PostMapping("/item/update-quantity")
//    public ResponseEntity<List<ItemModel>> updateItemQuantity(@RequestBody List<ItemModel> itemModels,
//                                                              @RequestHeader(AUTHOR) String token){
//        return new ResponseEntity<>(itemFacade.updateItemsQuantity(itemModels),HttpStatus.OK);
//    }

    @Override
    public ResponseEntity<ResponseObject> itemDeleteDelete(Long id) {
        return itemFacade.deleteItemById(id);
    }

    //supplier
    @Override
    public ResponseEntity<SupplierModel> itemSupplierSavePost(SupplierModel supplierModel) {
        SupplierModel supplierModelResp = itemFacade.saveSupplier(supplierModel);
        if (supplierModelResp == null) {
            return new ResponseEntity<>(supplierModelResp, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(supplierModelResp, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<SupplierModel>> itemSuppliersGet() {
        List<SupplierModel> suppliers = itemFacade.getAllSuppliers();
        if (suppliers == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SupplierModel> itemSupplierGetGet(Long id) {
        SupplierModel supplierModel = itemFacade.getSupplierById(id);
        if (supplierModel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(supplierModel, HttpStatus.OK);
    }

    public ResponseEntity<SupplierModel> itemSupplierUpdatePut(Long id, SupplierModel supplierModel) {
        SupplierModel supplier = itemFacade.updateSupplierById(id, supplierModel);
        if (supplier == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(supplier, HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> itemSupplierDeleteDelete(Long id) {
        return itemFacade.deleteSupplierById(id);
    }

}
