package com.example.weblogin.controller;

import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.user.User;
import com.example.weblogin.service.ItemService;
import com.example.weblogin.service.UserPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


// return 다시 설정



@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserPageService userPageService;

    // 메인 페이지 html 하나로 통일
    // 메인 페이지 (로그인 안 한 유저) /localhost:8080
    @GetMapping("/")
    public String mainPageNoneLogin(Model model) {
        // 로그인을 안 한 경우
        List<Item> items = itemService.allItemView();
        model.addAttribute("items", items);

        return "/main";
    }

    // 메인 페이지 (로그인 유저) - 어드민, 판매자, 구매자 로 로그인
    @GetMapping("/main")
    public String mainPage(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
            // 어드민, 판매자
            int sellerId = principalDetails.getUser().getId();
            List<Item> items = itemService.allItemView();
            model.addAttribute("items", items);
            model.addAttribute("user", userPageService.findUser(sellerId));

            return "/main";
        } else {
            // 구매자
            int userId = principalDetails.getUser().getId();
            List<Item> items = itemService.allItemView();
            model.addAttribute("items", items);
            model.addAttribute("user", userPageService.findUser(userId));

            return "/main";
        }
    }


    // 상품 등록 페이지 - 어드민/판매자만 가능
    @GetMapping("/item/new")
    public String itemSaveForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
            // 어드민, 판매자
            model.addAttribute("user", principalDetails.getUser());

            return "/seller/itemForm";
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }
    }

    // 상품 등록 (POST) - 어드민/판매자만 가능
    @PostMapping("/item/new/pro")
    public String itemSave(Item item, @AuthenticationPrincipal PrincipalDetails principalDetails, MultipartFile imgFile) throws Exception {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
            // 어드민, 판매자
            item.setSeller(principalDetails.getUser());
            itemService.saveItem(item, imgFile);

            return "redirect:/main";
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }
    }

    // 상품 수정 페이지 - 어드민/판매자만 가능
    @GetMapping("/item/modify/{id}")
    public String itemModifyForm(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || (principalDetails.getUser().getRole().equals("ROLE_SELLER"))) {
            // 어드민, 판매자
            User user = itemService.itemView(id).getSeller();
            // 상품을 올린 판매자 id와 현재 로그인 중인 판매자의 id가 같을 때
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

    // 상품 수정 (POST) - 어드민/판매자만 가능
    @PostMapping("/item/modify/pro/{id}")
    public String itemModify(Item item, @PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails, MultipartFile imgFile) throws Exception{

        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || (principalDetails.getUser().getRole().equals("ROLE_SELLER"))) {
            // 어드민, 판매자
            User user = itemService.itemView(id).getSeller();

            if(user.getId() == principalDetails.getUser().getId()) {
                // 아이템 등록자와, 로그인 유저가 같으면 수정 진행
                itemService.itemModify(item, id, imgFile);

                return "redirect:/main";
            } else {
                return "redirect:/main";
            }
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }
    }

    // 상품 상세 페이지 - 어드민, 판매자, 구매자 가능
    @GetMapping("/item/view/{id}")
    public String itemView(Model model, @PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || principalDetails.getUser().getRole().equals("ROLE_SELLER")) {
            // 어드민, 판매자
            model.addAttribute("item", itemService.itemView(id));
            model.addAttribute("user", principalDetails.getUser());

            return "itemView";
        } else if (principalDetails.getUser().getRole().equals("ROLE_USER")) {
            // 일반 회원
            model.addAttribute("item", itemService.itemView(id));
            model.addAttribute("user", principalDetails.getUser());

            return "itemView";
        } else {
            // 로그인 안 한 유저

            model.addAttribute("item", itemService.itemView(id));

            return "itemView";

        }
    }

    // 상품 삭제 - 어드민, 판매자만 가능
    @GetMapping("/item/delete/{id}")
    public String itemDelete(@PathVariable("id") Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") || (principalDetails.getUser().getRole().equals("ROLE_SELLER"))) {
            // 어드민, 판매자
            User user = itemService.itemView(id).getSeller();

            if(user.getId() == principalDetails.getUser().getId()) {

                itemService.itemDelete(id);

                return "redirect:/main";
            } else {
                return "redirect:/main";
            }
        } else {
            // 일반 회원이면 거절 당해서 main으로 되돌아감
            return "redirect:/main";
        }
    }

    // 상품 리스트 페이지
    @GetMapping("/item/list")
    public String itemList(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                           String searchKeyword) {

        Page<Item> items = null;

        if (searchKeyword == null) {  // 검색이 들어왔을 때
            items = itemService.allItemViewPage(pageable);
        }else {  // 검색이 들어오지 않았을 때
            items = itemService.itemSearchList(searchKeyword, pageable);
        }

        int nowPage = items.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, items.getTotalPages());

        model.addAttribute("items", items);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "itemList";
    }

}
