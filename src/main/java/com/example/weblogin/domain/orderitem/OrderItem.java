package com.example.weblogin.domain.orderitem;

import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.order.Order;
import com.example.weblogin.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private User seller; // 판매자

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="item_id")
    private Item item;

    private int orderPrice; //주문가격
    private int orderCount; //수량

    public static OrderItem createOrderItem(User seller, Item item, int count) {

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setSeller(seller);
        orderItem.setOrderCount(count);
        orderItem.setOrderPrice(item.getPrice());
        return orderItem;
    }


}
