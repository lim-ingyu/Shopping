package com.example.weblogin.controller;

import com.example.weblogin.domain.Item;
import com.example.weblogin.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 등록 페이지
    @GetMapping("/admin/item/new")
    public String itemSaveForm() {
        return "itemForm";
    }

    // 상품 등록 (POST)
    @PostMapping("/admin/item/new")
    public String itemSave(Item item, Model model) {
        try {
            itemService.saveItem(item);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "itemForm";
        }
        return "redirect:/";
    }
}
