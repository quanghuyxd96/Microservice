package com.example.demo.sync;

import lombok.RequiredArgsConstructor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@RequiredArgsConstructor
@Service
public class HibernateListener {

    @Autowired
    private InsertEventListener insertEventListener;

    @Autowired
    private UpdateEventListener updateEventListener;
    private final EntityManagerFactory entityManagerFactory;


    @PostConstruct
    private void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(insertEventListener);
        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(updateEventListener);
    }
}