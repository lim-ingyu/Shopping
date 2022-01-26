package com.example.weblogin.service;

import com.example.weblogin.domain.cartitem.CartItem;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.order.Order;
import com.example.weblogin.domain.orderitem.OrderItem;
import com.example.weblogin.domain.sale.Sale;
import com.example.weblogin.domain.sale.SaleRepository;
import com.example.weblogin.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final UserPageService userPageService;

    // 판매된 상품 모두 찾기
    public List<Sale> findAllSaleItems() {return saleRepository.findAll();}

    // 해당 판매자 id에서 판매된 상품 찾기
    public List<Sale> findSaleItemsById(int id) {return saleRepository.findSalesById(id);}

    // 판매내역에 저장
    @Transactional
    public void addSale(int sellerId, Item item) {

        User seller = userPageService.findUser(sellerId);

        Sale sale = Sale.createSale(seller, item);

        saleRepository.save(sale);

    }
}
