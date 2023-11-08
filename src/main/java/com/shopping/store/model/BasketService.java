package com.shopping.store.model;

import com.shopping.store.repository.BasketRepository;
//import com.shopping.store.repository.UserRepository;
import com.shopping.store.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private UserRepository userRepository;

    public Basket addBasketItem(Basket basket, Item item, Integer quantity) {
        BasketItem basketItem = new BasketItem();
        basketItem.setItem(item);
        basketItem.setQuantity(quantity);
        basket.addBasketItem(basketItem);

        return basketRepository.save(basket);
    }

}


