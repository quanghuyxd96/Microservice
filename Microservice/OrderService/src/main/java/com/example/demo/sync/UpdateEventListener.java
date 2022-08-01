package com.example.demo.sync;

import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

@Component
public class UpdateEventListener implements PostUpdateEventListener {
    @Override
    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {
        System.out.println(postUpdateEvent.getOldState());
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return true;
    }
}