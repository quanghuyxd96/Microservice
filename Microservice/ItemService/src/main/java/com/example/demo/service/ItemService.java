package com.example.demo.service;

import com.example.demo.dto.ItemDTO;
import com.example.demo.entity.Item;
import com.example.demo.mq.DeliverySource;
import com.example.demo.mq.OrderSource;
import com.example.demo.repository.ItemRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Value("${spring.rabbitmq.exchange}")
//    private String exchange;
//
//    @Value("${spring.rabbitmq.routingkey}")
//    private String routingkey;


    public List<Item> getAllItem() {
        List<Item> items = itemRepository.findAll();
        if(items==null){
            return null;
        }
        return items;
    }


    public Item saveItem(Item item) {
        Optional<Item> itemRepo = itemRepository.findByNameAndSupplierId(item.getName(), item.getSupplier().getId());
        if (!itemRepo.isPresent()) {
            return itemRepository.save(item);
        }
        return null;
    }


    public Item getItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            return item.get();
        }
        return null;
    }


    public Item updateItemById(Item item, long id) {
        Optional<Item> itemRepositoryById = itemRepository.findById(id);
        if (itemRepositoryById.isPresent()) {
            if (item.getName().length() > 0) {
                itemRepositoryById.get().setName(item.getName());
            }
            if (item.getPrice() != itemRepositoryById.get().getPrice()) {
                itemRepositoryById.get().setPrice(item.getPrice());
            }
            if (item.getQuantity() != itemRepositoryById.get().getQuantity()) {
                itemRepositoryById.get().setQuantity(item.getQuantity());
            }
            return itemRepository.save(itemRepositoryById.get());
        }
        return null;
    }

    public Item updateItemPriceOrItemQuantity(Item item,Long supplierId) {
        try{
            Optional<Item> itemRepo = itemRepository.findByNameAndSupplierId(item.getName(), supplierId);
            if (itemRepo.isPresent()) {
                itemRepo.get().setQuantity(item.getQuantity());
                itemRepo.get().setPrice(item.getPrice());
                return itemRepository.save(itemRepo.get());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Item> updateItemQuantity(List<Item> items) {
        List<Item> itemList = new ArrayList<>();
        for (Item item : items) {
            Optional<Item> itemById = itemRepository.findById(item.getId());
            if (itemById.isPresent()) {
                itemById.get().setQuantity(item.getQuantity());
                itemList.add(itemById.get());
            }
        }
        return itemRepository.saveAll(itemList);
    }

    public List<Item> updateItemQuantityByDeleteOrder(List<Item> items) {
        List<Item> itemList = new ArrayList<>();
        for (Item item : items) {
            Optional<Item> itemById = itemRepository.findById(item.getId());
            if (itemById.isPresent()) {
                itemById.get().setQuantity(itemById.get().getQuantity()+item.getQuantity());
                itemList.add(itemById.get());
            }
        }
        return itemRepository.saveAll(itemList);
    }


    public boolean deleteItemById(long id) {
        Optional<Item> itemRepositoryById = itemRepository.findById(id);
        if (itemRepositoryById.isPresent()) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @StreamListener(target = DeliverySource.DELIVERY_ITEM_CHANNEL)
    public void getItemToUpdate(List<Item> items) {
        System.out.println(items.get(0).getId());
        updateItemQuantity(items);
    }


    //chút sửa
    @StreamListener(target = OrderSource.ITEM)
    public void getItemFromOrderToUpdate(List<Item> items) {
        System.out.println(1);
//        updateItemQuantityByDeleteOrder(items);
    }

//    public void getAllItemDemoRabbit() {
//        rabbitTemplate.convertAndSend(exchange, routingkey, itemRepository.findAll());
//    }
//
//    public void getItemByIdDemorabbit(Long id) {
//        System.out.println(itemRepository.findById(id).get());
//        rabbitTemplate.convertAndSend(exchange, routingkey, itemRepository.findById(id).get());
//    }
}
