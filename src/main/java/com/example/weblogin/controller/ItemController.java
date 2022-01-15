package com.example.weblogin.controller;

import com.example.weblogin.domain.Item;
import com.example.weblogin.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @PostMapping("/admin/item/save")
    public String itemSave(Item item, Model model) {
        try {
            itemService.saveItem(item);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "itemForm";
        }
        return "redirect:/";
    }

    // 상품 수정 페이지
    @GetMapping("/admin/item/modify/{id}")
    public String itemModifyForm(@PathVariable("id") Integer id, Item item, Model model) {
        model.addAttribute("item", itemService.itemView(id));
        return "itemModify";
    }

    // 상품 수정 (POST)
    @PostMapping("/admin/item/update/{id}")
    public String itemModify(@PathVariable("id") Integer id, Item item) {

        Item update = itemService.itemView(id);

        update.setItemName(item.getItemName());
        update.setItemPrice(item.getItemPrice());
        update.setItemText(item.getItemText());
        update.setSellCount(item.getSellCount());
        update.setStock(item.getStock());
        update.setItemImage(item.getItemImage());

        itemService.saveItem(update);
        return "redirect:/";
    }
}
