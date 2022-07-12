package com.example.demo.facade;

import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.Store;
import com.example.demo.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StoreFacade {

    @Autowired
    private StoreService storeService;

    public Store saveStore(Store store){
        return storeService.saveStore(store);
    }

//    public long isValid(String user, String password){
//        List<Store> allStore = storeService.getAllStore();
//        for(Store store : allStore){
//            if(store.getUserName().equalsIgnoreCase(user)&&store.getPassword().equalsIgnoreCase(password)){
//                return store.getId();
//            }
//        }
//        return -1;
//    }


    public Store getStoreByUserName(Store store){
        return storeService.getStoreByUserName(store);
    }

    public List<OrderDTO> ordersByStoreId(long id, List<OrderDTO> orders){
        List<OrderDTO> orderList = new ArrayList<OrderDTO>();
        for(OrderDTO order : orders){
            if(order.getStoreId()==id){
                orderList.add(order);
            }
        }
        if(orderList.isEmpty()){
            return null;
        }
        return orderList;
    }


    public long isValid(Store store){
        return storeService.validUserPassword(store);
    }

    public long isValid(String userName, String password){
        return storeService.validUserPassword(userName,password);
    }

    public Store updateStore(Store store){
        return storeService.updateStoreById(store);
    }

    public List<Store> getAllStore(){
        return storeService.getAllStore();
    }





}
