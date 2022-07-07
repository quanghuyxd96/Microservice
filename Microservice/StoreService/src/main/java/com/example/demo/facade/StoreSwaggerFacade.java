package com.example.demo.facade;

import com.example.demo.entity.Store;
import com.example.demo.service.StoreService;
import io.tej.SwaggerCodgen.model.StoreSwagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StoreSwaggerFacade {
    @Autowired
    private StoreService storeService;

    public List<StoreSwagger> getAllStoreSwagger() {
        List<Store> stores = storeService.getAllStore();
        List<StoreSwagger> storeSwaggers = new ArrayList<StoreSwagger>();
        for (Store store : stores) {
            StoreSwagger storeSwagger = new StoreSwagger();
            storeSwagger = convertStoreToStoreSwagger(store);
            storeSwaggers.add(storeSwagger);
        }
        return storeSwaggers;
    }


    public StoreSwagger getStoreSwaggerById(Long id) {
        Store store = storeService.getStoreById(id);
        StoreSwagger storeSwagger = new StoreSwagger();
        storeSwagger = convertStoreToStoreSwagger(store);
        return storeSwagger;
    }

    public long isValid(String userName, String password){
        return storeService.validUserPassword(userName,password);
    }

    public StoreSwagger getStoreByUserName(String userName, String password){
        Store storeByUserName = storeService.getStoreByUserName(userName, password);
        StoreSwagger storeSwagger = new StoreSwagger();
        storeSwagger = convertStoreToStoreSwagger(storeByUserName);
        return storeSwagger;
    }

    public StoreSwagger convertStoreToStoreSwagger(Store store) {
        StoreSwagger storeSwagger = new StoreSwagger();
        storeSwagger.setUserName(store.getUserName());
        storeSwagger.setPassword(store.getPassword());
        storeSwagger.setName(store.getName());
        storeSwagger.setEmail(store.getEmail());
        storeSwagger.setAddress(store.getAddress());
        storeSwagger.setId(store.getId());
        storeSwagger.setPhoneNumber(store.getPhoneNumber());
        return storeSwagger;
    }

    public Store convertStoreSwaggerToStore(StoreSwagger storeSwagger) {
        Store store = new Store();
        store.setUserName(storeSwagger.getUserName());
        store.setPassword(storeSwagger.getPassword());
        store.setName(storeSwagger.getName());
        store.setEmail(storeSwagger.getEmail());
        store.setAddress(storeSwagger.getAddress());
        store.setPhoneNumber(storeSwagger.getPhoneNumber());
        return store;
    }

    public Store convertStoreSwaggerToStore(StoreSwagger storeSwagger,boolean check) {
        Store store = new Store();
        store.setUserName(storeSwagger.getUserName());
        store.setPassword(storeSwagger.getPassword());
        store.setName(storeSwagger.getName());
        store.setEmail(storeSwagger.getEmail());
        store.setAddress(storeSwagger.getAddress());
        store.setPhoneNumber(storeSwagger.getPhoneNumber());
        store.setConfirmPassword(storeSwagger.getConfirmPassword());
        return store;
    }

    public Store saveStore(StoreSwagger storeSwagger){
        Store store = new Store();
        store = convertStoreSwaggerToStore(storeSwagger);
        return storeService.saveStore(store);
    }

    public Store updateStore(StoreSwagger storeSwagger){
        Store store = new Store();
        store = convertStoreSwaggerToStore(storeSwagger,true);
        return storeService.updateStoreById(store);
    }

    public boolean deleteStore(Long id){
        return storeService.deleteStoreById(id);
    }


}
