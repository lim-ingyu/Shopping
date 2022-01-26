package com.example.weblogin.service;

import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.cartitem.CartItem;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.order.Order;
import com.example.weblogin.domain.order.OrderRepository;
import com.example.weblogin.domain.orderitem.OrderItem;
import com.example.weblogin.domain.orderitem.OrderItemRepository;
import com.example.weblogin.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final UserPageService userPageService;
    private final ItemRepository itemRepository;

    // 회원가입 하면 회원 당 주문내역 하나 생성 - 주문내역 없을 때 대비
    public void createOrder(User user){
        Order order = Order.createOrder(user);
        orderRepository.save(order);
    }

    // id에 해당하는 주문내역 찾기
    public List<Order> findUserOrders(int userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

    // id에 해당하는 주문아이템 찾기
    public List<OrderItem> findUserOrderItems(int userId) {
        return orderItemRepository.findOrderItemsById(userId);
    }

    // OrderItem 모두 찾기
    public List<OrderItem> findAllOrderItems() {return orderItemRepository.findAll();}


    // Order에 저장
    @Transactional
    public void addOrder(int userId, List<CartItem> cartItem) {

        Order userOrder = orderRepository.findByUserId(userId);

        User user = userPageService.findUser(userId);

        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartItem cartItem1 : cartItem) {
            User seller = cartItem1.getItem().getUser();
            Item item = itemRepository.findById(cartItem1.getItem().getId());
            OrderItem orderItem = OrderItem.createOrderItem(seller, item, cartItem1.getCount());
            orderItemList.add(orderItem);
            orderItemRepository.save(orderItem);
        }

        Order order = Order.createOrder(user, orderItemList);
        orderRepository.save(order);

    }




}
