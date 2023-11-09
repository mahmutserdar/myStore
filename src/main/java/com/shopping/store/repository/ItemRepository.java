package com.shopping.store.repository;

import com.shopping.store.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE (i.itemName LIKE %?1% or i.itemDesc LIKE %?1%)")
    List<Item> searchByName(String query);
}
