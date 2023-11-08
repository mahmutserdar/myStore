package com.shopping.store.model;

import com.shopping.store.repository.BasketRepository;
//import com.shopping.store.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;

//    @Autowired
//    private UserRepository userRepository;

    public Basket addBasketItem(Basket basket, Item item, Integer quantity) {
        BasketItem basketItem = new BasketItem();
        basketItem.setItem(item);
        basketItem.setQuantity(quantity);
        basket.addBasketItem(basketItem);

//        if (basket.getUser() == null) {
//            User user = getAuthenticatedUser();
//            basket.setUser(user);
//        }

        return basketRepository.save(basket);
    }

//    private User getAuthenticatedUser() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            String username = ((UserDetails)principal).getUsername();
//
//            return userRepository.findByUsername(username).orElseThrow(() ->
//                    new UsernameNotFoundException("User not found with username: " + username));
//        } else {
//            throw new IllegalStateException("User not found in context");
//        }
//    }
}


