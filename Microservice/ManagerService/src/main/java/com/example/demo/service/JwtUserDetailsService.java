package com.example.demo.service;

import com.example.demo.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private ManagerService managerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Manager> managers = managerService.getAllManagers();
        for (Manager manager : managers) {
            if (manager.getUserName().equalsIgnoreCase(username)) {
                return new User(manager.getUserName(), manager.getPassword(), new ArrayList<>());
            }
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}