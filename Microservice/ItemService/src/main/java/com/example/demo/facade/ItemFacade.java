package com.example.demo.facade;

import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.Item;
import com.example.demo.entity.Supplier;
import com.example.demo.service.ItemService;
import com.example.demo.service.SupplierService;
import io.tej.SwaggerCodgen.model.ItemModel;
import io.tej.SwaggerCodgen.model.ResponseObject;
import io.tej.SwaggerCodgen.model.SupplierModel;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ItemFacade {
    @Autowired
    private ItemService itemService;

    @Autowired
    private SupplierService supplierService;

    public Item convertItemDTOToItem(ItemDTO itemDTO) {
        Item item = new Item();
        Supplier supplier = new Supplier();
        supplier.setId(itemDTO.getSupplierId());
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setSupplier(supplier);
        item.setQuantity(itemDTO.getQuantity());
        return item;
    }

    public ItemDTO convertItemToItemDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName(item.getName());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setSupplierId(item.getSupplier().getId());
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setId(item.getId());
        return itemDTO;
    }

    public List<ItemDTO> convertListItemToListItemDTO(List<Item> items) {
        List<ItemDTO> itemDTOList = new ArrayList<ItemDTO>();
        for (Item item : items) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setName(item.getName());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setSupplierId(item.getSupplier().getId());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setId(item.getId());
            itemDTOList.add(itemDTO);
        }
        return itemDTOList;
    }


    public ItemModel saveItem(ItemModel itemModel) {
        Supplier supplier = new Supplier();
        supplier.setId(itemModel.getSupplierId());
        Item item = convertModel(itemModel, Item.class);
        item.setSupplier(supplier);
        Item item1 = itemService.saveItem(item);
        if (item1 == null) {
            return null;
        }
        return convertModel(item1, ItemModel.class);
    }

    public List<ItemModel> getAllItems() {
        List<Item> items = itemService.getAllItem();
        if (items == null) {
            return null;
        }
        return convertListModel(items, ItemModel.class);
    }

    public ItemModel getItemById(long id) {
        Item item = itemService.getItemById(id);
        if (item == null) {
            return null;
        }
        return convertModel(item, ItemModel.class);
    }

    public ItemModel updatePriceOrQuantity(ItemModel itemModel, long supplierId) {
        Item item = itemService.updateItemPriceOrItemQuantity(convertModel(itemModel, Item.class), supplierId);
        if (item == null) {
            return null;
        }
        return convertModel(item, ItemModel.class);
    }

    public ItemModel updateItem(ItemModel itemModel, long supplierId) {
        Item item = itemService.updateItemById(convertModel(itemModel, Item.class), supplierId);
        if (item == null) {
            return null;
        }
        return convertModel(item, ItemModel.class);
    }

    public List<ItemModel> updateItemsQuantity(List<ItemModel> itemModels) {
        List<Item> items = convertListModel(itemModels, Item.class);
        List<Item> itemsAfterUpdate = itemService.updateItemQuantityAfterUpdateOrder(items);
        return convertListModel(itemsAfterUpdate, ItemModel.class);
    }

    public ResponseEntity<ResponseObject> deleteItemById(long id) {
        boolean check = itemService.deleteItemById(id);
        ResponseObject responseObject = new ResponseObject();
        if (!check) {
            responseObject.setStatus("False");
            responseObject.setMessage("No item to delete");
            return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
        }
        responseObject.setStatus("OK");
        responseObject.setMessage("Deleted");
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }


    //supplier
    public SupplierModel saveSupplier(SupplierModel supplierModel) {
        if (supplierModel == null) {
            return null;
        }
        Supplier supplier = supplierService.saveSupplier(convertModel(supplierModel, Supplier.class));
        if (supplier == null) {
            return null;
        }
        return convertModel(supplier, SupplierModel.class);
    }

    public List<SupplierModel> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        if (suppliers == null) {
            return null;
        }
        return convertListModel(suppliers, SupplierModel.class);
    }

    public SupplierModel getSupplierById(long id) {
        Supplier supplier = supplierService.getSupplierById(id);
        if (supplier == null) {
            return null;
        }
        return convertModel(supplier, SupplierModel.class);
    }

    public SupplierModel updateSupplierById(long id, SupplierModel supplierModel) {
        if (supplierModel == null) {
            return null;
        }
        Supplier supplier = convertModel(supplierModel, Supplier.class);
        if (supplier == null) {
            return null;
        }
        return convertModel(supplier, SupplierModel.class);
    }

    public ResponseEntity<ResponseObject> deleteSupplierById(long id) {
        boolean check = supplierService.deleteSupplierById(id);
        ResponseObject responseObject = new ResponseObject();
        if (!check) {
            responseObject.setStatus("False");
            responseObject.setMessage("No supplier to delete!!!");
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        }
        responseObject.setStatus("OK");
        responseObject.setMessage("Deleted");
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    private <T, D> T convertModel(D obj, Class<T> classT) {
        ModelMapper modelMapper = new ModelMapper();
        T obj1 = modelMapper.map(obj, classT);
        return obj1;
    }

    private <T, D> List<T> convertListModel(List<D> objList, Class<T> classT) {
        List<T> objResults = new ArrayList<T>();
        for (D obj : objList) {
            ModelMapper modelMapper = new ModelMapper();
            T objResult = modelMapper.map(obj, classT);
            objResults.add(objResult);
        }
        return objResults;
    }


}
