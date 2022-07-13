package com.example.demo.service;

import com.example.demo.client.OrderFeignClient;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.Store;
import com.example.demo.repository.StoreRepository;
import lombok.Getter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderFeignClient orderFeignClient;

    public List<Store> getAllStore() {
        return storeRepository.findAll();
    }


    public Store saveStore(Store store) {
        store.setUserName(store.getUserName().toLowerCase());
        Store storeRepositoryByUserName = storeRepository.findByUserName(store.getUserName());
        if (storeRepositoryByUserName != null) {
            return null;
        }
        store.setPassword(endCodePassword(store.getPassword()));
        store.setPayment(0);
        return storeRepository.save(store);
    }


//    public Store updateStorePayment(long id) {
//        Optional<Store> store = storeRepository.findById(id);
//        if (store.isPresent()) {
//            store.get().setPayment(paid);
//            return storeRepository.save(store.get());
//        }
//        return null;
//    }


    public Store updateStoreById(Store store, long id) {
        Optional<Store> storeRepositoryById = storeRepository.findById(id);
        storeRepositoryById.get().setName(store.getName());
        storeRepositoryById.get().setEmail(store.getEmail());
        storeRepositoryById.get().setAddress(store.getAddress());
        storeRepositoryById.get().setPhoneNumber(store.getPhoneNumber());
        storeRepositoryById.get().setPassword(store.getPassword());
        return storeRepository.save(storeRepositoryById.get());
    }


    //Hien tai chua dung toi
    public Store updateStoreByUserName(Store store) {
        Store storeRepositoryByUserName = storeRepository.findByUserName(store.getUserName());
        storeRepositoryByUserName.setName(store.getName());
        storeRepositoryByUserName.setEmail(store.getEmail());
        storeRepositoryByUserName.setAddress(store.getAddress());
        storeRepositoryByUserName.setPhoneNumber(store.getPhoneNumber());
        storeRepositoryByUserName.setPassword(store.getPassword());
        return storeRepository.save(storeRepositoryByUserName);
    }

    public Store updateStoreById(Store store) {
        Store storeRepositoryByUserName = storeRepository.findByUserName(store.getUserName());

        if (store.getName().length() > 0) {
            storeRepositoryByUserName.setName(store.getName());
        }
        if (store.getEmail().length() > 0) {
            storeRepositoryByUserName.setEmail(store.getEmail());
        }
        if (store.getAddress().length() > 0) {
            storeRepositoryByUserName.setAddress(store.getAddress());
        }
        if (store.getPhoneNumber().length() > 0) {
            storeRepositoryByUserName.setPhoneNumber(store.getPhoneNumber());
        }
        if (store.getPassword().length() > 0 || store.getConfirmPassword().length() > 0) {
            if (!store.getPassword().equalsIgnoreCase(store.getConfirmPassword())) {
                return null;
            }
            storeRepositoryByUserName.setPassword(endCodePassword(store.getPassword()));
        }
        return storeRepository.save(storeRepositoryByUserName);
    }


    public Store getStoreById(Long id) {
        Optional<Store> store = storeRepository.findById(id);
        if (store.isPresent()) {
            return store.get();
        }
        return null;
    }


    public boolean deleteStoreById(long id) {
        Optional<Store> storeRepositoryById = storeRepository.findById(id);
        if (storeRepositoryById.isPresent()) {
            storeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean isPresentStoreUser(String user) {
        List<Store> allStore = storeRepository.findAll();
        for (Store store : allStore) {
            if (user.equalsIgnoreCase(store.getUserName())) {
                return true;
            }
        }
        return false;
    }

    public String endCodePassword(String password) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            stringBuilder.append((char) (password.charAt(i) + 2));
        }
        return stringBuilder.toString();
    }

    public long validUserPassword(Store store) {
        List<Store> stores = storeRepository.findAll();
        for (Store storeDB : stores) {
            if (storeDB.getUserName().equalsIgnoreCase(store.getUserName()) && storeDB.getPassword().equalsIgnoreCase(endCodePassword(store.getPassword()))) {
                return storeDB.getId();
            }
        }
        return -1;
    }

    public long validUserPassword(String userName, String password) {
        List<Store> stores = storeRepository.findAll();
        for (Store storeDB : stores) {
            if (storeDB.getUserName().equalsIgnoreCase(userName) && storeDB.getPassword().equalsIgnoreCase(endCodePassword(password))) {
                return storeDB.getId();
            }
        }
        return -1;
    }

    public Store getStoreByUserName(String userName, String password) {
        List<Store> stores = storeRepository.findAll();
        for (Store storeDB : stores) {
            if (storeDB.getUserName().equalsIgnoreCase(userName) && storeDB.getPassword().equalsIgnoreCase(endCodePassword(password))) {
                return storeDB;
            }
        }
        return null;
    }

    public OrderDTO saveOrder(Long id, List<OrderDetailDTO> orderDetailDTOList) {
        OrderDTO orderDTO = orderFeignClient.saveOrder(orderDetailDTOList, id).getBody();
        if (orderDTO == null) {
            return null;
        }
        return orderDTO;
    }

    public Store getStoreByUserName(Store store) {
        return storeRepository.findByUserName(store.getUserName());
    }
}
