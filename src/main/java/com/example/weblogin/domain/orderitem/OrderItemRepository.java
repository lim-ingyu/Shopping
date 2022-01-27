package com.example.weblogin.domain.orderitem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findOrderItemsByUserId(int userId);
    List<OrderItem> findAll();
}
