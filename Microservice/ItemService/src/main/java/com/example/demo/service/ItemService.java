package com.example.demo.service;

import com.example.demo.entity.Item;
import com.example.demo.repository.ItemRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;


    public List<Item> getAllItem() {
        return itemRepository.findAll();
    }


    public Item saveItem(Item item) {
        List<Item> items = itemRepository.findAll();
        long idMax=0;
        for(Item item1 : items){
            idMax=Math.max(idMax,item1.getId());
        }
        item.setId(idMax+1);
        System.out.println(idMax);
        System.out.println(item.toString());
        return itemRepository.save(item);
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
        if(itemRepositoryById.isPresent()){
            if(item.getName().length()>0){
                itemRepositoryById.get().setName(item.getName());
            }
            if(item.getPrice()!=itemRepositoryById.get().getPrice()){
                itemRepositoryById.get().setPrice(item.getPrice());
            }
            if(item.getQuantity()!=itemRepositoryById.get().getQuantity()){
                itemRepositoryById.get().setQuantity(item.getQuantity());
            }
            return itemRepository.save(itemRepositoryById.get());
        }
        return null;
    }


    public boolean deleteItemById(long id) {
        Optional<Item> itemRepositoryById = itemRepository.findById(id);
        if (itemRepositoryById.isPresent()) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void getAllItemDemoRabbit(){
        rabbitTemplate.convertAndSend(exchange, routingkey, itemRepository.findAll());
    }

    public void getItemByIdDemorabbit(Long id){
        System.out.println(itemRepository.findById(id).get());
        rabbitTemplate.convertAndSend(exchange,routingkey,itemRepository.findById(id).get());
    }
}
