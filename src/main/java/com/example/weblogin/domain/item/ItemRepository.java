package com.example.weblogin.domain.item;

import com.example.weblogin.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Item findItemById(int id);
}
