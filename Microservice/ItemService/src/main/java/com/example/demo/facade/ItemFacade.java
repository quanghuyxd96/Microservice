package com.example.demo.facade;

import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.Item;
import com.example.demo.entity.Supplier;
import com.example.demo.service.ItemService;
import com.example.demo.service.SupplierService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Item convertItemDTOToItem(ItemDTO itemDTO){
        Item item = new Item();
        Supplier supplier = new Supplier();
        supplier.setId(itemDTO.getSupplierId());
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setSupplier(supplier);
        item.setQuantity(itemDTO.getQuantity());
        return item;
    }

    public ItemDTO convertItemToItemDTO(Item item){
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName(item.getName());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setSupplierId(item.getSupplier().getId());
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setId(item.getId());
        return itemDTO;
    }

    public List<ItemDTO> convertListItemToListItemDTO(List<Item> items){
        List<ItemDTO> itemDTOList = new ArrayList<ItemDTO>();
        for(Item item : items){
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
}
