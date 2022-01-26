package com.example.weblogin.domain.saleitem;

import com.example.weblogin.domain.cartitem.CartItem;
import com.example.weblogin.domain.orderitem.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {
    List<SaleItem> findSaleItemsById(int id);
    List<SaleItem> findAll();
}
