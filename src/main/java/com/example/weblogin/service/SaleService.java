package com.example.weblogin.service;

import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.cartitem.CartItem;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.order.Order;
import com.example.weblogin.domain.orderitem.OrderItem;
import com.example.weblogin.domain.sale.Sale;
import com.example.weblogin.domain.sale.SaleRepository;
import com.example.weblogin.domain.saleitem.SaleItem;
import com.example.weblogin.domain.saleitem.SaleItemRepository;
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
    private final SaleItemRepository saleItemRepository;
    private final ItemRepository itemRepository;

    // 회원가입 하면 판매자 당 판매내역 하나 생성
    public void createSale(User user){
        Sale sale = Sale.createSale(user);
        saleRepository.save(sale);
    }

    // id에 해당하는 판매내역 찾기
    public List<Sale> findsellerSales(int sellerId) {
        return saleRepository.findSalesById(sellerId);
    }

    // id에 해당하는 판매아이템 찾기
    public List<SaleItem> findSellerSaleItems(int sellerId) {
        return saleItemRepository.findSaleItemsBySellerId(sellerId);
    }


    // 판매된 상품 모두 찾기
    public List<Sale> findAllSaleItems() {return saleRepository.findAll();}

    // 해당 판매자 id에서 판매된 상품 찾기
    public List<Sale> findSaleItemsById(int id) {return saleRepository.findSalesById(id);}

    public Sale findSaleById(int sellerId) { return saleRepository.findBySellerId(sellerId); }

    // 판매내역에 저장
    @Transactional
    public SaleItem addSale(int sellerId, Item sale_item, int count) {

        User seller = userPageService.findUser(sellerId);
        Sale sale = saleRepository.findBySellerId(sellerId);
        sale.setTotalCount(sale.getTotalCount()+count);
        saleRepository.save(sale);

        Item item = itemRepository.findItemById(sale_item.getId());
        SaleItem saleItem = SaleItem.createSaleItem(sale, seller, item, count);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&7sale = "+sale.getId()+" seller = "+seller.getId()+" item = "+item.getId()+" count = "+count);
        saleItemRepository.save(saleItem);

        return saleItem;
    }

    public List<SaleItem> findSellerSaleItems(Sale sellerSale) {
        int sellerSaleId = sellerSale.getId(); // 판매 고유 번호
        List<SaleItem> allSellerSaleItems = new ArrayList<>(); // 판매한 상품들

        List<SaleItem> allSaleItems = saleItemRepository.findAll(); // 판매자 상관없이 전체 판매 상품들

        for(SaleItem saleItem : allSaleItems) {
            if(saleItem.getSale().getId() == sellerSaleId) {
                allSellerSaleItems.add(saleItem);
            }
        }

        return allSellerSaleItems;
    }
}
