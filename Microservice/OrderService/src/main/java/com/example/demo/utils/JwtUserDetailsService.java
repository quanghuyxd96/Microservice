package com.example.demo.utils;

import com.example.demo.user.UserSecurity;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserSecurity userSecurity = new UserSecurity();
        if(userSecurity.getUsername().equals(username)){
                return new UserSecurity(username, null);
            }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}