package com.example.demo.facade;

import com.example.demo.client.ItemFeignClient;
import com.example.demo.client.OrderFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.SupplierDTO;
import com.example.demo.service.ManagerService;
import io.tej.SwaggerCodgen.model.Item;
import io.tej.SwaggerCodgen.model.Order;
import io.tej.SwaggerCodgen.model.Supplier;
import lombok.Getter;
import net.bytebuddy.description.type.TypeList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ManagerFacade {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private ItemFeignClient itemFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    public JavaMailSender emailSender;

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
