package com.example.weblogin.controller;

import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.service.ItemService;
import com.example.weblogin.service.UserPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class SellerPageController {

    private final UserPageService userPageService;
    private final ItemService itemService;

    // 판매자 페이지 접속
    @GetMapping("/seller/{id}")
    public String sellerPage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // 로그인이 되어있는 판매자의 id와 판매자 페이지에 접속하는 id가 같아야 한다.
        if (principalDetails.getUser().getId() == id) {

            model.addAttribute("user", userPageService.findUser(id));

            return "/seller/sellerPage";
        } else {

            return "redirect:/main";
        }

    }

    // 판매 관리 페이지 -> 판매자/관리자만 가능
    @GetMapping("/seller/manage")
    public String itemManage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // SELLER 와 ADMIN 만 접속 가능
        if(principalDetails.getUser().getRole().equals("ROLE_SELLER") || principalDetails.getUser().getRole().equals("ROLE_ADMIN")) {

            List<Item> allItem = itemService.allItemView();
            List<Item> userItem = new ArrayList<>();


            for(Item item : allItem ) {
                if(item.getUser().getId() == id) {
                    userItem.add(item);
                }
            }

            model.addAttribute("seller", userPageService.findUser(id));
            model.addAttribute("userItem", userItem);
            return "seller/itemManage";

        } else {

            // 일반 유저가 판매관리 페이지로 올 경우 main으로 리턴
            return "redirect:/main";

        }
    }
}
