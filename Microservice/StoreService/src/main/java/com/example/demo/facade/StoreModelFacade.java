package com.example.demo.facade;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.Store;
import com.example.demo.service.StoreService;
import io.tej.SwaggerCodgen.model.Order;
import io.tej.SwaggerCodgen.model.OrderDetail;
import io.tej.SwaggerCodgen.model.StoreModel;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class StoreModelFacade {
    @Autowired
    private StoreService storeService;

    public List<StoreModel> getAllStoreModel() {
        List<Store> stores = storeService.getAllStore();
        List<StoreModel> StoreModels = new ArrayList<StoreModel>();
        for (Store store : stores) {
            StoreModel StoreModel = new StoreModel();
            StoreModel = convertStoreToStoreModel(store);
            StoreModels.add(StoreModel);
        }
        return StoreModels;
    }


    public StoreModel getStoreModelById(Long id) {
        Store store = storeService.getStoreById(id);
        StoreModel StoreModel = new StoreModel();
        StoreModel = convertStoreToStoreModel(store);
        return StoreModel;
    }

    public long isValid(String userName, String password) {
        return storeService.validUserPassword(userName, password);
    }

    public StoreModel getStoreByUserName(String userName, String password) {
        Store storeByUserName = storeService.getStoreByUserName(userName, password);
        StoreModel StoreModel = new StoreModel();
        StoreModel = convertStoreToStoreModel(storeByUserName);
        return StoreModel;
    }

    public StoreModel convertStoreToStoreModel(Store store) {
        StoreModel StoreModel = new StoreModel();
        StoreModel.setUserName(store.getUserName());
        StoreModel.setPassword(store.getPassword());
        StoreModel.setName(store.getName());
        StoreModel.setEmail(store.getEmail());
        StoreModel.setAddress(store.getAddress());
        StoreModel.setId(store.getId());
        StoreModel.setPhoneNumber(store.getPhoneNumber());
        return StoreModel;
    }

    public Store convertStoreModelToStore(StoreModel StoreModel) {
        Store store = new Store();
        store.setUserName(StoreModel.getUserName());
        store.setPassword(StoreModel.getPassword());
        store.setName(StoreModel.getName());
        store.setEmail(StoreModel.getEmail());
        store.setAddress(StoreModel.getAddress());
        store.setPhoneNumber(StoreModel.getPhoneNumber());
        return store;
    }

    public Store convertStoreModelToStore(StoreModel StoreModel, boolean check) {
        Store store = new Store();
        store.setUserName(StoreModel.getUserName());
        store.setPassword(StoreModel.getPassword());
        store.setName(StoreModel.getName());
        store.setEmail(StoreModel.getEmail());
        store.setAddress(StoreModel.getAddress());
        store.setPhoneNumber(StoreModel.getPhoneNumber());
        store.setConfirmPassword(StoreModel.getConfirmPassword());
        return store;
    }

    public Store saveStore(StoreModel StoreModel) {
        Store store = new Store();
        store = convertStoreModelToStore(StoreModel);
        return storeService.saveStore(store);
    }

    public Store updateStore(StoreModel StoreModel) {
        Store store = new Store();
        store = convertStoreModelToStore(StoreModel, true);
        return storeService.updateStoreById(store);
    }

    public Order saveOrder(Long id, List<OrderDetail> orderDetail) {
        List<OrderDetailDTO> orderDetailDTOS = convertListModel(orderDetail, OrderDetailDTO.class);
        OrderDTO orderDTO = storeService.saveOrder(id, orderDetailDTOS);
        if (orderDTO == null) {
            return null;
        }
        return convertModel(orderDTO, Order.class);
    }

    public boolean deleteStore(Long id) {
        return storeService.deleteStoreById(id);
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
