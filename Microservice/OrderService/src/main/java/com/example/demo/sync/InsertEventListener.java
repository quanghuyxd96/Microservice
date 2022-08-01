package com.example.demo.sync;

import com.example.demo.entity.Order;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class InsertEventListener implements PostInsertEventListener {
    @Override
    public void onPostInsert(PostInsertEvent postInsertEvent) {

        System.out.println("The Event comes here with data: " + Arrays.toString(postInsertEvent.getState()));
        Object entity = postInsertEvent.getEntity();
        if(entity instanceof Order){
            System.out.println("Order");
        }else {
            System.out.println("Order Detail");
        }
        EntityPersister persister = postInsertEvent.getPersister();
        System.out.println(persister);
    }


    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return true;
    }
}
