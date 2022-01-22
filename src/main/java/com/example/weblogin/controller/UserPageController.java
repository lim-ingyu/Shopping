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
// 마이페이지, 장바구니

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

    // 장바구니 페이지
    @GetMapping("/user/cart/{id}")
    public String userCartPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인이 되어있는 유저의 id와 장바구니에 접속하는 id가 같아야 한다.
        if (principalDetails.getUser().getId() == id) {

            // 해당 유저의 카트가 있는지 없는지 찾기
            Cart userCart = cartService.findCart(id);

            if (userCart == null) {
                // 장바구니가 존재하지 않다면
                // 즉, 장바구니에 물건을 담은 적이 없다면
                return "redirect:/main";
            } else {
                // 장바구니가 존재한다면
                // 장바구니에 담은 상품 리스트 보여주기
                // cartItem 중 유저의 카트 id가 있는 상품들만 반환해야 함
                List<CartItem> cartItemList = cartService.findUserCartItems(userCart);

                // 총 가격 += 수량 * 가격
                int totalPrice = 0;
                for (CartItem item : cartItemList) {
                    totalPrice += item.getCount() * item.getItem().getPrice();
                }

                model.addAttribute("totalPrice", totalPrice);
                model.addAttribute("cartItems", cartItemList);
                model.addAttribute("user", userPageService.findUser(id));

                return "/user/userCart";
            }


            return "/user/userCart";
        }
        // 로그인 id와 장바구니 접속 id가 같지 않는 경우
        else {

            return "redirect:/main";
        }

    }

    // 장바구니에 물건 넣기
    @PostMapping("/user/cart/{id}/{itemId}")
    public String addCartItem(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {

        User loginUser = userPageService.findUser(id);
        Item item = itemService.itemView(itemId);

        cartService.addCart(loginUser, item);

        return "redirect:/item/{itemId}";
    }


    // 장바구니 삭제
    @GetMapping("/user/{id}/cart/{cart_itemId}/delete")
    public String deleteCartItem(@PathVariable("id") Integer id, @PathVariable("cart_itemId") Integer cart_itemId, Model model) {

        cartService.deleteCart_item(cart_itemId);

        Cart userCart = cartFinderService.findCart(id); // 유저의 카트
        // 카트가 있는 경우
        List<Cart_item> userCart_items = cartFinderService.findUserCart_items(userCart); // 유저의 카트ID가 들어간 모든 Cart_item 반환
        // 물품의 가격 총 합
        int totalPrice = 0;
        for (Cart_item item : userCart_items) {
            totalPrice += item.getCount() * item.getItem().getPrice();
        }
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItems", userCart_items);
        model.addAttribute("user", userPageService.findUser(id));

        return "/user/userCart";

    }
}
