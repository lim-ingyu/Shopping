package com.example.weblogin.domain.orderitem;

import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.order.Order;
import com.example.weblogin.domain.saleitem.SaleItem;
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
    @JoinColumn(name = "user_id")
    private User user; // 구매자

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="item_id")
    private Item item;

    private int orderPrice; //주문가격
    private int orderCount; //수량

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="saleItem_id")
    private SaleItem saleItem; // 주문상품에 매핑되는 판매상품

    private int isCancel; // 주문 취소 여부 (0:주문완료 / 1:주문취소)

    public static OrderItem createOrderItem(User user, Item item, int count, SaleItem saleItem) {

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setUser(user);
        orderItem.setOrderCount(count);
        orderItem.setOrderPrice(item.getPrice());
        orderItem.setSaleItem(saleItem);
        return orderItem;
    }

    public static OrderItem createOrderItem(User user, Order order, Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setUser(user);
        orderItem.setOrder(order);
        orderItem.setOrderCount(count);
        orderItem.setOrderPrice(item.getPrice());
        return orderItem;
    }


}
