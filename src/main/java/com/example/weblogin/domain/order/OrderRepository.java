package com.example.weblogin.domain.order;

import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // 유저의 id에 해당하는 주문내역
    Order findByUserId(int id);

    List<Order> findOrdersByUserId(int id);
}
