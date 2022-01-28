package com.example.weblogin.service;

import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.cartitem.CartItem;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.order.Order;
import com.example.weblogin.domain.order.OrderRepository;
import com.example.weblogin.domain.orderitem.OrderItem;
import com.example.weblogin.domain.orderitem.OrderItemRepository;
import com.example.weblogin.domain.saleitem.SaleItemRepository;
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
    private final SaleItemRepository saleItemRepository;

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

    // OrderItem 하나 찾기
    public OrderItem findOrderitem(int orderItemId) {return orderItemRepository.findOrderItemById(orderItemId);}


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

    // 주문 취소 기능
    @Transactional
    public void orderCancel(User user, OrderItem cancelItem) {

        // 판매자의 판매내역 totalCount 감소
        cancelItem.getSaleItem().getSale().setTotalCount(cancelItem.getSaleItem().getSale().getTotalCount()-cancelItem.getOrderCount());

        // 해당 item 재고 다시 증가
        cancelItem.getItem().setStock(cancelItem.getItem().getStock()+ cancelItem.getOrderCount());

        // 판매자 돈 감소
        cancelItem.getSaleItem().getSeller().setCoin(cancelItem.getSaleItem().getSeller().getCoin()- cancelItem.getOrderPrice());

        // 구매자 돈 증가
        cancelItem.getUser().setCoin(cancelItem.getUser().getCoin()+ cancelItem.getOrderPrice());

        // 해당 orderItem의 주문 상태 1로 변경 -> 주문 취소를 의미
        cancelItem.setIsCancel(cancelItem.getIsCancel()+1);

        // 해당 orderItem.getsaleItemId 로 saleItem 찾아서 판매 상태 1로 변경 -> 판매 취소를 의미
        cancelItem.getSaleItem().setIsCancel(cancelItem.getSaleItem().getIsCancel()+1);

        orderItemRepository.save(cancelItem);
        saleItemRepository.save(cancelItem.getSaleItem());


    }


}
