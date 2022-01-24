package com.example.weblogin.controller;

import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.cartitem.CartItem;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.user.User;
import com.example.weblogin.service.CartService;
import com.example.weblogin.service.ItemService;
import com.example.weblogin.service.UserPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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

            model.addAttribute("totalPrice", totalPrice);
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

        return "redirect:/item/view/{id}";
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
            totalPrice += cartitem.getCount();
        }


        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("cartItems", cartItemList);
        model.addAttribute("user", userPageService.findUser(id));

        return "/user/userCart";

    }

    // 구매 내역 조회 페이지
    @GetMapping("/user/orderHist/{id}")
    public String orderHist(@PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        return "/user/userOrderHist";
    }

    // 상품 주문


}
