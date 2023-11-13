package com.shopping.store.model;

import com.shopping.store.repository.BasketItemRepository;
import com.shopping.store.repository.BasketRepository;
//import com.shopping.store.repository.UserRepository;
import com.shopping.store.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BasketService {

    @Autowired
    private BasketItemRepository basketItemRepository;

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


    @Transactional

    public void deleteItemReferences(Long itemId) {
        // Assuming you have a method in your repository to find basket items by item ID
        List<BasketItem> basketItems = basketItemRepository.findByItemId(itemId);

        for (BasketItem basketItem : basketItems) {
            basketItemRepository.delete(basketItem);
        }
    }


}


