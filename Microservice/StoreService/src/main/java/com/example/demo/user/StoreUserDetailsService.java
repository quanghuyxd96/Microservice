//package com.example.demo.user;
//
//import com.example.demo.entity.Store;
//import com.example.demo.repository.StoreRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Optional;
//
//public class StoreUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private StoreRepository storeRepository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Store> store = storeRepository.findById(1L);
//        if (store == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        return new StoreUserDetails(store.get());
//    }
//}
