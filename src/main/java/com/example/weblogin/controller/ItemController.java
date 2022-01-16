package com.example.weblogin.controller;

import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.user.User;
import com.example.weblogin.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 메인 페이지 (로그인 안 한 유저)
    @GetMapping("/")
    public String mainPageNoneLogin(Model model) {
        // 로그인을 안 한 경우
        List<Item> items = itemService.allItemView();
        model.addAttribute("items", items);
        return "/main";
    }

    // 메인 페이지 (로그인 유저)
    @GetMapping("/main")
    public String mainPage(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
            // 어드민, 판매자
            List<Item> items = itemService.allItemView();
            model.addAttribute("items", items);
            model.addAttribute("user", principalDetails.getUser());
            return "/seller/mainSeller";
        } else {
            // 일반 유저일 경우
            List<Item> items = itemService.allItemView();
            model.addAttribute("items", items);
            model.addAttribute("user", principalDetails.getUser());
            return "user/mainUser";
        }
    }

    // 상품 등록 페이지
    @GetMapping("/item/new")
    public String itemSaveForm(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
            // 어드민, 판매자
            return "/seller/itemForm";
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }
    }

    // 상품 등록 (POST)
    @PostMapping("/item/new/pro")
    public String itemSave(Item item, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
            // 어드민
            item.setUser(principalDetails.getUser());
            itemService.saveItem(item);
            return "redirect:/main";
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }
    }

    // 상품 수정 페이지
    @GetMapping("/item/modify/{id}")
    public String itemModifyForm(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || (principalDetails.getUser().getRole().equals("ROLE_SELLER"))) {
            // 어드민 혹은 판매자
            User user = itemService.itemView(id).getUser();
            if(user.getId() == principalDetails.getUser().getId()) {
                model.addAttribute("item", itemService.itemView(id));
                return "/seller/itemModify";
            } else {
                return "redirect:/main";
            }
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }
    }

    // 상품 수정 (POST)
    @PostMapping("/item/modify/pro/{id}")
    public String itemModify(Item item, @PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || (principalDetails.getUser().getRole().equals("ROLE_SELLER"))) {
            // 어드민, 판매자
            User user = itemService.itemView(id).getUser();
            if(user.getId() == principalDetails.getUser().getId()) {
                // 아이템 등록자와, 로그인 유저가 같으면 수정 진행
                itemService.itemModify(item, id);
                return "redirect:/main";
            } else {
                return "redirect:/main";
            }
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }
    }
}
