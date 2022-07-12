package com.example.demo.facade;

import com.example.demo.service.DeliveryNoteService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class DeliveryNoteFacade {
    @Autowired
    private DeliveryNoteService deliveryNoteService;

}
