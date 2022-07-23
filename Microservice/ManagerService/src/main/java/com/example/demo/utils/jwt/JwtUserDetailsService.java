package com.example.demo.utils.jwt;

import com.example.demo.entity.Manager;
import com.example.demo.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
                return new UserSecurity(new Manager(manager.getUserName(), manager.getPassword()));
            }
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}