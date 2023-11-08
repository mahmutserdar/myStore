package com.shopping.store.repository;

import com.shopping.store.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemName(String itemName);

}
