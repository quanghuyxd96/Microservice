package com.example.demo.utils;

import com.example.demo.entity.Store;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserSecurity implements UserDetails {
    private Store store;


    public UserSecurity(Store store) {
        this.store = store;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if(getUsername().startsWith("admin")){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return authorities;
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.store.getPassword();
    }

    @Override
    public String getUsername() {
        return this.store.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
