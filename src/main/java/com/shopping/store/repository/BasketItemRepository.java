package com.shopping.store.repository;

import com.shopping.store.model.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {

    List<BasketItem> findByItemId(Long itemId);
}
