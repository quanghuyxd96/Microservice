package com.example.demo.facade;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderService;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class OrderFacade {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ItemFeignClient itemFeignClient;

    public <T,D> T convertModel(D obj, Class<T> classT){
        ModelMapper modelMapper = new ModelMapper();
        T obj1 = modelMapper.map(obj, classT);
        return obj1;
    }

    public <T,D> List<T> convertListModel(List<D> objList, Class<T> classT){
        List<T> objResults = new ArrayList<T>();
        for(D obj : objList){
            ModelMapper modelMapper = new ModelMapper();
            T objResult = modelMapper.map(obj,classT);
            objResults.add(objResult);
        }
        return objResults;
    }
}
