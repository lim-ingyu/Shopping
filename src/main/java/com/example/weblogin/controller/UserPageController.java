package com.example.weblogin.controller;

import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.cartitem.CartItem;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.order.Order;
import com.example.weblogin.domain.orderitem.OrderItem;
import com.example.weblogin.domain.user.User;
import com.example.weblogin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

// 유저(회원)에 해당하는 페이지 관리
// 마이페이지, 장바구니, 구매내역

@RequiredArgsConstructor
@Controller
public class UserPageController {

    private final UserPageService userPageService;
    private final CartService cartService;
    private final ItemService itemService;
    private final OrderService orderService;
    private final SaleService saleService;

    // 유저 페이지 접속
    @GetMapping("/user/{id}")
    public String userPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인이 되어있는 유저의 id와 유저 페이지에 접속하는 id가 같아야 한다.
        if (principalDetails.getUser().getId() == id) {

            model.addAttribute("user", userPageService.findUser(id));

            return "/user/userPage";
        } else {

            return "redirect:/main";
        }

    }

    // 회원(판매자) 정보 수정
    @GetMapping("/user/modify/{id}")
    public String userModify(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인이 되어있는 유저의 id와 수정페이지에 접속하는 id가 같아야 한다.
        if (principalDetails.getUser().getId() == id) {

            model.addAttribute("user", userPageService.findUser(id));

            return "/userModify";
        } else {

            return "redirect:/main";
        }

    }

    // 수정 실행
    @PostMapping("/user/update/{id}")
    public String userUpdate(@PathVariable("id") Integer id, User user) {
        userPageService.userModify(user);
        return "redirect:/user/{id}";
    }



    // 장바구니 페이지 접속
    @GetMapping("/user/cart/{id}")
    public String userCartPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인이 되어있는 유저의 id와 장바구니에 접속하는 id가 같아야 한다.
        if (principalDetails.getUser().getId() == id) {

            // 로그인 되어 있는 유저에 해당하는 장바구니 가져오기
            Cart userCart = principalDetails.getUser().getCart();

            // 장바구니에 들어있는 아이템 모두 가져오기
            List<CartItem> cartItemList = cartService.allUserCartView(userCart);

            // 장바구니에 들어있는 상품들의 총 가격
            int totalPrice = 0;
            for (CartItem cartitem : cartItemList) {
                totalPrice += cartitem.getCount() * cartitem.getItem().getPrice();
            }
            // 총 개수 += 수량
            int totalCount = 0;
            for (CartItem cartitem : cartItemList) {
                totalCount += cartitem.getCount();
            }


            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("cartItems", cartItemList);
            model.addAttribute("user", userPageService.findUser(id));

            return "/user/userCart";
        }
        // 로그인 id와 장바구니 접속 id가 같지 않는 경우
        else {
            return "redirect:/main";
        }
    }

    // 장바구니에 물건 넣기
    @PostMapping("/user/cart/{id}/{itemId}")
    public String addCartItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId, int amount) {

        User user = userPageService.findUser(id);
        Item item = itemService.itemView(itemId);

        cartService.addCart(user, item, amount);

        return "redirect:/item/view/{itemId}";
    }

    // 장바구니에서 물건 삭제
    // 삭제하고 남은 총액 다시 계산해서 모델로 보내기 = 장바구니 등록 로직이랑 같음
    // 삭제하고 남은 상품의 총 개수
    @GetMapping("/user/cart/{id}/{cartItemId}/delete")
    public String deleteCartItem(@PathVariable("id") Integer id, @PathVariable("cartItemId") Integer itemId, Model model) {

        cartService.cartItemDelete(itemId);

        // 해당 유저의 카트 찾기
        Cart userCart = cartService.findUserCart(id);

        // 해당 유저의 장바구니 상품들
        List<CartItem> cartItemList = cartService.allUserCartView(userCart);

        // 총 가격 += 수량 * 가격
        int totalPrice = 0;
        for (CartItem cartitem : cartItemList) {
            totalPrice += cartitem.getCount() * cartitem.getItem().getPrice();
        }

        // 총 개수 += 수량
        int totalCount = 0;
        for (CartItem cartitem : cartItemList) {
            totalCount += cartitem.getCount();
        }


        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("cartItems", cartItemList);
        model.addAttribute("user", userPageService.findUser(id));

        return "/user/userCart";

    }

    // 주문 내역 조회 페이지
    @GetMapping("/user/orderHist/{id}")
    public String orderList(@PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        // 로그인이 되어있는 유저의 id와 구매내역에 접속하는 id가 같아야 한다.
        if (principalDetails.getUser().getId() == id) {

            // 로그인 되어 있는 유저에 해당하는 구매내역 가져오기
            //List<Order> orders = orderService.findUserOrders(id);
            List<OrderItem> orderItemList = orderService.findUserOrderItems(id);

            System.out.println("******************************userId = " + id+" orderItemList = " + orderItemList);


            // 총 주문 개수 += 수량
            int totalCount = 0;
            for (OrderItem orderItem : orderItemList) {
                totalCount += orderItem.getOrderCount();
            }

            //model.addAttribute("orders", orders);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("orderItems", orderItemList);
            model.addAttribute("user", userPageService.findUser(id));

            return "user/userOrderList";
        }
        // 로그인 id와 구매내역 접속 id가 같지 않는 경우
        else {
            return "redirect:/main";
        }
    }



    // 장바구니 상품 전체 주문
    @Transactional
    @PostMapping("/user/cart/checkout/{id}")
    public String cartCheckout(@PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        if(principalDetails.getUser().getId() == id) {
            User user = userPageService.findUser(id);

            Cart userCart = cartService.findUserCart(user.getId()); // 유저 카트 찾기
            List<CartItem> userCartItems = cartService.allUserCartView(userCart); // 유저 카트 안에 있는 상품들
            System.out.println("*********************userCart 아이디 = "+userCart.getId()+"  userCartItems= "+userCartItems);


            // 최종 결제 금액
            int totalPrice = 0;
            for (CartItem cartItem : userCartItems) {
                totalPrice += cartItem.getCount() * cartItem.getItem().getPrice();
            }

            // 이 아래로부터 데이터베이스 저장 안됨



            // 유저 돈에서 최종 결제금액 빼야함
            user.setCoin(user.getCoin()-totalPrice);

            // 판매자의 돈은 최종 결제금액만큼 늘어남
            // 해당 상품들의 재고는 각각 구매한 수량만큼 줄어듬
            // 상품들 개별 판매 개수 다 더해서 최종 판매 개수 구하기
            // 장바구니에서 상품 전체 삭제하기
            for (CartItem cartItem : userCartItems) {
                User seller = cartItem.getItem().getSeller(); // 각 상품에 대한 판매자

                // 판매자 수익 증가
                seller.setCoin(seller.getCoin()+ (cartItem.getCount()*cartItem.getItem().getPrice()));

                // 재고 감소
                cartItem.getItem().setStock(cartItem.getItem().getStock()-cartItem.getCount());

                // 상품 개별로 판매 개수 증가
                cartItem.getItem().setCount(cartItem.getItem().getCount()+cartItem.getCount());

                // sale에 담기
                saleService.addSale(seller.getId(), cartItem.getItem(), cartItem.getCount());
            }
            //List<CartItem> cartItems = userCart.getCartItems();
            //System.out.println("*****  위에 userCartItems이랑 같아야함 cartItems= "+cartItems); -> 같음


            // 여기서부터 오류!/ orderitem 부터 담기지 않음 장바구니 삭제도 안됨
            // order에 담기 @aa41fbb
            orderService.addCartOrder(id, userCartItems);

            // 장바구니 상품 모두 삭제
            cartService.allCartItemDelete(id);

            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("cartItems", userCartItems);
            model.addAttribute("user", userPageService.findUser(id));

            return "redirect:/user/cart/{id}";
        } else {
            return "redirect:/main";
        }
    }


    // 상품 개별 주문 -> 주문 할 때마다 Order객체 생성해야함
    @Transactional
    @PostMapping("/user/{id}/checkout/{itemId}")
    public String checkout(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model, int count) {
        if(principalDetails.getUser().getId() == id) {

            User user = userPageService.findUser(id);
            Item item = itemService.itemView(itemId);

            // 최종 결제 금액
            int totalPrice = item.getPrice()*count;

            // 유저 돈에서 최종 결제금액 빼야함
            user.setCoin(user.getCoin()-totalPrice);

            // 판매자의 돈은 최종 결제금액만큼 늘어남
            item.getSeller().setCoin(item.getSeller().getCoin()+totalPrice);

            // 해당 상품들의 재고는 각각 구매한 수량만큼 줄어듬
            item.setStock(item.getStock()-count);

            orderService.addOneItemOrder(id, item, count);

            saleService.addSale(item.getId(), item, count);

            return "redirect:/user/orderHist/{id}";
        } else {
            return "redirect:/main";
        }
    }


}
