package com.example.demo.service;

import com.example.demo.client.OrderFeignClient;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.entity.Store;
import com.example.demo.mq.StoreSourceSend;
import com.example.demo.repository.StoreRepository;
import com.example.demo.utils.JwtTokenUtil;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Getter
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private StoreSourceSend storeSourceSend;

    private static final Logger logger = LogManager.getLogger(StoreService.class);

    public String forgotPassword(String email, String username) {
        Store store = storeRepository.findByEmailAndUserName(email, username);
        if (store == null) {
            return "Invalid email and username!!!";
        }
        String token = jwtTokenUtil.generateToken(username);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email",store.getEmail());
        hashMap.put("token",token);
        System.out.println(hashMap.get("token"));
        storeSourceSend.sendToGetNewPassword().send(MessageBuilder.withPayload(hashMap).build());
        return "We sent email to you!!!";
    }

    public String resetPassword(String token, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            logger.info("Password and confirm password are not equal.");
            return "Password and confirm password are not equal.";
        }
        if (!jwtTokenUtil.validateToken(token)) {
            logger.info("Invalid token");
            return "Invalid token";
        }
        Store store = getStoreByToken(token, true);
        if (store == null) {
            logger.info("No username to reset password!!!");
            return "No username to reset password!!!";
        }
        store.setPassword(endCodePassword(password));
        storeRepository.save(store);
        return "Your password successfully updated.";
    }

    public List<Store> getAllStore() {
        List<Store> stores = storeRepository.findAll();
        if (stores == null) {
            return null;
        }
        return stores;
    }


    public Store saveStore(Store store) {
        System.out.println(store.toString());
        if (store.getUserName().startsWith("admin")) {
            return null;
        }
        if (!store.getPassword().equals(store.getConfirmPassword())) {
            return null;
        }
        store.setUserName(store.getUserName().toLowerCase());
        Store storeRepositoryByUserName = storeRepository.findByUserNameQuery(store.getUserName());
        if (storeRepositoryByUserName != null) {
            return null;
        }
        store.setPassword(endCodePassword(store.getPassword()));
        System.out.println(store.getPassword());
        store.setPayment(0);
        return storeRepository.save(store);
    }


//    public Store updateStorePayment(long id) {
//        Optional<Store> store = storeRepository.findById(id);
//        if (store.isPresent()) {
//            store.get().setPayment(paid);
//            return storeRepository.save(store.get());
//        }
//        return null;
//    }


    public Store updateStoreById(Store store, long id) {
        Optional<Store> storeRepositoryById = storeRepository.findById(id);
        storeRepositoryById.get().setName(store.getName());
        storeRepositoryById.get().setEmail(store.getEmail());
        storeRepositoryById.get().setAddress(store.getAddress());
        storeRepositoryById.get().setPhoneNumber(store.getPhoneNumber());
        storeRepositoryById.get().setPassword(store.getPassword());
        return storeRepository.save(storeRepositoryById.get());
    }


    //Hien tai chua dung toi
//    public Store updateStoreByUserName(Store store) {
//        Store storeRepositoryByUserName = storeRepository.findByUserName(store.getUserName());
//        storeRepositoryByUserName.setName(store.getName());
//        storeRepositoryByUserName.setEmail(store.getEmail());
//        storeRepositoryByUserName.setAddress(store.getAddress());
//        storeRepositoryByUserName.setPhoneNumber(store.getPhoneNumber());
//        storeRepositoryByUserName.setPassword(store.getPassword());
//        return storeRepository.save(storeRepositoryByUserName);
//    }

    public Store updateStoreById(Store store) {
        Store storeRepositoryByUserName = storeRepository.findByUserNameQuery(store.getUserName());

        if (store.getName().length() > 0) {
            storeRepositoryByUserName.setName(store.getName());
        }
        if (store.getEmail().length() > 0) {
            storeRepositoryByUserName.setEmail(store.getEmail());
        }
        if (store.getAddress().length() > 0) {
            storeRepositoryByUserName.setAddress(store.getAddress());
        }
        if (store.getPhoneNumber().length() > 0) {
            storeRepositoryByUserName.setPhoneNumber(store.getPhoneNumber());
        }
        if (store.getPassword().length() > 0 || store.getConfirmPassword().length() > 0) {
            if (!store.getPassword().equalsIgnoreCase(store.getConfirmPassword())) {
                return null;
            }
            storeRepositoryByUserName.setPassword(endCodePassword(store.getPassword()));
        }
        return storeRepository.save(storeRepositoryByUserName);
    }


    public Store getStoreById(Long id) {
        Optional<Store> store = storeRepository.findById(id);
        if (store.isPresent()) {
            return store.get();
        }
        return null;
    }


    public boolean deleteStoreById(long id) {
        Optional<Store> storeRepositoryById = storeRepository.findById(id);
        if (storeRepositoryById.isPresent()) {
            storeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean isPresentStoreUser(String user) {
        List<Store> allStore = storeRepository.findAll();
        for (Store store : allStore) {
            if (user.equalsIgnoreCase(store.getUserName())) {
                return true;
            }
        }
        return false;
    }

    private String endCodePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode(password);
        return password;
    }

    public long validUserPassword(Store store) {
        List<Store> stores = storeRepository.findAll();
        for (Store storeDB : stores) {
            if (storeDB.getUserName().equalsIgnoreCase(store.getUserName()) && storeDB.getPassword().equalsIgnoreCase(endCodePassword(store.getPassword()))) {
                return storeDB.getId();
            }
        }
        return -1;
    }

    public long validUserPassword(String userName, String password) {
        List<Store> stores = storeRepository.findAll();
        for (Store storeDB : stores) {
            if (storeDB.getUserName().equalsIgnoreCase(userName) && storeDB.getPassword().equalsIgnoreCase(endCodePassword(password))) {
                return storeDB.getId();
            }
        }
        return -1;
    }

    public Store getStoreByUserName(String userName, String password) {
        List<Store> stores = storeRepository.findAll();
        for (Store storeDB : stores) {
            if (storeDB.getUserName().equalsIgnoreCase(userName) && storeDB.getPassword().equalsIgnoreCase(endCodePassword(password))) {
                return storeDB;
            }
        }
        return null;
    }

    public OrderDTO saveOrder(Long id, List<OrderDetailDTO> orderDetailDTOList) {
        OrderDTO orderDTO = orderFeignClient.saveOrder(orderDetailDTOList, id).getBody();
        if (orderDTO == null) {
            return null;
        }
        return orderDTO;
    }

    public Store getStoreByUserName(Store store) {
        return storeRepository.findByUserNameQuery(store.getUserName());
    }

    public Store getStoreByUserName(String userName) {
        Store store = storeRepository.findByUserName(userName);
        if (store == null) {
            return null;
        }
        return store;
    }

    public Store getStoreByToken(String token) {
        String tokenToUse = token.substring(7);
        logger.info(tokenToUse);
        String userName = jwtTokenUtil.getUsernameFromToken(tokenToUse);
        Store store = storeRepository.findByUserName(userName);
        if (store == null) {
            return null;
        }
        return store;
    }

    public Store getStoreByToken(String token, boolean check) {
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        Store store = storeRepository.findByUserName(userName);
        if (store == null) {
            return null;
        }
        return store;
    }

    public void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
