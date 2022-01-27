package com.example.weblogin.domain.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    // 유저의 id에 해당하는 장바구니
    Cart findByUserId(int id);
    Cart findCartById(int id);
    Cart findCartByUserId(int id);
}
