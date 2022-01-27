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
        return orderItemRepository.findOrderItemsByUserId(userId);
    }

    // OrderItem 모두 찾기
    public List<OrderItem> findAllOrderItems() {return orderItemRepository.findAll();}


    // Order에 저장 - 장바구니 주문 용
    @Transactional
    public void addCartOrder(int userId, List<CartItem> cartItem) {
        System.out.println("*******userId = "+userId+"  cartItem= "+cartItem);

        //Order userOrder = orderRepository.findByUserId(userId); // 뒤에 .get() 붙이기?!!? -> get()은 값이 존재하지 않으면 오류 발생하도록 하는 것

        User user = userPageService.findUser(userId);


        List<OrderItem> orderItemList = new ArrayList<>();
        //cartItem은 해당 유저의 카트 안에 있는 상품들
        for (CartItem cartItem1 : cartItem) {
            //User seller = cartItem1.getItem().getSeller();
            Item item = itemRepository.findById(cartItem1.getItem().getId());
            OrderItem orderItem = OrderItem.createOrderItem(user, item, cartItem1.getCount());
            orderItemList.add(orderItem);
            orderItemRepository.save(orderItem);
        }


        Order userOrder = Order.createOrder(user, orderItemList);
        System.out.println("*********************userId = "+userId + " userOrder = " + userOrder.getId());

        orderRepository.save(userOrder);

    }

    // 단일 상품 주문
    @Transactional
    public void addOneItemOrder(int userId, Item item, int count) {

        User user = userPageService.findUser(userId);

        Order userOrder = Order.createOrder(user);
        OrderItem orderItem = OrderItem.createOrderItem(user, userOrder, item, count);

        orderItemRepository.save(orderItem);
        orderRepository.save(userOrder);

    }




}
