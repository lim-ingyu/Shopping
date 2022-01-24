package com.example.weblogin.domain.order;

import com.example.weblogin.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
