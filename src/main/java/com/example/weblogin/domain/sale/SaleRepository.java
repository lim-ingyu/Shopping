package com.example.weblogin.domain.sale;

import com.example.weblogin.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Integer> {

    List<Sale> findAll();
    List<Sale> findSalesById(int id);
    Sale findBySellerId(int id);
}
