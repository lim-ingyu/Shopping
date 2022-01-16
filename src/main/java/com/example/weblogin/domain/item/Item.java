package com.example.weblogin.domain.item;

import com.example.weblogin.domain.cartitem.CartItem;
import com.example.weblogin.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String text; // 물건에 대한 상세설명

    private int price;

    private int count; // 판매 개수

    private int stock; // 재고

    private boolean isSoldout; // 상품 상태 (판매중 / 품절)

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; // 판매자 아이디

    @OneToMany(mappedBy = "item")
    private List<CartItem> cart_items = new ArrayList<>();

    private String photo; // 상품 사진


}
