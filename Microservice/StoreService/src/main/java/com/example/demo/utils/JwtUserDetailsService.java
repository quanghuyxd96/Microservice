package com.example.demo.utils;

import com.example.demo.entity.Store;
import com.example.demo.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private StoreService storeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Store store = storeService.getStoreByUserName(username);
        if (store == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new UserSecurity(new Store(store.getUserName(), store.getPassword()));
    }
}