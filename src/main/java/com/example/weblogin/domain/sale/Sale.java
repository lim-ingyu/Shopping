package com.example.weblogin.domain.sale;

import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.cartitem.CartItem;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.user.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user; // 판매자

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="item_id")
    private Item item;

    private String itemName;
    private int itemPrice;
    private int itemCount; // 상품 각각 몇 개 구매?

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate; // 날짜

    @PrePersist
    public void createDate(){
        this.createDate = LocalDate.now();
    }

    public static Sale createSale(User user, Item item) {
        Sale sale = new Sale();
        sale.setUser(user);
        sale.setItem(item);
        sale.setItemName(item.getName());
        sale.setItemPrice(item.getPrice());
        sale.setItemCount(item.getCount());
        return sale;
    }
}
