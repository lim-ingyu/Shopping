package com.example.weblogin.domain.cart;

import com.example.weblogin.domain.cartitem.CartItem;
import com.example.weblogin.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    User user;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cart_items = new ArrayList<>();
}
